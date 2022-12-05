package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public class SmartFiveGPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String TAG = "SmartFiveGPreferenceController";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    Integer mCallState;
    private boolean mChangedBy5gToggle = false;
    private final BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.SmartFiveGPreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (SmartFiveGPreferenceController.this.mPreference != null) {
                Log.d(SmartFiveGPreferenceController.TAG, "DDS is changed");
                SmartFiveGPreferenceController smartFiveGPreferenceController = SmartFiveGPreferenceController.this;
                smartFiveGPreferenceController.updateState(smartFiveGPreferenceController.mPreference);
            }
        }
    };
    private PhoneTelephonyCallback mPhoneTelephonyCallback;
    Preference mPreference;
    private SharedPreferences mSharedPreferences;
    private ContentObserver mSubsidySettingsObserver;
    private TelephonyManager mTelephonyManager;

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        return 0;
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

    public SmartFiveGPreferenceController(Context context, String str) {
        super(context, str);
    }

    public SmartFiveGPreferenceController init(int i) {
        if (this.mPhoneTelephonyCallback == null) {
            this.mPhoneTelephonyCallback = new PhoneTelephonyCallback();
        }
        if (this.mAllowedNetworkTypesListener == null) {
            AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
            this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
            allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AllowedNetworkTypesListener.OnAllowedNetworkTypesListener() { // from class: com.android.settings.network.telephony.SmartFiveGPreferenceController$$ExternalSyntheticLambda0
                @Override // com.android.settings.network.AllowedNetworkTypesListener.OnAllowedNetworkTypesListener
                public final void onAllowedNetworkTypesChanged() {
                    SmartFiveGPreferenceController.this.lambda$init$0();
                }
            });
        }
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) || this.mSubId != i) {
            this.mSubId = i;
            this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
            Context context = this.mContext;
            this.mSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
            return this;
        }
        return this;
    }

    private void update() {
        Log.d(TAG, "update.");
        lambda$init$0();
        this.mChangedBy5gToggle = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updatePreference */
    public void lambda$init$0() {
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mContext.registerReceiver(this.mDefaultDataChangedReceiver, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
        PhoneTelephonyCallback phoneTelephonyCallback = this.mPhoneTelephonyCallback;
        if (phoneTelephonyCallback != null) {
            phoneTelephonyCallback.register(this.mContext, this.mSubId);
        }
        AllowedNetworkTypesListener allowedNetworkTypesListener = this.mAllowedNetworkTypesListener;
        if (allowedNetworkTypesListener != null) {
            allowedNetworkTypesListener.register(this.mContext, this.mSubId);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        BroadcastReceiver broadcastReceiver = this.mDefaultDataChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
        PhoneTelephonyCallback phoneTelephonyCallback = this.mPhoneTelephonyCallback;
        if (phoneTelephonyCallback != null) {
            phoneTelephonyCallback.unregister();
        }
        AllowedNetworkTypesListener allowedNetworkTypesListener = this.mAllowedNetworkTypesListener;
        if (allowedNetworkTypesListener != null) {
            allowedNetworkTypesListener.unregister(this.mContext, this.mSubId);
        }
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        SwitchPreference switchPreference = (SwitchPreference) preference;
        switchPreference.setVisible(isAvailable());
        boolean z = true;
        if (isChecked()) {
            Log.d(TAG, "updateState check");
            switchPreference.setChecked(true);
        } else {
            switchPreference.setChecked(false);
        }
        int preferredNetworkMode = getPreferredNetworkMode();
        boolean isSwitchVisible = isSwitchVisible(this.mContext, this.mSubId);
        if (!isCallStateIdle() || preferredNetworkMode <= 22 || !isSwitchVisible) {
            z = false;
        }
        switchPreference.setEnabled(z);
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return false;
        }
        this.mChangedBy5gToggle = true;
        if (!z) {
            showDisableSmart5gDialog();
        } else {
            MobileNetworkUtils.setSmart5gMode(this.mContext, 1);
            MobileNetworkUtils.log5GEvent(this.mContext, "smart_5g", 1);
        }
        return true;
    }

    private void showDisableSmart5gDialog() {
        Log.d(TAG, "showDisableSmart5gDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.network.telephony.SmartFiveGPreferenceController.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    MobileNetworkUtils.setSmart5gMode(((AbstractPreferenceController) SmartFiveGPreferenceController.this).mContext, 0);
                    MobileNetworkUtils.log5GEvent(((AbstractPreferenceController) SmartFiveGPreferenceController.this).mContext, "smart_5g", 0);
                    return;
                }
                SmartFiveGPreferenceController.this.lambda$init$0();
            }
        };
        builder.setMessage(R.string.turn_off_smart5g_dialog_title).setNeutralButton(this.mContext.getResources().getString(R.string.cancel), onClickListener).setPositiveButton(this.mContext.getResources().getString(R.string.condition_turn_off), onClickListener).create().show();
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return MobileNetworkUtils.getSmart5gMode(this.mContext) == 1;
    }

    boolean isCallStateIdle() {
        Integer num = this.mCallState;
        boolean z = num == null || num.intValue() == 0;
        Log.d(TAG, "isCallStateIdle:" + z);
        return z;
    }

    private int getPreferredNetworkMode() {
        return MobileNetworkUtils.getNetworkTypeFromRaf((int) this.mTelephonyManager.getAllowedNetworkTypesForReason(0));
    }

    TelephonyManager getTelephonyManager(Context context, int i) {
        TelephonyManager createForSubscriptionId;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        return (SubscriptionManager.isValidSubscriptionId(i) && (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) != null) ? createForSubscriptionId : telephonyManager;
    }

    /* loaded from: classes.dex */
    private class PhoneTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mTelephonyManager;

        private PhoneTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            SmartFiveGPreferenceController.this.mCallState = Integer.valueOf(i);
            SmartFiveGPreferenceController smartFiveGPreferenceController = SmartFiveGPreferenceController.this;
            smartFiveGPreferenceController.updateState(smartFiveGPreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            TelephonyManager telephonyManager = SmartFiveGPreferenceController.this.getTelephonyManager(context, i);
            this.mTelephonyManager = telephonyManager;
            SmartFiveGPreferenceController.this.mCallState = Integer.valueOf(telephonyManager.getCallState(i));
            this.mTelephonyManager.registerTelephonyCallback(context.getMainExecutor(), this);
        }

        public void unregister() {
            SmartFiveGPreferenceController.this.mCallState = null;
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }
    }

    public static boolean isSwitchVisible(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return true;
        }
        return !configForSubId.getBoolean("hide_smart_5g_bool");
    }
}
