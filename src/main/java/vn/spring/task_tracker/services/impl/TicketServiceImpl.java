package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.repositories.*;
import vn.spring.task_tracker.services.TicketPriorityService;
import vn.spring.task_tracker.services.TicketService;
import vn.spring.task_tracker.services.TicketStatusService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SecurityHelper securityHelper;
    private final TicketPriorityService ticketPriorityService;
    private final TicketStatusService ticketStatusService;

    public Ticket createTicket(Ticket ticket){

        User currentUser = securityHelper.getCurrentUser();

        TicketPriority ticketPriority = ticketPriorityService.getOrDefaultPriority(ticket.getPriority());

        TicketStatus ticketStatus = ticketStatusService.getDefaultPriority();

        User assignee = null;

        if (ticket.getAssignee() != null) {
            assignee = userRepository.findById(ticket.getAssignee().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Assignee not found"));
        }

        ticket.setAssignee(assignee);

        ticket.setPriority(ticketPriority);
        ticket.setStatus(ticketStatus);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(currentUser);

        return this.ticketRepository.save(ticket);
    }

    public Ticket getActiveTicketById(UUID id){
        return  this.ticketRepository.findByIdAndArchivedFalse(id).orElseThrow(() ->
                new ResourceNotFoundException("Ticket not found"));
    }

}
