package vn.spring.task_tracker.mappers.tickets;

import vn.spring.task_tracker.dtos.responses.tickets.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;

import java.util.List;

public class TicketResponseMapper {
    public TicketResponse build(Ticket ticket){
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().getId(),
                ticket.getPriority().getName(),
                ticket.getStatus().getId(),
                ticket.getStatus().getName(),
                ticket.getAssignee() != null
                        ? ticket.getAssignee().getId()
                        : null,
                ticket.getAssignee() != null
                        ? ticket.getAssignee().getUsername()
                        : null,
                ticket.getCreatedBy().getUsername(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    public List<TicketResponse> buildList(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::build)
                .toList();
    }
}
