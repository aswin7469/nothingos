package com.android.settings.bluetooth;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.dashboard.DashboardFragment;

public class GroupBluetoothDevicesConnectedUpdater extends GroupBluetoothDeviceUpdater implements Preference.OnPreferenceClickListener {
    private final AudioManager mAudioManager;
    private int mGroupId;
    private GroupUtils mGroupUtils;

    /* access modifiers changed from: protected */
    public String getPreferenceKey() {
        return "group_devices_connected";
    }

    public GroupBluetoothDevicesConnectedUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, int i) {
        super(context, dashboardFragment, devicePreferenceCallback);
        this.mGroupId = i;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mGroupUtils = new GroupUtils(context);
    }

    public void onAudioModeChanged() {
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupBluetoothDevicesConnectedUpdater", "onAudioModeChanged ");
        }
        forceUpdate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice r8) {
        /*
            r7 = this;
            android.media.AudioManager r0 = r7.mAudioManager
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
            boolean r3 = r7.isDeviceConnected(r8)
            java.lang.String r4 = "GroupBluetoothDevicesConnectedUpdater"
            r5 = 0
            if (r3 == 0) goto L_0x004b
            boolean r3 = com.android.settings.bluetooth.GroupBluetoothDeviceUpdater.DBG
            if (r3 == 0) goto L_0x0034
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r6 = "isFilterMatched() current audio profile : "
            r3.append(r6)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r4, r3)
        L_0x0034:
            boolean r3 = r8.isConnectedHearingAidDevice()
            if (r3 == 0) goto L_0x003b
            return r5
        L_0x003b:
            if (r0 == r2) goto L_0x0045
            if (r0 == r1) goto L_0x0040
            goto L_0x004b
        L_0x0040:
            boolean r0 = r8.isConnectedA2dpDevice()
            goto L_0x0049
        L_0x0045:
            boolean r0 = r8.isConnectedHfpDevice()
        L_0x0049:
            r0 = r0 ^ r2
            goto L_0x004c
        L_0x004b:
            r0 = r5
        L_0x004c:
            boolean r1 = com.android.settings.bluetooth.GroupBluetoothDeviceUpdater.DBG
            if (r1 == 0) goto L_0x0078
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "isFilterMatche cachedDevice : "
            r1.append(r3)
            r1.append(r8)
            java.lang.String r3 = " name "
            r1.append(r3)
            java.lang.String r3 = r8.getName()
            r1.append(r3)
            java.lang.String r3 = ", isFilterMatched : "
            r1.append(r3)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r4, r1)
        L_0x0078:
            if (r0 == 0) goto L_0x008b
            boolean r0 = r7.isGroupDevice(r8)
            if (r0 == 0) goto L_0x008b
            int r0 = r7.mGroupId
            com.android.settings.bluetooth.GroupUtils r7 = r7.mGroupUtils
            int r7 = r7.getGroupId((com.android.settingslib.bluetooth.CachedBluetoothDevice) r8)
            if (r0 != r7) goto L_0x008b
            goto L_0x008c
        L_0x008b:
            r2 = r5
        L_0x008c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.GroupBluetoothDevicesConnectedUpdater.isFilterMatched(com.android.settingslib.bluetooth.CachedBluetoothDevice):boolean");
    }

    public boolean onPreferenceClick(Preference preference) {
        if (GroupBluetoothDeviceUpdater.DBG) {
            Log.d("GroupBluetoothDevicesConnectedUpdater", "onPreferenceClick " + preference);
        }
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        return ((BluetoothDevicePreference) preference).getBluetoothDevice().setActive();
    }
}
