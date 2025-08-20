package hexlet.code.dto;

import hexlet.code.dto.auth.LoginResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void gettersAndSetters() {
        LoginResponse dto = new LoginResponse();
        dto.setToken("abc");
        assertThat(dto.getToken()).isEqualTo("abc");
    }
}
