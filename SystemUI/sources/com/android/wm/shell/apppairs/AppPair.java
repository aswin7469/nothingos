package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.SurfaceUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.common.split.SplitWindowManager;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AppPair implements ShellTaskOrganizer.TaskListener, SplitLayout.SplitLayoutHandler {
    private static final String TAG = "AppPair";
    private final AppPairsController mController;
    private SurfaceControl mDimLayer1;
    private SurfaceControl mDimLayer2;
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private ActivityManager.RunningTaskInfo mRootTaskInfo;
    private SurfaceControl mRootTaskLeash;
    private SplitLayout mSplitLayout;
    private final SurfaceSession mSurfaceSession = new SurfaceSession();
    private final SyncTransactionQueue mSyncQueue;
    private ActivityManager.RunningTaskInfo mTaskInfo1;
    private ActivityManager.RunningTaskInfo mTaskInfo2;
    private SurfaceControl mTaskLeash1;
    private SurfaceControl mTaskLeash2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppPair(AppPairsController appPairsController) {
        this.mController = appPairsController;
        this.mSyncQueue = appPairsController.getSyncTransactionQueue();
        this.mDisplayController = appPairsController.getDisplayController();
        this.mDisplayImeController = appPairsController.getDisplayImeController();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean contains(int i) {
        return i == getRootTaskId() || i == getTaskId1() || i == getTaskId2();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -742394458, 5, null, Long.valueOf(runningTaskInfo.taskId), Long.valueOf(runningTaskInfo2.taskId), String.valueOf(this));
        }
        boolean z = runningTaskInfo.supportsMultiWindow;
        if (!z || !runningTaskInfo2.supportsMultiWindow) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -553798917, 15, null, Boolean.valueOf(z), Boolean.valueOf(runningTaskInfo2.supportsMultiWindow));
            }
            return false;
        }
        this.mTaskInfo1 = runningTaskInfo;
        this.mTaskInfo2 = runningTaskInfo2;
        this.mSplitLayout = new SplitLayout(TAG + "SplitDivider", this.mDisplayController.getDisplayContext(this.mRootTaskInfo.displayId), this.mRootTaskInfo.configuration, this, new SplitWindowManager.ParentContainerCallbacks() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda8
            @Override // com.android.wm.shell.common.split.SplitWindowManager.ParentContainerCallbacks
            public final void attachToParentSurface(SurfaceControl.Builder builder) {
                AppPair.this.lambda$pair$0(builder);
            }
        }, this.mDisplayImeController, this.mController.getTaskOrganizer());
        WindowContainerToken windowContainerToken = runningTaskInfo.token;
        WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mRootTaskInfo.token, false).reparent(windowContainerToken, this.mRootTaskInfo.token, true).reparent(windowContainerToken2, this.mRootTaskInfo.token, true).setWindowingMode(windowContainerToken, 6).setWindowingMode(windowContainerToken2, 6).setBounds(windowContainerToken, this.mSplitLayout.getBounds1()).setBounds(windowContainerToken2, this.mSplitLayout.getBounds2()).reorder(this.mRootTaskInfo.token, true);
        this.mController.getTaskOrganizer().applyTransaction(windowContainerTransaction);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pair$0(SurfaceControl.Builder builder) {
        builder.setParent(this.mRootTaskLeash);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unpair() {
        unpair(null);
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

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        int i;
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mRootTaskInfo;
        if (runningTaskInfo2 == null || (i = runningTaskInfo.taskId) == runningTaskInfo2.taskId) {
            this.mRootTaskInfo = runningTaskInfo;
            this.mRootTaskLeash = surfaceControl;
        } else if (i == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
            this.mTaskLeash1 = surfaceControl;
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda3
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    AppPair.this.lambda$onTaskAppeared$1(transaction);
                }
            });
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
            this.mTaskLeash2 = surfaceControl;
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda0
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    AppPair.this.lambda$onTaskAppeared$2(transaction);
                }
            });
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
        if (this.mTaskLeash1 == null || this.mTaskLeash2 == null) {
            return;
        }
        this.mSplitLayout.init();
        final SurfaceControl dividerLeash = this.mSplitLayout.getDividerLeash();
        final Rect dividerBounds = this.mSplitLayout.getDividerBounds();
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda5
            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                AppPair.this.lambda$onTaskAppeared$3(dividerLeash, dividerBounds, transaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$1(SurfaceControl.Transaction transaction) {
        this.mDimLayer1 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash1, "Dim layer", this.mSurfaceSession);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$2(SurfaceControl.Transaction transaction) {
        this.mDimLayer2 = SurfaceUtils.makeDimLayer(transaction, this.mTaskLeash2, "Dim layer", this.mSurfaceSession);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$3(SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        SurfaceControl.Transaction layer = transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
        SurfaceControl surfaceControl2 = this.mTaskLeash1;
        Point point = this.mTaskInfo1.positionInParent;
        SurfaceControl.Transaction position = layer.setPosition(surfaceControl2, point.x, point.y);
        SurfaceControl surfaceControl3 = this.mTaskLeash2;
        Point point2 = this.mTaskInfo2.positionInParent;
        position.setPosition(surfaceControl3, point2.x, point2.y).setPosition(surfaceControl, rect.left, rect.top).show(this.mRootTaskLeash).show(this.mTaskLeash1).show(this.mTaskLeash2);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(final ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
        } else if (runningTaskInfo.taskId == getRootTaskId()) {
            if (this.mRootTaskInfo.isVisible != runningTaskInfo.isVisible) {
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda4
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        AppPair.this.lambda$onTaskInfoChanged$4(runningTaskInfo, transaction);
                    }
                });
            }
            this.mRootTaskInfo = runningTaskInfo;
            SplitLayout splitLayout = this.mSplitLayout;
            if (splitLayout == null || !splitLayout.updateConfiguration(runningTaskInfo.configuration)) {
                return;
            }
            onBoundsChanged(this.mSplitLayout);
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mTaskInfo1 = runningTaskInfo;
        } else if (runningTaskInfo.taskId == getTaskId2()) {
            this.mTaskInfo2 = runningTaskInfo;
        } else {
            throw new IllegalStateException("Unknown task=" + runningTaskInfo.taskId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskInfoChanged$4(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl.Transaction transaction) {
        if (runningTaskInfo.isVisible) {
            transaction.show(this.mRootTaskLeash);
        } else {
            transaction.hide(this.mRootTaskLeash);
        }
    }

    @Override // com.android.wm.shell.common.split.SplitLayout.SplitLayoutHandler
    public int getSplitItemPosition(WindowContainerToken windowContainerToken) {
        if (windowContainerToken == null) {
            return -1;
        }
        if (windowContainerToken.equals(this.mTaskInfo1.getToken())) {
            return 0;
        }
        return windowContainerToken.equals(this.mTaskInfo2.getToken()) ? 1 : -1;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.taskId == getRootTaskId()) {
            this.mController.unpair(this.mRootTaskInfo.taskId, false);
        } else if (runningTaskInfo.taskId == getTaskId1()) {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda2
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    AppPair.this.lambda$onTaskVanished$5(transaction);
                }
            });
        } else if (runningTaskInfo.taskId != getTaskId2()) {
        } else {
            this.mController.unpair(this.mRootTaskInfo.taskId);
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda1
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    AppPair.this.lambda$onTaskVanished$6(transaction);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$5(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$6(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer2);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (getRootTaskId() == i) {
            builder.setParent(this.mRootTaskLeash);
        } else if (getTaskId1() == i) {
            builder.setParent(this.mTaskLeash1);
        } else if (getTaskId2() == i) {
            builder.setParent(this.mTaskLeash2);
        } else {
            throw new IllegalArgumentException("There is no surface for taskId=" + i);
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + this);
        printWriter.println(str2 + "Root taskId=" + getRootTaskId() + " winMode=" + this.mRootTaskInfo.getWindowingMode());
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

    @Override // com.android.wm.shell.common.split.SplitLayout.SplitLayoutHandler
    public void onSnappedToDismiss(boolean z) {
        unpair((z ? this.mTaskInfo1 : this.mTaskInfo2).token);
    }

    @Override // com.android.wm.shell.common.split.SplitLayout.SplitLayoutHandler
    public void onBoundsChanging(final SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda7
            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                AppPair.this.lambda$onBoundsChanging$7(splitLayout, transaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBoundsChanging$7(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2);
    }

    @Override // com.android.wm.shell.common.split.SplitLayout.SplitLayoutHandler
    public void onBoundsChanged(final SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyTaskChanges(windowContainerTransaction, this.mTaskInfo1, this.mTaskInfo2);
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda6
            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                AppPair.this.lambda$onBoundsChanged$8(splitLayout, transaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBoundsChanged$8(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        splitLayout.applySurfaceChanges(transaction, this.mTaskLeash1, this.mTaskLeash2, this.mDimLayer1, this.mDimLayer2);
    }
}
