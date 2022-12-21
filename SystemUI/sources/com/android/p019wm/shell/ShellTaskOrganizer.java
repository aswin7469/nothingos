package com.android.p019wm.shell;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.ITaskOrganizerController;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskAppearedInfo;
import android.window.TaskOrganizer;
import com.android.internal.protolog.common.ProtoLog;
import com.android.internal.util.FrameworkStatsLog;
import com.android.p019wm.shell.common.ScreenshotUtils;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.ShellTaskOrganizer */
public class ShellTaskOrganizer extends TaskOrganizer implements CompatUIController.CompatUICallback {
    private static final String TAG = "ShellTaskOrganizer";
    public static final int TASK_LISTENER_TYPE_FREEFORM = -5;
    public static final int TASK_LISTENER_TYPE_FULLSCREEN = -2;
    public static final int TASK_LISTENER_TYPE_MULTI_WINDOW = -3;
    public static final int TASK_LISTENER_TYPE_PIP = -4;
    public static final int TASK_LISTENER_TYPE_UNDEFINED = -1;
    private final CompatUIController mCompatUI;
    private final ArraySet<FocusListener> mFocusListeners;
    private ActivityManager.RunningTaskInfo mLastFocusedTaskInfo;
    private final ArrayMap<IBinder, TaskListener> mLaunchCookieToListener;
    private final Object mLock;
    private final ArraySet<LocusIdListener> mLocusIdListeners;
    private final Optional<RecentTasksController> mRecentTasks;
    private StartingWindowController mStartingWindow;
    private final SparseArray<TaskListener> mTaskListeners;
    private final SparseArray<TaskAppearedInfo> mTasks;
    private final SparseArray<LocusId> mVisibleTasksWithLocusId;

