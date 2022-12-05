package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.text.Html;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.SignalIcon$State;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.NetworkController;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes2.dex */
public class WifiSignalController extends SignalController<WifiState, SignalIcon$IconGroup> {
    private final SignalIcon$IconGroup mDefaultWifiIconGroup;
    private final boolean mHasMobileDataFeature;
    private final boolean mProviderModelSetting;
    private final SignalIcon$IconGroup mWifi4IconGroup;
    private final SignalIcon$IconGroup mWifi5IconGroup;
    private final SignalIcon$IconGroup mWifi6IconGroup;
    private final WifiManager mWifiManager;
    private final WifiStatusTracker mWifiTracker;
    private final SignalIcon$IconGroup mUnmergedWifiIconGroup = WifiIcons.UNMERGED_WIFI;
    private final SignalIcon$MobileIconGroup mCarrierMergedWifiIconGroup = TelephonyIcons.CARRIER_MERGED_WIFI;

    public WifiSignalController(Context context, boolean z, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, FeatureFlags featureFlags) {
        super("WifiSignalController", context, 1, callbackHandler, networkControllerImpl);
        this.mWifiManager = wifiManager;
        WifiStatusTracker wifiStatusTracker = new WifiStatusTracker(this.mContext, wifiManager, networkScoreManager, connectivityManager, new Runnable() { // from class: com.android.systemui.statusbar.policy.WifiSignalController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WifiSignalController.this.handleStatusUpdated();
            }
        });
        this.mWifiTracker = wifiStatusTracker;
        wifiStatusTracker.setListening(true);
        this.mHasMobileDataFeature = z;
        if (wifiManager != null) {
            wifiManager.registerTrafficStateCallback(context.getMainExecutor(), new WifiTrafficStateCallback());
        }
        int[][] iArr = WifiIcons.WIFI_SIGNAL_STRENGTH;
        int[][] iArr2 = WifiIcons.QS_WIFI_SIGNAL_STRENGTH;
        int[] iArr3 = AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH;
        int i = AccessibilityContentDescriptions.WIFI_NO_CONNECTION;
        SignalIcon$IconGroup signalIcon$IconGroup = new SignalIcon$IconGroup("Wi-Fi Icons", iArr, iArr2, iArr3, 17302902, 17302902, 17302902, 17302902, i);
        this.mDefaultWifiIconGroup = signalIcon$IconGroup;
        this.mWifi4IconGroup = new SignalIcon$IconGroup("Wi-Fi 4 Icons", WifiIcons.WIFI_4_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_4_SIGNAL_STRENGTH, iArr3, 17302902, 17302902, 17302902, 17302902, i);
        this.mWifi5IconGroup = new SignalIcon$IconGroup("Wi-Fi 5 Icons", WifiIcons.WIFI_5_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_5_SIGNAL_STRENGTH, iArr3, 17302902, 17302902, 17302902, 17302902, i);
        this.mWifi6IconGroup = new SignalIcon$IconGroup("Wi-Fi 6 Icons", WifiIcons.WIFI_6_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_6_SIGNAL_STRENGTH, iArr3, 17302902, 17302902, 17302902, 17302902, i);
        ((WifiState) this.mLastState).iconGroup = signalIcon$IconGroup;
        ((WifiState) this.mCurrentState).iconGroup = signalIcon$IconGroup;
        this.mProviderModelSetting = featureFlags.isProviderModelSettingEnabled();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.policy.SignalController
    /* renamed from: cleanState  reason: collision with other method in class */
    public WifiState mo1340cleanState() {
        return new WifiState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshLocale() {
        this.mWifiTracker.refreshLocale();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void notifyListeners(NetworkController.SignalCallback signalCallback) {
        T t = this.mCurrentState;
        if (((WifiState) t).isCarrierMerged) {
            if (!((WifiState) t).isDefault) {
                return;
            }
            notifyListenersForCarrierWifi(signalCallback);
            return;
        }
        notifyListenersForNonCarrierWifi(signalCallback);
    }

    private void notifyListenersForNonCarrierWifi(NetworkController.SignalCallback signalCallback) {
        boolean z = this.mContext.getResources().getBoolean(R$bool.config_showWifiIndicatorWhenEnabled);
        T t = this.mCurrentState;
        boolean z2 = ((WifiState) t).enabled && ((((WifiState) t).connected && ((WifiState) t).inetCondition == 1) || !this.mHasMobileDataFeature || ((WifiState) t).isDefault || z);
        NetworkController.IconState iconState = null;
        String str = ((WifiState) t).connected ? ((WifiState) t).ssid : null;
        boolean z3 = z2 && ((WifiState) t).ssid != null;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            charSequence = charSequence + "," + this.mContext.getString(R$string.data_connection_no_internet);
        }
        if (this.mProviderModelSetting) {
            NetworkController.IconState iconState2 = new NetworkController.IconState(z2, getCurrentIconId(), charSequence);
            if (((WifiState) this.mCurrentState).isDefault || (!this.mNetworkController.isRadioOn() && !this.mNetworkController.isEthernetDefault())) {
                iconState = new NetworkController.IconState(((WifiState) this.mCurrentState).connected, this.mWifiTracker.isCaptivePortal ? R$drawable.ic_qs_wifi_disconnected : getQsCurrentIconId(), charSequence);
            }
            NetworkController.IconState iconState3 = iconState;
            T t2 = this.mCurrentState;
            signalCallback.setWifiIndicators(new NetworkController.WifiIndicators(((WifiState) t2).enabled, iconState2, iconState3, z3 && ((WifiState) t2).activityIn, z3 && ((WifiState) t2).activityOut, str, ((WifiState) t2).isTransient, ((WifiState) t2).statusLabel));
            return;
        }
        NetworkController.IconState iconState4 = new NetworkController.IconState(z2, getCurrentIconId(), charSequence);
        NetworkController.IconState iconState5 = new NetworkController.IconState(((WifiState) this.mCurrentState).connected, this.mWifiTracker.isCaptivePortal ? R$drawable.ic_qs_wifi_disconnected : getQsCurrentIconId(), charSequence);
        T t3 = this.mCurrentState;
        signalCallback.setWifiIndicators(new NetworkController.WifiIndicators(((WifiState) t3).enabled, iconState4, iconState5, z3 && ((WifiState) t3).activityIn, z3 && ((WifiState) t3).activityOut, str, ((WifiState) t3).isTransient, ((WifiState) t3).statusLabel));
    }

    private void notifyListenersForCarrierWifi(NetworkController.SignalCallback signalCallback) {
        int i;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = this.mCarrierMergedWifiIconGroup;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        CharSequence textIfExists = getTextIfExists(signalIcon$MobileIconGroup.dataContentDescription);
        String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            obj = this.mContext.getString(R$string.data_connection_no_internet);
        }
        String str = obj;
        T t = this.mCurrentState;
        boolean z = ((WifiState) t).enabled && ((WifiState) t).connected && ((WifiState) t).isDefault;
        NetworkController.IconState iconState = new NetworkController.IconState(z, getCurrentIconIdForCarrierWifi(), charSequence);
        int i2 = z ? signalIcon$MobileIconGroup.dataType : 0;
        NetworkController.IconState iconState2 = null;
        if (z) {
            i = signalIcon$MobileIconGroup.qsDataType;
            iconState2 = new NetworkController.IconState(((WifiState) this.mCurrentState).connected, getQsCurrentIconIdForCarrierWifi(), charSequence);
        } else {
            i = 0;
        }
        String networkNameForCarrierWiFi = this.mNetworkController.getNetworkNameForCarrierWiFi(((WifiState) this.mCurrentState).subId);
        T t2 = this.mCurrentState;
        signalCallback.setMobileDataIndicators(new NetworkController.MobileDataIndicators(iconState, iconState2, i2, i, ((WifiState) t2).activityIn, ((WifiState) t2).activityOut, 0, str, textIfExists, networkNameForCarrierWiFi, signalIcon$MobileIconGroup.isWide, ((WifiState) t2).subId, false, true));
    }

