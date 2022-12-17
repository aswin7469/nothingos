package com.android.settings.system;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.UserManager;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class FactoryResetPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private int mLowBatteryWarningLevel = 15;
    private final UserManager mUm;

    public String getPreferenceKey() {
        return "factory_reset";
    }

    public FactoryResetPreferenceController(Context context) {
        super(context);
        this.mUm = (UserManager) context.getSystemService("user");
    }

    public boolean isAvailable() {
        return this.mUm.isAdminUser() || Utils.isDemoUser(this.mContext);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        BatteryManager batteryManager;
        if (!"factory_reset".equals(preference.getKey()) || (batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager")) == null) {
            return false;
        }
        if (batteryManager.getIntProperty(4) >= this.mLowBatteryWarningLevel) {
            this.mContext.startActivity(new Intent(this.mContext, Settings.FactoryResetActivity.class));
        } else {
            AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(R$string.nt_factory_reset_battery_low_title).setMessage((CharSequence) this.mContext.getString(R$string.nt_factory_reset_battery_low_message, new Object[]{Integer.valueOf(this.mLowBatteryWarningLevel)})).setCancelable(true).setPositiveButton(R$string.nt_factory_reset_battery_low_done, (DialogInterface.OnClickListener) null).create();
            create.requestWindowFeature(1);
            create.setCanceledOnTouchOutside(false);
            create.show();
        }
        return true;
    }
}
