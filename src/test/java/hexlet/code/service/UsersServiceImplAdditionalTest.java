package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersServiceImplAdditionalTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserCreateDto buildCreateDto(String email) {
        return new UserCreateDto(email, "John", "Doe", "secret");
    }

    @Order(1)
    @Test
    void createReturnsDtoWithCreatedAtAndEncodesPassword() {
        String email = "coverage.create@example.com";
        UserResponseDto dto = usersService.create(buildCreateDto(email));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.email()).isEqualTo(email);
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.createdAt()).isNotNull();

        Optional<User> saved = userRepository.findByEmail(email);
        assertThat(saved).isPresent();
        assertThat(saved.get().getPasswordHash()).isNotEqualTo("secret");
        assertThat(passwordEncoder.matches("secret", saved.get().getPasswordHash())).isTrue();
    }

    @Order(2)
    @Test
    void updateAllowsChangingEmailAndKeepsOtherFieldsWhenNull() {
        String initialEmail = "coverage.update1@example.com";
        usersService.create(buildCreateDto(initialEmail));
        User saved = userRepository.findByEmail(initialEmail).orElseThrow();

        UserUpdateDto partial = new UserUpdateDto("coverage.update2@example.com", null, null, null);
        UserResponseDto updated = usersService.update(saved.getId(), partial);

        assertThat(updated.email()).isEqualTo("coverage.update2@example.com");

        User reloaded = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getEmail()).isEqualTo("coverage.update2@example.com");
        assertThat(reloaded.getFirstName()).isEqualTo("John");
        assertThat(reloaded.getLastName()).isEqualTo("Doe");
    }

    @Order(3)
    @Test
    void deleteRemovesUser() {
        String email = "coverage.delete@example.com";
        usersService.create(buildCreateDto(email));
        User saved = userRepository.findByEmail(email).orElseThrow();

        usersService.delete(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
        assertThat(userRepository.findByEmail(email)).isEmpty();
    }
}
