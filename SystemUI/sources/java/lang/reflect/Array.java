package java.lang.reflect;

public final class Array {
    private static native Object createMultiArray(Class<?> cls, int[] iArr) throws NegativeArraySizeException;

    private static native Object createObjectArray(Class<?> cls, int i) throws NegativeArraySizeException;

    private Array() {
    }

    public static Object newInstance(Class<?> cls, int i) throws NegativeArraySizeException {
        return newArray(cls, i);
    }

    public static Object newInstance(Class<?> cls, int... iArr) throws IllegalArgumentException, NegativeArraySizeException {
        if (iArr.length <= 0 || iArr.length > 255) {
            throw new IllegalArgumentException("Bad number of dimensions: " + iArr.length);
        } else if (cls == Void.TYPE) {
            throw new IllegalArgumentException("Can't allocate an array of void");
        } else if (cls != null) {
            return createMultiArray(cls, iArr);
        } else {
            throw new NullPointerException("componentType == null");
        }
    }

    public static int getLength(Object obj) {
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj).length;
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj).length;
        }
        if (obj instanceof char[]) {
            return ((char[]) obj).length;
        }
        if (obj instanceof double[]) {
            return ((double[]) obj).length;
        }
        if (obj instanceof float[]) {
            return ((float[]) obj).length;
        }
        if (obj instanceof int[]) {
            return ((int[]) obj).length;
        }
        if (obj instanceof long[]) {
            return ((long[]) obj).length;
        }
        if (obj instanceof short[]) {
            return ((short[]) obj).length;
        }
        throw badArray(obj);
    }

    public static Object get(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof Object[]) {
            return ((Object[]) obj)[i];
        }
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj)[i] ? Boolean.TRUE : Boolean.FALSE;
        }
        if (obj instanceof byte[]) {
            return Byte.valueOf(((byte[]) obj)[i]);
        }
        if (obj instanceof char[]) {
            return Character.valueOf(((char[]) obj)[i]);
        }
        if (obj instanceof short[]) {
            return Short.valueOf(((short[]) obj)[i]);
        }
        if (obj instanceof int[]) {
            return Integer.valueOf(((int[]) obj)[i]);
        }
        if (obj instanceof long[]) {
            return Long.valueOf(((long[]) obj)[i]);
        }
        if (obj instanceof float[]) {
            return new Float(((float[]) obj)[i]);
        }
        if (obj instanceof double[]) {
            return new Double(((double[]) obj)[i]);
        }
        if (obj == null) {
            throw new NullPointerException("array == null");
        }
        throw notAnArray(obj);
    }

    public static boolean getBoolean(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static byte getByte(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof byte[]) {
            return ((byte[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static char getChar(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof char[]) {
            return ((char[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static short getShort(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof short[]) {
            return ((short[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return (short) ((byte[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static int getInt(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof int[]) {
            return ((int[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj)[i];
        }
        if (obj instanceof char[]) {
            return ((char[]) obj)[i];
        }
        if (obj instanceof short[]) {
            return ((short[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static long getLong(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof long[]) {
            return ((long[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return (long) ((byte[]) obj)[i];
        }
        if (obj instanceof char[]) {
            return (long) ((char[]) obj)[i];
        }
        if (obj instanceof int[]) {
            return (long) ((int[]) obj)[i];
        }
        if (obj instanceof short[]) {
            return (long) ((short[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static float getFloat(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof float[]) {
            return ((float[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return (float) ((byte[]) obj)[i];
        }
        if (obj instanceof char[]) {
            return (float) ((char[]) obj)[i];
        }
        if (obj instanceof int[]) {
            return (float) ((int[]) obj)[i];
        }
        if (obj instanceof long[]) {
            return (float) ((long[]) obj)[i];
        }
        if (obj instanceof short[]) {
            return (float) ((short[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static double getDouble(Object obj, int i) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof double[]) {
            return ((double[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return (double) ((byte[]) obj)[i];
        }
        if (obj instanceof char[]) {
            return (double) ((char[]) obj)[i];
        }
        if (obj instanceof float[]) {
            return (double) ((float[]) obj)[i];
        }
        if (obj instanceof int[]) {
            return (double) ((int[]) obj)[i];
        }
        if (obj instanceof long[]) {
            return (double) ((long[]) obj)[i];
        }
        if (obj instanceof short[]) {
            return (double) ((short[]) obj)[i];
        }
        throw badArray(obj);
    }

    public static void set(Object obj, int i, Object obj2) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (!obj.getClass().isArray()) {
            throw notAnArray(obj);
        } else if (obj instanceof Object[]) {
            if (obj2 == null || obj.getClass().getComponentType().isInstance(obj2)) {
                ((Object[]) obj)[i] = obj2;
                return;
            }
            throw incompatibleType(obj);
        } else if (obj2 == null) {
            throw new IllegalArgumentException("Primitive array can't take null values.");
        } else if (obj2 instanceof Boolean) {
            setBoolean(obj, i, ((Boolean) obj2).booleanValue());
        } else if (obj2 instanceof Byte) {
            setByte(obj, i, ((Byte) obj2).byteValue());
        } else if (obj2 instanceof Character) {
            setChar(obj, i, ((Character) obj2).charValue());
        } else if (obj2 instanceof Short) {
            setShort(obj, i, ((Short) obj2).shortValue());
        } else if (obj2 instanceof Integer) {
            setInt(obj, i, ((Integer) obj2).intValue());
        } else if (obj2 instanceof Long) {
            setLong(obj, i, ((Long) obj2).longValue());
        } else if (obj2 instanceof Float) {
            setFloat(obj, i, ((Float) obj2).floatValue());
        } else if (obj2 instanceof Double) {
            setDouble(obj, i, ((Double) obj2).doubleValue());
        }
    }

    public static void setBoolean(Object obj, int i, boolean z) {
        if (obj instanceof boolean[]) {
            ((boolean[]) obj)[i] = z;
            return;
        }
        throw badArray(obj);
    }

    public static void setByte(Object obj, int i, byte b) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof byte[]) {
            ((byte[]) obj)[i] = b;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) b;
        } else if (obj instanceof float[]) {
            ((float[]) obj)[i] = (float) b;
        } else if (obj instanceof int[]) {
            ((int[]) obj)[i] = b;
        } else if (obj instanceof long[]) {
            ((long[]) obj)[i] = (long) b;
        } else if (obj instanceof short[]) {
            ((short[]) obj)[i] = (short) b;
        } else {
            throw badArray(obj);
        }
    }

    public static void setChar(Object obj, int i, char c) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof char[]) {
            ((char[]) obj)[i] = c;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) c;
        } else if (obj instanceof float[]) {
            ((float[]) obj)[i] = (float) c;
        } else if (obj instanceof int[]) {
            ((int[]) obj)[i] = c;
        } else if (obj instanceof long[]) {
            ((long[]) obj)[i] = (long) c;
        } else {
            throw badArray(obj);
        }
    }

    public static void setShort(Object obj, int i, short s) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof short[]) {
            ((short[]) obj)[i] = s;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) s;
        } else if (obj instanceof float[]) {
            ((float[]) obj)[i] = (float) s;
        } else if (obj instanceof int[]) {
            ((int[]) obj)[i] = s;
        } else if (obj instanceof long[]) {
            ((long[]) obj)[i] = (long) s;
        } else {
            throw badArray(obj);
        }
    }

    public static void setInt(Object obj, int i, int i2) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof int[]) {
            ((int[]) obj)[i] = i2;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) i2;
        } else if (obj instanceof float[]) {
            ((float[]) obj)[i] = (float) i2;
        } else if (obj instanceof long[]) {
            ((long[]) obj)[i] = (long) i2;
        } else {
            throw badArray(obj);
        }
    }

    public static void setLong(Object obj, int i, long j) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof long[]) {
            ((long[]) obj)[i] = j;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) j;
        } else if (obj instanceof float[]) {
            ((float[]) obj)[i] = (float) j;
        } else {
            throw badArray(obj);
        }
    }

    public static void setFloat(Object obj, int i, float f) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof float[]) {
            ((float[]) obj)[i] = f;
        } else if (obj instanceof double[]) {
            ((double[]) obj)[i] = (double) f;
        } else {
            throw badArray(obj);
        }
    }

    public static void setDouble(Object obj, int i, double d) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (obj instanceof double[]) {
            ((double[]) obj)[i] = d;
            return;
        }
        throw badArray(obj);
    }

    private static Object newArray(Class<?> cls, int i) throws NegativeArraySizeException {
        if (!cls.isPrimitive()) {
            return createObjectArray(cls, i);
        }
        if (cls == Character.TYPE) {
            return new char[i];
        }
        if (cls == Integer.TYPE) {
            return new int[i];
        }
        if (cls == Byte.TYPE) {
            return new byte[i];
        }
        if (cls == Boolean.TYPE) {
            return new boolean[i];
        }
        if (cls == Short.TYPE) {
            return new short[i];
        }
        if (cls == Long.TYPE) {
            return new long[i];
        }
        if (cls == Float.TYPE) {
            return new float[i];
        }
        if (cls == Double.TYPE) {
            return new double[i];
        }
        if (cls == Void.TYPE) {
            throw new IllegalArgumentException("Can't allocate an array of void");
        }
        throw new AssertionError();
    }

    private static IllegalArgumentException notAnArray(Object obj) {
        throw new IllegalArgumentException("Not an array: " + obj.getClass());
    }

    private static IllegalArgumentException incompatibleType(Object obj) {
        throw new IllegalArgumentException("Array has incompatible type: " + obj.getClass());
    }

    private static RuntimeException badArray(Object obj) {
        if (obj == null) {
            throw new NullPointerException("array == null");
        } else if (!obj.getClass().isArray()) {
            throw notAnArray(obj);
        } else {
            throw incompatibleType(obj);
        }
    }
}
