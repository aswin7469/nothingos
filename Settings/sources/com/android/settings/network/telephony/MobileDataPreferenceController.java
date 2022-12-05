package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.network.MobileDataContentObserver;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class MobileDataPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String DIALOG_TAG = "MobileDataDialog";
    private AnotherSubCallStateListener mCallStateListener;
    private MobileDataContentObserver mDataContentObserver;
    int mDialogType;
    private FragmentManager mFragmentManager;
    boolean mNeedDialog;
    private SwitchPreference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private WifiPickerTrackerHelper mWifiPickerTrackerHelper;

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

    public MobileDataPreferenceController(Context context, String str) {
        super(context, str);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        MobileDataContentObserver mobileDataContentObserver = new MobileDataContentObserver(new Handler(Looper.getMainLooper()));
        this.mDataContentObserver = mobileDataContentObserver;
        mobileDataContentObserver.setOnMobileDataChangedListener(new MobileDataContentObserver.OnMobileDataChangedListener() { // from class: com.android.settings.network.telephony.MobileDataPreferenceController$$ExternalSyntheticLambda0
            @Override // com.android.settings.network.MobileDataContentObserver.OnMobileDataChangedListener
            public final void onMobileDataChanged() {
                MobileDataPreferenceController.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateState(this.mPreference);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        int i = this.mSubId;
        if (i != -1) {
            this.mDataContentObserver.register(this.mContext, i);
            if (this.mSubId != SubscriptionManager.getDefaultDataSubscriptionId()) {
                return;
            }
            this.mCallStateListener.register(this.mContext, this.mSubId);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        if (this.mSubId != -1) {
            this.mDataContentObserver.unRegister(this.mContext);
            this.mCallStateListener.unregister();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            if (!this.mNeedDialog) {
                return true;
            }
            showDialog(this.mDialogType);
            return true;
        }
        return false;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        boolean isDialogNeeded = isDialogNeeded();
        this.mNeedDialog = isDialogNeeded;
        if (!isDialogNeeded) {
            MobileNetworkUtils.setMobileDataEnabled(this.mContext, this.mSubId, z, false);
            WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
            if (wifiPickerTrackerHelper == null || wifiPickerTrackerHelper.isCarrierNetworkProvisionEnabled(this.mSubId)) {
                return true;
            }
            this.mWifiPickerTrackerHelper.setCarrierNetworkEnabled(z);
            return true;
        }
        return false;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return this.mTelephonyManager.isDataEnabled();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (isOpportunistic()) {
            preference.setEnabled(false);
            preference.setSummary(R.string.mobile_data_settings_summary_auto_switch);
        } else if (!this.mCallStateListener.isIdle()) {
            preference.setEnabled(false);
            preference.setSummary(R.string.mobile_data_settings_summary_default_data_unavailable);
        } else {
            if (TelephonyUtils.isSubsidyFeatureEnabled(this.mContext) && !TelephonyUtils.isSubsidySimCard(this.mContext, SubscriptionManager.getSlotIndex(this.mSubId))) {
                preference.setEnabled(false);
            } else {
                preference.setEnabled(true);
            }
            preference.setSummary(R.string.mobile_data_settings_summary);
        }
        if (this.mSubId == -1) {
            preference.setSelectable(false);
            preference.setSummary(R.string.mobile_data_settings_summary_unavailable);
            return;
        }
        preference.setSelectable(true);
    }

    private boolean isOpportunistic() {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(this.mSubId);
        return activeSubscriptionInfo != null && activeSubscriptionInfo.isOpportunistic();
    }

    public void init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mCallStateListener = new AnotherSubCallStateListener(this.mTelephonyManager, this.mSubscriptionManager, new Runnable() { // from class: com.android.settings.network.telephony.MobileDataPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MobileDataPreferenceController.this.lambda$init$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1() {
        updateState(this.mPreference);
    }

    public void setWifiPickerTrackerHelper(WifiPickerTrackerHelper wifiPickerTrackerHelper) {
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
    }

    boolean isDialogNeeded() {
        boolean z = !isChecked();
        boolean z2 = this.mTelephonyManager.getActiveModemCount() > 1;
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        boolean z3 = this.mSubscriptionManager.isActiveSubscriptionId(defaultDataSubscriptionId) && defaultDataSubscriptionId != this.mSubId;
        if (this.mContext.getResources().getBoolean(17891701)) {
            z3 = false;
        }
        if (!z || !z2 || !z3) {
            return false;
        }
        this.mDialogType = 1;
        return true;
    }

    private void showDialog(int i) {
        MobileDataDialogFragment.newInstance(i, this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnotherSubCallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private Runnable mRunnable;
        private SubscriptionManager mSubscriptionManager;
        private TelephonyManager mTelephonyManager;
        private int mState = 0;
        private Map<Integer, AnotherSubCallStateListener> mCallbacks = new TreeMap();

        public AnotherSubCallStateListener(TelephonyManager telephonyManager, SubscriptionManager subscriptionManager, Runnable runnable) {
            this.mTelephonyManager = telephonyManager;
            this.mSubscriptionManager = subscriptionManager;
            this.mRunnable = runnable;
        }

        public void register(Context context, int i) {
            for (SubscriptionInfo subscriptionInfo : SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager)) {
                if (subscriptionInfo.getSubscriptionId() != i) {
                    this.mTelephonyManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId()).registerTelephonyCallback(context.getMainExecutor(), this);
                    this.mCallbacks.put(Integer.valueOf(subscriptionInfo.getSubscriptionId()), this);
                }
            }
        }

        public void unregister() {
            for (Integer num : this.mCallbacks.keySet()) {
                int intValue = num.intValue();
                this.mTelephonyManager.createForSubscriptionId(intValue).unregisterTelephonyCallback(this.mCallbacks.get(Integer.valueOf(intValue)));
            }
            this.mCallbacks.clear();
        }

        public boolean isIdle() {
            return this.mState == 0;
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            this.mState = i;
            Runnable runnable = this.mRunnable;
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}
