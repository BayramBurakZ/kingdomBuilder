package kingdomBuilder.network.internal;

import java.util.ArrayList;
import java.util.List;

class MessageFormatBase {

    protected record Placeholder
    (
        String name,
        int begin,
        int end
    ){};

    protected static String getFormatString(Class<?> cls) {
        if(!cls.isAnnotationPresent(MessageFormat.class))
            throw new RuntimeException("Data transfer objects must be annotated with @Message.");

        String fmt = cls.getAnnotation(MessageFormat.class).format();
        if(fmt.isEmpty())
            throw new RuntimeException("Data transfer objects must define non-empty format.");

        return fmt;
    }

    protected static List<Placeholder> getPlaceholdersFromFormat(String format) {
        List<Placeholder> placeholders = new ArrayList<>();

        for(int it = 0; it < format.length(); ++it) {
            int beg = format.indexOf("#{", it);
            int end = format.indexOf("}", beg);

            if(beg == -1
                || beg + 2 >= format.length()
                || end == -1
                || beg + 2 == end) break;

            String name = format.substring(beg + 2, end);
            placeholders.add(new Placeholder(name, beg, end));
            it = end;
        }

        return placeholders;
    }

}
