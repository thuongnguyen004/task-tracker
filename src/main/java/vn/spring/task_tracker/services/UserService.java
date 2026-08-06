package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
