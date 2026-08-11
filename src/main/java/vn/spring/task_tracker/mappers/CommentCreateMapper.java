package vn.spring.task_tracker.mappers;

import vn.spring.task_tracker.dtos.requests.CommentRequest;
import vn.spring.task_tracker.entities.Comment;

public class CommentCreateMapper {
    public Comment build(CommentRequest commentRequest) {
        Comment comment = new Comment();

        comment.setContent(commentRequest.getContent());

        return comment;
    }
}
