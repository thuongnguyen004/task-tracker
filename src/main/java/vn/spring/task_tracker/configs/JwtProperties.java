package vn.spring.task_tracker.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private static final int HS512_MIN_SECRET_BYTES = 64;

    private String secretKey;

    private String issuer = "task-tracker-api";

    private Duration accessTokenExpiration = Duration.ofMinutes(15);

    private Duration refreshTokenExpiration = Duration.ofDays(14);

    public byte[] getSecretKeyBytes() {
        byte[] signingKey = secretKey.getBytes(StandardCharsets.UTF_8);

        if (signingKey.length < HS512_MIN_SECRET_BYTES) {
            throw new IllegalArgumentException("JWT secret key must contain at least 64 bytes for HS512");
        }

        return signingKey;
    }
}
