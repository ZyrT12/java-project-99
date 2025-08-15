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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<LabelDto>> getAll() {
        List<LabelDto> data = service.getAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(data.size()));
        return ResponseEntity.ok().headers(headers).body(data);
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
