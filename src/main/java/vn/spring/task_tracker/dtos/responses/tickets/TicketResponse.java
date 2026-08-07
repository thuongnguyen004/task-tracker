package vn.spring.task_tracker.dtos.responses.tickets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private UUID id;

    private String title;

    private String description;

    private Short priorityId;

    private String priority;

    private Short statusId;

    private String status;

    private String assignee;

    private String createdBy;

    private Long createdAt;

    private Long updatedAt;
}
