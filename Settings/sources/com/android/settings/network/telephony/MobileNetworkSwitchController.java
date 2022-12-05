package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
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
import com.android.internal.telephony.ITelephony;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.WirelessUtils;
import java.util.Iterator;
/* loaded from: classes.dex */
public class MobileNetworkSwitchController extends BasePreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver {
    private static final int EVENT_SIM_MODE_PROCESS_SUCCESS_DONE = 3;
    private static final String TAG = "MobileNetworkSwitchCtrl";
    private static boolean mSetRadioInprogress = false;
    private boolean isReceiverRegistered;
    private int mCallState;
    private SubscriptionsChangeListener mChangeListener;
    private Context mContext;
    private final BroadcastReceiver mIntentReceiver;
    private SubscriptionManager mSubscriptionManager;
    private SettingsMainSwitchPreference mSwitchBar;
    private TelephonyManager mTelephonyManager;
    private SubscriptionInfo mSubInfo = null;
    private boolean mChecked = false;
    private final Handler mHandler = new Handler() { // from class: com.android.settings.network.telephony.MobileNetworkSwitchController.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 3) {
                return;
            }
            int i = message.arg1;
            boolean z = true;
            if (message.arg2 != 1) {
                z = false;
            }
            Log.d(MobileNetworkSwitchController.TAG, "onReceive: subid= " + i + ", radioOn=" + z + ", previousDataEnable=" + ((Boolean) message.obj).booleanValue());
            boolean unused = MobileNetworkSwitchController.mSetRadioInprogress = false;
            MobileNetworkSwitchController.this.update();
        }
    };
    private int mSubId = -1;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 1;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MobileNetworkSwitchController(Context context, String str) {
        super(context, str);
        this.isReceiverRegistered = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.MobileNetworkSwitchController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                    MobileNetworkSwitchController mobileNetworkSwitchController = MobileNetworkSwitchController.this;
                    mobileNetworkSwitchController.mCallState = mobileNetworkSwitchController.mTelephonyManager.getCallState();
                    Log.d(MobileNetworkSwitchController.TAG, "onReceive: mCallState= " + MobileNetworkSwitchController.this.mCallState + ", mSubId=" + MobileNetworkSwitchController.this.mSubId);
                    MobileNetworkSwitchController.this.update();
                }
            }
        };
        this.mIntentReceiver = broadcastReceiver;
        this.mContext = context;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter);
        this.isReceiverRegistered = true;
        this.mCallState = this.mTelephonyManager.getCallState();
    }

    public void init(Lifecycle lifecycle, int i) {
        lifecycle.addObserver(this);
        this.mSubId = i;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        this.mChecked = this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId);
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

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(this.mPreferenceKey);
        this.mSwitchBar = settingsMainSwitchPreference;
        settingsMainSwitchPreference.setTitle(this.mContext.getString(R.string.mobile_network_use_sim_on));
        this.mSwitchBar.setOnBeforeCheckedChangeListener(new SettingsMainSwitchBar.OnBeforeCheckedChangeListener() { // from class: com.android.settings.network.telephony.MobileNetworkSwitchController$$ExternalSyntheticLambda0
            @Override // com.android.settings.widget.SettingsMainSwitchBar.OnBeforeCheckedChangeListener
            public final boolean onBeforeCheckedChanged(Switch r1, boolean z) {
                boolean lambda$displayPreference$0;
                lambda$displayPreference$0 = MobileNetworkSwitchController.this.lambda$displayPreference$0(r1, z);
                return lambda$displayPreference$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Switch r2, boolean z) {
        Log.d(TAG, "displayPreference: mSubId=" + this.mSubId + ", mSubInfo=" + this.mSubInfo);
        this.mChecked = z;
        setRadioPower();
        return true;
    }

    private void setRadioPower() {
        mSetRadioInprogress = true;
        this.mSwitchBar.setSwitchBarEnabled(false);
        if (this.mSubInfo != null) {
            Log.d(TAG, "setRadioPower, mChecked: " + this.mChecked + ", subId = " + this.mSubInfo.getSubscriptionId());
            new Thread(new Runnable() { // from class: com.android.settings.network.telephony.MobileNetworkSwitchController.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ITelephony asInterface = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
                        if (asInterface != null) {
                            if (asInterface.setRadioForSubscriber(MobileNetworkSwitchController.this.mSubInfo.getSubscriptionId(), MobileNetworkSwitchController.this.mChecked)) {
                                MobileNetworkSwitchController.this.mHandler.sendMessageDelayed(MobileNetworkSwitchController.this.mHandler.obtainMessage(3, MobileNetworkSwitchController.this.mSubInfo.getSubscriptionId(), MobileNetworkSwitchController.this.mChecked ? 1 : 0, Boolean.valueOf(MobileNetworkSwitchController.this.mTelephonyManager.getDataEnabled())), 1000L);
                            }
                        } else {
                            Log.d(MobileNetworkSwitchController.TAG, "telephony is null");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return;
        }
        Log.i(TAG, "setRadioPower fail");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        if (this.mSwitchBar == null) {
            return;
        }
        boolean isAirplaneModeOn = WirelessUtils.isAirplaneModeOn(this.mContext);
        Log.d(TAG, "update: isAirplaneModeOn = " + isAirplaneModeOn + " mSetRadioInprogress = " + mSetRadioInprogress);
        if (isAirplaneModeOn) {
            this.mSwitchBar.setSwitchBarEnabled(false);
        } else {
            this.mSwitchBar.setSwitchBarEnabled(!mSetRadioInprogress);
        }
        if (this.mTelephonyManager.getActiveModemCount() == 1 && !this.mSubscriptionManager.canDisablePhysicalSubscription()) {
            Log.d(TAG, "update: Hide SIM option for 1.4 HAL in single sim");
            this.mSwitchBar.hide();
            return;
        }
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
        boolean booleanValue = ((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue();
        if (this.mCallState != 0 || booleanValue) {
            Log.d(TAG, "update: disable switchbar, isEcbmEnabled=" + booleanValue + ", mCallState=" + this.mCallState);
            this.mSwitchBar.setSwitchBarEnabled(false);
        } else {
            this.mSwitchBar.setSwitchBarEnabled(true);
        }
        if (this.mSubInfo == null) {
            this.mSwitchBar.hide();
            return;
        }
        this.mSwitchBar.show();
        Log.d(TAG, "update(): mSubId=" + this.mSubId + ", isActiveSubscriptionId=" + this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId));
        this.mSwitchBar.setCheckedInternal(this.mChecked);
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }
}
