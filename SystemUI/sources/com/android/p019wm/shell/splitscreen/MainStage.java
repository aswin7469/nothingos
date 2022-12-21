package com.android.p019wm.shell.splitscreen;

import android.content.Context;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.splitscreen.StageTaskListener;

/* renamed from: com.android.wm.shell.splitscreen.MainStage */
class MainStage extends StageTaskListener {
    private static final String TAG = "MainStage";
    private boolean mIsActive = false;

    MainStage(Context context, ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }

    /* access modifiers changed from: package-private */
    public boolean isActive() {
        return this.mIsActive;
    }

    /* access modifiers changed from: package-private */
    public void activate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (!this.mIsActive) {
            WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
            if (z) {
                windowContainerTransaction.reparentTasks((WindowContainerToken) null, windowContainerToken, CONTROLLED_WINDOWING_MODES, CONTROLLED_ACTIVITY_TYPES, true, true);
            }
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
                windowContainerTransaction.reparentTasks(this.mRootTaskInfo.token, (WindowContainerToken) null, CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, CONTROLLED_ACTIVITY_TYPES, z);
            }
        }
    }
}
