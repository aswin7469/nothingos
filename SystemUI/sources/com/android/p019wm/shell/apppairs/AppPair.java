package com.android.p019wm.shell.apppairs;

import android.app.ActivityManager;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.SurfaceUtils;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.split.SplitLayout;
import com.android.p019wm.shell.common.split.SplitWindowManager;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.apppairs.AppPair */
class AppPair implements ShellTaskOrganizer.TaskListener, SplitLayout.SplitLayoutHandler {
    private static final String TAG = "AppPair";
    private final AppPairsController mController;
    private SurfaceControl mDimLayer1;
    private SurfaceControl mDimLayer2;
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final SplitWindowManager.ParentContainerCallbacks mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
        public void attachToParentSurface(SurfaceControl.Builder builder) {
            builder.setParent(AppPair.this.mRootTaskLeash);
        }

        public void onLeashReady(SurfaceControl surfaceControl) {
            AppPair.this.mSyncQueue.runInSync(new AppPair$1$$ExternalSyntheticLambda0(this, surfaceControl));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onLeashReady$0$com-android-wm-shell-apppairs-AppPair$1  reason: not valid java name */
        public /* synthetic */ void m3410lambda$onLeashReady$0$comandroidwmshellapppairsAppPair$1(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
            transaction.show(surfaceControl).setLayer(surfaceControl, Integer.MAX_VALUE).setPosition(surfaceControl, (float) AppPair.this.mSplitLayout.getDividerBounds().left, (float) AppPair.this.mSplitLayout.getDividerBounds().top);
        }
    };
    private ActivityManager.RunningTaskInfo mRootTaskInfo;
    /* access modifiers changed from: private */
    public SurfaceControl mRootTaskLeash;
    /* access modifiers changed from: private */
    public SplitLayout mSplitLayout;
    private final SurfaceSession mSurfaceSession = new SurfaceSession();
    /* access modifiers changed from: private */
    public final SyncTransactionQueue mSyncQueue;
    private ActivityManager.RunningTaskInfo mTaskInfo1;
    private ActivityManager.RunningTaskInfo mTaskInfo2;
    private SurfaceControl mTaskLeash1;
    private SurfaceControl mTaskLeash2;

    AppPair(AppPairsController appPairsController) {
        this.mController = appPairsController;
        this.mSyncQueue = appPairsController.getSyncTransactionQueue();
        this.mDisplayController = appPairsController.getDisplayController();
        this.mDisplayImeController = appPairsController.getDisplayImeController();
        this.mDisplayInsetsController = appPairsController.getDisplayInsetsController();
    }

