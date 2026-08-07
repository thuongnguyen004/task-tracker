package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.TicketStatus;

import java.util.List;

public interface TicketStatusService {

    TicketStatus getTicketStatusById(short id);
    List<TicketStatus> getAllTicketStatuses();
}
