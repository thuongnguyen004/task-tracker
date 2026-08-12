package vn.spring.task_tracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.constants.TicketMessage;
import vn.spring.task_tracker.constants.TicketPriorityMessage;
import vn.spring.task_tracker.constants.TicketStatusMessage;
import vn.spring.task_tracker.constants.UserMessage;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.TicketUpdateMapper;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.services.TicketActivityService;
import vn.spring.task_tracker.services.TicketService;

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

        TicketPriority ticketPriority = ticketPriorityRepository
            .findById(ticket.getPriority().getId())
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketPriorityMessage.NOT_FOUND)
            );

        TicketStatus ticketStatus = ticketStatusRepository
            .findById(ticket.getStatus().getId())
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND)
            );

        User assignee = null;

        if (ticket.getAssignee() != null) {
            assignee = userRepository
                .findById(ticket.getAssignee().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        UserMessage.ASSIGNEE_NOT_FOUND
                    )
                );
        }

        Optional<Ticket> latest = ticketRepository.findTopByOrderByCreatedAtDesc();

        long nextCode = latest
                .map(t -> Long.parseLong(t.getCode().replace("TICKET-", "")) + 1)
                .orElse(1L);

        ticket.setCode(String.format("TICKET-%05d", nextCode));
        ticket.setAssignee(assignee);
        ticket.setPriority(ticketPriority);
        ticket.setStatus(ticketStatus);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(currentUser);

        return this.ticketRepository.save(ticket);
    }

    public Ticket getActiveTicketById(UUID id) {
        return this.ticketRepository
            .findByIdAndArchivedFalse(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketMessage.NOT_FOUND)
            );
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

    public Ticket updateTicket(UUID ticketId, Ticket ticket) {
        Ticket oldValue = ticketRepository
            .findByIdAndArchivedFalse(ticketId)
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketMessage.NOT_FOUND)
            );

        ticketPriorityRepository
            .findById(ticket.getPriority().getId())
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketPriorityMessage.NOT_FOUND)
            );

        ticketStatusRepository
            .findById(ticket.getStatus().getId())
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND)
            );

        if (ticket.getAssignee() != null) {
            userRepository
                .findById(ticket.getAssignee().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(UserMessage.NOT_FOUND)
                );
        }

        new TicketUpdateMapper().update(oldValue, ticket);

        return ticketRepository.save(oldValue);
    }

    public Ticket changeStatusTicket(UUID ticketId, short statusId) {
        Ticket ticket = ticketRepository
            .findByIdAndArchivedFalse(ticketId)
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketMessage.NOT_FOUND)
            );

        TicketStatus ticketStatus = ticketStatusRepository
            .findById(statusId)
            .orElseThrow(() ->
                new ResourceNotFoundException(TicketStatusMessage.NOT_FOUND)
            );

        ticket.setStatus(ticketStatus);

        return ticketRepository.save(ticket);
    }
}
