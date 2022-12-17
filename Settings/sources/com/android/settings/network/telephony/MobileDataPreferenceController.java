package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.network.MobileDataContentObserver;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MobileDataPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String DIALOG_TAG = "MobileDataDialog";
    private static final String TAG = "MobileDataPreferenceController";
    /* access modifiers changed from: private */
    public static boolean mIsMultiSim;
    private AnotherSubCallStateListener mCallStateListener;
    private MobileDataContentObserver mDataContentObserver;
    int mDialogType;
    private FragmentManager mFragmentManager;
    boolean mNeedDialog;
    private SwitchPreference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private WifiPickerTrackerHelper mWifiPickerTrackerHelper;

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

    public MobileDataPreferenceController(Context context, String str) {
        super(context, str);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        MobileDataContentObserver mobileDataContentObserver = new MobileDataContentObserver(new Handler(Looper.getMainLooper()));
        this.mDataContentObserver = mobileDataContentObserver;
        mobileDataContentObserver.setOnMobileDataChangedListener(new MobileDataPreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateState(this.mPreference);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        int i = this.mSubId;
        if (i != -1) {
            this.mDataContentObserver.register(this.mContext, i);
            TelephonyManager telephonyManager = getTelephonyManager();
            this.mTelephonyManager = telephonyManager;
            boolean z = true;
            if (telephonyManager.getActiveModemCount() <= 1) {
                z = false;
            }
            mIsMultiSim = z;
            if (this.mSubId == SubscriptionManager.getDefaultDataSubscriptionId()) {
                this.mCallStateListener.register(this.mContext, this.mSubId);
            }
        }
    }

    public void onStop() {
        if (this.mSubId != -1) {
            this.mDataContentObserver.unRegister(this.mContext);
            this.mCallStateListener.unregister();
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        if (!this.mNeedDialog) {
            return true;
        }
        showDialog(this.mDialogType);
        return true;
    }

    public boolean setChecked(boolean z) {
        boolean isDialogNeeded = isDialogNeeded();
        this.mNeedDialog = isDialogNeeded;
        if (isDialogNeeded) {
            return false;
        }
        MobileNetworkUtils.setMobileDataEnabled(this.mContext, this.mSubId, z, false);
        WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
        if (wifiPickerTrackerHelper == null || wifiPickerTrackerHelper.isCarrierNetworkProvisionEnabled(this.mSubId)) {
            return true;
        }
        this.mWifiPickerTrackerHelper.setCarrierNetworkEnabled(z);
        return true;
    }

    public boolean isChecked() {
        TelephonyManager telephonyManager = getTelephonyManager();
        this.mTelephonyManager = telephonyManager;
        return telephonyManager.isDataEnabled();
    }

    public void updateState(Preference preference) {
        if (this.mTelephonyManager != null) {
            super.updateState(preference);
            if (isOpportunistic()) {
                preference.setEnabled(false);
                preference.setSummary(R$string.mobile_data_settings_summary_auto_switch);
            } else if (this.mCallStateListener.isIdle()) {
                if (!TelephonyUtils.isSubsidyFeatureEnabled(this.mContext) || TelephonyUtils.isSubsidySimCard(this.mContext, SubscriptionManager.getSlotIndex(this.mSubId))) {
                    preference.setEnabled(true);
                } else {
                    preference.setEnabled(false);
                }
                preference.setSummary(R$string.mobile_data_settings_summary);
            } else if (mIsMultiSim) {
                Log.d(TAG, "nDDS voice call in ongoing");
                if (isChecked()) {
                    Log.d(TAG, "Do not allow the user to turn off DDS mobile data");
                    preference.setEnabled(false);
                    preference.setSummary(R$string.mobile_data_settings_summary_default_data_unavailable);
                }
            }
            if (this.mSubId == -1) {
                preference.setSelectable(false);
                preference.setSummary(R$string.mobile_data_settings_summary_unavailable);
                return;
            }
            preference.setSelectable(true);
        }
    }

    private boolean isOpportunistic() {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(this.mSubId);
        return activeSubscriptionInfo != null && activeSubscriptionInfo.isOpportunistic();
    }

    public void init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        this.mTelephonyManager = null;
        this.mTelephonyManager = getTelephonyManager();
        this.mCallStateListener = new AnotherSubCallStateListener(this.mTelephonyManager, this.mSubscriptionManager, new MobileDataPreferenceController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1() {
        updateState(this.mPreference);
    }

    private TelephonyManager getTelephonyManager() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            return telephonyManager;
        }
        TelephonyManager telephonyManager2 = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        int i = this.mSubId;
        if (i != -1) {
            telephonyManager2 = telephonyManager2.createForSubscriptionId(i);
        }
        this.mTelephonyManager = telephonyManager2;
        return telephonyManager2;
    }

    public void setWifiPickerTrackerHelper(WifiPickerTrackerHelper wifiPickerTrackerHelper) {
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
    }

    /* access modifiers changed from: package-private */
    public boolean isDialogNeeded() {
        boolean z;
        boolean z2 = !isChecked();
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        boolean z3 = this.mSubscriptionManager.isActiveSubscriptionId(defaultDataSubscriptionId) && defaultDataSubscriptionId != this.mSubId;
        if (this.mContext.getResources().getBoolean(17891828)) {
            z3 = false;
        }
        try {
            if (this.mTelephonyManager.getImsRegistration(SubscriptionManager.getSlotIndex(this.mSubId), 1).getRegistrationTechnology() == 2) {
                z = true;
                Log.d(TAG, "isDialogNeeded: enableData=" + z2 + ", isMultiSim=" + mIsMultiSim + ", needToDisableOthers=" + z3 + ", isImsRegisteredOverCiwlan=" + z + ", callIdle=" + this.mCallStateListener.isIdle());
                if (z2 && mIsMultiSim && z3) {
                    this.mDialogType = 1;
                    return true;
                } else if (!this.mCallStateListener.isIdle() || !z || z2) {
                    return false;
                } else {
                    this.mDialogType = 2;
                    return true;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getRegistrationTechnology failed", e);
        }
        z = false;
        Log.d(TAG, "isDialogNeeded: enableData=" + z2 + ", isMultiSim=" + mIsMultiSim + ", needToDisableOthers=" + z3 + ", isImsRegisteredOverCiwlan=" + z + ", callIdle=" + this.mCallStateListener.isIdle());
        if (!(z2 || mIsMultiSim)) {
        }
        if (!this.mCallStateListener.isIdle()) {
        }
        return false;
    }

    private void showDialog(int i) {
        MobileDataDialogFragment.newInstance(i, this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    private static class AnotherSubCallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private Map<Integer, AnotherSubCallStateListener> mCallbacks;
        private Runnable mRunnable;
        private int mState = 0;
        private SubscriptionManager mSubscriptionManager;
        private TelephonyManager mTelephonyManager;

        public AnotherSubCallStateListener(TelephonyManager telephonyManager, SubscriptionManager subscriptionManager, Runnable runnable) {
            this.mTelephonyManager = telephonyManager;
            this.mSubscriptionManager = subscriptionManager;
            this.mRunnable = runnable;
            this.mCallbacks = new TreeMap();
        }

        public void register(Context context, int i) {
            List<SubscriptionInfo> activeSubscriptions = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager);
            if (MobileDataPreferenceController.mIsMultiSim) {
                for (SubscriptionInfo next : activeSubscriptions) {
                    if (next.getSubscriptionId() != i) {
                        this.mTelephonyManager.createForSubscriptionId(next.getSubscriptionId()).registerTelephonyCallback(context.getMainExecutor(), this);
                        this.mCallbacks.put(Integer.valueOf(next.getSubscriptionId()), this);
                    }
                }
                return;
            }
            this.mTelephonyManager.createForSubscriptionId(i).registerTelephonyCallback(context.getMainExecutor(), this);
            this.mCallbacks.put(Integer.valueOf(i), this);
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
