package vn.spring.task_tracker.dtos.responses;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String fullName,
        String username,
        String email,
        Long createdAt,
        Long updatedAt
) {
}
