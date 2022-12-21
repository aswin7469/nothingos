package java.lang.invoke;

import dalvik.system.EmulatedStackFrame;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.Transformers;
import java.util.List;
import java.util.Objects;

public abstract class MethodHandle {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int IGET = 8;
    public static final int INVOKE_DIRECT = 2;
    public static final int INVOKE_INTERFACE = 4;
    public static final int INVOKE_STATIC = 3;
    public static final int INVOKE_SUPER = 1;
    public static final int INVOKE_TRANSFORM = 5;
    public static final int INVOKE_VAR_HANDLE = 6;
    public static final int INVOKE_VAR_HANDLE_EXACT = 7;
    public static final int INVOKE_VIRTUAL = 0;
    public static final int IPUT = 9;
    public static final int SGET = 10;
    public static final int SPUT = 11;
    protected final long artFieldOrMethod;
    MethodHandle asTypeCache;
    private MethodHandle cachedSpreadInvoker;
    protected final int handleKind;
    private final MethodType type;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PolymorphicSignature {
    }

    @PolymorphicSignature
    public final native Object invoke(Object... objArr) throws Throwable;

    @PolymorphicSignature
    public final native Object invokeExact(Object... objArr) throws Throwable;

    /* access modifiers changed from: package-private */
    public native void invokeExactWithFrame(EmulatedStackFrame emulatedStackFrame) throws Throwable;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.lang.invoke.MethodHandle.invokeWithArguments(java.lang.Object[]):java.lang.Object, dex: classes3.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public java.lang.Object invokeWithArguments(java.lang.Object... r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.lang.invoke.MethodHandle.invokeWithArguments(java.lang.Object[]):java.lang.Object, dex: classes3.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.invoke.MethodHandle.invokeWithArguments(java.lang.Object[]):java.lang.Object");
    }

    public boolean isVarargsCollector() {
        return false;
    }

    protected MethodHandle(long j, int i, MethodType methodType) {
        this.artFieldOrMethod = j;
        this.handleKind = i;
        this.type = methodType;
    }

    public MethodType type() {
        return this.type;
    }

    public Object invokeWithArguments(List<?> list) throws Throwable {
        return invokeWithArguments(list.toArray());
    }

    public MethodHandle asType(MethodType methodType) {
        if (methodType.equals((Object) this.type)) {
            return this;
        }
        MethodHandle asTypeCached = asTypeCached(methodType);
        if (asTypeCached != null) {
            return asTypeCached;
        }
        return asTypeUncached(methodType);
    }

    private MethodHandle asTypeCached(MethodType methodType) {
        MethodHandle methodHandle = this.asTypeCache;
        if (methodHandle == null || !methodType.equals((Object) methodHandle.type)) {
            return null;
        }
        return methodHandle;
    }

    /* access modifiers changed from: package-private */
    public MethodHandle asTypeUncached(MethodType methodType) {
        if (this.type.isConvertibleTo(methodType)) {
            Transformers.AsTypeAdapter asTypeAdapter = new Transformers.AsTypeAdapter(this, methodType);
            this.asTypeCache = asTypeAdapter;
            return asTypeAdapter;
        }
        throw new WrongMethodTypeException("cannot convert " + this + " to " + methodType);
    }

    public MethodHandle asSpreader(Class<?> cls, int i) {
        return asSpreader(type().parameterCount() - i, cls, i);
    }

    public MethodHandle asSpreader(int i, Class<?> cls, int i2) {
        MethodType asSpreaderChecks = asSpreaderChecks(cls, i, i2);
        return new Transformers.Spreader(asType(asSpreaderChecks), asSpreaderChecks.replaceParameterTypes(i, i + i2, cls), i, i2);
    }

