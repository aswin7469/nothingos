package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class BluetoothBroadcastSourceInfoEntries extends BleBroadcastSourceInfoUpdater implements Preference.OnPreferenceClickListener {
    public BluetoothBroadcastSourceInfoEntries(Context context, DashboardFragment dashboardFragment, BleBroadcastSourceInfoPreferenceCallback bleBroadcastSourceInfoPreferenceCallback, CachedBluetoothDevice cachedBluetoothDevice) {
        super(context, dashboardFragment, bleBroadcastSourceInfoPreferenceCallback, cachedBluetoothDevice);
    }

    public boolean onPreferenceClick(Preference preference) {
        BleBroadcastSourceInfo bleBroadcastSourceInfo = ((BleBroadcastSourceInfoPreference) preference).getBleBroadcastSourceInfo();
        BroadcastScanAssistanceUtils.debug("BluetoothBroadcastSourceInfoEntries", "onPreferenceClick: " + bleBroadcastSourceInfo);
        return true;
    }
}
