package libcore.reflect;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Arrays;

public final class AnnotationMember implements Serializable {
    protected static final char ARRAY = '[';
    protected static final char ERROR = '!';
    protected static final Object NO_VALUE = DefaultValues.NO_VALUE;
    protected static final char OTHER = '*';
    protected transient Method definingMethod;
    protected transient Class<?> elementType;
    protected final String name;
    protected final char tag;
    protected final Object value;

    private enum DefaultValues {
        NO_VALUE
    }

    public AnnotationMember(String str, Object obj) {
        this.name = str;
        obj = obj == null ? NO_VALUE : obj;
        this.value = obj;
        if (obj instanceof Throwable) {
            this.tag = ERROR;
        } else if (obj.getClass().isArray()) {
            this.tag = ARRAY;
        } else {
            this.tag = OTHER;
        }
    }

    public AnnotationMember(String str, Object obj, Class cls, Method method) {
        this(str, obj);
        this.definingMethod = method;
        if (cls == Integer.TYPE) {
            this.elementType = Integer.class;
        } else if (cls == Boolean.TYPE) {
            this.elementType = Boolean.class;
        } else if (cls == Character.TYPE) {
            this.elementType = Character.class;
        } else if (cls == Float.TYPE) {
            this.elementType = Float.class;
        } else if (cls == Double.TYPE) {
            this.elementType = Double.class;
        } else if (cls == Long.TYPE) {
            this.elementType = Long.class;
        } else if (cls == Short.TYPE) {
            this.elementType = Short.class;
        } else if (cls == Byte.TYPE) {
            this.elementType = Byte.class;
        } else {
            this.elementType = cls;
        }
    }

    /* access modifiers changed from: protected */
    public AnnotationMember setDefinition(AnnotationMember annotationMember) {
        this.definingMethod = annotationMember.definingMethod;
        this.elementType = annotationMember.elementType;
        return this;
    }

    public String toString() {
        if (this.tag == '[') {
            StringBuilder sb = new StringBuilder(80);
            sb.append(this.name);
            sb.append("=[");
            int length = Array.getLength(this.value);
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(Array.get(this.value, i));
            }
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
            return sb.toString();
        }
        return this.name + "=" + this.value;
    }

    public boolean equals(Object obj) {
        char c;
        if (obj == this) {
            return true;
        }
        if (obj instanceof AnnotationMember) {
            AnnotationMember annotationMember = (AnnotationMember) obj;
            if (this.name.equals(annotationMember.name) && (c = this.tag) == annotationMember.tag) {
                if (c == '[') {
                    return equalArrayValue(annotationMember.value);
                }
                if (c == '!') {
                    return false;
                }
                return this.value.equals(annotationMember.value);
            }
        }
        return false;
    }

    public boolean equalArrayValue(Object obj) {
        Object obj2 = this.value;
        if ((obj2 instanceof Object[]) && (obj instanceof Object[])) {
            return Arrays.equals((Object[]) obj2, (Object[]) obj);
        }
        Class<?> cls = obj2.getClass();
        if (cls != obj.getClass()) {
            return false;
        }
        if (cls == int[].class) {
            return Arrays.equals((int[]) this.value, (int[]) obj);
        }
        if (cls == byte[].class) {
            return Arrays.equals((byte[]) this.value, (byte[]) obj);
        }
        if (cls == short[].class) {
            return Arrays.equals((short[]) this.value, (short[]) obj);
        }
        if (cls == long[].class) {
            return Arrays.equals((long[]) this.value, (long[]) obj);
        }
        if (cls == char[].class) {
            return Arrays.equals((char[]) this.value, (char[]) obj);
        }
        if (cls == boolean[].class) {
            return Arrays.equals((boolean[]) this.value, (boolean[]) obj);
        }
        if (cls == float[].class) {
            return Arrays.equals((float[]) this.value, (float[]) obj);
        }
        if (cls == double[].class) {
            return Arrays.equals((double[]) this.value, (double[]) obj);
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int hashCode2 = this.name.hashCode() * 127;
        if (this.tag == '[') {
            Class<?> cls = this.value.getClass();
            if (cls == int[].class) {
                hashCode = Arrays.hashCode((int[]) this.value);
            } else if (cls == byte[].class) {
                hashCode = Arrays.hashCode((byte[]) this.value);
            } else if (cls == short[].class) {
                hashCode = Arrays.hashCode((short[]) this.value);
            } else if (cls == long[].class) {
                hashCode = Arrays.hashCode((long[]) this.value);
            } else if (cls == char[].class) {
                hashCode = Arrays.hashCode((char[]) this.value);
            } else if (cls == boolean[].class) {
                hashCode = Arrays.hashCode((boolean[]) this.value);
            } else if (cls == float[].class) {
                hashCode = Arrays.hashCode((float[]) this.value);
            } else if (cls == double[].class) {
                hashCode = Arrays.hashCode((double[]) this.value);
            } else {
                hashCode = Arrays.hashCode((Object[]) this.value);
            }
        } else {
            hashCode = this.value.hashCode();
        }
        return hashCode ^ hashCode2;
    }

    public void rethrowError() throws Throwable {
        int i;
        if (this.tag == '!') {
            Object obj = this.value;
            if (obj instanceof TypeNotPresentException) {
                TypeNotPresentException typeNotPresentException = (TypeNotPresentException) obj;
                throw new TypeNotPresentException(typeNotPresentException.typeName(), typeNotPresentException.getCause());
            } else if (obj instanceof EnumConstantNotPresentException) {
                EnumConstantNotPresentException enumConstantNotPresentException = (EnumConstantNotPresentException) obj;
                throw new EnumConstantNotPresentException(enumConstantNotPresentException.enumType(), enumConstantNotPresentException.constantName());
            } else if (!(obj instanceof ArrayStoreException)) {
                Throwable th = (Throwable) obj;
                StackTraceElement[] stackTrace = th.getStackTrace();
                if (stackTrace == null) {
                    i = 512;
                } else {
                    i = (stackTrace.length + 1) * 80;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(th);
                objectOutputStream.flush();
                objectOutputStream.close();
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                Throwable th2 = (Throwable) objectInputStream.readObject();
                objectInputStream.close();
                throw th2;
            } else {
                throw new ArrayStoreException(((ArrayStoreException) obj).getMessage());
            }
        }
    }

    public Object validateValue() throws Throwable {
        if (this.tag == '!') {
            rethrowError();
        }
        Object obj = this.value;
        if (obj == NO_VALUE) {
            return null;
        }
        if (this.elementType == obj.getClass() || this.elementType.isInstance(this.value)) {
            return copyValue();
        }
        throw new AnnotationTypeMismatchException(this.definingMethod, this.value.getClass().getName());
    }

    public Object copyValue() throws Throwable {
        if (this.tag != '[' || Array.getLength(this.value) == 0) {
            return this.value;
        }
        Class<?> cls = this.value.getClass();
        if (cls == int[].class) {
            return ((int[]) this.value).clone();
        }
        if (cls == byte[].class) {
            return ((byte[]) this.value).clone();
        }
        if (cls == short[].class) {
            return ((short[]) this.value).clone();
        }
        if (cls == long[].class) {
            return ((long[]) this.value).clone();
        }
        if (cls == char[].class) {
            return ((char[]) this.value).clone();
        }
        if (cls == boolean[].class) {
            return ((boolean[]) this.value).clone();
        }
        if (cls == float[].class) {
            return ((float[]) this.value).clone();
        }
        if (cls == double[].class) {
            return ((double[]) this.value).clone();
        }
        return ((Object[]) this.value).clone();
    }
}
