package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    List<User> getAssignees();
}
