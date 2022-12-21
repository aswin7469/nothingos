package jdk.internal.misc;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import kotlin.UShort;
import sun.reflect.Reflection;

public final class Unsafe {
    public static final int ADDRESS_SIZE;
    public static final int ARRAY_BOOLEAN_BASE_OFFSET;
    public static final int ARRAY_BOOLEAN_INDEX_SCALE;
    public static final int ARRAY_BYTE_BASE_OFFSET;
    public static final int ARRAY_BYTE_INDEX_SCALE;
    public static final int ARRAY_CHAR_BASE_OFFSET;
    public static final int ARRAY_CHAR_INDEX_SCALE;
    public static final int ARRAY_DOUBLE_BASE_OFFSET;
    public static final int ARRAY_DOUBLE_INDEX_SCALE;
    public static final int ARRAY_FLOAT_BASE_OFFSET;
    public static final int ARRAY_FLOAT_INDEX_SCALE;
    public static final int ARRAY_INT_BASE_OFFSET;
    public static final int ARRAY_INT_INDEX_SCALE;
    public static final int ARRAY_LONG_BASE_OFFSET;
    public static final int ARRAY_LONG_INDEX_SCALE;
    public static final int ARRAY_OBJECT_BASE_OFFSET;
    public static final int ARRAY_OBJECT_INDEX_SCALE;
    public static final int ARRAY_SHORT_BASE_OFFSET;
    public static final int ARRAY_SHORT_INDEX_SCALE;
    public static final int INVALID_FIELD_OFFSET = -1;
    private static final Unsafe THE_ONE;
    private static final Unsafe theUnsafe;

    private native void copyMemory0(Object obj, long j, Object obj2, long j2, long j3);

    private static native int getArrayBaseOffsetForComponentType(Class cls);

    private static native int getArrayIndexScaleForComponentType(Class cls);

    private boolean is32BitClean(long j) {
        return (j >>> 32) == 0;
    }

    private static int toUnsignedInt(byte b) {
        return b & 255;
    }

    private static int toUnsignedInt(short s) {
        return s & UShort.MAX_VALUE;
    }

    private static long toUnsignedLong(byte b) {
        return ((long) b) & 255;
    }

    private static long toUnsignedLong(int i) {
        return ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }

    private static long toUnsignedLong(short s) {
        return ((long) s) & 65535;
    }

    public native int addressSize();

    public native Object allocateInstance(Class<?> cls);

    public native long allocateMemory(long j);

    public final native boolean compareAndSetInt(Object obj, long j, int i, int i2);

    public final native boolean compareAndSetLong(Object obj, long j, long j2, long j3);

    public final native boolean compareAndSetObject(Object obj, long j, Object obj2, Object obj3);

    public native boolean compareAndSwapInt(Object obj, long j, int i, int i2);

    public native boolean compareAndSwapLong(Object obj, long j, long j2, long j3);

    public native boolean compareAndSwapObject(Object obj, long j, Object obj2, Object obj3);

    public native void freeMemory(long j);

    public native void fullFence();

    public native boolean getBoolean(Object obj, long j);

    public native byte getByte(long j);

    public native byte getByte(Object obj, long j);

    public native char getChar(long j);

    public native char getChar(Object obj, long j);

    public native double getDouble(long j);

    public native double getDouble(Object obj, long j);

    public native float getFloat(long j);

    public native float getFloat(Object obj, long j);

    public native int getInt(long j);

    public native int getInt(Object obj, long j);

    public native int getIntVolatile(Object obj, long j);

    public native long getLong(long j);

    public native long getLong(Object obj, long j);

    public native long getLongVolatile(Object obj, long j);

    public native Object getObject(Object obj, long j);

    public native Object getObjectVolatile(Object obj, long j);

    public native short getShort(long j);

    public native short getShort(Object obj, long j);

    public native void loadFence();

    public native int pageSize();

    public native void park(boolean z, long j);

    public native void putBoolean(Object obj, long j, boolean z);

