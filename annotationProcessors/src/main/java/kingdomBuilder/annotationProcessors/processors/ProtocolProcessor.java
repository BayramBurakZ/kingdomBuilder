package kingdomBuilder.annotationProcessors.processors;

import kingdomBuilder.annotationProcessors.TemplateRenderer;
import kingdomBuilder.annotationProcessors.templates.ProtocolConsumer;
import kingdomBuilder.annotationProcessors.templates.ProtocolDeserializer;
import kingdomBuilder.annotationProcessors.templates.ProtocolSerializer;
import kingdomBuilder.annotations.Protocol;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProtocolProcessor extends AbstractProcessor {
    private final static String SERVER_MESSAGE_PREFIX = "[SERVER_MESSAGE]";
    private final static String REPLY_MESSAGE_PREFIX = "[REPLY_MESSAGE]";
    private final static String GAME_MESSAGE_PREFIX = "[GAME_MESSAGE]";
    private final static String ERROR_MESSAGE_PREFIX = "[ERROR_MESSAGE]";

    private Filer filer;
    private Messager messager;

    private final Set<TypeElement> responses = new HashSet<>();
    private final Set<TypeElement> requests = new HashSet<>();

    private void sortByFormatPrefix(Element element) {
        final Protocol protocol = element.getAnnotation(Protocol.class);
        final String format = protocol.format();
        boolean isIncomingMessage = format.startsWith(SERVER_MESSAGE_PREFIX)
                                    || format.startsWith(REPLY_MESSAGE_PREFIX)
                                    || format.startsWith(GAME_MESSAGE_PREFIX)
                                    || format.startsWith(ERROR_MESSAGE_PREFIX)
                                    || protocol.isComponent();

        if(isIncomingMessage)   responses.add((TypeElement) element);
        else                    requests.add((TypeElement) element);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    private <T> void generateFromTemplate(T template, Element... originatingElements) throws IOException {
        final String name = template.getClass().getSimpleName();
        JavaFileObject file = filer.createSourceFile(name, originatingElements);
        String content = TemplateRenderer.render(template);
        try(PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.write(content);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final var elements = annotations
                .stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        if(elements.isEmpty())
            return false;

        elements.forEach(this::sortByFormatPrefix);

        try {
            generateFromTemplate(
                    new ProtocolConsumer("kingdomBuilder.network.generated", responses),
                    responses.toArray(new TypeElement[0])
            );

            generateFromTemplate(
                    new ProtocolSerializer("kingdomBuilder.network.generated", requests, processingEnv.getTypeUtils()),
                    responses.toArray(new TypeElement[0])
            );

            generateFromTemplate(
                    new ProtocolDeserializer(
                            "kingdomBuilder.network.generated",
                            responses,
                            processingEnv.getTypeUtils()),
                    responses.toArray(new TypeElement[0])
            );
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Protocol.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
