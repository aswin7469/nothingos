package com.android.systemui.recents;

import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.icu.text.DateFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.InputDeviceCompat;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.util.ScreenshotHelper;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.shared.recents.ISystemUiProxy;
import com.android.systemui.shared.recents.model.Task;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import com.android.systemui.statusbar.policy.CallbackController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Inject;

@SysUISingleton
public class OverviewProxyService extends CurrentUserTracker implements CallbackController<OverviewProxyListener>, NavigationModeController.ModeChangedListener, Dumpable {
    private static final String ACTION_QUICKSTEP = "android.intent.action.QUICKSTEP_SERVICE";
    private static final long BACKOFF_MILLIS = 1000;
    private static final long DEFERRED_CALLBACK_MILLIS = 5000;
    private static final long MAX_BACKOFF_MILLIS = 600000;
    public static final String TAG_OPS = "OverviewProxyService";
    private Region mActiveNavBarRegion;
    /* access modifiers changed from: private */
    public final Optional<BackAnimation> mBackAnimation;
    private boolean mBound;
    /* access modifiers changed from: private */
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    /* access modifiers changed from: private */
    public final CommandQueue mCommandQueue;
    /* access modifiers changed from: private */
    public int mConnectionBackoffAttempts;
    /* access modifiers changed from: private */
    public final List<OverviewProxyListener> mConnectionCallbacks = new ArrayList();
    private final Runnable mConnectionRunnable = new OverviewProxyService$$ExternalSyntheticLambda2(this);
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentBoundedUserId = -1;
    /* access modifiers changed from: private */
    public final Runnable mDeferredConnectionCallback = new OverviewProxyService$$ExternalSyntheticLambda3(this);
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public long mInputFocusTransferStartMillis;
    /* access modifiers changed from: private */
    public float mInputFocusTransferStartY;
    /* access modifiers changed from: private */
    public boolean mInputFocusTransferStarted;
    private boolean mIsEnabled;
    private final BroadcastReceiver mLauncherStateChangedReceiver;
    private float mNavBarButtonAlpha;
    private final Lazy<NavigationBarController> mNavBarControllerLazy;
    private int mNavBarMode = 0;
    /* access modifiers changed from: private */
    public final Optional<OneHanded> mOneHandedOptional;
    /* access modifiers changed from: private */
    public IOverviewProxy mOverviewProxy;
    private final ServiceConnection mOverviewServiceConnection;
    /* access modifiers changed from: private */
    public final IBinder.DeathRecipient mOverviewServiceDeathRcpt;
    /* access modifiers changed from: private */
    public final Optional<Pip> mPipOptional;
    private final Intent mQuickStepIntent;
    /* access modifiers changed from: private */
    public final Optional<RecentTasks> mRecentTasks;
    private final ComponentName mRecentsComponentName;
    /* access modifiers changed from: private */
    public final ScreenshotHelper mScreenshotHelper;
    /* access modifiers changed from: private */
    public final ShellTransitions mShellTransitions;
    private final BiConsumer<Rect, Rect> mSplitScreenBoundsChangeListener;
    /* access modifiers changed from: private */
    public final Optional<SplitScreen> mSplitScreenOptional;
    /* access modifiers changed from: private */
    public final Optional<StartingSurface> mStartingSurface;
    private final NotificationShadeWindowController mStatusBarWinController;
    private final StatusBarWindowCallback mStatusBarWindowCallback;
    /* access modifiers changed from: private */
    public boolean mSupportsRoundedCornersOnWindows;
    public ISystemUiProxy mSysUiProxy = new ISystemUiProxy.Stub() {
        public Rect getNonMinimizedSplitScreenSecondaryBounds() {
            return null;
        }

        public void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) {
        }

        public void setSplitScreenMinimized(boolean z) {
        }

