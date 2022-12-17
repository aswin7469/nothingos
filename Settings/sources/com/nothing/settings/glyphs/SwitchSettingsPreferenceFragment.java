package com.nothing.settings.glyphs;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public abstract class SwitchSettingsPreferenceFragment extends DashboardFragment implements Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_glyphs_switch_settings);
    private ContentObserver mContentObserver;
    private ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public SwitchPreference mSwitchPreference;

    public abstract String getFeatureString();

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "GlyphsContact";
    }

    public int getMetricsCategory() {
        return 1853;
    }

    public abstract String getSwitchKey();

    public abstract String getSwitchTitle();

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R$xml.nt_glyphs_switch_settings;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference findPreference = preferenceScreen.findPreference("key_features");
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference("glyphs_switch");
        findPreference.setTitle((CharSequence) getFeatureString());
        this.mSwitchPreference.setTitle((CharSequence) getSwitchTitle());
        this.mSwitchPreference.setChecked(isChecked());
        this.mSwitchPreference.setOnPreferenceChangeListener(this);
    }

    public void onDestroy() {
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(this.mContentObserver);
        }
        super.onDestroy();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContentResolver = getActivity().getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                if (SwitchSettingsPreferenceFragment.this.mSwitchPreference != null) {
                    SwitchSettingsPreferenceFragment.this.mSwitchPreference.setChecked(SwitchSettingsPreferenceFragment.this.isChecked());
                }
            }
        };
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(getSwitchKey()), false, this.mContentObserver, -1);
    }

    public boolean isChecked() {
        if (Settings.Global.getInt(getActivity().getContentResolver(), getSwitchKey(), 0) == 1) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(getActivity().getContentResolver(), getSwitchKey(), z ? 1 : 0);
    }

    public int getPreferenceScreenResId() {
        return getLayoutId();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (preference == switchPreference) {
            setChecked(!switchPreference.isChecked());
        }
        return true;
    }
}
