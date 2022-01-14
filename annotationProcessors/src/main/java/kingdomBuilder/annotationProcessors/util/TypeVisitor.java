package kingdomBuilder.annotationProcessors.util;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import java.util.ArrayList;
import java.util.List;

public class TypeVisitor implements javax.lang.model.type.TypeVisitor<Void, Void> {
    private final List<Element> parameters = new ArrayList<>();
    private List<? extends TypeMirror> types;

    public List<? extends TypeMirror> getTypes() {
        return types;
    }

    @Override
    public Void visit(TypeMirror t, Void unused) {
        return null;
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, Void unused) {
        return null;
    }

    @Override
    public Void visitNull(NullType t, Void unused) {
        return null;
    }

    @Override
    public Void visitArray(ArrayType t, Void unused) {
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Void unused) {
        types = t.getTypeArguments();
        return null;
    }

    @Override
    public Void visitError(ErrorType t, Void unused) {
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Void unused) {
        parameters.add(t.asElement());
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Void unused) {
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Void unused) {
        return null;
    }

    @Override
    public Void visitNoType(NoType t, Void unused) {
        return null;
    }

    @Override
    public Void visitUnknown(TypeMirror t, Void unused) {
        return null;
    }

    @Override
    public Void visitUnion(UnionType t, Void unused) {
        return null;
    }

    @Override
    public Void visitIntersection(IntersectionType t, Void unused) {
        return null;
    }
}
