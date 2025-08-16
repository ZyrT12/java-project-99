package hexlet.code.dto.users;

public record UserUpdateDto(
        String email,
        String firstName,
        String lastName,
        String password
) {

}
