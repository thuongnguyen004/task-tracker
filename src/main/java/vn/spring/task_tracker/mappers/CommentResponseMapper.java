package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.responses.CommentResponse;
import vn.spring.task_tracker.entities.Comment;

import java.util.List;

public class CommentResponseMapper {
    public CommentResponse build(Comment comment) {
        String author = comment.getCreatedBy() != null ? comment.getCreatedBy().getUsername() : "Unknown";

        boolean edited = comment.getUpdatedAt() != null
                && comment.getCreatedAt() != null
                && !comment.getUpdatedAt().equals(comment.getCreatedAt());

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                author,
                comment.getCreatedBy() != null ? comment.getCreatedBy().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                edited
        );
    }

    public List<CommentResponse> buildList(List<Comment> comments) {
        if (comments == null) return List.of();

        return comments.stream().map(this::build).toList();
    }
}
