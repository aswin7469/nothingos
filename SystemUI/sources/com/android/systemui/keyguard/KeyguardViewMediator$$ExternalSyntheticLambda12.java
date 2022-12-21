package com.android.systemui.keyguard;

import android.os.Handler;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda12 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda12(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        boolean unused = this.f$0.post(runnable);
    }
}
