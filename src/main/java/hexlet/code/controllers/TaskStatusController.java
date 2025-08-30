package hexlet.code.controllers;

import hexlet.code.dto.tasks.OnCreate;
import hexlet.code.dto.tasks.OnUpdate;
import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.service.TaskStatusService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping({"/api/task_statuses", "/api/task-statuses", "/task_statuses"})
public class TaskStatusController {
    private final TaskStatusService service;

    public TaskStatusController(TaskStatusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> index() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskStatusResponseDto> create(@Validated(OnCreate.class)
                                                        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatusResponseDto created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> update(@PathVariable Long id,
                                                        @Validated(OnUpdate.class)
                                                        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatusResponseDto updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> partialUpdate(@PathVariable Long id,
                                                               @Validated(OnUpdate.class)
                                                               @RequestBody TaskStatusUpsertDto dto) {
        TaskStatusResponseDto updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
