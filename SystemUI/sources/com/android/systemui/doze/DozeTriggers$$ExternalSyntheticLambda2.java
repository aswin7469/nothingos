package com.android.systemui.doze;

import com.android.systemui.doze.DozeMachine;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ DozeTriggers f$0;
    public final /* synthetic */ DozeMachine.State f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda2(DozeTriggers dozeTriggers, DozeMachine.State state, int i) {
        this.f$0 = dozeTriggers;
        this.f$1 = state;
        this.f$2 = i;
    }

    public final void accept(Object obj) {
        this.f$0.m2745lambda$onWakeScreen$4$comandroidsystemuidozeDozeTriggers(this.f$1, this.f$2, (Boolean) obj);
    }
}
