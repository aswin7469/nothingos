package com.android.settings.deviceinfo;

import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.deviceinfo.AbstractIpAddressPreferenceController;

public class IpAddressPreferenceController extends AbstractIpAddressPreferenceController implements PreferenceControllerMixin {
    public IpAddressPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_wifi_ip_address);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("wifi_ip_address");
        if (findPreference != null) {
            findPreference.setVisible(false);
        }
    }
}
