package com.android.p019wm.shell.stagesplit;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.stagesplit.StageTaskListener;

/* renamed from: com.android.wm.shell.stagesplit.SideStage */
class SideStage extends StageTaskListener implements DisplayInsetsController.OnInsetsChangedListener {
    private static final String TAG = "SideStage";
    private final Context mContext;
    private OutlineManager mOutlineManager;

    SideStage(Context context, ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, StageTaskUnfoldController stageTaskUnfoldController) {
        super(shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, stageTaskUnfoldController);
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public void addTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, WindowContainerTransaction windowContainerTransaction) {
        WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
        windowContainerTransaction.setBounds(windowContainerToken, rect).reparent(runningTaskInfo.token, windowContainerToken, true).reorder(windowContainerToken, true);
    }

    /* access modifiers changed from: package-private */
    public boolean removeAllTasks(WindowContainerTransaction windowContainerTransaction, boolean z) {
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, false);
        if (this.mChildrenTaskInfo.size() == 0) {
            return false;
        }
        windowContainerTransaction.reparentTasks(this.mRootTaskInfo.token, (WindowContainerToken) null, CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, CONTROLLED_ACTIVITY_TYPES, z);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean removeTask(int i, WindowContainerToken windowContainerToken, WindowContainerTransaction windowContainerTransaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) this.mChildrenTaskInfo.get(i);
        if (runningTaskInfo == null) {
            return false;
        }
        windowContainerTransaction.reparent(runningTaskInfo.token, windowContainerToken, false);
        return true;
    }

    public SurfaceControl getOutlineLeash() {
        return this.mOutlineManager.getOutlineLeash();
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        super.onTaskAppeared(runningTaskInfo, surfaceControl);
        if (isRootTask(runningTaskInfo)) {
            this.mOutlineManager = new OutlineManager(this.mContext, runningTaskInfo.configuration);
            enableOutline(true);
        }
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        super.onTaskInfoChanged(runningTaskInfo);
        if (isRootTask(runningTaskInfo)) {
            this.mOutlineManager.setRootBounds(runningTaskInfo.configuration.windowConfiguration.getBounds());
        }
    }

    private boolean isRootTask(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return this.mRootTaskInfo != null && this.mRootTaskInfo.taskId == runningTaskInfo.taskId;
    }

    /* access modifiers changed from: package-private */
    public void enableOutline(boolean z) {
        OutlineManager outlineManager = this.mOutlineManager;
        if (outlineManager != null) {
            if (!z) {
                outlineManager.release();
            } else if (this.mRootTaskInfo != null) {
                this.mOutlineManager.inflate(this.mRootLeash, this.mRootTaskInfo.configuration.windowConfiguration.getBounds());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setOutlineVisibility(boolean z) {
        this.mOutlineManager.setVisibility(z);
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mOutlineManager.onInsetsChanged(insetsState);
    }

    public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        insetsChanged(insetsState);
    }
}
