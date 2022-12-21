package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ThresholdSensorImpl implements ThresholdSensor {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "ThresholdSensor";
    private final Execution mExecution;
    private Boolean mLastBelow;
    private List<ThresholdSensor.Listener> mListeners;
    private boolean mPaused;
    private boolean mRegistered;
    private final Sensor mSensor;
    private int mSensorDelay;
    private SensorEventListener mSensorEventListener;
    private final AsyncSensorManager mSensorManager;
    private String mTag;
    /* access modifiers changed from: private */
    public final float mThreshold;
    /* access modifiers changed from: private */
    public final float mThresholdLatch;

    private ThresholdSensorImpl(AsyncSensorManager asyncSensorManager, Sensor sensor, Execution execution, float f, float f2, int i) {
        this.mListeners = new ArrayList();
        this.mSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                boolean z = true;
                boolean z2 = sensorEvent.values[0] < ThresholdSensorImpl.this.mThreshold;
                if (sensorEvent.values[0] < ThresholdSensorImpl.this.mThresholdLatch) {
                    z = false;
                }
                ThresholdSensorImpl.this.logDebug("Sensor value: " + sensorEvent.values[0]);
                ThresholdSensorImpl.this.onSensorEvent(z2, z, sensorEvent.timestamp);
            }
        };
        this.mSensorManager = asyncSensorManager;
        this.mExecution = execution;
        this.mSensor = sensor;
        this.mThreshold = f;
        this.mThresholdLatch = f2;
        this.mSensorDelay = i;
    }

    public void setTag(String str) {
        this.mTag = str;
    }

    public void setDelay(int i) {
        if (i != this.mSensorDelay) {
            this.mSensorDelay = i;
            if (isLoaded()) {
                unregisterInternal();
                registerInternal();
            }
        }
    }

    public boolean isLoaded() {
        return this.mSensor != null;
    }

    /* access modifiers changed from: package-private */
    public boolean isRegistered() {
        return this.mRegistered;
    }

    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
        registerInternal();
    }

    public void unregister(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        this.mListeners.remove((Object) listener);
        unregisterInternal();
    }

    public void pause() {
        this.mExecution.assertIsMainThread();
        this.mPaused = true;
        unregisterInternal();
    }

    public void resume() {
        this.mExecution.assertIsMainThread();
        this.mPaused = false;
        registerInternal();
    }

    private void alertListenersInternal(boolean z, long j) {
        new ArrayList(this.mListeners).forEach(new ThresholdSensorImpl$$ExternalSyntheticLambda0(z, j));
    }

    private void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            logDebug("Registering sensor listener");
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mSensorDelay);
            this.mRegistered = true;
        }
    }

    private void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            logDebug("Unregister sensor listener");
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mRegistered = false;
            this.mLastBelow = null;
        }
    }

    /* access modifiers changed from: private */
    public void onSensorEvent(boolean z, boolean z2, long j) {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            Boolean bool = this.mLastBelow;
            if (bool != null) {
                if (bool.booleanValue() && !z2) {
                    return;
                }
                if (!this.mLastBelow.booleanValue() && !z) {
                    return;
                }
            }
            this.mLastBelow = Boolean.valueOf(z);
            logDebug("Alerting below: " + z);
            alertListenersInternal(z, j);
        }
    }

    public String getName() {
        Sensor sensor = this.mSensor;
        if (sensor != null) {
            return sensor.getName();
        }
        return null;
    }

    public String getType() {
        Sensor sensor = this.mSensor;
        if (sensor != null) {
            return sensor.getStringType();
        }
        return null;
    }

    public String toString() {
        return String.format("{isLoaded=%s, registered=%s, paused=%s, threshold=%s, sensor=%s}", Boolean.valueOf(isLoaded()), Boolean.valueOf(this.mRegistered), Boolean.valueOf(this.mPaused), Float.valueOf(this.mThreshold), this.mSensor);
    }

    /* access modifiers changed from: private */
    public void logDebug(String str) {
        if (DEBUG) {
            Log.d(TAG, (this.mTag != null ? NavigationBarInflaterView.SIZE_MOD_START + this.mTag + "] " : "") + str);
        }
    }

    public static class Builder {
        private final Execution mExecution;
        private final Resources mResources;
        private Sensor mSensor;
        private int mSensorDelay = 3;
        private final AsyncSensorManager mSensorManager;
        private boolean mSensorSet;
        private float mThresholdLatchValue;
        private boolean mThresholdLatchValueSet;
        private boolean mThresholdSet;
        private float mThresholdValue;

        @Inject
        Builder(@Main Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
            this.mResources = resources;
            this.mSensorManager = asyncSensorManager;
            this.mExecution = execution;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensorDelay(int i) {
            this.mSensorDelay = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensorResourceId(int i, boolean z) {
            setSensorType(this.mResources.getString(i), z);
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdResourceId(int i) {
            try {
                setThresholdValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdLatchResourceId(int i) {
            try {
                setThresholdLatchValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensorType(String str, boolean z) {
            Sensor findSensorByType = findSensorByType(str, z);
            if (findSensorByType != null) {
                setSensor(findSensorByType);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdValue(float f) {
            this.mThresholdValue = f;
            this.mThresholdSet = true;
            if (!this.mThresholdLatchValueSet) {
                this.mThresholdLatchValue = f;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdLatchValue(float f) {
            this.mThresholdLatchValue = f;
            this.mThresholdLatchValueSet = true;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensor(Sensor sensor) {
            this.mSensor = sensor;
            this.mSensorSet = true;
            return this;
        }

        public ThresholdSensor build() {
            if (!this.mSensorSet) {
                throw new IllegalStateException("A sensor was not successfully set.");
            } else if (!this.mThresholdSet) {
                throw new IllegalStateException("A threshold was not successfully set.");
            } else if (this.mThresholdValue <= this.mThresholdLatchValue) {
                return new ThresholdSensorImpl(this.mSensorManager, this.mSensor, this.mExecution, this.mThresholdValue, this.mThresholdLatchValue, this.mSensorDelay);
            } else {
                throw new IllegalStateException("Threshold must be less than or equal to Threshold Latch");
            }
        }

        /* access modifiers changed from: package-private */
        public Sensor findSensorByType(String str, boolean z) {
            Sensor sensor = null;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            for (Sensor sensor2 : this.mSensorManager.getSensorList(-1)) {
                if (str.equals(sensor2.getStringType())) {
                    if (!z || sensor2.isWakeUpSensor()) {
                        return sensor2;
                    }
                    sensor = sensor2;
                }
            }
            return sensor;
        }
    }

    public static class BuilderFactory {
        private final Execution mExecution;
        private final Resources mResources;
        private final AsyncSensorManager mSensorManager;

        @Inject
        BuilderFactory(@Main Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
            this.mResources = resources;
            this.mSensorManager = asyncSensorManager;
            this.mExecution = execution;
        }

        /* access modifiers changed from: package-private */
        public Builder createBuilder() {
            return new Builder(this.mResources, this.mSensorManager, this.mExecution);
        }
    }
}
