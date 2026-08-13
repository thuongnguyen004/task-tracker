package vn.spring.task_tracker.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.spring.task_tracker.entities.TicketActivity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketActivityRepository extends JpaRepository<TicketActivity, UUID> {
    Page<TicketActivity> findByTicket_IdOrderByCreatedAtDesc(UUID ticketId, Pageable pageable);

    Page<TicketActivity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
