package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public class ConnectedBluetoothDeviceUpdater extends BluetoothDeviceUpdater {
    private static final boolean DBG = Log.isLoggable("ConnBluetoothDeviceUpdater", 3);
    private final AudioManager mAudioManager;

    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    protected String getPreferenceKey() {
        return "connected_bt";
    }

    public ConnectedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onAudioModeChanged() {
        forceUpdate();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006f  */
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isFilterMatched(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean isConnectedHfpDevice;
        boolean z;
        int mode = this.mAudioManager.getMode();
        int i = (mode == 1 || mode == 2 || mode == 3) ? 1 : 2;
        if (isDeviceConnected(cachedBluetoothDevice)) {
            boolean z2 = DBG;
            if (z2) {
                Log.d("ConnBluetoothDeviceUpdater", "isFilterMatched() current audio profile : " + i);
            }
            if (cachedBluetoothDevice.isConnectedHearingAidDevice()) {
                return false;
            }
            if (i == 1) {
                isConnectedHfpDevice = cachedBluetoothDevice.isConnectedHfpDevice();
            } else if (i != 2) {
                z = false;
                if (z2) {
                    Log.d("ConnBluetoothDeviceUpdater", "isFilterMatched() device : " + cachedBluetoothDevice.getName() + ", isFilterMatched : " + z);
                }
                if (z) {
                    if (isGroupDevice(cachedBluetoothDevice)) {
                        if (!z2) {
                            return false;
                        }
                        Log.d("ConnBluetoothDeviceUpdater", "It is isGroupDevice ignore showing ");
                        return false;
                    } else if (isPrivateAddr(cachedBluetoothDevice)) {
                        if (!z2) {
                            return false;
                        }
                        Log.d("ConnBluetoothDeviceUpdater", "It is isPrivateAddr ignore showing ");
                        return false;
                    }
                }
                return z;
            } else {
                isConnectedHfpDevice = cachedBluetoothDevice.isConnectedA2dpDevice();
            }
            z = !isConnectedHfpDevice;
            if (z2) {
            }
            if (z) {
            }
            return z;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice) {
        super.addPreference(cachedBluetoothDevice);
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) this.mPreferenceMap.get(device);
            bluetoothDevicePreference.setOnGearClickListener(null);
            bluetoothDevicePreference.hideSecondTarget(true);
            bluetoothDevicePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.bluetooth.ConnectedBluetoothDeviceUpdater$$ExternalSyntheticLambda0
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public final boolean onPreferenceClick(Preference preference) {
                    boolean lambda$addPreference$0;
                    lambda$addPreference$0 = ConnectedBluetoothDeviceUpdater.this.lambda$addPreference$0(preference);
                    return lambda$addPreference$0;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addPreference$0(Preference preference) {
        lambda$new$0(preference);
        return true;
    }
}
