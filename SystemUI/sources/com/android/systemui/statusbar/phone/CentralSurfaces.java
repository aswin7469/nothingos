package com.android.systemui.statusbar.phone;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.p019wm.shell.transition.Transitions;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.RemoteTransitionAdapter;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.GestureRecorder;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import java.p026io.PrintWriter;
import sun.util.locale.LanguageTag;

public interface CentralSurfaces extends Dumpable, ActivityStarter, LifecycleOwner {
    public static final String ACTION_FAKE_ARTWORK = "fake_artwork";
    public static final int[] CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES = {39, 82, 139, 213, 0, 127};
    public static final long[] CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS = {20, 20, 20, 20, 100, 20};
    public static final boolean CHATTY = false;
    public static final boolean CLOSE_PANEL_WHEN_EMPTIED = true;
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_CAMERA_LIFT = false;
    public static final boolean DEBUG_GESTURES = false;
    public static final boolean DEBUG_MEDIA_FAKE_ARTWORK = false;
    public static final boolean DEBUG_WINDOW_STATE = false;
    public static final boolean DUMPTRUCK = true;
    public static final boolean ENABLE_LOCKSCREEN_WALLPAPER = true;
    public static final int FADE_KEYGUARD_DURATION = 300;
    public static final int FADE_KEYGUARD_DURATION_PULSING = 96;
    public static final int FADE_KEYGUARD_START_DELAY = 100;
    public static final long LAUNCH_TRANSITION_TIMEOUT_MS = 5000;
    public static final int MSG_DISMISS_KEYBOARD_SHORTCUTS_MENU = 1027;
    public static final boolean MULTIUSER_DEBUG = false;
    public static final boolean SHOW_LOCKSCREEN_MEDIA_ARTWORK = true;
    public static final boolean SPEW = false;
    public static final String SYSTEM_DIALOG_REASON_DREAM = "dream";
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final String SYSTEM_DIALOG_REASON_SCREENSHOT = "screenshot";
    public static final String TAG = "CentralSurfaces";

    void acquireGestureWakeLock(long j);

    void animateCollapsePanels(int i, boolean z);

    void animateCollapseQuickSettings();

    void animateExpandNotificationsPanel();

    void animateExpandSettingsPanel(String str);

    void animateKeyguardUnoccluding();

    boolean areNotificationAlertsDisabled();

    void awakenDreams();

    void checkBarModes();

    void clearNotificationEffects();

    void clearTransient();

    void collapsePanelOnMainThread();

    void collapsePanelWithDuration(int i);

    void collapseShade();

    void createAndAddWindows(RegisterStatusBarResult registerStatusBarResult);

