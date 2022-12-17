package com.android.settings.connecteddevice;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.Uri;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.slices.SlicePreferenceController;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;

public class ConnectedDeviceDashboardFragment extends DashboardFragment {
    public static final boolean DBG_GROUP = Log.isLoggable("Group", 3);
    private static final boolean DEBUG = Log.isLoggable("ConnectedDeviceFrag", 3);
    static final String KEY_AVAILABLE_DEVICES = "available_device_list";
    static final String KEY_CONNECTED_DEVICES = "connected_device_list";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.connected_devices);
    BluetoothAdapter mBluetoothAdapter;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConnectedDeviceFrag";
    }

    public int getMetricsCategory() {
        return 747;
    }

    public int getHelpResource() {
        return R$string.help_url_connected_devices;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.connected_devices;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        NothingBluetoothUtil.getinstance().startBleScanning(this.mBluetoothAdapter);
        boolean z = DeviceConfig.getBoolean("settings_ui", "bt_near_by_suggestion_enabled", true);
        String initialCallingPackage = ((SettingsActivity) getActivity()).getInitialCallingPackage();
        String action = getIntent() != null ? getIntent().getAction() : "";
        if (DEBUG) {
            Log.d("ConnectedDeviceFrag", "onAttach() calling package name is : " + initialCallingPackage + ", action : " + action);
        }
        ((AvailableMediaDeviceGroupController) use(AvailableMediaDeviceGroupController.class)).init(this);
        ((ConnectedDeviceGroupController) use(ConnectedDeviceGroupController.class)).init(this);
        ((SavedTwsDeviceGroupController) use(SavedTwsDeviceGroupController.class)).init(this);
        ((PreviouslyConnectedDevicePreferenceController) use(PreviouslyConnectedDevicePreferenceController.class)).init(this);
        ((GroupConnectedBluetoothDevicesController) use(GroupConnectedBluetoothDevicesController.class)).init(this);
        ((GroupPreviouslyConnectedDevicePreferenceController) use(GroupPreviouslyConnectedDevicePreferenceController.class)).init(this);
        ((SlicePreferenceController) use(SlicePreferenceController.class)).setSliceUri(z ? Uri.parse(getString(R$string.config_nearby_devices_slice_uri)) : null);
        ((DiscoverableFooterPreferenceController) use(DiscoverableFooterPreferenceController.class)).setAlwaysDiscoverable(isAlwaysDiscoverable(initialCallingPackage, action));
    }

    /* access modifiers changed from: package-private */
    public boolean isAlwaysDiscoverable(String str, String str2) {
        if (TextUtils.equals("com.android.settings.SEARCH_RESULT_TRAMPOLINE", str2)) {
            return false;
        }
        if (TextUtils.equals("com.android.settings", str) || TextUtils.equals("com.android.systemui", str)) {
            return true;
        }
        return false;
    }

    public void onDetach() {
        super.onDetach();
        NothingBluetoothUtil.getinstance().stopBleScanning(this.mBluetoothAdapter);
    }
}
