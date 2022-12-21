package android.net.util;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MultinetworkPolicyTracker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MultinetworkPolicyTracker f$0;

    public /* synthetic */ MultinetworkPolicyTracker$$ExternalSyntheticLambda0(MultinetworkPolicyTracker multinetworkPolicyTracker) {
        this.f$0 = multinetworkPolicyTracker;
    }

    public final void run() {
        this.f$0.reevaluateInternal();
    }
}
