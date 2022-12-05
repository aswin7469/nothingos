package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import androidx.collection.ArrayMap;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.network.MobileDataEnabledListener;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.DataConnectivityListener;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.SignalStrengthListener;
import com.android.settings.network.telephony.TelephonyDisplayInfoListener;
import com.android.settings.widget.MutableGearPreference;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.net.SignalStrengthUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class SubscriptionsPreferenceController extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient, MobileDataEnabledListener.Client, DataConnectivityListener.Client, SignalStrengthListener.Callback, TelephonyDisplayInfoListener.Callback {
    private MobileMappings.Config mConfig;
    private DataConnectivityListener mConnectivityListener;
    private MobileDataEnabledListener mDataEnabledListener;
    private PreferenceGroup mPreferenceGroup;
    private String mPreferenceGroupKey;
    private SignalStrengthListener mSignalStrengthListener;
    private int mStartOrder;
    private MutableGearPreference mSubsGearPref;
    private SubsPrefCtrlInjector mSubsPrefCtrlInjector;
    private SubscriptionManager mSubscriptionManager;
    private SubscriptionsChangeListener mSubscriptionsListener;
    private TelephonyDisplayInfoListener mTelephonyDisplayInfoListener;
    private TelephonyManager mTelephonyManager;
    private UpdateListener mUpdateListener;
    private WifiPickerTrackerHelper mWifiPickerTrackerHelper;
    final BroadcastReceiver mConnectionChangeReceiver = new BroadcastReceiver() { // from class: com.android.settings.network.SubscriptionsPreferenceController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                SubscriptionsPreferenceController subscriptionsPreferenceController = SubscriptionsPreferenceController.this;
                subscriptionsPreferenceController.mConfig = subscriptionsPreferenceController.mSubsPrefCtrlInjector.getConfig(((AbstractPreferenceController) SubscriptionsPreferenceController.this).mContext);
                SubscriptionsPreferenceController.this.update();
            } else if (!action.equals("android.net.wifi.supplicant.CONNECTION_CHANGE")) {
            } else {
                SubscriptionsPreferenceController.this.update();
            }
        }
    };
    private TelephonyDisplayInfo mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    private Map<Integer, Preference> mSubscriptionPreferences = new ArrayMap();

    /* loaded from: classes.dex */
    public interface UpdateListener {
        void onChildrenUpdated();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return null;
    }

    public SubscriptionsPreferenceController(Context context, Lifecycle lifecycle, UpdateListener updateListener, String str, int i) {
        super(context);
        this.mConfig = null;
        this.mUpdateListener = updateListener;
        this.mPreferenceGroupKey = str;
        this.mStartOrder = i;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
        this.mDataEnabledListener = new MobileDataEnabledListener(context, this);
        this.mConnectivityListener = new DataConnectivityListener(context, this);
        this.mSignalStrengthListener = new SignalStrengthListener(context, this);
        this.mTelephonyDisplayInfoListener = new TelephonyDisplayInfoListener(context, this);
        lifecycle.addObserver(this);
        SubsPrefCtrlInjector createSubsPrefCtrlInjector = createSubsPrefCtrlInjector();
        this.mSubsPrefCtrlInjector = createSubsPrefCtrlInjector;
        this.mConfig = createSubsPrefCtrlInjector.getConfig(this.mContext);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        this.mContext.registerReceiver(this.mConnectionChangeReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        BroadcastReceiver broadcastReceiver = this.mConnectionChangeReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mSubscriptionsListener.start();
        this.mDataEnabledListener.start(this.mSubsPrefCtrlInjector.getDefaultDataSubscriptionId());
        this.mConnectivityListener.start();
        this.mSignalStrengthListener.resume();
        this.mTelephonyDisplayInfoListener.resume();
        registerReceiver();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mSubscriptionsListener.stop();
        this.mDataEnabledListener.stop();
        this.mConnectivityListener.stop();
        this.mSignalStrengthListener.pause();
        this.mTelephonyDisplayInfoListener.pause();
        unRegisterReceiver();
        resetProviderPreferenceSummary();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceGroupKey);
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        if (this.mPreferenceGroup == null) {
            return;
        }
        if (!isAvailable()) {
            MutableGearPreference mutableGearPreference = this.mSubsGearPref;
            if (mutableGearPreference != null) {
                this.mPreferenceGroup.removePreference(mutableGearPreference);
            }
            for (Preference preference : this.mSubscriptionPreferences.values()) {
                this.mPreferenceGroup.removePreference(preference);
            }
            this.mSubscriptionPreferences.clear();
            this.mSignalStrengthListener.updateSubscriptionIds(Collections.emptySet());
            this.mTelephonyDisplayInfoListener.updateSubscriptionIds(Collections.emptySet());
            this.mUpdateListener.onChildrenUpdated();
            return;
        }
        updateForBase();
    }

    private void resetProviderPreferenceSummary() {
        MutableGearPreference mutableGearPreference = this.mSubsGearPref;
        if (mutableGearPreference == null) {
            return;
        }
        mutableGearPreference.setSummary("");
    }

    private void updateForBase() {
        Map<Integer, Preference> map = this.mSubscriptionPreferences;
        this.mSubscriptionPreferences = new ArrayMap();
        int i = this.mStartOrder;
        ArraySet arraySet = new ArraySet();
        int defaultDataSubscriptionId = this.mSubsPrefCtrlInjector.getDefaultDataSubscriptionId();
        for (SubscriptionInfo subscriptionInfo : SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager)) {
            final int subscriptionId = subscriptionInfo.getSubscriptionId();
            if (this.mSubsPrefCtrlInjector.canSubscriptionBeDisplayed(this.mContext, subscriptionId)) {
                arraySet.add(Integer.valueOf(subscriptionId));
                Preference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new Preference(this.mPreferenceGroup.getContext());
                    this.mPreferenceGroup.addPreference(remove);
                }
                remove.setTitle(SubscriptionUtil.getDisplayName(subscriptionInfo));
                boolean z = subscriptionId == defaultDataSubscriptionId;
                remove.setSummary(getSimNumber(subscriptionInfo));
                setIcon(remove, subscriptionId, z);
                remove.setOrder(i);
                remove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.network.SubscriptionsPreferenceController$$ExternalSyntheticLambda0
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public final boolean onPreferenceClick(Preference preference) {
                        boolean lambda$updateForBase$2;
                        lambda$updateForBase$2 = SubscriptionsPreferenceController.this.lambda$updateForBase$2(subscriptionId, preference);
                        return lambda$updateForBase$2;
                    }
                });
                this.mSubscriptionPreferences.put(Integer.valueOf(subscriptionId), remove);
                i++;
            }
        }
        this.mSignalStrengthListener.updateSubscriptionIds(arraySet);
        for (Preference preference : map.values()) {
            this.mPreferenceGroup.removePreference(preference);
        }
        this.mUpdateListener.onChildrenUpdated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateForBase$2(int i, Preference preference) {
        startMobileNetworkActivity(this.mContext, i);
        return true;
    }

    private static void startMobileNetworkActivity(Context context, int i) {
        Intent intent = new Intent(context, MobileNetworkActivity.class);
        intent.putExtra("android.provider.extra.SUB_ID", i);
        context.startActivity(intent);
    }

    boolean shouldInflateSignalStrength(int i) {
        return SignalStrengthUtil.shouldInflateSignalStrength(this.mContext, i);
    }

    void setIcon(Preference preference, int i, boolean z) {
        TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
        SignalStrength signalStrength = createForSubscriptionId.getSignalStrength();
        boolean z2 = false;
        int level = signalStrength == null ? 0 : signalStrength.getLevel();
        int i2 = 5;
        if (shouldInflateSignalStrength(i)) {
            level++;
            i2 = 6;
        }
        if (!z || !createForSubscriptionId.isDataEnabled()) {
            z2 = true;
        }
        preference.setIcon(this.mSubsPrefCtrlInjector.getIcon(this.mContext, level, i2, z2));
    }

    protected String getSimNumber(SubscriptionInfo subscriptionInfo) {
        return DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, subscriptionInfo);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        List<SubscriptionInfo> activeSubscriptions;
        if (!this.mSubscriptionsListener.isAirplaneModeOn() && (activeSubscriptions = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager)) != null) {
            return activeSubscriptions.stream().filter(new Predicate() { // from class: com.android.settings.network.SubscriptionsPreferenceController$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$isAvailable$3;
                    lambda$isAvailable$3 = SubscriptionsPreferenceController.this.lambda$isAvailable$3((SubscriptionInfo) obj);
                    return lambda$isAvailable$3;
                }
            }).count() >= ((long) (this.mSubsPrefCtrlInjector.isProviderModelEnabled(this.mContext) ? 1 : 2));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isAvailable$3(SubscriptionInfo subscriptionInfo) {
        return this.mSubsPrefCtrlInjector.canSubscriptionBeDisplayed(this.mContext, subscriptionInfo.getSubscriptionId());
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        int defaultDataSubscriptionId = this.mSubsPrefCtrlInjector.getDefaultDataSubscriptionId();
        if (defaultDataSubscriptionId != this.mDataEnabledListener.getSubId()) {
            this.mDataEnabledListener.stop();
            this.mDataEnabledListener.start(defaultDataSubscriptionId);
        }
        update();
    }

    @Override // com.android.settings.network.MobileDataEnabledListener.Client
    public void onMobileDataEnabledChange() {
        update();
    }

    @Override // com.android.settings.network.telephony.DataConnectivityListener.Client
    public void onDataConnectivityChange() {
        update();
    }

    @Override // com.android.settings.network.telephony.SignalStrengthListener.Callback
    public void onSignalStrengthChanged() {
        update();
    }

    @Override // com.android.settings.network.telephony.TelephonyDisplayInfoListener.Callback
    public void onTelephonyDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
        this.mTelephonyDisplayInfo = telephonyDisplayInfo;
        update();
    }

    boolean canSubscriptionBeDisplayed(Context context, int i) {
        return SubscriptionUtil.getAvailableSubscription(context, ProxySubscriptionManager.getInstance(context), i) != null;
    }

    public void setWifiPickerTrackerHelper(WifiPickerTrackerHelper wifiPickerTrackerHelper) {
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
    }

    public void connectCarrierNetwork() {
        WifiPickerTrackerHelper wifiPickerTrackerHelper;
        if (MobileNetworkUtils.isMobileDataEnabled(this.mContext) && (wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper) != null) {
            wifiPickerTrackerHelper.connectCarrierNetwork(null);
        }
    }

    SubsPrefCtrlInjector createSubsPrefCtrlInjector() {
        return new SubsPrefCtrlInjector();
    }

    /* loaded from: classes.dex */
    public static class SubsPrefCtrlInjector {
        public boolean canSubscriptionBeDisplayed(Context context, int i) {
            return SubscriptionUtil.getAvailableSubscription(context, ProxySubscriptionManager.getInstance(context), i) != null;
        }

        public int getDefaultDataSubscriptionId() {
            return SubscriptionManager.getDefaultDataSubscriptionId();
        }

        public boolean isProviderModelEnabled(Context context) {
            return Utils.isProviderModelEnabled(context);
        }

        public MobileMappings.Config getConfig(Context context) {
            return MobileMappings.Config.readConfig(context);
        }

        public Drawable getIcon(Context context, int i, int i2, boolean z) {
            return MobileNetworkUtils.getSignalStrengthIcon(context, i, i2, 0, z);
        }
    }
}
