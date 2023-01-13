package com.android.p019wm.shell.bubbles;

import android.app.PendingIntent;
import com.android.p019wm.shell.bubbles.Bubbles;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Bubble$$ExternalSyntheticLambda0 implements PendingIntent.CancelListener {
    public final /* synthetic */ Bubble f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ Bubbles.PendingIntentCanceledListener f$2;

    public /* synthetic */ Bubble$$ExternalSyntheticLambda0(Bubble bubble, Executor executor, Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener) {
        this.f$0 = bubble;
        this.f$1 = executor;
        this.f$2 = pendingIntentCanceledListener;
    }

    public final void onCanceled(PendingIntent pendingIntent) {
        this.f$0.m3413lambda$new$1$comandroidwmshellbubblesBubble(this.f$1, this.f$2, pendingIntent);
    }
}
