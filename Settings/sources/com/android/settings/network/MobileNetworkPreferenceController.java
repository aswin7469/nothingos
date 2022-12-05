package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;
/* loaded from: classes.dex */
public class MobileNetworkPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver {
    static final String KEY_MOBILE_NETWORK_SETTINGS = "mobile_network_settings";
    private final boolean mIsSecondaryUser;
    private Preference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private String mSummary;
    MobileNetworkTelephonyCallback mTelephonyCallback;
    private final TelephonyManager mTelephonyManager;
    private final UserManager mUserManager;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.settings.network.MobileNetworkPreferenceController.2
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            MobileNetworkPreferenceController.this.updateDisplayName();
            MobileNetworkPreferenceController mobileNetworkPreferenceController = MobileNetworkPreferenceController.this;
            mobileNetworkPreferenceController.updateState(mobileNetworkPreferenceController.mPreference);
        }
    };
    private BroadcastReceiver mAirplanModeChangedReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.MobileNetworkPreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            MobileNetworkPreferenceController.this.updateDisplayName();
            MobileNetworkPreferenceController mobileNetworkPreferenceController = MobileNetworkPreferenceController.this;
            mobileNetworkPreferenceController.updateState(mobileNetworkPreferenceController.mPreference);
        }
    };

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_MOBILE_NETWORK_SETTINGS;
    }

    public MobileNetworkPreferenceController(Context context) {
        super(context);
        UserManager userManager = (UserManager) context.getSystemService("user");
        this.mUserManager = userManager;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mIsSecondaryUser = !userManager.isAdminUser();
        this.mSubscriptionManager = SubscriptionManager.from(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return !isUserRestricted() && !Utils.isWifiOnly(this.mContext);
    }

    public boolean isUserRestricted() {
        return this.mIsSecondaryUser || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_config_mobile_networks", UserHandle.myUserId());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    /* loaded from: classes.dex */
    class MobileNetworkTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener {
        MobileNetworkTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            MobileNetworkPreferenceController.this.updateDisplayName();
            MobileNetworkPreferenceController mobileNetworkPreferenceController = MobileNetworkPreferenceController.this;
            mobileNetworkPreferenceController.updateState(mobileNetworkPreferenceController.mPreference);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        SubscriptionManager subscriptionManager = this.mSubscriptionManager;
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
        }
        if (isAvailable()) {
            if (this.mTelephonyCallback == null) {
                this.mTelephonyCallback = new MobileNetworkTelephonyCallback();
            }
            this.mTelephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallback);
        }
        BroadcastReceiver broadcastReceiver = this.mAirplanModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayName() {
        if (this.mPreference != null) {
            List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
            if (activeSubscriptionInfoList != null && !activeSubscriptionInfoList.isEmpty()) {
                boolean z = false;
                StringBuilder sb = new StringBuilder();
                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                    if (isSubscriptionInService(subscriptionInfo.getSubscriptionId())) {
                        if (z) {
                            sb.append(", ");
                        }
                        sb.append(this.mTelephonyManager.getNetworkOperatorName(subscriptionInfo.getSubscriptionId()));
                        z = true;
                    }
                }
                this.mSummary = sb.toString();
                return;
            }
            this.mSummary = this.mTelephonyManager.getNetworkOperatorName();
        }
    }

    private boolean isSubscriptionInService(int i) {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        return telephonyManager != null && telephonyManager.getServiceStateForSubscriber(i).getState() == 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        MobileNetworkTelephonyCallback mobileNetworkTelephonyCallback = this.mTelephonyCallback;
        if (mobileNetworkTelephonyCallback != null) {
            this.mTelephonyManager.unregisterTelephonyCallback(mobileNetworkTelephonyCallback);
        }
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
        BroadcastReceiver broadcastReceiver = this.mAirplanModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (!(preference instanceof RestrictedPreference) || !((RestrictedPreference) preference).isDisabledByAdmin()) {
            boolean z = false;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
                z = true;
            }
            preference.setEnabled(z);
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (KEY_MOBILE_NETWORK_SETTINGS.equals(preference.getKey())) {
            this.mContext.startActivity(new Intent(this.mContext, MobileNetworkActivity.class));
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        return this.mSummary;
    }
}
