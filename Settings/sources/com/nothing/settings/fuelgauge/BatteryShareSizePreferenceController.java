package com.nothing.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TetheredClient;
import android.net.TetheringManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.Collection;
import java.util.Iterator;

public class BatteryShareSizePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, TetheringManager.TetheringEventCallback, Preference.OnPreferenceChangeListener {
    private BroadcastReceiver chargingReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("plugged", -1);
            Log.d("BatteryShareSizePreferenceController", "charge type: " + intExtra);
            BatteryShareSizePreferenceController.this.mIsInWirelessCharge = intExtra == 4;
            BatteryShareSizePreferenceController.this.checkUpdateBatteryValueDisabled(Settings.Global.getInt(BatteryShareSizePreferenceController.this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", 15));
        }
    };
    private AllowBatterySharePreferenceController mAllowBatteryController;
    SeekBarPreference mBatteryShareSizeProgressBarPref;
    private boolean mBatteryValueDisabled;
    /* access modifiers changed from: private */
    public Context mContext;
    private boolean mHasTetheringClient;
    /* access modifiers changed from: private */
    public boolean mIsInWirelessCharge;
    private TetheringManager mTetheringManager;

    public String getPreferenceKey() {
        return "nt_battery_share_size";
    }

    public boolean isAvailable() {
        return true;
    }

    public BatteryShareSizePreferenceController(Context context) {
        super(context);
        this.mContext = context;
        this.mAllowBatteryController = new AllowBatterySharePreferenceController(context);
        this.mContext.registerReceiver(this.chargingReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService("tethering");
        this.mTetheringManager = tetheringManager;
        tetheringManager.registerTetheringEventCallback(new HandlerExecutor(new Handler()), this);
    }

    public void updateState(Preference preference) {
        SeekBarPreference seekBarPreference = (SeekBarPreference) preference;
        this.mBatteryShareSizeProgressBarPref = seekBarPreference;
        seekBarPreference.setContinuousUpdates(true);
        this.mBatteryShareSizeProgressBarPref.setMax(10);
        this.mBatteryShareSizeProgressBarPref.setMin(2);
        this.mBatteryShareSizeProgressBarPref.setOnPreferenceChangeListener(this);
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", 15);
        this.mBatteryShareSizeProgressBarPref.setProgress(i / 5);
        checkUpdateBatteryValueDisabled(i);
        updateTitle(i);
        Log.d("BatteryShareSizePreferenceController", "updateState value: " + i);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int intValue = ((Integer) obj).intValue() * 5;
        checkUpdateBatteryValueDisabled(intValue);
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", intValue);
        updateTitle(intValue);
        return true;
    }

    private void updateTitle(int i) {
        SeekBarPreference seekBarPreference = this.mBatteryShareSizeProgressBarPref;
        seekBarPreference.setTitle((CharSequence) this.mContext.getString(R$string.nt_stop_sharing_battery_slider_title) + " " + i + "%");
    }

    public void onClientsChanged(Collection<TetheredClient> collection) {
        boolean z;
        Iterator<TetheredClient> it = collection.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().getTetheringType() == 5) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        this.mHasTetheringClient = z;
        updateSwitchState();
    }

    private void updateSwitchState() {
        Log.d("BatteryShareSizePreferenceController", "updateSwitchState: mIsInWirelessCharge = " + this.mIsInWirelessCharge + ", mHasTetheringClient = " + this.mHasTetheringClient + ", mBatteryValueDisabled = " + this.mBatteryValueDisabled);
        if (this.mIsInWirelessCharge || this.mHasTetheringClient || this.mBatteryValueDisabled) {
            this.mAllowBatteryController.setSwitchChecked(false);
            this.mAllowBatteryController.setSwitchEnabled(false);
            return;
        }
        this.mAllowBatteryController.setSwitchEnabled(true);
    }

    public void checkUpdateBatteryValueDisabled(int i) {
        BatteryManager batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager");
        if (batteryManager != null) {
            int intProperty = batteryManager.getIntProperty(4);
            Log.d("BatteryShareSizePreferenceController", "checkUpdateBatteryValueDisabled value: " + i + ", currentBatteryLevel: " + intProperty);
            this.mBatteryValueDisabled = i > intProperty;
            updateSwitchState();
        }
    }

    public void releaseChargingReceiver() {
        try {
            this.mContext.unregisterReceiver(this.chargingReceiver);
            this.mTetheringManager.unregisterTetheringEventCallback(this);
        } catch (Exception unused) {
        }
    }
}
