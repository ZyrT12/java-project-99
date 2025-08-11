package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusCreateDto;
import hexlet.code.dto.TaskStatusUpdateDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusController {

    private final TaskStatusRepository repo;

    public TaskStatusController(TaskStatusRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public TaskStatus getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("TaskStatus not found"));
    }

    @GetMapping
    public List<TaskStatus> getAll() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public TaskStatus create(@Valid @RequestBody TaskStatusCreateDto body) {
        if (repo.existsByName(body.getName())) throw new EntityExistsException("name already used");
        if (repo.existsBySlug(body.getSlug())) throw new EntityExistsException("slug already used");
        var saved = repo.save(new TaskStatus(body.getName(), body.getSlug()));
        return saved;
    }

    @PutMapping("/{id}")
    @Transactional
    public TaskStatus update(@PathVariable Long id, @RequestBody TaskStatusUpdateDto body) {
        var entity = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("TaskStatus not found"));
        if (body.getName() != null) {
            if (repo.existsByName(body.getName()) && !body.getName().equals(entity.getName()))
                throw new EntityExistsException("name already used");
            entity.setName(body.getName());
        }
        if (body.getSlug() != null) {
            if (repo.existsBySlug(body.getSlug()) && !body.getSlug().equals(entity.getSlug()))
                throw new EntityExistsException("slug already used");
            entity.setSlug(body.getSlug());
        }
        return repo.save(entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("TaskStatus not found");
        repo.deleteById(id);
    }

    // Общая обработка типичных ошибок под удобные статусы
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String onNotFound(RuntimeException e) { return e.getMessage(); }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({EntityExistsException.class, DataIntegrityViolationException.class})
    public String onConflict(RuntimeException e) { return e.getMessage(); }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public String onBadReq(RuntimeException e) { return e.getMessage(); }
}
