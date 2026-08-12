package vn.spring.task_tracker.services.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.spring.task_tracker.constants.AuthMessage;
import vn.spring.task_tracker.constants.UserMessage;
import vn.spring.task_tracker.dtos.requests.LoginRequest;
import vn.spring.task_tracker.dtos.requests.RegisterRequest;
import vn.spring.task_tracker.dtos.responses.LoginResponse;
import vn.spring.task_tracker.dtos.responses.LoginResult;
import vn.spring.task_tracker.dtos.responses.RefreshTokenResult;
import vn.spring.task_tracker.dtos.responses.UserProfileResponse;
import vn.spring.task_tracker.entities.RefreshToken;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.exceptions.ConflictException;
import vn.spring.task_tracker.exceptions.InvalidException;
import vn.spring.task_tracker.exceptions.ResourceNotFoundException;
import vn.spring.task_tracker.mappers.AuthMapper;
import vn.spring.task_tracker.repositories.UserRepository;
import vn.spring.task_tracker.security.JwtService;
import vn.spring.task_tracker.security.RefreshTokenService;
import vn.spring.task_tracker.services.AuthService;
import vn.spring.task_tracker.services.CurrentUserService;

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
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException(AuthMessage.EMAIL_ALREADY_EXISTS);
        }

        if (
            userRepository.existsByUsernameIgnoreCase(
                request.getUsername().trim()
            )
        ) {
            throw new ConflictException(AuthMessage.USERNAME_ALREADY_EXISTS);
        }

        User user = authMapper.toEntity(request);
        user.setEmail(email);
        user.setUsername(request.getUsername().trim());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResult login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email,
                    request.getPassword()
                )
            );
        } catch (AuthenticationException exception) {
            throw new InvalidException(AuthMessage.INVALID_CREDENTIALS);
        }

        User user = findByEmail(email);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.save(refreshToken, user);

        LoginResponse response = authMapper.toLoginResponse(user, accessToken);

        return new LoginResult(response, refreshToken);
    }

    @Override
    @Transactional
    public RefreshTokenResult refresh(String refreshToken) {
        if (!jwtService.verifyRefreshToken(refreshToken)) {
            throw new InvalidException(AuthMessage.INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedRefreshToken = refreshTokenService.getValidToken(
            refreshToken
        );

        User user = storedRefreshToken.getUser();

        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.save(newRefreshToken, user);

        return new RefreshTokenResult(accessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revokeIfExists(refreshToken);
    }

    @Override
    public UserProfileResponse getCurrentUser() {
        UUID userId = currentUserService.getCurrentUserId();

        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(UserMessage.NOT_FOUND));

        return authMapper.toUserProfileResponse(user);
    }

    private User findByEmail(String email) {
        return userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new InvalidException(AuthMessage.INVALID_CREDENTIALS)
            );
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
