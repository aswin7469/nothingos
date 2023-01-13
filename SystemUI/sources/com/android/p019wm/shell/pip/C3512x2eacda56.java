package com.android.p019wm.shell.pip;

import com.android.p019wm.shell.pip.PinnedStackListenerForwarder;

/* renamed from: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3512x2eacda56 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ C3512x2eacda56(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, boolean z, int i) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = z;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.mo49993x9304c9c1(this.f$1, this.f$2);
    }
}
