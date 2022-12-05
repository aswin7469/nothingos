package com.nt.settings.aod;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class NtAODDisplaySchedulePreferenceController extends TogglePreferenceController {
    private static final String KEY_AOD_DISPLAY_MODE = "aod_display_mode";
    private static final String KEY_AOD_DISPLAY_SCHEDULE = "nt_aod_display_schedule";
    private final UpdateDisplayModeCallback mCallback;

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_AOD_DISPLAY_SCHEDULE;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtAODDisplaySchedulePreferenceController(Context context, String str, UpdateDisplayModeCallback updateDisplayModeCallback) {
        super(context, str);
        this.mCallback = updateDisplayModeCallback;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, 0) == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        boolean putInt = Settings.Secure.putInt(this.mContext.getContentResolver(), KEY_AOD_DISPLAY_MODE, z ? 1 : 0);
        this.mCallback.updateDisplayMode(this.mContext);
        return putInt;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
    }
}
