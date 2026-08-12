package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.TicketPriority;

import java.util.List;

public interface TicketPriorityService {
    List<TicketPriority> getAllTicketPriorities();
}
