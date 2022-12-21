package com.android.systemui.p012qs.tiles.dialog;

import android.os.Handler;
import java.util.concurrent.Executor;

/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda0 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ InternetDialogController$$ExternalSyntheticLambda0(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        boolean unused = this.f$0.post(runnable);
    }
}
