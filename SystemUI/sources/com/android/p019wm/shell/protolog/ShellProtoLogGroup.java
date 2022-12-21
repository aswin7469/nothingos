package com.android.p019wm.shell.protolog;

import com.android.internal.protolog.common.IProtoLogGroup;
import com.android.p019wm.shell.startingsurface.StartingWindowController;

/* renamed from: com.android.wm.shell.protolog.ShellProtoLogGroup */
public enum ShellProtoLogGroup implements IProtoLogGroup {
    WM_SHELL_TASK_ORG(true, true, false, "WindowManagerShell"),
    WM_SHELL_TRANSITIONS(true, true, true, "WindowManagerShell"),
    WM_SHELL_DRAG_AND_DROP(true, true, false, "WindowManagerShell"),
    WM_SHELL_STARTING_WINDOW(true, true, false, StartingWindowController.TAG),
    WM_SHELL_BACK_PREVIEW(true, true, true, "ShellBackPreview"),
    WM_SHELL_RECENT_TASKS(true, true, false, "WindowManagerShell"),
    WM_SHELL_PICTURE_IN_PICTURE(true, true, false, "WindowManagerShell"),
    WM_SHELL_SPLIT_SCREEN(true, true, false, "WindowManagerShell"),
    TEST_GROUP(true, true, false, "WindowManagerShellProtoLogTest");
    
    private final boolean mEnabled;
    private volatile boolean mLogToLogcat;
    private volatile boolean mLogToProto;
    private final String mTag;

    private ShellProtoLogGroup(boolean z, boolean z2, boolean z3, String str) {
        this.mEnabled = z;
        this.mLogToProto = z2;
        this.mLogToLogcat = z3;
        this.mTag = str;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isLogToProto() {
        return this.mLogToProto;
    }

    public boolean isLogToLogcat() {
        return this.mLogToLogcat;
    }

    public boolean isLogToAny() {
        return this.mLogToLogcat || this.mLogToProto;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setLogToProto(boolean z) {
        this.mLogToProto = z;
    }

    public void setLogToLogcat(boolean z) {
        this.mLogToLogcat = z;
    }

    /* renamed from: com.android.wm.shell.protolog.ShellProtoLogGroup$Consts */
    private static class Consts {
        private static final boolean ENABLE_DEBUG = true;
        private static final boolean ENABLE_LOG_TO_PROTO_DEBUG = true;
        private static final String TAG_WM_SHELL = "WindowManagerShell";
        private static final String TAG_WM_STARTING_WINDOW = "ShellStartingWindow";

        private Consts() {
        }
    }
}
