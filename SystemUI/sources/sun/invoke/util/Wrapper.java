package sun.invoke.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public enum Wrapper {
    BOOLEAN(Boolean.class, "Boolean", Boolean.TYPE, "boolean", 'Z', new boolean[0], Format.unsigned(1)),
    BYTE(Byte.class, "Byte", Byte.TYPE, "byte", 'B', new byte[0], Format.signed(8)),
    SHORT(Short.class, "Short", Short.TYPE, "short", 'S', new short[0], Format.signed(16)),
    CHAR(Character.class, "Character", Character.TYPE, "char", 'C', new char[0], Format.unsigned(16)),
    INT(Integer.class, "Integer", Integer.TYPE, "int", 'I', new int[0], Format.signed(32)),
    LONG(Long.class, "Long", Long.TYPE, "long", 'J', new long[0], Format.signed(64)),
    FLOAT(Float.class, "Float", Float.TYPE, "float", 'F', new float[0], Format.floating(32)),
    DOUBLE(Double.class, "Double", Double.TYPE, "double", 'D', new double[0], Format.floating(64)),
    OBJECT(Object.class, "Object", Object.class, "Object", 'L', new Object[0], Format.other(1)),
    VOID(Void.class, "Void", Void.TYPE, "void", 'V', (String) null, Format.other(0));
    
    public static final int COUNT = 10;
    private static final Object DOUBLE_ZERO = null;
    private static final Object FLOAT_ZERO = null;
    private static final Wrapper[] FROM_CHAR = null;
    private static final Wrapper[] FROM_PRIM = null;
    private static final Wrapper[] FROM_WRAP = null;
    private final char basicTypeChar;
    private final Object emptyArray;
    private final int format;
    private final String primitiveSimpleName;
    private final Class<?> primitiveType;
    private final String wrapperSimpleName;
    private final Class<?> wrapperType;

    private static boolean boolValue(byte b) {
        return ((byte) (b & 1)) != 0;
    }

    static <T> Class<T> forceType(Class<?> cls, Class<T> cls2) {
        return cls;
    }

    static {
        int i;
        DOUBLE_ZERO = Double.valueOf(0.0d);
        FLOAT_ZERO = Float.valueOf(0.0f);
        FROM_PRIM = new Wrapper[16];
        FROM_WRAP = new Wrapper[16];
        FROM_CHAR = new Wrapper[16];
        for (Wrapper wrapper : values()) {
            int hashPrim = hashPrim(wrapper.primitiveType);
            int hashWrap = hashWrap(wrapper.wrapperType);
            int hashChar = hashChar(wrapper.basicTypeChar);
            FROM_PRIM[hashPrim] = wrapper;
            FROM_WRAP[hashWrap] = wrapper;
            FROM_CHAR[hashChar] = wrapper;
        }
    }

    private Wrapper(Class<?> cls, String str, Class<?> cls2, String str2, char c, Object obj, int i) {
        this.wrapperType = cls;
        this.primitiveType = cls2;
        this.basicTypeChar = c;
        this.emptyArray = obj;
        this.format = i;
        this.wrapperSimpleName = str;
        this.primitiveSimpleName = str2;
    }

    public String detailString() {
        return this.wrapperSimpleName + Arrays.asList(this.wrapperType, this.primitiveType, Character.valueOf(this.basicTypeChar), zero(), "0x" + Integer.toHexString(this.format));
    }

    private static abstract class Format {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final int BOOLEAN = 5;
        static final int CHAR = 65;
        static final int FLOAT = 4225;
        static final int FLOATING = 4096;
        static final int INT = -3967;
        static final int KIND_SHIFT = 12;
        static final int NUM_MASK = -4;
        static final int SHORT = -4031;
        static final int SIGNED = -4096;
        static final int SIZE_MASK = 1023;
        static final int SIZE_SHIFT = 2;
        static final int SLOT_MASK = 3;
        static final int SLOT_SHIFT = 0;
        static final int UNSIGNED = 0;
        static final int VOID = 0;

        static int format(int i, int i2, int i3) {
            return i | (i2 << 2) | (i3 << 0);
        }

        static int other(int i) {
            return i << 0;
        }

        static {
            Class<Wrapper> cls = Wrapper.class;
        }

        private Format() {
        }

        static int signed(int i) {
            return format(SIGNED, i, i > 32 ? 2 : 1);
        }

        static int unsigned(int i) {
            return format(0, i, i > 32 ? 2 : 1);
        }

        static int floating(int i) {
            return format(4096, i, i > 32 ? 2 : 1);
        }
    }

    public int bitWidth() {
        return (this.format >> 2) & 1023;
    }

    public int stackSlots() {
        return (this.format >> 0) & 3;
    }

    public boolean isSingleWord() {
        return (this.format & 1) != 0;
    }

    public boolean isDoubleWord() {
        return (this.format & 2) != 0;
    }

    public boolean isNumeric() {
        return (this.format & -4) != 0;
    }

    public boolean isIntegral() {
        return isNumeric() && this.format < 4225;
    }

    public boolean isSubwordOrInt() {
        return isIntegral() && isSingleWord();
    }

    public boolean isSigned() {
        return this.format < 0;
    }

    public boolean isUnsigned() {
        int i = this.format;
        return i >= 5 && i < 4225;
    }

    public boolean isFloating() {
        return this.format >= 4225;
    }

    public boolean isOther() {
        return (this.format & -4) == 0;
    }

    public boolean isConvertibleFrom(Wrapper wrapper) {
        if (this == wrapper) {
            return true;
        }
        if (compareTo(wrapper) < 0) {
            return false;
        }
        return (((this.format & wrapper.format) & -4096) != 0) || isOther() || wrapper.format == 65;
    }

    private static boolean checkConvertibleFrom() {
        for (Wrapper wrapper : values()) {
            Wrapper wrapper2 = VOID;
            if (wrapper != CHAR) {
                boolean isConvertibleFrom = wrapper.isConvertibleFrom(INT);
            }
            if (!(wrapper == BOOLEAN || wrapper == wrapper2)) {
                Wrapper wrapper3 = OBJECT;
            }
            if (wrapper.isSigned()) {
                for (Wrapper wrapper4 : values()) {
                    if (wrapper != wrapper4 && !wrapper4.isFloating() && wrapper4.isSigned()) {
                        wrapper.compareTo(wrapper4);
                    }
                }
            }
            if (wrapper.isFloating()) {
                for (Wrapper wrapper5 : values()) {
                    if (wrapper != wrapper5 && !wrapper5.isSigned() && wrapper5.isFloating()) {
                        wrapper.compareTo(wrapper5);
                    }
                }
            }
        }
        return true;
    }

    /* renamed from: sun.invoke.util.Wrapper$1 */
    static /* synthetic */ class C47301 {
        static final /* synthetic */ int[] $SwitchMap$sun$invoke$util$Wrapper = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                sun.invoke.util.Wrapper[] r0 = sun.invoke.util.Wrapper.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$sun$invoke$util$Wrapper = r0
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.BOOLEAN     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x001d }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.INT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0028 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.BYTE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0033 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.CHAR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x003e }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.SHORT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0049 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.LONG     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0054 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.FLOAT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0060 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.DOUBLE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x006c }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.VOID     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$sun$invoke$util$Wrapper     // Catch:{ NoSuchFieldError -> 0x0078 }
                sun.invoke.util.Wrapper r1 = sun.invoke.util.Wrapper.OBJECT     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.invoke.util.Wrapper.C47301.<clinit>():void");
        }
    }

    public Object zero() {
        switch (C47301.$SwitchMap$sun$invoke$util$Wrapper[ordinal()]) {
            case 1:
                return Boolean.FALSE;
            case 2:
                return 0;
            case 3:
                return (byte) 0;
            case 4:
                return 0;
            case 5:
                return (short) 0;
            case 6:
                return 0L;
            case 7:
                return FLOAT_ZERO;
            case 8:
                return DOUBLE_ZERO;
            default:
                return null;
        }
    }

    public <T> T zero(Class<T> cls) {
        return convert(zero(), cls);
    }

    public static Wrapper forPrimitiveType(Class<?> cls) {
        Wrapper findPrimitiveType = findPrimitiveType(cls);
        if (findPrimitiveType != null) {
            return findPrimitiveType;
        }
        if (cls.isPrimitive()) {
            throw new InternalError();
        }
        throw newIllegalArgumentException("not primitive: " + cls);
    }

    public static Wrapper forPrimitiveType(char c) {
        if (c == 'F') {
            return FLOAT;
        }
        if (c == 'S') {
            return SHORT;
        }
        if (c == 'V') {
            return VOID;
        }
        if (c == 'Z') {
            return BOOLEAN;
        }
        if (c == 'I') {
            return INT;
        }
        if (c == 'J') {
            return LONG;
        }
        switch (c) {
            case 'B':
                return BYTE;
            case 'C':
                return CHAR;
            case 'D':
                return DOUBLE;
            default:
                throw newIllegalArgumentException("not primitive: " + c);
        }
    }

    static Wrapper findPrimitiveType(Class<?> cls) {
        Wrapper wrapper = FROM_PRIM[hashPrim(cls)];
        if (wrapper == null || wrapper.primitiveType != cls) {
            return null;
        }
        return wrapper;
    }

    public static Wrapper forWrapperType(Class<?> cls) {
        Wrapper findWrapperType = findWrapperType(cls);
        if (findWrapperType != null) {
            return findWrapperType;
        }
        Wrapper[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            if (values[i].wrapperType != cls) {
                i++;
            } else {
                throw new InternalError();
            }
        }
        throw newIllegalArgumentException("not wrapper: " + cls);
    }

    static Wrapper findWrapperType(Class<?> cls) {
        Wrapper wrapper = FROM_WRAP[hashWrap(cls)];
        if (wrapper == null || wrapper.wrapperType != cls) {
            return null;
        }
        return wrapper;
    }

    public static Wrapper forBasicType(char c) {
        Wrapper wrapper = FROM_CHAR[hashChar(c)];
        if (wrapper != null && wrapper.basicTypeChar == c) {
            return wrapper;
        }
        Wrapper[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            Wrapper wrapper2 = values[i];
            if (wrapper.basicTypeChar != c) {
                i++;
            } else {
                throw new InternalError();
            }
        }
        throw newIllegalArgumentException("not basic type char: " + c);
    }

    public static Wrapper forBasicType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return forPrimitiveType(cls);
        }
        return OBJECT;
    }

    private static int hashPrim(Class<?> cls) {
        String name = cls.getName();
        if (name.length() < 3) {
            return 0;
        }
        return (name.charAt(0) + name.charAt(2)) % 16;
    }

    private static int hashWrap(Class<?> cls) {
        String name = cls.getName();
        if (name.length() < 13) {
            return 0;
        }
        return ((name.charAt(11) * 3) + name.charAt(12)) % 16;
    }

    private static int hashChar(char c) {
        return (c + (c >> 1)) % 16;
    }

    public Class<?> primitiveType() {
        return this.primitiveType;
    }

    public Class<?> wrapperType() {
        return this.wrapperType;
    }

    public <T> Class<T> wrapperType(Class<T> cls) {
        Class<?> cls2 = this.wrapperType;
        if (cls == cls2) {
            return cls;
        }
        if (cls == this.primitiveType || cls2 == Object.class || cls.isInterface()) {
            return forceType(this.wrapperType, cls);
        }
        throw newClassCastException(cls, this.primitiveType);
    }

    private static ClassCastException newClassCastException(Class<?> cls, Class<?> cls2) {
        return new ClassCastException(cls + " is not compatible with " + cls2);
    }

    public static <T> Class<T> asWrapperType(Class<T> cls) {
        return cls.isPrimitive() ? forPrimitiveType((Class<?>) cls).wrapperType(cls) : cls;
    }

    public static <T> Class<T> asPrimitiveType(Class<T> cls) {
        Wrapper findWrapperType = findWrapperType(cls);
        return findWrapperType != null ? forceType(findWrapperType.primitiveType(), cls) : cls;
    }

    public static boolean isWrapperType(Class<?> cls) {
        return findWrapperType(cls) != null;
    }

    public static boolean isPrimitiveType(Class<?> cls) {
        return cls.isPrimitive();
    }

    public static char basicTypeChar(Class<?> cls) {
        if (!cls.isPrimitive()) {
            return 'L';
        }
        return forPrimitiveType(cls).basicTypeChar();
    }

    public char basicTypeChar() {
        return this.basicTypeChar;
    }

    public String wrapperSimpleName() {
        return this.wrapperSimpleName;
    }

    public String primitiveSimpleName() {
        return this.primitiveSimpleName;
    }

    public <T> T cast(Object obj, Class<T> cls) {
        return convert(obj, cls, true);
    }

    public <T> T convert(Object obj, Class<T> cls) {
        return convert(obj, cls, false);
    }

    private <T> T convert(Object obj, Class<T> cls, boolean z) {
        if (this == OBJECT) {
            if (!cls.isInterface()) {
                cls.cast(obj);
            }
            return obj;
        }
        Class<T> wrapperType2 = wrapperType(cls);
        if (wrapperType2.isInstance(obj)) {
            return wrapperType2.cast(obj);
        }
        if (!z) {
            Class<?> cls2 = obj.getClass();
            Wrapper findWrapperType = findWrapperType(cls2);
            if (findWrapperType == null || !isConvertibleFrom(findWrapperType)) {
                throw newClassCastException(wrapperType2, cls2);
            }
        } else if (obj == null) {
            return zero();
        }
        return wrap(obj);
    }

    public Object wrap(Object obj) {
        char c = this.basicTypeChar;
        if (c == 'L') {
            return obj;
        }
        if (c == 'V') {
            return null;
        }
        Number numberValue = numberValue(obj);
        char c2 = this.basicTypeChar;
        if (c2 == 'F') {
            return Float.valueOf(numberValue.floatValue());
        }
        if (c2 == 'S') {
            return Short.valueOf((short) numberValue.intValue());
        }
        if (c2 == 'Z') {
            return Boolean.valueOf(boolValue(numberValue.byteValue()));
        }
        if (c2 == 'I') {
            return Integer.valueOf(numberValue.intValue());
        }
        if (c2 == 'J') {
            return Long.valueOf(numberValue.longValue());
        }
        switch (c2) {
            case 'B':
                return Byte.valueOf((byte) numberValue.intValue());
            case 'C':
                return Character.valueOf((char) numberValue.intValue());
            case 'D':
                return Double.valueOf(numberValue.doubleValue());
            default:
                throw new InternalError("bad wrapper");
        }
    }

    public Object wrap(int i) {
        char c = this.basicTypeChar;
        if (c == 'L') {
            return Integer.valueOf(i);
        }
        if (c == 'F') {
            return Float.valueOf((float) i);
        }
        if (c == 'L') {
            throw newIllegalArgumentException("cannot wrap to object type");
        } else if (c == 'S') {
            return Short.valueOf((short) i);
        } else {
            if (c == 'V') {
                return null;
            }
            if (c == 'Z') {
                return Boolean.valueOf(boolValue((byte) i));
            }
            if (c == 'I') {
                return Integer.valueOf(i);
            }
            if (c == 'J') {
                return Long.valueOf((long) i);
            }
            switch (c) {
                case 'B':
                    return Byte.valueOf((byte) i);
                case 'C':
                    return Character.valueOf((char) i);
                case 'D':
                    return Double.valueOf((double) i);
                default:
                    throw new InternalError("bad wrapper");
            }
        }
    }

    private static Number numberValue(Object obj) {
        if (obj instanceof Number) {
            return (Number) obj;
        }
        if (obj instanceof Character) {
            return Integer.valueOf((int) ((Character) obj).charValue());
        }
        if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        return (Number) obj;
    }

    private static RuntimeException newIllegalArgumentException(String str, Object obj) {
        return newIllegalArgumentException(str + obj);
    }

    private static RuntimeException newIllegalArgumentException(String str) {
        return new IllegalArgumentException(str);
    }

    public Object makeArray(int i) {
        return Array.newInstance(this.primitiveType, i);
    }

    public Class<?> arrayType() {
        return this.emptyArray.getClass();
    }

    public void copyArrayUnboxing(Object[] objArr, int i, Object obj, int i2, int i3) {
        if (obj.getClass() != arrayType()) {
            arrayType().cast(obj);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            Array.set(obj, i4 + i2, convert(objArr[i4 + i], this.primitiveType));
        }
    }

    public void copyArrayBoxing(Object obj, int i, Object[] objArr, int i2, int i3) {
        if (obj.getClass() != arrayType()) {
            arrayType().cast(obj);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            objArr[i4 + i2] = Array.get(obj, i4 + i);
        }
    }
}
