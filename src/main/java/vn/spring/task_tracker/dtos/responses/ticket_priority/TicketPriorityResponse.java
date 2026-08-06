package vn.spring.task_tracker.dtos.responses.ticket_priority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketPriorityResponse {
    private short id;
    private String name;
}
