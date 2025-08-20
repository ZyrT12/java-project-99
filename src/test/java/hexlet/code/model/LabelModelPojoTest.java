package hexlet.code.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LabelModelPojoTest {

    @Test
    void fieldsRoundTrip() {
        Label l = new Label();
        l.setId(1L);
        l.setName("Bug");
        l.setSlug("bug");

        assertThat(l.getId()).isEqualTo(1L);
        assertThat(l.getName()).isEqualTo("Bug");
        assertThat(l.getSlug()).isEqualTo("bug");
        assertThat(l.toString()).isNotNull();
    }
}
