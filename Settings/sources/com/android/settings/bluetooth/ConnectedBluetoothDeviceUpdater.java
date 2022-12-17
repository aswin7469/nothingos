package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class ConnectedBluetoothDeviceUpdater extends BluetoothDeviceUpdater {
    private static final boolean DBG = Log.isLoggable("ConnBluetoothDeviceUpdater", 3);
    private final AudioManager mAudioManager;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "connected_bt";
    }

    public ConnectedBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void onAudioModeChanged() {
        forceUpdate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice r9) {
        /*
            r8 = this;
            android.media.AudioManager r0 = r8.mAudioManager
            int r0 = r0.getMode()
            r1 = 2
            r2 = 1
            if (r0 == r2) goto L_0x0012
            if (r0 == r1) goto L_0x0012
            r3 = 3
            if (r0 != r3) goto L_0x0010
            goto L_0x0012
        L_0x0010:
            r0 = r1
            goto L_0x0013
        L_0x0012:
            r0 = r2
        L_0x0013:
            boolean r3 = r8.isDeviceConnected(r9)
            r4 = 0
            if (r3 == 0) goto L_0x0099
            boolean r3 = r8.isDeviceInCachedDevicesList(r9)
            if (r3 == 0) goto L_0x0099
            boolean r3 = DBG
            java.lang.String r5 = "ConnBluetoothDeviceUpdater"
            if (r3 == 0) goto L_0x003a
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "isFilterMatched() current audio profile : "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
        L_0x003a:
            boolean r6 = r9.isConnectedHearingAidDevice()
            if (r6 != 0) goto L_0x0099
            boolean r6 = r9.isConnectedLeAudioDevice()
            if (r6 == 0) goto L_0x0047
            goto L_0x0099
        L_0x0047:
            if (r0 == r2) goto L_0x0052
            if (r0 == r1) goto L_0x004d
            r0 = r4
            goto L_0x0057
        L_0x004d:
            boolean r0 = r9.isConnectedA2dpDevice()
            goto L_0x0056
        L_0x0052:
            boolean r0 = r9.isConnectedHfpDevice()
        L_0x0056:
            r0 = r0 ^ r2
        L_0x0057:
            if (r3 == 0) goto L_0x0079
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "isFilterMatched() device : "
            r1.append(r2)
            java.lang.String r2 = r9.getName()
            r1.append(r2)
            java.lang.String r2 = ", isFilterMatched : "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r5, r1)
        L_0x0079:
            if (r0 == 0) goto L_0x0097
            boolean r1 = r8.isGroupDevice(r9)
            if (r1 == 0) goto L_0x0089
            if (r3 == 0) goto L_0x0099
            java.lang.String r8 = "It is isGroupDevice ignore showing "
            android.util.Log.d(r5, r8)
            goto L_0x0099
        L_0x0089:
            boolean r8 = r8.isPrivateAddr(r9)
            if (r8 == 0) goto L_0x0097
            if (r3 == 0) goto L_0x0099
            java.lang.String r8 = "It is isPrivateAddr ignore showing "
            android.util.Log.d(r5, r8)
            goto L_0x0099
        L_0x0097:
            r4 = r0
        L_0x0099:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.ConnectedBluetoothDeviceUpdater.isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice):boolean");
    }

    /* access modifiers changed from: protected */
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice) {
        super.addPreference(cachedBluetoothDevice);
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        if (this.mPreferenceMap.containsKey(device)) {
            BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) this.mPreferenceMap.get(device);
            bluetoothDevicePreference.setOnGearClickListener((GearPreference.OnGearClickListener) null);
            bluetoothDevicePreference.hideSecondTarget(true);
            bluetoothDevicePreference.setOnPreferenceClickListener(new ConnectedBluetoothDeviceUpdater$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addPreference$0(Preference preference) {
        lambda$new$0(preference);
        return true;
    }
}
