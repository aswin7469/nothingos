package java.lang.reflect;

import java.lang.annotation.Annotation;

public class AccessibleObject implements AnnotatedElement {
    boolean override;

    public static void setAccessible(AccessibleObject[] accessibleObjectArr, boolean z) throws SecurityException {
        for (AccessibleObject accessible0 : accessibleObjectArr) {
            setAccessible0(accessible0, z);
        }
    }

    public void setAccessible(boolean z) throws SecurityException {
        setAccessible0(this, z);
    }

    private static void setAccessible0(AccessibleObject accessibleObject, boolean z) throws SecurityException {
        if ((accessibleObject instanceof Constructor) && z) {
            Class<Field> declaringClass = ((Constructor) accessibleObject).getDeclaringClass();
            if (declaringClass == Class.class) {
                throw new SecurityException("Can not make a java.lang.Class constructor accessible");
            } else if (declaringClass == Method.class) {
                throw new SecurityException("Can not make a java.lang.reflect.Method constructor accessible");
            } else if (declaringClass == Field.class) {
                throw new SecurityException("Can not make a java.lang.reflect.Field constructor accessible");
            }
        }
        accessibleObject.override = z;
    }

    public boolean isAccessible() {
        return this.override;
    }

    protected AccessibleObject() {
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        throw new AssertionError((Object) "All subclasses should override this method");
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return super.isAnnotationPresent(cls);
    }

    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        throw new AssertionError((Object) "All subclasses should override this method");
    }

    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    public <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        return getAnnotation(cls);
    }

    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        return getAnnotationsByType(cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        throw new AssertionError((Object) "All subclasses should override this method");
    }
}
