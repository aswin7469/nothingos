package com.android.p019wm.shell.recents;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskInfo;
import android.content.Context;
import android.os.RemoteException;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SingleInstanceRemoteListener;
import com.android.p019wm.shell.common.TaskStackListenerCallback;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.recents.IRecentTasks;
import com.android.p019wm.shell.util.GroupedRecentTaskInfo;
import com.android.p019wm.shell.util.StagedSplitBounds;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: com.android.wm.shell.recents.RecentTasksController */
public class RecentTasksController implements TaskStackListenerCallback, RemoteCallable<RecentTasksController> {
    private static final String TAG = "RecentTasksController";
    private final ArrayList<Runnable> mCallbacks = new ArrayList<>();
    private final Context mContext;
    private final RecentTasks mImpl = new RecentTasksImpl();
    private final ShellExecutor mMainExecutor;
    private final SparseIntArray mSplitTasks = new SparseIntArray();
    private final Map<Integer, StagedSplitBounds> mTaskSplitBoundsMap = new HashMap();
    private final TaskStackListenerImpl mTaskStackListener;

    public static RecentTasksController create(Context context, TaskStackListenerImpl taskStackListenerImpl, @ShellMainThread ShellExecutor shellExecutor) {
        if (!context.getResources().getBoolean(17891678)) {
            return null;
        }
        return new RecentTasksController(context, taskStackListenerImpl, shellExecutor);
    }

    RecentTasksController(Context context, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mTaskStackListener = taskStackListenerImpl;
        this.mMainExecutor = shellExecutor;
    }

    public RecentTasks asRecentTasks() {
        return this.mImpl;
    }

    public void init() {
        this.mTaskStackListener.addListener(this);
    }

    public void addSplitPair(int i, int i2, StagedSplitBounds stagedSplitBounds) {
        if (i != i2) {
            if (this.mSplitTasks.get(i, -1) != i2 || !this.mTaskSplitBoundsMap.get(Integer.valueOf(i)).equals(stagedSplitBounds)) {
                removeSplitPair(i);
                removeSplitPair(i2);
                this.mTaskSplitBoundsMap.remove(Integer.valueOf(i));
                this.mTaskSplitBoundsMap.remove(Integer.valueOf(i2));
                this.mSplitTasks.put(i, i2);
                this.mSplitTasks.put(i2, i);
                this.mTaskSplitBoundsMap.put(Integer.valueOf(i), stagedSplitBounds);
                this.mTaskSplitBoundsMap.put(Integer.valueOf(i2), stagedSplitBounds);
                notifyRecentTasksChanged();
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, "Add split pair: %d, %d, %s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), stagedSplitBounds});
            }
        }
    }

