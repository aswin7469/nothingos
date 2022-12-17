package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class GroupBluetoothDevicesBondedUpdater extends GroupBluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private int mGroupId;
    private GroupUtils mGroupUtils;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "group_options_bonded_devices_updater";
    }

    public GroupBluetoothDevicesBondedUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, int i) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mGroupUtils = new GroupUtils(context);
        this.mGroupId = i;
    }

    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupBluetoothDevicesBondedUpdater", "isFilterMatched " + cachedBluetoothDevice + "bond state  " + device.getBondState() + " mGroupId " + this.mGroupId);
        }
        return device.getBondState() == 12 && !device.isConnected() && isGroupDevice(cachedBluetoothDevice) && this.mGroupId == this.mGroupUtils.getGroupId(cachedBluetoothDevice);
    }

    public boolean onPreferenceClick(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        ((BluetoothDevicePreference) preference).getBluetoothDevice().connect();
        return true;
    }
}
