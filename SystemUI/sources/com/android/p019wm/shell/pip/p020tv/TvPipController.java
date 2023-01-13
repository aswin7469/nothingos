package com.android.p019wm.shell.pip.p020tv;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.RemoteException;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TaskStackListenerCallback;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.pip.PinnedStackListenerForwarder;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsController;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import com.android.p019wm.shell.pip.p020tv.TvPipNotificationController;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* renamed from: com.android.wm.shell.pip.tv.TvPipController */
public class TvPipController implements PipTransitionController.PipTransitionCallback, TvPipBoundsController.PipBoundsListener, TvPipMenuController.Delegate, TvPipNotificationController.Delegate, DisplayController.OnDisplaysChangedListener {
    static final boolean DEBUG = false;
    private static final int NONEXISTENT_TASK_ID = -1;
    private static final int STATE_NO_PIP = 0;
    private static final int STATE_PIP = 1;
    private static final int STATE_PIP_MENU = 2;
    private static final String TAG = "TvPipController";
    /* access modifiers changed from: private */
    public final PipAppOpsListener mAppOpsListener;
    /* access modifiers changed from: private */
    public RemoteAction mCloseAction;
    private final Context mContext;
    private int mEduTextWindowExitAnimationDurationMs;
    private final TvPipImpl mImpl = new TvPipImpl();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private int mPinnedTaskId = -1;
    private int mPipForceCloseDelay;
    private final PipMediaController mPipMediaController;
    private final TvPipNotificationController mPipNotificationController;
    private final PipTaskOrganizer mPipTaskOrganizer;
    /* access modifiers changed from: private */
    public int mPreviousGravity = 85;
    private int mResizeAnimationDuration;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    public final TvPipBoundsAlgorithm mTvPipBoundsAlgorithm;
    private final TvPipBoundsController mTvPipBoundsController;
    /* access modifiers changed from: private */
    public final TvPipBoundsState mTvPipBoundsState;
    /* access modifiers changed from: private */
    public final TvPipMenuController mTvPipMenuController;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.pip.tv.TvPipController$State */
    public @interface State {
    }

    public static Pip create(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, PipTransitionController pipTransitionController, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        TvPipController tvPipController = r0;
        TvPipController tvPipController2 = new TvPipController(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, pipTransitionController, tvPipMenuController, pipMediaController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor);
        return tvPipController.mImpl;
    }

    private TvPipController(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, PipTransitionController pipTransitionController, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        TvPipMenuController tvPipMenuController2 = tvPipMenuController;
        TvPipNotificationController tvPipNotificationController2 = tvPipNotificationController;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mTvPipBoundsState = tvPipBoundsState;
        tvPipBoundsState.setDisplayId(context.getDisplayId());
        tvPipBoundsState.setDisplayLayout(new DisplayLayout(context, context.getDisplay()));
        this.mTvPipBoundsAlgorithm = tvPipBoundsAlgorithm;
        this.mTvPipBoundsController = tvPipBoundsController;
        tvPipBoundsController.setListener(this);
        this.mPipMediaController = pipMediaController;
        this.mPipNotificationController = tvPipNotificationController2;
        tvPipNotificationController2.setDelegate(this);
        this.mTvPipMenuController = tvPipMenuController2;
        tvPipMenuController2.setDelegate(this);
        this.mAppOpsListener = pipAppOpsListener;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        PipTransitionController pipTransitionController2 = pipTransitionController;
        pipTransitionController.registerPipTransitionCallback(this);
        loadConfigurations();
        registerPipParamsChangedListener(pipParamsChangedForwarder);
        registerTaskStackListenerCallback(taskStackListenerImpl);
        registerWmShellPinnedStackListener(windowManagerShellWrapper);
        displayController.addDisplayWindowListener(this);
    }

    /* access modifiers changed from: private */
    public void onConfigurationChanged(Configuration configuration) {
        if (isPipShown()) {
            closePip();
        }
        loadConfigurations();
        this.mPipNotificationController.onConfigurationChanged(this.mContext);
        this.mTvPipBoundsAlgorithm.onConfigurationChanged(this.mContext);
    }

    private boolean isPipShown() {
        return this.mState != 0;
    }

