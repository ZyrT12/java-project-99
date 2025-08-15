package hexlet.code.controllers;

import hexlet.code.dto.tasks.OnCreate;
import hexlet.code.dto.tasks.OnUpdate;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({"/api/task-statuses", "/api/task_statuses"})
public class TaskStatusController {

    private final TaskStatusService service;

    public TaskStatusController(TaskStatusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskStatus>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end
    ) {
        List<TaskStatus> all = service.findAll();
        int total = all.size();
        int from = start != null ? Math.max(0, start) : 0;
        int to = end != null ? Math.min(total, end) : total;
        List<TaskStatus> page = all.subList(from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> show(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskStatus> create(@Validated(OnCreate.class) @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/task-statuses/" + saved.getId())).body(saved);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskStatus> patch(@PathVariable Long id, @Validated(OnUpdate.class)
        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskStatus> update(@PathVariable Long id, @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
