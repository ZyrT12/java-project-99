package hexlet.code.dto.users;

import hexlet.code.model.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toDto(User u) {
        return new UserResponseDto(
                u.getId(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getCreatedAt()
        );
    }
}
