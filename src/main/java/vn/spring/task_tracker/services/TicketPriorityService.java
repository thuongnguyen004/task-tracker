package vn.spring.task_tracker.services;

import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketPriority;

import java.util.List;

public interface TicketPriorityService {
    List<TicketPriority> getAllTicketPriorities();
}
