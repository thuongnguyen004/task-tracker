package vn.spring.task_tracker.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMessage {
    public static final String CREATE_SUCCESS = "Add comment successfully";
    public static final String GET_ALL_SUCCESS = "Get comments successfully";
    public static final String UPDATE_SUCCESS = "Update comment successfully";
    public static final String DELETE_SUCCESS = "Delete comment successfully";

    public static final String NOT_FOUND = "Comment not found.";

    public static final String FORBIDDEN_EDIT = "You can only edit your own comment";
    public static final String FORBIDDEN_DELETE = "You can only delete your own comment";
}
