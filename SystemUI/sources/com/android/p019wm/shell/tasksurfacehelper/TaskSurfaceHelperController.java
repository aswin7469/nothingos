package com.android.p019wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.ShellExecutor;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController */
public class TaskSurfaceHelperController {
    private final TaskSurfaceHelperImpl mImpl = new TaskSurfaceHelperImpl();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final ShellTaskOrganizer mTaskOrganizer;

    public TaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
    }

    public TaskSurfaceHelper asTaskSurfaceHelper() {
        return this.mImpl;
    }

    public void setGameModeForTask(int i, int i2) {
        this.mTaskOrganizer.setSurfaceMetadata(i, 8, i2);
    }

    public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        this.mTaskOrganizer.screenshotTask(runningTaskInfo, rect, consumer);
    }

    /* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl */
    private class TaskSurfaceHelperImpl implements TaskSurfaceHelper {
        private TaskSurfaceHelperImpl() {
        }

        public void setGameModeForTask(int i, int i2) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new C3607x3c1cf256(this, i, i2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setGameModeForTask$0$com-android-wm-shell-tasksurfacehelper-TaskSurfaceHelperController$TaskSurfaceHelperImpl */
        public /* synthetic */ void mo51225x15e153a7(int i, int i2) {
            TaskSurfaceHelperController.this.setGameModeForTask(i, i2);
        }

        public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new C3605x3c1cf254(this, runningTaskInfo, rect, executor, consumer));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$screenshotTask$3$com-android-wm-shell-tasksurfacehelper-TaskSurfaceHelperController$TaskSurfaceHelperImpl */
        public /* synthetic */ void mo51224xe141e4b0(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer consumer) {
            TaskSurfaceHelperController.this.screenshotTask(runningTaskInfo, rect, new C3604x3c1cf253(executor, consumer));
        }
    }
}
