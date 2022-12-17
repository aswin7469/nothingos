package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.sysprop.TelephonyProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.widget.SettingsMainSwitchPreference;
import java.util.Iterator;

public class MobileNetworkSwitchController extends BasePreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver {
    private static final String TAG = "MobileNetworkSwitchCtrl";
    private boolean isReceiverRegistered = false;
    /* access modifiers changed from: private */
    public int mCallState;
    private SubscriptionsChangeListener mChangeListener;
    private Context mContext;
    private final BroadcastReceiver mIntentReceiver;
    /* access modifiers changed from: private */
    public int mSubId;
    private SubscriptionInfo mSubInfo = null;
    private SubscriptionManager mSubscriptionManager;
    private SettingsMainSwitchPreference mSwitchBar;
    /* access modifiers changed from: private */
    public TelephonyManager mTelephonyManager;

    public int getAvailabilityStatus() {
        return 1;
    }

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

    public MobileNetworkSwitchController(Context context, String str) {
        super(context, str);
        C11081 r3 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                    MobileNetworkSwitchController mobileNetworkSwitchController = MobileNetworkSwitchController.this;
                    mobileNetworkSwitchController.mCallState = mobileNetworkSwitchController.mTelephonyManager.getCallState();
                    Log.d(MobileNetworkSwitchController.TAG, "onReceive: mCallState= " + MobileNetworkSwitchController.this.mCallState + ", mSubId=" + MobileNetworkSwitchController.this.mSubId);
                    MobileNetworkSwitchController.this.update();
                }
            }
        };
        this.mIntentReceiver = r3;
        this.mContext = context;
        this.mSubId = -1;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.mContext.registerReceiver(r3, intentFilter);
        this.isReceiverRegistered = true;
        this.mCallState = this.mTelephonyManager.getCallState();
    }

    /* access modifiers changed from: package-private */
    public void init(int i) {
        this.mSubId = i;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (this.isReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mIntentReceiver);
            this.isReceiverRegistered = false;
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(this.mPreferenceKey);
        this.mSwitchBar = settingsMainSwitchPreference;
        settingsMainSwitchPreference.setOnBeforeCheckedChangeListener(new MobileNetworkSwitchController$$ExternalSyntheticLambda0(this));
        update();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Switch switchR, boolean z) {
        SubscriptionManager.getSlotIndex(this.mSubId);
        Log.d(TAG, "displayPreference: mSubId=" + this.mSubId + ", mSubInfo=" + this.mSubInfo);
        if (this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId) == z) {
            return false;
        }
        SubscriptionUtil.startToggleSubscriptionDialogActivity(this.mContext, this.mSubId, z);
        return true;
    }

    /* access modifiers changed from: private */
    public void update() {
        if (this.mSwitchBar != null) {
            if (this.mTelephonyManager.getActiveModemCount() != 1 || this.mSubscriptionManager.canDisablePhysicalSubscription()) {
                Iterator<SubscriptionInfo> it = SubscriptionUtil.getAvailableSubscriptions(this.mContext).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SubscriptionInfo next = it.next();
                    if (next.getSubscriptionId() == this.mSubId) {
                        this.mSubInfo = next;
                        break;
                    }
                }
                boolean emergencyCallbackMode = this.mTelephonyManager.getEmergencyCallbackMode();
                boolean booleanValue = ((Boolean) TelephonyProperties.in_scbm().orElse(Boolean.FALSE)).booleanValue();
                if (this.mCallState != 0 || emergencyCallbackMode || booleanValue) {
                    Log.d(TAG, "update: disable switchbar, isEcbmEnabled=" + emergencyCallbackMode + ", isScbmEnabled=" + booleanValue + ", mCallState=" + this.mCallState);
                    this.mSwitchBar.setSwitchBarEnabled(false);
                } else {
                    this.mSwitchBar.setSwitchBarEnabled(true);
                }
                if (this.mSubInfo == null) {
                    this.mSwitchBar.hide();
                    return;
                }
                this.mSwitchBar.show();
                boolean isActiveSubscriptionId = this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId);
                boolean areUiccApplicationsEnabled = this.mSubInfo.areUiccApplicationsEnabled();
                Log.d(TAG, "update(): mSubId=" + this.mSubId + ", isSubActive=" + isActiveSubscriptionId + ", uiccAppsEnabled=" + areUiccApplicationsEnabled);
                if (isActiveSubscriptionId == areUiccApplicationsEnabled) {
                    this.mSwitchBar.setCheckedInternal(isActiveSubscriptionId);
                    return;
                }
                return;
            }
            Log.d(TAG, "update: Hide SIM option for 1.4 HAL in single sim");
            this.mSwitchBar.hide();
        }
    }

    public void onSubscriptionsChanged() {
        update();
    }
}
