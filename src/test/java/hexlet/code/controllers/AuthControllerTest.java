package hexlet.code.controllers;

import hexlet.code.dto.auth.LoginRequest;
import hexlet.code.dto.auth.LoginResponse;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void loginOk() {
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        JwtService jwt = Mockito.mock(JwtService.class);

        Authentication auth = new UsernamePasswordAuthenticationToken("john@example.com", "secret");
        when(authManager.authenticate(ArgumentMatchers
                .any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        User user = new User();
        user.setId(100L);
        user.setEmail("john@example.com");
        user.setPasswordHash("$2a$10$hash");
        when(repo.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        when(jwt.issue(ArgumentMatchers.anyLong(),
                ArgumentMatchers.eq("john@example.com"))).thenReturn("token-123");

        AuthController controller = new AuthController(authManager, jwt, repo);

        LoginRequest req = new LoginRequest();
        req.setEmail("john@example.com");
        req.setPassword("secret");

        ResponseEntity<LoginResponse> resp = controller.login(req);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getToken()).isEqualTo("token-123");
    }

    @Test
    void loginUnauthorizedWhenBadCredentials() {
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        JwtService jwt = Mockito.mock(JwtService.class);

        when(authManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad creds"));

        AuthController controller = new AuthController(authManager, jwt, repo);

        LoginRequest req = new LoginRequest();
        req.setEmail("john@example.com");
        req.setPassword("wrong");

        assertThrows(org.springframework.security.core.AuthenticationException.class, () -> controller.login(req));
    }
}
