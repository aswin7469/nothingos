package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;
import com.android.settings.core.BasePreferenceController;

public class DeviceSoftwareinfoController extends BasePreferenceController {
    public static final String KEY = "nt_nothing_os_version";
    private static final String KEY_OS_NAME = "ro.build.nothing.version";
    private static final String KEY_OS_VERSION = "ro.nothing.version.id";
    private static final String TAG = "DeviceSoftwareinfoController";
    private Preference mPreference;
    private final UserManager mUm;

    public int getAvailabilityStatus() {
        return 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DeviceSoftwareinfoController(Context context, String str) {
        super(context, str);
        this.mUm = UserManager.get(context);
    }

    public CharSequence getSummary() {
        String str = SystemProperties.get(KEY_OS_VERSION);
        return !TextUtils.isEmpty(str) ? str : "1.0.0";
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(KEY);
    }

    private boolean isClickable() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_system_update_settings) && this.mUm.isAdminUser();
    }

    public void updateState(Preference preference) {
        Preference preference2 = this.mPreference;
        if (preference2 != null) {
            preference2.setTitle((CharSequence) SystemProperties.get(KEY_OS_NAME));
            this.mPreference.setSummary(getSummary());
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        PersistableBundle config;
        if (!isClickable()) {
            return true;
        }
        if (TextUtils.equals(getPreferenceKey(), preference.getKey()) && (config = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfig()) != null && config.getBoolean("ci_action_on_sys_update_bool")) {
            startOtaPage(config);
        }
        return false;
    }

    private void startOtaPage(PersistableBundle persistableBundle) {
        String string = persistableBundle.getString("ci_action_on_sys_update_intent_string");
        if (!TextUtils.isEmpty(string)) {
            String string2 = persistableBundle.getString("ci_action_on_sys_update_extra_string");
            String string3 = persistableBundle.getString("ci_action_on_sys_update_extra_val_string");
            Intent intent = new Intent(string);
            if (!TextUtils.isEmpty(string2)) {
                intent.putExtra(string2, string3);
            }
            intent.addFlags(268435456);
            this.mContext.getApplicationContext().sendBroadcast(intent);
        }
    }
}
