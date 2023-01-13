package com.android.p019wm.shell.kidsmode;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.window.ITaskOrganizerController;
import android.window.TaskAppearedInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/* renamed from: com.android.wm.shell.kidsmode.KidsModeTaskOrganizer */
public class KidsModeTaskOrganizer extends ShellTaskOrganizer {
    private static final int[] CONTROLLED_ACTIVITY_TYPES = {0, 1};
    private static final int[] CONTROLLED_WINDOWING_MODES = {1, 0};
    private static final String TAG = "KidsModeTaskOrganizer";
    private final Context mContext;
    final IBinder mCookie = new Binder();
    /* access modifiers changed from: private */
    public final DisplayController mDisplayController;
    /* access modifiers changed from: private */
    public int mDisplayHeight;
    private final DisplayInsetsController mDisplayInsetsController;
    /* access modifiers changed from: private */
    public int mDisplayWidth;
    private boolean mEnabled;
    /* access modifiers changed from: private */
    public final InsetsState mInsetsState = new InsetsState();
    private KidsModeSettingsObserver mKidsModeSettingsObserver;
    SurfaceControl mLaunchRootLeash;
    ActivityManager.RunningTaskInfo mLaunchRootTask;
    private final Handler mMainHandler;
    DisplayController.OnDisplaysChangedListener mOnDisplaysChangedListener = new DisplayController.OnDisplaysChangedListener() {
        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            DisplayLayout displayLayout;
            if (i == 0 && (displayLayout = KidsModeTaskOrganizer.this.mDisplayController.getDisplayLayout(0)) != null) {
                int width = displayLayout.width();
                int height = displayLayout.height();
                if (width != KidsModeTaskOrganizer.this.mDisplayWidth && height != KidsModeTaskOrganizer.this.mDisplayHeight) {
                    int unused = KidsModeTaskOrganizer.this.mDisplayWidth = width;
                    int unused2 = KidsModeTaskOrganizer.this.mDisplayHeight = height;
                    KidsModeTaskOrganizer.this.updateBounds();
                }
            }
        }
    };
    DisplayInsetsController.OnInsetsChangedListener mOnInsetsChangedListener = new DisplayInsetsController.OnInsetsChangedListener() {
        public void insetsChanged(InsetsState insetsState) {
            if (!Objects.equals(insetsState.peekSource(1), KidsModeTaskOrganizer.this.mInsetsState.peekSource(1)) || !Objects.equals(insetsState.peekSource(21), KidsModeTaskOrganizer.this.mInsetsState.peekSource(21))) {
                KidsModeTaskOrganizer.this.mInsetsState.set(insetsState);
                KidsModeTaskOrganizer.this.updateBounds();
            }
        }
    };
    private final SyncTransactionQueue mSyncQueue;
    private final BroadcastReceiver mUserSwitchIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            KidsModeTaskOrganizer.this.mo49602xd6c5359c();
        }
    };

    KidsModeTaskOrganizer(ITaskOrganizerController iTaskOrganizerController, ShellExecutor shellExecutor, Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional, KidsModeSettingsObserver kidsModeSettingsObserver) {
        super(iTaskOrganizerController, shellExecutor, context, (CompatUIController) null, optional);
        this.mContext = context;
        this.mMainHandler = handler;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mKidsModeSettingsObserver = kidsModeSettingsObserver;
    }

    public KidsModeTaskOrganizer(ShellExecutor shellExecutor, Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional) {
        super(shellExecutor, context, (CompatUIController) null, optional);
        this.mContext = context;
        this.mMainHandler = handler;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
    }

    public void initialize(StartingWindowController startingWindowController) {
        initStartingWindow(startingWindowController);
        if (this.mKidsModeSettingsObserver == null) {
            this.mKidsModeSettingsObserver = new KidsModeSettingsObserver(this.mMainHandler, this.mContext);
        }
        this.mKidsModeSettingsObserver.setOnChangeRunnable(new KidsModeTaskOrganizer$$ExternalSyntheticLambda1(this));
        mo49602xd6c5359c();
        this.mKidsModeSettingsObserver.register();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        this.mContext.registerReceiverForAllUsers(this.mUserSwitchIntentReceiver, intentFilter, (String) null, this.mMainHandler);
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mEnabled && this.mLaunchRootTask == null && runningTaskInfo.launchCookies != null && runningTaskInfo.launchCookies.contains(this.mCookie)) {
            this.mLaunchRootTask = runningTaskInfo;
            this.mLaunchRootLeash = surfaceControl;
            updateTask();
        }
        super.onTaskAppeared(runningTaskInfo, surfaceControl);
        this.mSyncQueue.runInSync(new KidsModeTaskOrganizer$$ExternalSyntheticLambda3(surfaceControl));
    }

    static /* synthetic */ void lambda$onTaskAppeared$1(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.setCrop(surfaceControl, (Rect) null);
        transaction.setPosition(surfaceControl, 0.0f, 0.0f);
        transaction.setAlpha(surfaceControl, 1.0f);
        transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
        transaction.show(surfaceControl);
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mLaunchRootTask;
        if (runningTaskInfo2 != null && runningTaskInfo2.taskId == runningTaskInfo.taskId && !runningTaskInfo.equals(this.mLaunchRootTask)) {
            this.mLaunchRootTask = runningTaskInfo;
        }
        super.onTaskInfoChanged(runningTaskInfo);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: updateKidsModeState */
    public void mo49602xd6c5359c() {
        boolean isEnabled = this.mKidsModeSettingsObserver.isEnabled();
        if (this.mEnabled != isEnabled) {
            this.mEnabled = isEnabled;
            if (isEnabled) {
                enable();
            } else {
                disable();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void enable() {
        setIsIgnoreOrientationRequestDisabled(true);
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout != null) {
            this.mDisplayWidth = displayLayout.width();
            this.mDisplayHeight = displayLayout.height();
        }
        this.mInsetsState.set(this.mDisplayController.getInsetsState(0));
        this.mDisplayInsetsController.addInsetsChangedListener(0, this.mOnInsetsChangedListener);
        this.mDisplayController.addDisplayWindowListener(this.mOnDisplaysChangedListener);
        List<TaskAppearedInfo> registerOrganizer = registerOrganizer();
        for (int i = 0; i < registerOrganizer.size(); i++) {
            TaskAppearedInfo taskAppearedInfo = registerOrganizer.get(i);
            onTaskAppeared(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash());
        }
        createRootTask(0, 1, this.mCookie);
        updateTask();
    }

    /* access modifiers changed from: package-private */
    public void disable() {
        setIsIgnoreOrientationRequestDisabled(false);
        this.mDisplayInsetsController.removeInsetsChangedListener(0, this.mOnInsetsChangedListener);
        this.mDisplayController.removeDisplayWindowListener(this.mOnDisplaysChangedListener);
        updateTask();
        WindowContainerToken windowContainerToken = this.mLaunchRootTask.token;
        if (windowContainerToken != null) {
            deleteRootTask(windowContainerToken);
        }
        this.mLaunchRootTask = null;
        this.mLaunchRootLeash = null;
        unregisterOrganizer();
    }

    private void updateTask() {
        updateTask(getWindowContainerTransaction());
    }

    private void updateTask(WindowContainerTransaction windowContainerTransaction) {
        if (this.mLaunchRootTask != null && this.mLaunchRootLeash != null) {
            Rect calculateBounds = calculateBounds();
            WindowContainerToken windowContainerToken = this.mLaunchRootTask.token;
            windowContainerTransaction.setBounds(windowContainerToken, this.mEnabled ? calculateBounds : null);
            boolean z = this.mEnabled;
            windowContainerTransaction.setLaunchRoot(windowContainerToken, z ? CONTROLLED_WINDOWING_MODES : null, z ? CONTROLLED_ACTIVITY_TYPES : null);
            boolean z2 = this.mEnabled;
            windowContainerTransaction.reparentTasks(z2 ? null : windowContainerToken, z2 ? windowContainerToken : null, CONTROLLED_WINDOWING_MODES, CONTROLLED_ACTIVITY_TYPES, true);
            windowContainerTransaction.reorder(windowContainerToken, this.mEnabled);
            this.mSyncQueue.queue(windowContainerTransaction);
            this.mSyncQueue.runInSync(new KidsModeTaskOrganizer$$ExternalSyntheticLambda0(this.mLaunchRootLeash, calculateBounds));
        }
    }

    static /* synthetic */ void lambda$updateTask$2(SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height());
    }

    private Rect calculateBounds() {
        Rect rect = new Rect(0, 0, this.mDisplayWidth, this.mDisplayHeight);
        InsetsSource peekSource = this.mInsetsState.peekSource(1);
        InsetsSource peekSource2 = this.mInsetsState.peekSource(21);
        if (peekSource != null && !peekSource.getFrame().isEmpty()) {
            rect.inset(peekSource.calculateInsets(rect, false));
        } else if (peekSource2 == null || peekSource2.getFrame().isEmpty()) {
            rect.setEmpty();
        } else {
            rect.inset(peekSource2.calculateInsets(rect, false));
        }
        return rect;
    }

    /* access modifiers changed from: private */
    public void updateBounds() {
        if (this.mLaunchRootTask != null) {
            WindowContainerTransaction windowContainerTransaction = getWindowContainerTransaction();
            Rect calculateBounds = calculateBounds();
            windowContainerTransaction.setBounds(this.mLaunchRootTask.token, calculateBounds);
            this.mSyncQueue.queue(windowContainerTransaction);
            this.mSyncQueue.runInSync(new KidsModeTaskOrganizer$$ExternalSyntheticLambda2(this.mLaunchRootLeash, calculateBounds));
        }
    }

    static /* synthetic */ void lambda$updateBounds$3(SurfaceControl surfaceControl, Rect rect, SurfaceControl.Transaction transaction) {
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height());
    }

    /* access modifiers changed from: package-private */
    public WindowContainerTransaction getWindowContainerTransaction() {
        return new WindowContainerTransaction();
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + " mEnabled=" + this.mEnabled);
        printWriter.println(str2 + " mLaunchRootTask=" + this.mLaunchRootTask);
        printWriter.println(str2 + " mLaunchRootLeash=" + this.mLaunchRootLeash);
        printWriter.println(str2 + " mDisplayWidth=" + this.mDisplayWidth);
        printWriter.println(str2 + " mDisplayHeight=" + this.mDisplayHeight);
        printWriter.println(str2 + " mInsetsState=" + this.mInsetsState);
        super.dump(printWriter, str2);
    }
}
