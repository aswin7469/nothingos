package com.android.p019wm.shell.freeform;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.freeform.FreeformTaskListener */
public class FreeformTaskListener implements ShellTaskOrganizer.TaskListener {
    private static final String TAG = "FreeformTaskListener";
    private final SyncTransactionQueue mSyncQueue;
    private final SparseArray<State> mTasks = new SparseArray<>();

    public String toString() {
        return TAG;
    }

    /* renamed from: com.android.wm.shell.freeform.FreeformTaskListener$State */
    private static class State {
        SurfaceControl mLeash;
        ActivityManager.RunningTaskInfo mTaskInfo;

        private State() {
        }
    }

    public FreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        this.mSyncQueue = syncTransactionQueue;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Freeform Task Appeared: #%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
            State state = new State();
            state.mTaskInfo = runningTaskInfo;
            state.mLeash = surfaceControl;
            this.mTasks.put(runningTaskInfo.taskId, state);
            this.mSyncQueue.runInSync(new FreeformTaskListener$$ExternalSyntheticLambda1(runningTaskInfo, surfaceControl, runningTaskInfo.configuration.windowConfiguration.getBounds()));
            return;
        }
        throw new RuntimeException("Task appeared more than once: #" + runningTaskInfo.taskId);
    }

    static /* synthetic */ void lambda$onTaskAppeared$0(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        Point point = runningTaskInfo.positionInParent;
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y).setWindowCrop(surfaceControl, rect.width(), rect.height()).show(surfaceControl);
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            Slog.e(TAG, "Task already vanished: #" + runningTaskInfo.taskId);
            return;
        }
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Freeform Task Vanished: #%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
        this.mTasks.remove(runningTaskInfo.taskId);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        State state = this.mTasks.get(runningTaskInfo.taskId);
        if (state != null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Freeform Task Info Changed: #%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
            state.mTaskInfo = runningTaskInfo;
            Rect bounds = runningTaskInfo.configuration.windowConfiguration.getBounds();
            this.mSyncQueue.runInSync(new FreeformTaskListener$$ExternalSyntheticLambda0(runningTaskInfo, state.mLeash, bounds));
            return;
        }
        throw new RuntimeException("Task info changed before appearing: #" + runningTaskInfo.taskId);
    }

    static /* synthetic */ void lambda$onTaskInfoChanged$1(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        Point point = runningTaskInfo.positionInParent;
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y).setWindowCrop(surfaceControl, rect.width(), rect.height()).show(surfaceControl);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        if (this.mTasks.contains(i)) {
            return this.mTasks.get(i).mLeash;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
        printWriter.println((str + "  ") + this.mTasks.size() + " tasks");
    }

    public static boolean isFreeformEnabled(Context context) {
        return context.getPackageManager().hasSystemFeature("android.software.freeform_window_management") || Settings.Global.getInt(context.getContentResolver(), "enable_freeform_support", 0) != 0;
    }
}
