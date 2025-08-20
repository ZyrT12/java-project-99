package hexlet.code.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

public final class SlugUtils {
    private SlugUtils() {
    }

    public static String slugify(String input) {
        if (input == null) {
            return null;
        }
        String s = Normalizer.normalize(input, Normalizer.Form.NFD);
        s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s = s.toLowerCase(Locale.ROOT);
        s = s.replaceAll("[^a-z0-9]+", "-");
        s = s.replaceAll("^-+|-+$", "");
        s = s.replaceAll("-{2,}", "-");
        if (s.isEmpty()) {
            s = UUID.randomUUID().toString();
        }
        return s;
    }
}
