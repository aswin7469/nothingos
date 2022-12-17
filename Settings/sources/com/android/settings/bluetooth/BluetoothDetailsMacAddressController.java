package com.android.settings.bluetooth;

import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class BluetoothDetailsMacAddressController extends BluetoothDetailsController {
    private Preference mFooterPreference;

    public String getPreferenceKey() {
        return "device_details_footer";
    }

    public BluetoothDetailsMacAddressController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        Preference findPreference = preferenceScreen.findPreference("device_details_footer");
        this.mFooterPreference = findPreference;
        findPreference.setTitle(R$string.bluetooth);
        this.mFooterPreference.setSummary((CharSequence) this.mCachedDevice.getAddress());
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        this.mFooterPreference.setSummary((CharSequence) this.mCachedDevice.getAddress());
    }
}
