package com.android.systemui.recents;

import android.os.Bundle;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$3$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ Bundle f$0;

    public /* synthetic */ OverviewProxyService$3$$ExternalSyntheticLambda1(Bundle bundle) {
        this.f$0 = bundle;
    }

    public final void accept(Object obj) {
        this.f$0.putBinder(QuickStepContract.KEY_EXTRA_SHELL_SPLIT_SCREEN, ((SplitScreen) obj).createExternalInterface().asBinder());
    }
}
