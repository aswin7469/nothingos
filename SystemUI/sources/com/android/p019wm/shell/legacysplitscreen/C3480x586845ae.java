package com.android.p019wm.shell.legacysplitscreen;

import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3480x586845ae implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ C3480x586845ae(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, Consumer consumer) {
        this.f$0 = splitScreenImpl;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.mo49737x1213837f(this.f$1);
    }
}
