package vn.spring.task_tracker.services;

import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refresh(String refreshToken);
    void logout(String refreshToken);
}

