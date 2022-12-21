package android.net.wifi;

import android.annotation.SystemApi;
import android.app.ActivityManager;
import android.app.admin.WifiSsidPolicy;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.DhcpOption;
import android.net.MacAddress;
import android.net.Network;
import android.net.Uri;
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
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.WorkSource;
import android.os.connectivity.WifiActivityEnergyInfo;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.CloseGuard;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.modules.utils.HandlerExecutor;
import com.android.wifi.p018x.com.android.modules.utils.ParceledListSlice;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import sun.security.util.SecurityConstants;

public class WifiManager {
    public static final String ACTION_DISMISS_DIALOG = "android.net.wifi.action.DISMISS_DIALOG";
    public static final String ACTION_LAUNCH_DIALOG = "android.net.wifi.action.LAUNCH_DIALOG";
    @SystemApi
    public static final String ACTION_LINK_CONFIGURATION_CHANGED = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    @SystemApi
    public static final String ACTION_NETWORK_SETTINGS_RESET = "android.net.wifi.action.NETWORK_SETTINGS_RESET";
    public static final String ACTION_PASSPOINT_DEAUTH_IMMINENT = "android.net.wifi.action.PASSPOINT_DEAUTH_IMMINENT";
    public static final String ACTION_PASSPOINT_ICON = "android.net.wifi.action.PASSPOINT_ICON";
    @SystemApi
    public static final String ACTION_PASSPOINT_LAUNCH_OSU_VIEW = "android.net.wifi.action.PASSPOINT_LAUNCH_OSU_VIEW";
    public static final String ACTION_PASSPOINT_OSU_PROVIDERS_LIST = "android.net.wifi.action.PASSPOINT_OSU_PROVIDERS_LIST";
    public static final String ACTION_PASSPOINT_SUBSCRIPTION_REMEDIATION = "android.net.wifi.action.PASSPOINT_SUBSCRIPTION_REMEDIATION";
    public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    @SystemApi
    public static final String ACTION_REFRESH_USER_PROVISIONING = "android.net.wifi.action.REFRESH_USER_PROVISIONING";
    public static final int ACTION_REMOVE_SUGGESTION_DISCONNECT = 2;
    public static final int ACTION_REMOVE_SUGGESTION_LINGER = 1;
    @SystemApi
    public static final String ACTION_REQUEST_DISABLE = "android.net.wifi.action.REQUEST_DISABLE";
    @SystemApi
    public static final String ACTION_REQUEST_ENABLE = "android.net.wifi.action.REQUEST_ENABLE";
    public static final String ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE = "android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION = "android.net.wifi.action.WIFI_NETWORK_SUGGESTION_POST_CONNECTION";
    public static final String ACTION_WIFI_SCAN_AVAILABILITY_CHANGED = "android.net.wifi.action.WIFI_SCAN_AVAILABILITY_CHANGED";
    public static final MacAddress ALL_ZEROS_MAC_ADDRESS = MacAddress.fromString("00:00:00:00:00:00");
    @SystemApi
    public static final int API_AUTOJOIN_GLOBAL = 5;
    public static final int API_MAX = 7;
    @SystemApi
    public static final int API_SCANNING_ENABLED = 1;
    public static final int API_SET_ONE_SHOT_SCREEN_ON_CONNECTIVITY_SCAN_DELAY = 7;
    @SystemApi
    public static final int API_SET_SCAN_SCHEDULE = 6;
    @SystemApi
    public static final int API_SOFT_AP = 3;
    @SystemApi
    public static final int API_TETHERED_HOTSPOT = 4;
    @SystemApi
    public static final int API_WIFI_ENABLED = 2;
    @Deprecated
    public static final String BATCHED_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.BATCHED_RESULTS";
    @SystemApi
    public static final int CHANGE_REASON_ADDED = 0;
    @SystemApi
    public static final int CHANGE_REASON_CONFIG_CHANGE = 2;
    @SystemApi
    public static final int CHANGE_REASON_REMOVED = 1;
    @SystemApi
    public static final int COEX_RESTRICTION_SOFTAP = 2;
    @SystemApi
    public static final int COEX_RESTRICTION_WIFI_AWARE = 4;
    @SystemApi
    public static final int COEX_RESTRICTION_WIFI_DIRECT = 1;
    @SystemApi
    public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
    public static final boolean DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED = false;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_HIGH_MVMT = 1;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_LOW_MVMT = 2;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_STATIONARY = 3;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_UNKNOWN = 0;
    public static final int DIALOG_REPLY_CANCELLED = 3;
    public static final int DIALOG_REPLY_NEGATIVE = 1;
    public static final int DIALOG_REPLY_NEUTRAL = 2;
    public static final int DIALOG_REPLY_POSITIVE = 0;
    public static final int DIALOG_TYPE_P2P_INVITATION_RECEIVED = 3;
    public static final int DIALOG_TYPE_P2P_INVITATION_SENT = 2;
    public static final int DIALOG_TYPE_SIMPLE = 1;
    public static final int DIALOG_TYPE_UNKNOWN = 0;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_BRAINPOOLP256R1 = 3;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_BRAINPOOLP384R1 = 4;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_BRAINPOOLP512R1 = 5;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_PRIME256V1 = 0;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_SECP384R1 = 1;
    @SystemApi
    public static final int EASY_CONNECT_CRYPTOGRAPHY_CURVE_SECP521R1 = 2;
    private static final int EASY_CONNECT_DEVICE_INFO_MAXIMUM_LENGTH = 40;
    @SystemApi
    public static final int EASY_CONNECT_NETWORK_ROLE_AP = 1;
    @SystemApi
    public static final int EASY_CONNECT_NETWORK_ROLE_STA = 0;
    @Deprecated
    public static final int ERROR_AUTHENTICATING = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_EAP_FAILURE = 3;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_NONE = 0;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_TIMEOUT = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_WRONG_PSWD = 2;
    public static final String EXTRA_ANQP_ELEMENT_DATA = "android.net.wifi.extra.ANQP_ELEMENT_DATA";
    @Deprecated
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_BSSID_LONG = "android.net.wifi.extra.BSSID_LONG";
    @SystemApi
    public static final String EXTRA_CHANGE_REASON = "changeReason";
    public static final String EXTRA_DELAY = "android.net.wifi.extra.DELAY";
    public static final String EXTRA_DIALOG_ID = "android.net.wifi.extra.DIALOG_ID";
    public static final String EXTRA_DIALOG_MESSAGE = "android.net.wifi.extra.DIALOG_MESSAGE";
    public static final String EXTRA_DIALOG_MESSAGE_URL = "android.net.wifi.extra.DIALOG_MESSAGE_URL";
    public static final String EXTRA_DIALOG_MESSAGE_URL_END = "android.net.wifi.extra.DIALOG_MESSAGE_URL_END";
    public static final String EXTRA_DIALOG_MESSAGE_URL_START = "android.net.wifi.extra.DIALOG_MESSAGE_URL_START";
    public static final String EXTRA_DIALOG_NEGATIVE_BUTTON_TEXT = "android.net.wifi.extra.DIALOG_NEGATIVE_BUTTON_TEXT";
    public static final String EXTRA_DIALOG_NEUTRAL_BUTTON_TEXT = "android.net.wifi.extra.DIALOG_NEUTRAL_BUTTON_TEXT";
    public static final String EXTRA_DIALOG_POSITIVE_BUTTON_TEXT = "android.net.wifi.extra.DIALOG_POSITIVE_BUTTON_TEXT";
    public static final String EXTRA_DIALOG_TITLE = "android.net.wifi.extra.DIALOG_TITLE";
    public static final String EXTRA_DIALOG_TYPE = "android.net.wifi.extra.DIALOG_TYPE";
    public static final String EXTRA_ESS = "android.net.wifi.extra.ESS";
    public static final String EXTRA_FILENAME = "android.net.wifi.extra.FILENAME";
    public static final String EXTRA_ICON = "android.net.wifi.extra.ICON";
    @SystemApi
    @Deprecated
    public static final String EXTRA_LINK_PROPERTIES = "android.net.wifi.extra.LINK_PROPERTIES";
    @SystemApi
    @Deprecated
    public static final String EXTRA_MULTIPLE_NETWORKS_CHANGED = "multipleChanges";
    public static final String EXTRA_NETWORK_CAPABILITIES = "networkCapabilities";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_SUGGESTION = "android.net.wifi.extra.NETWORK_SUGGESTION";
    public static final String EXTRA_NEW_RSSI = "newRssi";
    @Deprecated
    public static final String EXTRA_NEW_STATE = "newState";
    @SystemApi
    public static final String EXTRA_OSU_NETWORK = "android.net.wifi.extra.OSU_NETWORK";
    public static final String EXTRA_P2P_DEVICE_NAME = "android.net.wifi.extra.P2P_DEVICE_NAME";
    public static final String EXTRA_P2P_DISPLAY_PIN = "android.net.wifi.extra.P2P_DISPLAY_PIN";
    public static final String EXTRA_P2P_PIN_REQUESTED = "android.net.wifi.extra.P2P_PIN_REQUESTED";
    public static final String EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE = "EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE";
    @SystemApi
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
    public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
    public static final String EXTRA_RESULTS_UPDATED = "resultsUpdated";
    public static final String EXTRA_SCAN_AVAILABLE = "android.net.wifi.extra.SCAN_AVAILABLE";
    public static final String EXTRA_SUBSCRIPTION_REMEDIATION_METHOD = "android.net.wifi.extra.SUBSCRIPTION_REMEDIATION_METHOD";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR_REASON = "supplicantErrorReason";
    @SystemApi
    public static final String EXTRA_URL = "android.net.wifi.extra.URL";
    @SystemApi
    public static final String EXTRA_WIFI_AP_FAILURE_REASON = "android.net.wifi.extra.WIFI_AP_FAILURE_REASON";
    @SystemApi
    public static final String EXTRA_WIFI_AP_INTERFACE_NAME = "android.net.wifi.extra.WIFI_AP_INTERFACE_NAME";
    @SystemApi
    public static final String EXTRA_WIFI_AP_MODE = "android.net.wifi.extra.WIFI_AP_MODE";
    @SystemApi
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    @SystemApi
    @Deprecated
    public static final String EXTRA_WIFI_CONFIGURATION = "wifiConfiguration";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_EVENT_TYPE = "et";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_SSID = "ssid";
    @Deprecated
    public static final String EXTRA_WIFI_INFO = "wifiInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_state";
    @SystemApi
    public static final int IFACE_IP_MODE_CONFIGURATION_ERROR = 0;
    @SystemApi
    public static final int IFACE_IP_MODE_LOCAL_ONLY = 2;
    @SystemApi
    public static final int IFACE_IP_MODE_TETHERED = 1;
    @SystemApi
    public static final int IFACE_IP_MODE_UNSPECIFIED = -1;
    public static final int INVALID_DIALOG_ID = -1;
    public static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    private static final int MAX_ACTIVE_LOCKS = 50;
    private static final int MAX_RSSI = -55;
    private static final int MIN_RSSI = -100;
    public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final int NETWORK_SUGGESTIONS_MAX_PER_APP_HIGH_RAM = 1024;
    public static final int NETWORK_SUGGESTIONS_MAX_PER_APP_LOW_RAM = 256;
    @SystemApi
    public static final int PASSPOINT_HOME_NETWORK = 0;
    @SystemApi
    public static final int PASSPOINT_ROAMING_NETWORK = 1;
    public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
    public static final int RSSI_LEVELS = 5;
    @SystemApi
    public static final int SAP_CLIENT_BLOCK_REASON_CODE_BLOCKED_BY_USER = 0;
    @SystemApi
    public static final int SAP_CLIENT_BLOCK_REASON_CODE_NO_MORE_STAS = 1;
    public static final int SAP_CLIENT_DISCONNECT_REASON_CODE_UNSPECIFIED = 2;
    @SystemApi
    public static final int SAP_START_FAILURE_GENERAL = 0;
    @SystemApi
    public static final int SAP_START_FAILURE_NO_CHANNEL = 1;
    @SystemApi
    public static final int SAP_START_FAILURE_UNSUPPORTED_CONFIGURATION = 2;
    public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE = 3;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP = 4;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_INVALID = 7;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_NOT_ALLOWED = 6;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED = 2;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL = 1;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID = 5;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_RESTRICTED_BY_ADMIN = 8;
    public static final int STATUS_NETWORK_SUGGESTIONS_SUCCESS = 0;
    public static final int STATUS_SUGGESTION_APPROVAL_APPROVED_BY_CARRIER_PRIVILEGE = 4;
    public static final int STATUS_SUGGESTION_APPROVAL_APPROVED_BY_USER = 2;
    public static final int STATUS_SUGGESTION_APPROVAL_PENDING = 1;
    public static final int STATUS_SUGGESTION_APPROVAL_REJECTED_BY_USER = 3;
    public static final int STATUS_SUGGESTION_APPROVAL_UNKNOWN = 0;
    public static final int STATUS_SUGGESTION_CONNECTION_FAILURE_ASSOCIATION = 1;
    public static final int STATUS_SUGGESTION_CONNECTION_FAILURE_AUTHENTICATION = 2;
    public static final int STATUS_SUGGESTION_CONNECTION_FAILURE_IP_PROVISIONING = 3;
    public static final int STATUS_SUGGESTION_CONNECTION_FAILURE_UNKNOWN = 0;
    @Deprecated
    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    @Deprecated
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    private static final String TAG = "WifiManager";
    public static final String UNKNOWN_SSID = "<unknown ssid>";
    @SystemApi
    public static final int VERBOSE_LOGGING_LEVEL_DISABLED = 0;
    @SystemApi
    public static final int VERBOSE_LOGGING_LEVEL_ENABLED = 1;
    @SystemApi
    public static final int VERBOSE_LOGGING_LEVEL_ENABLED_SHOW_KEY = 2;
    @SystemApi
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLED = 11;
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLING = 10;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLED = 13;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLING = 12;
    @SystemApi
    public static final int WIFI_AP_STATE_FAILED = 14;
    @SystemApi
    public static final String WIFI_CREDENTIAL_CHANGED_ACTION = "android.net.wifi.WIFI_CREDENTIAL_CHANGED";
    @SystemApi
    public static final int WIFI_CREDENTIAL_FORGOT = 1;
    @SystemApi
    public static final int WIFI_CREDENTIAL_SAVED = 0;
    public static final long WIFI_FEATURE_ADDITIONAL_STA = 2048;
    public static final long WIFI_FEATURE_ADDITIONAL_STA_LOCAL_ONLY = 17592186044416L;
    public static final long WIFI_FEATURE_ADDITIONAL_STA_MBB = 35184372088832L;
    public static final long WIFI_FEATURE_ADDITIONAL_STA_MULTI_INTERNET = 9007199254740992L;
    public static final long WIFI_FEATURE_ADDITIONAL_STA_RESTRICTED = 70368744177664L;
    public static final long WIFI_FEATURE_AP_RAND_MAC = 17179869184L;
    public static final long WIFI_FEATURE_AP_STA = 32768;
    public static final long WIFI_FEATURE_AWARE = 64;
    public static final long WIFI_FEATURE_BATCH_SCAN = 512;
    public static final long WIFI_FEATURE_BRIDGED_AP = 4398046511104L;
    public static final long WIFI_FEATURE_CONFIG_NDO = 2097152;
    public static final long WIFI_FEATURE_CONNECTED_RAND_MAC = 8589934592L;
    public static final long WIFI_FEATURE_CONTROL_ROAMING = 8388608;
    public static final long WIFI_FEATURE_D2AP_RTT = 256;
    public static final long WIFI_FEATURE_D2D_RTT = 128;
    public static final long WIFI_FEATURE_DECORATED_IDENTITY = 2251799813685248L;
    public static final long WIFI_FEATURE_DPP = 2147483648L;
    public static final long WIFI_FEATURE_DPP_AKM = 18014398509481984L;
    public static final long WIFI_FEATURE_DPP_ENROLLEE_RESPONDER = 140737488355328L;
    public static final long WIFI_FEATURE_EPR = 16384;
    public static final long WIFI_FEATURE_FILS_SHA256 = 274877906944L;
    public static final long WIFI_FEATURE_FILS_SHA384 = 549755813888L;
    public static final long WIFI_FEATURE_HAL_EPNO = 262144;
    public static final long WIFI_FEATURE_IE_WHITELIST = 16777216;
    public static final long WIFI_FEATURE_INFRA = 1;
    public static final long WIFI_FEATURE_INFRA_60G = 8796093022208L;
    public static final long WIFI_FEATURE_LINK_LAYER_STATS = 65536;
    public static final long WIFI_FEATURE_LOGGER = 131072;
    public static final long WIFI_FEATURE_LOW_LATENCY = 1073741824;
    public static final long WIFI_FEATURE_MBO = 34359738368L;
    public static final long WIFI_FEATURE_MKEEP_ALIVE = 1048576;
    public static final long WIFI_FEATURE_MOBILE_HOTSPOT = 16;
    public static final long WIFI_FEATURE_OCE = 68719476736L;
    public static final long WIFI_FEATURE_OWE = 536870912;
    public static final long WIFI_FEATURE_P2P = 8;
    public static final long WIFI_FEATURE_P2P_RAND_MAC = 4294967296L;
    public static final long WIFI_FEATURE_PASSPOINT = 4;
    public static final long WIFI_FEATURE_PASSPOINT_TERMS_AND_CONDITIONS = 281474976710656L;
    public static final long WIFI_FEATURE_PNO = 1024;
    public static final long WIFI_FEATURE_RSSI_MONITOR = 524288;
    public static final long WIFI_FEATURE_SAE_H2E = 562949953421312L;
    public static final long WIFI_FEATURE_SAE_PK = 1099511627776L;
    public static final long WIFI_FEATURE_SCANNER = 32;
    public static final long WIFI_FEATURE_SCAN_RAND = 33554432;
    public static final long WIFI_FEATURE_STA_BRIDGED_AP = 2199023255552L;
    public static final long WIFI_FEATURE_TDLS = 4096;
    public static final long WIFI_FEATURE_TDLS_OFFCHANNEL = 8192;
    public static final long WIFI_FEATURE_TRANSMIT_POWER = 4194304;
    public static final long WIFI_FEATURE_TRUST_ON_FIRST_USE = 4503599627370496L;
    public static final long WIFI_FEATURE_TX_POWER_LIMIT = 67108864;
    public static final long WIFI_FEATURE_WAPI = 137438953472L;
    public static final long WIFI_FEATURE_WFD_R2 = 1125899906842624L;
    public static final long WIFI_FEATURE_WPA3_SAE = 134217728;
    public static final long WIFI_FEATURE_WPA3_SUITE_B = 268435456;
    public static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
    public static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
    public static final int WIFI_FREQUENCY_BAND_AUTO = 0;
    public static final int WIFI_INTERFACE_TYPE_AP = 1;
    public static final int WIFI_INTERFACE_TYPE_AWARE = 2;
    public static final int WIFI_INTERFACE_TYPE_DIRECT = 3;
    public static final int WIFI_INTERFACE_TYPE_STA = 0;
    @Deprecated
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
    public static final int WIFI_MODE_FULL_LOW_LATENCY = 4;
    public static final int WIFI_MODE_NO_LOCKS_HELD = 0;
    @Deprecated
    public static final int WIFI_MODE_SCAN_ONLY = 2;
    public static final int WIFI_MULTI_INTERNET_MODE_DBS_AP = 1;
    public static final int WIFI_MULTI_INTERNET_MODE_DISABLED = 0;
    public static final int WIFI_MULTI_INTERNET_MODE_MULTI_AP = 2;
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_UNKNOWN = 4;
    public static final int WPS_AUTH_FAILURE = 6;
    public static final int WPS_OVERLAP_ERROR = 3;
    public static final int WPS_TIMED_OUT = 7;
    public static final int WPS_TKIP_ONLY_PROHIBITED = 5;
    public static final int WPS_WEP_PROHIBITED = 4;
    private static final SparseArray<IOnWifiDriverCountryCodeChangedListener> sActiveCountryCodeChangedCallbackMap = new SparseArray<>();
    private static final SparseArray<ISoftApCallback> sLocalOnlyHotspotSoftApCallbackMap = new SparseArray<>();
    private static final SparseArray<INetworkRequestMatchCallback> sNetworkRequestMatchCallbackMap = new SparseArray<>();
    private static final SparseArray<IOnWifiUsabilityStatsListener> sOnWifiUsabilityStatsListenerMap = new SparseArray<>();
    private static final SparseArray<ISoftApCallback> sSoftApCallbackMap = new SparseArray<>();
    private static final SparseArray<ISuggestionConnectionStatusListener> sSuggestionConnectionStatusListenerMap = new SparseArray<>();
    private static final SparseArray<ISuggestionUserApprovalStatusListener> sSuggestionUserApprovalStatusListenerMap = new SparseArray<>();
    private static final SparseArray<ITrafficStateCallback> sTrafficStateCallbackMap = new SparseArray<>();
    private static final SparseArray<IWifiVerboseLoggingStatusChangedListener> sWifiVerboseLoggingStatusChangedListenerMap = new SparseArray<>();
    /* access modifiers changed from: private */
    public int mActiveLockCount;
    private Context mContext;
    private LocalOnlyHotspotCallbackProxy mLOHSCallbackProxy;
    private LocalOnlyHotspotObserverProxy mLOHSObserverProxy;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private Looper mLooper;
    IWifiManager mService;
    private final int mTargetSdkVersion;
    /* access modifiers changed from: private */
    public boolean mVerboseLoggingEnabled = false;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionAfterRemovingSuggestion {
    }

