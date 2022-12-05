package com.nt.settings.aod;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.display.darkmode.TimeFormatter;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import java.time.LocalTime;
/* loaded from: classes2.dex */
public class NtAODStartTimePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, TimePickerDialog.OnTimeSetListener {
    private Context mContext;
    private final NtAODDisplayModePreferenceController mDisplayModePreferenceController;
    private TimeFormatter mFormatter;
    private final StartTimePreferenceHost mHost;
    private String mStartTime;

    /* loaded from: classes2.dex */
    public interface StartTimePreferenceHost extends UpdateDisplayModeCallback {
        void showStartTimePicker();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "nt_aod_start_time";
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public NtAODStartTimePreferenceController(Context context, StartTimePreferenceHost startTimePreferenceHost, NtAODDisplayModePreferenceController ntAODDisplayModePreferenceController) {
        super(context);
        this.mHost = startTimePreferenceHost;
        this.mContext = context;
        this.mFormatter = new TimeFormatter(context);
        this.mDisplayModePreferenceController = ntAODDisplayModePreferenceController;
        String string = Settings.Secure.getString(context.getContentResolver(), "nt_aod_start_time");
        this.mStartTime = string;
        if (TextUtils.isEmpty(string)) {
            this.mStartTime = "0700";
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        if (!(preference instanceof RestrictedPreference)) {
            return;
        }
        preference.setSummary(this.mFormatter.of(LocalTime.of(NtAODSettingUtils.getHour(this.mStartTime), NtAODSettingUtils.getMin(this.mStartTime))));
        if (((RestrictedPreference) preference).isDisabledByAdmin()) {
            return;
        }
        preference.setVisible(isShow());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals("nt_aod_start_time", preference.getKey())) {
            return false;
        }
        this.mHost.showStartTimePicker();
        return true;
    }

    @Override // android.app.TimePickerDialog.OnTimeSetListener
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.mStartTime = buildTimeString(i, i2);
        Context context = this.mContext;
        if (context != null) {
            Settings.Secure.putString(context.getContentResolver(), "nt_aod_start_time", this.mStartTime);
            this.mHost.updateDisplayMode(this.mContext);
        }
    }

    public TimePickerDialog buildTimePicker(Activity activity) {
        return new TimePickerDialog(activity, this, NtAODSettingUtils.getHour(this.mStartTime), NtAODSettingUtils.getMin(this.mStartTime), this.mFormatter.is24HourFormat());
    }

    private String buildTimeString(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        if (i < 10) {
            sb.append("0");
            sb.append(i);
        } else {
            sb.append(i);
        }
        if (i2 < 10) {
            sb.append("0");
            sb.append(i2);
        } else {
            sb.append(i2);
        }
        return sb.toString();
    }

    private boolean isShow() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "aod_display_mode", 0) == 1;
    }
}
