package vn.spring.task_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.spring.task_tracker.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
