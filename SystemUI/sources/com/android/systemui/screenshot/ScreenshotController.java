package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ExitTransitionCoordinator;
import android.app.ICompatCameraControlCallback;
import android.app.Notification;
import android.app.SharedElementCallback;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.AudioAttributes;
import android.media.AudioSystem;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.ScrollCaptureResponse;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import android.window.WindowContext;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.util.Assert;
import com.google.common.util.concurrent.ListenableFuture;
import com.nothing.systemui.screenshot.ScreenshotControllerEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Inject;

public class ScreenshotController {
    static final String ACTION_TYPE_DELETE = "Delete";
    static final String ACTION_TYPE_EDIT = "Edit";
    static final String ACTION_TYPE_SHARE = "Share";
    static final String EXTRA_ACTION_INTENT = "android:screenshot_action_intent";
    static final String EXTRA_ACTION_TYPE = "android:screenshot_action_type";
    static final String EXTRA_CANCEL_NOTIFICATION = "android:screenshot_cancel_notification";
    static final String EXTRA_DISALLOW_ENTER_PIP = "android:screenshot_disallow_enter_pip";
    static final String EXTRA_ID = "android:screenshot_id";
    static final String EXTRA_OVERRIDE_TRANSITION = "android:screenshot_override_transition";
    static final String EXTRA_SMART_ACTIONS_ENABLED = "android:smart_actions_enabled";
    private static final int SCREENSHOT_CORNER_DEFAULT_TIMEOUT_MILLIS = 6000;
    static final IRemoteAnimationRunner.Stub SCREENSHOT_REMOTE_RUNNER = new IRemoteAnimationRunner.Stub() {
        public void onAnimationCancelled(boolean z) {
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            try {
                iRemoteAnimationFinishedCallback.onAnimationFinished();
            } catch (RemoteException e) {
                Log.e(ScreenshotController.TAG, "Error finishing screenshot remote animation", e);
            }
        }
    };
    static final String SCREENSHOT_URI_ID = "android:screenshot_uri_id";
    private static final String SETTINGS_SECURE_USER_SETUP_COMPLETE = "user_setup_complete";
    /* access modifiers changed from: private */
    public static final String TAG = LogConfig.logTag(ScreenshotController.class);
    private final AccessibilityManager mAccessibilityManager;
    private final ExecutorService mBgExecutor;
    /* access modifiers changed from: private */
    public boolean mBlockAttach;
    private final BroadcastSender mBroadcastSender;
    private final ListenableFuture<MediaPlayer> mCameraSound;
    /* access modifiers changed from: private */
    public final InterestingConfigChanges mConfigChanges;
    /* access modifiers changed from: private */
    public final WindowContext mContext;
    private BroadcastReceiver mCopyBroadcastReceiver;
    private TakeScreenshotService.RequestCallback mCurrentRequestCallback;
    private final DisplayManager mDisplayManager;
    private final ImageExporter mImageExporter;
    private final boolean mIsLowRamDevice;
    private ListenableFuture<ScrollCaptureResponse> mLastScrollCaptureRequest;
    private ScrollCaptureResponse mLastScrollCaptureResponse;
    ListenableFuture<ScrollCaptureController.LongScreenshot> mLongScreenshotFuture;
    private final LongScreenshotData mLongScreenshotHolder;
    private final Executor mMainExecutor;
    private final ScreenshotNotificationsController mNotificationsController;
    private String mPackageName = "";
    private SaveImageInBackgroundTask mSaveInBgTask;
    private Bitmap mScreenBitmap;
    /* access modifiers changed from: private */
    public Animator mScreenshotAnimation;
    /* access modifiers changed from: private */
    public final TimeoutHandler mScreenshotHandler;
    private final ScreenshotSmartActions mScreenshotSmartActions;
    private boolean mScreenshotTakenInPortrait;
    /* access modifiers changed from: private */
    public ScreenshotView mScreenshotView;
    private final ScrollCaptureClient mScrollCaptureClient;
    private final ScrollCaptureController mScrollCaptureController;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    private final PhoneWindow mWindow;
    private final WindowManager.LayoutParams mWindowLayoutParams;
    /* access modifiers changed from: private */
    public final WindowManager mWindowManager;

    interface ActionsReadyListener {
        void onActionsReady(SavedImageData savedImageData);
    }

