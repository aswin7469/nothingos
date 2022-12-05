package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.content.Context;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BleBroadcastSourceInfoDetailsFragment extends RestrictedDashboardFragment {
    BleBroadcastSourceInfo mBleBroadcastSourceInfo;
    CachedBluetoothDevice mCachedDevice;
    String mDeviceAddress;
    LocalBluetoothManager mManager;
    Integer mSourceInfoIndex = -1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "SourceInfoDetailsFrg";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1009;
    }

    public BleBroadcastSourceInfoDetailsFragment() {
        super("no_config_bluetooth");
    }

    CachedBluetoothDevice getCachedDevice(String str) {
        return this.mManager.getCachedDeviceManager().findDevice(this.mManager.getBluetoothAdapter().getRemoteDevice(str));
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
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
        } else if (this.mSourceInfoIndex != null) {
        } else {
            Log.w("SourceInfoDetailsFrg", "onAttach()  mSourceInfoIndex null!");
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.bcast_source_info_details_fragment;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (this.mCachedDevice != null && this.mBleBroadcastSourceInfo != null) {
            arrayList.add(new BleBroadcastSourceInfoDetailsController(context, this, this.mBleBroadcastSourceInfo, this.mCachedDevice, this.mSourceInfoIndex.intValue(), getSettingsLifecycle()));
        }
        return arrayList;
    }
}
