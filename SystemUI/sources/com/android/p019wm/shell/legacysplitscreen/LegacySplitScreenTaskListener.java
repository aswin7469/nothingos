package com.android.p019wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.TaskOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener */
class LegacySplitScreenTaskListener implements ShellTaskOrganizer.TaskListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "LegacySplitScreenTaskListener";
    Rect mHomeBounds = new Rect();
    private final SparseArray<SurfaceControl> mLeashByTaskId = new SparseArray<>();
    private final SparseArray<Point> mPositionByTaskId = new SparseArray<>();
    ActivityManager.RunningTaskInfo mPrimary;
    SurfaceControl mPrimaryDim;
    SurfaceControl mPrimarySurface;
    ActivityManager.RunningTaskInfo mSecondary;
    SurfaceControl mSecondaryDim;
    SurfaceControl mSecondarySurface;
    final LegacySplitScreenController mSplitScreenController;
    private boolean mSplitScreenSupported = false;
    private final LegacySplitScreenTransitions mSplitTransitions;
    final SurfaceSession mSurfaceSession = new SurfaceSession();
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;

    LegacySplitScreenTaskListener(LegacySplitScreenController legacySplitScreenController, ShellTaskOrganizer shellTaskOrganizer, Transitions transitions, SyncTransactionQueue syncTransactionQueue) {
        this.mSplitScreenController = legacySplitScreenController;
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTransitions legacySplitScreenTransitions = new LegacySplitScreenTransitions(legacySplitScreenController.mTransactionPool, transitions, legacySplitScreenController, this);
        this.mSplitTransitions = legacySplitScreenTransitions;
        transitions.addHandler(legacySplitScreenTransitions);
        this.mSyncQueue = syncTransactionQueue;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        synchronized (this) {
            try {
                this.mTaskOrganizer.createRootTask(0, 3, this);
                this.mTaskOrganizer.createRootTask(0, 4, this);
            } catch (Exception e) {
                this.mTaskOrganizer.removeListener(this);
                throw e;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isSplitScreenSupported() {
        return this.mSplitScreenSupported;
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl.Transaction getTransaction() {
        return this.mSplitScreenController.mTransactionPool.acquire();
    }

    /* access modifiers changed from: package-private */
    public void releaseTransaction(SurfaceControl.Transaction transaction) {
        this.mSplitScreenController.mTransactionPool.release(transaction);
    }

    /* access modifiers changed from: package-private */
    public TaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    /* access modifiers changed from: package-private */
    public LegacySplitScreenTransitions getSplitTransitions() {
        return this.mSplitTransitions;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00ab, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTaskAppeared(android.app.ActivityManager.RunningTaskInfo r8, android.view.SurfaceControl r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            boolean r0 = r8.hasParentTask()     // Catch:{ all -> 0x00ac }
            if (r0 == 0) goto L_0x000c
            r7.handleChildTaskAppeared(r8, r9)     // Catch:{ all -> 0x00ac }
            monitor-exit(r7)     // Catch:{ all -> 0x00ac }
            return
        L_0x000c:
            int r0 = r8.getWindowingMode()     // Catch:{ all -> 0x00ac }
            r1 = 3
            r2 = 2
            r3 = 0
            r4 = 1
            if (r0 != r1) goto L_0x0030
            com.android.wm.shell.protolog.ShellProtoLogGroup r0 = com.android.p019wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00ac }
            java.lang.String r1 = "%s onTaskAppeared Primary taskId=%d"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x00ac }
            java.lang.String r5 = TAG     // Catch:{ all -> 0x00ac }
            r2[r3] = r5     // Catch:{ all -> 0x00ac }
            int r5 = r8.taskId     // Catch:{ all -> 0x00ac }
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x00ac }
            r2[r4] = r5     // Catch:{ all -> 0x00ac }
            com.android.internal.protolog.common.ProtoLog.v(r0, r1, r2)     // Catch:{ all -> 0x00ac }
            r7.mPrimary = r8     // Catch:{ all -> 0x00ac }
            r7.mPrimarySurface = r9     // Catch:{ all -> 0x00ac }
            goto L_0x0068
        L_0x0030:
            r5 = 4
            if (r0 != r5) goto L_0x004d
            com.android.wm.shell.protolog.ShellProtoLogGroup r0 = com.android.p019wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00ac }
            java.lang.String r1 = "%s onTaskAppeared Secondary taskId=%d"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x00ac }
            java.lang.String r5 = TAG     // Catch:{ all -> 0x00ac }
            r2[r3] = r5     // Catch:{ all -> 0x00ac }
            int r5 = r8.taskId     // Catch:{ all -> 0x00ac }
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x00ac }
            r2[r4] = r5     // Catch:{ all -> 0x00ac }
            com.android.internal.protolog.common.ProtoLog.v(r0, r1, r2)     // Catch:{ all -> 0x00ac }
            r7.mSecondary = r8     // Catch:{ all -> 0x00ac }
            r7.mSecondarySurface = r9     // Catch:{ all -> 0x00ac }
            goto L_0x0068
        L_0x004d:
            com.android.wm.shell.protolog.ShellProtoLogGroup r9 = com.android.p019wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00ac }
            java.lang.String r5 = "%s onTaskAppeared unknown taskId=%d winMode=%d"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x00ac }
            java.lang.String r6 = TAG     // Catch:{ all -> 0x00ac }
            r1[r3] = r6     // Catch:{ all -> 0x00ac }
            int r8 = r8.taskId     // Catch:{ all -> 0x00ac }
            java.lang.Integer r8 = java.lang.Integer.valueOf((int) r8)     // Catch:{ all -> 0x00ac }
            r1[r4] = r8     // Catch:{ all -> 0x00ac }
            java.lang.Integer r8 = java.lang.Integer.valueOf((int) r0)     // Catch:{ all -> 0x00ac }
            r1[r2] = r8     // Catch:{ all -> 0x00ac }
            com.android.internal.protolog.common.ProtoLog.v(r9, r5, r1)     // Catch:{ all -> 0x00ac }
        L_0x0068:
            boolean r8 = r7.mSplitScreenSupported     // Catch:{ all -> 0x00ac }
            if (r8 != 0) goto L_0x00aa
            android.view.SurfaceControl r8 = r7.mPrimarySurface     // Catch:{ all -> 0x00ac }
            if (r8 == 0) goto L_0x00aa
            android.view.SurfaceControl r8 = r7.mSecondarySurface     // Catch:{ all -> 0x00ac }
            if (r8 == 0) goto L_0x00aa
            r7.mSplitScreenSupported = r4     // Catch:{ all -> 0x00ac }
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r8 = r7.mSplitScreenController     // Catch:{ all -> 0x00ac }
            r8.onSplitScreenSupported()     // Catch:{ all -> 0x00ac }
            com.android.wm.shell.protolog.ShellProtoLogGroup r8 = com.android.p019wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00ac }
            java.lang.String r9 = "%s onTaskAppeared Supported"
            java.lang.Object[] r0 = new java.lang.Object[r4]     // Catch:{ all -> 0x00ac }
            java.lang.String r1 = TAG     // Catch:{ all -> 0x00ac }
            r0[r3] = r1     // Catch:{ all -> 0x00ac }
            com.android.internal.protolog.common.ProtoLog.v(r8, r9, r0)     // Catch:{ all -> 0x00ac }
            android.view.SurfaceControl$Transaction r8 = r7.getTransaction()     // Catch:{ all -> 0x00ac }
            android.view.SurfaceControl r9 = r7.mPrimarySurface     // Catch:{ all -> 0x00ac }
            java.lang.String r0 = "Primary Divider Dim"
            android.view.SurfaceSession r1 = r7.mSurfaceSession     // Catch:{ all -> 0x00ac }
            android.view.SurfaceControl r9 = com.android.p019wm.shell.common.SurfaceUtils.makeDimLayer(r8, r9, r0, r1)     // Catch:{ all -> 0x00ac }
            r7.mPrimaryDim = r9     // Catch:{ all -> 0x00ac }
            android.view.SurfaceControl r9 = r7.mSecondarySurface     // Catch:{ all -> 0x00ac }
            java.lang.String r0 = "Secondary Divider Dim"
            android.view.SurfaceSession r1 = r7.mSurfaceSession     // Catch:{ all -> 0x00ac }
            android.view.SurfaceControl r9 = com.android.p019wm.shell.common.SurfaceUtils.makeDimLayer(r8, r9, r0, r1)     // Catch:{ all -> 0x00ac }
            r7.mSecondaryDim = r9     // Catch:{ all -> 0x00ac }
            r8.apply()     // Catch:{ all -> 0x00ac }
            r7.releaseTransaction(r8)     // Catch:{ all -> 0x00ac }
        L_0x00aa:
            monitor-exit(r7)     // Catch:{ all -> 0x00ac }
            return
        L_0x00ac:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00ac }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenTaskListener.onTaskAppeared(android.app.ActivityManager$RunningTaskInfo, android.view.SurfaceControl):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTaskVanished(android.app.ActivityManager.RunningTaskInfo r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            android.util.SparseArray<android.graphics.Point> r0 = r4.mPositionByTaskId     // Catch:{ all -> 0x006d }
            int r1 = r5.taskId     // Catch:{ all -> 0x006d }
            r0.remove(r1)     // Catch:{ all -> 0x006d }
            boolean r0 = r5.hasParentTask()     // Catch:{ all -> 0x006d }
            if (r0 == 0) goto L_0x0017
            android.util.SparseArray<android.view.SurfaceControl> r0 = r4.mLeashByTaskId     // Catch:{ all -> 0x006d }
            int r5 = r5.taskId     // Catch:{ all -> 0x006d }
            r0.remove(r5)     // Catch:{ all -> 0x006d }
            monitor-exit(r4)     // Catch:{ all -> 0x006d }
            return
        L_0x0017:
            android.app.ActivityManager$RunningTaskInfo r0 = r4.mPrimary     // Catch:{ all -> 0x006d }
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x002b
            android.window.WindowContainerToken r0 = r5.token     // Catch:{ all -> 0x006d }
            android.app.ActivityManager$RunningTaskInfo r3 = r4.mPrimary     // Catch:{ all -> 0x006d }
            android.window.WindowContainerToken r3 = r3.token     // Catch:{ all -> 0x006d }
            boolean r0 = r0.equals(r3)     // Catch:{ all -> 0x006d }
            if (r0 == 0) goto L_0x002b
            r0 = r1
            goto L_0x002c
        L_0x002b:
            r0 = r2
        L_0x002c:
            android.app.ActivityManager$RunningTaskInfo r3 = r4.mSecondary     // Catch:{ all -> 0x006d }
            if (r3 == 0) goto L_0x003d
            android.window.WindowContainerToken r5 = r5.token     // Catch:{ all -> 0x006d }
            android.app.ActivityManager$RunningTaskInfo r3 = r4.mSecondary     // Catch:{ all -> 0x006d }
            android.window.WindowContainerToken r3 = r3.token     // Catch:{ all -> 0x006d }
            boolean r5 = r5.equals(r3)     // Catch:{ all -> 0x006d }
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r1 = r2
        L_0x003e:
            boolean r5 = r4.mSplitScreenSupported     // Catch:{ all -> 0x006d }
            if (r5 == 0) goto L_0x006b
            if (r0 != 0) goto L_0x0046
            if (r1 == 0) goto L_0x006b
        L_0x0046:
            r4.mSplitScreenSupported = r2     // Catch:{ all -> 0x006d }
            android.view.SurfaceControl$Transaction r5 = r4.getTransaction()     // Catch:{ all -> 0x006d }
            android.view.SurfaceControl r0 = r4.mPrimaryDim     // Catch:{ all -> 0x006d }
            r5.remove(r0)     // Catch:{ all -> 0x006d }
            android.view.SurfaceControl r0 = r4.mSecondaryDim     // Catch:{ all -> 0x006d }
            r5.remove(r0)     // Catch:{ all -> 0x006d }
            android.view.SurfaceControl r0 = r4.mPrimarySurface     // Catch:{ all -> 0x006d }
            r5.remove(r0)     // Catch:{ all -> 0x006d }
            android.view.SurfaceControl r0 = r4.mSecondarySurface     // Catch:{ all -> 0x006d }
            r5.remove(r0)     // Catch:{ all -> 0x006d }
            r5.apply()     // Catch:{ all -> 0x006d }
            r4.releaseTransaction(r5)     // Catch:{ all -> 0x006d }
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r5 = r4.mSplitScreenController     // Catch:{ all -> 0x006d }
            r5.onTaskVanished()     // Catch:{ all -> 0x006d }
        L_0x006b:
            monitor-exit(r4)     // Catch:{ all -> 0x006d }
            return
        L_0x006d:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x006d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenTaskListener.onTaskVanished(android.app.ActivityManager$RunningTaskInfo):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0038, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo r4) {
        /*
            r3 = this;
            int r0 = r4.displayId
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            monitor-enter(r3)
            boolean r0 = r4.supportsMultiWindow     // Catch:{ all -> 0x0068 }
            if (r0 != 0) goto L_0x0039
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r0 = r3.mSplitScreenController     // Catch:{ all -> 0x0068 }
            boolean r0 = r0.isDividerVisible()     // Catch:{ all -> 0x0068 }
            if (r0 == 0) goto L_0x0037
            int r0 = r4.taskId     // Catch:{ all -> 0x0068 }
            android.app.ActivityManager$RunningTaskInfo r1 = r3.mPrimary     // Catch:{ all -> 0x0068 }
            int r1 = r1.taskId     // Catch:{ all -> 0x0068 }
            if (r0 == r1) goto L_0x0030
            int r0 = r4.parentTaskId     // Catch:{ all -> 0x0068 }
            android.app.ActivityManager$RunningTaskInfo r1 = r3.mPrimary     // Catch:{ all -> 0x0068 }
            int r1 = r1.taskId     // Catch:{ all -> 0x0068 }
            if (r0 != r1) goto L_0x0023
            goto L_0x0030
        L_0x0023:
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r0 = r3.mSplitScreenController     // Catch:{ all -> 0x0068 }
            boolean r4 = r4.isFocused     // Catch:{ all -> 0x0068 }
            if (r4 != 0) goto L_0x002b
            r4 = 1
            goto L_0x002c
        L_0x002b:
            r4 = 0
        L_0x002c:
            r0.startDismissSplit(r4)     // Catch:{ all -> 0x0068 }
            goto L_0x0037
        L_0x0030:
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r0 = r3.mSplitScreenController     // Catch:{ all -> 0x0068 }
            boolean r4 = r4.isFocused     // Catch:{ all -> 0x0068 }
            r0.startDismissSplit(r4)     // Catch:{ all -> 0x0068 }
        L_0x0037:
            monitor-exit(r3)     // Catch:{ all -> 0x0068 }
            return
        L_0x0039:
            boolean r0 = r4.hasParentTask()     // Catch:{ all -> 0x0068 }
            if (r0 == 0) goto L_0x0055
            android.graphics.Point r0 = r4.positionInParent     // Catch:{ all -> 0x0068 }
            android.util.SparseArray<android.graphics.Point> r1 = r3.mPositionByTaskId     // Catch:{ all -> 0x0068 }
            int r2 = r4.taskId     // Catch:{ all -> 0x0068 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0068 }
            boolean r0 = r0.equals(r1)     // Catch:{ all -> 0x0068 }
            if (r0 == 0) goto L_0x0051
            monitor-exit(r3)     // Catch:{ all -> 0x0068 }
            return
        L_0x0051:
            r3.handleChildTaskChanged(r4)     // Catch:{ all -> 0x0068 }
            goto L_0x0058
        L_0x0055:
            r3.handleTaskInfoChanged(r4)     // Catch:{ all -> 0x0068 }
        L_0x0058:
            android.util.SparseArray<android.graphics.Point> r0 = r3.mPositionByTaskId     // Catch:{ all -> 0x0068 }
            int r1 = r4.taskId     // Catch:{ all -> 0x0068 }
            android.graphics.Point r2 = new android.graphics.Point     // Catch:{ all -> 0x0068 }
            android.graphics.Point r4 = r4.positionInParent     // Catch:{ all -> 0x0068 }
            r2.<init>(r4)     // Catch:{ all -> 0x0068 }
            r0.put(r1, r2)     // Catch:{ all -> 0x0068 }
            monitor-exit(r3)     // Catch:{ all -> 0x0068 }
            return
        L_0x0068:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0068 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenTaskListener.onTaskInfoChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    private void handleChildTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        this.mLeashByTaskId.put(runningTaskInfo.taskId, surfaceControl);
        this.mPositionByTaskId.put(runningTaskInfo.taskId, new Point(runningTaskInfo.positionInParent));
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            updateChildTaskSurface(runningTaskInfo, surfaceControl, true);
        }
    }

    private void handleChildTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            updateChildTaskSurface(runningTaskInfo, this.mLeashByTaskId.get(runningTaskInfo.taskId), false);
        }
    }

    private void updateChildTaskSurface(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, boolean z) {
        this.mSyncQueue.runInSync(new LegacySplitScreenTaskListener$$ExternalSyntheticLambda0(surfaceControl, runningTaskInfo.positionInParent, z));
    }

    static /* synthetic */ void lambda$updateChildTaskSurface$0(SurfaceControl surfaceControl, Point point, boolean z, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, (Rect) null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        if (z && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            transaction.setAlpha(surfaceControl, 1.0f);
            transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
            transaction.show(surfaceControl);
        }
    }

    private void handleTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!this.mSplitScreenSupported) {
            Log.e(TAG, "Got handleTaskInfoChanged when not initialized: " + runningTaskInfo);
            return;
        }
        boolean z = true;
        boolean z2 = this.mSecondary.topActivityType == 2 || (this.mSecondary.topActivityType == 3 && this.mSplitScreenController.isHomeStackResizable());
        boolean z3 = this.mPrimary.topActivityType == 0;
        boolean z4 = this.mSecondary.topActivityType == 0;
        if (runningTaskInfo.token.asBinder() == this.mPrimary.token.asBinder()) {
            this.mPrimary = runningTaskInfo;
        } else if (runningTaskInfo.token.asBinder() == this.mSecondary.token.asBinder()) {
            this.mSecondary = runningTaskInfo;
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            boolean z5 = this.mPrimary.topActivityType == 0;
            boolean z6 = this.mSecondary.topActivityType == 0;
            if (this.mSecondary.topActivityType != 2 && (this.mSecondary.topActivityType != 3 || !this.mSplitScreenController.isHomeStackResizable())) {
                z = false;
            }
            if (z5 != z3 || z4 != z6 || z2 != z) {
                if (z5 || z6) {
                    if (this.mSplitScreenController.isDividerVisible()) {
                        this.mSplitScreenController.startDismissSplit(false);
                    } else if (!z5 && z3 && z4) {
                        this.mSplitScreenController.startEnterSplit();
                    }
                } else if (z) {
                    ArrayList arrayList = new ArrayList();
                    this.mSplitScreenController.getWmProxy().getHomeAndRecentsTasks(arrayList, this.mSplitScreenController.getSecondaryRoot());
                    for (int i = 0; i < arrayList.size(); i++) {
                        ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) arrayList.get(i);
                        SurfaceControl surfaceControl = this.mLeashByTaskId.get(runningTaskInfo2.taskId);
                        if (surfaceControl != null) {
                            updateChildTaskSurface(runningTaskInfo2, surfaceControl, false);
                        }
                    }
                    this.mSplitScreenController.ensureMinimizedSplit();
                } else {
                    this.mSplitScreenController.ensureNormalSplit();
                }
            }
        }
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        if (this.mLeashByTaskId.contains(i)) {
            return this.mLeashByTaskId.get(i);
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        str2 + "  ";
        printWriter.println(str + this);
        printWriter.println(str2 + "mSplitScreenSupported=" + this.mSplitScreenSupported);
        if (this.mPrimary != null) {
            printWriter.println(str2 + "mPrimary.taskId=" + this.mPrimary.taskId);
        }
        if (this.mSecondary != null) {
            printWriter.println(str2 + "mSecondary.taskId=" + this.mSecondary.taskId);
        }
    }

    public String toString() {
        return TAG;
    }
}
