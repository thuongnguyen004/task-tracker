package vn.spring.task_tracker.dtos.requests.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TicketUpdateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 150 , message = "Title must not exceed 150 characters.")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;

    @NotNull(message = "Priority is required.")
    private short priorityId;

    @NotNull(message = "Status is required.")
    private short statusId;

    private UUID assigneeId;
}
