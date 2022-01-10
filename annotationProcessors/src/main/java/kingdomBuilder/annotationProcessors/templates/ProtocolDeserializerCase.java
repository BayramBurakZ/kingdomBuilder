package kingdomBuilder.annotationProcessors.templates;

import kingdomBuilder.annotationProcessors.Template;
import kingdomBuilder.annotationProcessors.protocol.Placeholder;
import kingdomBuilder.annotationProcessors.util.TypeVisitor;
import kingdomBuilder.annotations.Protocol;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Template
public class ProtocolDeserializerCase {
    private final TypeElement element;
    private final String messageType;

    public ProtocolDeserializerCase(TypeElement element, Types types) {
        this.element = element;
        final String format = element.getAnnotation(Protocol.class).format();
        this.messageType = format.split(" ", 3)[1];
    }

    public String getCase() {
        return String.format("\"%s\"", this.messageType);
    }

    public String getPacketType() {
        return element.getSimpleName().toString();
    }

    public String getDeserializeParam() {
        return element.getRecordComponents().size() > 0
                ? "sections[2]"
                : "";
    }

}
