package com.android.settings.core;

import android.content.Context;
import android.content.IntentFilter;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.TwoStateButtonPreference;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.instrumentation.SettingsJankMonitor;
import com.android.settingslib.widget.MainSwitchPreference;

public abstract class TogglePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "TogglePrefController";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public abstract int getSliceHighlightMenuRes();

    public int getSliceType() {
        return 1;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public abstract boolean isChecked();

    public boolean isPublicSlice() {
        return false;
    }

    public boolean isSliceable() {
        return true;
    }

    public abstract boolean setChecked(boolean z);

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TogglePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference instanceof MainSwitchPreference) {
            ((MainSwitchPreference) findPreference).addOnSwitchChangeListener(new TogglePreferenceController$$ExternalSyntheticLambda0(this));
        }
        if (findPreference != null) {
            findPreference.setOnPreferenceChangeListener(this);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(Switch switchR, boolean z) {
        SettingsJankMonitor.detectToggleJank(getPreferenceKey(), switchR);
    }

    public void updateState(Preference preference) {
        if (preference instanceof TwoStatePreference) {
            ((TwoStatePreference) preference).setChecked(isChecked());
        } else if (preference instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference).setChecked(isChecked());
        } else if (preference instanceof TwoStateButtonPreference) {
            ((TwoStateButtonPreference) preference).setChecked(isChecked());
        } else {
            refreshSummary(preference);
        }
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        if ((preference instanceof PrimarySwitchPreference) || (preference instanceof TwoStateButtonPreference)) {
            FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().logClickedPreference(preference, getMetricsCategory());
        }
        return setChecked(((Boolean) obj).booleanValue());
    }
}
