package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.tickets.TicketUpdateMapper;
import vn.spring.task_tracker.repositories.*;
import vn.spring.task_tracker.services.TicketService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketStatusRepository ticketStatusRepository;
    private final TicketPriorityRepository ticketPriorityRepository;
    private final UserRepository userRepository;
    private final SecurityHelper securityHelper;

    public Ticket createTicket(Ticket ticket){

        User currentUser = this.securityHelper.getCurrentUser();

        TicketPriority priority;

        if (ticket.getPriority() != null) {

            priority = this.ticketPriorityRepository.findById(ticket.getPriority().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Priority not found"));

        } else {

            priority = this.ticketPriorityRepository.findByName("Medium")
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Default priority not found"));

        }

        TicketStatus status = this.ticketStatusRepository.findByName("TO DO")
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default status not found"));

        User assignee = null;

        if (ticket.getAssignee().getId() != null) {

            assignee = userRepository.findById(ticket.getAssignee().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));

        }

        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(currentUser);

        return this.ticketRepository.save(ticket);
    }

    public Ticket updateTicket (UUID ticketId, Ticket ticket) {
        Ticket oldValue = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));

        ticketPriorityRepository.findById(ticket.getPriority().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket priority not found."));

        ticketStatusRepository.findById(ticket.getStatus().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket status not found."));

        if (ticket.getAssignee() != null) {
            User assignee = userRepository.findById(ticket.getAssignee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));

            ticket.setAssignee(assignee);
        }

        new TicketUpdateMapper().update(oldValue, ticket);

        return ticketRepository.save(oldValue);
    }
}
