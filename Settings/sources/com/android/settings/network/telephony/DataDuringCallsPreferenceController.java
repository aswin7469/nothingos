package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.datausage.DataUsageUtils;
import com.android.settings.network.MobileDataContentObserver;
import com.android.settings.network.SubscriptionsChangeListener;

public class DataDuringCallsPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final String TAG = "DataDuringCalls";
    private SubscriptionsChangeListener mChangeListener;
    private final BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (DataDuringCallsPreferenceController.this.mPreference != null) {
                Log.d(DataDuringCallsPreferenceController.TAG, "DDS is changed");
                DataDuringCallsPreferenceController.this.lambda$onResume$0();
            }
        }
    };
    private TelephonyManager mManager;
    private MobileDataContentObserver mMobileDataContentObserver;
    /* access modifiers changed from: private */
    public SwitchPreference mPreference;
    private PreferenceScreen mScreen;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DataDuringCallsPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: package-private */
    public void init(int i) {
        this.mSubId = i;
        this.mManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mChangeListener == null) {
            this.mChangeListener = new SubscriptionsChangeListener(this.mContext, this);
        }
        this.mChangeListener.start();
        if (this.mMobileDataContentObserver == null) {
            MobileDataContentObserver mobileDataContentObserver = new MobileDataContentObserver(new Handler(Looper.getMainLooper()));
            this.mMobileDataContentObserver = mobileDataContentObserver;
            mobileDataContentObserver.setOnMobileDataChangedListener(new DataDuringCallsPreferenceController$$ExternalSyntheticLambda0(this));
        }
        this.mMobileDataContentObserver.register(this.mContext, this.mSubId);
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (defaultDataSubscriptionId != this.mSubId) {
            this.mMobileDataContentObserver.register(this.mContext, defaultDataSubscriptionId);
        }
        this.mContext.registerReceiver(this.mDefaultDataChangedReceiver, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        SubscriptionsChangeListener subscriptionsChangeListener = this.mChangeListener;
        if (subscriptionsChangeListener != null) {
            subscriptionsChangeListener.stop();
        }
        MobileDataContentObserver mobileDataContentObserver = this.mMobileDataContentObserver;
        if (mobileDataContentObserver != null) {
            mobileDataContentObserver.unRegister(this.mContext);
        }
        BroadcastReceiver broadcastReceiver = this.mDefaultDataChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mScreen = preferenceScreen;
    }

    public boolean isChecked() {
        TelephonyManager telephonyManager = this.mManager;
        if (telephonyManager == null) {
            return false;
        }
        return telephonyManager.isMobileDataPolicyEnabled(1);
    }

    public boolean setChecked(boolean z) {
        this.mManager.setMobileDataPolicyEnabled(1, z);
        return true;
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public boolean hasMobileData() {
        return DataUsageUtils.hasMobileData(this.mContext);
    }

    public int getAvailabilityStatus(int i) {
        TelephonyManager telephonyManager;
        if (!SubscriptionManager.isValidSubscriptionId(i) || SubscriptionManager.getDefaultDataSubscriptionId() == i || !hasMobileData() || (telephonyManager = this.mManager) == null || !telephonyManager.createForSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId()).isDataEnabled()) {
            return 2;
        }
        if (!TelephonyUtils.isSubsidyFeatureEnabled(this.mContext) || TelephonyUtils.isSubsidySimCard(this.mContext, SubscriptionManager.getSlotIndex(this.mSubId))) {
            return 0;
        }
        return 2;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setVisible(isAvailable());
        }
    }

    public void onSubscriptionsChanged() {
        updateState(this.mPreference);
    }

    @VisibleForTesting
    /* renamed from: refreshPreference */
    public void lambda$onResume$0() {
        PreferenceScreen preferenceScreen = this.mScreen;
        if (preferenceScreen != null) {
            super.displayPreference(preferenceScreen);
        }
    }
}
