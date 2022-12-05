package com.nt.settings.panel;

import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
/* loaded from: classes2.dex */
public class SettingsLifecycleObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.e("SettingsLifecycleObserver", "SettingsLifecycleObserver onDestroy");
    }
}
