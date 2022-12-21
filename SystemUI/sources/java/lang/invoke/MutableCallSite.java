package java.lang.invoke;

public class MutableCallSite extends CallSite {
    public MutableCallSite(MethodType methodType) {
        super(methodType);
    }

    public MutableCallSite(MethodHandle methodHandle) {
        super(methodHandle);
    }

    public final MethodHandle getTarget() {
        return this.target;
    }

    public void setTarget(MethodHandle methodHandle) {
        checkTargetChange(this.target, methodHandle);
        setTargetNormal(methodHandle);
    }

    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }
}
