package com.android.settings.network.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneStateListener;
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
import com.android.settings.R$array;
import com.android.settings.R$string;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.network.CarrierConfigCache;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EnabledNetworkModePreferenceController extends TelephonyBasePreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final String LOG_TAG = "EnabledNetworkMode";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    /* access modifiers changed from: private */
    public PreferenceEntriesBuilder mBuilder;
    /* access modifiers changed from: private */
    public int mCallState = 0;
    private CarrierConfigCache mCarrierConfigCache;
    private PhoneCallStateListener mPhoneStateListener;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private PreferenceScreen mPreferenceScreen;
    private SubscriptionsChangeListener mSubscriptionsListener;
    /* access modifiers changed from: private */
    public PhoneCallStateTelephonyCallback mTelephonyCallback;
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

    public EnabledNetworkModePreferenceController(Context context, String str) {
        super(context, str);
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
        this.mCarrierConfigCache = CarrierConfigCache.getInstance(context);
        if (this.mTelephonyCallback == null) {
            this.mTelephonyCallback = new PhoneCallStateTelephonyCallback();
        }
    }

    public int getAvailabilityStatus(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(i);
        boolean z = true;
        if (i == -1 || configForSubId == null || !CarrierConfigManager.isConfigForIdentifiedCarrier(configForSubId) || configForSubId.getBoolean("hide_carrier_network_settings_bool") || configForSubId.getBoolean("hide_preferred_network_type_bool") || configForSubId.getBoolean("world_phone_bool")) {
            z = false;
        } else if (!isCallStateIdle()) {
            return 1;
        }
        if (z) {
            return 0;
        }
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean isCallStateIdle() {
        return this.mCallState == 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mSubscriptionsListener.start();
        if (this.mAllowedNetworkTypesListener != null && this.mTelephonyCallback != null) {
            PhoneCallStateListener phoneCallStateListener = this.mPhoneStateListener;
            if (phoneCallStateListener != null) {
                phoneCallStateListener.register(this.mContext, this.mSubId);
            }
            this.mAllowedNetworkTypesListener.register(this.mContext, this.mSubId);
            this.mTelephonyCallback.register(this.mTelephonyManager, this.mSubId);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mSubscriptionsListener.stop();
        if (this.mAllowedNetworkTypesListener != null && this.mTelephonyCallback != null) {
            PhoneCallStateListener phoneCallStateListener = this.mPhoneStateListener;
            if (phoneCallStateListener != null) {
                phoneCallStateListener.unregister();
            }
            this.mAllowedNetworkTypesListener.unregister(this.mContext, this.mSubId);
            this.mTelephonyCallback.unregister();
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        if (this.mBuilder != null) {
            super.updateState(preference);
            ListPreference listPreference = (ListPreference) preference;
            this.mBuilder.setPreferenceEntries();
            this.mBuilder.setPreferenceValueAndSummary();
            listPreference.setEntries((CharSequence[]) this.mBuilder.getEntries());
            listPreference.setEntryValues(this.mBuilder.getEntryValues());
            listPreference.setValue(Integer.toString(this.mBuilder.getSelectedEntryValue()));
            listPreference.setSummary(this.mBuilder.getSummary());
            listPreference.setEnabled(isCallStateIdle());
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        ListPreference listPreference = (ListPreference) preference;
        if (!this.mTelephonyManager.setPreferredNetworkTypeBitmask(MobileNetworkUtils.getRafFromNetworkType(parseInt))) {
            return false;
        }
        this.mBuilder.setPreferenceValueAndSummary(parseInt);
        listPreference.setValue(Integer.toString(this.mBuilder.getSelectedEntryValue()));
        listPreference.setSummary(this.mBuilder.getSummary());
        return true;
    }

    /* access modifiers changed from: package-private */
    public void init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mBuilder = new PreferenceEntriesBuilder(this.mContext, this.mSubId);
        if (this.mPhoneStateListener == null) {
            this.mPhoneStateListener = new PhoneCallStateListener();
        }
        if (this.mAllowedNetworkTypesListener == null) {
            AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(this.mContext.getMainExecutor());
            this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
            allowedNetworkTypesListener.setAllowedNetworkTypesListener(new EnabledNetworkModePreferenceController$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0() {
        this.mBuilder.updateConfig();
        updatePreference();
    }

    /* access modifiers changed from: private */
    public void updatePreference() {
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null) {
            displayPreference(preferenceScreen);
        }
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }

    private final class PreferenceEntriesBuilder {
        private boolean mAllowed5gNetworkType;
        private CarrierConfigCache mCarrierConfigCache;
        private Context mContext;
        private List<String> mEntries = new ArrayList();
        private List<Integer> mEntriesValue = new ArrayList();
        private boolean mIs5gEntryDisplayed;
        private boolean mIsGlobalCdma;
        private int mSelectedEntry;
        private boolean mShow4gForLTE;
        private int mSubId;
        private String mSummary;
        private boolean mSupported5gRadioAccessFamily;
        private TelephonyManager mTelephonyManager;

        private int addNrToLteNetworkType(int i) {
            switch (i) {
                case 8:
                    return 25;
                case 9:
                    return 26;
                case 10:
                    return 27;
                case 11:
                    return 24;
                case 12:
                    return 28;
                case 15:
                    return 29;
                case 17:
                    return 30;
                case 19:
                    return 31;
                case 20:
                    return 32;
                case 22:
                    return 33;
                default:
                    return i;
            }
        }

        private boolean checkSupportedRadioBitmask(long j, long j2) {
            return (j2 & j) > 0;
        }

        private int reduceNrToLteNetworkType(int i) {
            switch (i) {
                case 24:
                    return 11;
                case 25:
                    return 8;
                case 26:
                    return 9;
                case 27:
                    return 10;
                case 28:
                    return 12;
                case 29:
                    return 15;
                case 30:
                    return 17;
                case 31:
                    return 19;
                case 32:
                    return 20;
                case 33:
                    return 22;
                default:
                    return i;
            }
        }

        PreferenceEntriesBuilder(Context context, int i) {
            this.mContext = context;
            this.mSubId = i;
            this.mCarrierConfigCache = CarrierConfigCache.getInstance(context);
            this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
            updateConfig();
        }

        public void updateConfig() {
            this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
            PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(this.mSubId);
            this.mAllowed5gNetworkType = checkSupportedRadioBitmask(this.mTelephonyManager.getAllowedNetworkTypesForReason(2), 524288);
            this.mSupported5gRadioAccessFamily = checkSupportedRadioBitmask(this.mTelephonyManager.getSupportedRadioAccessFamily(), 524288);
            boolean z = true;
            this.mIsGlobalCdma = this.mTelephonyManager.isLteCdmaEvdoGsmWcdmaEnabled() && configForSubId != null && configForSubId.getBoolean("show_cdma_choices_bool");
            if (configForSubId == null || !configForSubId.getBoolean("show_4g_for_lte_data_icon_bool")) {
                z = false;
            }
            this.mShow4gForLTE = z;
            Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "PreferenceEntriesBuilder: subId" + this.mSubId + ",Supported5gRadioAccessFamily :" + this.mSupported5gRadioAccessFamily + ",mAllowed5gNetworkType :" + this.mAllowed5gNetworkType + ",IsGlobalCdma :" + this.mIsGlobalCdma + ",Show4gForLTE :" + this.mShow4gForLTE);
        }

        /* access modifiers changed from: package-private */
        public void setPreferenceEntries() {
            NetworkModeChoicesProto$UiOptions.Builder builder;
            NetworkModeChoicesProto$UiOptions.PresentFormat presentFormat;
            this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
            boolean z = this.mCarrierConfigCache.getConfigForSubId(this.mSubId).getBoolean("prefer_2g_bool");
            clearAllEntries();
            NetworkModeChoicesProto$UiOptions.Builder newBuilder = NetworkModeChoicesProto$UiOptions.newBuilder();
            newBuilder.setType(getEnabledNetworkType());
            switch (C10981.f204x51424e9e[newBuilder.getType().ordinal()]) {
                case 1:
                    builder = newBuilder.setChoices(R$array.enabled_networks_cdma_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAndLteEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add1xEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addGlobalEntry);
                    break;
                case 2:
                    builder = newBuilder.setChoices(R$array.enabled_networks_cdma_no_lte_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add1xEntry);
                    break;
                case 3:
                    builder = newBuilder.setChoices(R$array.enabled_networks_cdma_only_lte_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addLteEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addGlobalEntry);
                    break;
                case 4:
                    NetworkModeChoicesProto$UiOptions.Builder choices = newBuilder.setChoices(R$array.enabled_networks_tdscdma_values);
                    if (this.mShow4gForLTE) {
                        presentFormat = NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAnd4gEntry;
                    } else {
                        presentFormat = NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAndLteEntry;
                    }
                    builder = choices.addFormat(presentFormat).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add2gEntry);
                    break;
                case 5:
                    builder = newBuilder.setChoices(R$array.enabled_networks_except_gsm_lte_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry);
                    break;
                case 6:
                    builder = newBuilder.setChoices(R$array.enabled_networks_except_gsm_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAnd4gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry);
                    break;
                case 7:
                    builder = newBuilder.setChoices(R$array.enabled_networks_except_gsm_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAndLteEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry);
                    break;
                case 8:
                    builder = newBuilder.setChoices(R$array.enabled_networks_except_lte_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add2gEntry);
                    break;
                case 9:
                    builder = newBuilder.setChoices(R$array.enabled_networks_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAnd4gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add2gEntry);
                    break;
                case 10:
                    builder = newBuilder.setChoices(R$array.enabled_networks_values).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAndLteEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.add2gEntry);
                    break;
                case 11:
                    builder = newBuilder.setChoices(R$array.preferred_network_mode_values_world_mode).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addGlobalEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addWorldModeCdmaEntry).addFormat(NetworkModeChoicesProto$UiOptions.PresentFormat.addWorldModeGsmEntry);
                    break;
                default:
                    throw new IllegalArgumentException("Not supported enabled network types.");
            }
            int[] array = Stream.of(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getStringArray(builder.getChoices())).mapToInt(new C1101x7d702dd4()).toArray();
            List<NetworkModeChoicesProto$UiOptions.PresentFormat> formatList = builder.getFormatList();
            if (array.length >= formatList.size()) {
                IntStream.range(0, formatList.size()).forEach(new C1102x7d702dd5(this, formatList, z, array));
                return;
            }
            throw new IllegalArgumentException(builder.getType().name() + " index error.");
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPreferenceEntries$0(List list, boolean z, int[] iArr, int i) {
            switch (C10981.f205x64a23af0[((NetworkModeChoicesProto$UiOptions.PresentFormat) list.get(i)).ordinal()]) {
                case 1:
                    if (z) {
                        add1xEntry(iArr[i]);
                        return;
                    }
                    return;
                case 2:
                    if (z) {
                        add2gEntry(iArr[i]);
                        return;
                    }
                    return;
                case 3:
                    add3gEntry(iArr[i]);
                    return;
                case 4:
                    addGlobalEntry(iArr[i]);
                    return;
                case 5:
                    addCustomEntry(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_world_mode_cdma_lte), iArr[i]);
                    return;
                case 6:
                    addCustomEntry(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_world_mode_gsm_lte), iArr[i]);
                    return;
                case 7:
                    add4gEntry(iArr[i]);
                    return;
                case 8:
                    addLteEntry(iArr[i]);
                    return;
                case 9:
                    add5gEntry(addNrToLteNetworkType(iArr[i]));
                    return;
                case 10:
                    add5gEntry(addNrToLteNetworkType(iArr[i]));
                    add4gEntry(iArr[i]);
                    return;
                case 11:
                    add5gEntry(addNrToLteNetworkType(iArr[i]));
                    addLteEntry(iArr[i]);
                    return;
                default:
                    throw new IllegalArgumentException("Not supported ui options format.");
            }
        }

        private int getPreferredNetworkMode() {
            int networkTypeFromRaf = MobileNetworkUtils.getNetworkTypeFromRaf((int) this.mTelephonyManager.getAllowedNetworkTypesForReason(0));
            if (!showNrList()) {
                Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "Network mode :" + networkTypeFromRaf + " reduce NR");
                networkTypeFromRaf = reduceNrToLteNetworkType(networkTypeFromRaf);
            }
            Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "getPreferredNetworkMode: " + networkTypeFromRaf);
            return networkTypeFromRaf;
        }

        private NetworkModeChoicesProto$EnabledNetworks getEnabledNetworkType() {
            NetworkModeChoicesProto$EnabledNetworks networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_UNKNOWN;
            int phoneType = this.mTelephonyManager.getPhoneType();
            PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(this.mSubId);
            if (phoneType == 2) {
                ContentResolver contentResolver = this.mContext.getContentResolver();
                int i = Settings.Global.getInt(contentResolver, "lte_service_forced" + this.mSubId, 0);
                int preferredNetworkMode = getPreferredNetworkMode();
                if (this.mTelephonyManager.isLteCdmaEvdoGsmWcdmaEnabled()) {
                    if (i != 0) {
                        switch (preferredNetworkMode) {
                            case 4:
                            case 5:
                            case 6:
                                networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_NO_LTE_CHOICES;
                                break;
                            case 7:
                            case 8:
                            case 10:
                            case 11:
                                networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_ONLY_LTE_CHOICES;
                                break;
                            default:
                                networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_CHOICES;
                                break;
                        }
                    } else {
                        networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_CHOICES;
                    }
                }
            } else if (phoneType == 1) {
                networkModeChoicesProto$EnabledNetworks = MobileNetworkUtils.isTdscdmaSupported(this.mContext, this.mSubId) ? NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_TDSCDMA_CHOICES : (configForSubId == null || configForSubId.getBoolean("prefer_2g_bool") || configForSubId.getBoolean("lte_enabled_bool")) ? (configForSubId == null || configForSubId.getBoolean("prefer_2g_bool")) ? (configForSubId == null || configForSubId.getBoolean("lte_enabled_bool")) ? this.mIsGlobalCdma ? NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_CHOICES : this.mShow4gForLTE ? NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_4G_CHOICES : NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CHOICES : NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_LTE_CHOICES : this.mShow4gForLTE ? NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_4G_CHOICES : NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_CHOICES : NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_LTE_CHOICES;
            }
            if (MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                networkModeChoicesProto$EnabledNetworks = NetworkModeChoicesProto$EnabledNetworks.PREFERRED_NETWORK_MODE_CHOICES_WORLD_MODE;
            }
            Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "enabledNetworkType: " + networkModeChoicesProto$EnabledNetworks);
            return networkModeChoicesProto$EnabledNetworks;
        }

        /* access modifiers changed from: package-private */
        public void setPreferenceValueAndSummary(int i) {
            setSelectedEntry(i);
            switch (i) {
                case 0:
                case 2:
                case 3:
                    if (!this.mIsGlobalCdma) {
                        setSelectedEntry(0);
                        setSummary(R$string.network_3G);
                        return;
                    }
                    setSelectedEntry(10);
                    setSummary(R$string.network_global);
                    return;
                case 1:
                    if (!this.mIsGlobalCdma) {
                        setSelectedEntry(1);
                        setSummary(R$string.network_2G);
                        return;
                    }
                    setSelectedEntry(10);
                    setSummary(R$string.network_global);
                    return;
                case 4:
                case 6:
                case 7:
                    setSelectedEntry(4);
                    setSummary(R$string.network_3G);
                    return;
                case 5:
                    setSelectedEntry(5);
                    setSummary(R$string.network_1x);
                    return;
                case 8:
                    if (MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                        setSummary(R$string.preferred_network_mode_lte_cdma_summary);
                        return;
                    }
                    setSelectedEntry(8);
                    setSummary(is5gEntryDisplayed() ? R$string.network_lte_pure : R$string.network_lte);
                    return;
                case 9:
                    if (MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                        setSummary(R$string.preferred_network_mode_lte_gsm_umts_summary);
                        return;
                    }
                    break;
                case 10:
                case 15:
                case 17:
                case 19:
                case 20:
                case 22:
                    if (MobileNetworkUtils.isTdscdmaSupported(this.mContext, this.mSubId)) {
                        setSelectedEntry(22);
                        setSummary(is5gEntryDisplayed() ? R$string.network_lte_pure : R$string.network_lte);
                        return;
                    }
                    setSelectedEntry(10);
                    if (this.mTelephonyManager.getPhoneType() == 2 || this.mIsGlobalCdma || MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                        setSummary(R$string.network_global);
                        return;
                    } else if (is5gEntryDisplayed()) {
                        setSummary(this.mShow4gForLTE ? R$string.network_4G_pure : R$string.network_lte_pure);
                        return;
                    } else {
                        setSummary(this.mShow4gForLTE ? R$string.network_4G : R$string.network_lte);
                        return;
                    }
                case 11:
                case 12:
                    break;
                case 13:
                    setSelectedEntry(13);
                    setSummary(R$string.network_3G);
                    return;
                case 14:
                case 16:
                case 18:
                    setSelectedEntry(18);
                    setSummary(R$string.network_3G);
                    return;
                case 21:
                    setSelectedEntry(21);
                    setSummary(R$string.network_3G);
                    return;
                case 23:
                case 24:
                case 26:
                case 28:
                    setSelectedEntry(26);
                    setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_5G_recommended));
                    return;
                case 25:
                    setSelectedEntry(25);
                    setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_5G_recommended));
                    return;
                case 27:
                    setSelectedEntry(27);
                    if (this.mTelephonyManager.getPhoneType() == 2 || this.mIsGlobalCdma || MobileNetworkUtils.isWorldMode(this.mContext, this.mSubId)) {
                        setSummary(R$string.network_global);
                        return;
                    } else {
                        setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_5G_recommended));
                        return;
                    }
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                    setSelectedEntry(33);
                    setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_5G_recommended));
                    return;
                default:
                    setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.mobile_network_mode_error, new Object[]{Integer.valueOf(i)}));
                    return;
            }
            if (!this.mIsGlobalCdma) {
                setSelectedEntry(9);
                if (is5gEntryDisplayed()) {
                    setSummary(this.mShow4gForLTE ? R$string.network_4G_pure : R$string.network_lte_pure);
                } else {
                    setSummary(this.mShow4gForLTE ? R$string.network_4G : R$string.network_lte);
                }
            } else {
                setSelectedEntry(10);
                setSummary(R$string.network_global);
            }
        }

        /* access modifiers changed from: private */
        public void setPreferenceValueAndSummary() {
            setPreferenceValueAndSummary(getPreferredNetworkMode());
        }

        private void add5gEntry(int i) {
            boolean z = i >= 23;
            if (!showNrList() || !z) {
                this.mIs5gEntryDisplayed = false;
                Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "Hide 5G option.  supported5GRadioAccessFamily: " + this.mSupported5gRadioAccessFamily + " allowed5GNetworkType: " + this.mAllowed5gNetworkType + " isNRValue: " + z);
                return;
            }
            this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_5G_recommended));
            this.mEntriesValue.add(Integer.valueOf(i));
            this.mIs5gEntryDisplayed = true;
        }

        private void addGlobalEntry(int i) {
            Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "addGlobalEntry.  supported5GRadioAccessFamily: " + this.mSupported5gRadioAccessFamily + " allowed5GNetworkType: " + this.mAllowed5gNetworkType);
            this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_global));
            if (showNrList()) {
                i = addNrToLteNetworkType(i);
            }
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private boolean showNrList() {
            return this.mSupported5gRadioAccessFamily && this.mAllowed5gNetworkType;
        }

        private void addLteEntry(int i) {
            if (showNrList()) {
                this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_lte_pure));
            } else {
                this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_lte));
            }
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private void add4gEntry(int i) {
            if (showNrList()) {
                this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_4G_pure));
            } else {
                this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_4G));
            }
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private void add3gEntry(int i) {
            this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_3G));
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private void add2gEntry(int i) {
            this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_2G));
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private void add1xEntry(int i) {
            this.mEntries.add(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(R$string.network_1x));
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        private void addCustomEntry(String str, int i) {
            this.mEntries.add(str);
            this.mEntriesValue.add(Integer.valueOf(i));
        }

        /* access modifiers changed from: private */
        public String[] getEntries() {
            return (String[]) this.mEntries.toArray(new String[0]);
        }

        private void clearAllEntries() {
            this.mEntries.clear();
            this.mEntriesValue.clear();
        }

        /* access modifiers changed from: private */
        public String[] getEntryValues() {
            return (String[]) Arrays.stream((Integer[]) this.mEntriesValue.toArray(new Integer[0])).map(new C1099x7d702dd2()).toArray(new C1100x7d702dd3());
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ String[] lambda$getEntryValues$1(int i) {
            return new String[i];
        }

        /* access modifiers changed from: private */
        public int getSelectedEntryValue() {
            return this.mSelectedEntry;
        }

        private void setSelectedEntry(int i) {
            if (this.mEntriesValue.stream().anyMatch(new C1103x7d702dd6(i))) {
                this.mSelectedEntry = i;
            } else if (this.mEntriesValue.size() > 0) {
                this.mSelectedEntry = this.mEntriesValue.get(0).intValue();
            } else {
                Log.e(EnabledNetworkModePreferenceController.LOG_TAG, "entriesValue is empty");
            }
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$setSelectedEntry$2(int i, Integer num) {
            return num.intValue() == i;
        }

        /* access modifiers changed from: private */
        public String getSummary() {
            return this.mSummary;
        }

        private void setSummary(int i) {
            setSummary(EnabledNetworkModePreferenceController.this.getResourcesForSubId().getString(i));
        }

        private void setSummary(String str) {
            this.mSummary = str;
        }

        private boolean is5gEntryDisplayed() {
            return this.mIs5gEntryDisplayed;
        }
    }

    /* renamed from: com.android.settings.network.telephony.EnabledNetworkModePreferenceController$1 */
    static /* synthetic */ class C10981 {

        /* renamed from: $SwitchMap$com$android$settings$network$telephony$NetworkModeChoicesProto$EnabledNetworks */
        static final /* synthetic */ int[] f204x51424e9e;

        /* renamed from: $SwitchMap$com$android$settings$network$telephony$NetworkModeChoicesProto$UiOptions$PresentFormat */
        static final /* synthetic */ int[] f205x64a23af0;

        /* JADX WARNING: Can't wrap try/catch for region: R(45:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|21|22|23|(2:25|26)|27|(2:29|30)|31|(2:33|34)|35|(2:37|38)|39|(2:41|42)|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|(3:65|66|68)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(46:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|(2:17|18)|19|21|22|23|(2:25|26)|27|(2:29|30)|31|(2:33|34)|35|(2:37|38)|39|(2:41|42)|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|(3:65|66|68)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(48:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|(2:17|18)|19|21|22|23|(2:25|26)|27|29|30|31|(2:33|34)|35|(2:37|38)|39|41|42|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|(3:65|66|68)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(50:0|(2:1|2)|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|21|22|23|(2:25|26)|27|29|30|31|(2:33|34)|35|(2:37|38)|39|41|42|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|(3:65|66|68)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(55:0|1|2|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|21|22|23|25|26|27|29|30|31|(2:33|34)|35|37|38|39|41|42|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|68) */
        /* JADX WARNING: Can't wrap try/catch for region: R(56:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|(2:33|34)|35|37|38|39|41|42|43|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|68) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x0095 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x009f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00a9 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00b3 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x00bd */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x00c7 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x00d1 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:61:0x00db */
        /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x00e5 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x00ef */
        static {
            /*
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat[] r0 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f205x64a23af0 = r0
                r1 = 1
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r2 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add1xEntry     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r3 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add2gEntry     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r4 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add3gEntry     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r5 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.addGlobalEntry     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                r4 = 5
                int[] r5 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r6 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.addWorldModeCdmaEntry     // Catch:{ NoSuchFieldError -> 0x003e }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                r5 = 6
                int[] r6 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r7 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.addWorldModeGsmEntry     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r6[r7] = r5     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                r6 = 7
                int[] r7 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r8 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add4gEntry     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r8 = r8.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r7[r8] = r6     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                r7 = 8
                int[] r8 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r9 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.addLteEntry     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r9 = r9.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r8[r9] = r7     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                r8 = 9
                int[] r9 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x006c }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r10 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add5gEntry     // Catch:{ NoSuchFieldError -> 0x006c }
                int r10 = r10.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r9[r10] = r8     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                r9 = 10
                int[] r10 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r11 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAnd4gEntry     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r11 = r11.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r10[r11] = r9     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                r10 = 11
                int[] r11 = f205x64a23af0     // Catch:{ NoSuchFieldError -> 0x0084 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions$PresentFormat r12 = com.android.settings.network.telephony.NetworkModeChoicesProto$UiOptions.PresentFormat.add5gAndLteEntry     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r12 = r12.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r11[r12] = r10     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks[] r11 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.values()
                int r11 = r11.length
                int[] r11 = new int[r11]
                f204x51424e9e = r11
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r12 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_CHOICES     // Catch:{ NoSuchFieldError -> 0x0095 }
                int r12 = r12.ordinal()     // Catch:{ NoSuchFieldError -> 0x0095 }
                r11[r12] = r1     // Catch:{ NoSuchFieldError -> 0x0095 }
            L_0x0095:
                int[] r1 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x009f }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r11 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_NO_LTE_CHOICES     // Catch:{ NoSuchFieldError -> 0x009f }
                int r11 = r11.ordinal()     // Catch:{ NoSuchFieldError -> 0x009f }
                r1[r11] = r0     // Catch:{ NoSuchFieldError -> 0x009f }
            L_0x009f:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00a9 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CDMA_ONLY_LTE_CHOICES     // Catch:{ NoSuchFieldError -> 0x00a9 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a9 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00a9 }
            L_0x00a9:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00b3 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_TDSCDMA_CHOICES     // Catch:{ NoSuchFieldError -> 0x00b3 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b3 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x00b3 }
            L_0x00b3:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00bd }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_LTE_CHOICES     // Catch:{ NoSuchFieldError -> 0x00bd }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00bd }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x00bd }
            L_0x00bd:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00c7 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_4G_CHOICES     // Catch:{ NoSuchFieldError -> 0x00c7 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c7 }
                r0[r1] = r5     // Catch:{ NoSuchFieldError -> 0x00c7 }
            L_0x00c7:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00d1 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_GSM_CHOICES     // Catch:{ NoSuchFieldError -> 0x00d1 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d1 }
                r0[r1] = r6     // Catch:{ NoSuchFieldError -> 0x00d1 }
            L_0x00d1:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00db }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_EXCEPT_LTE_CHOICES     // Catch:{ NoSuchFieldError -> 0x00db }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00db }
                r0[r1] = r7     // Catch:{ NoSuchFieldError -> 0x00db }
            L_0x00db:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00e5 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_4G_CHOICES     // Catch:{ NoSuchFieldError -> 0x00e5 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e5 }
                r0[r1] = r8     // Catch:{ NoSuchFieldError -> 0x00e5 }
            L_0x00e5:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00ef }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.ENABLED_NETWORKS_CHOICES     // Catch:{ NoSuchFieldError -> 0x00ef }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ef }
                r0[r1] = r9     // Catch:{ NoSuchFieldError -> 0x00ef }
            L_0x00ef:
                int[] r0 = f204x51424e9e     // Catch:{ NoSuchFieldError -> 0x00f9 }
                com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks r1 = com.android.settings.network.telephony.NetworkModeChoicesProto$EnabledNetworks.PREFERRED_NETWORK_MODE_CHOICES_WORLD_MODE     // Catch:{ NoSuchFieldError -> 0x00f9 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00f9 }
                r0[r1] = r10     // Catch:{ NoSuchFieldError -> 0x00f9 }
            L_0x00f9:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.EnabledNetworkModePreferenceController.C10981.<clinit>():void");
        }
    }

    class PhoneCallStateTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mTelephonyManager;

        PhoneCallStateTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            Log.d(EnabledNetworkModePreferenceController.LOG_TAG, "onCallStateChanged:" + i);
            EnabledNetworkModePreferenceController.this.mCallState = i;
            EnabledNetworkModePreferenceController.this.mBuilder.updateConfig();
            EnabledNetworkModePreferenceController.this.updatePreference();
        }

        public void register(TelephonyManager telephonyManager, int i) {
            this.mTelephonyManager = telephonyManager;
            EnabledNetworkModePreferenceController.this.mCallState = telephonyManager.getCallState(i);
            this.mTelephonyManager.registerTelephonyCallback(EnabledNetworkModePreferenceController.this.mContext.getMainExecutor(), EnabledNetworkModePreferenceController.this.mTelephonyCallback);
        }

        public void unregister() {
            EnabledNetworkModePreferenceController.this.mCallState = 0;
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.unregisterTelephonyCallback(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public PhoneCallStateTelephonyCallback getTelephonyCallback() {
        return this.mTelephonyCallback;
    }

    public void onSubscriptionsChanged() {
        PreferenceEntriesBuilder preferenceEntriesBuilder = this.mBuilder;
        if (preferenceEntriesBuilder != null) {
            preferenceEntriesBuilder.updateConfig();
        }
    }

    private class PhoneCallStateListener extends PhoneStateListener {
        private TelephonyManager mTelephonyManager;

        PhoneCallStateListener() {
            super(Looper.getMainLooper());
        }

        public void onCallStateChanged(int i, String str) {
            EnabledNetworkModePreferenceController.this.mCallState = i;
            EnabledNetworkModePreferenceController enabledNetworkModePreferenceController = EnabledNetworkModePreferenceController.this;
            enabledNetworkModePreferenceController.updateState(enabledNetworkModePreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
            }
            this.mTelephonyManager.listen(this, 32);
        }

        public void unregister() {
            EnabledNetworkModePreferenceController.this.mCallState = 0;
            this.mTelephonyManager.listen(this, 0);
        }
    }
}
