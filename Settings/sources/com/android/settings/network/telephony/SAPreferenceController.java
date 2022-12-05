package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import org.codeaurora.internal.IExtTelephony;
/* loaded from: classes.dex */
public class SAPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String TAG = "SAPreferenceController";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    Integer mCallState;
    private PhoneTelephonyCallback mPhoneTelephonyCallback;
    Preference mPreference;
    private SharedPreferences mSharedPreferences;
    private int mSlotId;
    private ContentObserver mSubsidySettingsObserver;
    private TelephonyManager mTelephonyManager;
    private boolean mChangedBy5gToggle = false;
    private IExtTelephony mExtTelephony = IExtTelephony.Stub.asInterface(ServiceManager.getService("qti.radio.extphone"));
    private final BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.SAPreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (SAPreferenceController.this.mPreference != null) {
                Log.d(SAPreferenceController.TAG, "DDS is changed");
                SAPreferenceController sAPreferenceController = SAPreferenceController.this;
                sAPreferenceController.updateState(sAPreferenceController.mPreference);
            }
        }
    };

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public SAPreferenceController(Context context, String str) {
        super(context, str);
    }

    public SAPreferenceController init(int i) {
        if (this.mPhoneTelephonyCallback == null) {
            this.mPhoneTelephonyCallback = new PhoneTelephonyCallback();
        }
        if (this.mAllowedNetworkTypesListener == null) {
            AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
            this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
            allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AllowedNetworkTypesListener.OnAllowedNetworkTypesListener() { // from class: com.android.settings.network.telephony.SAPreferenceController$$ExternalSyntheticLambda0
                @Override // com.android.settings.network.AllowedNetworkTypesListener.OnAllowedNetworkTypesListener
                public final void onAllowedNetworkTypesChanged() {
                    SAPreferenceController.this.lambda$init$0();
                }
            });
        }
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) || this.mSubId != i) {
            this.mSubId = i;
            this.mSlotId = SubscriptionManager.getPhoneId(i);
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

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId == null || carrierConfigForSubId.getBoolean("hide_5g_standalone_bool")) {
            return 2;
        }
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        return defaultDataSubscriptionId == i && SubscriptionManager.isValidSubscriptionId(defaultDataSubscriptionId) && SubscriptionManager.from(this.mContext).isActiveSubId(defaultDataSubscriptionId) ? 0 : 2;
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

    private int getPreferredNetworkMode() {
        return MobileNetworkUtils.getNetworkTypeFromRaf((int) this.mTelephonyManager.getAllowedNetworkTypesForReason(0));
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean z = true;
        switchPreference.setVisible(isAvailable() && isSAEnabledFromCarrierConfig());
        int preferredNetworkMode = getPreferredNetworkMode();
        if (!isCallStateIdle() || preferredNetworkMode <= 22) {
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
            set5gModeForSlotId(1);
            MobileNetworkUtils.log5GEvent(this.mContext, "5g_sa", 0);
        } else {
            set5gModeForSlotId(0);
            MobileNetworkUtils.log5GEvent(this.mContext, "5g_sa", 1);
        }
        return true;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return get5gModeForSlotId() == 0;
    }

    boolean isCallStateIdle() {
        Integer num = this.mCallState;
        boolean z = num == null || num.intValue() == 0;
        Log.d(TAG, "isCallStateIdle:" + z);
        return z;
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
            SAPreferenceController.this.mCallState = Integer.valueOf(i);
            SAPreferenceController sAPreferenceController = SAPreferenceController.this;
            sAPreferenceController.updateState(sAPreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            TelephonyManager telephonyManager = SAPreferenceController.this.getTelephonyManager(context, i);
            this.mTelephonyManager = telephonyManager;
            SAPreferenceController.this.mCallState = Integer.valueOf(telephonyManager.getCallState(i));
            this.mTelephonyManager.registerTelephonyCallback(context.getMainExecutor(), this);
        }

        public void unregister() {
            SAPreferenceController.this.mCallState = null;
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }
    }

    private int get5gModeForSlotId() {
        try {
            return this.mExtTelephony.get5GMode(this.mSlotId);
        } catch (RemoteException e) {
            Log.d(TAG, "RemoteException @get5gModeForSlotId=" + e + ", phoneId=" + this.mSlotId);
            return 0;
        } catch (NullPointerException e2) {
            Log.d(TAG, "NullPointerException @get5gModeForSlotId=" + e2 + ", phoneId=" + this.mSlotId);
            return 0;
        }
    }

    private void set5gModeForSlotId(final int i) {
        new Thread(new Runnable() { // from class: com.android.settings.network.telephony.SAPreferenceController.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    SAPreferenceController.this.mExtTelephony.set5GMode(i, SAPreferenceController.this.mSlotId);
                } catch (RemoteException e) {
                    Log.d(SAPreferenceController.TAG, "RemoteException @set5gModeForSlotId=" + e + ", phoneId=" + SAPreferenceController.this.mSlotId);
                } catch (NullPointerException e2) {
                    Log.d(SAPreferenceController.TAG, "NullPointerException @set5gModeForSlotId=" + e2 + ", phoneId=" + SAPreferenceController.this.mSlotId);
                }
            }
        }).start();
    }

    private boolean isSAEnabledFromCarrierConfig() {
        PersistableBundle configForSubId;
        super.isAvailable();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mSubId)) == null) {
            return false;
        }
        boolean z = false;
        for (int i : configForSubId.getIntArray("carrier_nr_availabilities_int_array")) {
            if (i == 2) {
                z = true;
            }
        }
        return z;
    }
}
