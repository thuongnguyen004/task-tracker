package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
}
