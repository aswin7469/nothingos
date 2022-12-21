package com.android.p019wm.shell.apppairs;

import android.app.ActivityManager;
import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.apppairs.AppPairs */
public interface AppPairs {
    boolean pair(int i, int i2);

    boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2);

    void unpair(int i);
}
