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
/* loaded from: classes.dex */
public abstract class GroupBluetoothDetailsController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStop, OnStart, BluetoothCallback {
    protected final Context mContext;
    protected final PreferenceFragmentCompat mFragment;
    protected LocalBluetoothManager mLocalManager;

    protected abstract void init(PreferenceScreen preferenceScreen);

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    protected abstract void loadDevices();

    protected abstract void refresh();

    public GroupBluetoothDetailsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, int i, Lifecycle lifecycle) {
        super(context);
        this.mContext = context;
        this.mFragment = preferenceFragmentCompat;
        lifecycle.addObserver(this);
        this.mLocalManager = Utils.getLocalBtManager(context);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mLocalManager.getEventManager().registerCallback(this);
        loadDevices();
        refresh();
    }

    public void onStop() {
        this.mLocalManager.getEventManager().unregisterCallback(this);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public final void displayPreference(PreferenceScreen preferenceScreen) {
        init(preferenceScreen);
        super.displayPreference(preferenceScreen);
    }
}
