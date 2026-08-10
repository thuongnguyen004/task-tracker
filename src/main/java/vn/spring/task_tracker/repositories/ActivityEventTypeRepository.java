package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.enums.ActivityEventCode;

import java.util.Optional;

public interface ActivityEventTypeRepository extends JpaRepository<ActivityEventType, Short> {
    Optional<ActivityEventType> findByCode(ActivityEventCode code);

    boolean existsByCode(ActivityEventCode code);
}