    public void showPictureInPictureMenu() {
        if (this.mState != 0) {
            setState(2);
            this.mTvPipMenuController.showMenu();
            updatePinnedStackBounds();
        }
    }

    public void onMenuClosed() {
        setState(1);
        updatePinnedStackBounds();
    }

    public void onInMoveModeChanged() {
        updatePinnedStackBounds();
    }

    public void movePipToFullscreen() {
        this.mPipTaskOrganizer.exitPip(this.mResizeAnimationDuration, false);
        onPipDisappeared();
    }

    public void togglePipExpansion() {
        boolean z = !this.mTvPipBoundsState.isTvPipExpanded();
        int updateGravityOnExpandToggled = this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(this.mPreviousGravity, z);
        if (updateGravityOnExpandToggled != 0) {
            this.mPreviousGravity = updateGravityOnExpandToggled;
        }
        this.mTvPipBoundsState.setTvPipManuallyCollapsed(!z);
        this.mTvPipBoundsState.setTvPipExpanded(z);
        this.mPipNotificationController.updateExpansionState();
        updatePinnedStackBounds();
    }

    public void enterPipMovementMenu() {
        setState(2);
        this.mTvPipMenuController.showMovementMenuOnly();
    }

    public void movePip(int i) {
        if (this.mTvPipBoundsAlgorithm.updateGravity(i)) {
            this.mTvPipMenuController.updateGravity(this.mTvPipBoundsState.getTvPipGravity());
            this.mPreviousGravity = 0;
            updatePinnedStackBounds();
        }
    }

    public int getPipGravity() {
        return this.mTvPipBoundsState.getTvPipGravity();
    }

    public int getOrientation() {
        return this.mTvPipBoundsState.getTvFixedPipOrientation();
    }

