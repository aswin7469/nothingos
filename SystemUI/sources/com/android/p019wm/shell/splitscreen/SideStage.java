package com.android.p019wm.shell.splitscreen;

import android.app.ActivityManager;
import android.content.Context;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.splitscreen.StageTaskListener;

/* renamed from: com.android.wm.shell.splitscreen.SideStage */
class SideStage extends StageTaskListener {
    private static final String TAG = "SideStage";

    SideStage(Context context, ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }

    /* access modifiers changed from: package-private */
    public boolean removeAllTasks(WindowContainerTransaction windowContainerTransaction, boolean z) {
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
}
