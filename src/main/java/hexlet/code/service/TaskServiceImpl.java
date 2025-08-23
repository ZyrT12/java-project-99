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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        return toDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> list(String titleCont,
                                      Long assigneeId,
                                      String statusSlug,
                                      Long labelId) {
        return taskRepo.findAll().stream()
                .filter(t -> titleCont == null || containsIgnoreCase(t.getTitle(), titleCont))
                .filter(t -> assigneeId == null || hasAssignee(t, assigneeId))
                .filter(t -> statusSlug == null || hasStatusSlug(t, statusSlug))
                .filter(t -> labelId == null || hasLabel(t, labelId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDto create(TaskUpsertDto dto) {
        Task task = new Task();
        applyUpsert(task, dto, true);
        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    @Override
    @Transactional
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        applyUpsert(task, dto, false);
        Task saved = taskRepo.save(task);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        taskRepo.delete(task);
    }

    private void applyUpsert(Task task, TaskUpsertDto dto, boolean isCreate) {
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            task.setContent(dto.getContent());
        }

        if (dto.getAssigneeId() != null) {
            if (dto.getAssigneeId() == 0L) {
                task.setAssignee(null);
            } else {
                User assignee = userRepo.findById(dto.getAssigneeId()).orElseThrow(NoSuchElementException::new);
                task.setAssignee(assignee);
            }
        }

        String statusSlug = dto.getStatus();
        Long statusId = firstPresentLong(dto, "getStatusId", "getTaskStatusId");
        if (statusSlug != null) {
            TaskStatus st = statusRepo.findBySlug(statusSlug).orElseThrow(NoSuchElementException::new);
            task.setTaskStatus(st);
        } else if (statusId != null) {
            TaskStatus st = statusRepo.findById(statusId).orElseThrow(NoSuchElementException::new);
            task.setTaskStatus(st);
        } else if (isCreate) {
            TaskStatus st = statusRepo.findBySlug("new").orElseThrow(NoSuchElementException::new);
            task.setTaskStatus(st);
        }

        List<Long> labelIds = dto.getLabelIds();
        if (labelIds == null) {
            labelIds = firstPresentListLong(dto, "getTaskLabelIds");
        }
        if (labelIds != null) {
            Set<Label> labels = new HashSet<>(labelRepo.findAllById(labelIds));
            task.setLabels(labels);
        }
    }

    private TaskResponseDto toDto(Task t) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setContent(t.getContent());
        dto.setStatus(t.getTaskStatus() != null ? t.getTaskStatus().getSlug() : null);
        dto.setAssigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null);
        dto.setLabelIds(t.getLabels() != null
                ? t.getLabels().stream().map(Label::getId).collect(Collectors.toList())
                : List.of());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setIndex(t.getIndex());
        return dto;
    }

    private static boolean containsIgnoreCase(String source, String needle) {
        return source != null && needle != null && source.toLowerCase().contains(needle.toLowerCase());
    }

    private static boolean hasAssignee(Task t, Long assigneeId) {
        return t.getAssignee() != null && assigneeId.equals(t.getAssignee().getId());
    }

    private static boolean hasStatusSlug(Task t, String slug) {
        return t.getTaskStatus() != null && slug.equals(t.getTaskStatus().getSlug());
    }

    private static boolean hasLabel(Task t, Long labelId) {
        return t.getLabels() != null && t.getLabels().stream().map(Label::getId).anyMatch(id -> id.equals(labelId));
    }

    private static Long firstPresentLong(Object target, String... getters) {
        for (String g : getters) {
            Long v = tryGetLong(target, g);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private static Long tryGetLong(Object target, String getterName) {
        try {
            Method m = target.getClass().getMethod(getterName);
            Object v = m.invoke(target);
            return v == null ? null : (Long) v;
        } catch (Exception ignored) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Long> firstPresentListLong(Object target, String... getters) {
        for (String g : getters) {
            try {
                Method m = target.getClass().getMethod(g);
                Object v = m.invoke(target);
                if (v != null) {
                    return (List<Long>) v;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
