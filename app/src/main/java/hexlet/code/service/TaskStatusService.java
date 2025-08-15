package hexlet.code.service;

import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskStatusService {

    private final TaskStatusRepository repository;

    public TaskStatusService(TaskStatusRepository repository) {
        this.repository = repository;
    }

    public TaskStatus create(TaskStatusUpsertDto dto) {
        TaskStatus ts = new TaskStatus();
        ts.setName(dto.getName());
        ts.setSlug(dto.getSlug());
        return repository.save(ts);
    }

    @Transactional(readOnly = true)
    public TaskStatus get(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TaskStatus> list() {
        return repository.findAll();
    }

    public TaskStatus updatePartial(Long id, TaskStatusUpsertDto dto) {
        TaskStatus existing = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }
        if (dto.getSlug() != null && !dto.getSlug().isBlank()) {
            existing.setSlug(dto.getSlug());
        }
        return repository.save(existing);
    }

    public void delete(Long id) {
        TaskStatus existing = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.delete(existing);
    }
}
