package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(String code, Ticket ticket);

    Ticket changeStatusTicket(UUID ticketId, short statusId);

    List<Ticket> getAllActiveTickets();

    Ticket getTicketByCode(String code);

    List<Ticket> findWithFilters(
        String title,
        List<Short> priorityIds,
        UUID assigneeId,
        boolean unassigned
    );

    List<Ticket> getAllArchiveTickets();

    void archiveTicket(UUID ticketId);

    void restoreTicket(UUID ticketId);

}
