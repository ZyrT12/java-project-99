package hexlet.code.controllers;

import hexlet.code.dto.tasks.OnCreate;
import hexlet.code.dto.tasks.OnUpdate;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
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

@RestController
@RequestMapping({"/api/task-statuses", "/api/task_statuses"})
public class TaskStatusController {

    private final TaskStatusService service;
    private final TaskStatusMapper taskStatusMapper;

    public TaskStatusController(TaskStatusService service, TaskStatusMapper taskStatusMapper) {
        this.service = service;
        this.taskStatusMapper = taskStatusMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> index() {
        List<TaskStatus> list = service.findAll();
        List<TaskStatusResponseDto> body = list.stream().map(taskStatusMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> show(@PathVariable Long id) {
        TaskStatus status = service.findById(id);
        return ResponseEntity.ok(taskStatusMapper.toDto(status));
    }

    @PostMapping
    public ResponseEntity<TaskStatusResponseDto> create(@Validated(OnCreate.class)
                                                            @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus saved = service.create(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/task-statuses/" + saved.getId()));
        return ResponseEntity.created(URI.create("/api/task-statuses/" + saved.getId()))
                .headers(headers)
                .body(taskStatusMapper.toDto(saved));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> updatePartial(@PathVariable Long id,
                                                               @Validated(OnUpdate.class)
                                                               @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(taskStatusMapper.toDto(updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> update(@PathVariable Long id,
                                                        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(taskStatusMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
