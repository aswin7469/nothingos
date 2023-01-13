package com.android.wifitrackerlib;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class WifiEntry {
    public static final int CONNECTED_STATE_CONNECTED = 2;
    public static final int CONNECTED_STATE_CONNECTING = 1;
    public static final int CONNECTED_STATE_DISCONNECTED = 0;
    public static final int FREQUENCY_2_4_GHZ = 2400;
    public static final int FREQUENCY_5_GHZ = 5000;
    public static final int FREQUENCY_60_GHZ = 60000;
    public static final int FREQUENCY_6_GHZ = 6000;
    public static final int FREQUENCY_UNKNOWN = -1;
    public static final int MAX_FREQ_24GHZ = 2500;
    public static final int MAX_FREQ_5GHZ = 5900;
    public static final int MAX_FREQ_60GHZ = 70200;
    public static final int MAX_FREQ_6GHZ = 7125;
    protected static final int MAX_VERBOSE_LOG_DISPLAY_SCANRESULT_COUNT = 4;
    public static final int METERED_CHOICE_AUTO = 0;
    public static final int METERED_CHOICE_METERED = 1;
    public static final int METERED_CHOICE_UNMETERED = 2;
    public static final int MIN_FREQ_24GHZ = 2400;
    public static final int MIN_FREQ_5GHZ = 4900;
    public static final int MIN_FREQ_60GHZ = 58320;
    public static final int MIN_FREQ_6GHZ = 5925;
    public static final int NUM_SECURITY_TYPES = 8;
    public static final int PRIVACY_DEVICE_MAC = 0;
    public static final int PRIVACY_RANDOMIZED_MAC = 1;
    public static final int PRIVACY_UNKNOWN = 2;
    public static final int SECURITY_EAP = 3;
    public static final int SECURITY_EAP_SUITE_B = 6;
    public static final int SECURITY_EAP_WPA3_ENTERPRISE = 7;
    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_OWE = 4;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_SAE = 5;
    public static final int SECURITY_WEP = 1;
    public static Comparator<WifiEntry> TITLE_COMPARATOR = Comparator.comparing(new WifiEntry$$ExternalSyntheticLambda1());
    public static final int WIFI_LEVEL_MAX = 4;
    public static final int WIFI_LEVEL_MIN = 0;
    public static final int WIFI_LEVEL_UNREACHABLE = -1;
    public static Comparator<WifiEntry> WIFI_PICKER_COMPARATOR = Comparator.comparing(new WifiEntry$$ExternalSyntheticLambda6()).thenComparing(new WifiEntry$$ExternalSyntheticLambda7()).thenComparing(new WifiEntry$$ExternalSyntheticLambda8()).thenComparing(new WifiEntry$$ExternalSyntheticLambda9()).thenComparing(new WifiEntry$$ExternalSyntheticLambda10()).thenComparing(new WifiEntry$$ExternalSyntheticLambda11()).thenComparing(new WifiEntry$$ExternalSyntheticLambda12());
    protected final Handler mCallbackHandler;
    protected boolean mCalledConnect = false;
    protected boolean mCalledDisconnect = false;
    protected ConnectCallback mConnectCallback;
    protected ConnectedInfo mConnectedInfo;
    private int mDeviceWifiStandard;
    protected DisconnectCallback mDisconnectCallback;
    final boolean mForSavedNetworksPage;
    protected ForgetCallback mForgetCallback;
    protected boolean mIsDefaultNetwork;
    protected boolean mIsLowQuality;
    private boolean mIsOweTransitionMode;
    private boolean mIsPskSaeTransitionMode;
    private boolean mIsValidated;
    protected int mLevel = -1;
    private WifiEntryCallback mListener;
    private Optional<ManageSubscriptionAction> mManageSubscriptionAction = Optional.empty();
    protected NetworkCapabilities mNetworkCapabilities;
    protected NetworkInfo mNetworkInfo;
    protected WifiInfo mWifiInfo;
    protected final WifiManager mWifiManager;
    private int mWifiStandard = 1;

    public interface ConnectCallback {
        public static final int CONNECT_STATUS_FAILURE_NO_CONFIG = 1;
        public static final int CONNECT_STATUS_FAILURE_SIM_ABSENT = 3;
        public static final int CONNECT_STATUS_FAILURE_UNKNOWN = 2;
        public static final int CONNECT_STATUS_SUCCESS = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ConnectStatus {
        }

        void onConnectResult(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectedState {
    }

    public interface DisconnectCallback {
        public static final int DISCONNECT_STATUS_FAILURE_UNKNOWN = 1;
        public static final int DISCONNECT_STATUS_SUCCESS = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DisconnectStatus {
        }

        void onDisconnectResult(int i);
    }

    public interface ForgetCallback {
        public static final int FORGET_STATUS_FAILURE_UNKNOWN = 1;
        public static final int FORGET_STATUS_SUCCESS = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ForgetStatus {
        }

        void onForgetResult(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Frequency {
    }

    public interface ManageSubscriptionAction {
        void onExecute();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MeteredChoice {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Privacy {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Security {
    }

    public interface SignInCallback {
        public static final int SIGNIN_STATUS_FAILURE_UNKNOWN = 1;
        public static final int SIGNIN_STATUS_SUCCESS = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface SignInStatus {
        }

        void onSignInResult(int i);
    }

    public interface WifiEntryCallback {
        void onUpdated();
    }

    public boolean canConnect() {
        return false;
    }

    public boolean canDisconnect() {
        return false;
    }

    public boolean canEasyConnect() {
        return false;
    }

    public boolean canForget() {
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSetPrivacy() {
        return false;
    }

    public boolean canShare() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    public void connect(ConnectCallback connectCallback) {
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return false;
    }

    public void disconnect(DisconnectCallback disconnectCallback) {
    }

    public void forget(ForgetCallback forgetCallback) {
    }

    public String getHelpUriString() {
        return null;
    }

    public String getKey() {
        return "";
    }

    public String getMacAddress() {
        return null;
    }

    public int getMeteredChoice() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return "";
    }

    public int getPrivacy() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    public CharSequence getSecondSummary() {
        return "";
    }

    public String getSecurityString(boolean z) {
        return "";
    }

    public String getSsid() {
        return null;
    }

    public String getStandardString() {
        return "";
    }

    public String getSummary(boolean z) {
        return "";
    }

    public String getTitle() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return false;
    }

    public boolean isExpired() {
        return false;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public void setAutoJoinEnabled(boolean z) {
    }

    public void setMeteredChoice(int i) {
    }

    public void setPrivacy(int i) {
    }

    public boolean shouldEditBeforeConnect() {
        return false;
    }

    public void signIn(SignInCallback signInCallback) {
    }

    /* access modifiers changed from: protected */
    public void updateSecurityTypes() {
    }

    static /* synthetic */ Boolean lambda$static$0(WifiEntry wifiEntry) {
        return Boolean.valueOf(wifiEntry.getConnectedState() != 2);
    }

    public WifiEntry(Handler handler, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        Preconditions.checkNotNull(handler, "Cannot construct with null handler!");
        Preconditions.checkNotNull(wifiManager, "Cannot construct with null WifiManager!");
        this.mCallbackHandler = handler;
        this.mForSavedNetworksPage = z;
        this.mWifiManager = wifiManager;
        updatetDeviceWifiGenerationInfo();
    }

    public static boolean isGbkSsidSupported() {
        return WifiTrackerInjector.isGbkSsidSupported();
    }

    public synchronized int getConnectedState() {
        if (this.mNetworkInfo == null) {
            return 0;
        }
        switch (C33521.$SwitchMap$android$net$NetworkInfo$DetailedState[this.mNetworkInfo.getDetailedState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
                return 2;
            default:
                return 0;
        }
    }

    /* renamed from: com.android.wifitrackerlib.WifiEntry$1 */
    static /* synthetic */ class C33521 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                android.net.NetworkInfo$DetailedState[] r0 = android.net.NetworkInfo.DetailedState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$net$NetworkInfo$DetailedState = r0
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.SCANNING     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x001d }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0028 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.AUTHENTICATING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0033 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x003e }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.VERIFYING_POOR_LINK     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0049 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0054 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTED     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.WifiEntry.C33521.<clinit>():void");
        }
    }

    public String getSummary() {
        return getSummary(true);
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean shouldShowXLevelIcon() {
        return getConnectedState() != 0 && (!this.mIsValidated || !this.mIsDefaultNetwork) && !canSignIn();
    }

    public boolean hasInternetAccess() {
        return this.mIsValidated;
    }

    public boolean isDefaultNetwork() {
        return this.mIsDefaultNetwork;
    }

    public int getSecurity() {
        switch (Utils.getSingleSecurityTypeFromMultipleSecurityTypes(getSecurityTypes())) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 4;
            case 9:
                return 7;
            case 11:
            case 12:
                return 3;
            default:
                return 0;
        }
    }

    public List<Integer> getSecurityTypes() {
        return Collections.emptyList();
    }

    public synchronized ConnectedInfo getConnectedInfo() {
        if (getConnectedState() != 2) {
            return null;
        }
        return new ConnectedInfo(this.mConnectedInfo);
    }

    public static class ConnectedInfo {
        public List<String> dnsServers = new ArrayList();
        public int frequencyMhz;
        public String gateway;
        public String ipAddress;
        public List<String> ipv6Addresses = new ArrayList();
        public int linkSpeedMbps;
        public NetworkCapabilities networkCapabilities;
        public String subnetMask;
        public int wifiStandard = 0;

        public ConnectedInfo() {
        }

        public ConnectedInfo(ConnectedInfo connectedInfo) {
            this.frequencyMhz = connectedInfo.frequencyMhz;
            this.dnsServers = new ArrayList(this.dnsServers);
            this.linkSpeedMbps = connectedInfo.linkSpeedMbps;
            this.ipAddress = connectedInfo.ipAddress;
            this.ipv6Addresses = new ArrayList(connectedInfo.ipv6Addresses);
            this.gateway = connectedInfo.gateway;
            this.subnetMask = connectedInfo.subnetMask;
            this.wifiStandard = connectedInfo.wifiStandard;
            this.networkCapabilities = connectedInfo.networkCapabilities;
        }
    }

    public boolean canManageSubscription() {
        return this.mManageSubscriptionAction.isPresent();
    }

    public void manageSubscription() {
        this.mManageSubscriptionAction.ifPresent(new WifiEntry$$ExternalSyntheticLambda0());
    }

    public void setManageSubscriptionAction(ManageSubscriptionAction manageSubscriptionAction) {
        boolean z = !this.mManageSubscriptionAction.isPresent();
        this.mManageSubscriptionAction = Optional.m1751of(manageSubscriptionAction);
        if (z) {
            notifyOnUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public String getNetworkCapabilityDescription() {
        StringBuilder sb = new StringBuilder();
        if (getConnectedState() == 2) {
            sb.append("isValidated:").append(this.mIsValidated).append(", isDefaultNetwork:").append(this.mIsDefaultNetwork).append(", isLowQuality:").append(this.mIsLowQuality);
        }
        return sb.toString();
    }

    public synchronized void setListener(WifiEntryCallback wifiEntryCallback) {
        this.mListener = wifiEntryCallback;
    }

    /* access modifiers changed from: protected */
    public void notifyOnUpdated() {
        if (this.mListener != null) {
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$notifyOnUpdated$8$com-android-wifitrackerlib-WifiEntry  reason: not valid java name */
    public /* synthetic */ void m3378lambda$notifyOnUpdated$8$comandroidwifitrackerlibWifiEntry() {
        WifiEntryCallback wifiEntryCallback = this.mListener;
        if (wifiEntryCallback != null) {
            wifiEntryCallback.onUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || networkInfo == null)) {
            if (connectionInfoMatches(wifiInfo, networkInfo)) {
                this.mWifiInfo = wifiInfo;
                this.mNetworkInfo = networkInfo;
                int rssi = wifiInfo.getRssi();
                if (rssi != -127) {
                    this.mLevel = this.mWifiManager.calculateSignalLevel(rssi);
                }
                if (getConnectedState() == 2) {
                    if (this.mCalledConnect) {
                        this.mCalledConnect = false;
                        this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda2(this));
                    }
                    if (this.mConnectedInfo == null) {
                        this.mConnectedInfo = new ConnectedInfo();
                    }
                    this.mConnectedInfo.frequencyMhz = wifiInfo.getFrequency();
                    this.mConnectedInfo.linkSpeedMbps = wifiInfo.getLinkSpeed();
                    this.mConnectedInfo.wifiStandard = wifiInfo.getWifiStandard();
                }
                updateSecurityTypes();
                notifyOnUpdated();
            }
        }
        this.mWifiInfo = null;
        this.mNetworkInfo = null;
        this.mNetworkCapabilities = null;
        this.mConnectedInfo = null;
        this.mIsValidated = false;
        this.mIsDefaultNetwork = false;
        this.mIsLowQuality = false;
        if (this.mCalledDisconnect) {
            this.mCalledDisconnect = false;
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda3(this));
        }
        updateSecurityTypes();
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateConnectionInfo$9$com-android-wifitrackerlib-WifiEntry */
    public /* synthetic */ void mo47886x6620d799() {
        ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateConnectionInfo$10$com-android-wifitrackerlib-WifiEntry */
    public /* synthetic */ void mo47885x4cd5470f() {
        DisconnectCallback disconnectCallback = this.mDisconnectCallback;
        if (disconnectCallback != null) {
            disconnectCallback.onDisconnectResult(0);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateLinkProperties(LinkProperties linkProperties) {
        if (linkProperties != null) {
            if (getConnectedState() == 2) {
                if (this.mConnectedInfo == null) {
                    this.mConnectedInfo = new ConnectedInfo();
                }
                ArrayList arrayList = new ArrayList();
                for (LinkAddress next : linkProperties.getLinkAddresses()) {
                    if (next.getAddress() instanceof Inet4Address) {
                        this.mConnectedInfo.ipAddress = next.getAddress().getHostAddress();
                        try {
                            InetAddress byAddress = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
                            this.mConnectedInfo.subnetMask = Utils.getNetworkPart(byAddress, next.getPrefixLength()).getHostAddress();
                        } catch (UnknownHostException unused) {
                        }
                    } else if (next.getAddress() instanceof Inet6Address) {
                        arrayList.add(next.getAddress().getHostAddress());
                    }
                }
                this.mConnectedInfo.ipv6Addresses = arrayList;
                Iterator<RouteInfo> it = linkProperties.getRoutes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next2 = it.next();
                    if (next2.isDefaultRoute() && (next2.getDestination().getAddress() instanceof Inet4Address) && next2.hasGateway()) {
                        this.mConnectedInfo.gateway = next2.getGateway().getHostAddress();
                        break;
                    }
                }
                this.mConnectedInfo.dnsServers = (List) linkProperties.getDnsServers().stream().map(new WifiEntry$$ExternalSyntheticLambda4()).collect(Collectors.toList());
                notifyOnUpdated();
                return;
            }
        }
        this.mConnectedInfo = null;
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public synchronized void setIsDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = z;
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public synchronized void setIsLowQuality(boolean z) {
        this.mIsLowQuality = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities;
        ConnectedInfo connectedInfo = this.mConnectedInfo;
        if (connectedInfo != null) {
            connectedInfo.networkCapabilities = networkCapabilities;
            NetworkCapabilities networkCapabilities2 = this.mNetworkCapabilities;
            this.mIsValidated = networkCapabilities2 != null && networkCapabilities2.hasCapability(16);
            notifyOnUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized String getWifiInfoDescription() {
        StringJoiner stringJoiner;
        stringJoiner = new StringJoiner(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (getConnectedState() == 2 && this.mWifiInfo != null) {
            stringJoiner.add("f = " + this.mWifiInfo.getFrequency());
            String bssid = this.mWifiInfo.getBSSID();
            if (bssid != null) {
                stringJoiner.add(bssid);
            }
            stringJoiner.add("standard = " + getStandardString());
            stringJoiner.add("rssi = " + this.mWifiInfo.getRssi());
            stringJoiner.add("score = " + this.mWifiInfo.getScore());
            stringJoiner.add(String.format(" tx=%.1f,", Double.valueOf(this.mWifiInfo.getSuccessfulTxPacketsPerSecond())));
            stringJoiner.add(String.format("%.1f,", Double.valueOf(this.mWifiInfo.getRetriedTxPacketsPerSecond())));
            stringJoiner.add(String.format("%.1f ", Double.valueOf(this.mWifiInfo.getLostTxPacketsPerSecond())));
            stringJoiner.add(String.format("rx=%.1f", Double.valueOf(this.mWifiInfo.getSuccessfulRxPacketsPerSecond())));
        }
        return stringJoiner.toString();
    }

    protected class ConnectActionListener implements WifiManager.ActionListener {
        protected ConnectActionListener() {
        }

        public void onSuccess() {
            synchronized (WifiEntry.this) {
                WifiEntry.this.mCalledConnect = true;
            }
            WifiEntry.this.mCallbackHandler.postDelayed(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda0(this), 10000);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSuccess$0$com-android-wifitrackerlib-WifiEntry$ConnectActionListener */
        public /* synthetic */ void mo47899xf3841d7d() {
            ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
            if (connectCallback != null && WifiEntry.this.mCalledConnect && WifiEntry.this.getConnectedState() == 0) {
                connectCallback.onConnectResult(2);
                WifiEntry.this.mCalledConnect = false;
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFailure$1$com-android-wifitrackerlib-WifiEntry$ConnectActionListener */
        public /* synthetic */ void mo47898xecc374b5() {
            ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
        }
    }

    protected class ForgetActionListener implements WifiManager.ActionListener {
        protected ForgetActionListener() {
        }

        public void onSuccess() {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ForgetActionListener$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSuccess$0$com-android-wifitrackerlib-WifiEntry$ForgetActionListener */
        public /* synthetic */ void mo47902x9651724e() {
            ForgetCallback forgetCallback = WifiEntry.this.mForgetCallback;
            if (forgetCallback != null) {
                forgetCallback.onForgetResult(0);
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ForgetActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFailure$1$com-android-wifitrackerlib-WifiEntry$ForgetActionListener */
        public /* synthetic */ void mo47901xe8ae5416() {
            ForgetCallback forgetCallback = WifiEntry.this.mForgetCallback;
            if (forgetCallback != null) {
                forgetCallback.onForgetResult(1);
            }
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WifiEntry)) {
            return false;
        }
        return getKey().equals(((WifiEntry) obj).getKey());
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public String toString() {
        return getKey() + ",title:" + getTitle() + ",summary:" + getSummary() + ",isSaved:" + isSaved() + ",isSubscription:" + isSubscription() + ",isSuggestion:" + isSuggestion() + ",level:" + getLevel() + (shouldShowXLevelIcon() ? "X" : "") + ",security:" + getSecurityTypes() + ",standard:" + getWifiStandard() + ",connected:" + (getConnectedState() == 2 ? "true" : "false") + ",connectedInfo:" + getConnectedInfo() + ",isValidated:" + this.mIsValidated + ",isDefaultNetwork:" + this.mIsDefaultNetwork;
    }

    /* access modifiers changed from: protected */
    public void updateTransitionModeCapa(ScanResult scanResult) {
        this.mIsPskSaeTransitionMode = scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
        this.mIsOweTransitionMode = scanResult.capabilities.contains("OWE_TRANSITION");
    }

    public boolean isPskSaeTransitionMode() {
        return this.mIsPskSaeTransitionMode;
    }

    public boolean isOweTransitionMode() {
        return this.mIsOweTransitionMode;
    }

    private void updatetDeviceWifiGenerationInfo() {
        if (this.mWifiManager.isWifiStandardSupported(6)) {
            this.mDeviceWifiStandard = 6;
        } else if (this.mWifiManager.isWifiStandardSupported(5)) {
            this.mDeviceWifiStandard = 5;
        } else if (this.mWifiManager.isWifiStandardSupported(4)) {
            this.mDeviceWifiStandard = 4;
        } else {
            this.mDeviceWifiStandard = 1;
        }
    }

    public int getWifiStandard() {
        if (getConnectedInfo() == null || this.mWifiInfo == null || getConnectedState() != 2) {
            return this.mWifiStandard;
        }
        return this.mWifiInfo.getWifiStandard();
    }

    /* access modifiers changed from: protected */
    public void updateWifiGenerationInfo(List<ScanResult> list) {
        int i = this.mDeviceWifiStandard;
        for (ScanResult next : list) {
            int wifiStandard = next.getWifiStandard();
            if (wifiStandard < i) {
                i = wifiStandard;
            } else if (next.getBand() == 1 && wifiStandard == 6 && i == 5) {
                i = 4;
            }
        }
        this.mWifiStandard = i;
    }
}
