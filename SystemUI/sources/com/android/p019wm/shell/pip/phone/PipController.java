package com.android.p019wm.shell.pip.phone;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.app.StatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Size;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
import android.window.WindowContainerTransaction;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.common.DisplayChangeController;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SingleInstanceRemoteListener;
import com.android.p019wm.shell.common.TaskStackListenerCallback;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.p019wm.shell.pip.IPip;
import com.android.p019wm.shell.pip.IPipAnimationListener;
import com.android.p019wm.shell.pip.PinnedStackListenerForwarder;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipUtils;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.phone.PipController */
public class PipController implements PipTransitionController.PipTransitionCallback, RemoteCallable<PipController> {
    private static final String TAG = "PipController";
    /* access modifiers changed from: private */
    public PipAppOpsListener mAppOpsListener;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public DisplayController mDisplayController;
    final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener = new DisplayController.OnDisplaysChangedListener() {
        public void onFixedRotationStarted(int i, int i2) {
            boolean unused = PipController.this.mIsInFixedRotation = true;
        }

        public void onFixedRotationFinished(int i) {
            boolean unused = PipController.this.mIsInFixedRotation = false;
        }

        public void onDisplayAdded(int i) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), false);
            }
        }

        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), true);
            }
        }

        public void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
            if (PipController.this.mPipBoundsState.getDisplayId() == i) {
                PipController.this.mPipBoundsState.setKeepClearAreas(set, set2);
            }
        }
    };
    /* access modifiers changed from: private */
    public final int mEnterAnimationDuration;
    protected final PipImpl mImpl;
    /* access modifiers changed from: private */
    public boolean mIsInFixedRotation;
    private boolean mIsKeyguardShowingOrAnimating;
    protected ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public PipMediaController mMediaController;
    protected PhonePipMenuController mMenuController;
    private Optional<OneHandedController> mOneHandedController;
    private PipAnimationListener mPinnedStackAnimationRecentsCallback;
    protected PinnedStackListenerForwarder.PinnedTaskListener mPinnedTaskListener = new PipControllerPinnedTaskListener();
    /* access modifiers changed from: private */
    public PipBoundsAlgorithm mPipBoundsAlgorithm;
    /* access modifiers changed from: private */
    public PipBoundsState mPipBoundsState;
    /* access modifiers changed from: private */
    public PipInputConsumer mPipInputConsumer;
    private PipParamsChangedForwarder mPipParamsChangedForwarder;
    protected PipTaskOrganizer mPipTaskOrganizer;
    private PipTransitionController mPipTransitionController;
    private final DisplayChangeController.OnDisplayChangingListener mRotationController = new PipController$$ExternalSyntheticLambda7(this);
    private TaskStackListenerImpl mTaskStackListener;
    private final Rect mTmpInsetBounds = new Rect();
    /* access modifiers changed from: private */
    public PipTouchHandler mTouchHandler;
    private WindowManagerShellWrapper mWindowManagerShellWrapper;

    /* renamed from: com.android.wm.shell.pip.phone.PipController$PipAnimationListener */
    private interface PipAnimationListener {
        void onExpandPip();

        void onPipAnimationStarted();

        void onPipResourceDimensionsChanged(int i, int i2);
    }

    private String getTransitionTag(int i) {
        switch (i) {
            case 2:
                return "TRANSITION_TO_PIP";
            case 3:
                return "TRANSITION_LEAVE_PIP";
            case 4:
                return "TRANSITION_LEAVE_PIP_TO_SPLIT_SCREEN";
            case 5:
                return "TRANSITION_REMOVE_STACK";
            case 6:
                return "TRANSITION_SNAP_AFTER_RESIZE";
            case 7:
                return "TRANSITION_USER_RESIZE";
            case 8:
                return "TRANSITION_EXPAND_OR_UNEXPAND";
            default:
                return "TRANSITION_LEAVE_UNKNOWN";
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-pip-phone-PipController  reason: not valid java name */
    public /* synthetic */ void m3466lambda$new$0$comandroidwmshellpipphonePipController(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (!this.mPipTransitionController.handleRotateDisplay(i2, i3, windowContainerTransaction)) {
            if (this.mPipBoundsState.getDisplayLayout().rotation() == i3) {
                updateMovementBounds((Rect) null, false, false, false, windowContainerTransaction);
            } else if (!this.mPipTaskOrganizer.isInPip() || this.mPipTaskOrganizer.isEntryScheduled()) {
                onDisplayRotationChangedNotInPip(this.mContext, i3);
                updateMovementBounds(this.mPipBoundsState.getNormalBounds(), true, false, false, windowContainerTransaction);
                this.mPipTaskOrganizer.onDisplayRotationSkipped();
            } else {
                Rect currentOrAnimatingBounds = this.mPipTaskOrganizer.getCurrentOrAnimatingBounds();
                Rect rect = new Rect();
                if (onDisplayRotationChanged(this.mContext, rect, currentOrAnimatingBounds, this.mTmpInsetBounds, i, i2, i3, windowContainerTransaction)) {
                    this.mTouchHandler.adjustBoundsForRotation(rect, this.mPipBoundsState.getBounds(), this.mTmpInsetBounds);
                    if (!this.mIsInFixedRotation) {
                        this.mPipBoundsState.setShelfVisibility(false, 0, false);
                        this.mPipBoundsState.setImeVisibility(false, 0);
                        this.mTouchHandler.onShelfVisibilityChanged(false, 0);
                        this.mTouchHandler.onImeVisibilityChanged(false, 0);
                    }
                    updateMovementBounds(rect, true, false, false, windowContainerTransaction);
                }
            }
        }
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipController$PipControllerPinnedTaskListener */
    private class PipControllerPinnedTaskListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        private PipControllerPinnedTaskListener() {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            PipController.this.mPipBoundsState.setImeVisibility(z, i);
            PipController.this.mTouchHandler.onImeVisibilityChanged(z, i);
        }

        public void onMovementBoundsChanged(boolean z) {
            PipController.this.updateMovementBounds((Rect) null, false, z, false, (WindowContainerTransaction) null);
        }

        public void onActivityHidden(ComponentName componentName) {
            if (componentName.equals(PipController.this.mPipBoundsState.getLastPipComponentName())) {
                PipController.this.mPipBoundsState.setLastPipComponentName((ComponentName) null);
            }
        }
    }

    public static Pip create(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (context.getPackageManager().hasSystemFeature("android.software.picture_in_picture")) {
            return new PipController(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, pipParamsChangedForwarder, optional, shellExecutor).mImpl;
        }
        ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Device doesn't support Pip feature", new Object[]{TAG});
        return null;
    }

    protected PipController(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (UserManager.get(context).getProcessUserId() == 0) {
            this.mContext = context;
            this.mImpl = new PipImpl();
            this.mWindowManagerShellWrapper = windowManagerShellWrapper;
            this.mDisplayController = displayController;
            this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
            this.mPipBoundsState = pipBoundsState;
            this.mPipTaskOrganizer = pipTaskOrganizer;
            this.mMainExecutor = shellExecutor;
            this.mMediaController = pipMediaController;
            this.mMenuController = phonePipMenuController;
            this.mTouchHandler = pipTouchHandler;
            this.mAppOpsListener = pipAppOpsListener;
            this.mOneHandedController = optional;
            this.mPipTransitionController = pipTransitionController;
            this.mTaskStackListener = taskStackListenerImpl;
            this.mEnterAnimationDuration = this.mContext.getResources().getInteger(C3353R.integer.config_pipEnterAnimationDuration);
            this.mPipParamsChangedForwarder = pipParamsChangedForwarder;
            this.mMainExecutor.execute(new PipController$$ExternalSyntheticLambda8(this));
            return;
        }
        throw new IllegalStateException("Non-primary Pip component not currently supported.");
    }

    public void init() {
        this.mPipInputConsumer = new PipInputConsumer(WindowManagerGlobal.getWindowManagerService(), "pip_input_consumer", this.mMainExecutor);
        this.mPipTransitionController.registerPipTransitionCallback(this);
        this.mPipTaskOrganizer.registerOnDisplayIdChangeCallback(new PipController$$ExternalSyntheticLambda1(this));
        this.mPipBoundsState.setOnMinimalSizeChangeCallback(new PipController$$ExternalSyntheticLambda2(this));
        this.mPipBoundsState.setOnShelfVisibilityChangeCallback(new PipController$$ExternalSyntheticLambda3(this));
        PipTouchHandler pipTouchHandler = this.mTouchHandler;
        if (pipTouchHandler != null) {
            PipInputConsumer pipInputConsumer = this.mPipInputConsumer;
            Objects.requireNonNull(pipTouchHandler);
            pipInputConsumer.setInputListener(new PipController$$ExternalSyntheticLambda4(pipTouchHandler));
            PipInputConsumer pipInputConsumer2 = this.mPipInputConsumer;
            PipTouchHandler pipTouchHandler2 = this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler2);
            pipInputConsumer2.setRegistrationListener(new PipController$$ExternalSyntheticLambda5(pipTouchHandler2));
        }
        this.mDisplayController.addDisplayChangingController(this.mRotationController);
        this.mDisplayController.addDisplayWindowListener(this.mDisplaysChangedListener);
        this.mPipBoundsState.setDisplayId(this.mContext.getDisplayId());
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Context context = this.mContext;
        pipBoundsState.setDisplayLayout(new DisplayLayout(context, context.getDisplay()));
        try {
            this.mWindowManagerShellWrapper.addPinnedStackListener(this.mPinnedTaskListener);
        } catch (RemoteException e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to register pinned stack listener, %s", new Object[]{TAG, e});
        }
        try {
            if (ActivityTaskManager.getService().getRootTaskInfo(2, 0) != null) {
                this.mPipInputConsumer.registerInputConsumer();
            }
        } catch (RemoteException | UnsupportedOperationException e2) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to register pinned stack listener, %s", new Object[]{TAG, e2});
            e2.printStackTrace();
        }
        this.mTaskStackListener.addListener(new TaskStackListenerCallback() {
            public void onActivityPinned(String str, int i, int i2, int i3) {
                PipController.this.mTouchHandler.onActivityPinned();
                PipController.this.mMediaController.onActivityPinned();
                PipController.this.mAppOpsListener.onActivityPinned(str);
                PipController.this.mPipInputConsumer.registerInputConsumer();
            }

            public void onActivityUnpinned() {
                PipController.this.mTouchHandler.onActivityUnpinned((ComponentName) PipUtils.getTopPipActivity(PipController.this.mContext).first);
                PipController.this.mAppOpsListener.onActivityUnpinned();
                PipController.this.mPipInputConsumer.unregisterInputConsumer();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (runningTaskInfo.getWindowingMode() == 2) {
                    PipController.this.mTouchHandler.getMotionHelper().expandLeavePip(z2);
                }
            }
        });
        this.mPipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onAspectRatioChanged(float f) {
                PipController.this.mPipBoundsState.setAspectRatio(f);
                Rect adjustedDestinationBounds = PipController.this.mPipBoundsAlgorithm.getAdjustedDestinationBounds(PipController.this.mPipBoundsState.getBounds(), PipController.this.mPipBoundsState.getAspectRatio());
                Objects.requireNonNull(adjustedDestinationBounds, "Missing destination bounds");
                PipController.this.mPipTaskOrganizer.scheduleAnimateResizePip(adjustedDestinationBounds, PipController.this.mEnterAnimationDuration, (Consumer<Rect>) null);
                PipController.this.mTouchHandler.onAspectRatioChanged();
                PipController.this.updateMovementBounds((Rect) null, false, false, false, (WindowContainerTransaction) null);
            }

            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                PipController.this.mMenuController.setAppActions(list, remoteAction);
            }
        });
        this.mOneHandedController.ifPresent(new PipController$$ExternalSyntheticLambda6(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$1$com-android-wm-shell-pip-phone-PipController  reason: not valid java name */
    public /* synthetic */ void m3462lambda$init$1$comandroidwmshellpipphonePipController(int i) {
        this.mPipBoundsState.setDisplayId(i);
        onDisplayChanged(this.mDisplayController.getDisplayLayout(i), false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$2$com-android-wm-shell-pip-phone-PipController  reason: not valid java name */
    public /* synthetic */ void m3463lambda$init$2$comandroidwmshellpipphonePipController() {
        updateMovementBounds((Rect) null, false, false, false, (WindowContainerTransaction) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$3$com-android-wm-shell-pip-phone-PipController  reason: not valid java name */
    public /* synthetic */ void m3464lambda$init$3$comandroidwmshellpipphonePipController(Boolean bool, Integer num, Boolean bool2) {
        this.mTouchHandler.onShelfVisibilityChanged(bool.booleanValue(), num.intValue());
        if (bool2.booleanValue()) {
            updateMovementBounds(this.mPipBoundsState.getBounds(), false, false, true, (WindowContainerTransaction) null);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$4$com-android-wm-shell-pip-phone-PipController  reason: not valid java name */
    public /* synthetic */ void m3465lambda$init$4$comandroidwmshellpipphonePipController(OneHandedController oneHandedController) {
        oneHandedController.asOneHanded().registerTransitionCallback(new OneHandedTransitionCallback() {
            public void onStartFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }

            public void onStopFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }
        });
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    /* access modifiers changed from: private */
    public void onConfigurationChanged(Configuration configuration) {
        this.mPipBoundsAlgorithm.onConfigurationChanged(this.mContext);
        this.mTouchHandler.onConfigurationChanged();
        this.mPipBoundsState.onConfigurationChanged();
    }

    /* access modifiers changed from: private */
    public void onDensityOrFontScaleChanged() {
        this.mPipTaskOrganizer.onDensityOrFontScaleChanged(this.mContext);
        onPipResourceDimensionsChanged();
    }

    /* access modifiers changed from: private */
    public void onOverlayChanged() {
        this.mTouchHandler.onOverlayChanged();
        Context context = this.mContext;
        onDisplayChanged(new DisplayLayout(context, context.getDisplay()), false);
    }

    /* access modifiers changed from: private */
    public void onDisplayChanged(DisplayLayout displayLayout, boolean z) {
        if (!this.mPipBoundsState.getDisplayLayout().isSameGeometry(displayLayout)) {
            PipController$$ExternalSyntheticLambda0 pipController$$ExternalSyntheticLambda0 = new PipController$$ExternalSyntheticLambda0(this, displayLayout);
            if (!this.mPipTaskOrganizer.isInPip() || !z) {
                pipController$$ExternalSyntheticLambda0.run();
                return;
            }
            this.mMenuController.attachPipMenuView();
            PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
            Rect rect = new Rect(this.mPipBoundsState.getBounds());
            float snapFraction = snapAlgorithm.getSnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect), this.mPipBoundsState.getStashedState());
            pipController$$ExternalSyntheticLambda0.run();
            snapAlgorithm.applySnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
            this.mTouchHandler.getMotionHelper().movePip(rect);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDisplayChanged$5$com-android-wm-shell-pip-phone-PipController */
    public /* synthetic */ void mo50331xd33eb319(DisplayLayout displayLayout) {
        boolean z = Transitions.ENABLE_SHELL_TRANSITIONS && this.mPipBoundsState.getDisplayLayout().rotation() != displayLayout.rotation();
        this.mPipBoundsState.setDisplayLayout(displayLayout);
        WindowContainerTransaction windowContainerTransaction = z ? new WindowContainerTransaction() : null;
        updateMovementBounds((Rect) null, z, false, false, windowContainerTransaction);
        if (windowContainerTransaction != null) {
            this.mPipTaskOrganizer.applyFinishBoundsResize(windowContainerTransaction, 1, false);
        }
    }

    /* access modifiers changed from: private */
    public void registerSessionListenerForCurrentUser() {
        this.mMediaController.registerSessionListenerForCurrentUser();
    }

    /* access modifiers changed from: private */
    public void onSystemUiStateChanged(boolean z, int i) {
        this.mTouchHandler.onSystemUiStateChanged(z);
    }

    public void expandPip() {
        this.mTouchHandler.getMotionHelper().expandLeavePip(false);
    }

    public void hidePipMenu(Runnable runnable, Runnable runnable2) {
        this.mMenuController.hideMenu(runnable, runnable2);
    }

    public void showPictureInPictureMenu() {
        this.mTouchHandler.showPictureInPictureMenu();
    }

    /* access modifiers changed from: private */
    public void onKeyguardVisibilityChanged(boolean z, boolean z2) {
        if (this.mPipTaskOrganizer.isInPip()) {
            if (z) {
                this.mIsKeyguardShowingOrAnimating = true;
                hidePipMenu((Runnable) null, (Runnable) null);
                this.mPipTaskOrganizer.setPipVisibility(false);
            } else if (!z2) {
                this.mIsKeyguardShowingOrAnimating = false;
                this.mPipTaskOrganizer.setPipVisibility(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onKeyguardDismissAnimationFinished() {
        if (this.mPipTaskOrganizer.isInPip()) {
            this.mIsKeyguardShowingOrAnimating = false;
            this.mPipTaskOrganizer.setPipVisibility(true);
        }
    }

    public void setTouchGesture(PipTouchGesture pipTouchGesture) {
        this.mTouchHandler.setTouchGesture(pipTouchGesture);
    }

    /* access modifiers changed from: private */
    public void setShelfHeight(boolean z, int i) {
        if (!this.mIsKeyguardShowingOrAnimating) {
            setShelfHeightLocked(z, i);
        }
    }

    private void setShelfHeightLocked(boolean z, int i) {
        if (!z) {
            i = 0;
        }
        this.mPipBoundsState.setShelfVisibility(z, i);
    }

    /* access modifiers changed from: private */
    public void setPinnedStackAnimationType(int i) {
        this.mPipTaskOrganizer.setOneShotAnimationType(i);
        this.mPipTransitionController.setIsFullAnimation(i == 0);
    }

    /* access modifiers changed from: private */
    public void setPinnedStackAnimationListener(PipAnimationListener pipAnimationListener) {
        this.mPinnedStackAnimationRecentsCallback = pipAnimationListener;
        onPipResourceDimensionsChanged();
    }

    private void onPipResourceDimensionsChanged() {
        PipAnimationListener pipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (pipAnimationListener != null) {
            pipAnimationListener.onPipResourceDimensionsChanged(this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.pip_corner_radius), this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.pip_shadow_radius));
        }
    }

    /* access modifiers changed from: private */
    public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
        setShelfHeightLocked(i2 > 0, i2);
        onDisplayRotationChangedNotInPip(this.mContext, i);
        Rect startSwipePipToHome = this.mPipTaskOrganizer.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams);
        this.mPipBoundsState.setNormalBounds(startSwipePipToHome);
        return startSwipePipToHome;
    }

    /* access modifiers changed from: private */
    public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        this.mPipTaskOrganizer.stopSwipePipToHome(i, componentName, rect, surfaceControl);
    }

    public void onPipTransitionStarted(int i, Rect rect) {
        InteractionJankMonitor.getInstance().begin(InteractionJankMonitor.Configuration.Builder.withSurface(35, this.mContext, this.mPipTaskOrganizer.getSurfaceControl()).setTag(getTransitionTag(i)).setTimeout(StatsManager.DEFAULT_TIMEOUT_MILLIS));
        if (PipAnimationController.isOutPipDirection(i)) {
            saveReentryState(rect);
        }
        this.mTouchHandler.setTouchEnabled(false);
        PipAnimationListener pipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (pipAnimationListener != null) {
            pipAnimationListener.onPipAnimationStarted();
            if (i == 3) {
                this.mPinnedStackAnimationRecentsCallback.onExpandPip();
            }
        }
    }

    public void saveReentryState(Rect rect) {
        float snapFraction = this.mPipBoundsAlgorithm.getSnapFraction(rect);
        if (this.mPipBoundsState.hasUserResizedPip()) {
            Rect userResizeBounds = this.mTouchHandler.getUserResizeBounds();
            this.mPipBoundsState.saveReentryState(new Size(userResizeBounds.width(), userResizeBounds.height()), snapFraction);
            return;
        }
        this.mPipBoundsState.saveReentryState((Size) null, snapFraction);
    }

    public void onPipTransitionFinished(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    public void onPipTransitionCanceled(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    private void onPipTransitionFinishedOrCanceled(int i) {
        InteractionJankMonitor.getInstance().end(35);
        this.mTouchHandler.setTouchEnabled(true);
        this.mTouchHandler.onPinnedStackAnimationEnded(i);
    }

    /* access modifiers changed from: private */
    public void updateMovementBounds(Rect rect, boolean z, boolean z2, boolean z3, WindowContainerTransaction windowContainerTransaction) {
        Rect rect2 = new Rect(rect);
        int rotation = this.mPipBoundsState.getDisplayLayout().rotation();
        this.mPipBoundsAlgorithm.getInsetBounds(this.mTmpInsetBounds);
        this.mPipBoundsState.setNormalBounds(this.mPipBoundsAlgorithm.getNormalBounds());
        if (rect2.isEmpty()) {
            rect2.set(this.mPipBoundsAlgorithm.getDefaultBounds());
        }
        this.mPipTaskOrganizer.onMovementBoundsChanged(rect2, z, z2, z3, windowContainerTransaction);
        this.mPipTaskOrganizer.finishResizeForMenu(rect2);
        this.mTouchHandler.onMovementBoundsChanged(this.mTmpInsetBounds, this.mPipBoundsState.getNormalBounds(), rect2, z2, z3, rotation);
    }

    private void onDisplayRotationChangedNotInPip(Context context, int i) {
        this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i);
    }

    private boolean onDisplayRotationChanged(Context context, Rect rect, Rect rect2, Rect rect3, int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        Rect rect4 = rect;
        int i4 = i3;
        if (i == this.mPipBoundsState.getDisplayId() && i2 != i4) {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
                if (rootTaskInfo == null) {
                    return false;
                }
                PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
                Rect rect5 = new Rect(rect2);
                float snapFraction = snapAlgorithm.getSnapFraction(rect5, this.mPipBoundsAlgorithm.getMovementBounds(rect5), this.mPipBoundsState.getStashedState());
                this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i4);
                snapAlgorithm.applySnapFraction(rect5, this.mPipBoundsAlgorithm.getMovementBounds(rect5, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
                this.mPipBoundsAlgorithm.getInsetBounds(rect3);
                rect4.set(rect5);
                windowContainerTransaction.setBounds(rootTaskInfo.token, rect4);
                return true;
            } catch (RemoteException e) {
                ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to get RootTaskInfo for pinned task, %s", new Object[]{TAG, e});
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        this.mMenuController.dump(printWriter, "  ");
        this.mTouchHandler.dump(printWriter, "  ");
        this.mPipBoundsAlgorithm.dump(printWriter, "  ");
        this.mPipTaskOrganizer.dump(printWriter, "  ");
        this.mPipBoundsState.dump(printWriter, "  ");
        this.mPipInputConsumer.dump(printWriter, "  ");
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipController$PipImpl */
    private class PipImpl implements Pip {
        private IPipImpl mIPip;

        private PipImpl() {
        }

        public IPip createExternalInterface() {
            IPipImpl iPipImpl = this.mIPip;
            if (iPipImpl != null) {
                iPipImpl.invalidate();
            }
            IPipImpl iPipImpl2 = new IPipImpl(PipController.this);
            this.mIPip = iPipImpl2;
            return iPipImpl2;
        }

        public void expandPip() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$expandPip$0$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50343x425aedf() {
            PipController.this.expandPip();
        }

        public void onConfigurationChanged(Configuration configuration) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda12(this, configuration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConfigurationChanged$1$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50344xf3696164(Configuration configuration) {
            PipController.this.onConfigurationChanged(configuration);
        }

        public void onDensityOrFontScaleChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDensityOrFontScaleChanged$2$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50345x1f706895() {
            PipController.this.onDensityOrFontScaleChanged();
        }

        public void onOverlayChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onOverlayChanged$3$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50347xd24b9f7c() {
            PipController.this.onOverlayChanged();
        }

        public void onSystemUiStateChanged(boolean z, int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda5(this, z, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSystemUiStateChanged$4$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50348x1c263b79(boolean z, int i) {
            PipController.this.onSystemUiStateChanged(z, i);
        }

        public void registerSessionListenerForCurrentUser() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda10(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSessionListenerForCurrentUser$5$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50349x4196c4f5() {
            PipController.this.registerSessionListenerForCurrentUser();
        }

        public void setShelfHeight(boolean z, int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda4(this, z, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setShelfHeight$6$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50352x6df2dc6d(boolean z, int i) {
            PipController.this.setShelfHeight(z, i);
        }

        public void setPinnedStackAnimationType(int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda7(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setPinnedStackAnimationType$7$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50351xe8dfa785(int i) {
            PipController.this.setPinnedStackAnimationType(i);
        }

        public void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda9(this, consumer));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addPipExclusionBoundsChangeListener$8$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50341xba5c8fc3(Consumer consumer) {
            PipController.this.mPipBoundsState.addPipExclusionBoundsChangeCallback(consumer);
        }

        public void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda11(this, consumer));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$removePipExclusionBoundsChangeListener$9$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50350x963ec405(Consumer consumer) {
            PipController.this.mPipBoundsState.removePipExclusionBoundsChangeCallback(consumer);
        }

        public void showPictureInPictureMenu() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$showPictureInPictureMenu$10$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50353xcdd901c() {
            PipController.this.showPictureInPictureMenu();
        }

        public void onKeyguardVisibilityChanged(boolean z, boolean z2) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda8(this, z, z2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onKeyguardVisibilityChanged$11$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50346xb17848e7(boolean z, boolean z2) {
            PipController.this.onKeyguardVisibilityChanged(z, z2);
        }

        public void onKeyguardDismissAnimationFinished() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda6(PipController.this));
        }

        public void dump(PrintWriter printWriter) {
            try {
                PipController.this.mMainExecutor.executeBlocking(new PipController$PipImpl$$ExternalSyntheticLambda13(this, printWriter));
            } catch (InterruptedException unused) {
                ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to dump PipController in 2s", new Object[]{PipController.TAG});
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$dump$13$com-android-wm-shell-pip-phone-PipController$PipImpl */
        public /* synthetic */ void mo50342x51c76416(PrintWriter printWriter) {
            PipController.this.dump(printWriter);
        }
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipController$IPipImpl */
    private static class IPipImpl extends IPip.Stub {
        private PipController mController;
        /* access modifiers changed from: private */
        public final SingleInstanceRemoteListener<PipController, IPipAnimationListener> mListener;
        private final PipAnimationListener mPipAnimationListener = new PipAnimationListener() {
            public void onPipAnimationStarted() {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda0());
            }

            public void onPipResourceDimensionsChanged(int i, int i2) {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda2(i, i2));
            }

            public void onExpandPip() {
                IPipImpl.this.mListener.call(new PipController$IPipImpl$1$$ExternalSyntheticLambda1());
            }
        };

        IPipImpl(PipController pipController) {
            this.mController = pipController;
            this.mListener = new SingleInstanceRemoteListener<>(this.mController, new PipController$IPipImpl$$ExternalSyntheticLambda4(this), new PipController$IPipImpl$$ExternalSyntheticLambda5());
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-android-wm-shell-pip-phone-PipController$IPipImpl */
        public /* synthetic */ void mo50336xe4c4d869(PipController pipController) {
            pipController.setPinnedStackAnimationListener(this.mPipAnimationListener);
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mController = null;
        }

        public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
            Rect[] rectArr = new Rect[1];
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda1(rectArr, componentName, activityInfo, pictureInPictureParams, i, i2), true);
            return rectArr[0];
        }

        static /* synthetic */ void lambda$startSwipePipToHome$2(Rect[] rectArr, ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2, PipController pipController) {
            rectArr[0] = pipController.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams, i, i2);
        }

        public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "stopSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda0(i, componentName, rect, surfaceControl));
        }

        public void setShelfHeight(boolean z, int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setShelfHeight", new PipController$IPipImpl$$ExternalSyntheticLambda3(z, i));
        }

        public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setPinnedStackAnimationListener", new PipController$IPipImpl$$ExternalSyntheticLambda2(this, iPipAnimationListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setPinnedStackAnimationListener$5$com-android-wm-shell-pip-phone-PipController$IPipImpl */
        public /* synthetic */ void mo50337x2f8ea6b8(IPipAnimationListener iPipAnimationListener, PipController pipController) {
            if (iPipAnimationListener != null) {
                this.mListener.register(iPipAnimationListener);
            } else {
                this.mListener.unregister();
            }
        }
    }
}
