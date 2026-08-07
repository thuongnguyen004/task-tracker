package vn.spring.task_tracker.services;

import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.LoginResult;
import vn.spring.task_tracker.dtos.responses.RefreshTokenResult;
import vn.spring.task_tracker.dtos.responses.UserProfileResponse;
import vn.spring.task_tracker.entities.User;

public interface AuthService {
    User register(RegisterRequest request);

    LoginResult login(LoginRequest request);

    RefreshTokenResult refresh(String refreshToken);

    void logout(String refreshToken);

    UserProfileResponse getCurrentUser();
}
