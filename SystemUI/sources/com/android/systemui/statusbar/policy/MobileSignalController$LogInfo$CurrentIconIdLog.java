package com.android.systemui.statusbar.policy;

import android.util.Log;
/* loaded from: classes2.dex */
class MobileSignalController$LogInfo$CurrentIconIdLog {
    private Changed lastChanged = Changed.NONE;
    private final Runnable logRunnable;
    private final String tag;

    /* loaded from: classes2.dex */
    enum Changed {
        CARRIER_NETWORK_CHANGE,
        CONNECTED,
        CONNECTED_DATADISABED,
        CONNECTED_NOINTERNET,
        ENABLED,
        NONE
    }

    public MobileSignalController$LogInfo$CurrentIconIdLog(String str, Runnable runnable) {
        this.tag = str;
        this.logRunnable = runnable;
    }

    public void logIfChanged(Changed changed) {
        if (this.lastChanged == changed) {
            return;
        }
        this.lastChanged = changed;
        String str = this.tag;
        Log.d(str, "CurrentIconId changed for: " + changed);
        this.logRunnable.run();
    }
}
