package vn.spring.task_tracker.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.constants.CommentMessage;
import vn.spring.task_tracker.dtos.requests.CommentRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.CommentResponse;
import vn.spring.task_tracker.entities.Comment;
import vn.spring.task_tracker.mappers.CommentCreateMapper;
import vn.spring.task_tracker.mappers.CommentResponseMapper;
import vn.spring.task_tracker.services.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable
            UUID ticketId,

            @Valid
            @RequestBody
            CommentRequest createCommentRequest
    ) {
        Comment comment = new CommentCreateMapper().build(createCommentRequest);

        Comment savedComment = commentService.addComment(ticketId, comment);

        CommentResponse commentResponse = new CommentResponseMapper().build(savedComment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(CommentMessage.CREATE_SUCCESS, commentResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable
            UUID ticketId
    ) {
        List<Comment> comments = commentService.getCommentsByTicketId(ticketId);

        List<CommentResponse> commentResponseList = new CommentResponseMapper().buildList(comments);

        return ResponseEntity.ok(ApiResponse.success(CommentMessage.GET_ALL_SUCCESS, commentResponseList));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable
            UUID commentId,

            @Valid
            @RequestBody
            CommentRequest updateCommentRequest
    ) {
        Comment comment = new CommentCreateMapper().build(updateCommentRequest);

        Comment updatedComment = commentService.updateComment(commentId, comment);

        CommentResponse commentResponse = new CommentResponseMapper().build(updatedComment);

        return ResponseEntity.ok(ApiResponse.success(CommentMessage.UPDATE_SUCCESS, commentResponse));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok(ApiResponse.success(CommentMessage.DELETE_SUCCESS, null));
    }
}
