package com.android.p019wm.shell.splitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.RemoteTransition;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.InstanceId;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SingleInstanceRemoteListener;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.p019wm.shell.common.split.SplitLayout;
import com.android.p019wm.shell.draganddrop.DragAndDropPolicy;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.ISplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.transition.LegacyTransitions;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController */
public class SplitScreenController implements DragAndDropPolicy.Starter, RemoteCallable<SplitScreenController> {
    static final int EXIT_REASON_APP_DOES_NOT_SUPPORT_MULTIWINDOW = 1;
    static final int EXIT_REASON_APP_FINISHED = 2;
    static final int EXIT_REASON_CHILD_TASK_ENTER_PIP = 9;
    static final int EXIT_REASON_DEVICE_FOLDED = 3;
    static final int EXIT_REASON_DRAG_DIVIDER = 4;
    static final int EXIT_REASON_RETURN_HOME = 5;
    static final int EXIT_REASON_ROOT_TASK_VANISHED = 6;
    static final int EXIT_REASON_SCREEN_LOCKED = 7;
    static final int EXIT_REASON_SCREEN_LOCKED_SHOW_ON_TOP = 8;
    static final int EXIT_REASON_UNKNOWN = 0;
    /* access modifiers changed from: private */
    public static final String TAG = "SplitScreenController";
    private final Context mContext;
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final IconProvider mIconProvider;
    private final SplitScreenImpl mImpl = new SplitScreenImpl();
    private final SplitscreenEventLogger mLogger;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final Optional<RecentTasksController> mRecentTasksOptional;
    private final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    private SurfaceControl mSplitTasksContainerLayer;
    /* access modifiers changed from: private */
    public StageCoordinator mStageCoordinator;
    /* access modifiers changed from: private */
    public final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    private final Provider<Optional<StageTaskUnfoldController>> mUnfoldControllerProvider;

