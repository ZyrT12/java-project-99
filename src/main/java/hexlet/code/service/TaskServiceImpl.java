package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskMappingHelper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.spec.TaskSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final TaskStatusRepository statusRepo;
    private final TaskMapper mapper;
    private final TaskMappingHelper helper;

    public TaskServiceImpl(TaskRepository taskRepo,
                           TaskStatusRepository statusRepo,
                           TaskMapper mapper,
                           TaskMappingHelper helper) {
        this.taskRepo = taskRepo;
        this.statusRepo = statusRepo;
        this.mapper = mapper;
        this.helper = helper;
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
        Specification<Task> spec = Specification.allOf(
                TaskSpecifications.titleContains(titleCont),
                TaskSpecifications.hasAssignee(assigneeId),
                TaskSpecifications.hasStatusSlug(statusSlug),
                TaskSpecifications.hasLabel(labelId)
        );

        return taskRepo.findAll(spec).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto create(TaskUpsertDto dto) {
        Task task = new Task();
        mapper.updateFromDto(dto, task, helper);
        applyStatus(task, dto);
        Task saved = taskRepo.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public TaskResponseDto update(Long id, TaskUpsertDto dto) {
        Task task = taskRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateFromDto(dto, task, helper);
        applyStatus(task, dto);
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

    private void applyStatus(Task task, TaskUpsertDto dto) {
        if (task.getTaskStatus() == null && dto.getTaskStatusId() != null) {
            TaskStatus st = statusRepo.findById(dto.getTaskStatusId())
                    .orElseThrow(EntityNotFoundException::new);
            task.setTaskStatus(st);
        }
    }
}
