package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.spring.task_tracker.entities.TicketPriority;
import vn.spring.task_tracker.entities.TicketStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketPriorityRepository extends JpaRepository<TicketPriority, Short> {
    Optional<TicketPriority> findByName(String name);

    Optional<TicketPriority> findById(short id);

    boolean existsByName(String name);
}
