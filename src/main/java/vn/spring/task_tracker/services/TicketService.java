package vn.spring.task_tracker.services;

import java.util.List;
import java.util.UUID;
import vn.spring.task_tracker.entities.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket ticket);

    Ticket getActiveTicketById(UUID id);

    Ticket updateTicket(UUID ticketId, Ticket ticket);

    void changeStatusTicket(UUID ticketId, short statusId);

    List<Ticket> getAllActiveTickets();

    List<Ticket> findWithFilters(
        String title,
        List<Short> priorityIds,
        UUID assigneeId,
        boolean unassigned
    );
}
