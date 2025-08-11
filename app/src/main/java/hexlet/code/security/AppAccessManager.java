package hexlet.code.security;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AppAccessManager implements AccessManager {
    @Override
    public void manage(Handler handler, Context ctx, Set<? extends RouteRole> roles) throws Exception {
        if (roles.isEmpty() || roles.contains(Role.ANYONE)) {
            handler.handle(ctx); // публично
            return;
        }
        var user = JwtAuthMiddleware.getAuthUser(ctx);
        if (user == null) {
            throw new UnauthorizedResponse("Unauthorized");
        }
        if (roles.contains(Role.ADMIN) && !user.isAdmin()) {
            ctx.status(403).result("Forbidden");
            return;
        }
        handler.handle(ctx);
    }
}