    @SystemApi
    public interface ActionListener {
        public static final int FAILURE_BUSY = 2;
        public static final int FAILURE_INTERNAL_ERROR = 0;
        public static final int FAILURE_INVALID_ARGS = 3;
        public static final int FAILURE_IN_PROGRESS = 1;
        public static final int FAILURE_NOT_AUTHORIZED = 4;

        void onFailure(int i);

        void onSuccess();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionListenerFailureReason {
    }

    @SystemApi
    public interface ActiveCountryCodeChangedCallback {
        void onActiveCountryCodeChanged(String str);

        void onCountryCodeInactive();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApiType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CoexRestriction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceMobilityState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogReply {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectCryptographyCurve {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectNetworkRole {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IfaceIpMode {
    }

    public static class LocalOnlyHotspotCallback {
        public static final int ERROR_GENERIC = 2;
        public static final int ERROR_INCOMPATIBLE_MODE = 3;
        public static final int ERROR_NO_CHANNEL = 1;
        public static final int ERROR_TETHERING_DISALLOWED = 4;
        public static final int REQUEST_REGISTERED = 0;

        public void onFailed(int i) {
        }

        public void onStarted(LocalOnlyHotspotReservation localOnlyHotspotReservation) {
        }

        public void onStopped() {
        }
    }

    public static class LocalOnlyHotspotObserver {
        public void onRegistered(LocalOnlyHotspotSubscription localOnlyHotspotSubscription) {
        }

        public void onStarted(SoftApConfiguration softApConfiguration) {
        }

        public void onStopped() {
        }
    }

    @SystemApi
    public interface NetworkRequestMatchCallback {
        void onAbort() {
        }

        void onMatch(List<ScanResult> list) {
        }

        void onUserSelectionCallbackRegistration(NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback) {
        }

        void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) {
        }

        void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) {
        }
    }

    @SystemApi
    public interface NetworkRequestUserSelectionCallback {
        void reject() {
        }

        void select(WifiConfiguration wifiConfiguration) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkSuggestionsStatusCode {
    }

    @SystemApi
    public interface OnWifiActivityEnergyInfoListener {
        void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo);
    }

    @SystemApi
    public interface OnWifiUsabilityStatsListener {
        void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry);
    }

