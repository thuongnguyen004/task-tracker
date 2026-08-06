package vn.spring.task_tracker.dtos.responses;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String username,
        String email
) {
}
