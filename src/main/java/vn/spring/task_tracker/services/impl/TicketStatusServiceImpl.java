package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.services.TicketStatusService;

@Service
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus getTicketPriorityById(short id){

        return this.ticketStatusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket status not found"));
    }
}