    public void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
        if (this.mTvPipBoundsState.getDisplayId() == i) {
            this.mTvPipBoundsState.setKeepClearAreas(set, set2);
            updatePinnedStackBounds(this.mResizeAnimationDuration, !Objects.equals(set2, this.mTvPipBoundsState.getUnrestrictedKeepClearAreas()));
        }
    }

    /* access modifiers changed from: private */
    public void updatePinnedStackBounds() {
        updatePinnedStackBounds(this.mResizeAnimationDuration, true);
    }

    private void updatePinnedStackBounds(int i, boolean z) {
        if (this.mState != 0) {
            boolean isInMoveMode = this.mTvPipMenuController.isInMoveMode();
            this.mTvPipBoundsController.recalculatePipBounds(isInMoveMode, this.mState == 2 || isInMoveMode, i, z);
        }
    }

    public void onPipTargetBoundsChange(Rect rect, int i) {
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, i, new TvPipController$$ExternalSyntheticLambda0(this));
        this.mTvPipMenuController.onPipTransitionStarted(rect);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPipTargetBoundsChange$0$com-android-wm-shell-pip-tv-TvPipController */
    public /* synthetic */ void mo50574x7753947f(Rect rect) {
        this.mTvPipMenuController.updateExpansionState();
    }

    public void closePip() {
        RemoteAction remoteAction = this.mCloseAction;
        if (remoteAction != null) {
            try {
                remoteAction.getActionIntent().send();
            } catch (PendingIntent.CanceledException e) {
                ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to send close action, %s", new Object[]{TAG, e});
            }
            this.mMainExecutor.executeDelayed(new TvPipController$$ExternalSyntheticLambda1(this), (long) this.mPipForceCloseDelay);
            return;
        }
        closeCurrentPiP(this.mPinnedTaskId);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$closePip$1$com-android-wm-shell-pip-tv-TvPipController  reason: not valid java name */
    public /* synthetic */ void m3483lambda$closePip$1$comandroidwmshellpiptvTvPipController() {
        closeCurrentPiP(this.mPinnedTaskId);
    }

    private void closeCurrentPiP(int i) {
        int i2 = this.mPinnedTaskId;
        if (i2 != i) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: PiP has already been closed by custom close action", new Object[]{TAG});
            return;
        }
        removeTask(i2);
        onPipDisappeared();
    }

    public void closeEduText() {
        updatePinnedStackBounds(this.mEduTextWindowExitAnimationDurationMs, false);
    }

    /* access modifiers changed from: private */
    public void registerSessionListenerForCurrentUser() {
        this.mPipMediaController.registerSessionListenerForCurrentUser();
    }

    /* access modifiers changed from: private */
    public void checkIfPinnedTaskAppeared() {
        TaskInfo pinnedTaskInfo = getPinnedTaskInfo();
        if (pinnedTaskInfo != null && pinnedTaskInfo.topActivity != null) {
            this.mPinnedTaskId = pinnedTaskInfo.taskId;
            this.mPipMediaController.onActivityPinned();
            this.mPipNotificationController.show(pinnedTaskInfo.topActivity.getPackageName());
        }
    }

    /* access modifiers changed from: private */
    public void checkIfPinnedTaskIsGone() {
        if (isPipShown() && getPinnedTaskInfo() == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Pinned task is gone.", new Object[]{TAG});
            onPipDisappeared();
        }
    }

    private void onPipDisappeared() {
        this.mPipNotificationController.dismiss();
        this.mTvPipMenuController.closeMenu();
        this.mTvPipBoundsState.resetTvPipState();
        this.mTvPipBoundsController.onPipDismissed();
        setState(0);
        this.mPinnedTaskId = -1;
    }

    public void onPipTransitionStarted(int i, Rect rect) {
        this.mTvPipMenuController.notifyPipAnimating(true);
    }

    public void onPipTransitionCanceled(int i) {
        this.mTvPipMenuController.notifyPipAnimating(false);
    }

    public void onPipTransitionFinished(int i) {
        if (PipAnimationController.isInPipDirection(i) && this.mState == 0) {
            setState(1);
        }
        this.mTvPipMenuController.notifyPipAnimating(false);
    }

    private void setState(int i) {
        this.mState = i;
    }

    private void loadConfigurations() {
        Resources resources = this.mContext.getResources();
        this.mResizeAnimationDuration = resources.getInteger(C3353R.integer.config_pipResizeAnimationDuration);
        this.mPipForceCloseDelay = resources.getInteger(C3353R.integer.config_pipForceCloseDelay);
        this.mEduTextWindowExitAnimationDurationMs = resources.getInteger(C3353R.integer.pip_edu_text_window_exit_animation_duration_ms);
    }

    private void registerTaskStackListenerCallback(TaskStackListenerImpl taskStackListenerImpl) {
        taskStackListenerImpl.addListener(new TaskStackListenerCallback() {
            public void onActivityPinned(String str, int i, int i2, int i3) {
                TvPipController.this.checkIfPinnedTaskAppeared();
                TvPipController.this.mAppOpsListener.onActivityPinned(str);
            }

            public void onActivityUnpinned() {
                TvPipController.this.mAppOpsListener.onActivityUnpinned();
            }

            public void onTaskStackChanged() {
                TvPipController.this.checkIfPinnedTaskIsGone();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (runningTaskInfo.getWindowingMode() == 2) {
                    TvPipController.this.movePipToFullscreen();
                }
            }
        });
    }

    private void registerPipParamsChangedListener(PipParamsChangedForwarder pipParamsChangedForwarder) {
        pipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onActionsChanged()", new Object[]{TvPipController.TAG});
                TvPipController.this.mTvPipMenuController.setAppActions(list, remoteAction);
                RemoteAction unused = TvPipController.this.mCloseAction = remoteAction;
            }

            public void onAspectRatioChanged(float f) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onAspectRatioChanged: %f", new Object[]{TvPipController.TAG, Float.valueOf(f)});
                TvPipController.this.mTvPipBoundsState.setAspectRatio(f);
                if (!TvPipController.this.mTvPipBoundsState.isTvPipExpanded()) {
                    TvPipController.this.updatePinnedStackBounds();
                }
            }

            public void onExpandedAspectRatioChanged(float f) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onExpandedAspectRatioChanged: %f", new Object[]{TvPipController.TAG, Float.valueOf(f)});
                TvPipController.this.mTvPipBoundsState.setDesiredTvExpandedAspectRatio(f, false);
                TvPipController.this.mTvPipMenuController.updateExpansionState();
                if (TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f != 0.0f) {
                    TvPipController.this.mTvPipBoundsAlgorithm.updateExpandedPipSize();
                    TvPipController.this.updatePinnedStackBounds();
                }
                if (TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f == 0.0f) {
                    int updateGravityOnExpandToggled = TvPipController.this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(TvPipController.this.mPreviousGravity, false);
                    if (updateGravityOnExpandToggled != 0) {
                        int unused = TvPipController.this.mPreviousGravity = updateGravityOnExpandToggled;
                    }
                    TvPipController.this.mTvPipBoundsState.setTvPipExpanded(false);
                    TvPipController.this.updatePinnedStackBounds();
                }
                if (!TvPipController.this.mTvPipBoundsState.isTvPipExpanded() && f != 0.0f && !TvPipController.this.mTvPipBoundsState.isTvPipManuallyCollapsed()) {
                    TvPipController.this.mTvPipBoundsAlgorithm.updateExpandedPipSize();
                    int updateGravityOnExpandToggled2 = TvPipController.this.mTvPipBoundsAlgorithm.updateGravityOnExpandToggled(TvPipController.this.mPreviousGravity, true);
                    if (updateGravityOnExpandToggled2 != 0) {
                        int unused2 = TvPipController.this.mPreviousGravity = updateGravityOnExpandToggled2;
                    }
                    TvPipController.this.mTvPipBoundsState.setTvPipExpanded(true);
                    TvPipController.this.updatePinnedStackBounds();
                }
            }
        });
    }

    private void registerWmShellPinnedStackListener(WindowManagerShellWrapper windowManagerShellWrapper) {
        try {
            windowManagerShellWrapper.addPinnedStackListener(new PinnedStackListenerForwarder.PinnedTaskListener() {
                public void onImeVisibilityChanged(boolean z, int i) {
                    if (z != TvPipController.this.mTvPipBoundsState.isImeShowing() || (z && i != TvPipController.this.mTvPipBoundsState.getImeHeight())) {
                        TvPipController.this.mTvPipBoundsState.setImeVisibility(z, i);
                        if (TvPipController.this.mState != 0) {
                            TvPipController.this.updatePinnedStackBounds();
                        }
                    }
                }
            });
        } catch (RemoteException e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to register pinned stack listener, %s", new Object[]{TAG, e});
        }
    }

    private static TaskInfo getPinnedTaskInfo() {
        try {
            return ActivityTaskManager.getService().getRootTaskInfo(2, 0);
        } catch (RemoteException e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: getRootTaskInfo() failed, %s", new Object[]{TAG, e});
            return null;
        }
    }

    private static void removeTask(int i) {
        try {
            ActivityTaskManager.getService().removeTask(i);
        } catch (Exception e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Atm.removeTask() failed, %s", new Object[]{TAG, e});
        }
    }

    private static String stateToName(int i) {
        if (i == 0) {
            return "NO_PIP";
        }
        if (i == 1) {
            return "PIP";
        }
        if (i == 2) {
            return "PIP_MENU";
        }
        throw new IllegalArgumentException("Unknown state " + i);
    }

    /* renamed from: com.android.wm.shell.pip.tv.TvPipController$TvPipImpl */
    private class TvPipImpl implements Pip {
        private TvPipImpl() {
        }

        public void onConfigurationChanged(Configuration configuration) {
            TvPipController.this.mMainExecutor.execute(new TvPipController$TvPipImpl$$ExternalSyntheticLambda1(this, configuration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConfigurationChanged$0$com-android-wm-shell-pip-tv-TvPipController$TvPipImpl */
        public /* synthetic */ void mo50581xe58cbed9(Configuration configuration) {
            TvPipController.this.onConfigurationChanged(configuration);
        }

        public void registerSessionListenerForCurrentUser() {
            TvPipController.this.mMainExecutor.execute(new TvPipController$TvPipImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSessionListenerForCurrentUser$1$com-android-wm-shell-pip-tv-TvPipController$TvPipImpl */
        public /* synthetic */ void mo50582xe10ecda5() {
            TvPipController.this.registerSessionListenerForCurrentUser();
        }
    }
}
