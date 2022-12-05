package com.nt.settings.about;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class NtAboutPhoneNothingOsPreferenceController extends BasePreferenceController {
    public static final String KEY = "nt_nothing_os_version";
    private static final String TAG = "NtAboutPhoneNothingOS";
    private Preference mPreference;
    private final UserManager mUm;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 1;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtAboutPhoneNothingOsPreferenceController(Context context, String str) {
        super(context, str);
        this.mUm = UserManager.get(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        String str = SystemProperties.get("ro.nothing.version.id");
        return !TextUtils.isEmpty(str) ? str : "1.0.0";
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(KEY);
    }

    private boolean isClickable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_system_update_settings) && this.mUm.isAdminUser();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        Preference preference2 = this.mPreference;
        if (preference2 == null) {
            return;
        }
        preference2.setTitle("Nothing OS (1)");
        this.mPreference.setSummary(mo485getSummary());
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        PersistableBundle config;
        Log.d(TAG, "@_@ ------ handlePreferenceTreeClick [" + getPreferenceKey() + "," + preference.getKey() + "]");
        if (!isClickable()) {
            return true;
        }
        if (!TextUtils.equals(getPreferenceKey(), preference.getKey()) || (config = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfig()) == null || !config.getBoolean("ci_action_on_sys_update_bool")) {
            return false;
        }
        ciActionOnSysUpdate(config);
        return false;
    }

    private void ciActionOnSysUpdate(PersistableBundle persistableBundle) {
        String string = persistableBundle.getString("ci_action_on_sys_update_intent_string");
        Log.d(TAG, "@_@ ------ ciActionOnSysUpdate intentStr: [" + string + "]");
        if (!TextUtils.isEmpty(string)) {
            String string2 = persistableBundle.getString("ci_action_on_sys_update_extra_string");
            String string3 = persistableBundle.getString("ci_action_on_sys_update_extra_val_string");
            Intent intent = new Intent(string);
            if (!TextUtils.isEmpty(string2)) {
                intent.putExtra(string2, string3);
            }
            Log.d(TAG, "ciActionOnSysUpdate: broadcasting intent " + string + " with extra " + string2 + ", " + string3);
            intent.addFlags(16777216);
            this.mContext.getApplicationContext().sendBroadcast(intent);
        }
    }
}
