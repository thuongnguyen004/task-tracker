package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.spring.task_tracker.entities.TicketStatus;

import java.util.Optional;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, Short> {
    Optional<TicketStatus> findByName(String name);

    boolean existsByName(String name);
}
