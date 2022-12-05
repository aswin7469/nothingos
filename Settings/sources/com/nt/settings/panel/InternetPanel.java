package com.nt.settings.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.network.InternetUpdater;
import com.android.settings.network.ProviderModelSliceHelper;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.DataConnectivityListener;
import com.android.settings.panel.PanelContentCallback;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class InternetPanel extends SettingsLifecycleObserver implements NtPanelContent, InternetUpdater.InternetChangeListener, DataConnectivityListener.Client, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static List<String> SETTINGS;
    private PanelContentCallback mCallback;
    private DataConnectivityListener mConnectivityListener;
    private SettingContentRegistry mContentRegistry;
    private final Context mContext;
    private int mDefaultDataSubid;
    protected HandlerInjector mHandlerInjector;
    InternetUpdater mInternetUpdater;
    protected boolean mIsProgressBarVisible;
    protected boolean mIsScanningSubTitleShownOnce;
    ProviderModelSliceHelper mProviderModelSliceHelper;
    private SubscriptionsChangeListener mSubscriptionsListener;
    private TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;
    private final IntentFilter mWifiStateFilter;
    private final Handler mWorkHandler;
    private final HandlerThread mWorkTH;
    private static final int SUBTITLE_TEXT_WIFI_IS_OFF = R.string.wifi_is_off;
    private static final int SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT = R.string.tap_a_network_to_connect;
    private static final int SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS = R.string.wifi_empty_list_wifi_on;
    private static final int SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE = R.string.non_carrier_network_unavailable;
    private static final int SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE = R.string.all_network_unavailable;
    private final BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() { // from class: com.nt.settings.panel.InternetPanel.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.equals(action, "android.net.wifi.SCAN_RESULTS")) {
                InternetPanel.this.updateProgressBar();
                InternetPanel.this.updatePanelTitle();
            } else if (!TextUtils.equals(action, "android.net.wifi.STATE_CHANGE")) {
            } else {
                InternetPanel.this.updateProgressBar();
                InternetPanel.this.updatePanelTitle();
            }
        }
    };
    private int mSubtitle = -1;
    protected Runnable mHideProgressBarRunnable = new Runnable() { // from class: com.nt.settings.panel.InternetPanel$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            InternetPanel.this.lambda$new$0();
        }
    };
    protected Runnable mHideScanningSubTitleRunnable = new Runnable() { // from class: com.nt.settings.panel.InternetPanel$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            InternetPanel.this.lambda$new$1();
        }
    };
    boolean mIsProviderModelEnabled = true;
    private final NetworkProviderTelephonyCallback mTelephonyCallback = new NetworkProviderTelephonyCallback();

    static {
        ArrayList arrayList = new ArrayList();
        SETTINGS = arrayList;
        arrayList.add("mobile_data");
        SETTINGS.add("hot_pot");
        SETTINGS.add("wifi");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setProgressBarVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mIsScanningSubTitleShownOnce = true;
        updatePanelTitle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class HandlerInjector {
        protected final Handler mHandler;

        HandlerInjector(Handler handler) {
            this.mHandler = handler;
        }

        public void postDelay(Runnable runnable) {
            this.mHandler.postDelayed(runnable, 2000L);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public InternetPanel(Context context, SettingContentRegistry settingContentRegistry) {
        this.mDefaultDataSubid = -1;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mContentRegistry = settingContentRegistry;
        HandlerThread handlerThread = new HandlerThread("InternetPanel");
        this.mWorkTH = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        this.mWorkHandler = handler;
        this.mHandlerInjector = new HandlerInjector(handler);
        this.mInternetUpdater = new InternetUpdater(context, null, this);
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
        this.mConnectivityListener = new DataConnectivityListener(context, this);
        this.mDefaultDataSubid = getDefaultDataSubscriptionId();
        this.mTelephonyManager = (TelephonyManager) applicationContext.getSystemService(TelephonyManager.class);
        this.mWifiManager = (WifiManager) applicationContext.getSystemService(WifiManager.class);
        IntentFilter intentFilter = new IntentFilter();
        this.mWifiStateFilter = intentFilter;
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mProviderModelSliceHelper = new ProviderModelSliceHelper(applicationContext, null);
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onResume() {
        super.onResume();
        if (!this.mIsProviderModelEnabled) {
            return;
        }
        this.mInternetUpdater.onResume();
        this.mSubscriptionsListener.start();
        this.mConnectivityListener.start();
        this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mWorkHandler), this.mTelephonyCallback);
        this.mContext.registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter, null, this.mWorkHandler);
        this.mWorkHandler.post(new Runnable() { // from class: com.nt.settings.panel.InternetPanel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                InternetPanel.this.lambda$onResume$2();
            }
        });
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onResume();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$2() {
        updateProgressBar();
        updatePanelTitle();
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onPause() {
        super.onPause();
        if (!this.mIsProviderModelEnabled) {
            return;
        }
        this.mInternetUpdater.onPause();
        this.mSubscriptionsListener.stop();
        this.mConnectivityListener.stop();
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
        this.mContext.unregisterReceiver(this.mWifiStateReceiver);
        this.mHandlerInjector.removeCallbacks(this.mHideProgressBarRunnable);
        this.mHandlerInjector.removeCallbacks(this.mHideScanningSubTitleRunnable);
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onPause();
        }
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public CharSequence getTitle() {
        if (this.mIsProviderModelEnabled) {
            return this.mContext.getText(this.mInternetUpdater.isAirplaneModeOn() ? R.string.airplane_mode : R.string.provider_internet_settings);
        }
        return this.mContext.getText(R.string.internet_connectivity_panel_title);
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public CharSequence getSubTitle() {
        int i;
        if (!this.mIsProviderModelEnabled || (i = this.mSubtitle) == -1) {
            return null;
        }
        return this.mContext.getText(i);
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public boolean isProgressBarVisible() {
        return this.mIsProgressBarVisible;
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public void registerCallback(PanelContentCallback panelContentCallback) {
        this.mCallback = panelContentCallback;
    }

    @Override // com.android.settings.network.InternetUpdater.InternetChangeListener
    public void onAirplaneModeChanged(boolean z) {
        log("onAirplaneModeChanged: isAirplaneModeOn:" + z);
        updatePanelTitle();
    }

    @Override // com.android.settings.network.InternetUpdater.InternetChangeListener
    public void onWifiEnabledChanged(boolean z) {
        log("onWifiEnabledChanged: enabled:" + z);
        updatePanelTitle();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        int defaultDataSubscriptionId = getDefaultDataSubscriptionId();
        log("onSubscriptionsChanged: defaultDataSubId:" + defaultDataSubscriptionId);
        if (this.mDefaultDataSubid == defaultDataSubscriptionId) {
            return;
        }
        if (SubscriptionManager.isUsableSubscriptionId(defaultDataSubscriptionId)) {
            this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
        }
        updatePanelTitle();
    }

    @Override // com.android.settings.network.telephony.DataConnectivityListener.Client
    public void onDataConnectivityChange() {
        log("onDataConnectivityChange");
        updatePanelTitle();
    }

    void updatePanelTitle() {
        if (this.mCallback == null) {
            return;
        }
        updateSubtitleText();
        this.mCallback.onHeaderChanged();
    }

    int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    private void updateSubtitleText() {
        this.mSubtitle = -1;
        if (this.mInternetUpdater.isAirplaneModeOn()) {
            log("Airplane mode on");
        } else if (!this.mInternetUpdater.isWifiEnabled()) {
            log("Airplane mode off + Wi-Fi off.");
            this.mSubtitle = SUBTITLE_TEXT_WIFI_IS_OFF;
        } else {
            List<SettingItemData> dates = this.mContentRegistry.getContentProvider(this.mContext, "wifi").getDates();
            if (dates != null && dates.size() != 0) {
                log("Airplane mode off + Wi-Fi on + Wi-Fi items");
                this.mSubtitle = SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT;
            } else if (!this.mIsScanningSubTitleShownOnce && this.mIsProgressBarVisible) {
                log("Airplane mode off + Wi-Fi on + no Wi-Fi items");
                this.mSubtitle = SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS;
            } else {
                log("No Wi-Fi item.");
                if (!this.mProviderModelSliceHelper.hasCarrier() || (!this.mProviderModelSliceHelper.isVoiceStateInService() && !this.mProviderModelSliceHelper.isDataStateInService())) {
                    log("no carrier or out of service.");
                    this.mSubtitle = SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE;
                } else if (!this.mProviderModelSliceHelper.isMobileDataEnabled()) {
                    log("mobile data off");
                    this.mSubtitle = SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE;
                } else if (!this.mProviderModelSliceHelper.isDataSimActive()) {
                    log("no carrier data.");
                    this.mSubtitle = SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE;
                } else {
                    this.mSubtitle = SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE;
                }
            }
        }
    }

    protected void updateProgressBar() {
        if (this.mWifiManager == null || !this.mInternetUpdater.isWifiEnabled()) {
            setProgressBarVisible(false);
            return;
        }
        setProgressBarVisible(true);
        List<SettingItemData> dates = this.mContentRegistry.getContentProvider(this.mContext, "wifi").getDates();
        if (dates != null && dates.size() > 0) {
            this.mHandlerInjector.postDelay(this.mHideProgressBarRunnable);
        } else if (this.mIsScanningSubTitleShownOnce) {
        } else {
            this.mHandlerInjector.postDelay(this.mHideScanningSubTitleRunnable);
        }
    }

    protected void setProgressBarVisible(boolean z) {
        if (this.mIsProgressBarVisible == z) {
            return;
        }
        this.mIsProgressBarVisible = z;
        PanelContentCallback panelContentCallback = this.mCallback;
        if (panelContentCallback == null) {
            return;
        }
        panelContentCallback.onProgressBarVisibleChanged();
        updatePanelTitle();
    }

    /* loaded from: classes2.dex */
    private class NetworkProviderTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.ServiceStateListener {
        private NetworkProviderTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            InternetPanel.log("onServiceStateChanged voiceState=" + serviceState.getState() + " dataState=" + serviceState.getDataRegistrationState());
            InternetPanel.this.updatePanelTitle();
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            InternetPanel.log("onDataConnectionStateChanged: networkType=" + i2 + " state=" + i);
            InternetPanel.this.updatePanelTitle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("InternetPanel", str);
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onCreate() {
        super.onCreate();
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onCreate();
        }
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onDestroy() {
        super.onDestroy();
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onDestroy();
        }
        this.mWorkTH.quit();
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public List<String> getLists() {
        return SETTINGS;
    }
}
