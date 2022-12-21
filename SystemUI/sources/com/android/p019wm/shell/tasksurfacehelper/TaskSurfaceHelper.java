package com.android.p019wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper */
public interface TaskSurfaceHelper {
    void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
    }

    void setGameModeForTask(int i, int i2) {
    }
}
