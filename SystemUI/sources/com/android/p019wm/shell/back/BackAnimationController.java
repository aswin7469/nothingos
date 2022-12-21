package com.android.p019wm.shell.back;

import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.WindowConfiguration;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.HardwareBuffer;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.BackEvent;
import android.window.BackNavigationInfo;
import android.window.IOnBackInvokedCallback;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.back.IBackAnimation;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.annotations.ShellBackgroundThread;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.util.concurrent.atomic.AtomicBoolean;

/* renamed from: com.android.wm.shell.back.BackAnimationController */
public class BackAnimationController implements RemoteCallable<BackAnimationController> {
    public static final boolean IS_ENABLED;
    private static final long MAX_TRANSITION_DURATION = 2000;
    private static final String PREDICTIVE_BACK_PROGRESS_THRESHOLD_PROP = "persist.wm.debug.predictive_back_progress_threshold";
    private static final int PROGRESS_THRESHOLD = SystemProperties.getInt(PREDICTIVE_BACK_PROGRESS_THRESHOLD_PROP, -1);
    private static final int SETTING_VALUE_OFF = 0;
    private static final int SETTING_VALUE_ON = 1;
    private static final String TAG = "BackAnimationController";
    private final IActivityTaskManager mActivityTaskManager;
    private final BackAnimation mBackAnimation;
    private boolean mBackGestureStarted;
    private BackNavigationInfo mBackNavigationInfo;
    private IOnBackInvokedCallback mBackToLauncherCallback;
    private final Context mContext;
    private final AtomicBoolean mEnableAnimations;
    private final PointF mInitTouchLocation;
    private float mProgressThreshold;
    private final Runnable mResetTransitionRunnable;
    /* access modifiers changed from: private */
    public final ShellExecutor mShellExecutor;
    private final Point mTouchEventDelta;
    private final SurfaceControl.Transaction mTransaction;
    private boolean mTransitionInProgress;
    private boolean mTriggerBack;
    private float mTriggerThreshold;

