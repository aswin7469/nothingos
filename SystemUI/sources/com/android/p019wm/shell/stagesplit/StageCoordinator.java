package com.android.p019wm.shell.stagesplit;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.window.DisplayAreaInfo;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.InstanceId;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.split.SplitLayout;
import com.android.p019wm.shell.common.split.SplitWindowManager;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.stagesplit.SplitScreen;
import com.android.p019wm.shell.stagesplit.StageTaskListener;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.stagesplit.StageCoordinator */
class StageCoordinator implements SplitLayout.SplitLayoutHandler, RootTaskDisplayAreaOrganizer.RootTaskDisplayAreaListener, Transitions.TransitionHandler {
    private static final int NO_DISMISS = -2;
    /* access modifiers changed from: private */
    public static final String TAG = "StageCoordinator";
    private final Context mContext;
    private int mDismissTop;
    private DisplayAreaInfo mDisplayAreaInfo;
    /* access modifiers changed from: private */
    public final int mDisplayId;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private boolean mDividerVisible;
    private boolean mExitSplitScreenOnHide;
    private boolean mKeyguardOccluded;
    private final List<SplitScreen.SplitScreenListener> mListeners;
    private final SplitscreenEventLogger mLogger;
    /* access modifiers changed from: private */
    public final MainStage mMainStage;
    private final StageListenerImpl mMainStageListener;
    private final StageTaskUnfoldController mMainUnfoldController;
    private final Runnable mOnTransitionAnimationComplete;
    private final SplitWindowManager.ParentContainerCallbacks mParentContainerCallbacks;
    /* access modifiers changed from: private */
    public final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    private final SideStage mSideStage;
    private final StageListenerImpl mSideStageListener;
    private int mSideStagePosition;
    private final StageTaskUnfoldController mSideUnfoldController;
    private SplitLayout mSplitLayout;
    private final SplitScreenTransitions mSplitTransitions;
    private final SurfaceSession mSurfaceSession;
    /* access modifiers changed from: private */
    public final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private int mTopStageAfterFoldDismiss;
    private boolean mUseLegacySplit;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-stagesplit-StageCoordinator  reason: not valid java name */
    public /* synthetic */ void m3485lambda$new$0$comandroidwmshellstagesplitStageCoordinator() {
        if (!isSplitScreenVisible()) {
            setDividerVisibility(false);
            this.mSplitLayout.resetDividerPosition();
        }
        this.mDismissTop = -2;
    }

