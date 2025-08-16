package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabelService {
    private final LabelRepository repository;

    public LabelService(LabelRepository repository) {
        this.repository = repository;
    }

    public LabelDto get(Long id) {
        Label label = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toDto(label);
    }

    public List<LabelDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public LabelDto create(@Valid LabelCreateDto data) {
        if (repository.existsByNameIgnoreCase(data.getName())) {
            throw new ConstraintViolationException("Label name must be unique", null);
        }
        Label label = new Label();
        label.setName(data.getName().trim());
        Label saved = repository.save(label);
        return toDto(saved);
    }

    public LabelDto update(Long id, @Valid LabelUpdateDto data) {
        Label label = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        String newName = data.getName().trim();
        if (!label.getName().equalsIgnoreCase(newName) && repository.existsByNameIgnoreCase(newName)) {
            throw new ConstraintViolationException("Label name must be unique", null);
        }
        label.setName(newName);
        return toDto(label);
    }

    public void delete(Long id) {
        Label label = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (label.getTasks() != null && !label.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete label in use");
        }
        repository.delete(label);
    }

    private LabelDto toDto(Label label) {
        LabelDto dto = new LabelDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setCreatedAt(label.getCreatedAt());
        return dto;
    }
}
