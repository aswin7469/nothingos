package com.nothing.systemui.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class LiftWakeGestureController {
    private static final int FINGERPRINT_DISPLAY_SENSOR = 65542;
    private static final String TAG = "LiftWakeGestureController";
    /* access modifiers changed from: private */
    public LiftAndMotionCallback mCallback;
    private final SensorEventListener mEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (LiftWakeGestureController.this.mLock) {
                NTLogUtil.m1680d(LiftWakeGestureController.TAG, "onSensorChanged: isNear = " + LiftWakeGestureController.this.mIsNear);
                if (!LiftWakeGestureController.this.mIsNear) {
                    LiftWakeGestureController.this.mHandler.post(LiftWakeGestureController.this.mLiftUpRunnable);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mIsCancelLiftInAodNear = false;
    /* access modifiers changed from: private */
    public boolean mIsNear;
    private Sensor mLiftSensor;
    private boolean mLiftTriggerRequested;
    /* access modifiers changed from: private */
    public final Runnable mLiftUpRunnable = new Runnable() {
        public void run() {
            if (LiftWakeGestureController.this.mCallback != null) {
                LiftWakeGestureController.this.mCallback.onLiftUp();
            }
        }
    };
    private ThresholdSensor.Listener mListener = new ThresholdSensor.Listener() {
        public void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            NTLogUtil.m1680d(LiftWakeGestureController.TAG, "onThresholdCrossed: event = " + thresholdSensorEvent + " mIsCancelLiftInAodNear =" + LiftWakeGestureController.this.mIsCancelLiftInAodNear);
            boolean unused = LiftWakeGestureController.this.mIsNear = thresholdSensorEvent.getBelow();
            if (!LiftWakeGestureController.this.mIsNear && LiftWakeGestureController.this.mIsCancelLiftInAodNear) {
                LiftWakeGestureController.this.requestLiftSensorTrigger();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    /* access modifiers changed from: private */
    public final Object mLockForMotion = new Object();
    private final Object mLockForProximity = new Object();
    private final SensorEventListener mMotionListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (LiftWakeGestureController.this.mLockForMotion) {
                NTLogUtil.m1680d(LiftWakeGestureController.TAG, "onSensorChanged: isNear = " + LiftWakeGestureController.this.mIsNear + " event.value = " + sensorEvent.values[0]);
                if (!LiftWakeGestureController.this.mIsNear && sensorEvent.values[0] == 1.0f) {
                    LiftWakeGestureController.this.mHandler.post(LiftWakeGestureController.this.mMotionRunnable);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mMotionRunnable = new Runnable() {
        public void run() {
            NTLogUtil.m1680d(LiftWakeGestureController.TAG, "mMotionRunnable= callbacks: " + LiftWakeGestureController.this.mCallback);
            if (LiftWakeGestureController.this.mCallback != null) {
                LiftWakeGestureController.this.mCallback.onMotion();
            }
        }
    };
    private Sensor mMotionSensor;
    private boolean mMotionTriggerRequested;
    private ProximitySensor mProximitySensor;
    private final SensorManager mSensorManager;

    public interface LiftAndMotionCallback {
        void onLiftUp();

        void onMotion();
    }

    public void setCallback(LiftAndMotionCallback liftAndMotionCallback) {
        this.mCallback = liftAndMotionCallback;
    }

    @Inject
    public LiftWakeGestureController(Context context, @Main Handler handler, ProximitySensor proximitySensor) {
        this.mHandler = handler;
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mLiftSensor = sensorManager.getDefaultSensor(26);
        this.mMotionSensor = sensorManager.getDefaultSensor(FINGERPRINT_DISPLAY_SENSOR, true);
        this.mProximitySensor = proximitySensor;
        proximitySensor.setTag(TAG);
    }

    public boolean isLiftSensorSupported() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mLiftSensor != null;
        }
        return z;
    }

    public boolean isMotionSensorSupported() {
        boolean z;
        synchronized (this.mLockForMotion) {
            z = this.mMotionSensor != null;
        }
        return z;
    }

    public void requestWakeUpTrigger() {
        NTLogUtil.m1680d(TAG, "requestWakeUpTrigger ");
        requestLiftSensorTrigger();
    }

    public void cancelWakeUpTrigger() {
        NTLogUtil.m1680d(TAG, "cancelWakeUpTrigger: ");
        this.mIsCancelLiftInAodNear = false;
        cancelLiftSensorTrigger();
        if (!this.mMotionTriggerRequested && !this.mLiftTriggerRequested) {
            cancelProximityTrigger();
        }
    }

    public void requestMotionTrigger() {
        synchronized (this.mLockForMotion) {
            Sensor sensor = this.mMotionSensor;
            if (sensor != null && !this.mMotionTriggerRequested) {
                this.mMotionTriggerRequested = true;
                NTLogUtil.m1680d(TAG, "requestMotionTrigger: result = " + this.mSensorManager.registerListener(this.mMotionListener, sensor, 3, this.mHandler));
            }
        }
    }

    public void cancelMotionTrigger() {
        NTLogUtil.m1680d(TAG, "cancelMotionTrigger: ");
        synchronized (this.mLockForMotion) {
            Sensor sensor = this.mMotionSensor;
            if (sensor != null && this.mMotionTriggerRequested) {
                this.mMotionTriggerRequested = false;
                this.mSensorManager.unregisterListener(this.mMotionListener, sensor);
            }
        }
        if (!this.mMotionTriggerRequested && !this.mLiftTriggerRequested) {
            cancelProximityTrigger();
        }
    }

    public void requestProximityTrigger() {
        NTLogUtil.m1680d(TAG, "requestProximityTrigger: mProximitySensor = " + this.mProximitySensor);
        synchronized (this.mLockForProximity) {
            ProximitySensor proximitySensor = this.mProximitySensor;
            if (proximitySensor != null) {
                proximitySensor.register(this.mListener);
            }
        }
    }

    public void cancelProximityTrigger() {
        synchronized (this.mLockForProximity) {
            ProximitySensor proximitySensor = this.mProximitySensor;
            if (proximitySensor != null) {
                proximitySensor.unregister(this.mListener);
            }
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            printWriter.println(str + TAG);
            String str2 = str + "  ";
            printWriter.println(str2 + "mTriggerRequested=" + this.mLiftTriggerRequested);
            printWriter.println(str2 + "mLiftSensor=" + this.mLiftSensor);
            printWriter.println(str2 + "mMotionTriggerRequested=" + this.mMotionTriggerRequested);
            printWriter.println(str2 + "mMotionSensor=" + this.mMotionSensor);
        }
    }

    public boolean isNear() {
        return this.mIsNear;
    }

    private boolean shouldEnableLiftGestureLp() {
        boolean isLiftWakeEnable = ((AODController) NTDependencyEx.get(AODController.class)).isLiftWakeEnable();
        NTLogUtil.m1680d(TAG, "shouldEnableLiftGestureLp: liftWakeEnable = " + isLiftWakeEnable);
        return isLiftWakeEnable && isLiftSensorSupported();
    }

    public void requestLiftSensorTrigger() {
        if (shouldEnableLiftGestureLp()) {
            NTLogUtil.m1680d(TAG, "requestLiftSensorTrigger mLiftTriggerRequested=" + this.mLiftTriggerRequested);
            this.mIsCancelLiftInAodNear = false;
            synchronized (this.mLock) {
                Sensor sensor = this.mLiftSensor;
                if (sensor != null && !this.mLiftTriggerRequested) {
                    this.mLiftTriggerRequested = true;
                    this.mSensorManager.registerListener(this.mEventListener, sensor, 3, this.mHandler);
                }
            }
        }
    }

    public void cancelLiftSensorTrigger() {
        NTLogUtil.m1680d(TAG, "cancelLiftSensorTrigger mLiftTriggerRequested=" + this.mLiftTriggerRequested);
        synchronized (this.mLock) {
            Sensor sensor = this.mLiftSensor;
            if (sensor != null && this.mLiftTriggerRequested) {
                this.mLiftTriggerRequested = false;
                this.mSensorManager.unregisterListener(this.mEventListener, sensor);
            }
        }
    }

    public void mayCancelLiftSensorTrigger() {
        NTLogUtil.m1680d(TAG, "mayCancelLiftSensorTrigger mIsNear=" + this.mIsNear + ", shouldShowAODView=" + ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView());
        if (this.mIsNear && ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
            cancelLiftSensorTrigger();
            this.mIsCancelLiftInAodNear = true;
        }
    }
}
