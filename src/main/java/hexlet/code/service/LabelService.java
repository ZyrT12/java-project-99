package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import jakarta.validation.Valid;

import java.util.List;

public interface LabelService {
    LabelDto get(Long id);
    List<LabelDto> getAll();
    LabelDto create(@Valid LabelCreateDto data);
    LabelDto update(Long id, @Valid LabelUpdateDto data);
    void delete(Long id);
}
