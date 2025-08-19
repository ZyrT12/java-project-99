package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import java.util.List;
import java.util.Optional;

public interface UsersService {
    UserResponseDto create(UserCreateDto dto);
    List<UserResponseDto> getAll();
    Optional<UserResponseDto> findById(Long id);
    UserResponseDto getById(Long id);
    UserResponseDto update(Long id, UserUpdateDto dto);
    void delete(Long id);
}
