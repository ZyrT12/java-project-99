package hexlet.code.service;

import hexlet.code.dto.TaskCreateDto;
import hexlet.code.dto.TaskResponseDto;
import hexlet.code.dto.TaskUpdateDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final TaskStatusRepository statusRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo, TaskStatusRepository statusRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
    }

    public TaskResponseDto get(Long id) {
        var task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
        return toDto(task);
    }

    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream().map(this::toDto).toList();
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        validateCreate(dto);

        var task = new Task();
        task.setIndex(dto.getIndex());
        task.setTitle(dto.getTitle().trim());
        task.setContent(dto.getContent());

        var status = statusRepo.findBySlug(dto.getStatus())
                .orElseThrow(() -> new NoSuchElementException("Status not found: " + dto.getStatus()));
        task.setTaskStatus(status);

        if (dto.getAssigneeId() != null) {
            var assignee = userRepo.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new NoSuchElementException("Assignee not found: " + dto.getAssigneeId()));
            task.setAssignee(assignee);
        }

        var saved = taskRepo.save(task);
        return toDto(saved);
    }

    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        var task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (dto.getIndex() != null) {
            task.setIndex(dto.getIndex());
        }
        if (dto.getTitle() != null) {
            var t = dto.getTitle().trim();
            if (t.isEmpty()) {
                throw new IllegalArgumentException("title must be at least 1 char");
            }
            task.setTitle(t);
        }
        if (dto.getContent() != null) {
            task.setContent(dto.getContent());
        }
        if (dto.getStatus() != null) {
            var st = statusRepo.findBySlug(dto.getStatus())
                    .orElseThrow(() -> new NoSuchElementException("Status not found: " + dto.getStatus()));
            task.setTaskStatus(st);
        }
        if (dto.getAssigneeId() != null) {
            if (dto.getAssigneeId() == 0) {
                task.setAssignee(null);
            } else {
                var assignee = userRepo.findById(dto.getAssigneeId())
                        .orElseThrow(() -> new NoSuchElementException("Assignee not found: " + dto.getAssigneeId()));
                task.setAssignee(assignee);
            }
        }

        var saved = taskRepo.save(task);
        return toDto(saved);
    }

    public void delete(Long id) {
        var task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskRepo.delete(task);
    }

    private void validateCreate(TaskCreateDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new IllegalArgumentException("status is required");
        }
    }

    private TaskResponseDto toDto(Task t) {
        var dto = new TaskResponseDto();
        dto.setId(t.getId());
        dto.setIndex(t.getIndex());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setAssigneeId(t.getAssignee() == null ? null : t.getAssignee().getId());
        dto.setTitle(t.getTitle());
        dto.setContent(t.getContent());
        dto.setStatus(t.getTaskStatus().getSlug());
        return dto;
    }
}
