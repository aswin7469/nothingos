package com.android.p019wm.shell.apppairs;

import android.app.ActivityManager;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.apppairs.AppPairsController */
public class AppPairsController {
    /* access modifiers changed from: private */
    public static final String TAG = "AppPairsController";
    private final SparseArray<AppPair> mActiveAppPairs = new SparseArray<>();
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final AppPairsImpl mImpl = new AppPairsImpl();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private AppPairsPool mPairsPool;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;

    public AppPairsController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mMainExecutor = shellExecutor;
    }

    public AppPairs asAppPairs() {
        return this.mImpl;
    }

    public void onOrganizerRegistered() {
        if (this.mPairsPool == null) {
            setPairsPool(new AppPairsPool(this));
        }
    }

    public void setPairsPool(AppPairsPool appPairsPool) {
        this.mPairsPool = appPairsPool;
    }

    public boolean pair(int i, int i2) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTaskOrganizer.getRunningTaskInfo(i2);
        if (runningTaskInfo == null || runningTaskInfo2 == null) {
            return false;
        }
        return pair(runningTaskInfo, runningTaskInfo2);
    }

    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        return pairInner(runningTaskInfo, runningTaskInfo2) != null;
    }

    public AppPair pairInner(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        AppPair acquire = this.mPairsPool.acquire();
        if (!acquire.pair(runningTaskInfo, runningTaskInfo2)) {
            this.mPairsPool.release(acquire);
            return null;
        }
        this.mActiveAppPairs.put(acquire.getRootTaskId(), acquire);
        return acquire;
    }

    public void unpair(int i) {
        unpair(i, true);
    }

    public void unpair(int i, boolean z) {
        AppPair appPair = this.mActiveAppPairs.get(i);
        if (appPair == null) {
            int size = this.mActiveAppPairs.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                AppPair valueAt = this.mActiveAppPairs.valueAt(size);
                if (valueAt.contains(i)) {
                    appPair = valueAt;
                    break;
                }
                size--;
            }
        }
        if (appPair == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "taskId %d isn't isn't in an app-pair.", new Object[]{Integer.valueOf(i)});
            return;
        }
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "unpair taskId=%d pair=%s", new Object[]{Integer.valueOf(i), appPair});
        this.mActiveAppPairs.remove(appPair.getRootTaskId());
        appPair.unpair();
        if (z) {
            this.mPairsPool.release(appPair);
        }
    }

    /* access modifiers changed from: package-private */
    public ShellTaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    /* access modifiers changed from: package-private */
    public SyncTransactionQueue getSyncTransactionQueue() {
        return this.mSyncQueue;
    }

    /* access modifiers changed from: package-private */
    public DisplayController getDisplayController() {
        return this.mDisplayController;
    }

    /* access modifiers changed from: package-private */
    public DisplayImeController getDisplayImeController() {
        return this.mDisplayImeController;
    }

    /* access modifiers changed from: package-private */
    public DisplayInsetsController getDisplayInsetsController() {
        return this.mDisplayInsetsController;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = (str + "  ") + "  ";
        printWriter.println(str + this);
        for (int size = this.mActiveAppPairs.size() - 1; size >= 0; size--) {
            this.mActiveAppPairs.valueAt(size).dump(printWriter, str2);
        }
        AppPairsPool appPairsPool = this.mPairsPool;
        if (appPairsPool != null) {
            appPairsPool.dump(printWriter, str);
        }
    }

    public String toString() {
        return TAG + "#" + this.mActiveAppPairs.size();
    }

    /* renamed from: com.android.wm.shell.apppairs.AppPairsController$AppPairsImpl */
    private class AppPairsImpl implements AppPairs {
        private AppPairsImpl() {
        }

        public boolean pair(int i, int i2) {
            boolean[] zArr = new boolean[1];
            try {
                AppPairsController.this.mMainExecutor.executeBlocking(new AppPairsController$AppPairsImpl$$ExternalSyntheticLambda2(this, zArr, i, i2));
            } catch (InterruptedException unused) {
                Slog.e(AppPairsController.TAG, "Failed to pair tasks: " + i + ", " + i2);
            }
            return zArr[0];
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$pair$0$com-android-wm-shell-apppairs-AppPairsController$AppPairsImpl */
        public /* synthetic */ void mo48194xe2052ddc(boolean[] zArr, int i, int i2) {
            zArr[0] = AppPairsController.this.pair(i, i2);
        }

        public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
            boolean[] zArr = new boolean[1];
            try {
                AppPairsController.this.mMainExecutor.executeBlocking(new AppPairsController$AppPairsImpl$$ExternalSyntheticLambda1(this, zArr, runningTaskInfo, runningTaskInfo2));
            } catch (InterruptedException unused) {
                Slog.e(AppPairsController.TAG, "Failed to pair tasks: " + runningTaskInfo + ", " + runningTaskInfo2);
            }
            return zArr[0];
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$pair$1$com-android-wm-shell-apppairs-AppPairsController$AppPairsImpl */
        public /* synthetic */ void mo48195xd594b21d(boolean[] zArr, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
            zArr[0] = AppPairsController.this.pair(runningTaskInfo, runningTaskInfo2);
        }

        public void unpair(int i) {
            AppPairsController.this.mMainExecutor.execute(new AppPairsController$AppPairsImpl$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unpair$2$com-android-wm-shell-apppairs-AppPairsController$AppPairsImpl */
        public /* synthetic */ void mo48196x5fa14d37(int i) {
            AppPairsController.this.unpair(i);
        }
    }
}
