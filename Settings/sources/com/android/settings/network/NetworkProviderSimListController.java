package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NetworkProviderSimListController extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private SubscriptionsChangeListener mChangeListener;
    final BroadcastReceiver mDataSubscriptionChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                NetworkProviderSimListController.this.update();
            }
        }
    };
    private PreferenceCategory mPreferenceCategory;
    private Map<Integer, Preference> mPreferences;
    private SubscriptionManager mSubscriptionManager;

    public String getPreferenceKey() {
        return "provider_model_sim_list";
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public NetworkProviderSimListController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        this.mPreferences = new ArrayMap();
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mDataSubscriptionChangedReceiver, intentFilter);
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
        BroadcastReceiver broadcastReceiver = this.mDataSubscriptionChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference("provider_model_sim_category");
        update();
    }

    /* access modifiers changed from: private */
    public void update() {
        if (this.mPreferenceCategory != null) {
            Map<Integer, Preference> map = this.mPreferences;
            this.mPreferences = new ArrayMap();
            for (SubscriptionInfo next : getAvailablePhysicalSubscription()) {
                int subscriptionId = next.getSubscriptionId();
                Preference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new Preference(this.mPreferenceCategory.getContext());
                    this.mPreferenceCategory.addPreference(remove);
                }
                CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(next, this.mContext);
                CharSequence ntUniqueSubscriptionDisplayName = SubscriptionUtil.getNtUniqueSubscriptionDisplayName(next, this.mContext, true);
                String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, next);
                remove.setTitle(ntUniqueSubscriptionDisplayName);
                remove.setSummary(getSummary(subscriptionId, uniqueSubscriptionDisplayName, bidiFormattedPhoneNumber));
                remove.setOnPreferenceClickListener(new NetworkProviderSimListController$$ExternalSyntheticLambda0(this, subscriptionId, next));
                this.mPreferences.put(Integer.valueOf(subscriptionId), remove);
            }
            adjustPreferenceSequenceIfNeeded();
            for (Preference removePreference : map.values()) {
                this.mPreferenceCategory.removePreference(removePreference);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$0(int i, SubscriptionInfo subscriptionInfo, Preference preference) {
        if (this.mSubscriptionManager.isActiveSubscriptionId(i) || SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
            MobileNetworkUtils.launchMobileNetworkSettings(this.mContext, subscriptionInfo);
        } else {
            SubscriptionUtil.startToggleSubscriptionDialogActivity(this.mContext, i, true);
        }
        return true;
    }

    private void adjustPreferenceSequenceIfNeeded() {
        PreferenceCategory preferenceCategory = this.mPreferenceCategory;
        if (preferenceCategory != null && preferenceCategory.getPreferenceCount() == 2) {
            Preference preference = this.mPreferenceCategory.getPreference(0);
            Preference preference2 = this.mPreferenceCategory.getPreference(1);
            int i = -1;
            int i2 = -1;
            int i3 = -1;
            int i4 = -1;
            for (Preference next : this.mPreferences.values()) {
                if (next == preference) {
                    i = ((Integer) ((Map) this.mPreferences.entrySet().stream().collect(Collectors.toMap(new NetworkProviderSimListController$$ExternalSyntheticLambda1(), new NetworkProviderSimListController$$ExternalSyntheticLambda2()))).get(next)).intValue();
                    i2 = SubscriptionManager.getPhoneId(i);
                }
                if (next == preference2) {
                    i3 = ((Integer) ((Map) this.mPreferences.entrySet().stream().collect(Collectors.toMap(new NetworkProviderSimListController$$ExternalSyntheticLambda3(), new NetworkProviderSimListController$$ExternalSyntheticLambda4()))).get(next)).intValue();
                    i4 = SubscriptionManager.getPhoneId(i3);
                }
            }
            Log.d("NetworkProviderSimListCtrl", "subId1 = " + i + ", slotId1 = " + i2 + ", subId2 = " + i3 + ", slotId2 = " + i4);
            if (i2 > i4) {
                this.mPreferenceCategory.removePreference(preference);
                this.mPreferences.remove(Integer.valueOf(i));
                Preference copy = copy(preference);
                this.mPreferenceCategory.addPreference(copy);
                this.mPreferences.put(Integer.valueOf(i), copy);
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Preference lambda$adjustPreferenceSequenceIfNeeded$1(Map.Entry entry) {
        return (Preference) entry.getValue();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$adjustPreferenceSequenceIfNeeded$2(Map.Entry entry) {
        return (Integer) entry.getKey();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Preference lambda$adjustPreferenceSequenceIfNeeded$3(Map.Entry entry) {
        return (Preference) entry.getValue();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$adjustPreferenceSequenceIfNeeded$4(Map.Entry entry) {
        return (Integer) entry.getKey();
    }

    private Preference copy(Preference preference) {
        if (preference == null) {
            return null;
        }
        Preference preference2 = new Preference(this.mPreferenceCategory.getContext());
        preference2.setTitle(preference.getTitle());
        preference2.setSummary(preference.getSummary());
        preference2.setOnPreferenceClickListener(preference.getOnPreferenceClickListener());
        return preference2;
    }

    public boolean isAvailable() {
        return !getAvailablePhysicalSubscription().isEmpty();
    }

    /* access modifiers changed from: protected */
    public List<SubscriptionInfo> getAvailablePhysicalSubscription() {
        ArrayList arrayList = new ArrayList();
        for (SubscriptionInfo next : SubscriptionUtil.getAvailableSubscriptions(this.mContext)) {
            if (!next.isEmbedded()) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public void onSubscriptionsChanged() {
        update();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(this.mPreferenceCategory);
        update();
    }

    /* access modifiers changed from: protected */
    public int getDefaultVoiceSubscriptionId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public int getDefaultSmsSubscriptionId() {
        return SubscriptionManager.getDefaultSmsSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    public CharSequence getSummary(int i, CharSequence charSequence, CharSequence charSequence2) {
        if (this.mSubscriptionManager.isActiveSubscriptionId(i)) {
            if (!TextUtils.isEmpty(charSequence2)) {
                return charSequence2;
            }
            CharSequence defaultSimConfig = SubscriptionUtil.getDefaultSimConfig(this.mContext, i);
            String string = this.mContext.getResources().getString(R$string.sim_category_active_sim);
            if (defaultSimConfig == null) {
                return string;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(defaultSimConfig);
            return sb;
        } else if (SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
            return this.mContext.getString(R$string.sim_category_inactive_sim);
        } else {
            return this.mContext.getString(R$string.mobile_network_tap_to_activate, new Object[]{charSequence});
        }
    }
}
