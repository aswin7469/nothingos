package com.android.settings.network.telephony.gsm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.DomesticRoamUtils;
import com.android.settings.network.telephony.Enhanced4gBasePreferenceController;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.TelephonyBasePreferenceController;
import com.android.settings.network.telephony.gsm.AutoSelectPreferenceController;

public class OpenNetworkSelectPagePreferenceController extends TelephonyBasePreferenceController implements AutoSelectPreferenceController.OnNetworkSelectModeListener, Enhanced4gBasePreferenceController.On4gLteUpdateListener, LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    private int mCacheOfModeStatus = 0;
    private Preference mPreference;
    private PreferenceScreen mPreferenceScreen;
    private SubscriptionsChangeListener mSubscriptionsListener;
    private TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public OpenNetworkSelectPagePreferenceController(Context context, String str) {
        super(context, str);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubId = -1;
        AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(context.getMainExecutor());
        this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
        allowedNetworkTypesListener.setAllowedNetworkTypesListener(new C1125xcfe1fbf5(this));
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
    }

    public void on4gLteUpdated() {
        updateState(this.mPreference);
    }

    /* access modifiers changed from: private */
    /* renamed from: updatePreference */
    public void lambda$new$0() {
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null) {
            displayPreference(preferenceScreen);
        }
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    public int getAvailabilityStatus(int i) {
        return MobileNetworkUtils.shouldDisplayNetworkSelectOptions(this.mContext, i) ? 0 : 2;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mAllowedNetworkTypesListener.register(this.mContext, this.mSubId);
        this.mSubscriptionsListener.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mAllowedNetworkTypesListener.unregister(this.mContext, this.mSubId);
        this.mSubscriptionsListener.stop();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean z = false;
        if (this.mTelephonyManager.getPhoneType() == 2) {
            preference.setEnabled(false);
        } else {
            if (this.mCacheOfModeStatus != 1) {
                z = true;
            }
            preference.setEnabled(z);
        }
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$NetworkSelectActivity");
        intent.putExtra("android.provider.extra.SUB_ID", this.mSubId);
        preference.setIntent(intent);
    }

    public CharSequence getSummary() {
        String registeredOperatorName;
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        if (serviceState == null || serviceState.getState() != 0) {
            return this.mContext.getString(R$string.network_disconnected);
        }
        if (!DomesticRoamUtils.isFeatureEnabled(this.mContext) || "" == (registeredOperatorName = DomesticRoamUtils.getRegisteredOperatorName(this.mContext, this.mSubId))) {
            return MobileNetworkUtils.getCurrentCarrierNameForDisplay(this.mContext, this.mSubId);
        }
        return registeredOperatorName;
    }

    public OpenNetworkSelectPagePreferenceController init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        return this;
    }

    public void onNetworkSelectModeUpdated(int i) {
        this.mCacheOfModeStatus = i;
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    public void onSubscriptionsChanged() {
        updateState(this.mPreference);
    }
}
