package hexlet.code.dto.labels;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LabelResponseDtoTest {

    @Test
    void gettersAndSetters() {
        LabelResponseDto dto = new LabelResponseDto();
        dto.setId(10L);
        dto.setName("Bug");
        dto.setSlug("bug");
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("Bug");
        assertThat(dto.getSlug()).isEqualTo("bug");
    }
}
