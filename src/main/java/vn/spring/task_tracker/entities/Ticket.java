package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Table(name = "tickets")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "archived", nullable = false, columnDefinition = "boolean default false")
    private boolean archived;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false)
    private TicketPriority priority;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;
}
