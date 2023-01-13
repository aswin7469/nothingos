package com.nothing.systemui.p024qs.tiles.settings.panel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.nothing.systemui.util.NTLogUtil;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingsLifecycleObserver */
public class SettingsLifecycleObserver implements LifecycleObserver {
    private static final String TAG = "SettingsLifecycleObserver";

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        NTLogUtil.m1687e(TAG, "SettingsLifecycleObserver onDestroy");
    }
}
