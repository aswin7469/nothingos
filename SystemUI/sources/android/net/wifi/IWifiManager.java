package android.net.wifi;

import android.net.DhcpInfo;
import android.net.DhcpOption;
import android.net.Network;
import android.net.wifi.IActionListener;
import android.net.wifi.IBooleanListener;
import android.net.wifi.ICoexCallback;
import android.net.wifi.IDppCallback;
import android.net.wifi.IInterfaceCreationInfoCallback;
import android.net.wifi.ILastCallerListener;
import android.net.wifi.ILocalOnlyHotspotCallback;
import android.net.wifi.INetworkRequestMatchCallback;
import android.net.wifi.IOnWifiActivityEnergyInfoListener;
import android.net.wifi.IOnWifiDriverCountryCodeChangedListener;
import android.net.wifi.IOnWifiUsabilityStatsListener;
import android.net.wifi.IPnoScanResultsCallback;
import android.net.wifi.IScanResultsCallback;
import android.net.wifi.ISoftApCallback;
import android.net.wifi.ISubsystemRestartCallback;
import android.net.wifi.ISuggestionConnectionStatusListener;
import android.net.wifi.ISuggestionUserApprovalStatusListener;
import android.net.wifi.ITrafficStateCallback;
import android.net.wifi.IWifiConnectedNetworkScorer;
import android.net.wifi.IWifiVerboseLoggingStatusChangedListener;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;
import com.android.wifi.p018x.com.android.modules.utils.ParceledListSlice;
import java.util.List;
import java.util.Map;

public interface IWifiManager extends IInterface {

    public static class Default implements IWifiManager {
        public void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException {
        }

        public boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException {
            return false;
        }

        public void addCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr, List<DhcpOption> list) throws RemoteException {
        }

