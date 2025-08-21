package hexlet.code.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtils {
    private static final Pattern NON_ALNUM = Pattern.compile("[^\\p{Alnum}]+");
    private static final Pattern DASH_RUNS = Pattern.compile("-+");
    private static final Pattern EDGE_DASH = Pattern.compile("(?:(?<=^)-|-(?=$))");

    private SlugUtils() { }

    public static String slugify(String raw) {
        if (raw == null) {
            return "";
        }
        String s = Normalizer.normalize(raw, Normalizer.Form.NFKD)
                .toLowerCase(Locale.ROOT);
        s = NON_ALNUM.matcher(s).replaceAll("-");
        s = DASH_RUNS.matcher(s).replaceAll("-");
        s = EDGE_DASH.matcher(s).replaceAll("");
        return s;
    }
}
