package com.android.p019wm.shell.fullscreen;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.SurfaceControl;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.Optional;

/* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener */
public class FullscreenTaskListener implements ShellTaskOrganizer.TaskListener {
    private static final String TAG = "FullscreenTaskListener";
    private final AnimatableTasksListener mAnimatableTasksListener;
    /* access modifiers changed from: private */
    public final SparseArray<TaskData> mDataByTaskId;
    /* access modifiers changed from: private */
    public final FullscreenUnfoldController mFullscreenUnfoldController;
    private final Optional<RecentTasksController> mRecentTasksOptional;
    private final SyncTransactionQueue mSyncQueue;

    public FullscreenTaskListener(SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional) {
        this(syncTransactionQueue, optional, Optional.empty());
    }

    public FullscreenTaskListener(SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional, Optional<RecentTasksController> optional2) {
        this.mDataByTaskId = new SparseArray<>();
        this.mAnimatableTasksListener = new AnimatableTasksListener();
        this.mSyncQueue = syncTransactionQueue;
        this.mFullscreenUnfoldController = optional.orElse(null);
        this.mRecentTasksOptional = optional2;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Fullscreen Task Appeared: #%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
            Point point = runningTaskInfo.positionInParent;
            this.mDataByTaskId.put(runningTaskInfo.taskId, new TaskData(surfaceControl, point));
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mSyncQueue.runInSync(new FullscreenTaskListener$$ExternalSyntheticLambda0(surfaceControl, point));
                this.mAnimatableTasksListener.onTaskAppeared(runningTaskInfo);
                updateRecentsForVisibleFullscreenTask(runningTaskInfo);
                return;
            }
            return;
        }
        throw new IllegalStateException("Task appeared more than once: #" + runningTaskInfo.taskId);
    }

    static /* synthetic */ void lambda$onTaskAppeared$0(SurfaceControl surfaceControl, Point point, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, (Rect) null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        transaction.setAlpha(surfaceControl, 1.0f);
        transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
        transaction.show(surfaceControl);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mAnimatableTasksListener.onTaskInfoChanged(runningTaskInfo);
            updateRecentsForVisibleFullscreenTask(runningTaskInfo);
            TaskData taskData = this.mDataByTaskId.get(runningTaskInfo.taskId);
            Point point = runningTaskInfo.positionInParent;
            if (!point.equals(taskData.positionInParent)) {
                taskData.positionInParent.set(point.x, point.y);
                this.mSyncQueue.runInSync(new FullscreenTaskListener$$ExternalSyntheticLambda2(taskData, point));
            }
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            Slog.e(TAG, "Task already vanished: #" + runningTaskInfo.taskId);
            return;
        }
        this.mAnimatableTasksListener.onTaskVanished(runningTaskInfo);
        this.mDataByTaskId.remove(runningTaskInfo.taskId);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Fullscreen Task Vanished: #%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
    }

    private void updateRecentsForVisibleFullscreenTask(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mRecentTasksOptional.ifPresent(new FullscreenTaskListener$$ExternalSyntheticLambda1(runningTaskInfo));
    }

    static /* synthetic */ void lambda$updateRecentsForVisibleFullscreenTask$2(ActivityManager.RunningTaskInfo runningTaskInfo, RecentTasksController recentTasksController) {
        if (runningTaskInfo.isVisible) {
            recentTasksController.removeSplitPair(runningTaskInfo.taskId);
        }
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        if (this.mDataByTaskId.contains(i)) {
            return this.mDataByTaskId.get(i).surface;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
        printWriter.println((str + "  ") + this.mDataByTaskId.size() + " Tasks");
    }

    public String toString() {
        return "FullscreenTaskListener:" + ShellTaskOrganizer.taskListenerTypeToString(-2);
    }

    /* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener$TaskData */
    private static class TaskData {
        public final Point positionInParent;
        public final SurfaceControl surface;

        public TaskData(SurfaceControl surfaceControl, Point point) {
            this.surface = surfaceControl;
            this.positionInParent = point;
        }
    }

    /* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener$AnimatableTasksListener */
    class AnimatableTasksListener {
        private final SparseBooleanArray mTaskIds = new SparseBooleanArray();

        AnimatableTasksListener() {
        }

        public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo) {
            if (isAnimatable(runningTaskInfo)) {
                this.mTaskIds.put(runningTaskInfo.taskId, true);
                if (FullscreenTaskListener.this.mFullscreenUnfoldController != null) {
                    FullscreenTaskListener.this.mFullscreenUnfoldController.onTaskAppeared(runningTaskInfo, ((TaskData) FullscreenTaskListener.this.mDataByTaskId.get(runningTaskInfo.taskId)).surface);
                }
            }
        }

        public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
            boolean z = this.mTaskIds.get(runningTaskInfo.taskId);
            boolean isAnimatable = isAnimatable(runningTaskInfo);
            if (z) {
                if (!isAnimatable) {
                    if (FullscreenTaskListener.this.mFullscreenUnfoldController != null) {
                        FullscreenTaskListener.this.mFullscreenUnfoldController.onTaskVanished(runningTaskInfo);
                    }
                    this.mTaskIds.put(runningTaskInfo.taskId, false);
                } else if (FullscreenTaskListener.this.mFullscreenUnfoldController != null) {
                    FullscreenTaskListener.this.mFullscreenUnfoldController.onTaskInfoChanged(runningTaskInfo);
                }
            } else if (isAnimatable) {
                this.mTaskIds.put(runningTaskInfo.taskId, true);
                if (FullscreenTaskListener.this.mFullscreenUnfoldController != null) {
                    FullscreenTaskListener.this.mFullscreenUnfoldController.onTaskAppeared(runningTaskInfo, ((TaskData) FullscreenTaskListener.this.mDataByTaskId.get(runningTaskInfo.taskId)).surface);
                }
            }
        }

        public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
            if (this.mTaskIds.get(runningTaskInfo.taskId) && FullscreenTaskListener.this.mFullscreenUnfoldController != null) {
                FullscreenTaskListener.this.mFullscreenUnfoldController.onTaskVanished(runningTaskInfo);
            }
            this.mTaskIds.put(runningTaskInfo.taskId, false);
        }

        private boolean isAnimatable(TaskInfo taskInfo) {
            return (taskInfo == null || !taskInfo.isVisible || taskInfo.getConfiguration().windowConfiguration.getActivityType() == 2) ? false : true;
        }
    }
}
