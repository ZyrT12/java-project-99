package hexlet.code.dto.users;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) {

}