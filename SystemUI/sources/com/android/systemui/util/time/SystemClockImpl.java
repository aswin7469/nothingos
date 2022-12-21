package com.android.systemui.util.time;

import android.os.SystemClock;

public class SystemClockImpl implements SystemClock {
    public long uptimeMillis() {
        return SystemClock.uptimeMillis();
    }

    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public long elapsedRealtimeNanos() {
        return SystemClock.elapsedRealtimeNanos();
    }

    public long currentThreadTimeMillis() {
        return SystemClock.currentThreadTimeMillis();
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
