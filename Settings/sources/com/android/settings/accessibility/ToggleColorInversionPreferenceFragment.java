package com.android.settings.accessibility;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.R;
import com.android.settings.widget.SettingsMainSwitchPreference;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ToggleColorInversionPreferenceFragment extends ToggleFeaturePreferenceFragment {
    private final Handler mHandler = new Handler();
    private SettingsContentObserver mSettingsContentObserver;

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1817;
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void onPreferenceToggled(String str, boolean z) {
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(this.mComponentName, z);
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_inversion_enabled", z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_color_inversion_settings;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    public void onRemoveSwitchPreferenceToggleSwitch() {
        super.onRemoveSwitchPreferenceToggleSwitch();
        this.mToggleServiceSwitchPreference.setOnPreferenceClickListener(null);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        settingsMainSwitchPreference.setTitle(R.string.accessibility_display_inversion_switch_title);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mComponentName = AccessibilityShortcutController.COLOR_INVERSION_COMPONENT_NAME;
        this.mPackageName = getText(R.string.accessibility_display_inversion_preference_title);
        this.mHtmlDescription = getText(R.string.accessibility_display_inversion_preference_subtitle);
        this.mImageUri = new Uri.Builder().scheme("android.resource").authority(getPrefContext().getPackageName()).appendPath(String.valueOf(R.raw.accessibility_color_inversion_banner)).build();
        ArrayList arrayList = new ArrayList(1);
        arrayList.add("accessibility_display_inversion_enabled");
        this.mSettingsContentObserver = new SettingsContentObserver(this.mHandler, arrayList) { // from class: com.android.settings.accessibility.ToggleColorInversionPreferenceFragment.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                ToggleColorInversionPreferenceFragment.this.updateSwitchBarToggleSwitch();
            }
        };
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
        this.mSettingsContentObserver.register(getContentResolver());
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        this.mSettingsContentObserver.unregister(getContentResolver());
        super.onPause();
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_url_color_inversion;
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void updateSwitchBarToggleSwitch() {
        boolean z = false;
        if (Settings.Secure.getInt(getContentResolver(), "accessibility_display_inversion_enabled", 0) == 1) {
            z = true;
        }
        if (this.mToggleServiceSwitchPreference.isChecked() == z) {
            return;
        }
        this.mToggleServiceSwitchPreference.setChecked(z);
    }
}
