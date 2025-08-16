package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.model.Label;

import java.util.Optional;

import hexlet.code.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LabelServiceTest {

    private LabelRepository labelRepository;
    private LabelService labelService;

    @BeforeEach
    void setUp() {
        labelRepository = mock(LabelRepository.class);
        labelService = new LabelService(labelRepository);
    }

    @Test
    void createLabel() {
        when(labelRepository.save(any(Label.class))).thenAnswer(i -> {
            Label l = i.getArgument(0);
            l.setId(7L);
            return l;
        });
        LabelCreateDto dto = new LabelCreateDto();
        dto.setName("bug");
        LabelDto created = labelService.create(dto);
        assertThat(created.getId()).isEqualTo(7L);
        assertThat(created.getName()).isEqualTo("bug");
    }

    @Test
    void getLabel() {
        Label label = new Label();
        label.setId(3L);
        label.setName("feature");
        when(labelRepository.findById(3L)).thenReturn(Optional.of(label));
        LabelDto found = labelService.get(3L);
        assertThat(found.getName()).isEqualTo("feature");
    }
}
