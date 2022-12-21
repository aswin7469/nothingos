package com.android.p019wm.shell.fullscreen;

import android.app.ActivityManager;
import com.android.p019wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FullscreenTaskListener$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;

    public /* synthetic */ FullscreenTaskListener$$ExternalSyntheticLambda1(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = runningTaskInfo;
    }

    public final void accept(Object obj) {
        FullscreenTaskListener.lambda$updateRecentsForVisibleFullscreenTask$2(this.f$0, (RecentTasksController) obj);
    }
}
