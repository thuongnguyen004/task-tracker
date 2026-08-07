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

    public TicketPriority getTicketPriorityById(short id){

        return this.ticketPriorityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket priority not found"));
    }

    public TicketPriority getOrDefaultPriority(TicketPriority priority) {

        if (priority == null) {
            return ticketPriorityRepository.findByName("Medium")
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Default priority not found"));
        }

        return getTicketPriorityById(priority.getId());
    }

    public List<TicketPriority> getTicketPriorities() {
        return ticketPriorityRepository.findAll();
    }
}
