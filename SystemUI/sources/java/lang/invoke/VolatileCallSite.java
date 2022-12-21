package java.lang.invoke;

public class VolatileCallSite extends CallSite {
    public VolatileCallSite(MethodType methodType) {
        super(methodType);
    }

    public VolatileCallSite(MethodHandle methodHandle) {
        super(methodHandle);
    }

    public final MethodHandle getTarget() {
        return getTargetVolatile();
    }

    public void setTarget(MethodHandle methodHandle) {
        checkTargetChange(getTargetVolatile(), methodHandle);
        setTargetVolatile(methodHandle);
    }

    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }
}
