package com.nt.settings.fuelgauge;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
/* loaded from: classes2.dex */
public class NtAllowBatterySharePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static SwitchPreference mSwitchPreference;
    private final Handler mHandler;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "nt_allow_battery_share";
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public NtAllowBatterySharePreferenceController(Context context) {
        super(context);
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("nt_wireless_reverse_charge"), false, new ContentObserver(handler) { // from class: com.nt.settings.fuelgauge.NtAllowBatterySharePreferenceController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                Log.d("NtAllowBatteryShare", "@_@ onChange ------------ reverse charge status changed");
                NtAllowBatterySharePreferenceController.this.updateState(NtAllowBatterySharePreferenceController.mSwitchPreference);
            }
        }, -1);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        mSwitchPreference = switchPreference;
        if (switchPreference == null) {
            Log.w("NtAllowBatteryShare", "@_@ displayPreference ------------ switch preference is null");
        } else if (!DEBUG) {
        } else {
            Log.d("NtAllowBatteryShare", "@_@ displayPreference ------------ init switch preference ok");
        }
    }

    public void setSwitchChecked(boolean z) {
        SwitchPreference switchPreference = mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setChecked(z);
        } else {
            Log.w("NtAllowBatteryShare", "@_@ setSwitchChecked ------------ switch preference is null");
        }
    }

    public void setSwitchEnabled(boolean z) {
        SwitchPreference switchPreference = mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setEnabled(z);
        } else {
            Log.w("NtAllowBatteryShare", "@_@ setSwitchEnabled ------------ switch preference is null");
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "nt_wireless_reverse_charge", 0);
        if (DEBUG) {
            Log.d("NtAllowBatteryShare", "@_@ updateState ------------ value: " + i);
        }
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (i != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (DEBUG) {
            Log.d("NtAllowBatteryShare", "@_@ onPreferenceChange ------------ value: " + booleanValue);
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_wireless_reverse_charge", booleanValue ? 1 : 0);
        return true;
    }
}
