package vn.spring.task_tracker.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthMessage {
    public static final String REGISTER_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Account login successfully";
    public static final String REFRESH_SUCCESS = "Access token refreshed successfully";
    public static final String LOGOUT_SUCCESS = "Account logout successfully";
    public static final String GET_CURRENT_USER_SUCCESS = "User retrieved successfully";

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String REFRESH_TOKEN_MISSING = "Refresh token is missing";
    public static final String USER_NOT_AUTHENTICATED = "User is not authenticated";
}
