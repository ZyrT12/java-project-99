package hexlet.code.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlugUtilsExtraTest {

    @Test
    void trimsAndCollapsesSpaces() {
        String input = "  New   Feature   ";
        String slug = SlugUtils.slugify(input);
        assertThat(slug).isEqualTo("new-feature");
    }

    @Test
    void handlesEmptyString() {
        String slug = SlugUtils.slugify("");
        assertThat(slug).matches("[0-9a-f\\-]{36}");
    }
}
