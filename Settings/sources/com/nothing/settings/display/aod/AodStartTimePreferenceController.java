package com.nothing.settings.display.aod;

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

public class AodStartTimePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, TimePickerDialog.OnTimeSetListener {
    private Context mContext;
    private TimeFormatter mFormatter;
    private final StartTimePreferenceHost mHost;
    private String mStartTime;

    public interface StartTimePreferenceHost extends UpdateDisplayModeCallback {
        void showStartTimePicker();
    }

    public String getPreferenceKey() {
        return "nt_aod_start_time";
    }

    public boolean isAvailable() {
        return true;
    }

    public AodStartTimePreferenceController(Context context, StartTimePreferenceHost startTimePreferenceHost) {
        super(context);
        this.mHost = startTimePreferenceHost;
        this.mContext = context;
        this.mFormatter = new TimeFormatter(context);
        String string = Settings.Secure.getString(context.getContentResolver(), "nt_aod_start_time");
        this.mStartTime = string;
        if (TextUtils.isEmpty(string)) {
            this.mStartTime = "0700";
        }
    }

    public void updateState(Preference preference) {
        if (preference instanceof RestrictedPreference) {
            preference.setSummary((CharSequence) this.mFormatter.mo12819of(LocalTime.of(AodUtils.getHour(this.mStartTime), AodUtils.getMin(this.mStartTime))));
            if (!((RestrictedPreference) preference).isDisabledByAdmin()) {
                preference.setVisible(isShow());
            }
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals("nt_aod_start_time", preference.getKey())) {
            return false;
        }
        this.mHost.showStartTimePicker();
        return true;
    }

    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.mStartTime = buildTimeString(i, i2);
        Context context = this.mContext;
        if (context != null) {
            Settings.Secure.putString(context.getContentResolver(), "nt_aod_start_time", this.mStartTime);
            this.mHost.updateDisplayMode(this.mContext);
        }
    }

    public TimePickerDialog buildTimePicker(Activity activity) {
        return new TimePickerDialog(activity, this, AodUtils.getHour(this.mStartTime), AodUtils.getMin(this.mStartTime), this.mFormatter.is24HourFormat());
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
        return Settings.Secure.getInt(this.mContext.getContentResolver(), AodScheduleSwitchPreferenceController.KEY_AOD_DISPLAY_MODE, 1) == 1;
    }
}
