package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskMappingHelper;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskStatusRepository statusRepository;
    private UserRepository userRepository;
    private LabelRepository labelRepository;

    private TaskMapper taskMapper;
    private TaskMappingHelper taskMappingHelper;

    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        statusRepository = Mockito.mock(TaskStatusRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        labelRepository = Mockito.mock(LabelRepository.class);

        taskMapper = Mappers.getMapper(TaskMapper.class);
        taskMappingHelper = new TaskMappingHelper(statusRepository, userRepository, labelRepository);

        service = new TaskServiceImpl(
                taskRepository,
                statusRepository,
                userRepository,
                labelRepository,
                taskMapper,
                taskMappingHelper
        );
    }

    @Test
    void createWithStatusSlugAssigneeAndLabels() {
        TaskUpsertDto dto = new TaskUpsertDto();
        dto.setTitle("Make tests");
        dto.setContent("Body");
        dto.setStatus("in_progress");
        dto.setAssigneeId(10L);
        dto.setLabelIds(List.of(100L, 200L));

        TaskStatus inProgress = new TaskStatus();
        inProgress.setSlug("in_progress");
        inProgress.setName("In progress");
        when(statusRepository.findBySlug("in_progress")).thenReturn(Optional.of(inProgress));
        when(statusRepository.findByNameIgnoreCase("new")).thenReturn(Optional.empty());

        User assignee = mock(User.class);
        when(assignee.getId()).thenReturn(10L);
        when(userRepository.findById(10L)).thenReturn(Optional.of(assignee));

        Label l1 = mock(Label.class);
        when(l1.getId()).thenReturn(100L);
        Label l2 = mock(Label.class);
        when(l2.getId()).thenReturn(200L);
        when(labelRepository.findAllById(List.of(100L, 200L))).thenReturn(List.of(l1, l2));

        when(taskRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TaskResponseDto created = service.create(dto);

        assertThat(created.getTitle()).isEqualTo("Make tests");
        assertThat(created.getAssigneeId()).isEqualTo(10L);
        assertThat(created.getTaskLabelIds()).containsExactlyInAnyOrder(100L, 200L);
    }
}
