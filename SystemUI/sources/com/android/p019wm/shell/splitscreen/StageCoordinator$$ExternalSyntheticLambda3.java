package com.android.p019wm.shell.splitscreen;

import android.app.ActivityManager;
import com.android.p019wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda3(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = runningTaskInfo;
    }

    public final void accept(Object obj) {
        ((RecentTasksController) obj).removeSplitPair(this.f$0.taskId);
    }
}
