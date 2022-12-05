package com.android.settings.deviceinfo;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.PhoneNumberPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.DeviceInfoUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class PhoneNumberPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final long DELAY_MILLIS = 500;
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PREFERENCE_CATEGORY = "basic_info_category";
    private static final String TAG = "PhoneNumberPreferenceController";
    private int mPhoneCount;
    private final TelephonyManager mTelephonyManager;
    private final List<Preference> mPreferenceList = new ArrayList();
    private HashMap<Integer, ImsConnector> mImsConnectorMap = new HashMap<>();
    private final ImsMmTelManager.RegistrationCallback mImsRegistrationCallback = new AnonymousClass1();
    private final SubscriptionManager mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
    private Handler mHandler = new Handler(Looper.getMainLooper());

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

    @Override // com.android.settings.slices.Sliceable
    public boolean useDynamicSliceSummary() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.settings.deviceinfo.PhoneNumberPreferenceController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ImsMmTelManager.RegistrationCallback {
        AnonymousClass1() {
        }

        public void onRegistered(int i) {
            String str = PhoneNumberPreferenceController.TAG;
            Log.d(str, "onRegistered: imsTransportType=" + i);
            if (PhoneNumberPreferenceController.this.mHandler.hasMessagesOrCallbacks()) {
                Log.d(PhoneNumberPreferenceController.TAG, "onRegistered: optimize to remove unhandled runnables");
                PhoneNumberPreferenceController.this.mHandler.removeCallbacksAndMessages(null);
            }
            PhoneNumberPreferenceController.this.mHandler.postDelayed(new Runnable() { // from class: com.android.settings.deviceinfo.PhoneNumberPreferenceController$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneNumberPreferenceController.AnonymousClass1.this.lambda$onRegistered$0();
                }
            }, PhoneNumberPreferenceController.DELAY_MILLIS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRegistered$0() {
            PhoneNumberPreferenceController.this.updateState(null);
        }
    }

    public PhoneNumberPreferenceController(Context context, String str) {
        super(context, str);
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        this.mPhoneCount = telephonyManager.getPhoneCount();
        initImsConnectors();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mTelephonyManager.isVoiceCapable() ? 0 : 3;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        return getFirstPhoneNumber();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
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

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        for (int i = 0; i < this.mPreferenceList.size(); i++) {
            Preference preference2 = this.mPreferenceList.get(i);
            preference2.setTitle(getPreferenceTitle(i));
            preference2.setSummary(getPhoneNumber(i));
        }
    }

    @Override // com.android.settings.slices.Sliceable
    public void copy() {
        ((ClipboardManager) this.mContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", getFirstPhoneNumber()));
        Context context = this.mContext;
        Toast.makeText(this.mContext, context.getString(R.string.copyable_slice_toast, context.getText(R.string.status_number)), 0).show();
    }

    private CharSequence getFirstPhoneNumber() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.isEmpty()) {
            return this.mContext.getText(R.string.device_info_default);
        }
        return getFormattedPhoneNumber(activeSubscriptionInfoList.get(0));
    }

    private CharSequence getPhoneNumber(int i) {
        SubscriptionInfo subscriptionInfo = getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            return this.mContext.getText(R.string.device_info_default);
        }
        return getFormattedPhoneNumber(subscriptionInfo);
    }

    private CharSequence getPreferenceTitle(int i) {
        if (this.mTelephonyManager.getPhoneCount() > 1) {
            return this.mContext.getString(R.string.status_number_sim_slot, Integer.valueOf(i + 1));
        }
        return this.mContext.getString(R.string.status_number);
    }

    SubscriptionInfo getSubscriptionInfo(int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (subscriptionInfo.getSimSlotIndex() == i) {
                    return subscriptionInfo;
                }
            }
            return null;
        }
        return null;
    }

    CharSequence getFormattedPhoneNumber(SubscriptionInfo subscriptionInfo) {
        String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, subscriptionInfo);
        return TextUtils.isEmpty(bidiFormattedPhoneNumber) ? this.mContext.getString(R.string.device_info_default) : bidiFormattedPhoneNumber;
    }

    Preference createNewPreference(Context context) {
        return new Preference(context);
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
        this.mHandler.removeCallbacksAndMessages(null);
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

    protected void finalize() throws Throwable {
        this.mImsConnectorMap.clear();
        super.finalize();
    }
}
