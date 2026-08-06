package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.TicketStatus;

public interface TicketStatusService {

    TicketStatus getTicketPriorityById(short id);
}
