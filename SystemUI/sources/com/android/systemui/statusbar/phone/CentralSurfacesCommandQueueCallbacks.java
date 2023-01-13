package com.android.systemui.statusbar.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.view.AppearanceRegion;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import java.util.Optional;
import javax.inject.Inject;

@CentralSurfacesComponent.CentralSurfacesScope
public class CentralSurfacesCommandQueueCallbacks implements CommandQueue.Callbacks {
    private static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    private final AssistManager mAssistManager;
    private final VibrationEffect mCameraLaunchGestureVibrationEffect;
    private final CentralSurfaces mCentralSurfaces;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final DisableFlagsLogger mDisableFlagsLogger;
    private final int mDisplayId;
    private final DozeServiceHost mDozeServiceHost;
    private final HeadsUpManager mHeadsUpManager;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LightBarController mLightBarController;
    private final MetricsLogger mMetricsLogger;
    private final NotificationPanelViewController mNotificationPanelViewController;
    private final NotificationShadeWindowView mNotificationShadeWindowView;
    private final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    private final PowerManager mPowerManager;
    private final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    private final ShadeController mShadeController;
    private final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private final boolean mVibrateOnOpening;
    private final VibratorHelper mVibratorHelper;
    private final Optional<Vibrator> mVibratorOptional;
    private final WakefulnessLifecycle mWakefulnessLifecycle;

    public void appTransitionCancelled(int i) {
    }

    public void appTransitionFinished(int i) {
    }

    @Inject
    CentralSurfacesCommandQueueCallbacks(CentralSurfaces centralSurfaces, Context context, @Main Resources resources, ShadeController shadeController, CommandQueue commandQueue, NotificationPanelViewController notificationPanelViewController, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, MetricsLogger metricsLogger, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, HeadsUpManager headsUpManager, WakefulnessLifecycle wakefulnessLifecycle, DeviceProvisionedController deviceProvisionedController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, AssistManager assistManager, DozeServiceHost dozeServiceHost, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowView notificationShadeWindowView, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, PowerManager powerManager, VibratorHelper vibratorHelper, Optional<Vibrator> optional, LightBarController lightBarController, DisableFlagsLogger disableFlagsLogger, @DisplayId int i) {
        Resources resources2 = resources;
        Optional<Vibrator> optional2 = optional;
        this.mCentralSurfaces = centralSurfaces;
        this.mContext = context;
        this.mShadeController = shadeController;
        this.mCommandQueue = commandQueue;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mMetricsLogger = metricsLogger;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mHeadsUpManager = headsUpManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mAssistManager = assistManager;
        this.mDozeServiceHost = dozeServiceHost;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotificationShadeWindowView = notificationShadeWindowView;
        this.mNotificationStackScrollLayoutController = notificationStackScrollLayoutController;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mPowerManager = powerManager;
        this.mVibratorHelper = vibratorHelper;
        this.mVibratorOptional = optional2;
        this.mLightBarController = lightBarController;
        this.mDisableFlagsLogger = disableFlagsLogger;
        this.mDisplayId = i;
        this.mVibrateOnOpening = resources.getBoolean(C1894R.bool.config_vibrateOnIconAnimation);
        this.mCameraLaunchGestureVibrationEffect = getCameraGestureVibrationEffect(optional2, resources);
    }

