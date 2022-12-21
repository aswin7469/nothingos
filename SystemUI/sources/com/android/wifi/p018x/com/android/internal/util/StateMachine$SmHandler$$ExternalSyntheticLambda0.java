package com.android.wifi.p018x.com.android.internal.util;

import com.android.wifi.p018x.com.android.internal.util.StateMachine;
import java.util.function.Predicate;

/* renamed from: com.android.wifi.x.com.android.internal.util.StateMachine$SmHandler$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StateMachine$SmHandler$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ StateMachine.SmHandler.StateInfo f$0;

    public /* synthetic */ StateMachine$SmHandler$$ExternalSyntheticLambda0(StateMachine.SmHandler.StateInfo stateInfo) {
        this.f$0 = stateInfo;
    }

    public final boolean test(Object obj) {
        return StateMachine.SmHandler.lambda$removeState$0(this.f$0, (StateMachine.SmHandler.StateInfo) obj);
    }
}
