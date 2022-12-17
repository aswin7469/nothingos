package com.nothing.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.PersistableBundle;
import android.os.ServiceManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.TelephonyTogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Objects;
import org.codeaurora.internal.IExtTelephony;

public class SAPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String HIDE_5G_STANDALONE = "hide_5g_standalone_bool";
    private static final String LOG_5G_Event_5G_SA = "5g_sa";
    private static final String TAG = "SAPreferenceController";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    Integer mCallState;
    private final BroadcastReceiver mDefaultDataChangedReceiver;
    private IExtTelephony mExtTelephony = IExtTelephony.Stub.asInterface(ServiceManager.getService("qti.radio.extphone"));
    private PhoneTelephonyCallback mPhoneTelephonyCallback;
    Preference mPreference;
    private boolean mReceiverRegistered = false;
    private int mSlotId;
    private TelephonyManager mTelephonyManager;

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

    public SAPreferenceController(Context context, String str) {
        super(context, str);
        C20401 r4 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (SAPreferenceController.this.mPreference != null) {
                    Log.d(SAPreferenceController.TAG, "DDS is changed");
                    SAPreferenceController sAPreferenceController = SAPreferenceController.this;
                    sAPreferenceController.updateState(sAPreferenceController.mPreference);
                }
            }
        };
        this.mDefaultDataChangedReceiver = r4;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (!this.mReceiverRegistered) {
            this.mContext.registerReceiver(r4, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
            this.mReceiverRegistered = true;
        }
    }

    public SAPreferenceController init(int i) {
        if (this.mPhoneTelephonyCallback == null) {
            this.mPhoneTelephonyCallback = new PhoneTelephonyCallback();
        }
        if (this.mAllowedNetworkTypesListener == null) {
            AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
            this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
            allowedNetworkTypesListener.setAllowedNetworkTypesListener(new SAPreferenceController$$ExternalSyntheticLambda0(this));
        }
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId) && this.mSubId == i) {
            return this;
        }
        this.mSubId = i;
        this.mSlotId = SubscriptionManager.getPhoneId(i);
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        }
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
        }
        return this;
    }

    /* renamed from: updatePreference */
    public void lambda$init$0() {
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    public int getAvailabilityStatus(int i) {
        int defaultDataSubscriptionId;
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId == null || carrierConfigForSubId.getBoolean(HIDE_5G_STANDALONE) || (defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId()) != i || !SubscriptionManager.isValidSubscriptionId(defaultDataSubscriptionId) || !SubscriptionManager.from(this.mContext).isActiveSubId(defaultDataSubscriptionId)) {
            return 2;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        PhoneTelephonyCallback phoneTelephonyCallback = this.mPhoneTelephonyCallback;
        if (phoneTelephonyCallback != null) {
            phoneTelephonyCallback.register(this.mSubId, this.mTelephonyManager);
        }
        AllowedNetworkTypesListener allowedNetworkTypesListener = this.mAllowedNetworkTypesListener;
        if (allowedNetworkTypesListener != null) {
            allowedNetworkTypesListener.register(this.mContext, this.mSubId);
        }
    }

    public void onStop() {
        if (this.mReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mDefaultDataChangedReceiver);
            this.mReceiverRegistered = false;
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

    public void updateState(Preference preference) {
        super.updateState(preference);
        SwitchPreference switchPreference = (SwitchPreference) preference;
        switchPreference.setVisible(isAvailable());
        switchPreference.setEnabled(isCallStateIdle() && getPreferredNetworkMode() > 22);
    }

    public boolean setChecked(boolean z) {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return false;
        }
        if (!z) {
            set5gModeForSlotId(1);
            MobileNetworkUtils.log5GEvent(this.mContext, LOG_5G_Event_5G_SA, 0);
        } else {
            set5gModeForSlotId(0);
            MobileNetworkUtils.log5GEvent(this.mContext, LOG_5G_Event_5G_SA, 1);
        }
        return true;
    }

    public boolean isChecked() {
        return get5gModeForSlotId() == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isCallStateIdle() {
        Integer num = this.mCallState;
        boolean z = num == null || num.intValue() == 0;
        Log.d(TAG, "isCallStateIdle:" + z);
        return z;
    }

    public class PhoneTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mLocalTelephonyManager;

        private PhoneTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            SAPreferenceController.this.mCallState = Integer.valueOf(i);
            SAPreferenceController sAPreferenceController = SAPreferenceController.this;
            sAPreferenceController.updateState(sAPreferenceController.mPreference);
        }

        public void register(int i, TelephonyManager telephonyManager) {
            this.mLocalTelephonyManager = telephonyManager;
            SAPreferenceController.this.mCallState = Integer.valueOf(telephonyManager.getCallState(i));
            TelephonyManager telephonyManager2 = this.mLocalTelephonyManager;
            Handler uiThreadHandler = ThreadUtils.getUiThreadHandler();
            Objects.requireNonNull(uiThreadHandler);
            telephonyManager2.registerTelephonyCallback(new HandlerExecutor(uiThreadHandler), this);
        }

        public void unregister() {
            SAPreferenceController.this.mCallState = null;
            this.mLocalTelephonyManager.unregisterTelephonyCallback(this);
        }
    }

    private int get5gModeForSlotId() {
        try {
            return this.mExtTelephony.get5GMode(this.mSlotId);
        } catch (Exception e) {
            Log.d(TAG, "Exception @get5gModeForSlotId, " + e);
            return 0;
        }
    }

    private void set5gModeForSlotId(int i) {
        try {
            this.mExtTelephony.set5GMode(i, this.mSlotId);
        } catch (Exception e) {
            Log.d(TAG, "Exception @set5gModeForSlotId, " + e);
        }
    }
}
