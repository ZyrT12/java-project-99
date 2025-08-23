package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.SlugUtils;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository repository;
    private final LabelMapper labelMapper;

    @Autowired
    public LabelServiceImpl(LabelRepository repository, LabelMapper labelMapper) {
        this.repository = repository;
        this.labelMapper = labelMapper;
    }

    LabelServiceImpl(LabelRepository repository) {
        this(repository, label -> {
            LabelDto dto = new LabelDto();
            dto.setId(label.getId());
            dto.setName(label.getName());
            dto.setCreatedAt(label.getCreatedAt());
            return dto;
        });
    }

    @Override
    public List<LabelDto> getAll() {
        return repository.findAll().stream()
                .map(labelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDto get(Long id) {
        Label label = repository.findById(id).orElseThrow(NoSuchElementException::new);
        return labelMapper.toDto(label);
    }

    @Override
    public LabelDto create(@Valid LabelCreateDto data) {
        Label label = new Label();
        label.setName(data.getName());
        label.setSlug(SlugUtils.slugify(data.getName()));
        label.setCreatedAt(Instant.now());
        Label saved = repository.save(label);
        return labelMapper.toDto(saved);
    }

    @Override
    public LabelDto update(Long id, @Valid LabelUpdateDto data) {
        Label label = repository.findById(id).orElseThrow(NoSuchElementException::new);
        if (data.getName() != null) {
            label.setName(data.getName());
            label.setSlug(SlugUtils.slugify(data.getName()));
        }
        Label saved = repository.save(label);
        return labelMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Label l = repository.findById(id).orElseThrow(NoSuchElementException::new);
        repository.delete(l);
    }
}
