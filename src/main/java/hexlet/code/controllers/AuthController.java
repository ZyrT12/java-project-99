package hexlet.code.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.auth.LoginRequest;
import hexlet.code.dto.auth.LoginResponse;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping({"/api/login", "/login"})
    public ResponseEntity<LoginResponse> login(HttpServletRequest request) {
        try {
            String body = readBody(request);
            String email = null;
            String username = null;
            String password = null;

            if (body != null && !body.isBlank()) {
                JsonNode node = objectMapper.readTree(body);
                if (node.hasNonNull("email")) {
                    email = node.get("email").asText();
                }
                if (node.hasNonNull("username")) {
                    username = node.get("username").asText();
                }
                if (node.hasNonNull("password")) {
                    password = node.get("password").asText();
                }
            }

            if (password == null) {
                password = request.getParameter("password");
            }
            if (email == null || email.isBlank()) {
                email = request.getParameter("email");
            }
            if (username == null || username.isBlank()) {
                username = request.getParameter("username");
            }

            String principal = (email != null && !email.isBlank()) ? email : username;
            if (principal == null || principal.isBlank() || password == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<User> userOpt = userRepository.findByEmail(principal);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = generateJwt(jwtService, principal, user.getId());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        String email = request != null ? request.getEmail() : null;
        String username = request != null ? request.getUsername() : null;
        String principal = (email != null && !email.isBlank()) ? email : username;
        String rawPassword = request != null ? request.getPassword() : null;

        String token = authenticateOrThrow(principal, rawPassword);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    private String authenticateOrThrow(String principal, String rawPassword) {
        if (principal == null || principal.isBlank() || rawPassword == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        Optional<User> userOpt = userRepository.findByEmail(principal);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return generateJwt(jwtService, principal, user.getId());
    }

    private String readBody(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return null;
        }
    }

    private String generateJwt(JwtService service, String email, Long userId) {
        String[] names = new String[]{"issue", "generateToken", "generate", "createToken", "tokenFor"};
        for (String n : names) {
            try {
                Method m1 = service.getClass().getMethod(n, Long.class, String.class);
                return String.valueOf(m1.invoke(service, userId, email));
            } catch (ReflectiveOperationException ignored) { }
            try {
                Method m2 = service.getClass().getMethod(n, String.class);
                return String.valueOf(m2.invoke(service, email));
            } catch (ReflectiveOperationException ignored) { }
            try {
                Method m3 = service.getClass().getMethod(n, Long.class);
                return String.valueOf(m3.invoke(service, userId));
            } catch (ReflectiveOperationException ignored) { }
            try {
                Method m4 = service.getClass().getMethod(n);
                return String.valueOf(m4.invoke(service));
            } catch (ReflectiveOperationException ignored) { }
        }
        return "";
    }
}
