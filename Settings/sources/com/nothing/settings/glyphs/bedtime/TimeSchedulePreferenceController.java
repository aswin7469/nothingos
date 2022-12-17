package com.nothing.settings.glyphs.bedtime;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.nothing.settings.glyphs.utils.BedTimeFormatter;
import com.nothing.settings.utils.NtUtils;
import java.time.LocalTime;

public class TimeSchedulePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private PrimarySwitchPreference mPreference;
    private BedTimeFormatter mTimeFormatter;

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

    public void onPause() {
    }

    public boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TimeSchedulePreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeFormatter = new BedTimeFormatter(context);
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_schedule_time_ebable", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        NtUtils.trackIntGlyph(this.mContext, "bts", z ? 1 : 0);
        return Settings.Global.putInt(this.mContentResolver, "led_effect_schedule_time_ebable", z);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = primarySwitchPreference;
        primarySwitchPreference.setChecked(isChecked());
    }

    private void refreshSummary() {
        this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R$string.nt_glyphs_bed_time_summary, new Object[]{this.mTimeFormatter.getFormattedTimeString(getStartTime()), this.mTimeFormatter.getFormattedTimeString(getEndTime()), this.mTimeFormatter.getWeekSelectedSummary(this.mContext, getWeekSelected())}));
    }

    public void onResume() {
        refreshSummary();
    }

    private LocalTime getStartTime() {
        int[] str2Array = this.mTimeFormatter.str2Array(Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_start_time"));
        return LocalTime.of(str2Array[0], str2Array[1]);
    }

    private LocalTime getEndTime() {
        int[] str2Array = this.mTimeFormatter.str2Array(Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_end_time"));
        return LocalTime.of(str2Array[0], str2Array[1]);
    }

    public String getWeekSelected() {
        return Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_week");
    }
}
