package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.*;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.repositories.*;
import vn.spring.task_tracker.services.TicketActivityService;
import vn.spring.task_tracker.constants.TicketMessage;
import vn.spring.task_tracker.constants.TicketPriorityMessage;
import vn.spring.task_tracker.constants.TicketStatusMessage;
import vn.spring.task_tracker.constants.UserMessage;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.mappers.TicketUpdateMapper;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.services.TicketService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketStatusRepository ticketStatusRepository;
    private final TicketPriorityRepository ticketPriorityRepository;
    private final TicketActivityService ticketActivityService;
    private final UserRepository userRepository;
    private final SecurityHelper securityHelper;

    public Ticket createTicket(Ticket ticket) {

        User currentUser = securityHelper.getCurrentUser();

        TicketPriority ticketPriority = ticketPriorityRepository.findById(ticket.getPriority().getId())
                .orElseThrow(() -> new ResourceNotFoundException(TicketPriorityMessage.NOT_FOUND));

        TicketStatus ticketStatus = ticketStatusRepository.findById(ticket.getStatus().getId())
                .orElseThrow(() -> new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND));

        User assignee = null;

        if (ticket.getAssignee() != null) {
            assignee = userRepository.findById(ticket.getAssignee().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(UserMessage.ASSIGNEE_NOT_FOUND));
        }

        ticket.setAssignee(assignee);
        ticket.setPriority(ticketPriority);
        ticket.setStatus(ticketStatus);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(currentUser);

        return this.ticketRepository.save(ticket);
    }

    public Ticket getActiveTicketById(UUID id) {
        return this.ticketRepository.findByIdAndArchivedFalse(id).orElseThrow(() ->
                new ResourceNotFoundException(TicketMessage.NOT_FOUND));
    }

    public List<Ticket> getAllActiveTickets() {
        return ticketRepository.findByArchivedFalse();
    }

    public Ticket updateTicket(UUID ticketId, Ticket ticket) {
        User currentUser = securityHelper.getCurrentUser();

        Ticket oldValue = ticketRepository.findByIdAndArchivedFalse(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(TicketMessage.NOT_FOUND));

        TicketPriority ticketPriority = ticketPriorityRepository.findById(ticket.getPriority().getId())
                .orElseThrow(() -> new ResourceNotFoundException(TicketPriorityMessage.NOT_FOUND));

        TicketStatus ticketStatus = ticketStatusRepository.findById(ticket.getStatus().getId())
                .orElseThrow(() -> new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND));

        User assignee = ticket.getAssignee() == null ? null : userRepository.findById(ticket.getAssignee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(UserMessage.NOT_FOUND));

        ticket.setPriority(ticketPriority);

        ticket.setStatus(ticketStatus);

        ticket.setAssignee(assignee);

        ticketActivityService.createTicketActivity(
                oldValue,
                ticket,
                currentUser
        );

        new TicketUpdateMapper().update(oldValue, ticket);

        return ticketRepository.save(oldValue);
    }

    public void changeStatusTicket(UUID ticketId, short statusId) {
        User currentUser = securityHelper.getCurrentUser();
        Ticket ticket = ticketRepository.findByIdAndArchivedFalse(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(TicketMessage.NOT_FOUND));

        TicketStatus ticketStatus = ticketStatusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND));

        Ticket newTicket = new Ticket(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticketStatus,
                ticket.getAssignee()
        );

        ticketActivityService.createTicketActivity(
                ticket,
                newTicket,
                currentUser
        );

        ticket.setStatus(ticketStatus);

        ticketRepository.save(ticket);
    }
}
