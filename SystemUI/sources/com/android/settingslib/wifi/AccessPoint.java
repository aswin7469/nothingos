package com.android.settingslib.wifi;

import android.app.AppGlobals;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.NetworkScoreManager;
import android.net.NetworkScorerAppData;
import android.net.ScoredNetwork;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import com.android.internal.util.CollectionUtils;
import com.android.settingslib.C1757R;
import com.android.settingslib.utils.ThreadUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class AccessPoint implements Comparable<AccessPoint> {
    private static final int EAP_UNKNOWN = 0;
    private static final int EAP_WPA = 1;
    private static final int EAP_WPA2_WPA3 = 2;
    public static final int HIGHER_FREQ_24GHZ = 2500;
    public static final int HIGHER_FREQ_5GHZ = 5900;
    public static final int HIGHER_FREQ_60GHZ = 70200;
    public static final int HIGHER_FREQ_6GHZ = 7115;
    static final String KEY_CONFIG = "key_config";
    static final String KEY_EAPTYPE = "eap_psktype";
    static final String KEY_FQDN = "key_fqdn";
    static final String KEY_IS_OWE_TRANSITION_MODE = "key_is_owe_transition_mode";
    static final String KEY_IS_PSK_SAE_TRANSITION_MODE = "key_is_psk_sae_transition_mode";
    static final String KEY_NETWORKINFO = "key_networkinfo";
    static final String KEY_PASSPOINT_CONFIGURATION_VERSION = "key_passpoint_configuration_version";
    static final String KEY_PASSPOINT_UNIQUE_ID = "key_passpoint_unique_id";
    public static final String KEY_PREFIX_AP = "AP:";
    public static final String KEY_PREFIX_OSU = "OSU:";
    public static final String KEY_PREFIX_PASSPOINT_UNIQUE_ID = "PASSPOINT:";
    static final String KEY_PROVIDER_FRIENDLY_NAME = "key_provider_friendly_name";
    static final String KEY_PSKTYPE = "key_psktype";
    static final String KEY_SCANRESULTS = "key_scanresults";
    static final String KEY_SCOREDNETWORKCACHE = "key_scorednetworkcache";
    static final String KEY_SECURITY = "key_security";
    static final String KEY_SPEED = "key_speed";
    static final String KEY_SSID = "key_ssid";
    static final String KEY_SUBSCRIPTION_EXPIRATION_TIME_IN_MILLIS = "key_subscription_expiration_time_in_millis";
    static final String KEY_WIFIINFO = "key_wifiinfo";
    public static final int LOWER_FREQ_24GHZ = 2400;
    public static final int LOWER_FREQ_5GHZ = 4900;
    public static final int LOWER_FREQ_60GHZ = 58320;
    public static final int LOWER_FREQ_6GHZ = 5935;
    private static final int PSK_UNKNOWN = 0;
    private static final int PSK_WPA = 1;
    private static final int PSK_WPA2 = 2;
    private static final int PSK_WPA_WPA2 = 3;
    public static final int SECURITY_DPP = 8;
    public static final int SECURITY_EAP = 3;
    public static final int SECURITY_EAP_SUITE_B = 6;
    public static final int SECURITY_EAP_WPA3_ENTERPRISE = 7;
    public static final int SECURITY_MAX_VAL = 9;
    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_OWE = 4;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_SAE = 5;
    public static final int SECURITY_WEP = 1;
    static final String TAG = "SettingsLib.AccessPoint";
    public static final int UNREACHABLE_RSSI = Integer.MIN_VALUE;
    static final AtomicInteger sLastId = new AtomicInteger(0);
    private String bssid;
    AccessPointListener mAccessPointListener;
    private WifiConfiguration mConfig;
    /* access modifiers changed from: private */
    public WifiManager.ActionListener mConnectListener;
    /* access modifiers changed from: private */
    public final Context mContext;
    private int mDeviceWifiStandard;
    private int mEapType;
    private final ArraySet<ScanResult> mExtraScanResults;
    private String mFqdn;
    private WifiInfo mInfo;
    private boolean mIsOweTransitionMode;
    private boolean mIsPskSaeTransitionMode;
    private boolean mIsRoaming;
    private boolean mIsScoredNetworkMetered;
    private String mKey;
    private final Object mLock;
    private NetworkInfo mNetworkInfo;
    /* access modifiers changed from: private */
    public String mOsuFailure;
    /* access modifiers changed from: private */
    public OsuProvider mOsuProvider;
    /* access modifiers changed from: private */
    public boolean mOsuProvisioningComplete;
    /* access modifiers changed from: private */
    public String mOsuStatus;
    private int mPasspointConfigurationVersion;
    private String mPasspointUniqueId;
    private String mProviderFriendlyName;
    private int mRssi;
    private final ArraySet<ScanResult> mScanResults;
    private final Map<String, TimestampedScoredNetwork> mScoredNetworkCache;
    private int mSpeed;
    private long mSubscriptionExpirationTimeInMillis;
    private Object mTag;
    private WifiManager mWifiManager;
    private int mWifiStandard;
    private int networkId;
    private int pskType;
    private int security;
    private String ssid;

    public interface AccessPointListener {
        void onAccessPointChanged(AccessPoint accessPoint);

        void onLevelChanged(AccessPoint accessPoint);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PasspointConfigurationVersion {
        public static final int INVALID = 0;
        public static final int NO_OSU_PROVISIONED = 1;
        public static final int OSU_PROVISIONED = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Speed {
        public static final int FAST = 20;
        public static final int MODERATE = 10;
        public static final int NONE = 0;
        public static final int SLOW = 5;
        public static final int VERY_FAST = 30;
    }

    private static int roundToClosestSpeedEnum(int i) {
        if (i < 5) {
            return 0;
        }
        if (i < 7) {
            return 5;
        }
        if (i < 15) {
            return 10;
        }
        return i < 25 ? 20 : 30;
    }

    public static String securityToString(int i, int i2) {
        return i == 1 ? "WEP" : i == 2 ? i2 == 1 ? "WPA" : i2 == 2 ? "WPA2" : i2 == 3 ? "WPA_WPA2" : "PSK" : i == 3 ? "EAP" : i == 8 ? "DPP" : i == 5 ? "SAE" : i == 6 ? "SUITE_B" : i == 4 ? "OWE" : "NONE";
    }

    public AccessPoint(Context context, Bundle bundle) {
        this.mLock = new Object();
        ArraySet<ScanResult> arraySet = new ArraySet<>();
        this.mScanResults = arraySet;
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        if (bundle.containsKey(KEY_CONFIG)) {
            this.mConfig = (WifiConfiguration) bundle.getParcelable(KEY_CONFIG);
        }
        WifiConfiguration wifiConfiguration = this.mConfig;
        if (wifiConfiguration != null) {
            loadConfig(wifiConfiguration);
        }
        if (bundle.containsKey(KEY_SSID)) {
            this.ssid = bundle.getString(KEY_SSID);
        }
        if (bundle.containsKey(KEY_SECURITY)) {
            this.security = bundle.getInt(KEY_SECURITY);
        }
        if (bundle.containsKey(KEY_SPEED)) {
            this.mSpeed = bundle.getInt(KEY_SPEED);
        }
        if (bundle.containsKey(KEY_PSKTYPE)) {
            this.pskType = bundle.getInt(KEY_PSKTYPE);
        }
        if (bundle.containsKey(KEY_EAPTYPE)) {
            this.mEapType = bundle.getInt(KEY_EAPTYPE);
        }
        this.mInfo = (WifiInfo) bundle.getParcelable(KEY_WIFIINFO);
        if (bundle.containsKey(KEY_NETWORKINFO)) {
            this.mNetworkInfo = (NetworkInfo) bundle.getParcelable(KEY_NETWORKINFO);
        }
        if (bundle.containsKey(KEY_SCANRESULTS)) {
            Parcelable[] parcelableArray = bundle.getParcelableArray(KEY_SCANRESULTS);
            arraySet.clear();
            for (Parcelable parcelable : parcelableArray) {
                this.mScanResults.add((ScanResult) parcelable);
            }
        }
        if (bundle.containsKey(KEY_SCOREDNETWORKCACHE)) {
            Iterator it = bundle.getParcelableArrayList(KEY_SCOREDNETWORKCACHE).iterator();
            while (it.hasNext()) {
                TimestampedScoredNetwork timestampedScoredNetwork = (TimestampedScoredNetwork) it.next();
                this.mScoredNetworkCache.put(timestampedScoredNetwork.getScore().networkKey.wifiKey.bssid, timestampedScoredNetwork);
            }
        }
        if (bundle.containsKey(KEY_PASSPOINT_UNIQUE_ID)) {
            this.mPasspointUniqueId = bundle.getString(KEY_PASSPOINT_UNIQUE_ID);
        }
        if (bundle.containsKey(KEY_FQDN)) {
            this.mFqdn = bundle.getString(KEY_FQDN);
        }
        if (bundle.containsKey(KEY_PROVIDER_FRIENDLY_NAME)) {
            this.mProviderFriendlyName = bundle.getString(KEY_PROVIDER_FRIENDLY_NAME);
        }
        if (bundle.containsKey(KEY_SUBSCRIPTION_EXPIRATION_TIME_IN_MILLIS)) {
            this.mSubscriptionExpirationTimeInMillis = bundle.getLong(KEY_SUBSCRIPTION_EXPIRATION_TIME_IN_MILLIS);
        }
        if (bundle.containsKey(KEY_PASSPOINT_CONFIGURATION_VERSION)) {
            this.mPasspointConfigurationVersion = bundle.getInt(KEY_PASSPOINT_CONFIGURATION_VERSION);
        }
        if (bundle.containsKey(KEY_IS_PSK_SAE_TRANSITION_MODE)) {
            this.mIsPskSaeTransitionMode = bundle.getBoolean(KEY_IS_PSK_SAE_TRANSITION_MODE);
        }
        if (bundle.containsKey(KEY_IS_OWE_TRANSITION_MODE)) {
            this.mIsOweTransitionMode = bundle.getBoolean(KEY_IS_OWE_TRANSITION_MODE);
        }
        update(this.mConfig, this.mInfo, this.mNetworkInfo);
        updateKey();
        updateBestRssiInfo();
        updateDeviceWifiGenerationInfo();
        updateWifiGeneration();
    }

    public AccessPoint(Context context, WifiConfiguration wifiConfiguration) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        loadConfig(wifiConfiguration);
        updateKey();
        updateDeviceWifiGenerationInfo();
    }

    public AccessPoint(Context context, PasspointConfiguration passpointConfiguration) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        this.mPasspointUniqueId = passpointConfiguration.getUniqueId();
        this.mFqdn = passpointConfiguration.getHomeSp().getFqdn();
        this.mProviderFriendlyName = passpointConfiguration.getHomeSp().getFriendlyName();
        this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
        if (passpointConfiguration.isOsuProvisioned()) {
            this.mPasspointConfigurationVersion = 2;
        } else {
            this.mPasspointConfigurationVersion = 1;
        }
        updateKey();
        updateDeviceWifiGenerationInfo();
    }

    public AccessPoint(Context context, WifiConfiguration wifiConfiguration, Collection<ScanResult> collection, Collection<ScanResult> collection2) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        this.networkId = wifiConfiguration.networkId;
        this.mConfig = wifiConfiguration;
        this.mPasspointUniqueId = wifiConfiguration.getKey();
        this.mFqdn = wifiConfiguration.FQDN;
        updateDeviceWifiGenerationInfo();
        setScanResultsPasspoint(collection, collection2);
        updateKey();
    }

    public AccessPoint(Context context, OsuProvider osuProvider, Collection<ScanResult> collection) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        updateDeviceWifiGenerationInfo();
        setScanResults(collection);
        updateKey();
    }

    AccessPoint(Context context, Collection<ScanResult> collection) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mWifiStandard = 1;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        updateDeviceWifiGenerationInfo();
        setScanResults(collection);
        updateKey();
    }

    /* access modifiers changed from: package-private */
    public void loadConfig(WifiConfiguration wifiConfiguration) {
        this.ssid = wifiConfiguration.SSID == null ? "" : removeDoubleQuotes(wifiConfiguration.SSID);
        this.bssid = wifiConfiguration.BSSID;
        this.security = getSecurity(wifiConfiguration);
        this.networkId = wifiConfiguration.networkId;
        this.mConfig = wifiConfiguration;
    }

    private void updateKey() {
        if (isPasspoint()) {
            this.mKey = getKey(this.mConfig);
        } else if (isPasspointConfig()) {
            this.mKey = getKey(this.mPasspointUniqueId);
        } else if (isOsuProvider()) {
            this.mKey = getKey(this.mOsuProvider);
        } else {
            this.mKey = getKey(getSsidStr(), getBssid(), getSecurity());
        }
    }

    public int compareTo(AccessPoint accessPoint) {
        if (isActive() && !accessPoint.isActive()) {
            return -1;
        }
        if (!isActive() && accessPoint.isActive()) {
            return 1;
        }
        if (isReachable() && !accessPoint.isReachable()) {
            return -1;
        }
        if (!isReachable() && accessPoint.isReachable()) {
            return 1;
        }
        if (isSaved() && !accessPoint.isSaved()) {
            return -1;
        }
        if (!isSaved() && accessPoint.isSaved()) {
            return 1;
        }
        if (getSpeed() != accessPoint.getSpeed()) {
            return accessPoint.getSpeed() - getSpeed();
        }
        WifiManager wifiManager = getWifiManager();
        int calculateSignalLevel = wifiManager.calculateSignalLevel(accessPoint.mRssi) - wifiManager.calculateSignalLevel(this.mRssi);
        if (calculateSignalLevel != 0) {
            return calculateSignalLevel;
        }
        int compareToIgnoreCase = getTitle().compareToIgnoreCase(accessPoint.getTitle());
        if (compareToIgnoreCase != 0) {
            return compareToIgnoreCase;
        }
        return getSsidStr().compareTo(accessPoint.getSsidStr());
    }

    public boolean equals(Object obj) {
        if ((obj instanceof AccessPoint) && compareTo((AccessPoint) obj) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        WifiInfo wifiInfo = this.mInfo;
        int i = 0;
        if (wifiInfo != null) {
            i = 0 + (wifiInfo.hashCode() * 13);
        }
        return i + (this.mRssi * 19) + (this.networkId * 23) + (this.ssid.hashCode() * 29);
    }

    public String toString() {
        StringBuilder append = new StringBuilder("AccessPoint(").append(this.ssid);
        if (this.bssid != null) {
            append.append(":").append(this.bssid);
        }
        if (isSaved()) {
            append.append(",saved");
        }
        if (isActive()) {
            append.append(",active");
        }
        if (isEphemeral()) {
            append.append(",ephemeral");
        }
        if (isConnectable()) {
            append.append(",connectable");
        }
        int i = this.security;
        if (!(i == 0 || i == 4)) {
            append.append(',').append(securityToString(this.security, this.pskType));
        }
        append.append(",level=").append(getLevel());
        if (this.mSpeed != 0) {
            append.append(",speed=").append(this.mSpeed);
        }
        append.append(",metered=").append(isMetered());
        if (isVerboseLoggingEnabled()) {
            append.append(",rssi=").append(this.mRssi);
            synchronized (this.mLock) {
                append.append(",scan cache size=").append(this.mScanResults.size() + this.mExtraScanResults.size());
            }
        }
        return append.append(')').toString();
    }

    /* access modifiers changed from: package-private */
    public boolean update(WifiNetworkScoreCache wifiNetworkScoreCache, boolean z, long j) {
        boolean updateScores = z ? updateScores(wifiNetworkScoreCache, j) : false;
        if (updateMetered(wifiNetworkScoreCache) || updateScores) {
            return true;
        }
        return false;
    }

    private boolean updateScores(WifiNetworkScoreCache wifiNetworkScoreCache, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            Iterator<ScanResult> it = this.mScanResults.iterator();
            while (it.hasNext()) {
                ScanResult next = it.next();
                ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(next);
                if (scoredNetwork != null) {
                    TimestampedScoredNetwork timestampedScoredNetwork = this.mScoredNetworkCache.get(next.BSSID);
                    if (timestampedScoredNetwork == null) {
                        this.mScoredNetworkCache.put(next.BSSID, new TimestampedScoredNetwork(scoredNetwork, elapsedRealtime));
                    } else {
                        timestampedScoredNetwork.update(scoredNetwork, elapsedRealtime);
                    }
                }
            }
        }
        Iterator<TimestampedScoredNetwork> it2 = this.mScoredNetworkCache.values().iterator();
        it2.forEachRemaining(new AccessPoint$$ExternalSyntheticLambda0(elapsedRealtime - j, it2));
        return updateSpeed();
    }

    static /* synthetic */ void lambda$updateScores$0(long j, Iterator it, TimestampedScoredNetwork timestampedScoredNetwork) {
        if (timestampedScoredNetwork.getUpdatedTimestampMillis() < j) {
            it.remove();
        }
    }

    private boolean updateSpeed() {
        int i = this.mSpeed;
        int generateAverageSpeedForSsid = generateAverageSpeedForSsid();
        this.mSpeed = generateAverageSpeedForSsid;
        boolean z = i != generateAverageSpeedForSsid;
        if (isVerboseLoggingEnabled() && z) {
            Log.i(TAG, String.format("%s: Set speed to %d", this.ssid, Integer.valueOf(this.mSpeed)));
        }
        return z;
    }

    private int generateAverageSpeedForSsid() {
        int i;
        if (this.mScoredNetworkCache.isEmpty()) {
            return 0;
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, String.format("Generating fallbackspeed for %s using cache: %s", getSsidStr(), this.mScoredNetworkCache));
        }
        int i2 = 0;
        int i3 = 0;
        for (TimestampedScoredNetwork score : this.mScoredNetworkCache.values()) {
            int calculateBadge = score.getScore().calculateBadge(this.mRssi);
            if (calculateBadge != 0) {
                i2++;
                i3 += calculateBadge;
            }
        }
        if (i2 == 0) {
            i = 0;
        } else {
            i = i3 / i2;
        }
        if (isVerboseLoggingEnabled()) {
            Log.i(TAG, String.format("%s generated fallback speed is: %d", getSsidStr(), Integer.valueOf(i)));
        }
        return roundToClosestSpeedEnum(i);
    }

    private boolean updateMetered(WifiNetworkScoreCache wifiNetworkScoreCache) {
        WifiInfo wifiInfo;
        boolean z = this.mIsScoredNetworkMetered;
        this.mIsScoredNetworkMetered = false;
        if (!isActive() || (wifiInfo = this.mInfo) == null) {
            synchronized (this.mLock) {
                Iterator<ScanResult> it = this.mScanResults.iterator();
                while (it.hasNext()) {
                    ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(it.next());
                    if (scoredNetwork != null) {
                        this.mIsScoredNetworkMetered = scoredNetwork.meteredHint | this.mIsScoredNetworkMetered;
                    }
                }
            }
        } else {
            ScoredNetwork scoredNetwork2 = wifiNetworkScoreCache.getScoredNetwork(NetworkKey.createFromWifiInfo(wifiInfo));
            if (scoredNetwork2 != null) {
                this.mIsScoredNetworkMetered = scoredNetwork2.meteredHint | this.mIsScoredNetworkMetered;
            }
        }
        if (z != this.mIsScoredNetworkMetered) {
            return true;
        }
        return false;
    }

    public static String getKey(Context context, ScanResult scanResult) {
        return getKey(scanResult.SSID, scanResult.BSSID, getSecurity(context, scanResult));
    }

    public static String getKey(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            return getKey(wifiConfiguration.getKey());
        }
        return getKey(removeDoubleQuotes(wifiConfiguration.SSID), wifiConfiguration.BSSID, getSecurity(wifiConfiguration));
    }

    public static String getKey(String str) {
        return KEY_PREFIX_PASSPOINT_UNIQUE_ID + str;
    }

    public static String getKey(OsuProvider osuProvider) {
        return KEY_PREFIX_OSU + osuProvider.getFriendlyName() + ',' + osuProvider.getServerUri();
    }

    private static String getKey(String str, String str2, int i) {
        StringBuilder sb = new StringBuilder(KEY_PREFIX_AP);
        if (TextUtils.isEmpty(str)) {
            sb.append(str2);
        } else {
            sb.append(str);
        }
        sb.append(',').append(i);
        return sb.toString();
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean matches(AccessPoint accessPoint) {
        if (isPasspoint() || isPasspointConfig() || isOsuProvider()) {
            return getKey().equals(accessPoint.getKey());
        }
        if (!isSameSsidOrBssid(accessPoint)) {
            return false;
        }
        int security2 = accessPoint.getSecurity();
        if (!this.mIsPskSaeTransitionMode) {
            int i = this.security;
            if ((i == 5 || i == 2) && accessPoint.isPskSaeTransitionMode()) {
                return true;
            }
        } else if ((security2 == 5 && getWifiManager().isWpa3SaeSupported()) || security2 == 2) {
            return true;
        }
        if (!this.mIsOweTransitionMode) {
            int i2 = this.security;
            if ((i2 == 4 || i2 == 0) && accessPoint.isOweTransitionMode()) {
                return true;
            }
        } else if ((security2 == 4 && getWifiManager().isEnhancedOpenSupported()) || security2 == 0) {
            return true;
        }
        if (this.security == accessPoint.getSecurity()) {
            return true;
        }
        return false;
    }

    public boolean matches(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            if (!isPasspoint() || !wifiConfiguration.getKey().equals(this.mConfig.getKey())) {
                return false;
            }
            return true;
        } else if (!this.ssid.equals(removeDoubleQuotes(wifiConfiguration.SSID))) {
            return false;
        } else {
            WifiConfiguration wifiConfiguration2 = this.mConfig;
            if (wifiConfiguration2 != null && wifiConfiguration2.shared != wifiConfiguration.shared) {
                return false;
            }
            int security2 = getSecurity(wifiConfiguration);
            if (this.mIsPskSaeTransitionMode && ((security2 == 5 && getWifiManager().isWpa3SaeSupported()) || security2 == 2)) {
                return true;
            }
            if (this.mIsOweTransitionMode && ((security2 == 4 && getWifiManager().isEnhancedOpenSupported()) || security2 == 0)) {
                return true;
            }
            if (this.security == getSecurity(wifiConfiguration)) {
                return true;
            }
            return false;
        }
    }

    private boolean matches(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo) {
        if (wifiConfiguration == null || wifiInfo == null) {
            return false;
        }
        if (wifiConfiguration.isPasspoint() || isSameSsidOrBssid(wifiInfo)) {
            return matches(wifiConfiguration);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean matches(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        if (isPasspoint() || isOsuProvider()) {
            throw new IllegalStateException("Should not matches a Passpoint by ScanResult");
        } else if (!isSameSsidOrBssid(scanResult)) {
            return false;
        } else {
            if (!this.mIsPskSaeTransitionMode) {
                int i = this.security;
                if ((i == 5 || i == 2) && isPskSaeTransitionMode(scanResult)) {
                    return true;
                }
            } else if ((scanResult.capabilities.contains("SAE") && getWifiManager().isWpa3SaeSupported()) || scanResult.capabilities.contains("PSK")) {
                return true;
            }
            if (this.mIsOweTransitionMode) {
                int security2 = getSecurity(this.mContext, scanResult);
                if ((security2 == 4 && getWifiManager().isEnhancedOpenSupported()) || security2 == 0) {
                    return true;
                }
            } else {
                int i2 = this.security;
                if ((i2 == 4 || i2 == 0) && isOweTransitionMode(scanResult)) {
                    return true;
                }
            }
            if (this.security == getSecurity(this.mContext, scanResult)) {
                return true;
            }
            return false;
        }
    }

    public WifiConfiguration getConfig() {
        return this.mConfig;
    }

    public String getPasspointFqdn() {
        return this.mFqdn;
    }

    public void clearConfig() {
        this.mConfig = null;
        this.networkId = -1;
    }

    public boolean isSuiteBSupported() {
        if (!((WifiManager) this.mContext.getSystemService(WifiManager.class)).getCapabilities(WifiConfiguration.KeyMgmt.varName).contains("WPA-EAP-SUITE-B-192")) {
            return false;
        }
        Iterator<ScanResult> it = this.mScanResults.iterator();
        while (it.hasNext()) {
            if (it.next().capabilities.contains("EAP_SUITE_B_192")) {
                return true;
            }
        }
        return false;
    }

    public WifiInfo getInfo() {
        return this.mInfo;
    }

    public int getLevel() {
        return getWifiManager().calculateSignalLevel(this.mRssi);
    }

    public int getRssi() {
        return this.mRssi;
    }

    public Set<ScanResult> getScanResults() {
        ArraySet arraySet = new ArraySet();
        synchronized (this.mLock) {
            arraySet.addAll(this.mScanResults);
            arraySet.addAll(this.mExtraScanResults);
        }
        return arraySet;
    }

    public Map<String, TimestampedScoredNetwork> getScoredNetworkCache() {
        return this.mScoredNetworkCache;
    }

    private void updateBestRssiInfo() {
        ScanResult scanResult;
        int i;
        int i2;
        if (!isActive()) {
            synchronized (this.mLock) {
                Iterator<ScanResult> it = this.mScanResults.iterator();
                scanResult = null;
                i = Integer.MIN_VALUE;
                while (it.hasNext()) {
                    ScanResult next = it.next();
                    if (next.level > i) {
                        i = next.level;
                        scanResult = next;
                    }
                }
            }
            if (i == Integer.MIN_VALUE || (i2 = this.mRssi) == Integer.MIN_VALUE) {
                this.mRssi = i;
            } else {
                this.mRssi = (i2 + i) / 2;
            }
            if (scanResult != null) {
                this.ssid = scanResult.SSID;
                this.bssid = scanResult.BSSID;
                int security2 = getSecurity(this.mContext, scanResult);
                this.security = security2;
                if (security2 == 2 || security2 == 5) {
                    this.pskType = getPskType(scanResult);
                }
                if (this.security == 3) {
                    this.mEapType = getEapType(scanResult);
                }
                this.mIsPskSaeTransitionMode = isPskSaeTransitionMode(scanResult);
                this.mIsOweTransitionMode = isOweTransitionMode(scanResult);
            }
            if (isPasspoint()) {
                this.mConfig.SSID = convertToQuotedString(this.ssid);
            }
        }
    }

    public boolean isMetered() {
        return this.mIsScoredNetworkMetered || WifiConfiguration.isMetered(this.mConfig, this.mInfo);
    }

    public NetworkInfo getNetworkInfo() {
        return this.mNetworkInfo;
    }

    public int getSecurity() {
        return this.security;
    }

    public String getSecurityString(boolean z) {
        Context context = this.mContext;
        if (isPasspoint() || isPasspointConfig()) {
            if (z) {
                return context.getString(C1757R.string.wifi_security_short_eap);
            }
            return context.getString(C1757R.string.wifi_security_eap);
        } else if (this.mIsPskSaeTransitionMode) {
            if (z) {
                return context.getString(C1757R.string.wifi_security_short_psk_sae);
            }
            return context.getString(C1757R.string.wifi_security_psk_sae);
        } else if (!this.mIsOweTransitionMode) {
            switch (this.security) {
                case 1:
                    if (z) {
                        return context.getString(C1757R.string.wifi_security_short_wep);
                    }
                    return context.getString(C1757R.string.wifi_security_wep);
                case 2:
                    int i = this.pskType;
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (z) {
                                    return context.getString(C1757R.string.wifi_security_short_psk_generic);
                                }
                                return context.getString(C1757R.string.wifi_security_psk_generic);
                            } else if (z) {
                                return context.getString(C1757R.string.wifi_security_short_wpa_wpa2);
                            } else {
                                return context.getString(C1757R.string.wifi_security_wpa_wpa2);
                            }
                        } else if (z) {
                            return context.getString(C1757R.string.wifi_security_short_wpa2);
                        } else {
                            return context.getString(C1757R.string.wifi_security_wpa2);
                        }
                    } else if (z) {
                        return context.getString(C1757R.string.wifi_security_short_wpa);
                    } else {
                        return context.getString(C1757R.string.wifi_security_wpa);
                    }
                case 3:
                    int i2 = this.mEapType;
                    if (i2 != 1) {
                        if (i2 != 2) {
                            if (z) {
                                return context.getString(C1757R.string.wifi_security_short_eap);
                            }
                            return context.getString(C1757R.string.wifi_security_eap);
                        } else if (z) {
                            return context.getString(C1757R.string.wifi_security_short_eap_wpa2_wpa3);
                        } else {
                            return context.getString(C1757R.string.wifi_security_eap_wpa2_wpa3);
                        }
                    } else if (z) {
                        return context.getString(C1757R.string.wifi_security_short_eap_wpa);
                    } else {
                        return context.getString(C1757R.string.wifi_security_eap_wpa);
                    }
                case 4:
                    if (z) {
                        return context.getString(C1757R.string.wifi_security_short_owe);
                    }
                    return context.getString(C1757R.string.wifi_security_owe);
                case 5:
                    if (z) {
                        return context.getString(C1757R.string.wifi_security_short_sae);
                    }
                    return context.getString(C1757R.string.wifi_security_sae);
                case 6:
                    if (z) {
                        return context.getString(C1757R.string.wifi_security_short_eap_suiteb);
                    }
                    return context.getString(C1757R.string.wifi_security_eap_suiteb);
                case 8:
                    if (z) {
                        return context.getString(C1757R.string.wifi_security_short_dpp);
                    }
                    return context.getString(C1757R.string.wifi_security_dpp);
                default:
                    if (z) {
                        return "";
                    }
                    return context.getString(C1757R.string.wifi_security_none);
            }
        } else if (z) {
            return context.getString(C1757R.string.wifi_security_short_none_owe);
        } else {
            return context.getString(C1757R.string.wifi_security_none_owe);
        }
    }

    public String getSsidStr() {
        return this.ssid;
    }

    public String getBssid() {
        return this.bssid;
    }

    public CharSequence getSsid() {
        return this.ssid;
    }

    @Deprecated
    public String getConfigName() {
        WifiConfiguration wifiConfiguration = this.mConfig;
        if (wifiConfiguration != null && wifiConfiguration.isPasspoint()) {
            return this.mConfig.providerFriendlyName;
        }
        if (this.mPasspointUniqueId != null) {
            return this.mProviderFriendlyName;
        }
        return this.ssid;
    }

    public NetworkInfo.DetailedState getDetailedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo != null) {
            return networkInfo.getDetailedState();
        }
        Log.w(TAG, "NetworkInfo is null, cannot return detailed state");
        return null;
    }

    public String getSavedNetworkSummary() {
        ApplicationInfo applicationInfo;
        WifiConfiguration wifiConfiguration = this.mConfig;
        if (wifiConfiguration != null) {
            PackageManager packageManager = this.mContext.getPackageManager();
            String nameForUid = packageManager.getNameForUid(1000);
            int userId = UserHandle.getUserId(wifiConfiguration.creatorUid);
            if (wifiConfiguration.creatorName == null || !wifiConfiguration.creatorName.equals(nameForUid)) {
                try {
                    applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(wifiConfiguration.creatorName, 0, userId);
                } catch (RemoteException unused) {
                    applicationInfo = null;
                }
            } else {
                applicationInfo = this.mContext.getApplicationInfo();
            }
            if (applicationInfo != null && !applicationInfo.packageName.equals(this.mContext.getString(C1757R.string.settings_package)) && !applicationInfo.packageName.equals(this.mContext.getString(C1757R.string.certinstaller_package))) {
                return this.mContext.getString(C1757R.string.saved_network, new Object[]{applicationInfo.loadLabel(packageManager)});
            }
        }
        return (!isPasspointConfigurationR1() || !isExpired()) ? "" : this.mContext.getString(C1757R.string.wifi_passpoint_expired);
    }

    public String getTitle() {
        if (isPasspoint() && !TextUtils.isEmpty(this.mConfig.providerFriendlyName)) {
            return this.mConfig.providerFriendlyName;
        }
        if (isPasspointConfig() && !TextUtils.isEmpty(this.mProviderFriendlyName)) {
            return this.mProviderFriendlyName;
        }
        if (!isOsuProvider() || TextUtils.isEmpty(this.mOsuProvider.getFriendlyName())) {
            return !TextUtils.isEmpty(getSsidStr()) ? getSsidStr() : "";
        }
        return this.mOsuProvider.getFriendlyName();
    }

    public String getSummary() {
        return getSettingsSummary();
    }

    public String getSettingsSummary() {
        return getSettingsSummary(false);
    }

    public String getSettingsSummary(boolean z) {
        int i;
        if (isPasspointConfigurationR1() && isExpired()) {
            return this.mContext.getString(C1757R.string.wifi_passpoint_expired);
        }
        StringBuilder sb = new StringBuilder();
        if (isOsuProvider()) {
            if (this.mOsuProvisioningComplete) {
                sb.append(this.mContext.getString(C1757R.string.osu_sign_up_complete));
            } else {
                String str = this.mOsuFailure;
                if (str != null) {
                    sb.append(str);
                } else {
                    String str2 = this.mOsuStatus;
                    if (str2 != null) {
                        sb.append(str2);
                    } else {
                        sb.append(this.mContext.getString(C1757R.string.tap_to_sign_up));
                    }
                }
            }
        } else if (isActive()) {
            Context context = this.mContext;
            NetworkInfo.DetailedState detailedState = getDetailedState();
            WifiInfo wifiInfo = this.mInfo;
            boolean z2 = wifiInfo != null && wifiInfo.isEphemeral();
            WifiInfo wifiInfo2 = this.mInfo;
            sb.append(getSummary(context, (String) null, detailedState, z2, wifiInfo2 != null ? wifiInfo2.getRequestingPackageName() : null));
        } else {
            WifiConfiguration wifiConfiguration = this.mConfig;
            if (wifiConfiguration == null || !wifiConfiguration.hasNoInternetAccess()) {
                WifiConfiguration wifiConfiguration2 = this.mConfig;
                if (wifiConfiguration2 != null && wifiConfiguration2.getNetworkSelectionStatus().getNetworkSelectionStatus() != 0) {
                    int networkSelectionDisableReason = this.mConfig.getNetworkSelectionStatus().getNetworkSelectionDisableReason();
                    if (networkSelectionDisableReason == 1) {
                        sb.append(this.mContext.getString(C1757R.string.wifi_disabled_generic));
                    } else if (networkSelectionDisableReason == 2) {
                        sb.append(this.mContext.getString(C1757R.string.wifi_disabled_password_failure));
                    } else if (networkSelectionDisableReason == 3) {
                        sb.append(this.mContext.getString(C1757R.string.wifi_disabled_network_failure));
                    } else if (networkSelectionDisableReason == 8) {
                        sb.append(this.mContext.getString(C1757R.string.wifi_check_password_try_again));
                    }
                } else if (!isReachable()) {
                    sb.append(this.mContext.getString(C1757R.string.wifi_not_in_range));
                } else {
                    WifiConfiguration wifiConfiguration3 = this.mConfig;
                    if (wifiConfiguration3 != null) {
                        if (wifiConfiguration3.getRecentFailureReason() == 17) {
                            sb.append(this.mContext.getString(C1757R.string.wifi_ap_unable_to_handle_new_sta));
                        } else if (z) {
                            sb.append(this.mContext.getString(C1757R.string.wifi_disconnected));
                        } else {
                            sb.append(this.mContext.getString(C1757R.string.wifi_remembered));
                        }
                    }
                }
            } else {
                if (this.mConfig.getNetworkSelectionStatus().getNetworkSelectionStatus() == 2) {
                    i = C1757R.string.wifi_no_internet_no_reconnect;
                } else {
                    i = C1757R.string.wifi_no_internet;
                }
                sb.append(this.mContext.getString(i));
            }
        }
        if (isVerboseLoggingEnabled()) {
            sb.append(WifiUtils.buildLoggingSummary(this, this.mConfig));
        }
        WifiConfiguration wifiConfiguration4 = this.mConfig;
        if (wifiConfiguration4 != null && (WifiUtils.isMeteredOverridden(wifiConfiguration4) || this.mConfig.meteredHint)) {
            return this.mContext.getResources().getString(C1757R.string.preference_summary_default_combination, new Object[]{WifiUtils.getMeteredLabel(this.mContext, this.mConfig), sb.toString()});
        } else if (getSpeedLabel() != null && sb.length() != 0) {
            return this.mContext.getResources().getString(C1757R.string.preference_summary_default_combination, new Object[]{getSpeedLabel(), sb.toString()});
        } else if (getSpeedLabel() != null) {
            return getSpeedLabel();
        } else {
            return sb.toString();
        }
    }

    public boolean isActive() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        return (networkInfo == null || (this.networkId == -1 && networkInfo.getState() == NetworkInfo.State.DISCONNECTED)) ? false : true;
    }

    public boolean isConnectable() {
        return getLevel() != -1 && getDetailedState() == null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r1 = r1.mNetworkInfo;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEphemeral() {
        /*
            r1 = this;
            android.net.wifi.WifiInfo r0 = r1.mInfo
            if (r0 == 0) goto L_0x0018
            boolean r0 = r0.isEphemeral()
            if (r0 == 0) goto L_0x0018
            android.net.NetworkInfo r1 = r1.mNetworkInfo
            if (r1 == 0) goto L_0x0018
            android.net.NetworkInfo$State r1 = r1.getState()
            android.net.NetworkInfo$State r0 = android.net.NetworkInfo.State.DISCONNECTED
            if (r1 == r0) goto L_0x0018
            r1 = 1
            goto L_0x0019
        L_0x0018:
            r1 = 0
        L_0x0019:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.wifi.AccessPoint.isEphemeral():boolean");
    }

    public boolean isPasspoint() {
        WifiConfiguration wifiConfiguration = this.mConfig;
        return wifiConfiguration != null && wifiConfiguration.isPasspoint();
    }

    public boolean isPasspointConfig() {
        return this.mPasspointUniqueId != null && this.mConfig == null;
    }

    public boolean isOsuProvider() {
        return this.mOsuProvider != null;
    }

    public boolean isExpired() {
        if (this.mSubscriptionExpirationTimeInMillis > 0 && System.currentTimeMillis() >= this.mSubscriptionExpirationTimeInMillis) {
            return true;
        }
        return false;
    }

    public boolean isPasspointConfigurationR1() {
        return this.mPasspointConfigurationVersion == 1;
    }

    public boolean isPasspointConfigurationOsuProvisioned() {
        return this.mPasspointConfigurationVersion == 2;
    }

    public void startOsuProvisioning(WifiManager.ActionListener actionListener) {
        this.mConnectListener = actionListener;
        getWifiManager().startSubscriptionProvisioning(this.mOsuProvider, this.mContext.getMainExecutor(), new AccessPointProvisioningCallback());
    }

    private boolean isInfoForThisAccessPoint(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo) {
        if (wifiInfo.isOsuAp() || this.mOsuStatus != null) {
            if (!wifiInfo.isOsuAp() || this.mOsuStatus == null) {
                return false;
            }
            return true;
        } else if (!wifiInfo.isPasspointAp() && !isPasspoint()) {
            int i = this.networkId;
            if (i != -1) {
                if (i == wifiInfo.getNetworkId()) {
                    return true;
                }
                return false;
            } else if (wifiConfiguration != null) {
                return matches(wifiConfiguration, wifiInfo);
            } else {
                return TextUtils.equals(removeDoubleQuotes(wifiInfo.getSSID()), this.ssid);
            }
        } else if (!wifiInfo.isPasspointAp() || !isPasspoint() || !TextUtils.equals(wifiInfo.getPasspointFqdn(), this.mConfig.FQDN) || !TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mConfig.providerFriendlyName)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isSaved() {
        return this.mConfig != null;
    }

    public Object getTag() {
        return this.mTag;
    }

    public void setTag(Object obj) {
        this.mTag = obj;
    }

    public void generateOpenNetworkConfig() {
        if (!isOpenNetwork()) {
            throw new IllegalStateException();
        } else if (this.mConfig == null) {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            this.mConfig = wifiConfiguration;
            wifiConfiguration.SSID = convertToQuotedString(this.ssid);
            if (this.security == 0) {
                this.mConfig.allowedKeyManagement.set(0);
                return;
            }
            this.mConfig.allowedKeyManagement.set(9);
            this.mConfig.requirePmf = true;
        }
    }

    public void saveWifiState(Bundle bundle) {
        if (this.ssid != null) {
            bundle.putString(KEY_SSID, getSsidStr());
        }
        bundle.putInt(KEY_SECURITY, this.security);
        bundle.putInt(KEY_SPEED, this.mSpeed);
        bundle.putInt(KEY_PSKTYPE, this.pskType);
        bundle.putInt(KEY_EAPTYPE, this.mEapType);
        WifiConfiguration wifiConfiguration = this.mConfig;
        if (wifiConfiguration != null) {
            bundle.putParcelable(KEY_CONFIG, wifiConfiguration);
        }
        bundle.putParcelable(KEY_WIFIINFO, this.mInfo);
        synchronized (this.mLock) {
            ArraySet<ScanResult> arraySet = this.mScanResults;
            bundle.putParcelableArray(KEY_SCANRESULTS, (Parcelable[]) arraySet.toArray(new Parcelable[(arraySet.size() + this.mExtraScanResults.size())]));
        }
        bundle.putParcelableArrayList(KEY_SCOREDNETWORKCACHE, new ArrayList(this.mScoredNetworkCache.values()));
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo != null) {
            bundle.putParcelable(KEY_NETWORKINFO, networkInfo);
        }
        String str = this.mPasspointUniqueId;
        if (str != null) {
            bundle.putString(KEY_PASSPOINT_UNIQUE_ID, str);
        }
        String str2 = this.mFqdn;
        if (str2 != null) {
            bundle.putString(KEY_FQDN, str2);
        }
        String str3 = this.mProviderFriendlyName;
        if (str3 != null) {
            bundle.putString(KEY_PROVIDER_FRIENDLY_NAME, str3);
        }
        bundle.putLong(KEY_SUBSCRIPTION_EXPIRATION_TIME_IN_MILLIS, this.mSubscriptionExpirationTimeInMillis);
        bundle.putInt(KEY_PASSPOINT_CONFIGURATION_VERSION, this.mPasspointConfigurationVersion);
        bundle.putBoolean(KEY_IS_PSK_SAE_TRANSITION_MODE, this.mIsPskSaeTransitionMode);
        bundle.putBoolean(KEY_IS_OWE_TRANSITION_MODE, this.mIsOweTransitionMode);
    }

    public void setListener(AccessPointListener accessPointListener) {
        this.mAccessPointListener = accessPointListener;
    }

    /* access modifiers changed from: package-private */
    public void setScanResults(Collection<ScanResult> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            Log.d(TAG, "Cannot set scan results to empty list");
            return;
        }
        if (this.mKey != null && !isPasspoint() && !isOsuProvider()) {
            for (ScanResult next : collection) {
                if (!matches(next)) {
                    Log.d(TAG, String.format("ScanResult %s\nkey of %s did not match current AP key %s", next, getKey(this.mContext, next), this.mKey));
                    return;
                }
            }
        }
        int level = getLevel();
        synchronized (this.mLock) {
            this.mScanResults.clear();
            this.mScanResults.addAll(collection);
        }
        updateBestRssiInfo();
        updateWifiGeneration();
        int level2 = getLevel();
        if (level2 > 0 && level2 != level) {
            updateSpeed();
            ThreadUtils.postOnMainThread(new AccessPoint$$ExternalSyntheticLambda2(this));
        }
        ThreadUtils.postOnMainThread(new AccessPoint$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setScanResults$1$com-android-settingslib-wifi-AccessPoint  reason: not valid java name */
    public /* synthetic */ void m2512lambda$setScanResults$1$comandroidsettingslibwifiAccessPoint() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onLevelChanged(this);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setScanResults$2$com-android-settingslib-wifi-AccessPoint  reason: not valid java name */
    public /* synthetic */ void m2513lambda$setScanResults$2$comandroidsettingslibwifiAccessPoint() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void setScanResultsPasspoint(Collection<ScanResult> collection, Collection<ScanResult> collection2) {
        synchronized (this.mLock) {
            this.mExtraScanResults.clear();
            if (!CollectionUtils.isEmpty(collection)) {
                this.mIsRoaming = false;
                if (!CollectionUtils.isEmpty(collection2)) {
                    this.mExtraScanResults.addAll(collection2);
                }
                setScanResults(collection);
            } else if (!CollectionUtils.isEmpty(collection2)) {
                this.mIsRoaming = true;
                setScanResults(collection2);
            }
        }
    }

    public boolean update(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo, NetworkInfo networkInfo) {
        int level = getLevel();
        boolean z = true;
        boolean z2 = false;
        if (wifiInfo != null && isInfoForThisAccessPoint(wifiConfiguration, wifiInfo)) {
            if (this.mInfo == null) {
                z2 = true;
            }
            if (!isPasspoint() && this.mConfig != wifiConfiguration) {
                update(wifiConfiguration);
            }
            if (getWifiStandard() != wifiInfo.getWifiStandard()) {
                z2 = true;
            }
            if (this.mRssi == wifiInfo.getRssi() || wifiInfo.getRssi() == -127) {
                NetworkInfo networkInfo2 = this.mNetworkInfo;
                if (networkInfo2 == null || networkInfo == null || networkInfo2.getDetailedState() == networkInfo.getDetailedState()) {
                    z = z2;
                }
            } else {
                this.mRssi = wifiInfo.getRssi();
            }
            this.mInfo = wifiInfo;
            this.mNetworkInfo = networkInfo;
        } else if (this.mInfo != null) {
            this.mInfo = null;
            this.mNetworkInfo = null;
            updateWifiGeneration();
        } else {
            z = false;
        }
        if (z && this.mAccessPointListener != null) {
            ThreadUtils.postOnMainThread(new AccessPoint$$ExternalSyntheticLambda4(this));
            if (level != getLevel()) {
                ThreadUtils.postOnMainThread(new AccessPoint$$ExternalSyntheticLambda5(this));
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$update$3$com-android-settingslib-wifi-AccessPoint  reason: not valid java name */
    public /* synthetic */ void m2514lambda$update$3$comandroidsettingslibwifiAccessPoint() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$update$4$com-android-settingslib-wifi-AccessPoint  reason: not valid java name */
    public /* synthetic */ void m2515lambda$update$4$comandroidsettingslibwifiAccessPoint() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onLevelChanged(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void update(WifiConfiguration wifiConfiguration) {
        this.mConfig = wifiConfiguration;
        if (wifiConfiguration != null && !isPasspoint()) {
            this.ssid = removeDoubleQuotes(this.mConfig.SSID);
        }
        this.networkId = wifiConfiguration != null ? wifiConfiguration.networkId : -1;
        ThreadUtils.postOnMainThread(new AccessPoint$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$update$5$com-android-settingslib-wifi-AccessPoint  reason: not valid java name */
    public /* synthetic */ void m2516lambda$update$5$comandroidsettingslibwifiAccessPoint() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void setRssi(int i) {
        this.mRssi = i;
    }

    /* access modifiers changed from: package-private */
    public void setUnreachable() {
        setRssi(Integer.MIN_VALUE);
    }

    /* access modifiers changed from: package-private */
    public int getSpeed() {
        return this.mSpeed;
    }

    /* access modifiers changed from: package-private */
    public String getSpeedLabel() {
        return getSpeedLabel(this.mSpeed);
    }

    /* access modifiers changed from: package-private */
    public String getSpeedLabel(int i) {
        return getSpeedLabel(this.mContext, i);
    }

    private static String getSpeedLabel(Context context, int i) {
        if (i == 5) {
            return context.getString(C1757R.string.speed_label_slow);
        }
        if (i == 10) {
            return context.getString(C1757R.string.speed_label_okay);
        }
        if (i == 20) {
            return context.getString(C1757R.string.speed_label_fast);
        }
        if (i != 30) {
            return null;
        }
        return context.getString(C1757R.string.speed_label_very_fast);
    }

    public static String getSpeedLabel(Context context, ScoredNetwork scoredNetwork, int i) {
        return getSpeedLabel(context, roundToClosestSpeedEnum(scoredNetwork.calculateBadge(i)));
    }

    public boolean isReachable() {
        return this.mRssi != Integer.MIN_VALUE;
    }

    private static CharSequence getAppLabel(String str, PackageManager packageManager) {
        try {
            ApplicationInfo applicationInfoAsUser = packageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(-2));
            if (applicationInfoAsUser != null) {
                return applicationInfoAsUser.loadLabel(packageManager);
            }
            return "";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get app info", e);
            return "";
        }
    }

    public static String getSummary(Context context, String str, NetworkInfo.DetailedState detailedState, boolean z, String str2) {
        NetworkCapabilities networkCapabilities;
        if (detailedState == NetworkInfo.DetailedState.CONNECTED) {
            if (z && !TextUtils.isEmpty(str2)) {
                return context.getString(C1757R.string.connected_via_app, new Object[]{getAppLabel(str2, context.getPackageManager())});
            } else if (z) {
                NetworkScorerAppData activeScorer = ((NetworkScoreManager) context.getSystemService(NetworkScoreManager.class)).getActiveScorer();
                if (activeScorer == null || activeScorer.getRecommendationServiceLabel() == null) {
                    return context.getString(C1757R.string.connected_via_network_scorer_default);
                }
                return String.format(context.getString(C1757R.string.connected_via_network_scorer), activeScorer.getRecommendationServiceLabel());
            }
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (detailedState == NetworkInfo.DetailedState.CONNECTED && (networkCapabilities = connectivityManager.getNetworkCapabilities(((WifiManager) context.getSystemService(WifiManager.class)).getCurrentNetwork())) != null) {
            if (networkCapabilities.hasCapability(17)) {
                return context.getString(context.getResources().getIdentifier("network_available_sign_in", "string", "android"));
            }
            if (networkCapabilities.hasCapability(24)) {
                return context.getString(C1757R.string.wifi_limited_connection);
            }
            if (!networkCapabilities.hasCapability(16)) {
                Settings.Global.getString(context.getContentResolver(), "private_dns_mode");
                if (networkCapabilities.isPrivateDnsBroken()) {
                    return context.getString(C1757R.string.private_dns_broken);
                }
                return context.getString(C1757R.string.wifi_connected_no_internet);
            }
        }
        if (detailedState == null) {
            Log.w(TAG, "state is null, returning empty summary");
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(str == null ? C1757R.array.wifi_status : C1757R.array.wifi_status_with_ssid);
        int ordinal = detailedState.ordinal();
        if (ordinal >= stringArray.length || stringArray[ordinal].length() == 0) {
            return "";
        }
        return String.format(stringArray[ordinal], str);
    }

    public static String convertToQuotedString(String str) {
        return "\"" + str + "\"";
    }

    private static int getPskType(ScanResult scanResult) {
        boolean contains = scanResult.capabilities.contains("WPA-PSK");
        boolean contains2 = scanResult.capabilities.contains("RSN-PSK");
        boolean contains3 = scanResult.capabilities.contains("RSN-SAE");
        if (contains2 && contains) {
            return 3;
        }
        if (contains2) {
            return 2;
        }
        if (contains) {
            return 1;
        }
        if (contains3) {
            return 0;
        }
        Log.w(TAG, "Received abnormal flag string: " + scanResult.capabilities);
        return 0;
    }

    private static int getEapType(ScanResult scanResult) {
        if (scanResult.capabilities.contains("RSN-EAP")) {
            return 2;
        }
        return scanResult.capabilities.contains("WPA-EAP") ? 1 : 0;
    }

    private static int getSecurity(Context context, ScanResult scanResult) {
        boolean contains = scanResult.capabilities.contains("WEP");
        boolean contains2 = scanResult.capabilities.contains("SAE");
        boolean contains3 = scanResult.capabilities.contains("PSK");
        boolean contains4 = scanResult.capabilities.contains("EAP_SUITE_B_192");
        boolean contains5 = scanResult.capabilities.contains("EAP");
        boolean contains6 = scanResult.capabilities.contains("OWE");
        boolean contains7 = scanResult.capabilities.contains("OWE_TRANSITION");
        boolean contains8 = scanResult.capabilities.contains("DPP");
        if (!contains2 || !contains3) {
            if (contains7) {
                if (((WifiManager) context.getSystemService("wifi")).isEnhancedOpenSupported()) {
                    return 4;
                }
                return 0;
            } else if (contains8) {
                return 8;
            } else {
                if (contains) {
                    return 1;
                }
                if (contains2) {
                    return 5;
                }
                if (contains3) {
                    return 2;
                }
                if (contains4) {
                    return 6;
                }
                if (contains5) {
                    return 3;
                }
                return contains6 ? 4 : 0;
            }
        } else if (((WifiManager) context.getSystemService("wifi")).isWpa3SaeSupported()) {
            return 5;
        } else {
            return 2;
        }
    }

    static int getSecurity(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return 5;
        }
        if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return 2;
        }
        if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return 6;
        }
        if (wifiConfiguration.allowedKeyManagement.get(2) || wifiConfiguration.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (wifiConfiguration.allowedKeyManagement.get(17)) {
            return 8;
        }
        if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return 4;
        }
        if (wifiConfiguration.wepTxKeyIndex < 0 || wifiConfiguration.wepTxKeyIndex >= wifiConfiguration.wepKeys.length || wifiConfiguration.wepKeys[wifiConfiguration.wepTxKeyIndex] == null) {
            return 0;
        }
        return 1;
    }

    static String removeDoubleQuotes(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    /* access modifiers changed from: private */
    public WifiManager getWifiManager() {
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        }
        return this.mWifiManager;
    }

    public boolean isOpenNetwork() {
        int i = this.security;
        return i == 0 || i == 4;
    }

    private static boolean isVerboseLoggingEnabled() {
        return WifiTracker.sVerboseLogging || Log.isLoggable(TAG, 2);
    }

    class AccessPointProvisioningCallback extends ProvisioningCallback {
        AccessPointProvisioningCallback() {
        }

        public void onProvisioningFailure(int i) {
            if (TextUtils.equals(AccessPoint.this.mOsuStatus, AccessPoint.this.mContext.getString(C1757R.string.osu_completing_sign_up))) {
                AccessPoint accessPoint = AccessPoint.this;
                String unused = accessPoint.mOsuFailure = accessPoint.mContext.getString(C1757R.string.osu_sign_up_failed);
            } else {
                AccessPoint accessPoint2 = AccessPoint.this;
                String unused2 = accessPoint2.mOsuFailure = accessPoint2.mContext.getString(C1757R.string.osu_connect_failed);
            }
            String unused3 = AccessPoint.this.mOsuStatus = null;
            boolean unused4 = AccessPoint.this.mOsuProvisioningComplete = false;
            ThreadUtils.postOnMainThread(new C1868xb3759df7(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningFailure$0$com-android-settingslib-wifi-AccessPoint$AccessPointProvisioningCallback */
        public /* synthetic */ void mo29379x972e0a5a() {
            if (AccessPoint.this.mAccessPointListener != null) {
                AccessPoint.this.mAccessPointListener.onAccessPointChanged(AccessPoint.this);
            }
        }

        public void onProvisioningStatus(int i) {
            String str;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    str = String.format(AccessPoint.this.mContext.getString(C1757R.string.osu_opening_provider), AccessPoint.this.mOsuProvider.getFriendlyName());
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    str = AccessPoint.this.mContext.getString(C1757R.string.osu_completing_sign_up);
                    break;
                default:
                    str = null;
                    break;
            }
            boolean equals = true ^ TextUtils.equals(AccessPoint.this.mOsuStatus, str);
            String unused = AccessPoint.this.mOsuStatus = str;
            String unused2 = AccessPoint.this.mOsuFailure = null;
            boolean unused3 = AccessPoint.this.mOsuProvisioningComplete = false;
            if (equals) {
                ThreadUtils.postOnMainThread(new C1866xb3759df5(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningStatus$1$com-android-settingslib-wifi-AccessPoint$AccessPointProvisioningCallback */
        public /* synthetic */ void mo29380x699d555f() {
            if (AccessPoint.this.mAccessPointListener != null) {
                AccessPoint.this.mAccessPointListener.onAccessPointChanged(AccessPoint.this);
            }
        }

        public void onProvisioningComplete() {
            boolean unused = AccessPoint.this.mOsuProvisioningComplete = true;
            String unused2 = AccessPoint.this.mOsuFailure = null;
            String unused3 = AccessPoint.this.mOsuStatus = null;
            ThreadUtils.postOnMainThread(new C1867xb3759df6(this));
            WifiManager access$500 = AccessPoint.this.getWifiManager();
            PasspointConfiguration passpointConfiguration = access$500.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(AccessPoint.this.mOsuProvider)).get(AccessPoint.this.mOsuProvider);
            if (passpointConfiguration == null) {
                Log.e(AccessPoint.TAG, "Missing PasspointConfiguration for newly provisioned network!");
                if (AccessPoint.this.mConnectListener != null) {
                    AccessPoint.this.mConnectListener.onFailure(0);
                    return;
                }
                return;
            }
            String uniqueId = passpointConfiguration.getUniqueId();
            for (Pair next : access$500.getAllMatchingWifiConfigs(access$500.getScanResults())) {
                WifiConfiguration wifiConfiguration = (WifiConfiguration) next.first;
                if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                    access$500.connect(new AccessPoint(AccessPoint.this.mContext, wifiConfiguration, (List) ((Map) next.second).get(0), (List) ((Map) next.second).get(1)).getConfig(), AccessPoint.this.mConnectListener);
                    return;
                }
            }
            if (AccessPoint.this.mConnectListener != null) {
                AccessPoint.this.mConnectListener.onFailure(0);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProvisioningComplete$2$com-android-settingslib-wifi-AccessPoint$AccessPointProvisioningCallback */
        public /* synthetic */ void mo29378xee4db917() {
            if (AccessPoint.this.mAccessPointListener != null) {
                AccessPoint.this.mAccessPointListener.onAccessPointChanged(AccessPoint.this);
            }
        }
    }

    public boolean isPskSaeTransitionMode() {
        return this.mIsPskSaeTransitionMode;
    }

    public boolean isOweTransitionMode() {
        return this.mIsOweTransitionMode;
    }

    private static boolean isPskSaeTransitionMode(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
    }

    private static boolean isOweTransitionMode(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE_TRANSITION");
    }

    private boolean isSameSsidOrBssid(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        if (TextUtils.equals(this.ssid, scanResult.SSID)) {
            return true;
        }
        return scanResult.BSSID != null && TextUtils.equals(this.bssid, scanResult.BSSID);
    }

    private boolean isSameSsidOrBssid(WifiInfo wifiInfo) {
        if (wifiInfo == null) {
            return false;
        }
        if (TextUtils.equals(this.ssid, removeDoubleQuotes(wifiInfo.getSSID()))) {
            return true;
        }
        return wifiInfo.getBSSID() != null && TextUtils.equals(this.bssid, wifiInfo.getBSSID());
    }

    private boolean isSameSsidOrBssid(AccessPoint accessPoint) {
        if (accessPoint == null) {
            return false;
        }
        if (TextUtils.equals(this.ssid, accessPoint.getSsid())) {
            return true;
        }
        return accessPoint.getBssid() != null && TextUtils.equals(this.bssid, accessPoint.getBssid());
    }

    private void updateDeviceWifiGenerationInfo() {
        WifiManager wifiManager = getWifiManager();
        if (wifiManager.isWifiStandardSupported(6)) {
            this.mDeviceWifiStandard = 6;
        } else if (wifiManager.isWifiStandardSupported(5)) {
            this.mDeviceWifiStandard = 5;
        } else if (wifiManager.isWifiStandardSupported(4)) {
            this.mDeviceWifiStandard = 4;
        } else {
            this.mDeviceWifiStandard = 1;
        }
    }

    private void updateWifiGeneration() {
        int i = this.mDeviceWifiStandard;
        Iterator<ScanResult> it = this.mScanResults.iterator();
        while (it.hasNext()) {
            int wifiStandard = it.next().getWifiStandard();
            if (wifiStandard < i) {
                i = wifiStandard;
            }
        }
        this.mWifiStandard = i;
    }

    public int getWifiStandard() {
        WifiInfo wifiInfo;
        if (!isActive() || (wifiInfo = this.mInfo) == null) {
            return this.mWifiStandard;
        }
        return wifiInfo.getWifiStandard();
    }
}
