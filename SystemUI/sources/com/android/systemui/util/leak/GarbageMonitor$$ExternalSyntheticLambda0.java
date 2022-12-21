package com.android.systemui.util.leak;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class GarbageMonitor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ GarbageMonitor f$0;

    public /* synthetic */ GarbageMonitor$$ExternalSyntheticLambda0(GarbageMonitor garbageMonitor) {
        this.f$0 = garbageMonitor;
    }

    public final void run() {
        this.f$0.reinspectGarbageAfterGc();
    }
}
