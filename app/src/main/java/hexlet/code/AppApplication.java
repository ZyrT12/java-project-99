package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import hexlet.code.controller.UsersController;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import hexlet.code.dto.users.UserCreateDto;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public final class AppApplication{

    private AppApplication() {

    }

    public static void main(String[] args) {
        int port = resolvePort();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UsersController usersController = new UsersController(userService);

        Javalin app = Javalin.create(cfg -> {
            cfg.showJavalinBanner = false;
            cfg.jsonMapper(new JavalinJackson(objectMapper));
        });

        app.get("/ping", ctx -> ctx.result("pong"));

        app.get("/api/users/{id}", usersController::getOne);
        app.get("/api/users", usersController::list);
        app.post("/api/users", usersController::create);
        app.put("/api/users/{id}", usersController::update);     // частичное обновление допускаем через PUT
        app.delete("/api/users/{id}", usersController::delete);

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
