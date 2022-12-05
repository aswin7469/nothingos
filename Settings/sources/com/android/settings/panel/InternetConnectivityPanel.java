package com.android.settings.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.R;
import com.android.settings.network.AirplaneModePreferenceController;
import com.android.settings.network.InternetUpdater;
import com.android.settings.network.ProviderModelSliceHelper;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.DataConnectivityListener;
import com.android.settings.slices.CustomSliceRegistry;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class InternetConnectivityPanel implements PanelContent, LifecycleObserver, InternetUpdater.InternetChangeListener, DataConnectivityListener.Client, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private PanelContentCallback mCallback;
    private DataConnectivityListener mConnectivityListener;
    private final Context mContext;
    private int mDefaultDataSubid;
    protected HandlerInjector mHandlerInjector;
    protected Runnable mHideProgressBarRunnable;
    protected Runnable mHideScanningSubTitleRunnable;
    InternetUpdater mInternetUpdater;
    protected boolean mIsProgressBarVisible;
    boolean mIsProviderModelEnabled;
    protected boolean mIsScanningSubTitleShownOnce;
    ProviderModelSliceHelper mProviderModelSliceHelper;
    private SubscriptionsChangeListener mSubscriptionsListener;
    private int mSubtitle;
    private final NetworkProviderTelephonyCallback mTelephonyCallback;
    private TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;
    private final IntentFilter mWifiStateFilter;
    private final BroadcastReceiver mWifiStateReceiver;
    private static final int SUBTITLE_TEXT_WIFI_IS_OFF = R.string.wifi_is_off;
    private static final int SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT = R.string.tap_a_network_to_connect;
    private static final int SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS = R.string.wifi_empty_list_wifi_on;
    private static final int SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE = R.string.non_carrier_network_unavailable;
    private static final int SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE = R.string.all_network_unavailable;

    /* loaded from: classes.dex */
    private class NetworkProviderTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.ServiceStateListener {
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1654;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class HandlerInjector {
        protected final Handler mHandler;

        public void postDelay(Runnable runnable) {
            this.mHandler.postDelayed(runnable, 2000L);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (!this.mIsProviderModelEnabled) {
            return;
        }
        this.mInternetUpdater.onResume();
        this.mSubscriptionsListener.start();
        this.mConnectivityListener.start();
        this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
        this.mContext.registerReceiver(this.mWifiStateReceiver, this.mWifiStateFilter);
        updateProgressBar();
        updatePanelTitle();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
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
    }

    @Override // com.android.settings.panel.PanelContent
    public CharSequence getTitle() {
        if (this.mIsProviderModelEnabled) {
            return this.mContext.getText(this.mInternetUpdater.isAirplaneModeOn() ? R.string.airplane_mode : R.string.provider_internet_settings);
        }
        return this.mContext.getText(R.string.internet_connectivity_panel_title);
    }

    @Override // com.android.settings.panel.PanelContent
    public CharSequence getSubTitle() {
        int i;
        if (!this.mIsProviderModelEnabled || (i = this.mSubtitle) == -1) {
            return null;
        }
        return this.mContext.getText(i);
    }

    @Override // com.android.settings.panel.PanelContent
    public List<Uri> getSlices() {
        ArrayList arrayList = new ArrayList();
        if (this.mIsProviderModelEnabled) {
            arrayList.add(CustomSliceRegistry.PROVIDER_MODEL_SLICE_URI);
        } else {
            arrayList.add(CustomSliceRegistry.WIFI_SLICE_URI);
            arrayList.add(CustomSliceRegistry.MOBILE_DATA_SLICE_URI);
            arrayList.add(AirplaneModePreferenceController.SLICE_URI);
        }
        return arrayList;
    }

    @Override // com.android.settings.panel.PanelContent
    public Intent getSeeMoreIntent() {
        if (this.mIsProviderModelEnabled) {
            return null;
        }
        return new Intent("android.settings.WIRELESS_SETTINGS").addFlags(268435456);
    }

    @Override // com.android.settings.panel.PanelContent
    public boolean isProgressBarVisible() {
        return this.mIsProgressBarVisible;
    }

    @Override // com.android.settings.panel.PanelContent
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
        if (!this.mInternetUpdater.isWifiEnabled()) {
            if (this.mInternetUpdater.isAirplaneModeOn()) {
                return;
            }
            log("Airplane mode off + Wi-Fi off.");
            this.mSubtitle = SUBTITLE_TEXT_WIFI_IS_OFF;
        } else if (this.mInternetUpdater.isAirplaneModeOn()) {
        } else {
            List<ScanResult> scanResults = this.mWifiManager.getScanResults();
            if (scanResults != null && scanResults.size() != 0) {
                this.mSubtitle = SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT;
            } else if (!this.mIsScanningSubTitleShownOnce && this.mIsProgressBarVisible) {
                this.mSubtitle = SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS;
            } else {
                log("No Wi-Fi item.");
                if (!this.mProviderModelSliceHelper.hasCarrier() || (!this.mProviderModelSliceHelper.isVoiceStateInService() && !this.mProviderModelSliceHelper.isDataStateInService())) {
                    log("no carrier or service is out of service.");
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
        List<ScanResult> scanResults = this.mWifiManager.getScanResults();
        if (scanResults != null && scanResults.size() > 0) {
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

    private static void log(String str) {
        Log.d("InternetConnectivityPanel", str);
    }
}
