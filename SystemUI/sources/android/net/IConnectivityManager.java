package android.net;

import android.app.PendingIntent;
import android.net.IConnectivityDiagnosticsCallback;
import android.net.INetworkActivityListener;
import android.net.INetworkAgent;
import android.net.INetworkOfferCallback;
import android.net.IOnCompleteListener;
import android.net.IQosCallback;
import android.net.ISocketKeepaliveCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

public interface IConnectivityManager extends IInterface {

    public static class Default implements IConnectivityManager {
        public IBinder asBinder() {
            return null;
        }

        public void declareNetworkRequestUnfulfillable(NetworkRequest networkRequest) throws RemoteException {
        }

        public void factoryReset() throws RemoteException {
        }

        public LinkProperties getActiveLinkProperties() throws RemoteException {
            return null;
        }

        public Network getActiveNetwork() throws RemoteException {
            return null;
        }

        public Network getActiveNetworkForUid(int i, boolean z) throws RemoteException {
            return null;
        }

        public NetworkInfo getActiveNetworkInfo() throws RemoteException {
            return null;
        }

        public NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) throws RemoteException {
            return null;
        }

        public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
            return null;
        }

        public NetworkState[] getAllNetworkState() throws RemoteException {
            return null;
        }

        public List<NetworkStateSnapshot> getAllNetworkStateSnapshots() throws RemoteException {
            return null;
        }

        public Network[] getAllNetworks() throws RemoteException {
            return null;
        }

        public String getCaptivePortalServerUrl() throws RemoteException {
            return null;
        }

        public int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException {
            return 0;
        }

        public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i, String str, String str2) throws RemoteException {
            return null;
        }

        public NetworkRequest getDefaultRequest() throws RemoteException {
            return null;
        }

        public ProxyInfo getGlobalProxy() throws RemoteException {
            return null;
        }

        public int getLastTetherError(String str) throws RemoteException {
            return 0;
        }

        public LinkProperties getLinkProperties(Network network) throws RemoteException {
            return null;
        }

        public LinkProperties getLinkPropertiesForType(int i) throws RemoteException {
            return null;
        }

        public int getMultipathPreference(Network network) throws RemoteException {
            return 0;
        }

        public NetworkCapabilities getNetworkCapabilities(Network network, String str, String str2) throws RemoteException {
            return null;
        }

        public Network getNetworkForType(int i) throws RemoteException {
            return null;
        }

        public NetworkInfo getNetworkInfo(int i) throws RemoteException {
            return null;
        }

        public NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) throws RemoteException {
            return null;
        }

        public byte[] getNetworkWatchlistConfigHash() throws RemoteException {
            return null;
        }

        public ProxyInfo getProxyForNetwork(Network network) throws RemoteException {
            return null;
        }

        public LinkProperties getRedactedLinkPropertiesForPackage(LinkProperties linkProperties, int i, String str, String str2) throws RemoteException {
            return null;
        }

        public NetworkCapabilities getRedactedNetworkCapabilitiesForPackage(NetworkCapabilities networkCapabilities, int i, String str, String str2) throws RemoteException {
            return null;
        }

        public int getRestoreDefaultNetworkDelay(int i) throws RemoteException {
            return 0;
        }

        public int getRestrictBackgroundStatusByCaller() throws RemoteException {
            return 0;
        }

        public String[] getTetherableIfaces() throws RemoteException {
            return null;
        }

        public String[] getTetherableUsbRegexs() throws RemoteException {
            return null;
        }

        public String[] getTetherableWifiRegexs() throws RemoteException {
            return null;
        }

        public String[] getTetheredIfaces() throws RemoteException {
            return null;
        }

        public String[] getTetheringErroredIfaces() throws RemoteException {
            return null;
        }

        public boolean isActiveNetworkMetered() throws RemoteException {
            return false;
        }

        public boolean isDefaultNetworkActive() throws RemoteException {
            return false;
        }

        public boolean isNetworkSupported(int i) throws RemoteException {
            return false;
        }

        public NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder, int i, String str, String str2) throws RemoteException {
            return null;
        }

        public void offerNetwork(int i, NetworkScore networkScore, NetworkCapabilities networkCapabilities, INetworkOfferCallback iNetworkOfferCallback) throws RemoteException {
        }

        public void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException {
        }

        public NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException {
            return null;
        }

        public void registerConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback, NetworkRequest networkRequest, String str) throws RemoteException {
        }

        public void registerNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException {
        }

        public Network registerNetworkAgent(INetworkAgent iNetworkAgent, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, int i) throws RemoteException {
            return null;
        }

        public int registerNetworkProvider(Messenger messenger, String str) throws RemoteException {
            return 0;
        }

        public void registerQosSocketCallback(QosSocketInfo qosSocketInfo, IQosCallback iQosCallback) throws RemoteException {
        }

        public void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
        }

        public void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException {
        }

        public void replaceFirewallChain(int i, int[] iArr) throws RemoteException {
        }

        public void reportInetCondition(int i, int i2) throws RemoteException {
        }

        public void reportNetworkConnectivity(Network network, boolean z) throws RemoteException {
        }

        public boolean requestBandwidthUpdate(Network network) throws RemoteException {
            return false;
        }

        public NetworkRequest requestNetwork(int i, NetworkCapabilities networkCapabilities, int i2, Messenger messenger, int i3, IBinder iBinder, int i4, int i5, String str, String str2) throws RemoteException {
            return null;
        }

        public boolean requestRouteToHostAddress(int i, byte[] bArr, String str, String str2) throws RemoteException {
            return false;
        }

        public void setAcceptPartialConnectivity(Network network, boolean z, boolean z2) throws RemoteException {
        }

        public void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException {
        }

        public void setAirplaneMode(boolean z) throws RemoteException {
        }

        public void setAvoidUnvalidated(Network network) throws RemoteException {
        }

        public void setFirewallChainEnabled(int i, boolean z) throws RemoteException {
        }

        public void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException {
        }

        public void setLegacyLockdownVpnEnabled(boolean z) throws RemoteException {
        }

        public void setOemNetworkPreference(OemNetworkPreferences oemNetworkPreferences, IOnCompleteListener iOnCompleteListener) throws RemoteException {
        }

        public void setProfileNetworkPreferences(UserHandle userHandle, List<ProfileNetworkPreference> list, IOnCompleteListener iOnCompleteListener) throws RemoteException {
        }

        public void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException {
        }

        public void setRequireVpnForUids(boolean z, UidRange[] uidRangeArr) throws RemoteException {
        }

        public void setTestAllowBadWifiUntil(long j) throws RemoteException {
        }

        public void setUidFirewallRule(int i, int i2, int i3) throws RemoteException {
        }

        public boolean shouldAvoidBadWifi() throws RemoteException {
            return false;
        }

        public void simulateDataStall(int i, long j, Network network, PersistableBundle persistableBundle) throws RemoteException {
        }

        public void startCaptivePortalApp(Network network) throws RemoteException {
        }

        public void startCaptivePortalAppInternal(Network network, Bundle bundle) throws RemoteException {
        }

        public void startNattKeepalive(Network network, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, int i2, String str2) throws RemoteException {
        }

        public void startNattKeepaliveWithFd(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, int i2, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, String str2) throws RemoteException {
        }

        public IBinder startOrGetTestNetworkService() throws RemoteException {
            return null;
        }

        public void startTcpKeepalive(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback) throws RemoteException {
        }

        public void stopKeepalive(Network network, int i) throws RemoteException {
        }

        public void systemReady() throws RemoteException {
        }

        public void unofferNetwork(INetworkOfferCallback iNetworkOfferCallback) throws RemoteException {
        }

        public void unregisterConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback) throws RemoteException {
        }

        public void unregisterNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException {
        }

        public void unregisterNetworkProvider(Messenger messenger) throws RemoteException {
        }

        public void unregisterQosCallback(IQosCallback iQosCallback) throws RemoteException {
        }

        public void updateMeteredNetworkAllowList(int i, boolean z) throws RemoteException {
        }

        public void updateMeteredNetworkDenyList(int i, boolean z) throws RemoteException {
        }
    }

    void declareNetworkRequestUnfulfillable(NetworkRequest networkRequest) throws RemoteException;

    void factoryReset() throws RemoteException;

    LinkProperties getActiveLinkProperties() throws RemoteException;

    Network getActiveNetwork() throws RemoteException;

    Network getActiveNetworkForUid(int i, boolean z) throws RemoteException;

    NetworkInfo getActiveNetworkInfo() throws RemoteException;

    NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) throws RemoteException;

    NetworkInfo[] getAllNetworkInfo() throws RemoteException;

    NetworkState[] getAllNetworkState() throws RemoteException;

    List<NetworkStateSnapshot> getAllNetworkStateSnapshots() throws RemoteException;

    Network[] getAllNetworks() throws RemoteException;

    String getCaptivePortalServerUrl() throws RemoteException;

    int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException;

    NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i, String str, String str2) throws RemoteException;

    NetworkRequest getDefaultRequest() throws RemoteException;

    ProxyInfo getGlobalProxy() throws RemoteException;

    int getLastTetherError(String str) throws RemoteException;

    LinkProperties getLinkProperties(Network network) throws RemoteException;

    LinkProperties getLinkPropertiesForType(int i) throws RemoteException;

    int getMultipathPreference(Network network) throws RemoteException;

    NetworkCapabilities getNetworkCapabilities(Network network, String str, String str2) throws RemoteException;

    Network getNetworkForType(int i) throws RemoteException;

    NetworkInfo getNetworkInfo(int i) throws RemoteException;

    NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) throws RemoteException;

    byte[] getNetworkWatchlistConfigHash() throws RemoteException;

    ProxyInfo getProxyForNetwork(Network network) throws RemoteException;

    LinkProperties getRedactedLinkPropertiesForPackage(LinkProperties linkProperties, int i, String str, String str2) throws RemoteException;

    NetworkCapabilities getRedactedNetworkCapabilitiesForPackage(NetworkCapabilities networkCapabilities, int i, String str, String str2) throws RemoteException;

    int getRestoreDefaultNetworkDelay(int i) throws RemoteException;

    int getRestrictBackgroundStatusByCaller() throws RemoteException;

    String[] getTetherableIfaces() throws RemoteException;

    String[] getTetherableUsbRegexs() throws RemoteException;

    String[] getTetherableWifiRegexs() throws RemoteException;

    String[] getTetheredIfaces() throws RemoteException;

    String[] getTetheringErroredIfaces() throws RemoteException;

    boolean isActiveNetworkMetered() throws RemoteException;

    boolean isDefaultNetworkActive() throws RemoteException;

    boolean isNetworkSupported(int i) throws RemoteException;

    NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder, int i, String str, String str2) throws RemoteException;

    void offerNetwork(int i, NetworkScore networkScore, NetworkCapabilities networkCapabilities, INetworkOfferCallback iNetworkOfferCallback) throws RemoteException;

    void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException;

    NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException;

    void registerConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback, NetworkRequest networkRequest, String str) throws RemoteException;

    void registerNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    Network registerNetworkAgent(INetworkAgent iNetworkAgent, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, int i) throws RemoteException;

    int registerNetworkProvider(Messenger messenger, String str) throws RemoteException;

    void registerQosSocketCallback(QosSocketInfo qosSocketInfo, IQosCallback iQosCallback) throws RemoteException;

    void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException;

    void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException;

    void replaceFirewallChain(int i, int[] iArr) throws RemoteException;

    void reportInetCondition(int i, int i2) throws RemoteException;

    void reportNetworkConnectivity(Network network, boolean z) throws RemoteException;

    boolean requestBandwidthUpdate(Network network) throws RemoteException;

    NetworkRequest requestNetwork(int i, NetworkCapabilities networkCapabilities, int i2, Messenger messenger, int i3, IBinder iBinder, int i4, int i5, String str, String str2) throws RemoteException;

    boolean requestRouteToHostAddress(int i, byte[] bArr, String str, String str2) throws RemoteException;

    void setAcceptPartialConnectivity(Network network, boolean z, boolean z2) throws RemoteException;

    void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException;

    void setAirplaneMode(boolean z) throws RemoteException;

    void setAvoidUnvalidated(Network network) throws RemoteException;

    void setFirewallChainEnabled(int i, boolean z) throws RemoteException;

    void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException;

    void setLegacyLockdownVpnEnabled(boolean z) throws RemoteException;

    void setOemNetworkPreference(OemNetworkPreferences oemNetworkPreferences, IOnCompleteListener iOnCompleteListener) throws RemoteException;

    void setProfileNetworkPreferences(UserHandle userHandle, List<ProfileNetworkPreference> list, IOnCompleteListener iOnCompleteListener) throws RemoteException;

    void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException;

    void setRequireVpnForUids(boolean z, UidRange[] uidRangeArr) throws RemoteException;

    void setTestAllowBadWifiUntil(long j) throws RemoteException;

    void setUidFirewallRule(int i, int i2, int i3) throws RemoteException;

    boolean shouldAvoidBadWifi() throws RemoteException;

    void simulateDataStall(int i, long j, Network network, PersistableBundle persistableBundle) throws RemoteException;

    void startCaptivePortalApp(Network network) throws RemoteException;

    void startCaptivePortalAppInternal(Network network, Bundle bundle) throws RemoteException;

    void startNattKeepalive(Network network, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, int i2, String str2) throws RemoteException;

    void startNattKeepaliveWithFd(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, int i2, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, String str2) throws RemoteException;

    IBinder startOrGetTestNetworkService() throws RemoteException;

    void startTcpKeepalive(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback) throws RemoteException;

    void stopKeepalive(Network network, int i) throws RemoteException;

    void systemReady() throws RemoteException;

    void unofferNetwork(INetworkOfferCallback iNetworkOfferCallback) throws RemoteException;

    void unregisterConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback) throws RemoteException;

    void unregisterNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    void unregisterNetworkProvider(Messenger messenger) throws RemoteException;

    void unregisterQosCallback(IQosCallback iQosCallback) throws RemoteException;

    void updateMeteredNetworkAllowList(int i, boolean z) throws RemoteException;

    void updateMeteredNetworkDenyList(int i, boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements IConnectivityManager {
        public static final String DESCRIPTOR = "android.net.IConnectivityManager";
        static final int TRANSACTION_declareNetworkRequestUnfulfillable = 40;
        static final int TRANSACTION_factoryReset = 57;
        static final int TRANSACTION_getActiveLinkProperties = 12;
        static final int TRANSACTION_getActiveNetwork = 1;
        static final int TRANSACTION_getActiveNetworkForUid = 2;
        static final int TRANSACTION_getActiveNetworkInfo = 3;
        static final int TRANSACTION_getActiveNetworkInfoForUid = 4;
        static final int TRANSACTION_getAllNetworkInfo = 7;
        static final int TRANSACTION_getAllNetworkState = 18;
        static final int TRANSACTION_getAllNetworkStateSnapshots = 19;
        static final int TRANSACTION_getAllNetworks = 9;
        static final int TRANSACTION_getCaptivePortalServerUrl = 62;
        static final int TRANSACTION_getConnectionOwnerUid = 64;
        static final int TRANSACTION_getDefaultNetworkCapabilitiesForUser = 10;
        static final int TRANSACTION_getDefaultRequest = 55;
        static final int TRANSACTION_getGlobalProxy = 30;
        static final int TRANSACTION_getLastTetherError = 22;
        static final int TRANSACTION_getLinkProperties = 14;
        static final int TRANSACTION_getLinkPropertiesForType = 13;
        static final int TRANSACTION_getMultipathPreference = 54;
        static final int TRANSACTION_getNetworkCapabilities = 16;
        static final int TRANSACTION_getNetworkForType = 8;
        static final int TRANSACTION_getNetworkInfo = 5;
        static final int TRANSACTION_getNetworkInfoForUid = 6;
        static final int TRANSACTION_getNetworkWatchlistConfigHash = 63;
        static final int TRANSACTION_getProxyForNetwork = 32;
        static final int TRANSACTION_getRedactedLinkPropertiesForPackage = 15;
        static final int TRANSACTION_getRedactedNetworkCapabilitiesForPackage = 17;
        static final int TRANSACTION_getRestoreDefaultNetworkDelay = 56;
        static final int TRANSACTION_getRestrictBackgroundStatusByCaller = 77;
        static final int TRANSACTION_getTetherableIfaces = 23;
        static final int TRANSACTION_getTetherableUsbRegexs = 26;
        static final int TRANSACTION_getTetherableWifiRegexs = 27;
        static final int TRANSACTION_getTetheredIfaces = 24;
        static final int TRANSACTION_getTetheringErroredIfaces = 25;
        static final int TRANSACTION_isActiveNetworkMetered = 20;
        static final int TRANSACTION_isDefaultNetworkActive = 72;
        static final int TRANSACTION_isNetworkSupported = 11;
        static final int TRANSACTION_listenForNetwork = 45;
        static final int TRANSACTION_offerNetwork = 78;
        static final int TRANSACTION_pendingListenForNetwork = 46;
        static final int TRANSACTION_pendingRequestForNetwork = 43;
        static final int TRANSACTION_registerConnectivityDiagnosticsCallback = 65;
        static final int TRANSACTION_registerNetworkActivityListener = 70;
        static final int TRANSACTION_registerNetworkAgent = 41;
        static final int TRANSACTION_registerNetworkProvider = 38;
        static final int TRANSACTION_registerQosSocketCallback = 73;
        static final int TRANSACTION_releaseNetworkRequest = 47;
        static final int TRANSACTION_releasePendingNetworkRequest = 44;
        static final int TRANSACTION_replaceFirewallChain = 85;
        static final int TRANSACTION_reportInetCondition = 28;
        static final int TRANSACTION_reportNetworkConnectivity = 29;
        static final int TRANSACTION_requestBandwidthUpdate = 37;
        static final int TRANSACTION_requestNetwork = 42;
        static final int TRANSACTION_requestRouteToHostAddress = 21;
        static final int TRANSACTION_setAcceptPartialConnectivity = 49;
        static final int TRANSACTION_setAcceptUnvalidated = 48;
        static final int TRANSACTION_setAirplaneMode = 36;
        static final int TRANSACTION_setAvoidUnvalidated = 50;
        static final int TRANSACTION_setFirewallChainEnabled = 84;
        static final int TRANSACTION_setGlobalProxy = 31;
        static final int TRANSACTION_setLegacyLockdownVpnEnabled = 34;
        static final int TRANSACTION_setOemNetworkPreference = 75;
        static final int TRANSACTION_setProfileNetworkPreferences = 76;
        static final int TRANSACTION_setProvisioningNotificationVisible = 35;
        static final int TRANSACTION_setRequireVpnForUids = 33;
        static final int TRANSACTION_setTestAllowBadWifiUntil = 80;
        static final int TRANSACTION_setUidFirewallRule = 83;
        static final int TRANSACTION_shouldAvoidBadWifi = 53;
        static final int TRANSACTION_simulateDataStall = 68;
        static final int TRANSACTION_startCaptivePortalApp = 51;
        static final int TRANSACTION_startCaptivePortalAppInternal = 52;
        static final int TRANSACTION_startNattKeepalive = 58;
        static final int TRANSACTION_startNattKeepaliveWithFd = 59;
        static final int TRANSACTION_startOrGetTestNetworkService = 67;
        static final int TRANSACTION_startTcpKeepalive = 60;
        static final int TRANSACTION_stopKeepalive = 61;
        static final int TRANSACTION_systemReady = 69;
        static final int TRANSACTION_unofferNetwork = 79;
        static final int TRANSACTION_unregisterConnectivityDiagnosticsCallback = 66;
        static final int TRANSACTION_unregisterNetworkActivityListener = 71;
        static final int TRANSACTION_unregisterNetworkProvider = 39;
        static final int TRANSACTION_unregisterQosCallback = 74;
        static final int TRANSACTION_updateMeteredNetworkAllowList = 81;
        static final int TRANSACTION_updateMeteredNetworkDenyList = 82;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "getActiveNetwork";
                case 2:
                    return "getActiveNetworkForUid";
                case 3:
                    return "getActiveNetworkInfo";
                case 4:
                    return "getActiveNetworkInfoForUid";
                case 5:
                    return "getNetworkInfo";
                case 6:
                    return "getNetworkInfoForUid";
                case 7:
                    return "getAllNetworkInfo";
                case 8:
                    return "getNetworkForType";
                case 9:
                    return "getAllNetworks";
                case 10:
                    return "getDefaultNetworkCapabilitiesForUser";
                case 11:
                    return "isNetworkSupported";
                case 12:
                    return "getActiveLinkProperties";
                case 13:
                    return "getLinkPropertiesForType";
                case 14:
                    return "getLinkProperties";
                case 15:
                    return "getRedactedLinkPropertiesForPackage";
                case 16:
                    return "getNetworkCapabilities";
                case 17:
                    return "getRedactedNetworkCapabilitiesForPackage";
                case 18:
                    return "getAllNetworkState";
                case 19:
                    return "getAllNetworkStateSnapshots";
                case 20:
                    return "isActiveNetworkMetered";
                case 21:
                    return "requestRouteToHostAddress";
                case 22:
                    return "getLastTetherError";
                case 23:
                    return "getTetherableIfaces";
                case 24:
                    return "getTetheredIfaces";
                case 25:
                    return "getTetheringErroredIfaces";
                case 26:
                    return "getTetherableUsbRegexs";
                case 27:
                    return "getTetherableWifiRegexs";
                case 28:
                    return "reportInetCondition";
                case 29:
                    return "reportNetworkConnectivity";
                case 30:
                    return "getGlobalProxy";
                case 31:
                    return "setGlobalProxy";
                case 32:
                    return "getProxyForNetwork";
                case 33:
                    return "setRequireVpnForUids";
                case 34:
                    return "setLegacyLockdownVpnEnabled";
                case 35:
                    return "setProvisioningNotificationVisible";
                case 36:
                    return "setAirplaneMode";
                case 37:
                    return "requestBandwidthUpdate";
                case 38:
                    return "registerNetworkProvider";
                case 39:
                    return "unregisterNetworkProvider";
                case 40:
                    return "declareNetworkRequestUnfulfillable";
                case 41:
                    return "registerNetworkAgent";
                case 42:
                    return "requestNetwork";
                case 43:
                    return "pendingRequestForNetwork";
                case 44:
                    return "releasePendingNetworkRequest";
                case 45:
                    return "listenForNetwork";
                case 46:
                    return "pendingListenForNetwork";
                case 47:
                    return "releaseNetworkRequest";
                case 48:
                    return "setAcceptUnvalidated";
                case 49:
                    return "setAcceptPartialConnectivity";
                case 50:
                    return "setAvoidUnvalidated";
                case 51:
                    return "startCaptivePortalApp";
                case 52:
                    return "startCaptivePortalAppInternal";
                case 53:
                    return "shouldAvoidBadWifi";
                case 54:
                    return "getMultipathPreference";
                case 55:
                    return "getDefaultRequest";
                case 56:
                    return "getRestoreDefaultNetworkDelay";
                case 57:
                    return "factoryReset";
                case 58:
                    return "startNattKeepalive";
                case 59:
                    return "startNattKeepaliveWithFd";
                case 60:
                    return "startTcpKeepalive";
                case 61:
                    return "stopKeepalive";
                case 62:
                    return "getCaptivePortalServerUrl";
                case 63:
                    return "getNetworkWatchlistConfigHash";
                case 64:
                    return "getConnectionOwnerUid";
                case 65:
                    return "registerConnectivityDiagnosticsCallback";
                case 66:
                    return "unregisterConnectivityDiagnosticsCallback";
                case 67:
                    return "startOrGetTestNetworkService";
                case 68:
                    return "simulateDataStall";
                case 69:
                    return "systemReady";
                case 70:
                    return "registerNetworkActivityListener";
                case 71:
                    return "unregisterNetworkActivityListener";
                case 72:
                    return "isDefaultNetworkActive";
                case 73:
                    return "registerQosSocketCallback";
                case 74:
                    return "unregisterQosCallback";
                case 75:
                    return "setOemNetworkPreference";
                case 76:
                    return "setProfileNetworkPreferences";
                case 77:
                    return "getRestrictBackgroundStatusByCaller";
                case 78:
                    return "offerNetwork";
                case 79:
                    return "unofferNetwork";
                case 80:
                    return "setTestAllowBadWifiUntil";
                case 81:
                    return "updateMeteredNetworkAllowList";
                case 82:
                    return "updateMeteredNetworkDenyList";
                case 83:
                    return "setUidFirewallRule";
                case 84:
                    return "setFirewallChainEnabled";
                case 85:
                    return "replaceFirewallChain";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 84;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IConnectivityManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IConnectivityManager)) {
                return new Proxy(iBinder);
            }
            return (IConnectivityManager) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = i;
            Parcel parcel3 = parcel;
            Parcel parcel4 = parcel2;
            if (i3 >= 1 && i3 <= 16777215) {
                parcel3.enforceInterface(DESCRIPTOR);
            }
            if (i3 != 1598968902) {
                switch (i3) {
                    case 1:
                        Network activeNetwork = getActiveNetwork();
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(activeNetwork, 1);
                        break;
                    case 2:
                        Network activeNetworkForUid = getActiveNetworkForUid(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(activeNetworkForUid, 1);
                        break;
                    case 3:
                        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(activeNetworkInfo, 1);
                        break;
                    case 4:
                        NetworkInfo activeNetworkInfoForUid = getActiveNetworkInfoForUid(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(activeNetworkInfoForUid, 1);
                        break;
                    case 5:
                        NetworkInfo networkInfo = getNetworkInfo(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(networkInfo, 1);
                        break;
                    case 6:
                        NetworkInfo networkInfoForUid = getNetworkInfoForUid((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(networkInfoForUid, 1);
                        break;
                    case 7:
                        NetworkInfo[] allNetworkInfo = getAllNetworkInfo();
                        parcel2.writeNoException();
                        parcel4.writeTypedArray(allNetworkInfo, 1);
                        break;
                    case 8:
                        Network networkForType = getNetworkForType(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(networkForType, 1);
                        break;
                    case 9:
                        Network[] allNetworks = getAllNetworks();
                        parcel2.writeNoException();
                        parcel4.writeTypedArray(allNetworks, 1);
                        break;
                    case 10:
                        NetworkCapabilities[] defaultNetworkCapabilitiesForUser = getDefaultNetworkCapabilitiesForUser(parcel.readInt(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedArray(defaultNetworkCapabilitiesForUser, 1);
                        break;
                    case 11:
                        boolean isNetworkSupported = isNetworkSupported(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeBoolean(isNetworkSupported);
                        break;
                    case 12:
                        LinkProperties activeLinkProperties = getActiveLinkProperties();
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(activeLinkProperties, 1);
                        break;
                    case 13:
                        LinkProperties linkPropertiesForType = getLinkPropertiesForType(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(linkPropertiesForType, 1);
                        break;
                    case 14:
                        LinkProperties linkProperties = getLinkProperties((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(linkProperties, 1);
                        break;
                    case 15:
                        LinkProperties redactedLinkPropertiesForPackage = getRedactedLinkPropertiesForPackage((LinkProperties) parcel3.readTypedObject(LinkProperties.CREATOR), parcel.readInt(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(redactedLinkPropertiesForPackage, 1);
                        break;
                    case 16:
                        NetworkCapabilities networkCapabilities = getNetworkCapabilities((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(networkCapabilities, 1);
                        break;
                    case 17:
                        NetworkCapabilities redactedNetworkCapabilitiesForPackage = getRedactedNetworkCapabilitiesForPackage((NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), parcel.readInt(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(redactedNetworkCapabilitiesForPackage, 1);
                        break;
                    case 18:
                        NetworkState[] allNetworkState = getAllNetworkState();
                        parcel2.writeNoException();
                        parcel4.writeTypedArray(allNetworkState, 1);
                        break;
                    case 19:
                        List<NetworkStateSnapshot> allNetworkStateSnapshots = getAllNetworkStateSnapshots();
                        parcel2.writeNoException();
                        parcel4.writeTypedList(allNetworkStateSnapshots);
                        break;
                    case 20:
                        boolean isActiveNetworkMetered = isActiveNetworkMetered();
                        parcel2.writeNoException();
                        parcel4.writeBoolean(isActiveNetworkMetered);
                        break;
                    case 21:
                        boolean requestRouteToHostAddress = requestRouteToHostAddress(parcel.readInt(), parcel.createByteArray(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeBoolean(requestRouteToHostAddress);
                        break;
                    case 22:
                        int lastTetherError = getLastTetherError(parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(lastTetherError);
                        break;
                    case 23:
                        String[] tetherableIfaces = getTetherableIfaces();
                        parcel2.writeNoException();
                        parcel4.writeStringArray(tetherableIfaces);
                        break;
                    case 24:
                        String[] tetheredIfaces = getTetheredIfaces();
                        parcel2.writeNoException();
                        parcel4.writeStringArray(tetheredIfaces);
                        break;
                    case 25:
                        String[] tetheringErroredIfaces = getTetheringErroredIfaces();
                        parcel2.writeNoException();
                        parcel4.writeStringArray(tetheringErroredIfaces);
                        break;
                    case 26:
                        String[] tetherableUsbRegexs = getTetherableUsbRegexs();
                        parcel2.writeNoException();
                        parcel4.writeStringArray(tetherableUsbRegexs);
                        break;
                    case 27:
                        String[] tetherableWifiRegexs = getTetherableWifiRegexs();
                        parcel2.writeNoException();
                        parcel4.writeStringArray(tetherableWifiRegexs);
                        break;
                    case 28:
                        reportInetCondition(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 29:
                        reportNetworkConnectivity((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 30:
                        ProxyInfo globalProxy = getGlobalProxy();
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(globalProxy, 1);
                        break;
                    case 31:
                        setGlobalProxy((ProxyInfo) parcel3.readTypedObject(ProxyInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 32:
                        ProxyInfo proxyForNetwork = getProxyForNetwork((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(proxyForNetwork, 1);
                        break;
                    case 33:
                        setRequireVpnForUids(parcel.readBoolean(), (UidRange[]) parcel3.createTypedArray(UidRange.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 34:
                        setLegacyLockdownVpnEnabled(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 35:
                        setProvisioningNotificationVisible(parcel.readBoolean(), parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 36:
                        setAirplaneMode(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 37:
                        boolean requestBandwidthUpdate = requestBandwidthUpdate((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        parcel4.writeBoolean(requestBandwidthUpdate);
                        break;
                    case 38:
                        int registerNetworkProvider = registerNetworkProvider((Messenger) parcel3.readTypedObject(Messenger.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeInt(registerNetworkProvider);
                        break;
                    case 39:
                        unregisterNetworkProvider((Messenger) parcel3.readTypedObject(Messenger.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 40:
                        declareNetworkRequestUnfulfillable((NetworkRequest) parcel3.readTypedObject(NetworkRequest.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 41:
                        Network registerNetworkAgent = registerNetworkAgent(INetworkAgent.Stub.asInterface(parcel.readStrongBinder()), (NetworkInfo) parcel3.readTypedObject(NetworkInfo.CREATOR), (LinkProperties) parcel3.readTypedObject(LinkProperties.CREATOR), (NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), (NetworkScore) parcel3.readTypedObject(NetworkScore.CREATOR), (NetworkAgentConfig) parcel3.readTypedObject(NetworkAgentConfig.CREATOR), parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(registerNetworkAgent, 1);
                        break;
                    case 42:
                        NetworkRequest requestNetwork = requestNetwork(parcel.readInt(), (NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), parcel.readInt(), (Messenger) parcel3.readTypedObject(Messenger.CREATOR), parcel.readInt(), parcel.readStrongBinder(), parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(requestNetwork, 1);
                        break;
                    case 43:
                        NetworkRequest pendingRequestForNetwork = pendingRequestForNetwork((NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), (PendingIntent) parcel3.readTypedObject(PendingIntent.CREATOR), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(pendingRequestForNetwork, 1);
                        break;
                    case 44:
                        releasePendingNetworkRequest((PendingIntent) parcel3.readTypedObject(PendingIntent.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 45:
                        NetworkRequest listenForNetwork = listenForNetwork((NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), (Messenger) parcel3.readTypedObject(Messenger.CREATOR), parcel.readStrongBinder(), parcel.readInt(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(listenForNetwork, 1);
                        break;
                    case 46:
                        pendingListenForNetwork((NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), (PendingIntent) parcel3.readTypedObject(PendingIntent.CREATOR), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 47:
                        releaseNetworkRequest((NetworkRequest) parcel3.readTypedObject(NetworkRequest.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 48:
                        setAcceptUnvalidated((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readBoolean(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 49:
                        setAcceptPartialConnectivity((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readBoolean(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 50:
                        setAvoidUnvalidated((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 51:
                        startCaptivePortalApp((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 52:
                        startCaptivePortalAppInternal((Network) parcel3.readTypedObject(Network.CREATOR), (Bundle) parcel3.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 53:
                        boolean shouldAvoidBadWifi = shouldAvoidBadWifi();
                        parcel2.writeNoException();
                        parcel4.writeBoolean(shouldAvoidBadWifi);
                        break;
                    case 54:
                        int multipathPreference = getMultipathPreference((Network) parcel3.readTypedObject(Network.CREATOR));
                        parcel2.writeNoException();
                        parcel4.writeInt(multipathPreference);
                        break;
                    case 55:
                        NetworkRequest defaultRequest = getDefaultRequest();
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(defaultRequest, 1);
                        break;
                    case 56:
                        int restoreDefaultNetworkDelay = getRestoreDefaultNetworkDelay(parcel.readInt());
                        parcel2.writeNoException();
                        parcel4.writeInt(restoreDefaultNetworkDelay);
                        break;
                    case 57:
                        factoryReset();
                        parcel2.writeNoException();
                        break;
                    case 58:
                        startNattKeepalive((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readInt(), ISocketKeepaliveCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 59:
                        startNattKeepaliveWithFd((Network) parcel3.readTypedObject(Network.CREATOR), (ParcelFileDescriptor) parcel3.readTypedObject(ParcelFileDescriptor.CREATOR), parcel.readInt(), parcel.readInt(), ISocketKeepaliveCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 60:
                        startTcpKeepalive((Network) parcel3.readTypedObject(Network.CREATOR), (ParcelFileDescriptor) parcel3.readTypedObject(ParcelFileDescriptor.CREATOR), parcel.readInt(), ISocketKeepaliveCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 61:
                        stopKeepalive((Network) parcel3.readTypedObject(Network.CREATOR), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 62:
                        String captivePortalServerUrl = getCaptivePortalServerUrl();
                        parcel2.writeNoException();
                        parcel4.writeString(captivePortalServerUrl);
                        break;
                    case 63:
                        byte[] networkWatchlistConfigHash = getNetworkWatchlistConfigHash();
                        parcel2.writeNoException();
                        parcel4.writeByteArray(networkWatchlistConfigHash);
                        break;
                    case 64:
                        int connectionOwnerUid = getConnectionOwnerUid((ConnectionInfo) parcel3.readTypedObject(ConnectionInfo.CREATOR));
                        parcel2.writeNoException();
                        parcel4.writeInt(connectionOwnerUid);
                        break;
                    case 65:
                        registerConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback.Stub.asInterface(parcel.readStrongBinder()), (NetworkRequest) parcel3.readTypedObject(NetworkRequest.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 66:
                        unregisterConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 67:
                        IBinder startOrGetTestNetworkService = startOrGetTestNetworkService();
                        parcel2.writeNoException();
                        parcel4.writeStrongBinder(startOrGetTestNetworkService);
                        break;
                    case 68:
                        simulateDataStall(parcel.readInt(), parcel.readLong(), (Network) parcel3.readTypedObject(Network.CREATOR), (PersistableBundle) parcel3.readTypedObject(PersistableBundle.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 69:
                        systemReady();
                        parcel2.writeNoException();
                        break;
                    case 70:
                        registerNetworkActivityListener(INetworkActivityListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 71:
                        unregisterNetworkActivityListener(INetworkActivityListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 72:
                        boolean isDefaultNetworkActive = isDefaultNetworkActive();
                        parcel2.writeNoException();
                        parcel4.writeBoolean(isDefaultNetworkActive);
                        break;
                    case 73:
                        registerQosSocketCallback((QosSocketInfo) parcel3.readTypedObject(QosSocketInfo.CREATOR), IQosCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 74:
                        unregisterQosCallback(IQosCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 75:
                        setOemNetworkPreference((OemNetworkPreferences) parcel3.readTypedObject(OemNetworkPreferences.CREATOR), IOnCompleteListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 76:
                        setProfileNetworkPreferences((UserHandle) parcel3.readTypedObject(UserHandle.CREATOR), parcel3.createTypedArrayList(ProfileNetworkPreference.CREATOR), IOnCompleteListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 77:
                        int restrictBackgroundStatusByCaller = getRestrictBackgroundStatusByCaller();
                        parcel2.writeNoException();
                        parcel4.writeInt(restrictBackgroundStatusByCaller);
                        break;
                    case 78:
                        offerNetwork(parcel.readInt(), (NetworkScore) parcel3.readTypedObject(NetworkScore.CREATOR), (NetworkCapabilities) parcel3.readTypedObject(NetworkCapabilities.CREATOR), INetworkOfferCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 79:
                        unofferNetwork(INetworkOfferCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        break;
                    case 80:
                        setTestAllowBadWifiUntil(parcel.readLong());
                        parcel2.writeNoException();
                        break;
                    case 81:
                        updateMeteredNetworkAllowList(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 82:
                        updateMeteredNetworkDenyList(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 83:
                        setUidFirewallRule(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 84:
                        setFirewallChainEnabled(parcel.readInt(), parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 85:
                        replaceFirewallChain(parcel.readInt(), parcel.createIntArray());
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel4.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IConnectivityManager {
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

            public Network getActiveNetwork() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network) obtain2.readTypedObject(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Network getActiveNetworkForUid(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network) obtain2.readTypedObject(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkInfo getActiveNetworkInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkInfo) obtain2.readTypedObject(NetworkInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkInfo) obtain2.readTypedObject(NetworkInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkInfo getNetworkInfo(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkInfo) obtain2.readTypedObject(NetworkInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkInfo) obtain2.readTypedObject(NetworkInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkInfo[]) obtain2.createTypedArray(NetworkInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Network getNetworkForType(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network) obtain2.readTypedObject(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Network[] getAllNetworks() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network[]) obtain2.createTypedArray(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkCapabilities[]) obtain2.createTypedArray(NetworkCapabilities.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isNetworkSupported(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LinkProperties getActiveLinkProperties() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return (LinkProperties) obtain2.readTypedObject(LinkProperties.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LinkProperties getLinkPropertiesForType(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return (LinkProperties) obtain2.readTypedObject(LinkProperties.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LinkProperties getLinkProperties(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return (LinkProperties) obtain2.readTypedObject(LinkProperties.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LinkProperties getRedactedLinkPropertiesForPackage(LinkProperties linkProperties, int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(linkProperties, 0);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return (LinkProperties) obtain2.readTypedObject(LinkProperties.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkCapabilities getNetworkCapabilities(Network network, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkCapabilities) obtain2.readTypedObject(NetworkCapabilities.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkCapabilities getRedactedNetworkCapabilitiesForPackage(NetworkCapabilities networkCapabilities, int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkCapabilities) obtain2.readTypedObject(NetworkCapabilities.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkState[] getAllNetworkState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkState[]) obtain2.createTypedArray(NetworkState.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<NetworkStateSnapshot> getAllNetworkStateSnapshots() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(NetworkStateSnapshot.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isActiveNetworkMetered() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean requestRouteToHostAddress(int i, byte[] bArr, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getLastTetherError(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getTetherableIfaces() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getTetheredIfaces() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getTetheringErroredIfaces() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getTetherableUsbRegexs() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getTetherableWifiRegexs() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportInetCondition(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportNetworkConnectivity(Network network, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ProxyInfo getGlobalProxy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    return (ProxyInfo) obtain2.readTypedObject(ProxyInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(proxyInfo, 0);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ProxyInfo getProxyForNetwork(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return (ProxyInfo) obtain2.readTypedObject(ProxyInfo.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setRequireVpnForUids(boolean z, UidRange[] uidRangeArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeTypedArray(uidRangeArr, 0);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setLegacyLockdownVpnEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAirplaneMode(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean requestBandwidthUpdate(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int registerNetworkProvider(Messenger messenger, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(messenger, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterNetworkProvider(Messenger messenger) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(messenger, 0);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void declareNetworkRequestUnfulfillable(NetworkRequest networkRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkRequest, 0);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Network registerNetworkAgent(INetworkAgent iNetworkAgent, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkAgent);
                    obtain.writeTypedObject(networkInfo, 0);
                    obtain.writeTypedObject(linkProperties, 0);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeTypedObject(networkScore, 0);
                    obtain.writeTypedObject(networkAgentConfig, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Network) obtain2.readTypedObject(Network.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkRequest requestNetwork(int i, NetworkCapabilities networkCapabilities, int i2, Messenger messenger, int i3, IBinder iBinder, int i4, int i5, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(messenger, 0);
                    obtain.writeInt(i3);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkRequest) obtain2.readTypedObject(NetworkRequest.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeTypedObject(pendingIntent, 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkRequest) obtain2.readTypedObject(NetworkRequest.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(pendingIntent, 0);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder, int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeTypedObject(messenger, 0);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkRequest) obtain2.readTypedObject(NetworkRequest.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeTypedObject(pendingIntent, 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkRequest, 0);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAcceptPartialConnectivity(Network network, boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAvoidUnvalidated(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startCaptivePortalApp(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startCaptivePortalAppInternal(Network network, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean shouldAvoidBadWifi() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getMultipathPreference(Network network) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkRequest getDefaultRequest() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkRequest) obtain2.readTypedObject(NetworkRequest.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getRestoreDefaultNetworkDelay(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void factoryReset() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startNattKeepalive(Network network, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, int i2, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iSocketKeepaliveCallback);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeString(str2);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startNattKeepaliveWithFd(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, int i2, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStrongInterface(iSocketKeepaliveCallback);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startTcpKeepalive(Network network, ParcelFileDescriptor parcelFileDescriptor, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iSocketKeepaliveCallback);
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopKeepalive(Network network, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCaptivePortalServerUrl() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(62, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getNetworkWatchlistConfigHash() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(connectionInfo, 0);
                    this.mRemote.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback, NetworkRequest networkRequest, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iConnectivityDiagnosticsCallback);
                    obtain.writeTypedObject(networkRequest, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterConnectivityDiagnosticsCallback(IConnectivityDiagnosticsCallback iConnectivityDiagnosticsCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iConnectivityDiagnosticsCallback);
                    this.mRemote.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder startOrGetTestNetworkService() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readStrongBinder();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void simulateDataStall(int i, long j, Network network, PersistableBundle persistableBundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeTypedObject(persistableBundle, 0);
                    this.mRemote.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void systemReady() throws RemoteException {
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

            public void registerNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkActivityListener);
                    this.mRemote.transact(70, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkActivityListener);
                    this.mRemote.transact(71, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDefaultNetworkActive() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(72, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerQosSocketCallback(QosSocketInfo qosSocketInfo, IQosCallback iQosCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(qosSocketInfo, 0);
                    obtain.writeStrongInterface(iQosCallback);
                    this.mRemote.transact(73, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterQosCallback(IQosCallback iQosCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iQosCallback);
                    this.mRemote.transact(74, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOemNetworkPreference(OemNetworkPreferences oemNetworkPreferences, IOnCompleteListener iOnCompleteListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(oemNetworkPreferences, 0);
                    obtain.writeStrongInterface(iOnCompleteListener);
                    this.mRemote.transact(75, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setProfileNetworkPreferences(UserHandle userHandle, List<ProfileNetworkPreference> list, IOnCompleteListener iOnCompleteListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(userHandle, 0);
                    obtain.writeTypedList(list);
                    obtain.writeStrongInterface(iOnCompleteListener);
                    this.mRemote.transact(76, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getRestrictBackgroundStatusByCaller() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(77, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void offerNetwork(int i, NetworkScore networkScore, NetworkCapabilities networkCapabilities, INetworkOfferCallback iNetworkOfferCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(networkScore, 0);
                    obtain.writeTypedObject(networkCapabilities, 0);
                    obtain.writeStrongInterface(iNetworkOfferCallback);
                    this.mRemote.transact(78, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unofferNetwork(INetworkOfferCallback iNetworkOfferCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkOfferCallback);
                    this.mRemote.transact(79, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTestAllowBadWifiUntil(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateMeteredNetworkAllowList(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateMeteredNetworkDenyList(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(82, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setUidFirewallRule(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFirewallChainEnabled(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void replaceFirewallChain(int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
