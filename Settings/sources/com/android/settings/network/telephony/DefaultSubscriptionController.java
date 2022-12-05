package com.android.settings.network.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.sysprop.TelephonyProperties;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public abstract class DefaultSubscriptionController extends TelephonyBasePreferenceController implements LifecycleObserver, Preference.OnPreferenceChangeListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final String EMERGENCY_ACCOUNT_HANDLE_ID = "E";
    private static final String LIST_DATA_PREFERENCE_KEY = "data_preference";
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static final String TAG = "DefaultSubController";
    private int[] mCallState;
    protected SubscriptionsChangeListener mChangeListener;
    private boolean mIsRtlMode;
    protected SubscriptionManager mManager;
    private int mPhoneCount;
    private PhoneStateListener[] mPhoneStateListener;
    protected ListPreference mPreference;
    private ArrayList<SubscriptionInfo> mSelectableSubs;
    protected TelecomManager mTelecomManager;
    protected TelephonyManager mTelephonyManager;

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    protected abstract int getDefaultSubscriptionId();

    protected abstract SubscriptionInfo getDefaultSubscriptionInfo();

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    protected boolean isAskEverytimeSupported() {
        return true;
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    protected abstract void setDefaultSubscription(int i);

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DefaultSubscriptionController(Context context, String str) {
        super(context, str);
        this.mManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        this.mIsRtlMode = context.getResources().getConfiguration().getLayoutDirection() != 1 ? false : true;
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        int phoneCount = telephonyManager.getPhoneCount();
        this.mPhoneCount = phoneCount;
        this.mPhoneStateListener = new PhoneStateListener[phoneCount];
        this.mCallState = new int[phoneCount];
        this.mSelectableSubs = new ArrayList<>();
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        ArrayList<SubscriptionInfo> arrayList = this.mSelectableSubs;
        boolean z = true;
        if (arrayList == null || arrayList.size() <= 1) {
            z = false;
        }
        return z ? 0 : 2;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        registerPhoneStateListener();
        updateEntries();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
        unRegisterPhoneStateListener();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
        updateEntries();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        PhoneAccountHandle defaultCallingAccountHandle = getDefaultCallingAccountHandle();
        if (defaultCallingAccountHandle != null && !isCallingAccountBindToSubscription(defaultCallingAccountHandle)) {
            return getLabelFromCallingAccount(defaultCallingAccountHandle);
        }
        SubscriptionInfo defaultSubscriptionInfo = getDefaultSubscriptionInfo();
        if (defaultSubscriptionInfo != null) {
            return MobileNetworkUtils.getUniqueSubscriptionDisplayName(defaultSubscriptionInfo, this.mContext);
        }
        return isAskEverytimeSupported() ? this.mContext.getString(R.string.calls_and_sms_ask_every_time) : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEntries() {
        if (this.mPreference == null) {
            return;
        }
        updateSubStatus();
        if (this.mSelectableSubs.isEmpty()) {
            Log.d(TAG, "updateEntries: mSelectable subs is empty");
        } else if (!isAvailable()) {
            this.mPreference.setVisible(false);
        } else {
            boolean z = true;
            this.mPreference.setVisible(true);
            this.mPreference.setOnPreferenceChangeListener(this);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (Utils.isProviderModelEnabled(this.mContext) && this.mSelectableSubs.size() == 1) {
                this.mPreference.setEnabled(false);
                this.mPreference.setSummary(MobileNetworkUtils.getUniqueSubscriptionDisplayName(this.mSelectableSubs.get(0), this.mContext));
                return;
            }
            int defaultSubscriptionId = getDefaultSubscriptionId();
            Iterator<SubscriptionInfo> it = this.mSelectableSubs.iterator();
            boolean z2 = false;
            while (it.hasNext()) {
                SubscriptionInfo next = it.next();
                if (!next.isOpportunistic()) {
                    arrayList.add(MobileNetworkUtils.getUniqueSubscriptionDisplayName(next, this.mContext));
                    int subscriptionId = next.getSubscriptionId();
                    arrayList2.add(Integer.toString(subscriptionId));
                    if (subscriptionId == defaultSubscriptionId) {
                        z2 = true;
                    }
                }
            }
            if (TextUtils.equals(getPreferenceKey(), LIST_DATA_PREFERENCE_KEY)) {
                boolean booleanValue = ((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue();
                if (Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dds_switch", 0) == 0) {
                    ListPreference listPreference = this.mPreference;
                    if (!isCallStateIdle() || booleanValue || (TelephonyUtils.isSubsidyFeatureEnabled(this.mContext) && !TelephonyUtils.allowUsertoSetDDS(this.mContext))) {
                        z = false;
                    }
                    listPreference.setEnabled(z);
                } else {
                    this.mPreference.setEnabled(false);
                    this.mPreference.setSummary("Smart DDS switch is on");
                }
            } else if (isAskEverytimeSupported()) {
                arrayList.add(this.mContext.getString(R.string.calls_and_sms_ask_every_time));
                arrayList2.add(Integer.toString(-1));
            }
            this.mPreference.setEntries((CharSequence[]) arrayList.toArray(new CharSequence[0]));
            this.mPreference.setEntryValues((CharSequence[]) arrayList2.toArray(new CharSequence[0]));
            if (z2) {
                this.mPreference.setValue(Integer.toString(defaultSubscriptionId));
            } else {
                this.mPreference.setValue(Integer.toString(-1));
            }
        }
    }

    public PhoneAccountHandle getDefaultCallingAccountHandle() {
        PhoneAccountHandle userSelectedOutgoingPhoneAccount = getTelecomManager().getUserSelectedOutgoingPhoneAccount();
        if (userSelectedOutgoingPhoneAccount == null) {
            return null;
        }
        List<PhoneAccountHandle> callCapablePhoneAccounts = getTelecomManager().getCallCapablePhoneAccounts(false);
        if (userSelectedOutgoingPhoneAccount.equals(new PhoneAccountHandle(PSTN_CONNECTION_SERVICE_COMPONENT, EMERGENCY_ACCOUNT_HANDLE_ID))) {
            return null;
        }
        for (PhoneAccountHandle phoneAccountHandle : callCapablePhoneAccounts) {
            if (userSelectedOutgoingPhoneAccount.equals(phoneAccountHandle)) {
                return userSelectedOutgoingPhoneAccount;
            }
        }
        return null;
    }

    @VisibleForTesting
    TelecomManager getTelecomManager() {
        if (this.mTelecomManager == null) {
            this.mTelecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        }
        return this.mTelecomManager;
    }

    @VisibleForTesting
    PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) {
        return getTelecomManager().getPhoneAccount(phoneAccountHandle);
    }

    public boolean isCallingAccountBindToSubscription(PhoneAccountHandle phoneAccountHandle) {
        PhoneAccount phoneAccount = getPhoneAccount(phoneAccountHandle);
        if (phoneAccount == null) {
            return false;
        }
        return phoneAccount.hasCapabilities(4);
    }

    public CharSequence getLabelFromCallingAccount(PhoneAccountHandle phoneAccountHandle) {
        PhoneAccount phoneAccount = getPhoneAccount(phoneAccountHandle);
        CharSequence label = phoneAccount != null ? phoneAccount.getLabel() : null;
        if (label != null) {
            label = this.mContext.getPackageManager().getUserBadgedLabel(label, phoneAccountHandle.getUserHandle());
        }
        return label != null ? label : "";
    }

    private boolean isCallStateIdle() {
        boolean z = true;
        for (int i = 0; i < this.mPhoneCount; i++) {
            if (this.mCallState[i] != 0) {
                z = false;
            }
        }
        return z;
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        setDefaultSubscription(Integer.parseInt((String) obj));
        refreshSummary(this.mPreference);
        return true;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        ArrayList<SubscriptionInfo> arrayList = this.mSelectableSubs;
        if (arrayList != null) {
            arrayList.clear();
        }
        updateSubStatus();
        if (this.mPreference != null) {
            updateEntries();
            refreshSummary(this.mPreference);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRtlMode() {
        return this.mIsRtlMode;
    }

    private void registerPhoneStateListener() {
        updateSubStatus();
        for (int i = 0; i < this.mSelectableSubs.size(); i++) {
            this.mTelephonyManager.createForSubscriptionId(this.mSelectableSubs.get(i).getSubscriptionId()).listen(getPhoneStateListener(i), 32);
        }
    }

    private void unRegisterPhoneStateListener() {
        for (int i = 0; i < this.mPhoneCount; i++) {
            PhoneStateListener[] phoneStateListenerArr = this.mPhoneStateListener;
            if (phoneStateListenerArr[i] != null) {
                this.mTelephonyManager.listen(phoneStateListenerArr[i], 0);
                this.mPhoneStateListener[i] = null;
            }
        }
    }

    private PhoneStateListener getPhoneStateListener(final int i) {
        this.mPhoneStateListener[i] = new PhoneStateListener() { // from class: com.android.settings.network.telephony.DefaultSubscriptionController.1
            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int i2, String str) {
                DefaultSubscriptionController.this.mCallState[i] = i2;
                DefaultSubscriptionController.this.updateEntries();
            }
        };
        return this.mPhoneStateListener[i];
    }

    private void updateSubStatus() {
        if (!this.mSelectableSubs.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.mPhoneCount; i++) {
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = this.mManager.getActiveSubscriptionInfoForSimSlotIndex(i);
            if (activeSubscriptionInfoForSimSlotIndex != null) {
                this.mSelectableSubs.add(activeSubscriptionInfoForSimSlotIndex);
            }
        }
    }
}
