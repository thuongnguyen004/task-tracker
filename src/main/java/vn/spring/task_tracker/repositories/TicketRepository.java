package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.spring.task_tracker.entities.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByArchivedFalse();

    List<Ticket> findByArchivedTrueOrderByUpdatedAtDesc();

    Optional<Ticket> findByIdAndArchivedFalse(UUID id);

    List<Ticket> findByArchivedFalseOrderByUpdatedAtDesc();

    Optional<Ticket> findTopByOrderByCreatedAtDesc();

    Optional<Ticket> findByCode(String code);
}
