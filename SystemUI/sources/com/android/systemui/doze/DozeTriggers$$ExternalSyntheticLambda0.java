package com.android.systemui.doze;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ DozeTriggers f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda0(DozeTriggers dozeTriggers, Runnable runnable, int i) {
        this.f$0 = dozeTriggers;
        this.f$1 = runnable;
        this.f$2 = i;
    }

    public final void accept(Object obj) {
        this.f$0.m2741lambda$requestPulse$5$comandroidsystemuidozeDozeTriggers(this.f$1, this.f$2, (Boolean) obj);
    }
}
