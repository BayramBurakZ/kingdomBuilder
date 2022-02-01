package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Template
public class DeferredState {
    private final String packageName;
    private final TypeElement element;
    private final Types types;
    private final List<? extends Element> attributes;
    private final String imports;

    public DeferredState(String packageName, TypeElement element, Types types) {
        this.packageName = packageName;
        this.element = element;
        this.types = types;
        this.attributes = element
            .getEnclosedElements()
            .stream()
            .filter(e -> e.getKind().isField())
            .toList();

        this.imports = this.attributes
            .stream()
            .filter(a -> !a.asType().getKind().isPrimitive())
            .map(a -> types.asElement(a.asType()))
            .map(Object::toString)
            .map(a -> String.format("import %s;", a))
            .sorted()
            .collect(Collectors.joining("\n"));
    }

    public String getPackageName() {
        return packageName;
    }

    public String getImports() {
        return imports + String.format("\nimport %s;", element.getQualifiedName());
    }

    public String getQualifiedClassName() {
        return element.getQualifiedName().toString();
    }

    public String getConstructionVariableDeclarations() {
        return attributes
            .stream()
            .map(encl -> String.format("%s %s;", encl.asType(), encl.getSimpleName()))
            .collect(Collectors.joining("\n    "));
    }

    public String getStateType() {
        return element.getSimpleName().toString();
    }

    public String getConstructionVariableInitialization() {
        return attributes
            .stream()
            .map(encl -> String.format("%s = oldState.%s;", encl.getSimpleName(), encl.getSimpleName()))
            .collect(Collectors.joining("\n        "));
    }

    public String getDeferredSetters() {
        return attributes
            .stream()
            .map(encl -> String.format("""
    public void set%s(%s value) {
        changedAttributes.add(\"%s\");
        %s = value;
    }
""", capitalizeFirstLetter(encl.getSimpleName().toString()), encl.asType(), encl.getSimpleName(), encl.getSimpleName()))
            .collect(Collectors.joining("\n"));
    }

    public String getConstructionVariables() {
        return attributes
            .stream()
            .map(Element::getSimpleName)
            .map(Object::toString)
            .collect(Collectors.joining(", "));
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
