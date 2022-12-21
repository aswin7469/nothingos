package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.util.Objects;
import libcore.reflect.AnnotatedElements;

public final class Parameter implements AnnotatedElement {
    private final Executable executable;
    private final int index;
    private final int modifiers;
    private final String name;
    private volatile transient Class<?> parameterClassCache = null;
    private volatile transient Type parameterTypeCache;

    private static native <A extends Annotation> A getAnnotationNative(Executable executable2, int i, Class<A> cls);

    Parameter(String str, int i, Executable executable2, int i2) {
        this.name = str;
        this.modifiers = i;
        this.executable = executable2;
        this.index = i2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Parameter)) {
            return false;
        }
        Parameter parameter = (Parameter) obj;
        if (!parameter.executable.equals(this.executable) || parameter.index != this.index) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.index ^ this.executable.hashCode();
    }

    public boolean isNamePresent() {
        return this.executable.hasRealParameterData() && this.name != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String typeName = getParameterizedType().getTypeName();
        sb.append(Modifier.toString(getModifiers()));
        if (this.modifiers != 0) {
            sb.append(' ');
        }
        if (isVarArgs()) {
            sb.append(typeName.replaceFirst("\\[\\]$", "..."));
        } else {
            sb.append(typeName);
        }
        sb.append(' ');
        sb.append(getName());
        return sb.toString();
    }

    public Executable getDeclaringExecutable() {
        return this.executable;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public String getName() {
        String str = this.name;
        if (str != null && !str.equals("")) {
            return this.name;
        }
        return "arg" + this.index;
    }

    /* access modifiers changed from: package-private */
    public String getRealName() {
        return this.name;
    }

    public Type getParameterizedType() {
        Type type = this.parameterTypeCache;
        if (type != null) {
            return type;
        }
        Type type2 = this.executable.getAllGenericParameterTypes()[this.index];
        this.parameterTypeCache = type2;
        return type2;
    }

    public Class<?> getType() {
        Class<?> cls = this.parameterClassCache;
        if (cls != null) {
            return cls;
        }
        Class<?> cls2 = this.executable.getParameterTypes()[this.index];
        this.parameterClassCache = cls2;
        return cls2;
    }

    public boolean isImplicit() {
        return Modifier.isMandated(getModifiers());
    }

    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    public boolean isVarArgs() {
        return this.executable.isVarArgs() && this.index == this.executable.getParameterCount() - 1;
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return getAnnotationNative(this.executable, this.index, cls);
    }

    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        return AnnotatedElements.getDirectOrIndirectAnnotationsByType(this, cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        return this.executable.getParameterAnnotations()[this.index];
    }

    public <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        return getAnnotation(cls);
    }

    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        return getAnnotationsByType(cls);
    }

    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }
}
