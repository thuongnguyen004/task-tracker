package vn.spring.task_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.List;

@Entity
@Data
@Table(
        name = "activity_event_types",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_activity_event_types_name", columnNames = "name")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private ActivityEventCode code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    @OneToMany(mappedBy = "eventType", fetch = FetchType.LAZY)
    private List<TicketActivity> ticketActivities;

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