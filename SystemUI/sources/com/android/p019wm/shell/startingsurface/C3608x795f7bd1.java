package com.android.p019wm.shell.startingsurface;

import com.android.p019wm.shell.common.SingleInstanceRemoteListener;

/* renamed from: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3608x795f7bd1 implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ Integer f$0;
    public final /* synthetic */ Integer f$1;
    public final /* synthetic */ Integer f$2;

    public /* synthetic */ C3608x795f7bd1(Integer num, Integer num2, Integer num3) {
        this.f$0 = num;
        this.f$1 = num2;
        this.f$2 = num3;
    }

    public final void accept(Object obj) {
        ((IStartingWindowListener) obj).onTaskLaunching(this.f$0.intValue(), this.f$1.intValue(), this.f$2.intValue());
    }
}
