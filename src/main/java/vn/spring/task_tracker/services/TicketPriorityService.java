package vn.spring.task_tracker.services;

import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketPriority;

public interface TicketPriorityService {
    TicketPriority getTicketPriorityById(short id);
}
