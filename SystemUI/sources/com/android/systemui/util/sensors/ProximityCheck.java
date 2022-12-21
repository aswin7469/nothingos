package com.android.systemui.util.sensors;

import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.inject.Inject;

public class ProximityCheck implements Runnable {
    private List<Consumer<Boolean>> mCallbacks = new ArrayList();
    private final DelayableExecutor mDelayableExecutor;
    private final ThresholdSensor.Listener mListener;
    private final AtomicBoolean mRegistered = new AtomicBoolean();
    private final ProximitySensor mSensor;

    @Inject
    public ProximityCheck(ProximitySensor proximitySensor, @Main DelayableExecutor delayableExecutor) {
        this.mSensor = proximitySensor;
        proximitySensor.setTag("prox_check");
        this.mDelayableExecutor = delayableExecutor;
        this.mListener = new ProximityCheck$$ExternalSyntheticLambda0(this);
    }

    public void setTag(String str) {
        this.mSensor.setTag(str);
    }

    public void run() {
        unregister();
        onProximityEvent((ThresholdSensorEvent) null);
    }

    public void check(long j, Consumer<Boolean> consumer) {
        if (!this.mSensor.isLoaded()) {
            consumer.accept(null);
            return;
        }
        this.mCallbacks.add(consumer);
        if (!this.mRegistered.getAndSet(true)) {
            this.mSensor.register(this.mListener);
            this.mDelayableExecutor.executeDelayed(this, j);
        }
    }

    public void destroy() {
        this.mSensor.destroy();
    }

    private void unregister() {
        this.mSensor.unregister(this.mListener);
        this.mRegistered.set(false);
    }

    /* access modifiers changed from: private */
    public void onProximityEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mCallbacks.forEach(new ProximityCheck$$ExternalSyntheticLambda1(thresholdSensorEvent));
        this.mCallbacks.clear();
        unregister();
        this.mRegistered.set(false);
    }

    static /* synthetic */ void lambda$onProximityEvent$0(ThresholdSensorEvent thresholdSensorEvent, Consumer consumer) {
        consumer.accept(thresholdSensorEvent == null ? null : Boolean.valueOf(thresholdSensorEvent.getBelow()));
    }
}
