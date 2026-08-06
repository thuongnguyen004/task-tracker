package vn.spring.task_tracker.dtos.responses;

public record LoginResult(
        LoginResponse response,
        String refreshToken
) {
}