    interface QuickShareActionReadyListener {
        void onActionsReady(QuickShareData quickShareData);
    }

    interface TransitionDestination {
        void setTransitionDestination(Rect rect, Runnable runnable);
    }

    static class SaveImageInBackgroundData {
        public Consumer<Uri> finisher;
        public Bitmap image;
        public ActionsReadyListener mActionsReadyListener;
        public QuickShareActionReadyListener mQuickShareActionsReadyListener;

        SaveImageInBackgroundData() {
        }

        /* access modifiers changed from: package-private */
        public void clearImage() {
            this.image = null;
        }
    }

    static class SavedImageData {
        public Notification.Action deleteAction;
        public Supplier<ActionTransition> editTransition;
        public Notification.Action quickShareAction;
        public Supplier<ActionTransition> shareTransition;
        public List<Notification.Action> smartActions;
        public Uri uri;

        SavedImageData() {
        }

        static class ActionTransition {
            public Notification.Action action;
            public Bundle bundle;
            public Runnable onCancelRunnable;

            ActionTransition() {
            }
        }

        public void reset() {
            this.uri = null;
            this.shareTransition = null;
            this.editTransition = null;
            this.deleteAction = null;
            this.smartActions = null;
            this.quickShareAction = null;
        }
    }

    static class QuickShareData {
        public Notification.Action quickShareAction;

        QuickShareData() {
        }

        public void reset() {
            this.quickShareAction = null;
        }
    }

