package kingdomBuilder.network.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MessageMapper {

    private record Placeholder
    (
        String name,
        int begin,
        int end
    ){};

    private static String getFormatString(Object object) {
        if(!object.getClass().isAnnotationPresent(Message.class))
            throw new RuntimeException("Data transfer objects must be annotated with @Message.");

        String fmt = object.getClass().getAnnotation(Message.class).format();
        if(fmt.isEmpty())
            throw new RuntimeException("Data transfer objects must define non-empty format.");

        return fmt;
    }

    private static List<Placeholder> getPlaceholdersFromFormat(String format) {
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

    private static String getValueFromField(Field field, Object object) {
        try {
            return field.get(object).toString();
        } catch(IllegalAccessException e) {
            return null;
        }
    }

    private static String evaluateMethod(Method method, Object object) {
        try {
            return (String) method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private static String getValueForPlaceholder(Object object, String placeholder) {
        final Class<?> cls = object.getClass();
        final Field[] fields = cls.getFields();
        final Method[] methods = cls.getMethods();

        final var field = Arrays
            .stream(fields)
            .filter(f -> f.getName().equals(placeholder))
            .findFirst();

        if(field.isPresent()) return getValueFromField(field.get(), object);

        final var method = Arrays
                .stream(methods)
                .filter(m -> m.getName().equals(placeholder) && m.getParameterCount() == 0 && m.getReturnType() == String.class)
                .findFirst();

        if(method.isPresent()) return evaluateMethod(method.get(), object);

        throw new RuntimeException(String.format("Failed to determine substitute for placeholder '%s'.",placeholder));
    }

    private static HashMap<String, String> buildPlaceholderCache(Object object, List<Placeholder> placeholders) {
        HashMap<String, String> cache = new HashMap<>();
        final Class<?> cls = object.getClass();

        for(var p : placeholders) {
            if(cache.containsKey(p.name)) continue;
            cache.put(p.name, getValueForPlaceholder(object, p.name));
        }

        return cache;
    }

    private static String fillPlaceholders(String format, List<Placeholder> placeholders, HashMap<String, String> cache) {
        StringBuilder sb = new StringBuilder();

        for(var p: placeholders) {
            int beg = p.begin;
            int end = p.end;

            if(p.begin != 0)
                sb.append(format.substring(0, p.begin));

            sb.append(cache.get(p.name));
        }

        return sb.toString();
    }

    public static String mapToString(Object object) {
        var format = getFormatString(object);
        var placeholders = getPlaceholdersFromFormat(format);

        if(placeholders.isEmpty())
            return format;

        var cache = buildPlaceholderCache(object, placeholders);
        return fillPlaceholders(format, placeholders, cache);
    }

}
