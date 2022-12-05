package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Looper;
import android.os.PersistableBundle;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionManager;
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
/* loaded from: classes.dex */
public class Enabled5GPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final int NETWORK_MODE_TYPE_INVALID = -1;
    private static final String TAG = "Enable5g";
    private static final String USER_SELECTED_NW_MODE_KEY = "user_selected_network_type_";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    Integer mCallState;
    private PhoneCallStateListener mPhoneStateListener;
    Preference mPreference;
    private SharedPreferences mSharedPreferences;
    private ContentObserver mSubsidySettingsObserver;
    private TelephonyManager mTelephonyManager;
    private boolean mIsNrRadioSupported = false;
    private boolean mIsDualNrSupported = false;
    private boolean mChangedBy5gToggle = false;
    private final BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.Enabled5GPreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (Enabled5GPreferenceController.this.mPreference != null) {
                Log.d(Enabled5GPreferenceController.TAG, "DDS is changed");
                Enabled5GPreferenceController enabled5GPreferenceController = Enabled5GPreferenceController.this;
                enabled5GPreferenceController.updateState(enabled5GPreferenceController.mPreference);
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

    public Enabled5GPreferenceController(Context context, String str) {
        super(context, str);
    }

    public Enabled5GPreferenceController init(int i) {
        if (this.mPhoneStateListener == null) {
            this.mPhoneStateListener = new PhoneCallStateListener();
        }
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) || this.mSubId != i) {
            this.mSubId = i;
            TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
            this.mTelephonyManager = createForSubscriptionId;
            this.mIsNrRadioSupported = checkSupportedRadioBitmask(createForSubscriptionId.getSupportedRadioAccessFamily(), 524288L);
            this.mIsDualNrSupported = TelephonyUtils.isDual5gSupported(this.mTelephonyManager);
            if (this.mAllowedNetworkTypesListener == null) {
                AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
                this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
                allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AllowedNetworkTypesListener.OnAllowedNetworkTypesListener() { // from class: com.android.settings.network.telephony.Enabled5GPreferenceController$$ExternalSyntheticLambda0
                    @Override // com.android.settings.network.AllowedNetworkTypesListener.OnAllowedNetworkTypesListener
                    public final void onAllowedNetworkTypesChanged() {
                        Enabled5GPreferenceController.this.lambda$init$0();
                    }
                });
            }
            Context context = this.mContext;
            this.mSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
            return this;
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: update */
    public void lambda$init$0() {
        Log.d(TAG, "update.");
        updatePreference();
        if (!this.mChangedBy5gToggle) {
            cachePreviousSelectedNwType(NETWORK_MODE_TYPE_INVALID);
        }
        this.mChangedBy5gToggle = false;
    }

    private void updatePreference() {
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId != null && this.mTelephonyManager != null) {
            SubscriptionManager.getDefaultDataSubscriptionId();
            boolean z = this.mIsDualNrSupported;
            checkSupportedRadioBitmask(this.mTelephonyManager.getAllowedNetworkTypes(), 524288L);
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                carrierConfigForSubId.getBoolean("hide_enabled_5g_bool");
            }
        }
        return 2;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mContext.registerReceiver(this.mDefaultDataChangedReceiver, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
        PhoneCallStateListener phoneCallStateListener = this.mPhoneStateListener;
        if (phoneCallStateListener != null) {
            phoneCallStateListener.register(this.mContext, this.mSubId);
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
        PhoneCallStateListener phoneCallStateListener = this.mPhoneStateListener;
        if (phoneCallStateListener != null) {
            phoneCallStateListener.unregister();
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
        switchPreference.setChecked(isNrNetworkModeType(MobileNetworkUtils.getRafFromNetworkType(getAllowedNetworkMode())));
        switchPreference.setEnabled(isCallStateIdle());
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        long rafFromNetworkType;
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return false;
        }
        int allowedNetworkMode = getAllowedNetworkMode();
        if (23 != allowedNetworkMode) {
            long rafFromNetworkType2 = MobileNetworkUtils.getRafFromNetworkType(allowedNetworkMode);
            if (z) {
                long j = 93108 & rafFromNetworkType2;
                int i = ((397312 & rafFromNetworkType2) > 0L ? 1 : ((397312 & rafFromNetworkType2) == 0L ? 0 : -1));
                if (i == 0 && j == 0) {
                    rafFromNetworkType2 = MobileNetworkUtils.getRafFromNetworkType(22);
                    cachePreviousSelectedNwType(allowedNetworkMode);
                } else if (i == 0) {
                    rafFromNetworkType2 = allowedNetworkMode == 6 ? MobileNetworkUtils.getRafFromNetworkType(8) : rafFromNetworkType2 | 4096;
                    cachePreviousSelectedNwType(allowedNetworkMode);
                } else {
                    cachePreviousSelectedNwType(NETWORK_MODE_TYPE_INVALID);
                }
            }
            int previousSelectedNwType = getPreviousSelectedNwType();
            if (previousSelectedNwType == NETWORK_MODE_TYPE_INVALID || z) {
                rafFromNetworkType = z ? rafFromNetworkType2 | 524288 : rafFromNetworkType2 & (-524289);
            } else {
                Log.d(TAG, "userSelectedNwMode: " + previousSelectedNwType);
                rafFromNetworkType = MobileNetworkUtils.getRafFromNetworkType(previousSelectedNwType);
                cachePreviousSelectedNwType(NETWORK_MODE_TYPE_INVALID);
            }
        } else {
            rafFromNetworkType = MobileNetworkUtils.getRafFromNetworkType(11);
        }
        this.mTelephonyManager.setAllowedNetworkTypesForReason(0, rafFromNetworkType);
        this.mChangedBy5gToggle = true;
        return true;
    }

    private void cachePreviousSelectedNwType(int i) {
        Log.d(TAG, "cachePreviousSelectedNwType: " + i);
        int slotIndex = SubscriptionManager.getSlotIndex(this.mSubId);
        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
        edit.putInt(USER_SELECTED_NW_MODE_KEY + slotIndex, i).apply();
    }

    private int getPreviousSelectedNwType() {
        int slotIndex = SubscriptionManager.getSlotIndex(this.mSubId);
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        return sharedPreferences.getInt(USER_SELECTED_NW_MODE_KEY + slotIndex, NETWORK_MODE_TYPE_INVALID);
    }

    private int getAllowedNetworkMode() {
        return MobileNetworkUtils.getNetworkTypeFromRaf((int) this.mTelephonyManager.getAllowedNetworkTypesForReason(0));
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return isNrNetworkModeType(MobileNetworkUtils.getRafFromNetworkType(getAllowedNetworkMode()));
    }

    private boolean isNrNetworkModeType(long j) {
        return checkSupportedRadioBitmask(j, 524288L);
    }

    boolean checkSupportedRadioBitmask(long j, long j2) {
        Log.d(TAG, "supportedRadioBitmask: " + j);
        return (j2 & j) > 0;
    }

    boolean isCallStateIdle() {
        Integer num = this.mCallState;
        boolean z = num == null || num.intValue() == 0;
        Log.d(TAG, "isCallStateIdle:" + z);
        return z;
    }

    /* loaded from: classes.dex */
    private class PhoneCallStateListener extends PhoneStateListener {
        private TelephonyManager mTelephonyManager;

        PhoneCallStateListener() {
            super(Looper.getMainLooper());
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            Enabled5GPreferenceController.this.mCallState = Integer.valueOf(i);
            Enabled5GPreferenceController enabled5GPreferenceController = Enabled5GPreferenceController.this;
            enabled5GPreferenceController.updateState(enabled5GPreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
            }
            this.mTelephonyManager.listen(this, 32);
        }

        public void unregister() {
            Enabled5GPreferenceController.this.mCallState = null;
            this.mTelephonyManager.listen(this, 0);
        }
    }
}
