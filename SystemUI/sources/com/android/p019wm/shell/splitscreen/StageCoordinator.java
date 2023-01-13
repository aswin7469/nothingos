package com.android.p019wm.shell.splitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.Choreographer;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.InstanceId;
import com.android.internal.protolog.common.ProtoLog;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.split.SplitLayout;
import com.android.p019wm.shell.common.split.SplitWindowManager;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenTransitions;
import com.android.p019wm.shell.splitscreen.StageTaskListener;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.util.StagedSplitBounds;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator */
class StageCoordinator implements SplitLayout.SplitLayoutHandler, DisplayController.OnDisplaysChangedListener, Transitions.TransitionHandler, ShellTaskOrganizer.TaskListener {
    private static final long DELAYED_CHECK_DIVIDER_SHOW = 1000;
    /* access modifiers changed from: private */
    public static final String TAG = "StageCoordinator";
    private final Context mContext;
    private final DisplayController mDisplayController;
    private final int mDisplayId;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final DisplayLayout mDisplayLayout;
    /* access modifiers changed from: private */
    public ValueAnimator mDividerFadeInAnimator;
    private boolean mDividerVisible;
    private boolean mExitSplitScreenOnHide;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private boolean mIsDividerRemoteAnimating;
    private boolean mKeyguardShowing;
    private final List<SplitScreen.SplitScreenListener> mListeners;
    private final SplitscreenEventLogger mLogger;
    private final ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public final MainStage mMainStage;
    /* access modifiers changed from: private */
    public final StageListenerImpl mMainStageListener;
    private final StageTaskUnfoldController mMainUnfoldController;
    private final SplitWindowManager.ParentContainerCallbacks mParentContainerCallbacks;
    private final Optional<RecentTasksController> mRecentTasks;
    private boolean mResizingSplits;
    ActivityManager.RunningTaskInfo mRootTaskInfo;
    /* access modifiers changed from: private */
    public SurfaceControl mRootTaskLeash;
    private boolean mShouldUpdateRecents;
    /* access modifiers changed from: private */
    public final SideStage mSideStage;
    private final StageListenerImpl mSideStageListener;
    private int mSideStagePosition;
    private final StageTaskUnfoldController mSideUnfoldController;
    /* access modifiers changed from: private */
    public SplitLayout mSplitLayout;
    /* access modifiers changed from: private */
    public final SplitScreenTransitions mSplitTransitions;
    private final SurfaceSession mSurfaceSession;
    /* access modifiers changed from: private */
    public final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private int mTopStageAfterFoldDismiss;
    /* access modifiers changed from: private */
    public final TransactionPool mTransactionPool;

    private boolean shouldBreakPairedTaskInRecents(int i) {
        return i == 1 || i == 2 || i == 3 || i == 4 || i == 8 || i == 9;
    }

