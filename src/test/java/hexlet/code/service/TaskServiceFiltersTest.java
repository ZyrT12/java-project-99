package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceFiltersTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    private Long statusNewId;
    private Long statusProgId;
    private Long assigneeId;
    private Long labelBackendId;
    private String suffix;

    @BeforeEach
    void setUp() {
        suffix = UUID.randomUUID().toString();

        TaskStatus s1 = new TaskStatus();
        s1.setName("New " + suffix);
        s1.setSlug("new-" + suffix);
        statusNewId = taskStatusRepository.save(s1).getId();

        TaskStatus s2 = new TaskStatus();
        s2.setName("In progress " + suffix);
        s2.setSlug("progress-" + suffix);
        statusProgId = taskStatusRepository.save(s2).getId();

        User u = new User();
        u.setEmail("alpha+" + suffix + "@example.com");
        u.setFirstName("Alpha");
        u.setLastName("User");
        trySetPassword(u, "password123");
        assigneeId = userRepository.save(u).getId();

        Label l1 = new Label();
        l1.setName("backend-" + suffix);
        labelBackendId = labelRepository.save(l1).getId();

        TaskUpsertDto a = new TaskUpsertDto();
        a.setTitle("Alpha feature " + suffix);
        a.setDescription("Alpha desc");
        a.setTaskStatusId(statusNewId);
        a.setExecutorId(assigneeId);
        a.setLabelIds(List.of(labelBackendId));
        taskService.create(a);

        TaskUpsertDto b = new TaskUpsertDto();
        b.setTitle("Beta task " + suffix);
        b.setDescription("Beta desc");
        b.setTaskStatusId(statusProgId);
        taskService.create(b);
    }

    @Test
    void filterByAssignee() {
        List<TaskResponseDto> res = taskService.list(null, assigneeId, null, null);
        assertEquals(1, res.size());
        assertEquals(assigneeId, res.get(0).getExecutorId());
    }

    @Test
    void filterByStatusSlug() {
        List<TaskResponseDto> res = taskService.list(null, null, "progress-" + suffix, null);
        assertEquals(1, res.size());
        assertEquals("Beta task " + suffix, res.get(0).getTitle());
    }

    @Test
    void filterByLabel() {
        List<TaskResponseDto> res = taskService.list(null, null, null, labelBackendId);
        assertEquals(1, res.size());
        assertEquals("Alpha feature " + suffix, res.get(0).getTitle());
    }

    @Test
    void updateClearsAssigneeOnZero() {
        TaskUpsertDto dto = new TaskUpsertDto();
        dto.setTitle("Gamma " + suffix);
        dto.setTaskStatusId(statusNewId);
        dto.setExecutorId(assigneeId);
        Long id = taskService.create(dto).getId();

        TaskUpsertDto upd = new TaskUpsertDto();
        upd.setExecutorId(0L);
        TaskResponseDto after = taskService.update(id, upd);
        assertNull(after.getExecutorId());
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
