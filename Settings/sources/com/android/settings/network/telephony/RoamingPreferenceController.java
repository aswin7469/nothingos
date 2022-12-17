package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.network.GlobalSettingsChangeListener;
import com.android.settings.network.SubscriptionUtil;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.Map;
import java.util.TreeMap;

public class RoamingPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String DIALOG_TAG = "MobileDataDialog";
    private static final String TAG = "RoamingController";
    private CarrierConfigManager mCarrierConfigManager;
    int mDialogType;
    FragmentManager mFragmentManager;
    private GlobalSettingsChangeListener mListener;
    private GlobalSettingsChangeListener mListenerForSubId;
    private NonDdsCallStateListener mNonDdsCallStateListener;
    public SubscriptionManager mSubscriptionManager;
    /* access modifiers changed from: private */
    public RestrictedSwitchPreference mSwitchPreference;
    private TelephonyManager mTelephonyManager;

    public int getAvailabilityStatus(int i) {
        return i != -1 ? 0 : 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RoamingPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public void onStart() {
        if (this.mListener == null) {
            this.mListener = new GlobalSettingsChangeListener(this.mContext, "data_roaming") {
                public void onChanged(String str) {
                    RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                    roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
                }
            };
        }
        stopMonitorSubIdSpecific();
        if (this.mSubId != -1) {
            Context context = this.mContext;
            this.mListenerForSubId = new GlobalSettingsChangeListener(context, "data_roaming" + this.mSubId) {
                public void onChanged(String str) {
                    RoamingPreferenceController.this.stopMonitor();
                    RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                    roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
                }
            };
            if (this.mSubId == SubscriptionManager.getDefaultDataSubscriptionId()) {
                this.mNonDdsCallStateListener.register(this.mContext, this.mSubId);
            }
        }
    }

    public void onStop() {
        stopMonitor();
        stopMonitorSubIdSpecific();
        if (this.mSubId != -1) {
            this.mNonDdsCallStateListener.unregister();
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean setChecked(boolean z) {
        if (isDialogNeeded()) {
            showDialog(this.mDialogType);
            return false;
        }
        this.mTelephonyManager.setDataRoamingEnabled(z);
        return true;
    }

    public void updateState(Preference preference) {
        if (this.mTelephonyManager != null) {
            super.updateState(preference);
            RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
            if (!restrictedSwitchPreference.isDisabledByAdmin()) {
                restrictedSwitchPreference.setEnabled(this.mSubId != -1);
                restrictedSwitchPreference.setChecked(isChecked());
                if (!this.mNonDdsCallStateListener.isIdle()) {
                    Log.d(TAG, "nDDS voice call in ongoing");
                    if (isChecked()) {
                        Log.d(TAG, "Do not allow the user to turn off DDS data roaming");
                        preference.setEnabled(false);
                        preference.setSummary(R$string.mobile_data_settings_summary_dds_roaming_unavailable);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDialogNeeded() {
        boolean z;
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            return false;
        }
        boolean isDataRoamingEnabled = telephonyManager.isDataRoamingEnabled();
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
        if (isDataRoamingEnabled || (configForSubId != null && configForSubId.getBoolean("disable_charge_indication_bool"))) {
            boolean z2 = this.mTelephonyManager.getCallState() == 0;
            try {
                if (this.mTelephonyManager.getImsRegistration(SubscriptionManager.getSlotIndex(this.mSubId), 1).getRegistrationTechnology() == 2) {
                    z = true;
                    Log.d(TAG, "isDialogNeeded: isRoamingEnabled=" + isDataRoamingEnabled + ", isCallIdle=" + z2 + ", isImsRegisteredOverCiwlan=" + z);
                    if (isDataRoamingEnabled || z2 || !z) {
                        return false;
                    }
                    this.mDialogType = 1;
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "getRegistrationTechnology failed", e);
            }
            z = false;
            Log.d(TAG, "isDialogNeeded: isRoamingEnabled=" + isDataRoamingEnabled + ", isCallIdle=" + z2 + ", isImsRegisteredOverCiwlan=" + z);
            if (isDataRoamingEnabled) {
            }
            return false;
        }
        this.mDialogType = 0;
        return true;
    }

    public boolean isChecked() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            return false;
        }
        return telephonyManager.isDataRoamingEnabled();
    }

    public void init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        int i2 = this.mSubId;
        if (i2 != -1) {
            TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i2);
            if (createForSubscriptionId == null) {
                Log.w(TAG, "fail to init in sub" + this.mSubId);
                this.mSubId = -1;
                return;
            }
            this.mTelephonyManager = createForSubscriptionId;
            this.mNonDdsCallStateListener = new NonDdsCallStateListener(this.mTelephonyManager, this.mSubscriptionManager, new RoamingPreferenceController$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0() {
        updateState(this.mSwitchPreference);
    }

    private void showDialog(int i) {
        RoamingDialogFragment.newInstance(i, this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    /* access modifiers changed from: private */
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

    private static class NonDdsCallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private Map<Integer, NonDdsCallStateListener> mCallbacks;
        private Runnable mRunnable;
        private int mState = 0;
        private SubscriptionManager mSubscriptionManager;
        private TelephonyManager mTelephonyManager;

        public NonDdsCallStateListener(TelephonyManager telephonyManager, SubscriptionManager subscriptionManager, Runnable runnable) {
            this.mTelephonyManager = telephonyManager;
            this.mSubscriptionManager = subscriptionManager;
            this.mRunnable = runnable;
            this.mCallbacks = new TreeMap();
        }

        public void register(Context context, int i) {
            for (SubscriptionInfo next : SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager)) {
                if (next.getSubscriptionId() != i) {
                    this.mTelephonyManager.createForSubscriptionId(next.getSubscriptionId()).registerTelephonyCallback(context.getMainExecutor(), this);
                    this.mCallbacks.put(Integer.valueOf(next.getSubscriptionId()), this);
                }
            }
        }

        public void unregister() {
            for (Integer intValue : this.mCallbacks.keySet()) {
                int intValue2 = intValue.intValue();
                this.mTelephonyManager.createForSubscriptionId(intValue2).unregisterTelephonyCallback(this.mCallbacks.get(Integer.valueOf(intValue2)));
            }
            this.mCallbacks.clear();
        }

        public boolean isIdle() {
            return this.mState == 0;
        }

        public void onCallStateChanged(int i) {
            this.mState = i;
            Runnable runnable = this.mRunnable;
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}
