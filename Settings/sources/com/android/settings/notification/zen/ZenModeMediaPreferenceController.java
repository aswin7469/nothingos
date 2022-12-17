package com.android.settings.notification.zen;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class ZenModeMediaPreferenceController extends AbstractZenModePreferenceController implements Preference.OnPreferenceChangeListener {
    private final ZenModeBackend mBackend;

    public String getPreferenceKey() {
        return "zen_mode_media";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeMediaPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, "zen_mode_media", lifecycle);
        this.mBackend = ZenModeBackend.getInstance(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        NtCustSwitchPreference ntCustSwitchPreference = (NtCustSwitchPreference) preference;
        int zenMode = getZenMode();
        if (zenMode == 2) {
            ntCustSwitchPreference.setEnabled(false);
            ntCustSwitchPreference.setChecked(false);
        } else if (zenMode != 3) {
            ntCustSwitchPreference.setEnabled(true);
            ntCustSwitchPreference.setChecked(this.mBackend.isPriorityCategoryEnabled(64));
        } else {
            ntCustSwitchPreference.setEnabled(false);
            ntCustSwitchPreference.setChecked(true);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (ZenModeSettingsBase.DEBUG) {
            Log.d("PrefControllerMixin", "onPrefChange allowMedia=" + booleanValue);
        }
        this.mBackend.saveSoundPolicy(64, booleanValue);
        return true;
    }
}
