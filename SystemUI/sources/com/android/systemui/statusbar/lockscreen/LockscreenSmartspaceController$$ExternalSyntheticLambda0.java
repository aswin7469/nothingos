package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceTargetEvent;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenSmartspaceController$$ExternalSyntheticLambda0 implements BcSmartspaceDataPlugin.SmartspaceEventNotifier {
    public final /* synthetic */ LockscreenSmartspaceController f$0;

    public /* synthetic */ LockscreenSmartspaceController$$ExternalSyntheticLambda0(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.f$0 = lockscreenSmartspaceController;
    }

    public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
        LockscreenSmartspaceController.m3084connectSession$lambda2(this.f$0, smartspaceTargetEvent);
    }
}
