package com.android.settingslib.mobile;

import android.os.Handler;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MobileStatusTracker$$ExternalSyntheticLambda0 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ MobileStatusTracker$$ExternalSyntheticLambda0(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        boolean unused = this.f$0.post(runnable);
    }
}
