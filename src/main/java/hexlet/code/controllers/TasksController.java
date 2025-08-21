package hexlet.code.controllers;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@RequestMapping(path = {"/api/tasks", "/api/tasks/", "/api/task", "/api/task/"},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class TasksController {

    private final TaskService service;

    public TasksController(TaskService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<TaskResponseDto>> index(
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end,
            @RequestParam(name = "titleCont", required = false) String titleCont,
            @RequestParam(name = "assigneeId", required = false) Long assigneeId,
            @RequestParam(name = "status", required = false) String statusSlug,
            @RequestParam(name = "labelId", required = false) Long labelId
    ) {
        List<TaskResponseDto> filtered = service.list(titleCont, assigneeId, statusSlug, labelId);
        int total = filtered.size();
        int from = start != null ? Math.max(0, start) : 0;
        int to = end != null ? Math.min(total, end) : total;
        List<TaskResponseDto> page = filtered.subList(from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskUpsertDto dto) {
        TaskResponseDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Long id, @Valid @RequestBody TaskUpsertDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
