package com.android.p019wm.shell.startingsurface;

import android.os.IBinder;
import android.window.StartingWindowInfo;

/* renamed from: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StartingWindowController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StartingWindowController f$0;
    public final /* synthetic */ StartingWindowInfo f$1;
    public final /* synthetic */ IBinder f$2;

    public /* synthetic */ StartingWindowController$$ExternalSyntheticLambda0(StartingWindowController startingWindowController, StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        this.f$0 = startingWindowController;
        this.f$1 = startingWindowInfo;
        this.f$2 = iBinder;
    }

    public final void run() {
        this.f$0.mo51184xac73a8d2(this.f$1, this.f$2);
    }
}
