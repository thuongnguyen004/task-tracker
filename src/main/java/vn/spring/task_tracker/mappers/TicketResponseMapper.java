package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.responses.TicketResponse;
import vn.spring.task_tracker.entities.Ticket;

import java.util.List;

public class TicketResponseMapper {
    public TicketResponse build(Ticket ticket){
        return new TicketResponse(
                ticket.getId(),
                ticket.getCode(),
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
                        ? ticket.getAssignee().getFullName()
                        : null,
                ticket.getCreatedBy().getFullName(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.isArchived()
        );
    }

    public List<TicketResponse> buildList(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::build)
                .toList();
    }
}
