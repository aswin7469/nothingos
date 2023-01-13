package com.android.systemui.util.sensors;

import android.os.Build;
import android.util.Log;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.nothing.systemui.util.sensors.ProximitySensorImplEx;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

class ProximitySensorImpl implements ProximitySensor {
    private static final boolean DEBUG = (Log.isLoggable(TAG, 3) || Build.IS_DEBUGGABLE);
    private static final long SECONDARY_PING_INTERVAL_MS = 5000;
    private static final String TAG = "ProxSensor";
    private final AtomicBoolean mAlerting = new AtomicBoolean();
    /* access modifiers changed from: private */
    public Runnable mCancelSecondaryRunnable;
    /* access modifiers changed from: private */
    public final DelayableExecutor mDelayableExecutor;
    protected int mDevicePosture;
    private final Execution mExecution;
    boolean mInitializedListeners = false;
    ThresholdSensorEvent mLastEvent;
    /* access modifiers changed from: private */
    public ThresholdSensorEvent mLastPrimaryEvent;
    private final List<ThresholdSensor.Listener> mListeners = new ArrayList();
    protected boolean mPaused;
    final ThresholdSensor.Listener mPrimaryEventListener = new ProximitySensorImpl$$ExternalSyntheticLambda1(this);
    ThresholdSensor mPrimaryThresholdSensor;
    private boolean mRegistered;
    final ThresholdSensor.Listener mSecondaryEventListener = new ThresholdSensor.Listener() {
        public void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            if (!ProximitySensorImpl.this.mSecondarySafe && (ProximitySensorImpl.this.mLastPrimaryEvent == null || !ProximitySensorImpl.this.mLastPrimaryEvent.getBelow() || !thresholdSensorEvent.getBelow())) {
                ProximitySensorImpl.this.chooseSensor();
                if (ProximitySensorImpl.this.mLastPrimaryEvent != null && ProximitySensorImpl.this.mLastPrimaryEvent.getBelow()) {
                    ProximitySensorImpl proximitySensorImpl = ProximitySensorImpl.this;
                    Runnable unused = proximitySensorImpl.mCancelSecondaryRunnable = proximitySensorImpl.mDelayableExecutor.executeDelayed(new ProximitySensorImpl$1$$ExternalSyntheticLambda0(this), 5000);
                } else if (ProximitySensorImpl.this.mCancelSecondaryRunnable != null) {
                    ProximitySensorImpl.this.mCancelSecondaryRunnable.run();
                    Runnable unused2 = ProximitySensorImpl.this.mCancelSecondaryRunnable = null;
                    return;
                } else {
                    return;
                }
            }
            ProximitySensorImpl.this.logDebug("Secondary sensor event: " + thresholdSensorEvent.getBelow() + BaseIconCache.EMPTY_CLASS_NAME);
            if (!ProximitySensorImpl.this.mPaused) {
                ProximitySensorImpl.this.onSensorEvent(thresholdSensorEvent);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onThresholdCrossed$0$com-android-systemui-util-sensors-ProximitySensorImpl$1 */
        public /* synthetic */ void mo47095xe46fb3df() {
            ProximitySensorImpl.this.mPrimaryThresholdSensor.pause();
            ProximitySensorImpl.this.mSecondaryThresholdSensor.resume();
        }
    };
    /* access modifiers changed from: private */
    public boolean mSecondarySafe = false;
    ThresholdSensor mSecondaryThresholdSensor;
    private String mTag = null;

    @Inject
    ProximitySensorImpl(@PrimaryProxSensor ThresholdSensor thresholdSensor, @SecondaryProxSensor ThresholdSensor thresholdSensor2, @Main DelayableExecutor delayableExecutor, Execution execution) {
        this.mPrimaryThresholdSensor = thresholdSensor;
        this.mSecondaryThresholdSensor = thresholdSensor2;
        this.mDelayableExecutor = delayableExecutor;
        this.mExecution = execution;
    }

    public void setTag(String str) {
        this.mTag = str;
        this.mPrimaryThresholdSensor.setTag(str + ":primary");
        this.mSecondaryThresholdSensor.setTag(str + ":secondary");
    }

    public void setDelay(int i) {
        this.mExecution.assertIsMainThread();
        this.mPrimaryThresholdSensor.setDelay(i);
        this.mSecondaryThresholdSensor.setDelay(i);
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

    public void setSecondarySafe(boolean z) {
        this.mSecondarySafe = this.mSecondaryThresholdSensor.isLoaded() && z;
        chooseSensor();
    }

    public boolean isRegistered() {
        return this.mRegistered;
    }

    public boolean isLoaded() {
        return this.mPrimaryThresholdSensor.isLoaded();
    }

    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (isLoaded()) {
            if (this.mListeners.contains(listener)) {
                logDebug("ProxListener registered multiple times: " + listener);
            } else {
                this.mListeners.add(listener);
            }
            registerInternal();
        }
    }

    /* access modifiers changed from: protected */
    public void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            if (!this.mInitializedListeners) {
                this.mPrimaryThresholdSensor.pause();
                this.mSecondaryThresholdSensor.pause();
                this.mPrimaryThresholdSensor.register(this.mPrimaryEventListener);
                this.mSecondaryThresholdSensor.register(this.mSecondaryEventListener);
                this.mInitializedListeners = true;
            }
            this.mRegistered = true;
            chooseSensor();
        }
    }

    /* access modifiers changed from: private */
    public void chooseSensor() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            if (this.mSecondarySafe) {
                this.mSecondaryThresholdSensor.resume();
                this.mPrimaryThresholdSensor.pause();
                return;
            }
            this.mPrimaryThresholdSensor.resume();
            this.mSecondaryThresholdSensor.pause();
        }
    }

    public void unregister(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        this.mListeners.remove((Object) listener);
        if (this.mListeners.size() == 0) {
            unregisterInternal();
        }
    }

    public void destroy() {
        pause();
    }

    public String getName() {
        return this.mPrimaryThresholdSensor.getName();
    }

    public String getType() {
        return this.mPrimaryThresholdSensor.getType();
    }

    /* access modifiers changed from: protected */
    public void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            logDebug("unregistering sensor listener");
            this.mPrimaryThresholdSensor.pause();
            this.mSecondaryThresholdSensor.pause();
            Runnable runnable = this.mCancelSecondaryRunnable;
            if (runnable != null) {
                runnable.run();
                this.mCancelSecondaryRunnable = null;
            }
            this.mLastPrimaryEvent = null;
            this.mLastEvent = null;
            this.mRegistered = false;
        }
    }

    public Boolean isNear() {
        ThresholdSensorEvent thresholdSensorEvent;
        if (!isLoaded() || (thresholdSensorEvent = this.mLastEvent) == null) {
            return null;
        }
        return Boolean.valueOf(thresholdSensorEvent.getBelow());
    }

    public void alertListeners() {
        this.mExecution.assertIsMainThread();
        if (!this.mAlerting.getAndSet(true)) {
            ThresholdSensorEvent thresholdSensorEvent = this.mLastEvent;
            if (thresholdSensorEvent != null) {
                new ArrayList(this.mListeners).forEach(new ProximitySensorImpl$$ExternalSyntheticLambda0(thresholdSensorEvent));
                ProximitySensorImplEx.alertListeners(thresholdSensorEvent);
            }
            this.mAlerting.set(false);
        }
    }

    /* access modifiers changed from: private */
    public void onPrimarySensorEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mExecution.assertIsMainThread();
        if (this.mLastPrimaryEvent == null || thresholdSensorEvent.getBelow() != this.mLastPrimaryEvent.getBelow()) {
            this.mLastPrimaryEvent = thresholdSensorEvent;
            if (this.mSecondarySafe && this.mSecondaryThresholdSensor.isLoaded()) {
                logDebug("Primary sensor reported " + (thresholdSensorEvent.getBelow() ? "near" : "far") + ". Checking secondary.");
                if (this.mCancelSecondaryRunnable == null) {
                    this.mSecondaryThresholdSensor.resume();
                }
            } else if (!this.mSecondaryThresholdSensor.isLoaded()) {
                logDebug("Primary sensor event: " + thresholdSensorEvent.getBelow() + ". No secondary.");
                onSensorEvent(thresholdSensorEvent);
            } else if (thresholdSensorEvent.getBelow()) {
                logDebug("Primary sensor event: " + thresholdSensorEvent.getBelow() + ". Checking secondary.");
                Runnable runnable = this.mCancelSecondaryRunnable;
                if (runnable != null) {
                    runnable.run();
                }
                this.mSecondaryThresholdSensor.resume();
            } else {
                onSensorEvent(thresholdSensorEvent);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSensorEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mExecution.assertIsMainThread();
        if (this.mLastEvent == null || thresholdSensorEvent.getBelow() != this.mLastEvent.getBelow()) {
            if (!this.mSecondarySafe && !thresholdSensorEvent.getBelow()) {
                chooseSensor();
            }
            this.mLastEvent = thresholdSensorEvent;
            alertListeners();
        }
    }

    public String toString() {
        return String.format("{registered=%s, paused=%s, near=%s, posture=%s, primarySensor=%s, secondarySensor=%s secondarySafe=%s}", Boolean.valueOf(isRegistered()), Boolean.valueOf(this.mPaused), isNear(), Integer.valueOf(this.mDevicePosture), this.mPrimaryThresholdSensor, this.mSecondaryThresholdSensor, Boolean.valueOf(this.mSecondarySafe));
    }

    /* access modifiers changed from: package-private */
    public void logDebug(String str) {
        if (DEBUG) {
            Log.d(TAG, (this.mTag != null ? NavigationBarInflaterView.SIZE_MOD_START + this.mTag + "] " : "") + str);
        }
    }
}
