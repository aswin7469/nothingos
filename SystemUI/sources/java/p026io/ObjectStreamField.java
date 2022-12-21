package java.p026io;

import java.lang.reflect.Field;
import sun.reflect.CallerSensitive;

/* renamed from: java.io.ObjectStreamField */
public class ObjectStreamField implements Comparable<Object> {
    private final Field field;
    private final String name;
    private int offset;
    private final String signature;
    private final Class<?> type;
    private final boolean unshared;

    public ObjectStreamField(String str, Class<?> cls) {
        this(str, cls, false);
    }

    public ObjectStreamField(String str, Class<?> cls, boolean z) {
        this.offset = 0;
        str.getClass();
        this.name = str;
        this.type = cls;
        this.unshared = z;
        this.signature = getClassSignature(cls).intern();
        this.field = null;
    }

    ObjectStreamField(String str, String str2, boolean z) {
        this.offset = 0;
        str.getClass();
        this.name = str;
        this.signature = str2.intern();
        this.unshared = z;
        this.field = null;
        char charAt = str2.charAt(0);
        if (charAt != 'F') {
            if (charAt != 'L') {
                if (charAt == 'S') {
                    this.type = Short.TYPE;
                    return;
                } else if (charAt == 'I') {
                    this.type = Integer.TYPE;
                    return;
                } else if (charAt == 'J') {
                    this.type = Long.TYPE;
                    return;
                } else if (charAt == 'Z') {
                    this.type = Boolean.TYPE;
                    return;
                } else if (charAt != '[') {
                    switch (charAt) {
                        case 'B':
                            this.type = Byte.TYPE;
                            return;
                        case 'C':
                            this.type = Character.TYPE;
                            return;
                        case 'D':
                            this.type = Double.TYPE;
                            return;
                        default:
                            throw new IllegalArgumentException("illegal signature");
                    }
                }
            }
            this.type = Object.class;
            return;
        }
        this.type = Float.TYPE;
    }

    ObjectStreamField(Field field2, boolean z, boolean z2) {
        this.offset = 0;
        this.field = field2;
        this.unshared = z;
        this.name = field2.getName();
        Class type2 = field2.getType();
        this.type = (z2 || type2.isPrimitive()) ? type2 : Object.class;
        this.signature = getClassSignature(type2).intern();
    }

    public String getName() {
        return this.name;
    }

    @CallerSensitive
    public Class<?> getType() {
        return this.type;
    }

    public char getTypeCode() {
        return this.signature.charAt(0);
    }

    public String getTypeString() {
        if (isPrimitive()) {
            return null;
        }
        return this.signature;
    }

    public int getOffset() {
        return this.offset;
    }

    /* access modifiers changed from: protected */
    public void setOffset(int i) {
        this.offset = i;
    }

    public boolean isPrimitive() {
        char charAt = this.signature.charAt(0);
        return (charAt == 'L' || charAt == '[') ? false : true;
    }

    public boolean isUnshared() {
        return this.unshared;
    }

    public int compareTo(Object obj) {
        ObjectStreamField objectStreamField = (ObjectStreamField) obj;
        boolean isPrimitive = isPrimitive();
        if (isPrimitive != objectStreamField.isPrimitive()) {
            return isPrimitive ? -1 : 1;
        }
        return this.name.compareTo(objectStreamField.name);
    }

    public String toString() {
        return this.signature + ' ' + this.name;
    }

    /* access modifiers changed from: package-private */
    public Field getField() {
        return this.field;
    }

    /* access modifiers changed from: package-private */
    public String getSignature() {
        return this.signature;
    }

    private static String getClassSignature(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        while (cls.isArray()) {
            sb.append('[');
            cls = cls.getComponentType();
        }
        if (!cls.isPrimitive()) {
            sb.append("L" + cls.getName().replace('.', '/') + ';');
        } else if (cls == Integer.TYPE) {
            sb.append('I');
        } else if (cls == Byte.TYPE) {
            sb.append('B');
        } else if (cls == Long.TYPE) {
            sb.append('J');
        } else if (cls == Float.TYPE) {
            sb.append('F');
        } else if (cls == Double.TYPE) {
            sb.append('D');
        } else if (cls == Short.TYPE) {
            sb.append('S');
        } else if (cls == Character.TYPE) {
            sb.append('C');
        } else if (cls == Boolean.TYPE) {
            sb.append('Z');
        } else if (cls == Void.TYPE) {
            sb.append('V');
        } else {
            throw new InternalError();
        }
        return sb.toString();
    }
}
