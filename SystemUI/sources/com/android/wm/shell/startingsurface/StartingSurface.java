package com.android.wm.shell.startingsurface;

import android.app.TaskInfo;
/* loaded from: classes2.dex */
public interface StartingSurface {
    /* renamed from: createExternalInterface */
    default IStartingWindow mo1783createExternalInterface() {
        return null;
    }

    default int getBackgroundColor(TaskInfo taskInfo) {
        return -16777216;
    }
}
