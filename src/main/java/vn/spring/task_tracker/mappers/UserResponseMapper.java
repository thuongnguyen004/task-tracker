package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.responses.UserResponse;
import vn.spring.task_tracker.entities.User;

import java.util.List;

public class UserResponseMapper {
    public List<UserResponse> build(List<User> users) {
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFullName()
                ))
                .toList();
    }
}
