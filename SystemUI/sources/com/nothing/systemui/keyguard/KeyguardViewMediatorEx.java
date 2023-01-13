package com.nothing.systemui.keyguard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KeyguardViewMediatorEx {
    private static final int MSG_CRITICAL_TEMPERATURE_STATE_CHANGE = 104;
    private static final int MSG_GESTURE_FINGER_DOWN = 101;
    private static final int MSG_GESTURE_FINGER_UP = 102;
    private static final int MSG_GESTURE_TAP_WAKE = 103;
    private static final int NT_KEYCODE_FINGER_DOWN = 1000;
    private static final int NT_KEYCODE_FINGER_UP = 1001;
    private static final int NT_KEYCODE_WAKE_UP = 1002;
    private static final String TAG = "KeyguardViewMediatorEx";
    private PowerManager.WakeLock mAODFpAuthWakeLock;
    private Handler mHandler;
    private boolean mHighTemperature;
    private boolean mIsAODAuth = false;
    private boolean mIsDozing;
    private KeyguardUpdateMonitorEx mKeyguardUpdateMonitorEx;
    private KeyguardViewMediator mMediator;
    private final PowerManager mPM;

    @Inject
    public KeyguardViewMediatorEx(PowerManager powerManager, KeyguardUpdateMonitorEx keyguardUpdateMonitorEx) {
        this.mPM = powerManager;
        this.mKeyguardUpdateMonitorEx = keyguardUpdateMonitorEx;
    }

    public void init(Handler handler, KeyguardViewMediator keyguardViewMediator) {
        this.mHandler = handler;
        this.mMediator = keyguardViewMediator;
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 101:
                handleKeyGestureFingerDown();
                return true;
            case 102:
                handleKeyGestureFingerUp();
                return true;
            case 103:
                handleKeyGestureTapWakeUp();
                return true;
            case 104:
                handleCriticalTemperatureStateChange(((Boolean) message.obj).booleanValue());
                return true;
            default:
                return false;
        }
    }

    public void onSystemKeyPressed(int i) {
        NTLogUtil.m1686d(TAG, "onSystemKeyPressed: code = " + i);
        switch (i) {
            case 1000:
                onKeyGestureFingerDown();
                return;
            case 1001:
                onKeyGestureFingerUp();
                return;
            case 1002:
                onKeyGestureTapWakeUp();
                return;
            default:
                NTLogUtil.m1686d(TAG, "onNTSystemKeyPressed:  unhandled code = " + i);
                return;
        }
    }

    private void onKeyGestureFingerDown() {
        NTLogUtil.m1686d(TAG, "onKeyGesture: finger down");
        if (this.mAODFpAuthWakeLock == null) {
            PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, TAG);
            this.mAODFpAuthWakeLock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
        }
        aquireAODWakeLock();
        this.mIsAODAuth = true;
        this.mHandler.sendEmptyMessage(101);
    }

    private void handleKeyGestureFingerDown() {
        NTLogUtil.m1686d(TAG, "handleTapGestureDown: " + this.mIsAODAuth + ",mIsDozing " + this.mIsDozing);
        if (!this.mIsDozing) {
            Log.d(TAG, "Cancel handleKeyGestureFingerDown because is not in dozing");
            return;
        }
        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(0.0f);
        this.mKeyguardUpdateMonitorEx.handleGestureFingerDown();
    }

    private void onKeyGestureFingerUp() {
        NTLogUtil.m1686d(TAG, "onKeyGesture: finger up");
        this.mHandler.sendEmptyMessage(102);
    }

    private void handleKeyGestureFingerUp() {
        NTLogUtil.m1686d(TAG, "handleTapGestureUp: " + this.mIsAODAuth + ",isWakeAndUnlock " + ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isWakeAndUnlock() + ",shouldShowAODView " + ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView() + ",mIsDozing " + this.mIsDozing);
        this.mIsAODAuth = false;
        if (!((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isWakeAndUnlock() && ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(1.0f);
        }
        if (!this.mIsDozing) {
            Log.d(TAG, "Cancel handleKeyGestureFingerUp because is not in dozing");
        } else {
            this.mKeyguardUpdateMonitorEx.handleGestureFingerUp();
        }
    }

    private void onKeyGestureTapWakeUp() {
        NTLogUtil.m1686d(TAG, "onKeyGesture: tap wake up");
        this.mHandler.sendEmptyMessage(103);
    }

    private void handleKeyGestureTapWakeUp() {
        NTLogUtil.m1686d(TAG, "handleKeyGestureTapWakeUp: = ");
        this.mKeyguardUpdateMonitorEx.handleTapWakeUp();
    }

    private void aquireAODWakeLock() {
        NTLogUtil.m1686d(TAG, "aquireAODWakeLock");
        PowerManager.WakeLock wakeLock = this.mAODFpAuthWakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.mAODFpAuthWakeLock.release();
        }
        PowerManager.WakeLock wakeLock2 = this.mAODFpAuthWakeLock;
        if (wakeLock2 != null) {
            wakeLock2.acquire(3000);
        }
    }

    private void releaseAODWakeLock() {
        NTLogUtil.m1686d(TAG, "releaseAODWakeLock");
        PowerManager.WakeLock wakeLock = this.mAODFpAuthWakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.mAODFpAuthWakeLock.release();
        }
    }

    public void handleNotifyStartedWakingUp() {
        if (!((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isWakeAndUnlock()) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(1.0f);
        }
        this.mIsAODAuth = false;
        releaseAODWakeLock();
        NTLogUtil.m1686d(TAG, "onStartedWakingUp: misAodAuth = " + this.mIsAODAuth);
    }

    public void criticalTemperatureStateChange() {
        boolean z = this.mPM.getCurrentThermalStatus() >= 5;
        this.mHighTemperature = z;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(104, Boolean.valueOf(z)));
        NTLogUtil.m1686d(TAG, "highTemperatureLock: " + z);
    }

    private void handleCriticalTemperatureStateChange(boolean z) {
        NTLogUtil.m1686d(TAG, "handleCriticalTemperatureStateChange: " + z);
        if (z) {
            this.mMediator.doKeyguardLocked((Bundle) null);
        } else {
            this.mMediator.resetStateLocked();
        }
        this.mKeyguardUpdateMonitorEx.handleCriticalTemperatureStateChange(z);
    }

    public boolean isHighTemperature() {
        return this.mHighTemperature;
    }

    public void setDozing(boolean z) {
        this.mIsDozing = z;
    }
}
