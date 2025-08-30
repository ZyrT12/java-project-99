package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UserRepository userRepository,
                            UserMapper userMapper,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toResponse);
    }

    @Override
    public UserResponseDto get(Long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDto create(UserCreateDto dto) {
        User user = userMapper.fromCreate(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userMapper.updateFromDto(dto, user);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
