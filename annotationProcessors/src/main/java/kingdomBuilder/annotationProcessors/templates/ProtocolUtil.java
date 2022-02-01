package kingdomBuilder.annotationProcessors.templates;

import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

public class ProtocolUtil {

    public static String makeImports(Set<TypeElement> elements) {
        return elements
                .stream()
                .map(e -> String.format("import %s;", e.getQualifiedName()))
                .collect(Collectors.joining("\n"));
    }

}
