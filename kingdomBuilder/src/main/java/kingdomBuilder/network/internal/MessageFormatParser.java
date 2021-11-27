package kingdomBuilder.network.internal;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class MessageFormatParser extends MessageFormatBase {

    static private <T> Class<?> getTypeOfPlaceholder(Class<T> cls, String name) {
        Class<?> type = Arrays
            .stream(cls.getDeclaredFields())
            .filter(f -> f.getName().equals(name))
            .map(Field::getType)
            .findFirst()
            .orElse(null);

        return type;
    }

    public <T> T parseTo(@NotNull String message, @NotNull Class<T> cls) {
        final String format = getFormatString(cls);
        final List<Placeholder> placeholders = getPlaceholdersFromFormat(format);
        final Map<String, Object> constructionArgsCache = new HashMap<>();
        int nextPlaceholder = 0;

        for(int it = 0, jt = 0; it < message.length() && jt < format.length(); ++it, ++jt) {
            if(message.charAt(it) == format.charAt(jt)) continue;
            if(!format.startsWith("#{", jt)) return null;

            final Placeholder currentPlaceholder = placeholders.get(nextPlaceholder++);
            final Class<?> type = getTypeOfPlaceholder(cls, currentPlaceholder.name());

            char delimiter = format.charAt(currentPlaceholder.end() + 1);
            int end = message.indexOf(delimiter, it);

            if(type == int.class) {
                int val = Integer.parseInt(message, it, end, 10);
                constructionArgsCache.put(currentPlaceholder.name(), val);
            } else if(type == String.class) {
                String val = message.substring(it, end);
                constructionArgsCache.put(currentPlaceholder.name(), val);
            }

            it = end;
            jt = currentPlaceholder.end() + 1;
        }

        final List<Object> args = new ArrayList<>();
        var ctor = cls.getConstructors()[0];
        for(var p : ctor.getParameters())
            args.add(constructionArgsCache.get(p.getName()));

        try {
            return (T) ctor.newInstance(args.toArray());
        } catch(Exception e) {
            return null;
        }
    }

}
