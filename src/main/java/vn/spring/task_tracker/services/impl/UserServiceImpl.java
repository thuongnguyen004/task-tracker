package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<User> getAssignees() {
        return userRepository.findAll();
    }
}
