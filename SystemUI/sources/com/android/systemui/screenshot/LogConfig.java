package com.android.systemui.screenshot;

class LogConfig {
    static final boolean DEBUG_ACTIONS = false;
    private static final boolean DEBUG_ALL = false;
    static final boolean DEBUG_ANIM = false;
    static final boolean DEBUG_CALLBACK = false;
    static final boolean DEBUG_DISMISS = false;
    static final boolean DEBUG_INPUT = false;
    static final boolean DEBUG_SCROLL = false;
    static final boolean DEBUG_SERVICE = false;
    static final boolean DEBUG_STORAGE = false;
    static final boolean DEBUG_UI = false;
    static final boolean DEBUG_WINDOW = false;
    private static final String TAG_SS = "Screenshot";
    private static final boolean TAG_WITH_CLASS_NAME = false;

    static String logTag(Class<?> cls) {
        return TAG_SS;
    }

    LogConfig() {
    }
}
