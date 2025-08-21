package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserResponseDto create(UserCreateDto dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id).map(this::toResponse);
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return toResponse(user);
    }

    @Transactional
    @Override
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                toLocalDate(user.getCreatedAt())
        );
    }

    private static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();
    }
}
