package hexlet.code.dto.users;

import hexlet.code.model.User;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toDto(User u) {
        return new UserResponseDto(
                u.getId(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                toLocalDate(u.getCreatedAt())
        );
    }

    private static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();
    }
}
