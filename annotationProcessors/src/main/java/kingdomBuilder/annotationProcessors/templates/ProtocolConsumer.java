package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotations.Protocol;

import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Template
public class ProtocolConsumer {
    private final String packageName;
    private final Set<TypeElement> elements;

    public ProtocolConsumer(String packageName, Set<TypeElement> elements) {
        this.packageName = packageName;
        this.elements = elements;
    }

    /**
     * {@return Returns the package URI to which the generated source belongs.}
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * {@return Returns all imports required for the generated source.}
     */
    public String getImports() {
        return ProtocolUtil.makeImports(this.elements);
    }

    /**
     * {@return Returns all method declaration for the interface, where each method corresponds to an outgoing packet.}
     */
    public String getHooks() {
        return elements
                .stream()
                .filter(e -> !e.getAnnotation(Protocol.class).isComponent())
                .map(e -> String.format("void accept(%s message);", e.getSimpleName()))
                .collect(Collectors.joining("\n    "));
    }

}
