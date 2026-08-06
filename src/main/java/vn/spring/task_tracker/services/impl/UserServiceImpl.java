package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.TicketStatus;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.repositories.TicketStatusRepository;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.services.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User getUserById(UUID id){

        return this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
