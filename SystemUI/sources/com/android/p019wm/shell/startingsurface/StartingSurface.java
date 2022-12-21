package com.android.p019wm.shell.startingsurface;

import android.app.TaskInfo;
import androidx.core.view.ViewCompat;

/* renamed from: com.android.wm.shell.startingsurface.StartingSurface */
public interface StartingSurface {

    /* renamed from: com.android.wm.shell.startingsurface.StartingSurface$SysuiProxy */
    public interface SysuiProxy {
        void requestTopUi(boolean z, String str);
    }

    IStartingWindow createExternalInterface() {
        return null;
    }

    int getBackgroundColor(TaskInfo taskInfo) {
        return ViewCompat.MEASURED_STATE_MASK;
    }

    void setSysuiProxy(SysuiProxy sysuiProxy);
}
