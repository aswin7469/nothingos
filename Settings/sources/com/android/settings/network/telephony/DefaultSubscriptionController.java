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
import com.android.settings.R$string;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DefaultSubscriptionController extends TelephonyBasePreferenceController implements LifecycleObserver, Preference.OnPreferenceChangeListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final String EMERGENCY_ACCOUNT_HANDLE_ID = "E";
    private static final String LIST_DATA_PREFERENCE_KEY = "data_preference";
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static final String TAG = "DefaultSubController";
    /* access modifiers changed from: private */
    public int[] mCallState;
    protected SubscriptionsChangeListener mChangeListener;
    private boolean mIsRtlMode;
    protected SubscriptionManager mManager;
    private int mPhoneCount;
    private PhoneStateListener[] mPhoneStateListener;
    protected ListPreference mPreference;
    private ArrayList<SubscriptionInfo> mSelectableSubs;
    protected TelecomManager mTelecomManager;
    protected TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract int getDefaultSubscriptionId();

    /* access modifiers changed from: protected */
    public abstract SubscriptionInfo getDefaultSubscriptionInfo();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    /* access modifiers changed from: protected */
    public boolean isAskEverytimeSupported() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    /* access modifiers changed from: protected */
    public abstract void setDefaultSubscription(int i);

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

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
        updateEntries();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ CharSequence lambda$refreshSummary$0(Preference preference) {
        return getSummary();
    }

    /* access modifiers changed from: protected */
    public void refreshSummary(Preference preference) {
        if (preference != null) {
            preference.setSummaryProvider(new DefaultSubscriptionController$$ExternalSyntheticLambda0(this));
        }
    }

    public CharSequence getSummary() {
        PhoneAccountHandle defaultCallingAccountHandle = getDefaultCallingAccountHandle();
        if (defaultCallingAccountHandle != null && !isCallingAccountBindToSubscription(defaultCallingAccountHandle)) {
            return getLabelFromCallingAccount(defaultCallingAccountHandle);
        }
        SubscriptionInfo defaultSubscriptionInfo = getDefaultSubscriptionInfo();
        if (defaultSubscriptionInfo != null) {
            return SubscriptionUtil.getUniqueSubscriptionDisplayName(defaultSubscriptionInfo, this.mContext);
        }
        return isAskEverytimeSupported() ? this.mContext.getString(R$string.calls_and_sms_ask_every_time) : "";
    }

    /* access modifiers changed from: private */
    public void updateEntries() {
        if (this.mPreference != null) {
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
                if (this.mSelectableSubs.size() == 1) {
                    this.mPreference.setEnabled(false);
                    this.mPreference.setSummaryProvider(new DefaultSubscriptionController$$ExternalSyntheticLambda1(this));
                    return;
                }
                int defaultSubscriptionId = getDefaultSubscriptionId();
                Iterator<SubscriptionInfo> it = this.mSelectableSubs.iterator();
                boolean z2 = false;
                while (it.hasNext()) {
                    SubscriptionInfo next = it.next();
                    if (!next.isOpportunistic()) {
                        arrayList.add(SubscriptionUtil.getUniqueSubscriptionDisplayName(next, this.mContext));
                        int subscriptionId = next.getSubscriptionId();
                        arrayList2.add(Integer.toString(subscriptionId));
                        if (subscriptionId == defaultSubscriptionId) {
                            z2 = true;
                        }
                    }
                }
                if (TextUtils.equals(getPreferenceKey(), LIST_DATA_PREFERENCE_KEY)) {
                    boolean emergencyCallbackMode = this.mTelephonyManager.getEmergencyCallbackMode();
                    boolean booleanValue = ((Boolean) TelephonyProperties.in_scbm().orElse(Boolean.FALSE)).booleanValue();
                    if (Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dds_switch", 0) == 0) {
                        ListPreference listPreference = this.mPreference;
                        if (!isCallStateIdle() || emergencyCallbackMode || booleanValue || (TelephonyUtils.isSubsidyFeatureEnabled(this.mContext) && !TelephonyUtils.allowUsertoSetDDS(this.mContext))) {
                            z = false;
                        }
                        listPreference.setEnabled(z);
                    } else {
                        this.mPreference.setEnabled(false);
                        this.mPreference.setSummaryProvider(new DefaultSubscriptionController$$ExternalSyntheticLambda2(this));
                    }
                } else if (isAskEverytimeSupported()) {
                    arrayList.add(this.mContext.getString(R$string.calls_and_sms_ask_every_time));
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
    }

    /* access modifiers changed from: private */
    public /* synthetic */ CharSequence lambda$updateEntries$1(Preference preference) {
        return SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSelectableSubs.get(0), this.mContext);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ CharSequence lambda$updateEntries$2(Preference preference) {
        return getSmartDdsSummary();
    }

    private CharSequence getSmartDdsSummary() {
        return this.mContext.getString(R$string.dds_preference_smart_dds_switch_is_on);
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
        for (PhoneAccountHandle equals : callCapablePhoneAccounts) {
            if (userSelectedOutgoingPhoneAccount.equals(equals)) {
                return userSelectedOutgoingPhoneAccount;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public TelecomManager getTelecomManager() {
        if (this.mTelecomManager == null) {
            this.mTelecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        }
        return this.mTelecomManager;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) {
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

    public boolean onPreferenceChange(Preference preference, Object obj) {
        setDefaultSubscription(Integer.parseInt((String) obj));
        refreshSummary(this.mPreference);
        return true;
    }

    public void onSubscriptionsChanged() {
        ArrayList<SubscriptionInfo> arrayList = this.mSelectableSubs;
        if (arrayList != null) {
            arrayList.clear();
        }
        updateSubStatus();
        if (this.mPreference != null) {
            updateEntries();
            refreshSummary(this.mPreference);
            this.mPreference.setVisible(isAvailable());
        }
    }

    private void registerPhoneStateListener() {
        updateSubStatus();
        for (int i = 0; i < this.mSelectableSubs.size(); i++) {
            this.mTelephonyManager.createForSubscriptionId(this.mSelectableSubs.get(i).getSubscriptionId()).listen(getPhoneStateListener(i), 32);
        }
    }

    private void unRegisterPhoneStateListener() {
        for (int i = 0; i < this.mPhoneCount; i++) {
            PhoneStateListener phoneStateListener = this.mPhoneStateListener[i];
            if (phoneStateListener != null) {
                this.mTelephonyManager.listen(phoneStateListener, 0);
                this.mPhoneStateListener[i] = null;
            }
        }
    }

    private PhoneStateListener getPhoneStateListener(final int i) {
        this.mPhoneStateListener[i] = new PhoneStateListener() {
            public void onCallStateChanged(int i, String str) {
                DefaultSubscriptionController.this.mCallState[i] = i;
                DefaultSubscriptionController.this.updateEntries();
            }
        };
        return this.mPhoneStateListener[i];
    }

    private void updateSubStatus() {
        if (this.mSelectableSubs.isEmpty()) {
            for (int i = 0; i < this.mPhoneCount; i++) {
                SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = this.mManager.getActiveSubscriptionInfoForSimSlotIndex(i);
                if (activeSubscriptionInfoForSimSlotIndex != null) {
                    this.mSelectableSubs.add(activeSubscriptionInfoForSimSlotIndex);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isRtlMode() {
        return this.mIsRtlMode;
    }
}
