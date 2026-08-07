package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.TicketStatus;

import java.util.List;

public interface TicketStatusService {

    TicketStatus getTicketPriorityById(short id);
    TicketStatus getDefaultPriority();
    TicketStatus getOrDefaultStatus(TicketStatus status);
    List<TicketStatus> getTicketStatuses();
}
