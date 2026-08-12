package vn.spring.task_tracker.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}
