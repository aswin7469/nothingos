package com.android.p019wm.shell.pip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.RotationUtils;
import android.view.SurfaceControl;
import android.window.TaskSnapshot;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.animation.Interpolators;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ScreenshotUtils;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer */
public class PipTaskOrganizer implements ShellTaskOrganizer.TaskListener, DisplayController.OnDisplaysChangedListener, ShellTaskOrganizer.FocusListener {
    private static final int CONTENT_OVERLAY_FADE_OUT_DELAY_MS = 500;
    private static final boolean DEBUG = false;
    private static final int ONE_SHOT_ALPHA_ANIMATION_TIMEOUT_MS = 1000;
    private static final String TAG = "PipTaskOrganizer";
    private final Context mContext;
    private final int mCrossFadeAnimationDuration;
    private int mCurrentRotation;
    /* access modifiers changed from: private */
    public SurfaceControl.Transaction mDeferredAnimEndTransaction;
    private ActivityManager.RunningTaskInfo mDeferredTaskInfo;
    private final int mEnterAnimationDuration;
    private final int mExitAnimationDuration;
    private boolean mHasFadeOut;
    private long mLastOneShotAlphaAnimationTime;
    /* access modifiers changed from: private */
    public SurfaceControl mLeash;
    protected final ShellExecutor mMainExecutor;
    private int mNextRotation;
    private IntConsumer mOnDisplayIdChangeCallback;
    private int mOneShotAnimationType = 0;
    protected PictureInPictureParams mPictureInPictureParams;
    private final PipAnimationController.PipAnimationCallback mPipAnimationCallback = new PipAnimationController.PipAnimationCallback() {
        public void onPipAnimationStart(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            PipTaskOrganizer.this.sendOnPipTransitionStarted(pipTransitionAnimator.getTransitionDirection());
        }

        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            int animationType = pipTransitionAnimator.getAnimationType();
            Rect destinationBounds = pipTransitionAnimator.getDestinationBounds();
            boolean z = true;
            if (PipAnimationController.isInPipDirection(transitionDirection) && pipTransitionAnimator.getContentOverlayLeash() != null) {
                PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                SurfaceControl contentOverlayLeash = pipTransitionAnimator.getContentOverlayLeash();
                Objects.requireNonNull(pipTransitionAnimator);
                pipTaskOrganizer.fadeOutAndRemoveOverlay(contentOverlayLeash, new PipTaskOrganizer$$ExternalSyntheticLambda6(pipTransitionAnimator), true);
            }
            if (PipTaskOrganizer.this.mWaitForFixedRotation && animationType == 0 && transitionDirection == 2) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.scheduleFinishEnterPip(PipTaskOrganizer.this.mToken, destinationBounds);
                PipTaskOrganizer.this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
                PipTaskOrganizer.this.mSurfaceTransactionHelper.round(transaction, PipTaskOrganizer.this.mLeash, PipTaskOrganizer.this.isInPip());
                SurfaceControl.Transaction unused = PipTaskOrganizer.this.mDeferredAnimEndTransaction = transaction;
                return;
            }
            if (!PipAnimationController.isOutPipDirection(transitionDirection) && !PipAnimationController.isRemovePipDirection(transitionDirection)) {
                z = false;
            }
            if (PipTaskOrganizer.this.mPipTransitionState.getTransitionState() != 5 || z) {
                PipTaskOrganizer.this.finishResize(transaction, destinationBounds, transitionDirection, animationType);
                PipTaskOrganizer.this.sendOnPipTransitionFinished(transitionDirection);
            }
        }

        public void onPipAnimationCancel(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            if (PipAnimationController.isInPipDirection(transitionDirection) && pipTransitionAnimator.getContentOverlayLeash() != null) {
                PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                SurfaceControl contentOverlayLeash = pipTransitionAnimator.getContentOverlayLeash();
                Objects.requireNonNull(pipTransitionAnimator);
                pipTaskOrganizer.fadeOutAndRemoveOverlay(contentOverlayLeash, new PipTaskOrganizer$$ExternalSyntheticLambda6(pipTransitionAnimator), true);
            }
            PipTaskOrganizer.this.sendOnPipTransitionCancelled(transitionDirection);
        }
    };
    private final PipAnimationController mPipAnimationController;
    private final PipBoundsAlgorithm mPipBoundsAlgorithm;
    private final PipBoundsState mPipBoundsState;
    /* access modifiers changed from: private */
    public final PipMenuController mPipMenuController;
    protected final PipParamsChangedForwarder mPipParamsChangedForwarder;
    private final PipAnimationController.PipTransactionHandler mPipTransactionHandler = new PipAnimationController.PipTransactionHandler() {
        public boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            if (!PipTaskOrganizer.this.mPipMenuController.isMenuVisible()) {
                return false;
            }
            PipTaskOrganizer.this.mPipMenuController.movePipMenu(surfaceControl, transaction, rect);
            return true;
        }
    };
    private final PipTransitionController mPipTransitionController;
    /* access modifiers changed from: private */
    public PipTransitionState mPipTransitionState;
    private final PipUiEventLogger mPipUiEventLoggerLogger;
    private final Optional<SplitScreenController> mSplitScreenOptional;
    private PipSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
    /* access modifiers changed from: private */
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;
    SurfaceControl mSwipePipToHomeOverlay;
    private final SyncTransactionQueue mSyncTransactionQueue;
    private ActivityManager.RunningTaskInfo mTaskInfo;
    protected final ShellTaskOrganizer mTaskOrganizer;
    /* access modifiers changed from: private */
    public WindowContainerToken mToken;
    /* access modifiers changed from: private */
    public boolean mWaitForFixedRotation;

    public static /* synthetic */ SurfaceControl.Transaction $r8$lambda$YBq2EdTUd4n_Yb3LxCpNBunpmKU() {
        return new SurfaceControl.Transaction();
    }

    public int getOutPipWindowingMode() {
        return 0;
    }

    public boolean supportCompatUI() {
        return false;
    }

    public PipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipMenuController pipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor) {
        PipTransitionController pipTransitionController2 = pipTransitionController;
        ShellTaskOrganizer shellTaskOrganizer2 = shellTaskOrganizer;
        ShellExecutor shellExecutor2 = shellExecutor;
        this.mContext = context;
        this.mSyncTransactionQueue = syncTransactionQueue;
        this.mPipTransitionState = pipTransitionState;
        this.mPipBoundsState = pipBoundsState;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipMenuController = pipMenuController;
        this.mPipTransitionController = pipTransitionController2;
        this.mPipParamsChangedForwarder = pipParamsChangedForwarder;
        this.mEnterAnimationDuration = context.getResources().getInteger(C3353R.integer.config_pipEnterAnimationDuration);
        this.mExitAnimationDuration = context.getResources().getInteger(C3353R.integer.config_pipExitAnimationDuration);
        this.mCrossFadeAnimationDuration = context.getResources().getInteger(C3353R.integer.config_pipCrossfadeAnimationDuration);
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        this.mPipAnimationController = pipAnimationController;
        this.mPipUiEventLoggerLogger = pipUiEventLogger;
        this.mSurfaceControlTransactionFactory = new PipTaskOrganizer$$ExternalSyntheticLambda4();
        this.mSplitScreenOptional = optional;
        this.mTaskOrganizer = shellTaskOrganizer2;
        this.mMainExecutor = shellExecutor2;
        shellExecutor2.execute(new PipTaskOrganizer$$ExternalSyntheticLambda5(this));
        shellTaskOrganizer2.addFocusListener(this);
        pipTransitionController2.setPipOrganizer(this);
        displayController.addDisplayWindowListener(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-pip-PipTaskOrganizer  reason: not valid java name */
    public /* synthetic */ void m3461lambda$new$0$comandroidwmshellpipPipTaskOrganizer() {
        this.mTaskOrganizer.addListenerForType(this, -4);
    }

    public Rect getCurrentOrAnimatingBounds() {
        PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
        if (currentAnimator == null || !currentAnimator.isRunning()) {
            return this.mPipBoundsState.getBounds();
        }
        return new Rect(currentAnimator.getDestinationBounds());
    }

    public boolean isInPip() {
        return this.mPipTransitionState.isInPip();
    }

    private boolean isLaunchIntoPipTask() {
        PictureInPictureParams pictureInPictureParams = this.mPictureInPictureParams;
        return pictureInPictureParams != null && pictureInPictureParams.isLaunchIntoPip();
    }

    public boolean isEntryScheduled() {
        return this.mPipTransitionState.getTransitionState() == 2;
    }

    public void registerOnDisplayIdChangeCallback(IntConsumer intConsumer) {
        this.mOnDisplayIdChangeCallback = intConsumer;
    }

    public void setOneShotAnimationType(int i) {
        this.mOneShotAnimationType = i;
        if (i == 1) {
            this.mLastOneShotAlphaAnimationTime = SystemClock.uptimeMillis();
        }
    }

    public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams) {
        this.mPipTransitionState.setInSwipePipToHomeTransition(true);
        sendOnPipTransitionStarted(2);
        setBoundsStateForEntry(componentName, pictureInPictureParams, activityInfo);
        return this.mPipBoundsAlgorithm.getEntryDestinationBounds();
    }

    public void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        if (this.mPipTransitionState.getInSwipePipToHomeTransition()) {
            this.mPipBoundsState.setBounds(rect);
            this.mSwipePipToHomeOverlay = surfaceControl;
            if (Transitions.ENABLE_SHELL_TRANSITIONS && surfaceControl != null) {
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                this.mTaskOrganizer.reparentChildSurfaceToTask(i, surfaceControl, transaction);
                transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
                transaction.apply();
            }
        }
    }

    public ActivityManager.RunningTaskInfo getTaskInfo() {
        return this.mTaskInfo;
    }

    public SurfaceControl getSurfaceControl() {
        return this.mLeash;
    }

    private void setBoundsStateForEntry(ComponentName componentName, PictureInPictureParams pictureInPictureParams, ActivityInfo activityInfo) {
        this.mPipBoundsState.setBoundsStateForEntry(componentName, activityInfo, pictureInPictureParams, this.mPipBoundsAlgorithm);
    }

    public void exitPip(int i, boolean z) {
        if (!this.mPipTransitionState.isInPip() || this.mPipTransitionState.getTransitionState() == 5 || this.mToken == null) {
            ProtoLog.wtf(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Not allowed to exitPip in current state mState=%d mToken=%s", new Object[]{TAG, Integer.valueOf(this.mPipTransitionState.getTransitionState()), this.mToken});
            return;
        }
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (isLaunchIntoPipTask()) {
            exitLaunchIntoPipTask(windowContainerTransaction);
        } else if (!Transitions.ENABLE_SHELL_TRANSITIONS || !z || !this.mSplitScreenOptional.isPresent()) {
            Rect exitDestinationBounds = getExitDestinationBounds();
            int i2 = syncWithSplitScreenBounds(exitDestinationBounds, z) ? 4 : 3;
            if (!Transitions.ENABLE_SHELL_TRANSITIONS || i2 != 3) {
                SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
                this.mSurfaceTransactionHelper.scale(transaction, this.mLeash, exitDestinationBounds, this.mPipBoundsState.getBounds());
                transaction.setWindowCrop(this.mLeash, exitDestinationBounds.width(), exitDestinationBounds.height());
                windowContainerTransaction.setActivityWindowingMode(this.mToken, 1);
                windowContainerTransaction.setBounds(this.mToken, exitDestinationBounds);
                windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
            } else {
                windowContainerTransaction.setWindowingMode(this.mToken, 0);
                windowContainerTransaction.setBounds(this.mToken, (Rect) null);
            }
            cancelCurrentAnimator();
            this.mPipTransitionState.setTransitionState(5);
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mPipTransitionController.startExitTransition(13, windowContainerTransaction, exitDestinationBounds);
                return;
            }
            this.mSyncTransactionQueue.queue(windowContainerTransaction);
            this.mSyncTransactionQueue.runInSync(new PipTaskOrganizer$$ExternalSyntheticLambda9(this, exitDestinationBounds, i2, i));
        } else {
            this.mSplitScreenOptional.get().prepareEnterSplitScreen(windowContainerTransaction, this.mTaskInfo, isPipTopLeft() ^ true ? 1 : 0);
            this.mPipTransitionController.startExitTransition(14, windowContainerTransaction, (Rect) null);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$exitPip$1$com-android-wm-shell-pip-PipTaskOrganizer  reason: not valid java name */
    public /* synthetic */ void m3459lambda$exitPip$1$comandroidwmshellpipPipTaskOrganizer(Rect rect, int i, int i2, SurfaceControl.Transaction transaction) {
        PipAnimationController.PipTransitionAnimator<?> animateResizePip = animateResizePip(this.mPipBoundsState.getBounds(), rect, PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, rect), i, i2, 0.0f);
        if (animateResizePip != null) {
            animateResizePip.applySurfaceControlTransaction(this.mLeash, transaction, 0.0f);
        }
    }

    public Rect getExitDestinationBounds() {
        return this.mPipBoundsState.getDisplayBounds();
    }

    private void exitLaunchIntoPipTask(WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.startTask(this.mTaskInfo.launchIntoPipHostTaskId, (Bundle) null);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        removePip();
    }

    private void applyWindowingModeChangeOnExit(WindowContainerTransaction windowContainerTransaction, int i) {
        windowContainerTransaction.setWindowingMode(this.mToken, getOutPipWindowingMode());
        windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
    }

    public void removePip() {
        if (!this.mPipTransitionState.isInPip() || this.mToken == null) {
            ProtoLog.wtf(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Not allowed to removePip in current state mState=%d mToken=%s", new Object[]{TAG, Integer.valueOf(this.mPipTransitionState.getTransitionState()), this.mToken});
            return;
        }
        PipAnimationController.PipTransitionAnimator pipAnimationCallback = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), 1.0f, 0.0f).setTransitionDirection(5).setPipTransactionHandler(this.mPipTransactionHandler).setPipAnimationCallback(this.mPipAnimationCallback);
        pipAnimationCallback.setDuration((long) this.mExitAnimationDuration);
        pipAnimationCallback.setInterpolator(Interpolators.ALPHA_OUT);
        pipAnimationCallback.start();
        this.mPipTransitionState.setTransitionState(5);
    }

    private void removePipImmediately() {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.setBounds(this.mToken, (Rect) null);
            windowContainerTransaction.setWindowingMode(this.mToken, 0);
            windowContainerTransaction.reorder(this.mToken, false);
            this.mPipTransitionController.startExitTransition(15, windowContainerTransaction, (Rect) null);
            return;
        }
        try {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            windowContainerTransaction2.setBounds(this.mToken, (Rect) null);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction2);
            ActivityTaskManager.getService().removeRootTasksInWindowingModes(new int[]{2});
        } catch (RemoteException e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to remove PiP, %s", new Object[]{TAG, e});
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        IntConsumer intConsumer;
        Objects.requireNonNull(runningTaskInfo, "Requires RunningTaskInfo");
        this.mTaskInfo = runningTaskInfo;
        this.mToken = runningTaskInfo.token;
        this.mPipTransitionState.setTransitionState(1);
        this.mLeash = surfaceControl;
        this.mPictureInPictureParams = this.mTaskInfo.pictureInPictureParams;
        setBoundsStateForEntry(this.mTaskInfo.topActivity, this.mPictureInPictureParams, this.mTaskInfo.topActivityInfo);
        PictureInPictureParams pictureInPictureParams = this.mPictureInPictureParams;
        if (pictureInPictureParams != null) {
            this.mPipParamsChangedForwarder.notifyActionsChanged(pictureInPictureParams.getActions(), this.mPictureInPictureParams.getCloseAction());
            this.mPipParamsChangedForwarder.notifyTitleChanged(this.mPictureInPictureParams.getTitle());
            this.mPipParamsChangedForwarder.notifySubtitleChanged(this.mPictureInPictureParams.getSubtitle());
        }
        this.mPipUiEventLoggerLogger.setTaskInfo(this.mTaskInfo);
        this.mPipUiEventLoggerLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_ENTER);
        if (!(runningTaskInfo.displayId == this.mPipBoundsState.getDisplayId() || (intConsumer = this.mOnDisplayIdChangeCallback) == null)) {
            intConsumer.accept(runningTaskInfo.displayId);
        }
        if (!this.mPipTransitionState.getInSwipePipToHomeTransition()) {
            if (this.mOneShotAnimationType == 1 && SystemClock.uptimeMillis() - this.mLastOneShotAlphaAnimationTime > 1000) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Alpha animation is expired. Use bounds animation.", new Object[]{TAG});
                this.mOneShotAnimationType = 0;
            }
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                if (this.mWaitForFixedRotation) {
                    onTaskAppearedWithFixedRotation();
                    return;
                }
                Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
                Objects.requireNonNull(entryDestinationBounds, "Missing destination bounds");
                Rect bounds = this.mTaskInfo.configuration.windowConfiguration.getBounds();
                int i = this.mOneShotAnimationType;
                if (i == 0) {
                    this.mPipMenuController.attach(this.mLeash);
                    scheduleAnimateResizePip(bounds, entryDestinationBounds, 0.0f, PipBoundsAlgorithm.getValidSourceHintRect(runningTaskInfo.pictureInPictureParams, bounds), 2, this.mEnterAnimationDuration, (Consumer<Rect>) null);
                    this.mPipTransitionState.setTransitionState(3);
                } else if (i == 1) {
                    enterPipWithAlphaAnimation(entryDestinationBounds, (long) this.mEnterAnimationDuration);
                    this.mOneShotAnimationType = 0;
                } else {
                    throw new RuntimeException("Unrecognized animation type: " + this.mOneShotAnimationType);
                }
            }
        } else if (!this.mWaitForFixedRotation) {
            onEndOfSwipePipToHomeTransition();
        } else {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Defer onTaskAppeared-SwipePipToHome until end of fixed rotation.", new Object[]{TAG});
        }
    }

    private void onTaskAppearedWithFixedRotation() {
        if (this.mOneShotAnimationType == 1) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Defer entering PiP alpha animation, fixed rotation is ongoing", new Object[]{TAG});
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setAlpha(this.mLeash, 0.0f);
            transaction.show(this.mLeash);
            transaction.apply();
            this.mOneShotAnimationType = 0;
            return;
        }
        Rect bounds = this.mTaskInfo.configuration.windowConfiguration.getBounds();
        animateResizePip(bounds, this.mPipBoundsAlgorithm.getEntryDestinationBounds(), PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, bounds), 2, this.mEnterAnimationDuration, 0.0f);
        this.mPipTransitionState.setTransitionState(3);
    }

    public void onDisplayRotationSkipped() {
        if (isEntryScheduled()) {
            enterPipWithAlphaAnimation(this.mPipBoundsAlgorithm.getEntryDestinationBounds(), (long) this.mEnterAnimationDuration);
        }
    }

    /* access modifiers changed from: package-private */
    public void enterPipWithAlphaAnimation(Rect rect, long j) {
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        transaction.setAlpha(this.mLeash, 0.0f);
        transaction.apply();
        SurfaceControl.Transaction transaction2 = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.crop(transaction2, this.mLeash, rect).round(transaction2, this.mLeash, true);
        this.mPipTransitionState.setTransitionState(2);
        applyEnterPipSyncTransaction(rect, new PipTaskOrganizer$$ExternalSyntheticLambda10(this, rect, j), transaction2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$enterPipWithAlphaAnimation$2$com-android-wm-shell-pip-PipTaskOrganizer */
    public /* synthetic */ void mo50221x7a6279a5(Rect rect, long j) {
        this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, rect, 0.0f, 1.0f).setTransitionDirection(2).setPipAnimationCallback(this.mPipAnimationCallback).setPipTransactionHandler(this.mPipTransactionHandler).setDuration(j).start();
        this.mPipTransitionState.setTransitionState(3);
    }

    private void onEndOfSwipePipToHomeTransition() {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            Rect bounds = this.mPipBoundsState.getBounds();
            SurfaceControl surfaceControl = this.mSwipePipToHomeOverlay;
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            this.mSurfaceTransactionHelper.resetScale(transaction, this.mLeash, bounds).crop(transaction, this.mLeash, bounds).round(transaction, this.mLeash, isInPip());
            applyEnterPipSyncTransaction(bounds, new PipTaskOrganizer$$ExternalSyntheticLambda0(this, bounds, surfaceControl), transaction);
            this.mPipTransitionState.setInSwipePipToHomeTransition(false);
            this.mSwipePipToHomeOverlay = null;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEndOfSwipePipToHomeTransition$3$com-android-wm-shell-pip-PipTaskOrganizer */
    public /* synthetic */ void mo50226x18bb1b00(Rect rect, SurfaceControl surfaceControl) {
        finishResizeForMenu(rect);
        sendOnPipTransitionFinished(2);
        if (surfaceControl != null) {
            fadeOutAndRemoveOverlay(surfaceControl, (Runnable) null, false);
        }
    }

    private void applyEnterPipSyncTransaction(Rect rect, Runnable runnable, SurfaceControl.Transaction transaction) {
        this.mPipMenuController.attach(this.mLeash);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        windowContainerTransaction.setBounds(this.mToken, rect);
        if (transaction != null) {
            windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
        }
        this.mSyncTransactionQueue.queue(windowContainerTransaction);
        if (runnable != null) {
            this.mSyncTransactionQueue.runInSync(new PipTaskOrganizer$$ExternalSyntheticLambda7(runnable));
        }
    }

    /* access modifiers changed from: private */
    public void sendOnPipTransitionStarted(int i) {
        if (i == 2) {
            this.mPipTransitionState.setTransitionState(3);
        }
        this.mPipTransitionController.sendOnPipTransitionStarted(i);
    }

    /* access modifiers changed from: package-private */
    public void sendOnPipTransitionFinished(int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        if (i == 2) {
            this.mPipTransitionState.setTransitionState(4);
        }
        this.mPipTransitionController.sendOnPipTransitionFinished(i);
        if (i == 2 && (runningTaskInfo = this.mDeferredTaskInfo) != null) {
            onTaskInfoChanged(runningTaskInfo);
            this.mDeferredTaskInfo = null;
        }
    }

    /* access modifiers changed from: private */
    public void sendOnPipTransitionCancelled(int i) {
        this.mPipTransitionController.sendOnPipTransitionCancelled(i);
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mPipTransitionState.getTransitionState() != 0) {
            if (!Transitions.ENABLE_SHELL_TRANSITIONS || this.mPipTransitionState.getTransitionState() != 5) {
                WindowContainerToken windowContainerToken = runningTaskInfo.token;
                Objects.requireNonNull(windowContainerToken, "Requires valid WindowContainerToken");
                if (windowContainerToken.asBinder() != this.mToken.asBinder()) {
                    ProtoLog.wtf(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Unrecognized token: %s", new Object[]{TAG, windowContainerToken});
                    return;
                }
                cancelCurrentAnimator();
                onExitPipFinished(runningTaskInfo);
                if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                    this.mPipTransitionController.forceFinishTransition();
                }
            }
        }
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        Objects.requireNonNull(this.mToken, "onTaskInfoChanged requires valid existing mToken");
        if (this.mPipTransitionState.getTransitionState() == 4 || this.mPipTransitionState.getTransitionState() == 5) {
            this.mPipBoundsState.setLastPipComponentName(runningTaskInfo.topActivity);
            this.mPipBoundsState.setOverrideMinSize(this.mPipBoundsAlgorithm.getMinimalSize(runningTaskInfo.topActivityInfo));
            PictureInPictureParams pictureInPictureParams = runningTaskInfo.pictureInPictureParams;
            if (pictureInPictureParams != null && this.mPictureInPictureParams != null) {
                applyNewPictureInPictureParams(pictureInPictureParams);
                this.mPictureInPictureParams = pictureInPictureParams;
                return;
            }
            return;
        }
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Defer onTaskInfoChange in current state: %d", new Object[]{TAG, Integer.valueOf(this.mPipTransitionState.getTransitionState())});
        this.mDeferredTaskInfo = runningTaskInfo;
    }

    public void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mPipMenuController.onFocusTaskChanged(runningTaskInfo);
    }

    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        builder.setParent(findTaskSurface(i));
    }

    public void reparentChildSurfaceToTask(int i, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        transaction.reparent(surfaceControl, findTaskSurface(i));
    }

    private SurfaceControl findTaskSurface(int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if (runningTaskInfo != null && this.mLeash != null && runningTaskInfo.taskId == i) {
            return this.mLeash;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    public void onFixedRotationStarted(int i, int i2) {
        this.mNextRotation = i2;
        this.mWaitForFixedRotation = true;
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mPipTransitionController.onFixedRotationStarted();
        } else if (this.mPipTransitionState.isInPip()) {
            fadeExistingPip(false);
        }
    }

    public void onFixedRotationFinished(int i) {
        if (this.mWaitForFixedRotation) {
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                clearWaitForFixedRotation();
                return;
            }
            if (this.mPipTransitionState.getTransitionState() == 1) {
                if (this.mPipTransitionState.getInSwipePipToHomeTransition()) {
                    onEndOfSwipePipToHomeTransition();
                } else {
                    enterPipWithAlphaAnimation(this.mPipBoundsAlgorithm.getEntryDestinationBounds(), (long) this.mEnterAnimationDuration);
                }
            } else if (this.mPipTransitionState.getTransitionState() == 4 && this.mHasFadeOut) {
                fadeExistingPip(true);
            } else if (this.mPipTransitionState.getTransitionState() == 3 && this.mDeferredAnimEndTransaction != null) {
                Rect destinationBounds = this.mPipAnimationController.getCurrentAnimator().getDestinationBounds();
                this.mPipBoundsState.setBounds(destinationBounds);
                applyEnterPipSyncTransaction(destinationBounds, new PipTaskOrganizer$$ExternalSyntheticLambda8(this, destinationBounds), this.mDeferredAnimEndTransaction);
            }
            clearWaitForFixedRotation();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFixedRotationFinished$5$com-android-wm-shell-pip-PipTaskOrganizer */
    public /* synthetic */ void mo50227x8e3e81e8(Rect rect) {
        finishResizeForMenu(rect);
        sendOnPipTransitionFinished(2);
    }

    /* access modifiers changed from: package-private */
    public void onExitPipFinished(TaskInfo taskInfo) {
        IntConsumer intConsumer;
        clearWaitForFixedRotation();
        SurfaceControl surfaceControl = this.mSwipePipToHomeOverlay;
        if (surfaceControl != null) {
            removeContentOverlay(surfaceControl, (Runnable) null);
            this.mSwipePipToHomeOverlay = null;
        }
        resetShadowRadius();
        this.mPipTransitionState.setInSwipePipToHomeTransition(false);
        this.mPictureInPictureParams = null;
        this.mPipTransitionState.setTransitionState(0);
        this.mPipBoundsState.setBounds(new Rect());
        this.mPipUiEventLoggerLogger.setTaskInfo((TaskInfo) null);
        this.mPipMenuController.detach();
        if (taskInfo.displayId != 0 && (intConsumer = this.mOnDisplayIdChangeCallback) != null) {
            intConsumer.accept(0);
        }
    }

    private void fadeExistingPip(boolean z) {
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl == null || !surfaceControl.isValid()) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Invalid leash on fadeExistingPip: %s", new Object[]{TAG, this.mLeash});
            return;
        }
        this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setTransitionDirection(1).setPipTransactionHandler(this.mPipTransactionHandler).setDuration((long) (z ? this.mEnterAnimationDuration : this.mExitAnimationDuration)).start();
        this.mHasFadeOut = !z;
    }

    private void clearWaitForFixedRotation() {
        this.mWaitForFixedRotation = false;
        this.mDeferredAnimEndTransaction = null;
    }

    public void setPipVisibility(boolean z) {
        if (isInPip()) {
            SurfaceControl surfaceControl = this.mLeash;
            if (surfaceControl == null || !surfaceControl.isValid()) {
                ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Invalid leash on setPipVisibility: %s", new Object[]{TAG, this.mLeash});
                return;
            }
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            this.mSurfaceTransactionHelper.alpha(transaction, this.mLeash, z ? 1.0f : 0.0f);
            transaction.apply();
        }
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        this.mCurrentRotation = configuration.windowConfiguration.getRotation();
    }

    public void onDensityOrFontScaleChanged(Context context) {
        this.mSurfaceTransactionHelper.onDensityOrFontScaleChanged(context);
    }

    public void onMovementBoundsChanged(Rect rect, boolean z, boolean z2, boolean z3, WindowContainerTransaction windowContainerTransaction) {
        boolean z4 = true;
        int i = 0;
        boolean z5 = this.mWaitForFixedRotation && this.mPipTransitionState.getTransitionState() != 4;
        if ((!this.mPipTransitionState.getInSwipePipToHomeTransition() && !z5) || !z) {
            PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
            if (currentAnimator == null || !currentAnimator.isRunning() || currentAnimator.getTransitionDirection() != 2) {
                if (!this.mPipTransitionState.isInPip() || !z) {
                    z4 = false;
                }
                if (z4 && Transitions.ENABLE_SHELL_TRANSITIONS) {
                    this.mPipBoundsState.setBounds(rect);
                } else if (z4 && this.mWaitForFixedRotation && this.mHasFadeOut) {
                    this.mPipBoundsState.setBounds(rect);
                } else if (z4) {
                    this.mPipBoundsState.setBounds(rect);
                    if (currentAnimator != null) {
                        i = currentAnimator.getTransitionDirection();
                        PipAnimationController.quietCancel(currentAnimator);
                        sendOnPipTransitionCancelled(i);
                        sendOnPipTransitionFinished(i);
                    }
                    prepareFinishResizeTransaction(rect, i, createFinishResizeSurfaceTransaction(rect), windowContainerTransaction);
                } else if (currentAnimator == null || !currentAnimator.isRunning()) {
                    if (!this.mPipBoundsState.getBounds().isEmpty()) {
                        rect.set(this.mPipBoundsState.getBounds());
                    }
                } else if (!currentAnimator.getDestinationBounds().isEmpty()) {
                    rect.set(currentAnimator.getDestinationBounds());
                }
            } else {
                Rect destinationBounds = currentAnimator.getDestinationBounds();
                rect.set(destinationBounds);
                if (z2 || z3 || !this.mPipBoundsState.getDisplayBounds().contains(destinationBounds)) {
                    Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
                    if (!entryDestinationBounds.equals(destinationBounds)) {
                        if (currentAnimator.getAnimationType() == 0) {
                            if (this.mWaitForFixedRotation) {
                                Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
                                Rect rect2 = new Rect(entryDestinationBounds);
                                RotationUtils.rotateBounds(rect2, displayBounds, this.mNextRotation, this.mCurrentRotation);
                                currentAnimator.updateEndValue(rect2);
                            } else {
                                currentAnimator.updateEndValue(entryDestinationBounds);
                            }
                        }
                        currentAnimator.setDestinationBounds(entryDestinationBounds);
                        rect.set(entryDestinationBounds);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void applyNewPictureInPictureParams(PictureInPictureParams pictureInPictureParams) {
        if (this.mDeferredTaskInfo != null || PipUtils.aspectRatioChanged(pictureInPictureParams.getAspectRatioFloat(), this.mPictureInPictureParams.getAspectRatioFloat())) {
            this.mPipParamsChangedForwarder.notifyAspectRatioChanged(pictureInPictureParams.getAspectRatioFloat());
        }
        if (this.mDeferredTaskInfo != null || PipUtils.remoteActionsChanged(pictureInPictureParams.getActions(), this.mPictureInPictureParams.getActions()) || !PipUtils.remoteActionsMatch(pictureInPictureParams.getCloseAction(), this.mPictureInPictureParams.getCloseAction())) {
            this.mPipParamsChangedForwarder.notifyActionsChanged(pictureInPictureParams.getActions(), pictureInPictureParams.getCloseAction());
        }
    }

    public void scheduleAnimateResizePip(Rect rect, int i, Consumer<Rect> consumer) {
        scheduleAnimateResizePip(rect, i, 0, consumer);
    }

    public void scheduleAnimateResizePip(Rect rect, int i, int i2, Consumer<Rect> consumer) {
        if (this.mWaitForFixedRotation) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: skip scheduleAnimateResizePip, entering pip deferred", new Object[]{TAG});
            return;
        }
        scheduleAnimateResizePip(this.mPipBoundsState.getBounds(), rect, 0.0f, (Rect) null, i2, i, consumer);
    }

    public void scheduleAnimateResizePip(Rect rect, Rect rect2, int i, float f, Consumer<Rect> consumer) {
        if (this.mWaitForFixedRotation) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: skip scheduleAnimateResizePip, entering pip deferred", new Object[]{TAG});
            return;
        }
        scheduleAnimateResizePip(rect, rect2, f, (Rect) null, 6, i, consumer);
    }

    private PipAnimationController.PipTransitionAnimator<?> scheduleAnimateResizePip(Rect rect, Rect rect2, float f, Rect rect3, int i, int i2, Consumer<Rect> consumer) {
        if (!this.mPipTransitionState.isInPip()) {
            return null;
        }
        PipAnimationController.PipTransitionAnimator<?> animateResizePip = animateResizePip(rect, rect2, rect3, i, i2, f);
        if (consumer != null) {
            consumer.accept(rect2);
        }
        return animateResizePip;
    }

    public void scheduleResizePip(Rect rect, Consumer<Rect> consumer) {
        if (this.mToken == null || this.mLeash == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Abort animation, invalid leash", new Object[]{TAG});
            return;
        }
        this.mPipBoundsState.setBounds(rect);
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.crop(transaction, this.mLeash, rect).round(transaction, this.mLeash, this.mPipTransitionState.isInPip());
        if (this.mPipMenuController.isMenuVisible()) {
            this.mPipMenuController.resizePipMenu(this.mLeash, transaction, rect);
        } else {
            transaction.apply();
        }
        if (consumer != null) {
            consumer.accept(rect);
        }
    }

    public void scheduleUserResizePip(Rect rect, Rect rect2, Consumer<Rect> consumer) {
        scheduleUserResizePip(rect, rect2, 0.0f, consumer);
    }

    public void scheduleUserResizePip(Rect rect, Rect rect2, float f, Consumer<Rect> consumer) {
        if (this.mToken == null || this.mLeash == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Abort animation, invalid leash", new Object[]{TAG});
        } else if (rect.isEmpty() || rect2.isEmpty()) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Attempted to user resize PIP to or from empty bounds, aborting.", new Object[]{TAG});
        } else {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            this.mSurfaceTransactionHelper.scale(transaction, this.mLeash, rect, rect2, f).round(transaction, this.mLeash, rect, rect2);
            if (this.mPipMenuController.isMenuVisible()) {
                this.mPipMenuController.movePipMenu(this.mLeash, transaction, rect2);
            } else {
                transaction.apply();
            }
            if (consumer != null) {
                consumer.accept(rect2);
            }
        }
    }

    public void scheduleFinishResizePip(Rect rect) {
        scheduleFinishResizePip(rect, (Consumer<Rect>) null);
    }

    public void scheduleFinishResizePip(Rect rect, Consumer<Rect> consumer) {
        scheduleFinishResizePip(rect, 0, consumer);
    }

    public void scheduleFinishResizePip(Rect rect, int i, Consumer<Rect> consumer) {
        if (!this.mPipTransitionState.shouldBlockResizeRequest()) {
            finishResize(createFinishResizeSurfaceTransaction(rect), rect, i, -1);
            if (consumer != null) {
                consumer.accept(rect);
            }
        }
    }

    private SurfaceControl.Transaction createFinishResizeSurfaceTransaction(Rect rect) {
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.crop(transaction, this.mLeash, rect).resetScale(transaction, this.mLeash, rect).round(transaction, this.mLeash, this.mPipTransitionState.isInPip());
        return transaction;
    }

    public void scheduleOffsetPip(Rect rect, int i, int i2, Consumer<Rect> consumer) {
        if (!this.mPipTransitionState.shouldBlockResizeRequest() && !this.mPipTransitionState.getInSwipePipToHomeTransition()) {
            if (this.mWaitForFixedRotation) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: skip scheduleOffsetPip, entering pip deferred", new Object[]{TAG});
                return;
            }
            offsetPip(rect, 0, i, i2);
            Rect rect2 = new Rect(rect);
            rect2.offset(0, i);
            if (consumer != null) {
                consumer.accept(rect2);
            }
        }
    }

    private void offsetPip(Rect rect, int i, int i2, int i3) {
        if (this.mTaskInfo == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: mTaskInfo is not set", new Object[]{TAG});
            return;
        }
        Rect rect2 = new Rect(rect);
        rect2.offset(i, i2);
        animateResizePip(rect, rect2, (Rect) null, 1, i3, 0.0f);
    }

    /* access modifiers changed from: private */
    public void finishResize(SurfaceControl.Transaction transaction, Rect rect, int i, int i2) {
        PictureInPictureParams pictureInPictureParams;
        Rect rect2 = new Rect(this.mPipBoundsState.getBounds());
        boolean isPipTopLeft = isPipTopLeft();
        this.mPipBoundsState.setBounds(rect);
        if (i == 5) {
            removePipImmediately();
            return;
        }
        boolean z = true;
        if (!PipAnimationController.isInPipDirection(i) || i2 != 1) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            prepareFinishResizeTransaction(rect, i, transaction, windowContainerTransaction);
            if (!(i == 7 || i == 6 || i == 8) || (pictureInPictureParams = this.mPictureInPictureParams) == null || pictureInPictureParams.isSeamlessResizeEnabled()) {
                z = false;
            }
            if (z) {
                rect2.offsetTo(0, 0);
                Rect rect3 = new Rect(0, 0, rect.width(), rect.height());
                SurfaceControl takeScreenshot = ScreenshotUtils.takeScreenshot(this.mSurfaceControlTransactionFactory.getTransaction(), this.mLeash, rect2, 2147483645);
                if (takeScreenshot != null) {
                    this.mSyncTransactionQueue.queue(windowContainerTransaction);
                    this.mSyncTransactionQueue.runInSync(new PipTaskOrganizer$$ExternalSyntheticLambda2(this, takeScreenshot, rect2, rect3));
                } else {
                    applyFinishBoundsResize(windowContainerTransaction, i, isPipTopLeft);
                }
            } else {
                applyFinishBoundsResize(windowContainerTransaction, i, isPipTopLeft);
            }
            finishResizeForMenu(rect);
            return;
        }
        finishResizeForMenu(rect);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$finishResize$6$com-android-wm-shell-pip-PipTaskOrganizer  reason: not valid java name */
    public /* synthetic */ void m3460lambda$finishResize$6$comandroidwmshellpipPipTaskOrganizer(SurfaceControl surfaceControl, Rect rect, Rect rect2, SurfaceControl.Transaction transaction) {
        this.mSurfaceTransactionHelper.scale(transaction, surfaceControl, rect, rect2);
        fadeOutAndRemoveOverlay(surfaceControl, (Runnable) null, false);
    }

    public void finishResizeForMenu(Rect rect) {
        if (isInPip()) {
            this.mPipMenuController.movePipMenu((SurfaceControl) null, (SurfaceControl.Transaction) null, rect);
            this.mPipMenuController.updateMenuBounds(rect);
        }
    }

    private void prepareFinishResizeTransaction(Rect rect, int i, SurfaceControl.Transaction transaction, WindowContainerTransaction windowContainerTransaction) {
        if (PipAnimationController.isInPipDirection(i)) {
            windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        } else if (PipAnimationController.isOutPipDirection(i)) {
            applyWindowingModeChangeOnExit(windowContainerTransaction, i);
            rect = null;
        }
        this.mSurfaceTransactionHelper.round(transaction, this.mLeash, isInPip());
        windowContainerTransaction.setBounds(this.mToken, rect);
        windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
    }

    public void applyFinishBoundsResize(WindowContainerTransaction windowContainerTransaction, int i, boolean z) {
        if (i == 4) {
            this.mSplitScreenOptional.ifPresent(new PipTaskOrganizer$$ExternalSyntheticLambda1(this, z, windowContainerTransaction));
        } else {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyFinishBoundsResize$7$com-android-wm-shell-pip-PipTaskOrganizer */
    public /* synthetic */ void mo50220xfcb41eef(boolean z, WindowContainerTransaction windowContainerTransaction, SplitScreenController splitScreenController) {
        splitScreenController.enterSplitScreen(this.mTaskInfo.taskId, z, windowContainerTransaction);
    }

    private boolean isPipTopLeft() {
        if (!this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        Rect rect = new Rect();
        this.mSplitScreenOptional.get().getStageBounds(rect, new Rect());
        return rect.contains(this.mPipBoundsState.getBounds());
    }

    private PipAnimationController.PipTransitionAnimator<?> animateResizePip(Rect rect, Rect rect2, Rect rect3, int i, int i2, float f) {
        int i3 = i;
        if (this.mToken == null || this.mLeash == null) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Abort animation, invalid leash", new Object[]{TAG});
            return null;
        }
        int deltaRotation = this.mWaitForFixedRotation ? RotationUtils.deltaRotation(this.mCurrentRotation, this.mNextRotation) : 0;
        Rect rect4 = rect2;
        Rect rect5 = rect3;
        if (deltaRotation != 0) {
            rect5 = computeRotatedBounds(deltaRotation, i3, rect4, rect5);
        }
        Rect rect6 = rect5;
        Rect bounds = i3 == 6 ? this.mPipBoundsState.getBounds() : rect;
        boolean z = this.mPipAnimationController.getCurrentAnimator() != null && this.mPipAnimationController.getCurrentAnimator().isRunning();
        PipAnimationController.PipTransitionAnimator<?> animator = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, bounds, rect, rect2, rect6, i, f, deltaRotation);
        animator.setTransitionDirection(i3).setPipTransactionHandler(this.mPipTransactionHandler).setDuration((long) i2);
        if (!z) {
            animator.setPipAnimationCallback(this.mPipAnimationCallback);
        }
        if (PipAnimationController.isInPipDirection(i)) {
            if (rect6 == null) {
                animator.setColorContentOverlay(this.mContext);
            } else {
                TaskSnapshot taskSnapshot = PipUtils.getTaskSnapshot(this.mTaskInfo.launchIntoPipHostTaskId, false);
                if (taskSnapshot != null) {
                    animator.setSnapshotContentOverlay(taskSnapshot, rect6);
                }
            }
            if (deltaRotation != 0) {
                animator.setDestinationBounds(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            }
        }
        animator.start();
        return animator;
    }

    private Rect computeRotatedBounds(int i, int i2, Rect rect, Rect rect2) {
        if (i2 == 2) {
            this.mPipBoundsState.getDisplayLayout().rotateTo(this.mContext.getResources(), this.mNextRotation);
            Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
            rect.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            RotationUtils.rotateBounds(rect, displayBounds, this.mNextRotation, this.mCurrentRotation);
            if (!(rect2 == null || this.mTaskInfo.displayCutoutInsets == null || i != 3)) {
                rect2.offset(this.mTaskInfo.displayCutoutInsets.left, this.mTaskInfo.displayCutoutInsets.top);
            }
        } else if (i2 == 3) {
            Rect rect3 = new Rect(rect);
            RotationUtils.rotateBounds(rect3, this.mPipBoundsState.getDisplayBounds(), i);
            return PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, rect3);
        }
        return rect2;
    }

    private boolean syncWithSplitScreenBounds(Rect rect, boolean z) {
        if (!z || !this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        Rect rect2 = new Rect();
        Rect rect3 = new Rect();
        this.mSplitScreenOptional.get().getStageBounds(rect2, rect3);
        if (!isPipTopLeft()) {
            rect2 = rect3;
        }
        rect.set(rect2);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void fadeOutAndRemoveOverlay(final SurfaceControl surfaceControl, final Runnable runnable, boolean z) {
        if (surfaceControl != null && surfaceControl.isValid()) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat.setDuration((long) this.mCrossFadeAnimationDuration);
            ofFloat.addUpdateListener(new PipTaskOrganizer$$ExternalSyntheticLambda3(this, surfaceControl));
            ofFloat.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    PipTaskOrganizer.this.removeContentOverlay(surfaceControl, runnable);
                }
            });
            ofFloat.setStartDelay(z ? 500 : 0);
            ofFloat.start();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeOutAndRemoveOverlay$8$com-android-wm-shell-pip-PipTaskOrganizer */
    public /* synthetic */ void mo50223x5530480d(SurfaceControl surfaceControl, ValueAnimator valueAnimator) {
        if (this.mPipTransitionState.getTransitionState() == 0) {
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Task vanished, skip fadeOutAndRemoveOverlay", new Object[]{TAG});
            PipAnimationController.quietCancel(valueAnimator);
        } else if (surfaceControl.isValid()) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setAlpha(surfaceControl, floatValue);
            transaction.apply();
        }
    }

    /* access modifiers changed from: private */
    public void removeContentOverlay(SurfaceControl surfaceControl, Runnable runnable) {
        if (this.mPipTransitionState.getTransitionState() != 0) {
            if (surfaceControl == null || !surfaceControl.isValid()) {
                ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: trying to remove invalid content overlay (%s)", new Object[]{TAG, surfaceControl});
                return;
            }
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.remove(surfaceControl);
            transaction.apply();
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private void resetShadowRadius() {
        if (this.mPipTransitionState.getTransitionState() != 0) {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setShadowRadius(this.mLeash, 0.0f);
            transaction.apply();
        }
    }

    private void cancelCurrentAnimator() {
        PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
        if (currentAnimator != null) {
            if (currentAnimator.getContentOverlayLeash() != null) {
                SurfaceControl contentOverlayLeash = currentAnimator.getContentOverlayLeash();
                Objects.requireNonNull(currentAnimator);
                removeContentOverlay(contentOverlayLeash, new PipTaskOrganizer$$ExternalSyntheticLambda6(currentAnimator));
            }
            PipAnimationController.quietCancel(currentAnimator);
        }
    }

    public void setSurfaceControlTransactionFactory(PipSurfaceTransactionHelper.SurfaceControlTransactionFactory surfaceControlTransactionFactory) {
        this.mSurfaceControlTransactionFactory = surfaceControlTransactionFactory;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "mTaskInfo=" + this.mTaskInfo);
        StringBuilder append = new StringBuilder().append(str2).append("mToken=").append((Object) this.mToken).append(" binder=");
        WindowContainerToken windowContainerToken = this.mToken;
        printWriter.println(append.append((Object) windowContainerToken != null ? windowContainerToken.asBinder() : null).toString());
        printWriter.println(str2 + "mLeash=" + this.mLeash);
        printWriter.println(str2 + "mState=" + this.mPipTransitionState.getTransitionState());
        printWriter.println(str2 + "mOneShotAnimationType=" + this.mOneShotAnimationType);
        printWriter.println(str2 + "mPictureInPictureParams=" + this.mPictureInPictureParams);
    }

    public String toString() {
        return TAG + ":" + ShellTaskOrganizer.taskListenerTypeToString(-4);
    }
}
