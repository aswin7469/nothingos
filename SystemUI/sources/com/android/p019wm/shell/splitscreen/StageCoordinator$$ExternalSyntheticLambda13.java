package com.android.p019wm.shell.splitscreen;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda13 implements Consumer {
    public final /* synthetic */ StageCoordinator f$0;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda13(StageCoordinator stageCoordinator) {
        this.f$0 = stageCoordinator;
    }

    public final void accept(Object obj) {
        this.f$0.onFoldedStateChanged(((Boolean) obj).booleanValue());
    }
}
