package com.nothing.settings.display.aod;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TimePicker;
import androidx.preference.Preference;
import com.android.settings.display.darkmode.TimeFormatter;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import java.time.LocalTime;

public class AodEndTimePreferenceController extends AbstractPreferenceController implements TimePickerDialog.OnTimeSetListener {
    private Context mContext;
    private String mEndTime;
    private TimeFormatter mFormatter;
    private final EndTimePreferenceHost mHost;

    public interface EndTimePreferenceHost extends UpdateDisplayModeCallback {
        void showEndTimePicker();
    }

    public String getPreferenceKey() {
        return "nt_aod_end_time";
    }

    public boolean isAvailable() {
        return true;
    }

    public AodEndTimePreferenceController(Context context, EndTimePreferenceHost endTimePreferenceHost) {
        super(context);
        this.mHost = endTimePreferenceHost;
        this.mContext = context;
        this.mFormatter = new TimeFormatter(context);
        String string = Settings.Secure.getString(context.getContentResolver(), "nt_aod_end_time");
        this.mEndTime = string;
        if (TextUtils.isEmpty(string)) {
            this.mEndTime = "2300";
        }
    }

    public void updateState(Preference preference) {
        if (preference instanceof RestrictedPreference) {
            preference.setSummary((CharSequence) this.mFormatter.mo12819of(LocalTime.of(AodUtils.getHour(this.mEndTime), AodUtils.getMin(this.mEndTime))));
            if (!((RestrictedPreference) preference).isDisabledByAdmin()) {
                preference.setVisible(isShow());
            }
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals("nt_aod_end_time", preference.getKey())) {
            return false;
        }
        this.mHost.showEndTimePicker();
        return true;
    }

    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.mEndTime = buildTimeString(i, i2);
        Context context = this.mContext;
        if (context != null) {
            Settings.Secure.putString(context.getContentResolver(), "nt_aod_end_time", this.mEndTime);
            this.mHost.updateDisplayMode(this.mContext);
        }
    }

    public TimePickerDialog buildTimePicker(Activity activity) {
        return new TimePickerDialog(activity, this, AodUtils.getHour(this.mEndTime), AodUtils.getMin(this.mEndTime), this.mFormatter.is24HourFormat());
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
