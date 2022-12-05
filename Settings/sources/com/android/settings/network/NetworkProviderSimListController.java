package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class NetworkProviderSimListController extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private SubscriptionsChangeListener mChangeListener;
    private PreferenceCategory mPreferenceCategory;
    private SubscriptionManager mSubscriptionManager;
    final BroadcastReceiver mDataSubscriptionChangedReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.NetworkProviderSimListController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                NetworkProviderSimListController.this.update();
            }
        }
    };
    private Map<Integer, Preference> mPreferences = new ArrayMap();

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "provider_model_sim_list";
    }

    public NetworkProviderSimListController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
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

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference("provider_model_sim_category");
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        if (this.mPreferenceCategory == null) {
            return;
        }
        if (WirelessUtils.isAirplaneModeOn(this.mContext)) {
            this.mPreferenceCategory.setEnabled(false);
        } else {
            this.mPreferenceCategory.setEnabled(true);
        }
        Map<Integer, Preference> map = this.mPreferences;
        this.mPreferences = new ArrayMap();
        for (final SubscriptionInfo subscriptionInfo : getAvailablePhysicalSubscription()) {
            final int subscriptionId = subscriptionInfo.getSubscriptionId();
            Preference remove = map.remove(Integer.valueOf(subscriptionId));
            if (remove == null) {
                remove = new Preference(this.mPreferenceCategory.getContext());
                this.mPreferenceCategory.addPreference(remove);
            }
            remove.setTitle(MobileNetworkUtils.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext));
            remove.setSummary(DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, subscriptionInfo));
            remove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.network.NetworkProviderSimListController$$ExternalSyntheticLambda0
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public final boolean onPreferenceClick(Preference preference) {
                    boolean lambda$update$0;
                    lambda$update$0 = NetworkProviderSimListController.this.lambda$update$0(subscriptionId, subscriptionInfo, preference);
                    return lambda$update$0;
                }
            });
            this.mPreferences.put(Integer.valueOf(subscriptionId), remove);
        }
        for (Preference preference : map.values()) {
            this.mPreferenceCategory.removePreference(preference);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$0(int i, SubscriptionInfo subscriptionInfo, Preference preference) {
        if (!this.mSubscriptionManager.isActiveSubscriptionId(i) && !SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
            SubscriptionUtil.startToggleSubscriptionDialogActivity(this.mContext, i, true);
        } else {
            Intent intent = new Intent(this.mContext, MobileNetworkActivity.class);
            intent.putExtra("android.provider.extra.SUB_ID", subscriptionInfo.getSubscriptionId());
            this.mContext.startActivity(intent);
        }
        return true;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return !getAvailablePhysicalSubscription().isEmpty();
    }

    protected List<SubscriptionInfo> getAvailablePhysicalSubscription() {
        ArrayList arrayList = new ArrayList();
        for (SubscriptionInfo subscriptionInfo : SubscriptionUtil.getAvailableSubscriptions(this.mContext)) {
            if (!subscriptionInfo.isEmbedded()) {
                arrayList.add(subscriptionInfo);
            }
        }
        return arrayList;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        update();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(this.mPreferenceCategory);
        update();
    }

    protected int getDefaultVoiceSubscriptionId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    protected int getDefaultSmsSubscriptionId() {
        return SubscriptionManager.getDefaultSmsSubscriptionId();
    }

    protected int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }
}
