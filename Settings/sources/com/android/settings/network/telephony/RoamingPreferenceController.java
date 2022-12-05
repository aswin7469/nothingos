package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.network.GlobalSettingsChangeListener;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public class RoamingPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String DIALOG_TAG = "MobileDataDialog";
    private static final String TAG = "RoamingController";
    private CarrierConfigManager mCarrierConfigManager;
    FragmentManager mFragmentManager;
    private GlobalSettingsChangeListener mListener;
    private GlobalSettingsChangeListener mListenerForSubId;
    private RestrictedSwitchPreference mSwitchPreference;
    private TelephonyManager mTelephonyManager;

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        return i != -1 ? 0 : 1;
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RoamingPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        if (this.mListener == null) {
            this.mListener = new GlobalSettingsChangeListener(this.mContext, "data_roaming") { // from class: com.android.settings.network.telephony.RoamingPreferenceController.1
                @Override // com.android.settings.network.GlobalSettingsChangeListener
                public void onChanged(String str) {
                    RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                    roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
                }
            };
        }
        stopMonitorSubIdSpecific();
        if (this.mSubId == -1) {
            return;
        }
        Context context = this.mContext;
        this.mListenerForSubId = new GlobalSettingsChangeListener(context, "data_roaming" + this.mSubId) { // from class: com.android.settings.network.telephony.RoamingPreferenceController.2
            @Override // com.android.settings.network.GlobalSettingsChangeListener
            public void onChanged(String str) {
                RoamingPreferenceController.this.stopMonitor();
                RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
            }
        };
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        stopMonitor();
        stopMonitorSubIdSpecific();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(final boolean z) {
        if (z) {
            showRoamingDialog();
            return true;
        }
        new Thread(new Runnable() { // from class: com.android.settings.network.telephony.RoamingPreferenceController.3
            @Override // java.lang.Runnable
            public void run() {
                RoamingPreferenceController.this.mTelephonyManager.setDataRoamingEnabled(z);
            }
        }).start();
        return true;
    }

    private void showRoamingDialog() {
        Log.d(TAG, "showRoamingDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        int i = R.string.roaming_alert_title;
        int i2 = R.string.roaming_warning;
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
        if (configForSubId != null && configForSubId.getBoolean("check_pricing_with_carrier_data_roaming_bool")) {
            i2 = R.string.roaming_check_price_warning;
        }
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.network.telephony.RoamingPreferenceController.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                if (i3 == -1) {
                    RoamingPreferenceController.this.mTelephonyManager.setDataRoamingEnabled(true);
                    RoamingPreferenceController.this.mSwitchPreference.setChecked(true);
                    return;
                }
                RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
            }
        };
        builder.setMessage(this.mContext.getResources().getString(i2)).setTitle(i).setIconAttribute(16843605).setNeutralButton(this.mContext.getResources().getString(R.string.no), onClickListener).setPositiveButton(this.mContext.getResources().getString(R.string.yes), onClickListener).create().show();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
        if (!restrictedSwitchPreference.isDisabledByAdmin()) {
            restrictedSwitchPreference.setEnabled(this.mSubId != -1);
            if (isChecked()) {
                Log.d(TAG, "updateState check");
                restrictedSwitchPreference.setChecked(true);
                return;
            }
            restrictedSwitchPreference.setChecked(false);
        }
    }

    boolean isDialogNeeded() {
        boolean isDataRoamingEnabled = this.mTelephonyManager.isDataRoamingEnabled();
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
        if (!isDataRoamingEnabled) {
            return configForSubId == null || !configForSubId.getBoolean("disable_charge_indication_bool");
        }
        return false;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return this.mTelephonyManager.isDataRoamingEnabled();
    }

    public void init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        int i2 = this.mSubId;
        if (i2 == -1) {
            return;
        }
        TelephonyManager createForSubscriptionId = telephonyManager.createForSubscriptionId(i2);
        if (createForSubscriptionId == null) {
            Log.w(TAG, "fail to init in sub" + this.mSubId);
            this.mSubId = -1;
            return;
        }
        this.mTelephonyManager = createForSubscriptionId;
    }

    private void showDialog() {
        RoamingDialogFragment.newInstance(this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopMonitor() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListener;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListener = null;
        }
    }

    private void stopMonitorSubIdSpecific() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListenerForSubId;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListenerForSubId = null;
        }
    }
}
