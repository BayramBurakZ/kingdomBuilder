package kingdomBuilder.annotationProcessors.processors;

import kingdomBuilder.annotationProcessors.TemplateRenderer;
import kingdomBuilder.annotationProcessors.templates.DeferredState;
import kingdomBuilder.annotations.State;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

public class StateProcessor extends AbstractProcessor {
    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(State.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final var elements = annotations
                .stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        System.out.println("HERE");

        if(elements.isEmpty()) return false;
        if(elements.size() > 1) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Only one type may be annotated with @State per module."
            );

            return true;
        }

        TypeElement element = (TypeElement) elements.iterator().next();
        if(element.getKind() != ElementKind.RECORD) {
            messager.printMessage(
                Diagnostic.Kind.MANDATORY_WARNING,
            "Types annotated with @State should be records."
            );
        }

        try {
            final DeferredState stateTemplate = new DeferredState(
                    "kingdomBuilder.generated",
                    element,
                    processingEnv.getTypeUtils()
            );

            JavaFileObject file = processingEnv.getFiler().createSourceFile("DeferredState", element);
            String content = TemplateRenderer.render(stateTemplate);
            try(PrintWriter writer = new PrintWriter(file.openWriter())) {
                writer.write(content);
            }
        } catch(Exception exc) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "An exception occurred while processing @State: " + exc.toString()
            );
        }

        return true;
    }
}
