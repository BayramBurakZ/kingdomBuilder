package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.protocol.Placeholder;
import kingdomBuilder.annotationProcessors.util.TypeVisitor;
import kingdomBuilder.annotations.Protocol;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Template
public class ProtocolSerializerMethod {
    private final TypeElement element;
    private final Types types;

    public ProtocolSerializerMethod(TypeElement element, Types types) {
        this.element = element;
        this.types = types;
    }

    public String getPacketType() {
        return element.getSimpleName().toString();
    }

    public String getComponents() {
        return element
                .getRecordComponents()
                .stream()
                .map(comp -> String.format("String %s = %s;", comp.getSimpleName(), makeComponentValue(comp)))
                .collect(Collectors.joining("\n        "));
    }

    public String getBody() {
        final Protocol protocol = element.getAnnotation(Protocol.class);
        final String format = protocol.format();
        final List<Placeholder> placeholders = Placeholder.fromFormatString(format);
        final List<String> components = new ArrayList<>();

        int formatCursor = 0;
        for(Placeholder p: placeholders) {
            if(formatCursor != p.offset()) {
                final String component = String.format("\"%s\"", format.substring(formatCursor, p.offset()));
                components.add(component);
                formatCursor = p.end() + 2;
            }

            components.add(p.name());
        }

        if(!components.isEmpty() && formatCursor < format.length()) {
            String component = format.substring(formatCursor);
                   component = String.format("\"%s\"", component);
            components.add(component);
        }

        return components.isEmpty()
                ? String.format("\"%s\";", format)
                : String.join(" + ", components) + ";";
    }

    private String makeComponentValue(RecordComponentElement component) {
        final TypeKind type = component.asType().getKind();
        return switch (type) {
            case DECLARED   -> makeCompositeValue(component);
            default         -> String.format("String.valueOf(packet.%s())", component.getSimpleName());
        };
    }

    private String makeCompositeValue(RecordComponentElement component) {
        final TypeElement typeElement = getTypeElementForMirror(component.asType());
        final Class<?> cls = getClassForQualifiedName(typeElement.getQualifiedName());

        if(cls == String.class) return String.format("packet.%s()", component.getSimpleName());
        if(cls == List.class) return makeListValue(component, typeElement, cls);
        return "\"\"";
    }

    private String makeListValue(RecordComponentElement componentElement, TypeElement typeElement, Class<?> cls) {
        final var args = getGenericParameterTypes(componentElement.asType());
        final var arg = args.get(0);
        final var elementType = getClassForQualifiedName(arg.toString());

        String value = "";

        if(elementType == Integer.class) {
            value = String.format("""
packet
            .%s()
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","))
""", componentElement.getSimpleName());
        }

        return "String.format(\"{%s}\", " + value + "        )";
    }

    private Class<?> getClassForQualifiedName(Name name) {
        return getClassForQualifiedName(name.toString());
    }

    private Class<?> getClassForQualifiedName(String name) {
        try { return Class.forName(name); }
        catch(Exception exc) { return null; }
    }

    private TypeElement getTypeElementForMirror(TypeMirror typeMirror) {
        final Element elem = types.asElement(typeMirror);
        return (elem instanceof TypeElement e) ? e : null;
    }

    private static List<? extends TypeMirror> getGenericParameterTypes(TypeMirror type) {
        TypeVisitor visitor = new TypeVisitor();
        type.accept(visitor, null);
        return visitor.getTypes();
    }

}
