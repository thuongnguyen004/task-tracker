package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.constants.TicketStatusMessage;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatus getTicketStatusById(short id) {

        return this.ticketStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND));
    }

    public TicketStatus getDefaultStatus() {
        return ticketStatusRepository.findByName("To Do").orElseThrow(() ->
                new ResourceNotFoundException(TicketStatusMessage.DEFAULT_NOT_FOUND));
    }

    public List<TicketStatus> getAllTicketStatuses() {
        return ticketStatusRepository.findAll();
    }
}
