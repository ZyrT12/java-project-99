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

class JwtAuthMiddlewareTest {

    @Test
    void handleSetsAuthUserWhenBearerTokenValid() {
        String token = JwtService.createToken(7L, "user@example.com", "USER");
        Context ctx = mock(Context.class);
        doNothing().when(ctx).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
        org.mockito.Mockito.when(ctx.header("Authorization")).thenReturn("Bearer " + token);

        JwtAuthMiddleware middleware = new JwtAuthMiddleware();
        middleware.handle(ctx);

        ArgumentCaptor<AuthUser> captor = ArgumentCaptor.forClass(AuthUser.class);
        verify(ctx, times(1)).attribute(eq(JwtAuthMiddleware.ATTR_USER), captor.capture());
        AuthUser u = captor.getValue();
        assertEquals(7L, u.id());
        assertEquals("user@example.com", u.email());
        assertEquals("USER", u.role());
    }

    @Test
    void handleDoesNothingWhenNoAuthorizationHeader() {
        Context ctx = mock(Context.class);
        doNothing().when(ctx).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
        org.mockito.Mockito.when(ctx.header("Authorization")).thenReturn(null);

        JwtAuthMiddleware middleware = new JwtAuthMiddleware();
        middleware.handle(ctx);

        verify(ctx, times(0)).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
    }

    @Test
    void handleDoesNothingWhenTokenInvalid() {
        Context ctx = mock(Context.class);
        doNothing().when(ctx).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
        org.mockito.Mockito.when(ctx.header("Authorization")).thenReturn("Bearer invalid.token");

        JwtAuthMiddleware middleware = new JwtAuthMiddleware();
        middleware.handle(ctx);

        verify(ctx, times(0)).attribute(eq(JwtAuthMiddleware.ATTR_USER), any());
    }
}