    static {
        boolean z = true;
        if (SystemProperties.getInt("persist.wm.debug.predictive_back", 1) == 0) {
            z = false;
        }
        IS_ENABLED = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-back-BackAnimationController  reason: not valid java name */
    public /* synthetic */ void m3407lambda$new$0$comandroidwmshellbackBackAnimationController() {
        finishAnimation();
        this.mTransitionInProgress = false;
    }

    public BackAnimationController(@ShellMainThread ShellExecutor shellExecutor, @ShellBackgroundThread Handler handler, Context context) {
        this(shellExecutor, handler, new SurfaceControl.Transaction(), ActivityTaskManager.getService(), context, context.getContentResolver());
    }

    BackAnimationController(@ShellMainThread ShellExecutor shellExecutor, @ShellBackgroundThread Handler handler, SurfaceControl.Transaction transaction, IActivityTaskManager iActivityTaskManager, Context context, ContentResolver contentResolver) {
        this.mEnableAnimations = new AtomicBoolean(false);
        this.mInitTouchLocation = new PointF();
        this.mTouchEventDelta = new Point();
        this.mBackGestureStarted = false;
        this.mTransitionInProgress = false;
        this.mResetTransitionRunnable = new BackAnimationController$$ExternalSyntheticLambda0(this);
        this.mBackAnimation = new BackAnimationImpl();
        this.mShellExecutor = shellExecutor;
        this.mTransaction = transaction;
        this.mActivityTaskManager = iActivityTaskManager;
        this.mContext = context;
        setupAnimationDeveloperSettingsObserver(contentResolver, handler);
    }

    private void setupAnimationDeveloperSettingsObserver(ContentResolver contentResolver, @ShellBackgroundThread Handler handler) {
        contentResolver.registerContentObserver(Settings.Global.getUriFor("enable_back_animation"), false, new ContentObserver(handler) {
            public void onChange(boolean z, Uri uri) {
                BackAnimationController.this.updateEnableAnimationFromSetting();
            }
        }, 0);
        updateEnableAnimationFromSetting();
    }

    /* access modifiers changed from: private */
    @ShellBackgroundThread
    public void updateEnableAnimationFromSetting() {
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "enable_back_animation", 0) == 1;
        this.mEnableAnimations.set(z);
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "Back animation enabled=%s", new Object[]{Boolean.valueOf(z)});
    }

    public BackAnimation getBackAnimationImpl() {
        return this.mBackAnimation;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mShellExecutor;
    }

    /* renamed from: com.android.wm.shell.back.BackAnimationController$BackAnimationImpl */
    private class BackAnimationImpl implements BackAnimation {
        private IBackAnimationImpl mBackAnimation;

        private BackAnimationImpl() {
        }

        public IBackAnimation createExternalInterface() {
            IBackAnimationImpl iBackAnimationImpl = this.mBackAnimation;
            if (iBackAnimationImpl != null) {
                iBackAnimationImpl.invalidate();
            }
            IBackAnimationImpl iBackAnimationImpl2 = new IBackAnimationImpl(BackAnimationController.this);
            this.mBackAnimation = iBackAnimationImpl2;
            return iBackAnimationImpl2;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBackMotion$0$com-android-wm-shell-back-BackAnimationController$BackAnimationImpl */
        public /* synthetic */ void mo48215xab14ae37(float f, float f2, int i, int i2) {
            BackAnimationController.this.onMotionEvent(f, f2, i, i2);
        }

        public void onBackMotion(float f, float f2, int i, int i2) {
            BackAnimationController.this.mShellExecutor.execute(new C3363x542d5223(this, f, f2, i, i2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setTriggerBack$1$com-android-wm-shell-back-BackAnimationController$BackAnimationImpl */
        public /* synthetic */ void mo48217x21859bf9(boolean z) {
            BackAnimationController.this.setTriggerBack(z);
        }

        public void setTriggerBack(boolean z) {
            BackAnimationController.this.mShellExecutor.execute(new C3362x542d5222(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setSwipeThresholds$2$com-android-wm-shell-back-BackAnimationController$BackAnimationImpl */
        public /* synthetic */ void mo48216xdae5c13d(float f, float f2) {
            BackAnimationController.this.setSwipeThresholds(f, f2);
        }

        public void setSwipeThresholds(float f, float f2) {
            BackAnimationController.this.mShellExecutor.execute(new C3361x542d5221(this, f, f2));
        }
    }

    /* renamed from: com.android.wm.shell.back.BackAnimationController$IBackAnimationImpl */
    private static class IBackAnimationImpl extends IBackAnimation.Stub {
        private BackAnimationController mController;

        IBackAnimationImpl(BackAnimationController backAnimationController) {
            this.mController = backAnimationController;
        }

        public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setBackToLauncherCallback", new C3364x62452dae(iOnBackInvokedCallback));
        }

        public void clearBackToLauncherCallback() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "clearBackToLauncherCallback", new C3366x62452db0());
        }

        public void onBackToLauncherAnimationFinished() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onBackToLauncherAnimationFinished", new C3365x62452daf());
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mController = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) {
        this.mBackToLauncherCallback = iOnBackInvokedCallback;
    }

    /* access modifiers changed from: private */
    public void clearBackToLauncherCallback() {
        this.mBackToLauncherCallback = null;
    }

    /* access modifiers changed from: package-private */
    public void onBackToLauncherAnimationFinished() {
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        if (backNavigationInfo != null) {
            IOnBackInvokedCallback onBackInvokedCallback = backNavigationInfo.getOnBackInvokedCallback();
            if (this.mTriggerBack) {
                dispatchOnBackInvoked(onBackInvokedCallback);
            } else {
                dispatchOnBackCancelled(onBackInvokedCallback);
            }
        }
        finishAnimation();
    }

    public void onMotionEvent(float f, float f2, int i, int i2) {
        if (!this.mTransitionInProgress) {
            if (i == 2) {
                if (!this.mBackGestureStarted) {
                    initAnimation(f, f2);
                }
                onMove(f, f2, i2);
            } else if (i == 1 || i == 3) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "Finishing gesture with event action: %d", new Object[]{Integer.valueOf(i)});
                onGestureFinished();
            }
        }
    }

    private void initAnimation(float f, float f2) {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "initAnimation mMotionStarted=%b", new Object[]{Boolean.valueOf(this.mBackGestureStarted)});
        if (this.mBackGestureStarted || this.mBackNavigationInfo != null) {
            Log.e(TAG, "Animation is being initialized but is already started.");
            finishAnimation();
        }
        this.mInitTouchLocation.set(f, f2);
        this.mBackGestureStarted = true;
        try {
            BackNavigationInfo startBackNavigation = this.mActivityTaskManager.startBackNavigation(this.mEnableAnimations.get());
            this.mBackNavigationInfo = startBackNavigation;
            onBackNavigationInfoReceived(startBackNavigation);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to initAnimation", e);
            finishAnimation();
        }
    }

    private void onBackNavigationInfoReceived(BackNavigationInfo backNavigationInfo) {
        IOnBackInvokedCallback iOnBackInvokedCallback;
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "Received backNavigationInfo:%s", new Object[]{backNavigationInfo});
        if (backNavigationInfo == null) {
            Log.e(TAG, "Received BackNavigationInfo is null.");
            finishAnimation();
            return;
        }
        int type = backNavigationInfo.getType();
        if (type == 2) {
            HardwareBuffer screenshotHardwareBuffer = backNavigationInfo.getScreenshotHardwareBuffer();
            if (screenshotHardwareBuffer != null) {
                displayTargetScreenshot(screenshotHardwareBuffer, backNavigationInfo.getTaskWindowConfiguration());
            }
            this.mTransaction.apply();
        } else {
            if (shouldDispatchToLauncher(type)) {
                iOnBackInvokedCallback = this.mBackToLauncherCallback;
            } else if (type == 4) {
                iOnBackInvokedCallback = this.mBackNavigationInfo.getOnBackInvokedCallback();
            }
            dispatchOnBackStarted(iOnBackInvokedCallback);
        }
        iOnBackInvokedCallback = null;
        dispatchOnBackStarted(iOnBackInvokedCallback);
    }

    private void displayTargetScreenshot(HardwareBuffer hardwareBuffer, WindowConfiguration windowConfiguration) {
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        SurfaceControl screenshotSurface = backNavigationInfo == null ? null : backNavigationInfo.getScreenshotSurface();
        if (screenshotSurface == null) {
            Log.e(TAG, "BackNavigationInfo doesn't contain a surface for the screenshot. ");
            return;
        }
        float width = (float) windowConfiguration.getBounds().width();
        float height = (float) windowConfiguration.getBounds().height();
        float f = 1.0f;
        float width2 = width != ((float) hardwareBuffer.getWidth()) ? width / ((float) hardwareBuffer.getWidth()) : 1.0f;
        if (height != ((float) hardwareBuffer.getHeight())) {
            f = height / ((float) hardwareBuffer.getHeight());
        }
        this.mTransaction.setScale(screenshotSurface, width2, f);
        this.mTransaction.setBuffer(screenshotSurface, hardwareBuffer);
        this.mTransaction.setVisibility(screenshotSurface, true);
    }

    private void onMove(float f, float f2, int i) {
        IOnBackInvokedCallback iOnBackInvokedCallback;
        if (this.mBackGestureStarted && this.mBackNavigationInfo != null) {
            int round = Math.round(f - this.mInitTouchLocation.x);
            int i2 = PROGRESS_THRESHOLD;
            float min = Math.min(Math.max(((float) Math.abs(round)) / (i2 >= 0 ? (float) i2 : this.mProgressThreshold), 0.0f), 1.0f);
            int type = this.mBackNavigationInfo.getType();
            BackEvent backEvent = new BackEvent(f, f2, min, i, this.mBackNavigationInfo.getDepartingAnimationTarget());
            if (shouldDispatchToLauncher(type)) {
                iOnBackInvokedCallback = this.mBackToLauncherCallback;
            } else {
                iOnBackInvokedCallback = (type == 3 || type == 2 || type != 4) ? null : this.mBackNavigationInfo.getOnBackInvokedCallback();
            }
            dispatchOnBackProgressed(iOnBackInvokedCallback, backEvent);
        }
    }

    private void onGestureFinished() {
        BackNavigationInfo backNavigationInfo;
        IOnBackInvokedCallback iOnBackInvokedCallback;
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "onGestureFinished() mTriggerBack == %s", new Object[]{Boolean.valueOf(this.mTriggerBack)});
        if (this.mBackGestureStarted && (backNavigationInfo = this.mBackNavigationInfo) != null) {
            int type = backNavigationInfo.getType();
            boolean shouldDispatchToLauncher = shouldDispatchToLauncher(type);
            if (shouldDispatchToLauncher) {
                iOnBackInvokedCallback = this.mBackToLauncherCallback;
            } else {
                iOnBackInvokedCallback = this.mBackNavigationInfo.getOnBackInvokedCallback();
            }
            if (shouldDispatchToLauncher) {
                startTransition();
            }
            if (this.mTriggerBack) {
                dispatchOnBackInvoked(iOnBackInvokedCallback);
            } else {
                dispatchOnBackCancelled(iOnBackInvokedCallback);
            }
            if (type != 1 || !shouldDispatchToLauncher) {
                finishAnimation();
            }
        }
    }

    private boolean shouldDispatchToLauncher(int i) {
        if (i != 1 || this.mBackToLauncherCallback == null || !this.mEnableAnimations.get()) {
            return false;
        }
        return true;
    }

    private static void dispatchOnBackStarted(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackStarted();
            } catch (RemoteException e) {
                Log.e(TAG, "dispatchOnBackStarted error: ", e);
            }
        }
    }

    private static void dispatchOnBackInvoked(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackInvoked();
            } catch (RemoteException e) {
                Log.e(TAG, "dispatchOnBackInvoked error: ", e);
            }
        }
    }

    private static void dispatchOnBackCancelled(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackCancelled();
            } catch (RemoteException e) {
                Log.e(TAG, "dispatchOnBackCancelled error: ", e);
            }
        }
    }

    private static void dispatchOnBackProgressed(IOnBackInvokedCallback iOnBackInvokedCallback, BackEvent backEvent) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackProgressed(backEvent);
            } catch (RemoteException e) {
                Log.e(TAG, "dispatchOnBackProgressed error: ", e);
            }
        }
    }

    public void setTriggerBack(boolean z) {
        if (!this.mTransitionInProgress) {
            this.mTriggerBack = z;
        }
    }

    /* access modifiers changed from: private */
    public void setSwipeThresholds(float f, float f2) {
        this.mProgressThreshold = f2;
        this.mTriggerThreshold = f;
    }

    private void finishAnimation() {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, "BackAnimationController: finishAnimation()", new Object[0]);
        this.mBackGestureStarted = false;
        this.mTouchEventDelta.set(0, 0);
        this.mInitTouchLocation.set(0.0f, 0.0f);
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        boolean z = this.mTriggerBack;
        this.mBackNavigationInfo = null;
        this.mTriggerBack = false;
        if (backNavigationInfo != null) {
            RemoteAnimationTarget departingAnimationTarget = backNavigationInfo.getDepartingAnimationTarget();
            if (!(departingAnimationTarget == null || departingAnimationTarget.leash == null || !departingAnimationTarget.leash.isValid())) {
                this.mTransaction.remove(departingAnimationTarget.leash);
            }
            SurfaceControl screenshotSurface = backNavigationInfo.getScreenshotSurface();
            if (screenshotSurface != null && screenshotSurface.isValid()) {
                this.mTransaction.remove(screenshotSurface);
            }
            this.mTransaction.apply();
            stopTransition();
            backNavigationInfo.onBackNavigationFinished(z);
        }
    }

    private void startTransition() {
        if (!this.mTransitionInProgress) {
            this.mTransitionInProgress = true;
            this.mShellExecutor.executeDelayed(this.mResetTransitionRunnable, 2000);
        }
    }

    private void stopTransition() {
        if (this.mTransitionInProgress) {
            this.mShellExecutor.removeCallbacks(this.mResetTransitionRunnable);
            this.mTransitionInProgress = false;
        }
    }
}
