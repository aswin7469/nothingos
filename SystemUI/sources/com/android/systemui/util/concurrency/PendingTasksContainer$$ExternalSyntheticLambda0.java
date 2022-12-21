package com.android.systemui.util.concurrency;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PendingTasksContainer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PendingTasksContainer f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ PendingTasksContainer$$ExternalSyntheticLambda0(PendingTasksContainer pendingTasksContainer, String str) {
        this.f$0 = pendingTasksContainer;
        this.f$1 = str;
    }

    public final void run() {
        PendingTasksContainer.m3307registerTask$lambda0(this.f$0, this.f$1);
    }
}
