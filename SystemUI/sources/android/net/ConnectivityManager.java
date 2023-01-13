package android.net;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.Context;
import android.net.INetworkActivityListener;
import android.net.IOnCompleteListener;
import android.net.ISocketKeepaliveCallback;
import android.net.ITestNetworkManager;
import android.net.IpSecManager;
import android.net.NetworkRequest;
import android.net.ProfileNetworkPreference;
import android.net.QosCallback;
import android.net.SocketKeepalive;
import android.net.TetheringManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Range;
import android.util.SparseIntArray;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.p026io.IOException;
import java.p026io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import libcore.net.event.NetworkEventDispatcher;

public class ConnectivityManager {
    @Deprecated
    public static final String ACTION_BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
    public static final String ACTION_CAPTIVE_PORTAL_SIGN_IN = "android.net.conn.CAPTIVE_PORTAL";
    public static final String ACTION_CAPTIVE_PORTAL_TEST_COMPLETED = "android.net.conn.CAPTIVE_PORTAL_TEST_COMPLETED";
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final String ACTION_CLEAR_DNS_CACHE = "android.net.action.CLEAR_DNS_CACHE";
    public static final String ACTION_DATA_ACTIVITY_CHANGE = "android.net.conn.DATA_ACTIVITY_CHANGE";
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final String ACTION_PROMPT_LOST_VALIDATION = "android.net.action.PROMPT_LOST_VALIDATION";
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final String ACTION_PROMPT_PARTIAL_CONNECTIVITY = "android.net.action.PROMPT_PARTIAL_CONNECTIVITY";
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final String ACTION_PROMPT_UNVALIDATED = "android.net.action.PROMPT_UNVALIDATED";
    public static final String ACTION_RESTRICT_BACKGROUND_CHANGED = "android.net.conn.RESTRICT_BACKGROUND_CHANGED";
    public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
    /* access modifiers changed from: private */
    public static final NetworkRequest ALREADY_UNREGISTERED = new NetworkRequest.Builder().clearCapabilities().build();
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_METERED_REASON_ADMIN_DISABLED = 262144;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_METERED_REASON_DATA_SAVER = 65536;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_METERED_REASON_MASK = -65536;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_METERED_REASON_USER_RESTRICTED = 131072;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_APP_STANDBY = 4;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_BATTERY_SAVER = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_DOZE = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_LOCKDOWN_VPN = 16;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_LOW_POWER_STANDBY = 32;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_NONE = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int BLOCKED_REASON_RESTRICTED_MODE = 8;
    public static final int CALLBACK_AVAILABLE = 2;
    public static final int CALLBACK_BLK_CHANGED = 11;
    public static final int CALLBACK_CAP_CHANGED = 6;
    public static final int CALLBACK_IP_CHANGED = 7;
    public static final int CALLBACK_LOSING = 3;
    public static final int CALLBACK_LOST = 4;
    public static final int CALLBACK_PRECHECK = 1;
    public static final int CALLBACK_RESUMED = 10;
    public static final int CALLBACK_SUSPENDED = 9;
    public static final int CALLBACK_UNAVAIL = 5;
    @Deprecated
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    @Deprecated
    public static final int DEFAULT_NETWORK_PREFERENCE = 1;
    private static final int DEPRECATED_PHONE_CONSTANT_APN_ALREADY_ACTIVE = 0;
    private static final int DEPRECATED_PHONE_CONSTANT_APN_REQUEST_FAILED = 3;
    private static final int DEPRECATED_PHONE_CONSTANT_APN_REQUEST_STARTED = 1;
    private static final int EXPIRE_LEGACY_REQUEST = 8;
    public static final String EXTRA_ACTIVE_LOCAL_ONLY = "android.net.extra.ACTIVE_LOCAL_ONLY";
    public static final String EXTRA_ACTIVE_TETHER = "tetherArray";
    public static final String EXTRA_ADD_TETHER_TYPE = "extraAddTetherType";
    public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
    public static final String EXTRA_CAPTIVE_PORTAL = "android.net.extra.CAPTIVE_PORTAL";
    @SystemApi
    public static final String EXTRA_CAPTIVE_PORTAL_PROBE_SPEC = "android.net.extra.CAPTIVE_PORTAL_PROBE_SPEC";
    public static final String EXTRA_CAPTIVE_PORTAL_URL = "android.net.extra.CAPTIVE_PORTAL_URL";
    @SystemApi
    public static final String EXTRA_CAPTIVE_PORTAL_USER_AGENT = "android.net.extra.CAPTIVE_PORTAL_USER_AGENT";
    public static final String EXTRA_DEVICE_TYPE = "deviceType";
    public static final String EXTRA_ERRORED_TETHER = "erroredArray";
    @Deprecated
    public static final String EXTRA_EXTRA_INFO = "extraInfo";
    public static final String EXTRA_INET_CONDITION = "inetCondition";
    public static final String EXTRA_IS_ACTIVE = "isActive";
    public static final String EXTRA_IS_CAPTIVE_PORTAL = "captivePortal";
    @Deprecated
    public static final String EXTRA_IS_FAILOVER = "isFailover";
    public static final String EXTRA_NETWORK = "android.net.extra.NETWORK";
    @Deprecated
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_REQUEST = "android.net.extra.NETWORK_REQUEST";
    @Deprecated
    public static final String EXTRA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
    @Deprecated
    public static final String EXTRA_OTHER_NETWORK_INFO = "otherNetwork";
    public static final String EXTRA_PROVISION_CALLBACK = "extraProvisionCallback";
    public static final String EXTRA_REALTIME_NS = "tsNanos";
    public static final String EXTRA_REASON = "reason";
    public static final String EXTRA_REM_TETHER_TYPE = "extraRemTetherType";
    public static final String EXTRA_RUN_PROVISION = "extraRunProvision";
    public static final String EXTRA_SET_ALARM = "extraSetAlarm";
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_CHAIN_DOZABLE = 1;
    public static final int FIREWALL_CHAIN_LOCKDOWN_VPN = 6;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_CHAIN_LOW_POWER_STANDBY = 5;
    public static final int FIREWALL_CHAIN_OEM_DENY_1 = 7;
    public static final int FIREWALL_CHAIN_OEM_DENY_2 = 8;
    public static final int FIREWALL_CHAIN_OEM_DENY_3 = 9;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_CHAIN_POWERSAVE = 3;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_CHAIN_RESTRICTED = 4;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_CHAIN_STANDBY = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_RULE_ALLOW = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_RULE_DEFAULT = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int FIREWALL_RULE_DENY = 2;
    public static final String INET_CONDITION_ACTION = "android.net.conn.INET_CONDITION_ACTION";
    public static final int MAX_NETWORK_TYPE = 18;
    public static final int MAX_RADIO_TYPE = 18;
    private static final int MIN_NETWORK_TYPE = 0;
    public static final int MULTIPATH_PREFERENCE_HANDOVER = 1;
    public static final int MULTIPATH_PREFERENCE_PERFORMANCE = 4;
    public static final int MULTIPATH_PREFERENCE_RELIABILITY = 2;
    public static final int MULTIPATH_PREFERENCE_UNMETERED = 7;
    public static final int NETID_UNSET = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int PROFILE_NETWORK_PREFERENCE_DEFAULT = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int PROFILE_NETWORK_PREFERENCE_ENTERPRISE = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int PROFILE_NETWORK_PREFERENCE_ENTERPRISE_NO_FALLBACK = 2;
    public static final int REQUEST_ID_UNSET = 0;
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    private static final String TAG = "ConnectivityManager";
    @SystemApi
    public static final int TETHERING_BLUETOOTH = 2;
    public static final int TETHERING_INVALID = -1;
    @SystemApi
    public static final int TETHERING_USB = 1;
    @SystemApi
    public static final int TETHERING_WIFI = 0;
    public static final int TETHERING_WIFI_P2P = 3;
    @Deprecated
    public static final int TETHER_ERROR_DHCPSERVER_ERROR = 12;
    @Deprecated
    public static final int TETHER_ERROR_DISABLE_NAT_ERROR = 9;
    @Deprecated
    public static final int TETHER_ERROR_ENABLE_NAT_ERROR = 8;
    @SystemApi
    @Deprecated
    public static final int TETHER_ERROR_ENTITLEMENT_UNKONWN = 13;
    @Deprecated
    public static final int TETHER_ERROR_IFACE_CFG_ERROR = 10;
    @Deprecated
    public static final int TETHER_ERROR_MASTER_ERROR = 5;
    @SystemApi
    @Deprecated
    public static final int TETHER_ERROR_NO_ERROR = 0;
    @SystemApi
    @Deprecated
    public static final int TETHER_ERROR_PROVISION_FAILED = 11;
    @Deprecated
    public static final int TETHER_ERROR_SERVICE_UNAVAIL = 2;
    @Deprecated
    public static final int TETHER_ERROR_TETHER_IFACE_ERROR = 6;
    @Deprecated
    public static final int TETHER_ERROR_UNAVAIL_IFACE = 4;
    @Deprecated
    public static final int TETHER_ERROR_UNKNOWN_IFACE = 1;
    @Deprecated
    public static final int TETHER_ERROR_UNSUPPORTED = 3;
    @Deprecated
    public static final int TETHER_ERROR_UNTETHER_IFACE_ERROR = 7;
    private static final int TUN_INTF_NETID_RANGE = 1024;
    private static final int TUN_INTF_NETID_START = 64512;
    @Deprecated
    public static final int TYPE_BLUETOOTH = 7;
    @Deprecated
    public static final int TYPE_DUMMY = 8;
    @Deprecated
    public static final int TYPE_ETHERNET = 9;
    @Deprecated
    public static final int TYPE_MOBILE = 0;
    @Deprecated
    public static final int TYPE_MOBILE_CBS = 12;
    @Deprecated
    public static final int TYPE_MOBILE_DUN = 4;
    @Deprecated
    public static final int TYPE_MOBILE_EMERGENCY = 15;
    @Deprecated
    public static final int TYPE_MOBILE_FOTA = 10;
    @Deprecated
    public static final int TYPE_MOBILE_HIPRI = 5;
    @Deprecated
    public static final int TYPE_MOBILE_IA = 14;
    @Deprecated
    public static final int TYPE_MOBILE_IMS = 11;
    @Deprecated
    public static final int TYPE_MOBILE_MMS = 2;
    @Deprecated
    public static final int TYPE_MOBILE_SUPL = 3;
    @SystemApi
    public static final int TYPE_NONE = -1;
    @SystemApi
    @Deprecated
    public static final int TYPE_PROXY = 16;
    @Deprecated
    public static final int TYPE_TEST = 18;
    @Deprecated
    public static final int TYPE_VPN = 17;
    @Deprecated
    public static final int TYPE_WIFI = 1;
    @SystemApi
    @Deprecated
    public static final int TYPE_WIFI_P2P = 13;
    @Deprecated
    public static final int TYPE_WIMAX = 6;
    private static CallbackHandler sCallbackHandler;
    /* access modifiers changed from: private */
    public static final HashMap<NetworkRequest, NetworkCallback> sCallbacks = new HashMap<>();
    private static ConnectivityManager sInstance;
    private static final HashMap<NetworkCapabilities, LegacyRequest> sLegacyRequests = new HashMap<>();
    private static final SparseIntArray sLegacyTypeToCapability;
    private static final SparseIntArray sLegacyTypeToTransport;
    private final Context mContext;
    private final ArrayMap<OnNetworkActiveListener, INetworkActivityListener> mNetworkActivityListeners = new ArrayMap<>();
    private final List<QosCallbackConnection> mQosCallbackConnections = new ArrayList();
    /* access modifiers changed from: private */
    public final IConnectivityManager mService;
    private final ArrayMap<OnTetheringEventCallback, TetheringManager.TetheringEventCallback> mTetheringEventCallbacks = new ArrayMap<>();
    private TetheringManager mTetheringManager;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BlockedReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntitlementResultCode {
    }

