package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus getTicketStatusById(short id){

        return ticketStatusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket status not found"));
    }

    public List<TicketStatus> getAllTicketStatuses() {
        return ticketStatusRepository.findAll();
    }
}
