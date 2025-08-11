package hexlet.code.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.dto.users.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserRepository repo;

    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserResponseDto create(UserCreateDto dto) {
        // Валидация DTO
        Set<ConstraintViolation<UserCreateDto>> violations = VALIDATOR.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Optional<User> existing = repo.findByEmail(dto.email());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("email already exists");
        }

        User u = new User();
        u.setEmail(dto.email());
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setPasswordHash(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));

        repo.save(u);
        return UserMapper.toDto(u);
    }

    public UserResponseDto get(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        return UserMapper.toDto(u);
    }

    public List<UserResponseDto> list() {
        return repo.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserResponseDto update(Long id, UserUpdateDto dto) {
        User u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        if (dto.email() != null && !dto.email().isBlank()) {
            Optional<User> sameEmail = repo.findByEmail(dto.email());
            if (sameEmail.isPresent() && !sameEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("email already exists");
            }
            u.setEmail(dto.email());
        }

        if (dto.firstName() != null) {
            u.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            u.setLastName(dto.lastName());
        }
        if (dto.password() != null) {
            if (dto.password().length() < 3) {
                throw new IllegalArgumentException("password too short");
            }
            u.setPasswordHash(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        }

        repo.save(u);
        return UserMapper.toDto(u);
    }

    public void delete(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        repo.delete(u);
    }

    public static Map<String, String> errorBody(String message) {
        String msg = (message == null || message.isBlank()) ? "validation error" : message;
        return Map.of("error", msg);
    }
}
