package vn.spring.task_tracker.exceptions;

public class InvalidException extends RuntimeException {

    public InvalidException(String message) {
        super(message);
    }
}
