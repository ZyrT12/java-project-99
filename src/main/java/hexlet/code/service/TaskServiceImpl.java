package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final LabelRepository labelRepo;
    private final TaskMapper mapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepo,
                           TaskStatusRepository statusRepo,
                           UserRepository userRepo,
                           LabelRepository labelRepo,
                           TaskMapper mapper) {
        this.taskRepo = taskRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.labelRepo = labelRepo;
        this.mapper = mapper;
    }

    @Override
    public TaskResponseDto create(TaskUpsertDto dto) {
        Task task = new Task();
        if (mapper != null) {
            mapper.updateFromDto(dto, task);
        }
        applyBasicFields(dto, task);
        applyStatusOnCreate(dto, task);
        applyAssignee(dto, task);
        applyLabels(dto, task);
        Task saved = taskRepo.save(task);
        return mapToResponse(saved);
    }

    @Override
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        if (mapper != null) {
            mapper.updateFromDto(dto, task);
        }
        applyBasicFields(dto, task);
        applyStatusOnUpdate(dto, task);
        applyAssignee(dto, task);
        applyLabels(dto, task);
        Task saved = taskRepo.save(task);
        return mapToResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new NoSuchElementException();
        }
        taskRepo.deleteById(id);
    }

    @Override
    public TaskResponseDto get(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        return mapToResponse(task);
    }

    @Override
    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> list(String titleCont, Long assigneeId, String statusSlug, Long labelId) {
        return taskRepo.findAll().stream()
                .filter(t -> titleCont == null || (t.getTitle() != null
                        && t.getTitle().toLowerCase().contains(titleCont.toLowerCase())))
                .filter(t -> assigneeId == null || (t.getAssignee() != null
                        && Objects.equals(t.getAssignee().getId(), assigneeId)))
                .filter(t -> statusSlug == null || (t.getTaskStatus() != null
                        && Objects.equals(t.getTaskStatus().getSlug(), statusSlug)))
                .filter(t -> labelId == null || (t.getLabels() != null
                        && t.getLabels().stream().anyMatch(l -> Objects.equals(l.getId(), labelId))))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void applyBasicFields(TaskUpsertDto dto, Task task) {
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        String content = dto.getContent();
        if (content != null) {
            setIfPresent(task, "setContent", String.class, content);
            setIfPresent(task, "setDescription", String.class, content);
        }
    }

    private void applyStatusOnCreate(TaskUpsertDto dto, Task task) {
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = statusRepo.findById(dto.getTaskStatusId()).orElseThrow(NoSuchElementException::new);
            task.setTaskStatus(status);
            return;
        }
        String statusVal = dto.getStatus();
        if (statusVal != null && !statusVal.isBlank()) {
            setStatusBySlugOrName(statusVal, task);
            return;
        }
        TaskStatus def = statusRepo.findBySlug("new")
                .orElseGet(() -> statusRepo.findByNameIgnoreCase("new").orElseThrow(NoSuchElementException::new));
        task.setTaskStatus(def);
    }

    private void applyStatusOnUpdate(TaskUpsertDto dto, Task task) {
        if (dto.getTaskStatusId() != null) {
            TaskStatus status = statusRepo.findById(dto.getTaskStatusId()).orElseThrow(NoSuchElementException::new);
            task.setTaskStatus(status);
            return;
        }
        String statusVal = dto.getStatus();
        if (statusVal != null && !statusVal.isBlank()) {
            setStatusBySlugOrName(statusVal, task);
        }
    }

    private void applyAssignee(TaskUpsertDto dto, Task task) {
        Long exec = firstNonNull(dto.getExecutorId(), dto.getAssigneeId());
        if (exec == null) {
            return;
        }
        if (exec == 0L) {
            task.setAssignee(null);
            return;
        }
        User assignee = userRepo.findById(exec).orElseThrow(NoSuchElementException::new);
        task.setAssignee(assignee);
    }

    private void applyLabels(TaskUpsertDto dto, Task task) {
        List<Long> ids = firstNonNull(dto.getLabelIds(), dto.getTaskLabelIds());
        if (ids == null) {
            return;
        }
        Set<Label> labels = new HashSet<>(labelRepo.findAllById(ids));
        task.setLabels(labels);
    }

    private void setStatusBySlugOrName(String value, Task task) {
        Optional<TaskStatus> bySlug = statusRepo.findBySlug(value);
        Optional<TaskStatus> byName = statusRepo.findByNameIgnoreCase(value);
        TaskStatus status = bySlug.orElseGet(() -> byName.orElseThrow(NoSuchElementException::new));
        task.setTaskStatus(status);
    }

    private TaskResponseDto mapToResponse(Task task) {
        if (mapper != null) {
            TaskResponseDto mapped = mapper.toResponse(task);
            if (mapped != null) {
                return mapped;
            }
        }
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        if (task.getTaskStatus() != null) {
            dto.setStatus(task.getTaskStatus().getSlug());
        }
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        if (task.getLabels() != null) {
            dto.setTaskLabelIds(task.getLabels().stream().map(Label::getId).collect(Collectors.toList()));
        }
        return dto;
    }

    private static <T> void setIfPresent(Object target, String method, Class<T> param, T value) {
        try {
            Method m = target.getClass().getMethod(method, param);
            m.invoke(target, value);
        } catch (Exception ignored) {
        }
    }

    private static <T> T firstNonNull(T a, T b) {
        return a != null ? a : b;
    }
}
