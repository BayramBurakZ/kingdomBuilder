package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.TemplateRenderer;

import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Template
public class ProtocolSerializer {
    private final String packageName;
    private final Set<TypeElement> elements;

    public ProtocolSerializer(String packageName, Set<TypeElement> elements) {
        this.packageName = packageName;
        this.elements = elements;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getImports() {
        return ProtocolUtil.makeImports(elements);
    }

    public String getSerializers() throws IOException {
        List<String> serializers = new ArrayList<>();
        for(TypeElement element: elements)
            serializers.add(makeSerializer(element));

        return String.join("\n    ", serializers);
    }

    private static String makeSerializer(TypeElement element) throws IOException {
        ProtocolSerializerMethod psm = new ProtocolSerializerMethod(element);
        return TemplateRenderer.render(psm);
    }
}
