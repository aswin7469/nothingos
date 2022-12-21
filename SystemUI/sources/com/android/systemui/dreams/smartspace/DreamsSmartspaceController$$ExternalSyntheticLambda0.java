package com.android.systemui.dreams.smartspace;

import android.app.smartspace.SmartspaceTargetEvent;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DreamsSmartspaceController$$ExternalSyntheticLambda0 implements BcSmartspaceDataPlugin.SmartspaceEventNotifier {
    public final /* synthetic */ DreamsSmartspaceController f$0;

    public /* synthetic */ DreamsSmartspaceController$$ExternalSyntheticLambda0(DreamsSmartspaceController dreamsSmartspaceController) {
        this.f$0 = dreamsSmartspaceController;
    }

    public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
        DreamsSmartspaceController.m2744connectSession$lambda2(this.f$0, smartspaceTargetEvent);
    }
}
