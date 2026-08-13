package vn.spring.task_tracker.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.List;
import java.util.UUID;

public interface TicketActivityService {
    Page<TicketActivity> getTicketActivityByIdTicket(UUID ticketId, int page, int size);
    Page<TicketActivity> getTicketActivity(int page, int size);
    void createTicketActivity(Ticket ticket, ActivityEventCode eventCode, User performedBy, String oldValue, String newValue);
    void createTicketActivity(Ticket oldTicket, Ticket newTicket, User performedBy);
}
