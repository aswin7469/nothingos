package com.nothingos.systemui.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
/* loaded from: classes2.dex */
public class LiftWakeGestureController {
    private LiftAndMotionCallback mCallback;
    private Handler mHandler;
    private boolean mIsNear;
    private Sensor mLiftSensor;
    private boolean mLiftTriggerRequested;
    private Sensor mMotionSensor;
    private boolean mMotionTriggerRequested;
    private ProximitySensor mProximitySensor;
    private final SensorManager mSensorManager;
    private final Object mLock = new Object();
    private final Object mLockForProximity = new Object();
    private final Object mLockForMotion = new Object();
    private ThresholdSensor.Listener mListener = new ThresholdSensor.Listener() { // from class: com.nothingos.systemui.doze.LiftWakeGestureController.1
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            Log.d("LiftWakeGestureController", "onThresholdCrossed: event = " + thresholdSensorEvent);
            LiftWakeGestureController.this.mIsNear = thresholdSensorEvent.getBelow();
        }
    };
    private final SensorEventListener mEventListener = new SensorEventListener() { // from class: com.nothingos.systemui.doze.LiftWakeGestureController.2
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (LiftWakeGestureController.this.mLock) {
                LiftWakeGestureController.this.mLiftTriggerRequested = false;
                Log.d("LiftWakeGestureController", "onSensorChanged: isNear = " + LiftWakeGestureController.this.mIsNear);
                if (!LiftWakeGestureController.this.mIsNear) {
                    LiftWakeGestureController.this.mHandler.post(LiftWakeGestureController.this.mLiftUpRunnable);
                }
            }
        }
    };
    private final SensorEventListener mMotionListener = new SensorEventListener() { // from class: com.nothingos.systemui.doze.LiftWakeGestureController.3
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (LiftWakeGestureController.this.mLockForMotion) {
                Log.d("LiftWakeGestureController", "onSensorChanged: isNear = " + LiftWakeGestureController.this.mIsNear + " event.value = " + sensorEvent.values[0]);
                if (!LiftWakeGestureController.this.mIsNear) {
                    LiftWakeGestureController.this.mHandler.post(LiftWakeGestureController.this.mMotionRunnable);
                }
                LiftWakeGestureController.this.cancelMotionTrigger();
                LiftWakeGestureController.this.mMotionTriggerRequested = false;
                if (LiftWakeGestureController.this.mIsNear) {
                    LiftWakeGestureController.this.requestMotionTrigger();
                }
            }
        }
    };
    private final Runnable mLiftUpRunnable = new Runnable() { // from class: com.nothingos.systemui.doze.LiftWakeGestureController.4
        @Override // java.lang.Runnable
        public void run() {
            if (LiftWakeGestureController.this.mCallback != null) {
                LiftWakeGestureController.this.mCallback.onLiftUp();
            }
        }
    };
    private final Runnable mMotionRunnable = new Runnable() { // from class: com.nothingos.systemui.doze.LiftWakeGestureController.5
        @Override // java.lang.Runnable
        public void run() {
            if (LiftWakeGestureController.this.mCallback != null) {
                LiftWakeGestureController.this.mCallback.onMotion();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface LiftAndMotionCallback {
        void onLiftUp();

        void onMotion();
    }

    public void setCallback(LiftAndMotionCallback liftAndMotionCallback) {
        this.mCallback = liftAndMotionCallback;
    }

    public LiftWakeGestureController(Context context, Handler handler, ProximitySensor proximitySensor) {
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mHandler = handler;
        this.mLiftSensor = sensorManager.getDefaultSensor(26);
        this.mMotionSensor = sensorManager.getDefaultSensor(65542, true);
        this.mProximitySensor = proximitySensor;
        proximitySensor.setTag("LiftWakeGestureController");
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
        Log.d("LiftWakeGestureController", "requestWakeUpTrigger ");
        synchronized (this.mLock) {
            Sensor sensor = this.mLiftSensor;
            if (sensor != null && !this.mLiftTriggerRequested) {
                this.mLiftTriggerRequested = true;
                this.mSensorManager.registerListener(this.mEventListener, sensor, 3, this.mHandler);
            }
        }
    }

    public void cancelWakeUpTrigger() {
        Log.d("LiftWakeGestureController", "cancelWakeUpTrigger: ");
        synchronized (this.mLock) {
            Sensor sensor = this.mLiftSensor;
            if (sensor != null && this.mLiftTriggerRequested) {
                this.mLiftTriggerRequested = false;
                this.mSensorManager.unregisterListener(this.mEventListener, sensor);
            }
        }
        if (this.mMotionTriggerRequested || this.mLiftTriggerRequested) {
            return;
        }
        cancelProximityTrigger();
    }

    public void requestMotionTrigger() {
        synchronized (this.mLockForMotion) {
            Sensor sensor = this.mMotionSensor;
            if (sensor != null && !this.mMotionTriggerRequested) {
                this.mMotionTriggerRequested = true;
                boolean registerListener = this.mSensorManager.registerListener(this.mMotionListener, sensor, 3, this.mHandler);
                Log.d("LiftWakeGestureController", "requestMotionTrigger: result = " + registerListener);
            }
        }
    }

    public void cancelMotionTrigger() {
        Log.d("LiftWakeGestureController", "cancelMotionTrigger: ");
        synchronized (this.mLockForMotion) {
            Sensor sensor = this.mMotionSensor;
            if (sensor != null && this.mMotionTriggerRequested) {
                this.mMotionTriggerRequested = false;
                this.mSensorManager.unregisterListener(this.mMotionListener, sensor);
            }
        }
        if (this.mMotionTriggerRequested || this.mLiftTriggerRequested) {
            return;
        }
        cancelProximityTrigger();
    }

    public void requestProximityTrigger() {
        Log.d("LiftWakeGestureController", "requestProximityTrigger: mProximitySensor = " + this.mProximitySensor);
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

    public boolean isNear() {
        return this.mIsNear;
    }
}
