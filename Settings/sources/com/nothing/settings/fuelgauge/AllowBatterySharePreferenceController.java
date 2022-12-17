package com.nothing.settings.fuelgauge;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class AllowBatterySharePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private static final boolean DEBUG = Log.isLoggable("PrefControllerMixin", 3);
    /* access modifiers changed from: private */
    public static SwitchPreference mSwitchPreference;
    private final Handler mHandler;

    public String getPreferenceKey() {
        return "nt_allow_battery_share";
    }

    public boolean isAvailable() {
        return true;
    }

    public AllowBatterySharePreferenceController(Context context) {
        super(context);
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("nt_wireless_reverse_charge"), false, new ContentObserver(handler) {
            public void onChange(boolean z) {
                AllowBatterySharePreferenceController.this.updateState(AllowBatterySharePreferenceController.mSwitchPreference);
            }
        }, -1);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        mSwitchPreference = switchPreference;
        if (switchPreference == null) {
            Log.d("AllowBatteryShare", "displayPreference switch preference is null");
        } else if (DEBUG) {
            Log.d("AllowBatteryShare", "displayPreference init switch preference ok");
        }
        mSwitchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Settings.Global.putInt(AllowBatterySharePreferenceController.this.mContext.getContentResolver(), "nt_wireless_reverse_charge_user", ((SwitchPreference) preference).isChecked() ? 1 : 0);
                return false;
            }
        });
    }

    public void setSwitchChecked(boolean z) {
        SwitchPreference switchPreference = mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setChecked(z);
        } else {
            Log.d("AllowBatteryShare", "setSwitchChecked switch preference is null");
        }
    }

    public void setSwitchEnabled(boolean z) {
        SwitchPreference switchPreference = mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setEnabled(z);
        } else {
            Log.w("AllowBatteryShare", "setSwitchEnabled switch preference is null");
        }
    }

    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "nt_wireless_reverse_charge", 0);
        if (DEBUG) {
            Log.d("AllowBatteryShare", "updateState value: " + i);
        }
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (i != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (DEBUG) {
            Log.d("AllowBatteryShare", "onPreferenceChange value: " + booleanValue);
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_wireless_reverse_charge", booleanValue ? 1 : 0);
        return true;
    }
}
