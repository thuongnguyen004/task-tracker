package vn.spring.task_tracker.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UUID id;
    private String username;
    private String email;
    private String accessToken;
}
