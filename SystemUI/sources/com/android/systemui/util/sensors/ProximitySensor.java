package com.android.systemui.util.sensors;

import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ProximitySensor implements ThresholdSensor {
    private static final boolean DEBUG = Log.isLoggable("ProxSensor", 3);
    private Runnable mCancelSecondaryRunnable;
    private final DelayableExecutor mDelayableExecutor;
    private final Execution mExecution;
    @VisibleForTesting
    ThresholdSensor.ThresholdSensorEvent mLastEvent;
    private ThresholdSensor.ThresholdSensorEvent mLastPrimaryEvent;
    @VisibleForTesting
    protected boolean mPaused;
    private final ThresholdSensor mPrimaryThresholdSensor;
    private boolean mRegistered;
    private final ThresholdSensor mSecondaryThresholdSensor;
    private final List<ThresholdSensor.Listener> mListeners = new ArrayList();
    private String mTag = null;
    private final AtomicBoolean mAlerting = new AtomicBoolean();
    private boolean mInitializedListeners = false;
    private boolean mSecondarySafe = false;
    private final ThresholdSensor.Listener mPrimaryEventListener = new ThresholdSensor.Listener() { // from class: com.android.systemui.util.sensors.ProximitySensor$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public final void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            ProximitySensor.this.onPrimarySensorEvent(thresholdSensorEvent);
        }
    };
    private final ThresholdSensor.Listener mSecondaryEventListener = new AnonymousClass1();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.util.sensors.ProximitySensor$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ThresholdSensor.Listener {
        AnonymousClass1() {
        }

        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            if (!ProximitySensor.this.mSecondarySafe && (ProximitySensor.this.mLastPrimaryEvent == null || !ProximitySensor.this.mLastPrimaryEvent.getBelow() || !thresholdSensorEvent.getBelow())) {
                ProximitySensor.this.chooseSensor();
                if (ProximitySensor.this.mLastPrimaryEvent == null || !ProximitySensor.this.mLastPrimaryEvent.getBelow()) {
                    if (ProximitySensor.this.mCancelSecondaryRunnable == null) {
                        return;
                    }
                    ProximitySensor.this.mCancelSecondaryRunnable.run();
                    ProximitySensor.this.mCancelSecondaryRunnable = null;
                    return;
                }
                ProximitySensor proximitySensor = ProximitySensor.this;
                proximitySensor.mCancelSecondaryRunnable = proximitySensor.mDelayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.util.sensors.ProximitySensor$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProximitySensor.AnonymousClass1.this.lambda$onThresholdCrossed$0();
                    }
                }, 5000L);
            }
            ProximitySensor proximitySensor2 = ProximitySensor.this;
            proximitySensor2.logDebug("Secondary sensor event: " + thresholdSensorEvent.getBelow() + ".");
            ProximitySensor proximitySensor3 = ProximitySensor.this;
            if (!proximitySensor3.mPaused) {
                proximitySensor3.onSensorEvent(thresholdSensorEvent);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onThresholdCrossed$0() {
            ProximitySensor.this.mPrimaryThresholdSensor.pause();
            ProximitySensor.this.mSecondaryThresholdSensor.resume();
        }
    }

    public ProximitySensor(ThresholdSensor thresholdSensor, ThresholdSensor thresholdSensor2, DelayableExecutor delayableExecutor, Execution execution) {
        this.mPrimaryThresholdSensor = thresholdSensor;
        this.mSecondaryThresholdSensor = thresholdSensor2;
        this.mDelayableExecutor = delayableExecutor;
        this.mExecution = execution;
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void setTag(String str) {
        this.mTag = str;
        ThresholdSensor thresholdSensor = this.mPrimaryThresholdSensor;
        thresholdSensor.setTag(str + ":primary");
        ThresholdSensor thresholdSensor2 = this.mSecondaryThresholdSensor;
        thresholdSensor2.setTag(str + ":secondary");
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void setDelay(int i) {
        this.mExecution.assertIsMainThread();
        this.mPrimaryThresholdSensor.setDelay(i);
        this.mSecondaryThresholdSensor.setDelay(i);
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void pause() {
        this.mExecution.assertIsMainThread();
        this.mPaused = true;
        unregisterInternal();
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
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

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public boolean isLoaded() {
        return this.mPrimaryThresholdSensor.isLoaded();
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (!isLoaded()) {
            return;
        }
        if (this.mListeners.contains(listener)) {
            logDebug("ProxListener registered multiple times: " + listener);
        } else {
            this.mListeners.add(listener);
        }
        registerInternal();
    }

    protected void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered || this.mPaused || this.mListeners.isEmpty()) {
            return;
        }
        if (!this.mInitializedListeners) {
            this.mPrimaryThresholdSensor.pause();
            this.mSecondaryThresholdSensor.pause();
            this.mPrimaryThresholdSensor.register(this.mPrimaryEventListener);
            this.mSecondaryThresholdSensor.register(this.mSecondaryEventListener);
            this.mInitializedListeners = true;
        }
        logDebug("Registering sensor listener");
        this.mRegistered = true;
        chooseSensor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chooseSensor() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered || this.mPaused || this.mListeners.isEmpty()) {
            return;
        }
        if (this.mSecondarySafe) {
            this.mSecondaryThresholdSensor.resume();
            this.mPrimaryThresholdSensor.pause();
            return;
        }
        this.mPrimaryThresholdSensor.resume();
        this.mSecondaryThresholdSensor.pause();
    }

    public void unregister(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        this.mListeners.remove(listener);
        if (this.mListeners.size() == 0) {
            unregisterInternal();
        }
    }

    protected void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered) {
            return;
        }
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

    public Boolean isNear() {
        ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent;
        if (!isLoaded() || (thresholdSensorEvent = this.mLastEvent) == null) {
            return null;
        }
        return Boolean.valueOf(thresholdSensorEvent.getBelow());
    }

    public void alertListeners() {
        this.mExecution.assertIsMainThread();
        if (this.mAlerting.getAndSet(true)) {
            return;
        }
        final ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent = this.mLastEvent;
        if (thresholdSensorEvent != null) {
            new ArrayList(this.mListeners).forEach(new Consumer() { // from class: com.android.systemui.util.sensors.ProximitySensor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ThresholdSensor.Listener) obj).onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent.this);
                }
            });
        }
        this.mAlerting.set(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPrimarySensorEvent(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
        this.mExecution.assertIsMainThread();
        if (this.mLastPrimaryEvent == null || thresholdSensorEvent.getBelow() != this.mLastPrimaryEvent.getBelow()) {
            this.mLastPrimaryEvent = thresholdSensorEvent;
            if (this.mSecondarySafe && this.mSecondaryThresholdSensor.isLoaded()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Primary sensor reported ");
                sb.append(thresholdSensorEvent.getBelow() ? "near" : "far");
                sb.append(". Checking secondary.");
                logDebug(sb.toString());
                if (this.mCancelSecondaryRunnable != null) {
                    return;
                }
                this.mSecondaryThresholdSensor.resume();
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

    /* JADX INFO: Access modifiers changed from: private */
    public void onSensorEvent(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
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
        return String.format("{registered=%s, paused=%s, near=%s, primarySensor=%s, secondarySensor=%s secondarySafe=%s}", Boolean.valueOf(isRegistered()), Boolean.valueOf(this.mPaused), isNear(), this.mPrimaryThresholdSensor, this.mSecondaryThresholdSensor, Boolean.valueOf(this.mSecondarySafe));
    }

    /* loaded from: classes2.dex */
    public static class ProximityCheck implements Runnable {
        private final DelayableExecutor mDelayableExecutor;
        private final ProximitySensor mSensor;
        private List<Consumer<Boolean>> mCallbacks = new ArrayList();
        private final AtomicBoolean mRegistered = new AtomicBoolean();
        private final ThresholdSensor.Listener mListener = new ThresholdSensor.Listener() { // from class: com.android.systemui.util.sensors.ProximitySensor$ProximityCheck$$ExternalSyntheticLambda0
            @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
            public final void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
                ProximitySensor.ProximityCheck.this.onProximityEvent(thresholdSensorEvent);
            }
        };

        public ProximityCheck(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
            this.mSensor = proximitySensor;
            proximitySensor.setTag("prox_check");
            this.mDelayableExecutor = delayableExecutor;
        }

        @Override // java.lang.Runnable
        public void run() {
            unregister();
            onProximityEvent(null);
        }

        public void check(long j, Consumer<Boolean> consumer) {
            if (!this.mSensor.isLoaded()) {
                consumer.accept(null);
                return;
            }
            this.mCallbacks.add(consumer);
            if (this.mRegistered.getAndSet(true)) {
                return;
            }
            this.mSensor.register(this.mListener);
            this.mDelayableExecutor.executeDelayed(this, j);
        }

        private void unregister() {
            this.mSensor.unregister(this.mListener);
            this.mRegistered.set(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onProximityEvent(final ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            this.mCallbacks.forEach(new Consumer() { // from class: com.android.systemui.util.sensors.ProximitySensor$ProximityCheck$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProximitySensor.ProximityCheck.lambda$onProximityEvent$0(ThresholdSensor.ThresholdSensorEvent.this, (Consumer) obj);
                }
            });
            this.mCallbacks.clear();
            unregister();
            this.mRegistered.set(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onProximityEvent$0(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent, Consumer consumer) {
            consumer.accept(thresholdSensorEvent == null ? null : Boolean.valueOf(thresholdSensorEvent.getBelow()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDebug(String str) {
        String str2;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            if (this.mTag != null) {
                str2 = "[" + this.mTag + "] ";
            } else {
                str2 = "";
            }
            sb.append(str2);
            sb.append(str);
            Log.d("ProxSensor", sb.toString());
        }
    }
}