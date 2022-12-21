package com.android.systemui.util.sensors;

import android.util.Log;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import javax.inject.Inject;

class PostureDependentProximitySensor extends ProximitySensorImpl {
    private final DevicePostureController.Callback mDevicePostureCallback;
    private final DevicePostureController mDevicePostureController;
    private final ThresholdSensor[] mPostureToPrimaryProxSensorMap;
    private final ThresholdSensor[] mPostureToSecondaryProxSensorMap;

    @Inject
    PostureDependentProximitySensor(@PrimaryProxSensor ThresholdSensor[] thresholdSensorArr, @SecondaryProxSensor ThresholdSensor[] thresholdSensorArr2, @Main DelayableExecutor delayableExecutor, Execution execution, DevicePostureController devicePostureController) {
        super(thresholdSensorArr[0], thresholdSensorArr2[0], delayableExecutor, execution);
        PostureDependentProximitySensor$$ExternalSyntheticLambda0 postureDependentProximitySensor$$ExternalSyntheticLambda0 = new PostureDependentProximitySensor$$ExternalSyntheticLambda0(this);
        this.mDevicePostureCallback = postureDependentProximitySensor$$ExternalSyntheticLambda0;
        this.mPostureToPrimaryProxSensorMap = thresholdSensorArr;
        this.mPostureToSecondaryProxSensorMap = thresholdSensorArr2;
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        devicePostureController.addCallback(postureDependentProximitySensor$$ExternalSyntheticLambda0);
        chooseSensors();
    }

    public void destroy() {
        super.destroy();
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
    }

    private void chooseSensors() {
        if (this.mDevicePosture >= this.mPostureToPrimaryProxSensorMap.length || this.mDevicePosture >= this.mPostureToSecondaryProxSensorMap.length) {
            Log.e("PostureDependProxSensor", "unsupported devicePosture=" + this.mDevicePosture);
            return;
        }
        ThresholdSensor thresholdSensor = this.mPostureToPrimaryProxSensorMap[this.mDevicePosture];
        ThresholdSensor thresholdSensor2 = this.mPostureToSecondaryProxSensorMap[this.mDevicePosture];
        if (thresholdSensor != this.mPrimaryThresholdSensor || thresholdSensor2 != this.mSecondaryThresholdSensor) {
            logDebug("Register new proximity sensors newPosture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
            unregisterInternal();
            if (this.mPrimaryThresholdSensor != null) {
                this.mPrimaryThresholdSensor.unregister(this.mPrimaryEventListener);
            }
            if (this.mSecondaryThresholdSensor != null) {
                this.mSecondaryThresholdSensor.unregister(this.mSecondaryEventListener);
            }
            this.mPrimaryThresholdSensor = thresholdSensor;
            this.mSecondaryThresholdSensor = thresholdSensor2;
            this.mInitializedListeners = false;
            registerInternal();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-util-sensors-PostureDependentProximitySensor */
    public /* synthetic */ void mo47063xb7bbe84e(int i) {
        if (this.mDevicePosture != i) {
            this.mDevicePosture = i;
            chooseSensors();
        }
    }

    public String toString() {
        return String.format("{posture=%s, proximitySensor=%s}", DevicePostureController.devicePostureToString(this.mDevicePosture), super.toString());
    }
}
