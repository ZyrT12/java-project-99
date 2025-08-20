package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskStatusRepository statusRepository;

    private Long statusId;
    private String statusSlug;

    @BeforeEach
    void setUp() {
        TaskStatus status = new TaskStatus();
        status.setName("In progress");
        status.setSlug("in_progress");
        statusId = statusRepository.save(status).getId();
        statusSlug = "in_progress";
    }

    @Test
    void createUpdateList() {
        TaskUpsertDto createDto = new TaskUpsertDto();
        createDto.setTitle("ServiceTask");
        createDto.setDescription("Body");
        createDto.setTaskStatusId(statusId);
        TaskResponseDto created = taskService.create(createDto);
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals("ServiceTask", created.getTitle());
        Assertions.assertEquals(statusSlug, created.getStatus());

        TaskUpsertDto updateDto = new TaskUpsertDto();
        updateDto.setTitle("ServiceTask2");
        updateDto.setDescription("Body2");
        updateDto.setTaskStatusId(statusId);
        TaskResponseDto updated = taskService.update(created.getId(), updateDto);
        Assertions.assertEquals("ServiceTask2", updated.getTitle());
        Assertions.assertEquals(statusSlug, updated.getStatus());

        List<TaskResponseDto> all = taskService.list();
        Assertions.assertTrue(all.stream().anyMatch(t -> t.getId().equals(created.getId())));
    }
}
