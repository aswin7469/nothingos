package com.android.p019wm.shell.stagesplit;

import android.graphics.Rect;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.stagesplit.StageTaskListener;

/* renamed from: com.android.wm.shell.stagesplit.MainStage */
class MainStage extends StageTaskListener {
    private static final String TAG = "MainStage";
    private boolean mIsActive = false;

    MainStage(ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, StageTaskUnfoldController stageTaskUnfoldController) {
        super(shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, stageTaskUnfoldController);
    }

    /* access modifiers changed from: package-private */
    public boolean isActive() {
        return this.mIsActive;
    }

    /* access modifiers changed from: package-private */
    public void activate(Rect rect, WindowContainerTransaction windowContainerTransaction) {
        if (!this.mIsActive) {
            WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
            windowContainerTransaction.setBounds(windowContainerToken, rect).setWindowingMode(windowContainerToken, 6).setLaunchRoot(windowContainerToken, CONTROLLED_WINDOWING_MODES, CONTROLLED_ACTIVITY_TYPES).reparentTasks((WindowContainerToken) null, windowContainerToken, CONTROLLED_WINDOWING_MODES, CONTROLLED_ACTIVITY_TYPES, true).reorder(windowContainerToken, true);
            this.mIsActive = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void deactivate(WindowContainerTransaction windowContainerTransaction) {
        deactivate(windowContainerTransaction, false);
    }

    /* access modifiers changed from: package-private */
    public void deactivate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (this.mIsActive) {
            this.mIsActive = false;
            if (this.mRootTaskInfo != null) {
                WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
                windowContainerTransaction.setLaunchRoot(windowContainerToken, (int[]) null, (int[]) null).reparentTasks(windowContainerToken, (WindowContainerToken) null, CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, CONTROLLED_ACTIVITY_TYPES, z).reorder(windowContainerToken, false);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateConfiguration(int i, Rect rect, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.setBounds(this.mRootTaskInfo.token, rect).setWindowingMode(this.mRootTaskInfo.token, i);
    }
}
