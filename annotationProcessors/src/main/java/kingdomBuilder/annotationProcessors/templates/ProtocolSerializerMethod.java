package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.protocol.Placeholder;
import kingdomBuilder.annotations.Protocol;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

@Template
public class ProtocolSerializerMethod {
    private final TypeElement element;

    public ProtocolSerializerMethod(TypeElement element) {
        this.element = element;
    }

    public String getName() {
        return String.valueOf(element.getSimpleName());
    }

    public String getBody() {
        final String format = element.getAnnotation(Protocol.class).format();
        final List<Placeholder> placeholders = Placeholder.fromFormatString(format);
        final List<String> components = new ArrayList<>();

        int formatCursor = 0;
        for(Placeholder p: placeholders) {
            if(formatCursor != p.offset()) {
                String component = format.substring(formatCursor, p.offset());
                       component = String.format("\"%s\"", component);
                components.add(component);
                formatCursor = p.end() + 1;
            }

            String component = "packet." + p.name() + "()";
            components.add(component);
        }

        if(components.isEmpty())
            components.add(String.format("\"%s\"", format));

        return String.join(" + ", components) + ";";
    }

}
