package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.List;
import java.util.UUID;

public interface TicketActivityService {
    List<TicketActivity> getTicketActivityByIdTicket(UUID ticketId);
    void createTicketActivity(Ticket oldTicket, Ticket newTicket, User performedBy);
}
