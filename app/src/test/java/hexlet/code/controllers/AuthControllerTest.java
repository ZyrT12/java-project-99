package hexlet.code.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private static void setId(User user, Long id) {
        try {
            Field f = User.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginOk() {
        String email = "user@example.com";
        String raw = "secret123";
        String hash = BCrypt.withDefaults().hashToString(10, raw.toCharArray());

        User u = new User();
        setId(u, 1L);
        u.setEmail(email);
        u.setPasswordHash(hash);

        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail(email)).thenReturn(Optional.of(u));

        Context ctx = mock(Context.class);
        when(ctx.bodyAsClass(Map.class)).thenReturn(Map.of("username", email, "password", raw));
        doReturn(ctx).when(ctx).result(anyString());

        AuthController controller = new AuthController(repo);
        controller.login(ctx);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(ctx).result(tokenCaptor.capture());
        String token = tokenCaptor.getValue();
        assertNotNull(token);
    }

    @Test
    void loginWrongPassword() {
        String email = "user@example.com";
        String good = "secret123";
        String bad = "wrong";
        String hash = BCrypt.withDefaults().hashToString(10, good.toCharArray());

        User u = new User();
        setId(u, 2L);
        u.setEmail(email);
        u.setPasswordHash(hash);

        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail(email)).thenReturn(Optional.of(u));

        Context ctx = mock(Context.class);
        when(ctx.bodyAsClass(Map.class)).thenReturn(Map.of("username", email, "password", bad));

        AuthController controller = new AuthController(repo);
        assertThrows(UnauthorizedResponse.class, () -> controller.login(ctx));
    }

    @Test
    void loginUserNotFound() {
        String email = "absent@example.com";
        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail(email)).thenReturn(Optional.empty());

        Context ctx = mock(Context.class);
        when(ctx.bodyAsClass(Map.class)).thenReturn(Map.of("username", email, "password", "x"));

        AuthController controller = new AuthController(repo);
        assertThrows(UnauthorizedResponse.class, () -> controller.login(ctx));
    }
}