    void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z);

    boolean dispatchKeyEventPreIme(KeyEvent keyEvent);

    void dump(PrintWriter printWriter, String[] strArr);

    void endAffordanceLaunch();

    void executeRunnableDismissingKeyguard(Runnable runnable, Runnable runnable2, boolean z, boolean z2, boolean z3);

    void executeRunnableDismissingKeyguard(Runnable runnable, Runnable runnable2, boolean z, boolean z2, boolean z3, boolean z4);

    void extendDozePulse();

    void fadeKeyguardAfterLaunchTransition(Runnable runnable, Runnable runnable2, Runnable runnable3);

    void fadeKeyguardWhilePulsing();

    void finishKeyguardFadingAway();

    View getAmbientIndicationContainer();

    int getBarMode();

    BiometricUnlockController getBiometricUnlockController();

    ViewGroup getBouncerContainer();

    int getDisabled1();

    int getDisabled2();

    float getDisplayDensity();

    float getDisplayHeight();

    int getDisplayId();

    float getDisplayWidth();

    Intent getEmergencyActionIntent();

    GestureRecorder getGestureRecorder();

    NotificationGutsManager getGutsManager();

    KeyguardBottomAreaView getKeyguardBottomAreaView();

    Lifecycle getLifecycle();

    LightRevealScrim getLightRevealScrim();

    NavigationBarView getNavigationBarView();

    NotificationPanelViewController getNotificationPanelViewController();

    ViewGroup getNotificationScrollLayout();

    NotificationShadeWindowView getNotificationShadeWindowView();

    NotificationShadeWindowViewController getNotificationShadeWindowViewController();

    NotificationPanelViewController getPanelController();

    NotificationPresenter getPresenter();

    QSPanelController getQSPanelController();

    int getRotation();

    int getStatusBarHeight();

    int getWakefulnessState();

    boolean hideKeyguard();

    boolean hideKeyguardImpl(boolean z);

    void instantCollapseNotificationPanel();

    boolean interceptMediaKey(KeyEvent keyEvent);

    boolean isBouncerShowing();

    boolean isBouncerShowingOverDream();

    boolean isBouncerShowingScrimmed();

    boolean isCameraAllowedByAdmin();

    boolean isDeviceInVrMode();

    boolean isDeviceInteractive();

    boolean isExpandedVisible();

    boolean isFalsingThresholdNeeded();

    boolean isGoingToSleep();

    boolean isInLaunchTransition();

    boolean isKeyguardSecure();

    boolean isKeyguardShowing();

    boolean isLaunchingActivityOverLockscreen();

    boolean isOccluded();

    boolean isOverviewEnabled();

    boolean isPanelExpanded();

    boolean isPulsing();

    boolean isScreenFullyOff();

    boolean isShadeDisabled();

    boolean isWakeUpComingFromTouch();

    void keyguardGoingAway();

    void makeExpandedVisible(boolean z);

    void notifyBiometricAuthModeChanged();

    boolean onBackPressed();

    void onBouncerPreHideAnimation();

    void onCameraHintStarted();

    void onClosingFinished();

    void onHintFinished();

    void onInputFocusTransfer(boolean z, boolean z2, float f);

    void onKeyguardViewManagerStatesUpdated();

    void onLaunchAnimationCancelled(boolean z);

    void onLaunchAnimationEnd(boolean z);

    boolean onMenuPressed();

    void onPhoneHintStarted();

    boolean onSpacePressed();

    void onTouchEvent(MotionEvent motionEvent);

    void onTrackingStarted();

    void onTrackingStopped(boolean z);

    void onUnlockHintStarted();

    void onVoiceAssistHintStarted();

    void postAnimateCollapsePanels();

    void postAnimateForceCollapsePanels();

    void postAnimateOpenPanels();

    void postQSRunnableDismissingKeyguard(Runnable runnable);

    void postStartActivityDismissingKeyguard(PendingIntent pendingIntent);

    void postStartActivityDismissingKeyguard(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller);

    void postStartActivityDismissingKeyguard(Intent intent, int i);

    void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller);

    void readyForKeyguardDone();

    void requestFaceAuth(boolean z);

    void requestNotificationUpdate(String str);

    void resendMessage(int i);

    void resendMessage(Object obj);

    void resetUserExpandedStates();

    boolean setAppearance(int i);

    void setBarStateForTest(int i);

    void setBouncerHiddenFraction(float f);

    void setBouncerShowing(boolean z);

    void setDisabled1(int i);

    void setDisabled2(int i);

    void setInteracting(int i, boolean z);

    void setKeyguardFadingAway(long j, long j2, long j3, boolean z);

    void setLastCameraLaunchSource(int i);

    void setLaunchCameraOnFinishedGoingToSleep(boolean z);

    void setLaunchCameraOnFinishedWaking(boolean z);

    void setLaunchEmergencyActionOnFinishedGoingToSleep(boolean z);

    void setLaunchEmergencyActionOnFinishedWaking(boolean z);

    void setLockscreenUser(int i);

    void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption);

    void setPanelExpanded(boolean z);

    void setQsExpanded(boolean z);

    void setQsScrimEnabled(boolean z);

    void setTopHidesStatusBar(boolean z);

    void setTransitionToFullShadeProgress(float f);

    boolean shouldAnimateLaunch(boolean z);

    boolean shouldAnimateLaunch(boolean z, boolean z2);

    boolean shouldIgnoreTouch();

    void showBouncerWithDimissAndCancelIfKeyguard(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable);

    void showKeyguard();

    void showKeyguardImpl();

    void showPinningEnterExitToast(boolean z);

    void showPinningEscapeToast();

    void showScreenPinningRequest(int i, boolean z);

    void showTransientUnchecked();

    void showWirelessChargingAnimation(int i);

    void start();

    void startActivity(Intent intent, boolean z);

    void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2);

    void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2, UserHandle userHandle);

    void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback);

    void startActivity(Intent intent, boolean z, boolean z2);

    void startActivity(Intent intent, boolean z, boolean z2, int i);

    void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2);

    void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, int i);

    void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, boolean z3, ActivityStarter.Callback callback, int i, ActivityLaunchAnimator.Controller controller, UserHandle userHandle);

    void startLaunchTransitionTimeout();

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller);

    void togglePanel();

    void unlockedScreenOffAnimationCancel() {
    }

    void updateBubblesVisibility();

    boolean updateIsKeyguard();

    boolean updateIsKeyguard(boolean z);

    void updateNotificationPanelTouchState();

    void updateQsExpansionEnabled();

    void updateScrimController();

    void userActivity();

    void visibilityChanged(boolean z);

    void wakeUpForFullScreenIntent();

    void wakeUpIfDozing(long j, View view, String str);

    void wakeUpIfDozing(long j, View view, String str, PointF pointF);

    static String viewInfo(View view) {
        return "[(" + view.getLeft() + NavigationBarInflaterView.BUTTON_SEPARATOR + view.getTop() + ")(" + view.getRight() + NavigationBarInflaterView.BUTTON_SEPARATOR + view.getBottom() + ") " + view.getWidth() + LanguageTag.PRIVATEUSE + view.getHeight() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    static void dumpBarTransitions(PrintWriter printWriter, String str, BarTransitions barTransitions) {
        printWriter.print("  ");
        printWriter.print(str);
        printWriter.print(".BarTransitions.mMode=");
        if (barTransitions != null) {
            printWriter.println(BarTransitions.modeToString(barTransitions.getMode()));
        } else {
            printWriter.println(FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN);
        }
    }

    static Bundle getActivityOptions(int i, RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions defaultActivityOptions = getDefaultActivityOptions(remoteAnimationAdapter);
        defaultActivityOptions.setLaunchDisplayId(i);
        defaultActivityOptions.setCallerDisplayId(i);
        return defaultActivityOptions.toBundle();
    }

    static Bundle getActivityOptions(int i, RemoteAnimationAdapter remoteAnimationAdapter, boolean z, long j) {
        ActivityOptions defaultActivityOptions = getDefaultActivityOptions(remoteAnimationAdapter);
        defaultActivityOptions.setSourceInfo(z ? 3 : 2, j);
        defaultActivityOptions.setLaunchDisplayId(i);
        defaultActivityOptions.setCallerDisplayId(i);
        return defaultActivityOptions.toBundle();
    }

    static ActivityOptions getDefaultActivityOptions(RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions activityOptions;
        if (remoteAnimationAdapter == null) {
            activityOptions = ActivityOptions.makeBasic();
        } else if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            activityOptions = ActivityOptions.makeRemoteTransition(RemoteTransitionAdapter.adaptRemoteAnimation(remoteAnimationAdapter));
        } else {
            activityOptions = ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter);
        }
        activityOptions.setSplashScreenStyle(0);
        return activityOptions;
    }

    static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    public static class KeyboardShortcutsMessage {
        final int mDeviceId;

        KeyboardShortcutsMessage(int i) {
            this.mDeviceId = i;
        }
    }
}
