package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import libcore.util.EmptyArray;
import sun.reflect.CallerSensitive;

public final class Constructor<T> extends Executable {
    private static final Comparator<Method> ORDER_BY_SIGNATURE = null;
    private final Class<?> serializationClass;
    private final Class<?> serializationCtor;

    private native T newInstance0(Object... objArr) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    private static native Object newInstanceFromSerialization(Class<?> cls, Class<?> cls2) throws InstantiationException, IllegalArgumentException, InvocationTargetException;

    public native Class<?>[] getExceptionTypes();

    private Constructor() {
        this((Class<?>) null, (Class<?>) null);
    }

    private Constructor(Class<?> cls, Class<?> cls2) {
        this.serializationCtor = cls;
        this.serializationClass = cls2;
    }

    public Constructor<T> serializationCopy(Class<?> cls, Class<?> cls2) {
        return new Constructor<>(cls, cls2);
    }

    /* access modifiers changed from: package-private */
    public boolean hasGenericInformation() {
        return super.hasGenericInformationInternal();
    }

    public Class<T> getDeclaringClass() {
        return super.getDeclaringClassInternal();
    }

    public String getName() {
        return getDeclaringClass().getName();
    }

    public int getModifiers() {
        return super.getModifiersInternal();
    }

    public TypeVariable<Constructor<T>>[] getTypeParameters() {
        return (TypeVariable[]) getMethodOrConstructorGenericInfoInternal().formalTypeParameters.clone();
    }

    public Class<?>[] getParameterTypes() {
        Class<?>[] parameterTypesInternal = super.getParameterTypesInternal();
        return parameterTypesInternal == null ? EmptyArray.CLASS : parameterTypesInternal;
    }

    public int getParameterCount() {
        return super.getParameterCountInternal();
    }

    public Type[] getGenericParameterTypes() {
        return super.getGenericParameterTypes();
    }

    public Type[] getGenericExceptionTypes() {
        return super.getGenericExceptionTypes();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Constructor)) {
            return false;
        }
        Constructor constructor = (Constructor) obj;
        if (getDeclaringClass() == constructor.getDeclaringClass()) {
            return equalParamTypes(getParameterTypes(), constructor.getParameterTypes());
        }
        return false;
    }

    public int hashCode() {
        return getDeclaringClass().getName().hashCode();
    }

    public String toString() {
        return sharedToString(Modifier.constructorModifiers(), false, getParameterTypes(), getExceptionTypes());
    }

    /* access modifiers changed from: package-private */
    public void specificToStringHeader(StringBuilder sb) {
        sb.append(getDeclaringClass().getTypeName());
    }

    public String toGenericString() {
        return sharedToGenericString(Modifier.constructorModifiers(), false);
    }

    /* access modifiers changed from: package-private */
    public void specificToGenericStringHeader(StringBuilder sb) {
        specificToStringHeader(sb);
    }

    @CallerSensitive
    public T newInstance(Object... objArr) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> cls = this.serializationClass;
        if (cls == null) {
            return newInstance0(objArr);
        }
        return newInstanceFromSerialization(this.serializationCtor, cls);
    }

    public boolean isVarArgs() {
        return super.isVarArgs();
    }

    public boolean isSynthetic() {
        return super.isSynthetic();
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        return super.getAnnotation(cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        return super.getDeclaredAnnotations();
    }

    public Annotation[][] getParameterAnnotations() {
        return super.getParameterAnnotationsInternal();
    }
}
