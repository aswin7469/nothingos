package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes.dex */
public class PreferredNetworkModePreferenceController extends TelephonyBasePreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver {
    private static final String LOG_TAG = "PreferredNetworkMode";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    Integer mCallState;
    private CarrierConfigManager mCarrierConfigManager;
    private boolean mIsGlobalCdma;
    private PersistableBundle mPersistableBundle;
    private PhoneTelephonyCallback mPhoneTelephonyCallback;
    private Preference mPreference;
    private TelephonyManager mTelephonyManager;

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
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

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PreferredNetworkModePreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    @Override // com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        return i != -1 && configForSubId != null && !configForSubId.getBoolean("hide_carrier_network_settings_bool") && !configForSubId.getBoolean("hide_preferred_network_type_bool") && configForSubId.getBoolean("world_phone_bool") ? 0 : 2;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        PhoneTelephonyCallback phoneTelephonyCallback = this.mPhoneTelephonyCallback;
        if (phoneTelephonyCallback != null) {
            phoneTelephonyCallback.register(this.mContext, this.mSubId);
        }
        AllowedNetworkTypesListener allowedNetworkTypesListener = this.mAllowedNetworkTypesListener;
        if (allowedNetworkTypesListener != null) {
            allowedNetworkTypesListener.register(this.mContext, this.mSubId);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        PhoneTelephonyCallback phoneTelephonyCallback = this.mPhoneTelephonyCallback;
        if (phoneTelephonyCallback != null) {
            phoneTelephonyCallback.unregister();
        }
        AllowedNetworkTypesListener allowedNetworkTypesListener = this.mAllowedNetworkTypesListener;
        if (allowedNetworkTypesListener != null) {
            allowedNetworkTypesListener.unregister(this.mContext, this.mSubId);
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        ListPreference listPreference = (ListPreference) preference;
        int preferredNetworkMode = getPreferredNetworkMode();
        listPreference.setValue(Integer.toString(preferredNetworkMode));
        listPreference.setSummary(getPreferredNetworkModeSummaryResId(preferredNetworkMode));
        listPreference.setEnabled(isCallStateIdle());
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        this.mTelephonyManager.setAllowedNetworkTypesForReason(0, MobileNetworkUtils.getRafFromNetworkType(parseInt));
        ((ListPreference) preference).setSummary(getPreferredNetworkModeSummaryResId(parseInt));
        return true;
    }

    public void init(Lifecycle lifecycle, int i) {
        this.mSubId = i;
        if (this.mPhoneTelephonyCallback == null) {
            this.mPhoneTelephonyCallback = new PhoneTelephonyCallback();
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
        TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = createForSubscriptionId;
        this.mIsGlobalCdma = createForSubscriptionId.isLteCdmaEvdoGsmWcdmaEnabled() && configForSubId.getBoolean("show_cdma_choices_bool");
        if (this.mAllowedNetworkTypesListener == null) {
            AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
            this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
            allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AllowedNetworkTypesListener.OnAllowedNetworkTypesListener() { // from class: com.android.settings.network.telephony.PreferredNetworkModePreferenceController$$ExternalSyntheticLambda0
                @Override // com.android.settings.network.AllowedNetworkTypesListener.OnAllowedNetworkTypesListener
                public final void onAllowedNetworkTypesChanged() {
                    PreferredNetworkModePreferenceController.this.lambda$init$0();
                }
            });
        }
        lifecycle.addObserver(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updatePreference */
    public void lambda$init$0() {
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    private int getPreferredNetworkMode() {
        return MobileNetworkUtils.getNetworkTypeFromRaf((int) this.mTelephonyManager.getAllowedNetworkTypesForReason(0));
    }

    private int getPreferredNetworkModeSummaryResId(int i) {
        switch (i) {
            case 0:
                return R.string.preferred_network_mode_wcdma_perf_summary;
            case 1:
                return R.string.preferred_network_mode_gsm_only_summary;
            case 2:
                return R.string.preferred_network_mode_wcdma_only_summary;
            case 3:
                return R.string.preferred_network_mode_gsm_wcdma_summary;
            case 4:
                if (this.mTelephonyManager.isLteCdmaEvdoGsmWcdmaEnabled()) {
                    return R.string.preferred_network_mode_cdma_summary;
                }
                return R.string.preferred_network_mode_cdma_evdo_summary;
            case 5:
                return R.string.preferred_network_mode_cdma_only_summary;
            case 6:
                return R.string.preferred_network_mode_evdo_only_summary;
            case 7:
                return R.string.preferred_network_mode_cdma_evdo_gsm_wcdma_summary;
            case 8:
                return R.string.preferred_network_mode_lte_cdma_evdo_summary;
            case 9:
                return R.string.preferred_network_mode_lte_gsm_wcdma_summary;
            case 10:
                if (this.mTelephonyManager.getPhoneType() == 2 || this.mIsGlobalCdma || MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                    return R.string.preferred_network_mode_lte_cdma_evdo_gsm_wcdma_summary;
                }
                return R.string.preferred_network_mode_lte_summary;
            case 11:
                return R.string.preferred_network_mode_lte_summary;
            case 12:
                return R.string.preferred_network_mode_lte_wcdma_summary;
            case 13:
                return R.string.preferred_network_mode_tdscdma_summary;
            case 14:
                return R.string.preferred_network_mode_tdscdma_wcdma_summary;
            case 15:
                return R.string.preferred_network_mode_lte_tdscdma_summary;
            case 16:
                return R.string.preferred_network_mode_tdscdma_gsm_summary;
            case 17:
                return R.string.preferred_network_mode_lte_tdscdma_gsm_summary;
            case 18:
                return R.string.preferred_network_mode_tdscdma_gsm_wcdma_summary;
            case 19:
                return R.string.preferred_network_mode_lte_tdscdma_wcdma_summary;
            case 20:
                return R.string.preferred_network_mode_lte_tdscdma_gsm_wcdma_summary;
            case 21:
                return R.string.nt_preferred_network_mode_tdscdma_cdma_evdo_gsm_wcdma_summary;
            case 22:
                return R.string.nt_preferred_network_mode_lte_tdscdma_cdma_evdo_gsm_wcdma_summary;
            case 23:
                return R.string.preferred_network_mode_nr_only_summary;
            case 24:
                return R.string.preferred_network_mode_nr_lte_summary;
            case 25:
                return R.string.preferred_network_mode_nr_lte_cdma_evdo_summary;
            case 26:
                return R.string.preferred_network_mode_nr_lte_gsm_wcdma_summary;
            case 27:
                return R.string.preferred_network_mode_global_summary;
            case 28:
                return R.string.preferred_network_mode_nr_lte_wcdma_summary;
            case 29:
                return R.string.preferred_network_mode_nr_lte_tdscdma_summary;
            case 30:
                return R.string.preferred_network_mode_nr_lte_tdscdma_gsm_summary;
            case 31:
                return R.string.preferred_network_mode_nr_lte_tdscdma_wcdma_summary;
            case 32:
                return R.string.preferred_network_mode_nr_lte_tdscdma_gsm_wcdma_summary;
            case 33:
                return R.string.nt_preferred_network_mode_nr_lte_tdscdma_cdma_evdo_gsm_wcdma_summary;
            default:
                return R.string.preferred_network_mode_global_summary;
        }
    }

    private boolean isCallStateIdle() {
        Integer num = this.mCallState;
        boolean z = num == null || num.intValue() == 0;
        Log.d(LOG_TAG, "isCallStateIdle:" + z);
        return z;
    }

    TelephonyManager getTelephonyManager(Context context, int i) {
        TelephonyManager createForSubscriptionId;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        return (SubscriptionManager.isValidSubscriptionId(i) && (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) != null) ? createForSubscriptionId : telephonyManager;
    }

    /* loaded from: classes.dex */
    private class PhoneTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mTelephonyManager;

        private PhoneTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            PreferredNetworkModePreferenceController.this.mCallState = Integer.valueOf(i);
            PreferredNetworkModePreferenceController preferredNetworkModePreferenceController = PreferredNetworkModePreferenceController.this;
            preferredNetworkModePreferenceController.updateState(preferredNetworkModePreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            TelephonyManager telephonyManager = PreferredNetworkModePreferenceController.this.getTelephonyManager(context, i);
            this.mTelephonyManager = telephonyManager;
            PreferredNetworkModePreferenceController.this.mCallState = Integer.valueOf(telephonyManager.getCallState(i));
            this.mTelephonyManager.registerTelephonyCallback(context.getMainExecutor(), this);
        }

        public void unregister() {
            PreferredNetworkModePreferenceController.this.mCallState = null;
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }
    }
}
