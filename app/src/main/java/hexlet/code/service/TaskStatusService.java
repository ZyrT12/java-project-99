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

    public List<TaskStatus> findAll() {
        return repository.findAll();
    }

    public TaskStatus findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public TaskStatus create(TaskStatusUpsertDto dto) {
        TaskStatus entity = new TaskStatus();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        return repository.save(entity);
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
