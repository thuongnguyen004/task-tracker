package vn.spring.task_tracker.dtos.responses;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201,message,data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message, null);
    }
}