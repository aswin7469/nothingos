package com.google.android.setupcompat.util;

import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public final class Logger {
    public static final String TAG = "SetupLibrary";
    private final String prefix;

    public Logger(Class<?> cls) {
        this(cls.getSimpleName());
    }

    public Logger(String str) {
        this.prefix = NavigationBarInflaterView.SIZE_MOD_START + str + "] ";
    }

    public boolean isV() {
        return Log.isLoggable(TAG, 2);
    }

    public boolean isD() {
        return Log.isLoggable(TAG, 3);
    }

    public boolean isI() {
        return Log.isLoggable(TAG, 4);
    }

    public void atVerbose(String str) {
        if (isV()) {
            Log.v(TAG, this.prefix.concat(str));
        }
    }

    public void atDebug(String str) {
        if (isD()) {
            Log.d(TAG, this.prefix.concat(str));
        }
    }

    public void atInfo(String str) {
        if (isI()) {
            Log.i(TAG, this.prefix.concat(str));
        }
    }

    /* renamed from: w */
    public void mo55164w(String str) {
        Log.w(TAG, this.prefix.concat(str));
    }

    /* renamed from: e */
    public void mo55159e(String str) {
        Log.e(TAG, this.prefix.concat(str));
    }

    /* renamed from: e */
    public void mo55160e(String str, Throwable th) {
        Log.e(TAG, this.prefix.concat(str), th);
    }
}
