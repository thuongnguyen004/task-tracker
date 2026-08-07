package vn.spring.task_tracker.mappers.tickets;

import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;

public class TicketResponseMapper {
    public TicketResponse build(Ticket ticket) {
        String assignee = null;

        if (ticket.getAssignee() != null) {
            assignee = ticket.getAssignee().getFullName();
        }

        Short priorityId = ticket.getPriority() != null ? ticket.getPriority().getId() : null;
        String priorityName = ticket.getPriority() != null ? ticket.getPriority().getName() : null;

        Short statusId = ticket.getStatus() != null ? ticket.getStatus().getId() : null;
        String statusName = ticket.getStatus() != null ? ticket.getStatus().getName() : null;

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                priorityId,
                priorityName,
                statusId,
                statusName,
                assignee,
                ticket.getCreatedBy().getUsername(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
