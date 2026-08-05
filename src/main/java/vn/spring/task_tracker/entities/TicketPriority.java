package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(
        name = "ticket_priorities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_ticket_priorities_name", columnNames = "name")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    @OneToMany(mappedBy = "priority", fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
