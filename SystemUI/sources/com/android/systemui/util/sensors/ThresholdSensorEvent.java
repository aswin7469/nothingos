package com.android.systemui.util.sensors;

import java.util.Locale;

public class ThresholdSensorEvent {
    private final boolean mBelow;
    private final long mTimestampNs;

    public ThresholdSensorEvent(boolean z, long j) {
        this.mBelow = z;
        this.mTimestampNs = j;
    }

    public boolean getBelow() {
        return this.mBelow;
    }

    public long getTimestampNs() {
        return this.mTimestampNs;
    }

    public long getTimestampMs() {
        return this.mTimestampNs / 1000000;
    }

    public String toString() {
        Locale locale = null;
        return String.format((Locale) null, "{near=%s, timestamp_ns=%d}", Boolean.valueOf(this.mBelow), Long.valueOf(this.mTimestampNs));
    }
}
