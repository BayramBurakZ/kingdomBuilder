package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.TemplateRenderer;
import kingdomBuilder.annotations.Protocol;

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
        for(TypeElement element: elements) {
            if(!element.getAnnotation(Protocol.class).isComponent())
                cases.add(makeCase(element));
        }

        return String.join("\n", cases);
    }

    public String getDeserializerMethods() throws IOException {
        List<String> methods = new ArrayList<>();
        for(TypeElement element: elements) {
            ProtocolDeserializerMethod pdm = new ProtocolDeserializerMethod(element, types);
            methods.add(TemplateRenderer.render(pdm));
        }

        return String.join("\n", methods);
    }

    private String makeCase(TypeElement element) throws IOException {
        ProtocolDeserializerCase psc = new ProtocolDeserializerCase(element, types);
        return TemplateRenderer.render(psc);
    }


}