    public SplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, IconProvider iconProvider, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mContext = context;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mMainExecutor = shellExecutor;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransitions = transitions;
        this.mTransactionPool = transactionPool;
        this.mUnfoldControllerProvider = provider;
        this.mLogger = new SplitscreenEventLogger();
        this.mIconProvider = iconProvider;
        this.mRecentTasksOptional = optional;
    }

    public SplitScreen asSplitScreen() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onOrganizerRegistered() {
        if (this.mStageCoordinator == null) {
            this.mStageCoordinator = new StageCoordinator(this.mContext, 0, this.mSyncQueue, this.mTaskOrganizer, this.mDisplayController, this.mDisplayImeController, this.mDisplayInsetsController, this.mTransitions, this.mTransactionPool, this.mLogger, this.mIconProvider, this.mMainExecutor, this.mRecentTasksOptional, this.mUnfoldControllerProvider);
        }
    }

    public boolean isSplitScreenVisible() {
        return this.mStageCoordinator.isSplitScreenVisible();
    }

    public ActivityManager.RunningTaskInfo getTaskInfo(int i) {
        if (!isSplitScreenVisible() || i == -1) {
            return null;
        }
        return this.mTaskOrganizer.getRunningTaskInfo(this.mStageCoordinator.getTaskId(i));
    }

    public boolean isTaskInSplitScreen(int i) {
        return isSplitScreenVisible() && this.mStageCoordinator.getStageOfTask(i) != -1;
    }

    public int getSplitPosition(int i) {
        return this.mStageCoordinator.getSplitPosition(i);
    }

    public boolean moveToSideStage(int i, int i2) {
        return moveToStage(i, 1, i2, new WindowContainerTransaction());
    }

    private boolean moveToStage(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        if (runningTaskInfo != null) {
            return this.mStageCoordinator.moveToStage(runningTaskInfo, i2, i3, windowContainerTransaction);
        }
        throw new IllegalArgumentException("Unknown taskId" + i);
    }

    public boolean removeFromSideStage(int i) {
        return this.mStageCoordinator.removeFromSideStage(i);
    }

    public void setSideStagePosition(int i) {
        this.mStageCoordinator.setSideStagePosition(i, (WindowContainerTransaction) null);
    }

    public void enterSplitScreen(int i, boolean z) {
        enterSplitScreen(i, z, new WindowContainerTransaction());
    }

    public void prepareEnterSplitScreen(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        this.mStageCoordinator.prepareEnterSplitScreen(windowContainerTransaction, runningTaskInfo, i);
    }

    public void finishEnterSplitScreen(SurfaceControl.Transaction transaction) {
        this.mStageCoordinator.finishEnterSplitScreen(transaction);
    }

    public void enterSplitScreen(int i, boolean z, WindowContainerTransaction windowContainerTransaction) {
        moveToStage(i, isSplitScreenVisible() ? -1 : 1, z ^ true ? 1 : 0, windowContainerTransaction);
    }

    public void exitSplitScreen(int i, int i2) {
        this.mStageCoordinator.exitSplitScreen(i, i2);
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        this.mStageCoordinator.onKeyguardVisibilityChanged(z);
    }

    public void onFinishedWakingUp() {
        this.mStageCoordinator.onFinishedWakingUp();
    }

    public void exitSplitScreenOnHide(boolean z) {
        this.mStageCoordinator.exitSplitScreenOnHide(z);
    }

    public void getStageBounds(Rect rect, Rect rect2) {
        this.mStageCoordinator.getStageBounds(rect, rect2);
    }

    public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.registerSplitScreenListener(splitScreenListener);
    }

    public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.unregisterSplitScreenListener(splitScreenListener);
    }

    public void startTask(int i, int i2, Bundle bundle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i2, bundle, (WindowContainerTransaction) null);
        try {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictChildTasks(i2, windowContainerTransaction);
            int startActivityFromRecents = ActivityTaskManager.getService().startActivityFromRecents(i, resolveStartStage);
            if (startActivityFromRecents == 0 || startActivityFromRecents == 2) {
                this.mSyncQueue.queue(windowContainerTransaction);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, (WindowContainerTransaction) null);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
        try {
            ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, (Rect) null, resolveStartStage, userHandle);
            this.mSyncQueue.queue(windowContainerTransaction);
        } catch (ActivityNotFoundException e) {
            Slog.e(TAG, "Failed to launch shortcut", e);
        }
    }

    public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            startIntentLegacy(pendingIntent, intent, i, bundle);
            return;
        }
        try {
            Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, (WindowContainerTransaction) null);
            if (intent == null) {
                intent = new Intent();
            }
            Intent intent2 = intent;
            intent2.addFlags(262144);
            pendingIntent.send(this.mContext, 0, intent2, (PendingIntent.OnFinished) null, (Handler) null, (String) null, resolveStartStage);
        } catch (PendingIntent.CanceledException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    private void startIntentLegacy(final PendingIntent pendingIntent, Intent intent, final int i, Bundle bundle) {
        final WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
        C35611 r1 = new LegacyTransitions.ILegacyTransition() {
            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, SurfaceControl.Transaction transaction) {
                ComponentName componentName = null;
                if (remoteAnimationTargetArr == null || remoteAnimationTargetArr.length == 0) {
                    ActivityManager.RunningTaskInfo taskInfo = SplitScreenController.this.getTaskInfo(SplitLayout.reversePosition(i));
                    ComponentName componentName2 = taskInfo != null ? taskInfo.baseActivity : null;
                    if (pendingIntent.getIntent() != null) {
                        componentName = pendingIntent.getIntent().getComponent();
                    }
                    if (componentName2 != null && componentName2.equals(componentName)) {
                        SplitScreenController splitScreenController = SplitScreenController.this;
                        splitScreenController.setSideStagePosition(SplitLayout.reversePosition(splitScreenController.mStageCoordinator.getSideStagePosition()));
                    }
                    transaction.apply();
                    return;
                }
                SplitScreenController.this.mStageCoordinator.updateSurfaceBounds((SplitLayout) null, transaction, false);
                for (int i2 = 0; i2 < remoteAnimationTargetArr.length; i2++) {
                    if (remoteAnimationTargetArr[i2].mode == 0) {
                        transaction.show(remoteAnimationTargetArr[i2].leash);
                    }
                }
                transaction.apply();
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Slog.e(SplitScreenController.TAG, "Error finishing legacy transition: ", e);
                    }
                }
                SplitScreenController.this.mSyncQueue.queue(windowContainerTransaction);
            }
        };
        WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, windowContainerTransaction2);
        if (intent == null) {
            intent = new Intent();
        }
        intent.addFlags(262144);
        windowContainerTransaction2.sendPendingIntent(pendingIntent, intent, resolveStartStage);
        this.mSyncQueue.queue(r1, 1, windowContainerTransaction2);
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (isSplitScreenVisible()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictInvisibleChildTasks(windowContainerTransaction);
            this.mSyncQueue.queue(windowContainerTransaction);
        }
        return reparentSplitTasksForAnimation(remoteAnimationTargetArr, true);
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        return reparentSplitTasksForAnimation(remoteAnimationTargetArr, false);
    }

    private RemoteAnimationTarget[] reparentSplitTasksForAnimation(RemoteAnimationTarget[] remoteAnimationTargetArr, boolean z) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            return null;
        }
        if (z && !isSplitScreenVisible()) {
            return null;
        }
        if (!z && remoteAnimationTargetArr.length < 2) {
            return null;
        }
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        SurfaceControl surfaceControl = this.mSplitTasksContainerLayer;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
        }
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("RecentsAnimationSplitTasks").setHidden(false).setCallsite("SplitScreenController#onGoingtoRecentsLegacy");
        this.mRootTDAOrganizer.attachToDisplayArea(0, callsite);
        this.mSplitTasksContainerLayer = callsite.build();
        Arrays.sort(remoteAnimationTargetArr, new SplitScreenController$$ExternalSyntheticLambda0());
        int length = remoteAnimationTargetArr.length;
        int i = 0;
        int i2 = 1;
        while (i < length) {
            RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[i];
            transaction.reparent(remoteAnimationTarget.leash, this.mSplitTasksContainerLayer);
            transaction.setPosition(remoteAnimationTarget.leash, (float) remoteAnimationTarget.screenSpaceBounds.left, (float) remoteAnimationTarget.screenSpaceBounds.top);
            transaction.setLayer(remoteAnimationTarget.leash, i2);
            i++;
            i2++;
        }
        transaction.apply();
        transaction.close();
        return new RemoteAnimationTarget[]{this.mStageCoordinator.getDividerBarLegacyTarget()};
    }

    static /* synthetic */ int lambda$reparentSplitTasksForAnimation$0(RemoteAnimationTarget remoteAnimationTarget, RemoteAnimationTarget remoteAnimationTarget2) {
        return remoteAnimationTarget.prefixOrderIndex - remoteAnimationTarget2.prefixOrderIndex;
    }

    public void logOnDroppedToSplit(int i, InstanceId instanceId) {
        this.mStageCoordinator.logOnDroppedToSplit(i, instanceId);
    }

    public static String exitReasonToString(int i) {
        switch (i) {
            case 0:
                return "UNKNOWN_EXIT";
            case 1:
                return "APP_DOES_NOT_SUPPORT_MULTIWINDOW";
            case 2:
                return "APP_FINISHED";
            case 3:
                return "DEVICE_FOLDED";
            case 4:
                return "DRAG_DIVIDER";
            case 5:
                return "RETURN_HOME";
            case 6:
                return "ROOT_TASK_VANISHED";
            case 7:
                return "SCREEN_LOCKED";
            case 8:
                return "SCREEN_LOCKED_SHOW_ON_TOP";
            case 9:
                return "CHILD_TASK_ENTER_PIP";
            default:
                return "unknown reason, reason int = " + i;
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + TAG);
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        if (stageCoordinator != null) {
            stageCoordinator.dump(printWriter, str);
        }
    }

    @ExternalThread
    /* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$SplitScreenImpl */
    private class SplitScreenImpl implements SplitScreen {
        /* access modifiers changed from: private */
        public final ArrayMap<SplitScreen.SplitScreenListener, Executor> mExecutors;
        private ISplitScreenImpl mISplitScreen;
        private final SplitScreen.SplitScreenListener mListener;

        private SplitScreenImpl() {
            this.mExecutors = new ArrayMap<>();
            this.mListener = new SplitScreen.SplitScreenListener() {
                public void onStagePositionChanged(int i, int i2) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new C3573xcb46d402(this, i3, i, i2));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onStagePositionChanged$0$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo50803x866440cf(int i, int i2, int i3) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onStagePositionChanged(i2, i3);
                }

                public void onTaskStageChanged(int i, int i2, boolean z) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new C3572xcb46d401(this, i3, i, i2, z));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onTaskStageChanged$1$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo50804x26ca89a0(int i, int i2, int i3, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onTaskStageChanged(i2, i3, z);
                }

                public void onSplitVisibilityChanged(boolean z) {
                    for (int i = 0; i < SplitScreenImpl.this.mExecutors.size(); i++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i)).execute(new C3571xcb46d400(this, i, z));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onSplitVisibilityChanged$2$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo50802x35076252(int i, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onSplitVisibilityChanged(z);
                }
            };
        }

        public ISplitScreen createExternalInterface() {
            ISplitScreenImpl iSplitScreenImpl = this.mISplitScreen;
            if (iSplitScreenImpl != null) {
                iSplitScreenImpl.invalidate();
            }
            ISplitScreenImpl iSplitScreenImpl2 = new ISplitScreenImpl(SplitScreenController.this);
            this.mISplitScreen = iSplitScreenImpl2;
            return iSplitScreenImpl2;
        }

        public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener, Executor executor) {
            if (!this.mExecutors.containsKey(splitScreenListener)) {
                SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1(this, splitScreenListener, executor));
                executor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2(this, splitScreenListener));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$0$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50799x486dac51(SplitScreen.SplitScreenListener splitScreenListener, Executor executor) {
            if (this.mExecutors.size() == 0) {
                SplitScreenController.this.registerSplitScreenListener(this.mListener);
            }
            this.mExecutors.put(splitScreenListener, executor);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$1$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50800xcab86130(SplitScreen.SplitScreenListener splitScreenListener) {
            SplitScreenController.this.mStageCoordinator.sendStatusToListener(splitScreenListener);
        }

        public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3(this, splitScreenListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterSplitScreenListener$2$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50801xe58688e8(SplitScreen.SplitScreenListener splitScreenListener) {
            this.mExecutors.remove(splitScreenListener);
            if (this.mExecutors.size() == 0) {
                SplitScreenController.this.unregisterSplitScreenListener(this.mListener);
            }
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onKeyguardVisibilityChanged$3$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50798x206d89e8(boolean z) {
            SplitScreenController.this.onKeyguardVisibilityChanged(z);
        }

        public void onFinishedWakingUp() {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFinishedWakingUp$4$com-android-wm-shell-splitscreen-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50797x8e66bf5d() {
            SplitScreenController.this.onFinishedWakingUp();
        }
    }

    /* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl */
    private static class ISplitScreenImpl extends ISplitScreen.Stub {
        private SplitScreenController mController;
        /* access modifiers changed from: private */
        public final SingleInstanceRemoteListener<SplitScreenController, ISplitScreenListener> mListener;
        private final SplitScreen.SplitScreenListener mSplitScreenListener = new SplitScreen.SplitScreenListener() {
            public void onStagePositionChanged(int i, int i2) {
                ISplitScreenImpl.this.mListener.call(new C3569xc118fe9b(i, i2));
            }

            public void onTaskStageChanged(int i, int i2, boolean z) {
                ISplitScreenImpl.this.mListener.call(new C3570xc118fe9c(i, i2, z));
            }
        };

        public ISplitScreenImpl(SplitScreenController splitScreenController) {
            this.mController = splitScreenController;
            this.mListener = new SingleInstanceRemoteListener<>(splitScreenController, new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1(this), new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-android-wm-shell-splitscreen-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50793xd830f43b(SplitScreenController splitScreenController) {
            splitScreenController.registerSplitScreenListener(this.mSplitScreenListener);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$1$com-android-wm-shell-splitscreen-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50794x9f3cdb3c(SplitScreenController splitScreenController) {
            splitScreenController.unregisterSplitScreenListener(this.mSplitScreenListener);
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mController = null;
        }

        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerSplitScreenListener", new C3566xeaa4ee03(this, iSplitScreenListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$2$com-android-wm-shell-splitscreen-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50795xca1e3634(ISplitScreenListener iSplitScreenListener, SplitScreenController splitScreenController) {
            this.mListener.register(iSplitScreenListener);
        }

        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterSplitScreenListener$3$com-android-wm-shell-splitscreen-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50796x915057c(SplitScreenController splitScreenController) {
            this.mListener.unregister();
        }

        public void exitSplitScreen(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreen", new C3564xeaa4ee01(i));
        }

        public void exitSplitScreenOnHide(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreenOnHide", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4(z));
        }

        public void removeFromSideStage(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "removeFromSideStage", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(i));
        }

        public void startTask(int i, int i2, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTask", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(i, i2, bundle));
        }

        public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new C3565xeaa4ee02(i, bundle, i2, bundle2, i3, f, remoteAnimationAdapter));
        }

        public void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntentAndTaskWithLegacyTransition", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(pendingIntent, intent, i, bundle, bundle2, i2, f, remoteAnimationAdapter));
        }

        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9(i, bundle, i2, bundle2, i3, f, remoteTransition));
        }

        public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startShortcut", new C3567xeaa4ee04(str, str2, i, bundle, userHandle));
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntent", new C3568xeaa4ee05(pendingIntent, intent, i, bundle));
        }

        public RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
            RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onGoingToRecentsLegacy", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6(remoteAnimationTargetArr2, remoteAnimationTargetArr), true);
            return remoteAnimationTargetArr2[0];
        }

        static /* synthetic */ void lambda$onGoingToRecentsLegacy$13(RemoteAnimationTarget[][] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, SplitScreenController splitScreenController) {
            remoteAnimationTargetArr[0] = splitScreenController.onGoingToRecentsLegacy(remoteAnimationTargetArr2);
        }

        public RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) {
            RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onStartingSplitLegacy", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(remoteAnimationTargetArr2, remoteAnimationTargetArr), true);
            return remoteAnimationTargetArr2[0];
        }

        static /* synthetic */ void lambda$onStartingSplitLegacy$14(RemoteAnimationTarget[][] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, SplitScreenController splitScreenController) {
            remoteAnimationTargetArr[0] = splitScreenController.onStartingSplitLegacy(remoteAnimationTargetArr2);
        }
    }
}
