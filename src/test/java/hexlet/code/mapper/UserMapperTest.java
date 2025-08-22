package hexlet.code.mapper;

import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.model.User;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toDtoMapsFields() {
        User u = new User();
        u.setEmail("john.doe@example.com");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setCreatedAt(LocalDate.of(2024, 1, 2));

        UserResponseDto dto = hexlet.code.dto.users.UserMapper.toDto(u);

        assertThat(dto.id()).isNull();
        assertThat(dto.email()).isEqualTo("john.doe@example.com");
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.createdAt()).isEqualTo(LocalDate.of(2024, 1, 2));
    }
}
