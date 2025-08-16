package hexlet.code.dto.users;

import java.time.LocalDate;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate createdAt
) {

}
