package com.android.settingslib.deviceinfo;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.AbstractPreferenceController;

public class AbstractSerialNumberPreferenceController extends AbstractPreferenceController {
    static final String KEY_SERIAL_NUMBER = "serial_number";
    private final String mSerialNumber;

    public String getPreferenceKey() {
        return "serial_number";
    }

    public AbstractSerialNumberPreferenceController(Context context) {
        this(context, Build.getSerial());
    }

    AbstractSerialNumberPreferenceController(Context context, String str) {
        super(context);
        this.mSerialNumber = str;
    }

    public boolean isAvailable() {
        return !TextUtils.isEmpty(this.mSerialNumber);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference("serial_number");
        if (findPreference != null) {
            findPreference.setSummary((CharSequence) this.mSerialNumber);
        }
    }
}
