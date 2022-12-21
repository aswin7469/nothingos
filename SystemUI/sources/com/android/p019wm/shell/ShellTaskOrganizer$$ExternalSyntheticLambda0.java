package com.android.p019wm.shell;

import android.app.ActivityManager;
import com.android.p019wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ShellTaskOrganizer$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;

    public /* synthetic */ ShellTaskOrganizer$$ExternalSyntheticLambda0(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = runningTaskInfo;
    }

    public final void accept(Object obj) {
        ((RecentTasksController) obj).onTaskRemoved(this.f$0);
    }
}
