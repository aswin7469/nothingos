package com.android.systemui.statusbar.policy;

import android.hardware.devicestate.DeviceStateManager;
import android.os.Trace;
import android.util.Log;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public final class DeviceStateRotationLockSettingController implements Listenable, RotationLockController.RotationLockControllerCallback {
    private static final String TAG = "DSRotateLockSettingCon";
    private int mDeviceState = -1;
    private DeviceStateManager.DeviceStateCallback mDeviceStateCallback;
    private final DeviceStateManager mDeviceStateManager;
    private DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener mDeviceStateRotationLockSettingsListener;
    private final DeviceStateRotationLockSettingsManager mDeviceStateRotationLockSettingsManager;
    private final Executor mMainExecutor;
    private final RotationPolicyWrapper mRotationPolicyWrapper;

    @Inject
    public DeviceStateRotationLockSettingController(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateManager deviceStateManager, @Main Executor executor, DeviceStateRotationLockSettingsManager deviceStateRotationLockSettingsManager) {
        this.mRotationPolicyWrapper = rotationPolicyWrapper;
        this.mDeviceStateManager = deviceStateManager;
        this.mMainExecutor = executor;
        this.mDeviceStateRotationLockSettingsManager = deviceStateRotationLockSettingsManager;
    }

    public void setListening(boolean z) {
        if (z) {
            C3156x3b31e417 deviceStateRotationLockSettingController$$ExternalSyntheticLambda0 = new C3156x3b31e417(this);
            this.mDeviceStateCallback = deviceStateRotationLockSettingController$$ExternalSyntheticLambda0;
            this.mDeviceStateManager.registerCallback(this.mMainExecutor, deviceStateRotationLockSettingController$$ExternalSyntheticLambda0);
            C3157x3b31e418 deviceStateRotationLockSettingController$$ExternalSyntheticLambda1 = new C3157x3b31e418(this);
            this.mDeviceStateRotationLockSettingsListener = deviceStateRotationLockSettingController$$ExternalSyntheticLambda1;
            this.mDeviceStateRotationLockSettingsManager.registerListener(deviceStateRotationLockSettingController$$ExternalSyntheticLambda1);
            return;
        }
        DeviceStateManager.DeviceStateCallback deviceStateCallback = this.mDeviceStateCallback;
        if (deviceStateCallback != null) {
            this.mDeviceStateManager.unregisterCallback(deviceStateCallback);
        }
        DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener deviceStateRotationLockSettingsListener = this.mDeviceStateRotationLockSettingsListener;
        if (deviceStateRotationLockSettingsListener != null) {
            this.mDeviceStateRotationLockSettingsManager.unregisterListener(deviceStateRotationLockSettingsListener);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setListening$0$com-android-systemui-statusbar-policy-DeviceStateRotationLockSettingController */
    public /* synthetic */ void mo45714x88130dd9() {
        readPersistedSetting(this.mDeviceState);
    }

    public void onRotationLockStateChanged(boolean z, boolean z2) {
        int i = this.mDeviceState;
        if (i == -1) {
            Log.wtf(TAG, "Device state was not initialized.");
        } else if (z == this.mDeviceStateRotationLockSettingsManager.isRotationLocked(i)) {
            Log.v(TAG, "Rotation lock same as the current setting, no need to update.");
        } else {
            saveNewRotationLockSetting(z);
        }
    }

    private void saveNewRotationLockSetting(boolean z) {
        Log.v(TAG, "saveNewRotationLockSetting [state=" + this.mDeviceState + "] [isRotationLocked=" + z + NavigationBarInflaterView.SIZE_MOD_END);
        this.mDeviceStateRotationLockSettingsManager.updateSetting(this.mDeviceState, z);
    }

    /* access modifiers changed from: private */
    public void updateDeviceState(int i) {
        Log.v(TAG, "updateDeviceState [state=" + i + NavigationBarInflaterView.SIZE_MOD_END);
        Trace.beginSection("updateDeviceState [state=" + i + NavigationBarInflaterView.SIZE_MOD_END);
        try {
            if (this.mDeviceState != i) {
                readPersistedSetting(i);
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    private void readPersistedSetting(int i) {
        int rotationLockSetting = this.mDeviceStateRotationLockSettingsManager.getRotationLockSetting(i);
        if (rotationLockSetting == 0) {
            Log.w(TAG, "Missing fallback. Ignoring new device state: " + i);
            return;
        }
        this.mDeviceState = i;
        boolean z = true;
        if (rotationLockSetting != 1) {
            z = false;
        }
        if (z != this.mRotationPolicyWrapper.isRotationLocked()) {
            this.mRotationPolicyWrapper.setRotationLock(z);
        }
    }
}
