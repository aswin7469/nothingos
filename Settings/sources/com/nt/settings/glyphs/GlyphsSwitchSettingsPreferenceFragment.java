package com.nt.settings.glyphs;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
/* loaded from: classes2.dex */
public abstract class GlyphsSwitchSettingsPreferenceFragment extends DashboardFragment implements Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_switch_settings);
    private ContentObserver mContentObserver;
    private ContentResolver mContentResolver;
    private SwitchPreference mSwitchPreference;

    abstract String getFeatureString();

    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "GlyphsContact";
    }

    public int getMetricsCategory() {
        return 1853;
    }

    abstract String getSwitchKey();

    abstract String getSwitchTitle();

    int getLayoutId() {
        return R.xml.nt_glyphs_switch_settings;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference findPreference = preferenceScreen.findPreference("key_features");
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference("glyphs_switch");
        findPreference.setTitle(getFeatureString());
        this.mSwitchPreference.setTitle(getSwitchTitle());
        this.mSwitchPreference.setChecked(isChecked());
        this.mSwitchPreference.setOnPreferenceChangeListener(this);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(this.mContentObserver);
        }
        super.onDestroy();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContentResolver = getActivity().getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (GlyphsSwitchSettingsPreferenceFragment.this.mSwitchPreference == null) {
                    return;
                }
                GlyphsSwitchSettingsPreferenceFragment.this.mSwitchPreference.setChecked(GlyphsSwitchSettingsPreferenceFragment.this.isChecked());
            }
        };
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(getSwitchKey()), false, this.mContentObserver, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChecked() {
        return Settings.Global.getInt(getActivity().getContentResolver(), getSwitchKey(), 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(getActivity().getContentResolver(), getSwitchKey(), z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return getLayoutId();
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (preference == switchPreference) {
            setChecked(!switchPreference.isChecked());
        }
        return true;
    }
}
