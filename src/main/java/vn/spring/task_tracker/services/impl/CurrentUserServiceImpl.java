package vn.spring.task_tracker.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.constants.AuthMessage;
import vn.spring.task_tracker.services.CurrentUserService;

import java.util.UUID;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException(AuthMessage.USER_NOT_AUTHENTICATED);
        }

        String userId = jwt.getClaimAsString("userId");

        assert userId != null;

        return UUID.fromString(userId);
    }
}
