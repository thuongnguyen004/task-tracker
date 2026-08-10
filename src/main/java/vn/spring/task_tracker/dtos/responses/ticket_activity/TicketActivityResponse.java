package vn.spring.task_tracker.dtos.responses.ticket_activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketActivityResponse {
    private UUID id;

    private String oldValue;

    private String newValue;

    private Long createdAt;

    private Long updatedAt;

    private ActivityEventCode eventCode;

    private String performedByName;
}
