package com.nt.settings.fuelgauge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TetheredClient;
import android.net.TetheringManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class NtBatteryShareSizePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, TetheringManager.TetheringEventCallback, Preference.OnPreferenceChangeListener {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private BroadcastReceiver chargingReceiver = new BroadcastReceiver() { // from class: com.nt.settings.fuelgauge.NtBatteryShareSizePreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("plugged", -1);
            if (NtBatteryShareSizePreferenceController.DEBUG) {
                Log.d("NtBatteryShare", "@_@ ------------ charge type: " + intExtra);
            }
            NtBatteryShareSizePreferenceController.this.mIsInWirelessCharge = intExtra == 4;
            NtBatteryShareSizePreferenceController.this.updateSwitchState();
        }
    };
    SeekBarPreference mBatteryShareSizeProgressBarPref;
    private boolean mBatteryValueDisabled;
    private Context mContext;
    private boolean mHasTetheringClient;
    private boolean mIsInWirelessCharge;
    private NtAllowBatterySharePreferenceController mNtAllowBatteryController;
    private TetheringManager mTm;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "nt_battery_share_size";
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public NtBatteryShareSizePreferenceController(Context context) {
        super(context);
        this.mContext = context;
        this.mNtAllowBatteryController = new NtAllowBatterySharePreferenceController(context);
        this.mContext.registerReceiver(this.chargingReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService("tethering");
        this.mTm = tetheringManager;
        tetheringManager.registerTetheringEventCallback(new HandlerExecutor(new Handler()), this);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        SeekBarPreference seekBarPreference = (SeekBarPreference) preference;
        this.mBatteryShareSizeProgressBarPref = seekBarPreference;
        seekBarPreference.setProgressBarConfig(10, 2);
        this.mBatteryShareSizeProgressBarPref.setOnPreferenceChangeListener(this);
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", 15);
        this.mBatteryShareSizeProgressBarPref.setProgress(i / 5);
        checkUpdateBatteryValueDisabled(i);
        updateTitle(i);
        if (DEBUG) {
            Log.d("NtBatteryShare", "@_@ updateState ------------ value: " + i);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        int intValue = ((Integer) obj).intValue() * 5;
        checkUpdateBatteryValueDisabled(intValue);
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", intValue);
        updateTitle(intValue);
        return true;
    }

    private void updateTitle(int i) {
        SeekBarPreference seekBarPreference = this.mBatteryShareSizeProgressBarPref;
        seekBarPreference.setTitle(this.mContext.getString(R.string.nt_stop_sharing_battery_slider_title) + " " + i + "%");
    }

    public void onClientsChanged(Collection<TetheredClient> collection) {
        boolean z;
        Iterator<TetheredClient> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().getTetheringType() == 5) {
                z = true;
                break;
            }
        }
        this.mHasTetheringClient = z;
        updateSwitchState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSwitchState() {
        Log.d("NtBatteryShare", "updateSwitchState: mIsInWirelessCharge = " + this.mIsInWirelessCharge + ", mHasTetheringClient = " + this.mHasTetheringClient + ", mBatteryValueDisabled = " + this.mBatteryValueDisabled);
        if (this.mIsInWirelessCharge || this.mHasTetheringClient || this.mBatteryValueDisabled) {
            this.mNtAllowBatteryController.setSwitchChecked(false);
            this.mNtAllowBatteryController.setSwitchEnabled(false);
            return;
        }
        this.mNtAllowBatteryController.setSwitchEnabled(true);
    }

    private void checkUpdateBatteryValueDisabled(int i) {
        BatteryManager batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager");
        if (batteryManager != null) {
            int intProperty = batteryManager.getIntProperty(4);
            if (DEBUG) {
                Log.d("NtBatteryShare", "checkUpdateBatteryValueDisabled ------------ value: " + i + ", currentBattery: " + intProperty);
            }
            this.mBatteryValueDisabled = i > intProperty;
            updateSwitchState();
        }
    }

    public void releaseChargingReceiver() {
        try {
            this.mContext.unregisterReceiver(this.chargingReceiver);
            this.mTm.unregisterTetheringEventCallback(this);
        } catch (Exception unused) {
        }
    }
}
