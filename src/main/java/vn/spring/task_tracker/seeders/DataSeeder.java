package vn.spring.task_tracker.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.enums.ActivityEventCode;
import vn.spring.task_tracker.repositories.ActivityEventTypeRepository;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TicketStatusRepository ticketStatusRepository;
    private final TicketPriorityRepository ticketPriorityRepository;
    private final ActivityEventTypeRepository activityEventTypeRepository;

    @Override
    public void run(String @NonNull ... args) {
        seedStatuses();
        seedPriorities();
        seedActivityEventTypes();
    }

    private void seedStatuses() {
        List<String> defaultStatuses = List.of(
                "To Do",
                "In Progress",
                "Code Review",
                "Ready for QA",
                "Done"
        );

        for (String statusName : defaultStatuses) {
            if (!ticketStatusRepository.existsByName(statusName)) {
                TicketStatus status = TicketStatus.builder()
                        .name(statusName)
                        .build();

                ticketStatusRepository.save(status);

                log.info("Seeded TicketStatus: {}", statusName);
            }
        }
    }

    private void seedPriorities() {
        List<String> defaultPriorities = List.of(
                "Low",
                "Medium",
                "High",
                "Critical"
        );

        for (String priorityName : defaultPriorities) {
            if (!ticketPriorityRepository.existsByName(priorityName)) {
                TicketPriority priority = TicketPriority.builder()
                        .name(priorityName)
                        .build();

                ticketPriorityRepository.save(priority);

                log.info("Seeded TicketPriority: {}", priorityName);
            }
        }
    }

    private void seedActivityEventTypes() {
        List<ActivityEventCode> defaultEventTypes = List.of(
                ActivityEventCode.TICKET_CREATED,
                ActivityEventCode.TICKET_ARCHIVED,
                ActivityEventCode.STATUS_CHANGED,
                ActivityEventCode.ASSIGNEE_CHANGED,
                ActivityEventCode.PRIORITY_CHANGED,
                ActivityEventCode.TITLE_CHANGED,
                ActivityEventCode.DESCRIPTION_CHANGED,
                ActivityEventCode.COMMENT_ADDED,
                ActivityEventCode.COMMENT_CHANGED
        );

        for (ActivityEventCode eventCode : defaultEventTypes) {
            if (!activityEventTypeRepository.existsByCode(eventCode)) {
                ActivityEventType eventType = ActivityEventType.builder()
                        .code(eventCode)
                        .name(eventCode.name().toLowerCase().replace("_", " "))
                        .build();

                activityEventTypeRepository.save(eventType);

                log.info("Seeded ActivityEventType: {}", eventCode);
            }
        }
    }
}
