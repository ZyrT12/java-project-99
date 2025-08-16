package hexlet.code.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleExceptionTest {

    @Test
    void testCustomExceptionMessage() {
        var exception = new RuntimeException("Test exception");
        assertThat(exception.getMessage()).isEqualTo("Test exception");
    }
}
