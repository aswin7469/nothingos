package com.android.settings.notification.zen;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class ZenModeRepeatCallersPreferenceController extends AbstractZenModePreferenceController implements Preference.OnPreferenceChangeListener {
    private final ZenModeBackend mBackend;
    private final int mRepeatCallersThreshold;

    public String getPreferenceKey() {
        return "zen_mode_repeat_callers";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeRepeatCallersPreferenceController(Context context, Lifecycle lifecycle, int i) {
        super(context, "zen_mode_repeat_callers", lifecycle);
        this.mRepeatCallersThreshold = i;
        this.mBackend = ZenModeBackend.getInstance(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        setRepeatCallerSummary(preferenceScreen.findPreference("zen_mode_repeat_callers"));
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        NtCustSwitchPreference ntCustSwitchPreference = (NtCustSwitchPreference) preference;
        int zenMode = getZenMode();
        if (zenMode == 2 || zenMode == 3) {
            ntCustSwitchPreference.setEnabled(false);
            ntCustSwitchPreference.setChecked(false);
            return;
        }
        if (this.mBackend.isPriorityCategoryEnabled(8) && this.mBackend.getPriorityCallSenders() == 0) {
            ntCustSwitchPreference.setEnabled(false);
            ntCustSwitchPreference.setChecked(true);
            return;
        }
        ntCustSwitchPreference.setEnabled(true);
        ntCustSwitchPreference.setChecked(this.mBackend.isPriorityCategoryEnabled(16));
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (ZenModeSettingsBase.DEBUG) {
            Log.d("PrefControllerMixin", "onPrefChange allowRepeatCallers=" + booleanValue);
        }
        this.mMetricsFeatureProvider.action(this.mContext, 171, booleanValue);
        this.mBackend.saveSoundPolicy(16, booleanValue);
        return true;
    }

    private void setRepeatCallerSummary(Preference preference) {
        preference.setSummary((CharSequence) this.mContext.getString(R$string.zen_mode_repeat_callers_summary, new Object[]{Integer.valueOf(this.mRepeatCallersThreshold)}));
    }
}
