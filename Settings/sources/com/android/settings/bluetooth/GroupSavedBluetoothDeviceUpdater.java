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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class GroupSavedBluetoothDeviceUpdater extends GroupBluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    protected String getPreferenceKey() {
        return "saved_group_bt";
    }

    public GroupSavedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public void forceUpdate() {
        if (this.mBluetoothAdapter.isEnabled()) {
            CachedBluetoothDeviceManager cachedDeviceManager = this.mLocalManager.getCachedDeviceManager();
            List<BluetoothDevice> mostRecentlyConnectedDevices = this.mBluetoothAdapter.getMostRecentlyConnectedDevices();
            removePreferenceIfNecessary(mostRecentlyConnectedDevices, cachedDeviceManager);
            for (BluetoothDevice bluetoothDevice : mostRecentlyConnectedDevices) {
                CachedBluetoothDevice findDevice = cachedDeviceManager.findDevice(bluetoothDevice);
                if (findDevice != null) {
                    update(findDevice);
                }
            }
            return;
        }
        removeAllDevicesFromPreference();
    }

    private void removePreferenceIfNecessary(List<BluetoothDevice> list, CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        Iterator it = new ArrayList(this.mPreferenceMap.keySet()).iterator();
        while (it.hasNext()) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) it.next();
            if (!list.contains(bluetoothDevice)) {
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
                        Log.w("GroupSavedBluetoothDeviceUpdater", "removePreferenceIfNecessary " + e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public void update(CachedBluetoothDevice cachedBluetoothDevice) {
        if (isFilterMatched(cachedBluetoothDevice)) {
            addPreference(cachedBluetoothDevice, 3);
        } else {
            removePreference(cachedBluetoothDevice);
        }
    }

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (GroupBluetoothDeviceUpdater.DBG) {
            StringBuilder sb = new StringBuilder();
            sb.append(" cachedDevice : ");
            sb.append(cachedBluetoothDevice);
            sb.append(", isConnected ");
            sb.append(device.isConnected());
            sb.append(" isBonded  ");
            sb.append(device.getBondState() == 12);
            Log.d("GroupSavedBluetoothDeviceUpdater", sb.toString());
        }
        return device.getBondState() == 12 && !device.isConnected() && isGroupDevice(cachedBluetoothDevice);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        ((BluetoothDevicePreference) preference).getBluetoothDevice().connect();
        return true;
    }
}
