package hexlet.code.controller;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.validation.ConstraintViolationException;

public class UsersController {
    private final UserService service;
    public UsersController(UserService service) { this.service = service; }

    public void getOne(Context ctx) {
        try {
            var id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(service.get(id));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    public void list(Context ctx) {
        ctx.json(service.list());
    }

    public void create(Context ctx) {
        try {
            var dto = ctx.bodyValidator(UserCreateDto.class).get();
            var created = service.create(dto);
            ctx.status(HttpStatus.CREATED).json(created);
        } catch (ConstraintViolationException | IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(
                    java.util.Map.of("error", e.getMessage() == null ? "validation error" : e.getMessage())
            );
        }
    }

    public void update(Context ctx) {
        try {
            var id = Long.parseLong(ctx.pathParam("id"));
            var dto = ctx.bodyValidator(UserUpdateDto.class).get(); // частичное обновление
            var updated = service.update(id, dto);
            ctx.json(updated);
        } catch (IllegalArgumentException e) {
            var msg = e.getMessage();
            if ("not found".equals(msg)) {
                ctx.status(HttpStatus.NOT_FOUND);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST).json(java.util.Map.of("error", msg));
            }
        }
    }

    public void delete(Context ctx) {
        try {
            var id = Long.parseLong(ctx.pathParam("id"));
            service.delete(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }
}
