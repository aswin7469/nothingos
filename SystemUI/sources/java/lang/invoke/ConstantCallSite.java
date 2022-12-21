package java.lang.invoke;

public class ConstantCallSite extends CallSite {
    private final boolean isFrozen = true;

    public ConstantCallSite(MethodHandle methodHandle) {
        super(methodHandle);
    }

    protected ConstantCallSite(MethodType methodType, MethodHandle methodHandle) throws Throwable {
        super(methodType, methodHandle);
    }

    public final MethodHandle getTarget() {
        if (this.isFrozen) {
            return this.target;
        }
        throw new IllegalStateException();
    }

    public final void setTarget(MethodHandle methodHandle) {
        throw new UnsupportedOperationException();
    }

    public final MethodHandle dynamicInvoker() {
        return getTarget();
    }
}
