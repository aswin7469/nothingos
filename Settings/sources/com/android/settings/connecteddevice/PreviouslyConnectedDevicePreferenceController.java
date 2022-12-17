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
import com.android.settings.R$string;
import com.android.settings.bluetooth.BluetoothDevicePreference;
import com.android.settings.bluetooth.BluetoothDeviceUpdater;
import com.android.settings.bluetooth.SavedBluetoothDeviceUpdater;
import com.android.settings.bluetooth.Utils;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PreviouslyConnectedDevicePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback, BluetoothCallback {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int DOCK_DEVICE_INDEX = 9;
    private static final String KEY_SEE_ALL = "previously_connected_devices_see_all";
    private static final int MAX_DEVICE_NUM = 3;
    private static final String TAG = "PreviouslyDevicePreController";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private final List<Preference> mDevicesList = new ArrayList();
    private final List<Preference> mDockDevicesList = new ArrayList();
    IntentFilter mIntentFilter;
    private LocalBluetoothAdapter mLocalAdapter;
    private PreferenceGroup mPreferenceGroup;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            PreviouslyConnectedDevicePreferenceController.this.updatePreferenceVisibility();
        }
    };
    private DockUpdater mSavedDockUpdater;
    Preference mSeeAllPreference;
    private LocalBluetoothManager manager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    public void onAudioModeChanged() {
    }

    public /* bridge */ /* synthetic */ void onBroadcastKeyGenerated() {
        super.onBroadcastKeyGenerated();
    }

    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
    }

    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    public void onScanningStateChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PreviouslyConnectedDevicePreferenceController(Context context, String str) {
        super(context, str);
        this.mSavedDockUpdater = FeatureFactory.getFactory(context).getDockUpdaterFeatureProvider().getSavedDockUpdater(context, this);
        this.mIntentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.manager = localBtManager;
        if (localBtManager != null) {
            this.mLocalAdapter = localBtManager.getBluetoothAdapter();
        }
    }

    public int getAvailabilityStatus() {
        return (this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") || this.mSavedDockUpdater != null) ? 0 : 2;
    }

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

    public void onStart() {
        this.mBluetoothDeviceUpdater.registerCallback();
        this.mSavedDockUpdater.registerCallback();
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter, 2);
        this.mBluetoothDeviceUpdater.refreshPreference();
        this.manager.getEventManager().registerCallback(this);
    }

    public void onStop() {
        this.mBluetoothDeviceUpdater.unregisterCallback();
        this.mSavedDockUpdater.unregisterCallback();
        this.mContext.unregisterReceiver(this.mReceiver);
        this.manager.getEventManager().unregisterCallback(this);
    }

    public void init(DashboardFragment dashboardFragment) {
        this.mBluetoothDeviceUpdater = new SavedBluetoothDeviceUpdater(dashboardFragment.getContext(), dashboardFragment, this);
    }

    public void onDeviceAdded(Preference preference) {
        List<BluetoothDevice> mostRecentlyConnectedDevices = this.mBluetoothAdapter.getMostRecentlyConnectedDevices();
        int indexOf = preference instanceof BluetoothDevicePreference ? mostRecentlyConnectedDevices.indexOf(((BluetoothDevicePreference) preference).getBluetoothDevice().getDevice()) : 9;
        if (DEBUG) {
            Log.d(TAG, "onDeviceAdded() " + preference.getTitle() + ", index of : " + indexOf);
            for (BluetoothDevice name : mostRecentlyConnectedDevices) {
                Log.d(TAG, "onDeviceAdded() most recently device : " + name.getName());
            }
        }
        addPreference(indexOf, preference);
        updatePreferenceVisibility();
    }

    private void addPreference(int i, Preference preference) {
        if (!(preference instanceof BluetoothDevicePreference)) {
            this.mDockDevicesList.add(preference);
        } else if (i < 0 || this.mDevicesList.size() < i) {
            this.mDevicesList.add(preference);
        } else {
            this.mDevicesList.add(i, preference);
        }
        addPreference();
    }

    private void addPreference() {
        this.mPreferenceGroup.removeAll();
        this.mPreferenceGroup.addPreference(this.mSeeAllPreference);
        int deviceListSize = getDeviceListSize();
        for (int i = 0; i < deviceListSize; i++) {
            if (DEBUG) {
                Log.d(TAG, "addPreference() add device : " + this.mDevicesList.get(i).getTitle());
            }
            this.mDevicesList.get(i).setOrder(i);
            this.mPreferenceGroup.addPreference(this.mDevicesList.get(i));
        }
        if (this.mDockDevicesList.size() > 0) {
            for (int i2 = 0; i2 < getDockDeviceListSize(3 - deviceListSize); i2++) {
                if (DEBUG) {
                    Log.d(TAG, "addPreference() add dock device : " + this.mDockDevicesList.get(i2).getTitle());
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

    public void onDeviceRemoved(Preference preference) {
        if (preference instanceof BluetoothDevicePreference) {
            this.mDevicesList.remove(preference);
        } else {
            this.mDockDevicesList.remove(preference);
        }
        addPreference();
        updatePreferenceVisibility();
    }

    public void onBluetoothStateChanged(int i) {
        updatePreferenceVisibility();
    }

    /* access modifiers changed from: package-private */
    public void setBluetoothDeviceUpdater(BluetoothDeviceUpdater bluetoothDeviceUpdater) {
        this.mBluetoothDeviceUpdater = bluetoothDeviceUpdater;
    }

    /* access modifiers changed from: package-private */
    public void setSavedDockUpdater(DockUpdater dockUpdater) {
        this.mSavedDockUpdater = dockUpdater;
    }

    /* access modifiers changed from: package-private */
    public void setPreferenceGroup(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceVisibility() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            this.mSeeAllPreference.setSummary((CharSequence) this.mContext.getString(R$string.connected_device_see_all_summary));
        } else {
            this.mSeeAllPreference.setSummary((CharSequence) "");
        }
    }
}
