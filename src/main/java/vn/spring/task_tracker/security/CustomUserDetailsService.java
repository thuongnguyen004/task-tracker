package vn.spring.task_tracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.spring.task_tracker.entities.User;
import vn.spring.task_tracker.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        String password = user.getPassword() == null ? "" : user.getPassword();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                password,
                List.of());
    }

    public UserDetails loadOrCreateUserFromOAuth(String email, String userName) {
        String normalizedEmail = email.toLowerCase().trim();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(normalizedEmail)
                            .username(userName)
                            .password(null)
                            .build();
                    return userRepository.save(newUser);
                });
        return loadUserByUsername(user.getEmail());
    }
}

