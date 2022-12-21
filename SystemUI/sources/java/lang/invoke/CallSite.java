package java.lang.invoke;

import java.lang.invoke.MethodHandles;

public abstract class CallSite {
    private static MethodHandle GET_TARGET;
    private static final long TARGET_OFFSET;
    MethodHandle target;

    public abstract MethodHandle dynamicInvoker();

    public abstract MethodHandle getTarget();

    public abstract void setTarget(MethodHandle methodHandle);

    CallSite(MethodType methodType) {
        MethodHandle throwException = MethodHandles.throwException(methodType.returnType(), IllegalStateException.class);
        this.target = throwException;
        this.target = MethodHandles.insertArguments(throwException, 0, new IllegalStateException("uninitialized call site"));
        if (methodType.parameterCount() > 0) {
            this.target = MethodHandles.dropArguments(this.target, 0, (Class<?>[]) methodType.ptypes());
        }
        initializeGetTarget();
    }

    CallSite(MethodHandle methodHandle) {
        methodHandle.type();
        this.target = methodHandle;
        initializeGetTarget();
    }

    CallSite(MethodType methodType, MethodHandle methodHandle) throws Throwable {
        this(methodType);
        MethodHandle methodHandle2 = (MethodHandle) methodHandle.invokeWithArguments((ConstantCallSite) this);
        checkTargetChange(this.target, methodHandle2);
        this.target = methodHandle2;
        initializeGetTarget();
    }

    public MethodType type() {
        return this.target.type();
    }

    /* access modifiers changed from: package-private */
    public void checkTargetChange(MethodHandle methodHandle, MethodHandle methodHandle2) {
        MethodType type = methodHandle.type();
        if (!methodHandle2.type().equals((Object) type)) {
            throw wrongTargetType(methodHandle2, type);
        }
    }

    private static WrongMethodTypeException wrongTargetType(MethodHandle methodHandle, MethodType methodType) {
        return new WrongMethodTypeException(String.valueOf((Object) methodHandle) + " should be of type " + methodType);
    }

    /* access modifiers changed from: package-private */
    public MethodHandle makeDynamicInvoker() {
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(type()), GET_TARGET.bindTo(this));
    }

    private void initializeGetTarget() {
        synchronized (CallSite.class) {
            if (GET_TARGET == null) {
                try {
                    GET_TARGET = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(CallSite.class, "getTarget", MethodType.methodType(MethodHandle.class));
                } catch (ReflectiveOperationException e) {
                    throw new InternalError((Throwable) e);
                }
            }
        }
    }

    static {
        try {
            TARGET_OFFSET = MethodHandleStatics.UNSAFE.objectFieldOffset(CallSite.class.getDeclaredField("target"));
        } catch (Exception e) {
            throw new Error((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTargetNormal(MethodHandle methodHandle) {
        this.target = methodHandle;
    }

    /* access modifiers changed from: package-private */
    public MethodHandle getTargetVolatile() {
        return (MethodHandle) MethodHandleStatics.UNSAFE.getObjectVolatile(this, TARGET_OFFSET);
    }

    /* access modifiers changed from: package-private */
    public void setTargetVolatile(MethodHandle methodHandle) {
        MethodHandleStatics.UNSAFE.putObjectVolatile(this, TARGET_OFFSET, methodHandle);
    }
}
