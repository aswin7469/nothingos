package com.nt.settings.aod;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class NtAODDisplayModePreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private static final int AOD_DISPLAY_ALL_DAY = 0;
    private static final int AOD_DISPLAY_SCHEDULE = 1;
    private static final String KEY_AOD_DISPLAY_MODE = "aod_display_mode";
    private final UpdateDisplayModeCallback mCallback;
    private int mCurrentMode;
    private DropDownPreference mPreference;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtAODDisplayModePreferenceController(Context context, String str, UpdateDisplayModeCallback updateDisplayModeCallback) {
        super(context, str);
        this.mCallback = updateDisplayModeCallback;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (DropDownPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        int currentMode = getCurrentMode();
        this.mCurrentMode = currentMode;
        this.mPreference.setValueIndex(currentMode);
    }

    private int getCurrentMode() {
        int i;
        int i2 = Settings.Secure.getInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, 0);
        if (i2 == 0) {
            i = R.string.nt_aod_display_none;
        } else if (i2 == 1) {
            i = R.string.nt_aod_display_custom_title;
        } else {
            i = R.string.nt_aod_display_none;
        }
        return this.mPreference.findIndexOfValue(this.mContext.getString(i));
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public final boolean onPreferenceChange(Preference preference, Object obj) {
        int findIndexOfValue = this.mPreference.findIndexOfValue((String) obj);
        if (findIndexOfValue == this.mCurrentMode) {
            return false;
        }
        if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(R.string.nt_aod_display_none))) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, 0);
        } else if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(R.string.nt_aod_display_custom_title))) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, 1);
        }
        this.mCallback.updateDisplayMode(this.mContext);
        this.mCurrentMode = findIndexOfValue;
        return true;
    }
}
