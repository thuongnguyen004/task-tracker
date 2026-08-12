package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAssignees();
}
