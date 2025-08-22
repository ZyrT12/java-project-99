package hexlet.code.controllers;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.service.UsersService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end
    ) {
        List<UserResponseDto> all = service.getAll();
        int total = all.size();

        int s = start != null ? Math.max(0, start) : 0;
        int e = end != null ? Math.min(end, total) : total;
        if (e < s) {
            e = s;
        }

        List<UserResponseDto> slice = all.subList(Math.min(s, total), Math.min(e, total));

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));

        return new ResponseEntity<>(slice, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public UserResponseDto show(@PathVariable long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto data) {
        UserResponseDto created = service.create(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/users/" + created.id()));
        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable long id, @RequestBody @Valid UserUpdateDto data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
