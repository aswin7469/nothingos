package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.common.SingleInstanceRemoteListener;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3559xc118fe9b implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ C3559xc118fe9b(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((ISplitScreenListener) obj).onStagePositionChanged(this.f$0, this.f$1);
    }
}
