package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {

    @Mock
    private LabelRepository repository;

    private LabelServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LabelServiceImpl(repository);
    }

    @Test
    void getReturnsDto() {
        Label l = new Label();
        l.setId(10L);
        l.setName("Bug");
        l.setCreatedAt(Instant.parse("2020-01-01T00:00:00Z"));
        when(repository.findById(10L)).thenReturn(Optional.of(l));

        LabelDto dto = service.get(10L);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("Bug");
        assertThat(dto.getCreatedAt()).isEqualTo(Instant.parse("2020-01-01T00:00:00Z"));
    }

    @Test
    void getAllMapsEntitiesToDtos() {
        Label a = new Label();
        a.setId(1L);
        a.setName("Backend");
        a.setCreatedAt(Instant.now());

        Label b = new Label();
        b.setId(2L);
        b.setName("Frontend");
        b.setCreatedAt(Instant.now());

        when(repository.findAll()).thenReturn(Arrays.asList(a, b));

        List<LabelDto> dtos = service.getAll();

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void createSavesAndReturnsDto() {
        LabelCreateDto data = new LabelCreateDto();
        data.setName("Critical");

        ArgumentCaptor<Label> captor = ArgumentCaptor.forClass(Label.class);
        Label saved = new Label();
        saved.setId(3L);
        saved.setName("Critical");
        saved.setCreatedAt(Instant.now());
        when(repository.save(any(Label.class))).thenReturn(saved);

        LabelDto dto = service.create(data);

        verify(repository).save(captor.capture());
        Label toSave = captor.getValue();
        assertThat(toSave.getName()).isEqualTo("Critical");
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getName()).isEqualTo("Critical");
    }

    @Test
    void updateWithNameChangesFields() {
        Label existing = new Label();
        existing.setId(5L);
        existing.setName("Old");
        existing.setCreatedAt(Instant.now());
        when(repository.findById(5L)).thenReturn(Optional.of(existing));

        LabelUpdateDto data = new LabelUpdateDto();
        data.setName("New");

        Label saved = new Label();
        saved.setId(5L);
        saved.setName("New");
        saved.setCreatedAt(existing.getCreatedAt());
        when(repository.save(any(Label.class))).thenReturn(saved);

        LabelDto dto = service.update(5L, data);

        assertThat(dto.getName()).isEqualTo("New");
    }

    @Test
    void updateWithoutNameKeepsExisting() {
        Label existing = new Label();
        existing.setId(6L);
        existing.setName("Keep");
        existing.setCreatedAt(Instant.now());
        when(repository.findById(6L)).thenReturn(Optional.of(existing));

        LabelUpdateDto data = new LabelUpdateDto();
        data.setName("");

        Label saved = new Label();
        saved.setId(6L);
        saved.setName("Keep");
        saved.setCreatedAt(existing.getCreatedAt());
        when(repository.save(any(Label.class))).thenReturn(saved);

        LabelDto dto = service.update(6L, data);

        assertThat(dto.getName()).isEqualTo("Keep");
    }

    @Test
    void deleteDelegatesToRepository() {
        Label existing = new Label();
        existing.setId(9L);
        when(repository.findById(9L)).thenReturn(Optional.of(existing));

        service.delete(9L);

        verify(repository).delete(existing);
    }

    @Test
    void getThrowsWhenNotFound() {
        when(repository.findById(777L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(777L)).isInstanceOf(RuntimeException.class);
    }
}