    StageCoordinator(Context context, int i, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, SplitscreenEventLogger splitscreenEventLogger, IconProvider iconProvider, ShellExecutor shellExecutor, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        Context context2 = context;
        int i2 = i;
        ShellTaskOrganizer shellTaskOrganizer2 = shellTaskOrganizer;
        DisplayController displayController2 = displayController;
        Transitions transitions2 = transitions;
        TransactionPool transactionPool2 = transactionPool;
        SurfaceSession surfaceSession = new SurfaceSession();
        this.mSurfaceSession = surfaceSession;
        StageListenerImpl stageListenerImpl = new StageListenerImpl();
        this.mMainStageListener = stageListenerImpl;
        StageListenerImpl stageListenerImpl2 = new StageListenerImpl();
        this.mSideStageListener = stageListenerImpl2;
        this.mSideStagePosition = 1;
        this.mListeners = new ArrayList();
        this.mTopStageAfterFoldDismiss = -1;
        this.mHandler = new Handler();
        this.mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
            public void attachToParentSurface(SurfaceControl.Builder builder) {
                builder.setParent(StageCoordinator.this.mRootTaskLeash);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onLeashReady$0$com-android-wm-shell-splitscreen-StageCoordinator$1 */
            public /* synthetic */ void mo50880x19a3c17a(SurfaceControl.Transaction transaction) {
                StageCoordinator.this.mo50853xb6a44c34(transaction);
            }

            public void onLeashReady(SurfaceControl surfaceControl) {
                StageCoordinator.this.mSyncQueue.runInSync(new StageCoordinator$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mContext = context2;
        this.mDisplayId = i2;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskOrganizer = shellTaskOrganizer2;
        this.mLogger = splitscreenEventLogger;
        this.mMainExecutor = shellExecutor;
        this.mRecentTasks = optional;
        StageTaskUnfoldController stageTaskUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mMainUnfoldController = stageTaskUnfoldController;
        StageTaskUnfoldController stageTaskUnfoldController2 = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mSideUnfoldController = stageTaskUnfoldController2;
        shellTaskOrganizer2.createRootTask(i2, 1, this);
        Context context3 = context;
        MainStage mainStage = r1;
        ShellTaskOrganizer shellTaskOrganizer3 = shellTaskOrganizer;
        StageTaskUnfoldController stageTaskUnfoldController3 = stageTaskUnfoldController2;
        int i3 = i;
        StageTaskUnfoldController stageTaskUnfoldController4 = stageTaskUnfoldController;
        SyncTransactionQueue syncTransactionQueue2 = syncTransactionQueue;
        StageListenerImpl stageListenerImpl3 = stageListenerImpl2;
        IconProvider iconProvider2 = iconProvider;
        MainStage mainStage2 = new MainStage(context3, shellTaskOrganizer3, i3, stageListenerImpl, syncTransactionQueue2, surfaceSession, iconProvider2, stageTaskUnfoldController4);
        this.mMainStage = mainStage;
        this.mSideStage = new SideStage(context3, shellTaskOrganizer3, i3, stageListenerImpl3, syncTransactionQueue2, surfaceSession, iconProvider2, stageTaskUnfoldController3);
        this.mDisplayController = displayController2;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransactionPool = transactionPool2;
        ((DeviceStateManager) context2.getSystemService(DeviceStateManager.class)).registerCallback(shellTaskOrganizer.getExecutor(), new DeviceStateManager.FoldStateListener(context2, new StageCoordinator$$ExternalSyntheticLambda1(this)));
        this.mSplitTransitions = new SplitScreenTransitions(transactionPool2, transitions2, new StageCoordinator$$ExternalSyntheticLambda15(this), this);
        displayController2.addDisplayWindowListener(this);
        this.mDisplayLayout = new DisplayLayout(displayController2.getDisplayLayout(i2));
        transitions2.addHandler(this);
    }

    StageCoordinator(Context context, int i, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer shellTaskOrganizer, MainStage mainStage, SideStage sideStage, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, SplitLayout splitLayout, Transitions transitions, TransactionPool transactionPool, SplitscreenEventLogger splitscreenEventLogger, ShellExecutor shellExecutor, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        Transitions transitions2 = transitions;
        TransactionPool transactionPool2 = transactionPool;
        this.mSurfaceSession = new SurfaceSession();
        this.mMainStageListener = new StageListenerImpl();
        this.mSideStageListener = new StageListenerImpl();
        this.mSideStagePosition = 1;
        this.mListeners = new ArrayList();
        this.mTopStageAfterFoldDismiss = -1;
        this.mHandler = new Handler();
        this.mParentContainerCallbacks = new SplitWindowManager.ParentContainerCallbacks() {
            public void attachToParentSurface(SurfaceControl.Builder builder) {
                builder.setParent(StageCoordinator.this.mRootTaskLeash);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onLeashReady$0$com-android-wm-shell-splitscreen-StageCoordinator$1 */
            public /* synthetic */ void mo50880x19a3c17a(SurfaceControl.Transaction transaction) {
                StageCoordinator.this.mo50853xb6a44c34(transaction);
            }

            public void onLeashReady(SurfaceControl surfaceControl) {
                StageCoordinator.this.mSyncQueue.runInSync(new StageCoordinator$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mContext = context;
        this.mDisplayId = i;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainStage = mainStage;
        this.mSideStage = sideStage;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransactionPool = transactionPool2;
        this.mSplitLayout = splitLayout;
        this.mSplitTransitions = new SplitScreenTransitions(transactionPool2, transitions2, new StageCoordinator$$ExternalSyntheticLambda15(this), this);
        this.mMainUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mSideUnfoldController = (StageTaskUnfoldController) provider.get().orElse(null);
        this.mLogger = splitscreenEventLogger;
        this.mMainExecutor = shellExecutor;
        this.mRecentTasks = optional;
        displayController.addDisplayWindowListener(this);
        this.mDisplayLayout = new DisplayLayout();
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
    public int getStageOfTask(int i) {
        if (this.mMainStage.containsTask(i)) {
            return 0;
        }
        return this.mSideStage.containsTask(i) ? 1 : -1;
    }

    /* access modifiers changed from: package-private */
    public boolean moveToStage(ActivityManager.RunningTaskInfo runningTaskInfo, int i, int i2, WindowContainerTransaction windowContainerTransaction) {
        StageTaskListener stageTaskListener;
        if (i == 0) {
            stageTaskListener = this.mMainStage;
            i2 = SplitLayout.reversePosition(i2);
        } else if (i == 1) {
            stageTaskListener = this.mSideStage;
        } else if (this.mMainStage.isActive()) {
            int i3 = this.mSideStagePosition;
            StageTaskListener stageTaskListener2 = i2 == i3 ? this.mSideStage : this.mMainStage;
            i2 = i3;
            stageTaskListener = stageTaskListener2;
        } else {
            stageTaskListener = this.mSideStage;
        }
        setSideStagePosition(i2, windowContainerTransaction);
        WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
        stageTaskListener.evictAllChildren(windowContainerTransaction2);
        stageTaskListener.addTask(runningTaskInfo, windowContainerTransaction);
        if (!windowContainerTransaction2.isEmpty()) {
            windowContainerTransaction.merge(windowContainerTransaction2, true);
        }
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            prepareEnterSplitScreen(windowContainerTransaction);
            this.mSplitTransitions.startEnterTransition(17, windowContainerTransaction, (RemoteTransition) null, this);
        } else {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean removeFromSideStage(int i) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        boolean removeTask = this.mSideStage.removeTask(i, this.mMainStage.isActive() ? this.mMainStage.mRootTaskInfo.token : null, windowContainerTransaction);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        return removeTask;
    }

    /* access modifiers changed from: package-private */
    public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        setSideStagePosition(i3, windowContainerTransaction);
        this.mSplitLayout.setDivideRatio(f);
        this.mMainStage.activate(windowContainerTransaction, false);
        updateWindowBounds(this.mSplitLayout, windowContainerTransaction);
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, true);
        addActivityOptions(bundle, this.mMainStage);
        addActivityOptions(bundle2, this.mSideStage);
        windowContainerTransaction.startTask(i, bundle);
        windowContainerTransaction.startTask(i2, bundle2);
        this.mSplitTransitions.startEnterTransition(16, windowContainerTransaction, remoteTransition, this);
    }

    /* access modifiers changed from: package-private */
    public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
        startWithLegacyTransition(i, i2, (PendingIntent) null, (Intent) null, bundle, bundle2, i3, f, remoteAnimationAdapter);
    }

    /* access modifiers changed from: package-private */
    public void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
        startWithLegacyTransition(i, -1, pendingIntent, intent, bundle, bundle2, i2, f, remoteAnimationAdapter);
    }

    private void startWithLegacyTransition(int i, int i2, PendingIntent pendingIntent, Intent intent, Bundle bundle, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
        Bundle bundle3;
        Bundle bundle4;
        final int i4 = i2;
        PendingIntent pendingIntent2 = pendingIntent;
        Intent intent2 = intent;
        boolean z = (pendingIntent2 == null || intent2 == null) ? false : true;
        this.mSplitLayout.init();
        this.mShouldUpdateRecents = false;
        this.mIsDividerRemoteAnimating = true;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        final WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
        prepareEvictChildTasks(0, windowContainerTransaction2);
        prepareEvictChildTasks(1, windowContainerTransaction2);
        final RemoteAnimationAdapter remoteAnimationAdapter2 = remoteAnimationAdapter;
        RemoteAnimationAdapter remoteAnimationAdapter3 = new RemoteAnimationAdapter(new IRemoteAnimationRunner.Stub() {
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0056 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onAnimationStart(int r10, android.view.RemoteAnimationTarget[] r11, android.view.RemoteAnimationTarget[] r12, android.view.RemoteAnimationTarget[] r13, final android.view.IRemoteAnimationFinishedCallback r14) {
                /*
                    r9 = this;
                    com.android.wm.shell.splitscreen.StageCoordinator r0 = com.android.p019wm.shell.splitscreen.StageCoordinator.this
                    android.os.Handler r0 = r0.mHandler
                    r1 = 0
                    r0.removeCallbacksAndMessages(r1)
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r0.<init>()
                    int r1 = r11.length
                    r2 = 0
                    r3 = r2
                L_0x0012:
                    if (r3 >= r1) goto L_0x0028
                    r4 = r11[r3]
                    int r5 = r4.mode
                    int r6 = r1
                    int r7 = r4.taskId
                    if (r6 != r7) goto L_0x0022
                    r6 = 2
                    if (r5 != r6) goto L_0x0022
                    goto L_0x0025
                L_0x0022:
                    r0.add(r4)
                L_0x0025:
                    int r3 = r3 + 1
                    goto L_0x0012
                L_0x0028:
                    int r11 = r13.length
                    int r11 = r11 + 1
                    android.view.RemoteAnimationTarget[] r7 = new android.view.RemoteAnimationTarget[r11]
                L_0x002d:
                    int r1 = r13.length
                    if (r2 >= r1) goto L_0x0037
                    r1 = r13[r2]
                    r7[r2] = r1
                    int r2 = r2 + 1
                    goto L_0x002d
                L_0x0037:
                    int r11 = r11 + -1
                    com.android.wm.shell.splitscreen.StageCoordinator r13 = com.android.p019wm.shell.splitscreen.StageCoordinator.this
                    android.view.RemoteAnimationTarget r13 = r13.getDividerBarLegacyTarget()
                    r7[r11] = r13
                    com.android.wm.shell.splitscreen.StageCoordinator$2$1 r8 = new com.android.wm.shell.splitscreen.StageCoordinator$2$1
                    r8.<init>(r14)
                    android.app.IActivityTaskManager r11 = android.app.ActivityTaskManager.getService()     // Catch:{ SecurityException -> 0x0056 }
                    android.view.RemoteAnimationAdapter r13 = r9     // Catch:{ SecurityException -> 0x0056 }
                    android.app.IApplicationThread r13 = r13.getCallingApplication()     // Catch:{ SecurityException -> 0x0056 }
                    r11.setRunningRemoteTransitionDelegate(r13)     // Catch:{ SecurityException -> 0x0056 }
                    goto L_0x005f
                L_0x0054:
                    r9 = move-exception
                    goto L_0x0078
                L_0x0056:
                    java.lang.String r11 = com.android.p019wm.shell.splitscreen.StageCoordinator.TAG     // Catch:{ RemoteException -> 0x0054 }
                    java.lang.String r13 = "Unable to boost animation thread. This should only happen during unit tests"
                    android.util.Slog.e(r11, r13)     // Catch:{ RemoteException -> 0x0054 }
                L_0x005f:
                    android.view.RemoteAnimationAdapter r9 = r9     // Catch:{ RemoteException -> 0x0054 }
                    android.view.IRemoteAnimationRunner r3 = r9.getRunner()     // Catch:{ RemoteException -> 0x0054 }
                    int r9 = r0.size()     // Catch:{ RemoteException -> 0x0054 }
                    android.view.RemoteAnimationTarget[] r9 = new android.view.RemoteAnimationTarget[r9]     // Catch:{ RemoteException -> 0x0054 }
                    java.lang.Object[] r9 = r0.toArray(r9)     // Catch:{ RemoteException -> 0x0054 }
                    r5 = r9
                    android.view.RemoteAnimationTarget[] r5 = (android.view.RemoteAnimationTarget[]) r5     // Catch:{ RemoteException -> 0x0054 }
                    r4 = r10
                    r6 = r12
                    r3.onAnimationStart(r4, r5, r6, r7, r8)     // Catch:{ RemoteException -> 0x0054 }
                    goto L_0x0081
                L_0x0078:
                    java.lang.String r10 = com.android.p019wm.shell.splitscreen.StageCoordinator.TAG
                    java.lang.String r11 = "Error starting remote animation"
                    android.util.Slog.e(r10, r11, r9)
                L_0x0081:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.splitscreen.StageCoordinator.C35772.onAnimationStart(int, android.view.RemoteAnimationTarget[], android.view.RemoteAnimationTarget[], android.view.RemoteAnimationTarget[], android.view.IRemoteAnimationFinishedCallback):void");
            }

            public void onAnimationCancelled(boolean z) {
                StageCoordinator.this.onRemoteAnimationFinishedOrCancelled(windowContainerTransaction2);
                try {
                    remoteAnimationAdapter2.getRunner().onAnimationCancelled(z);
                } catch (RemoteException e) {
                    Slog.e(StageCoordinator.TAG, "Error starting remote animation", e);
                }
            }
        }, remoteAnimationAdapter.getDuration(), remoteAnimationAdapter.getStatusBarTransitionDelay());
        if (bundle == null) {
            bundle3 = ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter3).toBundle();
        } else {
            ActivityOptions fromBundle = ActivityOptions.fromBundle(bundle);
            fromBundle.update(ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter3));
            bundle3 = fromBundle.toBundle();
        }
        if (bundle2 != null) {
            bundle4 = bundle2;
        } else {
            bundle4 = new Bundle();
        }
        setSideStagePosition(i3, windowContainerTransaction);
        this.mSplitLayout.setDivideRatio(f);
        if (!this.mMainStage.isActive()) {
            this.mMainStage.activate(windowContainerTransaction, false);
        }
        updateWindowBounds(this.mSplitLayout, windowContainerTransaction);
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, true);
        addActivityOptions(bundle3, this.mMainStage);
        addActivityOptions(bundle4, this.mSideStage);
        this.mHandler.postDelayed(new StageCoordinator$$ExternalSyntheticLambda12(this, windowContainerTransaction2), 1000);
        windowContainerTransaction.startTask(i, bundle3);
        if (z) {
            windowContainerTransaction.sendPendingIntent(pendingIntent2, intent2, bundle4);
        } else {
            windowContainerTransaction.startTask(i4, bundle4);
        }
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda13(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startWithLegacyTransition$1$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50854xfa2f69f5(WindowContainerTransaction windowContainerTransaction) {
        if (this.mDividerVisible) {
            onRemoteAnimationFinishedOrCancelled(windowContainerTransaction);
            this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda9(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startWithLegacyTransition$2$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50855x3dba87b6(SurfaceControl.Transaction transaction) {
        setDividerVisibility(true, transaction);
        updateSurfaceBounds(this.mSplitLayout, transaction, false);
    }

    /* access modifiers changed from: private */
    public void onRemoteAnimationFinishedOrCancelled(WindowContainerTransaction windowContainerTransaction) {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mIsDividerRemoteAnimating = false;
        this.mShouldUpdateRecents = true;
        if (this.mMainStage.getChildCount() == 0 || this.mSideStage.getChildCount() == 0) {
            this.mMainExecutor.execute(new StageCoordinator$$ExternalSyntheticLambda11(this));
        } else {
            this.mSyncQueue.queue(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onRemoteAnimationFinishedOrCancelled$3$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50848xacd38448() {
        exitSplitScreen(this.mMainStage.getChildCount() == 0 ? this.mSideStage : this.mMainStage, 0);
    }

    /* access modifiers changed from: package-private */
    public void prepareEvictChildTasks(int i, WindowContainerTransaction windowContainerTransaction) {
        if (i == this.mSideStagePosition) {
            this.mSideStage.evictAllChildren(windowContainerTransaction);
        } else {
            this.mMainStage.evictAllChildren(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    public void prepareEvictInvisibleChildTasks(WindowContainerTransaction windowContainerTransaction) {
        this.mMainStage.evictInvisibleChildren(windowContainerTransaction);
        this.mSideStage.evictInvisibleChildren(windowContainerTransaction);
    }

    /* access modifiers changed from: package-private */
    public Bundle resolveStartStage(int i, int i2, Bundle bundle, WindowContainerTransaction windowContainerTransaction) {
        int i3 = 1;
        if (i != -1) {
            if (i == 0) {
                if (i2 != -1) {
                    setSideStagePosition(SplitLayout.reversePosition(i2), windowContainerTransaction);
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
            Slog.w(TAG, "No stage type nor split position specified to resolve start stage");
            return bundle;
        } else if (!this.mMainStage.isActive()) {
            return resolveStartStage(1, i2, bundle, windowContainerTransaction);
        } else {
            if (i2 != this.mSideStagePosition) {
                i3 = 0;
            }
            return resolveStartStage(i3, i2, bundle, windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    public int getSideStagePosition() {
        return this.mSideStagePosition;
    }

    /* access modifiers changed from: package-private */
    public int getMainStagePosition() {
        return SplitLayout.reversePosition(this.mSideStagePosition);
    }

    /* access modifiers changed from: package-private */
    public int getTaskId(int i) {
        if (this.mSideStagePosition == i) {
            return this.mSideStage.getTopVisibleChildTaskId();
        }
        return this.mMainStage.getTopVisibleChildTaskId();
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
    public void onKeyguardVisibilityChanged(boolean z) {
        this.mKeyguardShowing = z;
        if (this.mMainStage.isActive()) {
            boolean z2 = this.mKeyguardShowing;
            if (z2 || this.mTopStageAfterFoldDismiss == -1) {
                setDividerVisibility(!z2, (SurfaceControl.Transaction) null);
            } else if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                prepareExitSplitScreen(this.mTopStageAfterFoldDismiss, windowContainerTransaction);
                this.mSplitTransitions.startDismissTransition((IBinder) null, windowContainerTransaction, this, this.mTopStageAfterFoldDismiss, 3);
            } else {
                exitSplitScreen(this.mTopStageAfterFoldDismiss == 0 ? this.mMainStage : this.mSideStage, 3);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onFinishedWakingUp() {
        if (this.mMainStage.isActive()) {
            boolean z = this.mMainStage.mRootTaskInfo.isVisible;
            if (!(this.mMainStage.mRootTaskInfo.isVisible != this.mSideStage.mRootTaskInfo.isVisible)) {
                return;
            }
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                exitSplitScreen(z ? this.mMainStage : this.mSideStage, 8);
                return;
            }
            boolean z2 = !z;
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            prepareExitSplitScreen(z2 ? 1 : 0, windowContainerTransaction);
            this.mSplitTransitions.startDismissTransition((IBinder) null, windowContainerTransaction, this, z2, 8);
        }
    }

    /* access modifiers changed from: package-private */
    public void exitSplitScreenOnHide(boolean z) {
        this.mExitSplitScreenOnHide = z;
    }

    /* access modifiers changed from: package-private */
    public void exitSplitScreen(int i, int i2) {
        StageTaskListener stageTaskListener;
        if (this.mMainStage.isActive()) {
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
    }

    /* access modifiers changed from: private */
    public void exitSplitScreen(StageTaskListener stageTaskListener, int i) {
        if (this.mMainStage.isActive()) {
            applyExitSplitScreen(stageTaskListener, new WindowContainerTransaction(), i);
        }
    }

    private void applyExitSplitScreen(StageTaskListener stageTaskListener, WindowContainerTransaction windowContainerTransaction, int i) {
        if (this.mMainStage.isActive()) {
            this.mRecentTasks.ifPresent(new StageCoordinator$$ExternalSyntheticLambda3(this, i));
            boolean z = false;
            this.mShouldUpdateRecents = false;
            boolean z2 = i == 9;
            SideStage sideStage = this.mSideStage;
            sideStage.removeAllTasks(windowContainerTransaction, !z2 && sideStage == stageTaskListener);
            MainStage mainStage = this.mMainStage;
            mainStage.deactivate(windowContainerTransaction, !z2 && mainStage == stageTaskListener);
            windowContainerTransaction.reorder(this.mRootTaskInfo.token, false);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda4(this));
            this.mSplitLayout.resetDividerPosition();
            this.mSplitLayout.release();
            this.mTopStageAfterFoldDismiss = -1;
            Slog.i(TAG, "applyExitSplitScreen, reason = " + SplitScreenController.exitReasonToString(i));
            if (stageTaskListener != null) {
                if (stageTaskListener == this.mMainStage) {
                    z = true;
                }
                logExitToStage(i, z);
                return;
            }
            logExit(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyExitSplitScreen$4$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50845xda58e806(int i, RecentTasksController recentTasksController) {
        if (shouldBreakPairedTaskInRecents(i) && this.mShouldUpdateRecents) {
            recentTasksController.removeSplitPair(this.mMainStage.getTopVisibleChildTaskId());
            recentTasksController.removeSplitPair(this.mSideStage.getTopVisibleChildTaskId());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyExitSplitScreen$5$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50846x1de405c7(SurfaceControl.Transaction transaction) {
        setResizingSplits(false);
        transaction.setWindowCrop(this.mMainStage.mRootLeash, (Rect) null).setWindowCrop(this.mSideStage.mRootLeash, (Rect) null);
        setDividerVisibility(false, transaction);
    }

    /* access modifiers changed from: private */
    public void prepareExitSplitScreen(int i, WindowContainerTransaction windowContainerTransaction) {
        if (this.mMainStage.isActive()) {
            boolean z = false;
            this.mSideStage.removeAllTasks(windowContainerTransaction, i == 1);
            MainStage mainStage = this.mMainStage;
            if (i == 0) {
                z = true;
            }
            mainStage.deactivate(windowContainerTransaction, z);
        }
    }

    private void prepareEnterSplitScreen(WindowContainerTransaction windowContainerTransaction) {
        prepareEnterSplitScreen(windowContainerTransaction, (ActivityManager.RunningTaskInfo) null, -1);
    }

    /* access modifiers changed from: package-private */
    public void prepareEnterSplitScreen(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        if (!this.mMainStage.isActive()) {
            if (runningTaskInfo != null) {
                setSideStagePosition(i, windowContainerTransaction);
                this.mSideStage.addTask(runningTaskInfo, windowContainerTransaction);
            }
            this.mMainStage.activate(windowContainerTransaction, true);
            updateWindowBounds(this.mSplitLayout, windowContainerTransaction);
            windowContainerTransaction.reorder(this.mRootTaskInfo.token, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void finishEnterSplitScreen(SurfaceControl.Transaction transaction) {
        this.mSplitLayout.init();
        setDividerVisibility(true, transaction);
        updateSurfaceBounds(this.mSplitLayout, transaction, false);
        setSplitsVisible(true);
        this.mShouldUpdateRecents = true;
        updateRecentTasksSplitPair();
        if (!this.mLogger.hasStartedSession()) {
            this.mLogger.logEnter(this.mSplitLayout.getDividerPositionAsFraction(), getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
        }
    }

    /* access modifiers changed from: package-private */
    public void getStageBounds(Rect rect, Rect rect2) {
        rect.set(this.mSplitLayout.getBounds1());
        rect2.set(this.mSplitLayout.getBounds2());
    }

    /* access modifiers changed from: package-private */
    public int getSplitPosition(int i) {
        if (this.mSideStage.getTopVisibleChildTaskId() == i) {
            return getSideStagePosition();
        }
        if (this.mMainStage.getTopVisibleChildTaskId() == i) {
            return getMainStagePosition();
        }
        return -1;
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
        if (z && z2) {
            updateRecentTasksSplitPair();
        }
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onTaskStageChanged(i, i2, z2);
        }
    }

    /* access modifiers changed from: private */
    public void onStageChildTaskEnterPip(StageListenerImpl stageListenerImpl, int i) {
        exitSplitScreen(stageListenerImpl == this.mMainStageListener ? this.mMainStage : this.mSideStage, 9);
    }

    private void updateRecentTasksSplitPair() {
        if (this.mShouldUpdateRecents) {
            this.mRecentTasks.ifPresent(new StageCoordinator$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateRecentTasksSplitPair$6$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50856xe3cae1b8(RecentTasksController recentTasksController) {
        int i;
        int i2;
        Rect bounds1 = this.mSplitLayout.getBounds1();
        Rect bounds2 = this.mSplitLayout.getBounds2();
        int topVisibleChildTaskId = this.mMainStage.getTopVisibleChildTaskId();
        int topVisibleChildTaskId2 = this.mSideStage.getTopVisibleChildTaskId();
        if (this.mSideStagePosition == 0) {
            i2 = topVisibleChildTaskId;
            i = topVisibleChildTaskId2;
        } else {
            i = topVisibleChildTaskId;
            i2 = topVisibleChildTaskId2;
        }
        StagedSplitBounds stagedSplitBounds = new StagedSplitBounds(bounds1, bounds2, i, i2);
        if (topVisibleChildTaskId != -1 && topVisibleChildTaskId2 != -1) {
            recentTasksController.addSplitPair(topVisibleChildTaskId, topVisibleChildTaskId2, stagedSplitBounds);
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
            updateUnfoldBounds();
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mRootTaskInfo != null || runningTaskInfo.hasParentTask()) {
            throw new IllegalArgumentException(this + "\n Unknown task appeared: " + runningTaskInfo);
        }
        this.mRootTaskInfo = runningTaskInfo;
        this.mRootTaskLeash = surfaceControl;
        if (this.mSplitLayout == null) {
            SplitLayout splitLayout = new SplitLayout(TAG + "SplitDivider", this.mContext, this.mRootTaskInfo.configuration, this, this.mParentContainerCallbacks, this.mDisplayImeController, this.mTaskOrganizer, 2);
            this.mSplitLayout = splitLayout;
            this.mDisplayInsetsController.addInsetsChangedListener(this.mDisplayId, splitLayout);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mMainUnfoldController;
        if (!(stageTaskUnfoldController == null || this.mSideUnfoldController == null)) {
            stageTaskUnfoldController.init();
            this.mSideUnfoldController.init();
        }
        onRootTaskAppeared();
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mRootTaskInfo;
        if (runningTaskInfo2 == null || runningTaskInfo2.taskId != runningTaskInfo.taskId) {
            throw new IllegalArgumentException(this + "\n Unknown task info changed: " + runningTaskInfo);
        }
        this.mRootTaskInfo = runningTaskInfo;
        SplitLayout splitLayout = this.mSplitLayout;
        if (splitLayout != null && splitLayout.updateConfiguration(runningTaskInfo.configuration) && this.mMainStage.isActive()) {
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                updateUnfoldBounds();
                return;
            }
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mIsDividerRemoteAnimating = false;
            this.mSplitLayout.update((SurfaceControl.Transaction) null);
            onLayoutSizeChanged(this.mSplitLayout);
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mRootTaskInfo != null) {
            onRootTaskVanished();
            SplitLayout splitLayout = this.mSplitLayout;
            if (splitLayout != null) {
                splitLayout.release();
                this.mSplitLayout = null;
            }
            this.mRootTaskInfo = null;
            return;
        }
        throw new IllegalArgumentException(this + "\n Unknown task vanished: " + runningTaskInfo);
    }

    /* access modifiers changed from: package-private */
    public void onRootTaskAppeared() {
        if (this.mRootTaskInfo != null && this.mMainStageListener.mHasRootTask && this.mSideStageListener.mHasRootTask) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.reparent(this.mMainStage.mRootTaskInfo.token, this.mRootTaskInfo.token, true);
            windowContainerTransaction.reparent(this.mSideStage.mRootTaskInfo.token, this.mRootTaskInfo.token, true);
            windowContainerTransaction.setAdjacentRoots(this.mMainStage.mRootTaskInfo.token, this.mSideStage.mRootTaskInfo.token, true);
            windowContainerTransaction.setLaunchAdjacentFlagRoot(this.mSideStage.mRootTaskInfo.token);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: private */
    public void onRootTaskVanished() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
        if (runningTaskInfo != null) {
            windowContainerTransaction.clearLaunchAdjacentFlagRoot(runningTaskInfo.token);
        }
        applyExitSplitScreen((StageTaskListener) null, windowContainerTransaction, 6);
        this.mDisplayInsetsController.removeInsetsChangedListener(this.mDisplayId, this.mSplitLayout);
    }

    /* access modifiers changed from: private */
    public void onStageVisibilityChanged(StageListenerImpl stageListenerImpl) {
        boolean z = this.mSideStageListener.mVisible;
        boolean z2 = this.mMainStageListener.mVisible;
        if (z2 == z) {
            if (!z2 && (this.mExitSplitScreenOnHide || (!this.mMainStage.mRootTaskInfo.isSleeping && !this.mSideStage.mRootTaskInfo.isSleeping))) {
                exitSplitScreen((StageTaskListener) null, 5);
            }
            this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda16(this, z, z2));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStageVisibilityChanged$7$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50850xc4664212(boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        transaction.setVisibility(this.mSideStage.mRootLeash, z).setVisibility(this.mMainStage.mRootLeash, z2);
        setDividerVisibility(z2, transaction);
    }

    private void setDividerVisibility(boolean z, SurfaceControl.Transaction transaction) {
        if (z != this.mDividerVisible) {
            ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_SPLIT_SCREEN;
            Object[] objArr = new Object[3];
            String str = TAG;
            objArr[0] = str;
            objArr[1] = z ? "show" : "hide";
            objArr[2] = Debug.getCaller();
            ProtoLog.d(shellProtoLogGroup, "%s: Request to %s divider bar from %s.", objArr);
            if (!z || !this.mKeyguardShowing) {
                this.mDividerVisible = z;
                sendSplitVisibilityChanged();
                if (this.mIsDividerRemoteAnimating) {
                    ProtoLog.d(ShellProtoLogGroup.WM_SHELL_SPLIT_SCREEN, "%s:   Skip animating divider bar due to it's remote animating.", new Object[]{str});
                } else if (transaction != null) {
                    mo50853xb6a44c34(transaction);
                } else {
                    this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda0(this));
                }
            } else {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_SPLIT_SCREEN, "%s:   Defer showing divider bar due to keyguard showing.", new Object[]{str});
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: applyDividerVisibility */
    public void mo50853xb6a44c34(SurfaceControl.Transaction transaction) {
        final SurfaceControl dividerLeash = this.mSplitLayout.getDividerLeash();
        if (dividerLeash == null) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_SPLIT_SCREEN, "%s:   Skip animating divider bar due to divider leash not ready.", new Object[]{TAG});
        } else if (this.mIsDividerRemoteAnimating) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_SPLIT_SCREEN, "%s:   Skip animating divider bar due to it's remote animating.", new Object[]{TAG});
        } else {
            ValueAnimator valueAnimator = this.mDividerFadeInAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mDividerFadeInAnimator.cancel();
            }
            if (this.mDividerVisible) {
                final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mDividerFadeInAnimator = ofFloat;
                ofFloat.addUpdateListener(new StageCoordinator$$ExternalSyntheticLambda10(this, dividerLeash, acquire));
                this.mDividerFadeInAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        SurfaceControl surfaceControl = dividerLeash;
                        if (surfaceControl == null || !surfaceControl.isValid()) {
                            StageCoordinator.this.mDividerFadeInAnimator.cancel();
                            return;
                        }
                        acquire.show(dividerLeash);
                        acquire.setAlpha(dividerLeash, 0.0f);
                        acquire.setLayer(dividerLeash, Integer.MAX_VALUE);
                        acquire.setPosition(dividerLeash, (float) StageCoordinator.this.mSplitLayout.getRefDividerBounds().left, (float) StageCoordinator.this.mSplitLayout.getRefDividerBounds().top);
                        acquire.apply();
                    }

                    public void onAnimationEnd(Animator animator) {
                        StageCoordinator.this.mTransactionPool.release(acquire);
                        ValueAnimator unused = StageCoordinator.this.mDividerFadeInAnimator = null;
                    }
                });
                this.mDividerFadeInAnimator.start();
                return;
            }
            transaction.hide(dividerLeash);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyDividerVisibility$9$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50844x9f9bec6e(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, ValueAnimator valueAnimator) {
        if (surfaceControl == null || !surfaceControl.isValid()) {
            this.mDividerFadeInAnimator.cancel();
            return;
        }
        transaction.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        transaction.setAlpha(surfaceControl, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public void onStageHasChildrenChanged(StageListenerImpl stageListenerImpl) {
        boolean z = stageListenerImpl.mHasChildren;
        boolean z2 = stageListenerImpl == this.mSideStageListener;
        if (!z) {
            if (z2 && this.mMainStageListener.mVisible) {
                exitSplitScreen((StageTaskListener) this.mMainStage, 2);
            } else if (!z2 && this.mSideStageListener.mVisible) {
                exitSplitScreen((StageTaskListener) this.mSideStage, 2);
            }
        } else if (z2 && !this.mMainStage.isActive()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mSplitLayout.init();
            prepareEnterSplitScreen(windowContainerTransaction);
            this.mSyncQueue.queue(windowContainerTransaction);
            this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda14(this));
        }
        if (this.mMainStageListener.mHasChildren && this.mSideStageListener.mHasChildren) {
            this.mShouldUpdateRecents = true;
            updateRecentTasksSplitPair();
            if (!this.mLogger.hasStartedSession()) {
                this.mLogger.logEnter(this.mSplitLayout.getDividerPositionAsFraction(), getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStageHasChildrenChanged$10$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50849xab7d7e25(SurfaceControl.Transaction transaction) {
        updateSurfaceBounds(this.mSplitLayout, transaction, false);
    }

    public void onSnappedToDismiss(boolean z) {
        int i = (!z ? this.mSideStagePosition != 0 : this.mSideStagePosition != 1) ? 0 : 1;
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            exitSplitScreen(i != 0 ? this.mMainStage : this.mSideStage, 4);
            return;
        }
        setResizingSplits(false);
        int i2 = i ^ 1;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        prepareExitSplitScreen(i2, windowContainerTransaction);
        this.mSplitTransitions.startDismissTransition((IBinder) null, windowContainerTransaction, this, i2, 4);
    }

    public void onDoubleTappedDivider() {
        setSideStagePosition(SplitLayout.reversePosition(this.mSideStagePosition), (WindowContainerTransaction) null);
        this.mLogger.logSwap(getMainStagePosition(), this.mMainStage.getTopChildTaskUid(), getSideStagePosition(), this.mSideStage.getTopChildTaskUid(), this.mSplitLayout.isLandscape());
    }

    public void onLayoutPositionChanging(SplitLayout splitLayout) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        acquire.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        updateSurfaceBounds(splitLayout, acquire, false);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public void onLayoutSizeChanging(SplitLayout splitLayout) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        acquire.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        setResizingSplits(true);
        updateSurfaceBounds(splitLayout, acquire, true);
        this.mMainStage.onResizing(getMainStageBounds(), acquire);
        this.mSideStage.onResizing(getSideStageBounds(), acquire);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public void onLayoutSizeChanged(SplitLayout splitLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        updateWindowBounds(splitLayout, windowContainerTransaction);
        updateUnfoldBounds();
        this.mSyncQueue.queue(windowContainerTransaction);
        this.mSyncQueue.runInSync(new StageCoordinator$$ExternalSyntheticLambda7(this, splitLayout));
        this.mLogger.logResize(this.mSplitLayout.getDividerPositionAsFraction());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayoutSizeChanged$11$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50847x85b35656(SplitLayout splitLayout, SurfaceControl.Transaction transaction) {
        setResizingSplits(false);
        updateSurfaceBounds(splitLayout, transaction, false);
        this.mMainStage.onResized(transaction);
        this.mSideStage.onResized(transaction);
    }

    private void updateUnfoldBounds() {
        StageTaskUnfoldController stageTaskUnfoldController = this.mMainUnfoldController;
        if (stageTaskUnfoldController != null && this.mSideUnfoldController != null) {
            stageTaskUnfoldController.onLayoutChanged(getMainStageBounds(), getMainStagePosition(), isLandscape());
            this.mSideUnfoldController.onLayoutChanged(getSideStageBounds(), getSideStagePosition(), isLandscape());
        }
    }

    private boolean isLandscape() {
        return this.mSplitLayout.isLandscape();
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

    /* access modifiers changed from: package-private */
    public void setResizingSplits(boolean z) {
        if (z != this.mResizingSplits) {
            try {
                ActivityTaskManager.getService().setSplitScreenResizing(z);
                this.mResizingSplits = z;
            } catch (RemoteException e) {
                Slog.w(TAG, "Error calling setSplitScreenResizing", e);
            }
        }
    }

    public int getSplitItemPosition(WindowContainerToken windowContainerToken) {
        if (windowContainerToken == null) {
            return -1;
        }
        if (this.mMainStage.containsToken(windowContainerToken)) {
            return getMainStagePosition();
        }
        if (this.mSideStage.containsToken(windowContainerToken)) {
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

    public void onDisplayAdded(int i) {
        if (i == 0) {
            this.mDisplayController.addDisplayChangingController(new StageCoordinator$$ExternalSyntheticLambda8(this));
        }
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        if (i == 0) {
            this.mDisplayLayout.set(this.mDisplayController.getDisplayLayout(i));
        }
    }

    /* access modifiers changed from: private */
    public void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (this.mMainStage.isActive() && Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mDisplayLayout.rotateTo(this.mContext.getResources(), i3);
            this.mSplitLayout.rotateTo(i3, this.mDisplayLayout.stableInsets());
            updateWindowBounds(this.mSplitLayout, windowContainerTransaction);
            updateUnfoldBounds();
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
        int activityType;
        IBinder iBinder2 = iBinder;
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        int i = 1;
        if (triggerTask == null) {
            if (!this.mMainStage.isActive()) {
                return null;
            }
            TransitionRequestInfo.DisplayChange displayChange = transitionRequestInfo.getDisplayChange();
            if (!(transitionRequestInfo.getType() != 6 || displayChange == null || displayChange.getStartRotation() == displayChange.getEndRotation())) {
                this.mSplitLayout.setFreezeDividerWindow(true);
            }
            return new WindowContainerTransaction();
        } else if (triggerTask.displayId != this.mDisplayId) {
            return null;
        } else {
            int type = transitionRequestInfo.getType();
            boolean isOpeningType = Transitions.isOpeningType(type);
            boolean z = triggerTask.getWindowingMode() == 1;
            if (isOpeningType && z) {
                this.mRecentTasks.ifPresent(new StageCoordinator$$ExternalSyntheticLambda5(triggerTask));
            }
            if (this.mMainStage.isActive()) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "  split is active so using splitTransition to handle request. triggerTask=%d type=%s mainChildren=%d sideChildren=%d", new Object[]{Integer.valueOf(triggerTask.taskId), WindowManager.transitTypeToString(type), Integer.valueOf(this.mMainStage.getChildCount()), Integer.valueOf(this.mSideStage.getChildCount())});
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                StageTaskListener stageOfTask = getStageOfTask(triggerTask);
                if (stageOfTask != null) {
                    if (Transitions.isClosingType(type) && stageOfTask.getChildCount() == 1) {
                        if (getStageType(stageOfTask) != 0) {
                            i = 0;
                        }
                        prepareExitSplitScreen(i, windowContainerTransaction);
                        this.mSplitTransitions.startDismissTransition(iBinder, windowContainerTransaction, this, i, 2);
                    }
                } else if (isOpeningType && z && (activityType = triggerTask.getActivityType()) != 4) {
                    if (activityType == 2 || activityType == 3) {
                        this.mSplitTransitions.startRecentTransition(iBinder, windowContainerTransaction, this, transitionRequestInfo.getRemoteTransition());
                    } else {
                        prepareExitSplitScreen(-1, windowContainerTransaction);
                        this.mSplitTransitions.startDismissTransition(iBinder, windowContainerTransaction, this, -1, 0);
                    }
                }
                return windowContainerTransaction;
            } else if (!isOpeningType || getStageOfTask(triggerTask) == null) {
                return null;
            } else {
                WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
                prepareEnterSplitScreen(windowContainerTransaction2);
                this.mSplitTransitions.mPendingEnter = iBinder2;
                return windowContainerTransaction2;
            }
        }
    }

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.mSplitTransitions.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, transitionFinishCallback);
    }

    public void onTransitionMerged(IBinder iBinder) {
        if (iBinder == this.mSplitTransitions.mPendingEnter) {
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            finishEnterSplitScreen(acquire);
            this.mSplitTransitions.mPendingEnter = null;
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x012b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x012c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startAnimation(android.os.IBinder r12, android.window.TransitionInfo r13, android.view.SurfaceControl.Transaction r14, android.view.SurfaceControl.Transaction r15, com.android.p019wm.shell.transition.Transitions.TransitionFinishCallback r16) {
        /*
            r11 = this;
            r0 = r11
            r1 = r12
            r3 = r14
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            android.os.IBinder r2 = r2.mPendingEnter
            r4 = 0
            if (r1 == r2) goto L_0x00f4
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            android.os.IBinder r2 = r2.mPendingRecent
            if (r1 == r2) goto L_0x00f4
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.SplitScreenTransitions$DismissTransition r2 = r2.mPendingDismiss
            if (r2 == 0) goto L_0x001e
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.SplitScreenTransitions$DismissTransition r2 = r2.mPendingDismiss
            android.os.IBinder r2 = r2.mTransition
            if (r2 == r1) goto L_0x00f4
        L_0x001e:
            com.android.wm.shell.splitscreen.MainStage r1 = r0.mMainStage
            boolean r1 = r1.isActive()
            if (r1 != 0) goto L_0x0027
            return r4
        L_0x0027:
            com.android.wm.shell.common.split.SplitLayout r1 = r0.mSplitLayout
            r1.setFreezeDividerWindow(r4)
            r1 = r4
        L_0x002d:
            java.util.List r2 = r13.getChanges()
            int r2 = r2.size()
            if (r1 >= r2) goto L_0x00db
            java.util.List r2 = r13.getChanges()
            java.lang.Object r2 = r2.get(r1)
            android.window.TransitionInfo$Change r2 = (android.window.TransitionInfo.Change) r2
            int r5 = r2.getMode()
            r6 = 6
            if (r5 != r6) goto L_0x0055
            int r5 = r2.getFlags()
            r5 = r5 & 32
            if (r5 == 0) goto L_0x0055
            com.android.wm.shell.common.split.SplitLayout r5 = r0.mSplitLayout
            r5.update(r14)
        L_0x0055:
            android.app.ActivityManager$RunningTaskInfo r5 = r2.getTaskInfo()
            if (r5 == 0) goto L_0x00d7
            boolean r6 = r5.hasParentTask()
            if (r6 != 0) goto L_0x0063
            goto L_0x00d7
        L_0x0063:
            com.android.wm.shell.splitscreen.StageTaskListener r6 = r11.getStageOfTask((android.app.ActivityManager.RunningTaskInfo) r5)
            if (r6 != 0) goto L_0x006a
            goto L_0x00d7
        L_0x006a:
            int r7 = r2.getMode()
            boolean r7 = com.android.p019wm.shell.transition.Transitions.isOpeningType(r7)
            java.lang.String r8 = " before startAnimation()."
            java.lang.String r9 = " to have been called with "
            if (r7 == 0) goto L_0x00a3
            int r2 = r5.taskId
            boolean r2 = r6.containsTask(r2)
            if (r2 != 0) goto L_0x00d7
            java.lang.String r2 = TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r10 = "Expected onTaskAppeared on "
            r7.<init>((java.lang.String) r10)
            java.lang.StringBuilder r6 = r7.append((java.lang.Object) r6)
            java.lang.StringBuilder r6 = r6.append((java.lang.String) r9)
            int r5 = r5.taskId
            java.lang.StringBuilder r5 = r6.append((int) r5)
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r8)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r2, r5)
            goto L_0x00d7
        L_0x00a3:
            int r2 = r2.getMode()
            boolean r2 = com.android.p019wm.shell.transition.Transitions.isClosingType(r2)
            if (r2 == 0) goto L_0x00d7
            int r2 = r5.taskId
            boolean r2 = r6.containsTask(r2)
            if (r2 == 0) goto L_0x00d7
            java.lang.String r2 = TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r10 = "Expected onTaskVanished on "
            r7.<init>((java.lang.String) r10)
            java.lang.StringBuilder r6 = r7.append((java.lang.Object) r6)
            java.lang.StringBuilder r6 = r6.append((java.lang.String) r9)
            int r5 = r5.taskId
            java.lang.StringBuilder r5 = r6.append((int) r5)
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r8)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r2, r5)
        L_0x00d7:
            int r1 = r1 + 1
            goto L_0x002d
        L_0x00db:
            com.android.wm.shell.splitscreen.MainStage r1 = r0.mMainStage
            int r1 = r1.getChildCount()
            if (r1 == 0) goto L_0x00ec
            com.android.wm.shell.splitscreen.SideStage r0 = r0.mSideStage
            int r0 = r0.getChildCount()
            if (r0 == 0) goto L_0x00ec
            return r4
        L_0x00ec:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Somehow removed the last task in a stage outside of a proper transition"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x00f4:
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            android.os.IBinder r2 = r2.mPendingEnter
            r8 = 1
            if (r2 != r1) goto L_0x0102
            boolean r2 = r11.startPendingEnterAnimation(r12, r13, r14)
        L_0x00ff:
            r5 = r13
            r6 = r15
            goto L_0x0129
        L_0x0102:
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            android.os.IBinder r2 = r2.mPendingRecent
            if (r2 != r1) goto L_0x010d
            boolean r2 = r11.startPendingRecentAnimation(r12, r13, r14)
            goto L_0x00ff
        L_0x010d:
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.SplitScreenTransitions$DismissTransition r2 = r2.mPendingDismiss
            if (r2 == 0) goto L_0x0126
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.SplitScreenTransitions$DismissTransition r2 = r2.mPendingDismiss
            android.os.IBinder r2 = r2.mTransition
            if (r2 != r1) goto L_0x0126
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.SplitScreenTransitions$DismissTransition r2 = r2.mPendingDismiss
            r5 = r13
            r6 = r15
            boolean r2 = r11.startPendingDismissAnimation(r2, r13, r14, r15)
            goto L_0x0129
        L_0x0126:
            r5 = r13
            r6 = r15
            r2 = r8
        L_0x0129:
            if (r2 != 0) goto L_0x012c
            return r4
        L_0x012c:
            com.android.wm.shell.splitscreen.SplitScreenTransitions r2 = r0.mSplitTransitions
            com.android.wm.shell.splitscreen.MainStage r4 = r0.mMainStage
            android.app.ActivityManager$RunningTaskInfo r4 = r4.mRootTaskInfo
            android.window.WindowContainerToken r7 = r4.token
            com.android.wm.shell.splitscreen.SideStage r0 = r0.mSideStage
            android.app.ActivityManager$RunningTaskInfo r0 = r0.mRootTaskInfo
            android.window.WindowContainerToken r9 = r0.token
            r0 = r2
            r1 = r12
            r2 = r13
            r3 = r14
            r4 = r15
            r5 = r16
            r6 = r7
            r7 = r9
            r0.playAnimation(r1, r2, r3, r4, r5, r6, r7)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.splitscreen.StageCoordinator.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    /* access modifiers changed from: package-private */
    public void onTransitionAnimationComplete() {
        if (!this.mMainStage.isActive()) {
            this.mSplitLayout.release();
            this.mSplitLayout.resetDividerPosition();
            this.mTopStageAfterFoldDismiss = -1;
        }
    }

    private boolean startPendingEnterAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
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
        if (transitionInfo.getType() == 17) {
            if (change == null && change2 == null) {
                throw new IllegalStateException("Launched a task in split, but didn't receive any task in transition.");
            }
        } else if (change == null || change2 == null) {
            throw new IllegalStateException("Launched 2 tasks in split, but didn't receive 2 tasks in transition. Possibly one of them failed to launch");
        }
        if (change != null && !this.mMainStage.containsTask(change.getTaskInfo().taskId)) {
            Log.w(TAG, "Expected onTaskAppeared on " + this.mMainStage + " to have been called with " + change.getTaskInfo().taskId + " before startAnimation().");
        }
        if (change2 != null && !this.mSideStage.containsTask(change2.getTaskInfo().taskId)) {
            Log.w(TAG, "Expected onTaskAppeared on " + this.mSideStage + " to have been called with " + change2.getTaskInfo().taskId + " before startAnimation().");
        }
        finishEnterSplitScreen(transaction);
        addDividerBarToTransition(transitionInfo, transaction, true);
        return true;
    }

    private boolean startPendingDismissAnimation(SplitScreenTransitions.DismissTransition dismissTransition, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
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
        this.mRecentTasks.ifPresent(new StageCoordinator$$ExternalSyntheticLambda6(this, dismissTransition, transitionInfo));
        this.mShouldUpdateRecents = false;
        setSplitsVisible(false);
        transaction.setWindowCrop(this.mMainStage.mRootLeash, (Rect) null);
        transaction.setWindowCrop(this.mSideStage.mRootLeash, (Rect) null);
        if (dismissTransition.mDismissTop == -1) {
            logExit(dismissTransition.mReason);
            this.mSplitLayout.release(transaction);
            this.mSplitTransitions.mPendingDismiss = null;
            return false;
        }
        logExitToStage(dismissTransition.mReason, dismissTransition.mDismissTop == 0);
        addDividerBarToTransition(transitionInfo, transaction, false);
        setDividerVisibility(false, transaction2);
        transaction2.hide(this.mMainStage.mDimLayer);
        transaction2.hide(this.mSideStage.mDimLayer);
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startPendingDismissAnimation$13$com-android-wm-shell-splitscreen-StageCoordinator */
    public /* synthetic */ void mo50852x482edfd9(SplitScreenTransitions.DismissTransition dismissTransition, TransitionInfo transitionInfo, RecentTasksController recentTasksController) {
        if (shouldBreakPairedTaskInRecents(dismissTransition.mReason) && this.mShouldUpdateRecents) {
            for (TransitionInfo.Change taskInfo : transitionInfo.getChanges()) {
                ActivityManager.RunningTaskInfo taskInfo2 = taskInfo.getTaskInfo();
                if (taskInfo2 != null && taskInfo2.getWindowingMode() == 1) {
                    recentTasksController.removeSplitPair(taskInfo2.taskId);
                }
            }
        }
    }

    private boolean startPendingRecentAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
        setDividerVisibility(false, transaction);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onRecentTransitionFinished(boolean z, WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        if (!this.mMainStage.isActive()) {
            setSplitsVisible(false);
        } else if (z) {
            prepareExitSplitScreen(-1, windowContainerTransaction);
            setSplitsVisible(false);
            logExit(5);
        } else {
            setDividerVisibility(true, transaction);
        }
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

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        String str3 = str2 + "  ";
        printWriter.println(str + TAG + " mDisplayId=" + this.mDisplayId);
        printWriter.println(str2 + "mDividerVisible=" + this.mDividerVisible);
        printWriter.println(str2 + "MainStage");
        printWriter.println(str3 + "stagePosition=" + getMainStagePosition());
        printWriter.println(str3 + "isActive=" + this.mMainStage.isActive());
        this.mMainStageListener.dump(printWriter, str3);
        printWriter.println(str2 + "SideStage");
        printWriter.println(str3 + "stagePosition=" + getSideStagePosition());
        this.mSideStageListener.dump(printWriter, str3);
        if (this.mMainStage.isActive()) {
            printWriter.println(str2 + "SplitLayout");
            this.mSplitLayout.dump(printWriter, str3);
        }
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

    /* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$StageListenerImpl */
    class StageListenerImpl implements StageTaskListener.StageListenerCallbacks {
        boolean mHasChildren = false;
        boolean mHasRootTask = false;
        boolean mVisible = false;

        StageListenerImpl() {
        }

        public void onRootTaskAppeared() {
            this.mHasRootTask = true;
            StageCoordinator.this.onRootTaskAppeared();
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

        public void onChildTaskEnterPip(int i) {
            StageCoordinator.this.onStageChildTaskEnterPip(this, i);
        }

        public void onRootTaskVanished() {
            reset();
            StageCoordinator.this.onRootTaskVanished();
        }

        public void onNoLongerSupportMultiWindow() {
            if (StageCoordinator.this.mMainStage.isActive()) {
                int i = StageCoordinator.this.mMainStageListener == this ? 1 : 0;
                if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                    StageCoordinator stageCoordinator = StageCoordinator.this;
                    stageCoordinator.exitSplitScreen(i != 0 ? stageCoordinator.mMainStage : stageCoordinator.mSideStage, 1);
                    return;
                }
                int i2 = i ^ 1;
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                StageCoordinator.this.prepareExitSplitScreen(i2, windowContainerTransaction);
                StageCoordinator.this.mSplitTransitions.startDismissTransition((IBinder) null, windowContainerTransaction, StageCoordinator.this, i2, 1);
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
