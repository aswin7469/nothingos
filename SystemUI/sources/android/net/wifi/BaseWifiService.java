package android.net.wifi;

import android.net.DhcpInfo;
import android.net.DhcpOption;
import android.net.Network;
import android.net.wifi.IWifiManager;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.WorkSource;
import com.android.wifi.p018x.com.android.modules.utils.ParceledListSlice;
import java.util.List;
import java.util.Map;

public class BaseWifiService extends IWifiManager.Stub {
    private static final String TAG = "BaseWifiService";

    public long getSupportedFeatures() {
        throw new UnsupportedOperationException();
    }

    public void getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener iOnWifiActivityEnergyInfoListener) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public ParceledListSlice getConfiguredNetworks(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public ParceledListSlice getConfiguredNetworks(String str, String str2, boolean z) {
        throw new UnsupportedOperationException();
    }

    public ParceledListSlice getPrivilegedConfiguredNetworks(String str, String str2, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public WifiConfiguration getPrivilegedConnectedNetwork(String str, String str2, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public void setScreenOnScanSchedule(int[] iArr, int[] iArr2) {
        throw new UnsupportedOperationException();
    }

    public void setOneShotScreenOnConnectivityScanDelayMillis(int i) {
        throw new UnsupportedOperationException();
    }

    public Map<String, Map<Integer, List<ScanResult>>> getAllMatchingFqdnsForScanResults(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public void setSsidsAllowlist(String str, List<WifiSsid> list) {
        throw new UnsupportedOperationException();
    }

    public List<WifiSsid> getSsidsAllowlist(String str) {
        throw new UnsupportedOperationException();
    }

    public Map<OsuProvider, List<ScanResult>> getMatchingOsuProviders(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public Map<OsuProvider, PasspointConfiguration> getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) {
        throw new UnsupportedOperationException();
    }

    public int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged(WifiConfiguration wifiConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean removePasspointConfiguration(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public List<PasspointConfiguration> getPasspointConfigurations(String str) {
        throw new UnsupportedOperationException();
    }

    public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) {
        throw new UnsupportedOperationException();
    }

    public void queryPasspointIcon(long j, String str) {
        throw new UnsupportedOperationException();
    }

    public int matchProviderWithCurrentNetwork(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean removeNetwork(int i, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean removeNonCallerConfiguredNetworks(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean enableNetwork(int i, boolean z, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean disableNetwork(int i, String str) {
        throw new UnsupportedOperationException();
    }

    public void allowAutojoinGlobal(boolean z) {
        throw new UnsupportedOperationException();
    }

    public void queryAutojoinGlobal(IBooleanListener iBooleanListener) {
        throw new UnsupportedOperationException();
    }

    public void allowAutojoin(int i, boolean z) {
        throw new UnsupportedOperationException();
    }

    public void allowAutojoinPasspoint(String str, boolean z) {
        throw new UnsupportedOperationException();
    }

    public void setMacRandomizationSettingPasspointEnabled(String str, boolean z) {
        throw new UnsupportedOperationException();
    }

    public void setPasspointMeteredOverride(String str, int i) {
        throw new UnsupportedOperationException();
    }

    public boolean startScan(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public List<ScanResult> getScanResults(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public boolean disconnect(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean reconnect(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean reassociate(String str) {
        throw new UnsupportedOperationException();
    }

    public WifiInfo getConnectionInfo(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiEnabled(String str, boolean z) {
        throw new UnsupportedOperationException();
    }

    public void registerSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) {
        throw new UnsupportedOperationException();
    }

    public void restartWifiSubsystem() {
        throw new UnsupportedOperationException();
    }

    public int getWifiEnabledState() {
        throw new UnsupportedOperationException();
    }

    public void registerDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener, String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void unregisterDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener) {
        throw new UnsupportedOperationException();
    }

    public String getCountryCode(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void setOverrideCountryCode(String str) {
        throw new UnsupportedOperationException();
    }

    public void clearOverrideCountryCode() {
        throw new UnsupportedOperationException();
    }

    public void setDefaultCountryCode(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean is24GHzBandSupported() {
        throw new UnsupportedOperationException();
    }

    public boolean is5GHzBandSupported() {
        throw new UnsupportedOperationException();
    }

    public String getCapabilities(String str) {
        throw new UnsupportedOperationException();
    }

    public boolean is6GHzBandSupported() {
        throw new UnsupportedOperationException();
    }

    public boolean is60GHzBandSupported() {
        throw new UnsupportedOperationException();
    }

    public boolean isWifiStandardSupported(int i) {
        throw new UnsupportedOperationException();
    }

    public DhcpInfo getDhcpInfo(String str) {
        throw new UnsupportedOperationException();
    }

    public void setScanAlwaysAvailable(boolean z, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean isScanAlwaysAvailable() {
        throw new UnsupportedOperationException();
    }

    public boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) {
        throw new UnsupportedOperationException();
    }

    public void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) {
        throw new UnsupportedOperationException();
    }

    public boolean releaseWifiLock(IBinder iBinder) {
        throw new UnsupportedOperationException();
    }

    public void initializeMulticastFiltering() {
        throw new UnsupportedOperationException();
    }

    public boolean isMulticastEnabled() {
        throw new UnsupportedOperationException();
    }

    public void acquireMulticastLock(IBinder iBinder, String str) {
        throw new UnsupportedOperationException();
    }

    public void releaseMulticastLock(String str) {
        throw new UnsupportedOperationException();
    }

    public void updateInterfaceIpState(String str, int i) {
        throw new UnsupportedOperationException();
    }

    public boolean isDefaultCoexAlgorithmEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setCoexUnsafeChannels(List<CoexUnsafeChannel> list, int i) {
        throw new UnsupportedOperationException();
    }

    public void registerCoexCallback(ICoexCallback iCoexCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterCoexCallback(ICoexCallback iCoexCallback) {
        throw new UnsupportedOperationException();
    }

    public boolean startSoftAp(WifiConfiguration wifiConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean startTetheredHotspot(SoftApConfiguration softApConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean stopSoftAp() {
        throw new UnsupportedOperationException();
    }

    public int startLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback, String str, String str2, SoftApConfiguration softApConfiguration, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public void stopLocalOnlyHotspot() {
        throw new UnsupportedOperationException();
    }

    public void registerLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public void unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) {
        throw new UnsupportedOperationException();
    }

    public void startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback) {
        throw new UnsupportedOperationException();
    }

    public void stopWatchLocalOnlyHotspot() {
        throw new UnsupportedOperationException();
    }

    public int getWifiApEnabledState() {
        throw new UnsupportedOperationException();
    }

    public WifiConfiguration getWifiApConfiguration() {
        throw new UnsupportedOperationException();
    }

    public SoftApConfiguration getSoftApConfiguration() {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean setSoftApConfiguration(SoftApConfiguration softApConfiguration, String str) {
        throw new UnsupportedOperationException();
    }

    public void notifyUserOfApBandConversion(String str) {
        throw new UnsupportedOperationException();
    }

    public void enableTdls(String str, boolean z) {
        throw new UnsupportedOperationException();
    }

    public void enableTdlsWithMacAddress(String str, boolean z) {
        throw new UnsupportedOperationException();
    }

    public String getCurrentNetworkWpsNfcConfigurationToken() {
        throw new UnsupportedOperationException();
    }

    public void enableVerboseLogging(int i) {
        throw new UnsupportedOperationException();
    }

    public int getVerboseLoggingLevel() {
        throw new UnsupportedOperationException();
    }

    public void disableEphemeralNetwork(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void factoryReset(String str) {
        throw new UnsupportedOperationException();
    }

    public Network getCurrentNetwork() {
        throw new UnsupportedOperationException();
    }

    public byte[] retrieveBackupData() {
        throw new UnsupportedOperationException();
    }

    public void restoreBackupData(byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    public byte[] retrieveSoftApBackupData() {
        throw new UnsupportedOperationException();
    }

    public SoftApConfiguration restoreSoftApBackupData(byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    public void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) {
        throw new UnsupportedOperationException();
    }

    public void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) {
        throw new UnsupportedOperationException();
    }

    public void addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) {
        throw new UnsupportedOperationException();
    }

    public void removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) {
        throw new UnsupportedOperationException();
    }

    public void registerSoftApCallback(ISoftApCallback iSoftApCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterSoftApCallback(ISoftApCallback iSoftApCallback) {
        throw new UnsupportedOperationException();
    }

    public void registerTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) {
        throw new UnsupportedOperationException();
    }

    public void registerNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) {
        throw new UnsupportedOperationException();
    }

    public int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, int i) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str) {
        throw new UnsupportedOperationException();
    }

    public List<WifiNetworkSuggestion> getNetworkSuggestions(String str) {
        throw new UnsupportedOperationException();
    }

    public void setCarrierNetworkOffloadEnabled(int i, boolean z, boolean z2) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public boolean isCarrierNetworkOffloadEnabled(int i, boolean z) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public String[] getFactoryMacAddresses() {
        throw new UnsupportedOperationException();
    }

    public void setDeviceMobilityState(int i) {
        throw new UnsupportedOperationException();
    }

    public void startDppAsConfiguratorInitiator(IBinder iBinder, String str, String str2, int i, int i2, IDppCallback iDppCallback) {
        throw new UnsupportedOperationException();
    }

    public void startDppAsEnrolleeInitiator(IBinder iBinder, String str, IDppCallback iDppCallback) {
        throw new UnsupportedOperationException();
    }

    public void startDppAsEnrolleeResponder(IBinder iBinder, String str, int i, IDppCallback iDppCallback) {
        throw new UnsupportedOperationException();
    }

    public void stopDppSession() throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) {
        throw new UnsupportedOperationException();
    }

    public void removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) {
        throw new UnsupportedOperationException();
    }

    public void updateWifiUsabilityScore(int i, int i2, int i3) {
        throw new UnsupportedOperationException();
    }

    public void connect(WifiConfiguration wifiConfiguration, int i, IActionListener iActionListener) {
        throw new UnsupportedOperationException();
    }

    public void connect(WifiConfiguration wifiConfiguration, int i, IActionListener iActionListener, String str) {
        throw new UnsupportedOperationException();
    }

    public void startRestrictingAutoJoinToSubscriptionId(int i) {
        throw new UnsupportedOperationException();
    }

    public void stopRestrictingAutoJoinToSubscriptionId() {
        throw new UnsupportedOperationException();
    }

    public void save(WifiConfiguration wifiConfiguration, IActionListener iActionListener) {
        throw new UnsupportedOperationException();
    }

    public void save(WifiConfiguration wifiConfiguration, IActionListener iActionListener, String str) {
        throw new UnsupportedOperationException();
    }

    public void forget(int i, IActionListener iActionListener) {
        throw new UnsupportedOperationException();
    }

    public void registerScanResultsCallback(IScanResultsCallback iScanResultsCallback) {
        throw new UnsupportedOperationException();
    }

    public void unregisterScanResultsCallback(IScanResultsCallback iScanResultsCallback) {
        throw new UnsupportedOperationException();
    }

    public void registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str) {
        throw new UnsupportedOperationException();
    }

    public int calculateSignalLevel(int i) {
        throw new UnsupportedOperationException();
    }

    public List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public void setExternalPnoScanRequest(IBinder iBinder, IPnoScanResultsCallback iPnoScanResultsCallback, List<WifiSsid> list, int[] iArr, String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void clearExternalPnoScanRequest() {
        throw new UnsupportedOperationException();
    }

    public void getLastCallerInfoForApi(int i, ILastCallerListener iLastCallerListener) {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiConnectedNetworkScorer(IBinder iBinder, IWifiConnectedNetworkScorer iWifiConnectedNetworkScorer) {
        throw new UnsupportedOperationException();
    }

    public void clearWifiConnectedNetworkScorer() {
        throw new UnsupportedOperationException();
    }

    public Map<WifiNetworkSuggestion, List<ScanResult>> getMatchingScanResults(List<WifiNetworkSuggestion> list, List<ScanResult> list2, String str, String str2) {
        throw new UnsupportedOperationException();
    }

    public void setScanThrottleEnabled(boolean z) {
        throw new UnsupportedOperationException();
    }

    public boolean isScanThrottleEnabled() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Map<Integer, List<ScanResult>>> getAllMatchingPasspointProfilesForScanResults(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public void setAutoWakeupEnabled(boolean z) {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoWakeupEnabled() {
        throw new UnsupportedOperationException();
    }

    public void addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) {
        throw new UnsupportedOperationException();
    }

    public void removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) {
        throw new UnsupportedOperationException();
    }

    public void setEmergencyScanRequestInProgress(boolean z) {
        throw new UnsupportedOperationException();
    }

    public void removeAppState(int i, String str) {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiScoringEnabled(boolean z) {
        throw new UnsupportedOperationException();
    }

    public void flushPasspointAnqpCache(String str) {
        throw new UnsupportedOperationException();
    }

    public List<WifiAvailableChannel> getUsableChannels(int i, int i2, int i3) {
        throw new UnsupportedOperationException();
    }

    public boolean isWifiPasspointEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setWifiPasspointEnabled(boolean z) {
        throw new UnsupportedOperationException();
    }

    public int getStaConcurrencyForMultiInternetMode() {
        throw new UnsupportedOperationException();
    }

    public boolean setStaConcurrencyForMultiInternetMode(int i) {
        throw new UnsupportedOperationException();
    }

    public void notifyMinimumRequiredWifiSecurityLevelChanged(int i) {
        throw new UnsupportedOperationException();
    }

    public void notifyWifiSsidPolicyChanged(int i, List<WifiSsid> list) {
        throw new UnsupportedOperationException();
    }

    public String[] getOemPrivilegedWifiAdminPackages() {
        throw new UnsupportedOperationException();
    }

    public void replyToP2pInvitationReceivedDialog(int i, boolean z, String str) {
        throw new UnsupportedOperationException();
    }

    public void replyToSimpleDialog(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    public void addCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr, List<DhcpOption> list) {
        throw new UnsupportedOperationException();
    }

    public void removeCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    public void reportCreateInterfaceImpact(String str, int i, boolean z, IInterfaceCreationInfoCallback iInterfaceCreationInfoCallback) {
        throw new UnsupportedOperationException();
    }
}
