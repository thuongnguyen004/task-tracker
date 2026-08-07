package vn.spring.task_tracker.mappers;

import org.springframework.stereotype.Component;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.LoginResponse;
import vn.spring.task_tracker.dtos.responses.RegisterResponse;
import vn.spring.task_tracker.dtos.responses.UserProfileResponse;
import vn.spring.task_tracker.entities.User;

@Component
public class AuthMapper {

    public User toEntity(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        return user;
    }

    public RegisterResponse toRegisterResponse(User user) {
        return new RegisterResponse(user.getId(), user.getUsername(), user.getFullName(), user.getEmail());
    }

    public LoginResponse toLoginResponse(User user, String accessToken) {
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
    }

    public UserProfileResponse toUserProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
