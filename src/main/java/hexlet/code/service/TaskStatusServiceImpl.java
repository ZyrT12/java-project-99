package hexlet.code.service;

import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository repository;
    private final TaskStatusMapper mapper;

    public TaskStatusServiceImpl(TaskStatusRepository repository, TaskStatusMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TaskStatusResponseDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskStatusResponseDto findById(Long id) {
        TaskStatus s = repository.findById(id).orElseThrow(NoSuchElementException::new);
        return mapper.toDto(s);
    }

    @Override
    public TaskStatusResponseDto create(TaskStatusUpsertDto dto) {
        TaskStatus s = new TaskStatus(dto.getName(), dto.getSlug());
        TaskStatus saved = repository.save(s);
        return mapper.toDto(saved);
    }

    @Override
    public TaskStatusResponseDto updatePartial(Long id, TaskStatusUpsertDto dto) {
        TaskStatus s = repository.findById(id).orElseThrow(NoSuchElementException::new);
        if (dto.getName() != null) {
            s.setName(dto.getName());
        }
        if (dto.getSlug() != null) {
            s.setSlug(dto.getSlug());
        }
        TaskStatus saved = repository.save(s);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
