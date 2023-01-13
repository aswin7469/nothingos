package com.android.p019wm.shell.back;

import android.window.IOnBackInvokedCallback;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.back.BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3374x62452dae implements Consumer {
    public final /* synthetic */ IOnBackInvokedCallback f$0;

    public /* synthetic */ C3374x62452dae(IOnBackInvokedCallback iOnBackInvokedCallback) {
        this.f$0 = iOnBackInvokedCallback;
    }

    public final void accept(Object obj) {
        ((BackAnimationController) obj).setBackToLauncherCallback(this.f$0);
    }
}
