package hexlet.code.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class UserModelTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createdAtIsSetAndPresentInJson() throws Exception {
        User u = new User();
        u.setEmail("model.check@example.com");
        u.setFirstName("A");
        u.setLastName("B");
        u.setPasswordHash("x");
        User saved = userRepository.save(u);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isInstanceOf(LocalDate.class);

        String json = objectMapper.writeValueAsString(saved);
        assertThat(json).contains("createdAt");
    }
}
