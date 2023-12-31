package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.TemplateRenderer;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Template
public class ProtocolSerializer {
    private final String packageName;
    private final Set<TypeElement> elements;
    private final Types types;

    public ProtocolSerializer(String packageName, Set<TypeElement> elements, Types types) {
        this.packageName = packageName;
        this.elements = elements;
        this.types = types;
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

        return String.join("\n", serializers);
    }

    private String makeSerializer(TypeElement element) throws IOException {
        ProtocolSerializerMethod psm = new ProtocolSerializerMethod(element, types);
        return TemplateRenderer.render(psm);
    }
}
