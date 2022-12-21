package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Objects;

public interface MethodHandleInfo {
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_invokeInterface = 9;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;

    Class<?> getDeclaringClass();

    MethodType getMethodType();

    int getModifiers();

    String getName();

    int getReferenceKind();

    <T extends Member> T reflectAs(Class<T> cls, MethodHandles.Lookup lookup);

    boolean isVarArgs() {
        if (MethodHandleNatives.refKindIsField((byte) getReferenceKind())) {
            return false;
        }
        return Modifier.isTransient(getModifiers());
    }

    static String referenceKindToString(int i) {
        if (MethodHandleNatives.refKindIsValid(i)) {
            return MethodHandleNatives.refKindName((byte) i);
        }
        throw MethodHandleStatics.newIllegalArgumentException("invalid reference kind", Integer.valueOf(i));
    }

    static String toString(int i, Class<?> cls, String str, MethodType methodType) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(methodType);
        return String.format("%s %s.%s:%s", referenceKindToString(i), cls.getName(), str, methodType);
    }

    @Deprecated
    static boolean refKindIsValid(int i) {
        return MethodHandleNatives.refKindIsValid(i);
    }

    @Deprecated
    static boolean refKindIsField(int i) {
        return MethodHandleNatives.refKindIsField((byte) i);
    }

    @Deprecated
    static String refKindName(int i) {
        return MethodHandleNatives.refKindName((byte) i);
    }
}