    /* renamed from: com.android.wm.shell.ShellTaskOrganizer$FocusListener */
    public interface FocusListener {
        void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo);
    }

    /* renamed from: com.android.wm.shell.ShellTaskOrganizer$LocusIdListener */
    public interface LocusIdListener {
        void onVisibilityChanged(int i, LocusId locusId, boolean z);
    }

    /* renamed from: com.android.wm.shell.ShellTaskOrganizer$TaskListenerType */
    public @interface TaskListenerType {
    }

    /* renamed from: com.android.wm.shell.ShellTaskOrganizer$TaskListener */
    public interface TaskListener {
        void dump(PrintWriter printWriter, String str) {
        }

        void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        }

        void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        boolean supportCompatUI() {
            return true;
        }

        void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
            throw new IllegalStateException("This task listener doesn't support child surface attachment.");
        }

        void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
            throw new IllegalStateException("This task listener doesn't support child surface reparent.");
        }
    }

    public ShellTaskOrganizer(ShellExecutor shellExecutor, Context context) {
        this((ITaskOrganizerController) null, shellExecutor, context, (CompatUIController) null, Optional.empty());
    }

    public ShellTaskOrganizer(ShellExecutor shellExecutor, Context context, CompatUIController compatUIController) {
        this((ITaskOrganizerController) null, shellExecutor, context, compatUIController, Optional.empty());
    }

    public ShellTaskOrganizer(ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        this((ITaskOrganizerController) null, shellExecutor, context, compatUIController, optional);
    }

    protected ShellTaskOrganizer(ITaskOrganizerController iTaskOrganizerController, ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        super(iTaskOrganizerController, shellExecutor);
        this.mTaskListeners = new SparseArray<>();
        this.mTasks = new SparseArray<>();
        this.mLaunchCookieToListener = new ArrayMap<>();
        this.mVisibleTasksWithLocusId = new SparseArray<>();
        this.mLocusIdListeners = new ArraySet<>();
        this.mFocusListeners = new ArraySet<>();
        this.mLock = new Object();
        this.mCompatUI = compatUIController;
        this.mRecentTasks = optional;
        if (compatUIController != null) {
            compatUIController.setCompatUICallback(this);
        }
    }

    public List<TaskAppearedInfo> registerOrganizer() {
        List<TaskAppearedInfo> registerOrganizer;
        synchronized (this.mLock) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Registering organizer", new Object[0]);
            registerOrganizer = ShellTaskOrganizer.super.registerOrganizer();
            for (int i = 0; i < registerOrganizer.size(); i++) {
                TaskAppearedInfo taskAppearedInfo = registerOrganizer.get(i);
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Existing task: id=%d component=%s", new Object[]{Integer.valueOf(taskAppearedInfo.getTaskInfo().taskId), taskAppearedInfo.getTaskInfo().baseIntent});
                onTaskAppeared(taskAppearedInfo);
            }
        }
        return registerOrganizer;
    }

    public void unregisterOrganizer() {
        ShellTaskOrganizer.super.unregisterOrganizer();
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.clearAllWindows();
        }
    }

    public void createRootTask(int i, int i2, TaskListener taskListener) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "createRootTask() displayId=%d winMode=%d listener=%s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), taskListener.toString()});
        Binder binder = new Binder();
        setPendingLaunchCookieListener(binder, taskListener);
        ShellTaskOrganizer.super.createRootTask(i, i2, binder);
    }

    public void initStartingWindow(StartingWindowController startingWindowController) {
        this.mStartingWindow = startingWindowController;
    }

    public void addListenerForTaskId(TaskListener taskListener, int i) {
        synchronized (this.mLock) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "addListenerForTaskId taskId=%s", new Object[]{Integer.valueOf(i)});
            if (this.mTaskListeners.get(i) == null) {
                TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
                if (taskAppearedInfo != null) {
                    TaskListener taskListener2 = getTaskListener(taskAppearedInfo.getTaskInfo());
                    this.mTaskListeners.put(i, taskListener);
                    updateTaskListenerIfNeeded(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash(), taskListener2, taskListener);
                } else {
                    throw new IllegalArgumentException("addListenerForTaskId unknown taskId=" + i);
                }
            } else {
                throw new IllegalArgumentException("Listener for taskId=" + i + " already exists");
            }
        }
    }

    public void addListenerForType(TaskListener taskListener, int... iArr) {
        synchronized (this.mLock) {
            int i = 0;
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "addListenerForType types=%s listener=%s", new Object[]{Arrays.toString(iArr), taskListener});
            int length = iArr.length;
            while (i < length) {
                int i2 = iArr[i];
                if (this.mTaskListeners.get(i2) == null) {
                    this.mTaskListeners.put(i2, taskListener);
                    i++;
                } else {
                    throw new IllegalArgumentException("Listener for listenerType=" + i2 + " already exists");
                }
            }
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                    taskListener.onTaskAppeared(valueAt.getTaskInfo(), valueAt.getLeash());
                }
            }
        }
    }

    public void removeListener(TaskListener taskListener) {
        synchronized (this.mLock) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Remove listener=%s", new Object[]{taskListener});
            if (this.mTaskListeners.indexOfValue(taskListener) == -1) {
                Log.w(TAG, "No registered listener found");
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                    arrayList.add(valueAt);
                }
            }
            for (int size2 = this.mTaskListeners.size() - 1; size2 >= 0; size2--) {
                if (this.mTaskListeners.valueAt(size2) == taskListener) {
                    this.mTaskListeners.removeAt(size2);
                }
            }
            for (int size3 = arrayList.size() - 1; size3 >= 0; size3--) {
                TaskAppearedInfo taskAppearedInfo = (TaskAppearedInfo) arrayList.get(size3);
                updateTaskListenerIfNeeded(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash(), (TaskListener) null, getTaskListener(taskAppearedInfo.getTaskInfo()));
            }
        }
    }

    public void setPendingLaunchCookieListener(IBinder iBinder, TaskListener taskListener) {
        synchronized (this.mLock) {
            this.mLaunchCookieToListener.put(iBinder, taskListener);
        }
    }

    public void addLocusIdListener(LocusIdListener locusIdListener) {
        synchronized (this.mLock) {
            this.mLocusIdListeners.add(locusIdListener);
            for (int i = 0; i < this.mVisibleTasksWithLocusId.size(); i++) {
                locusIdListener.onVisibilityChanged(this.mVisibleTasksWithLocusId.keyAt(i), this.mVisibleTasksWithLocusId.valueAt(i), true);
            }
        }
    }

    public void removeLocusIdListener(LocusIdListener locusIdListener) {
        synchronized (this.mLock) {
            this.mLocusIdListeners.remove(locusIdListener);
        }
    }

    public void addFocusListener(FocusListener focusListener) {
        synchronized (this.mLock) {
            this.mFocusListeners.add(focusListener);
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mLastFocusedTaskInfo;
            if (runningTaskInfo != null) {
                focusListener.onFocusTaskChanged(runningTaskInfo);
            }
        }
    }

    public void removeLocusIdListener(FocusListener focusListener) {
        synchronized (this.mLock) {
            this.mFocusListeners.remove(focusListener);
        }
    }

    public void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.addStartingWindow(startingWindowInfo, iBinder);
        }
    }

    public void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.removeStartingWindow(startingWindowRemovalInfo);
        }
    }

    public void copySplashScreenView(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.copySplashScreenView(i);
        }
    }

    public void onAppSplashScreenViewRemoved(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.onAppSplashScreenViewRemoved(i);
        }
    }

    public void onImeDrawnOnTask(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.onImeDrawnOnTask(i);
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this.mLock) {
            onTaskAppeared(new TaskAppearedInfo(runningTaskInfo, surfaceControl));
        }
    }

    private void onTaskAppeared(TaskAppearedInfo taskAppearedInfo) {
        int i = taskAppearedInfo.getTaskInfo().taskId;
        this.mTasks.put(i, taskAppearedInfo);
        TaskListener taskListener = getTaskListener(taskAppearedInfo.getTaskInfo(), true);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Task appeared taskId=%d listener=%s", new Object[]{Integer.valueOf(i), taskListener});
        if (taskListener != null) {
            taskListener.onTaskAppeared(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash());
        }
        notifyLocusVisibilityIfNeeded(taskAppearedInfo.getTaskInfo());
        notifyCompatUI(taskAppearedInfo.getTaskInfo(), taskListener);
    }

    public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        TaskAppearedInfo taskAppearedInfo = this.mTasks.get(runningTaskInfo.taskId);
        if (taskAppearedInfo != null) {
            ScreenshotUtils.captureLayer(taskAppearedInfo.getLeash(), rect, consumer);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0096 A[LOOP:0: B:32:0x0096->B:34:0x009e, LOOP_START, PHI: r6 
      PHI: (r6v1 int) = (r6v0 int), (r6v2 int) binds: [B:31:0x0094, B:34:0x009e] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo r11) {
        /*
            r10 = this;
            java.lang.Object r0 = r10.mLock
            monitor-enter(r0)
            com.android.wm.shell.protolog.ShellProtoLogGroup r1 = com.android.p019wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch:{ all -> 0x00b0 }
            java.lang.String r2 = "Task info changed taskId=%d"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b0 }
            int r5 = r11.taskId     // Catch:{ all -> 0x00b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x00b0 }
            r6 = 0
            r4[r6] = r5     // Catch:{ all -> 0x00b0 }
            com.android.internal.protolog.common.ProtoLog.v(r1, r2, r4)     // Catch:{ all -> 0x00b0 }
            android.util.SparseArray<android.window.TaskAppearedInfo> r1 = r10.mTasks     // Catch:{ all -> 0x00b0 }
            int r2 = r11.taskId     // Catch:{ all -> 0x00b0 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x00b0 }
            android.window.TaskAppearedInfo r1 = (android.window.TaskAppearedInfo) r1     // Catch:{ all -> 0x00b0 }
            android.app.ActivityManager$RunningTaskInfo r2 = r1.getTaskInfo()     // Catch:{ all -> 0x00b0 }
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r2 = r10.getTaskListener(r2)     // Catch:{ all -> 0x00b0 }
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r4 = r10.getTaskListener(r11)     // Catch:{ all -> 0x00b0 }
            android.util.SparseArray<android.window.TaskAppearedInfo> r5 = r10.mTasks     // Catch:{ all -> 0x00b0 }
            int r7 = r11.taskId     // Catch:{ all -> 0x00b0 }
            android.window.TaskAppearedInfo r8 = new android.window.TaskAppearedInfo     // Catch:{ all -> 0x00b0 }
            android.view.SurfaceControl r9 = r1.getLeash()     // Catch:{ all -> 0x00b0 }
            r8.<init>(r11, r9)     // Catch:{ all -> 0x00b0 }
            r5.put(r7, r8)     // Catch:{ all -> 0x00b0 }
            android.view.SurfaceControl r5 = r1.getLeash()     // Catch:{ all -> 0x00b0 }
            boolean r2 = r10.updateTaskListenerIfNeeded(r11, r5, r2, r4)     // Catch:{ all -> 0x00b0 }
            if (r2 != 0) goto L_0x004b
            if (r4 == 0) goto L_0x004b
            r4.onTaskInfoChanged(r11)     // Catch:{ all -> 0x00b0 }
        L_0x004b:
            r10.notifyLocusVisibilityIfNeeded(r11)     // Catch:{ all -> 0x00b0 }
            if (r2 != 0) goto L_0x005a
            android.app.ActivityManager$RunningTaskInfo r2 = r1.getTaskInfo()     // Catch:{ all -> 0x00b0 }
            boolean r2 = r11.equalsForCompatUi(r2)     // Catch:{ all -> 0x00b0 }
            if (r2 != 0) goto L_0x005d
        L_0x005a:
            r10.notifyCompatUI(r11, r4)     // Catch:{ all -> 0x00b0 }
        L_0x005d:
            android.app.ActivityManager$RunningTaskInfo r1 = r1.getTaskInfo()     // Catch:{ all -> 0x00b0 }
            int r1 = r1.getWindowingMode()     // Catch:{ all -> 0x00b0 }
            int r2 = r11.getWindowingMode()     // Catch:{ all -> 0x00b0 }
            if (r1 == r2) goto L_0x0075
            java.util.Optional<com.android.wm.shell.recents.RecentTasksController> r1 = r10.mRecentTasks     // Catch:{ all -> 0x00b0 }
            com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1 r2 = new com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1     // Catch:{ all -> 0x00b0 }
            r2.<init>(r11)     // Catch:{ all -> 0x00b0 }
            r1.ifPresent(r2)     // Catch:{ all -> 0x00b0 }
        L_0x0075:
            boolean r1 = r11.isFocused     // Catch:{ all -> 0x00b0 }
            if (r1 != 0) goto L_0x0085
            int r1 = r11.topActivityType     // Catch:{ all -> 0x00b0 }
            r2 = 2
            if (r1 != r2) goto L_0x0083
            boolean r1 = r11.isVisible     // Catch:{ all -> 0x00b0 }
            if (r1 == 0) goto L_0x0083
            goto L_0x0085
        L_0x0083:
            r1 = r6
            goto L_0x0086
        L_0x0085:
            r1 = r3
        L_0x0086:
            android.app.ActivityManager$RunningTaskInfo r2 = r10.mLastFocusedTaskInfo     // Catch:{ all -> 0x00b0 }
            if (r2 == 0) goto L_0x0090
            int r2 = r2.taskId     // Catch:{ all -> 0x00b0 }
            int r4 = r11.taskId     // Catch:{ all -> 0x00b0 }
            if (r2 == r4) goto L_0x0093
        L_0x0090:
            if (r1 == 0) goto L_0x0093
            goto L_0x0094
        L_0x0093:
            r3 = r6
        L_0x0094:
            if (r3 == 0) goto L_0x00ae
        L_0x0096:
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch:{ all -> 0x00b0 }
            int r1 = r1.size()     // Catch:{ all -> 0x00b0 }
            if (r6 >= r1) goto L_0x00ac
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch:{ all -> 0x00b0 }
            java.lang.Object r1 = r1.valueAt(r6)     // Catch:{ all -> 0x00b0 }
            com.android.wm.shell.ShellTaskOrganizer$FocusListener r1 = (com.android.p019wm.shell.ShellTaskOrganizer.FocusListener) r1     // Catch:{ all -> 0x00b0 }
            r1.onFocusTaskChanged(r11)     // Catch:{ all -> 0x00b0 }
            int r6 = r6 + 1
            goto L_0x0096
        L_0x00ac:
            r10.mLastFocusedTaskInfo = r11     // Catch:{ all -> 0x00b0 }
        L_0x00ae:
            monitor-exit(r0)     // Catch:{ all -> 0x00b0 }
            return
        L_0x00b0:
            r10 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00b0 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.ShellTaskOrganizer.onTaskInfoChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Task root back pressed taskId=%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
            TaskListener taskListener = getTaskListener(runningTaskInfo);
            if (taskListener != null) {
                taskListener.onBackPressedOnTaskRoot(runningTaskInfo);
            }
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Task vanished taskId=%d", new Object[]{Integer.valueOf(runningTaskInfo.taskId)});
            int i = runningTaskInfo.taskId;
            TaskListener taskListener = getTaskListener(this.mTasks.get(i).getTaskInfo());
            this.mTasks.remove(i);
            if (taskListener != null) {
                taskListener.onTaskVanished(runningTaskInfo);
            }
            notifyLocusVisibilityIfNeeded(runningTaskInfo);
            notifyCompatUI(runningTaskInfo, (TaskListener) null);
            this.mRecentTasks.ifPresent(new ShellTaskOrganizer$$ExternalSyntheticLambda0(runningTaskInfo));
        }
    }

    public ActivityManager.RunningTaskInfo getRunningTaskInfo(int i) {
        ActivityManager.RunningTaskInfo taskInfo;
        synchronized (this.mLock) {
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
            taskInfo = taskAppearedInfo != null ? taskAppearedInfo.getTaskInfo() : null;
        }
        return taskInfo;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSurfaceMetadata(int r2, int r3, int r4) {
        /*
            r1 = this;
            java.lang.Object r0 = r1.mLock
            monitor-enter(r0)
            android.util.SparseArray<android.window.TaskAppearedInfo> r1 = r1.mTasks     // Catch:{ all -> 0x0027 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0027 }
            android.window.TaskAppearedInfo r1 = (android.window.TaskAppearedInfo) r1     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x0025
            android.view.SurfaceControl r2 = r1.getLeash()     // Catch:{ all -> 0x0027 }
            if (r2 != 0) goto L_0x0014
            goto L_0x0025
        L_0x0014:
            android.view.SurfaceControl$Transaction r2 = new android.view.SurfaceControl$Transaction     // Catch:{ all -> 0x0027 }
            r2.<init>()     // Catch:{ all -> 0x0027 }
            android.view.SurfaceControl r1 = r1.getLeash()     // Catch:{ all -> 0x0027 }
            r2.setMetadata(r1, r3, r4)     // Catch:{ all -> 0x0027 }
            r2.apply()     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return
        L_0x0025:
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return
        L_0x0027:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.ShellTaskOrganizer.setSurfaceMetadata(int, int, int):void");
    }

    private boolean updateTaskListenerIfNeeded(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, TaskListener taskListener, TaskListener taskListener2) {
        if (taskListener == taskListener2) {
            return false;
        }
        if (taskListener != null) {
            taskListener.onTaskVanished(runningTaskInfo);
        }
        if (taskListener2 == null) {
            return true;
        }
        taskListener2.onTaskAppeared(runningTaskInfo, surfaceControl);
        return true;
    }

    private void notifyLocusVisibilityIfNeeded(TaskInfo taskInfo) {
        int i = taskInfo.taskId;
        LocusId locusId = this.mVisibleTasksWithLocusId.get(i);
        boolean equals = Objects.equals(locusId, taskInfo.mTopActivityLocusId);
        if (locusId == null) {
            if (taskInfo.mTopActivityLocusId != null && taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, taskInfo.mTopActivityLocusId);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
            }
        } else if (equals && !taskInfo.isVisible) {
            this.mVisibleTasksWithLocusId.remove(i);
            notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, false);
        } else if (equals) {
        } else {
            if (taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, taskInfo.mTopActivityLocusId);
                notifyLocusIdChange(i, locusId, false);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
                return;
            }
            this.mVisibleTasksWithLocusId.remove(taskInfo.taskId);
            notifyLocusIdChange(i, locusId, false);
        }
    }

    private void notifyLocusIdChange(int i, LocusId locusId, boolean z) {
        for (int i2 = 0; i2 < this.mLocusIdListeners.size(); i2++) {
            this.mLocusIdListeners.valueAt(i2).onVisibilityChanged(i, locusId, z);
        }
    }

    public void onSizeCompatRestartButtonAppeared(int i) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            logSizeCompatRestartButtonEventReported(taskAppearedInfo, 1);
        }
    }

    public void onSizeCompatRestartButtonClicked(int i) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            logSizeCompatRestartButtonEventReported(taskAppearedInfo, 2);
            restartTaskTopActivityProcessIfVisible(taskAppearedInfo.getTaskInfo().token);
        }
    }

    public void onCameraControlStateUpdated(int i, int i2) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            updateCameraCompatControlState(taskAppearedInfo.getTaskInfo().token, i2);
        }
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        TaskListener taskListener;
        synchronized (this.mLock) {
            taskListener = this.mTasks.contains(i) ? getTaskListener(this.mTasks.get(i).getTaskInfo()) : null;
        }
        if (taskListener == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_TASK_ORG, "Failed to find Task to reparent surface taskId=%d", new Object[]{Integer.valueOf(i)});
        } else {
            taskListener.reparentChildSurfaceToTask(i, surfaceControl, transaction);
        }
    }

    private void logSizeCompatRestartButtonEventReported(TaskAppearedInfo taskAppearedInfo, int i) {
        ActivityInfo activityInfo = taskAppearedInfo.getTaskInfo().topActivityInfo;
        if (activityInfo != null) {
            FrameworkStatsLog.write(387, activityInfo.applicationInfo.uid, i);
        }
    }

    private void notifyCompatUI(ActivityManager.RunningTaskInfo runningTaskInfo, TaskListener taskListener) {
        if (this.mCompatUI != null) {
            if (taskListener == null || !taskListener.supportCompatUI() || !runningTaskInfo.hasCompatUI() || !runningTaskInfo.isVisible) {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, (TaskListener) null);
            } else {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, taskListener);
            }
        }
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return getTaskListener(runningTaskInfo, false);
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z) {
        TaskListener taskListener;
        int i = runningTaskInfo.taskId;
        ArrayList arrayList = runningTaskInfo.launchCookies;
        int size = arrayList.size() - 1;
        while (size >= 0) {
            IBinder iBinder = (IBinder) arrayList.get(size);
            TaskListener taskListener2 = this.mLaunchCookieToListener.get(iBinder);
            if (taskListener2 == null) {
                size--;
            } else {
                if (z) {
                    this.mLaunchCookieToListener.remove(iBinder);
                    this.mTaskListeners.put(i, taskListener2);
                }
                return taskListener2;
            }
        }
        TaskListener taskListener3 = this.mTaskListeners.get(i);
        if (taskListener3 != null) {
            return taskListener3;
        }
        if (runningTaskInfo.hasParentTask() && (taskListener = this.mTaskListeners.get(runningTaskInfo.parentTaskId)) != null) {
            return taskListener;
        }
        return this.mTaskListeners.get(taskInfoToTaskListenerType(runningTaskInfo));
    }

    static int taskInfoToTaskListenerType(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int windowingMode = runningTaskInfo.getWindowingMode();
        if (windowingMode == 1) {
            return -2;
        }
        if (windowingMode == 2) {
            return -4;
        }
        if (windowingMode != 5) {
            return windowingMode != 6 ? -1 : -3;
        }
        return -5;
    }

    public static String taskListenerTypeToString(int i) {
        if (i == -5) {
            return "TASK_LISTENER_TYPE_FREEFORM";
        }
        if (i == -4) {
            return "TASK_LISTENER_TYPE_PIP";
        }
        if (i == -3) {
            return "TASK_LISTENER_TYPE_MULTI_WINDOW";
        }
        if (i != -2) {
            return i != -1 ? "taskId#" + i : "TASK_LISTENER_TYPE_UNDEFINED";
        }
        return "TASK_LISTENER_TYPE_FULLSCREEN";
    }

    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            String str2 = str + "  ";
            String str3 = str2 + "  ";
            printWriter.println(str + TAG);
            printWriter.println(str2 + this.mTaskListeners.size() + " Listeners");
            for (int size = this.mTaskListeners.size() - 1; size >= 0; size--) {
                printWriter.println(str2 + "#" + size + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + taskListenerTypeToString(this.mTaskListeners.keyAt(size)));
                this.mTaskListeners.valueAt(size).dump(printWriter, str3);
            }
            printWriter.println();
            printWriter.println(str2 + this.mTasks.size() + " Tasks");
            for (int size2 = this.mTasks.size() - 1; size2 >= 0; size2--) {
                printWriter.println(str2 + "#" + size2 + " task=" + this.mTasks.keyAt(size2) + " listener=" + getTaskListener(this.mTasks.valueAt(size2).getTaskInfo()));
            }
            printWriter.println();
            printWriter.println(str2 + this.mLaunchCookieToListener.size() + " Launch Cookies");
            for (int size3 = this.mLaunchCookieToListener.size() - 1; size3 >= 0; size3--) {
                printWriter.println(str2 + "#" + size3 + " cookie=" + this.mLaunchCookieToListener.keyAt(size3) + " listener=" + this.mLaunchCookieToListener.valueAt(size3));
            }
        }
    }
}
