package com.android.settings.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$id;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.LayoutPreference;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;
import com.nothing.settings.panel.PanelCircleDrawable;

public class BluetoothDetailsHeaderController extends BluetoothDetailsController {
    private CachedBluetoothDeviceManager mDeviceManager;
    private EntityHeaderController mHeaderController;
    private LocalBluetoothManager mLocalManager;

    public String getPreferenceKey() {
        return "bluetooth_device_header";
    }

    public BluetoothDetailsHeaderController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle, LocalBluetoothManager localBluetoothManager) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mLocalManager = localBluetoothManager;
        this.mDeviceManager = localBluetoothManager.getCachedDeviceManager();
    }

    public boolean isAvailable() {
        boolean anyMatch = this.mCachedDevice.getConnectableProfiles().stream().anyMatch(new BluetoothDetailsHeaderController$$ExternalSyntheticLambda0());
        boolean isNothingAppEnabled = NothingBluetoothUtil.getinstance().isNothingAppEnabled(this.mContext, "com.nothing.smartcenter");
        boolean z = NothingBluetoothUtil.getinstance().isNothingAppHasPermission(this.mContext, "com.nothing.smartcenter") && isNothingAppEnabled;
        Log.d("PrefControllerMixin", "isAvailable hasPerm:" + z + ", isEnabled:" + isNothingAppEnabled);
        if (Utils.isAdvancedDetailsHeader(this.mCachedDevice.getDevice()) || anyMatch || NothingBluetoothUtil.getinstance().isNothingEarDevice(this.mContext, this.mCachedDevice.getAddress()) || NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(this.mContext, this.mCachedDevice.getDevice()) || NothingBluetoothUtil.getinstance().isNothingEarDeviceFromMacAddr(this.mContext, this.mCachedDevice.getAddress(), this.mCachedDevice.getDevice().getName())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isAvailable$0(LocalBluetoothProfile localBluetoothProfile) {
        return localBluetoothProfile.getProfileId() == 22;
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference("bluetooth_device_header");
        this.mHeaderController = EntityHeaderController.newInstance(this.mFragment.getActivity(), this.mFragment, layoutPreference.findViewById(R$id.entity_header));
        preferenceScreen.addPreference(layoutPreference);
    }

    /* access modifiers changed from: protected */
    public void setHeaderProperties() {
        Pair<Drawable, String> btRainbowDrawableWithDescription = BluetoothUtils.getBtRainbowDrawableWithDescription(this.mContext, this.mCachedDevice);
        String connectionSummary = this.mCachedDevice.getConnectionSummary();
        if (TextUtils.isEmpty(connectionSummary)) {
            this.mHeaderController.setSecondSummary((CharSequence) null);
        } else {
            this.mHeaderController.setSecondSummary(this.mDeviceManager.getSubDeviceSummary(this.mCachedDevice));
        }
        this.mHeaderController.setLabel((CharSequence) this.mCachedDevice.getName());
        this.mHeaderController.setIcon((Drawable) new PanelCircleDrawable((Drawable) btRainbowDrawableWithDescription.first));
        this.mHeaderController.setIconContentDescription((String) btRainbowDrawableWithDescription.second);
        this.mHeaderController.setSummary((CharSequence) connectionSummary);
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        if (isAvailable()) {
            setHeaderProperties();
            this.mHeaderController.done((Activity) this.mFragment.getActivity(), true);
        }
    }
}
