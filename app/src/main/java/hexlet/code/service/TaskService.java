package hexlet.code.service;

import hexlet.code.dto.tasks.TaskCreateDto;
import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpdateDto;
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
@Transactional
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final TaskStatusRepository statusRepo;
    private final LabelRepository labelRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo, TaskStatusRepository statusRepo, LabelRepository labelRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
        this.labelRepo = labelRepo;
    }

    public TaskResponseDto get(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
        return toDto(task);
    }

    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream().map(this::toDto).toList();
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        validateCreate(dto);

        Task task = new Task();
        task.setTitle(dto.getTitle().trim());
        task.setContent(dto.getDescription());

        var status = statusRepo.findById(dto.getTaskStatusId())
                .orElseThrow(() -> new NoSuchElementException("Status not found: " + dto.getTaskStatusId()));
        task.setTaskStatus(status);

        if (dto.getExecutorId() != null) {
            var assignee = userRepo.findById(dto.getExecutorId())
                    .orElseThrow(() -> new NoSuchElementException("Assignee not found: " + dto.getExecutorId()));
            task.setAssignee(assignee);
        }

        applyLabels(task, dto.getLabelIds());

        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (dto.getTitle() != null) {
            String t = dto.getTitle().trim();
            if (t.isEmpty()) {
                throw new IllegalArgumentException("title must be at least 1 char");
            }
            task.setTitle(t);
        }
        if (dto.getDescription() != null) {
            task.setContent(dto.getDescription());
        }
        if (dto.getTaskStatusId() != null) {
            var st = statusRepo.findById(dto.getTaskStatusId())
                    .orElseThrow(() -> new NoSuchElementException("Status not found: " + dto.getTaskStatusId()));
            task.setTaskStatus(st);
        }
        if (dto.getExecutorId() != null) {
            if (dto.getExecutorId() == 0L) {
                task.setAssignee(null);
            } else {
                var assignee = userRepo.findById(dto.getExecutorId())
                        .orElseThrow(() -> new NoSuchElementException("Assignee not found: " + dto.getExecutorId()));
                task.setAssignee(assignee);
            }
        }
        if (dto.getLabelIds() != null) {
            applyLabels(task, dto.getLabelIds());
        }

        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    public void delete(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskRepo.delete(task);
    }

    private void validateCreate(TaskCreateDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        if (dto.getTaskStatusId() == null) {
            throw new IllegalArgumentException("taskStatusId is required");
        }
    }

    private void applyLabels(Task task, List<Long> labelIds) {
        if (labelIds == null) {
            return;
        }
        Set<Label> labels = new HashSet<>(labelRepo.findAllById(labelIds));
        task.setLabels(labels);
    }

    private TaskResponseDto toDto(Task t) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(t.getId());
        dto.setIndex(t.getIndex());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getContent());
        dto.setExecutorId(t.getAssignee() == null ? null : t.getAssignee().getId());
        dto.setTaskStatusId(t.getTaskStatus().getId());
        dto.setLabelIds(t.getLabels() == null ? List.of() : t.getLabels().stream().map(Label::getId).collect(Collectors.toList()));
        return dto;
    }
}
