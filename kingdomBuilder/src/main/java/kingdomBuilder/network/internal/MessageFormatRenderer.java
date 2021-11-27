package kingdomBuilder.network.internal;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MessageFormatRenderer extends MessageFormatBase {



    private static String getValueFromField(Field field, Object object) {
        try { return field.get(object).toString(); }
        catch(IllegalAccessException e) { return null; }
    }

    private static String getValueByEvaluatingMethod(Method method, Object object) {
        try { return (String) method.invoke(object); }
        catch(IllegalAccessException | InvocationTargetException e) { return null; }
    }

    private static String getValueForPlaceholder(Object object, String placeholder) {
        final Class<?> cls = object.getClass();
        final Field[] fields = cls.getFields();
        final Method[] methods = cls.getMethods();

        final Field field = Arrays
                .stream(fields)
                .filter(f -> f.getName().equals(placeholder))
                .findFirst()
                .orElse(null);

        if(field != null) return getValueFromField(field, object);

        final Method method = Arrays
                .stream(methods)
                .filter(m -> m.getName().equals(placeholder)
                        && m.getParameterCount() == 0
                        && m.getReturnType() == String.class)
                .findFirst()
                .orElseThrow(
                    () -> new RuntimeException(String.format("Failed to determine substitute for placeholder '%s'.", placeholder))
                );

        return getValueByEvaluatingMethod(method, object);
    }

    private static Map<String, String> buildPlaceholderCache(Object object, List<Placeholder> placeholders) {
        Map<String, String> cache = new HashMap<>();

        for(var p : placeholders) {
            if(cache.containsKey(p.name())) continue;
            cache.put(p.name(), getValueForPlaceholder(object, p.name()));
        }

        return cache;
    }

    private static String fillPlaceholders(String format, List<Placeholder> placeholders, Map<String, String> cache) {
        StringBuilder sb = new StringBuilder();

        for(var p: placeholders) {
            if(p.begin() != 0)
                sb.append(format, 0, p.begin());

            sb.append(cache.get(p.name()));
        }

        return sb.toString();
    }

    public <T> String render(@NotNull T message) {
        final Class<?> cls = message.getClass();
        final String format = getFormatString(cls);
        final List<Placeholder> placeholders = getPlaceholdersFromFormat(format);

        if(placeholders.isEmpty()) return format;

        final Map<String, String> cache = buildPlaceholderCache(message, placeholders);
        return fillPlaceholders(format, placeholders, cache);
    }


}
