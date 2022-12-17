package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GroupConnectedBluetoothDeviceUpdater extends GroupBluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "connected_group_bt";
    }

    public GroupConnectedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z = isDeviceConnected(cachedBluetoothDevice) && isGroupDevice(cachedBluetoothDevice);
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupConnectedBluetoothDeviceUpdater", "isFilterMatched cachedDevice " + cachedBluetoothDevice.getName() + " addr " + cachedBluetoothDevice.getAddress() + " isConnected " + isDeviceConnected(cachedBluetoothDevice) + " isFilterMatched " + z);
        }
        return z;
    }

    public void forceUpdate() {
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager == null) {
            Log.e("GroupConnectedBluetoothDeviceUpdater", "forceUpdate() Bluetooth is not supported on this device");
            return;
        }
        Collection<CachedBluetoothDevice> cachedDevicesCopy = localBluetoothManager.getCachedDeviceManager().getCachedDevicesCopy();
        CachedBluetoothDeviceManager cachedDeviceManager = this.mLocalManager.getCachedDeviceManager();
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            for (CachedBluetoothDevice update : cachedDevicesCopy) {
                update(update);
            }
            return;
        }
        removeAllDevicesFromPreference();
        removePreferenceIfNecessary(cachedDevicesCopy, cachedDeviceManager);
    }

    private void removePreferenceIfNecessary(Collection<CachedBluetoothDevice> collection, CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        Iterator it = new ArrayList(this.mPreferenceMap.keySet()).iterator();
        while (it.hasNext()) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) it.next();
            if (!collection.contains(bluetoothDevice)) {
                CachedBluetoothDevice findDevice = cachedBluetoothDeviceManager.findDevice(bluetoothDevice);
                if (findDevice != null) {
                    removePreference(findDevice);
                } else if (findDevice == null) {
                    try {
                        CachedBluetoothDevice bluetoothDevice2 = ((BluetoothDevicePreference) this.mPreferenceMap.get(bluetoothDevice)).getBluetoothDevice();
                        if (bluetoothDevice2 != null) {
                            removePreference(bluetoothDevice2);
                        }
                    } catch (Exception e) {
                        Log.w("GroupConnectedBluetoothDeviceUpdater", " removePreferenceIfNecessary " + e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (!this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = new BluetoothDevicePreference(this.mPrefContext, cachedBluetoothDevice, true, i, true);
            bluetoothDevicePreference.setOnGearClickListener(this.mDeviceProfilesListener);
            bluetoothDevicePreference.setOnPreferenceClickListener(this);
            this.mPreferenceMap.put(device, bluetoothDevicePreference);
            this.mDevicePreferenceCallback.onDeviceAdded(bluetoothDevicePreference);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupConnectedBluetoothDeviceUpdater", " onPreferenceClick " + preference);
        }
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        return ((BluetoothDevicePreference) preference).getBluetoothDevice().setActive();
    }
}
