package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.constants.ActivityEventTypeMessage;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.TicketActivity;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.mappers.TicketActivityCreateMapper;
import vn.spring.task_tracker.repositories.ActivityEventTypeRepository;
import vn.spring.task_tracker.repositories.TicketActivityRepository;
import vn.spring.task_tracker.services.TicketActivityService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketActivityServiceImpl implements TicketActivityService {

    private final TicketActivityRepository ticketActivityRepository;
    private final ActivityEventTypeRepository activityEventTypeRepository;
    private final TicketActivityChangeDetector changeDetector;

    @Override
    public Page<TicketActivity> getTicketActivityByIdTicket(UUID ticketId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketActivityRepository.findByTicket_IdOrderByCreatedAtDesc(ticketId, pageable);
    }

    @Override
    public Page<TicketActivity> getTicketActivity(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketActivityRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public void createTicketActivity(
            Ticket ticket,
            ActivityEventCode eventCode,
            User performedBy,
            String oldValue,
            String newValue
    ) {
        create(
                ticket,
                eventCode,
                performedBy,
                oldValue,
                newValue
        );
    }

    @Override
    public void createTicketActivity(Ticket oldTicket, Ticket newTicket, User performedBy) {
        List<TicketActivityChangeDetector.ActivityChange> changes =
                changeDetector.detect(oldTicket, newTicket);

        if (changes.isEmpty()) {
            return;
        }

        Set<ActivityEventCode> codes = changes.stream()
                .map(TicketActivityChangeDetector.ActivityChange::getEventCode)
                .collect(Collectors.toSet());

        Map<ActivityEventCode, ActivityEventType> eventTypes =
                activityEventTypeRepository.findAllByCodeIn(codes)
                        .stream()
                        .collect(Collectors.toMap(
                                ActivityEventType::getCode,
                                Function.identity()
                        ));

        List<TicketActivity> activities = changes.stream()
                .map(change -> new TicketActivityCreateMapper().build(
                        oldTicket,
                        getEventType(eventTypes, change.getEventCode()),
                        performedBy,
                        change.getOldValue(),
                        change.getNewValue()
                ))
                .toList();

        ticketActivityRepository.saveAll(activities);
    }

    private ActivityEventType getEventType(
            Map<ActivityEventCode, ActivityEventType> eventTypes,
            ActivityEventCode eventCode
    ) {
        ActivityEventType eventType = eventTypes.get(eventCode);
        if (eventType == null) {
            throw new ResourceNotFoundException(ActivityEventTypeMessage.NOT_FOUND);
        }
        return eventType;
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
                                        ActivityEventTypeMessage.NOT_FOUND
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
