package com.nothing.systemui.keyguard;

import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.util.Assert;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.inject.Singleton;

@Singleton
public class KeyguardUpdateMonitorEx {
    public static final int GF_FINGERPRINT_ACQUIRED_FINGER_DOWN = 1002;
    public static final int GF_FINGERPRINT_ACQUIRED_FINGER_UP = 1003;
    public static final int GF_FINGERPRINT_ACQUIRED_WAIT_FINGER_INPUT = 1001;
    private static final String TAG = "KeyguardUpdateMonitorEx";
    private AuthController mAuthController;
    private ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks;
    private boolean mLowPowerState = false;

    public void init(ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> arrayList, AuthController authController) {
        this.mCallbacks = arrayList;
        this.mAuthController = authController;
    }

    public void handleGestureFingerDown() {
        if (((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isNear()) {
            NTLogUtil.m1686d(TAG, "cancel handleTapGestureDown due to isNear");
            return;
        }
        NTLogUtil.m1686d(TAG, "handleTapGestureDown: ");
        if (this.mAuthController.getSensorRectF() != null) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onFingerprintDown();
        }
    }

    public void handleGestureFingerUp() {
        if (((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isNear()) {
            NTLogUtil.m1686d(TAG, "cancel handleTapGestureUp due to isNear");
            return;
        }
        NTLogUtil.m1686d(TAG, "handleTapGestureUp: ");
        if (this.mAuthController.getSensorRectF() != null) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onFingerprintUp();
        }
    }

    public void handleTapWakeUp() {
        Assert.isMainThread();
        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onTapWakeUp();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTapWakeUp();
            }
        }
    }

    public void handleCriticalTemperatureStateChange(boolean z) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onCriticalTemperaturWarning(z);
            }
        }
    }

    public void setLowPowerState(boolean z) {
        this.mLowPowerState = z;
    }

    public boolean isLowPowerState() {
        return this.mLowPowerState;
    }
}
