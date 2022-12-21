package com.android.p019wm.shell.stagesplit;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        this.f$0 = pendingIntent;
        this.f$1 = intent;
        this.f$2 = i;
        this.f$3 = bundle;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).startIntent(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
