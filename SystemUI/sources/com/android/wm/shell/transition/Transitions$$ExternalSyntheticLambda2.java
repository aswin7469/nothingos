package com.android.wm.shell.transition;

import android.os.IBinder;
import com.android.wm.shell.transition.Transitions;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class Transitions$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ Transitions$$ExternalSyntheticLambda2 INSTANCE = new Transitions$$ExternalSyntheticLambda2();

    private /* synthetic */ Transitions$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        IBinder iBinder;
        iBinder = ((Transitions.ActiveTransition) obj).mToken;
        return iBinder;
    }
}
