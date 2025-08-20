package hexlet.code.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskStatusModelPojoTest {

    @Test
    void fieldsRoundTrip() {
        TaskStatus s = new TaskStatus();
        s.setId(2L);
        s.setName("In progress");
        s.setSlug("in_progress");

        assertThat(s.getId()).isEqualTo(2L);
        assertThat(s.getName()).isEqualTo("In progress");
        assertThat(s.getSlug()).isEqualTo("in_progress");
        assertThat(s.toString()).isNotNull();
    }
}
