package vn.spring.task_tracker.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.entities.RefreshToken;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.AppException;
import vn.spring.task_tracker.repositories.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = generateSecureToken();
        String tokenHash = hashToken(rawToken);
        long expiresAt = System.currentTimeMillis() + refreshTokenExpirationMs;

        long now = System.currentTimeMillis();
        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .token(rawToken)
            .tokenHash(tokenHash)
            .expiresAt(expiresAt)
            .createdAt(now)
            .updatedAt(now)
            .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    @Transactional
    public RotationResult rotate(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new AppException(
                HttpStatus.UNAUTHORIZED,
                "Refresh token is required"
            );
        }

        String tokenHash = hashToken(rawToken);
        RefreshToken stored = refreshTokenRepository
            .findByTokenHash(tokenHash)
            .orElseThrow(() ->
                new AppException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid refresh token"
                )
            );

        if (stored.isRevoked()) {
            log.warn(
                "Refresh token reuse detected for user {}. Revoking all sessions.",
                stored.getUser().getId()
            );
            refreshTokenRepository.revokeAllActiveByUserId(
                stored.getUser().getId(),
                System.currentTimeMillis()
            );
            throw new AppException(
                HttpStatus.UNAUTHORIZED,
                "Refresh token reuse detected. All sessions have been revoked."
            );
        }

        if (stored.isExpired()) {
            throw new AppException(
                HttpStatus.UNAUTHORIZED,
                "Refresh token expired. Please log in again."
            );
        }

        User user = stored.getUser();
        String newRawRefresh = generateSecureToken();
        String newHash = hashToken(newRawRefresh);
        long now = System.currentTimeMillis();
        long expiresAt = now + refreshTokenExpirationMs;

        RefreshToken newToken = RefreshToken.builder()
            .user(user)
            .token(newRawRefresh)
            .tokenHash(newHash)
            .expiresAt(expiresAt)
            .createdAt(now)
            .updatedAt(now)
            .build();
        newToken = refreshTokenRepository.save(newToken);

        long revokeTime = System.currentTimeMillis();
        stored.setRevokedAt(revokeTime);
        stored.setUpdatedAt(revokeTime);
        stored.setReplacedByTokenId(newToken.getId());
        refreshTokenRepository.save(stored);

        String accessToken = jwtService.generateToken(
            user.getEmail(),
            Map.of()
        );

        return new RotationResult(accessToken, newRawRefresh, user);
    }

    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        String tokenHash = hashToken(rawToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(rt -> {
            if (!rt.isRevoked()) {
                long now = System.currentTimeMillis();
                rt.setRevokedAt(now);
                rt.setUpdatedAt(now);
                refreshTokenRepository.save(rt);
            }
        });
    }

    @Transactional
    public void revokeAllForUser(java.util.UUID userId) {
        refreshTokenRepository.revokeAllActiveByUserId(
            userId,
            System.currentTimeMillis()
        );
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                rawToken.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public record RotationResult(
        String accessToken,
        String refreshToken,
        User user
    ) {}
}
