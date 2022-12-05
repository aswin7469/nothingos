package com.android.settings.system;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.UserManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
/* loaded from: classes.dex */
public class FactoryResetPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private final UserManager mUm;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "factory_reset";
    }

    public FactoryResetPreferenceController(Context context) {
        super(context);
        this.mUm = (UserManager) context.getSystemService("user");
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return this.mUm.isAdminUser() || Utils.isDemoUser(this.mContext);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        BatteryManager batteryManager;
        if (!"factory_reset".equals(preference.getKey()) || (batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager")) == null) {
            return false;
        }
        int intProperty = batteryManager.getIntProperty(4);
        if (DEBUG) {
            Log.d("NtBatteryShare", "@_@ onPreferenceChange ------------ currentBattery: " + intProperty);
        }
        if (intProperty >= 15) {
            this.mContext.startActivity(new Intent(this.mContext, Settings.FactoryResetActivity.class));
        } else {
            AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(R.string.nt_factory_reset_battery_low_title).setMessage(this.mContext.getString(R.string.nt_factory_reset_battery_low_message, 15)).setCancelable(true).setPositiveButton(R.string.nt_factory_reset_battery_low_done, (DialogInterface.OnClickListener) null).create();
            create.requestWindowFeature(1);
            create.setCanceledOnTouchOutside(false);
            create.show();
        }
        return true;
    }
}