    StageCoordinator(Context context, int i, SyncTransactionQueue syncTransactionQueue, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellTaskOrganizer shellTaskOrganizer, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, SplitscreenEventLogger splitscreenEventLogger, Provider<Optional<StageTaskUnfoldController>> provider) {
        Context context2 = context;
        int i2 = i;
        RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer2 = rootTaskDisplayAreaOrganizer;
        DisplayInsetsController displayInsetsController2 = displayInsetsController;
        Transitions transitions2 = transitions;
        SurfaceSession surfaceSession = new SurfaceSession();
        this.mSurfaceSession = surfaceSession;
        StageListenerImpl stageListenerImpl = new StageListenerImpl();
        this.mMainStageListener = stageListenerImpl;
        StageListenerImpl stageListenerImpl2 = new StageListenerImpl();
        this.mSideStageListener = stageListenerImpl2;
        this.mSideStagePosition = 1;
        this.mListeners = new ArrayList();
        this.mDismissTop = -2;
        this.mTopStageAfterFoldDismiss = -1;
        StageCoordinator$$ExternalSyntheticLambda0 stageCoordinator$$ExternalSyntheticLambda0 = new StageCoordinator$$ExternalSyntheticLambda0(this);
        this.mOnTransitionAnimationComplete = stageCoordinator$$ExternalSyntheticLambda0;
        this.mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
            public void attachToParentSurface(SurfaceControl.Builder builder) {
                StageCoordinator.this.mRootTDAOrganizer.attachToDisplayArea(StageCoordinator.this.mDisplayId, builder);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onLeashReady$0$com-android-wm-shell-stagesplit-StageCoordinator$1 */
            public /* synthetic */ void mo51051x9e0c9ebc(SurfaceControl.Transaction transaction) {
                StageCoordinator.this.applyDividerVisibility(transaction);
            }

            public void onLeashReady(SurfaceControl surfaceControl) {
                StageCoordinator.this.mSyncQueue.runInSync(new StageCoordinator$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mContext = context2;
        this.mDisplayId = i2;
        this.mSyncQueue = syncTransactionQueue;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer2;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mLogger = splitscreenEventLogger;
        StageTaskUnfoldController stageTaskUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mMainUnfoldController = stageTaskUnfoldController;
        StageTaskUnfoldController stageTaskUnfoldController2 = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mSideUnfoldController = stageTaskUnfoldController2;
        MainStage mainStage = r1;
        MainStage mainStage2 = new MainStage(shellTaskOrganizer, i, stageListenerImpl, syncTransactionQueue, surfaceSession, stageTaskUnfoldController);
        this.mMainStage = mainStage;
        SideStage sideStage = new SideStage(context, shellTaskOrganizer, i, stageListenerImpl2, syncTransactionQueue, surfaceSession, stageTaskUnfoldController2);
        this.mSideStage = sideStage;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController2;
        displayInsetsController2.addInsetsChangedListener(i2, sideStage);
        rootTaskDisplayAreaOrganizer2.registerListener(i2, this);
        ((DeviceStateManager) context2.getSystemService(DeviceStateManager.class)).registerCallback(shellTaskOrganizer.getExecutor(), new DeviceStateManager.FoldStateListener(context2, new StageCoordinator$$ExternalSyntheticLambda1(this)));
        Transitions transitions3 = transitions;
        this.mSplitTransitions = new SplitScreenTransitions(transactionPool, transitions3, stageCoordinator$$ExternalSyntheticLambda0);
        transitions3.addHandler(this);
    }

    StageCoordinator(Context context, int i, SyncTransactionQueue syncTransactionQueue, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellTaskOrganizer shellTaskOrganizer, MainStage mainStage, SideStage sideStage, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, SplitLayout splitLayout, Transitions transitions, TransactionPool transactionPool, SplitscreenEventLogger splitscreenEventLogger, Provider<Optional<StageTaskUnfoldController>> provider) {
        Transitions transitions2 = transitions;
        this.mSurfaceSession = new SurfaceSession();
        this.mMainStageListener = new StageListenerImpl();
        this.mSideStageListener = new StageListenerImpl();
        this.mSideStagePosition = 1;
        this.mListeners = new ArrayList();
        this.mDismissTop = -2;
        this.mTopStageAfterFoldDismiss = -1;
        StageCoordinator$$ExternalSyntheticLambda0 stageCoordinator$$ExternalSyntheticLambda0 = new StageCoordinator$$ExternalSyntheticLambda0(this);
        this.mOnTransitionAnimationComplete = stageCoordinator$$ExternalSyntheticLambda0;
        this.mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
            public void attachToParentSurface(SurfaceControl.Builder builder) {
                StageCoordinator.this.mRootTDAOrganizer.attachToDisplayArea(StageCoordinator.this.mDisplayId, builder);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onLeashReady$0$com-android-wm-shell-stagesplit-StageCoordinator$1 */
            public /* synthetic */ void mo51051x9e0c9ebc(SurfaceControl.Transaction transaction) {
                StageCoordinator.this.applyDividerVisibility(transaction);
            }

            public void onLeashReady(SurfaceControl surfaceControl) {
                StageCoordinator.this.mSyncQueue.runInSync(new StageCoordinator$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mContext = context;
        this.mDisplayId = i;
        this.mSyncQueue = syncTransactionQueue;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainStage = mainStage;
        this.mSideStage = sideStage;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        rootTaskDisplayAreaOrganizer.registerListener(i, this);
        this.mSplitLayout = splitLayout;
        this.mSplitTransitions = new SplitScreenTransitions(transactionPool, transitions2, stageCoordinator$$ExternalSyntheticLambda0);
        this.mMainUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mSideUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mLogger = splitscreenEventLogger;
        transitions2.addHandler(this);
    }

    /* access modifiers changed from: package-private */
    public SplitScreenTransitions getSplitTransitions() {
        return this.mSplitTransitions;
    }

    /* access modifiers changed from: package-private */
    public boolean isSplitScreenVisible() {
        return this.mSideStageListener.mVisible && this.mMainStageListener.mVisible;
    }

    /* access modifiers changed from: package-private */
    public boolean moveToSideStage(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        setSideStagePosition(i, windowContainerTransaction);
        this.mMainStage.activate(getMainStageBounds(), windowContainerTransaction);
        this.mSideStage.addTask(runningTaskInfo, getSideStageBounds(), windowContainerTransaction);
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda2(this));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$moveToSideStage$1$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51026xe9506c92(SurfaceControl.Transaction transaction) {
        updateSurfaceBounds((SplitLayout) null, transaction, false);
    }

    /* access modifiers changed from: package-private */
    public boolean removeFromSideStage(int i) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        boolean removeTask = this.mSideStage.removeTask(i, this.mMainStage.isActive() ? this.mMainStage.mRootTaskInfo.token : null, windowContainerTransaction);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        return removeTask;
    }

    /* access modifiers changed from: package-private */
    public void setSideStageOutline(boolean z) {
        this.mSideStage.enableOutline(z);
    }

    /* access modifiers changed from: package-private */
    public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        setSideStagePosition(i3, windowContainerTransaction);
        this.mMainStage.activate(getMainStageBounds(), windowContainerTransaction);
        this.mSideStage.setBounds(getSideStageBounds(), windowContainerTransaction);
        addActivityOptions(bundle, this.mMainStage);
        addActivityOptions(bundle2, this.mSideStage);
        windowContainerTransaction.startTask(i, bundle);
        windowContainerTransaction.startTask(i2, bundle2);
        this.mSplitTransitions.startEnterTransition(16, windowContainerTransaction, remoteTransition, this);
    }

    /* access modifiers changed from: package-private */
    public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, final RemoteAnimationAdapter remoteAnimationAdapter) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        RemoteAnimationAdapter remoteAnimationAdapter2 = new RemoteAnimationAdapter(new IRemoteAnimationRunner.Stub() {
            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
                int length = remoteAnimationTargetArr3.length + 1;
                RemoteAnimationTarget[] remoteAnimationTargetArr4 = new RemoteAnimationTarget[length];
                for (int i2 = 0; i2 < remoteAnimationTargetArr3.length; i2++) {
                    remoteAnimationTargetArr4[i2] = remoteAnimationTargetArr3[i2];
                }
                remoteAnimationTargetArr4[length - 1] = StageCoordinator.this.getDividerBarLegacyTarget();
                try {
                    ActivityTaskManager.getService().setRunningRemoteTransitionDelegate(remoteAnimationAdapter.getCallingApplication());
                    remoteAnimationAdapter.getRunner().onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
                } catch (RemoteException e) {
                    Slog.e(StageCoordinator.TAG, "Error starting remote animation", e);
                }
            }

            public void onAnimationCancelled(boolean z) {
                try {
                    remoteAnimationAdapter.getRunner().onAnimationCancelled(z);
                } catch (RemoteException e) {
                    Slog.e(StageCoordinator.TAG, "Error starting remote animation", e);
                }
            }
        }, remoteAnimationAdapter.getDuration(), remoteAnimationAdapter.getStatusBarTransitionDelay());
        if (bundle == null) {
            bundle = ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter2).toBundle();
        } else {
            ActivityOptions.fromBundle(bundle).update(ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter2));
        }
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        setSideStagePosition(i3, windowContainerTransaction);
        this.mMainStage.activate(getMainStageBounds(), windowContainerTransaction);
        this.mSideStage.setBounds(getSideStageBounds(), windowContainerTransaction);
        addActivityOptions(bundle, this.mMainStage);
        addActivityOptions(bundle2, this.mSideStage);
        windowContainerTransaction.startTask(i, bundle);
        windowContainerTransaction.startTask(i2, bundle2);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
    }

    public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle, RemoteTransition remoteTransition) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.sendPendingIntent(pendingIntent, intent, resolveStartStage(i, i2, bundle, windowContainerTransaction));
        this.mSplitTransitions.startEnterTransition(17, windowContainerTransaction, remoteTransition, this);
    }

    /* access modifiers changed from: package-private */
    public Bundle resolveStartStage(int i, int i2, Bundle bundle, WindowContainerTransaction windowContainerTransaction) {
        int i3 = 0;
        if (i != -1) {
            if (i == 0) {
                if (i2 != -1) {
                    if (i2 == 0) {
                        i3 = 1;
                    }
                    setSideStagePosition(i3, windowContainerTransaction);
                } else {
                    i2 = getMainStagePosition();
                }
                if (bundle == null) {
                    bundle = new Bundle();
                }
                updateActivityOptions(bundle, i2);
                return bundle;
            } else if (i == 1) {
                if (i2 != -1) {
                    setSideStagePosition(i2, windowContainerTransaction);
                } else {
                    i2 = getSideStagePosition();
                }
                if (bundle == null) {
                    bundle = new Bundle();
                }
                updateActivityOptions(bundle, i2);
                return bundle;
            } else {
                throw new IllegalArgumentException("Unknown stage=" + i);
            }
        } else if (i2 == -1) {
            prepareExitSplitScreen(-1, windowContainerTransaction);
            return bundle;
        } else if (i2 == getSideStagePosition()) {
            return resolveStartStage(1, i2, bundle, windowContainerTransaction);
        } else {
            return resolveStartStage(0, i2, bundle, windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    public int getSideStagePosition() {
        return this.mSideStagePosition;
    }

    /* access modifiers changed from: package-private */
    public int getMainStagePosition() {
        return this.mSideStagePosition == 0 ? 1 : 0;
    }

    /* access modifiers changed from: package-private */
    public void setSideStagePosition(int i, WindowContainerTransaction windowContainerTransaction) {
        setSideStagePosition(i, true, windowContainerTransaction);
    }

    private void setSideStagePosition(int i, boolean z, WindowContainerTransaction windowContainerTransaction) {
        if (this.mSideStagePosition != i) {
            this.mSideStagePosition = i;
            sendOnStagePositionChanged();
            if (this.mSideStageListener.mVisible && z) {
                if (windowContainerTransaction == null) {
                    onLayoutSizeChanged(this.mSplitLayout);
                    return;
                }
                updateWindowBounds(this.mSplitLayout, windowContainerTransaction);
                updateUnfoldBounds();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setSideStageVisibility(boolean z) {
        if (this.mSideStageListener.mVisible != z) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mSideStage.setVisibility(z, windowContainerTransaction);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    public void onKeyguardOccludedChanged(boolean z) {
        this.mKeyguardOccluded = z;
    }

    /* access modifiers changed from: package-private */
    public void onKeyguardVisibilityChanged(boolean z) {
        int i;
        if (!z && this.mMainStage.isActive() && (i = this.mTopStageAfterFoldDismiss) != -1) {
            exitSplitScreen(i == 0 ? this.mMainStage : this.mSideStage, 5);
        }
    }

    /* access modifiers changed from: package-private */
    public void exitSplitScreenOnHide(boolean z) {
        this.mExitSplitScreenOnHide = z;
    }

    /* access modifiers changed from: package-private */
    public void exitSplitScreen(int i, int i2) {
        StageTaskListener stageTaskListener;
        if (this.mMainStage.containsTask(i)) {
            stageTaskListener = this.mMainStage;
        } else {
            stageTaskListener = this.mSideStage.containsTask(i) ? this.mSideStage : null;
        }
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (stageTaskListener != null) {
            stageTaskListener.reorderChild(i, true, windowContainerTransaction);
        }
        applyExitSplitScreen(stageTaskListener, windowContainerTransaction, i2);
    }

    /* access modifiers changed from: private */
    public void exitSplitScreen(StageTaskListener stageTaskListener, int i) {
        applyExitSplitScreen(stageTaskListener, new WindowContainerTransaction(), i);
    }

    private void applyExitSplitScreen(StageTaskListener stageTaskListener, WindowContainerTransaction windowContainerTransaction, int i) {
        SideStage sideStage = this.mSideStage;
        boolean z = true;
        sideStage.removeAllTasks(windowContainerTransaction, stageTaskListener == sideStage);
        MainStage mainStage = this.mMainStage;
        mainStage.deactivate(windowContainerTransaction, stageTaskListener == mainStage);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda7(this));
        setDividerVisibility(false);
        this.mSplitLayout.resetDividerPosition();
        this.mTopStageAfterFoldDismiss = -1;
        if (stageTaskListener != null) {
            if (stageTaskListener != this.mMainStage) {
                z = false;
            }
            logExitToStage(i, z);
            return;
        }
        logExit(i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyExitSplitScreen$2$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51025x6440dab8(SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(this.mMainStage.mRootLeash, (Rect) null).setWindowCrop(this.mSideStage.mRootLeash, (Rect) null);
    }

    /* access modifiers changed from: package-private */
    public void prepareExitSplitScreen(int i, WindowContainerTransaction windowContainerTransaction) {
        boolean z = false;
        this.mSideStage.removeAllTasks(windowContainerTransaction, i == 1);
        MainStage mainStage = this.mMainStage;
        if (i == 0) {
            z = true;
        }
        mainStage.deactivate(windowContainerTransaction, z);
    }

    /* access modifiers changed from: package-private */
    public void getStageBounds(Rect rect, Rect rect2) {
        rect.set(this.mSplitLayout.getBounds1());
        rect2.set(this.mSplitLayout.getBounds2());
    }

    private void addActivityOptions(Bundle bundle, StageTaskListener stageTaskListener) {
        bundle.putParcelable("android.activity.launchRootTaskToken", stageTaskListener.mRootTaskInfo.token);
    }

    /* access modifiers changed from: package-private */
    public void updateActivityOptions(Bundle bundle, int i) {
        addActivityOptions(bundle, i == this.mSideStagePosition ? this.mSideStage : this.mMainStage);
    }

    /* access modifiers changed from: package-private */
    public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        if (!this.mListeners.contains(splitScreenListener)) {
            this.mListeners.add(splitScreenListener);
            sendStatusToListener(splitScreenListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mListeners.remove((Object) splitScreenListener);
    }

    /* access modifiers changed from: package-private */
    public void sendStatusToListener(SplitScreen.SplitScreenListener splitScreenListener) {
        splitScreenListener.onStagePositionChanged(0, getMainStagePosition());
        splitScreenListener.onStagePositionChanged(1, getSideStagePosition());
        splitScreenListener.onSplitVisibilityChanged(isSplitScreenVisible());
        this.mSideStage.onSplitScreenListenerRegistered(splitScreenListener, 1);
        this.mMainStage.onSplitScreenListenerRegistered(splitScreenListener, 0);
    }

    private void sendOnStagePositionChanged() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            SplitScreen.SplitScreenListener splitScreenListener = this.mListeners.get(size);
            splitScreenListener.onStagePositionChanged(0, getMainStagePosition());
            splitScreenListener.onStagePositionChanged(1, getSideStagePosition());
        }
    }

    /* access modifiers changed from: private */
    public void onStageChildTaskStatusChanged(StageListenerImpl stageListenerImpl, int i, boolean z, boolean z2) {
        int i2 = z ? stageListenerImpl == this.mSideStageListener ? 1 : 0 : -1;
        if (i2 == 0) {
            this.mLogger.logMainStageAppChange(getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
        } else {
            this.mLogger.logSideStageAppChange(getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
        }
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onTaskStageChanged(i, i2, z2);
        }
    }

    private void sendSplitVisibilityChanged() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onSplitVisibilityChanged(this.mDividerVisible);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mMainUnfoldController;
        if (stageTaskUnfoldController != null && this.mSideUnfoldController != null) {
            stageTaskUnfoldController.onSplitVisibilityChanged(this.mDividerVisible);
            this.mSideUnfoldController.onSplitVisibilityChanged(this.mDividerVisible);
        }
    }

    /* access modifiers changed from: private */
    public void onStageRootTaskAppeared(StageListenerImpl stageListenerImpl) {
        if (this.mMainStageListener.mHasRootTask && this.mSideStageListener.mHasRootTask) {
            this.mUseLegacySplit = this.mContext.getResources().getBoolean(17891814);
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.setAdjacentRoots(this.mMainStage.mRootTaskInfo.token, this.mSideStage.mRootTaskInfo.token, true);
            if (!this.mUseLegacySplit) {
                windowContainerTransaction.setLaunchAdjacentFlagRoot(this.mSideStage.mRootTaskInfo.token);
            }
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: private */
    public void onStageRootTaskVanished(StageListenerImpl stageListenerImpl) {
        if (stageListenerImpl == this.mMainStageListener || stageListenerImpl == this.mSideStageListener) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mMainStage.deactivate(windowContainerTransaction);
            if (!this.mUseLegacySplit) {
                windowContainerTransaction.clearLaunchAdjacentFlagRoot(this.mSideStage.mRootTaskInfo.token);
            }
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    private void setDividerVisibility(boolean z) {
        if (this.mDividerVisible != z) {
            this.mDividerVisible = z;
            if (z) {
                this.mSplitLayout.init();
                updateUnfoldBounds();
            } else {
                this.mSplitLayout.release();
            }
            sendSplitVisibilityChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onStageVisibilityChanged(StageListenerImpl stageListenerImpl) {
        boolean z = this.mSideStageListener.mVisible;
        boolean z2 = this.mMainStageListener.mVisible;
        boolean z3 = true;
        boolean z4 = z && z2;
        boolean z5 = !z && !z2;
        if (z != z2) {
            z3 = false;
        }
        if (z3) {
            setDividerVisibility(z4);
        }
        StageTaskListener stageTaskListener = null;
        if (z5) {
            if (this.mExitSplitScreenOnHide || (!this.mMainStage.mRootTaskInfo.isSleeping && !this.mSideStage.mRootTaskInfo.isSleeping)) {
                exitSplitScreen((StageTaskListener) null, 2);
            }
        } else if (this.mKeyguardOccluded) {
            if (z2) {
                stageTaskListener = this.mMainStage;
            } else if (z) {
                stageTaskListener = this.mSideStage;
            }
            exitSplitScreen(stageTaskListener, 4);
        }
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda6(this, z3, z4));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStageVisibilityChanged$3$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51031xba06c2ee(boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        if (z) {
            transaction.setVisibility(this.mSideStage.mRootLeash, z2).setVisibility(this.mMainStage.mRootLeash, z2);
            applyDividerVisibility(transaction);
            applyOutlineVisibility(transaction);
        }
    }

    /* access modifiers changed from: private */
    public void applyDividerVisibility(SurfaceControl.Transaction transaction) {
        SurfaceControl dividerLeash = this.mSplitLayout.getDividerLeash();
        if (dividerLeash != null) {
            if (this.mDividerVisible) {
                transaction.show(dividerLeash).setLayer(dividerLeash, Integer.MAX_VALUE).setPosition(dividerLeash, (float) this.mSplitLayout.getDividerBounds().left, (float) this.mSplitLayout.getDividerBounds().top);
            } else {
                transaction.hide(dividerLeash);
            }
        }
    }

    private void applyOutlineVisibility(SurfaceControl.Transaction transaction) {
        SurfaceControl outlineLeash = this.mSideStage.getOutlineLeash();
        if (outlineLeash != null) {
            if (this.mDividerVisible) {
                transaction.show(outlineLeash).setLayer(outlineLeash, Integer.MAX_VALUE);
            } else {
                transaction.hide(outlineLeash);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onStageHasChildrenChanged(StageListenerImpl stageListenerImpl) {
        boolean z = stageListenerImpl.mHasChildren;
        boolean z2 = stageListenerImpl == this.mSideStageListener;
        if (!z) {
            if (z2 && this.mMainStageListener.mVisible) {
                exitSplitScreen((StageTaskListener) this.mMainStage, 7);
            } else if (!z2 && this.mSideStageListener.mVisible) {
                exitSplitScreen((StageTaskListener) this.mSideStage, 7);
            }
        } else if (z2) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mMainStage.activate(getMainStageBounds(), windowContainerTransaction);
            this.mSideStage.setBounds(getSideStageBounds(), windowContainerTransaction);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
        if (!this.mLogger.hasStartedSession() && this.mMainStageListener.mHasChildren && this.mSideStageListener.mHasChildren) {
            this.mLogger.logEnter(this.mSplitLayout.getDividerPositionAsFraction(), getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
        }
    }

    /* access modifiers changed from: package-private */
    public IBinder onSnappedToDismissTransition(boolean z) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        prepareExitSplitScreen(z ^ true ? 1 : 0, windowContainerTransaction);
        return this.mSplitTransitions.startSnapToDismiss(windowContainerTransaction, this);
    }

    public void onSnappedToDismiss(boolean z) {
        boolean z2 = false;
        if (!z ? this.mSideStagePosition == 0 : this.mSideStagePosition == 1) {
            z2 = true;
        }
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            onSnappedToDismissTransition(z2);
        } else {
            exitSplitScreen(z2 ? this.mMainStage : this.mSideStage, 1);
        }
    }

    public void onDoubleTappedDivider() {
        setSideStagePosition(this.mSideStagePosition == 0 ? 1 : 0, (WindowContainerTransaction) null);
        this.mLogger.logSwap(getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutPositionChanging$4$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51028xd4a6cf71(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        updateSurfaceBounds(splitLayout, transaction, true);
    }

    public void onLayoutPositionChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda5(this, splitLayout));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutSizeChanging$5$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51030x500a14b8(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        updateSurfaceBounds(splitLayout, transaction, true);
    }

    public void onLayoutSizeChanging(SplitLayout splitLayout) {
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda3(this, splitLayout));
        this.mSideStage.setOutlineVisibility(false);
    }

    public void onLayoutSizeChanged(SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        updateWindowBounds(splitLayout, windowContainerTransaction);
        updateUnfoldBounds();
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda4(this, splitLayout));
        this.mSideStage.setOutlineVisibility(true);
        this.mLogger.logResize(this.mSplitLayout.getDividerPositionAsFraction());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutSizeChanged$6$com-android-wm-shell-stagesplit-StageCoordinator */
    public /* synthetic */ void mo51029x2642199e(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        updateSurfaceBounds(splitLayout, transaction, false);
    }

    private void updateUnfoldBounds() {
        StageTaskUnfoldController stageTaskUnfoldController = this.mMainUnfoldController;
        if (stageTaskUnfoldController != null && this.mSideUnfoldController != null) {
            stageTaskUnfoldController.onLayoutChanged(getMainStageBounds());
            this.mSideUnfoldController.onLayoutChanged(getSideStageBounds());
        }
    }

    private void updateWindowBounds(SplitLayout splitLayout, WindowContainerTransaction windowContainerTransaction) {
        int i = this.mSideStagePosition;
        splitLayout.applyTaskChanges(windowContainerTransaction, (i == 0 ? this.mSideStage : this.mMainStage).mRootTaskInfo, (i == 0 ? this.mMainStage : this.mSideStage).mRootTaskInfo);
    }

    /* access modifiers changed from: package-private */
    public void updateSurfaceBounds(SplitLayout splitLayout, SurfaceControl.Transaction transaction, boolean z) {
        int i = this.mSideStagePosition;
        StageTaskListener stageTaskListener = i == 0 ? this.mSideStage : this.mMainStage;
        StageTaskListener stageTaskListener2 = i == 0 ? this.mMainStage : this.mSideStage;
        if (splitLayout == null) {
            splitLayout = this.mSplitLayout;
        }
        splitLayout.applySurfaceChanges(transaction, stageTaskListener.mRootLeash, stageTaskListener2.mRootLeash, stageTaskListener.mDimLayer, stageTaskListener2.mDimLayer, z);
    }

    public int getSplitItemPosition(WindowContainerToken windowContainerToken) {
        if (windowContainerToken == null) {
            return -1;
        }
        if (windowContainerToken.equals(this.mMainStage.mRootTaskInfo.getToken())) {
            return getMainStagePosition();
        }
        if (windowContainerToken.equals(this.mSideStage.mRootTaskInfo.getToken())) {
            return getSideStagePosition();
        }
        return -1;
    }

    public void setLayoutOffsetTarget(int i, int i2, SplitLayout splitLayout) {
        int i3 = this.mSideStagePosition;
        StageTaskListener stageTaskListener = i3 == 0 ? this.mSideStage : this.mMainStage;
        StageTaskListener stageTaskListener2 = i3 == 0 ? this.mMainStage : this.mSideStage;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        splitLayout.applyLayoutOffsetTarget(windowContainerTransaction, i, i2, stageTaskListener.mRootTaskInfo, stageTaskListener2.mRootTaskInfo);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo) {
        this.mDisplayAreaInfo = displayAreaInfo;
        if (this.mSplitLayout == null) {
            SplitLayout splitLayout = new SplitLayout(TAG + "SplitDivider", this.mContext, this.mDisplayAreaInfo.configuration, this, this.mParentContainerCallbacks, this.mDisplayImeController, this.mTaskOrganizer, 1);
            this.mSplitLayout = splitLayout;
            this.mDisplayInsetsController.addInsetsChangedListener(this.mDisplayId, splitLayout);
            StageTaskUnfoldController stageTaskUnfoldController = this.mMainUnfoldController;
            if (stageTaskUnfoldController != null && this.mSideUnfoldController != null) {
                stageTaskUnfoldController.init();
                this.mSideUnfoldController.init();
            }
        }
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        throw new IllegalStateException("Well that was unexpected...");
    }

    public void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        this.mDisplayAreaInfo = displayAreaInfo;
        SplitLayout splitLayout = this.mSplitLayout;
        if (splitLayout != null && splitLayout.updateConfiguration(displayAreaInfo.configuration) && this.mMainStage.isActive()) {
            onLayoutSizeChanged(this.mSplitLayout);
        }
    }

    /* access modifiers changed from: private */
    public void onFoldedStateChanged(boolean z) {
        this.mTopStageAfterFoldDismiss = -1;
        if (z) {
            if (this.mMainStage.isFocused()) {
                this.mTopStageAfterFoldDismiss = 0;
            } else if (this.mSideStage.isFocused()) {
                this.mTopStageAfterFoldDismiss = 1;
            }
        }
    }

    private Rect getSideStageBounds() {
        return this.mSideStagePosition == 0 ? this.mSplitLayout.getBounds1() : this.mSplitLayout.getBounds2();
    }

    private Rect getMainStageBounds() {
        return this.mSideStagePosition == 0 ? this.mSplitLayout.getBounds2() : this.mSplitLayout.getBounds1();
    }

    private StageTaskListener getStageOfTask(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mMainStage.mRootTaskInfo != null && runningTaskInfo.parentTaskId == this.mMainStage.mRootTaskInfo.taskId) {
            return this.mMainStage;
        }
        if (this.mSideStage.mRootTaskInfo == null || runningTaskInfo.parentTaskId != this.mSideStage.mRootTaskInfo.taskId) {
            return null;
        }
        return this.mSideStage;
    }

    private int getStageType(StageTaskListener stageTaskListener) {
        return stageTaskListener == this.mMainStage ? 0 : 1;
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        WindowContainerTransaction windowContainerTransaction = null;
        if (triggerTask != null) {
            int type = transitionRequestInfo.getType();
            if (isSplitScreenVisible()) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "  split is active so using splitTransition to handle request. triggerTask=%d type=%s mainChildren=%d sideChildren=%d", new Object[]{Integer.valueOf(triggerTask.taskId), WindowManager.transitTypeToString(type), Integer.valueOf(this.mMainStage.getChildCount()), Integer.valueOf(this.mSideStage.getChildCount())});
                windowContainerTransaction = new WindowContainerTransaction();
                StageTaskListener stageOfTask = getStageOfTask(triggerTask);
                if (stageOfTask != null) {
                    if (Transitions.isClosingType(type) && stageOfTask.getChildCount() == 1) {
                        this.mDismissTop = getStageType(stageOfTask) == 0 ? 1 : 0;
                    }
                } else if (triggerTask.getActivityType() == 2 && Transitions.isOpeningType(type)) {
                    this.mDismissTop = -1;
                }
                if (this.mDismissTop != -2) {
                    ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "  splitTransition  deduced Dismiss from request. toTop=%s", new Object[]{SplitScreen.stageTypeToString(this.mDismissTop)});
                    prepareExitSplitScreen(this.mDismissTop, windowContainerTransaction);
                    this.mSplitTransitions.mPendingDismiss = iBinder;
                }
            } else if ((type == 1 || type == 3) && getStageOfTask(triggerTask) != null) {
                throw new IllegalStateException("Entering split implicitly with only one task isn't supported.");
            }
            return windowContainerTransaction;
        } else if (isSplitScreenVisible()) {
            return new WindowContainerTransaction();
        } else {
            return null;
        }
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        boolean z;
        StageTaskListener stageOfTask;
        if (iBinder == this.mSplitTransitions.mPendingDismiss || iBinder == this.mSplitTransitions.mPendingEnter) {
            if (this.mSplitTransitions.mPendingEnter == iBinder) {
                z = startPendingEnterAnimation(iBinder, transitionInfo, transaction);
            } else {
                z = this.mSplitTransitions.mPendingDismiss == iBinder ? startPendingDismissAnimation(iBinder, transitionInfo, transaction) : true;
            }
            if (!z) {
                return false;
            }
            this.mSplitTransitions.playAnimation(iBinder, transitionInfo, transaction, transaction2, transitionFinishCallback, this.mMainStage.mRootTaskInfo.token, this.mSideStage.mRootTaskInfo.token);
            return true;
        } else if (!isSplitScreenVisible()) {
            return false;
        } else {
            for (int i = 0; i < transitionInfo.getChanges().size(); i++) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                ActivityManager.RunningTaskInfo taskInfo = change.getTaskInfo();
                if (!(taskInfo == null || !taskInfo.hasParentTask() || (stageOfTask = getStageOfTask(taskInfo)) == null)) {
                    if (Transitions.isOpeningType(change.getMode())) {
                        if (!stageOfTask.containsTask(taskInfo.taskId)) {
                            Log.w(TAG, "Expected onTaskAppeared on " + stageOfTask + " to have been called with " + taskInfo.taskId + " before startAnimation().");
                        }
                    } else if (Transitions.isClosingType(change.getMode()) && stageOfTask.containsTask(taskInfo.taskId)) {
                        Log.w(TAG, "Expected onTaskVanished on " + stageOfTask + " to have been called with " + taskInfo.taskId + " before startAnimation().");
                    }
                }
            }
            if (this.mMainStage.getChildCount() != 0 && this.mSideStage.getChildCount() != 0) {
                return false;
            }
            throw new IllegalStateException("Somehow removed the last task in a stage outside of a proper transition");
        }
    }

    private boolean startPendingEnterAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
        if (transitionInfo.getType() == 16) {
            TransitionInfo.Change change = null;
            TransitionInfo.Change change2 = null;
            for (int i = 0; i < transitionInfo.getChanges().size(); i++) {
                TransitionInfo.Change change3 = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                ActivityManager.RunningTaskInfo taskInfo = change3.getTaskInfo();
                if (taskInfo != null && taskInfo.hasParentTask()) {
                    int stageType = getStageType(getStageOfTask(taskInfo));
                    if (stageType == 0) {
                        change = change3;
                    } else if (stageType == 1) {
                        change2 = change3;
                    }
                }
            }
            if (change == null || change2 == null) {
                throw new IllegalStateException("Launched 2 tasks in split, but didn't receive 2 tasks in transition. Possibly one of them failed to launch");
            }
            setDividerVisibility(true);
            setSideStagePosition(1, false, (WindowContainerTransaction) null);
            setSplitsVisible(true);
            addDividerBarToTransition(transitionInfo, transaction, true);
            if (!this.mMainStage.containsTask(change.getTaskInfo().taskId)) {
                Log.w(TAG, "Expected onTaskAppeared on " + this.mMainStage + " to have been called with " + change.getTaskInfo().taskId + " before startAnimation().");
            }
            if (!this.mSideStage.containsTask(change2.getTaskInfo().taskId)) {
                Log.w(TAG, "Expected onTaskAppeared on " + this.mSideStage + " to have been called with " + change2.getTaskInfo().taskId + " before startAnimation().");
            }
            return true;
        }
        throw new RuntimeException("Unsupported split-entry");
    }

    private boolean startPendingDismissAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
        if (this.mMainStage.getChildCount() != 0) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < this.mMainStage.getChildCount()) {
                sb.append(i != 0 ? ", " : "");
                sb.append(this.mMainStage.mChildrenTaskInfo.keyAt(i));
                i++;
            }
            Log.w(TAG, "Expected onTaskVanished on " + this.mMainStage + " to have been called with [" + sb.toString() + "] before startAnimation().");
        }
        if (this.mSideStage.getChildCount() != 0) {
            StringBuilder sb2 = new StringBuilder();
            int i2 = 0;
            while (i2 < this.mSideStage.getChildCount()) {
                sb2.append(i2 != 0 ? ", " : "");
                sb2.append(this.mSideStage.mChildrenTaskInfo.keyAt(i2));
                i2++;
            }
            Log.w(TAG, "Expected onTaskVanished on " + this.mSideStage + " to have been called with [" + sb2.toString() + "] before startAnimation().");
        }
        setSplitsVisible(false);
        if (transitionInfo.getType() == 18) {
            transaction.setWindowCrop(this.mMainStage.mRootLeash, (Rect) null);
            transaction.setWindowCrop(this.mSideStage.mRootLeash, (Rect) null);
        }
        if (this.mDismissTop == -1) {
            transaction.hide(this.mSplitLayout.getDividerLeash());
            setDividerVisibility(false);
            this.mSplitTransitions.mPendingDismiss = null;
            return false;
        }
        addDividerBarToTransition(transitionInfo, transaction, false);
        return true;
    }

    private void addDividerBarToTransition(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, boolean z) {
        SurfaceControl dividerLeash = this.mSplitLayout.getDividerLeash();
        TransitionInfo.Change change = new TransitionInfo.Change((WindowContainerToken) null, dividerLeash);
        Rect dividerBounds = this.mSplitLayout.getDividerBounds();
        change.setStartAbsBounds(dividerBounds);
        change.setEndAbsBounds(dividerBounds);
        change.setMode(z ? 3 : 4);
        change.setFlags(256);
        transitionInfo.addChange(change);
        if (z) {
            transaction.setAlpha(dividerLeash, 1.0f);
            transaction.setLayer(dividerLeash, Integer.MAX_VALUE);
            transaction.setPosition(dividerLeash, (float) dividerBounds.left, (float) dividerBounds.top);
            transaction.show(dividerLeash);
        }
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationTarget getDividerBarLegacyTarget() {
        Rect dividerBounds = this.mSplitLayout.getDividerBounds();
        SurfaceControl dividerLeash = this.mSplitLayout.getDividerLeash();
        Point point = r0;
        Point point2 = new Point(0, 0);
        WindowConfiguration windowConfiguration = r0;
        WindowConfiguration windowConfiguration2 = new WindowConfiguration();
        return new RemoteAnimationTarget(-1, -1, dividerLeash, false, (Rect) null, (Rect) null, Integer.MAX_VALUE, point, dividerBounds, dividerBounds, windowConfiguration, true, (SurfaceControl) null, (Rect) null, (ActivityManager.RunningTaskInfo) null, false, 2034);
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationTarget getOutlineLegacyTarget() {
        Rect bounds = this.mSideStage.mRootTaskInfo.configuration.windowConfiguration.getBounds();
        SurfaceControl outlineLeash = this.mSideStage.getOutlineLeash();
        Point point = r0;
        Point point2 = new Point(0, 0);
        WindowConfiguration windowConfiguration = r0;
        WindowConfiguration windowConfiguration2 = new WindowConfiguration();
        return new RemoteAnimationTarget(-1, -1, outlineLeash, false, (Rect) null, (Rect) null, Integer.MAX_VALUE, point, bounds, bounds, windowConfiguration, true, (SurfaceControl) null, (Rect) null, (ActivityManager.RunningTaskInfo) null, false, 2034);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        String str3 = str2 + "  ";
        printWriter.println(str + TAG + " mDisplayId=" + this.mDisplayId);
        printWriter.println(str2 + "mDividerVisible=" + this.mDividerVisible);
        printWriter.println(str2 + "MainStage");
        printWriter.println(str3 + "isActive=" + this.mMainStage.isActive());
        this.mMainStageListener.dump(printWriter, str3);
        printWriter.println(str2 + "SideStage");
        this.mSideStageListener.dump(printWriter, str3);
        printWriter.println(str2 + "mSplitLayout=" + this.mSplitLayout);
    }

    private void setSplitsVisible(boolean z) {
        StageListenerImpl stageListenerImpl = this.mMainStageListener;
        this.mSideStageListener.mVisible = z;
        stageListenerImpl.mVisible = z;
        StageListenerImpl stageListenerImpl2 = this.mMainStageListener;
        this.mSideStageListener.mHasChildren = z;
        stageListenerImpl2.mHasChildren = z;
    }

    public void logOnDroppedToSplit(int i, InstanceId instanceId) {
        this.mLogger.enterRequestedByDrag(i, instanceId);
    }

    private void logExit(int i) {
        this.mLogger.logExit(i, -1, 0, -1, 0, this.mSplitLayout.isLandscape());
    }

    private void logExitToStage(int i, boolean z) {
        SplitscreenEventLogger splitscreenEventLogger = this.mLogger;
        int i2 = -1;
        int mainStagePosition = z ? getMainStagePosition() : -1;
        int topChildTaskUid = z ? this.mMainStage.getTopChildTaskUid() : 0;
        if (!z) {
            i2 = getSideStagePosition();
        }
        splitscreenEventLogger.logExit(i, mainStagePosition, topChildTaskUid, i2, !z ? this.mSideStage.getTopChildTaskUid() : 0, this.mSplitLayout.isLandscape());
    }

    /* renamed from: com.android.wm.shell.stagesplit.StageCoordinator$StageListenerImpl */
    class StageListenerImpl implements StageTaskListener.StageListenerCallbacks {
        boolean mHasChildren = false;
        boolean mHasRootTask = false;
        boolean mVisible = false;

        StageListenerImpl() {
        }

        public void onRootTaskAppeared() {
            this.mHasRootTask = true;
            StageCoordinator.this.onStageRootTaskAppeared(this);
        }

        public void onStatusChanged(boolean z, boolean z2) {
            if (this.mHasRootTask) {
                if (this.mHasChildren != z2) {
                    this.mHasChildren = z2;
                    StageCoordinator.this.onStageHasChildrenChanged(this);
                }
                if (this.mVisible != z) {
                    this.mVisible = z;
                    StageCoordinator.this.onStageVisibilityChanged(this);
                }
            }
        }

        public void onChildTaskStatusChanged(int i, boolean z, boolean z2) {
            StageCoordinator.this.onStageChildTaskStatusChanged(this, i, z, z2);
        }

        public void onRootTaskVanished() {
            reset();
            StageCoordinator.this.onStageRootTaskVanished(this);
        }

        public void onNoLongerSupportMultiWindow() {
            if (StageCoordinator.this.mMainStage.isActive()) {
                StageCoordinator.this.exitSplitScreen((StageTaskListener) null, 8);
            }
        }

        private void reset() {
            this.mHasRootTask = false;
            this.mVisible = false;
            this.mHasChildren = false;
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "mHasRootTask=" + this.mHasRootTask);
            printWriter.println(str + "mVisible=" + this.mVisible);
            printWriter.println(str + "mHasChildren=" + this.mHasChildren);
        }
    }
}
