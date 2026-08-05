package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token", length = 300, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "revoked", nullable = false, columnDefinition = "boolean default false")
    private boolean revoked;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
