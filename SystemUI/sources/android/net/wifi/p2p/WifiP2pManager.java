package android.net.wifi.p2p;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.MacAddress;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceResponse;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.CloseGuard;
import android.util.Log;
import android.view.Display;
import com.android.wifi.p018x.com.android.internal.util.AsyncChannel;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiP2pManager {
    @SystemApi
    public static final String ACTION_WIFI_P2P_PERSISTENT_GROUPS_CHANGED = "android.net.wifi.p2p.action.WIFI_P2P_PERSISTENT_GROUPS_CHANGED";
    public static final String ACTION_WIFI_P2P_REQUEST_RESPONSE_CHANGED = "android.net.wifi.p2p.action.WIFI_P2P_REQUEST_RESPONSE_CHANGED";
    public static final int ADD_EXTERNAL_APPROVER = 139366;
    public static final int ADD_LOCAL_SERVICE = 139292;
    public static final int ADD_LOCAL_SERVICE_FAILED = 139293;
    public static final int ADD_LOCAL_SERVICE_SUCCEEDED = 139294;
    public static final int ADD_SERVICE_REQUEST = 139301;
    public static final int ADD_SERVICE_REQUEST_FAILED = 139302;
    public static final int ADD_SERVICE_REQUEST_SUCCEEDED = 139303;
    private static final int BASE = 139264;
    public static final int BUSY = 2;
    public static final String CALLING_BINDER = "android.net.wifi.p2p.CALLING_BINDER";
    public static final String CALLING_FEATURE_ID = "android.net.wifi.p2p.CALLING_FEATURE_ID";
    public static final String CALLING_PACKAGE = "android.net.wifi.p2p.CALLING_PACKAGE";
    public static final int CANCEL_CONNECT = 139274;
    public static final int CANCEL_CONNECT_FAILED = 139275;
    public static final int CANCEL_CONNECT_SUCCEEDED = 139276;
    public static final int CLEAR_LOCAL_SERVICES = 139298;
    public static final int CLEAR_LOCAL_SERVICES_FAILED = 139299;
    public static final int CLEAR_LOCAL_SERVICES_SUCCEEDED = 139300;
    public static final int CLEAR_SERVICE_REQUESTS = 139307;
    public static final int CLEAR_SERVICE_REQUESTS_FAILED = 139308;
    public static final int CLEAR_SERVICE_REQUESTS_SUCCEEDED = 139309;
    public static final int CONNECT = 139271;
    public static final int CONNECTION_REQUEST_ACCEPT = 0;
    public static final int CONNECTION_REQUEST_DEFER_SHOW_PIN_TO_SERVICE = 3;
    public static final int CONNECTION_REQUEST_DEFER_TO_SERVICE = 2;
    public static final int CONNECTION_REQUEST_REJECT = 1;
    public static final int CONNECT_FAILED = 139272;
    public static final int CONNECT_SUCCEEDED = 139273;
    public static final int CREATE_GROUP = 139277;
    public static final int CREATE_GROUP_FAILED = 139278;
    public static final int CREATE_GROUP_SUCCEEDED = 139279;
    public static final int DELETE_PERSISTENT_GROUP = 139318;
    public static final int DELETE_PERSISTENT_GROUP_FAILED = 139319;
    public static final int DELETE_PERSISTENT_GROUP_SUCCEEDED = 139320;
    public static final int DISCOVER_PEERS = 139265;
    public static final int DISCOVER_PEERS_FAILED = 139266;
    public static final int DISCOVER_PEERS_SUCCEEDED = 139267;
    public static final int DISCOVER_SERVICES = 139310;
    public static final int DISCOVER_SERVICES_FAILED = 139311;
    public static final int DISCOVER_SERVICES_SUCCEEDED = 139312;
    public static final int ERROR = 0;
    public static final int EXTERNAL_APPROVER_ATTACH = 139367;
    public static final int EXTERNAL_APPROVER_CONNECTION_REQUESTED = 139369;
    public static final int EXTERNAL_APPROVER_DETACH = 139368;
    public static final int EXTERNAL_APPROVER_PIN_GENERATED = 139370;
    public static final String EXTRA_DISCOVERY_STATE = "discoveryState";
    public static final String EXTRA_HANDOVER_MESSAGE = "android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_P2P_DEVICE_LIST = "wifiP2pDeviceList";
    public static final String EXTRA_PARAM_KEY_CONFIG = "android.net.wifi.p2p.EXTRA_PARAM_KEY_CONFIG";
    public static final String EXTRA_PARAM_KEY_DEVICE = "android.net.wifi.p2p.EXTRA_PARAM_KEY_DEVICE";
    public static final String EXTRA_PARAM_KEY_DISPLAY_ID = "android.net.wifi.p2p.EXTRA_PARAM_KEY_DISPLAY_ID";
    public static final String EXTRA_PARAM_KEY_INFORMATION_ELEMENT_LIST = "android.net.wifi.p2p.EXTRA_PARAM_KEY_INFORMATION_ELEMENT_LIST";
    public static final String EXTRA_PARAM_KEY_INTERNAL_MESSAGE = "android.net.wifi.p2p.EXTRA_PARAM_KEY_INTERNAL_MESSAGE";
    public static final String EXTRA_PARAM_KEY_PEER_ADDRESS = "android.net.wifi.p2p.EXTRA_PARAM_KEY_PEER_ADDRESS";
    public static final String EXTRA_PARAM_KEY_PEER_DISCOVERY_FREQ = "android.net.wifi.p2p.EXTRA_PARAM_KEY_PEER_DISCOVERY_FREQ";
    public static final String EXTRA_PARAM_KEY_SERVICE_INFO = "android.net.wifi.p2p.EXTRA_PARAM_KEY_SERVICE_INFO";
    public static final String EXTRA_PARAM_KEY_WPS_PIN = "android.net.wifi.p2p.EXTRA_PARAM_KEY_WPS_PIN";
    public static final String EXTRA_REQUEST_CONFIG = "android.net.wifi.p2p.extra.REQUEST_CONFIG";
    public static final String EXTRA_REQUEST_RESPONSE = "android.net.wifi.p2p.extra.REQUEST_RESPONSE";
    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";
    public static final String EXTRA_WIFI_P2P_GROUP = "p2pGroupInfo";
    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";
    public static final int FACTORY_RESET = 139346;
    public static final int FACTORY_RESET_FAILED = 139347;
    public static final int FACTORY_RESET_SUCCEEDED = 139348;
    public static final long FEATURE_FLEXIBLE_DISCOVERY = 2;
    public static final long FEATURE_GROUP_CLIENT_REMOVAL = 4;
    public static final long FEATURE_SET_VENDOR_ELEMENTS = 1;
    public static final int GET_HANDOVER_REQUEST = 139339;
    public static final int GET_HANDOVER_SELECT = 139340;
    public static final int INITIATOR_REPORT_NFC_HANDOVER = 139342;
    @SystemApi
    public static final int MIRACAST_DISABLED = 0;
    @SystemApi
    public static final int MIRACAST_SINK = 2;
    @SystemApi
    public static final int MIRACAST_SOURCE = 1;
    public static final int NO_SERVICE_REQUESTS = 3;
    public static final int P2P_UNSUPPORTED = 1;
    public static final int PING = 139313;
    public static final int REMOVE_CLIENT = 139363;
    public static final int REMOVE_CLIENT_FAILED = 139364;
    public static final int REMOVE_CLIENT_SUCCEEDED = 139365;
    public static final int REMOVE_EXTERNAL_APPROVER = 139371;
    public static final int REMOVE_EXTERNAL_APPROVER_FAILED = 139372;
    public static final int REMOVE_EXTERNAL_APPROVER_SUCCEEDED = 139373;
    public static final int REMOVE_GROUP = 139280;
    public static final int REMOVE_GROUP_FAILED = 139281;
    public static final int REMOVE_GROUP_SUCCEEDED = 139282;
    public static final int REMOVE_LOCAL_SERVICE = 139295;
    public static final int REMOVE_LOCAL_SERVICE_FAILED = 139296;
    public static final int REMOVE_LOCAL_SERVICE_SUCCEEDED = 139297;
    public static final int REMOVE_SERVICE_REQUEST = 139304;
    public static final int REMOVE_SERVICE_REQUEST_FAILED = 139305;
    public static final int REMOVE_SERVICE_REQUEST_SUCCEEDED = 139306;
    public static final int REPORT_NFC_HANDOVER_FAILED = 139345;
    public static final int REPORT_NFC_HANDOVER_SUCCEEDED = 139344;
    public static final int REQUEST_CONNECTION_INFO = 139285;
    public static final int REQUEST_DEVICE_INFO = 139361;
    public static final int REQUEST_DISCOVERY_STATE = 139356;
    public static final int REQUEST_GROUP_INFO = 139287;
    public static final int REQUEST_NETWORK_INFO = 139358;
    public static final int REQUEST_ONGOING_PEER_CONFIG = 139349;
    public static final int REQUEST_P2P_STATE = 139354;
    public static final int REQUEST_PEERS = 139283;
    public static final int REQUEST_PERSISTENT_GROUP_INFO = 139321;
    public static final int RESPONDER_REPORT_NFC_HANDOVER = 139343;
    public static final int RESPONSE_CONNECTION_INFO = 139286;
    public static final int RESPONSE_DEVICE_INFO = 139362;
    public static final int RESPONSE_DISCOVERY_STATE = 139357;
    public static final int RESPONSE_GET_HANDOVER_MESSAGE = 139341;
    public static final int RESPONSE_GROUP_INFO = 139288;
    public static final int RESPONSE_NETWORK_INFO = 139359;
    public static final int RESPONSE_ONGOING_PEER_CONFIG = 139350;
    public static final int RESPONSE_P2P_STATE = 139355;
    public static final int RESPONSE_PEERS = 139284;
    public static final int RESPONSE_PERSISTENT_GROUP_INFO = 139322;
    public static final int RESPONSE_SERVICE = 139314;
    public static final int SET_CHANNEL = 139335;
    public static final int SET_CHANNEL_FAILED = 139336;
    public static final int SET_CHANNEL_SUCCEEDED = 139337;
    public static final int SET_CONNECTION_REQUEST_RESULT = 139374;
    public static final int SET_CONNECTION_REQUEST_RESULT_FAILED = 139375;
    public static final int SET_CONNECTION_REQUEST_RESULT_SUCCEEDED = 139376;
    public static final int SET_DEVICE_NAME = 139315;
    public static final int SET_DEVICE_NAME_FAILED = 139316;
    public static final int SET_DEVICE_NAME_SUCCEEDED = 139317;
    public static final int SET_ONGOING_PEER_CONFIG = 139351;
    public static final int SET_ONGOING_PEER_CONFIG_FAILED = 139352;
    public static final int SET_ONGOING_PEER_CONFIG_SUCCEEDED = 139353;
    public static final int SET_VENDOR_ELEMENTS = 139377;
    public static final int SET_VENDOR_ELEMENTS_FAILED = 139378;
    public static final int SET_VENDOR_ELEMENTS_SUCCEEDED = 139379;
    public static final int SET_WFDR2_INFO = 139380;
    public static final int SET_WFD_INFO = 139323;
    public static final int SET_WFD_INFO_FAILED = 139324;
    public static final int SET_WFD_INFO_SUCCEEDED = 139325;
    public static final int START_LISTEN = 139329;
    public static final int START_LISTEN_FAILED = 139330;
    public static final int START_LISTEN_SUCCEEDED = 139331;
    public static final int START_WPS = 139326;
    public static final int START_WPS_FAILED = 139327;
    public static final int START_WPS_SUCCEEDED = 139328;
    public static final int STOP_DISCOVERY = 139268;
    public static final int STOP_DISCOVERY_FAILED = 139269;
    public static final int STOP_DISCOVERY_SUCCEEDED = 139270;
    public static final int STOP_LISTEN = 139332;
    public static final int STOP_LISTEN_FAILED = 139333;
    public static final int STOP_LISTEN_SUCCEEDED = 139334;
    private static final String TAG = "WifiP2pManager";
    public static final int UPDATE_CHANNEL_INFO = 139360;
    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION = "android.net.wifi.p2p.CONNECTION_STATE_CHANGE";
    public static final String WIFI_P2P_DISCOVERY_CHANGED_ACTION = "android.net.wifi.p2p.DISCOVERY_STATE_CHANGE";
    public static final int WIFI_P2P_DISCOVERY_STARTED = 2;
    public static final int WIFI_P2P_DISCOVERY_STOPPED = 1;
    public static final String WIFI_P2P_PEERS_CHANGED_ACTION = "android.net.wifi.p2p.PEERS_CHANGED";
    public static final int WIFI_P2P_SCAN_FREQ_UNSPECIFIED = 0;
    public static final int WIFI_P2P_SCAN_FULL = 0;
    public static final int WIFI_P2P_SCAN_SINGLE_FREQ = 2;
    public static final int WIFI_P2P_SCAN_SOCIAL = 1;
    public static final String WIFI_P2P_STATE_CHANGED_ACTION = "android.net.wifi.p2p.STATE_CHANGED";
    public static final int WIFI_P2P_STATE_DISABLED = 1;
    public static final int WIFI_P2P_STATE_ENABLED = 2;
    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION = "android.net.wifi.p2p.THIS_DEVICE_CHANGED";
    private static final int WIFI_P2P_VENDOR_ELEMENTS_MAXIMUM_LENGTH = 512;
    IWifiP2pManager mService;

    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    public interface ChannelListener {
        void onChannelDisconnected();
    }

    public interface ConnectionInfoListener {
        void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectionRequestResponse {
    }

    public interface DeviceInfoListener {
        void onDeviceInfoAvailable(WifiP2pDevice wifiP2pDevice);
    }

    public interface DiscoveryStateListener {
        void onDiscoveryStateAvailable(int i);
    }

    public interface DnsSdServiceResponseListener {
        void onDnsSdServiceAvailable(String str, String str2, WifiP2pDevice wifiP2pDevice);
    }

    public interface DnsSdTxtRecordListener {
        void onDnsSdTxtRecordAvailable(String str, Map<String, String> map, WifiP2pDevice wifiP2pDevice);
    }

    public interface ExternalApproverRequestListener {
        public static final int APPROVER_DETACH_REASON_CLOSE = 3;
        public static final int APPROVER_DETACH_REASON_FAILURE = 1;
        public static final int APPROVER_DETACH_REASON_REMOVE = 0;
        public static final int APPROVER_DETACH_REASON_REPLACE = 2;
        public static final int REQUEST_TYPE_INVITATION = 1;
        public static final int REQUEST_TYPE_JOIN = 2;
        public static final int REQUEST_TYPE_NEGOTIATION = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ApproverDetachReason {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface RequestType {
        }

        void onAttached(MacAddress macAddress);

        void onConnectionRequested(int i, WifiP2pConfig wifiP2pConfig, WifiP2pDevice wifiP2pDevice);

        void onDetached(MacAddress macAddress, int i);

        void onPinGenerated(MacAddress macAddress, String str);
    }

    public interface GroupInfoListener {
        void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup);
    }

    public interface HandoverMessageListener {
        void onHandoverMessageAvailable(String str);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MiracastMode {
    }

    public interface NetworkInfoListener {
        void onNetworkInfoAvailable(NetworkInfo networkInfo);
    }

    public interface OngoingPeerInfoListener {
        void onOngoingPeerAvailable(WifiP2pConfig wifiP2pConfig);
    }

    public interface P2pStateListener {
        void onP2pStateAvailable(int i);
    }

    public interface PeerListListener {
        void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList);
    }

    @SystemApi
    public interface PersistentGroupInfoListener {
        void onPersistentGroupInfoAvailable(WifiP2pGroupList wifiP2pGroupList);
    }

    public interface ServiceResponseListener {
        void onServiceAvailable(int i, byte[] bArr, WifiP2pDevice wifiP2pDevice);
    }

    public interface UpnpServiceResponseListener {
        void onUpnpServiceAvailable(List<String> list, WifiP2pDevice wifiP2pDevice);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiP2pDiscoveryState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiP2pScanType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiP2pState {
    }

    public static int getP2pMaxAllowedVendorElementsLengthBytes() {
        return 512;
    }

    public WifiP2pManager(IWifiP2pManager iWifiP2pManager) {
        this.mService = iWifiP2pManager;
    }

    public static class Channel implements AutoCloseable {
        private static final int INVALID_LISTENER_KEY = 0;
        /* access modifiers changed from: private */
        public AsyncChannel mAsyncChannel;
        final Binder mBinder;
        /* access modifiers changed from: private */
        public ChannelListener mChannelListener;
        private final CloseGuard mCloseGuard;
        Context mContext;
        /* access modifiers changed from: private */
        public DnsSdServiceResponseListener mDnsSdServRspListener;
        /* access modifiers changed from: private */
        public DnsSdTxtRecordListener mDnsSdTxtListener;
        /* access modifiers changed from: private */
        public P2pHandler mHandler;
        private int mListenerKey = 0;
        private HashMap<Integer, Object> mListenerMap = new HashMap<>();
        private final Object mListenerMapLock = new Object();
        private final WifiP2pManager mP2pManager;
        /* access modifiers changed from: private */
        public ServiceResponseListener mServRspListener;
        /* access modifiers changed from: private */
        public UpnpServiceResponseListener mUpnpServRspListener;

        public Channel(Context context, Looper looper, ChannelListener channelListener, Binder binder, WifiP2pManager wifiP2pManager) {
            CloseGuard closeGuard = new CloseGuard();
            this.mCloseGuard = closeGuard;
            this.mAsyncChannel = new AsyncChannel();
            this.mHandler = new P2pHandler(looper);
            this.mChannelListener = channelListener;
            this.mContext = context;
            this.mBinder = binder;
            this.mP2pManager = wifiP2pManager;
            closeGuard.open("close");
        }

        public Binder getBinder() {
            return this.mBinder;
        }

        public void close() {
            WifiP2pManager wifiP2pManager = this.mP2pManager;
            if (wifiP2pManager == null) {
                Log.w(WifiP2pManager.TAG, "Channel.close(): Null mP2pManager!?");
            } else {
                try {
                    wifiP2pManager.mService.close(this.mBinder);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mAsyncChannel.disconnect();
            this.mCloseGuard.close();
            Reference.reachabilityFence(this);
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            try {
                CloseGuard closeGuard = this.mCloseGuard;
                if (closeGuard != null) {
                    closeGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }

        class P2pHandler extends Handler {
            P2pHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message message) {
                Object obj;
                switch (message.what) {
                    case WifiP2pManager.EXTERNAL_APPROVER_ATTACH /*139367*/:
                    case WifiP2pManager.EXTERNAL_APPROVER_CONNECTION_REQUESTED /*139369*/:
                    case WifiP2pManager.EXTERNAL_APPROVER_PIN_GENERATED /*139370*/:
                        obj = Channel.this.getListener(message.arg2);
                        break;
                    default:
                        obj = Channel.this.removeListener(message.arg2);
                        break;
                }
                String str = null;
                switch (message.what) {
                    case AsyncChannel.CMD_CHANNEL_DISCONNECTED /*69636*/:
                        if (Channel.this.mChannelListener != null) {
                            Channel.this.mChannelListener.onChannelDisconnected();
                            Channel.this.mChannelListener = null;
                            return;
                        }
                        return;
                    case WifiP2pManager.DISCOVER_PEERS_FAILED /*139266*/:
                    case WifiP2pManager.STOP_DISCOVERY_FAILED /*139269*/:
                    case WifiP2pManager.CONNECT_FAILED /*139272*/:
                    case WifiP2pManager.CANCEL_CONNECT_FAILED /*139275*/:
                    case WifiP2pManager.CREATE_GROUP_FAILED /*139278*/:
                    case WifiP2pManager.REMOVE_GROUP_FAILED /*139281*/:
                    case WifiP2pManager.ADD_LOCAL_SERVICE_FAILED /*139293*/:
                    case WifiP2pManager.REMOVE_LOCAL_SERVICE_FAILED /*139296*/:
                    case WifiP2pManager.CLEAR_LOCAL_SERVICES_FAILED /*139299*/:
                    case WifiP2pManager.ADD_SERVICE_REQUEST_FAILED /*139302*/:
                    case WifiP2pManager.REMOVE_SERVICE_REQUEST_FAILED /*139305*/:
                    case WifiP2pManager.CLEAR_SERVICE_REQUESTS_FAILED /*139308*/:
                    case WifiP2pManager.DISCOVER_SERVICES_FAILED /*139311*/:
                    case WifiP2pManager.SET_DEVICE_NAME_FAILED /*139316*/:
                    case WifiP2pManager.DELETE_PERSISTENT_GROUP_FAILED /*139319*/:
                    case WifiP2pManager.SET_WFD_INFO_FAILED /*139324*/:
                    case WifiP2pManager.START_WPS_FAILED /*139327*/:
                    case WifiP2pManager.START_LISTEN_FAILED /*139330*/:
                    case WifiP2pManager.STOP_LISTEN_FAILED /*139333*/:
                    case WifiP2pManager.SET_CHANNEL_FAILED /*139336*/:
                    case WifiP2pManager.REPORT_NFC_HANDOVER_FAILED /*139345*/:
                    case WifiP2pManager.FACTORY_RESET_FAILED /*139347*/:
                    case WifiP2pManager.SET_ONGOING_PEER_CONFIG_FAILED /*139352*/:
                    case WifiP2pManager.REMOVE_CLIENT_FAILED /*139364*/:
                    case WifiP2pManager.REMOVE_EXTERNAL_APPROVER_FAILED /*139372*/:
                    case WifiP2pManager.SET_CONNECTION_REQUEST_RESULT_FAILED /*139375*/:
                    case WifiP2pManager.SET_VENDOR_ELEMENTS_FAILED /*139378*/:
                        if (obj != null) {
                            ((ActionListener) obj).onFailure(message.arg1);
                            return;
                        }
                        return;
                    case WifiP2pManager.DISCOVER_PEERS_SUCCEEDED /*139267*/:
                    case WifiP2pManager.STOP_DISCOVERY_SUCCEEDED /*139270*/:
                    case WifiP2pManager.CONNECT_SUCCEEDED /*139273*/:
                    case WifiP2pManager.CANCEL_CONNECT_SUCCEEDED /*139276*/:
                    case WifiP2pManager.CREATE_GROUP_SUCCEEDED /*139279*/:
                    case WifiP2pManager.REMOVE_GROUP_SUCCEEDED /*139282*/:
                    case WifiP2pManager.ADD_LOCAL_SERVICE_SUCCEEDED /*139294*/:
                    case WifiP2pManager.REMOVE_LOCAL_SERVICE_SUCCEEDED /*139297*/:
                    case WifiP2pManager.CLEAR_LOCAL_SERVICES_SUCCEEDED /*139300*/:
                    case WifiP2pManager.ADD_SERVICE_REQUEST_SUCCEEDED /*139303*/:
                    case WifiP2pManager.REMOVE_SERVICE_REQUEST_SUCCEEDED /*139306*/:
                    case WifiP2pManager.CLEAR_SERVICE_REQUESTS_SUCCEEDED /*139309*/:
                    case WifiP2pManager.DISCOVER_SERVICES_SUCCEEDED /*139312*/:
                    case WifiP2pManager.SET_DEVICE_NAME_SUCCEEDED /*139317*/:
                    case WifiP2pManager.DELETE_PERSISTENT_GROUP_SUCCEEDED /*139320*/:
                    case WifiP2pManager.SET_WFD_INFO_SUCCEEDED /*139325*/:
                    case WifiP2pManager.START_WPS_SUCCEEDED /*139328*/:
                    case WifiP2pManager.START_LISTEN_SUCCEEDED /*139331*/:
                    case WifiP2pManager.STOP_LISTEN_SUCCEEDED /*139334*/:
                    case WifiP2pManager.SET_CHANNEL_SUCCEEDED /*139337*/:
                    case WifiP2pManager.REPORT_NFC_HANDOVER_SUCCEEDED /*139344*/:
                    case WifiP2pManager.FACTORY_RESET_SUCCEEDED /*139348*/:
                    case WifiP2pManager.SET_ONGOING_PEER_CONFIG_SUCCEEDED /*139353*/:
                    case WifiP2pManager.REMOVE_CLIENT_SUCCEEDED /*139365*/:
                    case WifiP2pManager.REMOVE_EXTERNAL_APPROVER_SUCCEEDED /*139373*/:
                    case WifiP2pManager.SET_CONNECTION_REQUEST_RESULT_SUCCEEDED /*139376*/:
                    case WifiP2pManager.SET_VENDOR_ELEMENTS_SUCCEEDED /*139379*/:
                        if (obj != null) {
                            ((ActionListener) obj).onSuccess();
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_PEERS /*139284*/:
                        WifiP2pDeviceList wifiP2pDeviceList = (WifiP2pDeviceList) message.obj;
                        if (obj != null) {
                            ((PeerListListener) obj).onPeersAvailable(wifiP2pDeviceList);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_CONNECTION_INFO /*139286*/:
                        WifiP2pInfo wifiP2pInfo = (WifiP2pInfo) message.obj;
                        if (obj != null) {
                            ((ConnectionInfoListener) obj).onConnectionInfoAvailable(wifiP2pInfo);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_GROUP_INFO /*139288*/:
                        WifiP2pGroup wifiP2pGroup = (WifiP2pGroup) message.obj;
                        if (obj != null) {
                            ((GroupInfoListener) obj).onGroupInfoAvailable(wifiP2pGroup);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_SERVICE /*139314*/:
                        Channel.this.handleServiceResponse((WifiP2pServiceResponse) message.obj);
                        return;
                    case WifiP2pManager.RESPONSE_PERSISTENT_GROUP_INFO /*139322*/:
                        WifiP2pGroupList wifiP2pGroupList = (WifiP2pGroupList) message.obj;
                        if (obj != null) {
                            ((PersistentGroupInfoListener) obj).onPersistentGroupInfoAvailable(wifiP2pGroupList);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_GET_HANDOVER_MESSAGE /*139341*/:
                        Bundle bundle = (Bundle) message.obj;
                        if (obj != null) {
                            if (bundle != null) {
                                str = bundle.getString(WifiP2pManager.EXTRA_HANDOVER_MESSAGE);
                            }
                            ((HandoverMessageListener) obj).onHandoverMessageAvailable(str);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_ONGOING_PEER_CONFIG /*139350*/:
                        WifiP2pConfig wifiP2pConfig = (WifiP2pConfig) message.obj;
                        if (obj != null) {
                            ((OngoingPeerInfoListener) obj).onOngoingPeerAvailable(wifiP2pConfig);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_P2P_STATE /*139355*/:
                        if (obj != null) {
                            ((P2pStateListener) obj).onP2pStateAvailable(message.arg1);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_DISCOVERY_STATE /*139357*/:
                        if (obj != null) {
                            ((DiscoveryStateListener) obj).onDiscoveryStateAvailable(message.arg1);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_NETWORK_INFO /*139359*/:
                        if (obj != null) {
                            ((NetworkInfoListener) obj).onNetworkInfoAvailable((NetworkInfo) message.obj);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_DEVICE_INFO /*139362*/:
                        if (obj != null) {
                            ((DeviceInfoListener) obj).onDeviceInfoAvailable((WifiP2pDevice) message.obj);
                            return;
                        }
                        return;
                    case WifiP2pManager.EXTERNAL_APPROVER_ATTACH /*139367*/:
                        if (obj != null) {
                            ((ExternalApproverRequestListener) obj).onAttached((MacAddress) message.obj);
                            return;
                        }
                        return;
                    case WifiP2pManager.EXTERNAL_APPROVER_DETACH /*139368*/:
                        if (obj != null) {
                            ((ExternalApproverRequestListener) obj).onDetached((MacAddress) message.obj, message.arg1);
                            return;
                        }
                        return;
                    case WifiP2pManager.EXTERNAL_APPROVER_CONNECTION_REQUESTED /*139369*/:
                        if (obj != null) {
                            int i = message.arg1;
                            Bundle bundle2 = (Bundle) message.obj;
                            ExternalApproverRequestListener externalApproverRequestListener = (ExternalApproverRequestListener) obj;
                            externalApproverRequestListener.onConnectionRequested(i, (WifiP2pConfig) bundle2.getParcelable(WifiP2pManager.EXTRA_PARAM_KEY_CONFIG), (WifiP2pDevice) bundle2.getParcelable(WifiP2pManager.EXTRA_PARAM_KEY_DEVICE));
                            return;
                        }
                        return;
                    case WifiP2pManager.EXTERNAL_APPROVER_PIN_GENERATED /*139370*/:
                        if (obj != null) {
                            Bundle bundle3 = (Bundle) message.obj;
                            ((ExternalApproverRequestListener) obj).onPinGenerated((MacAddress) bundle3.getParcelable(WifiP2pManager.EXTRA_PARAM_KEY_PEER_ADDRESS), bundle3.getString(WifiP2pManager.EXTRA_PARAM_KEY_WPS_PIN));
                            return;
                        }
                        return;
                    default:
                        Log.d(WifiP2pManager.TAG, "Ignored " + message);
                        return;
                }
            }
        }

        /* access modifiers changed from: private */
        public void handleServiceResponse(WifiP2pServiceResponse wifiP2pServiceResponse) {
            if (wifiP2pServiceResponse instanceof WifiP2pDnsSdServiceResponse) {
                handleDnsSdServiceResponse((WifiP2pDnsSdServiceResponse) wifiP2pServiceResponse);
            } else if (!(wifiP2pServiceResponse instanceof WifiP2pUpnpServiceResponse)) {
                ServiceResponseListener serviceResponseListener = this.mServRspListener;
                if (serviceResponseListener != null) {
                    serviceResponseListener.onServiceAvailable(wifiP2pServiceResponse.getServiceType(), wifiP2pServiceResponse.getRawData(), wifiP2pServiceResponse.getSrcDevice());
                }
            } else if (this.mUpnpServRspListener != null) {
                handleUpnpServiceResponse((WifiP2pUpnpServiceResponse) wifiP2pServiceResponse);
            }
        }

        private void handleUpnpServiceResponse(WifiP2pUpnpServiceResponse wifiP2pUpnpServiceResponse) {
            this.mUpnpServRspListener.onUpnpServiceAvailable(wifiP2pUpnpServiceResponse.getUniqueServiceNames(), wifiP2pUpnpServiceResponse.getSrcDevice());
        }

        private void handleDnsSdServiceResponse(WifiP2pDnsSdServiceResponse wifiP2pDnsSdServiceResponse) {
            if (wifiP2pDnsSdServiceResponse.getDnsType() == 12) {
                DnsSdServiceResponseListener dnsSdServiceResponseListener = this.mDnsSdServRspListener;
                if (dnsSdServiceResponseListener != null) {
                    dnsSdServiceResponseListener.onDnsSdServiceAvailable(wifiP2pDnsSdServiceResponse.getInstanceName(), wifiP2pDnsSdServiceResponse.getDnsQueryName(), wifiP2pDnsSdServiceResponse.getSrcDevice());
                }
            } else if (wifiP2pDnsSdServiceResponse.getDnsType() == 16) {
                DnsSdTxtRecordListener dnsSdTxtRecordListener = this.mDnsSdTxtListener;
                if (dnsSdTxtRecordListener != null) {
                    dnsSdTxtRecordListener.onDnsSdTxtRecordAvailable(wifiP2pDnsSdServiceResponse.getDnsQueryName(), wifiP2pDnsSdServiceResponse.getTxtRecord(), wifiP2pDnsSdServiceResponse.getSrcDevice());
                }
            } else {
                Log.e(WifiP2pManager.TAG, "Unhandled resp " + wifiP2pDnsSdServiceResponse);
            }
        }

        /* access modifiers changed from: private */
        public int putListener(Object obj) {
            int i;
            if (obj == null) {
                return 0;
            }
            synchronized (this.mListenerMapLock) {
                do {
                    i = this.mListenerKey;
                    this.mListenerKey = i + 1;
                } while (i == 0);
                this.mListenerMap.put(Integer.valueOf(i), obj);
            }
            return i;
        }

        /* access modifiers changed from: private */
        public Object getListener(int i) {
            Object obj;
            if (i == 0) {
                return null;
            }
            synchronized (this.mListenerMapLock) {
                obj = this.mListenerMap.get(Integer.valueOf(i));
            }
            return obj;
        }

        /* access modifiers changed from: private */
        public Object removeListener(int i) {
            Object remove;
            if (i == 0) {
                return null;
            }
            synchronized (this.mListenerMapLock) {
                remove = this.mListenerMap.remove(Integer.valueOf(i));
            }
            return remove;
        }
    }

    private static void checkChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel needs to be initialized");
        }
    }

    private static void checkServiceInfo(WifiP2pServiceInfo wifiP2pServiceInfo) {
        if (wifiP2pServiceInfo == null) {
            throw new IllegalArgumentException("service info is null");
        }
    }

    private static void checkServiceRequest(WifiP2pServiceRequest wifiP2pServiceRequest) {
        if (wifiP2pServiceRequest == null) {
            throw new IllegalArgumentException("service request is null");
        }
    }

    private static void checkP2pConfig(WifiP2pConfig wifiP2pConfig) {
        if (wifiP2pConfig == null) {
            throw new IllegalArgumentException("config cannot be null");
        } else if (TextUtils.isEmpty(wifiP2pConfig.deviceAddress)) {
            throw new IllegalArgumentException("deviceAddress cannot be empty");
        }
    }

    public Channel initialize(Context context, Looper looper, ChannelListener channelListener) {
        Binder binder = new Binder();
        Bundle prepareExtrasBundle = prepareExtrasBundle(context);
        int i = 0;
        try {
            Display display = context.getDisplay();
            if (display != null) {
                i = display.getDisplayId();
            }
        } catch (UnsupportedOperationException unused) {
        }
        prepareExtrasBundle.putInt(EXTRA_PARAM_KEY_DISPLAY_ID, i);
        return initializeChannel(context, looper, channelListener, getMessenger(binder, context.getOpPackageName(), prepareExtrasBundle), binder);
    }

    public Channel initializeInternal(Context context, Looper looper, ChannelListener channelListener) {
        return initializeChannel(context, looper, channelListener, getP2pStateMachineMessenger(), (Binder) null);
    }

    private Bundle prepareExtrasBundle(Channel channel) {
        Bundle prepareExtrasBundle = prepareExtrasBundle(channel.mContext);
        prepareExtrasBundle.putBinder(CALLING_BINDER, channel.getBinder());
        return prepareExtrasBundle;
    }

    private Bundle prepareExtrasBundle(Context context) {
        Bundle bundle = new Bundle();
        if (SdkLevel.isAtLeastS()) {
            bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, context.getAttributionSource());
        }
        return bundle;
    }

    private Channel initializeChannel(Context context, Looper looper, ChannelListener channelListener, Messenger messenger, Binder binder) {
        if (messenger == null) {
            return null;
        }
        Channel channel = new Channel(context, looper, channelListener, binder, this);
        if (channel.mAsyncChannel.connectSync(context, (Handler) channel.mHandler, messenger) == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(CALLING_PACKAGE, channel.mContext.getOpPackageName());
            bundle.putString(CALLING_FEATURE_ID, channel.mContext.getAttributionTag());
            bundle.putBinder(CALLING_BINDER, binder);
            if (SdkLevel.isAtLeastT()) {
                bundle.putParcelable(WifiManager.EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, channel.mContext.getAttributionSource());
            }
            channel.mAsyncChannel.sendMessage(UPDATE_CHANNEL_INFO, 0, channel.putListener((Object) null), bundle);
            return channel;
        }
        channel.close();
        return null;
    }

    public void discoverPeers(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(DISCOVER_PEERS, 0, channel.putListener(actionListener), prepareExtrasBundle(channel));
    }

    public void discoverPeersOnSocialChannels(Channel channel, ActionListener actionListener) {
        if (isChannelConstrainedDiscoverySupported()) {
            checkChannel(channel);
            channel.mAsyncChannel.sendMessage(DISCOVER_PEERS, 1, channel.putListener(actionListener), prepareExtrasBundle(channel));
            return;
        }
        throw new UnsupportedOperationException();
    }

    public void discoverPeersOnSpecificFrequency(Channel channel, int i, ActionListener actionListener) {
        if (isChannelConstrainedDiscoverySupported()) {
            checkChannel(channel);
            if (i > 0) {
                Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
                prepareExtrasBundle.putInt(EXTRA_PARAM_KEY_PEER_DISCOVERY_FREQ, i);
                channel.mAsyncChannel.sendMessage(DISCOVER_PEERS, 2, channel.putListener(actionListener), prepareExtrasBundle);
                return;
            }
            throw new IllegalArgumentException("This frequency must be a positive value.");
        }
        throw new UnsupportedOperationException();
    }

    public void stopPeerDiscovery(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(STOP_DISCOVERY, 0, channel.putListener(actionListener));
    }

    public void connect(Channel channel, WifiP2pConfig wifiP2pConfig, ActionListener actionListener) {
        checkChannel(channel);
        checkP2pConfig(wifiP2pConfig);
        Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
        prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_CONFIG, wifiP2pConfig);
        channel.mAsyncChannel.sendMessage(CONNECT, 0, channel.putListener(actionListener), prepareExtrasBundle);
    }

    public void cancelConnect(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(CANCEL_CONNECT, 0, channel.putListener(actionListener));
    }

    public void createGroup(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(CREATE_GROUP, -2, channel.putListener(actionListener), prepareExtrasBundle(channel));
    }

    public void createGroup(Channel channel, WifiP2pConfig wifiP2pConfig, ActionListener actionListener) {
        checkChannel(channel);
        Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
        prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_CONFIG, wifiP2pConfig);
        channel.mAsyncChannel.sendMessage(CREATE_GROUP, 0, channel.putListener(actionListener), prepareExtrasBundle);
    }

    public void removeGroup(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REMOVE_GROUP, 0, channel.putListener(actionListener));
    }

    public void startListening(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(START_LISTEN, 0, channel.putListener(actionListener), prepareExtrasBundle(channel));
    }

    public void stopListening(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(STOP_LISTEN, 0, channel.putListener(actionListener));
    }

    @SystemApi
    public void setWifiP2pChannels(Channel channel, int i, int i2, ActionListener actionListener) {
        checkChannel(channel);
        Bundle bundle = new Bundle();
        bundle.putInt("lc", i);
        bundle.putInt("oc", i2);
        channel.mAsyncChannel.sendMessage(SET_CHANNEL, 0, channel.putListener(actionListener), bundle);
    }

    public void startWps(Channel channel, WpsInfo wpsInfo, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(START_WPS, 0, channel.putListener(actionListener), wpsInfo);
    }

    public void addLocalService(Channel channel, WifiP2pServiceInfo wifiP2pServiceInfo, ActionListener actionListener) {
        checkChannel(channel);
        checkServiceInfo(wifiP2pServiceInfo);
        Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
        prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_SERVICE_INFO, wifiP2pServiceInfo);
        channel.mAsyncChannel.sendMessage(ADD_LOCAL_SERVICE, 0, channel.putListener(actionListener), prepareExtrasBundle);
    }

    public void removeLocalService(Channel channel, WifiP2pServiceInfo wifiP2pServiceInfo, ActionListener actionListener) {
        checkChannel(channel);
        checkServiceInfo(wifiP2pServiceInfo);
        channel.mAsyncChannel.sendMessage(REMOVE_LOCAL_SERVICE, 0, channel.putListener(actionListener), wifiP2pServiceInfo);
    }

    public void clearLocalServices(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(CLEAR_LOCAL_SERVICES, 0, channel.putListener(actionListener));
    }

    public void setServiceResponseListener(Channel channel, ServiceResponseListener serviceResponseListener) {
        checkChannel(channel);
        channel.mServRspListener = serviceResponseListener;
    }

    public void setDnsSdResponseListeners(Channel channel, DnsSdServiceResponseListener dnsSdServiceResponseListener, DnsSdTxtRecordListener dnsSdTxtRecordListener) {
        checkChannel(channel);
        channel.mDnsSdServRspListener = dnsSdServiceResponseListener;
        channel.mDnsSdTxtListener = dnsSdTxtRecordListener;
    }

    public void setUpnpServiceResponseListener(Channel channel, UpnpServiceResponseListener upnpServiceResponseListener) {
        checkChannel(channel);
        channel.mUpnpServRspListener = upnpServiceResponseListener;
    }

    public void discoverServices(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(DISCOVER_SERVICES, 0, channel.putListener(actionListener), prepareExtrasBundle(channel));
    }

    public void addServiceRequest(Channel channel, WifiP2pServiceRequest wifiP2pServiceRequest, ActionListener actionListener) {
        checkChannel(channel);
        checkServiceRequest(wifiP2pServiceRequest);
        channel.mAsyncChannel.sendMessage(ADD_SERVICE_REQUEST, 0, channel.putListener(actionListener), wifiP2pServiceRequest);
    }

    public void removeServiceRequest(Channel channel, WifiP2pServiceRequest wifiP2pServiceRequest, ActionListener actionListener) {
        checkChannel(channel);
        checkServiceRequest(wifiP2pServiceRequest);
        channel.mAsyncChannel.sendMessage(REMOVE_SERVICE_REQUEST, 0, channel.putListener(actionListener), wifiP2pServiceRequest);
    }

    public void clearServiceRequests(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(CLEAR_SERVICE_REQUESTS, 0, channel.putListener(actionListener));
    }

    public void requestPeers(Channel channel, PeerListListener peerListListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REQUEST_PEERS, 0, channel.putListener(peerListListener), prepareExtrasBundle(channel));
    }

    public void requestConnectionInfo(Channel channel, ConnectionInfoListener connectionInfoListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REQUEST_CONNECTION_INFO, 0, channel.putListener(connectionInfoListener));
    }

    public void requestGroupInfo(Channel channel, GroupInfoListener groupInfoListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REQUEST_GROUP_INFO, 0, channel.putListener(groupInfoListener), prepareExtrasBundle(channel));
    }

    @SystemApi
    public void setDeviceName(Channel channel, String str, ActionListener actionListener) {
        checkChannel(channel);
        WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();
        wifiP2pDevice.deviceName = str;
        channel.mAsyncChannel.sendMessage(SET_DEVICE_NAME, 0, channel.putListener(actionListener), wifiP2pDevice);
    }

    @SystemApi
    public void setWfdInfo(Channel channel, WifiP2pWfdInfo wifiP2pWfdInfo, ActionListener actionListener) {
        setWFDInfo(channel, wifiP2pWfdInfo, actionListener);
    }

    public void setWFDInfo(Channel channel, WifiP2pWfdInfo wifiP2pWfdInfo, ActionListener actionListener) {
        checkChannel(channel);
        try {
            this.mService.checkConfigureWifiDisplayPermission();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        channel.mAsyncChannel.sendMessage(SET_WFD_INFO, 0, channel.putListener(actionListener), wifiP2pWfdInfo);
    }

    public void removeClient(Channel channel, MacAddress macAddress, ActionListener actionListener) {
        if (isGroupClientRemovalSupported()) {
            checkChannel(channel);
            channel.mAsyncChannel.sendMessage(REMOVE_CLIENT, 0, channel.putListener(actionListener), macAddress);
            return;
        }
        throw new UnsupportedOperationException();
    }

    public void setWFDR2Info(Channel channel, WifiP2pWfdInfo wifiP2pWfdInfo, ActionListener actionListener) {
        checkChannel(channel);
        try {
            this.mService.checkConfigureWifiDisplayPermission();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        channel.mAsyncChannel.sendMessage(SET_WFDR2_INFO, 0, channel.putListener(actionListener), wifiP2pWfdInfo);
    }

    @SystemApi
    public void deletePersistentGroup(Channel channel, int i, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(DELETE_PERSISTENT_GROUP, i, channel.putListener(actionListener));
    }

    @SystemApi
    public void requestPersistentGroupInfo(Channel channel, PersistentGroupInfoListener persistentGroupInfoListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REQUEST_PERSISTENT_GROUP_INFO, 0, channel.putListener(persistentGroupInfoListener), prepareExtrasBundle(channel));
    }

    @SystemApi
    public void setMiracastMode(int i) {
        try {
            this.mService.setMiracastMode(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private Messenger getMessenger(Binder binder, String str, Bundle bundle) {
        try {
            return this.mService.getMessenger(binder, str, bundle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Messenger getP2pStateMachineMessenger() {
        try {
            return this.mService.getP2pStateMachineMessenger();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private long getSupportedFeatures() {
        try {
            return this.mService.getSupportedFeatures();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private boolean isFeatureSupported(long j) {
        return (getSupportedFeatures() & j) == j;
    }

    public boolean isSetVendorElementsSupported() {
        return isFeatureSupported(1);
    }

    public boolean isChannelConstrainedDiscoverySupported() {
        return isFeatureSupported(2);
    }

    public boolean isGroupClientRemovalSupported() {
        return isFeatureSupported(4);
    }

    public void getNfcHandoverRequest(Channel channel, HandoverMessageListener handoverMessageListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(GET_HANDOVER_REQUEST, 0, channel.putListener(handoverMessageListener));
    }

    public void getNfcHandoverSelect(Channel channel, HandoverMessageListener handoverMessageListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(GET_HANDOVER_SELECT, 0, channel.putListener(handoverMessageListener));
    }

    public void initiatorReportNfcHandover(Channel channel, String str, ActionListener actionListener) {
        checkChannel(channel);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HANDOVER_MESSAGE, str);
        channel.mAsyncChannel.sendMessage(INITIATOR_REPORT_NFC_HANDOVER, 0, channel.putListener(actionListener), bundle);
    }

    public void responderReportNfcHandover(Channel channel, String str, ActionListener actionListener) {
        checkChannel(channel);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HANDOVER_MESSAGE, str);
        channel.mAsyncChannel.sendMessage(RESPONDER_REPORT_NFC_HANDOVER, 0, channel.putListener(actionListener), bundle);
    }

    @SystemApi
    public void factoryReset(Channel channel, ActionListener actionListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(FACTORY_RESET, 0, channel.putListener(actionListener));
    }

    public void requestOngoingPeerConfig(Channel channel, OngoingPeerInfoListener ongoingPeerInfoListener) {
        checkChannel(channel);
        channel.mAsyncChannel.sendMessage(REQUEST_ONGOING_PEER_CONFIG, Binder.getCallingUid(), channel.putListener(ongoingPeerInfoListener));
    }

    public void setOngoingPeerConfig(Channel channel, WifiP2pConfig wifiP2pConfig, ActionListener actionListener) {
        checkChannel(channel);
        checkP2pConfig(wifiP2pConfig);
        channel.mAsyncChannel.sendMessage(SET_ONGOING_PEER_CONFIG, 0, channel.putListener(actionListener), wifiP2pConfig);
    }

    public void requestP2pState(Channel channel, P2pStateListener p2pStateListener) {
        checkChannel(channel);
        if (p2pStateListener != null) {
            channel.mAsyncChannel.sendMessage(REQUEST_P2P_STATE, 0, channel.putListener(p2pStateListener));
            return;
        }
        throw new IllegalArgumentException("This listener cannot be null.");
    }

    public void requestDiscoveryState(Channel channel, DiscoveryStateListener discoveryStateListener) {
        checkChannel(channel);
        if (discoveryStateListener != null) {
            channel.mAsyncChannel.sendMessage(REQUEST_DISCOVERY_STATE, 0, channel.putListener(discoveryStateListener));
            return;
        }
        throw new IllegalArgumentException("This listener cannot be null.");
    }

    public void requestNetworkInfo(Channel channel, NetworkInfoListener networkInfoListener) {
        checkChannel(channel);
        if (networkInfoListener != null) {
            channel.mAsyncChannel.sendMessage(REQUEST_NETWORK_INFO, 0, channel.putListener(networkInfoListener));
            return;
        }
        throw new IllegalArgumentException("This listener cannot be null.");
    }

    public void requestDeviceInfo(Channel channel, DeviceInfoListener deviceInfoListener) {
        checkChannel(channel);
        if (deviceInfoListener != null) {
            channel.mAsyncChannel.sendMessage(REQUEST_DEVICE_INFO, 0, channel.putListener(deviceInfoListener), prepareExtrasBundle(channel));
            return;
        }
        throw new IllegalArgumentException("This listener cannot be null.");
    }

    public void addExternalApprover(Channel channel, MacAddress macAddress, ExternalApproverRequestListener externalApproverRequestListener) {
        checkChannel(channel);
        if (externalApproverRequestListener == null) {
            throw new IllegalArgumentException("This listener cannot be null.");
        } else if (macAddress != null) {
            Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
            prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_PEER_ADDRESS, macAddress);
            channel.mAsyncChannel.sendMessage(ADD_EXTERNAL_APPROVER, 0, channel.putListener(externalApproverRequestListener), prepareExtrasBundle);
        } else {
            throw new IllegalArgumentException("deviceAddress cannot be empty");
        }
    }

    public void removeExternalApprover(Channel channel, MacAddress macAddress, ActionListener actionListener) {
        checkChannel(channel);
        if (macAddress != null) {
            Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
            prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_PEER_ADDRESS, macAddress);
            channel.mAsyncChannel.sendMessage(REMOVE_EXTERNAL_APPROVER, 0, channel.putListener(actionListener), prepareExtrasBundle);
            return;
        }
        throw new IllegalArgumentException("deviceAddress cannot be empty");
    }

    public void setConnectionRequestResult(Channel channel, MacAddress macAddress, int i, ActionListener actionListener) {
        checkChannel(channel);
        if (macAddress != null) {
            Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
            prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_PEER_ADDRESS, macAddress);
            channel.mAsyncChannel.sendMessage(SET_CONNECTION_REQUEST_RESULT, i, channel.putListener(actionListener), prepareExtrasBundle);
            return;
        }
        throw new IllegalArgumentException("deviceAddress cannot be empty");
    }

    public void setConnectionRequestResult(Channel channel, MacAddress macAddress, int i, String str, ActionListener actionListener) {
        checkChannel(channel);
        if (macAddress == null) {
            throw new IllegalArgumentException("deviceAddress cannot be empty");
        } else if (i != 0 || !TextUtils.isEmpty(str)) {
            Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
            prepareExtrasBundle.putParcelable(EXTRA_PARAM_KEY_PEER_ADDRESS, macAddress);
            prepareExtrasBundle.putString(EXTRA_PARAM_KEY_WPS_PIN, str);
            channel.mAsyncChannel.sendMessage(SET_CONNECTION_REQUEST_RESULT, i, channel.putListener(actionListener), prepareExtrasBundle);
        } else {
            throw new IllegalArgumentException("PIN cannot be empty for accepting a request");
        }
    }

    public void setVendorElements(Channel channel, List<ScanResult.InformationElement> list, ActionListener actionListener) {
        if (isSetVendorElementsSupported()) {
            checkChannel(channel);
            int i = 0;
            for (ScanResult.InformationElement next : list) {
                if (next.f54id != 221) {
                    throw new IllegalArgumentException("received InformationElement which is not a Vendor Specific IE (VSIE). VSIEs have an ID = 221.");
                } else if (next.bytes == null || next.bytes.length > 255) {
                    throw new IllegalArgumentException("received InformationElement whose payload size is 0 or greater than 255.");
                } else {
                    i += next.bytes.length + 2;
                    if (i > 512) {
                        throw new IllegalArgumentException("received InformationElement whose total size is greater than 512.");
                    }
                }
            }
            Bundle prepareExtrasBundle = prepareExtrasBundle(channel);
            prepareExtrasBundle.putParcelableArrayList(EXTRA_PARAM_KEY_INFORMATION_ELEMENT_LIST, new ArrayList(list));
            channel.mAsyncChannel.sendMessage(SET_VENDOR_ELEMENTS, 0, channel.putListener(actionListener), prepareExtrasBundle);
            return;
        }
        throw new UnsupportedOperationException();
    }
}
