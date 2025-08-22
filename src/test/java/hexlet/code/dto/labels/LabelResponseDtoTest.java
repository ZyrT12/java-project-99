package hexlet.code.dto.labels;

import java.time.Instant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LabelResponseDtoTest {

    @Test
    void ctorAndAccessorsWork() {
        Instant now = Instant.now();
        LabelResponseDto dto = new LabelResponseDto(1L, "Bug", "bug", now);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Bug");
        assertThat(dto.slug()).isEqualTo("bug");
        assertThat(dto.createdAt()).isEqualTo(now);
    }
}
