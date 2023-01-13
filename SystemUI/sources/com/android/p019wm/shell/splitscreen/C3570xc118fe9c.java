package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.common.SingleInstanceRemoteListener;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3570xc118fe9c implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ C3570xc118fe9c(int i, int i2, boolean z) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = z;
    }

    public final void accept(Object obj) {
        ((ISplitScreenListener) obj).onTaskStageChanged(this.f$0, this.f$1, this.f$2);
    }
}
