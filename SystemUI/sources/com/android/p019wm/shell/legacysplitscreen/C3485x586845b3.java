package com.android.p019wm.shell.legacysplitscreen;

import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3485x586845b3 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ C3485x586845b3(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, Consumer consumer) {
        this.f$0 = splitScreenImpl;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.mo49740x31a3e4f7(this.f$1);
    }
}
