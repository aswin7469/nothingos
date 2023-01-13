package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.app.admin.WifiSsidPolicy;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.core.p004os.BuildCompat;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifitrackerlib.WifiEntry;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandardWifiEntry extends WifiEntry {
    public static final String KEY_PREFIX = "StandardWifiEntry:";
    static final String TAG = "StandardWifiEntry";
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private boolean mHasAddConfigUserRestriction;
    private final WifiTrackerInjector mInjector;
    private boolean mIsAdminRestricted;
    private final boolean mIsEnhancedOpenSupported;
    private boolean mIsUserShareable;
    private final boolean mIsWpa3SaeSupported;
    private final boolean mIsWpa3SuiteBSupported;
    private final StandardWifiEntryKey mKey;
    private final Map<Integer, List<ScanResult>> mMatchingScanResults;
    private final Map<Integer, WifiConfiguration> mMatchingWifiConfigs;
    private boolean mShouldAutoOpenCaptivePortal;
    private final List<ScanResult> mTargetScanResults;
    private List<Integer> mTargetSecurityTypes;
    private WifiConfiguration mTargetWifiConfig;
    private final UserManager mUserManager;

    StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, boolean z) {
        super(handler, wifiManager, z);
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurityTypes = new ArrayList();
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mIsAdminRestricted = false;
        this.mHasAddConfigUserRestriction = false;
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        this.mUserManager = wifiTrackerInjector.getUserManager();
        this.mDevicePolicyManager = wifiTrackerInjector.getDevicePolicyManager();
        updateSecurityTypes();
        updateAdminRestrictions();
    }

    StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        this(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }

    public String getKey() {
        return this.mKey.toString();
    }

    /* access modifiers changed from: package-private */
    public StandardWifiEntryKey getStandardWifiEntryKey() {
        return this.mKey;
    }

    public String getTitle() {
        if (isGbkSsidSupported()) {
            return Utils.getReadableText(this.mKey.getScanResultKey().getSsid());
        }
        return this.mKey.getScanResultKey().getSsid();
    }

    public synchronized String getSummary(boolean z) {
        String str;
        if (hasAdminRestrictions()) {
            return this.mContext.getString(C3351R.string.wifitrackerlib_admin_restricted_network);
        }
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(C3351R.string.wifitrackerlib_summary_separator));
        int connectedState = getConnectedState();
        if (connectedState == 0) {
            str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mTargetWifiConfig, this.mForSavedNetworksPage, z);
        } else if (connectedState == 1) {
            str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
        } else if (connectedState != 2) {
            Log.e(TAG, "getConnectedState() returned unknown state: " + connectedState);
            str = null;
        } else {
            str = Utils.getConnectedDescription(this.mContext, this.mTargetWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
        }
        if (!TextUtils.isEmpty(str)) {
            stringJoiner.add(str);
        }
        String autoConnectDescription = Utils.getAutoConnectDescription(this.mContext, this);
        if (!TextUtils.isEmpty(autoConnectDescription)) {
            stringJoiner.add(autoConnectDescription);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public CharSequence getSecondSummary() {
        return getConnectedState() == 2 ? Utils.getImsiProtectionDescription(this.mContext, getWifiConfiguration()) : "";
    }

    public String getSsid() {
        return this.mKey.getScanResultKey().getSsid();
    }

    public synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    public synchronized String getMacAddress() {
        if (this.mWifiInfo != null) {
            String macAddress = this.mWifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && !TextUtils.equals(macAddress, WifiInfo.DEFAULT_MAC_ADDRESS)) {
                return macAddress;
            }
        }
        if (this.mTargetWifiConfig != null) {
            if (getPrivacy() == 1) {
                return this.mTargetWifiConfig.getRandomizedMacAddress().toString();
            }
        }
        String[] factoryMacAddresses = this.mWifiManager.getFactoryMacAddresses();
        if (factoryMacAddresses.length <= 0) {
            return null;
        }
        return factoryMacAddresses[0];
    }

    public synchronized boolean isMetered() {
        boolean z;
        WifiConfiguration wifiConfiguration;
        z = true;
        if (getMeteredChoice() != 1 && ((wifiConfiguration = this.mTargetWifiConfig) == null || !wifiConfiguration.meteredHint)) {
            z = false;
        }
        return z;
    }

    public synchronized boolean isSaved() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion && !this.mTargetWifiConfig.isEphemeral();
    }

    public synchronized boolean isSuggestion() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public synchronized WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0076, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0078, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x007a, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canConnect() {
        /*
            r5 = this;
            monitor-enter(r5)
            int r0 = r5.mLevel     // Catch:{ all -> 0x007b }
            r1 = -1
            r2 = 0
            if (r0 == r1) goto L_0x0079
            int r0 = r5.getConnectedState()     // Catch:{ all -> 0x007b }
            if (r0 == 0) goto L_0x000e
            goto L_0x0079
        L_0x000e:
            boolean r0 = r5.hasAdminRestrictions()     // Catch:{ all -> 0x007b }
            if (r0 == 0) goto L_0x0016
            monitor-exit(r5)
            return r2
        L_0x0016:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x007b }
            r3 = 3
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)     // Catch:{ all -> 0x007b }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x007b }
            r3 = 1
            if (r0 == 0) goto L_0x0077
            android.net.wifi.WifiConfiguration r0 = r5.mTargetWifiConfig     // Catch:{ all -> 0x007b }
            if (r0 == 0) goto L_0x0077
            android.net.wifi.WifiEnterpriseConfig r0 = r0.enterpriseConfig     // Catch:{ all -> 0x007b }
            if (r0 == 0) goto L_0x0077
            android.net.wifi.WifiConfiguration r0 = r5.mTargetWifiConfig     // Catch:{ all -> 0x007b }
            android.net.wifi.WifiEnterpriseConfig r0 = r0.enterpriseConfig     // Catch:{ all -> 0x007b }
            boolean r0 = r0.isAuthenticationSimBased()     // Catch:{ all -> 0x007b }
            if (r0 != 0) goto L_0x0038
            monitor-exit(r5)
            return r3
        L_0x0038:
            android.content.Context r0 = r5.mContext     // Catch:{ all -> 0x007b }
            java.lang.Class<android.telephony.SubscriptionManager> r4 = android.telephony.SubscriptionManager.class
            java.lang.Object r0 = r0.getSystemService(r4)     // Catch:{ all -> 0x007b }
            android.telephony.SubscriptionManager r0 = (android.telephony.SubscriptionManager) r0     // Catch:{ all -> 0x007b }
            java.util.List r0 = r0.getActiveSubscriptionInfoList()     // Catch:{ all -> 0x007b }
            if (r0 == 0) goto L_0x0075
            int r4 = r0.size()     // Catch:{ all -> 0x007b }
            if (r4 != 0) goto L_0x004f
            goto L_0x0075
        L_0x004f:
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x007b }
            int r4 = r4.carrierId     // Catch:{ all -> 0x007b }
            if (r4 != r1) goto L_0x0057
            monitor-exit(r5)
            return r3
        L_0x0057:
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x007b }
        L_0x005b:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x0073
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x007b }
            android.telephony.SubscriptionInfo r1 = (android.telephony.SubscriptionInfo) r1     // Catch:{ all -> 0x007b }
            int r1 = r1.getCarrierId()     // Catch:{ all -> 0x007b }
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x007b }
            int r4 = r4.carrierId     // Catch:{ all -> 0x007b }
            if (r1 != r4) goto L_0x005b
            monitor-exit(r5)
            return r3
        L_0x0073:
            monitor-exit(r5)
            return r2
        L_0x0075:
            monitor-exit(r5)
            return r2
        L_0x0077:
            monitor-exit(r5)
            return r3
        L_0x0079:
            monitor-exit(r5)
            return r2
        L_0x007b:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canConnect():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x013e, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x014e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void connect(com.android.wifitrackerlib.WifiEntry.ConnectCallback r7) {
        /*
            r6 = this;
            java.lang.String r0 = "\""
            java.lang.String r1 = "\""
            java.lang.String r2 = "\""
            monitor-enter(r6)
            r6.mConnectCallback = r7     // Catch:{ all -> 0x014f }
            r3 = 1
            r6.mShouldAutoOpenCaptivePortal = r3     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiManager r3 = r6.mWifiManager     // Catch:{ all -> 0x014f }
            r3.stopRestrictingAutoJoinToSubscriptionId()     // Catch:{ all -> 0x014f }
            boolean r3 = r6.isSaved()     // Catch:{ all -> 0x014f }
            if (r3 != 0) goto L_0x011d
            boolean r3 = r6.isSuggestion()     // Catch:{ all -> 0x014f }
            if (r3 == 0) goto L_0x001f
            goto L_0x011d
        L_0x001f:
            java.util.List<java.lang.Integer> r3 = r6.mTargetSecurityTypes     // Catch:{ all -> 0x014f }
            r4 = 6
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x014f }
            boolean r3 = r3.contains(r5)     // Catch:{ all -> 0x014f }
            r5 = 0
            if (r3 == 0) goto L_0x00bf
            android.net.wifi.WifiConfiguration r7 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x014f }
            r7.<init>()     // Catch:{ all -> 0x014f }
            boolean r1 = isGbkSsidSupported()     // Catch:{ all -> 0x014f }
            if (r1 == 0) goto L_0x0045
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x014f }
            r7.SSID = r1     // Catch:{ all -> 0x014f }
            goto L_0x0064
        L_0x0045:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x014f }
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r2 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r2 = r2.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r2 = r2.getSsid()     // Catch:{ all -> 0x014f }
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)     // Catch:{ all -> 0x014f }
            java.lang.String r2 = "\""
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)     // Catch:{ all -> 0x014f }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x014f }
            r7.SSID = r1     // Catch:{ all -> 0x014f }
        L_0x0064:
            r7.setSecurityParams((int) r4)     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiManager r1 = r6.mWifiManager     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r2 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x014f }
            r2.<init>()     // Catch:{ all -> 0x014f }
            r1.connect((android.net.wifi.WifiConfiguration) r7, (android.net.wifi.WifiManager.ActionListener) r2)     // Catch:{ all -> 0x014f }
            java.util.List<java.lang.Integer> r7 = r6.mTargetSecurityTypes     // Catch:{ all -> 0x014f }
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x014f }
            boolean r7 = r7.contains(r1)     // Catch:{ all -> 0x014f }
            if (r7 == 0) goto L_0x014d
            android.net.wifi.WifiConfiguration r7 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x014f }
            r7.<init>()     // Catch:{ all -> 0x014f }
            boolean r1 = isGbkSsidSupported()     // Catch:{ all -> 0x014f }
            if (r1 == 0) goto L_0x0095
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x014f }
            r7.SSID = r0     // Catch:{ all -> 0x014f }
            goto L_0x00b4
        L_0x0095:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x014f }
            r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x014f }
            java.lang.StringBuilder r0 = r1.append((java.lang.String) r0)     // Catch:{ all -> 0x014f }
            java.lang.String r1 = "\""
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)     // Catch:{ all -> 0x014f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x014f }
            r7.SSID = r0     // Catch:{ all -> 0x014f }
        L_0x00b4:
            r7.setSecurityParams((int) r5)     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiManager r0 = r6.mWifiManager     // Catch:{ all -> 0x014f }
            r1 = 0
            r0.save(r7, r1)     // Catch:{ all -> 0x014f }
            goto L_0x014d
        L_0x00bf:
            java.util.List<java.lang.Integer> r0 = r6.mTargetSecurityTypes     // Catch:{ all -> 0x014f }
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x014f }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x014f }
            if (r0 == 0) goto L_0x0110
            android.net.wifi.WifiConfiguration r7 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x014f }
            r7.<init>()     // Catch:{ all -> 0x014f }
            boolean r0 = isGbkSsidSupported()     // Catch:{ all -> 0x014f }
            if (r0 == 0) goto L_0x00e3
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x014f }
            r7.SSID = r0     // Catch:{ all -> 0x014f }
            goto L_0x0102
        L_0x00e3:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x014f }
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r6.mKey     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x014f }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x014f }
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)     // Catch:{ all -> 0x014f }
            java.lang.String r1 = "\""
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)     // Catch:{ all -> 0x014f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x014f }
            r7.SSID = r0     // Catch:{ all -> 0x014f }
        L_0x0102:
            r7.setSecurityParams((int) r5)     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiManager r0 = r6.mWifiManager     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x014f }
            r1.<init>()     // Catch:{ all -> 0x014f }
            r0.connect((android.net.wifi.WifiConfiguration) r7, (android.net.wifi.WifiManager.ActionListener) r1)     // Catch:{ all -> 0x014f }
            goto L_0x014d
        L_0x0110:
            if (r7 == 0) goto L_0x014d
            android.os.Handler r0 = r6.mCallbackHandler     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1     // Catch:{ all -> 0x014f }
            r1.<init>(r7)     // Catch:{ all -> 0x014f }
            r0.post(r1)     // Catch:{ all -> 0x014f }
            goto L_0x014d
        L_0x011d:
            android.net.wifi.WifiConfiguration r0 = r6.mTargetWifiConfig     // Catch:{ all -> 0x014f }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimCredential(r0)     // Catch:{ all -> 0x014f }
            if (r0 == 0) goto L_0x013f
            android.content.Context r0 = r6.mContext     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiConfiguration r1 = r6.mTargetWifiConfig     // Catch:{ all -> 0x014f }
            int r1 = r1.carrierId     // Catch:{ all -> 0x014f }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimPresent(r0, r1)     // Catch:{ all -> 0x014f }
            if (r0 != 0) goto L_0x013f
            if (r7 == 0) goto L_0x013d
            android.os.Handler r0 = r6.mCallbackHandler     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0     // Catch:{ all -> 0x014f }
            r1.<init>(r7)     // Catch:{ all -> 0x014f }
            r0.post(r1)     // Catch:{ all -> 0x014f }
        L_0x013d:
            monitor-exit(r6)
            return
        L_0x013f:
            android.net.wifi.WifiManager r7 = r6.mWifiManager     // Catch:{ all -> 0x014f }
            android.net.wifi.WifiConfiguration r0 = r6.mTargetWifiConfig     // Catch:{ all -> 0x014f }
            int r0 = r0.networkId     // Catch:{ all -> 0x014f }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x014f }
            r1.<init>()     // Catch:{ all -> 0x014f }
            r7.connect((int) r0, (android.net.wifi.WifiManager.ActionListener) r1)     // Catch:{ all -> 0x014f }
        L_0x014d:
            monitor-exit(r6)
            return
        L_0x014f:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connect(com.android.wifitrackerlib.WifiEntry$ConnectCallback):void");
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        if (canDisconnect()) {
            this.mCalledDisconnect = true;
            this.mDisconnectCallback = disconnectCallback;
            this.mCallbackHandler.postDelayed(new StandardWifiEntry$$ExternalSyntheticLambda2(this, disconnectCallback), 10000);
            this.mWifiManager.disableEphemeralNetwork("\"" + this.mKey.getScanResultKey().getSsid() + "\"");
            this.mWifiManager.disconnect();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$disconnect$2$com-android-wifitrackerlib-StandardWifiEntry  reason: not valid java name */
    public /* synthetic */ void m3377lambda$disconnect$2$comandroidwifitrackerlibStandardWifiEntry(WifiEntry.DisconnectCallback disconnectCallback) {
        if (disconnectCallback != null && this.mCalledDisconnect) {
            disconnectCallback.onDisconnectResult(1);
        }
    }

    public boolean canForget() {
        return getWifiConfiguration() != null;
    }

    public synchronized void forget(WifiEntry.ForgetCallback forgetCallback) {
        if (canForget()) {
            this.mForgetCallback = forgetCallback;
            this.mWifiManager.forget(this.mTargetWifiConfig.networkId, new WifiEntry.ForgetActionListener());
        }
    }

    public synchronized boolean canSignIn() {
        return this.mNetworkCapabilities != null && this.mNetworkCapabilities.hasCapability(17);
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
        if (canSignIn()) {
            NonSdkApiWrapper.startCaptivePortalApp((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class), this.mWifiManager.getCurrentNetwork());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x005e, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canShare() {
        /*
            r5 = this;
            monitor-enter(r5)
            com.android.wifitrackerlib.WifiTrackerInjector r0 = r5.mInjector     // Catch:{ all -> 0x0061 }
            boolean r0 = r0.isDemoMode()     // Catch:{ all -> 0x0061 }
            r1 = 0
            if (r0 == 0) goto L_0x000c
            monitor-exit(r5)
            return r1
        L_0x000c:
            android.net.wifi.WifiConfiguration r0 = r5.getWifiConfiguration()     // Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x0014
            monitor-exit(r5)
            return r1
        L_0x0014:
            boolean r2 = androidx.core.p004os.BuildCompat.isAtLeastT()     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x0038
            android.os.UserManager r2 = r5.mUserManager     // Catch:{ all -> 0x0061 }
            java.lang.String r3 = "no_sharing_admin_configured_wifi"
            int r4 = r0.creatorUid     // Catch:{ all -> 0x0061 }
            android.os.UserHandle r4 = android.os.UserHandle.getUserHandleForUid(r4)     // Catch:{ all -> 0x0061 }
            boolean r2 = r2.hasUserRestrictionForUser(r3, r4)     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x0038
            int r2 = r0.creatorUid     // Catch:{ all -> 0x0061 }
            java.lang.String r0 = r0.creatorName     // Catch:{ all -> 0x0061 }
            android.content.Context r3 = r5.mContext     // Catch:{ all -> 0x0061 }
            boolean r0 = com.android.wifitrackerlib.Utils.isDeviceOrProfileOwner(r2, r0, r3)     // Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0038
            monitor-exit(r5)
            return r1
        L_0x0038:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x0061 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0061 }
        L_0x003e:
            boolean r2 = r0.hasNext()     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x005f
            java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x0061 }
            java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ all -> 0x0061 }
            int r2 = r2.intValue()     // Catch:{ all -> 0x0061 }
            r3 = 1
            if (r2 == 0) goto L_0x005d
            if (r2 == r3) goto L_0x005d
            r4 = 2
            if (r2 == r4) goto L_0x005d
            r4 = 4
            if (r2 == r4) goto L_0x005d
            r4 = 6
            if (r2 == r4) goto L_0x005d
            goto L_0x003e
        L_0x005d:
            monitor-exit(r5)
            return r3
        L_0x005f:
            monitor-exit(r5)
            return r1
        L_0x0061:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canShare():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005e, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canEasyConnect() {
        /*
            r5 = this;
            monitor-enter(r5)
            com.android.wifitrackerlib.WifiTrackerInjector r0 = r5.mInjector     // Catch:{ all -> 0x005f }
            boolean r0 = r0.isDemoMode()     // Catch:{ all -> 0x005f }
            r1 = 0
            if (r0 == 0) goto L_0x000c
            monitor-exit(r5)
            return r1
        L_0x000c:
            android.net.wifi.WifiConfiguration r0 = r5.getWifiConfiguration()     // Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x0014
            monitor-exit(r5)
            return r1
        L_0x0014:
            android.net.wifi.WifiManager r2 = r5.mWifiManager     // Catch:{ all -> 0x005f }
            boolean r2 = r2.isEasyConnectSupported()     // Catch:{ all -> 0x005f }
            if (r2 != 0) goto L_0x001e
            monitor-exit(r5)
            return r1
        L_0x001e:
            boolean r2 = androidx.core.p004os.BuildCompat.isAtLeastT()     // Catch:{ all -> 0x005f }
            if (r2 == 0) goto L_0x0042
            android.os.UserManager r2 = r5.mUserManager     // Catch:{ all -> 0x005f }
            java.lang.String r3 = "no_sharing_admin_configured_wifi"
            int r4 = r0.creatorUid     // Catch:{ all -> 0x005f }
            android.os.UserHandle r4 = android.os.UserHandle.getUserHandleForUid(r4)     // Catch:{ all -> 0x005f }
            boolean r2 = r2.hasUserRestrictionForUser(r3, r4)     // Catch:{ all -> 0x005f }
            if (r2 == 0) goto L_0x0042
            int r2 = r0.creatorUid     // Catch:{ all -> 0x005f }
            java.lang.String r0 = r0.creatorName     // Catch:{ all -> 0x005f }
            android.content.Context r3 = r5.mContext     // Catch:{ all -> 0x005f }
            boolean r0 = com.android.wifitrackerlib.Utils.isDeviceOrProfileOwner(r2, r0, r3)     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0042
            monitor-exit(r5)
            return r1
        L_0x0042:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x005f }
            r2 = 2
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x005f }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x005c
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x005f }
            r2 = 4
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x005f }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x005d
        L_0x005c:
            r1 = 1
        L_0x005d:
            monitor-exit(r5)
            return r1
        L_0x005f:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canEasyConnect():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0018, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int getMeteredChoice() {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.isSuggestion()     // Catch:{ all -> 0x001a }
            if (r0 != 0) goto L_0x0017
            android.net.wifi.WifiConfiguration r0 = r2.mTargetWifiConfig     // Catch:{ all -> 0x001a }
            if (r0 == 0) goto L_0x0017
            int r0 = r0.meteredOverride     // Catch:{ all -> 0x001a }
            r1 = 1
            if (r0 != r1) goto L_0x0012
            monitor-exit(r2)
            return r1
        L_0x0012:
            r1 = 2
            if (r0 != r1) goto L_0x0017
            monitor-exit(r2)
            return r1
        L_0x0017:
            monitor-exit(r2)
            r2 = 0
            return r2
        L_0x001a:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.getMeteredChoice():int");
    }

    public boolean canSetMeteredChoice() {
        return getWifiConfiguration() != null;
    }

    public synchronized void setMeteredChoice(int i) {
        if (canSetMeteredChoice()) {
            if (i == 0) {
                this.mTargetWifiConfig.meteredOverride = 0;
            } else if (i == 1) {
                this.mTargetWifiConfig.meteredOverride = 1;
            } else if (i == 2) {
                this.mTargetWifiConfig.meteredOverride = 2;
            }
            this.mWifiManager.save(this.mTargetWifiConfig, (WifiManager.ActionListener) null);
        }
    }

    public boolean canSetPrivacy() {
        return isSaved();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000d, code lost:
        return 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int getPrivacy() {
        /*
            r1 = this;
            monitor-enter(r1)
            android.net.wifi.WifiConfiguration r0 = r1.mTargetWifiConfig     // Catch:{ all -> 0x000f }
            if (r0 == 0) goto L_0x000c
            int r0 = r0.macRandomizationSetting     // Catch:{ all -> 0x000f }
            if (r0 != 0) goto L_0x000c
            monitor-exit(r1)
            r1 = 0
            return r1
        L_0x000c:
            monitor-exit(r1)
            r1 = 1
            return r1
        L_0x000f:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.getPrivacy():int");
    }

    public synchronized void setPrivacy(int i) {
        if (canSetPrivacy()) {
            this.mTargetWifiConfig.macRandomizationSetting = i == 1 ? 3 : 0;
            this.mWifiManager.save(this.mTargetWifiConfig, (WifiManager.ActionListener) null);
        }
    }

    public synchronized boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public boolean canSetAutoJoinEnabled() {
        return isSaved() || isSuggestion();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setAutoJoinEnabled(boolean r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.WifiConfiguration r0 = r2.mTargetWifiConfig     // Catch:{ all -> 0x0019 }
            if (r0 == 0) goto L_0x0017
            boolean r0 = r2.canSetAutoJoinEnabled()     // Catch:{ all -> 0x0019 }
            if (r0 != 0) goto L_0x000c
            goto L_0x0017
        L_0x000c:
            android.net.wifi.WifiManager r0 = r2.mWifiManager     // Catch:{ all -> 0x0019 }
            android.net.wifi.WifiConfiguration r1 = r2.mTargetWifiConfig     // Catch:{ all -> 0x0019 }
            int r1 = r1.networkId     // Catch:{ all -> 0x0019 }
            r0.allowAutojoin(r1, r3)     // Catch:{ all -> 0x0019 }
            monitor-exit(r2)
            return
        L_0x0017:
            monitor-exit(r2)
            return
        L_0x0019:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.setAutoJoinEnabled(boolean):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:103:0x019e, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004d, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0062, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0077, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x008c, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a1, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ba, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00cf, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x014d, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x017b, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSecurityString(boolean r6) {
        /*
            r5 = this;
            java.lang.String r0 = "Couldn't get string for security types: "
            monitor-enter(r5)
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            int r1 = r1.size()     // Catch:{ all -> 0x019f }
            if (r1 != 0) goto L_0x001a
            if (r6 == 0) goto L_0x0010
            java.lang.String r6 = ""
            goto L_0x0018
        L_0x0010:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x0018:
            monitor-exit(r5)
            return r6
        L_0x001a:
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            int r1 = r1.size()     // Catch:{ all -> 0x019f }
            r2 = 1
            r3 = 9
            r4 = 0
            if (r1 != r2) goto L_0x00d0
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x019f }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x019f }
            int r1 = r1.intValue()     // Catch:{ all -> 0x019f }
            if (r1 == r3) goto L_0x00bb
            switch(r1) {
                case 0: goto L_0x00ac;
                case 1: goto L_0x00a2;
                case 2: goto L_0x008d;
                case 3: goto L_0x0078;
                case 4: goto L_0x0063;
                case 5: goto L_0x004e;
                case 6: goto L_0x0039;
                default: goto L_0x0037;
            }     // Catch:{ all -> 0x019f }
        L_0x0037:
            goto L_0x00d0
        L_0x0039:
            if (r6 == 0) goto L_0x0044
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_owe     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x004c
        L_0x0044:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_owe     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x004c:
            monitor-exit(r5)
            return r6
        L_0x004e:
            if (r6 == 0) goto L_0x0059
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_eap_suiteb     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x0061
        L_0x0059:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_eap_suiteb     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x0061:
            monitor-exit(r5)
            return r6
        L_0x0063:
            if (r6 == 0) goto L_0x006e
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_sae     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x0076
        L_0x006e:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_sae     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x0076:
            monitor-exit(r5)
            return r6
        L_0x0078:
            if (r6 == 0) goto L_0x0083
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_eap_wpa_wpa2     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x008b
        L_0x0083:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_eap_wpa_wpa2     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x008b:
            monitor-exit(r5)
            return r6
        L_0x008d:
            if (r6 == 0) goto L_0x0098
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_wpa_wpa2     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x00a0
        L_0x0098:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_wpa_wpa2     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x00a0:
            monitor-exit(r5)
            return r6
        L_0x00a2:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_wep     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            monitor-exit(r5)
            return r6
        L_0x00ac:
            if (r6 == 0) goto L_0x00b1
            java.lang.String r6 = ""
            goto L_0x00b9
        L_0x00b1:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x00b9:
            monitor-exit(r5)
            return r6
        L_0x00bb:
            if (r6 == 0) goto L_0x00c6
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_eap_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x00ce
        L_0x00c6:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_eap_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x00ce:
            monitor-exit(r5)
            return r6
        L_0x00d0:
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            int r1 = r1.size()     // Catch:{ all -> 0x019f }
            r2 = 2
            if (r1 != r2) goto L_0x017c
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r4)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x0120
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            r4 = 6
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r4)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x0120
            java.util.StringJoiner r0 = new java.util.StringJoiner     // Catch:{ all -> 0x019f }
            java.lang.String r1 = "/"
            r0.<init>(r1)     // Catch:{ all -> 0x019f }
            android.content.Context r1 = r5.mContext     // Catch:{ all -> 0x019f }
            int r2 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x019f }
            java.lang.String r1 = r1.getString(r2)     // Catch:{ all -> 0x019f }
            r0.add(r1)     // Catch:{ all -> 0x019f }
            if (r6 == 0) goto L_0x010f
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r1 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_owe     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r1)     // Catch:{ all -> 0x019f }
            goto L_0x0117
        L_0x010f:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r1 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_owe     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r1)     // Catch:{ all -> 0x019f }
        L_0x0117:
            r0.add(r6)     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x019f }
            monitor-exit(r5)
            return r6
        L_0x0120:
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x014e
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            r2 = 4
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x014e
            if (r6 == 0) goto L_0x0144
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_wpa_wpa2_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x014c
        L_0x0144:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_wpa_wpa2_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x014c:
            monitor-exit(r5)
            return r6
        L_0x014e:
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            r2 = 3
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x017c
            java.util.List<java.lang.Integer> r1 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r3)     // Catch:{ all -> 0x019f }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x019f }
            if (r1 == 0) goto L_0x017c
            if (r6 == 0) goto L_0x0172
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_short_eap_wpa_wpa2_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
            goto L_0x017a
        L_0x0172:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_eap_wpa_wpa2_wpa3     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x017a:
            monitor-exit(r5)
            return r6
        L_0x017c:
            java.lang.String r1 = "StandardWifiEntry"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x019f }
            r2.<init>((java.lang.String) r0)     // Catch:{ all -> 0x019f }
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x019f }
            java.lang.StringBuilder r0 = r2.append((java.lang.Object) r0)     // Catch:{ all -> 0x019f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x019f }
            android.util.Log.e(r1, r0)     // Catch:{ all -> 0x019f }
            if (r6 == 0) goto L_0x0195
            java.lang.String r6 = ""
            goto L_0x019d
        L_0x0195:
            android.content.Context r6 = r5.mContext     // Catch:{ all -> 0x019f }
            int r0 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x019f }
            java.lang.String r6 = r6.getString(r0)     // Catch:{ all -> 0x019f }
        L_0x019d:
            monitor-exit(r5)
            return r6
        L_0x019f:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.getSecurityString(boolean):java.lang.String");
    }

    public synchronized String getStandardString() {
        if (this.mWifiInfo != null) {
            return Utils.getStandardString(this.mContext, this.mWifiInfo.getWifiStandard());
        } else if (this.mTargetScanResults.isEmpty()) {
            return "";
        } else {
            return Utils.getStandardString(this.mContext, this.mTargetScanResults.get(0).getWifiStandard());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002b, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002e, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean shouldEditBeforeConnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.net.wifi.WifiConfiguration r0 = r3.getWifiConfiguration()     // Catch:{ all -> 0x002f }
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r3)
            return r1
        L_0x000a:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r0 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            int r2 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x002d
            r2 = 2
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 8
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 5
            int r0 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r0 <= 0) goto L_0x002d
        L_0x002a:
            monitor-exit(r3)
            r3 = 1
            return r3
        L_0x002d:
            monitor-exit(r3)
            return r1
        L_0x002f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.shouldEditBeforeConnect():boolean");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        String ssid = this.mKey.getScanResultKey().getSsid();
        for (ScanResult next : list) {
            String str = next.SSID;
            if (isGbkSsidSupported()) {
                str = next.getWifiSsid().toString();
            }
            if (!TextUtils.equals(str, ssid)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + str + ", ScanResult: " + next);
            }
        }
        this.mMatchingScanResults.clear();
        Set<Integer> securityTypes = this.mKey.getScanResultKey().getSecurityTypes();
        for (ScanResult next2 : list) {
            for (Integer intValue : Utils.getSecurityTypesFromScanResult(next2)) {
                int intValue2 = intValue.intValue();
                if (securityTypes.contains(Integer.valueOf(intValue2))) {
                    if (isSecurityTypeSupported(intValue2)) {
                        if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                            this.mMatchingScanResults.put(Integer.valueOf(intValue2), new ArrayList());
                        }
                        this.mMatchingScanResults.get(Integer.valueOf(intValue2)).add(next2);
                    }
                }
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private synchronized void updateTargetScanResultInfo() {
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (bestScanResultByLevel != null) {
            updateTransitionModeCapa(bestScanResultByLevel);
        }
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
        }
        updateWifiGenerationInfo(this.mTargetScanResults);
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            signIn((WifiEntry.SignInCallback) null);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        if (list == null) {
            list = Collections.emptyList();
        }
        ScanResultKey scanResultKey = this.mKey.getScanResultKey();
        String ssid = scanResultKey.getSsid();
        Set<Integer> securityTypes = scanResultKey.getSecurityTypes();
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration next : list) {
            String str = next.SSID;
            if (!isGbkSsidSupported()) {
                str = WifiInfo.sanitizeSsid(next.SSID);
            }
            if (TextUtils.equals(ssid, str)) {
                Iterator<Integer> it = Utils.getSecurityTypesFromWifiConfiguration(next).iterator();
                while (true) {
                    if (it.hasNext()) {
                        int intValue = it.next().intValue();
                        if (!securityTypes.contains(Integer.valueOf(intValue))) {
                            throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + securityTypes + ", Actual: " + intValue + ", Config: " + next);
                        } else if (isSecurityTypeSupported(intValue)) {
                            this.mMatchingWifiConfigs.put(Integer.valueOf(intValue), next);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + WifiInfo.sanitizeSsid(next.SSID) + ", Config: " + next);
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private boolean isSecurityTypeSupported(int i) {
        if (i == 4) {
            return this.mIsWpa3SaeSupported;
        }
        if (i == 5) {
            return this.mIsWpa3SuiteBSupported;
        }
        if (i != 6) {
            return true;
        }
        return this.mIsEnhancedOpenSupported;
    }

    /* access modifiers changed from: protected */
    public synchronized void updateSecurityTypes() {
        boolean z;
        this.mTargetSecurityTypes.clear();
        if (!(this.mWifiInfo == null || this.mWifiInfo.getCurrentSecurityType() == -1)) {
            this.mTargetSecurityTypes.add(Integer.valueOf(this.mWifiInfo.getCurrentSecurityType()));
        }
        Set<Integer> keySet = this.mMatchingWifiConfigs.keySet();
        if (this.mTargetSecurityTypes.isEmpty() && this.mKey.isTargetingNewNetworks()) {
            Set<Integer> keySet2 = this.mMatchingScanResults.keySet();
            Iterator<Integer> it = keySet.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (keySet2.contains(Integer.valueOf(it.next().intValue()))) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                this.mTargetSecurityTypes.addAll(keySet2);
            }
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(keySet);
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(this.mKey.getScanResultKey().getSecurityTypes());
        }
        this.mTargetWifiConfig = this.mMatchingWifiConfigs.get(Integer.valueOf(Utils.getSingleSecurityTypeFromMultipleSecurityTypes(this.mTargetSecurityTypes)));
        ArraySet arraySet = new ArraySet();
        for (Integer intValue : this.mTargetSecurityTypes) {
            int intValue2 = intValue.intValue();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                arraySet.addAll(this.mMatchingScanResults.get(Integer.valueOf(intValue2)));
            }
        }
        this.mTargetScanResults.clear();
        this.mTargetScanResults.addAll(arraySet);
    }

    /* access modifiers changed from: package-private */
    public synchronized void setUserShareable(boolean z) {
        this.mIsUserShareable = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isUserShareable() {
        return this.mIsUserShareable;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean connectionInfoMatches(android.net.wifi.WifiInfo r4, android.net.NetworkInfo r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r5 = r4.isPasspointAp()     // Catch:{ all -> 0x0034 }
            r0 = 0
            if (r5 != 0) goto L_0x0032
            boolean r5 = r4.isOsuAp()     // Catch:{ all -> 0x0034 }
            if (r5 == 0) goto L_0x000f
            goto L_0x0032
        L_0x000f:
            java.util.Map<java.lang.Integer, android.net.wifi.WifiConfiguration> r5 = r3.mMatchingWifiConfigs     // Catch:{ all -> 0x0034 }
            java.util.Collection r5 = r5.values()     // Catch:{ all -> 0x0034 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x0034 }
        L_0x0019:
            boolean r1 = r5.hasNext()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0030
            java.lang.Object r1 = r5.next()     // Catch:{ all -> 0x0034 }
            android.net.wifi.WifiConfiguration r1 = (android.net.wifi.WifiConfiguration) r1     // Catch:{ all -> 0x0034 }
            int r1 = r1.networkId     // Catch:{ all -> 0x0034 }
            int r2 = r4.getNetworkId()     // Catch:{ all -> 0x0034 }
            if (r1 != r2) goto L_0x0019
            monitor-exit(r3)
            r3 = 1
            return r3
        L_0x0030:
            monitor-exit(r3)
            return r0
        L_0x0032:
            monitor-exit(r3)
            return r0
        L_0x0034:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connectionInfoMatches(android.net.wifi.WifiInfo, android.net.NetworkInfo):boolean");
    }

    static StandardWifiEntryKey ssidAndSecurityTypeToStandardWifiEntryKey(String str, int i) {
        return ssidAndSecurityTypeToStandardWifiEntryKey(str, i, false);
    }

    static StandardWifiEntryKey ssidAndSecurityTypeToStandardWifiEntryKey(String str, int i, boolean z) {
        return new StandardWifiEntryKey(new ScanResultKey(str, Collections.singletonList(Integer.valueOf(i))), z);
    }

    /* access modifiers changed from: protected */
    public synchronized String getScanResultDescription() {
        if (this.mTargetScanResults.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        sb.append(getScanResultDescription(2400, 2500)).append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        sb.append(getScanResultDescription(4900, 5900)).append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        sb.append(getScanResultDescription((int) WifiEntry.MIN_FREQ_6GHZ, (int) WifiEntry.MAX_FREQ_6GHZ)).append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        sb.append(getScanResultDescription(58320, 70200));
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    private synchronized String getScanResultDescription(int i, int i2) {
        List list = (List) this.mTargetScanResults.stream().filter(new StandardWifiEntry$$ExternalSyntheticLambda3(i, i2)).sorted(Comparator.comparingInt(new StandardWifiEntry$$ExternalSyntheticLambda4())).collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(NavigationBarInflaterView.KEY_CODE_START).append(size).append(NavigationBarInflaterView.KEY_CODE_END);
        if (size > 4) {
            sb.append("max=").append(list.stream().mapToInt(new StandardWifiEntry$$ExternalSyntheticLambda5()).max().getAsInt()).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
        }
        list.forEach(new StandardWifiEntry$$ExternalSyntheticLambda6(this, sb, SystemClock.elapsedRealtime()));
        return sb.toString();
    }

    static /* synthetic */ boolean lambda$getScanResultDescription$3(int i, int i2, ScanResult scanResult) {
        return scanResult.frequency >= i && scanResult.frequency <= i2;
    }

    static /* synthetic */ int lambda$getScanResultDescription$4(ScanResult scanResult) {
        return scanResult.level * -1;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getScanResultDescription$6$com-android-wifitrackerlib-StandardWifiEntry */
    public /* synthetic */ void mo47859x2217af08(StringBuilder sb, long j, ScanResult scanResult) {
        sb.append(getScanResultDescription(scanResult, j));
    }

    private synchronized String getScanResultDescription(ScanResult scanResult, long j) {
        StringBuilder sb;
        sb = new StringBuilder(" \n{");
        sb.append(scanResult.BSSID);
        if (this.mWifiInfo != null && scanResult.BSSID.equals(this.mWifiInfo.getBSSID())) {
            sb.append("*");
        }
        sb.append("=").append(scanResult.frequency);
        sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(scanResult.level);
        int wifiStandard = scanResult.getWifiStandard();
        sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(Utils.getStandardString(this.mContext, wifiStandard));
        if (BuildCompat.isAtLeastT() && wifiStandard == 8) {
            sb.append(",mldMac=").append((Object) scanResult.getApMldMacAddress());
            sb.append(",linkId=").append(scanResult.getApMloLinkId());
            sb.append(",affLinks=").append((Object) scanResult.getAffiliatedMloLinks());
        }
        sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(((int) (j - (scanResult.timestamp / 1000))) / 1000).append("s}");
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    /* access modifiers changed from: package-private */
    public void updateAdminRestrictions() {
        boolean z;
        if (BuildCompat.isAtLeastT()) {
            UserManager userManager = this.mUserManager;
            if (userManager != null) {
                this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
            }
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager != null) {
                int minimumRequiredWifiSecurityLevel = devicePolicyManager.getMinimumRequiredWifiSecurityLevel();
                if (minimumRequiredWifiSecurityLevel != 0) {
                    Iterator<Integer> it = getSecurityTypes().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = false;
                            break;
                        }
                        int convertSecurityTypeToDpmWifiSecurity = Utils.convertSecurityTypeToDpmWifiSecurity(it.next().intValue());
                        if (convertSecurityTypeToDpmWifiSecurity != -1 && minimumRequiredWifiSecurityLevel <= convertSecurityTypeToDpmWifiSecurity) {
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        this.mIsAdminRestricted = true;
                        return;
                    }
                }
                WifiSsidPolicy wifiSsidPolicy = this.mDevicePolicyManager.getWifiSsidPolicy();
                if (wifiSsidPolicy != null) {
                    int policyType = wifiSsidPolicy.getPolicyType();
                    Set ssids = wifiSsidPolicy.getSsids();
                    if (policyType == 0 && !ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                        this.mIsAdminRestricted = true;
                        return;
                    } else if (policyType == 1 && ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                        this.mIsAdminRestricted = true;
                        return;
                    }
                }
            }
            this.mIsAdminRestricted = false;
        }
    }

    private boolean hasAdminRestrictions() {
        return (this.mHasAddConfigUserRestriction && !isSaved() && !isSuggestion()) || this.mIsAdminRestricted;
    }

    static class StandardWifiEntryKey {
        private static final String KEY_IS_NETWORK_REQUEST = "IS_NETWORK_REQUEST";
        private static final String KEY_IS_TARGETING_NEW_NETWORKS = "IS_TARGETING_NEW_NETWORKS";
        private static final String KEY_SCAN_RESULT_KEY = "SCAN_RESULT_KEY";
        private static final String KEY_SUGGESTION_PROFILE_KEY = "SUGGESTION_PROFILE_KEY";
        private boolean mIsNetworkRequest;
        private boolean mIsTargetingNewNetworks;
        private ScanResultKey mScanResultKey;
        private String mSuggestionProfileKey;

        StandardWifiEntryKey(ScanResultKey scanResultKey) {
            this(scanResultKey, false);
        }

        StandardWifiEntryKey(ScanResultKey scanResultKey, boolean z) {
            this.mScanResultKey = scanResultKey;
            this.mIsTargetingNewNetworks = z;
        }

        StandardWifiEntryKey(WifiConfiguration wifiConfiguration) {
            this(wifiConfiguration, false);
        }

        StandardWifiEntryKey(WifiConfiguration wifiConfiguration, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey(wifiConfiguration);
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR).add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
            this.mIsTargetingNewNetworks = z;
        }

        StandardWifiEntryKey(String str) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey();
            if (!str.startsWith(StandardWifiEntry.KEY_PREFIX)) {
                Log.e(StandardWifiEntry.TAG, "String key does not start with key prefix!");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str.substring(18));
                if (jSONObject.has(KEY_SCAN_RESULT_KEY)) {
                    this.mScanResultKey = new ScanResultKey(jSONObject.getString(KEY_SCAN_RESULT_KEY));
                }
                if (jSONObject.has(KEY_SUGGESTION_PROFILE_KEY)) {
                    this.mSuggestionProfileKey = jSONObject.getString(KEY_SUGGESTION_PROFILE_KEY);
                }
                if (jSONObject.has(KEY_IS_NETWORK_REQUEST)) {
                    this.mIsNetworkRequest = jSONObject.getBoolean(KEY_IS_NETWORK_REQUEST);
                }
                if (jSONObject.has(KEY_IS_TARGETING_NEW_NETWORKS)) {
                    this.mIsTargetingNewNetworks = jSONObject.getBoolean(KEY_IS_TARGETING_NEW_NETWORKS);
                }
            } catch (JSONException e) {
                Log.e(StandardWifiEntry.TAG, "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put(KEY_SCAN_RESULT_KEY, (Object) scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put(KEY_SUGGESTION_PROFILE_KEY, (Object) str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put(KEY_IS_NETWORK_REQUEST, z);
                }
                boolean z2 = this.mIsTargetingNewNetworks;
                if (z2) {
                    jSONObject.put(KEY_IS_TARGETING_NEW_NETWORKS, z2);
                }
            } catch (JSONException e) {
                Log.wtf(StandardWifiEntry.TAG, "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            return StandardWifiEntry.KEY_PREFIX + jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public ScanResultKey getScanResultKey() {
            return this.mScanResultKey;
        }

        /* access modifiers changed from: package-private */
        public String getSuggestionProfileKey() {
            return this.mSuggestionProfileKey;
        }

        /* access modifiers changed from: package-private */
        public boolean isNetworkRequest() {
            return this.mIsNetworkRequest;
        }

        /* access modifiers changed from: package-private */
        public boolean isTargetingNewNetworks() {
            return this.mIsTargetingNewNetworks;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            if (!Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) || !TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) || this.mIsNetworkRequest != standardWifiEntryKey.mIsNetworkRequest) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest));
        }
    }

    static class ScanResultKey {
        private static final String KEY_SECURITY_TYPES = "SECURITY_TYPES";
        private static final String KEY_SSID = "SSID";
        private Set<Integer> mSecurityTypes;
        private String mSsid;

        ScanResultKey() {
            this.mSecurityTypes = new ArraySet();
        }

        ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new ArraySet();
            this.mSsid = str;
            for (Integer intValue : list) {
                int intValue2 = intValue.intValue();
                if (intValue2 == 0) {
                    this.mSecurityTypes.add(6);
                } else if (intValue2 == 6) {
                    this.mSecurityTypes.add(0);
                } else if (intValue2 == 9) {
                    this.mSecurityTypes.add(3);
                } else if (intValue2 == 2) {
                    this.mSecurityTypes.add(4);
                } else if (intValue2 == 3) {
                    this.mSecurityTypes.add(9);
                } else if (intValue2 == 4) {
                    this.mSecurityTypes.add(2);
                } else if (intValue2 != 11) {
                    if (intValue2 == 12) {
                    }
                }
                this.mSecurityTypes.add(Integer.valueOf(intValue2));
            }
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        ScanResultKey(android.net.wifi.ScanResult r2) {
            /*
                r1 = this;
                boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()
                if (r0 == 0) goto L_0x000f
                android.net.wifi.WifiSsid r0 = r2.getWifiSsid()
                java.lang.String r0 = r0.toString()
                goto L_0x0011
            L_0x000f:
                java.lang.String r0 = r2.SSID
            L_0x0011:
                java.util.List r2 = com.android.wifitrackerlib.Utils.getSecurityTypesFromScanResult(r2)
                r1.<init>(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.ScanResultKey.<init>(android.net.wifi.ScanResult):void");
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        ScanResultKey(android.net.wifi.WifiConfiguration r2) {
            /*
                r1 = this;
                boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()
                if (r0 == 0) goto L_0x0009
                java.lang.String r0 = r2.SSID
                goto L_0x000f
            L_0x0009:
                java.lang.String r0 = r2.SSID
                java.lang.String r0 = android.net.wifi.WifiInfo.sanitizeSsid(r0)
            L_0x000f:
                java.util.List r2 = com.android.wifitrackerlib.Utils.getSecurityTypesFromWifiConfiguration(r2)
                r1.<init>(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.ScanResultKey.<init>(android.net.wifi.WifiConfiguration):void");
        }

        ScanResultKey(String str) {
            this.mSecurityTypes = new ArraySet();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mSsid = jSONObject.getString(KEY_SSID);
                JSONArray jSONArray = jSONObject.getJSONArray(KEY_SECURITY_TYPES);
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mSecurityTypes.add(Integer.valueOf(jSONArray.getInt(i)));
                }
            } catch (JSONException e) {
                Log.wtf(StandardWifiEntry.TAG, "JSONException while constructing ScanResultKey from string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put(KEY_SSID, (Object) str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer intValue : this.mSecurityTypes) {
                        jSONArray.put(intValue.intValue());
                    }
                    jSONObject.put(KEY_SECURITY_TYPES, (Object) jSONArray);
                }
            } catch (JSONException e) {
                Log.e(StandardWifiEntry.TAG, "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public String getSsid() {
            return this.mSsid;
        }

        /* access modifiers changed from: package-private */
        public Set<Integer> getSecurityTypes() {
            return this.mSecurityTypes;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            if (!TextUtils.equals(this.mSsid, scanResultKey.mSsid) || !this.mSecurityTypes.equals(scanResultKey.mSecurityTypes)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mSsid, this.mSecurityTypes);
        }
    }
}
