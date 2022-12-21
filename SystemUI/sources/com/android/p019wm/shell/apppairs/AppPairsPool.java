package com.android.p019wm.shell.apppairs;

import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.p026io.PrintWriter;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.apppairs.AppPairsPool */
class AppPairsPool {
    private static final String TAG = "AppPairsPool";
    final AppPairsController mController;
    private final ArrayList<AppPair> mPool = new ArrayList<>();

    AppPairsPool(AppPairsController appPairsController) {
        this.mController = appPairsController;
        incrementPool();
    }

    /* access modifiers changed from: package-private */
    public AppPair acquire() {
        ArrayList<AppPair> arrayList = this.mPool;
        AppPair remove = arrayList.remove(arrayList.size() - 1);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "acquire entry.taskId=%s listener=%s size=%d", new Object[]{Integer.valueOf(remove.getRootTaskId()), remove, Integer.valueOf(this.mPool.size())});
        if (this.mPool.size() == 0) {
            incrementPool();
        }
        return remove;
    }

    /* access modifiers changed from: package-private */
    public void release(AppPair appPair) {
        this.mPool.add(appPair);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "release entry.taskId=%s listener=%s size=%d", new Object[]{Integer.valueOf(appPair.getRootTaskId()), appPair, Integer.valueOf(this.mPool.size())});
    }

    /* access modifiers changed from: package-private */
    public void incrementPool() {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "incrementPool size=%d", new Object[]{Integer.valueOf(this.mPool.size())});
        AppPair appPair = new AppPair(this.mController);
        this.mController.getTaskOrganizer().createRootTask(0, 1, appPair);
        this.mPool.add(appPair);
    }

    /* access modifiers changed from: package-private */
    public int poolSize() {
        return this.mPool.size();
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = (str + "  ") + "  ";
        printWriter.println(str + this);
        for (int size = this.mPool.size() - 1; size >= 0; size--) {
            this.mPool.get(size).dump(printWriter, str2);
        }
    }

    public String toString() {
        return TAG + "#" + this.mPool.size();
    }
}
