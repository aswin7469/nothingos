package com.android.settingslib.development;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.ConfirmationDialogController;

public abstract class AbstractEnableAdbPreferenceController extends DeveloperOptionsPreferenceController implements ConfirmationDialogController {
    public static final String ACTION_ENABLE_ADB_STATE_CHANGED = "com.android.settingslib.development.AbstractEnableAdbController.ENABLE_ADB_STATE_CHANGED";
    public static final int ADB_SETTING_OFF = 0;
    public static final int ADB_SETTING_ON = 1;
    private static final String KEY_ENABLE_ADB = "enable_adb";
    protected RestrictedSwitchPreference mPreference;

    public String getPreferenceKey() {
        return KEY_ENABLE_ADB;
    }

    public AbstractEnableAdbPreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            this.mPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(KEY_ENABLE_ADB);
        }
    }

    public boolean isAvailable() {
        return ((UserManager) this.mContext.getSystemService(UserManager.class)).isAdminUser();
    }

    private boolean isAdbEnabled() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "adb_enabled", 0) != 0) {
            return true;
        }
        return false;
    }

    public void updateState(Preference preference) {
        ((TwoStatePreference) preference).setChecked(isAdbEnabled());
        if (isAvailable()) {
            ((RestrictedSwitchPreference) preference).setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(this.mContext, UserHandle.myUserId()));
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchEnabled() {
        super.onDeveloperOptionsSwitchEnabled();
        if (isAvailable()) {
            this.mPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(this.mContext, UserHandle.myUserId()));
        }
    }

    public void enablePreference(boolean z) {
        if (isAvailable()) {
            this.mPreference.setEnabled(z);
        }
    }

    /* JADX WARNING: type inference failed for: r0v3, types: [com.android.settingslib.RestrictedSwitchPreference, androidx.preference.Preference] */
    public void resetPreference() {
        if (this.mPreference.isChecked()) {
            this.mPreference.setChecked(false);
            handlePreferenceTreeClick(this.mPreference);
        }
    }

    public boolean haveDebugSettings() {
        return isAdbEnabled();
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (isUserAMonkey() || !TextUtils.equals(KEY_ENABLE_ADB, preference.getKey())) {
            return false;
        }
        if (!isAdbEnabled()) {
            showConfirmationDialog(preference);
            return true;
        }
        writeAdbSetting(false);
        return true;
    }

    /* access modifiers changed from: protected */
    public void writeAdbSetting(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "adb_enabled", z ? 1 : 0);
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent(ACTION_ENABLE_ADB_STATE_CHANGED));
    }

    /* access modifiers changed from: package-private */
    public boolean isUserAMonkey() {
        return ActivityManager.isUserAMonkey();
    }
}
