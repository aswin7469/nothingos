package com.android.systemui.statusbar.phone;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.mediator.ScreenOnCoordinator$$ExternalSyntheticLambda1;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.DozeServiceHostEx;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public final class DozeServiceHost implements DozeHost {
    private static final String TAG = "DozeServiceHost";
    private boolean mAlwaysOnSuppressed;
    private View mAmbientIndicationContainer;
    private boolean mAnimateWakeup;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final AuthController mAuthController;
    private final BatteryController mBatteryController;
    private final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    private final ArrayList<DozeHost.Callback> mCallbacks;
    /* access modifiers changed from: private */
    public CentralSurfaces mCentralSurfaces;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final DozeLog mDozeLog;
    private final DozeScrimController mDozeScrimController;
    private boolean mDozingRequested;
    private final FoldAodAnimationController mFoldAodAnimationController;
    private final HeadsUpManagerPhone mHeadsUpManagerPhone;
    /* access modifiers changed from: private */
    public boolean mIgnoreTouchWhilePulsing;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final NotificationIconAreaController mNotificationIconAreaController;
    /* access modifiers changed from: private */
    public NotificationPanelViewController mNotificationPanel;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private NotificationShadeWindowViewController mNotificationShadeWindowViewController;
    /* access modifiers changed from: private */
    public final NotificationWakeUpCoordinator mNotificationWakeUpCoordinator;
    private Runnable mPendingScreenOffCallback;
    private final PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public final PulseExpansionHandler mPulseExpansionHandler;
    /* access modifiers changed from: private */
    public boolean mPulsing;
    /* access modifiers changed from: private */
    public final ScrimController mScrimController;
    /* access modifiers changed from: private */
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    /* access modifiers changed from: private */
    public final SysuiStatusBarStateController mStatusBarStateController;
    boolean mWakeLockScreenPerformsAuth = SystemProperties.getBoolean("persist.sysui.wake_performs_auth", true);
    private final WakefulnessLifecycle mWakefulnessLifecycle;

    @Inject
    public DozeServiceHost(DozeLog dozeLog, PowerManager powerManager, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, DeviceProvisionedController deviceProvisionedController, HeadsUpManagerPhone headsUpManagerPhone, BatteryController batteryController, ScrimController scrimController, Lazy<BiometricUnlockController> lazy, KeyguardViewMediator keyguardViewMediator, Lazy<AssistManager> lazy2, DozeScrimController dozeScrimController, KeyguardUpdateMonitor keyguardUpdateMonitor, PulseExpansionHandler pulseExpansionHandler, Optional<SysUIUnfoldComponent> optional, NotificationShadeWindowController notificationShadeWindowController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, AuthController authController, NotificationIconAreaController notificationIconAreaController) {
        ArrayList<DozeHost.Callback> arrayList = new ArrayList<>();
        this.mCallbacks = arrayList;
        this.mDozeLog = dozeLog;
        this.mPowerManager = powerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mHeadsUpManagerPhone = headsUpManagerPhone;
        this.mBatteryController = batteryController;
        this.mScrimController = scrimController;
        this.mBiometricUnlockControllerLazy = lazy;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mAssistManagerLazy = lazy2;
        this.mDozeScrimController = dozeScrimController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mNotificationWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mAuthController = authController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mFoldAodAnimationController = (FoldAodAnimationController) optional.map(new ScreenOnCoordinator$$ExternalSyntheticLambda1()).orElse(null);
        ((DozeServiceHostEx) NTDependencyEx.get(DozeServiceHostEx.class)).init(arrayList);
    }

    public void initialize(CentralSurfaces centralSurfaces, StatusBarKeyguardViewManager statusBarKeyguardViewManager, NotificationShadeWindowViewController notificationShadeWindowViewController, NotificationPanelViewController notificationPanelViewController, View view) {
        this.mCentralSurfaces = centralSurfaces;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mNotificationPanel = notificationPanelViewController;
        this.mNotificationShadeWindowViewController = notificationShadeWindowViewController;
        this.mAmbientIndicationContainer = view;
    }

    public String toString() {
        return "PSB.DozeServiceHost[mCallbacks=" + this.mCallbacks.size() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    /* access modifiers changed from: package-private */
    public void firePowerSaveChanged(boolean z) {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onPowerSaveChanged(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void fireNotificationPulse(NotificationEntry notificationEntry) {
        DozeServiceHost$$ExternalSyntheticLambda0 dozeServiceHost$$ExternalSyntheticLambda0 = new DozeServiceHost$$ExternalSyntheticLambda0(this, notificationEntry);
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onNotificationAlerted(dozeServiceHost$$ExternalSyntheticLambda0);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fireNotificationPulse$0$com-android-systemui-statusbar-phone-DozeServiceHost */
    public /* synthetic */ void mo44044x85b51859(NotificationEntry notificationEntry) {
        notificationEntry.setPulseSuppressed(true);
        this.mNotificationIconAreaController.updateAodNotificationIcons();
    }

    /* access modifiers changed from: package-private */
    public boolean getDozingRequested() {
        return this.mDozingRequested;
    }

    /* access modifiers changed from: package-private */
    public boolean isPulsing() {
        return this.mPulsing;
    }

    public void addCallback(DozeHost.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(DozeHost.Callback callback) {
        this.mCallbacks.remove((Object) callback);
    }

    public void startDozing() {
        if (!this.mDozingRequested) {
            this.mDozingRequested = true;
            updateDozing();
            this.mDozeLog.traceDozing(this.mStatusBarStateController.isDozing());
            this.mCentralSurfaces.updateIsKeyguard();
            return;
        }
        this.mDozingRequested = true;
    }

    /* access modifiers changed from: package-private */
    public void updateDozing() {
        boolean z = false;
        boolean z2 = (this.mDozingRequested && this.mStatusBarStateController.getState() == 1) || this.mBiometricUnlockControllerLazy.get().getMode() == 2;
        if (this.mBiometricUnlockControllerLazy.get().getMode() != 1) {
            z = z2;
        }
        this.mStatusBarStateController.setIsDozing(z);
        this.mNotificationShadeWindowViewController.setDozing(z);
        FoldAodAnimationController foldAodAnimationController = this.mFoldAodAnimationController;
        if (foldAodAnimationController != null) {
            foldAodAnimationController.setIsDozing(z);
        }
    }

    public void pulseWhileDozing(final DozeHost.PulseCallback pulseCallback, int i) {
        if (i == 5) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:LONG_PRESS");
            this.mAssistManagerLazy.get().startAssist(new Bundle());
            return;
        }
        if (i == 8) {
            this.mScrimController.setWakeLockScreenSensorActive(true);
        }
        final boolean z = i == 8 && this.mWakeLockScreenPerformsAuth;
        this.mPulsing = true;
        this.mDozeScrimController.pulse(new DozeHost.PulseCallback() {
            public void onPulseStarted() {
                pulseCallback.onPulseStarted();
                DozeServiceHost.this.mCentralSurfaces.updateNotificationPanelTouchState();
                setPulsing(true);
            }

            public void onPulseFinished() {
                boolean unused = DozeServiceHost.this.mPulsing = false;
                pulseCallback.onPulseFinished();
                DozeServiceHost.this.mCentralSurfaces.updateNotificationPanelTouchState();
                DozeServiceHost.this.mScrimController.setWakeLockScreenSensorActive(false);
                setPulsing(false);
            }

            private void setPulsing(boolean z) {
                DozeServiceHost.this.mStatusBarKeyguardViewManager.setPulsing(z);
                DozeServiceHost.this.mNotificationPanel.setPulsing(z);
                DozeServiceHost.this.mStatusBarStateController.setPulsing(z);
                boolean unused = DozeServiceHost.this.mIgnoreTouchWhilePulsing = false;
                if (DozeServiceHost.this.mKeyguardUpdateMonitor != null && z) {
                    DozeServiceHost.this.mKeyguardUpdateMonitor.onAuthInterruptDetected(z);
                }
                DozeServiceHost.this.mCentralSurfaces.updateScrimController();
                DozeServiceHost.this.mPulseExpansionHandler.setPulsing(z);
                DozeServiceHost.this.mNotificationWakeUpCoordinator.setPulsing(z);
            }
        }, i);
        this.mCentralSurfaces.updateScrimController();
    }

    public void stopDozing() {
        if (this.mDozingRequested) {
            this.mDozingRequested = false;
            updateDozing();
            this.mDozeLog.traceDozing(this.mStatusBarStateController.isDozing());
        }
    }

    public void onIgnoreTouchWhilePulsing(boolean z) {
        if (z != this.mIgnoreTouchWhilePulsing) {
            this.mDozeLog.tracePulseTouchDisabledByProx(z);
        }
        this.mIgnoreTouchWhilePulsing = z;
        if (this.mStatusBarStateController.isDozing() && z) {
            this.mNotificationShadeWindowViewController.cancelCurrentTouch();
        }
    }

    public void dozeTimeTick() {
        this.mNotificationPanel.dozeTimeTick();
        this.mAuthController.dozeTimeTick();
        this.mNotificationShadeWindowViewController.dozeTimeTick();
        View view = this.mAmbientIndicationContainer;
        if (view instanceof DozeReceiver) {
            ((DozeReceiver) view).dozeTimeTick();
        }
    }

    public boolean isPowerSaveActive() {
        return this.mBatteryController.isAodPowerSave();
    }

    public boolean isPulsingBlocked() {
        return this.mBiometricUnlockControllerLazy.get().getMode() == 1;
    }

    public boolean isProvisioned() {
        return this.mDeviceProvisionedController.isDeviceProvisioned() && this.mDeviceProvisionedController.isCurrentUserSetup();
    }

    public void extendPulse(int i) {
        if (i == 8) {
            this.mScrimController.setWakeLockScreenSensorActive(true);
        }
        if (!this.mDozeScrimController.isPulsing() || !this.mHeadsUpManagerPhone.hasNotifications()) {
            this.mDozeScrimController.extendPulse();
        } else {
            this.mHeadsUpManagerPhone.extendHeadsUp();
        }
    }

    public void stopPulsing() {
        if (this.mDozeScrimController.isPulsing()) {
            this.mDozeScrimController.pulseOutNow();
        }
    }

    public void setAnimateWakeup(boolean z) {
        if (this.mWakefulnessLifecycle.getWakefulness() != 2 && this.mWakefulnessLifecycle.getWakefulness() != 1) {
            this.mAnimateWakeup = z;
        }
    }

    public void onSlpiTap(float f, float f2) {
        View view;
        if (f > 0.0f && f2 > 0.0f && (view = this.mAmbientIndicationContainer) != null && view.getVisibility() == 0) {
            int[] iArr = new int[2];
            this.mAmbientIndicationContainer.getLocationOnScreen(iArr);
            float f3 = f - ((float) iArr[0]);
            float f4 = f2 - ((float) iArr[1]);
            if (0.0f <= f3 && f3 <= ((float) this.mAmbientIndicationContainer.getWidth()) && 0.0f <= f4 && f4 <= ((float) this.mAmbientIndicationContainer.getHeight())) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = elapsedRealtime;
                float f5 = f;
                float f6 = f2;
                MotionEvent obtain = MotionEvent.obtain(elapsedRealtime, j, 0, f5, f6, 0);
                this.mAmbientIndicationContainer.dispatchTouchEvent(obtain);
                obtain.recycle();
                MotionEvent obtain2 = MotionEvent.obtain(elapsedRealtime, j, 1, f5, f6, 0);
                this.mAmbientIndicationContainer.dispatchTouchEvent(obtain2);
                obtain2.recycle();
            }
        }
    }

    public void setDozeScreenBrightness(int i) {
        this.mDozeLog.traceDozeScreenBrightness(i);
        this.mNotificationShadeWindowController.setDozeScreenBrightness(i);
    }

    public void setAodDimmingScrim(float f) {
        this.mDozeLog.traceSetAodDimmingScrim(f);
        this.mScrimController.setAodFrontScrimAlpha(f);
    }

    public void prepareForGentleSleep(Runnable runnable) {
        if (this.mPendingScreenOffCallback != null) {
            Log.w(TAG, "Overlapping onDisplayOffCallback. Ignoring previous one.");
        }
        this.mPendingScreenOffCallback = runnable;
        this.mCentralSurfaces.updateScrimController();
    }

    public void cancelGentleSleep() {
        this.mPendingScreenOffCallback = null;
        if (this.mScrimController.getState() == ScrimState.OFF) {
            this.mCentralSurfaces.updateScrimController();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasPendingScreenOffCallback() {
        return this.mPendingScreenOffCallback != null;
    }

    /* access modifiers changed from: package-private */
    public void executePendingScreenOffCallback() {
        Runnable runnable = this.mPendingScreenOffCallback;
        if (runnable != null) {
            runnable.run();
            this.mPendingScreenOffCallback = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldAnimateWakeup() {
        return this.mAnimateWakeup;
    }

    /* access modifiers changed from: package-private */
    public boolean getIgnoreTouchWhilePulsing() {
        return this.mIgnoreTouchWhilePulsing;
    }

    /* access modifiers changed from: package-private */
    public void setAlwaysOnSuppressed(boolean z) {
        if (z != this.mAlwaysOnSuppressed) {
            this.mAlwaysOnSuppressed = z;
            Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onAlwaysOnSuppressedChanged(z);
            }
        }
    }

    public boolean isAlwaysOnSuppressed() {
        return this.mAlwaysOnSuppressed;
    }
}
