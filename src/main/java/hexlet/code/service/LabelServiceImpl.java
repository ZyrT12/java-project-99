package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository repository;
    private final LabelMapper labelMapper;

    public LabelServiceImpl(LabelRepository repository, LabelMapper labelMapper) {
        this.repository = repository;
        this.labelMapper = labelMapper;
    }

    @Override
    public LabelDto get(Long id) {
        Label label = repository.findById(id).orElseThrow(NoSuchElementException::new);
        return labelMapper.toDto(label);
    }

    @Override
    public List<LabelDto> getAll() {
        return repository.findAll()
                .stream()
                .map(labelMapper::toDto)
                .toList();
    }

    @Override
    public LabelDto create(@Valid LabelCreateDto data) {
        Label label = labelMapper.fromCreate(data);
        Label saved = repository.save(label);
        return labelMapper.toDto(saved);
    }

    @Override
    public LabelDto update(Long id, @Valid LabelUpdateDto data) {
        Label label = repository.findById(id).orElseThrow(NoSuchElementException::new);
        labelMapper.updateFromDto(label, data);
        Label saved = repository.save(label);
        return labelMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Label label = repository.findById(id).orElseThrow(NoSuchElementException::new);
        repository.delete(label);
    }
}
