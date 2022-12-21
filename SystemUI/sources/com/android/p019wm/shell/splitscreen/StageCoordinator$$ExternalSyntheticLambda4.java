package com.android.p019wm.shell.splitscreen;

import android.window.TransitionInfo;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenTransitions;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SplitScreenTransitions.DismissTransition f$1;
    public final /* synthetic */ TransitionInfo f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda4(StageCoordinator stageCoordinator, SplitScreenTransitions.DismissTransition dismissTransition, TransitionInfo transitionInfo) {
        this.f$0 = stageCoordinator;
        this.f$1 = dismissTransition;
        this.f$2 = transitionInfo;
    }

    public final void accept(Object obj) {
        this.f$0.mo50843xc118a457(this.f$1, this.f$2, (RecentTasksController) obj);
    }
}
