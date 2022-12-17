package com.android.settingslib.core.instrumentation;

/* compiled from: SettingsJankMonitor.kt */
final class SettingsJankMonitor$detectToggleJank$1 implements Runnable {
    public static final SettingsJankMonitor$detectToggleJank$1 INSTANCE = new SettingsJankMonitor$detectToggleJank$1();

    SettingsJankMonitor$detectToggleJank$1() {
    }

    public final void run() {
        SettingsJankMonitor.jankMonitor.end(57);
    }
}
