package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.utils.SlugUtils;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LabelMapperTest {

    private LabelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(LabelMapper.class);
    }

    @Test
    void fromCreateSetsSlugAndCreatedAt() {
        LabelCreateDto dto = new LabelCreateDto();
        dto.setName("New Label");

        Label label = mapper.fromCreate(dto);

        assertNull(label.getId());
        assertEquals("New Label", label.getName());
        assertEquals(SlugUtils.slugify("New Label"), label.getSlug());
        assertNotNull(label.getCreatedAt());
    }

    @Test
    void updateFromDtoUpdatesSlugKeepsCreatedAt() {
        Label label = new Label();
        label.setId(7L);
        label.setName("Old");
        label.setSlug("old");
        Instant createdAt = Instant.now();
        label.setCreatedAt(createdAt);

        LabelUpdateDto dto = new LabelUpdateDto();
        dto.setName("New");

        mapper.updateFromDto(label, dto);

        assertEquals(7L, label.getId());
        assertEquals("New", label.getName());
        assertEquals(SlugUtils.slugify("New"), label.getSlug());
        assertEquals(createdAt, label.getCreatedAt());
    }
}
