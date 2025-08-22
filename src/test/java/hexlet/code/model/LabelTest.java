package hexlet.code.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class LabelTest {

    @Test
    void onCreateSetsCreatedAtAndSlug() {
        Label label = new Label();
        label.setName("My Label");

        // до вызова
        assertThat(label.getCreatedAt()).isNull();
        assertThat(label.getSlug()).isNull();

        // симуляция JPA persist
        label.onCreate();

        assertThat(label.getCreatedAt()).isNotNull();
        assertThat(label.getSlug()).isEqualTo("my-label");
    }

    @Test
    void onUpdateRegeneratesSlugIfMissing() {
        Label label = new Label();
        label.setName("Another Label");
        label.setSlug(null);

        label.onUpdate();

        assertThat(label.getSlug()).isEqualTo("another-label");
    }

    @Test
    void onUpdateDoesNothingIfSlugPresent() {
        Label label = new Label();
        label.setName("Keep Slug");
        label.setSlug("custom-slug");

        label.onUpdate();

        assertThat(label.getSlug()).isEqualTo("custom-slug");
    }

    @Test
    void setAndGetFieldsWork() {
        Label label = new Label();
        Instant now = Instant.now();

        label.setId(10L);
        label.setName("TestName");
        label.setSlug("test-slug");
        label.setCreatedAt(now);

        assertThat(label.getId()).isEqualTo(10L);
        assertThat(label.getName()).isEqualTo("TestName");
        assertThat(label.getSlug()).isEqualTo("test-slug");
        assertThat(label.getCreatedAt()).isEqualTo(now);
    }
}
