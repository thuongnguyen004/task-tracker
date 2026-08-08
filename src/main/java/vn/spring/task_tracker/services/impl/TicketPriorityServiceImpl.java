package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.services.TicketPriorityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketPriorityServiceImpl implements TicketPriorityService {

    private final TicketPriorityRepository ticketPriorityRepository;

    public List<TicketPriority> getAllTicketPriorities() {
        return ticketPriorityRepository.findAll();
    }
}
