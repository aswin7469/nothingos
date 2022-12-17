package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.accessibility.VibrationPreferenceConfig;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class VibrationTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    protected final VibrationPreferenceConfig mPreferenceConfig;
    private final VibrationPreferenceConfig.SettingObserver mSettingsContentObserver;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    protected VibrationTogglePreferenceController(Context context, String str, VibrationPreferenceConfig vibrationPreferenceConfig) {
        super(context, str);
        this.mPreferenceConfig = vibrationPreferenceConfig;
        this.mSettingsContentObserver = new VibrationPreferenceConfig.SettingObserver(vibrationPreferenceConfig);
    }

    public void onStart() {
        this.mSettingsContentObserver.register(this.mContext);
    }

    public void onStop() {
        this.mSettingsContentObserver.unregister(this.mContext);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mSettingsContentObserver.onDisplayPreference(this, findPreference);
        findPreference.setEnabled(this.mPreferenceConfig.isPreferenceEnabled());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setEnabled(this.mPreferenceConfig.isPreferenceEnabled());
            preference.setSummary(this.mPreferenceConfig.getSummary());
        }
    }

    public boolean isChecked() {
        return this.mPreferenceConfig.isPreferenceEnabled() && this.mPreferenceConfig.readIntensity() != 0;
    }

    public boolean setChecked(boolean z) {
        int i = 0;
        if (!this.mPreferenceConfig.isPreferenceEnabled()) {
            return false;
        }
        if (z) {
            i = this.mPreferenceConfig.getDefaultIntensity();
        }
        boolean updateIntensity = this.mPreferenceConfig.updateIntensity(i);
        if (updateIntensity && z) {
            this.mPreferenceConfig.playVibrationPreview();
        }
        return updateIntensity;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }
}
