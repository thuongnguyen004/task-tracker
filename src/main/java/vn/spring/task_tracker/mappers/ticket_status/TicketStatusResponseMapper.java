package vn.spring.task_tracker.mappers.ticket_status;

import vn.spring.task_tracker.dtos.responses.ticket_status.TicketStatusResponse;
import vn.spring.task_tracker.entities.TicketStatus;

import java.util.List;

public class TicketStatusResponseMapper {
    public List<TicketStatusResponse> build(List<TicketStatus> statuses) {
        return statuses.stream()
                .map(status -> new TicketStatusResponse(
                        status.getId(),
                        status.getName()
                ))
                .toList();
    }
}
