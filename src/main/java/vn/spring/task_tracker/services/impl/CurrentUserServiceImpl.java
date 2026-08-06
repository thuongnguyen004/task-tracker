package vn.spring.task_tracker.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.services.CurrentUserService;

import java.util.Objects;
import java.util.UUID;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public UUID getCurrentUserId() {
        Jwt jwt = (Jwt) Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication()
                ).getPrincipal();

        return UUID.fromString(jwt.getClaimAsString("userId"));
    }
}
