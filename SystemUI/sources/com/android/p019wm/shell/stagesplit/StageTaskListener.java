package com.android.p019wm.shell.stagesplit;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SurfaceUtils;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.stagesplit.SplitScreen;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.stagesplit.StageTaskListener */
class StageTaskListener implements ShellTaskOrganizer.TaskListener {
    protected static final int[] CONTROLLED_ACTIVITY_TYPES = {1};
    protected static final int[] CONTROLLED_WINDOWING_MODES = {1, 0};
    protected static final int[] CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE = {1, 0, 6};
    private static final String TAG = "StageTaskListener";
    private final StageListenerCallbacks mCallbacks;
    private final SparseArray<SurfaceControl> mChildrenLeashes = new SparseArray<>();
    protected SparseArray<ActivityManager.RunningTaskInfo> mChildrenTaskInfo = new SparseArray<>();
    protected SurfaceControl mDimLayer;
    protected SurfaceControl mRootLeash;
    protected ActivityManager.RunningTaskInfo mRootTaskInfo;
    private final StageTaskUnfoldController mStageTaskUnfoldController;
    private final SurfaceSession mSurfaceSession;
    protected final SyncTransactionQueue mSyncQueue;

    /* renamed from: com.android.wm.shell.stagesplit.StageTaskListener$StageListenerCallbacks */
    public interface StageListenerCallbacks {
        void onChildTaskStatusChanged(int i, boolean z, boolean z2);

        void onNoLongerSupportMultiWindow();

        void onRootTaskAppeared();

        void onRootTaskVanished();

        void onStatusChanged(boolean z, boolean z2);
    }

    StageTaskListener(ShellTaskOrganizer shellTaskOrganizer, int i, StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, StageTaskUnfoldController stageTaskUnfoldController) {
        this.mCallbacks = stageListenerCallbacks;
        this.mSyncQueue = syncTransactionQueue;
        this.mSurfaceSession = surfaceSession;
        this.mStageTaskUnfoldController = stageTaskUnfoldController;
        shellTaskOrganizer.createRootTask(i, 6, this);
    }

    /* access modifiers changed from: package-private */
    public int getChildCount() {
        return this.mChildrenTaskInfo.size();
    }

    /* access modifiers changed from: package-private */
    public boolean containsTask(int i) {
        return this.mChildrenTaskInfo.contains(i);
    }

