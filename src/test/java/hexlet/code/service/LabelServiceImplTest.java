package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LabelServiceImplTest {

    private LabelRepository repository;
    private LabelMapper labelMapper;
    private LabelService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LabelRepository.class);
        labelMapper = Mockito.mock(LabelMapper.class);
        service = new LabelServiceImpl(repository, labelMapper);

        when(labelMapper.toDto(ArgumentMatchers.any(Label.class))).thenAnswer(invocation -> {
            Label l = invocation.getArgument(0, Label.class);
            LabelDto dto = new LabelDto();
            dto.setId(l.getId());
            dto.setName(l.getName());
            dto.setCreatedAt(l.getCreatedAt());
            return dto;
        });

        when(labelMapper.fromCreate(ArgumentMatchers.any(LabelCreateDto.class))).thenAnswer(invocation -> {
            LabelCreateDto dto = invocation.getArgument(0, LabelCreateDto.class);
            Label l = new Label();
            l.setName(dto.getName());
            return l;
        });

        Mockito.doAnswer(invocation -> {
            Label target = invocation.getArgument(0, Label.class);
            LabelUpdateDto dto = invocation.getArgument(1, LabelUpdateDto.class);
            if (dto.getName() != null) {
                target.setName(dto.getName());
            }
            return null;
        }).when(labelMapper)
                .updateFromDto(ArgumentMatchers.any(Label.class), ArgumentMatchers.any(LabelUpdateDto.class));
    }

    @Test
    void getReturnsDto() {
        Label l = new Label();
        l.setId(10L);
        l.setName("bug");
        l.setCreatedAt(Instant.now());
        when(repository.findById(10L)).thenReturn(Optional.of(l));

        LabelDto dto = service.get(10L);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("bug");
        assertThat(dto.getCreatedAt()).isNotNull();
    }

    @Test
    void getAllMapsEach() {
        Label a = new Label();
        a.setId(1L);
        a.setName("a");
        a.setCreatedAt(Instant.now());
        Label b = new Label();
        b.setId(2L);
        b.setName("b");
        b.setCreatedAt(Instant.now());
        when(repository.findAll()).thenReturn(List.of(a, b));

        List<LabelDto> list = service.getAll();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getId()).isEqualTo(1L);
        assertThat(list.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void createUsesMapperAndReturnsDto() {
        LabelCreateDto in = new LabelCreateDto();
        in.setName("new");
        Mockito.when(repository.save(ArgumentMatchers.any(Label.class))).thenAnswer(invocation -> {
            Label saved = invocation.getArgument(0, Label.class);
            saved.setId(77L);
            saved.setCreatedAt(Instant.now());
            return saved;
        });

        LabelDto out = service.create(in);

        assertThat(out.getId()).isEqualTo(77L);
        assertThat(out.getName()).isEqualTo("new");
        assertThat(out.getCreatedAt()).isNotNull();
    }

    @Test
    void updateUsesMapperAndReturnsDto() {
        Label exist = new Label();
        exist.setId(5L);
        exist.setName("old");
        exist.setCreatedAt(Instant.now().minusSeconds(60));
        when(repository.findById(5L)).thenReturn(Optional.of(exist));
        when(repository.save(ArgumentMatchers
                .any(Label.class))).thenAnswer(invocation -> invocation.getArgument(0, Label.class));

        LabelUpdateDto upd = new LabelUpdateDto();
        upd.setName("new-name");

        LabelDto out = service.update(5L, upd);

        assertThat(out.getId()).isEqualTo(5L);
        assertThat(out.getName()).isEqualTo("new-name");
    }
}
