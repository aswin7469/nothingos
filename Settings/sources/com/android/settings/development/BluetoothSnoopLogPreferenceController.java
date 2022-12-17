package com.android.settings.development;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import java.util.Arrays;
import java.util.List;

public class BluetoothSnoopLogPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY = "persist.bluetooth.btsnooplogmode";
    static final int BTSNOOP_LOG_MODE_DISABLED_INDEX = 0;
    static final int BTSNOOP_LOG_MODE_FILTERED_INDEX = 1;
    static final int BTSNOOP_LOG_MODE_FULL_INDEX = 2;
    static final int BTSNOOP_LOG_MODE_MEDIAPKTSFILTERED_INDEX = 4;
    static final int BTSNOOP_LOG_MODE_SNOOPHEADERSFILTERED_INDEX = 3;
    private final String emptyVal = null;
    private final List<String> mListEnhancedValues;
    private final String[] mListEntries;
    private final String[] mListValues;

    public String getPreferenceKey() {
        return "bt_hci_snoop_log";
    }

    public BluetoothSnoopLogPreferenceController(Context context) {
        super(context);
        this.mListValues = context.getResources().getStringArray(R$array.bt_hci_snoop_log_values);
        this.mListEntries = context.getResources().getStringArray(R$array.bt_hci_snoop_log_entries);
        this.mListEnhancedValues = Arrays.asList(context.getResources().getStringArray(R$array.bt_hci_snoop_log_values_enhanced));
    }

    public int getDefaultModeIndex() {
        if (!Build.IS_DEBUGGABLE) {
            return 0;
        }
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "bluetooth_btsnoop_default_mode");
        int i = 0;
        while (true) {
            String[] strArr = this.mListValues;
            if (i >= strArr.length) {
                return 0;
            }
            if (TextUtils.equals(string, strArr[i])) {
                return i;
            }
            i++;
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String obj2 = obj.toString();
        if (this.mListEnhancedValues.contains(obj2)) {
            SystemProperties.set("persist.vendor.service.bt.adv_snoop", obj2);
            SystemProperties.set(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY, this.emptyVal);
        } else {
            SystemProperties.set(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY, obj2);
            SystemProperties.set("persist.vendor.service.bt.adv_snoop", this.emptyVal);
        }
        updateState(this.mPreference);
        return true;
    }

    public void updateState(Preference preference) {
        ListPreference listPreference = (ListPreference) preference;
        String str = SystemProperties.get(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY);
        String str2 = SystemProperties.get("persist.vendor.service.bt.adv_snoop");
        if (TextUtils.isEmpty(str)) {
            str = str2;
        }
        int defaultModeIndex = getDefaultModeIndex();
        int i = 0;
        while (true) {
            String[] strArr = this.mListValues;
            if (i >= strArr.length) {
                break;
            } else if (TextUtils.equals(str, strArr[i])) {
                defaultModeIndex = i;
                break;
            } else {
                i++;
            }
        }
        String[] strArr2 = this.mListValues;
        if (defaultModeIndex >= strArr2.length || defaultModeIndex >= this.mListEntries.length) {
            Log.e("PrefControllerMixin", "missing some entries in xml file\t some options in developer options will not be shown until added in xml file");
            return;
        }
        listPreference.setValue(strArr2[defaultModeIndex]);
        listPreference.setSummary(this.mListEntries[defaultModeIndex]);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        SystemProperties.set(BLUETOOTH_BTSNOOP_LOG_MODE_PROPERTY, (String) null);
        SystemProperties.set("persist.vendor.service.bt.adv_snoop", (String) null);
        ((ListPreference) this.mPreference).setValue(this.mListValues[getDefaultModeIndex()]);
        ((ListPreference) this.mPreference).setSummary(this.mListEntries[getDefaultModeIndex()]);
    }
}
