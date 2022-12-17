package com.android.settings.development;

import android.content.Context;
import android.net.ConnectivitySettingsManager;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class IngressRateLimitPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "ingress_rate_limit";
    }

    public IngressRateLimitPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            ConnectivitySettingsManager.setIngressRateLimitInBytesPerSecond(this.mContext, Long.parseLong(obj.toString()));
            return true;
        } catch (IllegalArgumentException e) {
            Log.e("IngressRateLimitPreferenceController", "invalid rate limit", e);
            return false;
        }
    }

    public void updateState(Preference preference) {
        String valueOf = String.valueOf(ConnectivitySettingsManager.getIngressRateLimitInBytesPerSecond(this.mContext));
        ListPreference listPreference = (ListPreference) preference;
        CharSequence[] entryValues = listPreference.getEntryValues();
        for (CharSequence contentEquals : entryValues) {
            if (valueOf.contentEquals(contentEquals)) {
                listPreference.setValue(valueOf);
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        ConnectivitySettingsManager.setIngressRateLimitInBytesPerSecond(this.mContext, -1);
        ((ListPreference) this.mPreference).setValue(String.valueOf(-1));
    }
}
