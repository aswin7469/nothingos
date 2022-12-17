package com.nothing.settings.glyphs.bedtime;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.nothing.settings.utils.NtUtils;

public class ScheduleTimeSwitchPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener {
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

    public ScheduleTimeSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        settingsMainSwitchPreference.setChecked(isChecked());
        settingsMainSwitchPreference.addOnSwitchChangeListener(this);
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "led_effect_schedule_time_ebable", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        NtUtils.trackIntGlyph(this.mContext, "bts", z ? 1 : 0);
        return Settings.Global.putInt(this.mContext.getContentResolver(), "led_effect_schedule_time_ebable", z);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        setChecked(z);
    }
}
