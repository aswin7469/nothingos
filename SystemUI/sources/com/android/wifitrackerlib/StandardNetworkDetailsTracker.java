package com.android.wifitrackerlib;

import android.content.Intent;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class StandardNetworkDetailsTracker extends NetworkDetailsTracker {
    private final StandardWifiEntry mChosenEntry;
    private NetworkInfo mCurrentNetworkInfo;
    private final StandardWifiEntry.StandardWifiEntryKey mKey;

    @Override // com.android.wifitrackerlib.NetworkDetailsTracker
    public WifiEntry getWifiEntry() {
        return this.mChosenEntry;
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleOnStart() {
        updateStartInfo();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateConfig();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleNetworkScoreCacheUpdated() {
        this.mChosenEntry.onScoreCacheUpdated();
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
        StandardWifiEntry standardWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        standardWifiEntry.setIsLowQuality(z);
    }

    private void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            cacheNewScanResults();
        } else {
            j += this.mScanIntervalMillis;
        }
        this.mChosenEntry.updateScanResultInfo(this.mScanResultUpdater.getScanResults(j));
    }

    private void conditionallyUpdateConfig() {
        this.mChosenEntry.updateConfig((List) this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.StandardNetworkDetailsTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean configMatches;
                configMatches = StandardNetworkDetailsTracker.this.configMatches((WifiConfiguration) obj);
                return configMatches;
            }
        }).collect(Collectors.toList()));
    }

    private void cacheNewScanResults() {
        this.mScanResultUpdater.update((List) this.mWifiManager.getScanResults().stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.StandardNetworkDetailsTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$cacheNewScanResults$0;
                lambda$cacheNewScanResults$0 = StandardNetworkDetailsTracker.this.lambda$cacheNewScanResults$0((ScanResult) obj);
                return lambda$cacheNewScanResults$0;
            }
        }).collect(Collectors.toList()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$cacheNewScanResults$0(ScanResult scanResult) {
        return new StandardWifiEntry.ScanResultKey(scanResult).equals(this.mKey.getScanResultKey());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean configMatches(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            return false;
        }
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = this.mKey;
        return standardWifiEntryKey.equals(new StandardWifiEntry.StandardWifiEntryKey(wifiConfiguration, standardWifiEntryKey.isTargetingNewNetworks()));
    }
}
