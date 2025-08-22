package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskStatusRepository statusRepository;
    private UserRepository userRepository;
    private LabelRepository labelRepository;
    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        statusRepository = mock(TaskStatusRepository.class);
        userRepository = mock(UserRepository.class);
        labelRepository = mock(LabelRepository.class);
        service = new TaskServiceImpl(taskRepository, statusRepository, userRepository, labelRepository);
    }

    @Test
    void createWithStatusSlugAssigneeAndLabels() {
        TaskUpsertDto dto = new TaskUpsertDto();
        dto.setTitle("Make tests");
        dto.setContent("Body");
        dto.setStatus("in-progress");
        dto.setAssigneeId(7L);
        dto.setLabelIds(List.of(3L, 4L));

        TaskStatus status = mock(TaskStatus.class);
        when(status.getSlug()).thenReturn("in-progress");
        when(statusRepository.findBySlug("in-progress")).thenReturn(Optional.of(status));

        User user = mock(User.class);
        when(user.getId()).thenReturn(7L);
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));

        Label l1 = mock(Label.class);
        when(l1.getId()).thenReturn(3L);
        Label l2 = mock(Label.class);
        when(l2.getId()).thenReturn(4L);
        when(labelRepository.findAllById(List.of(3L, 4L))).thenReturn(List.of(l1, l2));

        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponseDto res = service.create(dto);

        assertThat(res.getTitle()).isEqualTo("Make tests");
        assertThat(res.getStatus()).isEqualTo("in-progress");
        assertThat(res.getAssigneeId()).isEqualTo(7L);
        assertThat(res.getTaskLabelIds()).containsExactlyInAnyOrder(3L, 4L);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());
        Task saved = captor.getValue();
        assertThat(saved.getTaskStatus().getSlug()).isEqualTo("in-progress");
        assertThat(saved.getAssignee().getId()).isEqualTo(7L);
        Set<Long> ids = new HashSet<>();
        saved.getLabels().forEach(l -> ids.add(l.getId()));
        assertThat(ids).containsExactlyInAnyOrder(3L, 4L);
    }

    @Test
    void createUsesDefaultStatusWhenNoneProvided() {
        TaskUpsertDto dto = new TaskUpsertDto();
        dto.setTitle("No status");

        TaskStatus def = mock(TaskStatus.class);
        when(def.getSlug()).thenReturn("new");
        when(statusRepository.findBySlug("new")).thenReturn(Optional.of(def));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponseDto res = service.create(dto);

        assertThat(res.getStatus()).isEqualTo("new");
        verify(statusRepository, times(1)).findBySlug("new");
    }

    @Test
    void updateClearsAssigneeWhenExecutorIsZero() {
        Task existing = new Task();
        User assignee = mock(User.class);
        when(assignee.getId()).thenReturn(9L);
        existing.setAssignee(assignee);
        when(taskRepository.findById(200L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpsertDto patch = new TaskUpsertDto();
        patch.setExecutorId(0L);

        TaskResponseDto res = service.update(200L, patch);

        assertThat(res.getAssigneeId()).isNull();
        assertThat(existing.getAssignee()).isNull();
    }

    @Test
    void updateChangesStatusByIdAndLabels() {
        Task existing = new Task();
        existing.setLabels(new HashSet<>());
        when(taskRepository.findById(300L)).thenReturn(Optional.of(existing));

        TaskStatus st = mock(TaskStatus.class);
        when(st.getSlug()).thenReturn("done");
        when(statusRepository.findById(5L)).thenReturn(Optional.of(st));

        Label l = mock(Label.class);
        when(l.getId()).thenReturn(11L);
        when(labelRepository.findAllById(List.of(11L))).thenReturn(List.of(l));

        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpsertDto patch = new TaskUpsertDto();
        patch.setTaskStatusId(5L);
        patch.setTaskLabelIds(List.of(11L));

        TaskResponseDto res = service.update(300L, patch);

        assertThat(res.getStatus()).isEqualTo("done");
        assertThat(res.getTaskLabelIds()).containsExactly(11L);
    }

    @Test
    void listAppliesAllFilters() {
        TaskStatus st = mock(TaskStatus.class);
        when(st.getSlug()).thenReturn("in-progress");

        User u1 = mock(User.class);
        when(u1.getId()).thenReturn(1L);

        Label lab = mock(Label.class);
        when(lab.getId()).thenReturn(77L);

        Task t1 = new Task();
        t1.setTitle("Alpha Feature");
        t1.setTaskStatus(st);
        t1.setAssignee(u1);
        t1.setLabels(Set.of(lab));

        Task t2 = new Task();
        t2.setTitle("Beta");
        t2.setTaskStatus(st);
        t2.setAssignee(null);
        t2.setLabels(Set.of());

        List<Task> all = new ArrayList<>();
        all.add(t1);
        all.add(t2);
        when(taskRepository.findAll()).thenReturn(all);

        List<TaskResponseDto> filtered = service.list("Alpha", 1L, "in-progress", 77L);

        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).getTitle()).isEqualTo("Alpha Feature");
    }

    @Test
    void deleteThrowsWhenNotExists() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);
        Assertions.assertThrows(NoSuchElementException.class, () -> service.delete(999L));
    }

    @Test
    void getReturnsMappedDto() {
        TaskStatus st = mock(TaskStatus.class);
        when(st.getSlug()).thenReturn("new");

        User u = mock(User.class);
        when(u.getId()).thenReturn(55L);

        Label l = mock(Label.class);
        when(l.getId()).thenReturn(66L);

        Task t = new Task();
        t.setTitle("Get task");
        t.setContent("Text");
        t.setTaskStatus(st);
        t.setAssignee(u);
        t.setLabels(Set.of(l));

        when(taskRepository.findById(eq(500L))).thenReturn(Optional.of(t));

        TaskResponseDto dto = service.get(500L);

        assertThat(dto.getTitle()).isEqualTo("Get task");
        assertThat(dto.getStatus()).isEqualTo("new");
        assertThat(dto.getAssigneeId()).isEqualTo(55L);
        assertThat(dto.getTaskLabelIds()).containsExactly(66L);
    }
}
