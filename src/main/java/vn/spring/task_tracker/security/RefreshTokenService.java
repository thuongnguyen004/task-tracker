package vn.spring.task_tracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.configs.JwtProperties;
import vn.spring.task_tracker.entities.RefreshToken;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.AppException;
import vn.spring.task_tracker.repositories.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public void save(String token, User user) {
        long now = System.currentTimeMillis();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .expiredAt(now + jwtProperties.getRefreshTokenExpiration().toMillis())
                .createdAt(now)
                .updatedAt(now)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getValidToken(String token) {
        RefreshToken refreshToken = findRefreshToken(token);

        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        if (refreshToken.getExpiredAt() < System.currentTimeMillis()) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeIfExists(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    private RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
    }
}
