package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceSession;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenSmartspaceController$$ExternalSyntheticLambda1 implements SmartspaceSession.OnTargetsAvailableListener {
    public final /* synthetic */ LockscreenSmartspaceController f$0;

    public /* synthetic */ LockscreenSmartspaceController$$ExternalSyntheticLambda1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.f$0 = lockscreenSmartspaceController;
    }

    public final void onTargetsAvailable(List list) {
        LockscreenSmartspaceController.m3081sessionListener$lambda0(this.f$0, list);
    }
}
