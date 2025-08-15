package hexlet.code.controllers;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labels")
public class LabelsController {
    private final LabelService service;

    public LabelsController(LabelService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public LabelDto getOne(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public ResponseEntity<java.util.List<hexlet.code.dto.labels.LabelDto>> getAll(
            @org.springframework.web.bind.annotation.RequestParam(name = "_start", required = false) Integer start,
            @org.springframework.web.bind.annotation.RequestParam(name = "_end", required = false) Integer end
    ) {
        java.util.List<hexlet.code.dto.labels.LabelDto> all = service.getAll();
        int total = all.size();
        int from = start != null ? Math.max(0, start) : 0;
        int to = end != null ? Math.min(total, end) : total;
        java.util.List<hexlet.code.dto.labels.LabelDto> page = all.subList(from, to);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        return org.springframework.http.ResponseEntity.ok().headers(headers).body(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDto create(@Valid @RequestBody LabelCreateDto data) {
        return service.create(data);
    }

    @PutMapping("/{id}")
    public LabelDto update(@PathVariable Long id, @Valid @RequestBody LabelUpdateDto data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
