package hexlet.code.security;

import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthMiddlewareTest {

    @Test
    void handleSetsAuthUserWhenBearerTokenValid() {
        JwtService jwtService = new JwtService("test-secret");
        String token = jwtService.issue(7L, "user@example.com");

        Context ctx = mock(Context.class);
        doNothing().when(ctx).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
        when(ctx.header("Authorization")).thenReturn("Bearer " + token);

        JwtAuthMiddleware middleware = new JwtAuthMiddleware(jwtService);
        middleware.handle(ctx);

        ArgumentCaptor<JwtAuthMiddleware.AuthUser> captor = ArgumentCaptor.forClass(JwtAuthMiddleware.AuthUser.class);
        verify(ctx, times(1)).attribute(eq(JwtAuthMiddleware.ATTR_USER), captor.capture());

        JwtAuthMiddleware.AuthUser u = captor.getValue();
        assertEquals(7L, u.getId());
        assertEquals("user@example.com", u.getEmail());
    }
}
