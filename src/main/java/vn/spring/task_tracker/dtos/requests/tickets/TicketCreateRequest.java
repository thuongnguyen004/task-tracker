package vn.spring.task_tracker.dtos.requests.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 150)
    private String title;

    @Size(max = 5000)
    private String description;

    private short priorityId;

    private short statusId;

    private UUID assigneeId;
}

