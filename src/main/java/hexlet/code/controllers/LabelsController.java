package hexlet.code.controllers;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


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
