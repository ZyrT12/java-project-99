package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskMappingHelper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.spec.TaskSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final LabelRepository labelRepo;
    private final TaskMapper mapper;
    private final TaskMappingHelper mapHelper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepo,
                           TaskStatusRepository statusRepo,
                           UserRepository userRepo,
                           LabelRepository labelRepo,
                           TaskMapper mapper,
                           TaskMappingHelper mapHelper) {
        this.taskRepo = taskRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.labelRepo = labelRepo;
        this.mapper = mapper;
        this.mapHelper = mapHelper;
    }

    @Override
    public TaskResponseDto get(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toResponse(task);
    }

    @Override
    public List<TaskResponseDto> list() {
        return taskRepo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> list(String titleCont, Long assigneeId, String statusSlug, Long labelId) {
        List<Specification<Task>> parts = new ArrayList<>();

        Specification<Task> s1 = TaskSpecifications.titleContains(titleCont);
        if (s1 != null) {
            parts.add(s1);
        }

        Specification<Task> s2 = TaskSpecifications.assigneeIdEquals(assigneeId);
        if (s2 != null) {
            parts.add(s2);
        }

        Specification<Task> s3 = TaskSpecifications.statusSlugOrNameEquals(statusSlug);
        if (s3 != null) {
            parts.add(s3);
        }

        Specification<Task> s4 = TaskSpecifications.hasLabel(labelId);
        if (s4 != null) {
            parts.add(s4);
        }

        if (parts.isEmpty()) {
            return taskRepo.findAll().stream()
                    .map(mapper::toResponse)
                    .collect(java.util.stream.Collectors.toList());
        }

        Specification<Task> spec = Specification.allOf(parts.toArray(new Specification[0]));

        return taskRepo.findAll(spec).stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public TaskResponseDto create(TaskUpsertDto dto) {
        Task task = new Task();
        mapper.updateFromDto(dto, task, mapHelper);
        if (dto.getTaskStatusId() != null) {
            TaskStatus st = statusRepo.findById(dto.getTaskStatusId()).orElseThrow(EntityNotFoundException::new);
            task.setTaskStatus(st);
        }
        if (task.getTaskStatus() == null) {
            Optional<TaskStatus> def = statusRepo.findBySlug("new");
            TaskStatus status = def.orElseGet(() ->
                    statusRepo.findByNameIgnoreCase("new").orElseThrow(EntityNotFoundException::new));
            task.setTaskStatus(status);
        }
        Task saved = taskRepo.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateFromDto(dto, task, mapHelper);
        if (dto.getTaskStatusId() != null) {
            TaskStatus st = statusRepo.findById(dto.getTaskStatusId()).orElseThrow(EntityNotFoundException::new);
            task.setTaskStatus(st);
        }
        Task saved = taskRepo.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        taskRepo.deleteById(id);
    }
}
