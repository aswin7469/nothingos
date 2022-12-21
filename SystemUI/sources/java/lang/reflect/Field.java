package java.lang.reflect;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.lang.annotation.Annotation;
import java.util.Objects;
import libcore.reflect.AnnotatedElements;
import libcore.reflect.GenericSignatureParser;
import sun.reflect.CallerSensitive;

public final class Field extends AccessibleObject implements Member {
    private int accessFlags;
    private int artFieldIndex;
    private Class<?> declaringClass;
    private int offset;
    private Class<?> type;

    private native <A extends Annotation> A getAnnotationNative(Class<A> cls);

    private native String getNameInternal();

    private native String[] getSignatureAnnotation();

    private native boolean isAnnotationPresentNative(Class<? extends Annotation> cls);

    @CallerSensitive
    public native Object get(Object obj) throws IllegalArgumentException, IllegalAccessException;

    public native long getArtField();

    @CallerSensitive
    public native boolean getBoolean(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native byte getByte(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native char getChar(Object obj) throws IllegalArgumentException, IllegalAccessException;

    public native Annotation[] getDeclaredAnnotations();

    @CallerSensitive
    public native double getDouble(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native float getFloat(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native int getInt(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native long getLong(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native short getShort(Object obj) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void set(Object obj, Object obj2) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setBoolean(Object obj, boolean z) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setByte(Object obj, byte b) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setChar(Object obj, char c) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setDouble(Object obj, double d) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setFloat(Object obj, float f) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setInt(Object obj, int i) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setLong(Object obj, long j) throws IllegalArgumentException, IllegalAccessException;

    @CallerSensitive
    public native void setShort(Object obj, short s) throws IllegalArgumentException, IllegalAccessException;

    private Field() {
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClass;
    }

    public String getName() {
        if (!this.declaringClass.isProxy()) {
            return getNameInternal();
        }
        if ((getModifiers() & 8) != 0) {
            int i = this.artFieldIndex;
            if (i == 0) {
                return "interfaces";
            }
            if (i == 1) {
                return "throws";
            }
            throw new AssertionError((Object) "Invalid index for proxy: " + this.artFieldIndex);
        }
        throw new AssertionError((Object) "Invalid modifiers for proxy field: " + getModifiers());
    }

    public int getModifiers() {
        return this.accessFlags & 65535;
    }

    public boolean isEnumConstant() {
        return (getModifiers() & 16384) != 0;
    }

    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    public Class<?> getType() {
        return this.type;
    }

    public Type getGenericType() {
        String signatureAttribute = getSignatureAttribute();
        GenericSignatureParser genericSignatureParser = new GenericSignatureParser(this.declaringClass.getClassLoader());
        genericSignatureParser.parseForField(this.declaringClass, signatureAttribute);
        Type type2 = genericSignatureParser.fieldType;
        return type2 == null ? getType() : type2;
    }

    private String getSignatureAttribute() {
        String[] signatureAnnotation = getSignatureAnnotation();
        if (signatureAnnotation == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String append : signatureAnnotation) {
            sb.append(append);
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Field)) {
            return false;
        }
        Field field = (Field) obj;
        if (getDeclaringClass() == field.getDeclaringClass() && getName() == field.getName() && getType() == field.getType()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getName().hashCode() ^ getDeclaringClass().getName().hashCode();
    }

    public String toString() {
        String str;
        int modifiers = getModifiers();
        StringBuilder sb = new StringBuilder();
        if (modifiers == 0) {
            str = "";
        } else {
            str = Modifier.toString(modifiers) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        sb.append(str);
        sb.append(getType().getTypeName());
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(getDeclaringClass().getTypeName());
        sb.append(BaseIconCache.EMPTY_CLASS_NAME);
        sb.append(getName());
        return sb.toString();
    }

    public String toGenericString() {
        String str;
        int modifiers = getModifiers();
        Type genericType = getGenericType();
        StringBuilder sb = new StringBuilder();
        if (modifiers == 0) {
            str = "";
        } else {
            str = Modifier.toString(modifiers) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        sb.append(str);
        sb.append(genericType.getTypeName());
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(getDeclaringClass().getTypeName());
        sb.append(BaseIconCache.EMPTY_CLASS_NAME);
        sb.append(getName());
        return sb.toString();
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return getAnnotationNative(cls);
    }

    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        return AnnotatedElements.getDirectOrIndirectAnnotationsByType(this, cls);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        if (cls != null) {
            return isAnnotationPresentNative(cls);
        }
        throw new NullPointerException("annotationType == null");
    }

    public int getOffset() {
        return this.offset;
    }
}
