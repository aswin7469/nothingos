package com.android.p019wm.shell.splitscreen;

import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.common.DisplayChangeController;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda6 implements DisplayChangeController.OnDisplayChangingListener {
    public final /* synthetic */ StageCoordinator f$0;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda6(StageCoordinator stageCoordinator) {
        this.f$0 = stageCoordinator;
    }

    public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        this.f$0.onRotateDisplay(i, i2, i3, windowContainerTransaction);
    }
}
