package hexlet.code.controllers;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository repository;

    public UsersController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> index(
            @RequestParam(name = "_page", defaultValue = "1") int page,
            @RequestParam(name = "_perPage", defaultValue = "25") int perPage,
            HttpServletResponse response
    ) {
        int pageIndex = Math.max(page - 1, 0);
        Page<User> p = repository.findAll(PageRequest.of(pageIndex, perPage));
        response.setHeader("X-Total-Count", String.valueOf(p.getTotalElements()));
        response.setHeader("Access-Control-Expose-Headers", "X-Total-Count, Authorization, Content-Range");
        List<UserResponseDto> body = p.getContent().stream().map(this::toDto).toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> show(@PathVariable Long id) {
        Optional<User> opt = repository.findById(id);
        return opt.map(user -> ResponseEntity.ok(toDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto) {
        User u = new User();
        u.setEmail(dto.email());
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setPasswordHash(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        User saved = repository.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(toDto(saved));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> patch(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        Optional<User> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User u = opt.get();
        if (dto.email() != null) {
            u.setEmail(dto.email());
        }
        if (dto.firstName() != null) {
            u.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            u.setLastName(dto.lastName());
        }
        if (dto.password() != null) {
            u.setPasswordHash(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        }
        User saved = repository.save(u);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        return patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<User> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        repository.delete(opt.get());
        return ResponseEntity.noContent().build();
    }

    private UserResponseDto toDto(User u) {
        return new UserResponseDto(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getCreatedAt());
    }
}
