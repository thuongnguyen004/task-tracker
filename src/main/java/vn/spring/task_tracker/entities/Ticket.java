package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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

    @Column(length = 150, nullable = false)
    private String title;

    @Column()
    private String description;

    @Column( nullable = false, columnDefinition = "boolean default false")
    private boolean archived;

    @Column( nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;


    @OneToMany(mappedBy = "ticket")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket")
    private List<TicketActivity> ticketActivities;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false)
    private TicketPriority priority;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = true)
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

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

    public Ticket(String title, String description, TicketPriority priority, TicketStatus status, User assignee) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.assignee = assignee;
    }

}
