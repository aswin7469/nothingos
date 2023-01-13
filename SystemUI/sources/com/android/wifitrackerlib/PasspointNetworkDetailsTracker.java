package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PasspointNetworkDetailsTracker extends NetworkDetailsTracker {
    private static final String TAG = "PasspointNetworkDetailsTracker";
    private final PasspointWifiEntry mChosenEntry;
    private NetworkInfo mCurrentNetworkInfo;
    private WifiConfiguration mCurrentWifiConfig;
    private OsuWifiEntry mOsuWifiEntry;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public PasspointNetworkDetailsTracker(androidx.lifecycle.Lifecycle r15, android.content.Context r16, android.net.wifi.WifiManager r17, android.net.ConnectivityManager r18, android.os.Handler r19, android.os.Handler r20, java.time.Clock r21, long r22, long r24, java.lang.String r26) {
        /*
            r14 = this;
            com.android.wifitrackerlib.WifiTrackerInjector r1 = new com.android.wifitrackerlib.WifiTrackerInjector
            r3 = r16
            r1.<init>(r3)
            r0 = r14
            r2 = r15
            r4 = r17
            r5 = r18
            r6 = r19
            r7 = r20
            r8 = r21
            r9 = r22
            r11 = r24
            r13 = r26
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r11, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointNetworkDetailsTracker.<init>(androidx.lifecycle.Lifecycle, android.content.Context, android.net.wifi.WifiManager, android.net.ConnectivityManager, android.os.Handler, android.os.Handler, java.time.Clock, long, long, java.lang.String):void");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PasspointNetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, TAG);
        String str2 = str;
        Optional<PasspointConfiguration> findAny = this.mWifiManager.getPasspointConfigurations().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda0(str2)).findAny();
        if (findAny.isPresent()) {
            this.mChosenEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, findAny.get(), this.mWifiManager, false);
        } else {
            Optional<WifiConfiguration> findAny2 = this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda1(str2)).findAny();
            if (findAny2.isPresent()) {
                this.mChosenEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, findAny2.get(), this.mWifiManager, false);
            } else {
                throw new IllegalArgumentException("Cannot find config for given PasspointWifiEntry key!");
            }
        }
        updateStartInfo();
    }

    static /* synthetic */ boolean lambda$new$1(String str, WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.isPasspoint() && TextUtils.equals(str, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()));
    }

    public WifiEntry getWifiEntry() {
        return this.mChosenEntry;
    }

    /* access modifiers changed from: protected */
    public void handleOnStart() {
        updateStartInfo();
    }

    /* access modifiers changed from: protected */
    public void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
    }

    /* access modifiers changed from: protected */
    public void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, true));
    }

    /* access modifiers changed from: protected */
    public void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateConfig();
    }

    private void updateStartInfo() {
        boolean z = true;
        conditionallyUpdateScanResults(true);
        conditionallyUpdateConfig();
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        this.mChosenEntry.updateConnectionInfo(connectionInfo, networkInfo);
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        this.mChosenEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
        PasspointWifiEntry passpointWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        passpointWifiEntry.setIsLowQuality(z);
    }

    private void updatePasspointWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        for (Pair next : this.mWifiManager.getAllMatchingWifiConfigs(list)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) next.first;
            if (TextUtils.equals(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()), this.mChosenEntry.getKey())) {
                this.mCurrentWifiConfig = wifiConfiguration;
                this.mChosenEntry.updateScanResultInfo(wifiConfiguration, (List) ((Map) next.second).get(0), (List) ((Map) next.second).get(1));
                return;
            }
        }
        this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, (List<ScanResult>) null, (List<ScanResult>) null);
    }

    private void updateOsuWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map<OsuProvider, List<ScanResult>> matchingOsuProviders = this.mWifiManager.getMatchingOsuProviders(list);
        Map<OsuProvider, PasspointConfiguration> matchingPasspointConfigsForOsuProviders = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(matchingOsuProviders.keySet());
        OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
        if (osuWifiEntry != null) {
            osuWifiEntry.updateScanResultInfo(matchingOsuProviders.get(osuWifiEntry.getOsuProvider()));
        } else {
            for (OsuProvider next : matchingOsuProviders.keySet()) {
                PasspointConfiguration passpointConfiguration = matchingPasspointConfigsForOsuProviders.get(next);
                if (passpointConfiguration != null && TextUtils.equals(this.mChosenEntry.getKey(), PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()))) {
                    OsuWifiEntry osuWifiEntry2 = new OsuWifiEntry(this.mInjector, this.mContext, this.mMainHandler, next, this.mWifiManager, false);
                    this.mOsuWifiEntry = osuWifiEntry2;
                    osuWifiEntry2.updateScanResultInfo(matchingOsuProviders.get(next));
                    this.mOsuWifiEntry.setAlreadyProvisioned(true);
                    this.mChosenEntry.setOsuWifiEntry(this.mOsuWifiEntry);
                    return;
                }
            }
        }
        OsuWifiEntry osuWifiEntry3 = this.mOsuWifiEntry;
        if (osuWifiEntry3 != null && osuWifiEntry3.getLevel() == -1) {
            this.mChosenEntry.setOsuWifiEntry((OsuWifiEntry) null);
            this.mOsuWifiEntry = null;
        }
    }

    private void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(this.mCurrentWifiConfig, (List<ScanResult>) null, (List<ScanResult>) null);
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            cacheNewScanResults();
        } else {
            j += this.mScanIntervalMillis;
        }
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults(j);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
    }

    private void conditionallyUpdateConfig() {
        this.mWifiManager.getPasspointConfigurations().stream().filter(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda2(this)).findAny().ifPresent(new PasspointNetworkDetailsTracker$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$conditionallyUpdateConfig$2$com-android-wifitrackerlib-PasspointNetworkDetailsTracker */
    public /* synthetic */ boolean mo47828x38068e71(PasspointConfiguration passpointConfiguration) {
        return TextUtils.equals(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()), this.mChosenEntry.getKey());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$conditionallyUpdateConfig$3$com-android-wifitrackerlib-PasspointNetworkDetailsTracker */
    public /* synthetic */ void mo47829x48bc5b32(PasspointConfiguration passpointConfiguration) {
        this.mChosenEntry.updatePasspointConfig(passpointConfiguration);
    }

    private void cacheNewScanResults() {
        this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
    }
}
