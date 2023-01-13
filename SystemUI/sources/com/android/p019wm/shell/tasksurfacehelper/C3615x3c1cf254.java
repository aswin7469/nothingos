package com.android.p019wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3615x3c1cf254 implements Runnable {
    public final /* synthetic */ TaskSurfaceHelperController.TaskSurfaceHelperImpl f$0;
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$1;
    public final /* synthetic */ Rect f$2;
    public final /* synthetic */ Executor f$3;
    public final /* synthetic */ Consumer f$4;

    public /* synthetic */ C3615x3c1cf254(TaskSurfaceHelperController.TaskSurfaceHelperImpl taskSurfaceHelperImpl, ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer consumer) {
        this.f$0 = taskSurfaceHelperImpl;
        this.f$1 = runningTaskInfo;
        this.f$2 = rect;
        this.f$3 = executor;
        this.f$4 = consumer;
    }

    public final void run() {
        this.f$0.mo51235xe141e4b0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
