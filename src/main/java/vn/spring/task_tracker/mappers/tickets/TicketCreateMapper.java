package vn.spring.task_tracker.mappers.tickets;

import vn.spring.task_tracker.dtos.requests.tickets.TicketCreateRequest;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;

public class TicketCreateMapper {
    public Ticket build(
            TicketCreateRequest ticketCreateRequest,
            TicketPriority ticketPriority,
            TicketStatus ticketStatus,
            User user){
        return new Ticket(
                ticketCreateRequest.getTitle(),
                ticketCreateRequest.getDescription(),
                ticketPriority,
                ticketStatus,
                user
         );
    }
}
