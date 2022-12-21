package com.android.systemui.dreams.smartspace;

import android.app.smartspace.SmartspaceSession;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DreamsSmartspaceController$$ExternalSyntheticLambda1 implements SmartspaceSession.OnTargetsAvailableListener {
    public final /* synthetic */ DreamsSmartspaceController f$0;

    public /* synthetic */ DreamsSmartspaceController$$ExternalSyntheticLambda1(DreamsSmartspaceController dreamsSmartspaceController) {
        this.f$0 = dreamsSmartspaceController;
    }

    public final void onTargetsAvailable(List list) {
        DreamsSmartspaceController.m2745sessionListener$lambda1(this.f$0, list);
    }
}
