package hexlet.code.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SlugUtilsAsciiTest {

    @Test
    void slugifiesAsciiSentence() {
        String s = SlugUtils.slugify("Hello, world  from  ChatGPT!");
        assertThat(s).isEqualTo("hello-world-from-chatgpt");
    }

    @Test
    void returnsSameWhenAlreadySlug() {
        String s = SlugUtils.slugify("already-slug");
        assertThat(s).isEqualTo("already-slug");
    }
}
