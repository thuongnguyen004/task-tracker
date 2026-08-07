package vn.spring.task_tracker.dtos.responses.ticket_status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusResponse {
    private short id;
    private String name;
}
