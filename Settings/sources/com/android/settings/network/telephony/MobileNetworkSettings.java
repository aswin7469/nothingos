package com.android.settings.network.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.SearchIndexableResource;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.datausage.BillingCyclePreferenceController;
import com.android.settings.datausage.DataUsageSummaryPreferenceController;
import com.android.settings.network.ActiveSubscriptionsListener;
import com.android.settings.network.CarrierWifiTogglePreferenceController;
import com.android.settings.network.telephony.cdma.CdmaSubscriptionPreferenceController;
import com.android.settings.network.telephony.cdma.CdmaSystemSelectPreferenceController;
import com.android.settings.network.telephony.gsm.AutoSelectPreferenceController;
import com.android.settings.network.telephony.gsm.OpenNetworkSelectPagePreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class MobileNetworkSettings extends AbstractMobileNetworkSettings {
    static final String KEY_CLICKED_PREF = "key_clicked_pref";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.mobile_network_settings) { // from class: com.android.settings.network.telephony.MobileNetworkSettings.4
        @Override // com.android.settings.search.BaseSearchIndexProvider, com.android.settingslib.search.Indexable$SearchIndexProvider
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            return super.getXmlResourcesToIndex(context, z);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public boolean isPageSearchEnabled(Context context) {
            return ((UserManager) context.getSystemService(UserManager.class)).isAdminUser();
        }
    };
    private ActiveSubscriptionsListener mActiveSubscriptionsListener;
    private int mActiveSubscriptionsListenerCount;
    private CdmaSubscriptionPreferenceController mCdmaSubscriptionPreferenceController;
    private CdmaSystemSelectPreferenceController mCdmaSystemSelectPreferenceController;
    private String mClickedPrefKey;
    private boolean mDropFirstSubscriptionChangeNotify;
    private TelephonyManager mTelephonyManager;
    private UserManager mUserManager;
    private int mSubId = -1;
    private int mPhoneId = -1;
    private final BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.MobileNetworkSettings.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("ss");
                Log.d("NetworkSettings", "Received ACTION_SIM_STATE_CHANGED: " + stringExtra);
                MobileNetworkSettings.this.setScreenState();
            }
        }
    };
    private final BroadcastReceiver mAirplaneStateReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.telephony.MobileNetworkSettings.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                boolean isAirplaneModeOn = WirelessUtils.isAirplaneModeOn(context);
                Log.d("NetworkSettings", "Received ACTION_AIRPLANE_MODE_CHANGED: isAirplaneModeOn=" + isAirplaneModeOn);
                if (isAirplaneModeOn) {
                    MobileNetworkSettings.this.getPreferenceScreen().setEnabled(false);
                } else {
                    MobileNetworkSettings.this.getPreferenceScreen().setEnabled(true);
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NetworkSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1571;
    }

    @Override // com.android.settings.network.telephony.AbstractMobileNetworkSettings, com.android.settings.dashboard.DashboardFragment, androidx.preference.PreferenceGroup.OnExpandButtonClickListener
    public /* bridge */ /* synthetic */ void onExpandButtonClick() {
        super.onExpandButtonClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0046, code lost:
        if (r3.areUiccApplicationsEnabled() == false) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setScreenState() {
        boolean z = false;
        boolean z2 = true;
        if (this.mTelephonyManager.getSimState() == 1) {
            z2 = false;
        }
        if (z2) {
            SubscriptionInfo activeSubscriptionInfo = ((SubscriptionManager) getContext().getSystemService("telephony_subscription_service")).getActiveSubscriptionInfo(this.mSubId);
            Log.d("NetworkSettings", "subInfo: " + activeSubscriptionInfo + ", mSubId: " + this.mSubId);
            if (activeSubscriptionInfo != null) {
            }
        }
        z = z2;
        Log.d("NetworkSettings", "Setting screen state to: " + z);
        getPreferenceScreen().setEnabled(z);
    }

    public MobileNetworkSettings() {
        super("no_config_mobile_networks");
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        if (super.onPreferenceTreeClick(preference)) {
            return true;
        }
        String key = preference.getKey();
        if (!TextUtils.equals(key, "cdma_system_select_key") && !TextUtils.equals(key, "cdma_subscription_key")) {
            return false;
        }
        if (this.mTelephonyManager.getEmergencyCallbackMode()) {
            startActivityForResult(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null), 17);
            this.mClickedPrefKey = key;
        }
        return true;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        int i = getArguments().getInt("android.provider.extra.SUB_ID", MobileNetworkUtils.getSearchableSubscriptionId(context));
        this.mSubId = i;
        this.mPhoneId = SubscriptionManager.getPhoneId(i);
        Log.i("NetworkSettings", "display subId: " + this.mSubId + ", phoneId: " + this.mPhoneId);
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return Arrays.asList(new AbstractPreferenceController[0]);
        }
        return Arrays.asList(new DataUsageSummaryPreferenceController(getActivity(), getSettingsLifecycle(), this, this.mSubId));
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        DataUsageSummaryPreferenceController dataUsageSummaryPreferenceController = (DataUsageSummaryPreferenceController) use(DataUsageSummaryPreferenceController.class);
        if (dataUsageSummaryPreferenceController != null) {
            dataUsageSummaryPreferenceController.init(this.mSubId);
        }
        ((MobileNetworkSwitchController) use(MobileNetworkSwitchController.class)).init(mo959getLifecycle(), this.mSubId);
        ((CarrierSettingsVersionPreferenceController) use(CarrierSettingsVersionPreferenceController.class)).init(this.mSubId);
        ((BillingCyclePreferenceController) use(BillingCyclePreferenceController.class)).init(this.mSubId);
        ((MmsMessagePreferenceController) use(MmsMessagePreferenceController.class)).init(this.mSubId);
        ((DataDuringCallsPreferenceController) use(DataDuringCallsPreferenceController.class)).init(mo959getLifecycle(), this.mSubId);
        ((DisabledSubscriptionController) use(DisabledSubscriptionController.class)).init(mo959getLifecycle(), this.mSubId);
        ((DeleteSimProfilePreferenceController) use(DeleteSimProfilePreferenceController.class)).init(this.mSubId, this, 18);
        ((DisableSimFooterPreferenceController) use(DisableSimFooterPreferenceController.class)).init(this.mSubId);
        ((NrDisabledInDsdsFooterPreferenceController) use(NrDisabledInDsdsFooterPreferenceController.class)).init(this.mSubId);
        ((MobileDataPreferenceController) use(MobileDataPreferenceController.class)).init(getFragmentManager(), this.mSubId);
        ((MobileDataPreferenceController) use(MobileDataPreferenceController.class)).setWifiPickerTrackerHelper(new WifiPickerTrackerHelper(getSettingsLifecycle(), context, null));
        ((RoamingPreferenceController) use(RoamingPreferenceController.class)).init(getFragmentManager(), this.mSubId);
        ((ApnPreferenceController) use(ApnPreferenceController.class)).init(this.mSubId);
        ((UserPLMNPreferenceController) use(UserPLMNPreferenceController.class)).init(this.mSubId);
        ((CarrierPreferenceController) use(CarrierPreferenceController.class)).init(this.mSubId);
        ((DataUsagePreferenceController) use(DataUsagePreferenceController.class)).init(this.mSubId);
        ((PreferredNetworkModePreferenceController) use(PreferredNetworkModePreferenceController.class)).init(mo959getLifecycle(), this.mSubId);
        ((EnabledNetworkModePreferenceController) use(EnabledNetworkModePreferenceController.class)).init(mo959getLifecycle(), this.mSubId);
        ((DataServiceSetupPreferenceController) use(DataServiceSetupPreferenceController.class)).init(this.mSubId);
        ((Enable2gPreferenceController) use(Enable2gPreferenceController.class)).init(this.mSubId);
        ((CarrierWifiTogglePreferenceController) use(CarrierWifiTogglePreferenceController.class)).init(mo959getLifecycle(), this.mSubId);
        WifiCallingPreferenceController init = ((WifiCallingPreferenceController) use(WifiCallingPreferenceController.class)).init(this.mSubId);
        ((NetworkPreferenceCategoryController) use(NetworkPreferenceCategoryController.class)).init(mo959getLifecycle(), this.mSubId).setChildren(Arrays.asList(((AutoSelectPreferenceController) use(AutoSelectPreferenceController.class)).init(mo959getLifecycle(), this.mSubId).addListener(((OpenNetworkSelectPagePreferenceController) use(OpenNetworkSelectPagePreferenceController.class)).init(mo959getLifecycle(), this.mSubId))));
        CdmaSystemSelectPreferenceController cdmaSystemSelectPreferenceController = (CdmaSystemSelectPreferenceController) use(CdmaSystemSelectPreferenceController.class);
        this.mCdmaSystemSelectPreferenceController = cdmaSystemSelectPreferenceController;
        cdmaSystemSelectPreferenceController.init(getPreferenceManager(), this.mSubId);
        CdmaSubscriptionPreferenceController cdmaSubscriptionPreferenceController = (CdmaSubscriptionPreferenceController) use(CdmaSubscriptionPreferenceController.class);
        this.mCdmaSubscriptionPreferenceController = cdmaSubscriptionPreferenceController;
        cdmaSubscriptionPreferenceController.init(getPreferenceManager(), this.mSubId);
        VideoCallingPreferenceController init2 = ((VideoCallingPreferenceController) use(VideoCallingPreferenceController.class)).init(this.mSubId);
        BackupCallingPreferenceController init3 = ((BackupCallingPreferenceController) use(BackupCallingPreferenceController.class)).init(this.mSubId);
        ((Enabled5GPreferenceController) use(Enabled5GPreferenceController.class)).init(this.mSubId);
        ((CallingPreferenceCategoryController) use(CallingPreferenceCategoryController.class)).setChildren(Arrays.asList(init, init2, init3));
        ((Enhanced4gLtePreferenceController) use(Enhanced4gLtePreferenceController.class)).init(this.mSubId).addListener(init2);
        ((Enhanced4gCallingPreferenceController) use(Enhanced4gCallingPreferenceController.class)).init(this.mSubId).addListener(init2);
        ((Enhanced4gAdvancedCallingPreferenceController) use(Enhanced4gAdvancedCallingPreferenceController.class)).init(this.mSubId).addListener(init2);
        ((ContactDiscoveryPreferenceController) use(ContactDiscoveryPreferenceController.class)).init(getParentFragmentManager(), this.mSubId, mo959getLifecycle());
        ((NrAdvancedCallingPreferenceController) use(NrAdvancedCallingPreferenceController.class)).init(this.mSubId);
        ((SmartFiveGPreferenceController) use(SmartFiveGPreferenceController.class)).init(this.mSubId);
        ((SAPreferenceController) use(SAPreferenceController.class)).init(this.mSubId);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        Log.i("NetworkSettings", "onCreate:+");
        TelephonyStatusControlSession telephonyAvailabilityStatus = setTelephonyAvailabilityStatus(getPreferenceControllersAsList());
        super.onCreate(bundle);
        Context context = getContext();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        telephonyAvailabilityStatus.close();
        onRestoreInstance(bundle);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        Log.i("NetworkSettings", "onResume:+");
        super.onResume();
        Log.d("NetworkSettings", "onResume() subId=" + this.mSubId);
        if (this.mActiveSubscriptionsListener == null) {
            this.mActiveSubscriptionsListener = new ActiveSubscriptionsListener(getContext().getMainLooper(), getContext(), this.mSubId) { // from class: com.android.settings.network.telephony.MobileNetworkSettings.3
                @Override // com.android.settings.network.ActiveSubscriptionsListener
                public void onChanged() {
                    MobileNetworkSettings.this.onSubscriptionDetailChanged();
                }
            };
            this.mDropFirstSubscriptionChangeNotify = true;
        }
        this.mActiveSubscriptionsListener.start();
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
            context.registerReceiver(this.mAirplaneStateReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
            return;
        }
        Log.i("NetworkSettings", "context is null, not registering SimStateReceiver");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubscriptionDetailChanged() {
        if (this.mDropFirstSubscriptionChangeNotify) {
            this.mDropFirstSubscriptionChangeNotify = false;
            Log.d("NetworkSettings", "Callback during onResume()");
            return;
        }
        int i = this.mActiveSubscriptionsListenerCount + 1;
        this.mActiveSubscriptionsListenerCount = i;
        if (i != 1) {
            return;
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settings.network.telephony.MobileNetworkSettings$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MobileNetworkSettings.this.lambda$onSubscriptionDetailChanged$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSubscriptionDetailChanged$0() {
        this.mActiveSubscriptionsListenerCount = 0;
        redrawPreferenceControllers();
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        ActiveSubscriptionsListener activeSubscriptionsListener = this.mActiveSubscriptionsListener;
        if (activeSubscriptionsListener != null) {
            activeSubscriptionsListener.stop();
        }
        super.onDestroy();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        Log.i("NetworkSettings", "onPause:+");
        super.onPause();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(this.mSimStateReceiver);
            context.unregisterReceiver(this.mAirplaneStateReceiver);
            return;
        }
        Log.i("NetworkSettings", "context already null, not unregistering Receivers");
    }

    void onRestoreInstance(Bundle bundle) {
        if (bundle != null) {
            this.mClickedPrefKey = bundle.getString(KEY_CLICKED_PREF);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.mobile_network_settings;
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(KEY_CLICKED_PREF, this.mClickedPrefKey);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        Preference findPreference;
        FragmentActivity activity;
        if (i == 17) {
            if (i2 == 0 || (findPreference = getPreferenceScreen().findPreference(this.mClickedPrefKey)) == null) {
                return;
            }
            findPreference.performClick();
        } else if (i != 18 || i2 == 0 || (activity = getActivity()) == null || activity.isFinishing()) {
        } else {
            activity.finish();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            MenuItem add = menu.add(0, R.id.edit_sim_name, 0, R.string.mobile_network_sim_name);
            add.setIcon(17302768);
            add.setShowAsAction(2);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId) && menuItem.getItemId() == R.id.edit_sim_name) {
            RenameMobileNetworkDialogFragment.newInstance(this.mSubId).show(getFragmentManager(), "RenameMobileNetwork");
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
