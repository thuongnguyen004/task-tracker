package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(
    name = "refresh_tokens",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_refresh_tokens_token_hash",
            columnNames = "token_hash"
        ),
    }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "token_hash", length = 88, nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private Long expiresAt;

    @Column(nullable = true)
    private Long revokedAt;

    @Column(nullable = true)
    private UUID replacedByTokenId;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }
}
