package vn.spring.task_tracker.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RefreshTokenRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.AuthResponse;
import vn.spring.task_tracker.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.login(request);

        setAuthCookies(
            response,
            authResponse.getAccessToken(),
            authResponse.getRefreshToken(),
            authResponse.getExpiresInMs()
        );

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @Valid @RequestBody RegisterRequest request,
        HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.register(request);

        setAuthCookies(
            response,
            authResponse.getAccessToken(),
            authResponse.getRefreshToken(),
            authResponse.getExpiresInMs()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @RequestBody(required = false) RefreshTokenRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse response
    ) {
        String token = null;

        if (
            request != null &&
            request.getRefreshToken() != null &&
            !request.getRefreshToken().isBlank()
        ) {
            token = request.getRefreshToken();
        } else if (httpRequest.getCookies() != null) {
            for (Cookie c : httpRequest.getCookies()) {
                if ("refreshToken".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AuthResponse authResponse = authService.refresh(token);

        setAuthCookies(
            response,
            authResponse.getAccessToken(),
            authResponse.getRefreshToken(),
            authResponse.getExpiresInMs()
        );

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @RequestBody(required = false) RefreshTokenRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse response
    ) {
        String token = null;

        if (request != null && request.getRefreshToken() != null) {
            token = request.getRefreshToken();
        } else if (httpRequest.getCookies() != null) {
            for (Cookie c : httpRequest.getCookies()) {
                if ("refreshToken".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            authService.logout(token);
        }

        clearAuthCookies(response);
        return ResponseEntity.noContent().build();
    }

    private void setAuthCookies(
        HttpServletResponse response,
        String accessToken,
        String refreshToken,
        Long expiresInMs
    ) {
        long maxAgeSeconds = expiresInMs != null ? expiresInMs / 1000 : 86400;

        ResponseCookie accessCookie = ResponseCookie.from(
            "accessToken",
            accessToken
        )
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAgeSeconds)
            .sameSite("Lax")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from(
            "refreshToken",
            refreshToken
        )
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(604800)
            .sameSite("Lax")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .sameSite("Lax")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .sameSite("Lax")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