    public void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            this.mCentralSurfaces.clearTransient();
        }
    }

    public void addQsTile(ComponentName componentName) {
        QSPanelController qSPanelController = this.mCentralSurfaces.getQSPanelController();
        if (qSPanelController != null && qSPanelController.getHost() != null) {
            qSPanelController.getHost().addTile(componentName);
        }
    }

    public void remQsTile(ComponentName componentName) {
        QSPanelController qSPanelController = this.mCentralSurfaces.getQSPanelController();
        if (qSPanelController != null && qSPanelController.getHost() != null) {
            qSPanelController.getHost().removeTile(componentName);
        }
    }

    public void clickTile(ComponentName componentName) {
        QSPanelController qSPanelController = this.mCentralSurfaces.getQSPanelController();
        if (qSPanelController != null) {
            qSPanelController.clickTile(componentName);
        }
    }

    public void animateCollapsePanels(int i, boolean z) {
        this.mShadeController.animateCollapsePanels(i, z, false, 1.0f);
    }

    public void animateExpandNotificationsPanel() {
        if (this.mCommandQueue.panelsEnabled()) {
            this.mNotificationPanelViewController.expandWithoutQs();
        }
    }

    public void animateExpandSettingsPanel(String str) {
        if (this.mCommandQueue.panelsEnabled() && this.mDeviceProvisionedController.isCurrentUserSetup()) {
            this.mNotificationPanelViewController.expandWithQs();
        }
    }

    public void dismissKeyboardShortcutsMenu() {
        this.mCentralSurfaces.resendMessage((int) CentralSurfaces.MSG_DISMISS_KEYBOARD_SHORTCUTS_MENU);
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == this.mDisplayId) {
            int adjustDisableFlags = this.mRemoteInputQuickSettingsDisabler.adjustDisableFlags(i3);
            Log.d(CentralSurfaces.TAG, this.mDisableFlagsLogger.getDisableFlagsString(new DisableFlagsLogger.DisableState(this.mCentralSurfaces.getDisabled1(), this.mCentralSurfaces.getDisabled2()), new DisableFlagsLogger.DisableState(i2, i3), new DisableFlagsLogger.DisableState(i2, adjustDisableFlags)));
            int disabled1 = this.mCentralSurfaces.getDisabled1() ^ i2;
            this.mCentralSurfaces.setDisabled1(i2);
            int disabled2 = this.mCentralSurfaces.getDisabled2() ^ adjustDisableFlags;
            this.mCentralSurfaces.setDisabled2(adjustDisableFlags);
            if (!((disabled1 & 65536) == 0 || (65536 & i2) == 0)) {
                this.mShadeController.animateCollapsePanels();
            }
            if ((disabled1 & 262144) != 0 && this.mCentralSurfaces.areNotificationAlertsDisabled()) {
                this.mHeadsUpManager.releaseAllImmediately();
            }
            if ((disabled2 & 1) != 0) {
                this.mCentralSurfaces.updateQsExpansionEnabled();
            }
            if ((disabled2 & 4) != 0) {
                this.mCentralSurfaces.updateQsExpansionEnabled();
                if ((adjustDisableFlags & 4) != 0) {
                    this.mShadeController.animateCollapsePanels();
                }
            }
            this.mNotificationPanelViewController.disable(i2, adjustDisableFlags, z);
        }
    }

    public void handleSystemKey(int i) {
        if (this.mCommandQueue.panelsEnabled() && this.mKeyguardUpdateMonitor.isDeviceInteractive()) {
            if ((this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isOccluded()) || !this.mDeviceProvisionedController.isCurrentUserSetup()) {
                return;
            }
            if (280 == i) {
                this.mMetricsLogger.action(493);
                this.mNotificationPanelViewController.collapse(false, 1.0f);
            } else if (281 == i) {
                this.mMetricsLogger.action(494);
                if (this.mNotificationPanelViewController.isFullyCollapsed()) {
                    if (this.mVibrateOnOpening) {
                        this.mVibratorHelper.vibrate(2);
                    }
                    this.mNotificationPanelViewController.expand(true);
                    this.mNotificationStackScrollLayoutController.setWillExpand(true);
                    this.mHeadsUpManager.unpinAll(true);
                    this.mMetricsLogger.count("panel_open", 1);
                } else if (!this.mNotificationPanelViewController.isInSettings() && !this.mNotificationPanelViewController.isExpanding()) {
                    this.mNotificationPanelViewController.flingSettings(0.0f, 0);
                    this.mMetricsLogger.count("panel_open_qs", 1);
                }
            }
        }
    }

    public void onCameraLaunchGestureDetected(int i) {
        this.mCentralSurfaces.setLastCameraLaunchSource(i);
        if (this.mCentralSurfaces.isGoingToSleep()) {
            this.mCentralSurfaces.setLaunchCameraOnFinishedGoingToSleep(true);
        } else if (this.mNotificationPanelViewController.canCameraGestureBeLaunched()) {
            if (!this.mCentralSurfaces.isDeviceInteractive()) {
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE");
            }
            vibrateForCameraGesture();
            if (i == 1) {
                Log.v(CentralSurfaces.TAG, "Camera launch");
                this.mKeyguardUpdateMonitor.onCameraLaunched();
            }
            if (!this.mStatusBarKeyguardViewManager.isShowing()) {
                this.mCentralSurfaces.startActivityDismissingKeyguard(CameraIntents.getInsecureCameraIntent(this.mContext), false, true, true, (ActivityStarter.Callback) null, 0, (ActivityLaunchAnimator.Controller) null, UserHandle.CURRENT);
                return;
            }
            if (!this.mCentralSurfaces.isDeviceInteractive()) {
                this.mCentralSurfaces.acquireGestureWakeLock(6000);
            }
            if (isWakingUpOrAwake()) {
                if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    this.mStatusBarKeyguardViewManager.reset(true);
                }
                this.mNotificationPanelViewController.launchCamera(this.mCentralSurfaces.isDeviceInteractive(), i);
                this.mCentralSurfaces.updateScrimController();
                return;
            }
            this.mCentralSurfaces.setLaunchCameraOnFinishedWaking(true);
        }
    }

    public void onEmergencyActionLaunchGestureDetected() {
        Intent emergencyActionIntent = this.mCentralSurfaces.getEmergencyActionIntent();
        if (emergencyActionIntent == null) {
            Log.wtf(CentralSurfaces.TAG, "Couldn't find an app to process the emergency intent.");
        } else if (isGoingToSleep()) {
            this.mCentralSurfaces.setLaunchEmergencyActionOnFinishedGoingToSleep(true);
        } else {
            if (!this.mCentralSurfaces.isDeviceInteractive()) {
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:EMERGENCY_GESTURE");
            }
            if (!this.mStatusBarKeyguardViewManager.isShowing()) {
                this.mCentralSurfaces.startActivityDismissingKeyguard(emergencyActionIntent, false, true, true, (ActivityStarter.Callback) null, 0, (ActivityLaunchAnimator.Controller) null, UserHandle.CURRENT);
                return;
            }
            if (!this.mCentralSurfaces.isDeviceInteractive()) {
                this.mCentralSurfaces.acquireGestureWakeLock(6000);
            }
            if (isWakingUpOrAwake()) {
                if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    this.mStatusBarKeyguardViewManager.reset(true);
                }
                this.mContext.startActivityAsUser(emergencyActionIntent, UserHandle.CURRENT);
                return;
            }
            this.mCentralSurfaces.setLaunchEmergencyActionOnFinishedWaking(true);
        }
    }

    public void onRecentsAnimationStateChanged(boolean z) {
        this.mCentralSurfaces.setInteracting(2, z);
    }

    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        if (i == this.mDisplayId) {
            this.mLightBarController.onStatusBarAppearanceChanged(appearanceRegionArr, this.mCentralSurfaces.setAppearance(i2), this.mCentralSurfaces.getBarMode(), z);
            this.mCentralSurfaces.updateBubblesVisibility();
            this.mStatusBarStateController.setSystemBarAttributes(i2, i3, insetsVisibilities, str);
        }
    }

    public void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            this.mCentralSurfaces.showTransientUnchecked();
        }
    }

    public void toggleKeyboardShortcutsMenu(int i) {
        this.mCentralSurfaces.resendMessage((Object) new CentralSurfaces.KeyboardShortcutsMessage(i));
    }

    public void setTopAppHidesStatusBar(boolean z) {
        this.mStatusBarHideIconsForBouncerManager.setTopAppHidesStatusBarAndTriggerUpdate(z);
    }

    public void showAssistDisclosure() {
        this.mAssistManager.showDisclosure();
    }

    public void showPinningEnterExitToast(boolean z) {
        this.mCentralSurfaces.showPinningEnterExitToast(z);
    }

    public void showPinningEscapeToast() {
        this.mCentralSurfaces.showPinningEscapeToast();
    }

    public void showScreenPinningRequest(int i) {
        if (!this.mKeyguardStateController.isShowing()) {
            this.mCentralSurfaces.showScreenPinningRequest(i, true);
        }
    }

    public void showWirelessChargingAnimation(int i) {
        this.mCentralSurfaces.showWirelessChargingAnimation(i);
    }

    public void startAssist(Bundle bundle) {
        this.mAssistManager.startAssist(bundle);
    }

    public void suppressAmbientDisplay(boolean z) {
        this.mDozeServiceHost.setAlwaysOnSuppressed(z);
    }

    public void togglePanel() {
        if (this.mCentralSurfaces.isPanelExpanded()) {
            this.mShadeController.animateCollapsePanels();
        } else {
            animateExpandNotificationsPanel();
        }
    }

    private boolean isGoingToSleep() {
        return this.mWakefulnessLifecycle.getWakefulness() == 3;
    }

    private boolean isWakingUpOrAwake() {
        if (this.mWakefulnessLifecycle.getWakefulness() == 2 || this.mWakefulnessLifecycle.getWakefulness() == 1) {
            return true;
        }
        return false;
    }

    private void vibrateForCameraGesture() {
        this.mVibratorOptional.ifPresent(new CentralSurfacesCommandQueueCallbacks$$ExternalSyntheticLambda0());
    }

    private static VibrationEffect getCameraGestureVibrationEffect(Optional<Vibrator> optional, Resources resources) {
        if (optional.isPresent() && optional.get().areAllPrimitivesSupported(new int[]{4, 1})) {
            return VibrationEffect.startComposition().addPrimitive(4).addPrimitive(1, 1.0f, 50).compose();
        }
        if (optional.isPresent() && optional.get().hasAmplitudeControl()) {
            return VibrationEffect.createWaveform(CentralSurfaces.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS, CentralSurfaces.CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES, -1);
        }
        int[] intArray = resources.getIntArray(C1894R.array.config_cameraLaunchGestureVibePattern);
        long[] jArr = new long[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            jArr[i] = (long) intArray[i];
        }
        return VibrationEffect.createWaveform(jArr, -1);
    }
}
