package com.android.systemui.statusbar.notification.collection.coordinator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SmartspaceDedupingCoordinator$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TrackedSmartspaceTarget f$0;
    public final /* synthetic */ SmartspaceDedupingCoordinator f$1;

    public /* synthetic */ SmartspaceDedupingCoordinator$$ExternalSyntheticLambda0(TrackedSmartspaceTarget trackedSmartspaceTarget, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.f$0 = trackedSmartspaceTarget;
        this.f$1 = smartspaceDedupingCoordinator;
    }

    public final void run() {
        SmartspaceDedupingCoordinator.m3118updateAlertException$lambda3(this.f$0, this.f$1);
    }
}
