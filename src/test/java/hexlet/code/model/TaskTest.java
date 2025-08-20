package hexlet.code.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

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
    void equalsReflexiveHashCodeConsistentAndToStringNonEmpty() {
        Task t1 = new Task();
        Task t2 = new Task();

        setIfPresent(t1, "id", 100L);
        setIfPresent(t2, "id", 100L);

        assertThat(t1).isEqualTo(t1);
        assertThat(t1.equals(null)).isFalse();
        assertThat(t1.equals(new Object())).isFalse();

        if (t1.equals(t2)) {
            assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
        } else {
            assertThat(t1).isNotEqualTo(t2);
        }

        String s = t1.toString();
        assertThat(s).isNotNull();
        assertThat(s.length()).isGreaterThan(0);
    }
}
