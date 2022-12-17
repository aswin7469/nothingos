package com.android.settings.bluetooth;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class AvailableMediaBluetoothDeviceUpdater extends BluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private static final boolean DBG = Log.isLoggable("AvailableMediaBluetoothDeviceUpdater", 3);
    private final AudioManager mAudioManager;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "available_media_bt";
    }

    public AvailableMediaBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void onAudioModeChanged() {
        forceUpdate();
    }

    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        int mode = this.mAudioManager.getMode();
        int i = (mode == 1 || mode == 2 || mode == 3) ? 1 : 2;
        if (!isDeviceConnected(cachedBluetoothDevice) || !isDeviceInCachedDevicesList(cachedBluetoothDevice)) {
            return false;
        }
        boolean z2 = DBG;
        if (z2) {
            Log.d("AvailableMediaBluetoothDeviceUpdater", "isFilterMatched() current audio profile : " + i);
        }
        if (cachedBluetoothDevice.isConnectedHearingAidDevice() || cachedBluetoothDevice.isConnectedLeAudioDevice()) {
            Log.d("AvailableMediaBluetoothDeviceUpdater", "isFilterMatched() device : " + cachedBluetoothDevice.getName() + ", the profile is connected.");
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            if (i == 1) {
                z = cachedBluetoothDevice.isConnectedHfpDevice();
            } else if (i == 2) {
                z = cachedBluetoothDevice.isConnectedA2dpDevice();
            }
        }
        if (z2) {
            Log.d("AvailableMediaBluetoothDeviceUpdater", "isFilterMatched() device : " + cachedBluetoothDevice.getName() + ", isFilterMatched : " + z);
        }
        if (z) {
            if (isGroupDevice(cachedBluetoothDevice)) {
                if (!z2) {
                    return false;
                }
                Log.d("AvailableMediaBluetoothDeviceUpdater", "It is GroupDevice ignore showing ");
                return false;
            } else if (isPrivateAddr(cachedBluetoothDevice)) {
                if (!z2) {
                    return false;
                }
                Log.d("AvailableMediaBluetoothDeviceUpdater", "It is isPrivateAddr ignore showing ");
                return false;
            }
        }
        return z;
    }

    public boolean onPreferenceClick(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        return ((BluetoothDevicePreference) preference).getBluetoothDevice().setActive();
    }
}
