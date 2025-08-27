package hexlet.code.mapper;

import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.model.User;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toResponseMapsFields() {
        User u = new User();
        u.setEmail("john@example.com");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setCreatedAt(LocalDate.now());

        UserResponseDto dto = mapper.toResponse(u);

        assertThat(dto.email()).isEqualTo("john@example.com");
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.createdAt()).isNotNull();
    }
}
