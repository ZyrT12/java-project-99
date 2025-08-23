package hexlet.code.controllers;

import hexlet.code.dto.tasks.OnCreate;
import hexlet.code.dto.tasks.OnUpdate;
import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/task-statuses", "/api/task_statuses"})
public class TaskStatusController {

    private final TaskStatusService service;
    private final TaskStatusMapper mapper;

    public TaskStatusController(TaskStatusService service, TaskStatusMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end
    ) {
        List<TaskStatus> list = service.findAll();
        List<TaskStatusResponseDto> all = list.stream().map(mapper::toDto).collect(Collectors.toList());
        int total = all.size();
        int from = start == null ? 0 : Math.max(0, start);
        int to = end == null ? total : Math.min(total, end);
        if (from > to) {
            from = to;
        }
        List<TaskStatusResponseDto> page = all.subList(from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> show(@PathVariable Long id) {
        TaskStatus entity = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @PostMapping
    public ResponseEntity<TaskStatusResponseDto> create(@Validated(OnCreate.class)
                                                            @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/task-statuses/" + created.getId())).body(mapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> update(@PathVariable Long id, @Validated(OnUpdate.class)
        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus updated = service.updatePartial(id, dto);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> partialUpdate(@PathVariable Long id, @Validated(OnUpdate.class)
        @RequestBody TaskStatusUpsertDto dto) {
        TaskStatus result = service.updatePartial(id, dto);
        return ResponseEntity.ok(mapper.toDto(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
