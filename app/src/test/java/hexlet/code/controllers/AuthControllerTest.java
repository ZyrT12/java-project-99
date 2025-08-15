package hexlet.code.controllers;

import hexlet.code.dto.auth.LoginRequest;
import hexlet.code.dto.auth.LoginResponse;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void loginOk() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtService jwt = new JwtService("test-secret");

        User u = new User();
        u.setEmail("jack@example.com");
        u.setPasswordHash(encoder.encode("qwerty"));
        when(repo.findByEmail("jack@example.com")).thenReturn(Optional.of(u));

        AuthController controller = new AuthController(repo, encoder, jwt);

        LoginRequest req = new LoginRequest();
        req.setUsername("jack@example.com");
        req.setPassword("qwerty");

        ResponseEntity<LoginResponse> resp = controller.login(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getToken());
    }

    @Test
    void loginWrongPassword() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtService jwt = new JwtService("test-secret");

        User u = new User();
        u.setEmail("jack@example.com");
        u.setPasswordHash(encoder.encode("qwerty"));
        when(repo.findByEmail("jack@example.com")).thenReturn(Optional.of(u));

        AuthController controller = new AuthController(repo, encoder, jwt);

        LoginRequest req = new LoginRequest();
        req.setUsername("jack@example.com");
        req.setPassword("wrong");

        assertThrows(ResponseStatusException.class, () -> controller.login(req));
    }

    @Test
    void loginUserNotFound() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtService jwt = new JwtService("test-secret");

        when(repo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        AuthController controller = new AuthController(repo, encoder, jwt);

        LoginRequest req = new LoginRequest();
        req.setUsername("unknown@example.com");
        req.setPassword("any");

        assertThrows(ResponseStatusException.class, () -> controller.login(req));
    }
}
