package com.android.systemui.demomode;

import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public interface DemoMode extends DemoModeCommandReceiver {
    public static final String ACTION_DEMO = "com.android.systemui.demo";
    public static final List<String> COMMANDS = Lists.newArrayList(new String[]{COMMAND_BARS, COMMAND_BATTERY, COMMAND_CLOCK, COMMAND_NETWORK, COMMAND_NOTIFICATIONS, COMMAND_OPERATOR, "status", "volume"});
    public static final String COMMAND_BARS = "bars";
    public static final String COMMAND_BATTERY = "battery";
    public static final String COMMAND_CLOCK = "clock";
    public static final String COMMAND_ENTER = "enter";
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_NETWORK = "network";
    public static final String COMMAND_NOTIFICATIONS = "notifications";
    public static final String COMMAND_OPERATOR = "operator";
    public static final String COMMAND_STATUS = "status";
    public static final String COMMAND_VOLUME = "volume";
    public static final String EXTRA_COMMAND = "command";
    public static final List<String> NO_COMMANDS = new ArrayList();

    List<String> demoCommands() {
        return NO_COMMANDS;
    }
}
