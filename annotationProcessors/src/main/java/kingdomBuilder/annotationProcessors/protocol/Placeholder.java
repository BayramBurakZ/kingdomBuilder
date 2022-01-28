package kingdomBuilder.annotationProcessors.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a placeholder within protocol formats.
 */
public record Placeholder(
    String name,
    int offset
) {

    public int end() {
        return offset + name.length() + 1;
    }

    /**
     * Parses a format string and returns a list of placeholders.
     * @param format the format to parse.
     * @return A list of the placeholders found within the string.
     */
    public static List<Placeholder> fromFormatString(String format) {
        List<Placeholder> placeholders = new ArrayList<>();

        for(int it = 0; it < format.length(); ++it) {
            int beg = format.indexOf("#{", it);
            int end = format.indexOf("}", beg);

            if(beg == -1
                    || beg + 2 >= format.length()
                    || end == -1
                    || beg + 2 == end) break;

            String name = format.substring(beg + 2, end);
            placeholders.add(new Placeholder(name, beg));
            it = end;
        }

        return placeholders;
    }

}
