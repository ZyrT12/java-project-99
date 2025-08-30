package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
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
    void fromCreateIgnoresIdSlugCreatedAt() {
        LabelCreateDto dto = new LabelCreateDto();
        dto.setName("New Label");

        Label label = mapper.fromCreate(dto);
        assertNull(label.getId());
        assertNull(label.getSlug());
        assertNull(label.getCreatedAt());
        assertEquals("New Label", label.getName());
    }

    @Test
    void updateFromDtoChangesNameButKeepsSlugAndCreatedAt() {
        Label label = new Label();
        label.setId(7L);
        label.setName("Old");
        label.setSlug("old");
        label.setCreatedAt(Instant.now());

        LabelUpdateDto dto = new LabelUpdateDto();
        dto.setName("New");

        mapper.updateFromDto(label, dto);

        assertEquals(7L, label.getId());
        assertEquals("New", label.getName());
        assertEquals("old", label.getSlug());
        assertNotNull(label.getCreatedAt());
    }
}
