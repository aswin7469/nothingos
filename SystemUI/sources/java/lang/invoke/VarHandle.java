package java.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sun.misc.Unsafe;

public abstract class VarHandle {
    private static final int ALL_MODES_BIT_MASK;
    private static final int ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
    private static final int BITWISE_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
    private static final int NUMERIC_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
    private static final int READ_ACCESS_MODES_BIT_MASK;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final int WRITE_ACCESS_MODES_BIT_MASK;
    private final int accessModesBitMask;
    private final Class<?> coordinateType0;
    private final Class<?> coordinateType1;
    private final Class<?> varType;

    @MethodHandle.PolymorphicSignature
    public final native Object compareAndExchange(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object compareAndExchangeAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object compareAndExchangeRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native boolean compareAndSet(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object get(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndAdd(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndAddAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndAddRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseAnd(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseAndAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseAndRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseOr(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseOrAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseOrRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseXor(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseXorAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndBitwiseXorRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndSet(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndSetAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getAndSetRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getOpaque(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native Object getVolatile(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native void set(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native void setOpaque(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native void setRelease(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native void setVolatile(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native boolean weakCompareAndSet(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native boolean weakCompareAndSetAcquire(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native boolean weakCompareAndSetPlain(Object... objArr);

    @MethodHandle.PolymorphicSignature
    public final native boolean weakCompareAndSetRelease(Object... objArr);

    static {
        if (AccessMode.values().length <= 32) {
            int accessTypesToBitMask = accessTypesToBitMask(EnumSet.m1722of(AccessType.GET));
            READ_ACCESS_MODES_BIT_MASK = accessTypesToBitMask;
            int accessTypesToBitMask2 = accessTypesToBitMask(EnumSet.m1722of(AccessType.SET));
            WRITE_ACCESS_MODES_BIT_MASK = accessTypesToBitMask2;
            int accessTypesToBitMask3 = accessTypesToBitMask(EnumSet.m1724of(AccessType.COMPARE_AND_EXCHANGE, AccessType.COMPARE_AND_SET, AccessType.GET_AND_UPDATE));
            ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK = accessTypesToBitMask3;
            int accessTypesToBitMask4 = accessTypesToBitMask(EnumSet.m1722of(AccessType.GET_AND_UPDATE_NUMERIC));
            NUMERIC_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK = accessTypesToBitMask4;
            int accessTypesToBitMask5 = accessTypesToBitMask(EnumSet.m1722of(AccessType.GET_AND_UPDATE_BITWISE));
            BITWISE_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK = accessTypesToBitMask5;
            ALL_MODES_BIT_MASK = accessTypesToBitMask | accessTypesToBitMask2 | accessTypesToBitMask3 | accessTypesToBitMask4 | accessTypesToBitMask5;
            return;
        }
        throw new InternalError("accessModes overflow");
    }

    /* renamed from: java.lang.invoke.VarHandle$1 */
    static /* synthetic */ class C43221 {
        static final /* synthetic */ int[] $SwitchMap$java$lang$invoke$VarHandle$AccessType;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.lang.invoke.VarHandle$AccessType[] r0 = java.lang.invoke.VarHandle.AccessType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$lang$invoke$VarHandle$AccessType = r0
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.GET     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x001d }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.SET     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.COMPARE_AND_SET     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.COMPARE_AND_EXCHANGE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x003e }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.GET_AND_UPDATE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.GET_AND_UPDATE_BITWISE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$lang$invoke$VarHandle$AccessType     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.lang.invoke.VarHandle$AccessType r1 = java.lang.invoke.VarHandle.AccessType.GET_AND_UPDATE_NUMERIC     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.invoke.VarHandle.C43221.<clinit>():void");
        }
    }

    enum AccessType {
        GET,
        SET,
        COMPARE_AND_SET,
        COMPARE_AND_EXCHANGE,
        GET_AND_UPDATE,
        GET_AND_UPDATE_BITWISE,
        GET_AND_UPDATE_NUMERIC;

        /* access modifiers changed from: package-private */
        public MethodType accessModeType(Class<?> cls, Class<?> cls2, Class<?>... clsArr) {
            switch (C43221.$SwitchMap$java$lang$invoke$VarHandle$AccessType[ordinal()]) {
                case 1:
                    Class[] allocateParameters = allocateParameters(0, cls, clsArr);
                    fillParameters(allocateParameters, cls, clsArr);
                    return MethodType.methodType(cls2, (Class<?>[]) allocateParameters);
                case 2:
                    Class[] allocateParameters2 = allocateParameters(1, cls, clsArr);
                    allocateParameters2[fillParameters(allocateParameters2, cls, clsArr)] = cls2;
                    return MethodType.methodType((Class<?>) Void.TYPE, (Class<?>[]) allocateParameters2);
                case 3:
                    Class[] allocateParameters3 = allocateParameters(2, cls, clsArr);
                    int fillParameters = fillParameters(allocateParameters3, cls, clsArr);
                    allocateParameters3[fillParameters] = cls2;
                    allocateParameters3[fillParameters + 1] = cls2;
                    return MethodType.methodType((Class<?>) Boolean.TYPE, (Class<?>[]) allocateParameters3);
                case 4:
                    Class[] allocateParameters4 = allocateParameters(2, cls, clsArr);
                    int fillParameters2 = fillParameters(allocateParameters4, cls, clsArr);
                    allocateParameters4[fillParameters2] = cls2;
                    allocateParameters4[fillParameters2 + 1] = cls2;
                    return MethodType.methodType(cls2, (Class<?>[]) allocateParameters4);
                case 5:
                case 6:
                case 7:
                    Class[] allocateParameters5 = allocateParameters(1, cls, clsArr);
                    allocateParameters5[fillParameters(allocateParameters5, cls, clsArr)] = cls2;
                    return MethodType.methodType(cls2, (Class<?>[]) allocateParameters5);
                default:
                    throw new InternalError("Unknown AccessType");
            }
        }

        private static Class<?>[] allocateParameters(int i, Class<?> cls, Class<?>... clsArr) {
            return new Class[((cls != null ? 1 : 0) + clsArr.length + i)];
        }

        private static int fillParameters(Class<?>[] clsArr, Class<?> cls, Class<?>... clsArr2) {
            int i;
            int i2 = 0;
            if (cls != null) {
                clsArr[0] = cls;
                i = 1;
            } else {
                i = 0;
            }
            while (i2 < clsArr2.length) {
                clsArr[i] = clsArr2[i2];
                i2++;
                i++;
            }
            return i;
        }
    }

    public enum AccessMode {
        GET("get", AccessType.GET),
        SET("set", AccessType.SET),
        GET_VOLATILE("getVolatile", AccessType.GET),
        SET_VOLATILE("setVolatile", AccessType.SET),
        GET_ACQUIRE("getAcquire", AccessType.GET),
        SET_RELEASE("setRelease", AccessType.SET),
        GET_OPAQUE("getOpaque", AccessType.GET),
        SET_OPAQUE("setOpaque", AccessType.SET),
        COMPARE_AND_SET("compareAndSet", AccessType.COMPARE_AND_SET),
        COMPARE_AND_EXCHANGE("compareAndExchange", AccessType.COMPARE_AND_EXCHANGE),
        COMPARE_AND_EXCHANGE_ACQUIRE("compareAndExchangeAcquire", AccessType.COMPARE_AND_EXCHANGE),
        COMPARE_AND_EXCHANGE_RELEASE("compareAndExchangeRelease", AccessType.COMPARE_AND_EXCHANGE),
        WEAK_COMPARE_AND_SET_PLAIN("weakCompareAndSetPlain", AccessType.COMPARE_AND_SET),
        WEAK_COMPARE_AND_SET("weakCompareAndSet", AccessType.COMPARE_AND_SET),
        WEAK_COMPARE_AND_SET_ACQUIRE("weakCompareAndSetAcquire", AccessType.COMPARE_AND_SET),
        WEAK_COMPARE_AND_SET_RELEASE("weakCompareAndSetRelease", AccessType.COMPARE_AND_SET),
        GET_AND_SET("getAndSet", AccessType.GET_AND_UPDATE),
        GET_AND_SET_ACQUIRE("getAndSetAcquire", AccessType.GET_AND_UPDATE),
        GET_AND_SET_RELEASE("getAndSetRelease", AccessType.GET_AND_UPDATE),
        GET_AND_ADD("getAndAdd", AccessType.GET_AND_UPDATE_NUMERIC),
        GET_AND_ADD_ACQUIRE("getAndAddAcquire", AccessType.GET_AND_UPDATE_NUMERIC),
        GET_AND_ADD_RELEASE("getAndAddRelease", AccessType.GET_AND_UPDATE_NUMERIC),
        GET_AND_BITWISE_OR("getAndBitwiseOr", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_OR_RELEASE("getAndBitwiseOrRelease", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_OR_ACQUIRE("getAndBitwiseOrAcquire", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_AND("getAndBitwiseAnd", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_AND_RELEASE("getAndBitwiseAndRelease", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_AND_ACQUIRE("getAndBitwiseAndAcquire", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_XOR("getAndBitwiseXor", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_XOR_RELEASE("getAndBitwiseXorRelease", AccessType.GET_AND_UPDATE_BITWISE),
        GET_AND_BITWISE_XOR_ACQUIRE("getAndBitwiseXorAcquire", AccessType.GET_AND_UPDATE_BITWISE);
        
        static final Map<String, AccessMode> methodNameToAccessMode = null;

        /* renamed from: at */
        final AccessType f549at;
        final String methodName;

        static {
            AccessMode[] values = values();
            methodNameToAccessMode = new HashMap(((int) (((float) values.length) / 0.75f)) + 1);
            for (AccessMode accessMode : values) {
                methodNameToAccessMode.put(accessMode.methodName, accessMode);
            }
        }

        private AccessMode(String str, AccessType accessType) {
            this.methodName = str;
            this.f549at = accessType;
        }

        public String methodName() {
            return this.methodName;
        }

        public static AccessMode valueFromMethodName(String str) {
            AccessMode accessMode = methodNameToAccessMode.get(str);
            if (accessMode != null) {
                return accessMode;
            }
            throw new IllegalArgumentException("No AccessMode value for method name " + str);
        }
    }

    public final Class<?> varType() {
        return this.varType;
    }

    public final List<Class<?>> coordinateTypes() {
        Class<?> cls = this.coordinateType0;
        if (cls == null) {
            return Collections.EMPTY_LIST;
        }
        Class<?> cls2 = this.coordinateType1;
        if (cls2 == null) {
            return Collections.singletonList(cls);
        }
        return Collections.unmodifiableList(Arrays.asList(cls, cls2));
    }

    public final MethodType accessModeType(AccessMode accessMode) {
        if (this.coordinateType1 == null) {
            return accessMode.f549at.accessModeType(this.coordinateType0, this.varType, new Class[0]);
        }
        return accessMode.f549at.accessModeType(this.coordinateType0, this.varType, this.coordinateType1);
    }

    public final boolean isAccessModeSupported(AccessMode accessMode) {
        int ordinal = 1 << accessMode.ordinal();
        if ((this.accessModesBitMask & ordinal) == ordinal) {
            return true;
        }
        return false;
    }

    public final MethodHandle toMethodHandle(AccessMode accessMode) {
        return MethodHandles.varHandleExactInvoker(accessMode, accessModeType(accessMode)).bindTo(this);
    }

    public static void fullFence() {
        UNSAFE.fullFence();
    }

    public static void acquireFence() {
        UNSAFE.loadFence();
    }

    public static void releaseFence() {
        UNSAFE.storeFence();
    }

    public static void loadLoadFence() {
        UNSAFE.loadFence();
    }

    public static void storeStoreFence() {
        UNSAFE.storeFence();
    }

    VarHandle(Class<?> cls, boolean z) {
        this.varType = (Class) Objects.requireNonNull(cls);
        this.coordinateType0 = null;
        this.coordinateType1 = null;
        this.accessModesBitMask = alignedAccessModesBitMask(cls, z);
    }

    VarHandle(Class<?> cls, boolean z, Class<?> cls2) {
        this.varType = (Class) Objects.requireNonNull(cls);
        this.coordinateType0 = (Class) Objects.requireNonNull(cls2);
        this.coordinateType1 = null;
        this.accessModesBitMask = alignedAccessModesBitMask(cls, z);
    }

    VarHandle(Class<?> cls, Class<?> cls2, boolean z, Class<?> cls3, Class<?> cls4) {
        this.varType = (Class) Objects.requireNonNull(cls);
        this.coordinateType0 = (Class) Objects.requireNonNull(cls3);
        this.coordinateType1 = (Class) Objects.requireNonNull(cls4);
        Objects.requireNonNull(cls2);
        Class<?> componentType = cls2.getComponentType();
        if (componentType != cls && componentType != Byte.TYPE) {
            throw new InternalError("Unsupported backingArrayType: " + cls2);
        } else if (cls2.getComponentType() == cls) {
            this.accessModesBitMask = alignedAccessModesBitMask(cls, z);
        } else {
            this.accessModesBitMask = unalignedAccessModesBitMask(cls);
        }
    }

    static int accessTypesToBitMask(EnumSet<AccessType> enumSet) {
        int i = 0;
        for (AccessMode accessMode : AccessMode.values()) {
            if (enumSet.contains(accessMode.f549at)) {
                i |= 1 << accessMode.ordinal();
            }
        }
        return i;
    }

    static int alignedAccessModesBitMask(Class<?> cls, boolean z) {
        int i = ALL_MODES_BIT_MASK;
        if (z) {
            i &= READ_ACCESS_MODES_BIT_MASK;
        }
        if (!(cls == Byte.TYPE || cls == Short.TYPE || cls == Character.TYPE || cls == Integer.TYPE || cls == Long.TYPE || cls == Float.TYPE || cls == Double.TYPE)) {
            i &= ~NUMERIC_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
        }
        return (cls == Boolean.TYPE || cls == Byte.TYPE || cls == Short.TYPE || cls == Character.TYPE || cls == Integer.TYPE || cls == Long.TYPE) ? i : i & (~BITWISE_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK);
    }

    static int unalignedAccessModesBitMask(Class<?> cls) {
        int i = READ_ACCESS_MODES_BIT_MASK | WRITE_ACCESS_MODES_BIT_MASK;
        if (cls == Integer.TYPE || cls == Long.TYPE || cls == Float.TYPE || cls == Double.TYPE) {
            i |= ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
        }
        if (cls == Integer.TYPE || cls == Long.TYPE) {
            i |= NUMERIC_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK;
        }
        return (cls == Integer.TYPE || cls == Long.TYPE) ? i | BITWISE_ATOMIC_UPDATE_ACCESS_MODES_BIT_MASK : i;
    }
}
