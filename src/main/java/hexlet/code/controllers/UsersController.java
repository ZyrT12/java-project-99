package hexlet.code.controllers;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.security.OwnershipService;
import hexlet.code.service.UsersService;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {

    private final UsersService usersService;
    private final OwnershipService ownershipService;

    public UsersController(UsersService usersService, OwnershipService ownershipService) {
        this.usersService = usersService;
        this.ownershipService = ownershipService;
    }

    @GetMapping("/{id}")
    public UserResponseDto show(@PathVariable Long id) {
        return usersService.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping
    public Iterable<UserResponseDto> index() {
        return usersService.getAll();
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody UserCreateDto dto) {
        return usersService.create(dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@ownershipService.isSelf(#id)")
    public UserResponseDto update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        return usersService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ownershipService.isSelf(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usersService.delete(id);
    }
}
