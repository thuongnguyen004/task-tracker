package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.constants.CommentMessage;
import vn.spring.task_tracker.constants.TicketMessage;
import vn.spring.task_tracker.entities.Comment;
import vn.spring.task_tracker.entities.Ticket;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.enums.ActivityEventCode;
import vn.spring.task_tracker.exceptions.ForbiddenException;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.helpers.SecurityHelper;
import vn.spring.task_tracker.repositories.CommentRepository;
import vn.spring.task_tracker.repositories.TicketRepository;
import vn.spring.task_tracker.services.CommentService;
import vn.spring.task_tracker.services.TicketActivityService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TicketRepository ticketRepository;

    private final SecurityHelper securityHelper;

    private final TicketActivityService ticketActivityService;

    @Override
    @Transactional
    public Comment addComment(UUID ticketId, Comment comment) {
        User currentUser = securityHelper.getCurrentUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(TicketMessage.NOT_FOUND));

        comment.setContent(comment.getContent().trim());
        comment.setTicket(ticket);
        comment.setCreatedBy(currentUser);

        ticketActivityService.createTicketActivity(ticket, ActivityEventCode.COMMENT_ADDED, currentUser, null, null);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByTicketId(UUID ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    @Override
    @Transactional
    public Comment updateComment(
            UUID commentId,
            Comment comment
    ) {
        User currentUser = securityHelper.getCurrentUser();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(CommentMessage.NOT_FOUND));

        if (!existingComment.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(CommentMessage.FORBIDDEN_EDIT);
        }

        existingComment.setContent(comment.getContent().trim());

        return commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(UUID commentId) {
        User currentUser = securityHelper.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(CommentMessage.NOT_FOUND));

        if (!comment.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(CommentMessage.FORBIDDEN_DELETE);
        }

        commentRepository.delete(comment);
    }
}
