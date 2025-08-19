package hexlet.code.controllers;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
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
            @RequestParam(name = "titleCont", required = false) String titleCont,
            @RequestParam(name = "assigneeId", required = false) Long assigneeId,
            @RequestParam(name = "statusSlug", required = false) String statusSlug,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "labelId", required = false) Long labelId,
            @RequestParam(name = "_start", required = false) Integer start,
            @RequestParam(name = "_end", required = false) Integer end
    ) {
        String effectiveStatus = (statusSlug != null && !statusSlug.isBlank())
                ? statusSlug
                : (status != null && !status.isBlank() ? status : null);

        List<TaskResponseDto> all = (titleCont != null && !titleCont.isBlank())
                || assigneeId != null
                || effectiveStatus != null
                || labelId != null
                ? service.list(titleCont, assigneeId, effectiveStatus, labelId)
                : service.list();

        int total = all.size();
        int from = start != null ? Math.max(0, start) : 0;
        int to = end != null ? Math.min(total, end) : total;
        List<TaskResponseDto> page = all.subList(from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/{id}")
    public TaskResponseDto show(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
        public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskUpsertDto dto) {
        TaskResponseDto saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
        public ResponseEntity<TaskResponseDto> update(@PathVariable Long id, @Valid @RequestBody TaskUpsertDto dto) {
        TaskResponseDto saved = service.update(id, dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
