package com.android.settings.bluetooth;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public class GroupBluetoothDevicesConnectedUpdater extends GroupBluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private final AudioManager mAudioManager;
    private int mGroupId;
    private GroupUtils mGroupUtils;

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    protected String getPreferenceKey() {
        return "group_devices_connected";
    }

    public GroupBluetoothDevicesConnectedUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, int i) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mGroupId = i;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mGroupUtils = new GroupUtils(context);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onAudioModeChanged() {
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupBluetoothDevicesConnectedUpdater", "onAudioModeChanged ");
        }
        forceUpdate();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0050  */
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        boolean isConnectedHfpDevice;
        int mode = this.mAudioManager.getMode();
        int i = (mode == 1 || mode == 2 || mode == 3) ? 1 : 2;
        if (isDeviceConnected(cachedBluetoothDevice)) {
            if (GroupBluetoothDeviceUpdater.DBG) {
                Log.d("GroupBluetoothDevicesConnectedUpdater", "isFilterMatched() current audio profile : " + i);
            }
            if (cachedBluetoothDevice.isConnectedHearingAidDevice()) {
                return false;
            }
            if (i == 1) {
                isConnectedHfpDevice = cachedBluetoothDevice.isConnectedHfpDevice();
            } else if (i == 2) {
                isConnectedHfpDevice = cachedBluetoothDevice.isConnectedA2dpDevice();
            }
            z = !isConnectedHfpDevice;
            if (GroupBluetoothDeviceUpdater.DBG) {
                Log.d("GroupBluetoothDevicesConnectedUpdater", "isFilterMatche cachedDevice : " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + ", isFilterMatched : " + z);
            }
            return !z && isGroupDevice(cachedBluetoothDevice) && this.mGroupId == this.mGroupUtils.getGroupId(cachedBluetoothDevice);
        }
        z = false;
        if (GroupBluetoothDeviceUpdater.DBG) {
        }
        if (!z) {
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupBluetoothDevicesConnectedUpdater", "onPreferenceClick " + preference);
        }
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        return ((BluetoothDevicePreference) preference).getBluetoothDevice().setActive();
    }
}
