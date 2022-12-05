package com.nt.settings.glyphs;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
/* loaded from: classes2.dex */
public class GlyphsScheduleTimeSwitchPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
    private static final int OFF = 0;
    private static final int ON = 1;
    private SettingsMainSwitchPreference mSwitchPreference;

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

    public GlyphsScheduleTimeSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSwitchPreference = settingsMainSwitchPreference;
        settingsMainSwitchPreference.setChecked(isChecked());
        this.mSwitchPreference.addOnSwitchChangeListener(this);
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "led_effect_schedule_time_ebable", 0) == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContext.getContentResolver(), "led_effect_schedule_time_ebable", z ? 1 : 0);
    }

    @Override // com.android.settingslib.widget.OnMainSwitchChangeListener
    public void onSwitchChanged(Switch r1, boolean z) {
        setChecked(z);
    }
}