    public native void putByte(long j, byte b);

    public native void putByte(Object obj, long j, byte b);

    public native void putChar(long j, char c);

    public native void putChar(Object obj, long j, char c);

    public native void putDouble(long j, double d);

    public native void putDouble(Object obj, long j, double d);

    public native void putFloat(long j, float f);

    public native void putFloat(Object obj, long j, float f);

    public native void putInt(long j, int i);

    public native void putInt(Object obj, long j, int i);

    public native void putIntVolatile(Object obj, long j, int i);

    public native void putLong(long j, long j2);

    public native void putLong(Object obj, long j, long j2);

    public native void putLongVolatile(Object obj, long j, long j2);

    public native void putObject(Object obj, long j, Object obj2);

    public native void putObjectVolatile(Object obj, long j, Object obj2);

    public native void putOrderedInt(Object obj, long j, int i);

    public native void putOrderedLong(Object obj, long j, long j2);

    public native void putOrderedObject(Object obj, long j, Object obj2);

    public native void putShort(long j, short s);

    public native void putShort(Object obj, long j, short s);

    public native void setMemory(long j, long j2, byte b);

    public native void storeFence();

    public native void unpark(Object obj);

    static {
        Unsafe unsafe = new Unsafe();
        THE_ONE = unsafe;
        theUnsafe = unsafe;
        Class<boolean[]> cls = boolean[].class;
        ARRAY_BOOLEAN_BASE_OFFSET = unsafe.arrayBaseOffset(cls);
        Class<byte[]> cls2 = byte[].class;
        ARRAY_BYTE_BASE_OFFSET = unsafe.arrayBaseOffset(cls2);
        Class<short[]> cls3 = short[].class;
        ARRAY_SHORT_BASE_OFFSET = unsafe.arrayBaseOffset(cls3);
        Class<char[]> cls4 = char[].class;
        ARRAY_CHAR_BASE_OFFSET = unsafe.arrayBaseOffset(cls4);
        Class<int[]> cls5 = int[].class;
        ARRAY_INT_BASE_OFFSET = unsafe.arrayBaseOffset(cls5);
        Class<long[]> cls6 = long[].class;
        ARRAY_LONG_BASE_OFFSET = unsafe.arrayBaseOffset(cls6);
        Class<float[]> cls7 = float[].class;
        ARRAY_FLOAT_BASE_OFFSET = unsafe.arrayBaseOffset(cls7);
        Class<double[]> cls8 = double[].class;
        ARRAY_DOUBLE_BASE_OFFSET = unsafe.arrayBaseOffset(cls8);
        ARRAY_OBJECT_BASE_OFFSET = unsafe.arrayBaseOffset(Object[].class);
        ARRAY_BOOLEAN_INDEX_SCALE = unsafe.arrayIndexScale(cls);
        ARRAY_BYTE_INDEX_SCALE = unsafe.arrayIndexScale(cls2);
        ARRAY_SHORT_INDEX_SCALE = unsafe.arrayIndexScale(cls3);
        ARRAY_CHAR_INDEX_SCALE = unsafe.arrayIndexScale(cls4);
        ARRAY_INT_INDEX_SCALE = unsafe.arrayIndexScale(cls5);
        ARRAY_LONG_INDEX_SCALE = unsafe.arrayIndexScale(cls6);
        ARRAY_FLOAT_INDEX_SCALE = unsafe.arrayIndexScale(cls7);
        ARRAY_DOUBLE_INDEX_SCALE = unsafe.arrayIndexScale(cls8);
        ARRAY_OBJECT_INDEX_SCALE = unsafe.arrayIndexScale(Object[].class);
        ADDRESS_SIZE = unsafe.addressSize();
    }

    private Unsafe() {
    }

    public static Unsafe getUnsafe() {
        ClassLoader classLoader;
        Class<?> callerClass = Reflection.getCallerClass();
        if (callerClass == null) {
            classLoader = null;
        } else {
            classLoader = callerClass.getClassLoader();
        }
        if (classLoader == null || classLoader == Unsafe.class.getClassLoader()) {
            return THE_ONE;
        }
        throw new SecurityException("Unsafe access denied");
    }

