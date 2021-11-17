package kingdomBuilder.network.internal;

import java.util.*;

public class ProtocolMapper {

    private static List<String> getPlaceholders(String format) {
        List<String> placeholders = new ArrayList<>();

        for(int it = 0; it < format.length() && it != -1; ++it) {
            int beg = format.indexOf("#{", it);
            if(beg == -1 || beg + 2 >= format.length()) break;

            beg += 2;
            int end = format.indexOf("}", beg);
            if(end == -1) break;

            if(beg != end)
                placeholders.add(format.substring(beg, end));

            it = end;
        }

        return placeholders;
    }

    private String formatString(String format, HashMap<String, String> args) {
        return "";
    }

    public String mapToString(Object object) {
        final Class<?> cls = object.getClass();

        if(!cls.isAnnotationPresent(Message.class))
            throw new RuntimeException("Message objects must be annotated with @Message.");

        final Message meta = cls.getAnnotation(Message.class);
        final String format = meta.format();
        final List<String> placeholders = getPlaceholders(format);

        final var fields = cls.getFields();
        final var methods = cls.getMethods();
        final HashMap<String, String> placeholderCache = new HashMap<>();

        for(var placeholder: placeholders) {
            if(placeholderCache.containsKey(placeholder)) continue;

            var optField = Arrays
                .stream(fields)
                .filter(f -> f.getName().equals(placeholder))
                .findFirst();

            if(optField.isPresent()) {
                try {
                    String val = optField.get().get(object).toString();
                    placeholderCache.put(placeholder, val);
                } catch(Exception exc) {

                }

                continue;
            }

            var optMethod = Arrays
                    .stream(methods)
                    .filter(f -> f.getName().equals(placeholder)
                            && f.getParameterCount() == 0
                            && f.getReturnType() == String.class)
                    .findFirst();

            if(optMethod.isPresent()) {
                try {
                    String val = (String) optMethod.get().invoke(object);
                    placeholderCache.put(placeholder, val);
                } catch(Exception exc) {

                }
            }
        }

        return formatString(format, placeholderCache);
    }

    public Object mapFromString(String string) {
        return null;
    }

}
