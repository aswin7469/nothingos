package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class OsuWifiEntry extends WifiEntry {
    private final Context mContext;
    private final List<ScanResult> mCurrentScanResults = new ArrayList();
    private boolean mIsAlreadyProvisioned = false;
    private final String mKey;
    private final OsuProvider mOsuProvider;
    private String mOsuStatusString;
    private String mSsid;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.wifitrackerlib.WifiEntry
    public String getScanResultDescription() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OsuWifiEntry(Context context, Handler handler, OsuProvider osuProvider, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        Preconditions.checkNotNull(osuProvider, "Cannot construct with null osuProvider!");
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        this.mKey = osuProviderToOsuWifiEntryKey(osuProvider);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public String getKey() {
        return this.mKey;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized String getTitle() {
        String friendlyName = this.mOsuProvider.getFriendlyName();
        if (!TextUtils.isEmpty(friendlyName)) {
            return friendlyName;
        }
        if (!TextUtils.isEmpty(this.mSsid)) {
            return this.mSsid;
        }
        Uri serverUri = this.mOsuProvider.getServerUri();
        if (serverUri == null) {
            return "";
        }
        return serverUri.toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized String getSummary(boolean z) {
        String string;
        String str = this.mOsuStatusString;
        if (str != null) {
            return str;
        }
        if (!isAlreadyProvisioned()) {
            return this.mContext.getString(R$string.wifitrackerlib_tap_to_sign_up);
        }
        if (z) {
            string = this.mContext.getString(R$string.wifitrackerlib_wifi_passpoint_expired);
        } else {
            string = this.mContext.getString(R$string.wifitrackerlib_tap_to_renew_subscription_and_connect);
        }
        return string;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized String getSsid() {
        return this.mSsid;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startSubscriptionProvisioning(this.mOsuProvider, this.mContext.getMainExecutor(), new OsuWifiEntryProvisioningCallback());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mCurrentScanResults.clear();
        this.mCurrentScanResults.addAll(list);
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(list);
        if (bestScanResultByLevel != null) {
            updateTransitionModeCapa(bestScanResultByLevel);
            this.mSsid = bestScanResultByLevel.SSID;
            if (getConnectedState() == 0) {
                this.mLevel = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
            }
        } else {
            this.mLevel = -1;
        }
        updateWifiGenerationInfo(this.mCurrentScanResults);
        notifyOnUpdated();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String osuProviderToOsuWifiEntryKey(OsuProvider osuProvider) {
        Preconditions.checkNotNull(osuProvider, "Cannot create key with null OsuProvider!");
        return "OsuWifiEntry:" + osuProvider.getFriendlyName() + "," + osuProvider.getServerUri().toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    protected boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isOsuAp() && TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mOsuProvider.getFriendlyName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OsuProvider getOsuProvider() {
        return this.mOsuProvider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAlreadyProvisioned() {
        return this.mIsAlreadyProvisioned;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAlreadyProvisioned(boolean z) {
        this.mIsAlreadyProvisioned = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class OsuWifiEntryProvisioningCallback extends ProvisioningCallback {
        OsuWifiEntryProvisioningCallback() {
        }

        public void onProvisioningFailure(int i) {
            synchronized (OsuWifiEntry.this) {
                if (TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up))) {
                    OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                    osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_failed);
                } else {
                    OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
                    osuWifiEntry2.mOsuStatusString = osuWifiEntry2.mContext.getString(R$string.wifitrackerlib_osu_connect_failed);
                }
            }
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
            OsuWifiEntry.this.notifyOnUpdated();
        }

        public void onProvisioningStatus(int i) {
            String format;
            boolean z = false;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    format = String.format(OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_opening_provider), OsuWifiEntry.this.getTitle());
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    format = OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up);
                    break;
                default:
                    format = null;
                    break;
            }
            synchronized (OsuWifiEntry.this) {
                if (!TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, format)) {
                    z = true;
                }
                OsuWifiEntry.this.mOsuStatusString = format;
                if (z) {
                    OsuWifiEntry.this.notifyOnUpdated();
                }
            }
        }

        public void onProvisioningComplete() {
            ScanResult bestScanResultByLevel;
            synchronized (OsuWifiEntry.this) {
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_complete);
            }
            OsuWifiEntry.this.notifyOnUpdated();
            OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
            PasspointConfiguration passpointConfiguration = (PasspointConfiguration) osuWifiEntry2.mWifiManager.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(osuWifiEntry2.mOsuProvider)).get(OsuWifiEntry.this.mOsuProvider);
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (passpointConfiguration == null) {
                if (connectCallback == null) {
                    return;
                }
                connectCallback.onConnectResult(2);
                return;
            }
            String uniqueId = passpointConfiguration.getUniqueId();
            WifiManager wifiManager = OsuWifiEntry.this.mWifiManager;
            Iterator it = wifiManager.getAllMatchingWifiConfigs(wifiManager.getScanResults()).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair pair = (Pair) it.next();
                WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
                if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                    List list = (List) ((Map) pair.second).get(0);
                    List list2 = (List) ((Map) pair.second).get(1);
                    if (list != null && !list.isEmpty()) {
                        bestScanResultByLevel = Utils.getBestScanResultByLevel(list);
                    } else if (list2 != null && !list2.isEmpty()) {
                        bestScanResultByLevel = Utils.getBestScanResultByLevel(list2);
                    }
                    wifiConfiguration.SSID = "\"" + bestScanResultByLevel.SSID + "\"";
                    OsuWifiEntry.this.mWifiManager.connect(wifiConfiguration, null);
                    return;
                }
            }
            if (connectCallback == null) {
                return;
            }
            connectCallback.onConnectResult(2);
        }
    }
}
