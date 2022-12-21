package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.Html;
import com.android.internal.util.Preconditions;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.statusbar.connectivity.WifiSignalControllerEx;
import java.p026io.PrintWriter;

public class WifiSignalController extends SignalController<WifiState, SignalIcon.IconGroup> {
    private final Handler mBgHandler;
    private final SignalIcon.MobileIconGroup mCarrierMergedWifiIconGroup = TelephonyIcons.CARRIER_MERGED_WIFI;
    private final SignalIcon.IconGroup mDefaultWifiIconGroup;
    private final boolean mHasMobileDataFeature;
    private final SignalIcon.IconGroup mUnmergedWifiIconGroup = WifiIcons.UNMERGED_WIFI;
    private final SignalIcon.IconGroup mWifi4IconGroup;
    private final SignalIcon.IconGroup mWifi5IconGroup;
    private final SignalIcon.IconGroup mWifi6IconGroup;
    private final WifiManager mWifiManager;
    private final WifiStatusTracker mWifiTracker;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WifiSignalController(Context context, boolean z, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, WifiManager wifiManager, WifiStatusTrackerFactory wifiStatusTrackerFactory, @Background Handler handler) {
        super("WifiSignalController", context, 1, callbackHandler, networkControllerImpl);
        WifiManager wifiManager2 = wifiManager;
        Handler handler2 = handler;
        this.mBgHandler = handler2;
        this.mWifiManager = wifiManager2;
        WifiStatusTracker createTracker = wifiStatusTrackerFactory.createTracker(new WifiSignalController$$ExternalSyntheticLambda2(this), handler2);
        this.mWifiTracker = createTracker;
        createTracker.setListening(true);
        this.mHasMobileDataFeature = z;
        if (wifiManager2 != null) {
            wifiManager2.registerTrafficStateCallback(context.getMainExecutor(), new WifiTrafficStateCallback());
        }
        SignalIcon.IconGroup iconGroup = new SignalIcon.IconGroup("Wi-Fi Icons", WifiIcons.WIFI_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_SIGNAL_STRENGTH, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302913, 17302913, 17302913, 17302913, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
        this.mDefaultWifiIconGroup = iconGroup;
        this.mWifi4IconGroup = new SignalIcon.IconGroup("Wi-Fi 4 Icons", WifiIcons.WIFI_4_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_4_SIGNAL_STRENGTH, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302913, 17302913, 17302913, 17302913, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
        this.mWifi5IconGroup = new SignalIcon.IconGroup("Wi-Fi 5 Icons", WifiIcons.WIFI_5_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_5_SIGNAL_STRENGTH, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302913, 17302913, 17302913, 17302913, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
        this.mWifi6IconGroup = new SignalIcon.IconGroup("Wi-Fi 6 Icons", WifiIcons.WIFI_6_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_6_SIGNAL_STRENGTH, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302913, 17302913, 17302913, 17302913, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
        ((WifiState) this.mLastState).iconGroup = iconGroup;
        ((WifiState) this.mCurrentState).iconGroup = iconGroup;
    }

    /* access modifiers changed from: protected */
    public WifiState cleanState() {
        return new WifiState();
    }

    /* access modifiers changed from: package-private */
    public void refreshLocale() {
        this.mWifiTracker.refreshLocale();
    }

    public void notifyListeners(SignalCallback signalCallback) {
        if (!((WifiState) this.mCurrentState).isCarrierMerged) {
            notifyListenersForNonCarrierWifi(signalCallback);
        } else if (((WifiState) this.mCurrentState).isDefault || !this.mNetworkController.isRadioOn()) {
            notifyListenersForCarrierWifi(signalCallback);
        }
    }

    private void notifyListenersForNonCarrierWifi(SignalCallback signalCallback) {
        int i;
        boolean z = ((WifiState) this.mCurrentState).enabled && ((((WifiState) this.mCurrentState).connected && ((WifiState) this.mCurrentState).inetCondition == 1) || !this.mHasMobileDataFeature || ((WifiState) this.mCurrentState).isDefault || this.mContext.getResources().getBoolean(C1893R.bool.config_showWifiIndicatorWhenEnabled));
        String str = ((WifiState) this.mCurrentState).connected ? ((WifiState) this.mCurrentState).ssid : null;
        boolean z2 = z && ((WifiState) this.mCurrentState).ssid != null;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            charSequence = charSequence + NavigationBarInflaterView.BUTTON_SEPARATOR + this.mContext.getString(C1893R.string.data_connection_no_internet);
        }
        IconState iconState = new IconState(z, getCurrentIconId(), charSequence);
        boolean z3 = ((WifiState) this.mCurrentState).connected;
        if (this.mWifiTracker.isCaptivePortal) {
            i = C1893R.C1895drawable.ic_qs_wifi_disconnected;
        } else {
            i = getQsCurrentIconId();
        }
        signalCallback.setWifiIndicators(new WifiIndicators(((WifiState) this.mCurrentState).enabled, iconState, new IconState(z3, i, charSequence), z2 && ((WifiState) this.mCurrentState).activityIn, z2 && ((WifiState) this.mCurrentState).activityOut, str, ((WifiState) this.mCurrentState).isTransient, ((WifiState) this.mCurrentState).statusLabel));
        this.mNetworkController.notifyInternetTuningChanged(QSTileHostEx.getWifiSpec(), z);
    }

    private void notifyListenersForCarrierWifi(SignalCallback signalCallback) {
        int i;
        IconState iconState;
        SignalIcon.MobileIconGroup mobileIconGroup = this.mCarrierMergedWifiIconGroup;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        CharSequence textIfExists = getTextIfExists(mobileIconGroup.dataContentDescription);
        String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            obj = this.mContext.getString(C1893R.string.data_connection_no_internet);
        }
        String str = obj;
        boolean z = ((WifiState) this.mCurrentState).enabled && ((WifiState) this.mCurrentState).connected && ((WifiState) this.mCurrentState).isDefault;
        IconState iconState2 = new IconState(z, getCurrentIconIdForCarrierWifi(), charSequence);
        int i2 = z ? mobileIconGroup.dataType : 0;
        if (z) {
            i = mobileIconGroup.dataType;
            iconState = new IconState(((WifiState) this.mCurrentState).connected, getQsCurrentIconIdForCarrierWifi(), charSequence);
        } else {
            iconState = null;
            i = 0;
        }
        signalCallback.setMobileDataIndicators(new MobileDataIndicators(iconState2, iconState, i2, i, ((WifiState) this.mCurrentState).activityIn, ((WifiState) this.mCurrentState).activityOut, 0, str, textIfExists, this.mNetworkController.getNetworkNameForCarrierWiFi(((WifiState) this.mCurrentState).subId), ((WifiState) this.mCurrentState).subId, false, true, false));
        this.mNetworkController.notifyInternetTuningChanged(QSTileHostEx.getWifiSpec(), z);
    }

    private int getCurrentIconIdForCarrierWifi() {
        int i = ((WifiState) this.mCurrentState).level;
        boolean z = true;
        int maxSignalLevel = this.mWifiManager.getMaxSignalLevel() + 1;
        if (((WifiState) this.mCurrentState).inetCondition != 0) {
            z = false;
        }
        if (((WifiState) this.mCurrentState).connected) {
            return SignalDrawable.getState(i, maxSignalLevel, z);
        }
        if (((WifiState) this.mCurrentState).enabled) {
            return SignalDrawable.getEmptyState(maxSignalLevel);
        }
        return 0;
    }

    private int getQsCurrentIconIdForCarrierWifi() {
        return getCurrentIconIdForCarrierWifi();
    }

    private void updateIconGroup() {
        if (((WifiState) this.mCurrentState).wifiStandard == 4) {
            ((WifiState) this.mCurrentState).iconGroup = this.mWifi4IconGroup;
        } else if (((WifiState) this.mCurrentState).wifiStandard == 5) {
            ((WifiState) this.mCurrentState).iconGroup = this.mWifi5IconGroup;
        } else if (((WifiState) this.mCurrentState).wifiStandard == 6) {
            ((WifiState) this.mCurrentState).iconGroup = this.mWifi6IconGroup;
        } else {
            ((WifiState) this.mCurrentState).iconGroup = this.mDefaultWifiIconGroup;
        }
    }

    public void fetchInitialState() {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fetchInitialState$0$com-android-systemui-statusbar-connectivity-WifiSignalController */
    public /* synthetic */ void mo39457xdd29ecb1() {
        this.mWifiTracker.fetchInitialState();
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: package-private */
    public void handleBroadcast(Intent intent) {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda0(this, intent));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleBroadcast$1$com-android-systemui-statusbar-connectivity-WifiSignalController */
    public /* synthetic */ void mo39458xcac5a6de(Intent intent) {
        this.mWifiTracker.handleBroadcast(intent);
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: private */
    public void handleStatusUpdated() {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleStatusUpdated$2$com-android-systemui-statusbar-connectivity-WifiSignalController */
    public /* synthetic */ void mo39459xd672f7d5() {
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    private void doInBackground(Runnable runnable) {
        if (Thread.currentThread() != this.mBgHandler.getLooper().getThread()) {
            this.mBgHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void copyWifiStates() {
        Preconditions.checkState(this.mBgHandler.getLooper().isCurrentThread());
        ((WifiState) this.mCurrentState).enabled = this.mWifiTracker.enabled;
        ((WifiState) this.mCurrentState).isDefault = this.mWifiTracker.isDefaultNetwork;
        ((WifiState) this.mCurrentState).connected = this.mWifiTracker.connected;
        ((WifiState) this.mCurrentState).ssid = this.mWifiTracker.ssid;
        ((WifiState) this.mCurrentState).rssi = this.mWifiTracker.rssi;
        boolean z = ((WifiState) this.mCurrentState).level != this.mWifiTracker.level;
        ((WifiState) this.mCurrentState).level = this.mWifiTracker.level;
        ((WifiState) this.mCurrentState).statusLabel = this.mWifiTracker.statusLabel;
        ((WifiState) this.mCurrentState).isCarrierMerged = this.mWifiTracker.isCarrierMerged;
        ((WifiState) this.mCurrentState).subId = this.mWifiTracker.subId;
        ((WifiState) this.mCurrentState).wifiStandard = this.mWifiTracker.wifiStandard;
        updateIconGroup();
        if (z) {
            this.mNetworkController.notifyWifiLevelChange(((WifiState) this.mCurrentState).level);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCarrierMergedWifi(int i) {
        return ((WifiState) this.mCurrentState).isDefault && ((WifiState) this.mCurrentState).isCarrierMerged && ((WifiState) this.mCurrentState).subId == i;
    }

    /* access modifiers changed from: package-private */
    public void setActivity(int i) {
        boolean z = false;
        ((WifiState) this.mCurrentState).activityIn = i == 3 || i == 1;
        WifiState wifiState = (WifiState) this.mCurrentState;
        if (i == 3 || i == 2) {
            z = true;
        }
        wifiState.activityOut = z;
        notifyListenersIfNecessary();
    }

    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        this.mWifiTracker.dump(printWriter);
        dumpTableData(printWriter);
    }

    private class WifiTrafficStateCallback implements WifiManager.TrafficStateCallback {
        private WifiTrafficStateCallback() {
        }

        public void onStateChanged(int i) {
            WifiSignalController.this.setActivity(i);
        }
    }

    public int getCurrentIconId() {
        return ((WifiSignalControllerEx) NTDependencyEx.get(WifiSignalControllerEx.class)).getCurrentIconId((WifiState) this.mCurrentState, getIcons(), this.mDefaultWifiIconGroup);
    }
}
