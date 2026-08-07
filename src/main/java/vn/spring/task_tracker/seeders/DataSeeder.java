package vn.spring.task_tracker.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.repositories.TicketPriorityRepository;
import vn.spring.task_tracker.repositories.TicketStatusRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TicketStatusRepository ticketStatusRepository;
    private final TicketPriorityRepository ticketPriorityRepository;

    @Override
    public void run(String @NonNull ... args) {
        seedStatuses();
        seedPriorities();
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
}
