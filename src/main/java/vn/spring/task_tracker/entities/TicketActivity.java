package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@Table(name = "ticket_activities")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TicketActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_ticket"))
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_activities"))
    private ActivityEventType eventType;

    @ManyToOne
    @JoinColumn(name = "performed_by_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_user"))
    private User performedBy;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;
}
