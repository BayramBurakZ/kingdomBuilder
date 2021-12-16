package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.protocol.Placeholder;
import kingdomBuilder.annotationProcessors.util.TypeVisitor;
import kingdomBuilder.annotations.Protocol;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Template
public class ProtocolDeserializerCase {
    private final TypeElement element;
    private final String format;
    private final String[] sections;
    private final List<Placeholder> placeholders;
    private final Types types;

    public ProtocolDeserializerCase(TypeElement element, Types types) {
        this.element = element;
        this.format = element.getAnnotation(Protocol.class).format();
        this.sections = this.format.split(" ", 3);
        this.placeholders = Placeholder.fromFormatString(this.format);
        this.types = types;
    }

    public String getCase() {
        return "\"" + sections[1] + "\"";
    }

    public String getComponentCount() {
        return String.valueOf(placeholders.size());
    }

    public String getConstructionVariables() {
        List<String> lines = new ArrayList<>();
        int idx = 0;
        for(var comp: element.getRecordComponents())
            lines.add(makeConstructionVariable(comp, idx++));

        return String.join("\n                ", lines);
    }

    public String makeConstructionVariable(RecordComponentElement component, int idx) {
        String type = "";
        try {
            Class<?> cls = Class.forName(component.asType().toString());
            type = cls.getSimpleName();
        } catch(Exception exc) {
            type = component.asType().toString();
        }

        String value = makeVariableAssignment(component, idx);

        return type + " " + component.getSimpleName() + " = " + value;
    }

    public String makeVariableAssignment(RecordComponentElement component, int chunkIndex) {
        final TypeKind type = component.asType().getKind();

        switch(type) {

            case DECLARED -> {
                TypeElement typeElement = getTypeElementForMirror(component.asType());
                if(typeElement != null) {
                    final Class<?> cls = getClassForQualifiedName(typeElement.getQualifiedName());

                    if(cls == String.class) return String.format("tokens.get(%d);", chunkIndex);
                    else if(cls == List.class) {
                        final var args = getGenericTypes(component.asType());
                        final var elementType = getClassForQualifiedName(args.get(0).toString());
                        if(elementType == Integer.class) {
                            return String.format("parseIntegerList(tokens.get(%d));", chunkIndex);
                        }

                        return "new ArrayList<>(); // " + args.get(0);
                    }
                }

                return "null; // Failed to coerce type.";
            }

            case INT -> {
                return String.format("Integer.parseInt(tokens.get(%d));", chunkIndex);
            }

            default -> {
                return "0; // " + type.toString();
            }
        }
    }

    public String getBody() {
        int componentCount = placeholders.size();
        return "";
    }

    public String getPacketType() {
        return element.getSimpleName().toString();
    }

    public String getConstructionArgs() {
        return element
                .getRecordComponents()
                .stream()
                .map(RecordComponentElement::getSimpleName)
                .collect(Collectors.joining(", "));
    }

    private static boolean isPrimitive(TypeKind kind) {
        switch(kind) {
            case BOOLEAN:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
            case CHAR:
            case BYTE:
            case INT: return true;
            default: return false;
        }
    }

    private TypeElement getTypeElementForMirror(TypeMirror typeMirror) {
        final Element elem = types.asElement(typeMirror);
        return (elem instanceof TypeElement) ? (TypeElement) elem : null;
    }

    private Class<?> getClassForQualifiedName(Name name) {
        return getClassForQualifiedName(name.toString());
    }

    private Class<?> getClassForQualifiedName(String name) {
        try { return Class.forName(name.toString()); }
        catch(Exception exc) { return null; }
    }

    private static List<? extends TypeMirror> getGenericTypes(final TypeMirror type) {
        TypeVisitor visitor = new TypeVisitor();
        type.accept(visitor, null);

        return visitor.getTypes();
    }

}