    @SystemApi
    public interface PnoScanResultsCallback {
        public static final int REGISTER_PNO_CALLBACK_ALREADY_REGISTERED = 1;
        public static final int REGISTER_PNO_CALLBACK_PNO_NOT_SUPPORTED = 3;
        public static final int REGISTER_PNO_CALLBACK_RESOURCE_BUSY = 2;
        public static final int REGISTER_PNO_CALLBACK_UNKNOWN = 0;
        public static final int REMOVE_PNO_CALLBACK_RESULTS_DELIVERED = 1;
        public static final int REMOVE_PNO_CALLBACK_UNKNOWN = 0;
        public static final int REMOVE_PNO_CALLBACK_UNREGISTERED = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface RegisterFailureReason {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface RemovalReason {
        }

        void onRegisterFailed(int i);

        void onRegisterSuccess();

        void onRemoved(int i);

        void onScanResultsAvailable(List<ScanResult> list);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapClientBlockedReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapStartFailure {
    }

    @SystemApi
    public interface ScoreUpdateObserver {
        void blocklistCurrentBssid(int i) {
        }

        void notifyScoreUpdate(int i, int i2);

        void notifyStatusUpdate(int i, boolean z) {
        }

        void requestNudOperation(int i) {
        }

        void triggerUpdateOfWifiUsabilityStats(int i);
    }

    @SystemApi
    public interface SoftApCallback {
        void onBlockedClientConnecting(WifiClient wifiClient, int i) {
        }

        void onCapabilityChanged(SoftApCapability softApCapability) {
        }

        void onConnectedClientsChanged(SoftApInfo softApInfo, List<WifiClient> list) {
        }

        @Deprecated
        void onConnectedClientsChanged(List<WifiClient> list) {
        }

        @Deprecated
        void onInfoChanged(SoftApInfo softApInfo) {
        }

        void onInfoChanged(List<SoftApInfo> list) {
        }

        void onStateChanged(int i, int i2) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SuggestionConnectionStatusCode {
    }

    public interface SuggestionConnectionStatusListener {
        void onConnectionStatus(WifiNetworkSuggestion wifiNetworkSuggestion, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SuggestionUserApprovalStatus {
    }

    public interface SuggestionUserApprovalStatusListener {
        void onUserApprovalStatusChange(int i);
    }

    @SystemApi
    public interface TrafficStateCallback {
        public static final int DATA_ACTIVITY_IN = 1;
        public static final int DATA_ACTIVITY_INOUT = 3;
        public static final int DATA_ACTIVITY_NONE = 0;
        public static final int DATA_ACTIVITY_OUT = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DataActivity {
        }

        void onStateChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VerboseLoggingLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiApState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiInterfaceType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiMultiInternetMode {
    }

    @SystemApi
    public interface WifiVerboseLoggingStatusChangedListener {
        void onWifiVerboseLoggingStatusChanged(boolean z);
    }

    public static abstract class WpsCallback {
        public abstract void onFailed(int i);

        public abstract void onStarted(String str);

        public abstract void onSucceeded();
    }

    @Deprecated
    public static int calculateSignalLevel(int i, int i2) {
        if (i <= -100) {
            return 0;
        }
        if (i >= MAX_RSSI) {
            return i2 - 1;
        }
        return (int) ((((float) (i - -100)) * ((float) (i2 - 1))) / 45.0f);
    }

    public static int compareSignalLevel(int i, int i2) {
        return i - i2;
    }

    @SystemApi
    public static int getEasyConnectMaxAllowedResponderDeviceInfoLength() {
        return 40;
    }

    public static int getMaxNumberOfNetworkSuggestionsPerApp(boolean z) {
        return z ? 256 : 1024;
    }

    public String getCurrentNetworkWpsNfcConfigurationToken() {
        return null;
    }

    public boolean getEnableAutoJoinWhenAssociated() {
        return false;
    }

    public boolean isWpa3SaePublicKeySupported() {
        return false;
    }

    @Deprecated
    public boolean saveConfiguration() {
        return false;
    }

    public boolean setEnableAutoJoinWhenAssociated(boolean z) {
        return false;
    }

    public WifiManager(Context context, IWifiManager iWifiManager, Looper looper) {
        this.mContext = context;
        this.mService = iWifiManager;
        this.mLooper = looper;
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        updateVerboseLoggingEnabledFromService();
    }

    @Deprecated
    public List<WifiConfiguration> getConfiguredNetworks() {
        try {
            ParceledListSlice configuredNetworks = this.mService.getConfiguredNetworks(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), false);
            if (configuredNetworks == null) {
                return Collections.emptyList();
            }
            return configuredNetworks.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<WifiConfiguration> getCallerConfiguredNetworks() {
        try {
            ParceledListSlice configuredNetworks = this.mService.getConfiguredNetworks(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true);
            if (configuredNetworks == null) {
                return Collections.emptyList();
            }
            return configuredNetworks.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            Bundle bundle = new Bundle();
            if (SdkLevel.isAtLeastS()) {
                bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
            }
            ParceledListSlice privilegedConfiguredNetworks = this.mService.getPrivilegedConfiguredNetworks(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), bundle);
            if (privilegedConfiguredNetworks == null) {
                return Collections.emptyList();
            }
            return privilegedConfiguredNetworks.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public WifiConfiguration getPrivilegedConnectedNetwork() {
        try {
            Bundle bundle = new Bundle();
            if (SdkLevel.isAtLeastS()) {
                bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
            }
            return this.mService.getPrivilegedConnectedNetwork(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), bundle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<Pair<WifiConfiguration, Map<Integer, List<ScanResult>>>> getAllMatchingWifiConfigs(List<ScanResult> list) {
        ArrayList arrayList = new ArrayList();
        try {
            Map allMatchingPasspointProfilesForScanResults = this.mService.getAllMatchingPasspointProfilesForScanResults(list);
            if (allMatchingPasspointProfilesForScanResults.isEmpty()) {
                return arrayList;
            }
            for (WifiConfiguration next : this.mService.getWifiConfigsForPasspointProfiles(new ArrayList(allMatchingPasspointProfilesForScanResults.keySet()))) {
                Map map = (Map) allMatchingPasspointProfilesForScanResults.get(next.getProfileKey());
                if (map != null) {
                    arrayList.add(Pair.create(next, map));
                }
            }
            return arrayList;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public static class ScreenOnScanSchedule {
        private final Duration mScanInterval;
        private final int mScanType;

        public ScreenOnScanSchedule(Duration duration, int i) {
            if (duration != null) {
                this.mScanInterval = duration;
                this.mScanType = i;
                return;
            }
            throw new IllegalArgumentException("scanInterval can't be null");
        }

        public Duration getScanInterval() {
            return this.mScanInterval;
        }

        public int getScanType() {
            return this.mScanType;
        }
    }

    @SystemApi
    public void setScreenOnScanSchedule(List<ScreenOnScanSchedule> list) {
        if (list == null) {
            try {
                this.mService.setScreenOnScanSchedule((int[]) null, (int[]) null);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else if (!list.isEmpty()) {
            int[] iArr = new int[list.size()];
            int[] iArr2 = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                iArr[i] = (int) list.get(i).getScanInterval().toSeconds();
                iArr2[i] = list.get(i).getScanType();
            }
            this.mService.setScreenOnScanSchedule(iArr, iArr2);
        } else {
            throw new IllegalArgumentException("The input should either be null or a non-empty list");
        }
    }

    public void setOneShotScreenOnConnectivityScanDelayMillis(int i) {
        try {
            this.mService.setOneShotScreenOnConnectivityScanDelayMillis(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult> list) {
        try {
            return this.mService.getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(list);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @SystemApi
    public void setSsidsAllowlist(Set<WifiSsid> set) {
        if (set != null) {
            try {
                this.mService.setSsidsAllowlist(this.mContext.getOpPackageName(), new ArrayList(set));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("WifiManager: ssids can not be null");
        }
    }

    @SystemApi
    public Set<WifiSsid> getSsidsAllowlist() {
        try {
            return new ArraySet(this.mService.getSsidsAllowlist(this.mContext.getOpPackageName()));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Map<OsuProvider, List<ScanResult>> getMatchingOsuProviders(List<ScanResult> list) {
        if (list == null) {
            return new HashMap();
        }
        try {
            return this.mService.getMatchingOsuProviders(list);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Map<OsuProvider, PasspointConfiguration> getMatchingPasspointConfigsForOsuProviders(Set<OsuProvider> set) {
        try {
            return this.mService.getMatchingPasspointConfigsForOsuProviders(new ArrayList(set));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int addNetwork(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return -1;
        }
        wifiConfiguration.networkId = -1;
        return addOrUpdateNetwork(wifiConfiguration);
    }

    public AddNetworkResult addNetworkPrivileged(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            throw new IllegalArgumentException("config cannot be null");
        } else if (!wifiConfiguration.isSecurityType(13) || isFeatureSupported(WIFI_FEATURE_DPP_AKM)) {
            wifiConfiguration.networkId = -1;
            try {
                return this.mService.addOrUpdateNetworkPrivileged(wifiConfiguration, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("dpp akm is not supported");
        }
    }

    public static final class AddNetworkResult implements Parcelable {
        public static final Parcelable.Creator<AddNetworkResult> CREATOR = new Parcelable.Creator<AddNetworkResult>() {
            public AddNetworkResult createFromParcel(Parcel parcel) {
                return new AddNetworkResult(parcel.readInt(), parcel.readInt());
            }

            public AddNetworkResult[] newArray(int i) {
                return new AddNetworkResult[i];
            }
        };
        public static final int STATUS_ADD_PASSPOINT_FAILURE = 3;
        public static final int STATUS_ADD_WIFI_CONFIG_FAILURE = 4;
        public static final int STATUS_FAILURE_UNKNOWN = 1;
        public static final int STATUS_FAILURE_UPDATE_NETWORK_KEYS = 9;
        public static final int STATUS_INVALID_CONFIGURATION = 5;
        public static final int STATUS_INVALID_CONFIGURATION_ENTERPRISE = 10;
        public static final int STATUS_NO_PERMISSION = 2;
        public static final int STATUS_NO_PERMISSION_MODIFY_CONFIG = 6;
        public static final int STATUS_NO_PERMISSION_MODIFY_MAC_RANDOMIZATION = 8;
        public static final int STATUS_NO_PERMISSION_MODIFY_PROXY_SETTING = 7;
        public static final int STATUS_SUCCESS = 0;
        public final int networkId;
        public final int statusCode;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AddNetworkStatusCode {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.statusCode);
            parcel.writeInt(this.networkId);
        }

        public AddNetworkResult(int i, int i2) {
            this.statusCode = i;
            this.networkId = i2;
        }
    }

    @Deprecated
    public int updateNetwork(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null || wifiConfiguration.networkId < 0) {
            return -1;
        }
        return addOrUpdateNetwork(wifiConfiguration);
    }

    private int addOrUpdateNetwork(WifiConfiguration wifiConfiguration) {
        Bundle bundle = new Bundle();
        if (SdkLevel.isAtLeastS()) {
            bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
        }
        try {
            return this.mService.addOrUpdateNetwork(wifiConfiguration, this.mContext.getOpPackageName(), bundle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private class NetworkRequestUserSelectionCallbackProxy implements NetworkRequestUserSelectionCallback {
        private final INetworkRequestUserSelectionCallback mCallback;

        NetworkRequestUserSelectionCallbackProxy(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) {
            this.mCallback = iNetworkRequestUserSelectionCallback;
        }

        public void select(WifiConfiguration wifiConfiguration) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestUserSelectionCallbackProxy: select wificonfiguration: " + wifiConfiguration);
            }
            try {
                this.mCallback.select(wifiConfiguration);
            } catch (RemoteException e) {
                Log.e(WifiManager.TAG, "Failed to invoke onSelected", e);
                throw e.rethrowFromSystemServer();
            }
        }

        public void reject() {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestUserSelectionCallbackProxy: reject");
            }
            try {
                this.mCallback.reject();
            } catch (RemoteException e) {
                Log.e(WifiManager.TAG, "Failed to invoke onRejected", e);
                throw e.rethrowFromSystemServer();
            }
        }
    }

    private class NetworkRequestMatchCallbackProxy extends INetworkRequestMatchCallback.Stub {
        private final NetworkRequestMatchCallback mCallback;
        private final Executor mExecutor;

        NetworkRequestMatchCallbackProxy(Executor executor, NetworkRequestMatchCallback networkRequestMatchCallback) {
            this.mExecutor = executor;
            this.mCallback = networkRequestMatchCallback;
        }

        public void onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onUserSelectionCallbackRegistration callback: " + iNetworkRequestUserSelectionCallback);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0176x40052be4(this, iNetworkRequestUserSelectionCallback));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSelectionCallbackRegistration$0$android-net-wifi-WifiManager$NetworkRequestMatchCallbackProxy */
        public /* synthetic */ void mo5058x7ed1c7a6(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) {
            this.mCallback.onUserSelectionCallbackRegistration(new NetworkRequestUserSelectionCallbackProxy(iNetworkRequestUserSelectionCallback));
        }

        public void onAbort() {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onAbort");
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0174x40052be2(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAbort$1$android-net-wifi-WifiManager$NetworkRequestMatchCallbackProxy */
        public /* synthetic */ void mo5056x540613d8() {
            this.mCallback.onAbort();
        }

        public void onMatch(List<ScanResult> list) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onMatch scanResults: " + list);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0175x40052be3(this, list));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onMatch$2$android-net-wifi-WifiManager$NetworkRequestMatchCallbackProxy */
        public /* synthetic */ void mo5057x67ff26ae(List list) {
            this.mCallback.onMatch(list);
        }

        public void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onUserSelectionConnectSuccess  wificonfiguration: " + wifiConfiguration);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0172x40052be0(this, wifiConfiguration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSelectionConnectSuccess$3$android-net-wifi-WifiManager$NetworkRequestMatchCallbackProxy */
        public /* synthetic */ void mo5060x774ac244(WifiConfiguration wifiConfiguration) {
            this.mCallback.onUserSelectionConnectSuccess(wifiConfiguration);
        }

        public void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onUserSelectionConnectFailure wificonfiguration: " + wifiConfiguration);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0173x40052be1(this, wifiConfiguration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSelectionConnectFailure$4$android-net-wifi-WifiManager$NetworkRequestMatchCallbackProxy */
        public /* synthetic */ void mo5059xa951800c(WifiConfiguration wifiConfiguration) {
            this.mCallback.onUserSelectionConnectFailure(wifiConfiguration);
        }
    }

    @SystemApi
    public void registerNetworkRequestMatchCallback(Executor executor, NetworkRequestMatchCallback networkRequestMatchCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (networkRequestMatchCallback != null) {
            Log.v(TAG, "registerNetworkRequestMatchCallback: callback=" + networkRequestMatchCallback + ", executor=" + executor);
            try {
                SparseArray<INetworkRequestMatchCallback> sparseArray = sNetworkRequestMatchCallbackMap;
                synchronized (sparseArray) {
                    NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy = new NetworkRequestMatchCallbackProxy(executor, networkRequestMatchCallback);
                    sparseArray.put(System.identityHashCode(networkRequestMatchCallback), networkRequestMatchCallbackProxy);
                    this.mService.registerNetworkRequestMatchCallback(networkRequestMatchCallbackProxy);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void unregisterNetworkRequestMatchCallback(NetworkRequestMatchCallback networkRequestMatchCallback) {
        if (networkRequestMatchCallback != null) {
            Log.v(TAG, "unregisterNetworkRequestMatchCallback: callback=" + networkRequestMatchCallback);
            try {
                SparseArray<INetworkRequestMatchCallback> sparseArray = sNetworkRequestMatchCallbackMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(networkRequestMatchCallback);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.unregisterNetworkRequestMatchCallback(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void removeAppState(int i, String str) {
        try {
            this.mService.removeAppState(i, str);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int addNetworkSuggestions(List<WifiNetworkSuggestion> list) {
        try {
            return this.mService.addNetworkSuggestions(list, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list) {
        return removeNetworkSuggestions(list, 2);
    }

    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, int i) {
        try {
            return this.mService.removeNetworkSuggestions(list, this.mContext.getOpPackageName(), i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<WifiNetworkSuggestion> getNetworkSuggestions() {
        try {
            return this.mService.getNetworkSuggestions(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getMaxNumberOfNetworkSuggestionsPerApp() {
        return getMaxNumberOfNetworkSuggestionsPerApp(((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).isLowRamDevice());
    }

    public void addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration) {
        try {
            if (!this.mService.addOrUpdatePasspointConfiguration(passpointConfiguration, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removePasspointConfiguration(String str) {
        try {
            if (!this.mService.removePasspointConfiguration(str, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public List<PasspointConfiguration> getPasspointConfigurations() {
        try {
            return this.mService.getPasspointConfigurations(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void queryPasspointIcon(long j, String str) {
        try {
            this.mService.queryPasspointIcon(j, str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int matchProviderWithCurrentNetwork(String str) {
        try {
            return this.mService.matchProviderWithCurrentNetwork(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean removeNetwork(int i) {
        try {
            return this.mService.removeNetwork(i, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean removeNonCallerConfiguredNetworks() {
        try {
            return this.mService.removeNonCallerConfiguredNetworks(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean enableNetwork(int i, boolean z) {
        try {
            return this.mService.enableNetwork(i, z, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean disableNetwork(int i) {
        try {
            return this.mService.disableNetwork(i, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean disconnect() {
        try {
            return this.mService.disconnect(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean reconnect() {
        try {
            return this.mService.reconnect(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean reassociate() {
        try {
            return this.mService.reassociate(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean pingSupplicant() {
        return isWifiEnabled();
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

    public boolean isPasspointSupported() {
        return isFeatureSupported(4);
    }

    public boolean isP2pSupported() {
        return isFeatureSupported(8);
    }

    @SystemApi
    public boolean isPortableHotspotSupported() {
        return isFeatureSupported(16);
    }

    @SystemApi
    public boolean isWifiScannerSupported() {
        return isFeatureSupported(32);
    }

    public boolean isWifiAwareSupported() {
        return isFeatureSupported(64);
    }

    public boolean isStaApConcurrencySupported() {
        return isFeatureSupported(32768);
    }

    public boolean isStaConcurrencyForLocalOnlyConnectionsSupported() {
        return isFeatureSupported(WIFI_FEATURE_ADDITIONAL_STA_LOCAL_ONLY);
    }

    public boolean isMakeBeforeBreakWifiSwitchingSupported() {
        return isFeatureSupported(WIFI_FEATURE_ADDITIONAL_STA_MBB);
    }

    public boolean isStaConcurrencyForMultiInternetSupported() {
        return isFeatureSupported(WIFI_FEATURE_ADDITIONAL_STA_MULTI_INTERNET);
    }

    @SystemApi
    public boolean isStaConcurrencyForRestrictedConnectionsSupported() {
        return isFeatureSupported(WIFI_FEATURE_ADDITIONAL_STA_RESTRICTED);
    }

    @SystemApi
    @Deprecated
    public boolean isDeviceToDeviceRttSupported() {
        return isFeatureSupported(128);
    }

    @Deprecated
    public boolean isDeviceToApRttSupported() {
        return isFeatureSupported(256);
    }

    public boolean isPreferredNetworkOffloadSupported() {
        return isFeatureSupported(1024);
    }

    public boolean isTdlsSupported() {
        return isFeatureSupported(4096);
    }

    public boolean isOffChannelTdlsSupported() {
        return isFeatureSupported(8192);
    }

    public boolean isEnhancedPowerReportingSupported() {
        return isFeatureSupported(65536);
    }

    @SystemApi
    public boolean isConnectedMacRandomizationSupported() {
        return isFeatureSupported(WIFI_FEATURE_CONNECTED_RAND_MAC);
    }

    @SystemApi
    public boolean isApMacRandomizationSupported() {
        return isFeatureSupported(WIFI_FEATURE_AP_RAND_MAC);
    }

    public boolean is24GHzBandSupported() {
        try {
            return this.mService.is24GHzBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean is5GHzBandSupported() {
        try {
            return this.mService.is5GHzBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean is60GHzBandSupported() {
        try {
            return this.mService.is60GHzBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean is6GHzBandSupported() {
        try {
            return this.mService.is6GHzBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiStandardSupported(int i) {
        try {
            return this.mService.isWifiStandardSupported(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isStaBridgedApConcurrencySupported() {
        return isFeatureSupported(WIFI_FEATURE_STA_BRIDGED_AP);
    }

    public boolean isBridgedApConcurrencySupported() {
        return isFeatureSupported(WIFI_FEATURE_BRIDGED_AP);
    }

    private static class OnWifiActivityEnergyInfoProxy extends IOnWifiActivityEnergyInfoListener.Stub {
        private Executor mExecutor;
        private OnWifiActivityEnergyInfoListener mListener;
        private final Object mLock = new Object();

        OnWifiActivityEnergyInfoProxy(Executor executor, OnWifiActivityEnergyInfoListener onWifiActivityEnergyInfoListener) {
            this.mExecutor = executor;
            this.mListener = onWifiActivityEnergyInfoListener;
        }

        public void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo) {
            synchronized (this.mLock) {
                Executor executor = this.mExecutor;
                if (executor != null) {
                    OnWifiActivityEnergyInfoListener onWifiActivityEnergyInfoListener = this.mListener;
                    if (onWifiActivityEnergyInfoListener != null) {
                        this.mExecutor = null;
                        this.mListener = null;
                        Binder.clearCallingIdentity();
                        executor.execute(new C0179x8981bbf8(onWifiActivityEnergyInfoListener, wifiActivityEnergyInfo));
                    }
                }
            }
        }
    }

    @SystemApi
    public void getWifiActivityEnergyInfoAsync(Executor executor, OnWifiActivityEnergyInfoListener onWifiActivityEnergyInfoListener) {
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(onWifiActivityEnergyInfoListener, "listener cannot be null");
        try {
            this.mService.getWifiActivityEnergyInfoAsync(new OnWifiActivityEnergyInfoProxy(executor, onWifiActivityEnergyInfoListener));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean startScan() {
        return startScan((WorkSource) null);
    }

    @SystemApi
    public boolean startScan(WorkSource workSource) {
        try {
            return this.mService.startScan(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public WifiInfo getConnectionInfo() {
        try {
            return this.mService.getConnectionInfo(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ScanResult> getScanResults() {
        try {
            return this.mService.getScanResults(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Map<WifiNetworkSuggestion, List<ScanResult>> getMatchingScanResults(List<WifiNetworkSuggestion> list, List<ScanResult> list2) {
        if (list != null) {
            try {
                return this.mService.getMatchingScanResults(list, list2, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("networkSuggestions must not be null.");
        }
    }

    @SystemApi
    public void setScanAlwaysAvailable(boolean z) {
        try {
            this.mService.setScanAlwaysAvailable(z, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean isScanAlwaysAvailable() {
        try {
            return this.mService.isScanAlwaysAvailable();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static class OnDriverCountryCodeChangedProxy extends IOnWifiDriverCountryCodeChangedListener.Stub {
        private ActiveCountryCodeChangedCallback mCallback;
        private Executor mExecutor;

        OnDriverCountryCodeChangedProxy(Executor executor, ActiveCountryCodeChangedCallback activeCountryCodeChangedCallback) {
            Objects.requireNonNull(executor);
            Objects.requireNonNull(activeCountryCodeChangedCallback);
            this.mExecutor = executor;
            this.mCallback = activeCountryCodeChangedCallback;
        }

        public void onDriverCountryCodeChanged(String str) {
            Log.i(WifiManager.TAG, "OnDriverCountryCodeChangedProxy: receive onDriverCountryCodeChanged: " + str);
            Binder.clearCallingIdentity();
            if (str != null) {
                this.mExecutor.execute(new C0177xe24c6db9(this, str));
            } else {
                this.mExecutor.execute(new C0178xe24c6dba(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDriverCountryCodeChanged$0$android-net-wifi-WifiManager$OnDriverCountryCodeChangedProxy */
        public /* synthetic */ void mo5063x25558f23(String str) {
            this.mCallback.onActiveCountryCodeChanged(str);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDriverCountryCodeChanged$1$android-net-wifi-WifiManager$OnDriverCountryCodeChangedProxy */
        public /* synthetic */ void mo5064xb993fec2() {
            this.mCallback.onCountryCodeInactive();
        }
    }

    @SystemApi
    public void registerActiveCountryCodeChangedCallback(Executor executor, ActiveCountryCodeChangedCallback activeCountryCodeChangedCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (activeCountryCodeChangedCallback != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.d(TAG, "registerActiveCountryCodeChangedCallback: callback=" + activeCountryCodeChangedCallback + ", executor=" + executor);
            }
            int identityHashCode = System.identityHashCode(activeCountryCodeChangedCallback);
            SparseArray<IOnWifiDriverCountryCodeChangedListener> sparseArray = sActiveCountryCodeChangedCallbackMap;
            synchronized (sparseArray) {
                try {
                    OnDriverCountryCodeChangedProxy onDriverCountryCodeChangedProxy = new OnDriverCountryCodeChangedProxy(executor, activeCountryCodeChangedCallback);
                    sparseArray.put(identityHashCode, onDriverCountryCodeChangedProxy);
                    this.mService.registerDriverCountryCodeChangedListener(onDriverCountryCodeChangedProxy, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
                } catch (RemoteException e) {
                    sActiveCountryCodeChangedCallbackMap.remove(identityHashCode);
                    throw e.rethrowFromSystemServer();
                } catch (Throwable th) {
                    throw th;
                }
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void unregisterActiveCountryCodeChangedCallback(ActiveCountryCodeChangedCallback activeCountryCodeChangedCallback) {
        if (activeCountryCodeChangedCallback != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.d(TAG, "unregisterActiveCountryCodeChangedCallback: callback=" + activeCountryCodeChangedCallback);
            }
            int identityHashCode = System.identityHashCode(activeCountryCodeChangedCallback);
            SparseArray<IOnWifiDriverCountryCodeChangedListener> sparseArray = sActiveCountryCodeChangedCallbackMap;
            synchronized (sparseArray) {
                try {
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external listener " + identityHashCode);
                        sparseArray.remove(identityHashCode);
                        return;
                    }
                    this.mService.unregisterDriverCountryCodeChangedListener(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                } catch (Throwable th) {
                    sActiveCountryCodeChangedCallbackMap.remove(identityHashCode);
                    throw th;
                }
            }
        } else {
            throw new IllegalArgumentException("Callback cannot be null");
        }
    }

    @SystemApi
    public String getCountryCode() {
        try {
            return this.mService.getCountryCode(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setOverrideCountryCode(String str) {
        try {
            this.mService.setOverrideCountryCode(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void clearOverrideCountryCode() {
        try {
            this.mService.clearOverrideCountryCode();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setDefaultCountryCode(String str) {
        try {
            this.mService.setDefaultCountryCode(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public DhcpInfo getDhcpInfo() {
        try {
            return this.mService.getDhcpInfo(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean setWifiEnabled(boolean z) {
        try {
            return this.mService.setWifiEnabled(this.mContext.getOpPackageName(), z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static abstract class SubsystemRestartTrackingCallback {
        private final SubsystemRestartCallbackProxy mProxy = new SubsystemRestartCallbackProxy();

        public abstract void onSubsystemRestarted();

        public abstract void onSubsystemRestarting();

        /* access modifiers changed from: package-private */
        public SubsystemRestartCallbackProxy getProxy() {
            return this.mProxy;
        }

        private static class SubsystemRestartCallbackProxy extends ISubsystemRestartCallback.Stub {
            private SubsystemRestartTrackingCallback mCallback = null;
            private Executor mExecutor = null;
            private final Object mLock = new Object();

            SubsystemRestartCallbackProxy() {
            }

            /* access modifiers changed from: package-private */
            public void initProxy(Executor executor, SubsystemRestartTrackingCallback subsystemRestartTrackingCallback) {
                synchronized (this.mLock) {
                    this.mExecutor = executor;
                    this.mCallback = subsystemRestartTrackingCallback;
                }
            }

            /* access modifiers changed from: package-private */
            public void cleanUpProxy() {
                synchronized (this.mLock) {
                    this.mExecutor = null;
                    this.mCallback = null;
                }
            }

            public void onSubsystemRestarting() {
                Executor executor;
                SubsystemRestartTrackingCallback subsystemRestartTrackingCallback;
                synchronized (this.mLock) {
                    executor = this.mExecutor;
                    subsystemRestartTrackingCallback = this.mCallback;
                }
                if (executor != null && subsystemRestartTrackingCallback != null) {
                    Binder.clearCallingIdentity();
                    Objects.requireNonNull(subsystemRestartTrackingCallback);
                    executor.execute(new C0186x7e50bc9d(subsystemRestartTrackingCallback));
                }
            }

            public void onSubsystemRestarted() {
                Executor executor;
                SubsystemRestartTrackingCallback subsystemRestartTrackingCallback;
                synchronized (this.mLock) {
                    executor = this.mExecutor;
                    subsystemRestartTrackingCallback = this.mCallback;
                }
                if (executor != null && subsystemRestartTrackingCallback != null) {
                    Binder.clearCallingIdentity();
                    Objects.requireNonNull(subsystemRestartTrackingCallback);
                    executor.execute(new C0185x7e50bc9c(subsystemRestartTrackingCallback));
                }
            }
        }
    }

    public void registerSubsystemRestartTrackingCallback(Executor executor, SubsystemRestartTrackingCallback subsystemRestartTrackingCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        } else if (subsystemRestartTrackingCallback != null) {
            SubsystemRestartTrackingCallback.SubsystemRestartCallbackProxy proxy = subsystemRestartTrackingCallback.getProxy();
            proxy.initProxy(executor, subsystemRestartTrackingCallback);
            try {
                this.mService.registerSubsystemRestartCallback(proxy);
            } catch (RemoteException e) {
                proxy.cleanUpProxy();
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    public void unregisterSubsystemRestartTrackingCallback(SubsystemRestartTrackingCallback subsystemRestartTrackingCallback) {
        if (subsystemRestartTrackingCallback != null) {
            SubsystemRestartTrackingCallback.SubsystemRestartCallbackProxy proxy = subsystemRestartTrackingCallback.getProxy();
            try {
                this.mService.unregisterSubsystemRestartCallback(proxy);
                proxy.cleanUpProxy();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                proxy.cleanUpProxy();
                throw th;
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    @SystemApi
    public void restartWifiSubsystem() {
        try {
            this.mService.restartWifiSubsystem();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getWifiState() {
        try {
            return this.mService.getWifiEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiEnabled() {
        return getWifiState() == 3;
    }

    public int calculateSignalLevel(int i) {
        try {
            return this.mService.calculateSignalLevel(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMaxSignalLevel() {
        return calculateSignalLevel(Integer.MAX_VALUE);
    }

    @SystemApi
    public void updateInterfaceIpState(String str, int i) {
        try {
            this.mService.updateInterfaceIpState(str, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isDefaultCoexAlgorithmEnabled() {
        try {
            return this.mService.isDefaultCoexAlgorithmEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setCoexUnsafeChannels(List<CoexUnsafeChannel> list, int i) {
        if (list != null) {
            try {
                this.mService.setCoexUnsafeChannels(list, i);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("unsafeChannels must not be null");
        }
    }

    @SystemApi
    public void registerCoexCallback(Executor executor, CoexCallback coexCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        } else if (coexCallback != null) {
            CoexCallback.CoexCallbackProxy proxy = coexCallback.getProxy();
            proxy.initProxy(executor, coexCallback);
            try {
                this.mService.registerCoexCallback(proxy);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    @SystemApi
    public void unregisterCoexCallback(CoexCallback coexCallback) {
        if (coexCallback != null) {
            CoexCallback.CoexCallbackProxy proxy = coexCallback.getProxy();
            try {
                this.mService.unregisterCoexCallback(proxy);
                proxy.cleanUpProxy();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                proxy.cleanUpProxy();
                throw th;
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    @SystemApi
    public static abstract class CoexCallback {
        private final CoexCallbackProxy mCoexCallbackProxy;

        public abstract void onCoexUnsafeChannelsChanged(List<CoexUnsafeChannel> list, int i);

        public CoexCallback() {
            if (SdkLevel.isAtLeastS()) {
                this.mCoexCallbackProxy = new CoexCallbackProxy();
                return;
            }
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: package-private */
        public CoexCallbackProxy getProxy() {
            return this.mCoexCallbackProxy;
        }

        private static class CoexCallbackProxy extends ICoexCallback.Stub {
            private CoexCallback mCallback = null;
            private Executor mExecutor = null;
            private final Object mLock = new Object();

            CoexCallbackProxy() {
            }

            /* access modifiers changed from: package-private */
            public void initProxy(Executor executor, CoexCallback coexCallback) {
                synchronized (this.mLock) {
                    this.mExecutor = executor;
                    this.mCallback = coexCallback;
                }
            }

            /* access modifiers changed from: package-private */
            public void cleanUpProxy() {
                synchronized (this.mLock) {
                    this.mExecutor = null;
                    this.mCallback = null;
                }
            }

            public void onCoexUnsafeChannelsChanged(List<CoexUnsafeChannel> list, int i) {
                Executor executor;
                CoexCallback coexCallback;
                synchronized (this.mLock) {
                    executor = this.mExecutor;
                    coexCallback = this.mCallback;
                }
                if (executor != null && coexCallback != null) {
                    Binder.clearCallingIdentity();
                    executor.execute(new C0165x6ae08e5(coexCallback, list, i));
                }
            }
        }
    }

    public boolean startSoftAp(WifiConfiguration wifiConfiguration) {
        try {
            return this.mService.startSoftAp(wifiConfiguration, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean startTetheredHotspot(SoftApConfiguration softApConfiguration) {
        try {
            return this.mService.startTetheredHotspot(softApConfiguration, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean stopSoftAp() {
        try {
            return this.mService.stopSoftAp();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startLocalOnlyHotspot(LocalOnlyHotspotCallback localOnlyHotspotCallback, Handler handler) {
        startLocalOnlyHotspotInternal((SoftApConfiguration) null, handler == null ? null : new HandlerExecutor(handler), localOnlyHotspotCallback);
    }

    @SystemApi
    public void startLocalOnlyHotspot(SoftApConfiguration softApConfiguration, Executor executor, LocalOnlyHotspotCallback localOnlyHotspotCallback) {
        Objects.requireNonNull(softApConfiguration);
        startLocalOnlyHotspotInternal(softApConfiguration, executor, localOnlyHotspotCallback);
    }

    private void startLocalOnlyHotspotInternal(SoftApConfiguration softApConfiguration, Executor executor, LocalOnlyHotspotCallback localOnlyHotspotCallback) {
        if (executor == null) {
            executor = this.mContext.getMainExecutor();
        }
        synchronized (this.mLock) {
            LocalOnlyHotspotCallbackProxy localOnlyHotspotCallbackProxy = new LocalOnlyHotspotCallbackProxy(this, executor, localOnlyHotspotCallback);
            try {
                String opPackageName = this.mContext.getOpPackageName();
                String attributionTag = this.mContext.getAttributionTag();
                Bundle bundle = new Bundle();
                if (SdkLevel.isAtLeastS()) {
                    bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                }
                int startLocalOnlyHotspot = this.mService.startLocalOnlyHotspot(localOnlyHotspotCallbackProxy, opPackageName, attributionTag, softApConfiguration, bundle);
                if (startLocalOnlyHotspot != 0) {
                    localOnlyHotspotCallbackProxy.onHotspotFailed(startLocalOnlyHotspot);
                } else {
                    this.mLOHSCallbackProxy = localOnlyHotspotCallbackProxy;
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void cancelLocalOnlyHotspotRequest() {
        synchronized (this.mLock) {
            stopLocalOnlyHotspot();
        }
    }

    /* access modifiers changed from: private */
    public void stopLocalOnlyHotspot() {
        synchronized (this.mLock) {
            if (this.mLOHSCallbackProxy != null) {
                this.mLOHSCallbackProxy = null;
                try {
                    this.mService.stopLocalOnlyHotspot();
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @SystemApi
    public void registerLocalOnlyHotspotSoftApCallback(Executor executor, SoftApCallback softApCallback) {
        if (!SdkLevel.isAtLeastT()) {
            throw new UnsupportedOperationException();
        } else if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (softApCallback != null) {
            Log.v(TAG, "registerLocalOnlyHotspotSoftApCallback: callback=" + softApCallback + ", executor=" + executor);
            try {
                SparseArray<ISoftApCallback> sparseArray = sLocalOnlyHotspotSoftApCallbackMap;
                synchronized (sparseArray) {
                    SoftApCallbackProxy softApCallbackProxy = new SoftApCallbackProxy(executor, softApCallback, 2);
                    sparseArray.put(System.identityHashCode(softApCallback), softApCallbackProxy);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                    this.mService.registerLocalOnlyHotspotSoftApCallback(softApCallbackProxy, bundle);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void unregisterLocalOnlyHotspotSoftApCallback(SoftApCallback softApCallback) {
        if (!SdkLevel.isAtLeastT()) {
            throw new UnsupportedOperationException();
        } else if (softApCallback != null) {
            Log.v(TAG, "unregisterLocalOnlyHotspotSoftApCallback: callback=" + softApCallback);
            try {
                SparseArray<ISoftApCallback> sparseArray = sLocalOnlyHotspotSoftApCallbackMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(softApCallback);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_PARAM_KEY_ATTRIBUTION_SOURCE, this.mContext.getAttributionSource());
                    this.mService.unregisterLocalOnlyHotspotSoftApCallback(sparseArray.get(identityHashCode), bundle);
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    public void watchLocalOnlyHotspot(LocalOnlyHotspotObserver localOnlyHotspotObserver, Handler handler) {
        Executor executor;
        if (handler == null) {
            executor = this.mContext.getMainExecutor();
        } else {
            executor = new HandlerExecutor(handler);
        }
        synchronized (this.mLock) {
            LocalOnlyHotspotObserverProxy localOnlyHotspotObserverProxy = new LocalOnlyHotspotObserverProxy(this, executor, localOnlyHotspotObserver);
            this.mLOHSObserverProxy = localOnlyHotspotObserverProxy;
            try {
                this.mService.startWatchLocalOnlyHotspot(localOnlyHotspotObserverProxy);
                this.mLOHSObserverProxy.registered();
            } catch (RemoteException e) {
                this.mLOHSObserverProxy = null;
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void unregisterLocalOnlyHotspotObserver() {
        synchronized (this.mLock) {
            if (this.mLOHSObserverProxy != null) {
                this.mLOHSObserverProxy = null;
                try {
                    this.mService.stopWatchLocalOnlyHotspot();
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @SystemApi
    public int getWifiApState() {
        try {
            return this.mService.getWifiApEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    @SystemApi
    @Deprecated
    public WifiConfiguration getWifiApConfiguration() {
        try {
            return this.mService.getWifiApConfiguration();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public SoftApConfiguration getSoftApConfiguration() {
        try {
            return this.mService.getSoftApConfiguration();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration) {
        try {
            return this.mService.setWifiApConfiguration(wifiConfiguration, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean setSoftApConfiguration(SoftApConfiguration softApConfiguration) {
        try {
            return this.mService.setSoftApConfiguration(softApConfiguration, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabled(InetAddress inetAddress, boolean z) {
        try {
            this.mService.enableTdls(inetAddress.getHostAddress(), z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabledWithMacAddress(String str, boolean z) {
        try {
            this.mService.enableTdlsWithMacAddress(str, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private class SoftApCallbackProxy extends ISoftApCallback.Stub {
        private final SoftApCallback mCallback;
        private Map<String, List<WifiClient>> mCurrentClients = new HashMap();
        private Map<String, SoftApInfo> mCurrentInfos = new HashMap();
        private final Executor mExecutor;
        private final int mIpMode;

        private List<WifiClient> getConnectedClientList(Map<String, List<WifiClient>> map) {
            ArrayList arrayList = new ArrayList();
            for (List<WifiClient> addAll : map.values()) {
                arrayList.addAll(addAll);
            }
            return arrayList;
        }

        SoftApCallbackProxy(Executor executor, SoftApCallback softApCallback, int i) {
            this.mExecutor = executor;
            this.mCallback = softApCallback;
            this.mIpMode = i;
        }

        public void onStateChanged(int i, int i2) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", onStateChanged: state=" + i + ", failureReason=" + i2);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda6(this, i, i2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStateChanged$0$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5105xee92bc93(int i, int i2) {
            this.mCallback.onStateChanged(i, i2);
        }

        public void onConnectedClientsOrInfoChanged(Map<String, SoftApInfo> map, Map<String, List<WifiClient>> map2, boolean z, boolean z2) {
            ArrayList arrayList;
            SoftApInfo softApInfo;
            ArrayList arrayList2;
            Iterator<SoftApInfo> it;
            Map<String, SoftApInfo> map3 = map;
            Map<String, List<WifiClient>> map4 = map2;
            boolean z3 = z;
            boolean z4 = z2;
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", onConnectedClientsOrInfoChanged: clients: " + map4 + ", infos: " + map3 + ", isBridged is " + z3 + ", isRegistration is " + z4);
            }
            ArrayList arrayList3 = new ArrayList(map.values());
            HashMap hashMap = new HashMap();
            boolean z5 = map.size() == 0 && getConnectedClientList(map4).size() != getConnectedClientList(this.mCurrentClients).size();
            boolean z6 = map.size() != this.mCurrentInfos.size();
            if (z4) {
                for (SoftApInfo next : map.values()) {
                    String apInstanceIdentifier = next.getApInstanceIdentifier();
                    if (map4.getOrDefault(apInstanceIdentifier, Collections.emptyList()).size() > 0) {
                        hashMap.put(next, map4.get(apInstanceIdentifier));
                    }
                }
            }
            Iterator<SoftApInfo> it2 = this.mCurrentInfos.values().iterator();
            while (it2.hasNext()) {
                SoftApInfo next2 = it2.next();
                String apInstanceIdentifier2 = next2.getApInstanceIdentifier();
                List orDefault = map4.getOrDefault(apInstanceIdentifier2, Collections.emptyList());
                if (!arrayList3.contains(next2)) {
                    if (this.mCurrentClients.getOrDefault(apInstanceIdentifier2, Collections.emptyList()).size() > 0) {
                        SoftApInfo softApInfo2 = map3.get(apInstanceIdentifier2);
                        if (softApInfo2 == null || softApInfo2.getFrequency() == 0) {
                            Log.d(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", info changed on client connected instance(AP disabled)");
                            hashMap.put(next2, Collections.emptyList());
                        } else {
                            Log.d(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", info changed on client connected instance");
                            hashMap.put(softApInfo2, orDefault);
                        }
                    }
                    arrayList2 = arrayList3;
                    it = it2;
                    z6 = true;
                } else {
                    it = it2;
                    arrayList2 = arrayList3;
                    if (orDefault.size() != this.mCurrentClients.getOrDefault(apInstanceIdentifier2, Collections.emptyList()).size()) {
                        hashMap.put(next2, orDefault);
                    }
                }
                it2 = it;
                arrayList3 = arrayList2;
            }
            ArrayList arrayList4 = arrayList3;
            this.mCurrentClients = map4;
            this.mCurrentInfos = map3;
            if (z6 || !hashMap.isEmpty() || z4 || z5) {
                Binder.clearCallingIdentity();
                for (SoftApInfo softApInfo3 : hashMap.keySet()) {
                    Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", send onConnectedClientsChanged, changedInfo is " + softApInfo3 + " and clients are " + hashMap.get(softApInfo3));
                    this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda1(this, softApInfo3, hashMap));
                }
                if (z6 || z4) {
                    if (!z3) {
                        if (arrayList4.isEmpty()) {
                            softApInfo = new SoftApInfo();
                            arrayList = arrayList4;
                        } else {
                            arrayList = arrayList4;
                            softApInfo = (SoftApInfo) arrayList.get(0);
                        }
                        Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", send InfoChanged, newInfo: " + softApInfo);
                        this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda2(this, softApInfo));
                    } else {
                        arrayList = arrayList4;
                    }
                    Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", send InfoChanged, changedInfoList: " + arrayList);
                    this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda3(this, arrayList));
                }
                if (z4 || !hashMap.isEmpty() || z5) {
                    Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", send onConnectedClientsChanged(clients): " + getConnectedClientList(map4));
                    this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda4(this, map4));
                    return;
                }
                return;
            }
            Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", No changed & Not Registration don't need to notify the client");
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectedClientsOrInfoChanged$1$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5101x55fb2da9(SoftApInfo softApInfo, Map map) {
            this.mCallback.onConnectedClientsChanged(softApInfo, (List) map.get(softApInfo));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectedClientsOrInfoChanged$2$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5102x47a4d3c8(SoftApInfo softApInfo) {
            this.mCallback.onInfoChanged(softApInfo);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectedClientsOrInfoChanged$3$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5103x394e79e7(List list) {
            this.mCallback.onInfoChanged((List<SoftApInfo>) list);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectedClientsOrInfoChanged$4$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5104x2af82006(Map map) {
            this.mCallback.onConnectedClientsChanged(getConnectedClientList(map));
        }

        public void onCapabilityChanged(SoftApCapability softApCapability) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ",  onCapabilityChanged: SoftApCapability = " + softApCapability);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda0(this, softApCapability));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onCapabilityChanged$5$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5100x8df75e6d(SoftApCapability softApCapability) {
            this.mCallback.onCapabilityChanged(softApCapability);
        }

        public void onBlockedClientConnecting(WifiClient wifiClient, int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "SoftApCallbackProxy on mode " + this.mIpMode + ", onBlockedClientConnecting: client =" + wifiClient + " with reason = " + i);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda5(this, wifiClient, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBlockedClientConnecting$6$android-net-wifi-WifiManager$SoftApCallbackProxy */
        public /* synthetic */ void mo5099x47984039(WifiClient wifiClient, int i) {
            this.mCallback.onBlockedClientConnecting(wifiClient, i);
        }
    }

    @SystemApi
    public void registerSoftApCallback(Executor executor, SoftApCallback softApCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (softApCallback != null) {
            Log.v(TAG, "registerSoftApCallback: callback=" + softApCallback + ", executor=" + executor);
            try {
                SparseArray<ISoftApCallback> sparseArray = sSoftApCallbackMap;
                synchronized (sparseArray) {
                    SoftApCallbackProxy softApCallbackProxy = new SoftApCallbackProxy(executor, softApCallback, 1);
                    sparseArray.put(System.identityHashCode(softApCallback), softApCallbackProxy);
                    this.mService.registerSoftApCallback(softApCallbackProxy);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void unregisterSoftApCallback(SoftApCallback softApCallback) {
        if (softApCallback != null) {
            Log.v(TAG, "unregisterSoftApCallback: callback=" + softApCallback);
            try {
                SparseArray<ISoftApCallback> sparseArray = sSoftApCallbackMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(softApCallback);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.unregisterSoftApCallback(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    public class LocalOnlyHotspotReservation implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private boolean mClosed = false;
        private final SoftApConfiguration mSoftApConfig;
        private final WifiConfiguration mWifiConfig;

        public LocalOnlyHotspotReservation(SoftApConfiguration softApConfiguration) {
            CloseGuard closeGuard = new CloseGuard();
            this.mCloseGuard = closeGuard;
            this.mSoftApConfig = softApConfiguration;
            this.mWifiConfig = softApConfiguration.toWifiConfiguration();
            closeGuard.open("close");
        }

        @Deprecated
        public WifiConfiguration getWifiConfiguration() {
            return this.mWifiConfig;
        }

        public SoftApConfiguration getSoftApConfiguration() {
            return this.mSoftApConfig;
        }

        public void close() {
            try {
                synchronized (WifiManager.this.mLock) {
                    if (!this.mClosed) {
                        this.mClosed = true;
                        WifiManager.this.stopLocalOnlyHotspot();
                        this.mCloseGuard.close();
                    }
                }
            } catch (Exception unused) {
                try {
                    Log.e(WifiManager.TAG, "Failed to stop Local Only Hotspot.");
                } catch (Throwable th) {
                    Reference.reachabilityFence(this);
                    throw th;
                }
            }
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
    }

    private static class LocalOnlyHotspotCallbackProxy extends ILocalOnlyHotspotCallback.Stub {
        private final LocalOnlyHotspotCallback mCallback;
        private final Executor mExecutor;
        private final WeakReference<WifiManager> mWifiManager;

        LocalOnlyHotspotCallbackProxy(WifiManager wifiManager, Executor executor, LocalOnlyHotspotCallback localOnlyHotspotCallback) {
            this.mWifiManager = new WeakReference<>(wifiManager);
            this.mExecutor = executor;
            this.mCallback = localOnlyHotspotCallback;
        }

        public void onHotspotStarted(SoftApConfiguration softApConfiguration) {
            WifiManager wifiManager = this.mWifiManager.get();
            if (wifiManager != null) {
                if (softApConfiguration == null) {
                    Log.e(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: config cannot be null.");
                    onHotspotFailed(2);
                    return;
                }
                Objects.requireNonNull(wifiManager);
                LocalOnlyHotspotReservation localOnlyHotspotReservation = new LocalOnlyHotspotReservation(softApConfiguration);
                if (this.mCallback != null) {
                    this.mExecutor.execute(new C0166x7b940274(this, localOnlyHotspotReservation));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotStarted$0$android-net-wifi-WifiManager$LocalOnlyHotspotCallbackProxy */
        public /* synthetic */ void mo5036x8c08aba5(LocalOnlyHotspotReservation localOnlyHotspotReservation) {
            this.mCallback.onStarted(localOnlyHotspotReservation);
        }

        public void onHotspotStopped() {
            if (this.mWifiManager.get() != null) {
                Log.w(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: hotspot stopped");
                if (this.mCallback != null) {
                    this.mExecutor.execute(new C0167x7b940275(this));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotStopped$1$android-net-wifi-WifiManager$LocalOnlyHotspotCallbackProxy */
        public /* synthetic */ void mo5037xcba2e938() {
            this.mCallback.onStopped();
        }

        public void onHotspotFailed(int i) {
            if (this.mWifiManager.get() != null) {
                Log.w(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: failed to start.  reason: " + i);
                if (this.mCallback != null) {
                    this.mExecutor.execute(new C0168x7b940276(this, i));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotFailed$2$android-net-wifi-WifiManager$LocalOnlyHotspotCallbackProxy */
        public /* synthetic */ void mo5035xa22e29e1(int i) {
            this.mCallback.onFailed(i);
        }
    }

    public class LocalOnlyHotspotSubscription implements AutoCloseable {
        private final CloseGuard mCloseGuard;

        public LocalOnlyHotspotSubscription() {
            CloseGuard closeGuard = new CloseGuard();
            this.mCloseGuard = closeGuard;
            closeGuard.open("close");
        }

        public void close() {
            try {
                WifiManager.this.unregisterLocalOnlyHotspotObserver();
                this.mCloseGuard.close();
            } catch (Exception unused) {
                Log.e(WifiManager.TAG, "Failed to unregister LocalOnlyHotspotObserver.");
            } catch (Throwable th) {
                Reference.reachabilityFence(this);
                throw th;
            }
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
    }

    private static class LocalOnlyHotspotObserverProxy extends ILocalOnlyHotspotCallback.Stub {
        private final Executor mExecutor;
        private final LocalOnlyHotspotObserver mObserver;
        private final WeakReference<WifiManager> mWifiManager;

        public void onHotspotFailed(int i) {
        }

        LocalOnlyHotspotObserverProxy(WifiManager wifiManager, Executor executor, LocalOnlyHotspotObserver localOnlyHotspotObserver) {
            this.mWifiManager = new WeakReference<>(wifiManager);
            this.mExecutor = executor;
            this.mObserver = localOnlyHotspotObserver;
        }

        public void registered() throws RemoteException {
            WifiManager wifiManager = this.mWifiManager.get();
            if (wifiManager != null) {
                this.mExecutor.execute(new C0171xcaf6bdc5(this, wifiManager));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registered$0$android-net-wifi-WifiManager$LocalOnlyHotspotObserverProxy */
        public /* synthetic */ void mo5043x7738783(WifiManager wifiManager) {
            LocalOnlyHotspotObserver localOnlyHotspotObserver = this.mObserver;
            Objects.requireNonNull(wifiManager);
            localOnlyHotspotObserver.onRegistered(new LocalOnlyHotspotSubscription());
        }

        public void onHotspotStarted(SoftApConfiguration softApConfiguration) {
            if (this.mWifiManager.get() != null) {
                if (softApConfiguration == null) {
                    Log.e(WifiManager.TAG, "LocalOnlyHotspotObserverProxy: config cannot be null.");
                } else {
                    this.mExecutor.execute(new C0169xcaf6bdc3(this, softApConfiguration));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotStarted$1$android-net-wifi-WifiManager$LocalOnlyHotspotObserverProxy */
        public /* synthetic */ void mo5041x6b9f3713(SoftApConfiguration softApConfiguration) {
            this.mObserver.onStarted(softApConfiguration);
        }

        public void onHotspotStopped() {
            if (this.mWifiManager.get() != null) {
                this.mExecutor.execute(new C0170xcaf6bdc4(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotStopped$2$android-net-wifi-WifiManager$LocalOnlyHotspotObserverProxy */
        public /* synthetic */ void mo5042xab3974a6() {
            this.mObserver.onStopped();
        }
    }

    private class ActionListenerProxy extends IActionListener.Stub {
        private final String mActionTag;
        private final ActionListener mCallback;
        private final Handler mHandler;

        ActionListenerProxy(String str, Looper looper, ActionListener actionListener) {
            this.mActionTag = str;
            this.mHandler = new Handler(looper);
            this.mCallback = actionListener;
        }

        public void onSuccess() {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "ActionListenerProxy:" + this.mActionTag + ": onSuccess");
            }
            this.mHandler.post(new WifiManager$ActionListenerProxy$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSuccess$0$android-net-wifi-WifiManager$ActionListenerProxy */
        public /* synthetic */ void mo5014xfef70dbf() {
            this.mCallback.onSuccess();
        }

        public void onFailure(int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "ActionListenerProxy:" + this.mActionTag + ": onFailure=" + i);
            }
            this.mHandler.post(new WifiManager$ActionListenerProxy$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFailure$1$android-net-wifi-WifiManager$ActionListenerProxy */
        public /* synthetic */ void mo5013xe6e78af7(int i) {
            this.mCallback.onFailure(i);
        }
    }

    private void connectInternal(WifiConfiguration wifiConfiguration, int i, ActionListener actionListener) {
        ActionListenerProxy actionListenerProxy = actionListener != null ? new ActionListenerProxy(SecurityConstants.SOCKET_CONNECT_ACTION, this.mLooper, actionListener) : null;
        try {
            this.mService.connect(wifiConfiguration, i, actionListenerProxy, this.mContext.getOpPackageName());
        } catch (RemoteException unused) {
            if (actionListenerProxy != null) {
                actionListenerProxy.onFailure(0);
            }
        } catch (SecurityException unused2) {
            if (actionListenerProxy != null) {
                actionListenerProxy.onFailure(4);
            }
        }
    }

    @SystemApi
    public void connect(WifiConfiguration wifiConfiguration, ActionListener actionListener) {
        if (wifiConfiguration != null) {
            connectInternal(wifiConfiguration, -1, actionListener);
            return;
        }
        throw new IllegalArgumentException("config cannot be null");
    }

    @SystemApi
    public void connect(int i, ActionListener actionListener) {
        if (i >= 0) {
            connectInternal((WifiConfiguration) null, i, actionListener);
            return;
        }
        throw new IllegalArgumentException("Network id cannot be negative");
    }

    @SystemApi
    public void startRestrictingAutoJoinToSubscriptionId(int i) {
        try {
            this.mService.startRestrictingAutoJoinToSubscriptionId(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void stopRestrictingAutoJoinToSubscriptionId() {
        try {
            this.mService.stopRestrictingAutoJoinToSubscriptionId();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void save(WifiConfiguration wifiConfiguration, ActionListener actionListener) {
        if (wifiConfiguration != null) {
            ActionListenerProxy actionListenerProxy = actionListener != null ? new ActionListenerProxy("save", this.mLooper, actionListener) : null;
            try {
                this.mService.save(wifiConfiguration, actionListenerProxy, this.mContext.getOpPackageName());
            } catch (RemoteException unused) {
                if (actionListenerProxy != null) {
                    actionListenerProxy.onFailure(0);
                }
            } catch (SecurityException unused2) {
                if (actionListenerProxy != null) {
                    actionListenerProxy.onFailure(4);
                }
            }
        } else {
            throw new IllegalArgumentException("config cannot be null");
        }
    }

    @SystemApi
    public void forget(int i, ActionListener actionListener) {
        if (i >= 0) {
            ActionListenerProxy actionListenerProxy = actionListener != null ? new ActionListenerProxy("forget", this.mLooper, actionListener) : null;
            try {
                this.mService.forget(i, actionListenerProxy);
            } catch (RemoteException unused) {
                if (actionListenerProxy != null) {
                    actionListenerProxy.onFailure(0);
                }
            } catch (SecurityException unused2) {
                if (actionListenerProxy != null) {
                    actionListenerProxy.onFailure(4);
                }
            }
        } else {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
    }

    @SystemApi
    @Deprecated
    public void disable(int i, ActionListener actionListener) {
        if (i >= 0) {
            boolean disableNetwork = disableNetwork(i);
            if (actionListener == null) {
                return;
            }
            if (disableNetwork) {
                actionListener.onSuccess();
            } else {
                actionListener.onFailure(0);
            }
        } else {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
    }

    public void allowAutojoinGlobal(boolean z) {
        try {
            this.mService.allowAutojoinGlobal(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void queryAutojoinGlobal(final Executor executor, final Consumer<Boolean> consumer) {
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(consumer, "resultsCallback cannot be null");
        try {
            this.mService.queryAutojoinGlobal(new IBooleanListener.Stub() {
                public void onResult(boolean z) {
                    Binder.clearCallingIdentity();
                    executor.execute(new WifiManager$1$$ExternalSyntheticLambda0(consumer, z));
                }
            });
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void allowAutojoin(int i, boolean z) {
        try {
            this.mService.allowAutojoin(i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void allowAutojoinPasspoint(String str, boolean z) {
        try {
            this.mService.allowAutojoinPasspoint(str, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setMacRandomizationSettingPasspointEnabled(String str, boolean z) {
        try {
            this.mService.setMacRandomizationSettingPasspointEnabled(str, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setPasspointMeteredOverride(String str, int i) {
        try {
            this.mService.setPasspointMeteredOverride(str, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void disableEphemeralNetwork(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.mService.disableEphemeralNetwork(str, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("SSID cannot be null or empty!");
        }
    }

    public void startWps(WpsInfo wpsInfo, WpsCallback wpsCallback) {
        if (wpsCallback != null) {
            wpsCallback.onFailed(0);
        }
    }

    public void cancelWps(WpsCallback wpsCallback) {
        if (wpsCallback != null) {
            wpsCallback.onFailed(0);
        }
    }

    public class WifiLock {
        private final IBinder mBinder;
        private boolean mHeld;
        int mLockType;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;
        private WorkSource mWorkSource;

        private WifiLock(int i, String str) {
            this.mTag = str;
            this.mLockType = i;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
            if (r7.mHeld == false) goto L_0x0014;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void acquire() {
            /*
                r7 = this;
                android.os.IBinder r0 = r7.mBinder
                monitor-enter(r0)
                boolean r1 = r7.mRefCounted     // Catch:{ all -> 0x005a }
                r2 = 1
                if (r1 == 0) goto L_0x0010
                int r1 = r7.mRefCount     // Catch:{ all -> 0x005a }
                int r1 = r1 + r2
                r7.mRefCount = r1     // Catch:{ all -> 0x005a }
                if (r1 != r2) goto L_0x0058
                goto L_0x0014
            L_0x0010:
                boolean r1 = r7.mHeld     // Catch:{ all -> 0x005a }
                if (r1 != 0) goto L_0x0058
            L_0x0014:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0052 }
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch:{ RemoteException -> 0x0052 }
                android.os.IBinder r3 = r7.mBinder     // Catch:{ RemoteException -> 0x0052 }
                int r4 = r7.mLockType     // Catch:{ RemoteException -> 0x0052 }
                java.lang.String r5 = r7.mTag     // Catch:{ RemoteException -> 0x0052 }
                android.os.WorkSource r6 = r7.mWorkSource     // Catch:{ RemoteException -> 0x0052 }
                r1.acquireWifiLock(r3, r4, r5, r6)     // Catch:{ RemoteException -> 0x0052 }
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0052 }
                monitor-enter(r1)     // Catch:{ RemoteException -> 0x0052 }
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004f }
                int r3 = r3.mActiveLockCount     // Catch:{ all -> 0x004f }
                r4 = 50
                if (r3 >= r4) goto L_0x003e
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004f }
                int r4 = r3.mActiveLockCount     // Catch:{ all -> 0x004f }
                int r4 = r4 + r2
                r3.mActiveLockCount = r4     // Catch:{ all -> 0x004f }
                monitor-exit(r1)     // Catch:{ all -> 0x004f }
                r7.mHeld = r2     // Catch:{ all -> 0x005a }
                goto L_0x0058
            L_0x003e:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004f }
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch:{ all -> 0x004f }
                android.os.IBinder r7 = r7.mBinder     // Catch:{ all -> 0x004f }
                r2.releaseWifiLock(r7)     // Catch:{ all -> 0x004f }
                java.lang.UnsupportedOperationException r7 = new java.lang.UnsupportedOperationException     // Catch:{ all -> 0x004f }
                java.lang.String r2 = "Exceeded maximum number of wifi locks"
                r7.<init>((java.lang.String) r2)     // Catch:{ all -> 0x004f }
                throw r7     // Catch:{ all -> 0x004f }
            L_0x004f:
                r7 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x004f }
                throw r7     // Catch:{ RemoteException -> 0x0052 }
            L_0x0052:
                r7 = move-exception
                java.lang.RuntimeException r7 = r7.rethrowFromSystemServer()     // Catch:{ all -> 0x005a }
                throw r7     // Catch:{ all -> 0x005a }
            L_0x0058:
                monitor-exit(r0)     // Catch:{ all -> 0x005a }
                return
            L_0x005a:
                r7 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x005a }
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.acquire():void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
            if (r5.mHeld != false) goto L_0x0016;
         */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x003f A[DONT_GENERATE] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0041  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void release() {
            /*
                r5 = this;
                java.lang.String r0 = "WifiLock under-locked "
                android.os.IBinder r1 = r5.mBinder
                monitor-enter(r1)
                boolean r2 = r5.mRefCounted     // Catch:{ all -> 0x0055 }
                if (r2 == 0) goto L_0x0012
                int r2 = r5.mRefCount     // Catch:{ all -> 0x0055 }
                int r2 = r2 + -1
                r5.mRefCount = r2     // Catch:{ all -> 0x0055 }
                if (r2 != 0) goto L_0x003b
                goto L_0x0016
            L_0x0012:
                boolean r2 = r5.mHeld     // Catch:{ all -> 0x0055 }
                if (r2 == 0) goto L_0x003b
            L_0x0016:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch:{ RemoteException -> 0x0035 }
                android.os.IBinder r3 = r5.mBinder     // Catch:{ RemoteException -> 0x0035 }
                r2.releaseWifiLock(r3)     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0035 }
                monitor-enter(r2)     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x0032 }
                int r4 = r3.mActiveLockCount     // Catch:{ all -> 0x0032 }
                int r4 = r4 + -1
                r3.mActiveLockCount = r4     // Catch:{ all -> 0x0032 }
                monitor-exit(r2)     // Catch:{ all -> 0x0032 }
                r2 = 0
                r5.mHeld = r2     // Catch:{ all -> 0x0055 }
                goto L_0x003b
            L_0x0032:
                r5 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0032 }
                throw r5     // Catch:{ RemoteException -> 0x0035 }
            L_0x0035:
                r5 = move-exception
                java.lang.RuntimeException r5 = r5.rethrowFromSystemServer()     // Catch:{ all -> 0x0055 }
                throw r5     // Catch:{ all -> 0x0055 }
            L_0x003b:
                int r2 = r5.mRefCount     // Catch:{ all -> 0x0055 }
                if (r2 < 0) goto L_0x0041
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                return
            L_0x0041:
                java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ all -> 0x0055 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0055 }
                r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0055 }
                java.lang.String r5 = r5.mTag     // Catch:{ all -> 0x0055 }
                r3.append((java.lang.String) r5)     // Catch:{ all -> 0x0055 }
                java.lang.String r5 = r3.toString()     // Catch:{ all -> 0x0055 }
                r2.<init>((java.lang.String) r5)     // Catch:{ all -> 0x0055 }
                throw r2     // Catch:{ all -> 0x0055 }
            L_0x0055:
                r5 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.release():void");
        }

        public void setReferenceCounted(boolean z) {
            this.mRefCounted = z;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public void setWorkSource(WorkSource workSource) {
            synchronized (this.mBinder) {
                if (workSource != null) {
                    try {
                        if (workSource.isEmpty()) {
                            workSource = null;
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                boolean z = true;
                if (workSource == null) {
                    this.mWorkSource = null;
                } else {
                    WorkSource withoutNames = workSource.withoutNames();
                    WorkSource workSource2 = this.mWorkSource;
                    if (workSource2 == null) {
                        if (workSource2 == null) {
                            z = false;
                        }
                        this.mWorkSource = new WorkSource(withoutNames);
                    } else {
                        z = true ^ workSource2.equals(withoutNames);
                        if (z) {
                            this.mWorkSource.set(withoutNames);
                        }
                    }
                }
                if (z && this.mHeld) {
                    WifiManager.this.mService.updateWifiLockWorkSource(this.mBinder, this.mWorkSource);
                }
            }
        }

        public String toString() {
            String str;
            String str2;
            synchronized (this.mBinder) {
                String hexString = Integer.toHexString(System.identityHashCode(this));
                String str3 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    str = "refcounted: refcount = " + this.mRefCount;
                } else {
                    str = "not refcounted";
                }
                str2 = "WifiLock{ " + hexString + "; " + str3 + str + " }";
            }
            return str2;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            super.finalize();
            synchronized (this.mBinder) {
                if (this.mHeld) {
                    try {
                        WifiManager.this.mService.releaseWifiLock(this.mBinder);
                        synchronized (WifiManager.this) {
                            WifiManager wifiManager = WifiManager.this;
                            wifiManager.mActiveLockCount = wifiManager.mActiveLockCount - 1;
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    public WifiLock createWifiLock(int i, String str) {
        return new WifiLock(i, str);
    }

    @Deprecated
    public WifiLock createWifiLock(String str) {
        return new WifiLock(1, str);
    }

    public MulticastLock createMulticastLock(String str) {
        return new MulticastLock(str);
    }

    public class MulticastLock {
        private final IBinder mBinder;
        private boolean mHeld;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;

        private MulticastLock(String str) {
            this.mTag = str;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
            if (r5.mHeld == false) goto L_0x0014;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void acquire() {
            /*
                r5 = this;
                android.os.IBinder r0 = r5.mBinder
                monitor-enter(r0)
                boolean r1 = r5.mRefCounted     // Catch:{ all -> 0x0056 }
                r2 = 1
                if (r1 == 0) goto L_0x0010
                int r1 = r5.mRefCount     // Catch:{ all -> 0x0056 }
                int r1 = r1 + r2
                r5.mRefCount = r1     // Catch:{ all -> 0x0056 }
                if (r1 != r2) goto L_0x0054
                goto L_0x0014
            L_0x0010:
                boolean r1 = r5.mHeld     // Catch:{ all -> 0x0056 }
                if (r1 != 0) goto L_0x0054
            L_0x0014:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x004e }
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch:{ RemoteException -> 0x004e }
                android.os.IBinder r3 = r5.mBinder     // Catch:{ RemoteException -> 0x004e }
                java.lang.String r4 = r5.mTag     // Catch:{ RemoteException -> 0x004e }
                r1.acquireMulticastLock(r3, r4)     // Catch:{ RemoteException -> 0x004e }
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x004e }
                monitor-enter(r1)     // Catch:{ RemoteException -> 0x004e }
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004b }
                int r3 = r3.mActiveLockCount     // Catch:{ all -> 0x004b }
                r4 = 50
                if (r3 >= r4) goto L_0x003a
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004b }
                int r4 = r3.mActiveLockCount     // Catch:{ all -> 0x004b }
                int r4 = r4 + r2
                r3.mActiveLockCount = r4     // Catch:{ all -> 0x004b }
                monitor-exit(r1)     // Catch:{ all -> 0x004b }
                r5.mHeld = r2     // Catch:{ all -> 0x0056 }
                goto L_0x0054
            L_0x003a:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x004b }
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch:{ all -> 0x004b }
                java.lang.String r5 = r5.mTag     // Catch:{ all -> 0x004b }
                r2.releaseMulticastLock(r5)     // Catch:{ all -> 0x004b }
                java.lang.UnsupportedOperationException r5 = new java.lang.UnsupportedOperationException     // Catch:{ all -> 0x004b }
                java.lang.String r2 = "Exceeded maximum number of wifi locks"
                r5.<init>((java.lang.String) r2)     // Catch:{ all -> 0x004b }
                throw r5     // Catch:{ all -> 0x004b }
            L_0x004b:
                r5 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x004b }
                throw r5     // Catch:{ RemoteException -> 0x004e }
            L_0x004e:
                r5 = move-exception
                java.lang.RuntimeException r5 = r5.rethrowFromSystemServer()     // Catch:{ all -> 0x0056 }
                throw r5     // Catch:{ all -> 0x0056 }
            L_0x0054:
                monitor-exit(r0)     // Catch:{ all -> 0x0056 }
                return
            L_0x0056:
                r5 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0056 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.acquire():void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
            if (r5.mHeld != false) goto L_0x0016;
         */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x003f A[DONT_GENERATE] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0041  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void release() {
            /*
                r5 = this;
                java.lang.String r0 = "MulticastLock under-locked "
                android.os.IBinder r1 = r5.mBinder
                monitor-enter(r1)
                boolean r2 = r5.mRefCounted     // Catch:{ all -> 0x0055 }
                if (r2 == 0) goto L_0x0012
                int r2 = r5.mRefCount     // Catch:{ all -> 0x0055 }
                int r2 = r2 + -1
                r5.mRefCount = r2     // Catch:{ all -> 0x0055 }
                if (r2 != 0) goto L_0x003b
                goto L_0x0016
            L_0x0012:
                boolean r2 = r5.mHeld     // Catch:{ all -> 0x0055 }
                if (r2 == 0) goto L_0x003b
            L_0x0016:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch:{ RemoteException -> 0x0035 }
                java.lang.String r3 = r5.mTag     // Catch:{ RemoteException -> 0x0035 }
                r2.releaseMulticastLock(r3)     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch:{ RemoteException -> 0x0035 }
                monitor-enter(r2)     // Catch:{ RemoteException -> 0x0035 }
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch:{ all -> 0x0032 }
                int r4 = r3.mActiveLockCount     // Catch:{ all -> 0x0032 }
                int r4 = r4 + -1
                r3.mActiveLockCount = r4     // Catch:{ all -> 0x0032 }
                monitor-exit(r2)     // Catch:{ all -> 0x0032 }
                r2 = 0
                r5.mHeld = r2     // Catch:{ all -> 0x0055 }
                goto L_0x003b
            L_0x0032:
                r5 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0032 }
                throw r5     // Catch:{ RemoteException -> 0x0035 }
            L_0x0035:
                r5 = move-exception
                java.lang.RuntimeException r5 = r5.rethrowFromSystemServer()     // Catch:{ all -> 0x0055 }
                throw r5     // Catch:{ all -> 0x0055 }
            L_0x003b:
                int r2 = r5.mRefCount     // Catch:{ all -> 0x0055 }
                if (r2 < 0) goto L_0x0041
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                return
            L_0x0041:
                java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ all -> 0x0055 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0055 }
                r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0055 }
                java.lang.String r5 = r5.mTag     // Catch:{ all -> 0x0055 }
                r3.append((java.lang.String) r5)     // Catch:{ all -> 0x0055 }
                java.lang.String r5 = r3.toString()     // Catch:{ all -> 0x0055 }
                r2.<init>((java.lang.String) r5)     // Catch:{ all -> 0x0055 }
                throw r2     // Catch:{ all -> 0x0055 }
            L_0x0055:
                r5 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.release():void");
        }

        public void setReferenceCounted(boolean z) {
            this.mRefCounted = z;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public String toString() {
            String str;
            String str2;
            synchronized (this.mBinder) {
                String hexString = Integer.toHexString(System.identityHashCode(this));
                String str3 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    str = "refcounted: refcount = " + this.mRefCount;
                } else {
                    str = "not refcounted";
                }
                str2 = "MulticastLock{ " + hexString + "; " + str3 + str + " }";
            }
            return str2;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            super.finalize();
            setReferenceCounted(false);
            release();
        }
    }

    public boolean isMulticastEnabled() {
        try {
            return this.mService.isMulticastEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean initializeMulticastFiltering() {
        try {
            this.mService.initializeMulticastFiltering();
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setVerboseLoggingEnabled(boolean z) {
        enableVerboseLogging(z ? 1 : 0);
    }

    @SystemApi
    public void setVerboseLoggingLevel(int i) {
        enableVerboseLogging(i);
    }

    public void enableVerboseLogging(int i) {
        try {
            this.mService.enableVerboseLogging(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isVerboseLoggingEnabled() {
        return getVerboseLoggingLevel() > 0;
    }

    @SystemApi
    public int getVerboseLoggingLevel() {
        try {
            return this.mService.getVerboseLoggingLevel();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void factoryReset() {
        try {
            this.mService.factoryReset(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Network getCurrentNetwork() {
        try {
            return this.mService.getCurrentNetwork();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public byte[] retrieveBackupData() {
        try {
            return this.mService.retrieveBackupData();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void restoreBackupData(byte[] bArr) {
        try {
            this.mService.restoreBackupData(bArr);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public byte[] retrieveSoftApBackupData() {
        try {
            return this.mService.retrieveSoftApBackupData();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public SoftApConfiguration restoreSoftApBackupData(byte[] bArr) {
        try {
            return this.mService.restoreSoftApBackupData(bArr);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) {
        try {
            this.mService.restoreSupplicantBackupData(bArr, bArr2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startSubscriptionProvisioning(OsuProvider osuProvider, Executor executor, ProvisioningCallback provisioningCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        } else if (provisioningCallback != null) {
            try {
                this.mService.startSubscriptionProvisioning(osuProvider, new ProvisioningCallbackProxy(executor, provisioningCallback));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    private static class ProvisioningCallbackProxy extends IProvisioningCallback.Stub {
        private final ProvisioningCallback mCallback;
        private final Executor mExecutor;

        ProvisioningCallbackProxy(Executor executor, ProvisioningCallback provisioningCallback) {
            this.mExecutor = executor;
            this.mCallback = provisioningCallback;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningStatus$0$android-net-wifi-WifiManager$ProvisioningCallbackProxy */
        public /* synthetic */ void mo5077x74558e03(int i) {
            this.mCallback.onProvisioningStatus(i);
        }

        public void onProvisioningStatus(int i) {
            this.mExecutor.execute(new WifiManager$ProvisioningCallbackProxy$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningFailure$1$android-net-wifi-WifiManager$ProvisioningCallbackProxy */
        public /* synthetic */ void mo5076x5a06c17c(int i) {
            this.mCallback.onProvisioningFailure(i);
        }

        public void onProvisioningFailure(int i) {
            this.mExecutor.execute(new WifiManager$ProvisioningCallbackProxy$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningComplete$2$android-net-wifi-WifiManager$ProvisioningCallbackProxy */
        public /* synthetic */ void mo5075x4bc429a() {
            this.mCallback.onProvisioningComplete();
        }

        public void onProvisioningComplete() {
            this.mExecutor.execute(new WifiManager$ProvisioningCallbackProxy$$ExternalSyntheticLambda2(this));
        }
    }

    private class TrafficStateCallbackProxy extends ITrafficStateCallback.Stub {
        private final TrafficStateCallback mCallback;
        private final Executor mExecutor;

        TrafficStateCallbackProxy(Executor executor, TrafficStateCallback trafficStateCallback) {
            this.mExecutor = executor;
            this.mCallback = trafficStateCallback;
        }

        public void onStateChanged(int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "TrafficStateCallbackProxy: onStateChanged state=" + i);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$TrafficStateCallbackProxy$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStateChanged$0$android-net-wifi-WifiManager$TrafficStateCallbackProxy */
        public /* synthetic */ void mo5116x3a554b98(int i) {
            this.mCallback.onStateChanged(i);
        }
    }

    @SystemApi
    public void registerTrafficStateCallback(Executor executor, TrafficStateCallback trafficStateCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (trafficStateCallback != null) {
            Log.v(TAG, "registerTrafficStateCallback: callback=" + trafficStateCallback + ", executor=" + executor);
            try {
                SparseArray<ITrafficStateCallback> sparseArray = sTrafficStateCallbackMap;
                synchronized (sparseArray) {
                    TrafficStateCallbackProxy trafficStateCallbackProxy = new TrafficStateCallbackProxy(executor, trafficStateCallback);
                    sparseArray.put(System.identityHashCode(trafficStateCallback), trafficStateCallbackProxy);
                    this.mService.registerTrafficStateCallback(trafficStateCallbackProxy);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void unregisterTrafficStateCallback(TrafficStateCallback trafficStateCallback) {
        if (trafficStateCallback != null) {
            Log.v(TAG, "unregisterTrafficStateCallback: callback=" + trafficStateCallback);
            try {
                SparseArray<ITrafficStateCallback> sparseArray = sTrafficStateCallbackMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(trafficStateCallback);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.unregisterTrafficStateCallback(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    private void updateVerboseLoggingEnabledFromService() {
        this.mVerboseLoggingEnabled = isVerboseLoggingEnabled();
    }

    public String getCapabilities(String str) {
        try {
            return this.mService.getCapabilities(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWpa3SaeSupported() {
        return isFeatureSupported(WIFI_FEATURE_WPA3_SAE);
    }

    public boolean isWpa3SuiteBSupported() {
        return isFeatureSupported(WIFI_FEATURE_WPA3_SUITE_B);
    }

    public boolean isEnhancedOpenSupported() {
        return isFeatureSupported(WIFI_FEATURE_OWE);
    }

    public boolean isEasyConnectSupported() {
        return isFeatureSupported(WIFI_FEATURE_DPP);
    }

    public boolean isEasyConnectEnrolleeResponderModeSupported() {
        return isFeatureSupported(WIFI_FEATURE_DPP_ENROLLEE_RESPONDER);
    }

    public boolean isWapiSupported() {
        return isFeatureSupported(WIFI_FEATURE_WAPI);
    }

    public boolean isPasspointTermsAndConditionsSupported() {
        return isFeatureSupported(WIFI_FEATURE_PASSPOINT_TERMS_AND_CONDITIONS);
    }

    public boolean isWpa3SaeH2eSupported() {
        return isFeatureSupported(WIFI_FEATURE_SAE_H2E);
    }

    public boolean isWifiDisplayR2Supported() {
        return isFeatureSupported(1125899906842624L);
    }

    public boolean isDecoratedIdentitySupported() {
        return isFeatureSupported(WIFI_FEATURE_DECORATED_IDENTITY);
    }

    public boolean isTrustOnFirstUseSupported() {
        return isFeatureSupported(WIFI_FEATURE_TRUST_ON_FIRST_USE);
    }

    public boolean isEasyConnectDppAkmSupported() {
        return isFeatureSupported(WIFI_FEATURE_DPP_AKM);
    }

    @SystemApi
    public String[] getFactoryMacAddresses() {
        try {
            return this.mService.getFactoryMacAddresses();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setDeviceMobilityState(int i) {
        try {
            this.mService.setDeviceMobilityState(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startEasyConnectAsConfiguratorInitiator(String str, int i, int i2, Executor executor, EasyConnectStatusCallback easyConnectStatusCallback) {
        try {
            this.mService.startDppAsConfiguratorInitiator(new Binder(), this.mContext.getOpPackageName(), str, i, i2, new EasyConnectCallbackProxy(executor, easyConnectStatusCallback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startEasyConnectAsEnrolleeInitiator(String str, Executor executor, EasyConnectStatusCallback easyConnectStatusCallback) {
        try {
            this.mService.startDppAsEnrolleeInitiator(new Binder(), str, new EasyConnectCallbackProxy(executor, easyConnectStatusCallback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startEasyConnectAsEnrolleeResponder(String str, int i, Executor executor, EasyConnectStatusCallback easyConnectStatusCallback) {
        try {
            this.mService.startDppAsEnrolleeResponder(new Binder(), str, i, new EasyConnectCallbackProxy(executor, easyConnectStatusCallback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void stopEasyConnectSession() {
        try {
            this.mService.stopDppSession();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static class EasyConnectCallbackProxy extends IDppCallback.Stub {
        private final EasyConnectStatusCallback mEasyConnectStatusCallback;
        private final Executor mExecutor;

        EasyConnectCallbackProxy(Executor executor, EasyConnectStatusCallback easyConnectStatusCallback) {
            this.mExecutor = executor;
            this.mEasyConnectStatusCallback = easyConnectStatusCallback;
        }

        public void onSuccessConfigReceived(int i) {
            Log.d(WifiManager.TAG, "Easy Connect onSuccessConfigReceived callback");
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$EasyConnectCallbackProxy$$ExternalSyntheticLambda2(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSuccessConfigReceived$0$android-net-wifi-WifiManager$EasyConnectCallbackProxy */
        public /* synthetic */ void mo5029x41daa049(int i) {
            this.mEasyConnectStatusCallback.onEnrolleeSuccess(i);
        }

        public void onSuccess(int i) {
            Log.d(WifiManager.TAG, "Easy Connect onSuccess callback");
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$EasyConnectCallbackProxy$$ExternalSyntheticLambda3(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSuccess$1$android-net-wifi-WifiManager$EasyConnectCallbackProxy */
        public /* synthetic */ void mo5028x4a777767(int i) {
            this.mEasyConnectStatusCallback.onConfiguratorSuccess(i);
        }

        public void onFailure(int i, String str, String str2, int[] iArr) {
            Log.d(WifiManager.TAG, "Easy Connect onFailure callback");
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$EasyConnectCallbackProxy$$ExternalSyntheticLambda1(this, str2, i, str, iArr));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFailure$2$android-net-wifi-WifiManager$EasyConnectCallbackProxy */
        public /* synthetic */ void mo5026x85dc7d2f(String str, int i, String str2, int[] iArr) {
            this.mEasyConnectStatusCallback.onFailure(i, str2, WifiManager.parseDppChannelList(str), iArr);
        }

        public void onProgress(int i) {
            Log.d(WifiManager.TAG, "Easy Connect onProgress callback");
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$EasyConnectCallbackProxy$$ExternalSyntheticLambda4(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProgress$3$android-net-wifi-WifiManager$EasyConnectCallbackProxy */
        public /* synthetic */ void mo5027x295f858b(int i) {
            this.mEasyConnectStatusCallback.onProgress(i);
        }

        public void onBootstrapUriGenerated(String str) {
            Log.d(WifiManager.TAG, "Easy Connect onBootstrapUriGenerated callback");
            if (!SdkLevel.isAtLeastS()) {
                Log.e(WifiManager.TAG, "Easy Connect bootstrap URI callback supported only on S+");
                return;
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new WifiManager$EasyConnectCallbackProxy$$ExternalSyntheticLambda0(this, str));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBootstrapUriGenerated$4$android-net-wifi-WifiManager$EasyConnectCallbackProxy */
        public /* synthetic */ void mo5025x2687b938(String str) {
            this.mEasyConnectStatusCallback.onBootstrapUriGenerated(Uri.parse(str));
        }
    }

    @SystemApi
    public void addOnWifiUsabilityStatsListener(final Executor executor, final OnWifiUsabilityStatsListener onWifiUsabilityStatsListener) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (onWifiUsabilityStatsListener != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.v(TAG, "addOnWifiUsabilityStatsListener: listener=" + onWifiUsabilityStatsListener);
            }
            try {
                SparseArray<IOnWifiUsabilityStatsListener> sparseArray = sOnWifiUsabilityStatsListenerMap;
                synchronized (sparseArray) {
                    C01602 r1 = new IOnWifiUsabilityStatsListener.Stub() {
                        public void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) {
                            if (WifiManager.this.mVerboseLoggingEnabled) {
                                Log.v(WifiManager.TAG, "OnWifiUsabilityStatsListener: onWifiUsabilityStats: seqNum=" + i);
                            }
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiManager$2$$ExternalSyntheticLambda0(onWifiUsabilityStatsListener, i, z, wifiUsabilityStatsEntry));
                        }
                    };
                    sparseArray.put(System.identityHashCode(onWifiUsabilityStatsListener), r1);
                    this.mService.addOnWifiUsabilityStatsListener(r1);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }

    @SystemApi
    public void removeOnWifiUsabilityStatsListener(OnWifiUsabilityStatsListener onWifiUsabilityStatsListener) {
        if (onWifiUsabilityStatsListener != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.v(TAG, "removeOnWifiUsabilityStatsListener: listener=" + onWifiUsabilityStatsListener);
            }
            try {
                SparseArray<IOnWifiUsabilityStatsListener> sparseArray = sOnWifiUsabilityStatsListenerMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(onWifiUsabilityStatsListener);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.removeOnWifiUsabilityStatsListener(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }

    @SystemApi
    public void updateWifiUsabilityScore(int i, int i2, int i3) {
        try {
            this.mService.updateWifiUsabilityScore(i, i2, i3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static abstract class ScanResultsCallback {
        private final ScanResultsCallbackProxy mScanResultsCallbackProxy = new ScanResultsCallbackProxy();

        public abstract void onScanResultsAvailable();

        /* access modifiers changed from: package-private */
        public ScanResultsCallbackProxy getProxy() {
            return this.mScanResultsCallbackProxy;
        }

        private static class ScanResultsCallbackProxy extends IScanResultsCallback.Stub {
            private ScanResultsCallback mCallback = null;
            private Executor mExecutor = null;
            private final Object mLock = new Object();

            ScanResultsCallbackProxy() {
            }

            /* access modifiers changed from: package-private */
            public void initProxy(Executor executor, ScanResultsCallback scanResultsCallback) {
                synchronized (this.mLock) {
                    this.mExecutor = executor;
                    this.mCallback = scanResultsCallback;
                }
            }

            /* access modifiers changed from: package-private */
            public void cleanUpProxy() {
                synchronized (this.mLock) {
                    this.mExecutor = null;
                    this.mCallback = null;
                }
            }

            public void onScanResultsAvailable() {
                Executor executor;
                ScanResultsCallback scanResultsCallback;
                synchronized (this.mLock) {
                    executor = this.mExecutor;
                    scanResultsCallback = this.mCallback;
                }
                if (scanResultsCallback != null && executor != null) {
                    Binder.clearCallingIdentity();
                    Objects.requireNonNull(scanResultsCallback);
                    executor.execute(new C0184x70986bb1(scanResultsCallback));
                }
            }
        }
    }

    public void registerScanResultsCallback(Executor executor, ScanResultsCallback scanResultsCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (scanResultsCallback != null) {
            Log.v(TAG, "registerScanResultsCallback: callback=" + scanResultsCallback + ", executor=" + executor);
            ScanResultsCallback.ScanResultsCallbackProxy proxy = scanResultsCallback.getProxy();
            proxy.initProxy(executor, scanResultsCallback);
            try {
                this.mService.registerScanResultsCallback(proxy);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    public void unregisterScanResultsCallback(ScanResultsCallback scanResultsCallback) {
        if (scanResultsCallback != null) {
            Log.v(TAG, "unregisterScanResultsCallback: Callback=" + scanResultsCallback);
            ScanResultsCallback.ScanResultsCallbackProxy proxy = scanResultsCallback.getProxy();
            try {
                this.mService.unregisterScanResultsCallback(proxy);
                proxy.cleanUpProxy();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                proxy.cleanUpProxy();
                throw th;
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    private class SuggestionConnectionStatusListenerProxy extends ISuggestionConnectionStatusListener.Stub {
        private final Executor mExecutor;
        private final SuggestionConnectionStatusListener mListener;

        SuggestionConnectionStatusListenerProxy(Executor executor, SuggestionConnectionStatusListener suggestionConnectionStatusListener) {
            this.mExecutor = executor;
            this.mListener = suggestionConnectionStatusListener;
        }

        public void onConnectionStatus(WifiNetworkSuggestion wifiNetworkSuggestion, int i) {
            this.mExecutor.execute(new C0187xd317fe29(this, wifiNetworkSuggestion, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectionStatus$0$android-net-wifi-WifiManager$SuggestionConnectionStatusListenerProxy */
        public /* synthetic */ void mo5112x2fd5a31c(WifiNetworkSuggestion wifiNetworkSuggestion, int i) {
            this.mListener.onConnectionStatus(wifiNetworkSuggestion, i);
        }
    }

    @SystemApi
    public void addWifiVerboseLoggingStatusChangedListener(final Executor executor, final WifiVerboseLoggingStatusChangedListener wifiVerboseLoggingStatusChangedListener) {
        if (wifiVerboseLoggingStatusChangedListener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        } else if (executor != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.v(TAG, "addWifiVerboseLoggingStatusChangedListener listener=" + wifiVerboseLoggingStatusChangedListener + ", executor=" + executor);
            }
            try {
                SparseArray<IWifiVerboseLoggingStatusChangedListener> sparseArray = sWifiVerboseLoggingStatusChangedListenerMap;
                synchronized (sparseArray) {
                    C01613 r1 = new IWifiVerboseLoggingStatusChangedListener.Stub() {
                        public void onStatusChanged(boolean z) {
                            if (WifiManager.this.mVerboseLoggingEnabled) {
                                Log.v(WifiManager.TAG, "WifiVerboseLoggingStatusListener: onVerboseLoggingStatusChanged: enabled=" + z);
                            }
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiManager$3$$ExternalSyntheticLambda0(wifiVerboseLoggingStatusChangedListener, z));
                        }
                    };
                    sparseArray.put(System.identityHashCode(wifiVerboseLoggingStatusChangedListener), r1);
                    this.mService.addWifiVerboseLoggingStatusChangedListener(r1);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Executor cannot be null");
        }
    }

    @SystemApi
    public void removeWifiVerboseLoggingStatusChangedListener(WifiVerboseLoggingStatusChangedListener wifiVerboseLoggingStatusChangedListener) {
        if (wifiVerboseLoggingStatusChangedListener != null) {
            Log.v(TAG, "removeWifiVerboseLoggingStatusChangedListener: listener=" + wifiVerboseLoggingStatusChangedListener);
            try {
                SparseArray<IWifiVerboseLoggingStatusChangedListener> sparseArray = sWifiVerboseLoggingStatusChangedListenerMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(wifiVerboseLoggingStatusChangedListener);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.removeWifiVerboseLoggingStatusChangedListener(sparseArray.get(identityHashCode));
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Listener cannot be null");
        }
    }

    public void addSuggestionConnectionStatusListener(Executor executor, SuggestionConnectionStatusListener suggestionConnectionStatusListener) {
        if (suggestionConnectionStatusListener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        } else if (executor != null) {
            Log.v(TAG, "addSuggestionConnectionStatusListener listener=" + suggestionConnectionStatusListener + ", executor=" + executor);
            try {
                SparseArray<ISuggestionConnectionStatusListener> sparseArray = sSuggestionConnectionStatusListenerMap;
                synchronized (sparseArray) {
                    SuggestionConnectionStatusListenerProxy suggestionConnectionStatusListenerProxy = new SuggestionConnectionStatusListenerProxy(executor, suggestionConnectionStatusListener);
                    sparseArray.put(System.identityHashCode(suggestionConnectionStatusListener), suggestionConnectionStatusListenerProxy);
                    this.mService.registerSuggestionConnectionStatusListener(suggestionConnectionStatusListenerProxy, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Executor cannot be null");
        }
    }

    public void removeSuggestionConnectionStatusListener(SuggestionConnectionStatusListener suggestionConnectionStatusListener) {
        if (suggestionConnectionStatusListener != null) {
            Log.v(TAG, "removeSuggestionConnectionStatusListener: listener=" + suggestionConnectionStatusListener);
            try {
                SparseArray<ISuggestionConnectionStatusListener> sparseArray = sSuggestionConnectionStatusListenerMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(suggestionConnectionStatusListener);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.unregisterSuggestionConnectionStatusListener(sparseArray.get(identityHashCode), this.mContext.getOpPackageName());
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Listener cannot be null");
        }
    }

    public static SparseArray<int[]> parseDppChannelList(String str) {
        SparseArray<int[]> sparseArray = new SparseArray<>();
        if (TextUtils.isEmpty(str)) {
            return sparseArray;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, NavigationBarInflaterView.BUTTON_SEPARATOR);
        ArrayList arrayList = new ArrayList();
        String str2 = null;
        while (true) {
            try {
                if (stringTokenizer.hasMoreElements()) {
                    String nextToken = stringTokenizer.nextToken();
                    int indexOf = nextToken.indexOf(47);
                    if (indexOf != -1) {
                        if (str2 != null) {
                            int[] iArr = new int[arrayList.size()];
                            for (int i = 0; i < arrayList.size(); i++) {
                                iArr[i] = ((Integer) arrayList.get(i)).intValue();
                            }
                            sparseArray.append(Integer.parseInt(str2), iArr);
                            arrayList = new ArrayList();
                        }
                        str2 = nextToken.substring(0, indexOf);
                        arrayList.add(Integer.valueOf(Integer.parseInt(nextToken.substring(indexOf + 1))));
                    } else if (str2 == null) {
                        Log.e(TAG, "Cannot parse DPP channel list");
                        return new SparseArray<>();
                    } else {
                        arrayList.add(Integer.valueOf(Integer.parseInt(nextToken)));
                    }
                } else {
                    if (str2 != null) {
                        int[] iArr2 = new int[arrayList.size()];
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            iArr2[i2] = ((Integer) arrayList.get(i2)).intValue();
                        }
                        sparseArray.append(Integer.parseInt(str2), iArr2);
                    }
                    return sparseArray;
                }
            } catch (NumberFormatException unused) {
                Log.e(TAG, "Cannot parse DPP channel list");
                return new SparseArray<>();
            }
        }
    }

    private class ScoreUpdateObserverProxy implements ScoreUpdateObserver {
        private final IScoreUpdateObserver mScoreUpdateObserver;

        private ScoreUpdateObserverProxy(IScoreUpdateObserver iScoreUpdateObserver) {
            this.mScoreUpdateObserver = iScoreUpdateObserver;
        }

        public void notifyScoreUpdate(int i, int i2) {
            try {
                this.mScoreUpdateObserver.notifyScoreUpdate(i, i2);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void triggerUpdateOfWifiUsabilityStats(int i) {
            try {
                this.mScoreUpdateObserver.triggerUpdateOfWifiUsabilityStats(i);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void notifyStatusUpdate(int i, boolean z) {
            if (SdkLevel.isAtLeastS()) {
                try {
                    this.mScoreUpdateObserver.notifyStatusUpdate(i, z);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }

        public void requestNudOperation(int i) {
            if (SdkLevel.isAtLeastS()) {
                try {
                    this.mScoreUpdateObserver.requestNudOperation(i);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }

        public void blocklistCurrentBssid(int i) {
            if (SdkLevel.isAtLeastS()) {
                try {
                    this.mScoreUpdateObserver.blocklistCurrentBssid(i);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    @SystemApi
    public interface WifiConnectedNetworkScorer {
        void onSetScoreUpdateObserver(ScoreUpdateObserver scoreUpdateObserver);

        @Deprecated
        void onStart(int i) {
        }

        void onStop(int i);

        void onStart(WifiConnectedSessionInfo wifiConnectedSessionInfo) {
            onStart(wifiConnectedSessionInfo.getSessionId());
        }
    }

    private class PnoScanResultsCallbackProxy extends IPnoScanResultsCallback.Stub {
        private PnoScanResultsCallback mCallback;
        private Executor mExecutor;

        PnoScanResultsCallbackProxy(Executor executor, PnoScanResultsCallback pnoScanResultsCallback) {
            this.mExecutor = executor;
            this.mCallback = pnoScanResultsCallback;
        }

        public void onScanResultsAvailable(List<ScanResult> list) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "PnoScanResultsCallback: onScanResultsAvailable");
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0183xef8a1ac7(this, list));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onScanResultsAvailable$0$android-net-wifi-WifiManager$PnoScanResultsCallbackProxy */
        public /* synthetic */ void mo5074x532c20d7(List list) {
            this.mCallback.onScanResultsAvailable(list);
        }

        public void onRegisterSuccess() {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "PnoScanResultsCallback: onRegisterSuccess");
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0182xef8a1ac6(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRegisterSuccess$1$android-net-wifi-WifiManager$PnoScanResultsCallbackProxy */
        public /* synthetic */ void mo5072x98f1b29e() {
            this.mCallback.onRegisterSuccess();
        }

        public void onRegisterFailed(int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "PnoScanResultsCallback: onRegisterFailed " + i);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0180xef8a1ac4(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRegisterFailed$2$android-net-wifi-WifiManager$PnoScanResultsCallbackProxy */
        public /* synthetic */ void mo5071xc2265f65(int i) {
            this.mCallback.onRegisterFailed(i);
        }

        public void onRemoved(int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "PnoScanResultsCallback: onRemoved");
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0181xef8a1ac5(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRemoved$3$android-net-wifi-WifiManager$PnoScanResultsCallbackProxy */
        public /* synthetic */ void mo5073xca1c305c(int i) {
            this.mCallback.onRemoved(i);
        }
    }

    private class WifiConnectedNetworkScorerProxy extends IWifiConnectedNetworkScorer.Stub {
        private Executor mExecutor;
        private WifiConnectedNetworkScorer mScorer;

        WifiConnectedNetworkScorerProxy(Executor executor, WifiConnectedNetworkScorer wifiConnectedNetworkScorer) {
            this.mExecutor = executor;
            this.mScorer = wifiConnectedNetworkScorer;
        }

        public void onStart(WifiConnectedSessionInfo wifiConnectedSessionInfo) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "WifiConnectedNetworkScorer: onStart: sessionInfo=" + wifiConnectedSessionInfo);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0189xfd979a77(this, wifiConnectedSessionInfo));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStart$0$android-net-wifi-WifiManager$WifiConnectedNetworkScorerProxy */
        public /* synthetic */ void mo5122x33b91a30(WifiConnectedSessionInfo wifiConnectedSessionInfo) {
            this.mScorer.onStart(wifiConnectedSessionInfo);
        }

        public void onStop(int i) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "WifiConnectedNetworkScorer: onStop: sessionId=" + i);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0190xfd979a78(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStop$1$android-net-wifi-WifiManager$WifiConnectedNetworkScorerProxy */
        public /* synthetic */ void mo5123x8abe66b7(int i) {
            this.mScorer.onStop(i);
        }

        public void onSetScoreUpdateObserver(IScoreUpdateObserver iScoreUpdateObserver) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "WifiConnectedNetworkScorer: onSetScoreUpdateObserver: observerImpl=" + iScoreUpdateObserver);
            }
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new C0191xfd979a79(this, iScoreUpdateObserver));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSetScoreUpdateObserver$2$android-net-wifi-WifiManager$WifiConnectedNetworkScorerProxy */
        public /* synthetic */ void mo5121xe92edd89(IScoreUpdateObserver iScoreUpdateObserver) {
            this.mScorer.onSetScoreUpdateObserver(new ScoreUpdateObserverProxy(iScoreUpdateObserver));
        }
    }

    @SystemApi
    public void setExternalPnoScanRequest(List<WifiSsid> list, int[] iArr, Executor executor, PnoScanResultsCallback pnoScanResultsCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (pnoScanResultsCallback != null) {
            try {
                IWifiManager iWifiManager = this.mService;
                Binder binder = new Binder();
                PnoScanResultsCallbackProxy pnoScanResultsCallbackProxy = new PnoScanResultsCallbackProxy(executor, pnoScanResultsCallback);
                if (iArr == null) {
                    iArr = new int[0];
                }
                iWifiManager.setExternalPnoScanRequest(binder, pnoScanResultsCallbackProxy, list, iArr, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback cannot be null");
        }
    }

    @SystemApi
    public void clearExternalPnoScanRequest() {
        try {
            this.mService.clearExternalPnoScanRequest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void getLastCallerInfoForApi(int i, final Executor executor, final BiConsumer<String, Boolean> biConsumer) {
        if (executor == null) {
            throw new IllegalArgumentException("executor can't be null");
        } else if (biConsumer != null) {
            try {
                this.mService.getLastCallerInfoForApi(i, new ILastCallerListener.Stub() {
                    public void onResult(String str, boolean z) {
                        Binder.clearCallingIdentity();
                        executor.execute(new WifiManager$4$$ExternalSyntheticLambda0(biConsumer, str, z));
                    }
                });
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("resultsCallback can't be null");
        }
    }

    @SystemApi
    public boolean setWifiConnectedNetworkScorer(Executor executor, WifiConnectedNetworkScorer wifiConnectedNetworkScorer) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (wifiConnectedNetworkScorer != null) {
            if (this.mVerboseLoggingEnabled) {
                Log.v(TAG, "setWifiConnectedNetworkScorer: scorer=" + wifiConnectedNetworkScorer);
            }
            try {
                return this.mService.setWifiConnectedNetworkScorer(new Binder(), new WifiConnectedNetworkScorerProxy(executor, wifiConnectedNetworkScorer));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("scorer cannot be null");
        }
    }

    @SystemApi
    public void clearWifiConnectedNetworkScorer() {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "clearWifiConnectedNetworkScorer");
        }
        try {
            this.mService.clearWifiConnectedNetworkScorer();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setScanThrottleEnabled(boolean z) {
        try {
            this.mService.setScanThrottleEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isScanThrottleEnabled() {
        try {
            return this.mService.isScanThrottleEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setAutoWakeupEnabled(boolean z) {
        try {
            this.mService.setAutoWakeupEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isAutoWakeupEnabled() {
        try {
            return this.mService.isAutoWakeupEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setCarrierNetworkOffloadEnabled(int i, boolean z, boolean z2) {
        try {
            this.mService.setCarrierNetworkOffloadEnabled(i, z, z2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isCarrierNetworkOffloadEnabled(int i, boolean z) {
        try {
            return this.mService.isCarrierNetworkOffloadEnabled(i, z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private class SuggestionUserApprovalStatusListenerProxy extends ISuggestionUserApprovalStatusListener.Stub {
        private final Executor mExecutor;
        private final SuggestionUserApprovalStatusListener mListener;

        SuggestionUserApprovalStatusListenerProxy(Executor executor, SuggestionUserApprovalStatusListener suggestionUserApprovalStatusListener) {
            this.mExecutor = executor;
            this.mListener = suggestionUserApprovalStatusListener;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserApprovalStatusChange$0$android-net-wifi-WifiManager$SuggestionUserApprovalStatusListenerProxy */
        public /* synthetic */ void mo5114xc770782c(int i) {
            this.mListener.onUserApprovalStatusChange(i);
        }

        public void onUserApprovalStatusChange(int i) {
            this.mExecutor.execute(new C0188xa73c2fd9(this, i));
        }
    }

    public void addSuggestionUserApprovalStatusListener(Executor executor, SuggestionUserApprovalStatusListener suggestionUserApprovalStatusListener) {
        if (suggestionUserApprovalStatusListener == null) {
            throw new NullPointerException("Listener cannot be null");
        } else if (executor != null) {
            Log.v(TAG, "addSuggestionUserApprovalStatusListener listener=" + suggestionUserApprovalStatusListener + ", executor=" + executor);
            try {
                SparseArray<ISuggestionUserApprovalStatusListener> sparseArray = sSuggestionUserApprovalStatusListenerMap;
                synchronized (sparseArray) {
                    SuggestionUserApprovalStatusListenerProxy suggestionUserApprovalStatusListenerProxy = new SuggestionUserApprovalStatusListenerProxy(executor, suggestionUserApprovalStatusListener);
                    sparseArray.put(System.identityHashCode(suggestionUserApprovalStatusListener), suggestionUserApprovalStatusListenerProxy);
                    this.mService.addSuggestionUserApprovalStatusListener(suggestionUserApprovalStatusListenerProxy, this.mContext.getOpPackageName());
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new NullPointerException("Executor cannot be null");
        }
    }

    public void removeSuggestionUserApprovalStatusListener(SuggestionUserApprovalStatusListener suggestionUserApprovalStatusListener) {
        if (suggestionUserApprovalStatusListener != null) {
            Log.v(TAG, "removeSuggestionUserApprovalStatusListener: listener=" + suggestionUserApprovalStatusListener);
            try {
                SparseArray<ISuggestionUserApprovalStatusListener> sparseArray = sSuggestionUserApprovalStatusListenerMap;
                synchronized (sparseArray) {
                    int identityHashCode = System.identityHashCode(suggestionUserApprovalStatusListener);
                    if (!sparseArray.contains(identityHashCode)) {
                        Log.w(TAG, "Unknown external callback " + identityHashCode);
                        return;
                    }
                    this.mService.removeSuggestionUserApprovalStatusListener(sparseArray.get(identityHashCode), this.mContext.getOpPackageName());
                    sparseArray.remove(identityHashCode);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Listener cannot be null");
        }
    }

    public void setEmergencyScanRequestInProgress(boolean z) {
        try {
            this.mService.setEmergencyScanRequestInProgress(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean setWifiScoringEnabled(boolean z) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "setWifiScoringEnabled: " + z);
        }
        try {
            return this.mService.setWifiScoringEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void flushPasspointAnqpCache() {
        try {
            this.mService.flushPasspointAnqpCache(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<WifiAvailableChannel> getAllowedChannels(int i, int i2) {
        if (SdkLevel.isAtLeastS()) {
            try {
                return this.mService.getUsableChannels(i, i2, 0);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @SystemApi
    public List<WifiAvailableChannel> getUsableChannels(int i, int i2) {
        if (SdkLevel.isAtLeastS()) {
            try {
                return this.mService.getUsableChannels(i, i2, WifiAvailableChannel.getUsableFilter());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean isWifiPasspointEnabled() {
        try {
            return this.mService.isWifiPasspointEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setWifiPasspointEnabled(boolean z) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "setWifiPasspointEnabled: " + z);
        }
        try {
            this.mService.setWifiPasspointEnabled(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getStaConcurrencyForMultiInternetMode() {
        try {
            return this.mService.getStaConcurrencyForMultiInternetMode();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void notifyMinimumRequiredWifiSecurityLevelChanged(int i) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "notifyMinimumRequiredWifiSecurityLevelChanged");
        }
        try {
            this.mService.notifyMinimumRequiredWifiSecurityLevelChanged(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void notifyWifiSsidPolicyChanged(WifiSsidPolicy wifiSsidPolicy) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "notifyWifiSsidPolicyChanged");
        }
        if (wifiSsidPolicy != null) {
            try {
                this.mService.notifyWifiSsidPolicyChanged(wifiSsidPolicy.getPolicyType(), new ArrayList(wifiSsidPolicy.getSsids()));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public boolean setStaConcurrencyForMultiInternetMode(int i) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "setStaConcurrencyForMultiInternetMode: " + i);
        }
        try {
            return this.mService.setStaConcurrencyForMultiInternetMode(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Set<String> getOemPrivilegedWifiAdminPackages() {
        try {
            return new ArraySet(this.mService.getOemPrivilegedWifiAdminPackages());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void replyToSimpleDialog(int i, int i2) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "replyToWifiEnableRequestDialog: dialogId=" + i + " reply=" + i2);
        }
        try {
            this.mService.replyToSimpleDialog(i, i2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void replyToP2pInvitationReceivedDialog(int i, boolean z, String str) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "replyToP2pInvitationReceivedDialog: dialogId=" + i + ", accepted=" + z + ", pin=" + str);
        }
        try {
            this.mService.replyToP2pInvitationReceivedDialog(i, z, str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr, List<DhcpOption> list) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "addCustomDhcpOptions: ssid=" + wifiSsid + ", oui=" + Arrays.toString(bArr) + ", options=" + list);
        }
        try {
            this.mService.addCustomDhcpOptions(wifiSsid, bArr, list);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void removeCustomDhcpOptions(WifiSsid wifiSsid, byte[] bArr) {
        if (this.mVerboseLoggingEnabled) {
            Log.v(TAG, "removeCustomDhcpOptions: ssid=" + wifiSsid + ", oui=" + Arrays.toString(bArr));
        }
        try {
            this.mService.removeCustomDhcpOptions(wifiSsid, bArr);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static class InterfaceCreationImpact {
        private final int mInterfaceType;
        private final Set<String> mPackages;

        public InterfaceCreationImpact(int i, Set<String> set) {
            this.mInterfaceType = i;
            this.mPackages = set;
        }

        public int getInterfaceType() {
            return this.mInterfaceType;
        }

        public Set<String> getPackages() {
            return this.mPackages;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mInterfaceType), this.mPackages);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof InterfaceCreationImpact)) {
                return false;
            }
            InterfaceCreationImpact interfaceCreationImpact = (InterfaceCreationImpact) obj;
            if (this.mInterfaceType != interfaceCreationImpact.mInterfaceType || !Objects.equals(this.mPackages, interfaceCreationImpact.mPackages)) {
                return false;
            }
            return true;
        }
    }

    public void reportCreateInterfaceImpact(int i, boolean z, final Executor executor, final BiConsumer<Boolean, Set<InterfaceCreationImpact>> biConsumer) {
        Objects.requireNonNull(executor, "Non-null executor required");
        Objects.requireNonNull(biConsumer, "Non-null resultCallback required");
        try {
            this.mService.reportCreateInterfaceImpact(this.mContext.getOpPackageName(), i, z, new IInterfaceCreationInfoCallback.Stub() {
                public void onResults(boolean z, int[] iArr, String[] strArr) {
                    Set set;
                    Binder.clearCallingIdentity();
                    if ((iArr != null || strArr == null) && ((iArr == null || strArr != null) && (!z || (iArr != null && iArr.length == strArr.length)))) {
                        if (!z || iArr.length <= 0) {
                            set = Collections.emptySet();
                        } else {
                            set = new ArraySet();
                        }
                        if (z) {
                            for (int i = 0; i < iArr.length; i++) {
                                set.add(new InterfaceCreationImpact(iArr[i], new ArraySet(strArr[i].split(NavigationBarInflaterView.BUTTON_SEPARATOR))));
                            }
                        }
                        executor.execute(new WifiManager$5$$ExternalSyntheticLambda0(biConsumer, z, set));
                        return;
                    }
                    Log.e(WifiManager.TAG, "reportImpactToCreateIfaceRequest: Invalid callback parameters - canCreate=" + z + ", interfacesToDelete=" + Arrays.toString(iArr) + ", worksourcesForInterfaces=" + Arrays.toString((Object[]) strArr));
                }
            });
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
