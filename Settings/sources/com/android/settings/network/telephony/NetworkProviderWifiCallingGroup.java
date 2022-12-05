package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class NetworkProviderWifiCallingGroup extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    protected CarrierConfigManager mCarrierConfigManager;
    private PreferenceGroup mPreferenceGroup;
    private String mPreferenceGroupKey;
    private List<SubscriptionInfo> mSubInfoListForWfc;
    private SubscriptionManager mSubscriptionManager;
    private Map<Integer, TelephonyManager> mTelephonyManagerList = new HashMap();
    private Map<Integer, PhoneAccountHandle> mSimCallManagerList = new HashMap();
    private Map<Integer, Preference> mWifiCallingForSubPreferences = new ArrayMap();

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "provider_model_wfc_group";
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    public NetworkProviderWifiCallingGroup(Context context, Lifecycle lifecycle, String str) {
        super(context);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mPreferenceGroupKey = str;
        setSubscriptionInfoList(context);
        lifecycle.addObserver(this);
    }

    private void setSubscriptionInfoList(final Context context) {
        ArrayList arrayList = new ArrayList(SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager));
        this.mSubInfoListForWfc = arrayList;
        arrayList.removeIf(new Predicate() { // from class: com.android.settings.network.telephony.NetworkProviderWifiCallingGroup$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$setSubscriptionInfoList$0;
                lambda$setSubscriptionInfoList$0 = NetworkProviderWifiCallingGroup.this.lambda$setSubscriptionInfoList$0(context, (SubscriptionInfo) obj);
                return lambda$setSubscriptionInfoList$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setSubscriptionInfoList$0(Context context, SubscriptionInfo subscriptionInfo) {
        PreferenceGroup preferenceGroup;
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        if (MobileNetworkUtils.getWLANCallTitle(this.mContext, subscriptionId) && (preferenceGroup = this.mPreferenceGroup) != null) {
            preferenceGroup.setTitle(R.string.wlan_call_settings_title);
            Log.d("NetworkProviderWifiCallingGroup", "setSubscriptionInfoList wfcTitle=" + ((Object) this.mPreferenceGroup.getTitle()) + " subId" + subscriptionId);
        }
        setTelephonyManagerForSubscriptionId(context, subscriptionId);
        setPhoneAccountHandleForSubscriptionId(context, subscriptionId);
        return !shouldShowWifiCallingForSub(subscriptionId) && this.mSubInfoListForWfc.contains(subscriptionInfo);
    }

    private void setTelephonyManagerForSubscriptionId(Context context, int i) {
        this.mTelephonyManagerList.put(Integer.valueOf(i), ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i));
    }

    private void setPhoneAccountHandleForSubscriptionId(Context context, int i) {
        this.mSimCallManagerList.put(Integer.valueOf(i), ((TelecomManager) context.getSystemService(TelecomManager.class)).getSimCallManagerForSubscription(i));
    }

    private TelephonyManager getTelephonyManagerForSubscriptionId(int i) {
        return this.mTelephonyManagerList.get(Integer.valueOf(i));
    }

    protected PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int i) {
        return this.mSimCallManagerList.get(Integer.valueOf(i));
    }

    protected WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(this.mContext, i);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        update();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        List<SubscriptionInfo> list = this.mSubInfoListForWfc;
        if (list != null) {
            return list.size() >= 1;
        }
        Log.d("NetworkProviderWifiCallingGroup", "No active subscriptions, hide the controller");
        return false;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceGroupKey);
        update();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference == null) {
            return;
        }
        update();
    }

    private void update() {
        if (this.mPreferenceGroup == null) {
            return;
        }
        setSubscriptionInfoList(this.mContext);
        if (!isAvailable()) {
            for (Preference preference : this.mWifiCallingForSubPreferences.values()) {
                this.mPreferenceGroup.removePreference(preference);
            }
            this.mWifiCallingForSubPreferences.clear();
            return;
        }
        Map<Integer, Preference> map = this.mWifiCallingForSubPreferences;
        this.mWifiCallingForSubPreferences = new ArrayMap();
        setSubscriptionInfoForPreference(map);
        for (Preference preference2 : map.values()) {
            this.mPreferenceGroup.removePreference(preference2);
        }
    }

    private void setSubscriptionInfoForPreference(Map<Integer, Preference> map) {
        Intent buildPhoneAccountConfigureIntent;
        int i = 10;
        for (final SubscriptionInfo subscriptionInfo : this.mSubInfoListForWfc) {
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            if (shouldShowWifiCallingForSub(subscriptionId)) {
                Preference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new Preference(this.mPreferenceGroup.getContext());
                    this.mPreferenceGroup.addPreference(remove);
                }
                CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext);
                boolean z = false;
                if (getPhoneAccountHandleForSubscriptionId(subscriptionId) != null && (buildPhoneAccountConfigureIntent = MobileNetworkUtils.buildPhoneAccountConfigureIntent(this.mContext, getPhoneAccountHandleForSubscriptionId(subscriptionId))) != null) {
                    PackageManager packageManager = this.mContext.getPackageManager();
                    uniqueSubscriptionDisplayName = packageManager.queryIntentActivities(buildPhoneAccountConfigureIntent, 0).get(0).loadLabel(packageManager);
                    remove.setIntent(buildPhoneAccountConfigureIntent);
                }
                remove.setTitle(uniqueSubscriptionDisplayName);
                remove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.network.telephony.NetworkProviderWifiCallingGroup$$ExternalSyntheticLambda0
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public final boolean onPreferenceClick(Preference preference) {
                        boolean lambda$setSubscriptionInfoForPreference$1;
                        lambda$setSubscriptionInfoForPreference$1 = NetworkProviderWifiCallingGroup.this.lambda$setSubscriptionInfoForPreference$1(subscriptionInfo, preference);
                        return lambda$setSubscriptionInfoForPreference$1;
                    }
                });
                if (getTelephonyManagerForSubscriptionId(subscriptionId).getCallState() == 0) {
                    z = true;
                }
                remove.setEnabled(z);
                int i2 = i + 1;
                remove.setOrder(i);
                int i3 = 17041649;
                if (queryImsState(subscriptionId).isEnabledByUser()) {
                    i3 = R.string.calls_sms_wfc_summary;
                }
                remove.setSummary(i3);
                this.mWifiCallingForSubPreferences.put(Integer.valueOf(subscriptionId), remove);
                i = i2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setSubscriptionInfoForPreference$1(SubscriptionInfo subscriptionInfo, Preference preference) {
        Intent intent = new Intent(this.mContext, Settings.WifiCallingSettingsActivity.class);
        intent.putExtra("android.provider.extra.SUB_ID", subscriptionInfo.getSubscriptionId());
        this.mContext.startActivity(intent);
        return true;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        update();
    }

    protected boolean shouldShowWifiCallingForSub(int i) {
        return SubscriptionManager.isValidSubscriptionId(i) && MobileNetworkUtils.isWifiCallingEnabled(this.mContext, i, queryImsState(i), getPhoneAccountHandleForSubscriptionId(i)) && isWifiCallingAvailableForCarrier(i);
    }

    private boolean isWifiCallingAvailableForCarrier(int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("carrier_wfc_ims_available_bool");
    }
}
