package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hexlet.code.controllers.AuthController;
import hexlet.code.controllers.UsersController;
import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtAuthMiddleware;
import hexlet.code.security.Role;
import hexlet.code.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "hexlet.code")
public class AppApplication {

    private AppApplication() {}

    public static void main(String[] args) {
        ConfigurableApplicationContext spring =
                new SpringApplicationBuilder(AppApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);

        UserRepository userRepository = spring.getBean(UserRepository.class);
        UserService userService = spring.getBean(UserService.class);

        UsersController usersController = new UsersController(userService);
        AuthController authController = new AuthController(userRepository);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Javalin app = Javalin.create(cfg -> {
            cfg.showJavalinBanner = false;

            cfg.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }));

            cfg.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "static";
                staticFiles.location = Location.CLASSPATH;
            });
        });

        app.before("/api/*", new JwtAuthMiddleware());

        app.beforeMatched(ctx -> {
            var roles = ctx.routeRoles();
            if (roles.isEmpty() || roles.contains(Role.ANYONE)) {
                return;
            }

            var au = JwtAuthMiddleware.getAuthUser(ctx);
            if (au == null && roles.contains(Role.AUTHENTICATED)) {
                throw new UnauthorizedResponse("Unauthorized");
            }
        });

        app.get("/ping", ctx -> ctx.result("pong"), Role.ANYONE);

        app.get("/", ctx -> ctx.redirect("/index.html"), Role.ANYONE);

        app.post("/api/login", authController::login, Role.ANYONE);

        app.put("/api/users/{id}", ctx -> {
            var au = JwtAuthMiddleware.getAuthUser(ctx);
            long targetId = Long.parseLong(ctx.pathParam("id"));
            if (au == null) {
                ctx.status(401).result("Unauthorized");
                return;
            }
            if (!au.isAdmin() && au.id() != targetId) {
                ctx.status(403).result("Forbidden");
                return;
            }

        }, Role.AUTHENTICATED);

        app.patch("/api/users/{id}", ctx -> {
            var au = JwtAuthMiddleware.getAuthUser(ctx);
            long targetId = Long.parseLong(ctx.pathParam("id"));
            if (au == null) {
                ctx.status(401).result("Unauthorized");
                return;
            }
            if (!au.isAdmin() && au.id() != targetId) {
                ctx.status(403).result("Forbidden");
                return;
            }
        }, Role.AUTHENTICATED);

        app.delete("/api/users/{id}", ctx -> {
            var au = JwtAuthMiddleware.getAuthUser(ctx);
            long targetId = Long.parseLong(ctx.pathParam("id"));
            if (au == null) {
                ctx.status(401).result("Unauthorized");
                return;
            }
            if (!au.isAdmin() && au.id() != targetId) {
                ctx.status(403).result("Forbidden");
                return;
            }

        }, Role.AUTHENTICATED);

        int port = resolvePort();
        app.start(port);

        seedAdmin(userService);
    }

    private static int resolvePort() {
        String raw = System.getenv("PORT");
        if (raw == null || raw.isBlank()) {
            return 8080;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return 8080;
        }
    }

    private static void seedAdmin(UserService users) {
        try {
            users.create(new UserCreateDto(
                    "hexlet@example.com",
                    "Admin",
                    "Root",
                    "qwerty"
            ));
            System.out.println("Admin user created: hexlet@example.com / qwerty");
        } catch (Exception ignored) {
        }
    }
}
