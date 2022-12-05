package com.nt.settings.core.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import com.nt.settings.core.lifecycle.events.OnAttach;
import com.nt.settings.core.lifecycle.events.OnCreate;
import com.nt.settings.core.lifecycle.events.OnDestroy;
import com.nt.settings.core.lifecycle.events.OnOptionsItemSelected;
import com.nt.settings.core.lifecycle.events.OnPause;
import com.nt.settings.core.lifecycle.events.OnPrepareOptionsMenu;
import com.nt.settings.core.lifecycle.events.OnResume;
import com.nt.settings.core.lifecycle.events.OnSaveInstanceState;
import com.nt.settings.core.lifecycle.events.OnStart;
import com.nt.settings.core.lifecycle.events.OnStop;
import com.nt.settings.core.lifecycle.events.SetPreferenceScreen;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class Lifecycle {
    protected final List<LifecycleObserver> mObservers = new ArrayList();

    public void onAttach(Context context) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnAttach) {
                ((OnAttach) lifecycleObserver).onAttach(context);
            }
        }
    }

    public void onCreate(Bundle bundle) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnCreate) {
                ((OnCreate) lifecycleObserver).onCreate(bundle);
            }
        }
    }

    public void onStart() {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnStart) {
                ((OnStart) lifecycleObserver).onStart();
            }
        }
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof SetPreferenceScreen) {
                ((SetPreferenceScreen) lifecycleObserver).setPreferenceScreen(preferenceScreen);
            }
        }
    }

    public void onResume() {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnResume) {
                ((OnResume) lifecycleObserver).onResume();
            }
        }
    }

    public void onPause() {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnPause) {
                ((OnPause) lifecycleObserver).onPause();
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnSaveInstanceState) {
                ((OnSaveInstanceState) lifecycleObserver).onSaveInstanceState(bundle);
            }
        }
    }

    public void onStop() {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnStop) {
                ((OnStop) lifecycleObserver).onStop();
            }
        }
    }

    public void onDestroy() {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnDestroy) {
                ((OnDestroy) lifecycleObserver).onDestroy();
            }
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if (lifecycleObserver instanceof OnPrepareOptionsMenu) {
                ((OnPrepareOptionsMenu) lifecycleObserver).onPrepareOptionsMenu(menu);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        for (LifecycleObserver lifecycleObserver : this.mObservers) {
            if ((lifecycleObserver instanceof OnOptionsItemSelected) && ((OnOptionsItemSelected) lifecycleObserver).onOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
}
