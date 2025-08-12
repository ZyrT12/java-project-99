package hexlet.code.controllers;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto dto) {
        try {
            var created = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", msgOr("validation error", e.getMessage())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", msgOr("bad request", e.getMessage())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") long id) {
        try {
            var dto = service.get(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePut(@PathVariable("id") long id,
                                       @Valid @RequestBody UserUpdateDto dto) {
        return doUpdate(id, dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePatch(@PathVariable("id") long id,
                                         @Valid @RequestBody UserUpdateDto dto) {
        return doUpdate(id, dto);
    }

    private ResponseEntity<?> doUpdate(long id, UserUpdateDto dto) {
        try {
            var updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            var msg = e.getMessage();
            if ("not found".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", msgOr("bad request", msg)));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", msgOr("validation error", e.getMessage())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private static String msgOr(String fallback, String msg) {
        return (msg == null || msg.isBlank()) ? fallback : msg;
    }
}
