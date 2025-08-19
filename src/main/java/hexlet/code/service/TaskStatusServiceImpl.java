package hexlet.code.service;

import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository repository;

    public TaskStatusServiceImpl(TaskStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskStatus> findAll() {
        return repository.findAll();
    }

    @Override
    public TaskStatus findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public TaskStatus create(TaskStatusUpsertDto dto) {
        TaskStatus s = new TaskStatus(dto.getName(), dto.getSlug());
        return repository.save(s);
    }

    @Override
    public TaskStatus updatePartial(Long id, TaskStatusUpsertDto dto) {
        TaskStatus s = repository.findById(id).orElseThrow();
        if (dto.getName() != null) {
            s.setName(dto.getName());
        }
        if (dto.getSlug() != null) {
            s.setSlug(dto.getSlug());
        }
        return repository.save(s);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
