package com.nothing.settings.display.aod;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class AodScheduleSwitchPreferenceController extends TogglePreferenceController {
    public static final String KEY_AOD_DISPLAY_MODE = "aod_display_mode";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f257ON = 1;
    private static final String TAG = "AodScheduleSwitchPreferenceController";
    private final UpdateDisplayModeCallback mCallback;
    private SwitchPreference mPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AodScheduleSwitchPreferenceController(Context context, String str, UpdateDisplayModeCallback updateDisplayModeCallback) {
        super(context, str);
        this.mCallback = updateDisplayModeCallback;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(KEY_AOD_DISPLAY_MODE);
        setSummary(isChecked());
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, 1) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, z ? 1 : 0);
        setSummary(z);
        this.mCallback.updateDisplayMode(this.mContext);
        return true;
    }

    private void setSummary(boolean z) {
        SwitchPreference switchPreference = this.mPreference;
        if (switchPreference != null) {
            switchPreference.setSummary(z ? R$string.nt_aod_display_custom_title : R$string.nt_aod_display_none);
        }
    }
}
