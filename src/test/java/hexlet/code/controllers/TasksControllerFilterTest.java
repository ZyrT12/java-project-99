package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(username = "tester", roles = "USER")
class TasksControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long statusId;
    private Long labelId;
    private Long assigneeId;
    private String suffix;

    @BeforeEach
    void init() {
        suffix = UUID.randomUUID().toString();

        TaskStatus s = new TaskStatus();
        s.setName("New " + suffix);
        s.setSlug("new-" + suffix);
        statusId = taskStatusRepository.save(s).getId();

        Label l = new Label();
        l.setName("urgent-" + suffix);
        labelId = labelRepository.save(l).getId();

        User u = new User();
        u.setEmail("rest+" + suffix + "@example.com");
        u.setFirstName("Rest");
        u.setLastName("Assured");
        trySetPassword(u, "pwd12345");
        assigneeId = userRepository.save(u).getId();

        TaskUpsertDto a = new TaskUpsertDto();
        a.setTitle("Filter one " + suffix);
        a.setTaskStatusId(statusId);
        a.setExecutorId(assigneeId);
        a.setLabelIds(List.of(labelId));
        taskService.create(a);

        TaskUpsertDto b = new TaskUpsertDto();
        b.setTitle("Other " + suffix);
        b.setTaskStatusId(statusId);
        taskService.create(b);
    }

    @Test
    void listByTitleCont() throws Exception {
        mockMvc.perform(get("/api/tasks").param("titleCont", "filter one"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Filter one " + suffix)));
    }

    @Test
    void listByAssignee() throws Exception {
        mockMvc.perform(get("/api/tasks").param("assigneeId", String.valueOf(assigneeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].assignee_id", is(assigneeId.intValue())));
    }

    @Test
    void listByStatusSlug() throws Exception {
        mockMvc.perform(get("/api/tasks").param("status", "new-" + suffix))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void listByLabel() throws Exception {
        mockMvc.perform(get("/api/tasks").param("labelId", String.valueOf(labelId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Filter one " + suffix)));
    }

    private void trySetPassword(User u, String raw) {
        if (invokeSetter(u, "setPassword", raw)) {
            return;
        }
        if (invokeSetter(u, "setPasswordDigest", raw)) {
            return;
        }
        invokeSetter(u, "setPasswordHash", raw);
    }

    private boolean invokeSetter(User u, String method, String value) {
        try {
            Method m = User.class.getMethod(method, String.class);
            m.invoke(u, value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
