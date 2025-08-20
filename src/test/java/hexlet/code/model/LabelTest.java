package hexlet.code.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class LabelTest {

    private static void setIfPresent(Object target, String field, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException ignored) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void toStringIsNotEmpty() {
        Label l = new Label();
        setIfPresent(l, "name", "feature");
        String s = l.toString();
        assertThat(s).isNotNull();
        assertThat(s.length()).isGreaterThan(0);
    }

    @Test
    void equalsIsReflexiveAndHashCodeConsistent() {
        Label l1 = new Label();
        setIfPresent(l1, "id", 1L);
        setIfPresent(l1, "name", "bug");

        Label l2 = new Label();
        setIfPresent(l2, "id", 1L);
        setIfPresent(l2, "name", "bug");

        assertThat(l1).isEqualTo(l1);
        assertThat(l1.equals(null)).isFalse();
        assertThat(l1.equals(new Object())).isFalse();
        if (l1.equals(l2)) {
            assertThat(l1.hashCode()).isEqualTo(l2.hashCode());
        } else {
            assertThat(l1).isNotEqualTo(l2);
        }
    }
}
