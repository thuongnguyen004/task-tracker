package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.enums.ActivityEventCode;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.repositories.*;
import vn.spring.task_tracker.constants.TicketMessage;
import vn.spring.task_tracker.constants.TicketPriorityMessage;
import vn.spring.task_tracker.constants.TicketStatusMessage;
import vn.spring.task_tracker.constants.UserMessage;
import vn.spring.task_tracker.mappers.TicketUpdateMapper;
import vn.spring.task_tracker.services.TicketActivityService;
import vn.spring.task_tracker.services.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;

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

        Optional<Ticket> latest = ticketRepository.findTopByOrderByCreatedAtDesc();

        long nextCode = latest
                .map(t -> Long.parseLong(t.getCode().replace("TICKET-", "")) + 1)
                .orElse(1L);

        ticket.setCode(String.format("TICKET-%05d", nextCode));
        ticket.setPriority(ticketPriority);
        ticket.setStatus(ticketStatus);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(currentUser);

        Ticket savedTicket = ticketRepository.save(ticket);

        ticketActivityService.createTicketActivity(savedTicket, ActivityEventCode.TICKET_CREATED, currentUser,null, null);

        return savedTicket;
    }

    public Ticket getTicketByCode(String code) {
        return this.ticketRepository.findByCode(code).orElseThrow(() ->
                new ResourceNotFoundException(TicketMessage.NOT_FOUND));
    }

    public List<Ticket> getAllActiveTickets() {
        return ticketRepository.findByArchivedFalseOrderByUpdatedAtDesc();
    }

    public List<Ticket> findWithFilters(
        String title,
        List<Short> priorityIds,
        UUID assigneeId,
        boolean unassigned
    ) {
        boolean filterByAssignee;

        if (unassigned) {
            filterByAssignee = true;
            assigneeId = null;
        } else if (assigneeId != null) {
            filterByAssignee = true;
        } else {
            filterByAssignee = false;
        }
        return ticketRepository.findWithFilters(
            title,
            priorityIds,
            filterByAssignee,
            unassigned,
            assigneeId
        );
    }

    public List<Ticket> getAllArchiveTickets() {
        return ticketRepository.findByArchivedTrueOrderByUpdatedAtDesc();
    }

    public void archiveTicket(UUID ticketId) {
        User currentUser = securityHelper.getCurrentUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(TicketMessage.NOT_FOUND));

        ticket.setArchived(true);

        ticketActivityService.createTicketActivity(ticket, ActivityEventCode.TICKET_ARCHIVED, currentUser, null,null);

        ticketRepository.save(ticket);
    }

    public void restoreTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(TicketMessage.NOT_FOUND));

        ticket.setArchived(false);

        ticketRepository.save(ticket);
    }

    public Ticket updateTicket(String code, Ticket ticket) {
        User currentUser = securityHelper.getCurrentUser();

        Ticket oldValue = ticketRepository.findByCode(code)
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

    public Ticket changeStatusTicket(UUID ticketId, short statusId) {
        User currentUser = securityHelper.getCurrentUser();
        Ticket ticket = ticketRepository.findById(ticketId)
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

        return ticketRepository.save(ticket);
    }
}
