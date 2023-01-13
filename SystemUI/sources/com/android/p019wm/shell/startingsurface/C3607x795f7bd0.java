package com.android.p019wm.shell.startingsurface;

import com.android.p019wm.shell.startingsurface.StartingWindowController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3607x795f7bd0 implements Consumer {
    public final /* synthetic */ StartingWindowController.IStartingWindowImpl f$0;
    public final /* synthetic */ IStartingWindowListener f$1;

    public /* synthetic */ C3607x795f7bd0(StartingWindowController.IStartingWindowImpl iStartingWindowImpl, IStartingWindowListener iStartingWindowListener) {
        this.f$0 = iStartingWindowImpl;
        this.f$1 = iStartingWindowListener;
    }

    public final void accept(Object obj) {
        this.f$0.mo51209xe0563840(this.f$1, (StartingWindowController) obj);
    }
}
