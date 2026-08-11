package vn.spring.task_tracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.configs.JwtProperties;
import vn.spring.task_tracker.entities.RefreshToken;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.InvalidRefreshTokenException;
import vn.spring.task_tracker.repositories.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public void save(String token, User user) {
        revokeAllUserTokens(user);

        long now = System.currentTimeMillis();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(hash(token));
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiredAt(now + jwtProperties.getRefreshTokenExpiration().toMillis());
        refreshToken.setCreatedAt(now);
        refreshToken.setUpdatedAt(now);

        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getValidToken(String token) {
        RefreshToken refreshToken = findRefreshToken(hash(token));

        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            revokeAllUserTokens(refreshToken.getUser());
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        if (refreshToken.getExpiredAt() < System.currentTimeMillis()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeIfExists(String token) {
        refreshTokenRepository.findByToken(hash(token))
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(user.getId());
        tokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

    private RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash refresh token", e);
        }
    }
}
