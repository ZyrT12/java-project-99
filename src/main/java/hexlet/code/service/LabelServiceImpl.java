package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.SlugUtils;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository repository;

    public LabelServiceImpl(LabelRepository repository) {
        this.repository = repository;
    }

    @Override
    public LabelDto get(Long id) {
        Label label = repository.findById(id).orElseThrow();
        return toDto(label);
    }

    @Override
    public List<LabelDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public LabelDto create(@Valid LabelCreateDto data) {
        Label l = new Label();
        l.setName(data.getName());
        l.setSlug(SlugUtils.slugify(data.getName()));
        l.setCreatedAt(Instant.now());
        Label saved = repository.save(l);
        return toDto(saved);
    }

    @Override
    public LabelDto update(Long id, @Valid LabelUpdateDto data) {
        Label l = repository.findById(id).orElseThrow();
        if (data.getName() != null && !data.getName().isBlank()) {
            l.setName(data.getName());
            l.setSlug(SlugUtils.slugify(data.getName()));
        }
        Label saved = repository.save(l);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Label l = repository.findById(id).orElseThrow();
        repository.delete(l);
    }

    private LabelDto toDto(Label label) {
        LabelDto dto = new LabelDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setCreatedAt(label.getCreatedAt());
        return dto;
    }
}
