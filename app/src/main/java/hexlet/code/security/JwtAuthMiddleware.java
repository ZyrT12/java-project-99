package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class JwtAuthMiddleware implements Handler {
    public static final String ATTR_USER = "authUser";

    @Override
    public void handle(Context ctx) {
        var auth = ctx.header("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            JwtService.verify(auth.substring(7).trim())
                    .ifPresent(jwt -> ctx.attribute(ATTR_USER, toAuthUser(jwt)));
        }
    }
    private static AuthUser toAuthUser(DecodedJWT jwt) {
        return new AuthUser(jwt.getClaim("uid").asLong(),
                jwt.getClaim("email").asString(),
                jwt.getClaim("role").asString());
    }
    public static AuthUser getAuthUser(Context ctx) {
        return ctx.attribute(ATTR_USER);
    }
}

