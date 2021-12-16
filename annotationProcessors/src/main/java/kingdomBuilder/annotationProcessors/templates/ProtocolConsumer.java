package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;

import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

@Template
public class ProtocolConsumer {
    private final String packageName;
    private final Set<TypeElement> elements;

    public ProtocolConsumer(String packageName, Set<TypeElement> elements) {
        this.packageName = packageName;
        this.elements = elements;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getImports() {
        return ProtocolUtil.makeImports(this.elements);
    }

    public String getHooks() {
        return elements
                .stream()
                .map(e -> String.format("void accept(%s message);", e.getSimpleName()))
                .collect(Collectors.joining("\n    "));
    }

}
