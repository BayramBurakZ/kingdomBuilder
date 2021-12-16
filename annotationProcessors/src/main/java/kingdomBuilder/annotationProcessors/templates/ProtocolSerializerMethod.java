package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;

import javax.lang.model.element.TypeElement;

@Template
public class ProtocolSerializerMethod {
    private final TypeElement element;

    public ProtocolSerializerMethod(TypeElement element) {
        this.element = element;
    }

    public String getName() {
        return String.valueOf(element.getSimpleName());
    }
}
