package vn.spring.task_tracker.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.spring.task_tracker.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findByIdAndArchivedFalse(UUID id);

    List<Ticket> findByArchivedFalse();

    @Query(
        """
            SELECT t FROM Ticket t
            WHERE t.archived = false
            AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:priorityIds IS NULL OR t.priority.id IN :priorityIds)
            AND (
                :filterByAssignee = false
                OR (:unassigned = true AND t.assignee IS NULL)
                OR (:assigneeId IS NOT NULL AND t.assignee.id = :assigneeId)
            )
        """
    )
    List<Ticket> findWithFilters(
        @Param("title") String title,
        @Param("priorityIds") List<Short> priorityIds,
        @Param("filterByAssignee") boolean filterByAssignee,
        @Param("unassigned") boolean unassigned,
        @Param("assigneeId") UUID assigneeId
    );
}
