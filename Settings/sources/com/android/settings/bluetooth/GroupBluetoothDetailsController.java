package com.android.settings.bluetooth;

import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class GroupBluetoothDetailsController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStop, OnStart, BluetoothCallback {
    protected final Context mContext;
    protected final PreferenceFragmentCompat mFragment;
    protected LocalBluetoothManager mLocalManager;

    /* access modifiers changed from: protected */
    public abstract void init(PreferenceScreen preferenceScreen);

    public boolean isAvailable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract void loadDevices();

    /* access modifiers changed from: protected */
    public abstract void refresh();

    public GroupBluetoothDetailsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, int i, Lifecycle lifecycle) {
        super(context);
        this.mContext = context;
        this.mFragment = preferenceFragmentCompat;
        lifecycle.addObserver(this);
        this.mLocalManager = Utils.getLocalBtManager(context);
    }

    public void onStart() {
        this.mLocalManager.getEventManager().registerCallback(this);
        loadDevices();
        refresh();
    }

    public void onStop() {
        this.mLocalManager.getEventManager().unregisterCallback(this);
    }

    public final void displayPreference(PreferenceScreen preferenceScreen) {
        init(preferenceScreen);
        super.displayPreference(preferenceScreen);
    }
}
