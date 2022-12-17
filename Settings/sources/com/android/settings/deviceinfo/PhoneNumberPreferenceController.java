package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.DeviceInfoUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhoneNumberPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final long DELAY_MILLIS = 500;
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PREFERENCE_CATEGORY = "basic_info_category";
    /* access modifiers changed from: private */
    public static final String TAG = "PhoneNumberPreferenceController";
    /* access modifiers changed from: private */
    public Handler mHandler;
    private HashMap<Integer, ImsConnector> mImsConnectorMap = new HashMap<>();
    private final ImsMmTelManager.RegistrationCallback mImsRegistrationCallback = new ImsMmTelManager.RegistrationCallback() {
        public void onRegistered(int i) {
            String r0 = PhoneNumberPreferenceController.TAG;
            Log.d(r0, "onRegistered: imsTransportType=" + i);
            if (PhoneNumberPreferenceController.this.mHandler.hasMessagesOrCallbacks()) {
                Log.d(PhoneNumberPreferenceController.TAG, "onRegistered: optimize to remove unhandled runnables");
                PhoneNumberPreferenceController.this.mHandler.removeCallbacksAndMessages((Object) null);
            }
            PhoneNumberPreferenceController.this.mHandler.postDelayed(new PhoneNumberPreferenceController$1$$ExternalSyntheticLambda0(this), PhoneNumberPreferenceController.DELAY_MILLIS);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRegistered$0() {
            PhoneNumberPreferenceController.this.updateState((Preference) null);
        }
    };
    private int mPhoneCount;
    private final List<Preference> mPreferenceList = new ArrayList();
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;

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

    public boolean useDynamicSliceSummary() {
        return true;
    }

    public PhoneNumberPreferenceController(Context context, String str) {
        super(context, str);
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        this.mPhoneCount = telephonyManager.getPhoneCount();
        this.mHandler = new Handler(Looper.getMainLooper());
        initImsConnectors();
    }

    public int getAvailabilityStatus() {
        return this.mTelephonyManager.isVoiceCapable() ? 0 : 3;
    }

    public CharSequence getSummary() {
        return getFirstPhoneNumber();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY);
        this.mPreferenceList.add(findPreference);
        int order = findPreference.getOrder();
        for (int i = 1; i < this.mTelephonyManager.getPhoneCount(); i++) {
            Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
            createNewPreference.setOrder(order + i);
            createNewPreference.setKey(KEY_PHONE_NUMBER + i);
            createNewPreference.setSelectable(false);
            preferenceCategory.addPreference(createNewPreference);
            this.mPreferenceList.add(createNewPreference);
        }
    }

    public void updateState(Preference preference) {
        for (int i = 0; i < this.mPreferenceList.size(); i++) {
            Preference preference2 = this.mPreferenceList.get(i);
            preference2.setTitle(getPreferenceTitle(i));
            preference2.setSummary(getPhoneNumber(i));
        }
    }

    private CharSequence getFirstPhoneNumber() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.isEmpty()) {
            return this.mContext.getText(R$string.device_info_default);
        }
        return getFormattedPhoneNumber(activeSubscriptionInfoList.get(0));
    }

    private CharSequence getPhoneNumber(int i) {
        SubscriptionInfo subscriptionInfo = getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            return this.mContext.getText(R$string.device_info_default);
        }
        return getFormattedPhoneNumber(subscriptionInfo);
    }

    private CharSequence getPreferenceTitle(int i) {
        if (this.mTelephonyManager.getPhoneCount() <= 1) {
            return this.mContext.getString(R$string.status_number);
        }
        return this.mContext.getString(R$string.status_number_sim_slot, new Object[]{Integer.valueOf(i + 1)});
    }

    /* access modifiers changed from: protected */
    public SubscriptionInfo getSubscriptionInfo(int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return null;
        }
        for (SubscriptionInfo next : activeSubscriptionInfoList) {
            if (next.getSimSlotIndex() == i) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public CharSequence getFormattedPhoneNumber(SubscriptionInfo subscriptionInfo) {
        String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, subscriptionInfo);
        return TextUtils.isEmpty(bidiFormattedPhoneNumber) ? this.mContext.getString(R$string.device_info_default) : bidiFormattedPhoneNumber;
    }

    /* access modifiers changed from: protected */
    public Preference createNewPreference(Context context) {
        return new PhoneNumberSummaryPreference(context);
    }

    public void init(Lifecycle lifecycle) {
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        } else {
            Log.e(TAG, "init: lifecycle is null, invalid param");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        connect();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        disconnect();
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    private void initImsConnectors() {
        for (int i = 0; i < this.mPhoneCount; i++) {
            this.mImsConnectorMap.put(Integer.valueOf(i), new ImsConnector(this.mContext, i, this.mImsRegistrationCallback));
        }
    }

    private void connect() {
        if (this.mImsConnectorMap.isEmpty()) {
            Log.e(TAG, "connect: need init ims connectors");
            return;
        }
        int size = this.mImsConnectorMap.size();
        for (int i = 0; i < size; i++) {
            ImsConnector imsConnector = this.mImsConnectorMap.get(Integer.valueOf(i));
            if (imsConnector != null) {
                imsConnector.connect();
            }
        }
    }

    private void disconnect() {
        if (this.mImsConnectorMap.isEmpty()) {
            Log.d(TAG, "disconnect: need do nothing");
            return;
        }
        int size = this.mImsConnectorMap.size();
        for (int i = 0; i < size; i++) {
            ImsConnector imsConnector = this.mImsConnectorMap.get(Integer.valueOf(i));
            if (imsConnector != null) {
                imsConnector.disconnect();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        this.mImsConnectorMap.clear();
        super.finalize();
    }
}
