package hexlet.code.users;

import hexlet.code.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskStatusPojoTest {

    @Test
    void constructorSetsFields() {
        TaskStatus s = new TaskStatus("Name", "slug");
        assertThat(s.getName()).isEqualTo("Name");
        assertThat(s.getSlug()).isEqualTo("slug");
    }
}
