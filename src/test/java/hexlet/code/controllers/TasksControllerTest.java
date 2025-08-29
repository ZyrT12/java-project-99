package hexlet.code.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String bearer;

    @BeforeEach
    void setUp() {
        String email = "mvc@example.com";
        String pass = "pass";
        User u = userRepository.findByEmail(email).orElseGet(() -> {
            User nu = new User();
            nu.setEmail(email);
            nu.setPasswordHash(passwordEncoder.encode(pass));
            nu.setFirstName("M");
            nu.setLastName("V");
            return userRepository.save(nu);
        });
        bearer = "Bearer " + jwtService.issue(u.getId(), u.getEmail());
    }

    @Test
    void crudFlow() throws Exception {
        mockMvc.perform(get("/api/tasks/999999")
                        .header("Authorization", bearer)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
