package kingdomBuilder.network.internal;

import kingdomBuilder.network.protocol.tuples.ClientTuple;
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

    public Object[] processArray(Class<?> type, String payload) {
        String[] values = payload.split(",");

        if(type.getComponentType() == Integer.class)
            return Arrays
                    .stream(values)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .toArray(Integer[]::new);

        if(type.getComponentType() == ClientTuple.class) {
            // [client_id;client_name;game_id]
            ClientTuple[] clients = new ClientTuple[values.length];
            for (int i = 0; i < clients.length; i++) {
                clients[i] = parseTo(values[i], ClientTuple.class);
            }
            return clients;
        }

        return null;
    }

    public <T> T parseTo(@NotNull String message, @NotNull Class<T> cls) {
        final String format = getFormatString(cls); // "#{clientId};#{name};#{gameId}"
        final List<Placeholder> placeholders = getPlaceholdersFromFormat(format);
        final Map<String, Object> cache = new HashMap<>();
        int nextPlaceholder = 0;

        for(int it = 0, jt = 0; it < message.length() && jt < format.length(); ++it, ++jt) {
            if(message.charAt(it) == format.charAt(jt)) continue;
            if(!format.startsWith("#{", jt)) return null;

            final Placeholder currentPlaceholder = placeholders.get(nextPlaceholder++);
            final Class<?> type = getTypeOfPlaceholder(cls, currentPlaceholder.name());

            // TODO: check separately for chat messages, because they might contain delimiter ']'
            //  set end to last character explicitly for chat messages
            char delimiter = format.charAt(currentPlaceholder.end() + 1);
            int end = message.indexOf(delimiter, it);

            Object value = null;
            if(type == int.class)
                value = Integer.parseInt(message, it, end, 10);
            else if(type == String.class)
                value = message.substring(it, end);
            else if(type.isArray()) {
                value = processArray(type, message.substring(it + 1, end - 1));
            }

            cache.put(currentPlaceholder.name(), value);

            it = end;
            jt = currentPlaceholder.end() + 1;
        }

        final List<Object> args = new ArrayList<>();
        var ctor = cls.getConstructors()[0];
        Arrays.stream(ctor.getParameters()).forEach(p -> args.add(cache.get(p.getName())));

        try { return (T) ctor.newInstance(args.toArray()); }
        catch(Exception e) { return null; }
    }

}
