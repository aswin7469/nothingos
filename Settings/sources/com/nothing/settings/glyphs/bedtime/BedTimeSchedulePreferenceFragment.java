package com.nothing.settings.glyphs.bedtime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import java.time.LocalTime;

public class BedTimeSchedulePreferenceFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_time_schedule_settings);
    private ContentResolver mContentResolver;

    public int getDialogMetricsCategory(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 2005;
        }
        return 2004;
    }

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "BedTimeSchedule";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_time_schedule_settings;
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if ("key_time_end".equals(preference.getKey())) {
            writePreferenceClickMetric(preference);
            showDialog(1);
            return true;
        } else if (!"key_time_start".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preference);
        } else {
            writePreferenceClickMetric(preference);
            showDialog(0);
            return true;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContentResolver = getActivity().getContentResolver();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void showDialog(int i) {
        super.showDialog(i);
    }

    /* renamed from: onCreateDialog */
    public void lambda$onCreateDialog$0(int i, TimePicker timePicker, int i2, int i3) {
        if (i == 0) {
            saveStartTime(i2, i3);
        } else {
            saveEndTime(i2, i3);
        }
    }

    public Dialog onCreateDialog(int i) {
        LocalTime localTime;
        if (i != 0 && i != 1) {
            return super.onCreateDialog(i);
        }
        if (i == 0) {
            int[] startTime = getStartTime();
            localTime = LocalTime.of(startTime[0], startTime[1]);
        } else {
            int[] endTime = getEndTime();
            localTime = LocalTime.of(endTime[0], endTime[1]);
        }
        Context context = getContext();
        return new TimePickerDialog(context, new BedTimeSchedulePreferenceFragment$$ExternalSyntheticLambda0(this, i), localTime.getHour(), localTime.getMinute(), DateFormat.is24HourFormat(context));
    }

    private void saveStartTime(int i, int i2) {
        ContentResolver contentResolver = this.mContentResolver;
        Settings.Global.putString(contentResolver, "led_bed_time_custom_start_time", i + "," + i2);
        updatePreferenceStates();
    }

    private void saveEndTime(int i, int i2) {
        ContentResolver contentResolver = this.mContentResolver;
        Settings.Global.putString(contentResolver, "led_bed_time_custom_end_time", i + "," + i2);
        updatePreferenceStates();
    }

    private int[] getStartTime() {
        return str2Array(Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_start_time"));
    }

    private int[] getEndTime() {
        return str2Array(Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_end_time"));
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
