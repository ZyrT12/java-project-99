package hexlet.code.dto.labels;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LabelResponseDtoTest {

    @Test
    void gettersAndSettersWork() {
        LabelResponseDto dto = new LabelResponseDto();
        dto.setId(1L);
        dto.setName("Bug");
        dto.setSlug("bug");
        Instant now = Instant.now();
        dto.setCreatedAt(now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Bug");
        assertThat(dto.getSlug()).isEqualTo("bug");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
    }
}
