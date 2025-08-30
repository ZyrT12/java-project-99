package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.model.User;
import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UserResponseDto> getAll();
    Optional<UserResponseDto> findById(Long id);
    UserResponseDto get(Long id);
    UserResponseDto create(UserCreateDto dto);
    UserResponseDto update(Long id, UserUpdateDto dto);
    void delete(Long id);
    User getById(Long id);
}
