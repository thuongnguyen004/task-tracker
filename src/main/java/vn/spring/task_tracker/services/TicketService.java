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

    Ticket getTicketByCode(String code);
}
