package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipTaskOrganizer;

/* renamed from: com.android.wm.shell.dagger.TvPipModule$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TvPipModule$$ExternalSyntheticLambda1 implements PipAppOpsListener.Callback {
    public final /* synthetic */ PipTaskOrganizer f$0;

    public /* synthetic */ TvPipModule$$ExternalSyntheticLambda1(PipTaskOrganizer pipTaskOrganizer) {
        this.f$0 = pipTaskOrganizer;
    }

    public final void dismissPip() {
        this.f$0.removePip();
    }
}
