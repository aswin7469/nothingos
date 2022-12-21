package com.android.settingslib.development;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.C1757R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class AbstractLogdSizePreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener {
    public static final String ACTION_LOGD_SIZE_UPDATED = "com.android.settingslib.development.AbstractLogdSizePreferenceController.LOGD_SIZE_UPDATED";
    static final String DEFAULT_SNET_TAG = "I";
    public static final String EXTRA_CURRENT_LOGD_VALUE = "CURRENT_LOGD_VALUE";
    static final String LOW_RAM_CONFIG_PROPERTY_KEY = "ro.config.low_ram";
    private static final String SELECT_LOGD_DEFAULT_SIZE_PROPERTY = "ro.logd.size";
    static final String SELECT_LOGD_DEFAULT_SIZE_VALUE = "262144";
    static final String SELECT_LOGD_MINIMUM_SIZE_VALUE = "65536";
    static final String SELECT_LOGD_OFF_SIZE_MARKER_VALUE = "32768";
    private static final String SELECT_LOGD_RUNTIME_SNET_TAG_PROPERTY = "log.tag.snet_event_log";
    private static final String SELECT_LOGD_SIZE_KEY = "select_logd_size";
    static final String SELECT_LOGD_SIZE_PROPERTY = "persist.logd.size";
    static final String SELECT_LOGD_SNET_TAG_PROPERTY = "persist.log.tag.snet_event_log";
    private static final String SELECT_LOGD_SVELTE_DEFAULT_SIZE_VALUE = "65536";
    static final String SELECT_LOGD_TAG_PROPERTY = "persist.log.tag";
    static final String SELECT_LOGD_TAG_SILENCE = "Settings";
    private ListPreference mLogdSize;

    public String getPreferenceKey() {
        return SELECT_LOGD_SIZE_KEY;
    }

    public AbstractLogdSizePreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            this.mLogdSize = (ListPreference) preferenceScreen.findPreference(SELECT_LOGD_SIZE_KEY);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference != this.mLogdSize) {
            return false;
        }
        writeLogdSizeOption(obj);
        return true;
    }

    public void enablePreference(boolean z) {
        if (isAvailable()) {
            this.mLogdSize.setEnabled(z);
        }
    }

    private String defaultLogdSizeValue() {
        String str = SystemProperties.get(SELECT_LOGD_DEFAULT_SIZE_PROPERTY);
        if (str == null || str.length() == 0) {
            return SystemProperties.get(LOW_RAM_CONFIG_PROPERTY_KEY).equals("true") ? "65536" : SELECT_LOGD_DEFAULT_SIZE_VALUE;
        }
        return str;
    }

    public void updateLogdSizeValues() {
        int i;
        if (this.mLogdSize != null) {
            String str = SystemProperties.get(SELECT_LOGD_TAG_PROPERTY);
            String str2 = SystemProperties.get(SELECT_LOGD_SIZE_PROPERTY);
            if (str != null && str.startsWith(SELECT_LOGD_TAG_SILENCE)) {
                str2 = SELECT_LOGD_OFF_SIZE_MARKER_VALUE;
            }
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcastSync(new Intent(ACTION_LOGD_SIZE_UPDATED).putExtra(EXTRA_CURRENT_LOGD_VALUE, str2));
            if (str2 == null || str2.length() == 0) {
                str2 = defaultLogdSizeValue();
            }
            String[] stringArray = this.mContext.getResources().getStringArray(C1757R.array.select_logd_size_values);
            String[] stringArray2 = this.mContext.getResources().getStringArray(C1757R.array.select_logd_size_titles);
            if (SystemProperties.get(LOW_RAM_CONFIG_PROPERTY_KEY).equals("true")) {
                this.mLogdSize.setEntries(C1757R.array.select_logd_size_lowram_titles);
                stringArray2 = this.mContext.getResources().getStringArray(C1757R.array.select_logd_size_lowram_titles);
                i = 1;
            } else {
                i = 2;
            }
            String[] stringArray3 = this.mContext.getResources().getStringArray(C1757R.array.select_logd_size_summaries);
            int i2 = 0;
            while (true) {
                if (i2 >= stringArray2.length) {
                    break;
                } else if (str2.equals(stringArray[i2]) || str2.equals(stringArray2[i2])) {
                    i = i2;
                } else {
                    i2++;
                }
            }
            this.mLogdSize.setValue(stringArray[i]);
            this.mLogdSize.setSummary(stringArray3[i]);
        }
    }

    public void writeLogdSizeOption(Object obj) {
        String str;
        boolean z = obj != null && obj.toString().equals(SELECT_LOGD_OFF_SIZE_MARKER_VALUE);
        String str2 = SystemProperties.get(SELECT_LOGD_TAG_PROPERTY);
        String str3 = "";
        if (str2 == null) {
            str2 = str3;
        }
        String replaceFirst = str2.replaceAll(",+Settings", str3).replaceFirst("^Settings,*", str3).replaceAll(",+", NavigationBarInflaterView.BUTTON_SEPARATOR).replaceFirst(",+$", str3);
        if (z) {
            String str4 = SystemProperties.get(SELECT_LOGD_SNET_TAG_PROPERTY);
            if ((str4 == null || str4.length() == 0) && ((str = SystemProperties.get(SELECT_LOGD_RUNTIME_SNET_TAG_PROPERTY)) == null || str.length() == 0)) {
                SystemProperties.set(SELECT_LOGD_SNET_TAG_PROPERTY, DEFAULT_SNET_TAG);
            }
            if (replaceFirst.length() != 0) {
                replaceFirst = NavigationBarInflaterView.BUTTON_SEPARATOR + replaceFirst;
            }
            replaceFirst = SELECT_LOGD_TAG_SILENCE + replaceFirst;
            obj = "65536";
        }
        if (!replaceFirst.equals(str2)) {
            SystemProperties.set(SELECT_LOGD_TAG_PROPERTY, replaceFirst);
        }
        String defaultLogdSizeValue = defaultLogdSizeValue();
        String obj2 = (obj == null || obj.toString().length() == 0) ? defaultLogdSizeValue : obj.toString();
        if (!defaultLogdSizeValue.equals(obj2)) {
            str3 = obj2;
        }
        SystemProperties.set(SELECT_LOGD_SIZE_PROPERTY, str3);
        SystemProperties.set("ctl.start", "logd-reinit");
        SystemPropPoker.getInstance().poke();
        updateLogdSizeValues();
    }
}
