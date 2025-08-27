package hexlet.code.controllers;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final TaskService service;

    public TasksController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end,
            @RequestParam(name = "titleCont", required = false) String titleCont,
            @RequestParam(name = "assigneeId", required = false) Long assigneeId,
            @RequestParam(name = "status", required = false) String statusSlug,
            @RequestParam(name = "labelId", required = false) Long labelId
    ) {
        List<TaskResponseDto> all = service.list(titleCont, assigneeId, statusSlug, labelId);
        int total = all.size();
        int from = start == null ? 0 : Math.max(0, start);
        int to = end == null ? total : Math.min(total, end);
        if (from > to) {
            from = to;
        }
        List<TaskResponseDto> page = all.subList(from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskUpsertDto dto) {
        TaskResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Long id, @Valid @RequestBody TaskUpsertDto dto) {
        TaskResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
