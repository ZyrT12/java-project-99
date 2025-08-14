package hexlet.code.controllers;

import hexlet.code.dto.tasks.TaskCreateDto;
import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpdateDto;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("isAuthenticated()")
public class TasksController {

    private final TaskService service;

    public TasksController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Object getOne(@PathVariable Long id) {
        return service.get(id);
    }

    @PermitAll
    @GetMapping
    public List<TaskResponseDto> list(@RequestParam(value = "titleCont", required = false) String titleCont,
                                      @RequestParam(value = "assigneeId", required = false) Long assigneeId,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "labelId", required = false) Long labelId) {
        return service.list(titleCont, assigneeId, status, labelId);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TaskCreateDto body) {
        var dto = service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDto body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
