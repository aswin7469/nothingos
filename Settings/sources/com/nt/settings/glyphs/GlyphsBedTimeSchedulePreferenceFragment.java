package com.nt.settings.glyphs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import java.time.LocalTime;
/* loaded from: classes2.dex */
public class GlyphsBedTimeSchedulePreferenceFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_time_schedule_settings);
    private ContentResolver mContentResolver;

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 2005;
        }
        return 2004;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "BedTimeSchedule";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1845;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_glyphs_time_schedule_settings;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        if ("key_time_end".equals(preference.getKey())) {
            writePreferenceClickMetric(preference);
            showDialog(1);
            return true;
        } else if ("key_time_start".equals(preference.getKey())) {
            writePreferenceClickMetric(preference);
            showDialog(0);
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContentResolver = getActivity().getContentResolver();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.accessibility.MagnificationModePreferenceController.DialogHelper
    public void showDialog(int i) {
        super.showDialog(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(int i, TimePicker timePicker, int i2, int i3) {
        if (i == 0) {
            saveStartTime(i2, i3);
        } else {
            saveETime(i2, i3);
        }
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public Dialog onCreateDialog(final int i) {
        LocalTime of;
        if (i == 0 || i == 1) {
            if (i == 0) {
                int[] startTime = getStartTime();
                of = LocalTime.of(startTime[0], startTime[1]);
            } else {
                int[] endTime = getEndTime();
                of = LocalTime.of(endTime[0], endTime[1]);
            }
            Context context = getContext();
            return new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() { // from class: com.nt.settings.glyphs.GlyphsBedTimeSchedulePreferenceFragment$$ExternalSyntheticLambda0
                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public final void onTimeSet(TimePicker timePicker, int i2, int i3) {
                    GlyphsBedTimeSchedulePreferenceFragment.this.lambda$onCreateDialog$0(i, timePicker, i2, i3);
                }
            }, of.getHour(), of.getMinute(), DateFormat.is24HourFormat(context));
        }
        return super.onCreateDialog(i);
    }

    private void saveStartTime(int i, int i2) {
        ContentResolver contentResolver = this.mContentResolver;
        Settings.Global.putString(contentResolver, "led_bed_time_custom_start_time", i + "," + i2);
        updatePreferenceStates();
    }

    private void saveETime(int i, int i2) {
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
        if (str != null && str.length() != 0) {
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                iArr[i] = Integer.parseInt(split[i]);
            }
        }
        return iArr;
    }
}
