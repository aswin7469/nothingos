package com.nothing.settings.glyphs.bedtime;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.nothing.settings.glyphs.utils.BedTimeFormatter;
import java.time.LocalTime;

public class CustomEndTimePreferenceController extends BasePreferenceController {
    private final ContentResolver mContentResolver;
    private final BedTimeFormatter mTimeFormatter;

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

    public CustomEndTimePreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeFormatter = new BedTimeFormatter(context);
        this.mContentResolver = context.getContentResolver();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        updateState(preferenceScreen.findPreference(getPreferenceKey()));
    }

    public final void updateState(Preference preference) {
        preference.setSummary((CharSequence) this.mTimeFormatter.getFormattedTimeString(getEndTime()));
    }

    private LocalTime getEndTime() {
        int[] str2Array = str2Array(Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_end_time"));
        return LocalTime.of(str2Array[0], str2Array[1]);
    }

    private int[] str2Array(String str) {
        int[] iArr = new int[2];
        if (!(str == null || str.length() == 0)) {
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                iArr[i] = Integer.parseInt(split[i]);
            }
        }
        return iArr;
    }
}
