package com.android.systemui.util.wrapper;

import android.os.Build;
import javax.inject.Singleton;

@Singleton
public class BuildInfo {
    public boolean isDebuggable() {
        return Build.isDebuggable();
    }
}