    private int getCurrentIconIdForCarrierWifi() {
        int i = ((WifiState) this.mCurrentState).level;
        boolean z = true;
        int maxSignalLevel = this.mWifiManager.getMaxSignalLevel() + 1;
        T t = this.mCurrentState;
        if (((WifiState) t).inetCondition != 0) {
            z = false;
        }
        if (((WifiState) t).connected) {
            return SignalDrawable.getState(i, maxSignalLevel, z);
        }
        if (!((WifiState) t).enabled) {
            return 0;
        }
        return SignalDrawable.getEmptyState(maxSignalLevel);
    }

    private int getQsCurrentIconIdForCarrierWifi() {
        return getCurrentIconIdForCarrierWifi();
    }

    private void updateIconGroup() {
        T t = this.mCurrentState;
        if (((WifiState) t).wifiStandard == 4) {
            ((WifiState) t).iconGroup = this.mWifi4IconGroup;
        } else if (((WifiState) t).wifiStandard == 5) {
            ((WifiState) t).iconGroup = ((WifiState) t).isReady ? this.mWifi6IconGroup : this.mWifi5IconGroup;
        } else if (((WifiState) t).wifiStandard == 6) {
            ((WifiState) t).iconGroup = this.mWifi6IconGroup;
        } else {
            ((WifiState) t).iconGroup = this.mDefaultWifiIconGroup;
        }
    }

