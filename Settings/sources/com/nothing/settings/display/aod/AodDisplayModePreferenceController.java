package com.nothing.settings.display.aod;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class AodDisplayModePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    public static final int AOD_DISPLAY_ALL_DAY = 0;
    public static final int AOD_DISPLAY_SCHEDULE = 1;
    private static final String KEY_AOD_DISPLAY_MODE = "aod_display_mode";
    private final UpdateDisplayModeCallback mCallback;
    private int mCurrentMode;
    private DropDownPreference mDropDownPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AodDisplayModePreferenceController(Context context, String str, UpdateDisplayModeCallback updateDisplayModeCallback) {
        super(context, str);
        this.mCallback = updateDisplayModeCallback;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mDropDownPreference = (DropDownPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        int currentMode = getCurrentMode();
        this.mCurrentMode = currentMode;
        this.mDropDownPreference.setValueIndex(currentMode);
    }

    private int getCurrentMode() {
        int i;
        int i2 = Settings.Secure.getInt(this.mContext.getContentResolver(), "aod_display_mode", 1);
        if (i2 == 0) {
            i = R$string.nt_aod_display_none;
        } else if (i2 == 1) {
            i = R$string.nt_aod_display_custom_title;
        } else {
            i = R$string.nt_aod_display_none;
        }
        return this.mDropDownPreference.findIndexOfValue(this.mContext.getString(i));
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        int findIndexOfValue = this.mDropDownPreference.findIndexOfValue((String) obj);
        if (findIndexOfValue == this.mCurrentMode) {
            return false;
        }
        if (findIndexOfValue == this.mDropDownPreference.findIndexOfValue(this.mContext.getString(R$string.nt_aod_display_none))) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "aod_display_mode", 0);
        } else if (findIndexOfValue == this.mDropDownPreference.findIndexOfValue(this.mContext.getString(R$string.nt_aod_display_custom_title))) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "aod_display_mode", 1);
        }
        this.mCallback.updateDisplayMode(this.mContext);
        this.mCurrentMode = findIndexOfValue;
        return true;
    }
}