    /* access modifiers changed from: package-private */
    public int getRootTaskId() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    private int getTaskId1() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo1;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    private int getTaskId2() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo2;
        if (runningTaskInfo != null) {
            return runningTaskInfo.taskId;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean contains(int i) {
        return i == getRootTaskId() || i == getTaskId1() || i == getTaskId2();
    }

    /* access modifiers changed from: package-private */
    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        ActivityManager.RunningTaskInfo runningTaskInfo3 = runningTaskInfo;
        ActivityManager.RunningTaskInfo runningTaskInfo4 = runningTaskInfo2;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "pair task1=%d task2=%d in AppPair=%s", new Object[]{Integer.valueOf(runningTaskInfo3.taskId), Integer.valueOf(runningTaskInfo4.taskId), this});
        if (!runningTaskInfo3.supportsMultiWindow || !runningTaskInfo4.supportsMultiWindow) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Can't pair tasks that doesn't support multi window, task1.supportsMultiWindow=%b, task2.supportsMultiWindow=%b", new Object[]{Boolean.valueOf(runningTaskInfo3.supportsMultiWindow), Boolean.valueOf(runningTaskInfo4.supportsMultiWindow)});
            return false;
        }
        this.mTaskInfo1 = runningTaskInfo3;
        this.mTaskInfo2 = runningTaskInfo4;
        this.mSplitLayout = new SplitLayout(TAG + "SplitDivider", this.mDisplayController.getDisplayContext(this.mRootTaskInfo.displayId), this.mRootTaskInfo.configuration, this, this.mParentContainerCallbacks, this.mDisplayImeController, this.mController.getTaskOrganizer(), 1);
        this.mDisplayInsetsController.addInsetsChangedListener(this.mRootTaskInfo.displayId, this.mSplitLayout);
        WindowContainerToken windowContainerToken = runningTaskInfo3.token;
        WindowContainerToken windowContainerToken2 = runningTaskInfo4.token;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mRootTaskInfo.token, false).reparent(windowContainerToken, this.mRootTaskInfo.token, true).reparent(windowContainerToken2, this.mRootTaskInfo.token, true).setWindowingMode(windowContainerToken, 6).setWindowingMode(windowContainerToken2, 6).setBounds(windowContainerToken, this.mSplitLayout.getBounds1()).setBounds(windowContainerToken2, this.mSplitLayout.getBounds2()).reorder(this.mRootTaskInfo.token, true);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void unpair() {
        unpair((WindowContainerToken) null);
    }

    private void unpair(WindowContainerToken windowContainerToken) {
        WindowContainerToken windowContainerToken2 = this.mTaskInfo1.token;
        WindowContainerToken windowContainerToken3 = this.mTaskInfo2.token;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        boolean z = true;
        WindowContainerTransaction reparent = windowContainerTransaction.setHidden(this.mRootTaskInfo.token, true).reorder(this.mRootTaskInfo.token, false).reparent(windowContainerToken2, (WindowContainerToken) null, windowContainerToken2 == windowContainerToken);
        if (windowContainerToken3 != windowContainerToken) {
            z = false;
        }
        reparent.reparent(windowContainerToken3, (WindowContainerToken) null, z).setWindowingMode(windowContainerToken2, 0).setWindowingMode(windowContainerToken3, 0);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
        this.mTaskInfo1 = null;
        this.mTaskInfo2 = null;
        this.mSplitLayout.release();
        this.mSplitLayout = null;
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mRootTaskInfo == null || runningTaskInfo.taskId == this.mRootTaskInfo.taskId) {
            this.mRootTaskInfo = runningTaskInfo;
            this.mRootTaskLeash = surfaceControl;
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
            this.mTaskLeash1 = surfaceControl;
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda4(this));
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
            this.mTaskLeash2 = surfaceControl;
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda5(this));
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
        if (this.mTaskLeash1 != null && this.mTaskLeash2 != null) {
            this.mSplitLayout.init();
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda6(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskAppeared$0$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3404lambda$onTaskAppeared$0$comandroidwmshellapppairsAppPair(SurfaceControl.Transaction transaction) {
        this.mDimLayer1 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash1, "Dim layer", this.mSurfaceSession);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskAppeared$1$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3405lambda$onTaskAppeared$1$comandroidwmshellapppairsAppPair(SurfaceControl.Transaction transaction) {
        this.mDimLayer2 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash2, "Dim layer", this.mSurfaceSession);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskAppeared$2$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3406lambda$onTaskAppeared$2$comandroidwmshellapppairsAppPair(SurfaceControl.Transaction transaction) {
        transaction.show(this.mRootTaskLeash).show(this.mTaskLeash1).show(this.mTaskLeash2).setPosition(this.mTaskLeash1, (float) this.mTaskInfo1.positionInParent.x, (float) this.mTaskInfo1.positionInParent.y).setPosition(this.mTaskLeash2, (float) this.mTaskInfo2.positionInParent.x, (float) this.mTaskInfo2.positionInParent.y);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
        } else if (runningTaskInfo.taskId == getRootTaskId()) {
            if (this.mRootTaskInfo.isVisible != runningTaskInfo.isVisible) {
                this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda1(this, runningTaskInfo));
            }
            this.mRootTaskInfo = runningTaskInfo;
            SplitLayout splitLayout = this.mSplitLayout;
            if (splitLayout != null && splitLayout.updateConfiguration(runningTaskInfo.configuration)) {
                onLayoutSizeChanged(this.mSplitLayout);
            }
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskInfoChanged$3$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3407lambda$onTaskInfoChanged$3$comandroidwmshellapppairsAppPair(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl.Transaction transaction) {
        if (runningTaskInfo.isVisible) {
            transaction.show(this.mRootTaskLeash);
        } else {
            transaction.hide(this.mRootTaskLeash);
        }
    }

    public int getSplitItemPosition(WindowContainerToken windowContainerToken) {
        if (windowContainerToken == null) {
            return -1;
        }
        if (windowContainerToken.equals(this.mTaskInfo1.getToken())) {
            return 0;
        }
        if (windowContainerToken.equals(this.mTaskInfo2.getToken())) {
            return 1;
        }
        return -1;
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.taskId == getRootTaskId()) {
            this.mController.unpair(this.mRootTaskInfo.taskId, false);
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda7(this));
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda8(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskVanished$4$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3408lambda$onTaskVanished$4$comandroidwmshellapppairsAppPair(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer1);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTaskVanished$5$com-android-wm-shell-apppairs-AppPair  reason: not valid java name */
    public /* synthetic */ void m3409lambda$onTaskVanished$5$comandroidwmshellapppairsAppPair(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer2);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        if (getRootTaskId() == i) {
            return this.mRootTaskLeash;
        }
        if (getTaskId1() == i) {
            return this.mTaskLeash1;
        }
        if (getTaskId2() == i) {
            return this.mTaskLeash2;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        str2 + "  ";
        printWriter.println(str + this);
        if (this.mRootTaskInfo != null) {
            printWriter.println(str2 + "Root taskId=" + this.mRootTaskInfo.taskId + " winMode=" + this.mRootTaskInfo.getWindowingMode());
        }
        if (this.mTaskInfo1 != null) {
            printWriter.println(str2 + "1 taskId=" + this.mTaskInfo1.taskId + " winMode=" + this.mTaskInfo1.getWindowingMode());
        }
        if (this.mTaskInfo2 != null) {
            printWriter.println(str2 + "2 taskId=" + this.mTaskInfo2.taskId + " winMode=" + this.mTaskInfo2.getWindowingMode());
        }
    }

    public String toString() {
        return TAG + "#" + getRootTaskId();
    }

    public void onSnappedToDismiss(boolean z) {
        unpair((z ? this.mTaskInfo1 : this.mTaskInfo2).token);
    }

    public void onLayoutPositionChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda3(this, splitLayout));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutPositionChanging$6$com-android-wm-shell-apppairs-AppPair */
    public /* synthetic */ void mo48167xc90fc31a(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, true);
    }

    public void onLayoutSizeChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda0(this, splitLayout));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutSizeChanging$7$com-android-wm-shell-apppairs-AppPair */
    public /* synthetic */ void mo48169xaf230433(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, true);
    }

    public void onLayoutSizeChanged(SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyTaskChanges(windowContainerTransaction, this.mTaskInfo1, this.mTaskInfo2);
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda2(this, splitLayout));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutSizeChanged$8$com-android-wm-shell-apppairs-AppPair */
    public /* synthetic */ void mo48168x50fd130d(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2, false);
    }

    public void setLayoutOffsetTarget(int i, int i2, SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyLayoutOffsetTarget(windowContainerTransaction, i, i2, this.mTaskInfo1, this.mTaskInfo2);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
    }
}
