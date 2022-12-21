package java.lang.invoke;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class FieldVarHandle extends VarHandle {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final long artField;

    private FieldVarHandle(Field field, Class<?> cls) {
        super(field.getType(), Modifier.isFinal(field.getModifiers()), cls);
        this.artField = field.getArtField();
    }

    protected FieldVarHandle(Field field) {
        super(field.getType(), Modifier.isFinal(field.getModifiers()));
        this.artField = field.getArtField();
    }

    static FieldVarHandle create(Field field) {
        return new FieldVarHandle(field, field.getDeclaringClass());
    }
}
