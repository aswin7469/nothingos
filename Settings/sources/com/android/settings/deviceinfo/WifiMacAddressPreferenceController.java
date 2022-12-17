package com.android.settings.deviceinfo;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.R$string;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.deviceinfo.AbstractWifiMacAddressPreferenceController;

public class WifiMacAddressPreferenceController extends AbstractWifiMacAddressPreferenceController implements PreferenceControllerMixin {
    public WifiMacAddressPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_wifi_mac_address);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (Utils.isSupportCTPA(this.mContext)) {
            Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
            CharSequence summary = findPreference.getSummary();
            String string = Utils.getString(this.mContext, "ext_wifi_mac_address");
            String string2 = this.mContext.getString(R$string.status_unavailable);
            Log.d("PrefControllerMixin", "displayPreference: macAddress = " + string + " oldValue = " + summary + " unAvailable = " + string2);
            if (string == null || string.isEmpty()) {
                string = string2;
            }
            if (summary != null && ("02:00:00:00:00:00".equals(summary) || string2.equals(summary))) {
                findPreference.setSummary((CharSequence) string);
            }
        }
        Preference findPreference2 = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference2 != null) {
            findPreference2.setVisible(false);
        }
    }
}
