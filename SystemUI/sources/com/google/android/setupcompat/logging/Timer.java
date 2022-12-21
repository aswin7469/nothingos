package com.google.android.setupcompat.logging;

import android.util.Log;
import com.google.android.setupcompat.internal.ClockProvider;
import com.google.android.setupcompat.internal.Preconditions;

public final class Timer {
    private static final String TAG = "SetupCompat.Timer";
    private final MetricKey metricKey;
    private long startInNanos;
    private long stopInNanos;

    public Timer(MetricKey metricKey2) {
        this.metricKey = metricKey2;
    }

    public void start() {
        Preconditions.checkState(!isStopped(), "Timer cannot be started once stopped.");
        if (isStarted()) {
            Log.wtf(TAG, String.format("Timer instance was already started for: %s at [%s].", this.metricKey, Long.valueOf(this.startInNanos)));
            return;
        }
        this.startInNanos = ClockProvider.timeInNanos();
    }

    public void stop() {
        Preconditions.checkState(isStarted(), "Timer must be started before it can be stopped.");
        if (isStopped()) {
            Log.wtf(TAG, String.format("Timer instance was already stopped for: %s at [%s]", this.metricKey, Long.valueOf(this.stopInNanos)));
            return;
        }
        this.stopInNanos = ClockProvider.timeInNanos();
    }

    /* access modifiers changed from: package-private */
    public boolean isStopped() {
        return this.stopInNanos != 0;
    }

    private boolean isStarted() {
        return this.startInNanos != 0;
    }

    /* access modifiers changed from: package-private */
    public long getDurationInNanos() {
        return this.stopInNanos - this.startInNanos;
    }

    /* access modifiers changed from: package-private */
    public MetricKey getMetricKey() {
        return this.metricKey;
    }
}