    public void fetchInitialState() {
        this.mWifiTracker.fetchInitialState();
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    public void handleBroadcast(Intent intent) {
        this.mWifiTracker.handleBroadcast(intent);
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStatusUpdated() {
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    private void copyWifiStates() {
        T t = this.mCurrentState;
        WifiStatusTracker wifiStatusTracker = this.mWifiTracker;
        ((WifiState) t).enabled = wifiStatusTracker.enabled;
        ((WifiState) t).isDefault = wifiStatusTracker.isDefaultNetwork;
        ((WifiState) t).connected = wifiStatusTracker.connected;
        ((WifiState) t).ssid = wifiStatusTracker.ssid;
        ((WifiState) t).rssi = wifiStatusTracker.rssi;
        notifyWifiLevelChangeIfNecessary(wifiStatusTracker.level);
        T t2 = this.mCurrentState;
        WifiStatusTracker wifiStatusTracker2 = this.mWifiTracker;
        ((WifiState) t2).level = wifiStatusTracker2.level;
        ((WifiState) t2).statusLabel = wifiStatusTracker2.statusLabel;
        ((WifiState) t2).isCarrierMerged = wifiStatusTracker2.isCarrierMerged;
        ((WifiState) t2).subId = wifiStatusTracker2.subId;
        ((WifiState) t2).wifiStandard = wifiStatusTracker2.wifiStandard;
        ((WifiState) t2).isReady = wifiStatusTracker2.vhtMax8SpatialStreamsSupport && wifiStatusTracker2.he8ssCapableAp;
        updateIconGroup();
    }

    void notifyWifiLevelChangeIfNecessary(int i) {
        if (i != ((WifiState) this.mCurrentState).level) {
            this.mNetworkController.notifyWifiLevelChange(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCarrierMergedWifi(int i) {
        T t = this.mCurrentState;
        return ((WifiState) t).isDefault && ((WifiState) t).isCarrierMerged && ((WifiState) t).subId == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setActivity(int i) {
        T t = this.mCurrentState;
        boolean z = false;
        ((WifiState) t).activityIn = i == 3 || i == 1;
        WifiState wifiState = (WifiState) t;
        if (i == 3 || i == 2) {
            z = true;
        }
        wifiState.activityOut = z;
        notifyListenersIfNecessary();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        this.mWifiTracker.dump(printWriter);
    }

    /* loaded from: classes2.dex */
    private class WifiTrafficStateCallback implements WifiManager.TrafficStateCallback {
        private WifiTrafficStateCallback() {
        }

        public void onStateChanged(int i) {
            WifiSignalController.this.setActivity(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class WifiState extends SignalIcon$State {
        public boolean isCarrierMerged;
        public boolean isDefault;
        public boolean isReady;
        public boolean isTransient;
        public String ssid;
        public String statusLabel;
        public int subId;
        public int wifiStandard;

        WifiState() {
        }

        @Override // com.android.settingslib.SignalIcon$State
        public void copyFrom(SignalIcon$State signalIcon$State) {
            super.copyFrom(signalIcon$State);
            WifiState wifiState = (WifiState) signalIcon$State;
            this.ssid = wifiState.ssid;
            this.wifiStandard = wifiState.wifiStandard;
            this.isReady = wifiState.isReady;
            this.isTransient = wifiState.isTransient;
            this.isDefault = wifiState.isDefault;
            this.statusLabel = wifiState.statusLabel;
            this.isCarrierMerged = wifiState.isCarrierMerged;
            this.subId = wifiState.subId;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settingslib.SignalIcon$State
        public void toString(StringBuilder sb) {
            super.toString(sb);
            sb.append(",ssid=");
            sb.append(this.ssid);
            sb.append(",wifiStandard=");
            sb.append(this.wifiStandard);
            sb.append(",isReady=");
            sb.append(this.isReady);
            sb.append(",isTransient=");
            sb.append(this.isTransient);
            sb.append(",isDefault=");
            sb.append(this.isDefault);
            sb.append(",statusLabel=");
            sb.append(this.statusLabel);
            sb.append(",isCarrierMerged=");
            sb.append(this.isCarrierMerged);
            sb.append(",subId=");
            sb.append(this.subId);
        }

        @Override // com.android.settingslib.SignalIcon$State
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            WifiState wifiState = (WifiState) obj;
            return Objects.equals(wifiState.ssid, this.ssid) && wifiState.wifiStandard == this.wifiStandard && wifiState.isReady == this.isReady && wifiState.isTransient == this.isTransient && wifiState.isDefault == this.isDefault && TextUtils.equals(wifiState.statusLabel, this.statusLabel) && wifiState.isCarrierMerged == this.isCarrierMerged && wifiState.subId == this.subId;
        }
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public int getCurrentIconId() {
        SignalIcon$IconGroup icons = getIcons();
        int i = ((WifiState) this.mCurrentState).wifiStandard;
        if (i == 4 || i == 5 || i == 6) {
            icons = this.mDefaultWifiIconGroup;
        }
        return getSBCurrentIconId(icons);
    }

    private int getSBCurrentIconId(SignalIcon$IconGroup signalIcon$IconGroup) {
        T t = this.mCurrentState;
        if (((WifiState) t).connected) {
            return signalIcon$IconGroup.sbIcons[((WifiState) t).inetCondition][((WifiState) t).level];
        }
        if (((WifiState) t).enabled) {
            return signalIcon$IconGroup.sbDiscState;
        }
        return signalIcon$IconGroup.sbNullState;
    }
}
