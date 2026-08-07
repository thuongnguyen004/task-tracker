package vn.spring.task_tracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.users.UserResponse;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.mappers.users.UserResponseMapper;
import vn.spring.task_tracker.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/assignees")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAssignees () {
        List<User> user = userService.getAssignees();
        List<UserResponse> userResponse = new UserResponseMapper().build(user);
        return ResponseEntity.ok(ApiResponse.success("Get data user successfully.", userResponse));
    }
}
