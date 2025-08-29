package hexlet.code.it;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestAuth {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TestAuth(UserRepository userRepository,
                    PasswordEncoder passwordEncoder,
                    JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String ensureToken(String email, String rawPassword) {
        User u = userRepository.findByEmail(email).orElseGet(() -> {
            User nu = new User();
            nu.setEmail(email);
            nu.setPasswordHash(passwordEncoder.encode(rawPassword));
            nu.setFirstName("Test");
            nu.setLastName("User");
            return userRepository.save(nu);
        });
        return jwtService.issue(u.getId(), u.getEmail());
    }
}
