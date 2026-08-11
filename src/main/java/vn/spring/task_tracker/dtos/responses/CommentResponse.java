package vn.spring.task_tracker.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private UUID id;

    private String content;

    private String author;

    private UUID createdById;

    private Long createdAt;

    private Long updatedAt;

    private boolean edited;
}