    public void removeSplitPair(int i) {
        int i2 = this.mSplitTasks.get(i, -1);
        if (i2 != -1) {
            this.mSplitTasks.delete(i);
            this.mSplitTasks.delete(i2);
            this.mTaskSplitBoundsMap.remove(Integer.valueOf(i));
            this.mTaskSplitBoundsMap.remove(Integer.valueOf(i2));
            notifyRecentTasksChanged();
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, "Remove split pair: %d, %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onTaskStackChanged() {
        notifyRecentTasksChanged();
    }

    public void onRecentTaskListUpdated() {
        notifyRecentTasksChanged();
    }

    public void onTaskRemoved(TaskInfo taskInfo) {
        removeSplitPair(taskInfo.taskId);
        notifyRecentTasksChanged();
    }

    public void onTaskWindowingModeChanged(TaskInfo taskInfo) {
        notifyRecentTasksChanged();
    }

    /* access modifiers changed from: package-private */
    public void notifyRecentTasksChanged() {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, "Notify recent tasks changed", new Object[0]);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).run();
        }
    }

    /* access modifiers changed from: private */
    public void registerRecentTasksListener(Runnable runnable) {
        if (!this.mCallbacks.contains(runnable)) {
            this.mCallbacks.add(runnable);
        }
    }

    /* access modifiers changed from: private */
    public void unregisterRecentTasksListener(Runnable runnable) {
        this.mCallbacks.remove((Object) runnable);
    }

    /* access modifiers changed from: package-private */
    public List<ActivityManager.RecentTaskInfo> getRawRecentTasks(int i, int i2, int i3) {
        return ActivityTaskManager.getInstance().getRecentTasks(i, i2, i3);
    }

    /* access modifiers changed from: package-private */
    public ArrayList<GroupedRecentTaskInfo> getRecentTasks(int i, int i2, int i3) {
        List<ActivityManager.RecentTaskInfo> rawRecentTasks = getRawRecentTasks(i, i2, i3);
        SparseArray sparseArray = new SparseArray();
        for (int i4 = 0; i4 < rawRecentTasks.size(); i4++) {
            ActivityManager.RecentTaskInfo recentTaskInfo = rawRecentTasks.get(i4);
            sparseArray.put(recentTaskInfo.taskId, recentTaskInfo);
        }
        ArrayList<GroupedRecentTaskInfo> arrayList = new ArrayList<>();
        for (int i5 = 0; i5 < rawRecentTasks.size(); i5++) {
            ActivityManager.RecentTaskInfo recentTaskInfo2 = rawRecentTasks.get(i5);
            if (sparseArray.contains(recentTaskInfo2.taskId)) {
                int i6 = this.mSplitTasks.get(recentTaskInfo2.taskId);
                if (i6 == -1 || !sparseArray.contains(i6)) {
                    arrayList.add(new GroupedRecentTaskInfo(recentTaskInfo2));
                } else {
                    sparseArray.remove(i6);
                    arrayList.add(new GroupedRecentTaskInfo(recentTaskInfo2, (ActivityManager.RecentTaskInfo) sparseArray.get(i6), this.mTaskSplitBoundsMap.get(Integer.valueOf(i6))));
                }
            }
        }
        return arrayList;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        ArrayList<GroupedRecentTaskInfo> recentTasks = getRecentTasks(Integer.MAX_VALUE, 2, ActivityManager.getCurrentUser());
        for (int i = 0; i < recentTasks.size(); i++) {
            printWriter.println(str2 + recentTasks.get(i));
        }
    }

    @ExternalThread
    /* renamed from: com.android.wm.shell.recents.RecentTasksController$RecentTasksImpl */
    private class RecentTasksImpl implements RecentTasks {
        private IRecentTasksImpl mIRecentTasks;

        private RecentTasksImpl() {
        }

        public IRecentTasks createExternalInterface() {
            IRecentTasksImpl iRecentTasksImpl = this.mIRecentTasks;
            if (iRecentTasksImpl != null) {
                iRecentTasksImpl.invalidate();
            }
            IRecentTasksImpl iRecentTasksImpl2 = new IRecentTasksImpl(RecentTasksController.this);
            this.mIRecentTasks = iRecentTasksImpl2;
            return iRecentTasksImpl2;
        }
    }

    /* renamed from: com.android.wm.shell.recents.RecentTasksController$IRecentTasksImpl */
    private static class IRecentTasksImpl extends IRecentTasks.Stub {
        private RecentTasksController mController;
        /* access modifiers changed from: private */
        public final SingleInstanceRemoteListener<RecentTasksController, IRecentTasksListener> mListener;
        private final Runnable mRecentTasksListener = new Runnable() {
            public void run() {
                IRecentTasksImpl.this.mListener.call(new C3560x6ba2c7fb());
            }
        };

        public IRecentTasksImpl(RecentTasksController recentTasksController) {
            this.mController = recentTasksController;
            this.mListener = new SingleInstanceRemoteListener<>(recentTasksController, new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda0(this), new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-android-wm-shell-recents-RecentTasksController$IRecentTasksImpl */
        public /* synthetic */ void mo50726x2087de69(RecentTasksController recentTasksController) {
            recentTasksController.registerRecentTasksListener(this.mRecentTasksListener);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$1$com-android-wm-shell-recents-RecentTasksController$IRecentTasksImpl */
        public /* synthetic */ void mo50727x11d96dea(RecentTasksController recentTasksController) {
            recentTasksController.unregisterRecentTasksListener(this.mRecentTasksListener);
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mController = null;
        }

        public void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerRecentTasksListener", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda3(this, iRecentTasksListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerRecentTasksListener$2$com-android-wm-shell-recents-RecentTasksController$IRecentTasksImpl */
        public /* synthetic */ void mo50728x8012ebef(IRecentTasksListener iRecentTasksListener, RecentTasksController recentTasksController) {
            this.mListener.register(iRecentTasksListener);
        }

        public void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterRecentTasksListener", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterRecentTasksListener$3$com-android-wm-shell-recents-RecentTasksController$IRecentTasksImpl */
        public /* synthetic */ void mo50729xd1249f37(RecentTasksController recentTasksController) {
            this.mListener.unregister();
        }

        public GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException {
            RecentTasksController recentTasksController = this.mController;
            if (recentTasksController == null) {
                return new GroupedRecentTaskInfo[0];
            }
            GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(recentTasksController, "getRecentTasks", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda2(groupedRecentTaskInfoArr, i, i2, i3), true);
            return groupedRecentTaskInfoArr[0];
        }

        static /* synthetic */ void lambda$getRecentTasks$4(GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr, int i, int i2, int i3, RecentTasksController recentTasksController) {
            groupedRecentTaskInfoArr[0] = (GroupedRecentTaskInfo[]) recentTasksController.getRecentTasks(i, i2, i3).toArray(new GroupedRecentTaskInfo[0]);
        }
    }
}
