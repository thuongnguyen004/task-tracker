package vn.spring.task_tracker.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.configs.JwtProperties;
import vn.spring.task_tracker.entities.User;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final JWSAlgorithm ALGORITHM = JWSAlgorithm.HS512;

    private final JwtProperties jwtProperties;

    private final byte[] signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = jwtProperties.getSecretKeyBytes();
    }

    public String generateAccessToken(User user) {
        return generateToken(
                user,
                jwtProperties.getAccessTokenExpiration().toMillis(),
                TokenType.ACCESS
        );
    }

    public String generateRefreshToken(User user) {
        return generateToken(
                user,
                jwtProperties.getRefreshTokenExpiration().toMillis(),
                TokenType.REFRESH
        );
    }

    public boolean verifyRefreshToken(String token) {
        try {
            parseAndValidateToken(token);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public String extractUserIdFromRefreshToken(String token) {
        try {
            JWTClaimsSet claimsSet = parseAndValidateToken(token);
            return claimsSet.getStringClaim("userId");
        } catch (ParseException exception) {
            throw new IllegalArgumentException("Invalid JWT userId", exception);
        }
    }

    private String generateToken(
            User user,
            long expirationDurationMs,
            TokenType tokenType
    ) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        if (user.getId() == null) {
            throw new IllegalArgumentException("Cannot generate token for user without ID");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Cannot generate token for user without email");
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationDurationMs);

        JWSHeader header = new JWSHeader.Builder(ALGORITHM)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer(jwtProperties.getIssuer())
                .issueTime(issuedAt)
                .expirationTime(expiresAt)
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId().toString())
                .claim("tokenType", tokenType.name())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(new MACSigner(signingKey));
            return signedJWT.serialize();
        } catch (JOSEException exception) {
            throw new IllegalStateException("Cannot generate JWT token", exception);
        }
    }

    private JWTClaimsSet parseAndValidateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!ALGORITHM.equals(signedJWT.getHeader().getAlgorithm())) {
                throw new IllegalArgumentException("Invalid JWT algorithm");
            }

            JWSVerifier verifier = new MACVerifier(signingKey);

            if (!signedJWT.verify(verifier)) {
                throw new IllegalArgumentException("Invalid JWT signature");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            Date expirationTime = claimsSet.getExpirationTime();

            if (expirationTime == null || !expirationTime.after(new Date())) {
                throw new IllegalArgumentException("JWT token has expired");
            }

            if (!jwtProperties.getIssuer().equals(claimsSet.getIssuer())) {
                throw new IllegalArgumentException("Invalid JWT issuer");
            }

            String subject = claimsSet.getSubject();

            if (subject == null || subject.isBlank()) {
                throw new IllegalArgumentException("JWT does not contain subject");
            }

            String tokenType = claimsSet.getStringClaim("tokenType");

            if (!TokenType.REFRESH.name().equals(tokenType)) {
                throw new IllegalArgumentException("Invalid JWT token type");
            }

            return claimsSet;
        } catch (ParseException | JOSEException exception) {
            throw new IllegalArgumentException("Invalid JWT token", exception);
        }
    }

}
