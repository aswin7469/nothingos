package com.android.settings.connecteddevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.bluetooth.BluetoothDevicePreference;
import com.android.settings.bluetooth.BluetoothDeviceUpdater;
import com.android.settings.bluetooth.SavedBluetoothDeviceUpdater;
import com.android.settings.bluetooth.Utils;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class PreviouslyConnectedDevicePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback, BluetoothCallback {
    private static final int DOCK_DEVICE_INDEX = 9;
    private static final String KEY_SEE_ALL = "previously_connected_devices_see_all";
    private static final int MAX_DEVICE_NUM = 3;
    private BluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private LocalBluetoothAdapter mLocalAdapter;
    private PreferenceGroup mPreferenceGroup;
    private DockUpdater mSavedDockUpdater;
    Preference mSeeAllPreference;
    private LocalBluetoothManager manager;
    private static final String TAG = "PreviouslyDevicePreController";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final List<Preference> mDevicesList = new ArrayList();
    private final List<Preference> mDockDevicesList = new ArrayList();
    BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settings.connecteddevice.PreviouslyConnectedDevicePreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            PreviouslyConnectedDevicePreferenceController.this.updatePreferenceVisibility();
        }
    };
    IntentFilter mIntentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onAudioModeChanged() {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onBroadcastKeyGenerated() {
        super.onBroadcastKeyGenerated();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onScanningStateChanged(boolean z) {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PreviouslyConnectedDevicePreferenceController(Context context, String str) {
        super(context, str);
        this.mSavedDockUpdater = FeatureFactory.getFactory(context).getDockUpdaterFeatureProvider().getSavedDockUpdater(context, this);
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.manager = localBtManager;
        if (localBtManager != null) {
            this.mLocalAdapter = localBtManager.getBluetoothAdapter();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") || this.mSavedDockUpdater != null) ? 0 : 2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceGroup = preferenceGroup;
        this.mSeeAllPreference = preferenceGroup.findPreference(KEY_SEE_ALL);
        updatePreferenceVisibility();
        if (isAvailable()) {
            Context context = preferenceScreen.getContext();
            this.mBluetoothDeviceUpdater.setPrefContext(context);
            this.mSavedDockUpdater.setPreferenceContext(context);
            this.mBluetoothDeviceUpdater.forceUpdate();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mBluetoothDeviceUpdater.registerCallback();
        this.mSavedDockUpdater.registerCallback();
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        this.mBluetoothDeviceUpdater.refreshPreference();
        this.manager.getEventManager().registerCallback(this);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mBluetoothDeviceUpdater.unregisterCallback();
        this.mSavedDockUpdater.unregisterCallback();
        this.mContext.unregisterReceiver(this.mReceiver);
        this.manager.getEventManager().unregisterCallback(this);
    }

    public void init(DashboardFragment dashboardFragment) {
        this.mBluetoothDeviceUpdater = new SavedBluetoothDeviceUpdater(dashboardFragment.getContext(), dashboardFragment, this);
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceAdded(Preference preference) {
        List mostRecentlyConnectedDevices = this.mBluetoothAdapter.getMostRecentlyConnectedDevices();
        int indexOf = preference instanceof BluetoothDevicePreference ? mostRecentlyConnectedDevices.indexOf(((BluetoothDevicePreference) preference).getBluetoothDevice().getDevice()) : 9;
        if (DEBUG) {
            Log.d(TAG, "onDeviceAdded() " + ((Object) preference.getTitle()) + ", index of : " + indexOf);
            Iterator it = mostRecentlyConnectedDevices.iterator();
            while (it.hasNext()) {
                Log.d(TAG, "onDeviceAdded() most recently device : " + ((BluetoothDevice) it.next()).getName());
            }
        }
        addPreference(indexOf, preference);
        updatePreferenceVisibility();
    }

    private void addPreference(int i, Preference preference) {
        if (preference instanceof BluetoothDevicePreference) {
            if (i >= 0 && this.mDevicesList.size() >= i) {
                this.mDevicesList.add(i, preference);
            } else {
                this.mDevicesList.add(preference);
            }
        } else {
            this.mDockDevicesList.add(preference);
        }
        addPreference();
    }

    private void addPreference() {
        this.mPreferenceGroup.removeAll();
        this.mPreferenceGroup.addPreference(this.mSeeAllPreference);
        int deviceListSize = getDeviceListSize();
        for (int i = 0; i < deviceListSize; i++) {
            if (DEBUG) {
                Log.d(TAG, "addPreference() add device : " + ((Object) this.mDevicesList.get(i).getTitle()));
            }
            this.mDevicesList.get(i).setOrder(i);
            this.mPreferenceGroup.addPreference(this.mDevicesList.get(i));
        }
        if (this.mDockDevicesList.size() > 0) {
            for (int i2 = 0; i2 < getDockDeviceListSize(3 - deviceListSize); i2++) {
                if (DEBUG) {
                    Log.d(TAG, "addPreference() add dock device : " + ((Object) this.mDockDevicesList.get(i2).getTitle()));
                }
                this.mDockDevicesList.get(i2).setOrder(9);
                this.mPreferenceGroup.addPreference(this.mDockDevicesList.get(i2));
            }
        }
    }

    private int getDeviceListSize() {
        if (this.mDevicesList.size() >= 3) {
            return 3;
        }
        return this.mDevicesList.size();
    }

    private int getDockDeviceListSize(int i) {
        return this.mDockDevicesList.size() >= i ? i : this.mDockDevicesList.size();
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceRemoved(Preference preference) {
        if (preference instanceof BluetoothDevicePreference) {
            this.mDevicesList.remove(preference);
        } else {
            this.mDockDevicesList.remove(preference);
        }
        addPreference();
        updatePreferenceVisibility();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        updatePreferenceVisibility();
    }

    void setBluetoothDeviceUpdater(BluetoothDeviceUpdater bluetoothDeviceUpdater) {
        this.mBluetoothDeviceUpdater = bluetoothDeviceUpdater;
    }

    void setSavedDockUpdater(DockUpdater dockUpdater) {
        this.mSavedDockUpdater = dockUpdater;
    }

    void setPreferenceGroup(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
    }

    void updatePreferenceVisibility() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            this.mSeeAllPreference.setSummary("");
        } else {
            this.mSeeAllPreference.setSummary(this.mContext.getString(R.string.connected_device_see_all_summary));
        }
    }
}
