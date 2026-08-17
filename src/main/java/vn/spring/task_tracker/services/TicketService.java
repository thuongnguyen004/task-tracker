package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(UUID ticketId, Ticket ticket);

    Ticket changeStatusTicket(UUID ticketId, short statusId);

    Ticket getTicketById(UUID id);

    List<Ticket> getAllActiveTickets();

    Ticket getTicketByCode(String code);

    List<Ticket> getAllArchiveTickets();

    void archiveTicket(UUID ticketId);

    void restoreTicket(UUID ticketId);

}
