package vn.spring.task_tracker.dtos.responses;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken
) {
}
