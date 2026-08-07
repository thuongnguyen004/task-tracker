package vn.spring.task_tracker.mappers.tickets;

import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;

public class TicketResponseMapper {
    public TicketResponse build(Ticket ticket){
        String assignee = null;

        if (ticket.getAssignee() != null) {
            assignee = ticket.getAssignee().getUsername();
        }

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().getName(),
                ticket.getStatus().getName(),
                assignee,
                ticket.getCreatedBy().getUsername(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
