package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.content.Context;
import android.util.Log;
import com.android.settings.R$xml;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class BleBroadcastSourceInfoDetailsFragment extends RestrictedDashboardFragment {
    BleBroadcastSourceInfo mBleBroadcastSourceInfo;
    CachedBluetoothDevice mCachedDevice;
    String mDeviceAddress;
    LocalBluetoothManager mManager;
    Integer mSourceInfoIndex = -1;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SourceInfoDetailsFrg";
    }

    public int getMetricsCategory() {
        return 1009;
    }

    public BleBroadcastSourceInfoDetailsFragment() {
        super("no_config_bluetooth");
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getCachedDevice(String str) {
        return this.mManager.getCachedDeviceManager().findDevice(this.mManager.getBluetoothAdapter().getRemoteDevice(str));
    }

    public void onAttach(Context context) {
        this.mDeviceAddress = getArguments().getString("device_address");
        this.mManager = Utils.getLocalBtManager(context);
        this.mBleBroadcastSourceInfo = getArguments().getParcelable("broadcast_source_info");
        this.mCachedDevice = getCachedDevice(this.mDeviceAddress);
        this.mSourceInfoIndex = Integer.valueOf(getArguments().getInt("broadcast_source_index"));
        super.onAttach(context);
        if (this.mCachedDevice == null) {
            Log.w("SourceInfoDetailsFrg", "onAttach() CachedDevice is null!");
            finish();
        } else if (this.mBleBroadcastSourceInfo == null) {
            Log.w("SourceInfoDetailsFrg", "onAttach()  mBleBroadcastSourceInfo null!");
            finish();
        } else if (this.mSourceInfoIndex == null) {
            Log.w("SourceInfoDetailsFrg", "onAttach()  mSourceInfoIndex null!");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bcast_source_info_details_fragment;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (!(this.mCachedDevice == null || this.mBleBroadcastSourceInfo == null)) {
            Context context2 = context;
            arrayList.add(new BleBroadcastSourceInfoDetailsController(context2, this, this.mBleBroadcastSourceInfo, this.mCachedDevice, this.mSourceInfoIndex.intValue(), getSettingsLifecycle()));
        }
        return arrayList;
    }
}