    private MethodType asSpreaderChecks(Class<?> cls, int i, int i2) {
        int i3;
        spreadArrayChecks(cls, i2);
        int parameterCount = type().parameterCount();
        if (parameterCount < i2 || i2 < 0) {
            throw MethodHandleStatics.newIllegalArgumentException("bad spread array length");
        } else if (i < 0 || (i3 = i + i2) > parameterCount) {
            throw MethodHandleStatics.newIllegalArgumentException("bad spread position");
        } else {
            Class<?> componentType = cls.getComponentType();
            MethodType type2 = type();
            boolean z = true;
            int i4 = i;
            boolean z2 = true;
            while (true) {
                if (i4 >= i3) {
                    z = false;
                    break;
                }
                Class<?> parameterType = type2.parameterType(i4);
                if (parameterType != componentType) {
                    if (!MethodType.canConvert(componentType, parameterType)) {
                        z2 = false;
                        break;
                    }
                    z2 = false;
                }
                i4++;
            }
            if (z2) {
                return type2;
            }
            MethodType asSpreaderType = type2.asSpreaderType(cls, i, i2);
            if (!z) {
                return asSpreaderType;
            }
            asType(asSpreaderType);
            throw MethodHandleStatics.newInternalError("should not return");
        }
    }

    private void spreadArrayChecks(Class<?> cls, int i) {
        Class<?> componentType = cls.getComponentType();
        if (componentType == null) {
            throw MethodHandleStatics.newIllegalArgumentException("not an array type", cls);
        } else if ((i & 127) == i) {
        } else {
            if ((i & 255) != i) {
                throw MethodHandleStatics.newIllegalArgumentException("array length is not legal", Integer.valueOf(i));
            } else if (componentType == Long.TYPE || componentType == Double.TYPE) {
                throw MethodHandleStatics.newIllegalArgumentException("array length is not legal for long[] or double[]", Integer.valueOf(i));
            }
        }
    }

    public MethodHandle withVarargs(boolean z) {
        return z ? asVarargsCollector(type().lastParameterType()) : this;
    }

    public MethodHandle asCollector(Class<?> cls, int i) {
        return asCollector(type().parameterCount() - 1, cls, i);
    }

    public MethodHandle asCollector(int i, Class<?> cls, int i2) {
        asCollectorChecks(cls, i, i2);
        return new Transformers.Collector(this, cls, i, i2);
    }

    /* access modifiers changed from: package-private */
    public boolean asCollectorChecks(Class<?> cls, int i, int i2) {
        spreadArrayChecks(cls, i2);
        int parameterCount = type().parameterCount();
        if (i < 0 || i >= parameterCount) {
            throw MethodHandleStatics.newIllegalArgumentException("bad collect position");
        }
        if (parameterCount != 0) {
            Class<?> parameterType = type().parameterType(i);
            if (parameterType == cls) {
                return true;
            }
            if (parameterType.isAssignableFrom(cls)) {
                return false;
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("array type not assignable to argument", this, cls);
    }

    public MethodHandle asVarargsCollector(Class<?> cls) {
        Objects.requireNonNull(cls);
        boolean asCollectorChecks = asCollectorChecks(cls, type().parameterCount() - 1, 0);
        if (!isVarargsCollector() || !asCollectorChecks) {
            return new Transformers.VarargsCollector(this);
        }
        return this;
    }

    public MethodHandle asFixedArity() {
        return isVarargsCollector() ? ((Transformers.VarargsCollector) this).asFixedArity() : this;
    }

    public MethodHandle bindTo(Object obj) {
        return new Transformers.BindTo(this, this.type.leadingReferenceParameter().cast(obj));
    }

    public String toString() {
        return standardString();
    }

    /* access modifiers changed from: package-private */
    public String standardString() {
        return "MethodHandle" + this.type;
    }

    public int getHandleKind() {
        int i = this.handleKind;
        if (i == 7 || i == 6) {
            return 0;
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
        throw new AssertionError((Object) "MethodHandle.transform should never be called.");
    }

    /* access modifiers changed from: protected */
    public MethodHandle duplicate() {
        try {
            return (MethodHandle) clone();
        } catch (CloneNotSupportedException unused) {
            throw new AssertionError((Object) "Subclass of Transformer is not cloneable");
        }
    }

    private void transformInternal(EmulatedStackFrame emulatedStackFrame) throws Throwable {
        transform(emulatedStackFrame);
    }
}
