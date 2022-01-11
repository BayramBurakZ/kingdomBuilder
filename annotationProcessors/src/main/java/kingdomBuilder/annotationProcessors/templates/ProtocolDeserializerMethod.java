package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.util.TypeVisitor;
import kingdomBuilder.annotations.Protocol;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Template
public class ProtocolDeserializerMethod {
    private final Set<TypeElement> elements;
    private final TypeElement element;
    private final Types types;

    public ProtocolDeserializerMethod(Set<TypeElement> elements, TypeElement element, Types types) {
        this.elements = elements;
        this.element = element;
        this.types = types;
    }

    public String getPacketType() {
        return element.getSimpleName().toString();
    }

    public String getQualifiedPacketType() { return element.getQualifiedName().toString(); }

    public String getDeserializerParam() { return element.getRecordComponents().size() > 0 ? "String payload" : ""; }

    public String getPayloadPreprocessing() {
        return element.getRecordComponents().size() > 0
                ? "List<String> tokens = split(payload, " + getComponentCount() + ");"
                : "";
    }

    public String getConstructionVariables() {
        List<String> lines = new ArrayList<>();
        int idx = 0;
        for(var comp: element.getRecordComponents())
            lines.add(makeConstructionVariable(comp, idx++));

        return String.join("\n        ", lines);
    }

    public String getConstructionArgs() {
        return element
                .getRecordComponents()
                .stream()
                .map(RecordComponentElement::getSimpleName)
                .collect(Collectors.joining(", "));
    }

    public String getComponentCount() {
        return String.valueOf(element.getRecordComponents().size());
    }

    private String makeConstructionVariable(RecordComponentElement component, int idx) {
        Class<?> cls = getClassForQualifiedName(component.asType().toString());
        String type = cls != null ? cls.getSimpleName() : component.asType().toString();
        String value = makeConstructionVariableValue(component, cls, idx);

        return type + " " + component.getSimpleName() + " = " + value + ";";
    }

    private String makeConstructionVariableValue(RecordComponentElement component, Class<?> cls, int idx) {
        final TypeKind type = component.asType().getKind();
        switch (type) {
            case DECLARED: {
                TypeElement typeElement = getTypeElementForMirror(component.asType());
                if(typeElement != null) {
                    if(cls == null)
                        cls = getClassForQualifiedName(typeElement.getQualifiedName());

                    if(cls == String.class) return String.format("tokens.get(%d)", idx);
                    else if(cls == List.class) return makeListValue(component, cls, idx);
                    else if(elements.contains(typeElement)) {
                        return String.format("deserialize%s(tokens.get(%d))", typeElement.getSimpleName(), idx);
                    }
                }

                return "null";
            }

            case INT: return String.format("Integer.parseInt(tokens.get(%d))", idx);
            default: return "0";
        }
    }

    private String makeListValue(RecordComponentElement component, Class<?> cls, int idx) {
        final var args = getGenericParameterTypes(component.asType());
        final var arg = args.get(0);
        final var elementType = getClassForQualifiedName(arg.toString());

        if(elementType == Integer.class) return String.format("parseIntegerList(tokens.get(%d));", idx);

        // Check whether the argument is a component (Protocol.is_component = true).
        final TypeElement arg_element = elements
                .stream()
                .filter(elem -> elem.asType().equals(arg))
                .findFirst()
                .orElse(null);

        if(arg_element != null) {
            String name = arg_element.getSimpleName().toString();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            return String.format("""
splitList(tokens.get(%d))
            .stream()
            .map(ProtocolDeserializer::deserialize%s)
            .toList()""", idx, name);
        }

        return "new ArrayList<>();";
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
