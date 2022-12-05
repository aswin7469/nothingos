package com.android.settings.bluetooth;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public class GroupBluetoothDevicesAvailableMediaDeviceUpdater extends GroupBluetoothGroupDeviceUpdater {
    private final AudioManager mAudioManager;
    private int mGroupId;
    private GroupUtils mGroupUtils;

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    protected String getPreferenceKey() {
        return "group_options_active_devices";
    }

    public GroupBluetoothDevicesAvailableMediaDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, int i) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mGroupId = i;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mGroupUtils = new GroupUtils(context);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onAudioModeChanged() {
        forceUpdate();
    }

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        int mode = this.mAudioManager.getMode();
        int i = (mode == 1 || mode == 2 || mode == 3) ? 1 : 2;
        if (isDeviceConnected(cachedBluetoothDevice)) {
            boolean z2 = GroupBluetoothDeviceUpdater.DBG;
            if (z2) {
                Log.d("GroupBluetoothDevicesAvailableMediaDeviceUpdater", "isFilterMatched() current audio profile : " + i);
            }
            if (cachedBluetoothDevice.isConnectedHearingAidDevice()) {
                return false;
            }
            if (i != 1) {
                z = i != 2 ? false : cachedBluetoothDevice.isConnectedA2dpDevice();
            } else {
                z = cachedBluetoothDevice.isConnectedHfpDevice();
            }
            if (z2) {
                Log.d("GroupBluetoothDevicesAvailableMediaDeviceUpdater", "isFilterMatched " + z + " cachedDevice : " + cachedBluetoothDevice);
            }
        } else {
            z = false;
        }
        return z && isGroupDevice(cachedBluetoothDevice) && this.mGroupId == this.mGroupUtils.getGroupId(cachedBluetoothDevice);
    }
}