    /* access modifiers changed from: package-private */
    public int getTopChildTaskUid() {
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            ActivityManager.RunningTaskInfo valueAt = this.mChildrenTaskInfo.valueAt(size);
            if (valueAt.topActivityInfo != null) {
                return valueAt.topActivityInfo.applicationInfo.uid;
            }
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isFocused() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
        if (runningTaskInfo == null) {
            return false;
        }
        if (runningTaskInfo.isFocused) {
            return true;
        }
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            if (this.mChildrenTaskInfo.valueAt(size).isFocused) {
                return true;
            }
        }
        return false;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mRootTaskInfo == null && !runningTaskInfo.hasParentTask()) {
            this.mRootLeash = surfaceControl;
            this.mRootTaskInfo = runningTaskInfo;
            this.mCallbacks.onRootTaskAppeared();
            sendStatusChanged();
            this.mSyncQueue.runInSync(new StageTaskListener$$ExternalSyntheticLambda2(this));
        } else if (runningTaskInfo.parentTaskId == this.mRootTaskInfo.taskId) {
            int i = runningTaskInfo.taskId;
            this.mChildrenLeashes.put(i, surfaceControl);
            this.mChildrenTaskInfo.put(i, runningTaskInfo);
            updateChildTaskSurface(runningTaskInfo, surfaceControl, true);
            this.mCallbacks.onChildTaskStatusChanged(i, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            } else {
                return;
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mStageTaskUnfoldController;
        if (stageTaskUnfoldController != null) {
            stageTaskUnfoldController.onTaskAppeared(runningTaskInfo, surfaceControl);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskAppeared$0$com-android-wm-shell-stagesplit-StageTaskListener */
    public /* synthetic */ void mo51075x3f2e804d(SurfaceControl.Transaction transaction) {
        transaction.hide(this.mRootLeash);
        this.mDimLayer = SurfaceUtils.makeDimLayer(transaction, this.mRootLeash, "Dim layer", this.mSurfaceSession);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            this.mCallbacks.onNoLongerSupportMultiWindow();
            return;
        }
        if (this.mRootTaskInfo.taskId == runningTaskInfo.taskId) {
            this.mRootTaskInfo = runningTaskInfo;
        } else if (runningTaskInfo.parentTaskId == this.mRootTaskInfo.taskId) {
            this.mChildrenTaskInfo.put(runningTaskInfo.taskId, runningTaskInfo);
            this.mCallbacks.onChildTaskStatusChanged(runningTaskInfo.taskId, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                updateChildTaskSurface(runningTaskInfo, this.mChildrenLeashes.get(runningTaskInfo.taskId), false);
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            sendStatusChanged();
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int i = runningTaskInfo.taskId;
        if (this.mRootTaskInfo.taskId == i) {
            this.mCallbacks.onRootTaskVanished();
            this.mSyncQueue.runInSync(new StageTaskListener$$ExternalSyntheticLambda0(this));
            this.mRootTaskInfo = null;
        } else if (this.mChildrenTaskInfo.contains(i)) {
            this.mChildrenTaskInfo.remove(i);
            this.mChildrenLeashes.remove(i);
            this.mCallbacks.onChildTaskStatusChanged(i, false, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            } else {
                return;
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mStageTaskUnfoldController;
        if (stageTaskUnfoldController != null) {
            stageTaskUnfoldController.onTaskVanished(runningTaskInfo);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskVanished$1$com-android-wm-shell-stagesplit-StageTaskListener */
    public /* synthetic */ void mo51076xfb5ec274(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        if (this.mRootTaskInfo.taskId == i) {
            return this.mRootLeash;
        }
        if (this.mChildrenLeashes.contains(i)) {
            return this.mChildrenLeashes.get(i);
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    /* access modifiers changed from: package-private */
    public void setBounds(Rect rect, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.setBounds(this.mRootTaskInfo.token, rect);
    }

    /* access modifiers changed from: package-private */
    public void reorderChild(int i, boolean z, WindowContainerTransaction windowContainerTransaction) {
        if (containsTask(i)) {
            windowContainerTransaction.reorder(this.mChildrenTaskInfo.get(i).token, z);
        }
    }

    /* access modifiers changed from: package-private */
    public void setVisibility(boolean z, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, z);
    }

    /* access modifiers changed from: package-private */
    public void onSplitScreenListenerRegistered(SplitScreen.SplitScreenListener splitScreenListener, int i) {
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            int keyAt = this.mChildrenTaskInfo.keyAt(size);
            splitScreenListener.onTaskStageChanged(keyAt, i, this.mChildrenTaskInfo.get(keyAt).isVisible);
        }
    }

    private void updateChildTaskSurface(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, boolean z) {
        this.mSyncQueue.runInSync(new StageTaskListener$$ExternalSyntheticLambda1(surfaceControl, runningTaskInfo.positionInParent, z));
    }

    static /* synthetic */ void lambda$updateChildTaskSurface$2(SurfaceControl surfaceControl, Point point, boolean z, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, (Rect) null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        if (z && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            transaction.setAlpha(surfaceControl, 1.0f);
            transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
            transaction.show(surfaceControl);
        }
    }

    private void sendStatusChanged() {
        this.mCallbacks.onStatusChanged(this.mRootTaskInfo.isVisible, this.mChildrenTaskInfo.size() > 0);
    }

    public void dump(PrintWriter printWriter, String str) {
        (str + "  ") + "  ";
        printWriter.println(str + this);
    }
}
