package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@Table(
        name = "ticket_statuses",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_ticket_statuses_name", columnNames = "name")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column( nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @PrePersist
    public void prePersist() {
        long now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = System.currentTimeMillis();
    }
}
