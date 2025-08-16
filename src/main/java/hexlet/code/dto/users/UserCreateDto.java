package hexlet.code.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @Email @NotBlank String email,
        String firstName,
        String lastName,
        @NotBlank @Size(min = 3) String password
) {

}
