package hexlet.code.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class LabelTest {

    // --- уже существующие тесты ---
    @Test
    void onCreateSetsCreatedAtAndSlug() {
        Label label = new Label();
        label.setName("My Label");

        assertThat(label.getCreatedAt()).isNull();
        assertThat(label.getSlug()).isNull();

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


    @Test
    void equalsTrueWhenSameId() {
        Label a = new Label();
        a.setId(1L);
        a.setName("A");
        Label b = new Label();
        b.setId(1L);
        b.setName("B");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equalsTrueWhenIdNullButNamesEqual() {
        Label a = new Label();
        a.setName("Backend");
        Label b = new Label();
        b.setName("Backend");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equalsFalseWhenDifferentIdOrName() {
        Label a = new Label();
        a.setId(2L);
        a.setName("X");
        Label b = new Label();
        b.setId(3L);
        b.setName("Y");

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equalsHandlesNullAndOtherClass() {
        Label a = new Label();
        a.setName("Z");

        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("not a label")).isFalse();
    }
}
