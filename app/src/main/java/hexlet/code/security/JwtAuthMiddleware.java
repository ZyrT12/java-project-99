package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthMiddleware implements Handler {
    public static final String ATTR_USER = "authUser";

    private final JwtService jwtService;

    public JwtAuthMiddleware(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void handle(Context ctx) {
        String auth = ctx.header("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            Optional<DecodedJWT> jwt = jwtService.verify(auth.substring(7).trim());
            jwt.ifPresent(j -> ctx.attribute(ATTR_USER, toAuthUser(j)));
        }
    }

    private AuthUser toAuthUser(DecodedJWT jwt) {
        Long id = jwt.getClaim("uid").asLong();
        String email = jwt.getSubject();
        return new AuthUser(id, email);
    }

    public static class AuthUser {
        private final Long id;
        private final String email;

        public AuthUser(Long id, String email) {
            this.id = id;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }
}
