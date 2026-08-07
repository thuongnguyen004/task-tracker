package vn.spring.task_tracker.mappers.ticket_priority;

import vn.spring.task_tracker.dtos.responses.ticket_priority.TicketPriorityResponse;
import vn.spring.task_tracker.entities.TicketPriority;

import java.util.List;

public class TicketPriorityResponseMapper {
    public List<TicketPriorityResponse> build(List<TicketPriority> priorities) {
        return priorities.stream()
                .map(priority -> new TicketPriorityResponse(
                        priority.getId(),
                        priority.getName()
                ))
                .toList();
    }
}
