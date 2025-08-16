package hexlet.code.security;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANYONE, AUTHENTICATED, ADMIN
}