    public interface Errors {
        public static final int TOO_MANY_REQUESTS = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FirewallChain {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FirewallRule {
    }

    @Deprecated
    @Retention(RetentionPolicy.SOURCE)
    public @interface LegacyNetworkType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MultipathPreference {
    }

    public interface OnNetworkActiveListener {
        void onNetworkActive();
    }

    @SystemApi
    @Deprecated
    public static abstract class OnStartTetheringCallback {
        public void onTetheringFailed() {
        }

        public void onTetheringStarted() {
        }
    }

    @SystemApi
    @Deprecated
    public interface OnTetheringEntitlementResultListener {
        void onTetheringEntitlementResult(int i);
    }

    @SystemApi
    @Deprecated
    public static abstract class OnTetheringEventCallback {
        public void onUpstreamChanged(Network network) {
        }
    }

    public static class PacketKeepaliveCallback {
        public void onError(int i) {
        }

        public void onStarted() {
        }

        public void onStopped() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProfileNetworkPreferencePolicy {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RestrictBackgroundStatus {
    }

    public static class TooManyRequestsException extends RuntimeException {
    }

    @Deprecated
    public static boolean isNetworkTypeMobile(int i) {
        if (i == 0 || i == 2 || i == 3 || i == 4 || i == 5 || i == 14 || i == 15) {
            return true;
        }
        switch (i) {
            case 10:
            case 11:
            case 12:
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    public static boolean isNetworkTypeValid(int i) {
        return i >= 0 && i <= 18;
    }

    @Deprecated
    public static boolean isNetworkTypeWifi(int i) {
        return i == 1 || i == 13;
    }

    @Deprecated
    public boolean getBackgroundDataSetting() {
        return true;
    }

    @Deprecated
    public int getNetworkPreference() {
        return -1;
    }

    @Deprecated
    public void setBackgroundDataSetting(boolean z) {
    }

    @Deprecated
    public void setNetworkPreference(int i) {
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sLegacyTypeToTransport = sparseIntArray;
        sparseIntArray.put(0, 0);
        sparseIntArray.put(12, 0);
        sparseIntArray.put(4, 0);
        sparseIntArray.put(10, 0);
        sparseIntArray.put(5, 0);
        sparseIntArray.put(11, 0);
        sparseIntArray.put(2, 0);
        sparseIntArray.put(3, 0);
        sparseIntArray.put(1, 1);
        sparseIntArray.put(13, 1);
        sparseIntArray.put(7, 2);
        sparseIntArray.put(9, 3);
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        sLegacyTypeToCapability = sparseIntArray2;
        sparseIntArray2.put(12, 5);
        sparseIntArray2.put(4, 2);
        sparseIntArray2.put(10, 3);
        sparseIntArray2.put(11, 4);
        sparseIntArray2.put(2, 0);
        sparseIntArray2.put(3, 1);
        sparseIntArray2.put(13, 6);
    }

    private TetheringManager getTetheringManager() {
        TetheringManager tetheringManager;
        synchronized (this.mTetheringEventCallbacks) {
            if (this.mTetheringManager == null) {
                this.mTetheringManager = (TetheringManager) this.mContext.getSystemService(TetheringManager.class);
            }
            tetheringManager = this.mTetheringManager;
        }
        return tetheringManager;
    }

    @Deprecated
    public static String getNetworkTypeName(int i) {
        switch (i) {
            case -1:
                return "NONE";
            case 0:
                return "MOBILE";
            case 1:
                return "WIFI";
            case 2:
                return "MOBILE_MMS";
            case 3:
                return "MOBILE_SUPL";
            case 4:
                return "MOBILE_DUN";
            case 5:
                return "MOBILE_HIPRI";
            case 6:
                return "WIMAX";
            case 7:
                return "BLUETOOTH";
            case 8:
                return "DUMMY";
            case 9:
                return "ETHERNET";
            case 10:
                return "MOBILE_FOTA";
            case 11:
                return "MOBILE_IMS";
            case 12:
                return "MOBILE_CBS";
            case 13:
                return "WIFI_P2P";
            case 14:
                return "MOBILE_IA";
            case 15:
                return "MOBILE_EMERGENCY";
            case 16:
                return "PROXY";
            case 17:
                return "VPN";
            default:
                return Integer.toString(i);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void systemReady() {
        try {
            this.mService.systemReady();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo getActiveNetworkInfo() {
        try {
            return this.mService.getActiveNetworkInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network getActiveNetwork() {
        try {
            return this.mService.getActiveNetwork();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network getActiveNetworkForUid(int i) {
        return getActiveNetworkForUid(i, false);
    }

    public Network getActiveNetworkForUid(int i, boolean z) {
        try {
            return this.mService.getActiveNetworkForUid(i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setRequireVpnForUids(boolean z, Collection<Range<Integer>> collection) {
        Objects.requireNonNull(collection);
        UidRange[] uidRangeArr = new UidRange[collection.size()];
        int i = 0;
        for (Range next : collection) {
            uidRangeArr[i] = new UidRange(((Integer) next.getLower()).intValue(), ((Integer) next.getUpper()).intValue());
            i++;
        }
        try {
            this.mService.setRequireVpnForUids(z, uidRangeArr);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setLegacyLockdownVpnEnabled(boolean z) {
        try {
            this.mService.setLegacyLockdownVpnEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NetworkInfo getActiveNetworkInfoForUid(int i) {
        return getActiveNetworkInfoForUid(i, false);
    }

    public NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) {
        try {
            return this.mService.getActiveNetworkInfoForUid(i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo getNetworkInfo(int i) {
        try {
            return this.mService.getNetworkInfo(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo getNetworkInfo(Network network) {
        return getNetworkInfoForUid(network, Process.myUid(), false);
    }

    public NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) {
        try {
            return this.mService.getNetworkInfoForUid(network, i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo[] getAllNetworkInfo() {
        try {
            return this.mService.getAllNetworkInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public List<NetworkStateSnapshot> getAllNetworkStateSnapshots() {
        try {
            return this.mService.getAllNetworkStateSnapshots();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public Network getNetworkForType(int i) {
        try {
            return this.mService.getNetworkForType(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public Network[] getAllNetworks() {
        try {
            return this.mService.getAllNetworks();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i) {
        try {
            return this.mService.getDefaultNetworkCapabilitiesForUser(i, this.mContext.getOpPackageName(), getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public LinkProperties getActiveLinkProperties() {
        try {
            return this.mService.getActiveLinkProperties();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public LinkProperties getLinkProperties(int i) {
        try {
            return this.mService.getLinkPropertiesForType(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public LinkProperties getLinkProperties(Network network) {
        try {
            return this.mService.getLinkProperties(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public LinkProperties getRedactedLinkPropertiesForPackage(LinkProperties linkProperties, int i, String str) {
        try {
            return this.mService.getRedactedLinkPropertiesForPackage(linkProperties, i, str, getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NetworkCapabilities getNetworkCapabilities(Network network) {
        try {
            return this.mService.getNetworkCapabilities(network, this.mContext.getOpPackageName(), getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkCapabilities getRedactedNetworkCapabilitiesForPackage(NetworkCapabilities networkCapabilities, int i, String str) {
        try {
            return this.mService.getRedactedNetworkCapabilitiesForPackage(networkCapabilities, i, str, getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public String getCaptivePortalServerUrl() {
        try {
            return this.mService.getCaptivePortalServerUrl();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0058, code lost:
        if (r4 == null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005a, code lost:
        android.util.Log.d(TAG, "starting startUsingNetworkFeature for request " + r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006e, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006f, code lost:
        android.util.Log.d(TAG, " request Failed");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0076, code lost:
        return 3;
     */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int startUsingNetworkFeature(int r5, java.lang.String r6) {
        /*
            r4 = this;
            java.lang.String r0 = "renewing startUsingNetworkFeature request "
            r4.checkLegacyRoutingApiAccess()
            android.net.NetworkCapabilities r1 = r4.networkCapabilitiesForFeature(r5, r6)
            r2 = 3
            if (r1 != 0) goto L_0x0028
            java.lang.String r4 = "ConnectivityManager"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Can't satisfy startUsingNetworkFeature for "
            r0.<init>((java.lang.String) r1)
            r0.append((int) r5)
            java.lang.String r5 = ", "
            r0.append((java.lang.String) r5)
            r0.append((java.lang.String) r6)
            java.lang.String r5 = r0.toString()
            android.util.Log.d(r4, r5)
            return r2
        L_0x0028:
            java.util.HashMap<android.net.NetworkCapabilities, android.net.ConnectivityManager$LegacyRequest> r5 = sLegacyRequests
            monitor-enter(r5)
            java.lang.Object r6 = r5.get(r1)     // Catch:{ all -> 0x0077 }
            android.net.ConnectivityManager$LegacyRequest r6 = (android.net.ConnectivityManager.LegacyRequest) r6     // Catch:{ all -> 0x0077 }
            r3 = 1
            if (r6 == 0) goto L_0x0053
            java.lang.String r1 = "ConnectivityManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0077 }
            r2.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0077 }
            android.net.NetworkRequest r0 = r6.networkRequest     // Catch:{ all -> 0x0077 }
            r2.append((java.lang.Object) r0)     // Catch:{ all -> 0x0077 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0077 }
            android.util.Log.d(r1, r0)     // Catch:{ all -> 0x0077 }
            r4.renewRequestLocked(r6)     // Catch:{ all -> 0x0077 }
            android.net.Network r4 = r6.currentNetwork     // Catch:{ all -> 0x0077 }
            if (r4 == 0) goto L_0x0051
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            r4 = 0
            return r4
        L_0x0051:
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            return r3
        L_0x0053:
            android.net.NetworkRequest r4 = r4.requestNetworkForFeatureLocked(r1)     // Catch:{ all -> 0x0077 }
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            if (r4 == 0) goto L_0x006f
            java.lang.String r5 = "ConnectivityManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r0 = "starting startUsingNetworkFeature for request "
            r6.<init>((java.lang.String) r0)
            r6.append((java.lang.Object) r4)
            java.lang.String r4 = r6.toString()
            android.util.Log.d(r5, r4)
            return r3
        L_0x006f:
            java.lang.String r4 = "ConnectivityManager"
            java.lang.String r5 = " request Failed"
            android.util.Log.d(r4, r5)
            return r2
        L_0x0077:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.startUsingNetworkFeature(int, java.lang.String):int");
    }

    @Deprecated
    public int stopUsingNetworkFeature(int i, String str) {
        checkLegacyRoutingApiAccess();
        NetworkCapabilities networkCapabilitiesForFeature = networkCapabilitiesForFeature(i, str);
        if (networkCapabilitiesForFeature == null) {
            Log.d(TAG, "Can't satisfy stopUsingNetworkFeature for " + i + ", " + str);
            return -1;
        } else if (!removeRequestForFeature(networkCapabilitiesForFeature)) {
            return 1;
        } else {
            Log.d(TAG, "stopUsingNetworkFeature for " + i + ", " + str);
            return 1;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005e, code lost:
        if (r8.equals("enableCBS") == false) goto L_0x0013;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.net.NetworkCapabilities networkCapabilitiesForFeature(int r7, java.lang.String r8) {
        /*
            r6 = this;
            r6 = 0
            r0 = 1
            if (r7 != 0) goto L_0x0098
            r8.hashCode()
            int r7 = r8.hashCode()
            r1 = 5
            r2 = 4
            r3 = 3
            r4 = 2
            r5 = -1
            switch(r7) {
                case -1451370941: goto L_0x0061;
                case -631682191: goto L_0x0058;
                case -631680646: goto L_0x004d;
                case -631676084: goto L_0x0042;
                case -631672240: goto L_0x0037;
                case 1892790521: goto L_0x002c;
                case 1893183457: goto L_0x0021;
                case 1998933033: goto L_0x0016;
                default: goto L_0x0013;
            }
        L_0x0013:
            r0 = r5
            goto L_0x006b
        L_0x0016:
            java.lang.String r7 = "enableDUNAlways"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x001f
            goto L_0x0013
        L_0x001f:
            r0 = 7
            goto L_0x006b
        L_0x0021:
            java.lang.String r7 = "enableSUPL"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x002a
            goto L_0x0013
        L_0x002a:
            r0 = 6
            goto L_0x006b
        L_0x002c:
            java.lang.String r7 = "enableFOTA"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x0035
            goto L_0x0013
        L_0x0035:
            r0 = r1
            goto L_0x006b
        L_0x0037:
            java.lang.String r7 = "enableMMS"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x0040
            goto L_0x0013
        L_0x0040:
            r0 = r2
            goto L_0x006b
        L_0x0042:
            java.lang.String r7 = "enableIMS"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x004b
            goto L_0x0013
        L_0x004b:
            r0 = r3
            goto L_0x006b
        L_0x004d:
            java.lang.String r7 = "enableDUN"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x0056
            goto L_0x0013
        L_0x0056:
            r0 = r4
            goto L_0x006b
        L_0x0058:
            java.lang.String r7 = "enableCBS"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x006b
            goto L_0x0013
        L_0x0061:
            java.lang.String r7 = "enableHIPRI"
            boolean r7 = r8.equals(r7)
            if (r7 != 0) goto L_0x006a
            goto L_0x0013
        L_0x006a:
            r0 = 0
        L_0x006b:
            switch(r0) {
                case 0: goto L_0x0093;
                case 1: goto L_0x008c;
                case 2: goto L_0x0087;
                case 3: goto L_0x0080;
                case 4: goto L_0x007b;
                case 5: goto L_0x0074;
                case 6: goto L_0x006f;
                case 7: goto L_0x0087;
                default: goto L_0x006e;
            }
        L_0x006e:
            return r6
        L_0x006f:
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r3)
            return r6
        L_0x0074:
            r6 = 10
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r6)
            return r6
        L_0x007b:
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r4)
            return r6
        L_0x0080:
            r6 = 11
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r6)
            return r6
        L_0x0087:
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r2)
            return r6
        L_0x008c:
            r6 = 12
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r6)
            return r6
        L_0x0093:
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r1)
            return r6
        L_0x0098:
            if (r7 != r0) goto L_0x00a8
            java.lang.String r7 = "p2p"
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x00a8
            r6 = 13
            android.net.NetworkCapabilities r6 = networkCapabilitiesForType(r6)
        L_0x00a8:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.networkCapabilitiesForFeature(int, java.lang.String):android.net.NetworkCapabilities");
    }

    private int legacyTypeForNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return -1;
        }
        if (networkCapabilities.hasCapability(5)) {
            return 12;
        }
        if (networkCapabilities.hasCapability(4)) {
            return 11;
        }
        if (networkCapabilities.hasCapability(3)) {
            return 10;
        }
        if (networkCapabilities.hasCapability(2)) {
            return 4;
        }
        if (networkCapabilities.hasCapability(1)) {
            return 3;
        }
        if (networkCapabilities.hasCapability(0)) {
            return 2;
        }
        if (networkCapabilities.hasCapability(12)) {
            return 5;
        }
        if (networkCapabilities.hasCapability(6)) {
            return 13;
        }
        return -1;
    }

    private static class LegacyRequest {
        Network currentNetwork;
        int delay;
        int expireSequenceNumber;
        NetworkCallback networkCallback;
        NetworkCapabilities networkCapabilities;
        NetworkRequest networkRequest;

        private LegacyRequest() {
            this.delay = -1;
            this.networkCallback = new NetworkCallback() {
                public void onAvailable(Network network) {
                    LegacyRequest.this.currentNetwork = network;
                    Log.d(ConnectivityManager.TAG, "startUsingNetworkFeature got Network:" + network);
                    ConnectivityManager.setProcessDefaultNetworkForHostResolution(network);
                }

                public void onLost(Network network) {
                    if (network.equals(LegacyRequest.this.currentNetwork)) {
                        LegacyRequest.this.clearDnsBinding();
                    }
                    Log.d(ConnectivityManager.TAG, "startUsingNetworkFeature lost Network:" + network);
                }
            };
        }

        /* access modifiers changed from: private */
        public void clearDnsBinding() {
            if (this.currentNetwork != null) {
                this.currentNetwork = null;
                ConnectivityManager.setProcessDefaultNetworkForHostResolution((Network) null);
            }
        }
    }

    private NetworkRequest findRequestForFeature(NetworkCapabilities networkCapabilities) {
        HashMap<NetworkCapabilities, LegacyRequest> hashMap = sLegacyRequests;
        synchronized (hashMap) {
            LegacyRequest legacyRequest = hashMap.get(networkCapabilities);
            if (legacyRequest == null) {
                return null;
            }
            NetworkRequest networkRequest = legacyRequest.networkRequest;
            return networkRequest;
        }
    }

    private void renewRequestLocked(LegacyRequest legacyRequest) {
        legacyRequest.expireSequenceNumber++;
        Log.d(TAG, "renewing request to seqNum " + legacyRequest.expireSequenceNumber);
        sendExpireMsgForFeature(legacyRequest.networkCapabilities, legacyRequest.expireSequenceNumber, legacyRequest.delay);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        android.util.Log.d(TAG, "expireRequest with " + r2 + ", " + r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0032, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void expireRequest(android.net.NetworkCapabilities r4, int r5) {
        /*
            r3 = this;
            java.util.HashMap<android.net.NetworkCapabilities, android.net.ConnectivityManager$LegacyRequest> r0 = sLegacyRequests
            monitor-enter(r0)
            java.lang.Object r1 = r0.get(r4)     // Catch:{ all -> 0x0033 }
            android.net.ConnectivityManager$LegacyRequest r1 = (android.net.ConnectivityManager.LegacyRequest) r1     // Catch:{ all -> 0x0033 }
            if (r1 != 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            return
        L_0x000d:
            int r2 = r1.expireSequenceNumber     // Catch:{ all -> 0x0033 }
            int r1 = r1.expireSequenceNumber     // Catch:{ all -> 0x0033 }
            if (r1 != r5) goto L_0x0016
            r3.removeRequestForFeature(r4)     // Catch:{ all -> 0x0033 }
        L_0x0016:
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            java.lang.String r3 = "ConnectivityManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r0 = "expireRequest with "
            r4.<init>((java.lang.String) r0)
            r4.append((int) r2)
            java.lang.String r0 = ", "
            r4.append((java.lang.String) r0)
            r4.append((int) r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r3, r4)
            return
        L_0x0033:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.expireRequest(android.net.NetworkCapabilities, int):void");
    }

    private NetworkRequest requestNetworkForFeatureLocked(NetworkCapabilities networkCapabilities) {
        int legacyTypeForNetworkCapabilities = legacyTypeForNetworkCapabilities(networkCapabilities);
        try {
            int restoreDefaultNetworkDelay = this.mService.getRestoreDefaultNetworkDelay(legacyTypeForNetworkCapabilities);
            LegacyRequest legacyRequest = new LegacyRequest();
            legacyRequest.networkCapabilities = networkCapabilities;
            legacyRequest.delay = restoreDefaultNetworkDelay;
            legacyRequest.expireSequenceNumber = 0;
            legacyRequest.networkRequest = sendRequestForNetwork(networkCapabilities, legacyRequest.networkCallback, 0, NetworkRequest.Type.REQUEST, legacyTypeForNetworkCapabilities, getDefaultHandler());
            if (legacyRequest.networkRequest == null) {
                return null;
            }
            sLegacyRequests.put(networkCapabilities, legacyRequest);
            sendExpireMsgForFeature(networkCapabilities, legacyRequest.expireSequenceNumber, restoreDefaultNetworkDelay);
            return legacyRequest.networkRequest;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void sendExpireMsgForFeature(NetworkCapabilities networkCapabilities, int i, int i2) {
        if (i2 >= 0) {
            Log.d(TAG, "sending expire msg with seqNum " + i + " and delay " + i2);
            CallbackHandler defaultHandler = getDefaultHandler();
            defaultHandler.sendMessageDelayed(defaultHandler.obtainMessage(8, i, 0, networkCapabilities), (long) i2);
        }
    }

    private boolean removeRequestForFeature(NetworkCapabilities networkCapabilities) {
        LegacyRequest remove;
        HashMap<NetworkCapabilities, LegacyRequest> hashMap = sLegacyRequests;
        synchronized (hashMap) {
            remove = hashMap.remove(networkCapabilities);
        }
        if (remove == null) {
            return false;
        }
        unregisterNetworkCallback(remove.networkCallback);
        remove.clearDnsBinding();
        return true;
    }

    public static NetworkCapabilities networkCapabilitiesForType(int i) {
        NetworkCapabilities networkCapabilities = new NetworkCapabilities();
        int i2 = sLegacyTypeToTransport.get(i, -1);
        if (i2 != -1) {
            networkCapabilities.addTransportType(i2);
            networkCapabilities.addCapability(sLegacyTypeToCapability.get(i, 12));
            networkCapabilities.maybeMarkCapabilitiesRestricted();
            return networkCapabilities;
        }
        throw new IllegalArgumentException("unknown legacy type: " + i);
    }

    public class PacketKeepalive {
        public static final int BINDER_DIED = -10;
        public static final int ERROR_HARDWARE_ERROR = -31;
        public static final int ERROR_HARDWARE_UNSUPPORTED = -30;
        public static final int ERROR_INVALID_INTERVAL = -24;
        public static final int ERROR_INVALID_IP_ADDRESS = -21;
        public static final int ERROR_INVALID_LENGTH = -23;
        public static final int ERROR_INVALID_NETWORK = -20;
        public static final int ERROR_INVALID_PORT = -22;
        public static final int MIN_INTERVAL = 10;
        public static final int NATT_PORT = 4500;
        public static final int NO_KEEPALIVE = -1;
        public static final int SUCCESS = 0;
        private static final String TAG = "PacketKeepalive";
        /* access modifiers changed from: private */
        public final ISocketKeepaliveCallback mCallback;
        /* access modifiers changed from: private */
        public final ExecutorService mExecutor;
        private final Network mNetwork;
        /* access modifiers changed from: private */
        public volatile Integer mSlot;

        public void stop() {
            try {
                this.mExecutor.execute(new ConnectivityManager$PacketKeepalive$$ExternalSyntheticLambda0(this));
            } catch (RejectedExecutionException unused) {
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$stop$0$android-net-ConnectivityManager$PacketKeepalive  reason: not valid java name */
        public /* synthetic */ void m1879lambda$stop$0$androidnetConnectivityManager$PacketKeepalive() {
            try {
                if (this.mSlot != null) {
                    ConnectivityManager.this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Error stopping packet keepalive: ", e);
                throw e.rethrowFromSystemServer();
            }
        }

        private PacketKeepalive(Network network, final PacketKeepaliveCallback packetKeepaliveCallback) {
            Objects.requireNonNull(network, "network cannot be null");
            Objects.requireNonNull(packetKeepaliveCallback, "callback cannot be null");
            this.mNetwork = network;
            this.mExecutor = Executors.newSingleThreadExecutor();
            this.mCallback = new ISocketKeepaliveCallback.Stub(ConnectivityManager.this) {
                public void onDataReceived() {
                }

                public void onStarted(int i) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        PacketKeepalive.this.mExecutor.execute(new ConnectivityManager$PacketKeepalive$1$$ExternalSyntheticLambda2(this, i, packetKeepaliveCallback));
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onStarted$0$android-net-ConnectivityManager$PacketKeepalive$1 */
                public /* synthetic */ void mo2056x23f40a3e(int i, PacketKeepaliveCallback packetKeepaliveCallback) {
                    PacketKeepalive.this.mSlot = Integer.valueOf(i);
                    packetKeepaliveCallback.onStarted();
                }

                /* JADX INFO: finally extract failed */
                public void onStopped() {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        PacketKeepalive.this.mExecutor.execute(new ConnectivityManager$PacketKeepalive$1$$ExternalSyntheticLambda0(this, packetKeepaliveCallback));
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        PacketKeepalive.this.mExecutor.shutdown();
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onStopped$1$android-net-ConnectivityManager$PacketKeepalive$1 */
                public /* synthetic */ void mo2057xcdd9ab4b(PacketKeepaliveCallback packetKeepaliveCallback) {
                    PacketKeepalive.this.mSlot = null;
                    packetKeepaliveCallback.onStopped();
                }

                /* JADX INFO: finally extract failed */
                public void onError(int i) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        PacketKeepalive.this.mExecutor.execute(new ConnectivityManager$PacketKeepalive$1$$ExternalSyntheticLambda1(this, packetKeepaliveCallback, i));
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        PacketKeepalive.this.mExecutor.shutdown();
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onError$2$android-net-ConnectivityManager$PacketKeepalive$1 */
                public /* synthetic */ void mo2055xb34c2c7(PacketKeepaliveCallback packetKeepaliveCallback, int i) {
                    PacketKeepalive.this.mSlot = null;
                    packetKeepaliveCallback.onError(i);
                }
            };
        }
    }

    public PacketKeepalive startNattKeepalive(Network network, int i, PacketKeepaliveCallback packetKeepaliveCallback, InetAddress inetAddress, int i2, InetAddress inetAddress2) {
        PacketKeepalive packetKeepalive = new PacketKeepalive(network, packetKeepaliveCallback);
        try {
            this.mService.startNattKeepalive(network, i, packetKeepalive.mCallback, inetAddress.getHostAddress(), i2, inetAddress2.getHostAddress());
            return packetKeepalive;
        } catch (RemoteException e) {
            Log.e(TAG, "Error starting packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    private ParcelFileDescriptor createInvalidFd() {
        return ParcelFileDescriptor.adoptFd(-1);
    }

    public SocketKeepalive createSocketKeepalive(Network network, IpSecManager.UdpEncapsulationSocket udpEncapsulationSocket, InetAddress inetAddress, InetAddress inetAddress2, Executor executor, SocketKeepalive.Callback callback) {
        ParcelFileDescriptor parcelFileDescriptor;
        try {
            parcelFileDescriptor = ParcelFileDescriptor.dup(udpEncapsulationSocket.getFileDescriptor());
        } catch (IOException unused) {
            parcelFileDescriptor = createInvalidFd();
        }
        return new NattSocketKeepalive(this.mService, network, parcelFileDescriptor, udpEncapsulationSocket.getResourceId(), inetAddress, inetAddress2, executor, callback);
    }

    @SystemApi
    public SocketKeepalive createNattKeepalive(Network network, ParcelFileDescriptor parcelFileDescriptor, InetAddress inetAddress, InetAddress inetAddress2, Executor executor, SocketKeepalive.Callback callback) {
        ParcelFileDescriptor createInvalidFd;
        try {
            createInvalidFd = parcelFileDescriptor.dup();
        } catch (IOException unused) {
            createInvalidFd = createInvalidFd();
        }
        return new NattSocketKeepalive(this.mService, network, createInvalidFd, -1, inetAddress, inetAddress2, executor, callback);
    }

    @SystemApi
    public SocketKeepalive createSocketKeepalive(Network network, Socket socket, Executor executor, SocketKeepalive.Callback callback) {
        ParcelFileDescriptor createInvalidFd;
        try {
            createInvalidFd = ParcelFileDescriptor.fromSocket(socket);
        } catch (UncheckedIOException unused) {
            createInvalidFd = createInvalidFd();
        }
        return new TcpSocketKeepalive(this.mService, network, createInvalidFd, executor, callback);
    }

    @Deprecated
    public boolean requestRouteToHost(int i, int i2) {
        return requestRouteToHostAddress(i, NetworkUtils.intToInetAddress(i2));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public boolean requestRouteToHostAddress(int i, InetAddress inetAddress) {
        checkLegacyRoutingApiAccess();
        try {
            return this.mService.requestRouteToHostAddress(i, inetAddress.getAddress(), this.mContext.getOpPackageName(), getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private String getAttributionTag() {
        return this.mContext.getAttributionTag();
    }

    @Deprecated
    public boolean getMobileDataEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        if (telephonyManager != null) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            Log.d(TAG, "getMobileDataEnabled()+ subId=" + defaultDataSubscriptionId);
            boolean isDataEnabled = telephonyManager.createForSubscriptionId(defaultDataSubscriptionId).isDataEnabled();
            Log.d(TAG, "getMobileDataEnabled()- subId=" + defaultDataSubscriptionId + " retVal=" + isDataEnabled);
            return isDataEnabled;
        }
        Log.d(TAG, "getMobileDataEnabled()- remote exception retVal=false");
        return false;
    }

    public void addDefaultNetworkActiveListener(final OnNetworkActiveListener onNetworkActiveListener) {
        C00421 r0 = new INetworkActivityListener.Stub() {
            public void onNetworkActive() throws RemoteException {
                onNetworkActiveListener.onNetworkActive();
            }
        };
        synchronized (this.mNetworkActivityListeners) {
            try {
                this.mService.registerNetworkActivityListener(r0);
                this.mNetworkActivityListeners.put(onNetworkActiveListener, r0);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void removeDefaultNetworkActiveListener(OnNetworkActiveListener onNetworkActiveListener) {
        synchronized (this.mNetworkActivityListeners) {
            INetworkActivityListener iNetworkActivityListener = this.mNetworkActivityListeners.get(onNetworkActiveListener);
            if (iNetworkActivityListener != null) {
                try {
                    this.mService.unregisterNetworkActivityListener(iNetworkActivityListener);
                    this.mNetworkActivityListeners.remove(onNetworkActiveListener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                throw new IllegalArgumentException("Listener was not registered.");
            }
        }
    }

    public boolean isDefaultNetworkActive() {
        try {
            return this.mService.isDefaultNetworkActive();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ConnectivityManager(Context context, IConnectivityManager iConnectivityManager) {
        this.mContext = (Context) Objects.requireNonNull(context, "missing context");
        this.mService = (IConnectivityManager) Objects.requireNonNull(iConnectivityManager, "missing IConnectivityManager");
        sInstance = this;
    }

    public static ConnectivityManager from(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    public NetworkRequest getDefaultRequest() {
        try {
            return this.mService.getDefaultRequest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static boolean checkAndNoteWriteSettingsOperation(Context context, int i, String str, String str2, boolean z) {
        return Settings.checkAndNoteWriteSettingsOperation(context, i, str, str2, z);
    }

    @Deprecated
    static ConnectivityManager getInstanceOrNull() {
        return sInstance;
    }

    @Deprecated
    private static ConnectivityManager getInstance() {
        if (getInstanceOrNull() != null) {
            return getInstanceOrNull();
        }
        throw new IllegalStateException("No ConnectivityManager yet constructed");
    }

    @Deprecated
    public String[] getTetherableIfaces() {
        return getTetheringManager().getTetherableIfaces();
    }

    @Deprecated
    public String[] getTetheredIfaces() {
        return getTetheringManager().getTetheredIfaces();
    }

    @Deprecated
    public String[] getTetheringErroredIfaces() {
        return getTetheringManager().getTetheringErroredIfaces();
    }

    @Deprecated
    public String[] getTetheredDhcpRanges() {
        throw new UnsupportedOperationException("getTetheredDhcpRanges is not supported");
    }

    @Deprecated
    public int tether(String str) {
        return getTetheringManager().tether(str);
    }

    @Deprecated
    public int untether(String str) {
        return getTetheringManager().untether(str);
    }

    @SystemApi
    public boolean isTetheringSupported() {
        return getTetheringManager().isTetheringSupported();
    }

    @SystemApi
    @Deprecated
    public void startTethering(int i, boolean z, OnStartTetheringCallback onStartTetheringCallback) {
        startTethering(i, z, onStartTetheringCallback, (Handler) null);
    }

    @SystemApi
    @Deprecated
    public void startTethering(int i, boolean z, final OnStartTetheringCallback onStartTetheringCallback, final Handler handler) {
        Objects.requireNonNull(onStartTetheringCallback, "OnStartTetheringCallback cannot be null.");
        C00432 r0 = new Executor() {
            public void execute(Runnable runnable) {
                Handler handler = handler;
                if (handler == null) {
                    runnable.run();
                } else {
                    handler.post(runnable);
                }
            }
        };
        C00443 r5 = new TetheringManager.StartTetheringCallback() {
            public void onTetheringStarted() {
                onStartTetheringCallback.onTetheringStarted();
            }

            public void onTetheringFailed(int i) {
                onStartTetheringCallback.onTetheringFailed();
            }
        };
        getTetheringManager().startTethering(new TetheringManager.TetheringRequest.Builder(i).setShouldShowEntitlementUi(z).build(), (Executor) r0, (TetheringManager.StartTetheringCallback) r5);
    }

    @SystemApi
    @Deprecated
    public void stopTethering(int i) {
        getTetheringManager().stopTethering(i);
    }

    @SystemApi
    @Deprecated
    public void registerTetheringEventCallback(Executor executor, final OnTetheringEventCallback onTetheringEventCallback) {
        Objects.requireNonNull(onTetheringEventCallback, "OnTetheringEventCallback cannot be null.");
        C00454 r0 = new TetheringManager.TetheringEventCallback() {
            public void onUpstreamChanged(Network network) {
                onTetheringEventCallback.onUpstreamChanged(network);
            }
        };
        synchronized (this.mTetheringEventCallbacks) {
            this.mTetheringEventCallbacks.put(onTetheringEventCallback, r0);
            getTetheringManager().registerTetheringEventCallback(executor, r0);
        }
    }

    @SystemApi
    @Deprecated
    public void unregisterTetheringEventCallback(OnTetheringEventCallback onTetheringEventCallback) {
        Objects.requireNonNull(onTetheringEventCallback, "The callback must be non-null");
        synchronized (this.mTetheringEventCallbacks) {
            getTetheringManager().unregisterTetheringEventCallback(this.mTetheringEventCallbacks.remove(onTetheringEventCallback));
        }
    }

    @Deprecated
    public String[] getTetherableUsbRegexs() {
        return getTetheringManager().getTetherableUsbRegexs();
    }

    @Deprecated
    public String[] getTetherableWifiRegexs() {
        return getTetheringManager().getTetherableWifiRegexs();
    }

    @Deprecated
    public String[] getTetherableBluetoothRegexs() {
        return getTetheringManager().getTetherableBluetoothRegexs();
    }

    @Deprecated
    public int setUsbTethering(boolean z) {
        return getTetheringManager().setUsbTethering(z);
    }

    @Deprecated
    public int getLastTetherError(String str) {
        int lastTetherError = getTetheringManager().getLastTetherError(str);
        if (lastTetherError == 16) {
            return 1;
        }
        return lastTetherError;
    }

    @SystemApi
    @Deprecated
    public void getLatestTetheringEntitlementResult(int i, boolean z, final Executor executor, final OnTetheringEntitlementResultListener onTetheringEntitlementResultListener) {
        Objects.requireNonNull(onTetheringEntitlementResultListener, "TetheringEntitlementResultListener cannot be null.");
        getTetheringManager().requestLatestTetheringEntitlementResult(i, new ResultReceiver((Handler) null) {
            /* access modifiers changed from: protected */
            public void onReceiveResult(int i, Bundle bundle) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    executor.execute(new ConnectivityManager$5$$ExternalSyntheticLambda0(onTetheringEntitlementResultListener, i));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }, z);
    }

    public void reportInetCondition(int i, int i2) {
        printStackTrace();
        try {
            this.mService.reportInetCondition(i, i2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void reportBadNetwork(Network network) {
        printStackTrace();
        try {
            this.mService.reportNetworkConnectivity(network, true);
            this.mService.reportNetworkConnectivity(network, false);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void reportNetworkConnectivity(Network network, boolean z) {
        printStackTrace();
        try {
            this.mService.reportNetworkConnectivity(network, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setGlobalProxy(ProxyInfo proxyInfo) {
        try {
            this.mService.setGlobalProxy(proxyInfo);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public ProxyInfo getGlobalProxy() {
        try {
            return this.mService.getGlobalProxy();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProxyInfo getProxyForNetwork(Network network) {
        try {
            return this.mService.getProxyForNetwork(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProxyInfo getDefaultProxy() {
        return getProxyForNetwork(getBoundNetworkForProcess());
    }

    @Deprecated
    public boolean isNetworkSupported(int i) {
        try {
            return this.mService.isNetworkSupported(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isActiveNetworkMetered() {
        try {
            return this.mService.isActiveNetworkMetered();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void setProvisioningNotificationVisible(boolean z, int i, String str) {
        try {
            this.mService.setProvisioningNotificationVisible(z, i, str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setAirplaneMode(boolean z) {
        try {
            this.mService.setAirplaneMode(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public int registerNetworkProvider(NetworkProvider networkProvider) {
        if (networkProvider.getProviderId() == -1) {
            try {
                networkProvider.setProviderId(this.mService.registerNetworkProvider(networkProvider.getMessenger(), networkProvider.getName()));
                return networkProvider.getProviderId();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalStateException("NetworkProviders can only be registered once");
        }
    }

    @SystemApi
    public void unregisterNetworkProvider(NetworkProvider networkProvider) {
        try {
            this.mService.unregisterNetworkProvider(networkProvider.getMessenger());
            networkProvider.setProviderId(-1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void offerNetwork(int i, NetworkScore networkScore, NetworkCapabilities networkCapabilities, INetworkOfferCallback iNetworkOfferCallback) {
        try {
            this.mService.offerNetwork(i, (NetworkScore) Objects.requireNonNull(networkScore, "null score"), (NetworkCapabilities) Objects.requireNonNull(networkCapabilities, "null caps"), (INetworkOfferCallback) Objects.requireNonNull(iNetworkOfferCallback, "null callback"));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unofferNetwork(INetworkOfferCallback iNetworkOfferCallback) {
        try {
            this.mService.unofferNetwork((INetworkOfferCallback) Objects.requireNonNull(iNetworkOfferCallback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void declareNetworkRequestUnfulfillable(NetworkRequest networkRequest) {
        try {
            this.mService.declareNetworkRequestUnfulfillable(networkRequest);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network registerNetworkAgent(INetworkAgent iNetworkAgent, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, int i) {
        try {
            return this.mService.registerNetworkAgent(iNetworkAgent, networkInfo, linkProperties, networkCapabilities, networkScore, networkAgentConfig, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static class NetworkCallback {
        public static final int FLAG_INCLUDE_LOCATION_INFO = 1;
        public static final int FLAG_NONE = 0;
        private static final int VALID_FLAGS = 1;
        /* access modifiers changed from: private */
        public final int mFlags;
        /* access modifiers changed from: private */
        public NetworkRequest networkRequest;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Flag {
        }

        public void onAvailable(Network network) {
        }

        public void onBlockedStatusChanged(Network network, boolean z) {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        }

        public void onLosing(Network network, int i) {
        }

        public void onLost(Network network) {
        }

        public void onNetworkResumed(Network network) {
        }

        public void onNetworkSuspended(Network network) {
        }

        public void onPreCheck(Network network) {
        }

        public void onUnavailable() {
        }

        public NetworkCallback() {
            this(0);
        }

        public NetworkCallback(int i) {
            if ((i & 1) == i) {
                this.mFlags = i;
                return;
            }
            throw new IllegalArgumentException("Invalid flags");
        }

        public final void onAvailable(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, int i) {
            onAvailable(network, networkCapabilities, linkProperties, i != 0);
            onBlockedStatusChanged(network, i);
        }

        public void onAvailable(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, boolean z) {
            onAvailable(network);
            if (!networkCapabilities.hasCapability(21)) {
                onNetworkSuspended(network);
            }
            onCapabilitiesChanged(network, networkCapabilities);
            onLinkPropertiesChanged(network, linkProperties);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void onBlockedStatusChanged(Network network, int i) {
            onBlockedStatusChanged(network, i != 0);
        }
    }

    private static RuntimeException convertServiceException(ServiceSpecificException serviceSpecificException) {
        if (serviceSpecificException.errorCode == 1) {
            return new TooManyRequestsException();
        }
        Log.w(TAG, "Unknown service error code " + serviceSpecificException.errorCode);
        return new RuntimeException((Throwable) serviceSpecificException);
    }

    public static String getCallbackName(int i) {
        switch (i) {
            case 1:
                return "CALLBACK_PRECHECK";
            case 2:
                return "CALLBACK_AVAILABLE";
            case 3:
                return "CALLBACK_LOSING";
            case 4:
                return "CALLBACK_LOST";
            case 5:
                return "CALLBACK_UNAVAIL";
            case 6:
                return "CALLBACK_CAP_CHANGED";
            case 7:
                return "CALLBACK_IP_CHANGED";
            case 8:
                return "EXPIRE_LEGACY_REQUEST";
            case 9:
                return "CALLBACK_SUSPENDED";
            case 10:
                return "CALLBACK_RESUMED";
            case 11:
                return "CALLBACK_BLK_CHANGED";
            default:
                return Integer.toString(i);
        }
    }

    private class CallbackHandler extends Handler {
        private static final boolean DBG = false;
        private static final String TAG = "ConnectivityManager.CallbackHandler";

        CallbackHandler(Looper looper) {
            super(looper);
        }

        CallbackHandler(ConnectivityManager connectivityManager, Handler handler) {
            this(((Handler) Objects.requireNonNull(handler, "Handler cannot be null.")).getLooper());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0069, code lost:
            switch(r7.what) {
                case 1: goto L_0x00b7;
                case 2: goto L_0x00a1;
                case 3: goto L_0x009b;
                case 4: goto L_0x0097;
                case 5: goto L_0x0093;
                case 6: goto L_0x0087;
                case 7: goto L_0x007b;
                case 8: goto L_0x006c;
                case 9: goto L_0x0077;
                case 10: goto L_0x0073;
                case 11: goto L_0x006d;
                default: goto L_0x006c;
            };
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x006d, code lost:
            r4.onBlockedStatusChanged(r2, r7.arg1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0073, code lost:
            r4.onNetworkResumed(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0077, code lost:
            r4.onNetworkSuspended(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x007b, code lost:
            r4.onLinkPropertiesChanged(r2, (android.net.LinkProperties) getObject(r7, android.net.LinkProperties.class));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0087, code lost:
            r4.onCapabilitiesChanged(r2, (android.net.NetworkCapabilities) getObject(r7, android.net.NetworkCapabilities.class));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0093, code lost:
            r4.onUnavailable();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0097, code lost:
            r4.onLost(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x009b, code lost:
            r4.onLosing(r2, r7.arg1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a1, code lost:
            r4.onAvailable(r2, (android.net.NetworkCapabilities) getObject(r7, android.net.NetworkCapabilities.class), (android.net.LinkProperties) getObject(r7, android.net.LinkProperties.class), r7.arg1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b7, code lost:
            r4.onPreCheck(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r7) {
            /*
                r6 = this;
                java.lang.String r0 = "callback not found for "
                int r1 = r7.what
                r2 = 8
                if (r1 != r2) goto L_0x0014
                android.net.ConnectivityManager r6 = android.net.ConnectivityManager.this
                java.lang.Object r0 = r7.obj
                android.net.NetworkCapabilities r0 = (android.net.NetworkCapabilities) r0
                int r7 = r7.arg1
                r6.expireRequest(r0, r7)
                return
            L_0x0014:
                java.lang.Class<android.net.NetworkRequest> r1 = android.net.NetworkRequest.class
                java.lang.Object r1 = r6.getObject(r7, r1)
                android.net.NetworkRequest r1 = (android.net.NetworkRequest) r1
                java.lang.Class<android.net.Network> r2 = android.net.Network.class
                java.lang.Object r2 = r6.getObject(r7, r2)
                android.net.Network r2 = (android.net.Network) r2
                java.util.HashMap r3 = android.net.ConnectivityManager.sCallbacks
                monitor-enter(r3)
                java.util.HashMap r4 = android.net.ConnectivityManager.sCallbacks     // Catch:{ all -> 0x00bb }
                java.lang.Object r4 = r4.get(r1)     // Catch:{ all -> 0x00bb }
                android.net.ConnectivityManager$NetworkCallback r4 = (android.net.ConnectivityManager.NetworkCallback) r4     // Catch:{ all -> 0x00bb }
                if (r4 != 0) goto L_0x0053
                java.lang.String r6 = "ConnectivityManager.CallbackHandler"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
                r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x00bb }
                int r7 = r7.what     // Catch:{ all -> 0x00bb }
                java.lang.String r7 = android.net.ConnectivityManager.getCallbackName(r7)     // Catch:{ all -> 0x00bb }
                r1.append((java.lang.String) r7)     // Catch:{ all -> 0x00bb }
                java.lang.String r7 = " message"
                r1.append((java.lang.String) r7)     // Catch:{ all -> 0x00bb }
                java.lang.String r7 = r1.toString()     // Catch:{ all -> 0x00bb }
                android.util.Log.w(r6, r7)     // Catch:{ all -> 0x00bb }
                monitor-exit(r3)     // Catch:{ all -> 0x00bb }
                return
            L_0x0053:
                int r0 = r7.what     // Catch:{ all -> 0x00bb }
                r5 = 5
                if (r0 != r5) goto L_0x0066
                java.util.HashMap r0 = android.net.ConnectivityManager.sCallbacks     // Catch:{ all -> 0x00bb }
                r0.remove(r1)     // Catch:{ all -> 0x00bb }
                android.net.NetworkRequest r0 = android.net.ConnectivityManager.ALREADY_UNREGISTERED     // Catch:{ all -> 0x00bb }
                r4.networkRequest = r0     // Catch:{ all -> 0x00bb }
            L_0x0066:
                monitor-exit(r3)     // Catch:{ all -> 0x00bb }
                int r0 = r7.what
                switch(r0) {
                    case 1: goto L_0x00b7;
                    case 2: goto L_0x00a1;
                    case 3: goto L_0x009b;
                    case 4: goto L_0x0097;
                    case 5: goto L_0x0093;
                    case 6: goto L_0x0087;
                    case 7: goto L_0x007b;
                    case 8: goto L_0x006c;
                    case 9: goto L_0x0077;
                    case 10: goto L_0x0073;
                    case 11: goto L_0x006d;
                    default: goto L_0x006c;
                }
            L_0x006c:
                goto L_0x00ba
            L_0x006d:
                int r6 = r7.arg1
                r4.onBlockedStatusChanged((android.net.Network) r2, (int) r6)
                goto L_0x00ba
            L_0x0073:
                r4.onNetworkResumed(r2)
                goto L_0x00ba
            L_0x0077:
                r4.onNetworkSuspended(r2)
                goto L_0x00ba
            L_0x007b:
                java.lang.Class<android.net.LinkProperties> r0 = android.net.LinkProperties.class
                java.lang.Object r6 = r6.getObject(r7, r0)
                android.net.LinkProperties r6 = (android.net.LinkProperties) r6
                r4.onLinkPropertiesChanged(r2, r6)
                goto L_0x00ba
            L_0x0087:
                java.lang.Class<android.net.NetworkCapabilities> r0 = android.net.NetworkCapabilities.class
                java.lang.Object r6 = r6.getObject(r7, r0)
                android.net.NetworkCapabilities r6 = (android.net.NetworkCapabilities) r6
                r4.onCapabilitiesChanged(r2, r6)
                goto L_0x00ba
            L_0x0093:
                r4.onUnavailable()
                goto L_0x00ba
            L_0x0097:
                r4.onLost(r2)
                goto L_0x00ba
            L_0x009b:
                int r6 = r7.arg1
                r4.onLosing(r2, r6)
                goto L_0x00ba
            L_0x00a1:
                java.lang.Class<android.net.NetworkCapabilities> r0 = android.net.NetworkCapabilities.class
                java.lang.Object r0 = r6.getObject(r7, r0)
                android.net.NetworkCapabilities r0 = (android.net.NetworkCapabilities) r0
                java.lang.Class<android.net.LinkProperties> r1 = android.net.LinkProperties.class
                java.lang.Object r6 = r6.getObject(r7, r1)
                android.net.LinkProperties r6 = (android.net.LinkProperties) r6
                int r7 = r7.arg1
                r4.onAvailable((android.net.Network) r2, (android.net.NetworkCapabilities) r0, (android.net.LinkProperties) r6, (int) r7)
                goto L_0x00ba
            L_0x00b7:
                r4.onPreCheck(r2)
            L_0x00ba:
                return
            L_0x00bb:
                r6 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x00bb }
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.CallbackHandler.handleMessage(android.os.Message):void");
        }

        private <T> T getObject(Message message, Class<T> cls) {
            return message.getData().getParcelable(cls.getSimpleName());
        }
    }

    private CallbackHandler getDefaultHandler() {
        CallbackHandler callbackHandler;
        synchronized (sCallbacks) {
            if (sCallbackHandler == null) {
                sCallbackHandler = new CallbackHandler(ConnectivityThread.getInstanceLooper());
            }
            callbackHandler = sCallbackHandler;
        }
        return callbackHandler;
    }

    private NetworkRequest sendRequestForNetwork(int i, NetworkCapabilities networkCapabilities, NetworkCallback networkCallback, int i2, NetworkRequest.Type type, int i3, CallbackHandler callbackHandler) {
        NetworkRequest networkRequest;
        NetworkCallback networkCallback2 = networkCallback;
        NetworkRequest.Type type2 = type;
        printStackTrace();
        checkCallbackNotNull(networkCallback);
        if (type2 == NetworkRequest.Type.TRACK_DEFAULT || type2 == NetworkRequest.Type.TRACK_SYSTEM_DEFAULT || networkCapabilities != null) {
            String opPackageName = this.mContext.getOpPackageName();
            try {
                HashMap<NetworkRequest, NetworkCallback> hashMap = sCallbacks;
                synchronized (hashMap) {
                    if (!(networkCallback.networkRequest == null || networkCallback.networkRequest == ALREADY_UNREGISTERED)) {
                        Log.e(TAG, "NetworkCallback was already registered");
                    }
                    Messenger messenger = new Messenger(callbackHandler);
                    Binder binder = new Binder();
                    int r12 = networkCallback.mFlags;
                    if (type2 == NetworkRequest.Type.LISTEN) {
                        networkRequest = this.mService.listenForNetwork(networkCapabilities, messenger, binder, r12, opPackageName, getAttributionTag());
                    } else {
                        networkRequest = this.mService.requestNetwork(i, networkCapabilities, type.ordinal(), messenger, i2, binder, i3, r12, opPackageName, getAttributionTag());
                    }
                    if (networkRequest != null) {
                        hashMap.put(networkRequest, networkCallback2);
                    }
                    networkCallback2.networkRequest = networkRequest;
                }
                return networkRequest;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (ServiceSpecificException e2) {
                throw convertServiceException(e2);
            }
        } else {
            throw new IllegalArgumentException("null NetworkCapabilities");
        }
    }

    private NetworkRequest sendRequestForNetwork(NetworkCapabilities networkCapabilities, NetworkCallback networkCallback, int i, NetworkRequest.Type type, int i2, CallbackHandler callbackHandler) {
        return sendRequestForNetwork(-1, networkCapabilities, networkCallback, i, type, i2, callbackHandler);
    }

    @SystemApi
    public void requestNetwork(NetworkRequest networkRequest, int i, int i2, Handler handler, NetworkCallback networkCallback) {
        if (i2 != -1) {
            NetworkCallback networkCallback2 = networkCallback;
            int i3 = i;
            sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback2, i3, NetworkRequest.Type.REQUEST, i2, new CallbackHandler(this, handler));
            return;
        }
        throw new IllegalArgumentException("TYPE_NONE is meaningless legacy type");
    }

    public void requestNetwork(NetworkRequest networkRequest, NetworkCallback networkCallback) {
        requestNetwork(networkRequest, networkCallback, (Handler) getDefaultHandler());
    }

    public void requestNetwork(NetworkRequest networkRequest, NetworkCallback networkCallback, Handler handler) {
        NetworkCallback networkCallback2 = networkCallback;
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback2, 0, NetworkRequest.Type.REQUEST, -1, new CallbackHandler(this, handler));
    }

    public void requestNetwork(NetworkRequest networkRequest, NetworkCallback networkCallback, int i) {
        checkTimeout(i);
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback, i, NetworkRequest.Type.REQUEST, -1, getDefaultHandler());
    }

    public void requestNetwork(NetworkRequest networkRequest, NetworkCallback networkCallback, Handler handler, int i) {
        checkTimeout(i);
        NetworkCallback networkCallback2 = networkCallback;
        int i2 = i;
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback2, i2, NetworkRequest.Type.REQUEST, -1, new CallbackHandler(this, handler));
    }

    public void requestNetwork(NetworkRequest networkRequest, PendingIntent pendingIntent) {
        printStackTrace();
        checkPendingIntentNotNull(pendingIntent);
        try {
            this.mService.pendingRequestForNetwork(networkRequest.networkCapabilities, pendingIntent, this.mContext.getOpPackageName(), getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw convertServiceException(e2);
        }
    }

    public void releaseNetworkRequest(PendingIntent pendingIntent) {
        printStackTrace();
        checkPendingIntentNotNull(pendingIntent);
        try {
            this.mService.releasePendingNetworkRequest(pendingIntent);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static void checkPendingIntentNotNull(PendingIntent pendingIntent) {
        Objects.requireNonNull(pendingIntent, "PendingIntent cannot be null.");
    }

    private static void checkCallbackNotNull(NetworkCallback networkCallback) {
        Objects.requireNonNull(networkCallback, "null NetworkCallback");
    }

    private static void checkTimeout(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("timeoutMs must be strictly positive.");
        }
    }

    public void registerNetworkCallback(NetworkRequest networkRequest, NetworkCallback networkCallback) {
        registerNetworkCallback(networkRequest, networkCallback, getDefaultHandler());
    }

    public void registerNetworkCallback(NetworkRequest networkRequest, NetworkCallback networkCallback, Handler handler) {
        NetworkCallback networkCallback2 = networkCallback;
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback2, 0, NetworkRequest.Type.LISTEN, -1, new CallbackHandler(this, handler));
    }

    public void registerNetworkCallback(NetworkRequest networkRequest, PendingIntent pendingIntent) {
        printStackTrace();
        checkPendingIntentNotNull(pendingIntent);
        try {
            this.mService.pendingListenForNetwork(networkRequest.networkCapabilities, pendingIntent, this.mContext.getOpPackageName(), getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw convertServiceException(e2);
        }
    }

    public void registerDefaultNetworkCallback(NetworkCallback networkCallback) {
        registerDefaultNetworkCallback(networkCallback, getDefaultHandler());
    }

    public void registerDefaultNetworkCallback(NetworkCallback networkCallback, Handler handler) {
        registerDefaultNetworkCallbackForUid(-1, networkCallback, handler);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void registerDefaultNetworkCallbackForUid(int i, NetworkCallback networkCallback, Handler handler) {
        int i2 = i;
        NetworkCallback networkCallback2 = networkCallback;
        sendRequestForNetwork(i2, (NetworkCapabilities) null, networkCallback2, 0, NetworkRequest.Type.TRACK_DEFAULT, -1, new CallbackHandler(this, handler));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void registerSystemDefaultNetworkCallback(NetworkCallback networkCallback, Handler handler) {
        NetworkCallback networkCallback2 = networkCallback;
        sendRequestForNetwork((NetworkCapabilities) null, networkCallback2, 0, NetworkRequest.Type.TRACK_SYSTEM_DEFAULT, -1, new CallbackHandler(this, handler));
    }

    public void registerBestMatchingNetworkCallback(NetworkRequest networkRequest, NetworkCallback networkCallback, Handler handler) {
        NetworkCallback networkCallback2 = networkCallback;
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback2, 0, NetworkRequest.Type.LISTEN_FOR_BEST, -1, new CallbackHandler(this, handler));
    }

    public boolean requestBandwidthUpdate(Network network) {
        try {
            return this.mService.requestBandwidthUpdate(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterNetworkCallback(NetworkCallback networkCallback) {
        printStackTrace();
        checkCallbackNotNull(networkCallback);
        ArrayList<NetworkRequest> arrayList = new ArrayList<>();
        HashMap<NetworkRequest, NetworkCallback> hashMap = sCallbacks;
        synchronized (hashMap) {
            if (networkCallback.networkRequest == null) {
                throw new IllegalArgumentException("NetworkCallback was not registered");
            } else if (networkCallback.networkRequest == ALREADY_UNREGISTERED) {
                Log.d(TAG, "NetworkCallback was already unregistered");
            } else {
                for (Map.Entry next : hashMap.entrySet()) {
                    if (next.getValue() == networkCallback) {
                        arrayList.add((NetworkRequest) next.getKey());
                    }
                }
                for (NetworkRequest networkRequest : arrayList) {
                    try {
                        this.mService.releaseNetworkRequest(networkRequest);
                        sCallbacks.remove(networkRequest);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
                networkCallback.networkRequest = ALREADY_UNREGISTERED;
            }
        }
    }

    public void unregisterNetworkCallback(PendingIntent pendingIntent) {
        releaseNetworkRequest(pendingIntent);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setAcceptUnvalidated(Network network, boolean z, boolean z2) {
        try {
            this.mService.setAcceptUnvalidated(network, z, z2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setAcceptPartialConnectivity(Network network, boolean z, boolean z2) {
        try {
            this.mService.setAcceptPartialConnectivity(network, z, z2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setAvoidUnvalidated(Network network) {
        try {
            this.mService.setAvoidUnvalidated(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTestAllowBadWifiUntil(long j) {
        try {
            this.mService.setTestAllowBadWifiUntil(j);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void startCaptivePortalApp(Network network) {
        try {
            this.mService.startCaptivePortalApp(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startCaptivePortalApp(Network network, Bundle bundle) {
        try {
            this.mService.startCaptivePortalAppInternal(network, bundle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean shouldAvoidBadWifi() {
        try {
            return this.mService.shouldAvoidBadWifi();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMultipathPreference(Network network) {
        try {
            return this.mService.getMultipathPreference(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void factoryReset() {
        try {
            this.mService.factoryReset();
            getTetheringManager().stopAllTethering();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean bindProcessToNetwork(Network network) {
        return setProcessDefaultNetwork(network);
    }

    @Deprecated
    public static boolean setProcessDefaultNetwork(Network network) {
        int i = network == null ? 0 : network.netId;
        boolean z = i == NetworkUtils.getBoundNetworkForProcess();
        if (i != 0) {
            i = network.getNetIdForResolv();
        }
        if (!NetworkUtils.bindProcessToNetwork(i)) {
            return false;
        }
        if (!z) {
            try {
                Proxy.setHttpProxyConfiguration(getInstance().getDefaultProxy());
            } catch (SecurityException e) {
                Log.e(TAG, "Can't set proxy properties", e);
            }
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().dispatchNetworkConfigurationChange();
        }
        return true;
    }

    public Network getBoundNetworkForProcess() {
        return getProcessDefaultNetwork();
    }

    @Deprecated
    public static Network getProcessDefaultNetwork() {
        int boundNetworkForProcess = NetworkUtils.getBoundNetworkForProcess();
        if (boundNetworkForProcess == 0) {
            return null;
        }
        return new Network(boundNetworkForProcess);
    }

    private void unsupportedStartingFrom(int i) {
        if (Process.myUid() != 1000 && this.mContext.getApplicationInfo().targetSdkVersion >= i) {
            throw new UnsupportedOperationException("This method is not supported in target SDK version " + i + " and above");
        }
    }

    private void checkLegacyRoutingApiAccess() {
        unsupportedStartingFrom(23);
    }

    @Deprecated
    public static boolean setProcessDefaultNetworkForHostResolution(Network network) {
        return NetworkUtils.bindProcessToNetworkForHostResolution(network == null ? 0 : network.getNetIdForResolv());
    }

    public int getRestrictBackgroundStatus() {
        try {
            return this.mService.getRestrictBackgroundStatusByCaller();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public byte[] getNetworkWatchlistConfigHash() {
        try {
            return this.mService.getNetworkWatchlistConfigHash();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get watchlist config hash");
            throw e.rethrowFromSystemServer();
        }
    }

    public int getConnectionOwnerUid(int i, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) {
        try {
            return this.mService.getConnectionOwnerUid(new ConnectionInfo(i, inetSocketAddress, inetSocketAddress2));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void printStackTrace() {
        String stackTraceElement;
        if (DEBUG) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StringBuffer stringBuffer = new StringBuffer();
            int i = 3;
            while (i < stackTrace.length && (stackTraceElement = stackTrace[i].toString()) != null && !stackTraceElement.contains("android.os")) {
                stringBuffer.append(" [").append(stackTraceElement).append(NavigationBarInflaterView.SIZE_MOD_END);
                i++;
            }
            Log.d(TAG, "StackLog:" + stringBuffer.toString());
        }
    }

    public TestNetworkManager startOrGetTestNetworkManager() {
        try {
            return new TestNetworkManager(ITestNetworkManager.Stub.asInterface(this.mService.startOrGetTestNetworkService()));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ConnectivityDiagnosticsManager createDiagnosticsManager() {
        return new ConnectivityDiagnosticsManager(this.mContext, this.mService);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void simulateDataStall(int i, long j, Network network, PersistableBundle persistableBundle) {
        try {
            this.mService.simulateDataStall(i, j, network, persistableBundle);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void registerQosCallback(QosSocketInfo qosSocketInfo, Executor executor, QosCallback qosCallback) {
        Objects.requireNonNull(qosSocketInfo, "socketInfo must be non-null");
        Objects.requireNonNull(executor, "executor must be non-null");
        Objects.requireNonNull(qosCallback, "callback must be non-null");
        try {
            synchronized (this.mQosCallbackConnections) {
                if (getQosCallbackConnection(qosCallback) == null) {
                    QosCallbackConnection qosCallbackConnection = new QosCallbackConnection(this, qosCallback, executor);
                    this.mQosCallbackConnections.add(qosCallbackConnection);
                    this.mService.registerQosSocketCallback(qosSocketInfo, qosCallbackConnection);
                } else {
                    Log.e(TAG, "registerQosCallback: Callback already registered");
                    throw new QosCallback.QosCallbackRegistrationException();
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "registerQosCallback: Error while registering ", e);
            unregisterQosCallback(qosCallback);
            e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            Log.e(TAG, "registerQosCallback: Error while registering ", e2);
            unregisterQosCallback(qosCallback);
            throw convertServiceException(e2);
        }
    }

    @SystemApi
    public void unregisterQosCallback(QosCallback qosCallback) {
        Objects.requireNonNull(qosCallback, "The callback must be non-null");
        try {
            synchronized (this.mQosCallbackConnections) {
                QosCallbackConnection qosCallbackConnection = getQosCallbackConnection(qosCallback);
                if (qosCallbackConnection != null) {
                    qosCallbackConnection.stopReceivingMessages();
                    this.mService.unregisterQosCallback(qosCallbackConnection);
                    this.mQosCallbackConnections.remove((Object) qosCallbackConnection);
                } else {
                    Log.d(TAG, "unregisterQosCallback: Callback not registered");
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterQosCallback: Error while unregistering ", e);
            e.rethrowFromSystemServer();
        }
    }

    private QosCallbackConnection getQosCallbackConnection(QosCallback qosCallback) {
        for (QosCallbackConnection next : this.mQosCallbackConnections) {
            if (next.getCallback() == qosCallback) {
                return next;
            }
        }
        return null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void requestBackgroundNetwork(NetworkRequest networkRequest, NetworkCallback networkCallback, Handler handler) {
        sendRequestForNetwork(networkRequest.networkCapabilities, networkCallback, 0, NetworkRequest.Type.BACKGROUND_REQUEST, -1, new CallbackHandler(this, handler));
    }

    @SystemApi
    public void setOemNetworkPreference(OemNetworkPreferences oemNetworkPreferences, final Executor executor, final Runnable runnable) {
        C00476 r3;
        Objects.requireNonNull(oemNetworkPreferences, "OemNetworkPreferences must be non-null");
        if (runnable != null) {
            Objects.requireNonNull(executor, "Executor must be non-null");
        }
        if (runnable == null) {
            r3 = null;
        } else {
            r3 = new IOnCompleteListener.Stub() {
                public void onComplete() {
                    Executor executor = executor;
                    Runnable runnable = runnable;
                    Objects.requireNonNull(runnable);
                    executor.execute(new ConnectivityManager$6$$ExternalSyntheticLambda0(runnable));
                }
            };
        }
        try {
            this.mService.setOemNetworkPreference(oemNetworkPreferences, r3);
        } catch (RemoteException e) {
            Log.e(TAG, "setOemNetworkPreference() failed for preference: " + oemNetworkPreferences.toString());
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public void setProfileNetworkPreference(UserHandle userHandle, int i, Executor executor, Runnable runnable) {
        ProfileNetworkPreference.Builder builder = new ProfileNetworkPreference.Builder();
        builder.setPreference(i);
        if (i != 0) {
            builder.setPreferenceEnterpriseId(1);
        }
        setProfileNetworkPreferences(userHandle, List.m1729of(builder.build()), executor, runnable);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setProfileNetworkPreferences(UserHandle userHandle, List<ProfileNetworkPreference> list, final Executor executor, final Runnable runnable) {
        C00487 r4;
        if (runnable != null) {
            Objects.requireNonNull(executor, "Pass a non-null executor, or a null listener");
        }
        if (runnable == null) {
            r4 = null;
        } else {
            r4 = new IOnCompleteListener.Stub() {
                public void onComplete() {
                    Executor executor = executor;
                    Runnable runnable = runnable;
                    Objects.requireNonNull(runnable);
                    executor.execute(new ConnectivityManager$6$$ExternalSyntheticLambda0(runnable));
                }
            };
        }
        try {
            this.mService.setProfileNetworkPreferences(userHandle, list, r4);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Range<Integer> getIpSecNetIdRange() {
        return new Range<>(Integer.valueOf((int) TUN_INTF_NETID_START), 65535);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addUidToMeteredNetworkAllowList(int i) {
        try {
            this.mService.updateMeteredNetworkAllowList(i, true);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void removeUidFromMeteredNetworkAllowList(int i) {
        try {
            this.mService.updateMeteredNetworkAllowList(i, false);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addUidToMeteredNetworkDenyList(int i) {
        try {
            this.mService.updateMeteredNetworkDenyList(i, true);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void removeUidFromMeteredNetworkDenyList(int i) {
        try {
            this.mService.updateMeteredNetworkDenyList(i, false);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setUidFirewallRule(int i, int i2, int i3) {
        try {
            this.mService.setUidFirewallRule(i, i2, i3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setFirewallChainEnabled(int i, boolean z) {
        try {
            this.mService.setFirewallChainEnabled(i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void replaceFirewallChain(int i, int[] iArr) {
        Objects.requireNonNull(iArr);
        try {
            this.mService.replaceFirewallChain(i, iArr);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
