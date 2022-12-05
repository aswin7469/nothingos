package com.nt.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.nt.settings.glyphs.utils.BedTimeFormatter;
import java.time.LocalTime;
/* loaded from: classes2.dex */
public class GlyphsTimeSchedulePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private static final int OFF = 0;
    private static final int ON = 1;
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private PrimarySwitchPreference mPreference;
    private BedTimeFormatter mTimeFormatter;

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

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsTimeSchedulePreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeFormatter = new BedTimeFormatter(context);
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_schedule_time_ebable", 0) == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContentResolver, "led_effect_schedule_time_ebable", z ? 1 : 0);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = primarySwitchPreference;
        primarySwitchPreference.setChecked(isChecked());
    }

    private void refreshSummary() {
        this.mPreference.setSummary(this.mContext.getResources().getString(R.string.nt_glyphs_bed_time_summary, this.mTimeFormatter.getFormattedTimeString(getStartTime()), this.mTimeFormatter.getFormattedTimeString(getEndTime()), this.mTimeFormatter.getWeekSelectedSummary(this.mContext, getWeekSelected())));
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
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
