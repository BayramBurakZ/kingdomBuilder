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
public class ProtocolDeserializer {

    private final String packageName;
    private final Set<TypeElement> elements;
    private final Types types;

    public ProtocolDeserializer(String packageName, Set<TypeElement> elements, Types types) {
        this.packageName = packageName;
        this.elements = elements;
        this.types = types;
    }

    public String getPackageName() { return this.packageName; }

    public String getImports() { return ProtocolUtil.makeImports(elements); }

    public String getCases() throws IOException {
        List<String> cases = new ArrayList<>();
        for(TypeElement element: elements)
            cases.add(makeCase(element));

        return String.join("\n", cases);
    }

    private String makeCase(TypeElement element) throws IOException {
        ProtocolDeserializerCase psc = new ProtocolDeserializerCase(element, types);
        return TemplateRenderer.render(psc);
    }

}
