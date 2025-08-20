package hexlet.code.repository;

import hexlet.code.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailReturnsSavedUser() {
        User u = new User();
        u.setEmail("repo.find@example.com");
        u.setFirstName("X");
        u.setLastName("Y");
        u.setPasswordHash("ph");
        userRepository.save(u);

        Optional<User> found = userRepository.findByEmail("repo.find@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("repo.find@example.com");
    }
}
