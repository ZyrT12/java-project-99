package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
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
        l.setSlug(slugify(data.getName()));
        Label saved = repository.save(l);
        return toDto(saved);
    }

    @Override
    public LabelDto update(Long id, @Valid LabelUpdateDto data) {
        Label l = repository.findById(id).orElseThrow();
        l.setName(data.getName());
        l.setSlug(slugify(data.getName()));
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
        return dto;
    }

    private String slugify(String source) {
        if (source == null) {
            return null;
        }
        String nowhitespace = source.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\p{Alnum}-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ROOT);
    }
}
