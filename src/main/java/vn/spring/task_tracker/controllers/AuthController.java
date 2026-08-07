package vn.spring.task_tracker.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.task_tracker.configs.JwtProperties;
import vn.spring.task_tracker.mappers.AuthMapper;
import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.ApiResponse;
import vn.spring.task_tracker.dtos.responses.LoginResponse;
import vn.spring.task_tracker.dtos.responses.LoginResult;
import vn.spring.task_tracker.dtos.responses.RefreshTokenResponse;
import vn.spring.task_tracker.dtos.responses.RefreshTokenResult;
import vn.spring.task_tracker.dtos.responses.RegisterResponse;
import vn.spring.task_tracker.dtos.responses.UserProfileResponse;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.AppException;
import vn.spring.task_tracker.services.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication APIs")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;
    private final JwtProperties jwtProperties;

    @Operation(summary = "Register a new account")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {
        User user = authService.register(request);
        RegisterResponse response = authMapper.toRegisterResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Account created successfully", response));
    }

    @Operation(summary = "Login to an account")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {
        LoginResult loginResult = authService.login(request);

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", loginResult.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/")
                .maxAge(jwtProperties.getRefreshTokenExpiration())
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success("Account login successfully", loginResult.response()));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(
            @CookieValue(
                    name = "refreshToken",
                    required = false
            )
            String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        RefreshTokenResult result = authService.refresh(refreshToken);

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/")
                .maxAge(jwtProperties.getRefreshTokenExpiration())
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success("Access token refreshed successfully", new RefreshTokenResponse(result.accessToken())));
    }

    @Operation(summary = "Logout from an account")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(
                    name = "refreshToken",
                    required = false
            )
            String refreshToken
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success("Account logout successfully"));
    }

    @Operation(summary = "Get current user profile")
    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser() {
        UserProfileResponse response = authService.getCurrentUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User retrieved successfully", response));
    }
}