    public long objectFieldOffset(Field field) {
        if (!Modifier.isStatic(field.getModifiers())) {
            return (long) field.getOffset();
        }
        throw new IllegalArgumentException("valid for instance fields only");
    }

    public long objectFieldOffset(Class<?> cls, String str) {
        Field field = null;
        if (cls == null || str == null) {
            throw null;
        }
        Field[] declaredFields = cls.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Field field2 = declaredFields[i];
            if (field2.getName().equals(str)) {
                field = field2;
                break;
            }
            i++;
        }
        if (field != null) {
            return objectFieldOffset(field);
        }
        throw new InternalError();
    }

    public int arrayBaseOffset(Class cls) {
        Class<?> componentType = cls.getComponentType();
        if (componentType != null) {
            return getArrayBaseOffsetForComponentType(componentType);
        }
        throw new IllegalArgumentException("Valid for array classes only: " + cls);
    }

    public int arrayIndexScale(Class cls) {
        Class<?> componentType = cls.getComponentType();
        if (componentType != null) {
            return getArrayIndexScaleForComponentType(componentType);
        }
        throw new IllegalArgumentException("Valid for array classes only: " + cls);
    }

    public final long getLongUnaligned(Object obj, long j) {
        Object obj2 = obj;
        if ((j & 7) == 0) {
            return getLong(obj, j);
        }
        if ((j & 3) == 0) {
            return makeLong(getInt(obj, j), getInt(obj2, j + 4));
        }
        if ((j & 1) == 0) {
            return makeLong(getShort(obj, j), getShort(obj2, j + 2), getShort(obj2, j + 4), getShort(obj2, j + 6));
        }
        return makeLong(getByte(obj, j), getByte(obj2, j + 1), getByte(obj2, j + 2), getByte(obj2, j + 3), getByte(obj2, j + 4), getByte(obj2, j + 5), getByte(obj2, j + 6), getByte(obj2, j + 7));
    }

    public final int getIntUnaligned(Object obj, long j) {
        if ((j & 3) == 0) {
            return getInt(obj, j);
        }
        if ((j & 1) == 0) {
            return makeInt(getShort(obj, j), getShort(obj, j + 2));
        }
        return makeInt(getByte(obj, j), getByte(obj, 1 + j), getByte(obj, 2 + j), getByte(obj, j + 3));
    }

    private static long makeLong(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return (toUnsignedLong(b2) << 8) | toUnsignedLong(b) | (toUnsignedLong(b3) << 16) | (toUnsignedLong(b4) << 24) | (toUnsignedLong(b5) << 32) | (toUnsignedLong(b6) << 40) | (toUnsignedLong(b7) << 48) | (toUnsignedLong(b8) << 56);
    }

    private static long makeLong(short s, short s2, short s3, short s4) {
        return (toUnsignedLong(s2) << 16) | toUnsignedLong(s) | (toUnsignedLong(s3) << 32) | (toUnsignedLong(s4) << 48);
    }

    private static long makeLong(int i, int i2) {
        return (toUnsignedLong(i2) << 32) | toUnsignedLong(i);
    }

    private static int makeInt(short s, short s2) {
        return toUnsignedInt(s) | (toUnsignedInt(s2) << 16);
    }

    private static int makeInt(byte b, byte b2, byte b3, byte b4) {
        return toUnsignedInt(b) | (toUnsignedInt(b2) << 8) | (toUnsignedInt(b3) << 16) | (toUnsignedInt(b4) << 24);
    }

    private static short makeShort(byte b, byte b2) {
        return (short) (toUnsignedInt(b) | (toUnsignedInt(b2) << 8));
    }

    public void copyMemory(Object obj, long j, Object obj2, long j2, long j3) {
        copyMemoryChecks(obj, j, obj2, j2, j3);
        if (j3 != 0) {
            copyMemory0(obj, j, obj2, j2, j3);
        }
    }

    public void copyMemory(long j, long j2, long j3) {
        copyMemory((Object) null, j, (Object) null, j2, j3);
    }

    private void copyMemoryChecks(Object obj, long j, Object obj2, long j2, long j3) {
        checkSize(j3);
        checkPrimitivePointer(obj, j);
        checkPrimitivePointer(obj2, j2);
    }

    public final int getAndAddInt(Object obj, long j, int i) {
        int intVolatile;
        do {
            intVolatile = getIntVolatile(obj, j);
        } while (!compareAndSwapInt(obj, j, intVolatile, intVolatile + i));
        return intVolatile;
    }

    public final long getAndAddLong(Object obj, long j, long j2) {
        long longVolatile;
        do {
            longVolatile = getLongVolatile(obj, j);
        } while (!compareAndSwapLong(obj, j, longVolatile, longVolatile + j2));
        return longVolatile;
    }

    public final int getAndSetInt(Object obj, long j, int i) {
        int intVolatile;
        do {
            intVolatile = getIntVolatile(obj, j);
        } while (!compareAndSwapInt(obj, j, intVolatile, i));
        return intVolatile;
    }

    public final long getAndSetLong(Object obj, long j, long j2) {
        long longVolatile;
        do {
            longVolatile = getLongVolatile(obj, j);
        } while (!compareAndSwapLong(obj, j, longVolatile, j2));
        return longVolatile;
    }

    public final Object getAndSetObject(Object obj, long j, Object obj2) {
        Object objectVolatile;
        do {
            objectVolatile = getObjectVolatile(obj, j);
        } while (!compareAndSwapObject(obj, j, objectVolatile, obj2));
        return objectVolatile;
    }

    public final void putIntRelease(Object obj, long j, int i) {
        putIntVolatile(obj, j, i);
    }

    public final int getIntAcquire(Object obj, long j) {
        return getIntVolatile(obj, j);
    }

    public final void putLongRelease(Object obj, long j, long j2) {
        putLongVolatile(obj, j, j2);
    }

    public final long getLongAcquire(Object obj, long j) {
        return getLongVolatile(obj, j);
    }

    public final void putObjectRelease(Object obj, long j, Object obj2) {
        putObjectVolatile(obj, j, obj2);
    }

    public final Object getObjectAcquire(Object obj, long j) {
        return getObjectVolatile(obj, j);
    }

    public void ensureClassInitialized(Class<?> cls) {
        cls.getClass();
        try {
            Class.forName(cls.getName(), true, cls.getClassLoader());
        } catch (ClassNotFoundException unused) {
        }
    }

    private RuntimeException invalidInput() {
        return new IllegalArgumentException();
    }

    private void checkSize(long j) {
        if (ADDRESS_SIZE == 4) {
            if (!is32BitClean(j)) {
                throw invalidInput();
            }
        } else if (j < 0) {
            throw invalidInput();
        }
    }

    private void checkNativeAddress(long j) {
        if (ADDRESS_SIZE == 4 && (((j >> 32) + 1) & -2) != 0) {
            throw invalidInput();
        }
    }

    private void checkOffset(Object obj, long j) {
        if (ADDRESS_SIZE == 4) {
            if (!is32BitClean(j)) {
                throw invalidInput();
            }
        } else if (j < 0) {
            throw invalidInput();
        }
    }

    private void checkPointer(Object obj, long j) {
        if (obj == null) {
            checkNativeAddress(j);
        } else {
            checkOffset(obj, j);
        }
    }

    private void checkPrimitiveArray(Class<?> cls) {
        Class<?> componentType = cls.getComponentType();
        if (componentType == null || !componentType.isPrimitive()) {
            throw invalidInput();
        }
    }

    private void checkPrimitivePointer(Object obj, long j) {
        checkPointer(obj, j);
        if (obj != null) {
            checkPrimitiveArray(obj.getClass());
        }
    }
}
