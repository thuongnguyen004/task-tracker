package vn.spring.task_tracker.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.mappers.AuthMapper;
import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.LoginResponse;
import vn.spring.task_tracker.dtos.responses.LoginResult;
import vn.spring.task_tracker.dtos.responses.RefreshTokenResponse;
import vn.spring.task_tracker.dtos.responses.UserProfileResponse;
import vn.spring.task_tracker.entities.RefreshToken;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.AppException;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.security.JwtService;
import vn.spring.task_tracker.security.RefreshTokenService;
import vn.spring.task_tracker.services.AuthService;
import vn.spring.task_tracker.services.CurrentUserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CurrentUserService currentUserService;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername().trim())) {
            throw new AppException(HttpStatus.CONFLICT, "Username already exists");
        }


        User user = authMapper.toEntity(request);
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResult login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        User user = findByEmail(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.save(refreshToken, user);

        LoginResponse response = authMapper.toLoginResponse(user, accessToken);

        return new LoginResult(response, refreshToken);
    }

    @Override
    public RefreshTokenResponse refresh(String refreshToken) {
        if (!jwtService.verifyRefreshToken(refreshToken)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        RefreshToken storedRefreshToken = refreshTokenService.getValidToken(refreshToken);

        String tokenUserId = jwtService.extractUserIdFromRefreshToken(refreshToken);

        if (!storedRefreshToken.getUser().getId().toString().equals(tokenUserId)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String accessToken = jwtService.generateAccessToken(storedRefreshToken.getUser());

        return new RefreshTokenResponse(accessToken);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revokeIfExists(refreshToken);
    }

    @Override
    public UserProfileResponse getCurrentUser() {
        UUID userId = currentUserService.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        return authMapper.toUserProfileResponse(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    }
}
