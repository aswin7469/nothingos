package com.android.systemui.statusbar;

import com.android.systemui.plugins.ActivityStarter;
import kotlin.jvm.functions.Function1;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenShadeTransitionController$$ExternalSyntheticLambda0 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ LockscreenShadeTransitionController f$0;
    public final /* synthetic */ Function1 f$1;

    public /* synthetic */ LockscreenShadeTransitionController$$ExternalSyntheticLambda0(LockscreenShadeTransitionController lockscreenShadeTransitionController, Function1 function1) {
        this.f$0 = lockscreenShadeTransitionController;
        this.f$1 = function1;
    }

    public final boolean onDismiss() {
        return LockscreenShadeTransitionController.m3032goToLockedShadeInternal$lambda5(this.f$0, this.f$1);
    }
}
