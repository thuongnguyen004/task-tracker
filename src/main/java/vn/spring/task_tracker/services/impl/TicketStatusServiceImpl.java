package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus getTicketPriorityById(short id) {
        return this.ticketStatusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket status not found"));
    }

    public TicketStatus getDefaultPriority() {
        return ticketStatusRepository.findByName("To Do").orElseThrow(() ->
                new ResourceNotFoundException("Default status not found"));
    }

    public TicketStatus getOrDefaultStatus(TicketStatus status) {
        if (status == null || status.getId() == 0) {
            return getDefaultPriority();
        }
        return getTicketPriorityById(status.getId());
    }

    public List<TicketStatus> getTicketStatuses() {
        return ticketStatusRepository.findAll();
    }
}