        public int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, String str2) throws RemoteException {
            return 0;
        }

        public void addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException {
        }

        public int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str, Bundle bundle) throws RemoteException {
            return 0;
        }

        public WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
            return null;
        }

        public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) throws RemoteException {
            return false;
        }

        public void addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException {
        }

        public void addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException {
        }

        public void allowAutojoin(int i, boolean z) throws RemoteException {
        }

        public void allowAutojoinGlobal(boolean z) throws RemoteException {
        }

        public void allowAutojoinPasspoint(String str, boolean z) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public int calculateSignalLevel(int i) throws RemoteException {
            return 0;
        }

        public void clearExternalPnoScanRequest() throws RemoteException {
        }

        public void clearOverrideCountryCode() throws RemoteException {
        }

        public void clearWifiConnectedNetworkScorer() throws RemoteException {
        }

        public void connect(WifiConfiguration wifiConfiguration, int i, IActionListener iActionListener, String str) throws RemoteException {
        }

        public void disableEphemeralNetwork(String str, String str2) throws RemoteException {
        }

        public boolean disableNetwork(int i, String str) throws RemoteException {
            return false;
        }

        public boolean disconnect(String str) throws RemoteException {
            return false;
        }

        public boolean enableNetwork(int i, boolean z, String str) throws RemoteException {
            return false;
        }

        public void enableTdls(String str, boolean z) throws RemoteException {
        }

        public void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException {
        }

        public void enableVerboseLogging(int i) throws RemoteException {
        }

        public void factoryReset(String str) throws RemoteException {
        }

        public void flushPasspointAnqpCache(String str) throws RemoteException {
        }

        public void forget(int i, IActionListener iActionListener) throws RemoteException {
        }

        public Map getAllMatchingFqdnsForScanResults(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public Map getAllMatchingPasspointProfilesForScanResults(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public String getCapabilities(String str) throws RemoteException {
            return null;
        }

        public ParceledListSlice getConfiguredNetworks(String str, String str2, boolean z) throws RemoteException {
            return null;
        }

        public WifiInfo getConnectionInfo(String str, String str2) throws RemoteException {
            return null;
        }

        public String getCountryCode(String str, String str2) throws RemoteException {
            return null;
        }

        public Network getCurrentNetwork() throws RemoteException {
            return null;
        }

        public String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException {
            return null;
        }

        public DhcpInfo getDhcpInfo(String str) throws RemoteException {
            return null;
        }

        public String[] getFactoryMacAddresses() throws RemoteException {
            return null;
        }

        public void getLastCallerInfoForApi(int i, ILastCallerListener iLastCallerListener) throws RemoteException {
        }

        public Map getMatchingOsuProviders(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) throws RemoteException {
            return null;
        }

        public Map getMatchingScanResults(List<WifiNetworkSuggestion> list, List<ScanResult> list2, String str, String str2) throws RemoteException {
            return null;
        }

        public List<WifiNetworkSuggestion> getNetworkSuggestions(String str) throws RemoteException {
            return null;
        }

        public String[] getOemPrivilegedWifiAdminPackages() throws RemoteException {
            return null;
        }

        public List<PasspointConfiguration> getPasspointConfigurations(String str) throws RemoteException {
            return null;
        }

        public ParceledListSlice getPrivilegedConfiguredNetworks(String str, String str2, Bundle bundle) throws RemoteException {
            return null;
        }

        public WifiConfiguration getPrivilegedConnectedNetwork(String str, String str2, Bundle bundle) throws RemoteException {
            return null;
        }

        public List<ScanResult> getScanResults(String str, String str2) throws RemoteException {
            return null;
        }

        public SoftApConfiguration getSoftApConfiguration() throws RemoteException {
            return null;
        }

        public List getSsidsAllowlist(String str) throws RemoteException {
            return null;
        }

        public int getStaConcurrencyForMultiInternetMode() throws RemoteException {
            return 0;
        }

        public long getSupportedFeatures() throws RemoteException {
            return 0;
        }

        public List<WifiAvailableChannel> getUsableChannels(int i, int i2, int i3) throws RemoteException {
            return null;
        }

        public int getVerboseLoggingLevel() throws RemoteException {
            return 0;
        }

        public void getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener iOnWifiActivityEnergyInfoListener) throws RemoteException {
        }

        public WifiConfiguration getWifiApConfiguration() throws RemoteException {
            return null;
        }

        public int getWifiApEnabledState() throws RemoteException {
            return 0;
        }

        public List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) throws RemoteException {
            return null;
        }

        public int getWifiEnabledState() throws RemoteException {
            return 0;
        }

        public void initializeMulticastFiltering() throws RemoteException {
        }

        public boolean is24GHzBandSupported() throws RemoteException {
            return false;
        }

        public boolean is5GHzBandSupported() throws RemoteException {
            return false;
        }

        public boolean is60GHzBandSupported() throws RemoteException {
            return false;
        }

        public boolean is6GHzBandSupported() throws RemoteException {
            return false;
        }

        public boolean isAutoWakeupEnabled() throws RemoteException {
            return false;
        }

        public boolean isCarrierNetworkOffloadEnabled(int i, boolean z) throws RemoteException {
            return false;
        }

        public boolean isDefaultCoexAlgorithmEnabled() throws RemoteException {
            return false;
        }

        public boolean isMulticastEnabled() throws RemoteException {
            return false;
        }

        public boolean isScanAlwaysAvailable() throws RemoteException {
            return false;
        }

        public boolean isScanThrottleEnabled() throws RemoteException {
            return false;
        }

        public boolean isWifiPasspointEnabled() throws RemoteException {
            return false;
        }

        public boolean isWifiStandardSupported(int i) throws RemoteException {
            return false;
        }

        public int matchProviderWithCurrentNetwork(String str) throws RemoteException {
            return 0;
        }

        public void notifyMinimumRequiredWifiSecurityLevelChanged(int i) throws RemoteException {
        }

        public void notifyUserOfApBandConversion(String str) throws RemoteException {
        }

        public void notifyWifiSsidPolicyChanged(int i, List<WifiSsid> list) throws RemoteException {
        }

        public void queryAutojoinGlobal(IBooleanListener iBooleanListener) throws RemoteException {
        }

        public void queryPasspointIcon(long j, String str) throws RemoteException {
        }

        public boolean reassociate(String str) throws RemoteException {
            return false;
        }

        public boolean reconnect(String str) throws RemoteException {
            return false;
        }

        public void registerCoexCallback(ICoexCallback iCoexCallback) throws RemoteException {
        }

        public void registerDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener, String str, String str2) throws RemoteException {
        }

        public void registerLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException {
        }

        public void registerNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException {
        }

        public void registerScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException {
        }

        public void registerSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException {
        }

        public void registerSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException {
        }

        public void registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str, String str2) throws RemoteException {
        }

        public void registerTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException {
        }

        public void releaseMulticastLock(String str) throws RemoteException {
        }

        public boolean releaseWifiLock(IBinder iBinder) throws RemoteException {
            return false;
        }

        public void removeAppState(int i, String str) throws RemoteException {
        }

        public void removeCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr) throws RemoteException {
        }

        public boolean removeNetwork(int i, String str) throws RemoteException {
            return false;
        }

        public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, int i) throws RemoteException {
            return 0;
        }

        public boolean removeNonCallerConfiguredNetworks(String str) throws RemoteException {
            return false;
        }

        public void removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException {
        }

        public boolean removePasspointConfiguration(String str, String str2) throws RemoteException {
            return false;
        }

        public void removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException {
        }

        public void removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException {
        }

        public void replyToP2pInvitationReceivedDialog(int i, boolean z, String str) throws RemoteException {
        }

        public void replyToSimpleDialog(int i, int i2) throws RemoteException {
        }

        public void reportCreateInterfaceImpact(String str, int i, boolean z, IInterfaceCreationInfoCallback iInterfaceCreationInfoCallback) throws RemoteException {
        }

        public void restartWifiSubsystem() throws RemoteException {
        }

        public void restoreBackupData(byte[] bArr) throws RemoteException {
        }

        public SoftApConfiguration restoreSoftApBackupData(byte[] bArr) throws RemoteException {
            return null;
        }

        public void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) throws RemoteException {
        }

        public byte[] retrieveBackupData() throws RemoteException {
            return null;
        }

        public byte[] retrieveSoftApBackupData() throws RemoteException {
            return null;
        }

        public void save(WifiConfiguration wifiConfiguration, IActionListener iActionListener, String str) throws RemoteException {
        }

        public void setAutoWakeupEnabled(boolean z) throws RemoteException {
        }

        public void setCarrierNetworkOffloadEnabled(int i, boolean z, boolean z2) throws RemoteException {
        }

        public void setCoexUnsafeChannels(List<CoexUnsafeChannel> list, int i) throws RemoteException {
        }

        public void setDefaultCountryCode(String str) throws RemoteException {
        }

        public void setDeviceMobilityState(int i) throws RemoteException {
        }

        public void setEmergencyScanRequestInProgress(boolean z) throws RemoteException {
        }

        public void setExternalPnoScanRequest(IBinder iBinder, IPnoScanResultsCallback iPnoScanResultsCallback, List<WifiSsid> list, int[] iArr, String str, String str2) throws RemoteException {
        }

        public void setMacRandomizationSettingPasspointEnabled(String str, boolean z) throws RemoteException {
        }

        public void setOneShotScreenOnConnectivityScanDelayMillis(int i) throws RemoteException {
        }

        public void setOverrideCountryCode(String str) throws RemoteException {
        }

        public void setPasspointMeteredOverride(String str, int i) throws RemoteException {
        }

        public void setScanAlwaysAvailable(boolean z, String str) throws RemoteException {
        }

        public void setScanThrottleEnabled(boolean z) throws RemoteException {
        }

        public void setScreenOnScanSchedule(int[] iArr, int[] iArr2) throws RemoteException {
        }

        public boolean setSoftApConfiguration(SoftApConfiguration softApConfiguration, String str) throws RemoteException {
            return false;
        }

        public void setSsidsAllowlist(String str, List<WifiSsid> list) throws RemoteException {
        }

        public boolean setStaConcurrencyForMultiInternetMode(int i) throws RemoteException {
            return false;
        }

        public boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
            return false;
        }

        public boolean setWifiConnectedNetworkScorer(IBinder iBinder, IWifiConnectedNetworkScorer iWifiConnectedNetworkScorer) throws RemoteException {
            return false;
        }

        public boolean setWifiEnabled(String str, boolean z) throws RemoteException {
            return false;
        }

        public void setWifiPasspointEnabled(boolean z) throws RemoteException {
        }

        public boolean setWifiScoringEnabled(boolean z) throws RemoteException {
            return false;
        }

        public void startDppAsConfiguratorInitiator(IBinder iBinder, String str, String str2, int i, int i2, IDppCallback iDppCallback) throws RemoteException {
        }

        public void startDppAsEnrolleeInitiator(IBinder iBinder, String str, IDppCallback iDppCallback) throws RemoteException {
        }

        public void startDppAsEnrolleeResponder(IBinder iBinder, String str, int i, IDppCallback iDppCallback) throws RemoteException {
        }

        public int startLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback, String str, String str2, SoftApConfiguration softApConfiguration, Bundle bundle) throws RemoteException {
            return 0;
        }

        public void startRestrictingAutoJoinToSubscriptionId(int i) throws RemoteException {
        }

        public boolean startScan(String str, String str2) throws RemoteException {
            return false;
        }

        public boolean startSoftAp(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
            return false;
        }

        public void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) throws RemoteException {
        }

        public boolean startTetheredHotspot(SoftApConfiguration softApConfiguration, String str) throws RemoteException {
            return false;
        }

        public void startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback) throws RemoteException {
        }

        public void stopDppSession() throws RemoteException {
        }

        public void stopLocalOnlyHotspot() throws RemoteException {
        }

        public void stopRestrictingAutoJoinToSubscriptionId() throws RemoteException {
        }

        public boolean stopSoftAp() throws RemoteException {
            return false;
        }

        public void stopWatchLocalOnlyHotspot() throws RemoteException {
        }

        public void unregisterCoexCallback(ICoexCallback iCoexCallback) throws RemoteException {
        }

        public void unregisterDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener) throws RemoteException {
        }

        public void unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException {
        }

        public void unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException {
        }

        public void unregisterScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException {
        }

        public void unregisterSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException {
        }

        public void unregisterSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException {
        }

        public void unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str) throws RemoteException {
        }

        public void unregisterTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException {
        }

        public void updateInterfaceIpState(String str, int i) throws RemoteException {
        }

        public void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException {
        }

        public void updateWifiUsabilityScore(int i, int i2, int i3) throws RemoteException {
        }
    }

    void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException;

    boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException;

    void addCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr, List<DhcpOption> list) throws RemoteException;

    int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, String str2) throws RemoteException;

    void addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException;

    int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str, Bundle bundle) throws RemoteException;

    WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) throws RemoteException;

    void addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException;

    void addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException;

    void allowAutojoin(int i, boolean z) throws RemoteException;

    void allowAutojoinGlobal(boolean z) throws RemoteException;

    void allowAutojoinPasspoint(String str, boolean z) throws RemoteException;

    int calculateSignalLevel(int i) throws RemoteException;

    void clearExternalPnoScanRequest() throws RemoteException;

    void clearOverrideCountryCode() throws RemoteException;

    void clearWifiConnectedNetworkScorer() throws RemoteException;

    void connect(WifiConfiguration wifiConfiguration, int i, IActionListener iActionListener, String str) throws RemoteException;

    void disableEphemeralNetwork(String str, String str2) throws RemoteException;

    boolean disableNetwork(int i, String str) throws RemoteException;

    boolean disconnect(String str) throws RemoteException;

    boolean enableNetwork(int i, boolean z, String str) throws RemoteException;

    void enableTdls(String str, boolean z) throws RemoteException;

    void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException;

    void enableVerboseLogging(int i) throws RemoteException;

    void factoryReset(String str) throws RemoteException;

    void flushPasspointAnqpCache(String str) throws RemoteException;

    void forget(int i, IActionListener iActionListener) throws RemoteException;

    Map getAllMatchingFqdnsForScanResults(List<ScanResult> list) throws RemoteException;

    Map getAllMatchingPasspointProfilesForScanResults(List<ScanResult> list) throws RemoteException;

    String getCapabilities(String str) throws RemoteException;

    ParceledListSlice getConfiguredNetworks(String str, String str2, boolean z) throws RemoteException;

    WifiInfo getConnectionInfo(String str, String str2) throws RemoteException;

    String getCountryCode(String str, String str2) throws RemoteException;

    Network getCurrentNetwork() throws RemoteException;

    String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException;

    DhcpInfo getDhcpInfo(String str) throws RemoteException;

    String[] getFactoryMacAddresses() throws RemoteException;

    void getLastCallerInfoForApi(int i, ILastCallerListener iLastCallerListener) throws RemoteException;

    Map getMatchingOsuProviders(List<ScanResult> list) throws RemoteException;

    Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) throws RemoteException;

    Map getMatchingScanResults(List<WifiNetworkSuggestion> list, List<ScanResult> list2, String str, String str2) throws RemoteException;

    List<WifiNetworkSuggestion> getNetworkSuggestions(String str) throws RemoteException;

    String[] getOemPrivilegedWifiAdminPackages() throws RemoteException;

    List<PasspointConfiguration> getPasspointConfigurations(String str) throws RemoteException;

    ParceledListSlice getPrivilegedConfiguredNetworks(String str, String str2, Bundle bundle) throws RemoteException;

    WifiConfiguration getPrivilegedConnectedNetwork(String str, String str2, Bundle bundle) throws RemoteException;

    List<ScanResult> getScanResults(String str, String str2) throws RemoteException;

    SoftApConfiguration getSoftApConfiguration() throws RemoteException;

    List getSsidsAllowlist(String str) throws RemoteException;

    int getStaConcurrencyForMultiInternetMode() throws RemoteException;

    long getSupportedFeatures() throws RemoteException;

    List<WifiAvailableChannel> getUsableChannels(int i, int i2, int i3) throws RemoteException;

    int getVerboseLoggingLevel() throws RemoteException;

    void getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener iOnWifiActivityEnergyInfoListener) throws RemoteException;

    WifiConfiguration getWifiApConfiguration() throws RemoteException;

    int getWifiApEnabledState() throws RemoteException;

    List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult> list) throws RemoteException;

    List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) throws RemoteException;

    int getWifiEnabledState() throws RemoteException;

    void initializeMulticastFiltering() throws RemoteException;

    boolean is24GHzBandSupported() throws RemoteException;

    boolean is5GHzBandSupported() throws RemoteException;

    boolean is60GHzBandSupported() throws RemoteException;

    boolean is6GHzBandSupported() throws RemoteException;

    boolean isAutoWakeupEnabled() throws RemoteException;

    boolean isCarrierNetworkOffloadEnabled(int i, boolean z) throws RemoteException;

    boolean isDefaultCoexAlgorithmEnabled() throws RemoteException;

    boolean isMulticastEnabled() throws RemoteException;

    boolean isScanAlwaysAvailable() throws RemoteException;

    boolean isScanThrottleEnabled() throws RemoteException;

    boolean isWifiPasspointEnabled() throws RemoteException;

    boolean isWifiStandardSupported(int i) throws RemoteException;

    int matchProviderWithCurrentNetwork(String str) throws RemoteException;

    void notifyMinimumRequiredWifiSecurityLevelChanged(int i) throws RemoteException;

    void notifyUserOfApBandConversion(String str) throws RemoteException;

    void notifyWifiSsidPolicyChanged(int i, List<WifiSsid> list) throws RemoteException;

    void queryAutojoinGlobal(IBooleanListener iBooleanListener) throws RemoteException;

    void queryPasspointIcon(long j, String str) throws RemoteException;

    boolean reassociate(String str) throws RemoteException;

    boolean reconnect(String str) throws RemoteException;

    void registerCoexCallback(ICoexCallback iCoexCallback) throws RemoteException;

    void registerDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener, String str, String str2) throws RemoteException;

    void registerLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException;

    void registerNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException;

    void registerScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException;

    void registerSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException;

    void registerSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException;

    void registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str, String str2) throws RemoteException;

    void registerTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException;

    void releaseMulticastLock(String str) throws RemoteException;

    boolean releaseWifiLock(IBinder iBinder) throws RemoteException;

    void removeAppState(int i, String str) throws RemoteException;

    void removeCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr) throws RemoteException;

    boolean removeNetwork(int i, String str) throws RemoteException;

    int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, int i) throws RemoteException;

    boolean removeNonCallerConfiguredNetworks(String str) throws RemoteException;

    void removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException;

    boolean removePasspointConfiguration(String str, String str2) throws RemoteException;

    void removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException;

    void removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException;

    void replyToP2pInvitationReceivedDialog(int i, boolean z, String str) throws RemoteException;

    void replyToSimpleDialog(int i, int i2) throws RemoteException;

    void reportCreateInterfaceImpact(String str, int i, boolean z, IInterfaceCreationInfoCallback iInterfaceCreationInfoCallback) throws RemoteException;

    void restartWifiSubsystem() throws RemoteException;

    void restoreBackupData(byte[] bArr) throws RemoteException;

    SoftApConfiguration restoreSoftApBackupData(byte[] bArr) throws RemoteException;

    void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) throws RemoteException;

    byte[] retrieveBackupData() throws RemoteException;

    byte[] retrieveSoftApBackupData() throws RemoteException;

    void save(WifiConfiguration wifiConfiguration, IActionListener iActionListener, String str) throws RemoteException;

    void setAutoWakeupEnabled(boolean z) throws RemoteException;

    void setCarrierNetworkOffloadEnabled(int i, boolean z, boolean z2) throws RemoteException;

    void setCoexUnsafeChannels(List<CoexUnsafeChannel> list, int i) throws RemoteException;

    void setDefaultCountryCode(String str) throws RemoteException;

    void setDeviceMobilityState(int i) throws RemoteException;

    void setEmergencyScanRequestInProgress(boolean z) throws RemoteException;

    void setExternalPnoScanRequest(IBinder iBinder, IPnoScanResultsCallback iPnoScanResultsCallback, List<WifiSsid> list, int[] iArr, String str, String str2) throws RemoteException;

    void setMacRandomizationSettingPasspointEnabled(String str, boolean z) throws RemoteException;

    void setOneShotScreenOnConnectivityScanDelayMillis(int i) throws RemoteException;

    void setOverrideCountryCode(String str) throws RemoteException;

    void setPasspointMeteredOverride(String str, int i) throws RemoteException;

    void setScanAlwaysAvailable(boolean z, String str) throws RemoteException;

    void setScanThrottleEnabled(boolean z) throws RemoteException;

    void setScreenOnScanSchedule(int[] iArr, int[] iArr2) throws RemoteException;

    boolean setSoftApConfiguration(SoftApConfiguration softApConfiguration, String str) throws RemoteException;

    void setSsidsAllowlist(String str, List<WifiSsid> list) throws RemoteException;

    boolean setStaConcurrencyForMultiInternetMode(int i) throws RemoteException;

    boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    boolean setWifiConnectedNetworkScorer(IBinder iBinder, IWifiConnectedNetworkScorer iWifiConnectedNetworkScorer) throws RemoteException;

    boolean setWifiEnabled(String str, boolean z) throws RemoteException;

    void setWifiPasspointEnabled(boolean z) throws RemoteException;

    boolean setWifiScoringEnabled(boolean z) throws RemoteException;

    void startDppAsConfiguratorInitiator(IBinder iBinder, String str, String str2, int i, int i2, IDppCallback iDppCallback) throws RemoteException;

    void startDppAsEnrolleeInitiator(IBinder iBinder, String str, IDppCallback iDppCallback) throws RemoteException;

    void startDppAsEnrolleeResponder(IBinder iBinder, String str, int i, IDppCallback iDppCallback) throws RemoteException;

    int startLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback, String str, String str2, SoftApConfiguration softApConfiguration, Bundle bundle) throws RemoteException;

    void startRestrictingAutoJoinToSubscriptionId(int i) throws RemoteException;

    boolean startScan(String str, String str2) throws RemoteException;

    boolean startSoftAp(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) throws RemoteException;

    boolean startTetheredHotspot(SoftApConfiguration softApConfiguration, String str) throws RemoteException;

    void startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback) throws RemoteException;

    void stopDppSession() throws RemoteException;

    void stopLocalOnlyHotspot() throws RemoteException;

    void stopRestrictingAutoJoinToSubscriptionId() throws RemoteException;

    boolean stopSoftAp() throws RemoteException;

    void stopWatchLocalOnlyHotspot() throws RemoteException;

    void unregisterCoexCallback(ICoexCallback iCoexCallback) throws RemoteException;

    void unregisterDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener) throws RemoteException;

    void unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException;

    void unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException;

    void unregisterScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException;

    void unregisterSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException;

    void unregisterSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException;

    void unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str) throws RemoteException;

    void unregisterTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException;

    void updateInterfaceIpState(String str, int i) throws RemoteException;

    void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException;

    void updateWifiUsabilityScore(int i, int i2, int i3) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiManager {
        public static final String DESCRIPTOR = "android.net.wifi.IWifiManager";
        static final int TRANSACTION_acquireMulticastLock = 58;
        static final int TRANSACTION_acquireWifiLock = 53;
        static final int TRANSACTION_addCustomDhcpOptions = 158;
        static final int TRANSACTION_addNetworkSuggestions = 105;
        static final int TRANSACTION_addOnWifiUsabilityStatsListener = 98;
        static final int TRANSACTION_addOrUpdateNetwork = 13;
        static final int TRANSACTION_addOrUpdateNetworkPrivileged = 14;
        static final int TRANSACTION_addOrUpdatePasspointConfiguration = 15;
        static final int TRANSACTION_addSuggestionUserApprovalStatusListener = 142;
        static final int TRANSACTION_addWifiVerboseLoggingStatusChangedListener = 96;
        static final int TRANSACTION_allowAutojoin = 27;
        static final int TRANSACTION_allowAutojoinGlobal = 25;
        static final int TRANSACTION_allowAutojoinPasspoint = 28;
        static final int TRANSACTION_calculateSignalLevel = 122;
        static final int TRANSACTION_clearExternalPnoScanRequest = 127;
        static final int TRANSACTION_clearOverrideCountryCode = 43;
        static final int TRANSACTION_clearWifiConnectedNetworkScorer = 125;
        static final int TRANSACTION_connect = 115;
        static final int TRANSACTION_disableEphemeralNetwork = 85;
        static final int TRANSACTION_disableNetwork = 24;
        static final int TRANSACTION_disconnect = 33;
        static final int TRANSACTION_enableNetwork = 23;
        static final int TRANSACTION_enableTdls = 80;
        static final int TRANSACTION_enableTdlsWithMacAddress = 81;
        static final int TRANSACTION_enableVerboseLogging = 83;
        static final int TRANSACTION_factoryReset = 86;
        static final int TRANSACTION_flushPasspointAnqpCache = 147;
        static final int TRANSACTION_forget = 117;
        static final int TRANSACTION_getAllMatchingFqdnsForScanResults = 8;
        static final int TRANSACTION_getAllMatchingPasspointProfilesForScanResults = 132;
        static final int TRANSACTION_getCapabilities = 102;
        static final int TRANSACTION_getConfiguredNetworks = 5;
        static final int TRANSACTION_getConnectionInfo = 36;
        static final int TRANSACTION_getCountryCode = 41;
        static final int TRANSACTION_getCurrentNetwork = 87;
        static final int TRANSACTION_getCurrentNetworkWpsNfcConfigurationToken = 82;
        static final int TRANSACTION_getDhcpInfo = 50;
        static final int TRANSACTION_getFactoryMacAddresses = 108;
        static final int TRANSACTION_getLastCallerInfoForApi = 128;
        static final int TRANSACTION_getMatchingOsuProviders = 11;
        static final int TRANSACTION_getMatchingPasspointConfigsForOsuProviders = 12;
        static final int TRANSACTION_getMatchingScanResults = 129;
        static final int TRANSACTION_getNetworkSuggestions = 107;
        static final int TRANSACTION_getOemPrivilegedWifiAdminPackages = 155;
        static final int TRANSACTION_getPasspointConfigurations = 17;
        static final int TRANSACTION_getPrivilegedConfiguredNetworks = 6;
        static final int TRANSACTION_getPrivilegedConnectedNetwork = 7;
        static final int TRANSACTION_getScanResults = 32;
        static final int TRANSACTION_getSoftApConfiguration = 76;
        static final int TRANSACTION_getSsidsAllowlist = 10;
        static final int TRANSACTION_getStaConcurrencyForMultiInternetMode = 151;
        static final int TRANSACTION_getSupportedFeatures = 1;
        static final int TRANSACTION_getUsableChannels = 148;
        static final int TRANSACTION_getVerboseLoggingLevel = 84;
        static final int TRANSACTION_getWifiActivityEnergyInfoAsync = 2;
        static final int TRANSACTION_getWifiApConfiguration = 75;
        static final int TRANSACTION_getWifiApEnabledState = 74;

        /* renamed from: TRANSACTION_getWifiConfigForMatchedNetworkSuggestionsSharedWithUser */
        static final int f51x512467be = 123;
        static final int TRANSACTION_getWifiConfigsForPasspointProfiles = 18;
        static final int TRANSACTION_getWifiEnabledState = 38;
        static final int TRANSACTION_initializeMulticastFiltering = 56;
        static final int TRANSACTION_is24GHzBandSupported = 45;
        static final int TRANSACTION_is5GHzBandSupported = 46;
        static final int TRANSACTION_is60GHzBandSupported = 48;
        static final int TRANSACTION_is6GHzBandSupported = 47;
        static final int TRANSACTION_isAutoWakeupEnabled = 134;
        static final int TRANSACTION_isCarrierNetworkOffloadEnabled = 138;
        static final int TRANSACTION_isDefaultCoexAlgorithmEnabled = 61;
        static final int TRANSACTION_isMulticastEnabled = 57;
        static final int TRANSACTION_isScanAlwaysAvailable = 52;
        static final int TRANSACTION_isScanThrottleEnabled = 131;
        static final int TRANSACTION_isWifiPasspointEnabled = 149;
        static final int TRANSACTION_isWifiStandardSupported = 49;
        static final int TRANSACTION_matchProviderWithCurrentNetwork = 20;
        static final int TRANSACTION_notifyMinimumRequiredWifiSecurityLevelChanged = 153;
        static final int TRANSACTION_notifyUserOfApBandConversion = 79;
        static final int TRANSACTION_notifyWifiSsidPolicyChanged = 154;
        static final int TRANSACTION_queryAutojoinGlobal = 26;
        static final int TRANSACTION_queryPasspointIcon = 19;
        static final int TRANSACTION_reassociate = 35;
        static final int TRANSACTION_reconnect = 34;
        static final int TRANSACTION_registerCoexCallback = 63;
        static final int TRANSACTION_registerDriverCountryCodeChangedListener = 39;
        static final int TRANSACTION_registerLocalOnlyHotspotSoftApCallback = 70;
        static final int TRANSACTION_registerNetworkRequestMatchCallback = 103;
        static final int TRANSACTION_registerScanResultsCallback = 118;
        static final int TRANSACTION_registerSoftApCallback = 94;
        static final int TRANSACTION_registerSubsystemRestartCallback = 139;
        static final int TRANSACTION_registerSuggestionConnectionStatusListener = 120;
        static final int TRANSACTION_registerTrafficStateCallback = 100;
        static final int TRANSACTION_releaseMulticastLock = 59;
        static final int TRANSACTION_releaseWifiLock = 55;
        static final int TRANSACTION_removeAppState = 145;
        static final int TRANSACTION_removeCustomDhcpOptions = 159;
        static final int TRANSACTION_removeNetwork = 21;
        static final int TRANSACTION_removeNetworkSuggestions = 106;
        static final int TRANSACTION_removeNonCallerConfiguredNetworks = 22;
        static final int TRANSACTION_removeOnWifiUsabilityStatsListener = 99;
        static final int TRANSACTION_removePasspointConfiguration = 16;
        static final int TRANSACTION_removeSuggestionUserApprovalStatusListener = 143;
        static final int TRANSACTION_removeWifiVerboseLoggingStatusChangedListener = 97;
        static final int TRANSACTION_replyToP2pInvitationReceivedDialog = 156;
        static final int TRANSACTION_replyToSimpleDialog = 157;
        static final int TRANSACTION_reportCreateInterfaceImpact = 160;
        static final int TRANSACTION_restartWifiSubsystem = 141;
        static final int TRANSACTION_restoreBackupData = 89;
        static final int TRANSACTION_restoreSoftApBackupData = 91;
        static final int TRANSACTION_restoreSupplicantBackupData = 92;
        static final int TRANSACTION_retrieveBackupData = 88;
        static final int TRANSACTION_retrieveSoftApBackupData = 90;
        static final int TRANSACTION_save = 116;
        static final int TRANSACTION_setAutoWakeupEnabled = 133;
        static final int TRANSACTION_setCarrierNetworkOffloadEnabled = 137;
        static final int TRANSACTION_setCoexUnsafeChannels = 62;
        static final int TRANSACTION_setDefaultCountryCode = 44;
        static final int TRANSACTION_setDeviceMobilityState = 109;
        static final int TRANSACTION_setEmergencyScanRequestInProgress = 144;
        static final int TRANSACTION_setExternalPnoScanRequest = 126;
        static final int TRANSACTION_setMacRandomizationSettingPasspointEnabled = 29;
        static final int TRANSACTION_setOneShotScreenOnConnectivityScanDelayMillis = 4;
        static final int TRANSACTION_setOverrideCountryCode = 42;
        static final int TRANSACTION_setPasspointMeteredOverride = 30;
        static final int TRANSACTION_setScanAlwaysAvailable = 51;
        static final int TRANSACTION_setScanThrottleEnabled = 130;
        static final int TRANSACTION_setScreenOnScanSchedule = 3;
        static final int TRANSACTION_setSoftApConfiguration = 78;
        static final int TRANSACTION_setSsidsAllowlist = 9;
        static final int TRANSACTION_setStaConcurrencyForMultiInternetMode = 152;
        static final int TRANSACTION_setWifiApConfiguration = 77;
        static final int TRANSACTION_setWifiConnectedNetworkScorer = 124;
        static final int TRANSACTION_setWifiEnabled = 37;
        static final int TRANSACTION_setWifiPasspointEnabled = 150;
        static final int TRANSACTION_setWifiScoringEnabled = 146;
        static final int TRANSACTION_startDppAsConfiguratorInitiator = 110;
        static final int TRANSACTION_startDppAsEnrolleeInitiator = 111;
        static final int TRANSACTION_startDppAsEnrolleeResponder = 112;
        static final int TRANSACTION_startLocalOnlyHotspot = 68;
        static final int TRANSACTION_startRestrictingAutoJoinToSubscriptionId = 135;
        static final int TRANSACTION_startScan = 31;
        static final int TRANSACTION_startSoftAp = 65;
        static final int TRANSACTION_startSubscriptionProvisioning = 93;
        static final int TRANSACTION_startTetheredHotspot = 66;
        static final int TRANSACTION_startWatchLocalOnlyHotspot = 72;
        static final int TRANSACTION_stopDppSession = 113;
        static final int TRANSACTION_stopLocalOnlyHotspot = 69;
        static final int TRANSACTION_stopRestrictingAutoJoinToSubscriptionId = 136;
        static final int TRANSACTION_stopSoftAp = 67;
        static final int TRANSACTION_stopWatchLocalOnlyHotspot = 73;
        static final int TRANSACTION_unregisterCoexCallback = 64;
        static final int TRANSACTION_unregisterDriverCountryCodeChangedListener = 40;
        static final int TRANSACTION_unregisterLocalOnlyHotspotSoftApCallback = 71;
        static final int TRANSACTION_unregisterNetworkRequestMatchCallback = 104;
        static final int TRANSACTION_unregisterScanResultsCallback = 119;
        static final int TRANSACTION_unregisterSoftApCallback = 95;
        static final int TRANSACTION_unregisterSubsystemRestartCallback = 140;
        static final int TRANSACTION_unregisterSuggestionConnectionStatusListener = 121;
        static final int TRANSACTION_unregisterTrafficStateCallback = 101;
        static final int TRANSACTION_updateInterfaceIpState = 60;
        static final int TRANSACTION_updateWifiLockWorkSource = 54;
        static final int TRANSACTION_updateWifiUsabilityScore = 114;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiManager)) {
                return new Proxy(iBinder);
            }
            return (IWifiManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        long supportedFeatures = getSupportedFeatures();
                        parcel2.writeNoException();
                        parcel2.writeLong(supportedFeatures);
                        break;
                    case 2:
                        getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 3:
                        setScreenOnScanSchedule(parcel.createIntArray(), parcel.createIntArray());
                        parcel2.writeNoException();
                        break;
                    case 4:
                        setOneShotScreenOnConnectivityScanDelayMillis(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 5:
                        ParceledListSlice configuredNetworks = getConfiguredNetworks(parcel.readString(), parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(configuredNetworks, 1);
                        break;
                    case 6:
                        ParceledListSlice privilegedConfiguredNetworks = getPrivilegedConfiguredNetworks(parcel.readString(), parcel.readString(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(privilegedConfiguredNetworks, 1);
                        break;
                    case 7:
                        WifiConfiguration privilegedConnectedNetwork = getPrivilegedConnectedNetwork(parcel.readString(), parcel.readString(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(privilegedConnectedNetwork, 1);
                        break;
                    case 8:
                        Map allMatchingFqdnsForScanResults = getAllMatchingFqdnsForScanResults(parcel.createTypedArrayList(ScanResult.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeMap(allMatchingFqdnsForScanResults);
                        break;
                    case 9:
                        setSsidsAllowlist(parcel.readString(), parcel.createTypedArrayList(WifiSsid.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 10:
                        List ssidsAllowlist = getSsidsAllowlist(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeList(ssidsAllowlist);
                        break;
                    case 11:
                        Map matchingOsuProviders = getMatchingOsuProviders(parcel.createTypedArrayList(ScanResult.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeMap(matchingOsuProviders);
                        break;
                    case 12:
                        Map matchingPasspointConfigsForOsuProviders = getMatchingPasspointConfigsForOsuProviders(parcel.createTypedArrayList(OsuProvider.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeMap(matchingPasspointConfigsForOsuProviders);
                        break;
                    case 13:
                        int addOrUpdateNetwork = addOrUpdateNetwork((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), parcel.readString(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeInt(addOrUpdateNetwork);
                        break;
                    case 14:
                        WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged = addOrUpdateNetworkPrivileged((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(addOrUpdateNetworkPrivileged, 1);
                        break;
                    case 15:
                        boolean addOrUpdatePasspointConfiguration = addOrUpdatePasspointConfiguration((PasspointConfiguration) parcel.readTypedObject(PasspointConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(addOrUpdatePasspointConfiguration);
                        break;
                    case 16:
                        boolean removePasspointConfiguration = removePasspointConfiguration(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(removePasspointConfiguration);
                        break;
                    case 17:
                        List<PasspointConfiguration> passpointConfigurations = getPasspointConfigurations(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedList(passpointConfigurations);
                        break;
                    case 18:
                        List<WifiConfiguration> wifiConfigsForPasspointProfiles = getWifiConfigsForPasspointProfiles(parcel.createStringArrayList());
                        parcel2.writeNoException();
                        parcel2.writeTypedList(wifiConfigsForPasspointProfiles);
                        break;
                    case 19:
                        queryPasspointIcon(parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 20:
                        int matchProviderWithCurrentNetwork = matchProviderWithCurrentNetwork(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(matchProviderWithCurrentNetwork);
                        break;
                    case 21:
                        boolean removeNetwork = removeNetwork(parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(removeNetwork);
                        break;
                    case 22:
                        boolean removeNonCallerConfiguredNetworks = removeNonCallerConfiguredNetworks(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(removeNonCallerConfiguredNetworks);
                        break;
                    case 23:
                        boolean enableNetwork = enableNetwork(parcel.readInt(), parcel.readBoolean(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(enableNetwork);
                        break;
                    case 24:
                        boolean disableNetwork = disableNetwork(parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(disableNetwork);
                        break;
                    case 25:
                        allowAutojoinGlobal(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 26:
                        queryAutojoinGlobal(IBooleanListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 27:
                        allowAutojoin(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 28:
                        allowAutojoinPasspoint(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 29:
                        setMacRandomizationSettingPasspointEnabled(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 30:
                        setPasspointMeteredOverride(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 31:
                        boolean startScan = startScan(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(startScan);
                        break;
                    case 32:
                        List<ScanResult> scanResults = getScanResults(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedList(scanResults);
                        break;
                    case 33:
                        boolean disconnect = disconnect(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(disconnect);
                        break;
                    case 34:
                        boolean reconnect = reconnect(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(reconnect);
                        break;
                    case 35:
                        boolean reassociate = reassociate(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(reassociate);
                        break;
                    case 36:
                        WifiInfo connectionInfo = getConnectionInfo(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(connectionInfo, 1);
                        break;
                    case 37:
                        boolean wifiEnabled = setWifiEnabled(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(wifiEnabled);
                        break;
                    case 38:
                        int wifiEnabledState = getWifiEnabledState();
                        parcel2.writeNoException();
                        parcel2.writeInt(wifiEnabledState);
                        break;
                    case 39:
                        registerDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 40:
                        unregisterDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 41:
                        String countryCode = getCountryCode(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeString(countryCode);
                        break;
                    case 42:
                        setOverrideCountryCode(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 43:
                        clearOverrideCountryCode();
                        parcel2.writeNoException();
                        break;
                    case 44:
                        setDefaultCountryCode(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 45:
                        boolean is24GHzBandSupported = is24GHzBandSupported();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(is24GHzBandSupported);
                        break;
                    case 46:
                        boolean is5GHzBandSupported = is5GHzBandSupported();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(is5GHzBandSupported);
                        break;
                    case 47:
                        boolean is6GHzBandSupported = is6GHzBandSupported();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(is6GHzBandSupported);
                        break;
                    case 48:
                        boolean is60GHzBandSupported = is60GHzBandSupported();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(is60GHzBandSupported);
                        break;
                    case 49:
                        boolean isWifiStandardSupported = isWifiStandardSupported(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isWifiStandardSupported);
                        break;
                    case 50:
                        DhcpInfo dhcpInfo = getDhcpInfo(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(dhcpInfo, 1);
                        break;
                    case 51:
                        setScanAlwaysAvailable(parcel.readBoolean(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 52:
                        boolean isScanAlwaysAvailable = isScanAlwaysAvailable();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isScanAlwaysAvailable);
                        break;
                    case 53:
                        boolean acquireWifiLock = acquireWifiLock(parcel.readStrongBinder(), parcel.readInt(), parcel.readString(), (WorkSource) parcel.readTypedObject(WorkSource.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeBoolean(acquireWifiLock);
                        break;
                    case 54:
                        updateWifiLockWorkSource(parcel.readStrongBinder(), (WorkSource) parcel.readTypedObject(WorkSource.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 55:
                        boolean releaseWifiLock = releaseWifiLock(parcel.readStrongBinder());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(releaseWifiLock);
                        break;
                    case 56:
                        initializeMulticastFiltering();
                        parcel2.writeNoException();
                        break;
                    case 57:
                        boolean isMulticastEnabled = isMulticastEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isMulticastEnabled);
                        break;
                    case 58:
                        acquireMulticastLock(parcel.readStrongBinder(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 59:
                        releaseMulticastLock(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 60:
                        updateInterfaceIpState(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 61:
                        boolean isDefaultCoexAlgorithmEnabled = isDefaultCoexAlgorithmEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isDefaultCoexAlgorithmEnabled);
                        break;
                    case 62:
                        setCoexUnsafeChannels(parcel.createTypedArrayList(CoexUnsafeChannel.CREATOR), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 63:
                        registerCoexCallback(ICoexCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 64:
                        unregisterCoexCallback(ICoexCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 65:
                        boolean startSoftAp = startSoftAp((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(startSoftAp);
                        break;
                    case 66:
                        boolean startTetheredHotspot = startTetheredHotspot((SoftApConfiguration) parcel.readTypedObject(SoftApConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(startTetheredHotspot);
                        break;
                    case 67:
                        boolean stopSoftAp = stopSoftAp();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(stopSoftAp);
                        break;
                    case 68:
                        int startLocalOnlyHotspot = startLocalOnlyHotspot(ILocalOnlyHotspotCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), (SoftApConfiguration) parcel.readTypedObject(SoftApConfiguration.CREATOR), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeInt(startLocalOnlyHotspot);
                        break;
                    case 69:
                        stopLocalOnlyHotspot();
                        parcel2.writeNoException();
                        break;
                    case 70:
                        registerLocalOnlyHotspotSoftApCallback(ISoftApCallback.Stub.asInterface(parcel.readStrongBinder()), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 71:
                        unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback.Stub.asInterface(parcel.readStrongBinder()), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 72:
                        startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 73:
                        stopWatchLocalOnlyHotspot();
                        parcel2.writeNoException();
                        break;
                    case 74:
                        int wifiApEnabledState = getWifiApEnabledState();
                        parcel2.writeNoException();
                        parcel2.writeInt(wifiApEnabledState);
                        break;
                    case 75:
                        WifiConfiguration wifiApConfiguration = getWifiApConfiguration();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(wifiApConfiguration, 1);
                        break;
                    case 76:
                        SoftApConfiguration softApConfiguration = getSoftApConfiguration();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(softApConfiguration, 1);
                        break;
                    case 77:
                        boolean wifiApConfiguration2 = setWifiApConfiguration((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(wifiApConfiguration2);
                        break;
                    case 78:
                        boolean softApConfiguration2 = setSoftApConfiguration((SoftApConfiguration) parcel.readTypedObject(SoftApConfiguration.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(softApConfiguration2);
                        break;
                    case 79:
                        notifyUserOfApBandConversion(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 80:
                        enableTdls(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 81:
                        enableTdlsWithMacAddress(parcel.readString(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 82:
                        String currentNetworkWpsNfcConfigurationToken = getCurrentNetworkWpsNfcConfigurationToken();
                        parcel2.writeNoException();
                        parcel2.writeString(currentNetworkWpsNfcConfigurationToken);
                        break;
                    case 83:
                        enableVerboseLogging(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 84:
                        int verboseLoggingLevel = getVerboseLoggingLevel();
                        parcel2.writeNoException();
                        parcel2.writeInt(verboseLoggingLevel);
                        break;
                    case 85:
                        disableEphemeralNetwork(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 86:
                        factoryReset(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 87:
                        Network currentNetwork = getCurrentNetwork();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(currentNetwork, 1);
                        break;
                    case 88:
                        byte[] retrieveBackupData = retrieveBackupData();
                        parcel2.writeNoException();
                        parcel2.writeByteArray(retrieveBackupData);
                        break;
                    case 89:
                        restoreBackupData(parcel.createByteArray());
                        parcel2.writeNoException();
                        break;
                    case 90:
                        byte[] retrieveSoftApBackupData = retrieveSoftApBackupData();
                        parcel2.writeNoException();
                        parcel2.writeByteArray(retrieveSoftApBackupData);
                        break;
                    case 91:
                        SoftApConfiguration restoreSoftApBackupData = restoreSoftApBackupData(parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(restoreSoftApBackupData, 1);
                        break;
                    case 92:
                        restoreSupplicantBackupData(parcel.createByteArray(), parcel.createByteArray());
                        parcel2.writeNoException();
                        break;
                    case 93:
                        startSubscriptionProvisioning((OsuProvider) parcel.readTypedObject(OsuProvider.CREATOR), IProvisioningCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 94:
                        registerSoftApCallback(ISoftApCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 95:
                        unregisterSoftApCallback(ISoftApCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 96:
                        addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 97:
                        removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 98:
                        addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 99:
                        removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 100:
                        registerTrafficStateCallback(ITrafficStateCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 101:
                        unregisterTrafficStateCallback(ITrafficStateCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 102:
                        String capabilities = getCapabilities(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeString(capabilities);
                        break;
                    case 103:
                        registerNetworkRequestMatchCallback(INetworkRequestMatchCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 104:
                        unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 105:
                        int addNetworkSuggestions = addNetworkSuggestions(parcel.createTypedArrayList(WifiNetworkSuggestion.CREATOR), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(addNetworkSuggestions);
                        break;
                    case 106:
                        int removeNetworkSuggestions = removeNetworkSuggestions(parcel.createTypedArrayList(WifiNetworkSuggestion.CREATOR), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(removeNetworkSuggestions);
                        break;
                    case 107:
                        List<WifiNetworkSuggestion> networkSuggestions = getNetworkSuggestions(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeTypedList(networkSuggestions);
                        break;
                    case 108:
                        String[] factoryMacAddresses = getFactoryMacAddresses();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(factoryMacAddresses);
                        break;
                    case 109:
                        setDeviceMobilityState(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 110:
                        startDppAsConfiguratorInitiator(parcel.readStrongBinder(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), IDppCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 111:
                        startDppAsEnrolleeInitiator(parcel.readStrongBinder(), parcel.readString(), IDppCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 112:
                        startDppAsEnrolleeResponder(parcel.readStrongBinder(), parcel.readString(), parcel.readInt(), IDppCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 113:
                        stopDppSession();
                        parcel2.writeNoException();
                        break;
                    case 114:
                        updateWifiUsabilityScore(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 115:
                        connect((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), parcel.readInt(), IActionListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        break;
                    case 116:
                        save((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR), IActionListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        break;
                    case 117:
                        forget(parcel.readInt(), IActionListener.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 118:
                        registerScanResultsCallback(IScanResultsCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 119:
                        unregisterScanResultsCallback(IScanResultsCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 120:
                        registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 121:
                        unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 122:
                        int calculateSignalLevel = calculateSignalLevel(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(calculateSignalLevel);
                        break;
                    case 123:
                        List<WifiConfiguration> wifiConfigForMatchedNetworkSuggestionsSharedWithUser = getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(parcel.createTypedArrayList(ScanResult.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedList(wifiConfigForMatchedNetworkSuggestionsSharedWithUser);
                        break;
                    case 124:
                        boolean wifiConnectedNetworkScorer = setWifiConnectedNetworkScorer(parcel.readStrongBinder(), IWifiConnectedNetworkScorer.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeBoolean(wifiConnectedNetworkScorer);
                        break;
                    case 125:
                        clearWifiConnectedNetworkScorer();
                        parcel2.writeNoException();
                        break;
                    case 126:
                        setExternalPnoScanRequest(parcel.readStrongBinder(), IPnoScanResultsCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.createTypedArrayList(WifiSsid.CREATOR), parcel.createIntArray(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 127:
                        clearExternalPnoScanRequest();
                        parcel2.writeNoException();
                        break;
                    case 128:
                        getLastCallerInfoForApi(parcel.readInt(), ILastCallerListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 129:
                        Map matchingScanResults = getMatchingScanResults(parcel.createTypedArrayList(WifiNetworkSuggestion.CREATOR), parcel.createTypedArrayList(ScanResult.CREATOR), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeMap(matchingScanResults);
                        break;
                    case 130:
                        setScanThrottleEnabled(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 131:
                        boolean isScanThrottleEnabled = isScanThrottleEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isScanThrottleEnabled);
                        break;
                    case 132:
                        Map allMatchingPasspointProfilesForScanResults = getAllMatchingPasspointProfilesForScanResults(parcel.createTypedArrayList(ScanResult.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeMap(allMatchingPasspointProfilesForScanResults);
                        break;
                    case 133:
                        setAutoWakeupEnabled(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 134:
                        boolean isAutoWakeupEnabled = isAutoWakeupEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isAutoWakeupEnabled);
                        break;
                    case 135:
                        startRestrictingAutoJoinToSubscriptionId(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 136:
                        stopRestrictingAutoJoinToSubscriptionId();
                        parcel2.writeNoException();
                        break;
                    case 137:
                        setCarrierNetworkOffloadEnabled(parcel.readInt(), parcel.readBoolean(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 138:
                        boolean isCarrierNetworkOffloadEnabled = isCarrierNetworkOffloadEnabled(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isCarrierNetworkOffloadEnabled);
                        break;
                    case 139:
                        registerSubsystemRestartCallback(ISubsystemRestartCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 140:
                        unregisterSubsystemRestartCallback(ISubsystemRestartCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 141:
                        restartWifiSubsystem();
                        parcel2.writeNoException();
                        break;
                    case 142:
                        addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 143:
                        removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 144:
                        setEmergencyScanRequestInProgress(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 145:
                        removeAppState(parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 146:
                        boolean wifiScoringEnabled = setWifiScoringEnabled(parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(wifiScoringEnabled);
                        break;
                    case 147:
                        flushPasspointAnqpCache(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 148:
                        List<WifiAvailableChannel> usableChannels = getUsableChannels(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeTypedList(usableChannels);
                        break;
                    case 149:
                        boolean isWifiPasspointEnabled = isWifiPasspointEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isWifiPasspointEnabled);
                        break;
                    case 150:
                        setWifiPasspointEnabled(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 151:
                        int staConcurrencyForMultiInternetMode = getStaConcurrencyForMultiInternetMode();
                        parcel2.writeNoException();
                        parcel2.writeInt(staConcurrencyForMultiInternetMode);
                        break;
                    case 152:
                        boolean staConcurrencyForMultiInternetMode2 = setStaConcurrencyForMultiInternetMode(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(staConcurrencyForMultiInternetMode2);
                        break;
                    case 153:
                        notifyMinimumRequiredWifiSecurityLevelChanged(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 154:
                        notifyWifiSsidPolicyChanged(parcel.readInt(), parcel.createTypedArrayList(WifiSsid.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 155:
                        String[] oemPrivilegedWifiAdminPackages = getOemPrivilegedWifiAdminPackages();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(oemPrivilegedWifiAdminPackages);
                        break;
                    case 156:
                        replyToP2pInvitationReceivedDialog(parcel.readInt(), parcel.readBoolean(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 157:
                        replyToSimpleDialog(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 158:
                        addCustomDhcpOptions((WifiSsid) parcel.readTypedObject(WifiSsid.CREATOR), parcel.createByteArray(), parcel.createTypedArrayList(DhcpOption.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 159:
                        removeCustomDhcpOptions((WifiSsid) parcel.readTypedObject(WifiSsid.CREATOR), parcel.createByteArray());
                        parcel2.writeNoException();
                        break;
                    case 160:
                        reportCreateInterfaceImpact(parcel.readString(), parcel.readInt(), parcel.readBoolean(), IInterfaceCreationInfoCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public long getSupportedFeatures() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener iOnWifiActivityEnergyInfoListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnWifiActivityEnergyInfoListener);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setScreenOnScanSchedule(int[] iArr, int[] iArr2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    obtain.writeIntArray(iArr2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOneShotScreenOnConnectivityScanDelayMillis(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ParceledListSlice getConfiguredNetworks(String str, String str2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return (ParceledListSlice) obtain2.readTypedObject(ParceledListSlice.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ParceledListSlice getPrivilegedConfiguredNetworks(String str, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return (ParceledListSlice) obtain2.readTypedObject(ParceledListSlice.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public WifiConfiguration getPrivilegedConnectedNetwork(String str, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return (WifiConfiguration) obtain2.readTypedObject(WifiConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getAllMatchingFqdnsForScanResults(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSsidsAllowlist(String str, List<WifiSsid> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List getSsidsAllowlist(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readArrayList(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getMatchingOsuProviders(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeString(str);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return (WifiManager.AddNetworkResult) obtain2.readTypedObject(WifiManager.AddNetworkResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(passpointConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removePasspointConfiguration(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<PasspointConfiguration> getPasspointConfigurations(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(PasspointConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStringList(list);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(WifiConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void queryPasspointIcon(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int matchProviderWithCurrentNetwork(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removeNetwork(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removeNonCallerConfiguredNetworks(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean enableNetwork(int i, boolean z, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean disableNetwork(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void allowAutojoinGlobal(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void queryAutojoinGlobal(IBooleanListener iBooleanListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iBooleanListener);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void allowAutojoin(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void allowAutojoinPasspoint(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMacRandomizationSettingPasspointEnabled(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPasspointMeteredOverride(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean startScan(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<ScanResult> getScanResults(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(ScanResult.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean disconnect(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean reconnect(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean reassociate(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public WifiInfo getConnectionInfo(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                    return (WifiInfo) obtain2.readTypedObject(WifiInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setWifiEnabled(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getWifiEnabledState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnWifiDriverCountryCodeChangedListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterDriverCountryCodeChangedListener(IOnWifiDriverCountryCodeChangedListener iOnWifiDriverCountryCodeChangedListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnWifiDriverCountryCodeChangedListener);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCountryCode(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOverrideCountryCode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearOverrideCountryCode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDefaultCountryCode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean is24GHzBandSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean is5GHzBandSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean is6GHzBandSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean is60GHzBandSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isWifiStandardSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public DhcpInfo getDhcpInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                    return (DhcpInfo) obtain2.readTypedObject(DhcpInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setScanAlwaysAvailable(boolean z, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isScanAlwaysAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeTypedObject(workSource, 0);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeTypedObject(workSource, 0);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean releaseWifiLock(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void initializeMulticastFiltering() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isMulticastEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void releaseMulticastLock(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateInterfaceIpState(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDefaultCoexAlgorithmEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCoexUnsafeChannels(List<CoexUnsafeChannel> list, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeInt(i);
                    this.mRemote.transact(62, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCoexCallback(ICoexCallback iCoexCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iCoexCallback);
                    this.mRemote.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterCoexCallback(ICoexCallback iCoexCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iCoexCallback);
                    this.mRemote.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean startSoftAp(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean startTetheredHotspot(SoftApConfiguration softApConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(softApConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean stopSoftAp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback, String str, String str2, SoftApConfiguration softApConfiguration, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iLocalOnlyHotspotCallback);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(softApConfiguration, 0);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopLocalOnlyHotspot() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSoftApCallback);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(70, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback iSoftApCallback, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSoftApCallback);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(71, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback iLocalOnlyHotspotCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iLocalOnlyHotspotCallback);
                    this.mRemote.transact(72, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopWatchLocalOnlyHotspot() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getWifiApEnabledState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public WifiConfiguration getWifiApConfiguration() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, obtain, obtain2, 0);
                    obtain2.readException();
                    return (WifiConfiguration) obtain2.readTypedObject(WifiConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SoftApConfiguration getSoftApConfiguration() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, obtain, obtain2, 0);
                    obtain2.readException();
                    return (SoftApConfiguration) obtain2.readTypedObject(SoftApConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(77, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setSoftApConfiguration(SoftApConfiguration softApConfiguration, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(softApConfiguration, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(78, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyUserOfApBandConversion(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(79, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enableTdls(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(82, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enableVerboseLogging(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getVerboseLoggingLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disableEphemeralNetwork(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void factoryReset(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(86, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Network getCurrentNetwork() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(87, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network) obtain2.readTypedObject(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] retrieveBackupData() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(88, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void restoreBackupData(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(89, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] retrieveSoftApBackupData() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(90, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SoftApConfiguration restoreSoftApBackupData(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(91, obtain, obtain2, 0);
                    obtain2.readException();
                    return (SoftApConfiguration) obtain2.readTypedObject(SoftApConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    this.mRemote.transact(92, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(osuProvider, 0);
                    obtain.writeStrongInterface(iProvisioningCallback);
                    this.mRemote.transact(93, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSoftApCallback);
                    this.mRemote.transact(94, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterSoftApCallback(ISoftApCallback iSoftApCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSoftApCallback);
                    this.mRemote.transact(95, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iWifiVerboseLoggingStatusChangedListener);
                    this.mRemote.transact(96, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener iWifiVerboseLoggingStatusChangedListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iWifiVerboseLoggingStatusChangedListener);
                    this.mRemote.transact(97, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnWifiUsabilityStatsListener);
                    this.mRemote.transact(98, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnWifiUsabilityStatsListener);
                    this.mRemote.transact(99, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iTrafficStateCallback);
                    this.mRemote.transact(100, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterTrafficStateCallback(ITrafficStateCallback iTrafficStateCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iTrafficStateCallback);
                    this.mRemote.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCapabilities(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkRequestMatchCallback);
                    this.mRemote.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback iNetworkRequestMatchCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkRequestMatchCallback);
                    this.mRemote.transact(104, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(105, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(106, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<WifiNetworkSuggestion> getNetworkSuggestions(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(107, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(WifiNetworkSuggestion.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getFactoryMacAddresses() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(108, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDeviceMobilityState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(109, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDppAsConfiguratorInitiator(IBinder iBinder, String str, String str2, int i, int i2, IDppCallback iDppCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStrongInterface(iDppCallback);
                    this.mRemote.transact(110, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDppAsEnrolleeInitiator(IBinder iBinder, String str, IDppCallback iDppCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iDppCallback);
                    this.mRemote.transact(111, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDppAsEnrolleeResponder(IBinder iBinder, String str, int i, IDppCallback iDppCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iDppCallback);
                    this.mRemote.transact(112, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopDppSession() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(113, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateWifiUsabilityScore(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(114, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void connect(WifiConfiguration wifiConfiguration, int i, IActionListener iActionListener, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iActionListener);
                    obtain.writeString(str);
                    this.mRemote.transact(115, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void save(WifiConfiguration wifiConfiguration, IActionListener iActionListener, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    obtain.writeStrongInterface(iActionListener);
                    obtain.writeString(str);
                    this.mRemote.transact(116, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void forget(int i, IActionListener iActionListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iActionListener);
                    this.mRemote.transact(117, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void registerScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iScanResultsCallback);
                    this.mRemote.transact(118, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterScanResultsCallback(IScanResultsCallback iScanResultsCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iScanResultsCallback);
                    this.mRemote.transact(119, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSuggestionConnectionStatusListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(120, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener iSuggestionConnectionStatusListener, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSuggestionConnectionStatusListener);
                    obtain.writeString(str);
                    this.mRemote.transact(121, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int calculateSignalLevel(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(122, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(123, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(WifiConfiguration.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setWifiConnectedNetworkScorer(IBinder iBinder, IWifiConnectedNetworkScorer iWifiConnectedNetworkScorer) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongInterface(iWifiConnectedNetworkScorer);
                    this.mRemote.transact(124, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearWifiConnectedNetworkScorer() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(125, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setExternalPnoScanRequest(IBinder iBinder, IPnoScanResultsCallback iPnoScanResultsCallback, List<WifiSsid> list, int[] iArr, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongInterface(iPnoScanResultsCallback);
                    obtain.writeTypedList(list);
                    obtain.writeIntArray(iArr);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(126, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearExternalPnoScanRequest() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(127, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getLastCallerInfoForApi(int i, ILastCallerListener iLastCallerListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iLastCallerListener);
                    this.mRemote.transact(128, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getMatchingScanResults(List<WifiNetworkSuggestion> list, List<ScanResult> list2, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeTypedList(list2);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(129, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setScanThrottleEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(130, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isScanThrottleEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(131, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getAllMatchingPasspointProfilesForScanResults(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(132, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readHashMap(getClass().getClassLoader());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAutoWakeupEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(133, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isAutoWakeupEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(134, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startRestrictingAutoJoinToSubscriptionId(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(135, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopRestrictingAutoJoinToSubscriptionId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(136, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCarrierNetworkOffloadEnabled(int i, boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    this.mRemote.transact(137, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isCarrierNetworkOffloadEnabled(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(138, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSubsystemRestartCallback);
                    this.mRemote.transact(139, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterSubsystemRestartCallback(ISubsystemRestartCallback iSubsystemRestartCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSubsystemRestartCallback);
                    this.mRemote.transact(140, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void restartWifiSubsystem() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(141, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSuggestionUserApprovalStatusListener);
                    obtain.writeString(str);
                    this.mRemote.transact(142, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener iSuggestionUserApprovalStatusListener, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iSuggestionUserApprovalStatusListener);
                    obtain.writeString(str);
                    this.mRemote.transact(143, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setEmergencyScanRequestInProgress(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(144, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeAppState(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(145, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setWifiScoringEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(146, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void flushPasspointAnqpCache(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(147, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<WifiAvailableChannel> getUsableChannels(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(148, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(WifiAvailableChannel.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isWifiPasspointEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(149, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setWifiPasspointEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(150, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getStaConcurrencyForMultiInternetMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(151, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setStaConcurrencyForMultiInternetMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(152, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyMinimumRequiredWifiSecurityLevelChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(153, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyWifiSsidPolicyChanged(int i, List<WifiSsid> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(154, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getOemPrivilegedWifiAdminPackages() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(155, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void replyToP2pInvitationReceivedDialog(int i, boolean z, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeString(str);
                    this.mRemote.transact(156, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void replyToSimpleDialog(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(157, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr, List<DhcpOption> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiSsid, 0);
                    obtain.writeByteArray(bArr);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(158, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(wifiSsid, 0);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(159, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportCreateInterfaceImpact(String str, int i, boolean z, IInterfaceCreationInfoCallback iInterfaceCreationInfoCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeStrongInterface(iInterfaceCreationInfoCallback);
                    this.mRemote.transact(160, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
