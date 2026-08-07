package vn.spring.task_tracker.mappers.users;

import vn.spring.task_tracker.dtos.responses.users.UserResponse;
import vn.spring.task_tracker.entities.User;

import java.util.List;

public class UserResponseMapper {
    public List<UserResponse> build(List<User> users) {
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername()
                ))
                .toList();
    }
}
