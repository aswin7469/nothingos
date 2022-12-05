package com.android.internal.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.util.Log;
/* loaded from: classes4.dex */
public class ActivityTrigger {
    private static final String TAG = "ActivityTrigger";

    private native void native_at_deinit();

    private native float native_at_miscActivity(int i, String str, int i2, int i3);

    private native void native_at_pauseActivity(String str);

    private native void native_at_resumeActivity(String str);

    private native int native_at_startActivity(String str, int i);

    private native int native_at_startApp(String str, int i);

    private native void native_at_stopActivity(String str);

    protected void finalize() {
        native_at_deinit();
    }

    public void activityStartTrigger(ApplicationInfo appInfo, int pid) {
        String activity = appInfo.packageName + "/" + appInfo.processName + "/" + appInfo.longVersionCode + "/" + pid;
        native_at_startApp(activity, 0);
    }

    public void activityResumeTrigger(Intent intent, ActivityInfo acInfo, ApplicationInfo appInfo, boolean IsInFullScreen) {
        ComponentName cn = intent.getComponent();
        String activity = null;
        if (cn != null) {
            activity = cn.flattenToString() + "/" + appInfo.versionCode;
        }
        native_at_resumeActivity(activity);
    }

    public void activityPauseTrigger(Intent intent, ActivityInfo acInfo, ApplicationInfo appInfo) {
        ComponentName cn = intent.getComponent();
        String activity = null;
        Log.d(TAG, "ActivityTrigger activityPauseTrigger ");
        if (cn != null && appInfo != null) {
            activity = cn.flattenToString() + "/" + appInfo.versionCode;
        }
        native_at_pauseActivity(activity);
    }

    public void activityStopTrigger(Intent intent, ActivityInfo acInfo, ApplicationInfo appInfo) {
        ComponentName cn = intent.getComponent();
        String activity = null;
        Log.d(TAG, "ActivityTrigger activityStopTrigger ");
        if (cn != null && appInfo != null) {
            activity = cn.flattenToString() + "/" + appInfo.versionCode;
        }
        native_at_stopActivity(activity);
    }

    public float activityMiscTrigger(int func, String activity, int flag, int type) {
        return native_at_miscActivity(func, activity, flag, type);
    }
}
