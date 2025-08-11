package hexlet.code.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;

import java.util.Map;

public final class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(Context ctx) {
        var body = ctx.bodyAsClass(Map.class);
        var username = (String) body.get("username");
        var password = (String) body.get("password");

        if (username == null || password == null) {
            throw new BadRequestResponse("username and password are required");
        }

        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedResponse("Invalid credentials"));

        var hash = user.getPasswordHash();
        var ok = BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
        if (!ok) {
            throw new UnauthorizedResponse("Invalid credentials");
        }

        var token = JwtService.createToken(user.getId(), user.getEmail(), "USER"); // роль по умолчанию
        ctx.result(token);
    }
}
