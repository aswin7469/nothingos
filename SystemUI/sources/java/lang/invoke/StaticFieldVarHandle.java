package java.lang.invoke;

import java.lang.reflect.Field;

final class StaticFieldVarHandle extends FieldVarHandle {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Class declaringClass;

    private StaticFieldVarHandle(Field field) {
        super(field);
        this.declaringClass = field.getDeclaringClass();
    }

    static StaticFieldVarHandle create(Field field) {
        return new StaticFieldVarHandle(field);
    }
}
