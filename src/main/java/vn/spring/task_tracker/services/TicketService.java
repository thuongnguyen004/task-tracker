package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;

import java.util.UUID;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    Ticket getActiveTicketById(UUID id);
}
