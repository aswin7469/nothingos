package com.android.p019wm.shell.legacysplitscreen;

import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.BiConsumer;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3494x586845b2 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ BiConsumer f$1;

    public /* synthetic */ C3494x586845b2(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, BiConsumer biConsumer) {
        this.f$0 = splitScreenImpl;
        this.f$1 = biConsumer;
    }

    public final void run() {
        this.f$0.mo49745x31964bb9(this.f$1);
    }
}
