package hexlet.code.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlugUtilsTest {

    @Test
    void slugifyAscii() {
        String s = SlugUtils.slugify("Hello World  2025");
        assertThat(s).isEqualTo("hello-world-2025");
    }

    @Test
    void slugifyTrimsAndCondenses() {
        String s = SlugUtils.slugify("  A  B   C  ");
        assertThat(s).isEqualTo("a-b-c");
    }

    @Test
    void slugifyCyrillic() {
        String s = SlugUtils.slugify("метка багов");
        assertThat(s).isNotBlank();
        assertThat(s).matches("[a-z0-9-]+");
        assertThat(s).doesNotContain(" ");
    }
}