        public void startScreenPinning(int i) {
            verifyCallerAndClearCallingIdentityPostMain("startScreenPinning", new OverviewProxyService$1$$ExternalSyntheticLambda26(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$startScreenPinning$1$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37170xb5c1a563(int i) {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda11(i));
        }

        public void stopScreenPinning() {
            verifyCallerAndClearCallingIdentityPostMain("stopScreenPinning", new OverviewProxyService$1$$ExternalSyntheticLambda4());
        }

        static /* synthetic */ void lambda$stopScreenPinning$2() {
            try {
                ActivityTaskManager.getService().stopSystemLockTaskMode();
            } catch (RemoteException unused) {
                Log.e(OverviewProxyService.TAG_OPS, "Failed to stop screen pinning");
            }
        }

        public void onStatusBarMotionEvent(MotionEvent motionEvent) {
            verifyCallerAndClearCallingIdentity("onStatusBarMotionEvent", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda2(this, motionEvent));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStatusBarMotionEvent$5$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37165x68b640ae(MotionEvent motionEvent) {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda10(this, motionEvent));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStatusBarMotionEvent$4$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37164xdb7b8f2d(MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
            if (motionEvent.getActionMasked() == 0) {
                centralSurfaces.getPanelController().startExpandLatencyTracking();
            }
            OverviewProxyService.this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda23(this, motionEvent, centralSurfaces));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStatusBarMotionEvent$3$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37163x4e40ddac(MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
            int actionMasked = motionEvent.getActionMasked();
            boolean z = false;
            if (actionMasked == 0) {
                boolean unused = OverviewProxyService.this.mInputFocusTransferStarted = true;
                float unused2 = OverviewProxyService.this.mInputFocusTransferStartY = motionEvent.getY();
                long unused3 = OverviewProxyService.this.mInputFocusTransferStartMillis = motionEvent.getEventTime();
                centralSurfaces.onInputFocusTransfer(OverviewProxyService.this.mInputFocusTransferStarted, false, 0.0f);
            }
            if (actionMasked == 1 || actionMasked == 3) {
                boolean unused4 = OverviewProxyService.this.mInputFocusTransferStarted = false;
                float y = (motionEvent.getY() - OverviewProxyService.this.mInputFocusTransferStartY) / ((float) (motionEvent.getEventTime() - OverviewProxyService.this.mInputFocusTransferStartMillis));
                boolean access$1800 = OverviewProxyService.this.mInputFocusTransferStarted;
                if (actionMasked == 3) {
                    z = true;
                }
                centralSurfaces.onInputFocusTransfer(access$1800, z, y);
            }
            motionEvent.recycle();
        }

        public void onBackPressed() throws RemoteException {
            verifyCallerAndClearCallingIdentityPostMain("onBackPressed", new OverviewProxyService$1$$ExternalSyntheticLambda6(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBackPressed$6$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37161x293ac4ef() {
            sendEvent(0, 4);
            sendEvent(1, 4);
            OverviewProxyService.this.notifyBackAction(true, -1, -1, true, false);
        }

        public void onImeSwitcherPressed() throws RemoteException {
            ((InputMethodManager) OverviewProxyService.this.mContext.getSystemService(InputMethodManager.class)).showInputMethodPickerFromSystem(true, 0);
            OverviewProxyService.this.mUiEventLogger.log(KeyButtonView.NavBarButtonEvent.NAVBAR_IME_SWITCHER_BUTTON_TAP);
        }

        public void setHomeRotationEnabled(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("setHomeRotationEnabled", new OverviewProxyService$1$$ExternalSyntheticLambda21(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setHomeRotationEnabled$7$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37166x950f4370(boolean z) {
            OverviewProxyService.this.notifyHomeRotationEnabled(z);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setHomeRotationEnabled$8$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37167x2249f4f1(boolean z) {
            OverviewProxyService.this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda19(this, z));
        }

        public void notifyTaskbarStatus(boolean z, boolean z2) {
            verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarStatus", new OverviewProxyService$1$$ExternalSyntheticLambda0(this, z, z2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifyTaskbarStatus$9$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37158x3368604d(boolean z, boolean z2) {
            OverviewProxyService.this.onTaskbarStatusUpdated(z, z2);
        }

        public void notifyTaskbarAutohideSuspend(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarAutohideSuspend", new OverviewProxyService$1$$ExternalSyntheticLambda25(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifyTaskbarAutohideSuspend$10$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37157xe976299e(boolean z) {
            OverviewProxyService.this.onTaskbarAutohideSuspend(z);
        }

        private boolean sendEvent(int i, int i2) {
            long uptimeMillis = SystemClock.uptimeMillis();
            KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, i, i2, 0, 0, -1, 0, 72, 257);
            keyEvent.setDisplayId(OverviewProxyService.this.mContext.getDisplay().getDisplayId());
            return InputManager.getInstance().injectInputEvent(keyEvent, 0);
        }

        public void onOverviewShown(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("onOverviewShown", new OverviewProxyService$1$$ExternalSyntheticLambda15(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onOverviewShown$11$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37162xafd5ea06(boolean z) {
            for (int size = OverviewProxyService.this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
                ((OverviewProxyListener) OverviewProxyService.this.mConnectionCallbacks.get(size)).onOverviewShown(z);
            }
        }

        public void setNavBarButtonAlpha(float f, boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("setNavBarButtonAlpha", new OverviewProxyService$1$$ExternalSyntheticLambda1(this, f, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setNavBarButtonAlpha$12$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37168x84a9720c(float f, boolean z) {
            OverviewProxyService.this.notifyNavBarButtonAlphaChanged(f, z);
        }

        public void onAssistantProgress(float f) {
            verifyCallerAndClearCallingIdentityPostMain("onAssistantProgress", new OverviewProxyService$1$$ExternalSyntheticLambda8(this, f));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAssistantProgress$13$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37160xf2976595(float f) {
            OverviewProxyService.this.notifyAssistantProgress(f);
        }

        public void onAssistantGestureCompletion(float f) {
            verifyCallerAndClearCallingIdentityPostMain("onAssistantGestureCompletion", new OverviewProxyService$1$$ExternalSyntheticLambda24(this, f));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAssistantGestureCompletion$14$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37159x6c2f5382(float f) {
            OverviewProxyService.this.notifyAssistantGestureCompletion(f);
        }

        public void startAssistant(Bundle bundle) {
            verifyCallerAndClearCallingIdentityPostMain("startAssistant", new OverviewProxyService$1$$ExternalSyntheticLambda7(this, bundle));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$startAssistant$15$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37169xa2e9bced(Bundle bundle) {
            OverviewProxyService.this.notifyStartAssistant(bundle);
        }

        public void notifyAccessibilityButtonClicked(int i) {
            verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonClicked", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda14(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifyAccessibilityButtonClicked$16$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37152x64c7c9ba(int i) {
            AccessibilityManager.getInstance(OverviewProxyService.this.mContext).notifyAccessibilityButtonClicked(i);
        }

        public void notifyAccessibilityButtonLongClicked() {
            verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonLongClicked", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda16(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifyAccessibilityButtonLongClicked$17$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37153x794b3fd7() {
            Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
            intent.setClassName("android", AccessibilityButtonChooserActivity.class.getName());
            intent.addFlags(268468224);
            OverviewProxyService.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        }

        public void notifySwipeToHomeFinished() {
            verifyCallerAndClearCallingIdentity("notifySwipeToHomeFinished", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda9(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifySwipeToHomeFinished$19$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37155xa379406a() {
            OverviewProxyService.this.mPipOptional.ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda3());
        }

        public void notifySwipeUpGestureStarted() {
            verifyCallerAndClearCallingIdentityPostMain("notifySwipeUpGestureStarted", new OverviewProxyService$1$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifySwipeUpGestureStarted$20$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37156x34743cb9() {
            OverviewProxyService.this.notifySwipeUpGestureStartedInternal();
        }

        public void notifyPrioritizedRotation(int i) {
            verifyCallerAndClearCallingIdentityPostMain("notifyPrioritizedRotation", new OverviewProxyService$1$$ExternalSyntheticLambda18(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$notifyPrioritizedRotation$21$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37154x273e69a4(int i) {
            OverviewProxyService.this.notifyPrioritizedRotationInternal(i);
        }

        public void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task.TaskKey taskKey) {
            OverviewProxyService.this.mScreenshotHelper.provideScreenshot(bundle, rect, insets, taskKey.f344id, taskKey.userId, taskKey.sourceComponent, 3, OverviewProxyService.this.mHandler, (Consumer) null);
        }

        public void expandNotificationPanel() {
            verifyCallerAndClearCallingIdentity("expandNotificationPanel", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda13(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$expandNotificationPanel$22$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37151x7c2cbd80() {
            OverviewProxyService.this.mCommandQueue.handleSystemKey(281);
        }

        public void toggleNotificationPanel() {
            verifyCallerAndClearCallingIdentityPostMain("toggleNotificationPanel", new OverviewProxyService$1$$ExternalSyntheticLambda22(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$toggleNotificationPanel$23$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ void mo37171xeea721bb() {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda17());
        }

        public void notifyGoingToSleepByDoubleClick(int i, int i2) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setTapGoingToSleep(i, i2);
        }

        private boolean verifyCaller(String str) {
            int identifier = Binder.getCallingUserHandle().getIdentifier();
            if (identifier == OverviewProxyService.this.mCurrentBoundedUserId) {
                return true;
            }
            Log.w(OverviewProxyService.TAG_OPS, "Launcher called sysui with invalid user: " + identifier + ", reason: " + str);
            return false;
        }

        private <T> T verifyCallerAndClearCallingIdentity(String str, Supplier<T> supplier) {
            if (!verifyCaller(str)) {
                return null;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return supplier.get();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private void verifyCallerAndClearCallingIdentity(String str, Runnable runnable) {
            verifyCallerAndClearCallingIdentity(str, new OverviewProxyService$1$$ExternalSyntheticLambda20(runnable));
        }

        private void verifyCallerAndClearCallingIdentityPostMain(String str, Runnable runnable) {
            verifyCallerAndClearCallingIdentity(str, new OverviewProxyService$1$$ExternalSyntheticLambda12(this, runnable));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$verifyCallerAndClearCallingIdentityPostMain$25$com-android-systemui-recents-OverviewProxyService$1 */
        public /* synthetic */ Boolean mo37172x171701(Runnable runnable) {
            return Boolean.valueOf(OverviewProxyService.this.mHandler.post(runnable));
        }
    };
    /* access modifiers changed from: private */
    public SysUiState mSysUiState;
    /* access modifiers changed from: private */
    public final KeyguardUnlockAnimationController mSysuiUnlockAnimationController;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    private final IVoiceInteractionSessionListener mVoiceInteractionSessionListener;
    /* access modifiers changed from: private */
    public float mWindowCornerRadius;

    public interface OverviewProxyListener {
        void onAssistantGestureCompletion(float f) {
        }

        void onAssistantProgress(float f) {
        }

        void onConnectionChanged(boolean z) {
        }

        void onHomeRotationEnabled(boolean z) {
        }

        void onNavBarButtonAlphaChanged(float f, boolean z) {
        }

        void onOverviewShown(boolean z) {
        }

        void onPrioritizedRotation(int i) {
        }

        void onQuickScrubStarted() {
        }

        void onQuickStepStarted() {
        }

        void onSwipeUpGestureStarted() {
        }

        void onSystemUiStateChanged(int i) {
        }

        void onTaskbarAutohideSuspend(boolean z) {
        }

        void onTaskbarStatusUpdated(boolean z, boolean z2) {
        }

        void onToggleRecentApps() {
        }

        void startAssistant(Bundle bundle) {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-recents-OverviewProxyService  reason: not valid java name */
    public /* synthetic */ void m2994lambda$new$0$comandroidsystemuirecentsOverviewProxyService() {
        Log.w(TAG_OPS, "Binder supposed established connection but actual connection to service timed out, trying again");
        retryConnectionWithBackoff();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public OverviewProxyService(Context context, CommandQueue commandQueue, Lazy<NavigationBarController> lazy, Lazy<Optional<CentralSurfaces>> lazy2, NavigationModeController navigationModeController, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Optional<Pip> optional, Optional<SplitScreen> optional2, Optional<OneHanded> optional3, Optional<RecentTasks> optional4, Optional<BackAnimation> optional5, Optional<StartingSurface> optional6, BroadcastDispatcher broadcastDispatcher, ShellTransitions shellTransitions, ScreenLifecycle screenLifecycle, UiEventLogger uiEventLogger, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AssistUtils assistUtils, DumpManager dumpManager) {
        super(broadcastDispatcher);
        NotificationShadeWindowController notificationShadeWindowController2 = notificationShadeWindowController;
        SysUiState sysUiState2 = sysUiState;
        C24172 r6 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                OverviewProxyService.this.updateEnabledState();
                OverviewProxyService.this.startConnectionToCurrentUser();
            }
        };
        this.mLauncherStateChangedReceiver = r6;
        this.mOverviewServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                int unused = OverviewProxyService.this.mConnectionBackoffAttempts = 0;
                OverviewProxyService.this.mHandler.removeCallbacks(OverviewProxyService.this.mDeferredConnectionCallback);
                try {
                    iBinder.linkToDeath(OverviewProxyService.this.mOverviewServiceDeathRcpt, 0);
                    OverviewProxyService overviewProxyService = OverviewProxyService.this;
                    int unused2 = overviewProxyService.mCurrentBoundedUserId = overviewProxyService.getCurrentUserId();
                    IOverviewProxy unused3 = OverviewProxyService.this.mOverviewProxy = IOverviewProxy.Stub.asInterface(iBinder);
                    Bundle bundle = new Bundle();
                    bundle.putBinder(QuickStepContract.KEY_EXTRA_SYSUI_PROXY, OverviewProxyService.this.mSysUiProxy.asBinder());
                    bundle.putFloat(QuickStepContract.KEY_EXTRA_WINDOW_CORNER_RADIUS, OverviewProxyService.this.mWindowCornerRadius);
                    bundle.putBoolean(QuickStepContract.KEY_EXTRA_SUPPORTS_WINDOW_CORNERS, OverviewProxyService.this.mSupportsRoundedCornersOnWindows);
                    OverviewProxyService.this.mPipOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda0(bundle));
                    OverviewProxyService.this.mSplitScreenOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda1(bundle));
                    OverviewProxyService.this.mOneHandedOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda2(bundle));
                    bundle.putBinder(QuickStepContract.KEY_EXTRA_SHELL_SHELL_TRANSITIONS, OverviewProxyService.this.mShellTransitions.createExternalInterface().asBinder());
                    OverviewProxyService.this.mStartingSurface.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda3(bundle));
                    bundle.putBinder(QuickStepContract.KEY_EXTRA_UNLOCK_ANIMATION_CONTROLLER, OverviewProxyService.this.mSysuiUnlockAnimationController.asBinder());
                    OverviewProxyService.this.mRecentTasks.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda4(bundle));
                    OverviewProxyService.this.mBackAnimation.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda5(bundle));
                    try {
                        Log.d(OverviewProxyService.TAG_OPS, "OverviewProxyService connected, initializing overview proxy");
                        OverviewProxyService.this.mOverviewProxy.onInitialize(bundle);
                    } catch (RemoteException e) {
                        int unused4 = OverviewProxyService.this.mCurrentBoundedUserId = -1;
                        Log.e(OverviewProxyService.TAG_OPS, "Failed to call onInitialize()", e);
                    }
                    OverviewProxyService.this.dispatchNavButtonBounds();
                    OverviewProxyService.this.updateSystemUiStateFlags();
                    OverviewProxyService overviewProxyService2 = OverviewProxyService.this;
                    overviewProxyService2.notifySystemUiStateFlags(overviewProxyService2.mSysUiState.getFlags());
                    OverviewProxyService.this.notifyConnectionChanged();
                } catch (RemoteException e2) {
                    Log.e(OverviewProxyService.TAG_OPS, "Lost connection to launcher service", e2);
                    OverviewProxyService.this.disconnectFromLauncherService();
                    OverviewProxyService.this.retryConnectionWithBackoff();
                }
            }

            public void onNullBinding(ComponentName componentName) {
                Log.w(OverviewProxyService.TAG_OPS, "Null binding of '" + componentName + "', try reconnecting");
                int unused = OverviewProxyService.this.mCurrentBoundedUserId = -1;
                OverviewProxyService.this.retryConnectionWithBackoff();
            }

            public void onBindingDied(ComponentName componentName) {
                Log.w(OverviewProxyService.TAG_OPS, "Binding died of '" + componentName + "', try reconnecting");
                int unused = OverviewProxyService.this.mCurrentBoundedUserId = -1;
                OverviewProxyService.this.retryConnectionWithBackoff();
            }

            public void onServiceDisconnected(ComponentName componentName) {
                Log.w(OverviewProxyService.TAG_OPS, "Service disconnected");
                int unused = OverviewProxyService.this.mCurrentBoundedUserId = -1;
            }
        };
        OverviewProxyService$$ExternalSyntheticLambda4 overviewProxyService$$ExternalSyntheticLambda4 = new OverviewProxyService$$ExternalSyntheticLambda4(this);
        this.mStatusBarWindowCallback = overviewProxyService$$ExternalSyntheticLambda4;
        this.mSplitScreenBoundsChangeListener = new OverviewProxyService$$ExternalSyntheticLambda5(this);
        this.mOverviewServiceDeathRcpt = new OverviewProxyService$$ExternalSyntheticLambda6(this);
        C24194 r8 = new IVoiceInteractionSessionListener.Stub() {
            public void onSetUiHints(Bundle bundle) {
            }

            public void onVoiceSessionHidden() {
            }

            public void onVoiceSessionShown() {
            }

            public void onVoiceSessionWindowVisibilityChanged(boolean z) {
                OverviewProxyService.this.mContext.getMainExecutor().execute(new OverviewProxyService$4$$ExternalSyntheticLambda0(this, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onVoiceSessionWindowVisibilityChanged$0$com-android-systemui-recents-OverviewProxyService$4 */
            public /* synthetic */ void mo37199x763fedff(boolean z) {
                OverviewProxyService.this.onVoiceSessionWindowVisibilityChanged(z);
            }
        };
        this.mVoiceInteractionSessionListener = r8;
        this.mContext = context;
        this.mPipOptional = optional;
        this.mCentralSurfacesOptionalLazy = lazy2;
        this.mHandler = new Handler();
        this.mNavBarControllerLazy = lazy;
        this.mStatusBarWinController = notificationShadeWindowController2;
        this.mConnectionBackoffAttempts = 0;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(context.getString(17040027));
        this.mRecentsComponentName = unflattenFromString;
        this.mQuickStepIntent = new Intent(ACTION_QUICKSTEP).setPackage(unflattenFromString.getPackageName());
        this.mWindowCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mSupportsRoundedCornersOnWindows = ScreenDecorationsUtils.supportsRoundedCornersOnWindows(context.getResources());
        this.mSysUiState = sysUiState2;
        sysUiState2.addCallback(new OverviewProxyService$$ExternalSyntheticLambda7(this));
        this.mOneHandedOptional = optional3;
        this.mShellTransitions = shellTransitions;
        this.mRecentTasks = optional4;
        this.mBackAnimation = optional5;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarButtonAlpha = 1.0f;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        this.mNavBarMode = navigationModeController.addListener(this);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(unflattenFromString.getPackageName(), 0);
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        context.registerReceiver(r6, intentFilter);
        notificationShadeWindowController2.registerCallback(overviewProxyService$$ExternalSyntheticLambda4);
        this.mScreenshotHelper = new ScreenshotHelper(context);
        commandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void onTracingStateChanged(boolean z) {
                OverviewProxyService.this.mSysUiState.setFlag(4096, z).commitUpdate(OverviewProxyService.this.mContext.getDisplayId());
            }
        });
        this.mCommandQueue = commandQueue;
        this.mSplitScreenOptional = optional2;
        startTracking();
        screenLifecycle.addObserver(new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                OverviewProxyService.this.notifyScreenTurnedOn();
            }
        });
        updateEnabledState();
        startConnectionToCurrentUser();
        this.mStartingSurface = optional6;
        this.mSysuiUnlockAnimationController = keyguardUnlockAnimationController;
        assistUtils.registerVoiceInteractionSessionListener(r8);
    }

    public void onUserSwitched(int i) {
        this.mConnectionBackoffAttempts = 0;
        internalConnectToCurrentUser();
    }

    public void onVoiceSessionWindowVisibilityChanged(boolean z) {
        this.mSysUiState.setFlag(QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING, z).commitUpdate(this.mContext.getDisplayId());
    }

    public void notifyBackAction(boolean z, int i, int i2, boolean z2, boolean z3) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onBackAction(z, i, i2, z2, z3);
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to notify back action", e);
        }
    }

    /* access modifiers changed from: private */
    public void updateSystemUiStateFlags() {
        NavigationBar defaultNavigationBar = this.mNavBarControllerLazy.get().getDefaultNavigationBar();
        NavigationBarView navigationBarView = this.mNavBarControllerLazy.get().getNavigationBarView(this.mContext.getDisplayId());
        NotificationPanelViewController panelController = ((CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get()).getPanelController();
        if (defaultNavigationBar != null) {
            defaultNavigationBar.updateSystemUiStateFlags();
        }
        if (navigationBarView != null) {
            navigationBarView.updateDisabledSystemUiStateFlags(this.mSysUiState);
        }
        if (panelController != null) {
            panelController.updateSystemUiStateFlags();
        }
        NotificationShadeWindowController notificationShadeWindowController = this.mStatusBarWinController;
        if (notificationShadeWindowController != null) {
            notificationShadeWindowController.notifyStateChangedCallbacks();
        }
    }

    /* access modifiers changed from: private */
    public void notifySystemUiStateFlags(int i) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSystemUiStateChanged(i);
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to notify sysui state change", e);
        }
    }

    /* access modifiers changed from: private */
    public void onStatusBarStateChanged(boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5 = true;
        SysUiState flag = this.mSysUiState.setFlag(64, z && !z2);
        if (!z || !z2) {
            z5 = false;
        }
        flag.setFlag(512, z5).setFlag(8, z3).setFlag(2097152, z4).commitUpdate(this.mContext.getDisplayId());
    }

    public void onActiveNavBarRegionChanges(Region region) {
        this.mActiveNavBarRegion = region;
        dispatchNavButtonBounds();
    }

    /* access modifiers changed from: private */
    public void dispatchNavButtonBounds() {
        Region region;
        IOverviewProxy iOverviewProxy = this.mOverviewProxy;
        if (iOverviewProxy != null && (region = this.mActiveNavBarRegion) != null) {
            try {
                iOverviewProxy.onActiveNavBarRegionChanges(region);
            } catch (RemoteException e) {
                Log.e(TAG_OPS, "Failed to call onActiveNavBarRegionChanges()", e);
            }
        }
    }

    public void cleanupAfterDeath() {
        if (this.mInputFocusTransferStarted) {
            this.mHandler.post(new OverviewProxyService$$ExternalSyntheticLambda0(this));
        }
        startConnectionToCurrentUser();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$cleanupAfterDeath$2$com-android-systemui-recents-OverviewProxyService */
    public /* synthetic */ void mo37130x273a363e() {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new OverviewProxyService$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$cleanupAfterDeath$1$com-android-systemui-recents-OverviewProxyService */
    public /* synthetic */ void mo37129xe3af187d(CentralSurfaces centralSurfaces) {
        this.mInputFocusTransferStarted = false;
        centralSurfaces.onInputFocusTransfer(false, true, 0.0f);
    }

    public void startConnectionToCurrentUser() {
        if (this.mHandler.getLooper() != Looper.myLooper()) {
            this.mHandler.post(this.mConnectionRunnable);
        } else {
            internalConnectToCurrentUser();
        }
    }

    /* access modifiers changed from: private */
    public void internalConnectToCurrentUser() {
        disconnectFromLauncherService();
        if (!isEnabled()) {
            Log.v(TAG_OPS, "Cannot attempt connection, is enabled " + isEnabled());
            return;
        }
        this.mHandler.removeCallbacks(this.mConnectionRunnable);
        try {
            this.mBound = this.mContext.bindServiceAsUser(new Intent(ACTION_QUICKSTEP).setPackage(this.mRecentsComponentName.getPackageName()), this.mOverviewServiceConnection, InputDeviceCompat.SOURCE_HDMI, UserHandle.of(getCurrentUserId()));
        } catch (SecurityException e) {
            Log.e(TAG_OPS, "Unable to bind because of security error", e);
        }
        if (this.mBound) {
            this.mHandler.postDelayed(this.mDeferredConnectionCallback, 5000);
        } else {
            retryConnectionWithBackoff();
        }
    }

    /* access modifiers changed from: private */
    public void retryConnectionWithBackoff() {
        if (!this.mHandler.hasCallbacks(this.mConnectionRunnable)) {
            long min = (long) Math.min(Math.scalb(1000.0f, this.mConnectionBackoffAttempts), 600000.0f);
            this.mHandler.postDelayed(this.mConnectionRunnable, min);
            this.mConnectionBackoffAttempts++;
            Log.w(TAG_OPS, "Failed to connect on attempt " + this.mConnectionBackoffAttempts + " will try again in " + min + DateFormat.MINUTE_SECOND);
        }
    }

    public void addCallback(OverviewProxyListener overviewProxyListener) {
        if (!this.mConnectionCallbacks.contains(overviewProxyListener)) {
            this.mConnectionCallbacks.add(overviewProxyListener);
        }
        overviewProxyListener.onConnectionChanged(this.mOverviewProxy != null);
        overviewProxyListener.onNavBarButtonAlphaChanged(this.mNavBarButtonAlpha, false);
    }

    public void removeCallback(OverviewProxyListener overviewProxyListener) {
        this.mConnectionCallbacks.remove((Object) overviewProxyListener);
    }

    public boolean shouldShowSwipeUpUI() {
        return isEnabled() && !QuickStepContract.isLegacyMode(this.mNavBarMode);
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public IOverviewProxy getProxy() {
        return this.mOverviewProxy;
    }

    /* access modifiers changed from: private */
    public void disconnectFromLauncherService() {
        if (this.mBound) {
            this.mContext.unbindService(this.mOverviewServiceConnection);
            this.mBound = false;
        }
        IOverviewProxy iOverviewProxy = this.mOverviewProxy;
        if (iOverviewProxy != null) {
            iOverviewProxy.asBinder().unlinkToDeath(this.mOverviewServiceDeathRcpt, 0);
            this.mOverviewProxy = null;
            notifyNavBarButtonAlphaChanged(1.0f, false);
            notifyConnectionChanged();
        }
    }

    /* access modifiers changed from: private */
    public void notifyNavBarButtonAlphaChanged(float f, boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onNavBarButtonAlphaChanged(f, z);
        }
    }

    /* access modifiers changed from: private */
    public void notifyHomeRotationEnabled(boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onHomeRotationEnabled(z);
        }
    }

    /* access modifiers changed from: private */
    public void onTaskbarStatusUpdated(boolean z, boolean z2) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onTaskbarStatusUpdated(z, z2);
        }
    }

    /* access modifiers changed from: private */
    public void onTaskbarAutohideSuspend(boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onTaskbarAutohideSuspend(z);
        }
    }

    /* access modifiers changed from: private */
    public void notifyConnectionChanged() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onConnectionChanged(this.mOverviewProxy != null);
        }
    }

    public void notifyQuickStepStarted() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onQuickStepStarted();
        }
    }

    /* access modifiers changed from: private */
    public void notifyPrioritizedRotationInternal(int i) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onPrioritizedRotation(i);
        }
    }

    public void notifyQuickScrubStarted() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onQuickScrubStarted();
        }
    }

    /* access modifiers changed from: private */
    public void notifyAssistantProgress(float f) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onAssistantProgress(f);
        }
    }

    /* access modifiers changed from: private */
    public void notifyAssistantGestureCompletion(float f) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onAssistantGestureCompletion(f);
        }
    }

    /* access modifiers changed from: private */
    public void notifyStartAssistant(Bundle bundle) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).startAssistant(bundle);
        }
    }

    /* access modifiers changed from: private */
    public void notifySwipeUpGestureStartedInternal() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onSwipeUpGestureStarted();
        }
    }

    public void notifyAssistantVisibilityChanged(float f) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onAssistantVisibilityChanged(f);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for assistant visibility.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call notifyAssistantVisibilityChanged()", e);
        }
    }

    public void notifySplitScreenBoundsChanged(Rect rect, Rect rect2) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSplitScreenSecondaryBoundsChanged(rect, rect2);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for split screen bounds.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call onSplitScreenSecondaryBoundsChanged()", e);
        }
    }

    public void notifyScreenTurnedOn() {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onScreenTurnedOn();
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for screen turned on event.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call notifyScreenTurnedOn()", e);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyToggleRecentApps() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onToggleRecentApps();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.disable(i, i2, i3, z);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for disable flags.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call disable()", e);
        }
    }

    public void onRotationProposal(int i, boolean z) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onRotationProposal(i, z);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for proposing rotation.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call onRotationProposal()", e);
        }
    }

    public void onSystemBarAttributesChanged(int i, int i2) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSystemBarAttributesChanged(i, i2);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy for system bar attr change.");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call onSystemBarAttributesChanged()", e);
        }
    }

    public void onNavButtonsDarkIntensityChanged(float f) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onNavButtonsDarkIntensityChanged(f);
            } else {
                Log.e(TAG_OPS, "Failed to get overview proxy to update nav buttons dark intensity");
            }
        } catch (RemoteException e) {
            Log.e(TAG_OPS, "Failed to call onNavButtonsDarkIntensityChanged()", e);
        }
    }

    /* access modifiers changed from: private */
    public void updateEnabledState() {
        this.mIsEnabled = this.mContext.getPackageManager().resolveServiceAsUser(this.mQuickStepIntent, 1048576, ActivityManagerWrapper.getInstance().getCurrentUserId()) != null;
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("OverviewProxyService state:");
        printWriter.print("  isConnected=");
        printWriter.println(this.mOverviewProxy != null);
        printWriter.print("  mIsEnabled=");
        printWriter.println(isEnabled());
        printWriter.print("  mRecentsComponentName=");
        printWriter.println((Object) this.mRecentsComponentName);
        printWriter.print("  mQuickStepIntent=");
        printWriter.println((Object) this.mQuickStepIntent);
        printWriter.print("  mBound=");
        printWriter.println(this.mBound);
        printWriter.print("  mCurrentBoundedUserId=");
        printWriter.println(this.mCurrentBoundedUserId);
        printWriter.print("  mConnectionBackoffAttempts=");
        printWriter.println(this.mConnectionBackoffAttempts);
        printWriter.print("  mInputFocusTransferStarted=");
        printWriter.println(this.mInputFocusTransferStarted);
        printWriter.print("  mInputFocusTransferStartY=");
        printWriter.println(this.mInputFocusTransferStartY);
        printWriter.print("  mInputFocusTransferStartMillis=");
        printWriter.println(this.mInputFocusTransferStartMillis);
        printWriter.print("  mWindowCornerRadius=");
        printWriter.println(this.mWindowCornerRadius);
        printWriter.print("  mSupportsRoundedCornersOnWindows=");
        printWriter.println(this.mSupportsRoundedCornersOnWindows);
        printWriter.print("  mNavBarButtonAlpha=");
        printWriter.println(this.mNavBarButtonAlpha);
        printWriter.print("  mActiveNavBarRegion=");
        printWriter.println((Object) this.mActiveNavBarRegion);
        printWriter.print("  mNavBarMode=");
        printWriter.println(this.mNavBarMode);
        this.mSysUiState.dump(printWriter, strArr);
    }
}
