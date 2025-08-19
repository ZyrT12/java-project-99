package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
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
    private final UserRepository userRepo;
    private final TaskStatusRepository statusRepo;
    private final LabelRepository labelRepo;

    public TaskServiceImpl(TaskRepository taskRepo, UserRepository userRepo,
                           TaskStatusRepository statusRepo, LabelRepository labelRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
        this.labelRepo = labelRepo;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public TaskResponseDto get(Long id) {
        Task t = taskRepo.findById(id).orElseThrow();
        return toDto(t);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public TaskResponseDto create(TaskUpsertDto dto) {
        validateCreate(dto);
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setContent(dto.getDescription());
        if (dto.getTaskStatusId() != null) {
            task.setTaskStatus(statusRepo.findById(dto.getTaskStatusId()).orElseThrow());
        }
        if (dto.getExecutorId() != null && dto.getExecutorId() != 0L) {
            task.setAssignee(userRepo.findById(dto.getExecutorId()).orElseThrow());
        }
        if (dto.getLabelIds() != null) {
            Set<Label> labels = new HashSet<>(labelRepo.findAllById(dto.getLabelIds()));
            task.setLabels(labels);
        }
        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setContent(dto.getDescription());
        }
        if (dto.getTaskStatusId() != null) {
            task.setTaskStatus(statusRepo.findById(dto.getTaskStatusId()).orElseThrow());
        }
        if (dto.getExecutorId() != null) {
            if (dto.getExecutorId() == 0L) {
                task.setAssignee(null);
            } else {
                task.setAssignee(userRepo.findById(dto.getExecutorId()).orElseThrow());
            }
        }
        if (dto.getLabelIds() != null) {
            Set<Label> labels = new HashSet<>(labelRepo.findAllById(dto.getLabelIds()));
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
                        || (t.getTitle() != null && t.getTitle().toLowerCase().contains(titleCont.toLowerCase())))
                .filter(t -> assigneeId == null
                        || (t.getAssignee() != null && assigneeId.equals(t.getAssignee().getId())))
                .filter(t -> statusSlug == null
                        || (t.getTaskStatus() != null && statusSlug.equals(t.getTaskStatus().getSlug())))
                .filter(t -> labelId == null
                        || (t.getLabels() != null && t.getLabels().stream()
                                .anyMatch(l -> labelId.equals(l.getId()))))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void validateCreate(TaskUpsertDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
    }

    private TaskResponseDto toDto(Task t) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getContent());
        dto.setTaskStatusId(t.getTaskStatus() != null ? t.getTaskStatus().getId() : null);
        dto.setExecutorId(t.getAssignee() != null ? t.getAssignee().getId() : null);
        dto.setLabelIds(t.getLabels() != null
                ? t.getLabels().stream().map(Label::getId).collect(Collectors.toList())
                : List.of());
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}
