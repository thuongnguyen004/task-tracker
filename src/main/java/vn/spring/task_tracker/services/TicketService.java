package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    Ticket createTicket(Ticket ticket);

    Ticket getActiveTicketById(UUID id);

    Ticket updateTicket(UUID ticketId, Ticket ticket);

    Ticket changeStatusTicket(UUID ticketId, short statusId);

    List<Ticket> getAllActiveTickets();

    List<Ticket> getAllArchiveTickets();

    Ticket getArchiveTicketById(UUID id);

    void archiveTicket(UUID ticketId);

    void restoreTicket(UUID ticketId);

    Ticket getTicketByCode(String code);
}
