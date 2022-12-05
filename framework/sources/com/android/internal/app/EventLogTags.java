package com.android.internal.app;

import android.util.EventLog;
/* loaded from: classes4.dex */
public class EventLogTags {
    public static final int APP_SWITCH_WARNING_CALCEL = 53003;
    public static final int APP_SWITCH_WARNING_OPEN = 53002;
    public static final int HARMFUL_APP_WARNING_LAUNCH_ANYWAY = 53001;
    public static final int HARMFUL_APP_WARNING_UNINSTALL = 53000;

    private EventLogTags() {
    }

    public static void writeHarmfulAppWarningUninstall(String packageName) {
        EventLog.writeEvent((int) HARMFUL_APP_WARNING_UNINSTALL, packageName);
    }

    public static void writeHarmfulAppWarningLaunchAnyway(String packageName) {
        EventLog.writeEvent((int) HARMFUL_APP_WARNING_LAUNCH_ANYWAY, packageName);
    }

    public static void writeAppSwitchWarningOpen(String caller, String callee) {
        EventLog.writeEvent((int) APP_SWITCH_WARNING_OPEN, caller, callee);
    }

    public static void writeAppSwitchWarningCalcel(String caller, String callee) {
        EventLog.writeEvent((int) APP_SWITCH_WARNING_CALCEL, caller, callee);
    }
}
