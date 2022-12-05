package com.android.settings.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public abstract class GroupBluetoothDeviceUpdater extends BluetoothDeviceUpdater {
    protected static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;

    public GroupBluetoothDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        super(context, dashboardFragment, devicePreferenceCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.bluetooth.BluetoothDeviceUpdater
    public void addPreference(CachedBluetoothDevice cachedBluetoothDevice) {
        addPreference(cachedBluetoothDevice, 3);
    }

    public void launchgroupOptions(Preference preference) {
        if (DBG) {
            Log.d("GroupBluetoothDeviceUpdater", " launchgroupOptions :" + preference);
        }
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        Bundle bundle = new Bundle();
        bundle.putInt("group_id", ((GroupBluetoothSettingsPreference) preference).getGroupId());
        new SubSettingLauncher(this.mFragment.getContext()).setDestination(GroupBluetoothFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.group_options).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
    }
}
