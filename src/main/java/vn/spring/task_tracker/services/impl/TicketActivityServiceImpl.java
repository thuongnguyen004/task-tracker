package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.ticket_activity.TicketActivityCreateMapper;
import vn.spring.task_tracker.repositories.ActivityEventTypeRepository;
import vn.spring.task_tracker.repositories.TicketActivityRepository;
import vn.spring.task_tracker.services.TicketActivityService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketActivityServiceImpl implements TicketActivityService {

    private final TicketActivityRepository ticketActivityRepository;
    private final ActivityEventTypeRepository activityEventTypeRepository;
    private final SecurityHelper securityHelper;

    @Override
    public List<TicketActivity> getTicketActivityByIdTicket(UUID ticketId) {
        return ticketActivityRepository.findByTicket_IdOrderByCreatedAtDesc(ticketId);
    }

    @Override
    public void createTicketActivity(Ticket oldTicket, Ticket newTicket, User performedBy) {
        // Title
        createIfChanged(
                oldTicket,
                ActivityEventCode.TITLE_CHANGED,
                performedBy,
                oldTicket.getTitle(),
                newTicket.getTitle()
        );

        // Description
        createIfChanged(
                oldTicket,
                ActivityEventCode.DESCRIPTION_CHANGED,
                performedBy,
                oldTicket.getDescription(),
                newTicket.getDescription()
        );

        // Status
        createIfChanged(
                oldTicket,
                ActivityEventCode.STATUS_CHANGED,
                performedBy,
                oldTicket.getStatus().getId(),
                newTicket.getStatus().getId(),
                oldTicket.getStatus().getName(),
                newTicket.getStatus().getName()
        );

        // Priority
        createIfChanged(
                oldTicket,
                ActivityEventCode.PRIORITY_CHANGED,
                performedBy,
                oldTicket.getPriority().getId(),
                newTicket.getPriority().getId(),
                oldTicket.getPriority().getName(),
                newTicket.getPriority().getName()
        );

        // Assignee
        UUID oldAssigneeId = oldTicket.getAssignee() == null
                ? null
                : oldTicket.getAssignee().getId();

        UUID newAssigneeId = newTicket.getAssignee() == null
                ? null
                : newTicket.getAssignee().getId();

        String oldAssigneeName = oldTicket.getAssignee() == null
                ? null
                : oldTicket.getAssignee().getUsername();

        String newAssigneeName = newTicket.getAssignee() == null
                ? null
                : newTicket.getAssignee().getUsername();

        createIfChanged(
                oldTicket,
                ActivityEventCode.ASSIGNEE_CHANGED,
                performedBy,
                oldAssigneeId,
                newAssigneeId,
                oldAssigneeName,
                newAssigneeName
        );
    }

    // Dùng cho title, description
    private void createIfChanged(
            Ticket ticket,
            ActivityEventCode eventCode,
            User performedBy,
            String oldValue,
            String newValue
    ) {
        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        create(
                ticket,
                eventCode,
                performedBy,
                oldValue,
                newValue
        );
    }

    // Dùng cho status, priority, assignee
    private void createIfChanged(
            Ticket ticket,
            ActivityEventCode eventCode,
            User performedBy,
            Object oldCompareValue,
            Object newCompareValue,
            String oldValue,
            String newValue
    ) {
        if (Objects.equals(oldCompareValue, newCompareValue)) {
            return;
        }

        create(
                ticket,
                eventCode,
                performedBy,
                oldValue,
                newValue
        );
    }

    private void create(
            Ticket ticket,
            ActivityEventCode eventCode,
            User performedBy,
            String oldValue,
            String newValue
    ) {
        ActivityEventType eventType =
                activityEventTypeRepository.findByCode(eventCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity event type not found."
                                )
                        );

        TicketActivity activity =
                new TicketActivityCreateMapper().build(
                        ticket,
                        eventType,
                        performedBy,
                        oldValue,
                        newValue
                );

        ticketActivityRepository.save(activity);
    }
}
