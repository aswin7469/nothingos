package com.android.settings.applications.specialaccess.turnscreenon;

import android.app.AppOpsManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.applications.AppInfoWithHeader;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class TurnScreenOnDetails extends AppInfoWithHeader implements Preference.OnPreferenceChangeListener {
    private AppOpsManager mAppOpsManager;
    private NtCustSwitchPreference mSwitchPref;

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getMetricsCategory() {
        return 1922;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAppOpsManager = (AppOpsManager) getSystemService(AppOpsManager.class);
        addPreferencesFromResource(R$xml.turn_screen_on_permissions_details);
        NtCustSwitchPreference ntCustSwitchPreference = (NtCustSwitchPreference) findPreference("app_ops_settings_switch");
        this.mSwitchPref = ntCustSwitchPreference;
        ntCustSwitchPreference.setTitle(R$string.allow_turn_screen_on);
        this.mSwitchPref.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference != this.mSwitchPref) {
            return false;
        }
        setTurnScreenOnAppOp(this.mPackageInfo.applicationInfo.uid, this.mPackageName, ((Boolean) obj).booleanValue());
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        this.mSwitchPref.setChecked(isTurnScreenOnAllowed(this.mAppOpsManager, this.mPackageInfo.applicationInfo.uid, this.mPackageName));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setTurnScreenOnAppOp(int i, String str, boolean z) {
        this.mAppOpsManager.setMode(61, i, str, z ? 0 : 2);
    }

    static boolean isTurnScreenOnAllowed(AppOpsManager appOpsManager, int i, String str) {
        return appOpsManager.checkOpNoThrow(61, i, str) == 0;
    }

    public static int getPreferenceSummary(AppOpsManager appOpsManager, int i, String str) {
        if (isTurnScreenOnAllowed(appOpsManager, i, str)) {
            return R$string.app_permission_summary_allowed;
        }
        return R$string.app_permission_summary_not_allowed;
    }
}
