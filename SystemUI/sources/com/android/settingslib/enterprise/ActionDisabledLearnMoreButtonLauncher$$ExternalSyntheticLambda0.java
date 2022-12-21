package com.android.settingslib.enterprise;

import android.content.Context;
import com.android.settingslib.RestrictedLockUtils;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ActionDisabledLearnMoreButtonLauncher f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ RestrictedLockUtils.EnforcedAdmin f$2;

    public /* synthetic */ ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0(ActionDisabledLearnMoreButtonLauncher actionDisabledLearnMoreButtonLauncher, Context context, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.f$0 = actionDisabledLearnMoreButtonLauncher;
        this.f$1 = context;
        this.f$2 = enforcedAdmin;
    }

    public final void run() {
        this.f$0.mo28695xd743b19d(this.f$1, this.f$2);
    }
}
