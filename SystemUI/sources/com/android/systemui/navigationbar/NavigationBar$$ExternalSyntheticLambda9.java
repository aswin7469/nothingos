package com.android.systemui.navigationbar;

import android.os.Handler;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda9 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda9(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        boolean unused = this.f$0.post(runnable);
    }
}
