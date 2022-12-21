package com.android.settingslib.core.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.lifecycle.events.OnAttach;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnCreateOptionsMenu;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnOptionsItemSelected;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnPrepareOptionsMenu;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.core.lifecycle.events.SetPreferenceScreen;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.List;

public class Lifecycle extends LifecycleRegistry {
    private static final String TAG = "LifecycleObserver";
    private final List<LifecycleObserver> mObservers = new ArrayList();
    private final LifecycleProxy mProxy;

    public Lifecycle(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
        LifecycleProxy lifecycleProxy = new LifecycleProxy();
        this.mProxy = lifecycleProxy;
        addObserver(lifecycleProxy);
    }

    public void addObserver(LifecycleObserver lifecycleObserver) {
        ThreadUtils.ensureMainThread();
        super.addObserver(lifecycleObserver);
        if (lifecycleObserver instanceof LifecycleObserver) {
            this.mObservers.add((LifecycleObserver) lifecycleObserver);
        }
    }

    public void removeObserver(LifecycleObserver lifecycleObserver) {
        ThreadUtils.ensureMainThread();
        super.removeObserver(lifecycleObserver);
        if (lifecycleObserver instanceof LifecycleObserver) {
            this.mObservers.remove((Object) lifecycleObserver);
        }
    }

    public void onAttach(Context context) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnAttach) {
                ((OnAttach) lifecycleObserver).onAttach();
            }
        }
    }

    public void onCreate(Bundle bundle) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnCreate) {
                ((OnCreate) lifecycleObserver).onCreate(bundle);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onStart() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnStart) {
                ((OnStart) lifecycleObserver).onStart();
            }
        }
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof SetPreferenceScreen) {
                ((SetPreferenceScreen) lifecycleObserver).setPreferenceScreen(preferenceScreen);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onResume() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnResume) {
                ((OnResume) lifecycleObserver).onResume();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onPause() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnPause) {
                ((OnPause) lifecycleObserver).onPause();
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnSaveInstanceState) {
                ((OnSaveInstanceState) lifecycleObserver).onSaveInstanceState(bundle);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onStop() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnStop) {
                ((OnStop) lifecycleObserver).onStop();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDestroy() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnDestroy) {
                ((OnDestroy) lifecycleObserver).onDestroy();
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnCreateOptionsMenu) {
                ((OnCreateOptionsMenu) lifecycleObserver).onCreateOptionsMenu(menu, menuInflater);
            }
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if (lifecycleObserver instanceof OnPrepareOptionsMenu) {
                ((OnPrepareOptionsMenu) lifecycleObserver).onPrepareOptionsMenu(menu);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = this.mObservers.get(i);
            if ((lifecycleObserver instanceof OnOptionsItemSelected) && ((OnOptionsItemSelected) lifecycleObserver).onOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    private class LifecycleProxy implements LifecycleObserver {
        private LifecycleProxy() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void onLifecycleEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            switch (event) {
                case ON_START:
                    Lifecycle.this.onStart();
                    return;
                case ON_RESUME:
                    Lifecycle.this.onResume();
                    return;
                case ON_PAUSE:
                    Lifecycle.this.onPause();
                    return;
                case ON_STOP:
                    Lifecycle.this.onStop();
                    return;
                case ON_DESTROY:
                    Lifecycle.this.onDestroy();
                    return;
                case ON_ANY:
                    Log.wtf(Lifecycle.TAG, "Should not receive an 'ANY' event!");
                    return;
                default:
                    return;
            }
        }
    }
}
