package hexlet.code.controllers;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.security.OwnershipService;
import hexlet.code.service.UsersService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final OwnershipService ownershipService;

    public UsersController(UsersService usersService, OwnershipService ownershipService) {
        this.usersService = usersService;
        this.ownershipService = ownershipService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end,
            @RequestParam(name = "emailCont", required = false) String emailCont
    ) {
        List<UserResponseDto> all = usersService.getAll();
        int total = all.size();
        int from = start == null ? 0 : Math.max(0, start);
        int to = end == null ? total : Math.min(total, end);
        if (from > to) {
            from = to;
        }
        List<UserResponseDto> page = all.subList(from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    public UserResponseDto show(@PathVariable Long id) {
        return usersService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Valid @RequestBody UserCreateDto dto) {
        return usersService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ownershipService.isSelf(#id)")
    public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        return usersService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ownershipService.isSelf(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usersService.delete(id);
    }
}
