package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/users возвращает тот же список, что и в БД")
    void listUsersMatchesDatabase() throws Exception {
        MvcResult resp = mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = resp.getResponse().getContentAsString(StandardCharsets.UTF_8);

        List<UserResponseDto> fromApi = objectMapper.readValue(json, new TypeReference<List<UserResponseDto>>() { });
        List<User> fromDb = userRepository.findAll();

        List<String> apiEmails = fromApi.stream()
                .sorted(Comparator.comparing(UserResponseDto::id))
                .map(UserResponseDto::email)
                .collect(Collectors.toList());

        List<String> dbEmails = fromDb.stream()
                .sorted(Comparator.comparing(User::getId))
                .map(User::getEmail)
                .collect(Collectors.toList());

        assertThat(apiEmails).isEqualTo(dbEmails);
        assertThat(fromApi).hasSize(fromDb.size());
    }
}
