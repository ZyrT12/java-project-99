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
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final LabelRepository labelRepo;

    public TaskServiceImpl(TaskRepository taskRepo,
                           TaskStatusRepository statusRepo,
                           UserRepository userRepo,
                           LabelRepository labelRepo) {
        this.taskRepo = taskRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.labelRepo = labelRepo;
    }

    @Override
    public TaskResponseDto get(Long id) {
        Task t = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        return toDto(t);
    }

    @Override
    @Transactional
    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public TaskResponseDto create(TaskUpsertDto dto) {
        Task task = new Task();

        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        
        if (dto.getContent() != null || dto.getDescription() != null) {
            task.setContent(dto.getContent() != null ? dto.getContent() : dto.getDescription());
        }

        if (dto.getStatus() != null) {
            TaskStatus status = statusRepo.findBySlug(dto.getStatus()).orElseThrow();
            task.setTaskStatus(status);
        } else if (dto.getTaskStatusId() != null) {
            TaskStatus status = statusRepo.findById(dto.getTaskStatusId()).orElseThrow();
            task.setTaskStatus(status);
        }
        if (task.getTaskStatus() == null) {
            TaskStatus defaultStatus = statusRepo.findBySlug("new")
                    .orElseGet(() -> statusRepo.findAll().stream().findFirst().orElseThrow());
            task.setTaskStatus(defaultStatus);
        }

        if (dto.getAssigneeId() != null) {
            if (dto.getAssigneeId() == 0L) {
                task.setAssignee(null);
            } else {
                User user = userRepo.findById(dto.getAssigneeId()).orElseThrow();
                task.setAssignee(user);
            }
        } else if (dto.getExecutorId() != null) {
            if (dto.getExecutorId() == 0L) {
                task.setAssignee(null);
            } else {
                User user = userRepo.findById(dto.getExecutorId()).orElseThrow();
                task.setAssignee(user);
            }
        }

        List<Long> labelIds = dto.getTaskLabelIds() != null ? dto.getTaskLabelIds() : dto.getLabelIds();
        if (labelIds != null && !labelIds.isEmpty()) {
            Set<Label> labels = new HashSet<>(labelRepo.findAllById(labelIds));
            task.setLabels(labels);
        } else {
            task.setLabels(Set.of());
        }

        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);

        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null || dto.getDescription() != null) {
            task.setContent(dto.getContent() != null ? dto.getContent() : dto.getDescription());
        }

        if (dto.getStatus() != null) {
            TaskStatus status = statusRepo.findBySlug(dto.getStatus()).orElseThrow();
            task.setTaskStatus(status);
        } else if (dto.getTaskStatusId() != null) {
            TaskStatus status = statusRepo.findById(dto.getTaskStatusId()).orElseThrow();
            task.setTaskStatus(status);
        }

        if (dto.getAssigneeId() != null) {
            if (dto.getAssigneeId() == 0L) {
                task.setAssignee(null);
            } else {
                var assignee = userRepo.findById(dto.getAssigneeId()).orElseThrow();
                task.setAssignee(assignee);
            }
        } else if (dto.getExecutorId() != null) {
            if (dto.getExecutorId() == 0L) {
                task.setAssignee(null);
            } else {
                var assignee = userRepo.findById(dto.getExecutorId()).orElseThrow();
                task.setAssignee(assignee);
            }
        }

        List<Long> labelIds = dto.getTaskLabelIds() != null ? dto.getTaskLabelIds() : dto.getLabelIds();
        if (labelIds != null) {
            Set<Label> labels = new HashSet<>(labelRepo.findAllById(labelIds));
            task.setLabels(labels);
        }

        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new NoSuchElementException("Task not found: " + id);
        }
        taskRepo.deleteById(id);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<TaskResponseDto> list(String titleCont, Long assigneeId, String statusSlug, Long labelId) {
        return taskRepo.findAll().stream()
                .filter(t -> titleCont == null
                        || t.getTitle().toLowerCase().contains(titleCont.toLowerCase()))
                .filter(t -> assigneeId == null
                        || (t.getAssignee() != null && assigneeId.equals(t.getAssignee().getId())))
                .filter(t -> statusSlug == null
                        || (t.getTaskStatus() != null && statusSlug.equals(t.getTaskStatus().getSlug())))
                .filter(t -> labelId == null
                        || (t.getLabels() != null && t.getLabels()
                        .stream().anyMatch(l -> labelId.equals(l.getId()))))
                .map(this::toDto)
                .toList();
    }

    private TaskResponseDto toDto(Task t) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setContent(t.getContent());
        dto.setDescription(t.getContent());
        dto.setStatus(t.getTaskStatus() != null ? t.getTaskStatus().getSlug() : null);
        dto.setTaskStatusId(t.getTaskStatus() != null ? t.getTaskStatus().getId() : null);
        dto.setAssigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null);
        dto.setExecutorId(t.getAssignee() != null ? t.getAssignee().getId() : null);
        dto.setTaskLabelIds(t.getLabels() != null
                ? t.getLabels().stream().map(Label::getId).collect(Collectors.toList())
                : List.of());
        dto.setLabelIds(dto.getTaskLabelIds());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setIndex(t.getIndex());
        return dto;
    }
}
