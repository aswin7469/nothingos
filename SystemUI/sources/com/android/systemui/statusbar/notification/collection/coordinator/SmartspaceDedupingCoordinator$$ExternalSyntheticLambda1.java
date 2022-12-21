package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SmartspaceDedupingCoordinator$$ExternalSyntheticLambda1 implements BcSmartspaceDataPlugin.SmartspaceTargetListener {
    public final /* synthetic */ SmartspaceDedupingCoordinator f$0;

    public /* synthetic */ SmartspaceDedupingCoordinator$$ExternalSyntheticLambda1(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.f$0 = smartspaceDedupingCoordinator;
    }

    public final void onSmartspaceTargetsUpdated(List list) {
        this.f$0.onNewSmartspaceTargets(list);
    }
}
