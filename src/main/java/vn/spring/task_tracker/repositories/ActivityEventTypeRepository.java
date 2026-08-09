package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.spring.task_tracker.entities.ActivityEventType;
import vn.spring.task_tracker.enums.ActivityEventCode;

public interface ActivityEventTypeRepository extends JpaRepository<ActivityEventType, Short> {
    boolean existsByCode(ActivityEventCode code);
}