    @Inject
    ScreenshotController(Context context, ScreenshotSmartActions screenshotSmartActions, ScreenshotNotificationsController screenshotNotificationsController, ScrollCaptureClient scrollCaptureClient, UiEventLogger uiEventLogger, ImageExporter imageExporter, @Main Executor executor, ScrollCaptureController scrollCaptureController, LongScreenshotData longScreenshotData, ActivityManager activityManager, TimeoutHandler timeoutHandler, BroadcastSender broadcastSender) {
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges(-2147474556);
        this.mConfigChanges = interestingConfigChanges;
        this.mScreenshotSmartActions = screenshotSmartActions;
        this.mNotificationsController = screenshotNotificationsController;
        this.mScrollCaptureClient = scrollCaptureClient;
        this.mUiEventLogger = uiEventLogger;
        this.mImageExporter = imageExporter;
        this.mMainExecutor = executor;
        this.mScrollCaptureController = scrollCaptureController;
        this.mLongScreenshotHolder = longScreenshotData;
        this.mIsLowRamDevice = activityManager.isLowRamDevice();
        this.mBgExecutor = Executors.newSingleThreadExecutor();
        this.mBroadcastSender = broadcastSender;
        this.mScreenshotHandler = timeoutHandler;
        timeoutHandler.setDefaultTimeoutMillis(6000);
        timeoutHandler.setOnTimeoutRunnable(new ScreenshotController$$ExternalSyntheticLambda8(this));
        this.mDisplayManager = (DisplayManager) Objects.requireNonNull((DisplayManager) context.getSystemService(DisplayManager.class));
        WindowContext createWindowContext = context.createDisplayContext(getDefaultDisplay()).createWindowContext(2036, (Bundle) null);
        this.mContext = createWindowContext;
        WindowManager windowManager = (WindowManager) createWindowContext.getSystemService(WindowManager.class);
        this.mWindowManager = windowManager;
        this.mAccessibilityManager = AccessibilityManager.getInstance(createWindowContext);
        WindowManager.LayoutParams floatingWindowParams = FloatingWindowUtil.getFloatingWindowParams();
        this.mWindowLayoutParams = floatingWindowParams;
        floatingWindowParams.setTitle("ScreenshotAnimation");
        PhoneWindow floatingWindow = FloatingWindowUtil.getFloatingWindow(createWindowContext);
        this.mWindow = floatingWindow;
        floatingWindow.setWindowManager(windowManager, (IBinder) null, (String) null);
        interestingConfigChanges.applyNewConfig(context.getResources());
        reloadAssets();
        this.mCameraSound = loadCameraSound();
        C24492 r8 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (ClipboardOverlayController.COPY_OVERLAY_ACTION.equals(intent.getAction())) {
                    ScreenshotController.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_DISMISSED_OTHER);
                    ScreenshotController.this.dismissScreenshot(false);
                }
            }
        };
        this.mCopyBroadcastReceiver = r8;
        createWindowContext.registerReceiver(r8, new IntentFilter(ClipboardOverlayController.COPY_OVERLAY_ACTION), "com.android.systemui.permission.SELF", (Handler) null, 4);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37424xa1bd19de() {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_INTERACTION_TIMEOUT, 0, this.mPackageName);
        dismissScreenshot(false);
    }

    /* access modifiers changed from: package-private */
    public void takeScreenshotFullscreen(ComponentName componentName, Consumer<Uri> consumer, TakeScreenshotService.RequestCallback requestCallback) {
        Assert.isMainThread();
        this.mCurrentRequestCallback = requestCallback;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDefaultDisplay().getRealMetrics(displayMetrics);
        mo37438x926e76e9(componentName, consumer, new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
    }

    /* access modifiers changed from: package-private */
    public void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i, int i2, ComponentName componentName, Consumer<Uri> consumer, TakeScreenshotService.RequestCallback requestCallback) {
        Assert.isMainThread();
        if (bitmap == null) {
            Log.e(TAG, "Got null bitmap from screenshot message");
            this.mNotificationsController.notifyScreenshotError(C1894R.string.screenshot_failed_to_capture_text);
            requestCallback.reportError();
            return;
        }
        boolean z = false;
        if (!aspectRatiosMatch(bitmap, insets, rect)) {
            insets = Insets.NONE;
            rect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            z = true;
        }
        this.mCurrentRequestCallback = requestCallback;
        saveScreenshot(bitmap, consumer, rect, insets, componentName, z);
    }

    /* access modifiers changed from: package-private */
    public void takeScreenshotPartial(ComponentName componentName, Consumer<Uri> consumer, TakeScreenshotService.RequestCallback requestCallback) {
        Assert.isMainThread();
        this.mScreenshotView.reset();
        this.mCurrentRequestCallback = requestCallback;
        attachWindow();
        this.mWindow.setContentView(this.mScreenshotView);
        this.mScreenshotView.requestApplyInsets();
        this.mScreenshotView.takePartialScreenshot(new ScreenshotController$$ExternalSyntheticLambda18(this, componentName, consumer));
    }

    /* access modifiers changed from: package-private */
    public void dismissScreenshot(boolean z) {
        if (z || !this.mScreenshotView.isDismissing()) {
            this.mScreenshotHandler.cancelTimeout();
            if (z) {
                finishDismiss();
            } else {
                this.mScreenshotView.animateDismissal();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isPendingSharedTransition() {
        return this.mScreenshotView.isPendingSharedTransition();
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        removeWindow();
        releaseMediaPlayer();
        releaseContext();
        this.mBgExecutor.shutdownNow();
    }

    private void releaseContext() {
        this.mContext.unregisterReceiver(this.mCopyBroadcastReceiver);
        this.mContext.release();
    }

    private void releaseMediaPlayer() {
        try {
            MediaPlayer mediaPlayer = this.mCameraSound.get();
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        } catch (InterruptedException | ExecutionException unused) {
        }
    }

    private void reloadAssets() {
        ScreenshotView screenshotView = (ScreenshotView) LayoutInflater.from(this.mContext).inflate(C1894R.layout.screenshot, (ViewGroup) null);
        this.mScreenshotView = screenshotView;
        screenshotView.init(this.mUiEventLogger, new ScreenshotView.ScreenshotViewCallback() {
            public void onUserInteraction() {
                ScreenshotController.this.mScreenshotHandler.resetTimeout();
            }

            public void onDismiss() {
                ScreenshotController.this.finishDismiss();
            }

            public void onTouchOutside() {
                ScreenshotController.this.setWindowFocusable(false);
            }
        });
        this.mScreenshotView.setDefaultTimeoutMillis((long) this.mScreenshotHandler.getDefaultTimeoutMillis());
        this.mScreenshotView.setOnKeyListener(new ScreenshotController$$ExternalSyntheticLambda2(this));
        this.mScreenshotView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mScreenshotView);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$reloadAssets$2$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ boolean mo37428x396f9d66(View view, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        dismissScreenshot(false);
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: takeScreenshotInternal */
    public void mo37438x926e76e9(ComponentName componentName, Consumer<Uri> consumer, Rect rect) {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mScreenshotTakenInPortrait = z;
        Rect rect2 = new Rect(rect);
        Bitmap captureScreenshot = captureScreenshot(rect);
        if (captureScreenshot == null) {
            Log.e(TAG, "takeScreenshotInternal: Screenshot bitmap was null");
            this.mNotificationsController.notifyScreenshotError(C1894R.string.screenshot_failed_to_capture_text);
            TakeScreenshotService.RequestCallback requestCallback = this.mCurrentRequestCallback;
            if (requestCallback != null) {
                requestCallback.reportError();
                return;
            }
            return;
        }
        saveScreenshot(captureScreenshot, consumer, rect2, Insets.NONE, componentName, true);
        this.mBroadcastSender.sendBroadcast(new Intent(ClipboardOverlayController.SCREENSHOT_ACTION), "com.android.systemui.permission.SELF");
    }

    private Bitmap captureScreenshot(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        Display defaultDisplay = getDefaultDisplay();
        DisplayAddress.Physical address = defaultDisplay.getAddress();
        if (!(address instanceof DisplayAddress.Physical)) {
            Log.e(TAG, "Skipping Screenshot - Default display does not have a physical address: " + defaultDisplay);
            return null;
        }
        SurfaceControl.ScreenshotHardwareBuffer captureDisplay = SurfaceControl.captureDisplay(new SurfaceControl.DisplayCaptureArgs.Builder(SurfaceControl.getPhysicalDisplayToken(address.getPhysicalDisplayId())).setSourceCrop(rect).setSize(width, height).build());
        if (captureDisplay == null) {
            return null;
        }
        return captureDisplay.asBitmap();
    }

    private void saveScreenshot(Bitmap bitmap, Consumer<Uri> consumer, final Rect rect, Insets insets, ComponentName componentName, final boolean z) {
        String str;
        withWindowAttached(new ScreenshotController$$ExternalSyntheticLambda19(this));
        if (this.mScreenshotView.isAttachedToWindow()) {
            if (!this.mScreenshotView.isDismissing()) {
                this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_REENTERED, 0, this.mPackageName);
            }
            this.mScreenshotView.reset();
        }
        if (componentName == null) {
            str = "";
        } else {
            str = componentName.getPackageName();
        }
        this.mPackageName = str;
        this.mScreenshotView.setPackageName(str);
        this.mScreenshotView.updateOrientation(this.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
        this.mScreenBitmap = bitmap;
        if (!isUserSetupComplete()) {
            Log.w(TAG, "User setup not complete, displaying toast only");
            saveScreenshotAndToast(consumer);
            return;
        }
        this.mScreenBitmap.setHasAlpha(false);
        this.mScreenBitmap.prepareToDraw();
        saveScreenshotInWorkerThread(consumer, new ScreenshotController$$ExternalSyntheticLambda20(this), new ScreenshotController$$ExternalSyntheticLambda21(this));
        setWindowFocusable(true);
        withWindowAttached(new ScreenshotController$$ExternalSyntheticLambda22(this));
        attachWindow();
        this.mScreenshotView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                ScreenshotController.this.mScreenshotView.getViewTreeObserver().removeOnPreDrawListener(this);
                ScreenshotController.this.startAnimation(rect, z);
                return true;
            }
        });
        this.mScreenshotView.setScreenshot(this.mScreenBitmap, insets);
        setContentView(this.mScreenshotView);
        this.mWindow.getDecorView().setOnApplyWindowInsetsListener(new ScreenshotController$$ExternalSyntheticLambda1());
        this.mScreenshotHandler.cancelTimeout();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$saveScreenshot$3$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37432xebf89b1e() {
        this.mScreenshotView.announceForAccessibility(this.mContext.getResources().getString(C1894R.string.screenshot_saving_title));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$saveScreenshot$4$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37433x61419bd() {
        requestScrollCapture();
        this.mWindow.peekDecorView().getViewRootImpl().setActivityConfigCallback(new ViewRootImpl.ActivityConfigCallback() {
            public void onConfigurationChanged(Configuration configuration, int i) {
                if (ScreenshotController.this.mConfigChanges.applyNewConfig(ScreenshotController.this.mContext.getResources())) {
                    ScreenshotController.this.mScreenshotView.hideScrollChip();
                    ScreenshotController.this.mScreenshotHandler.postDelayed(new ScreenshotController$4$$ExternalSyntheticLambda0(ScreenshotController.this), 150);
                    ScreenshotController.this.mScreenshotView.updateInsets(ScreenshotController.this.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
                    if (ScreenshotController.this.mScreenshotAnimation != null && ScreenshotController.this.mScreenshotAnimation.isRunning()) {
                        ScreenshotController.this.mScreenshotAnimation.end();
                    }
                }
            }

            public void requestCompatCameraControl(boolean z, boolean z2, ICompatCameraControlCallback iCompatCameraControlCallback) {
                Log.w(ScreenshotController.TAG, "Unexpected requestCompatCameraControl callback");
            }
        });
    }

    /* access modifiers changed from: private */
    public void requestScrollCapture() {
        if (!allowLongScreenshots()) {
            Log.d(TAG, "Long screenshots not supported on this device");
            return;
        }
        this.mScrollCaptureClient.setHostWindowToken(this.mWindow.getDecorView().getWindowToken());
        ListenableFuture<ScrollCaptureResponse> listenableFuture = this.mLastScrollCaptureRequest;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
        }
        ListenableFuture<ScrollCaptureResponse> request = this.mScrollCaptureClient.request(0);
        this.mLastScrollCaptureRequest = request;
        request.addListener(new ScreenshotController$$ExternalSyntheticLambda9(this, request), this.mMainExecutor);
    }

    /* access modifiers changed from: private */
    /* renamed from: onScrollCaptureResponseReady */
    public void mo37429x79cdd634(Future<ScrollCaptureResponse> future) {
        try {
            ScrollCaptureResponse scrollCaptureResponse = this.mLastScrollCaptureResponse;
            if (scrollCaptureResponse != null) {
                scrollCaptureResponse.close();
                this.mLastScrollCaptureResponse = null;
            }
            if (!future.isCancelled()) {
                ScrollCaptureResponse scrollCaptureResponse2 = future.get();
                this.mLastScrollCaptureResponse = scrollCaptureResponse2;
                if (!scrollCaptureResponse2.isConnected()) {
                    Log.d(TAG, "ScrollCapture: " + this.mLastScrollCaptureResponse.getDescription() + " [" + this.mLastScrollCaptureResponse.getWindowTitle() + NavigationBarInflaterView.SIZE_MOD_END);
                    return;
                }
                Log.d(TAG, "ScrollCapture: connected to window [" + this.mLastScrollCaptureResponse.getWindowTitle() + NavigationBarInflaterView.SIZE_MOD_END);
                ScrollCaptureResponse scrollCaptureResponse3 = this.mLastScrollCaptureResponse;
                this.mScreenshotView.showScrollChip(scrollCaptureResponse3.getPackageName(), new ScreenshotController$$ExternalSyntheticLambda10(this, scrollCaptureResponse3));
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "requestScrollCapture failed", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onScrollCaptureResponseReady$8$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37426xdfd75c34(ScrollCaptureResponse scrollCaptureResponse) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDefaultDisplay().getRealMetrics(displayMetrics);
        this.mScreenshotView.prepareScrollingTransition(scrollCaptureResponse, this.mScreenBitmap, captureScreenshot(new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)), this.mScreenshotTakenInPortrait);
        this.mScreenshotView.post(new ScreenshotController$$ExternalSyntheticLambda6(this, scrollCaptureResponse));
    }

    /* access modifiers changed from: private */
    /* renamed from: runBatchScrollCapture */
    public void mo37425xc5bbdd95(ScrollCaptureResponse scrollCaptureResponse) {
        this.mLastScrollCaptureResponse = null;
        ListenableFuture<ScrollCaptureController.LongScreenshot> listenableFuture = this.mLongScreenshotFuture;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
        }
        ListenableFuture<ScrollCaptureController.LongScreenshot> run = this.mScrollCaptureController.run(scrollCaptureResponse);
        this.mLongScreenshotFuture = run;
        run.addListener(new ScreenshotController$$ExternalSyntheticLambda3(this), this.mMainExecutor);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$runBatchScrollCapture$10$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37430x1ee2c497() {
        try {
            ScrollCaptureController.LongScreenshot longScreenshot = this.mLongScreenshotFuture.get();
            if (longScreenshot.getHeight() == 0) {
                this.mScreenshotView.restoreNonScrollingUi();
                return;
            }
            this.mLongScreenshotHolder.setLongScreenshot(longScreenshot);
            this.mLongScreenshotHolder.setTransitionDestinationCallback(new ScreenshotController$$ExternalSyntheticLambda13(this, longScreenshot));
            Intent intent = new Intent(this.mContext, LongScreenshotActivity.class);
            intent.setFlags(335544320);
            WindowContext windowContext = this.mContext;
            windowContext.startActivity(intent, ActivityOptions.makeCustomAnimation(windowContext, 0, 0).toBundle());
            try {
                WindowManagerGlobal.getWindowManagerService().overridePendingAppTransitionRemote(new RemoteAnimationAdapter(SCREENSHOT_REMOTE_RUNNER, 0, 0), 0);
            } catch (Exception e) {
                Log.e(TAG, "Error overriding screenshot app transition", e);
            }
        } catch (CancellationException unused) {
            Log.e(TAG, "Long screenshot cancelled");
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "Exception", e2);
            this.mScreenshotView.restoreNonScrollingUi();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$runBatchScrollCapture$9$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37431x61d93a0b(ScrollCaptureController.LongScreenshot longScreenshot, Rect rect, Runnable runnable) {
        this.mScreenshotView.startLongScreenshotTransition(rect, runnable, longScreenshot);
    }

    private void withWindowAttached(final Runnable runnable) {
        final View decorView = this.mWindow.getDecorView();
        if (decorView.isAttachedToWindow()) {
            runnable.run();
        } else {
            decorView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                public void onWindowDetached() {
                }

                public void onWindowAttached() {
                    boolean unused = ScreenshotController.this.mBlockAttach = false;
                    decorView.getViewTreeObserver().removeOnWindowAttachListener(this);
                    runnable.run();
                }
            });
        }
    }

    private void setContentView(View view) {
        this.mWindow.setContentView(view);
    }

    private void attachWindow() {
        View decorView = this.mWindow.getDecorView();
        if (!decorView.isAttachedToWindow() && !this.mBlockAttach) {
            this.mBlockAttach = true;
            this.mWindowManager.addView(decorView, this.mWindowLayoutParams);
            decorView.requestApplyInsets();
        }
    }

    /* access modifiers changed from: package-private */
    public void removeWindow() {
        View peekDecorView = this.mWindow.peekDecorView();
        if (peekDecorView != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(peekDecorView);
        }
        ScreenshotView screenshotView = this.mScreenshotView;
        if (screenshotView != null) {
            screenshotView.stopInputListening();
        }
    }

    private ListenableFuture<MediaPlayer> loadCameraSound() {
        return CallbackToFutureAdapter.getFuture(new ScreenshotController$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$loadCameraSound$12$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ Object mo37423xb98335af(CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mBgExecutor.execute(new ScreenshotController$$ExternalSyntheticLambda12(this, completer));
        return "ScreenshotController#loadCameraSound";
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$loadCameraSound$11$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37422x9f67b710(CallbackToFutureAdapter.Completer completer) {
        WindowContext windowContext = this.mContext;
        completer.set(MediaPlayer.create(windowContext, Uri.fromFile(ScreenshotControllerEx.getScreenshotSound(windowContext)), (SurfaceHolder) null, new AudioAttributes.Builder().setUsage(13).setContentType(4).build(), AudioSystem.newAudioSessionId()));
    }

    private void playCameraSound() {
        if (!ScreenshotControllerEx.isScreenshotSoundEnable(this.mContext)) {
            NTLogUtil.m1686d(TAG, "Screenshot Sound is disable.");
        } else {
            this.mCameraSound.addListener(new ScreenshotController$$ExternalSyntheticLambda15(this), this.mBgExecutor);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$playCameraSound$13$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37427x8ab21340() {
        try {
            MediaPlayer mediaPlayer = this.mCameraSound.get();
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        } catch (InterruptedException | ExecutionException unused) {
        }
    }

    private void saveScreenshotAndToast(Consumer<Uri> consumer) {
        playCameraSound();
        saveScreenshotInWorkerThread(consumer, new ScreenshotController$$ExternalSyntheticLambda17(this, consumer), (QuickShareActionReadyListener) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$saveScreenshotAndToast$15$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37435x5b040d35(Consumer consumer, SavedImageData savedImageData) {
        consumer.accept(savedImageData.uri);
        if (savedImageData.uri == null) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_NOT_SAVED, 0, this.mPackageName);
            this.mNotificationsController.notifyScreenshotError(C1894R.string.screenshot_failed_to_save_text);
            return;
        }
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SAVED, 0, this.mPackageName);
        this.mScreenshotHandler.post(new ScreenshotController$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$saveScreenshotAndToast$14$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37434x40e88e96() {
        Toast.makeText(this.mContext, C1894R.string.screenshot_saved_title, 0).show();
    }

    /* access modifiers changed from: private */
    public void startAnimation(Rect rect, boolean z) {
        Animator animator = this.mScreenshotAnimation;
        if (animator != null && animator.isRunning()) {
            this.mScreenshotAnimation.cancel();
        }
        this.mScreenshotAnimation = this.mScreenshotView.createScreenshotDropInAnimation(rect, z);
        playCameraSound();
        this.mScreenshotAnimation.start();
    }

    /* access modifiers changed from: private */
    public void finishDismiss() {
        ListenableFuture<ScrollCaptureResponse> listenableFuture = this.mLastScrollCaptureRequest;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
            this.mLastScrollCaptureRequest = null;
        }
        ScrollCaptureResponse scrollCaptureResponse = this.mLastScrollCaptureResponse;
        if (scrollCaptureResponse != null) {
            scrollCaptureResponse.close();
            this.mLastScrollCaptureResponse = null;
        }
        ListenableFuture<ScrollCaptureController.LongScreenshot> listenableFuture2 = this.mLongScreenshotFuture;
        if (listenableFuture2 != null) {
            listenableFuture2.cancel(true);
        }
        TakeScreenshotService.RequestCallback requestCallback = this.mCurrentRequestCallback;
        if (requestCallback != null) {
            requestCallback.onFinish();
            this.mCurrentRequestCallback = null;
        }
        this.mScreenshotView.reset();
        removeWindow();
        this.mScreenshotHandler.cancelTimeout();
    }

    private void saveScreenshotInWorkerThread(Consumer<Uri> consumer, ActionsReadyListener actionsReadyListener, QuickShareActionReadyListener quickShareActionReadyListener) {
        SaveImageInBackgroundData saveImageInBackgroundData = new SaveImageInBackgroundData();
        saveImageInBackgroundData.image = this.mScreenBitmap;
        saveImageInBackgroundData.finisher = consumer;
        saveImageInBackgroundData.mActionsReadyListener = actionsReadyListener;
        saveImageInBackgroundData.mQuickShareActionsReadyListener = quickShareActionReadyListener;
        SaveImageInBackgroundTask saveImageInBackgroundTask = this.mSaveInBgTask;
        if (saveImageInBackgroundTask != null) {
            saveImageInBackgroundTask.setActionsReadyListener(new ScreenshotController$$ExternalSyntheticLambda11(this));
        }
        SaveImageInBackgroundTask saveImageInBackgroundTask2 = new SaveImageInBackgroundTask(this.mContext, this.mImageExporter, this.mScreenshotSmartActions, saveImageInBackgroundData, getActionTransitionSupplier());
        this.mSaveInBgTask = saveImageInBackgroundTask2;
        saveImageInBackgroundTask2.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void showUiOnActionsReady(SavedImageData savedImageData) {
        logSuccessOnActionsReady(savedImageData);
        this.mScreenshotHandler.resetTimeout();
        if (savedImageData.uri != null) {
            this.mScreenshotHandler.post(new ScreenshotController$$ExternalSyntheticLambda16(this, savedImageData));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUiOnActionsReady$16$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37436x970a5a97(final SavedImageData savedImageData) {
        Animator animator = this.mScreenshotAnimation;
        if (animator == null || !animator.isRunning()) {
            this.mScreenshotView.setChipIntents(savedImageData);
        } else {
            this.mScreenshotAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    ScreenshotController.this.mScreenshotView.setChipIntents(savedImageData);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void showUiOnQuickShareActionReady(QuickShareData quickShareData) {
        if (quickShareData.quickShareAction != null) {
            this.mScreenshotHandler.post(new ScreenshotController$$ExternalSyntheticLambda14(this, quickShareData));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUiOnQuickShareActionReady$17$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37437x70a5cdd1(final QuickShareData quickShareData) {
        Animator animator = this.mScreenshotAnimation;
        if (animator == null || !animator.isRunning()) {
            this.mScreenshotView.addQuickShareChip(quickShareData.quickShareAction);
        } else {
            this.mScreenshotAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    ScreenshotController.this.mScreenshotView.addQuickShareChip(quickShareData.quickShareAction);
                }
            });
        }
    }

    private Supplier<SavedImageData.ActionTransition> getActionTransitionSupplier() {
        return new ScreenshotController$$ExternalSyntheticLambda7(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getActionTransitionSupplier$19$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ SavedImageData.ActionTransition mo37421x46f594f1() {
        Pair startSharedElementAnimation = ActivityOptions.startSharedElementAnimation(this.mWindow, new ScreenshotExitTransitionCallbacksSupplier(true).get(), (SharedElementCallback) null, new Pair[]{Pair.create(this.mScreenshotView.getScreenshotPreview(), "screenshot_preview_image")});
        ((ExitTransitionCoordinator) startSharedElementAnimation.second).startExit();
        SavedImageData.ActionTransition actionTransition = new SavedImageData.ActionTransition();
        actionTransition.bundle = ((ActivityOptions) startSharedElementAnimation.first).toBundle();
        actionTransition.onCancelRunnable = new ScreenshotController$$ExternalSyntheticLambda0(this);
        return actionTransition;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getActionTransitionSupplier$18$com-android-systemui-screenshot-ScreenshotController */
    public /* synthetic */ void mo37420x2cda1652() {
        ActivityOptions.stopSharedElementAnimation(this.mWindow);
    }

    /* access modifiers changed from: private */
    public void logSuccessOnActionsReady(SavedImageData savedImageData) {
        if (savedImageData.uri == null) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_NOT_SAVED, 0, this.mPackageName);
            this.mNotificationsController.notifyScreenshotError(C1894R.string.screenshot_failed_to_save_text);
            return;
        }
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SAVED, 0, this.mPackageName);
    }

    private boolean isUserSetupComplete() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "user_setup_complete", 0) == 1;
    }

    /* access modifiers changed from: private */
    public void setWindowFocusable(boolean z) {
        View peekDecorView;
        int i = this.mWindowLayoutParams.flags;
        if (z) {
            this.mWindowLayoutParams.flags &= -9;
        } else {
            this.mWindowLayoutParams.flags |= 8;
        }
        if (this.mWindowLayoutParams.flags != i && (peekDecorView = this.mWindow.peekDecorView()) != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.updateViewLayout(peekDecorView, this.mWindowLayoutParams);
        }
    }

    private Display getDefaultDisplay() {
        return this.mDisplayManager.getDisplay(0);
    }

    private boolean allowLongScreenshots() {
        return !this.mIsLowRamDevice;
    }

    private static boolean aspectRatiosMatch(Bitmap bitmap, Insets insets, Rect rect) {
        int width = (bitmap.getWidth() - insets.left) - insets.right;
        int height = (bitmap.getHeight() - insets.top) - insets.bottom;
        if (height == 0 || width == 0 || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || Math.abs((((float) width) / ((float) height)) - (((float) rect.width()) / ((float) rect.height()))) >= 0.1f) {
            return false;
        }
        return true;
    }

    private class ScreenshotExitTransitionCallbacksSupplier implements Supplier<ExitTransitionCoordinator.ExitTransitionCallbacks> {
        final boolean mDismissOnHideSharedElements;

        ScreenshotExitTransitionCallbacksSupplier(boolean z) {
            this.mDismissOnHideSharedElements = z;
        }

        public ExitTransitionCoordinator.ExitTransitionCallbacks get() {
            return new ExitTransitionCoordinator.ExitTransitionCallbacks() {
                public boolean isReturnTransitionAllowed() {
                    return false;
                }

                public void onFinish() {
                }

                public void hideSharedElements() {
                    if (ScreenshotExitTransitionCallbacksSupplier.this.mDismissOnHideSharedElements) {
                        ScreenshotController.this.finishDismiss();
                    }
                }
            };
        }
    }
}
