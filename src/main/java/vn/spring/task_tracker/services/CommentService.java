package vn.spring.task_tracker.services;

import vn.spring.task_tracker.entities.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment addComment(UUID ticketId, Comment comment);

    List<Comment> getCommentsByTicketId(UUID ticketId);

    Comment updateComment(UUID commentId, Comment comment);

    void deleteComment(UUID commentId);
}
