package android.net.wifi;

import android.annotation.SystemApi;
import android.net.IpConfiguration;
import android.net.MacAddress;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.p004os.EnvironmentCompat;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import com.android.wifi.p018x.com.android.net.module.util.MacAddressUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import sun.util.locale.BaseLocale;
import sun.util.locale.LanguageTag;

@Deprecated
public class WifiConfiguration implements Parcelable {
    public static final int AP_BAND_2GHZ = 0;
    public static final int AP_BAND_5GHZ = 1;
    public static final int AP_BAND_60GHZ = 2;
    public static final int AP_BAND_ANY = -1;
    public static final int AP_BAND_DUAL = 3;
    private static final int BACKUP_VERSION = 3;
    public static final Parcelable.Creator<WifiConfiguration> CREATOR = new Parcelable.Creator<WifiConfiguration>() {
        public WifiConfiguration createFromParcel(Parcel parcel) {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.networkId = parcel.readInt();
            wifiConfiguration.status = parcel.readInt();
            wifiConfiguration.mNetworkSelectionStatus.readFromParcel(parcel);
            wifiConfiguration.SSID = parcel.readString();
            wifiConfiguration.BSSID = parcel.readString();
            wifiConfiguration.apBand = parcel.readInt();
            wifiConfiguration.apChannel = parcel.readInt();
            wifiConfiguration.FQDN = parcel.readString();
            wifiConfiguration.providerFriendlyName = parcel.readString();
            boolean z = true;
            wifiConfiguration.isHomeProviderNetwork = parcel.readInt() != 0;
            int readInt = parcel.readInt();
            wifiConfiguration.roamingConsortiumIds = new long[readInt];
            for (int i = 0; i < readInt; i++) {
                wifiConfiguration.roamingConsortiumIds[i] = parcel.readLong();
            }
            wifiConfiguration.preSharedKey = parcel.readString();
            for (int i2 = 0; i2 < wifiConfiguration.wepKeys.length; i2++) {
                wifiConfiguration.wepKeys[i2] = parcel.readString();
            }
            wifiConfiguration.wepTxKeyIndex = parcel.readInt();
            wifiConfiguration.priority = parcel.readInt();
            wifiConfiguration.mDeletionPriority = parcel.readInt();
            wifiConfiguration.hiddenSSID = parcel.readInt() != 0;
            wifiConfiguration.requirePmf = parcel.readInt() != 0;
            wifiConfiguration.updateIdentifier = parcel.readString();
            wifiConfiguration.allowedKeyManagement = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedProtocols = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedAuthAlgorithms = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedPairwiseCiphers = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedGroupCiphers = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedGroupManagementCiphers = WifiConfiguration.readBitSet(parcel);
            wifiConfiguration.allowedSuiteBCiphers = WifiConfiguration.readBitSet(parcel);
            int readInt2 = parcel.readInt();
            for (int i3 = 0; i3 < readInt2; i3++) {
                wifiConfiguration.mSecurityParamsList.add((SecurityParams) parcel.readParcelable((ClassLoader) null));
            }
            wifiConfiguration.enterpriseConfig = (WifiEnterpriseConfig) parcel.readParcelable((ClassLoader) null);
            wifiConfiguration.setIpConfiguration((IpConfiguration) parcel.readParcelable((ClassLoader) null));
            wifiConfiguration.dhcpServer = parcel.readString();
            wifiConfiguration.defaultGwMacAddress = parcel.readString();
            wifiConfiguration.validatedInternetAccess = parcel.readInt() != 0;
            wifiConfiguration.isLegacyPasspointConfig = parcel.readInt() != 0;
            wifiConfiguration.ephemeral = parcel.readInt() != 0;
            wifiConfiguration.trusted = parcel.readInt() != 0;
            wifiConfiguration.oemPaid = parcel.readInt() != 0;
            wifiConfiguration.oemPrivate = parcel.readInt() != 0;
            wifiConfiguration.carrierMerged = parcel.readInt() != 0;
            wifiConfiguration.fromWifiNetworkSuggestion = parcel.readInt() != 0;
            wifiConfiguration.fromWifiNetworkSpecifier = parcel.readInt() != 0;
            wifiConfiguration.dbsSecondaryInternet = parcel.readInt() != 0;
            wifiConfiguration.meteredHint = parcel.readInt() != 0;
            wifiConfiguration.mIsRepeaterEnabled = parcel.readBoolean();
            wifiConfiguration.meteredOverride = parcel.readInt();
            wifiConfiguration.useExternalScores = parcel.readInt() != 0;
            wifiConfiguration.creatorUid = parcel.readInt();
            wifiConfiguration.lastConnectUid = parcel.readInt();
            wifiConfiguration.lastUpdateUid = parcel.readInt();
            wifiConfiguration.creatorName = parcel.readString();
            wifiConfiguration.lastUpdateName = parcel.readString();
            wifiConfiguration.numScorerOverride = parcel.readInt();
            wifiConfiguration.numScorerOverrideAndSwitchedNetwork = parcel.readInt();
            wifiConfiguration.numAssociation = parcel.readInt();
            wifiConfiguration.allowAutojoin = parcel.readBoolean();
            wifiConfiguration.numNoInternetAccessReports = parcel.readInt();
            wifiConfiguration.noInternetAccessExpected = parcel.readInt() != 0;
            wifiConfiguration.shared = parcel.readInt() != 0;
            wifiConfiguration.mPasspointManagementObjectTree = parcel.readString();
            wifiConfiguration.recentFailure.setAssociationStatus(parcel.readInt(), parcel.readLong());
            wifiConfiguration.mRandomizedMacAddress = (MacAddress) parcel.readParcelable((ClassLoader) null);
            wifiConfiguration.macRandomizationSetting = parcel.readInt();
            if (parcel.readInt() == 0) {
                z = false;
            }
            wifiConfiguration.osu = z;
            wifiConfiguration.randomizedMacExpirationTimeMs = parcel.readLong();
            wifiConfiguration.randomizedMacLastModifiedTimeMs = parcel.readLong();
            wifiConfiguration.carrierId = parcel.readInt();
            wifiConfiguration.mPasspointUniqueId = parcel.readString();
            wifiConfiguration.subscriptionId = parcel.readInt();
            wifiConfiguration.restricted = parcel.readBoolean();
            wifiConfiguration.mSubscriptionGroup = (ParcelUuid) parcel.readParcelable((ClassLoader) null);
            wifiConfiguration.mBssidAllowlist = parcel.readArrayList(MacAddress.class.getClassLoader());
            wifiConfiguration.mIsDppConfigurator = parcel.readBoolean();
            wifiConfiguration.mDppPrivateEcKey = parcel.createByteArray();
            wifiConfiguration.mDppConnector = parcel.createByteArray();
            wifiConfiguration.mDppCSignKey = parcel.createByteArray();
            wifiConfiguration.mDppNetAccessKey = parcel.createByteArray();
            return wifiConfiguration;
        }

        public WifiConfiguration[] newArray(int i) {
            return new WifiConfiguration[i];
        }
    };
    public static final int HOME_NETWORK_RSSI_BOOST = 5;
    @SystemApi
    public static final int INVALID_NETWORK_ID = -1;
    public static int INVALID_RSSI = -127;
    public static final int LOCAL_ONLY_NETWORK_ID = -2;
    private static final int MAXIMUM_RANDOM_MAC_GENERATION_RETRY = 3;
    @SystemApi
    public static final int METERED_OVERRIDE_METERED = 1;
    @SystemApi
    public static final int METERED_OVERRIDE_NONE = 0;
    @SystemApi
    public static final int METERED_OVERRIDE_NOT_METERED = 2;
    public static final int RANDOMIZATION_AUTO = 3;
    public static final int RANDOMIZATION_NONE = 0;
    public static final int RANDOMIZATION_NON_PERSISTENT = 2;
    public static final int RANDOMIZATION_PERSISTENT = 1;
    @SystemApi
    public static final int RECENT_FAILURE_AP_UNABLE_TO_HANDLE_NEW_STA = 17;
    @SystemApi
    public static final int RECENT_FAILURE_DISCONNECTION_AP_BUSY = 1004;
    @SystemApi
    public static final int RECENT_FAILURE_MBO_ASSOC_DISALLOWED_AIR_INTERFACE_OVERLOADED = 1007;
    @SystemApi
    public static final int RECENT_FAILURE_MBO_ASSOC_DISALLOWED_AUTH_SERVER_OVERLOADED = 1008;
    @SystemApi
    public static final int RECENT_FAILURE_MBO_ASSOC_DISALLOWED_INSUFFICIENT_RSSI = 1009;
    @SystemApi
    public static final int RECENT_FAILURE_MBO_ASSOC_DISALLOWED_MAX_NUM_STA_ASSOCIATED = 1006;
    @SystemApi
    public static final int RECENT_FAILURE_MBO_ASSOC_DISALLOWED_UNSPECIFIED = 1005;
    @SystemApi
    public static final int RECENT_FAILURE_NETWORK_NOT_FOUND = 1011;
    @SystemApi
    public static final int RECENT_FAILURE_NONE = 0;
    @SystemApi
    public static final int RECENT_FAILURE_OCE_RSSI_BASED_ASSOCIATION_REJECTION = 1010;
    @SystemApi
    public static final int RECENT_FAILURE_POOR_CHANNEL_CONDITIONS = 1003;
    @SystemApi
    public static final int RECENT_FAILURE_REFUSED_TEMPORARILY = 1002;
    public static final int SECURITY_TYPE_DPP = 13;
    public static final int SECURITY_TYPE_EAP = 3;
    @Deprecated
    public static final int SECURITY_TYPE_EAP_SUITE_B = 5;
    public static final int SECURITY_TYPE_EAP_WPA3_ENTERPRISE = 9;
    public static final int SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT = 5;
    private static final String[] SECURITY_TYPE_NAMES = {"open", "wep", "wpa2-psk", "wpa2-enterprise", "wpa3-sae", "wpa3 enterprise 192-bit", "owe", "wapi-psk", "wapi-cert", "wpa3 enterprise", "wpa3 enterprise 192-bit", "passpoint r1/r2", "passpoint r3", "dpp"};
    public static final int SECURITY_TYPE_NUM = 13;
    public static final int SECURITY_TYPE_OPEN = 0;
    public static final int SECURITY_TYPE_OSEN = 10;
    public static final int SECURITY_TYPE_OWE = 6;
    public static final int SECURITY_TYPE_PASSPOINT_R1_R2 = 11;
    public static final int SECURITY_TYPE_PASSPOINT_R3 = 12;
    public static final int SECURITY_TYPE_PSK = 2;
    public static final int SECURITY_TYPE_SAE = 4;
    public static final int SECURITY_TYPE_WAPI_CERT = 8;
    public static final int SECURITY_TYPE_WAPI_PSK = 7;
    public static final int SECURITY_TYPE_WEP = 1;
    private static final String TAG = "WifiConfiguration";
    public static final int UNKNOWN_UID = -1;
    public static final String bssidVarName = "bssid";
    public static final String hiddenSSIDVarName = "scan_ssid";
    public static final String pmfVarName = "ieee80211w";
    public static final String priorityVarName = "priority";
    public static final String pskVarName = "psk";
    public static final String ssidVarName = "ssid";
    public static final String updateIdentiferVarName = "update_identifier";
    @Deprecated
    public static final String[] wepKeyVarNames = {"wep_key0", "wep_key1", "wep_key2", "wep_key3"};
    @Deprecated
    public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
    public String BSSID;
    public String FQDN;
    public String SSID;
    @SystemApi
    public boolean allowAutojoin;
    public BitSet allowedAuthAlgorithms;
    public BitSet allowedGroupCiphers;
    public BitSet allowedGroupManagementCiphers;
    public BitSet allowedKeyManagement;
    public BitSet allowedPairwiseCiphers;
    public BitSet allowedProtocols;
    public BitSet allowedSuiteBCiphers;
    public int apBand;
    public int apChannel;
    @SystemApi
    public int carrierId;
    @SystemApi
    public boolean carrierMerged;
    @SystemApi
    public String creatorName;
    @SystemApi
    public int creatorUid;
    public boolean dbsSecondaryInternet;
    public String defaultGwMacAddress;
    public String dhcpServer;
    public int dtimInterval;
    public WifiEnterpriseConfig enterpriseConfig;
    public boolean ephemeral;
    @SystemApi
    public boolean fromWifiNetworkSpecifier;
    @SystemApi
    public boolean fromWifiNetworkSuggestion;
    public boolean hiddenSSID;
    public boolean isHomeProviderNetwork;
    public boolean isLegacyPasspointConfig;
    public boolean isMostRecentlyConnected;
    @SystemApi
    public int lastConnectUid;
    public long lastConnected;
    public long lastDisconnected;
    @SystemApi
    public String lastUpdateName;
    @SystemApi
    public int lastUpdateUid;
    public long lastUpdated;
    public HashMap<String, Integer> linkedConfigurations;
    /* access modifiers changed from: private */
    public List<MacAddress> mBssidAllowlist;
    /* access modifiers changed from: private */
    public int mDeletionPriority;
    /* access modifiers changed from: private */
    public byte[] mDppCSignKey;
    /* access modifiers changed from: private */
    public byte[] mDppConnector;
    /* access modifiers changed from: private */
    public byte[] mDppNetAccessKey;
    /* access modifiers changed from: private */
    public byte[] mDppPrivateEcKey;
    private IpConfiguration mIpConfiguration;
    /* access modifiers changed from: private */
    public boolean mIsDppConfigurator;
    /* access modifiers changed from: private */
    public boolean mIsRepeaterEnabled;
    /* access modifiers changed from: private */
    public NetworkSelectionStatus mNetworkSelectionStatus;
    /* access modifiers changed from: private */
    public String mPasspointManagementObjectTree;
    /* access modifiers changed from: private */
    public String mPasspointUniqueId;
    /* access modifiers changed from: private */
    public MacAddress mRandomizedMacAddress;
    /* access modifiers changed from: private */
    public List<SecurityParams> mSecurityParamsList;
    /* access modifiers changed from: private */
    public ParcelUuid mSubscriptionGroup;
    @SystemApi
    public int macRandomizationSetting;
    @SystemApi
    public boolean meteredHint;
    @SystemApi
    public int meteredOverride;
    public int networkId;
    public boolean noInternetAccessExpected;
    @SystemApi
    public int numAssociation;
    public int numNoInternetAccessReports;
    public int numRebootsSinceLastUse;
    @SystemApi
    public int numScorerOverride;
    @SystemApi
    public int numScorerOverrideAndSwitchedNetwork;
    public boolean oemPaid;
    public boolean oemPrivate;
    public boolean osu;
    public String peerWifiConfiguration;
    public String preSharedKey;
    @Deprecated
    public int priority;
    public String providerFriendlyName;
    public long randomizedMacExpirationTimeMs;
    public long randomizedMacLastModifiedTimeMs;
    public final RecentFailure recentFailure;
    @SystemApi
    public boolean requirePmf;
    public boolean restricted;
    public long[] roamingConsortiumIds;
    public boolean selfAdded;
    @SystemApi
    public boolean shared;
    public int status;
    @SystemApi
    public int subscriptionId;
    public boolean trusted;
    public String updateIdentifier;
    @SystemApi
    public boolean useExternalScores;
    public boolean validatedInternetAccess;
    @Deprecated
    public String[] wepKeys;
    @Deprecated
    public int wepTxKeyIndex;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApBand {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MacRandomizationSetting {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MeteredOverride {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecentFailureReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType {
    }

    public int describeContents() {
        return 0;
    }

    public static class KeyMgmt {
        public static final int DPP = 17;
        public static final int FILS_SHA256 = 15;
        public static final int FILS_SHA384 = 16;
        public static final int FT_EAP = 7;
        public static final int FT_PSK = 6;
        public static final int IEEE8021X = 3;
        public static final int NONE = 0;
        public static final int OSEN = 5;
        public static final int OWE = 9;
        public static final int SAE = 8;
        public static final int SUITE_B_192 = 10;
        @SystemApi
        public static final int WAPI_CERT = 14;
        @SystemApi
        public static final int WAPI_PSK = 13;
        @SystemApi
        public static final int WPA2_PSK = 4;
        public static final int WPA_EAP = 2;
        public static final int WPA_EAP_SHA256 = 12;
        public static final int WPA_PSK = 1;
        public static final int WPA_PSK_SHA256 = 11;
        public static final String[] strings = {"NONE", "WPA_PSK", "WPA_EAP", "IEEE8021X", "WPA2_PSK", "OSEN", "FT_PSK", "FT_EAP", "SAE", "OWE", "SUITE_B_192", "WPA_PSK_SHA256", "WPA_EAP_SHA256", "WAPI_PSK", "WAPI_CERT", "FILS_SHA256", "FILS_SHA384", "DPP"};
        public static final String varName = "key_mgmt";

        @Retention(RetentionPolicy.SOURCE)
        public @interface KeyMgmtScheme {
        }

        private KeyMgmt() {
        }
    }

    public static class Protocol {
        public static final int OSEN = 2;
        public static final int RSN = 1;
        public static final int WAPI = 3;
        @Deprecated
        public static final int WPA = 0;
        public static final String[] strings = {"WPA", "RSN", "OSEN", "WAPI"};
        public static final String varName = "proto";

        @Retention(RetentionPolicy.SOURCE)
        public @interface ProtocolScheme {
        }

        private Protocol() {
        }
    }

    public static class AuthAlgorithm {
        public static final int LEAP = 2;
        public static final int OPEN = 0;
        public static final int SAE = 3;
        @Deprecated
        public static final int SHARED = 1;
        public static final String[] strings = {"OPEN", "SHARED", "LEAP", "SAE"};
        public static final String varName = "auth_alg";

        @Retention(RetentionPolicy.SOURCE)
        public @interface AuthAlgorithmScheme {
        }

        private AuthAlgorithm() {
        }
    }

    public static class PairwiseCipher {
        public static final int CCMP = 2;
        public static final int GCMP_128 = 5;
        public static final int GCMP_256 = 3;
        public static final int NONE = 0;
        public static final int SMS4 = 4;
        @Deprecated
        public static final int TKIP = 1;
        public static final String[] strings = {"NONE", "TKIP", "CCMP", "GCMP_256", "SMS4", "GCMP_128"};
        public static final String varName = "pairwise";

        @Retention(RetentionPolicy.SOURCE)
        public @interface PairwiseCipherScheme {
        }

        private PairwiseCipher() {
        }
    }

    public static class GroupCipher {
        public static final int CCMP = 3;
        public static final int GCMP_128 = 7;
        public static final int GCMP_256 = 5;
        public static final int GTK_NOT_USED = 4;
        public static final int SMS4 = 6;
        public static final int TKIP = 2;
        @Deprecated
        public static final int WEP104 = 1;
        @Deprecated
        public static final int WEP40 = 0;
        public static final String[] strings = {"WEP40", "WEP104", "TKIP", "CCMP", "GTK_NOT_USED", "GCMP_256", "SMS4", "GCMP_128"};
        public static final String varName = "group";

        @Retention(RetentionPolicy.SOURCE)
        public @interface GroupCipherScheme {
        }

        private GroupCipher() {
        }
    }

    public static class GroupMgmtCipher {
        public static final int BIP_CMAC_256 = 0;
        public static final int BIP_GMAC_128 = 1;
        public static final int BIP_GMAC_256 = 2;
        public static final String[] strings = {"BIP_CMAC_256", "BIP_GMAC_128", "BIP_GMAC_256"};
        private static final String varName = "groupMgmt";

        @Retention(RetentionPolicy.SOURCE)
        public @interface GroupMgmtCipherScheme {
        }

        private GroupMgmtCipher() {
        }
    }

    public static class SuiteBCipher {
        public static final int ECDHE_ECDSA = 0;
        public static final int ECDHE_RSA = 1;
        public static final String[] strings = {"ECDHE_ECDSA", "ECDHE_RSA"};
        private static final String varName = "SuiteB";

        @Retention(RetentionPolicy.SOURCE)
        public @interface SuiteBCipherScheme {
        }

        private SuiteBCipher() {
        }
    }

    public static class Status {
        public static final int CURRENT = 0;
        public static final int DISABLED = 1;
        public static final int ENABLED = 2;
        public static final String[] strings = {"current", "disabled", "enabled"};

        private Status() {
        }
    }

    private void updateLegacySecurityParams() {
        if (!this.mSecurityParamsList.isEmpty()) {
            this.mSecurityParamsList.get(0).updateLegacyWifiConfiguration(this);
        }
    }

    public void setSecurityParams(int i) {
        this.mSecurityParamsList.clear();
        addSecurityParams(i);
    }

    public void setSecurityParams(BitSet bitSet) {
        if (bitSet != null) {
            this.mSecurityParamsList.clear();
            this.allowedKeyManagement = (BitSet) bitSet.clone();
            convertLegacyFieldsToSecurityParamsIfNeeded();
            return;
        }
        throw new IllegalArgumentException("Invalid allowed key management mask.");
    }

    public void setSecurityParams(SecurityParams securityParams) {
        this.mSecurityParamsList.clear();
        addSecurityParams(securityParams);
    }

    public void setSecurityParams(List<SecurityParams> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("An empty security params list is invalid.");
        }
        this.mSecurityParamsList = (List) list.stream().map(new WifiConfiguration$$ExternalSyntheticLambda22()).collect(Collectors.toList());
        updateLegacySecurityParams();
    }

    static /* synthetic */ SecurityParams lambda$setSecurityParams$0(SecurityParams securityParams) {
        return new SecurityParams(securityParams);
    }

    public void addSecurityParams(int i) {
        if (!this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda5(i))) {
            addSecurityParams(SecurityParams.createSecurityParamsBySecurityType(i));
            return;
        }
        throw new IllegalArgumentException("duplicate security type " + i);
    }

    public void addSecurityParams(SecurityParams securityParams) {
        if (!this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda13(securityParams))) {
            if (!this.mSecurityParamsList.isEmpty()) {
                if (securityParams.isEnterpriseSecurityType() && !isEnterprise()) {
                    throw new IllegalArgumentException("An enterprise security type cannot be added to a personal configuation.");
                } else if (!securityParams.isEnterpriseSecurityType() && isEnterprise()) {
                    throw new IllegalArgumentException("A personal security type cannot be added to an enterprise configuation.");
                } else if (securityParams.isOpenSecurityType() && !isOpenNetwork()) {
                    throw new IllegalArgumentException("An open security type cannot be added to a non-open configuation.");
                } else if (!securityParams.isOpenSecurityType() && isOpenNetwork()) {
                    throw new IllegalArgumentException("A non-open security type cannot be added to an open configuation.");
                } else if (securityParams.isSecurityType(10)) {
                    throw new IllegalArgumentException("An OSEN security type must be the only one type.");
                }
            }
            this.mSecurityParamsList.add(new SecurityParams(securityParams));
            updateLegacySecurityParams();
            return;
        }
        throw new IllegalArgumentException("duplicate security params " + securityParams);
    }

    private boolean isWpa3EnterpriseConfiguration() {
        if ((this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3)) && this.requirePmf && this.allowedProtocols.cardinality() <= 1 && this.allowedProtocols.get(1) && !this.allowedPairwiseCiphers.get(1) && !this.allowedGroupCiphers.get(2)) {
            return true;
        }
        return false;
    }

    public void convertLegacyFieldsToSecurityParamsIfNeeded() {
        if (this.mSecurityParamsList.isEmpty()) {
            if (this.allowedKeyManagement.get(14)) {
                setSecurityParams(8);
            } else if (this.allowedKeyManagement.get(13)) {
                setSecurityParams(7);
            } else if (this.allowedKeyManagement.get(10)) {
                setSecurityParams(5);
            } else if (this.allowedKeyManagement.get(17)) {
                setSecurityParams(13);
            } else if (this.allowedKeyManagement.get(9)) {
                setSecurityParams(6);
            } else if (this.allowedKeyManagement.get(8)) {
                setSecurityParams(4);
            } else if (this.allowedKeyManagement.get(5)) {
                setSecurityParams(10);
            } else if (this.allowedKeyManagement.get(4)) {
                setSecurityParams(2);
            } else if (this.allowedKeyManagement.get(2)) {
                if (isWpa3EnterpriseConfiguration()) {
                    setSecurityParams(9);
                } else {
                    setSecurityParams(3);
                }
            } else if (this.allowedKeyManagement.get(1)) {
                setSecurityParams(2);
            } else if (!this.allowedKeyManagement.get(0)) {
                setSecurityParams(0);
            } else if (hasWepKeys()) {
                setSecurityParams(1);
            } else {
                setSecurityParams(0);
            }
        }
    }

    public void setSecurityParamsEnabled(int i, boolean z) {
        this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda3(i)).findAny().ifPresent(new WifiConfiguration$$ExternalSyntheticLambda4(z));
    }

    public void setSecurityParamsIsAddedByAutoUpgrade(int i, boolean z) {
        this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda23(i)).findAny().ifPresent(new WifiConfiguration$$ExternalSyntheticLambda24(z));
    }

    public SecurityParams getSecurityParams(int i) {
        SecurityParams orElse = this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda18(i)).findAny().orElse(null);
        if (orElse != null) {
            return new SecurityParams(orElse);
        }
        return null;
    }

    public boolean isSecurityType(int i) {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda2(i));
    }

    public List<SecurityParams> getSecurityParamsList() {
        return Collections.unmodifiableList(this.mSecurityParamsList);
    }

    public SecurityParams getDefaultSecurityParams() {
        return new SecurityParams(this.mSecurityParamsList.get(0));
    }

    public void enableFils(boolean z, boolean z2) {
        this.mSecurityParamsList.stream().forEach(new WifiConfiguration$$ExternalSyntheticLambda8(z, z2));
        updateLegacySecurityParams();
    }

    public boolean isFilsSha256Enabled() {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda17());
    }

    public boolean isFilsSha384Enabled() {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda0());
    }

    public void enableSuiteBCiphers(boolean z, boolean z2) {
        this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda19()).findAny().ifPresent(new WifiConfiguration$$ExternalSyntheticLambda20(z, z2));
        updateLegacySecurityParams();
    }

    public boolean isSuiteBCipherEcdheEcdsaEnabled() {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda21());
    }

    public boolean isSuiteBCipherEcdheRsaEnabled() {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda7());
    }

    public void enableSaeH2eOnlyMode(boolean z) {
        this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda25()).findAny().ifPresent(new WifiConfiguration$$ExternalSyntheticLambda1(z));
    }

    public void enableSaePkOnlyMode(boolean z) {
        this.mSecurityParamsList.stream().filter(new WifiConfiguration$$ExternalSyntheticLambda14()).findAny().ifPresent(new WifiConfiguration$$ExternalSyntheticLambda15(z));
    }

    @SystemApi
    public void setBssidAllowlist(List<MacAddress> list) {
        if (list == null) {
            this.mBssidAllowlist = null;
        } else {
            this.mBssidAllowlist = new ArrayList(list);
        }
    }

    @SystemApi
    public List<MacAddress> getBssidAllowlist() {
        if (this.mBssidAllowlist == null) {
            return null;
        }
        return new ArrayList(this.mBssidAllowlist);
    }

    public List<MacAddress> getBssidAllowlistInternal() {
        return this.mBssidAllowlist;
    }

    @SystemApi
    public void setDeletionPriority(int i) throws IllegalArgumentException {
        if (i >= 0) {
            this.mDeletionPriority = i;
            return;
        }
        throw new IllegalArgumentException("Deletion priority must be non-negative");
    }

    @SystemApi
    public int getDeletionPriority() {
        return this.mDeletionPriority;
    }

    @SystemApi
    public boolean hasNoInternetAccess() {
        return this.numNoInternetAccessReports > 0 && !this.validatedInternetAccess;
    }

    @SystemApi
    public boolean isNoInternetAccessExpected() {
        return this.noInternetAccessExpected;
    }

    @SystemApi
    public boolean isEphemeral() {
        return this.ephemeral;
    }

    @SystemApi
    public void setRepeaterEnabled(boolean z) {
        this.mIsRepeaterEnabled = z;
    }

    @SystemApi
    public boolean isRepeaterEnabled() {
        return this.mIsRepeaterEnabled;
    }

    @SystemApi
    public static boolean isMetered(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo) {
        boolean z = true;
        boolean z2 = wifiInfo != null && wifiInfo.getMeteredHint();
        if (wifiConfiguration != null && wifiConfiguration.meteredHint) {
            z2 = true;
        }
        if (wifiConfiguration == null || wifiConfiguration.meteredOverride != 1) {
            z = z2;
        }
        if (wifiConfiguration == null || wifiConfiguration.meteredOverride != 2) {
            return z;
        }
        return false;
    }

    private boolean hasWepKeys() {
        if (this.wepKeys == null) {
            return false;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.wepKeys;
            if (i >= strArr.length) {
                return false;
            }
            if (strArr[i] != null) {
                return true;
            }
            i++;
        }
    }

    public boolean isOpenNetwork() {
        return !this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda16()) && !hasWepKeys();
    }

    static /* synthetic */ boolean lambda$isOpenNetwork$20(SecurityParams securityParams) {
        return !securityParams.isOpenSecurityType();
    }

    public void setMacRandomizationSetting(int i) {
        this.macRandomizationSetting = i;
    }

    public int getMacRandomizationSetting() {
        return this.macRandomizationSetting;
    }

    public static boolean isValidMacAddressForRandomization(MacAddress macAddress) {
        return macAddress != null && !MacAddressUtils.isMulticastAddress(macAddress) && macAddress.isLocallyAssigned() && !MacAddress.fromString(WifiInfo.DEFAULT_MAC_ADDRESS).equals(macAddress);
    }

    public MacAddress getRandomizedMacAddress() {
        return this.mRandomizedMacAddress;
    }

    public void setRandomizedMacAddress(MacAddress macAddress) {
        if (macAddress == null) {
            Log.e(TAG, "setRandomizedMacAddress received null MacAddress.");
        } else {
            this.mRandomizedMacAddress = macAddress;
        }
    }

    public void setDppConnectionKeys(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr == null || bArr2 == null || bArr3 == null) {
            Log.e(TAG, "One of DPP key is null");
            return;
        }
        this.mDppConnector = (byte[]) bArr.clone();
        this.mDppCSignKey = (byte[]) bArr2.clone();
        this.mDppNetAccessKey = (byte[]) bArr3.clone();
    }

    public void setDppConfigurator(byte[] bArr) {
        if (bArr != null) {
            this.mDppPrivateEcKey = (byte[]) bArr.clone();
            this.mIsDppConfigurator = true;
        }
    }

    public boolean isDppConfigurator() {
        return this.mIsDppConfigurator;
    }

    @SystemApi
    public byte[] getDppPrivateEcKey() {
        return (byte[]) this.mDppPrivateEcKey.clone();
    }

    @SystemApi
    public byte[] getDppConnector() {
        return (byte[]) this.mDppConnector.clone();
    }

    @SystemApi
    public byte[] getDppCSignKey() {
        return (byte[]) this.mDppCSignKey.clone();
    }

    @SystemApi
    public byte[] getDppNetAccessKey() {
        return (byte[]) this.mDppNetAccessKey.clone();
    }

    @SystemApi
    public static class NetworkSelectionStatus {
        private static final int CONNECT_CHOICE_EXISTS = 1;
        private static final int CONNECT_CHOICE_NOT_EXISTS = -1;
        public static final int DISABLED_ASSOCIATION_REJECTION = 1;
        public static final int DISABLED_AUTHENTICATION_FAILURE = 2;
        public static final int DISABLED_AUTHENTICATION_NO_CREDENTIALS = 5;
        public static final int DISABLED_AUTHENTICATION_NO_SUBSCRIPTION = 9;
        public static final int DISABLED_AUTHENTICATION_PRIVATE_EAP_ERROR = 10;
        public static final int DISABLED_BY_WIFI_MANAGER = 7;
        public static final int DISABLED_BY_WRONG_PASSWORD = 8;
        public static final int DISABLED_CONSECUTIVE_FAILURES = 12;
        public static final int DISABLED_DHCP_FAILURE = 3;
        public static final int DISABLED_NETWORK_NOT_FOUND = 11;
        public static final int DISABLED_NONE = 0;
        public static final int DISABLED_NO_INTERNET_PERMANENT = 6;
        public static final int DISABLED_NO_INTERNET_TEMPORARY = 4;
        public static final SparseArray<DisableReasonInfo> DISABLE_REASON_INFOS = buildDisableReasonInfos();
        public static final long INVALID_NETWORK_SELECTION_DISABLE_TIMESTAMP = -1;
        public static final int NETWORK_SELECTION_DISABLED_MAX = 13;
        public static final int NETWORK_SELECTION_DISABLED_STARTING_INDEX = 1;
        public static final int NETWORK_SELECTION_ENABLED = 0;
        public static final int NETWORK_SELECTION_PERMANENTLY_DISABLED = 2;
        public static final int NETWORK_SELECTION_STATUS_MAX = 3;
        public static final int NETWORK_SELECTION_TEMPORARY_DISABLED = 1;
        public static final String[] QUALITY_NETWORK_SELECTION_STATUS = {"NETWORK_SELECTION_ENABLED", "NETWORK_SELECTION_TEMPORARY_DISABLED", "NETWORK_SELECTION_PERMANENTLY_DISABLED"};
        private ScanResult mCandidate;
        private int mCandidateScore;
        private SecurityParams mCandidateSecurityParams;
        private String mConnectChoice;
        private int mConnectChoiceRssi;
        private boolean mHasEverConnected = false;
        private boolean mHasNeverDetectedCaptivePortal = true;
        private SecurityParams mLastUsedSecurityParams;
        private int[] mNetworkSeclectionDisableCounter = new int[13];
        private String mNetworkSelectionBSSID;
        private int mNetworkSelectionDisableReason;
        private boolean mSeenInLastQualifiedNetworkSelection;
        private int mStatus;
        private long mTemporarilyDisabledTimestamp = -1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface NetworkEnabledStatus {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface NetworkSelectionDisableReason {
        }

        public static int getMaxNetworkSelectionDisableReason() {
            return 12;
        }

        public static final class DisableReasonInfo {
            public static final int PERMANENT_DISABLE_TIMEOUT = -1;
            public final int mDisableThreshold;
            public final int mDisableTimeoutMillis;
            public final String mReasonStr;

            public DisableReasonInfo(String str, int i, int i2) {
                this.mReasonStr = str;
                this.mDisableThreshold = i;
                this.mDisableTimeoutMillis = i2;
            }
        }

        private static SparseArray<DisableReasonInfo> buildDisableReasonInfos() {
            SparseArray<DisableReasonInfo> sparseArray = new SparseArray<>();
            sparseArray.append(0, new DisableReasonInfo("NETWORK_SELECTION_ENABLE", -1, 0));
            sparseArray.append(1, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_ASSOCIATION_REJECTION ", 3, 300000));
            sparseArray.append(2, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_AUTHENTICATION_FAILURE", 3, 300000));
            sparseArray.append(3, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_DHCP_FAILURE", 2, 300000));
            sparseArray.append(4, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_NO_INTERNET_TEMPORARY", 1, 600000));
            sparseArray.append(5, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_CREDENTIALS", 3, -1));
            sparseArray.append(6, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_NO_INTERNET_PERMANENT", 1, -1));
            sparseArray.append(7, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_BY_WIFI_MANAGER", 1, -1));
            sparseArray.append(8, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_BY_WRONG_PASSWORD", 1, -1));
            sparseArray.append(9, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_SUBSCRIPTION", 1, -1));
            sparseArray.append(10, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_AUTHENTICATION_PRIVATE_EAP_ERROR", 1, -1));
            sparseArray.append(11, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_NETWORK_NOT_FOUND", 2, 300000));
            sparseArray.append(12, new DisableReasonInfo("NETWORK_SELECTION_DISABLED_CONSECUTIVE_FAILURES", 1, 300000));
            return sparseArray;
        }

        public static int getDisableReasonByString(String str) {
            int i = 0;
            while (true) {
                SparseArray<DisableReasonInfo> sparseArray = DISABLE_REASON_INFOS;
                if (i < sparseArray.size()) {
                    int keyAt = sparseArray.keyAt(i);
                    DisableReasonInfo valueAt = sparseArray.valueAt(i);
                    if (valueAt != null && TextUtils.equals(str, valueAt.mReasonStr)) {
                        return keyAt;
                    }
                    i++;
                } else {
                    Log.e(WifiConfiguration.TAG, "Unrecognized network disable reason: " + str);
                    return -1;
                }
            }
        }

        public void setSeenInLastQualifiedNetworkSelection(boolean z) {
            this.mSeenInLastQualifiedNetworkSelection = z;
        }

        public boolean getSeenInLastQualifiedNetworkSelection() {
            return this.mSeenInLastQualifiedNetworkSelection;
        }

        public void setCandidate(ScanResult scanResult) {
            this.mCandidate = scanResult;
        }

        public ScanResult getCandidate() {
            return this.mCandidate;
        }

        public void setCandidateScore(int i) {
            this.mCandidateScore = i;
        }

        public int getCandidateScore() {
            return this.mCandidateScore;
        }

        public void setCandidateSecurityParams(SecurityParams securityParams) {
            this.mCandidateSecurityParams = securityParams;
        }

        public SecurityParams getCandidateSecurityParams() {
            return this.mCandidateSecurityParams;
        }

        public void setLastUsedSecurityParams(SecurityParams securityParams) {
            this.mLastUsedSecurityParams = securityParams;
        }

        public SecurityParams getLastUsedSecurityParams() {
            return this.mLastUsedSecurityParams;
        }

        public String getConnectChoice() {
            return this.mConnectChoice;
        }

        public void setConnectChoice(String str) {
            this.mConnectChoice = str;
        }

        public void setConnectChoiceRssi(int i) {
            this.mConnectChoiceRssi = i;
        }

        public int getConnectChoiceRssi() {
            return this.mConnectChoiceRssi;
        }

        public String getNetworkStatusString() {
            return QUALITY_NETWORK_SELECTION_STATUS[this.mStatus];
        }

        public void setHasEverConnected(boolean z) {
            this.mHasEverConnected = z;
        }

        public boolean hasEverConnected() {
            return this.mHasEverConnected;
        }

        public void setHasNeverDetectedCaptivePortal(boolean z) {
            this.mHasNeverDetectedCaptivePortal = z;
        }

        public boolean hasNeverDetectedCaptivePortal() {
            return this.mHasNeverDetectedCaptivePortal;
        }

        public static final class Builder {
            private final NetworkSelectionStatus mNetworkSelectionStatus = new NetworkSelectionStatus();

            public Builder setNetworkSelectionStatus(int i) {
                this.mNetworkSelectionStatus.setNetworkSelectionStatus(i);
                return this;
            }

            public Builder setNetworkSelectionDisableReason(int i) {
                this.mNetworkSelectionStatus.setNetworkSelectionDisableReason(i);
                return this;
            }

            public NetworkSelectionStatus build() {
                NetworkSelectionStatus networkSelectionStatus = new NetworkSelectionStatus();
                networkSelectionStatus.copy(this.mNetworkSelectionStatus);
                return networkSelectionStatus;
            }
        }

        public static String getNetworkSelectionDisableReasonString(int i) {
            DisableReasonInfo disableReasonInfo = DISABLE_REASON_INFOS.get(i);
            if (disableReasonInfo == null) {
                return null;
            }
            return disableReasonInfo.mReasonStr;
        }

        public String getNetworkSelectionDisableReasonString() {
            return getNetworkSelectionDisableReasonString(this.mNetworkSelectionDisableReason);
        }

        public int getNetworkSelectionStatus() {
            return this.mStatus;
        }

        public boolean isNetworkEnabled() {
            return this.mStatus == 0;
        }

        public boolean isNetworkTemporaryDisabled() {
            return this.mStatus == 1;
        }

        public boolean isNetworkPermanentlyDisabled() {
            return this.mStatus == 2;
        }

        public void setNetworkSelectionStatus(int i) {
            if (i >= 0 && i < 3) {
                this.mStatus = i;
            }
        }

        public int getNetworkSelectionDisableReason() {
            return this.mNetworkSelectionDisableReason;
        }

        public void setNetworkSelectionDisableReason(int i) {
            if (i < 0 || i >= 13) {
                throw new IllegalArgumentException("Illegal reason value: " + i);
            }
            this.mNetworkSelectionDisableReason = i;
        }

        public void setDisableTime(long j) {
            this.mTemporarilyDisabledTimestamp = j;
        }

        public long getDisableTime() {
            return this.mTemporarilyDisabledTimestamp;
        }

        public int getDisableReasonCounter(int i) {
            if (i >= 0 && i < 13) {
                return this.mNetworkSeclectionDisableCounter[i];
            }
            throw new IllegalArgumentException("Illegal reason value: " + i);
        }

        public void setDisableReasonCounter(int i, int i2) {
            if (i < 0 || i >= 13) {
                throw new IllegalArgumentException("Illegal reason value: " + i);
            }
            this.mNetworkSeclectionDisableCounter[i] = i2;
        }

        public void incrementDisableReasonCounter(int i) {
            if (i < 0 || i >= 13) {
                throw new IllegalArgumentException("Illegal reason value: " + i);
            }
            int[] iArr = this.mNetworkSeclectionDisableCounter;
            iArr[i] = iArr[i] + 1;
        }

        public void clearDisableReasonCounter(int i) {
            if (i < 0 || i >= 13) {
                throw new IllegalArgumentException("Illegal reason value: " + i);
            }
            this.mNetworkSeclectionDisableCounter[i] = 0;
        }

        public void clearDisableReasonCounter() {
            Arrays.fill(this.mNetworkSeclectionDisableCounter, 0);
        }

        public String getNetworkSelectionBSSID() {
            return this.mNetworkSelectionBSSID;
        }

        public void setNetworkSelectionBSSID(String str) {
            this.mNetworkSelectionBSSID = str;
        }

        public void copy(NetworkSelectionStatus networkSelectionStatus) {
            this.mStatus = networkSelectionStatus.mStatus;
            this.mNetworkSelectionDisableReason = networkSelectionStatus.mNetworkSelectionDisableReason;
            for (int i = 0; i < 13; i++) {
                this.mNetworkSeclectionDisableCounter[i] = networkSelectionStatus.mNetworkSeclectionDisableCounter[i];
            }
            this.mTemporarilyDisabledTimestamp = networkSelectionStatus.mTemporarilyDisabledTimestamp;
            this.mNetworkSelectionBSSID = networkSelectionStatus.mNetworkSelectionBSSID;
            setSeenInLastQualifiedNetworkSelection(networkSelectionStatus.getSeenInLastQualifiedNetworkSelection());
            setCandidate(networkSelectionStatus.getCandidate());
            setCandidateScore(networkSelectionStatus.getCandidateScore());
            setCandidateSecurityParams(networkSelectionStatus.getCandidateSecurityParams());
            setLastUsedSecurityParams(networkSelectionStatus.getLastUsedSecurityParams());
            setConnectChoice(networkSelectionStatus.getConnectChoice());
            setConnectChoiceRssi(networkSelectionStatus.getConnectChoiceRssi());
            setHasEverConnected(networkSelectionStatus.hasEverConnected());
            setHasNeverDetectedCaptivePortal(networkSelectionStatus.hasNeverDetectedCaptivePortal());
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(getNetworkSelectionStatus());
            parcel.writeInt(getNetworkSelectionDisableReason());
            for (int i2 = 0; i2 < 13; i2++) {
                parcel.writeInt(getDisableReasonCounter(i2));
            }
            parcel.writeLong(getDisableTime());
            parcel.writeString(getNetworkSelectionBSSID());
            if (getConnectChoice() != null) {
                parcel.writeInt(1);
                parcel.writeString(getConnectChoice());
                parcel.writeInt(getConnectChoiceRssi());
            } else {
                parcel.writeInt(-1);
            }
            parcel.writeInt(hasEverConnected() ? 1 : 0);
            parcel.writeInt(hasNeverDetectedCaptivePortal() ? 1 : 0);
            parcel.writeParcelable(getCandidateSecurityParams(), i);
            parcel.writeParcelable(getLastUsedSecurityParams(), i);
        }

        public void readFromParcel(Parcel parcel) {
            setNetworkSelectionStatus(parcel.readInt());
            setNetworkSelectionDisableReason(parcel.readInt());
            boolean z = false;
            for (int i = 0; i < 13; i++) {
                setDisableReasonCounter(i, parcel.readInt());
            }
            setDisableTime(parcel.readLong());
            setNetworkSelectionBSSID(parcel.readString());
            if (parcel.readInt() == 1) {
                setConnectChoice(parcel.readString());
                setConnectChoiceRssi(parcel.readInt());
            } else {
                setConnectChoice((String) null);
            }
            setHasEverConnected(parcel.readInt() != 0);
            if (parcel.readInt() != 0) {
                z = true;
            }
            setHasNeverDetectedCaptivePortal(z);
            setCandidateSecurityParams((SecurityParams) parcel.readParcelable((ClassLoader) null));
            setLastUsedSecurityParams((SecurityParams) parcel.readParcelable((ClassLoader) null));
        }
    }

    public static class RecentFailure {
        private int mAssociationStatus;
        private long mLastUpdateTimeSinceBootMillis;

        private RecentFailure() {
            this.mAssociationStatus = 0;
        }

        public void setAssociationStatus(int i, long j) {
            this.mAssociationStatus = i;
            this.mLastUpdateTimeSinceBootMillis = j;
        }

        public void clear() {
            this.mAssociationStatus = 0;
            this.mLastUpdateTimeSinceBootMillis = 0;
        }

        public int getAssociationStatus() {
            return this.mAssociationStatus;
        }

        public long getLastUpdateTimeSinceBootMillis() {
            return this.mLastUpdateTimeSinceBootMillis;
        }
    }

    @SystemApi
    public int getRecentFailureReason() {
        return this.recentFailure.getAssociationStatus();
    }

    @SystemApi
    public NetworkSelectionStatus getNetworkSelectionStatus() {
        return this.mNetworkSelectionStatus;
    }

    @SystemApi
    public void setNetworkSelectionStatus(NetworkSelectionStatus networkSelectionStatus) {
        this.mNetworkSelectionStatus = networkSelectionStatus;
    }

    public WifiConfiguration() {
        this.mSecurityParamsList = new ArrayList();
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.carrierId = -1;
        this.subscriptionId = -1;
        this.mSubscriptionGroup = null;
        this.allowAutojoin = true;
        this.meteredOverride = 0;
        this.macRandomizationSetting = 3;
        this.randomizedMacExpirationTimeMs = 0;
        this.randomizedMacLastModifiedTimeMs = 0;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        this.mPasspointUniqueId = null;
        this.isMostRecentlyConnected = false;
        this.networkId = -1;
        this.SSID = null;
        this.BSSID = null;
        this.FQDN = null;
        this.roamingConsortiumIds = new long[0];
        this.priority = 0;
        this.mDeletionPriority = 0;
        this.hiddenSSID = false;
        this.allowedKeyManagement = new BitSet();
        this.allowedProtocols = new BitSet();
        this.allowedAuthAlgorithms = new BitSet();
        this.allowedPairwiseCiphers = new BitSet();
        this.allowedGroupCiphers = new BitSet();
        this.allowedGroupManagementCiphers = new BitSet();
        this.allowedSuiteBCiphers = new BitSet();
        this.wepKeys = new String[4];
        int i = 0;
        while (true) {
            String[] strArr = this.wepKeys;
            if (i < strArr.length) {
                strArr[i] = null;
                i++;
            } else {
                this.enterpriseConfig = new WifiEnterpriseConfig();
                this.ephemeral = false;
                this.osu = false;
                this.trusted = true;
                this.oemPaid = false;
                this.oemPrivate = false;
                this.carrierMerged = false;
                this.fromWifiNetworkSuggestion = false;
                this.fromWifiNetworkSpecifier = false;
                this.dbsSecondaryInternet = false;
                this.meteredHint = false;
                this.mIsRepeaterEnabled = false;
                this.meteredOverride = 0;
                this.useExternalScores = false;
                this.validatedInternetAccess = false;
                this.mIpConfiguration = new IpConfiguration();
                this.lastUpdateUid = -1;
                this.creatorUid = -1;
                this.shared = true;
                this.dtimInterval = 0;
                this.mRandomizedMacAddress = MacAddress.fromString(WifiInfo.DEFAULT_MAC_ADDRESS);
                this.numRebootsSinceLastUse = 0;
                this.restricted = false;
                this.mBssidAllowlist = null;
                this.mIsDppConfigurator = false;
                this.mDppPrivateEcKey = new byte[0];
                this.mDppConnector = new byte[0];
                this.mDppCSignKey = new byte[0];
                this.mDppNetAccessKey = new byte[0];
                return;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        r0 = r2.enterpriseConfig;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isPasspoint() {
        /*
            r2 = this;
            java.lang.String r0 = r2.FQDN
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0025
            java.lang.String r0 = r2.providerFriendlyName
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0025
            android.net.wifi.WifiEnterpriseConfig r0 = r2.enterpriseConfig
            if (r0 == 0) goto L_0x0025
            int r0 = r0.getEapMethod()
            r1 = -1
            if (r0 == r1) goto L_0x0025
            java.lang.String r2 = r2.mPasspointUniqueId
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0025
            r2 = 1
            goto L_0x0026
        L_0x0025:
            r2 = 0
        L_0x0026:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiConfiguration.isPasspoint():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r0 = r3.linkedConfigurations;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isLinked(android.net.wifi.WifiConfiguration r3) {
        /*
            r2 = this;
            if (r3 == 0) goto L_0x0022
            java.util.HashMap<java.lang.String, java.lang.Integer> r0 = r3.linkedConfigurations
            if (r0 == 0) goto L_0x0022
            java.util.HashMap<java.lang.String, java.lang.Integer> r1 = r2.linkedConfigurations
            if (r1 == 0) goto L_0x0022
            java.lang.String r1 = r2.getKey()
            java.lang.Object r0 = r0.get(r1)
            if (r0 == 0) goto L_0x0022
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r2.linkedConfigurations
            java.lang.String r3 = r3.getKey()
            java.lang.Object r2 = r2.get(r3)
            if (r2 == 0) goto L_0x0022
            r2 = 1
            return r2
        L_0x0022:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiConfiguration.isLinked(android.net.wifi.WifiConfiguration):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0011, code lost:
        r2 = r2.enterpriseConfig;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEnterprise() {
        /*
            r2 = this;
            java.util.List<android.net.wifi.SecurityParams> r0 = r2.mSecurityParamsList
            java.util.stream.Stream r0 = r0.stream()
            android.net.wifi.WifiConfiguration$$ExternalSyntheticLambda9 r1 = new android.net.wifi.WifiConfiguration$$ExternalSyntheticLambda9
            r1.<init>()
            boolean r0 = r0.anyMatch(r1)
            if (r0 == 0) goto L_0x001e
            android.net.wifi.WifiEnterpriseConfig r2 = r2.enterpriseConfig
            if (r2 == 0) goto L_0x001e
            int r2 = r2.getEapMethod()
            r0 = -1
            if (r2 == r0) goto L_0x001e
            r2 = 1
            goto L_0x001f
        L_0x001e:
            r2 = 0
        L_0x001f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiConfiguration.isEnterprise():boolean");
    }

    private static String logTimeOfDay(long j) {
        Calendar instance = Calendar.getInstance();
        if (j < 0) {
            return Long.toString(j);
        }
        instance.setTimeInMillis(j);
        return String.format("%tm-%td %tH:%tM:%tS.%tL", instance, instance, instance, instance, instance, instance);
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        int i = this.status;
        if (i == 0) {
            sb.append("* ");
        } else if (i == 1) {
            sb.append("- DSBLE ");
        }
        sb.append("ID: ");
        sb.append(this.networkId);
        sb.append(" SSID: ");
        sb.append(this.SSID);
        sb.append(" PROVIDER-NAME: ");
        sb.append(this.providerFriendlyName);
        sb.append(" BSSID: ");
        sb.append(this.BSSID);
        sb.append(" FQDN: ");
        sb.append(this.FQDN);
        sb.append(" HOME-PROVIDER-NETWORK: ");
        sb.append(this.isHomeProviderNetwork);
        sb.append(" PRIO: ");
        sb.append(this.priority);
        sb.append(" HIDDEN: ");
        sb.append(this.hiddenSSID);
        sb.append(" PMF: ");
        sb.append(this.requirePmf);
        sb.append(" CarrierId: ");
        sb.append(this.carrierId);
        sb.append(" SubscriptionId: ");
        sb.append(this.subscriptionId);
        sb.append(" SubscriptionGroup: ");
        sb.append((Object) this.mSubscriptionGroup);
        sb.append("\n NetworkSelectionStatus ");
        sb.append(this.mNetworkSelectionStatus.getNetworkStatusString());
        sb.append("\n");
        if (this.mNetworkSelectionStatus.getNetworkSelectionDisableReason() > 0) {
            sb.append(" mNetworkSelectionDisableReason ");
            sb.append(this.mNetworkSelectionStatus.getNetworkSelectionDisableReasonString());
            sb.append("\n");
            for (int i2 = 0; i2 < 13; i2++) {
                if (this.mNetworkSelectionStatus.getDisableReasonCounter(i2) != 0) {
                    sb.append(NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i2));
                    sb.append(" counter:");
                    sb.append(this.mNetworkSelectionStatus.getDisableReasonCounter(i2));
                    sb.append("\n");
                }
            }
        }
        if (this.mNetworkSelectionStatus.getConnectChoice() != null) {
            sb.append(" connect choice: ");
            sb.append(this.mNetworkSelectionStatus.getConnectChoice());
            sb.append(" connect choice rssi: ");
            sb.append(this.mNetworkSelectionStatus.getConnectChoiceRssi());
        }
        sb.append(" hasEverConnected: ");
        sb.append(this.mNetworkSelectionStatus.hasEverConnected());
        sb.append("\n hasNeverDetectedCaptivePortal: ");
        sb.append(this.mNetworkSelectionStatus.hasNeverDetectedCaptivePortal());
        sb.append("\n mCandidateSecurityParams: ");
        sb.append((Object) this.mNetworkSelectionStatus.getCandidateSecurityParams());
        sb.append(" mLastUsedSecurityParams: ");
        sb.append((Object) this.mNetworkSelectionStatus.getLastUsedSecurityParams());
        if (this.numAssociation > 0) {
            sb.append(" numAssociation ");
            sb.append(this.numAssociation);
            sb.append("\n");
        }
        if (this.numNoInternetAccessReports > 0) {
            sb.append(" numNoInternetAccessReports ");
            sb.append(this.numNoInternetAccessReports);
            sb.append("\n");
        }
        if (this.validatedInternetAccess) {
            sb.append(" validatedInternetAccess");
        }
        if (this.shared) {
            sb.append(" shared");
        } else {
            sb.append(" not-shared");
        }
        if (this.ephemeral) {
            sb.append(" ephemeral");
        }
        if (this.osu) {
            sb.append(" osu");
        }
        if (this.trusted) {
            sb.append(" trusted");
        }
        if (this.restricted) {
            sb.append(" restricted");
        }
        if (this.oemPaid) {
            sb.append(" oemPaid");
        }
        if (this.oemPrivate) {
            sb.append(" oemPrivate");
        }
        if (this.carrierMerged) {
            sb.append(" carrierMerged");
        }
        if (this.fromWifiNetworkSuggestion) {
            sb.append(" fromWifiNetworkSuggestion");
        }
        if (this.fromWifiNetworkSpecifier) {
            sb.append(" fromWifiNetworkSpecifier");
        }
        if (this.dbsSecondaryInternet) {
            sb.append(" dbsSecondaryInternet");
        }
        if (this.meteredHint) {
            sb.append(" meteredHint");
        }
        if (this.mIsRepeaterEnabled) {
            sb.append(" repeaterEnabled");
        }
        if (this.useExternalScores) {
            sb.append(" useExternalScores");
        }
        if (this.validatedInternetAccess || this.ephemeral || this.trusted || this.oemPaid || this.oemPrivate || this.carrierMerged || this.fromWifiNetworkSuggestion || this.fromWifiNetworkSpecifier || this.meteredHint || this.useExternalScores || this.restricted || this.dbsSecondaryInternet) {
            sb.append("\n");
        }
        if (this.meteredOverride != 0) {
            sb.append(" meteredOverride ");
            sb.append(this.meteredOverride);
            sb.append("\n");
        }
        sb.append(" macRandomizationSetting: ");
        sb.append(this.macRandomizationSetting);
        sb.append("\n mRandomizedMacAddress: ");
        sb.append((Object) this.mRandomizedMacAddress);
        sb.append("\n randomizedMacExpirationTimeMs: ");
        long j = this.randomizedMacExpirationTimeMs;
        String str2 = "<none>";
        if (j == 0) {
            str = str2;
        } else {
            str = logTimeOfDay(j);
        }
        sb.append(str);
        sb.append("\n randomizedMacLastModifiedTimeMs: ");
        long j2 = this.randomizedMacLastModifiedTimeMs;
        if (j2 != 0) {
            str2 = logTimeOfDay(j2);
        }
        sb.append(str2);
        sb.append("\n deletionPriority: ");
        sb.append(this.mDeletionPriority);
        sb.append("\n KeyMgmt:");
        for (int i3 = 0; i3 < this.allowedKeyManagement.size(); i3++) {
            if (this.allowedKeyManagement.get(i3)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i3 < KeyMgmt.strings.length) {
                    sb.append(KeyMgmt.strings[i3]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append(" Protocols:");
        for (int i4 = 0; i4 < this.allowedProtocols.size(); i4++) {
            if (this.allowedProtocols.get(i4)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i4 < Protocol.strings.length) {
                    sb.append(Protocol.strings[i4]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n AuthAlgorithms:");
        for (int i5 = 0; i5 < this.allowedAuthAlgorithms.size(); i5++) {
            if (this.allowedAuthAlgorithms.get(i5)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i5 < AuthAlgorithm.strings.length) {
                    sb.append(AuthAlgorithm.strings[i5]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n PairwiseCiphers:");
        for (int i6 = 0; i6 < this.allowedPairwiseCiphers.size(); i6++) {
            if (this.allowedPairwiseCiphers.get(i6)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i6 < PairwiseCipher.strings.length) {
                    sb.append(PairwiseCipher.strings[i6]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n GroupCiphers:");
        for (int i7 = 0; i7 < this.allowedGroupCiphers.size(); i7++) {
            if (this.allowedGroupCiphers.get(i7)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i7 < GroupCipher.strings.length) {
                    sb.append(GroupCipher.strings[i7]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n GroupMgmtCiphers:");
        for (int i8 = 0; i8 < this.allowedGroupManagementCiphers.size(); i8++) {
            if (this.allowedGroupManagementCiphers.get(i8)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i8 < GroupMgmtCipher.strings.length) {
                    sb.append(GroupMgmtCipher.strings[i8]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n SuiteBCiphers:");
        for (int i9 = 0; i9 < this.allowedSuiteBCiphers.size(); i9++) {
            if (this.allowedSuiteBCiphers.get(i9)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i9 < SuiteBCipher.strings.length) {
                    sb.append(SuiteBCipher.strings[i9]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n PSK/SAE: ");
        if (this.preSharedKey != null) {
            sb.append('*');
        }
        sb.append("\nSecurityParams List:\n");
        this.mSecurityParamsList.stream().forEach(new WifiConfiguration$$ExternalSyntheticLambda12(sb));
        sb.append("\nEnterprise config:\n");
        sb.append((Object) this.enterpriseConfig);
        sb.append("IP config:\n");
        sb.append(this.mIpConfiguration.toString());
        if (this.mNetworkSelectionStatus.getNetworkSelectionBSSID() != null) {
            sb.append(" networkSelectionBSSID=" + this.mNetworkSelectionStatus.getNetworkSelectionBSSID());
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mNetworkSelectionStatus.getDisableTime() != -1) {
            sb.append(10);
            long disableTime = elapsedRealtime - this.mNetworkSelectionStatus.getDisableTime();
            if (disableTime <= 0) {
                sb.append(" blackListed since <incorrect>");
            } else {
                sb.append(" blackListed: ");
                sb.append(Long.toString(disableTime / 1000));
                sb.append("sec ");
            }
        }
        if (this.creatorUid != 0) {
            sb.append(" cuid=" + this.creatorUid);
        }
        if (this.creatorName != null) {
            sb.append(" cname=" + this.creatorName);
        }
        if (this.lastUpdateUid != 0) {
            sb.append(" luid=" + this.lastUpdateUid);
        }
        if (this.lastUpdateName != null) {
            sb.append(" lname=" + this.lastUpdateName);
        }
        if (this.updateIdentifier != null) {
            sb.append(" updateIdentifier=" + this.updateIdentifier);
        }
        sb.append(" lcuid=" + this.lastConnectUid);
        sb.append(" allowAutojoin=" + this.allowAutojoin);
        sb.append(" noInternetAccessExpected=" + this.noInternetAccessExpected);
        sb.append(" mostRecentlyConnected=" + this.isMostRecentlyConnected);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (this.lastConnected != 0) {
            sb.append(10);
            sb.append("lastConnected: ");
            sb.append(logTimeOfDay(this.lastConnected));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        sb.append(10);
        if (this.lastUpdated != 0) {
            sb.append(10);
            sb.append("lastUpdated: ");
            sb.append(logTimeOfDay(this.lastUpdated));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        sb.append(10);
        sb.append("numRebootsSinceLastUse: ");
        sb.append(this.numRebootsSinceLastUse);
        sb.append(10);
        HashMap<String, Integer> hashMap = this.linkedConfigurations;
        if (hashMap != null) {
            for (String append : hashMap.keySet()) {
                sb.append(" linked: ");
                sb.append(append);
                sb.append(10);
            }
        }
        sb.append("recentFailure: ");
        sb.append("Association Rejection code: ");
        sb.append(this.recentFailure.getAssociationStatus());
        sb.append(", last update time: ");
        sb.append(this.recentFailure.getLastUpdateTimeSinceBootMillis());
        sb.append("\n");
        if (this.mBssidAllowlist != null) {
            sb.append("bssidAllowList: [");
            for (MacAddress macAddress : this.mBssidAllowlist) {
                sb.append(macAddress + ", ");
            }
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        } else {
            sb.append("bssidAllowlist unset");
        }
        sb.append("\n");
        sb.append("IsDppConfigurator: ");
        sb.append(this.mIsDppConfigurator);
        sb.append("\n");
        return sb.toString();
    }

    @SystemApi
    public String getPrintableSsid() {
        return WifiInfo.removeDoubleQuotes(this.SSID);
    }

    public String getKeyIdForCredentials(WifiConfiguration wifiConfiguration) {
        try {
            if (TextUtils.isEmpty(this.SSID)) {
                this.SSID = wifiConfiguration.SSID;
            }
            if (this.allowedKeyManagement.cardinality() == 0) {
                this.allowedKeyManagement = wifiConfiguration.allowedKeyManagement;
            }
            String str = "";
            if (this.allowedKeyManagement.get(2)) {
                str = str + KeyMgmt.strings[2];
            }
            if (this.allowedKeyManagement.get(5)) {
                str = str + KeyMgmt.strings[5];
            }
            if (this.allowedKeyManagement.get(3)) {
                str = str + KeyMgmt.strings[3];
            }
            if (this.allowedKeyManagement.get(10)) {
                str = str + KeyMgmt.strings[10];
            }
            if (this.allowedKeyManagement.get(14)) {
                str = str + KeyMgmt.strings[14];
            }
            if (!TextUtils.isEmpty(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append(trimStringForKeyId(this.SSID));
                sb.append(BaseLocale.SEP);
                sb.append(str);
                sb.append(BaseLocale.SEP);
                sb.append(trimStringForKeyId(this.enterpriseConfig.getKeyId(wifiConfiguration != null ? wifiConfiguration.enterpriseConfig : null)));
                String sb2 = sb.toString();
                if (!this.fromWifiNetworkSuggestion) {
                    return sb2;
                }
                return sb2 + BaseLocale.SEP + trimStringForKeyId(this.BSSID) + BaseLocale.SEP + trimStringForKeyId(this.creatorName);
            }
            throw new IllegalStateException("Not an EAP network");
        } catch (NullPointerException unused) {
            throw new IllegalStateException("Invalid config details");
        }
    }

    private String trimStringForKeyId(String str) {
        return str == null ? "" : str.replace((CharSequence) "\"", (CharSequence) "").replace((CharSequence) WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, (CharSequence) "");
    }

    /* access modifiers changed from: private */
    public static BitSet readBitSet(Parcel parcel) {
        int readInt = parcel.readInt();
        BitSet bitSet = new BitSet();
        for (int i = 0; i < readInt; i++) {
            bitSet.set(parcel.readInt());
        }
        return bitSet;
    }

    private static void writeBitSet(Parcel parcel, BitSet bitSet) {
        parcel.writeInt(bitSet.cardinality());
        int i = -1;
        while (true) {
            i = bitSet.nextSetBit(i + 1);
            if (i != -1) {
                parcel.writeInt(i);
            } else {
                return;
            }
        }
    }

    @SystemApi
    public int getAuthType() {
        if (this.allowedKeyManagement.cardinality() > 1) {
            if (this.allowedKeyManagement.get(2)) {
                if (this.allowedKeyManagement.cardinality() == 2 && this.allowedKeyManagement.get(3)) {
                    return 2;
                }
                if (this.allowedKeyManagement.cardinality() == 3 && this.allowedKeyManagement.get(3) && this.allowedKeyManagement.get(10)) {
                    return 10;
                }
            }
            throw new IllegalStateException("Invalid auth type set: " + this.allowedKeyManagement);
        } else if (this.allowedKeyManagement.get(1)) {
            return 1;
        } else {
            if (this.allowedKeyManagement.get(4)) {
                return 4;
            }
            if (this.allowedKeyManagement.get(2)) {
                return 2;
            }
            if (this.allowedKeyManagement.get(3)) {
                return 3;
            }
            if (this.allowedKeyManagement.get(8)) {
                return 8;
            }
            if (this.allowedKeyManagement.get(9)) {
                return 9;
            }
            if (this.allowedKeyManagement.get(10)) {
                return 10;
            }
            if (this.allowedKeyManagement.get(13)) {
                return 13;
            }
            if (this.allowedKeyManagement.get(14)) {
                return 14;
            }
            return this.allowedKeyManagement.get(17) ? 17 : 0;
        }
    }

    public String getKey() {
        String str = this.mPasspointUniqueId;
        if (str != null) {
            return str;
        }
        String ssidAndSecurityTypeString = getSsidAndSecurityTypeString();
        if (this.shared) {
            return ssidAndSecurityTypeString;
        }
        return ssidAndSecurityTypeString + LanguageTag.SEP + UserHandle.getUserHandleForUid(this.creatorUid).getIdentifier();
    }

    public String getNetworkKey() {
        String str = this.mPasspointUniqueId;
        if (str != null) {
            return str;
        }
        String str2 = this.SSID + getDefaultSecurityType();
        if (this.shared) {
            return str2;
        }
        return str2 + LanguageTag.SEP + UserHandle.getUserHandleForUid(this.creatorUid).getIdentifier();
    }

    public String getSsidAndSecurityTypeString() {
        return this.SSID + getDefaultSecurityType();
    }

    @SystemApi
    public IpConfiguration getIpConfiguration() {
        return new IpConfiguration(this.mIpConfiguration);
    }

    public void setIpConfiguration(IpConfiguration ipConfiguration) {
        if (ipConfiguration == null) {
            ipConfiguration = new IpConfiguration();
        }
        this.mIpConfiguration = ipConfiguration;
    }

    public StaticIpConfiguration getStaticIpConfiguration() {
        return this.mIpConfiguration.getStaticIpConfiguration();
    }

    public void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this.mIpConfiguration.setStaticIpConfiguration(staticIpConfiguration);
    }

    public IpConfiguration.IpAssignment getIpAssignment() {
        return this.mIpConfiguration.getIpAssignment();
    }

    public void setIpAssignment(IpConfiguration.IpAssignment ipAssignment) {
        this.mIpConfiguration.setIpAssignment(ipAssignment);
    }

    public IpConfiguration.ProxySettings getProxySettings() {
        return this.mIpConfiguration.getProxySettings();
    }

    public void setProxySettings(IpConfiguration.ProxySettings proxySettings) {
        this.mIpConfiguration.setProxySettings(proxySettings);
    }

    public ProxyInfo getHttpProxy() {
        if (this.mIpConfiguration.getProxySettings() == IpConfiguration.ProxySettings.NONE) {
            return null;
        }
        return new ProxyInfo(this.mIpConfiguration.getHttpProxy());
    }

    public void setHttpProxy(ProxyInfo proxyInfo) {
        ProxyInfo proxyInfo2;
        IpConfiguration.ProxySettings proxySettings;
        if (proxyInfo == null) {
            this.mIpConfiguration.setProxySettings(IpConfiguration.ProxySettings.NONE);
            this.mIpConfiguration.setHttpProxy((ProxyInfo) null);
            return;
        }
        if (!Uri.EMPTY.equals(proxyInfo.getPacFileUrl())) {
            proxySettings = IpConfiguration.ProxySettings.PAC;
            proxyInfo2 = ProxyInfo.buildPacProxy(proxyInfo.getPacFileUrl(), proxyInfo.getPort());
        } else {
            proxySettings = IpConfiguration.ProxySettings.STATIC;
            String[] exclusionList = proxyInfo.getExclusionList();
            if (exclusionList == null) {
                exclusionList = new String[0];
            }
            proxyInfo2 = ProxyInfo.buildDirectProxy(proxyInfo.getHost(), proxyInfo.getPort(), Arrays.asList(exclusionList));
        }
        if (proxyInfo2.isValid()) {
            this.mIpConfiguration.setProxySettings(proxySettings);
            this.mIpConfiguration.setHttpProxy(proxyInfo2);
            return;
        }
        throw new IllegalArgumentException("Invalid ProxyInfo: " + proxyInfo2.toString());
    }

    public void setProxy(IpConfiguration.ProxySettings proxySettings, ProxyInfo proxyInfo) {
        this.mIpConfiguration.setProxySettings(proxySettings);
        this.mIpConfiguration.setHttpProxy(proxyInfo);
    }

    public void setPasspointManagementObjectTree(String str) {
        this.mPasspointManagementObjectTree = str;
    }

    public String getMoTree() {
        return this.mPasspointManagementObjectTree;
    }

    public WifiConfiguration(WifiConfiguration wifiConfiguration) {
        this.mSecurityParamsList = new ArrayList();
        int i = 0;
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.carrierId = -1;
        this.subscriptionId = -1;
        this.mSubscriptionGroup = null;
        this.allowAutojoin = true;
        this.meteredOverride = 0;
        this.macRandomizationSetting = 3;
        this.randomizedMacExpirationTimeMs = 0;
        this.randomizedMacLastModifiedTimeMs = 0;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        this.mPasspointUniqueId = null;
        this.isMostRecentlyConnected = false;
        if (wifiConfiguration != null) {
            this.networkId = wifiConfiguration.networkId;
            this.status = wifiConfiguration.status;
            this.SSID = wifiConfiguration.SSID;
            this.BSSID = wifiConfiguration.BSSID;
            this.FQDN = wifiConfiguration.FQDN;
            this.roamingConsortiumIds = (long[]) wifiConfiguration.roamingConsortiumIds.clone();
            this.providerFriendlyName = wifiConfiguration.providerFriendlyName;
            this.isHomeProviderNetwork = wifiConfiguration.isHomeProviderNetwork;
            this.preSharedKey = wifiConfiguration.preSharedKey;
            this.mNetworkSelectionStatus.copy(wifiConfiguration.getNetworkSelectionStatus());
            this.apBand = wifiConfiguration.apBand;
            this.apChannel = wifiConfiguration.apChannel;
            this.wepKeys = new String[4];
            while (true) {
                String[] strArr = this.wepKeys;
                if (i >= strArr.length) {
                    break;
                }
                strArr[i] = wifiConfiguration.wepKeys[i];
                i++;
            }
            this.wepTxKeyIndex = wifiConfiguration.wepTxKeyIndex;
            this.priority = wifiConfiguration.priority;
            this.mDeletionPriority = wifiConfiguration.mDeletionPriority;
            this.hiddenSSID = wifiConfiguration.hiddenSSID;
            this.allowedKeyManagement = (BitSet) wifiConfiguration.allowedKeyManagement.clone();
            this.allowedProtocols = (BitSet) wifiConfiguration.allowedProtocols.clone();
            this.allowedAuthAlgorithms = (BitSet) wifiConfiguration.allowedAuthAlgorithms.clone();
            this.allowedPairwiseCiphers = (BitSet) wifiConfiguration.allowedPairwiseCiphers.clone();
            this.allowedGroupCiphers = (BitSet) wifiConfiguration.allowedGroupCiphers.clone();
            this.allowedGroupManagementCiphers = (BitSet) wifiConfiguration.allowedGroupManagementCiphers.clone();
            this.allowedSuiteBCiphers = (BitSet) wifiConfiguration.allowedSuiteBCiphers.clone();
            this.mSecurityParamsList = (List) wifiConfiguration.mSecurityParamsList.stream().map(new WifiConfiguration$$ExternalSyntheticLambda10()).collect(Collectors.toList());
            this.enterpriseConfig = new WifiEnterpriseConfig(wifiConfiguration.enterpriseConfig);
            this.defaultGwMacAddress = wifiConfiguration.defaultGwMacAddress;
            this.mIpConfiguration = new IpConfiguration(wifiConfiguration.mIpConfiguration);
            HashMap<String, Integer> hashMap = wifiConfiguration.linkedConfigurations;
            if (hashMap != null && hashMap.size() > 0) {
                HashMap<String, Integer> hashMap2 = new HashMap<>();
                this.linkedConfigurations = hashMap2;
                hashMap2.putAll(wifiConfiguration.linkedConfigurations);
            }
            this.validatedInternetAccess = wifiConfiguration.validatedInternetAccess;
            this.isLegacyPasspointConfig = wifiConfiguration.isLegacyPasspointConfig;
            this.ephemeral = wifiConfiguration.ephemeral;
            this.osu = wifiConfiguration.osu;
            this.trusted = wifiConfiguration.trusted;
            this.restricted = wifiConfiguration.restricted;
            this.oemPaid = wifiConfiguration.oemPaid;
            this.oemPrivate = wifiConfiguration.oemPrivate;
            this.carrierMerged = wifiConfiguration.carrierMerged;
            this.fromWifiNetworkSuggestion = wifiConfiguration.fromWifiNetworkSuggestion;
            this.fromWifiNetworkSpecifier = wifiConfiguration.fromWifiNetworkSpecifier;
            this.dbsSecondaryInternet = wifiConfiguration.dbsSecondaryInternet;
            this.meteredHint = wifiConfiguration.meteredHint;
            this.mIsRepeaterEnabled = wifiConfiguration.mIsRepeaterEnabled;
            this.meteredOverride = wifiConfiguration.meteredOverride;
            this.useExternalScores = wifiConfiguration.useExternalScores;
            this.lastConnectUid = wifiConfiguration.lastConnectUid;
            this.lastUpdateUid = wifiConfiguration.lastUpdateUid;
            this.creatorUid = wifiConfiguration.creatorUid;
            this.creatorName = wifiConfiguration.creatorName;
            this.lastUpdateName = wifiConfiguration.lastUpdateName;
            this.peerWifiConfiguration = wifiConfiguration.peerWifiConfiguration;
            this.lastConnected = wifiConfiguration.lastConnected;
            this.lastDisconnected = wifiConfiguration.lastDisconnected;
            this.lastUpdated = wifiConfiguration.lastUpdated;
            this.numRebootsSinceLastUse = wifiConfiguration.numRebootsSinceLastUse;
            this.numScorerOverride = wifiConfiguration.numScorerOverride;
            this.numScorerOverrideAndSwitchedNetwork = wifiConfiguration.numScorerOverrideAndSwitchedNetwork;
            this.numAssociation = wifiConfiguration.numAssociation;
            this.allowAutojoin = wifiConfiguration.allowAutojoin;
            this.numNoInternetAccessReports = wifiConfiguration.numNoInternetAccessReports;
            this.noInternetAccessExpected = wifiConfiguration.noInternetAccessExpected;
            this.shared = wifiConfiguration.shared;
            this.recentFailure.setAssociationStatus(wifiConfiguration.recentFailure.getAssociationStatus(), wifiConfiguration.recentFailure.getLastUpdateTimeSinceBootMillis());
            this.mRandomizedMacAddress = wifiConfiguration.mRandomizedMacAddress;
            this.macRandomizationSetting = wifiConfiguration.macRandomizationSetting;
            this.randomizedMacExpirationTimeMs = wifiConfiguration.randomizedMacExpirationTimeMs;
            this.randomizedMacLastModifiedTimeMs = wifiConfiguration.randomizedMacLastModifiedTimeMs;
            this.requirePmf = wifiConfiguration.requirePmf;
            this.updateIdentifier = wifiConfiguration.updateIdentifier;
            this.carrierId = wifiConfiguration.carrierId;
            this.subscriptionId = wifiConfiguration.subscriptionId;
            this.mPasspointUniqueId = wifiConfiguration.mPasspointUniqueId;
            this.mSubscriptionGroup = wifiConfiguration.mSubscriptionGroup;
            if (wifiConfiguration.mBssidAllowlist != null) {
                this.mBssidAllowlist = new ArrayList(wifiConfiguration.mBssidAllowlist);
            } else {
                this.mBssidAllowlist = null;
            }
            this.mIsDppConfigurator = wifiConfiguration.mIsDppConfigurator;
            this.mDppPrivateEcKey = (byte[]) wifiConfiguration.mDppPrivateEcKey.clone();
            this.mDppConnector = (byte[]) wifiConfiguration.mDppConnector.clone();
            this.mDppCSignKey = (byte[]) wifiConfiguration.mDppCSignKey.clone();
            this.mDppNetAccessKey = (byte[]) wifiConfiguration.mDppNetAccessKey.clone();
        }
    }

    static /* synthetic */ SecurityParams lambda$new$23(SecurityParams securityParams) {
        return new SecurityParams(securityParams);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.networkId);
        parcel.writeInt(this.status);
        this.mNetworkSelectionStatus.writeToParcel(parcel, i);
        parcel.writeString(this.SSID);
        parcel.writeString(this.BSSID);
        parcel.writeInt(this.apBand);
        parcel.writeInt(this.apChannel);
        parcel.writeString(this.FQDN);
        parcel.writeString(this.providerFriendlyName);
        parcel.writeInt(this.isHomeProviderNetwork ? 1 : 0);
        parcel.writeInt(this.roamingConsortiumIds.length);
        for (long writeLong : this.roamingConsortiumIds) {
            parcel.writeLong(writeLong);
        }
        parcel.writeString(this.preSharedKey);
        for (String writeString : this.wepKeys) {
            parcel.writeString(writeString);
        }
        parcel.writeInt(this.wepTxKeyIndex);
        parcel.writeInt(this.priority);
        parcel.writeInt(this.mDeletionPriority);
        parcel.writeInt(this.hiddenSSID ? 1 : 0);
        parcel.writeInt(this.requirePmf ? 1 : 0);
        parcel.writeString(this.updateIdentifier);
        writeBitSet(parcel, this.allowedKeyManagement);
        writeBitSet(parcel, this.allowedProtocols);
        writeBitSet(parcel, this.allowedAuthAlgorithms);
        writeBitSet(parcel, this.allowedPairwiseCiphers);
        writeBitSet(parcel, this.allowedGroupCiphers);
        writeBitSet(parcel, this.allowedGroupManagementCiphers);
        writeBitSet(parcel, this.allowedSuiteBCiphers);
        parcel.writeInt(this.mSecurityParamsList.size());
        this.mSecurityParamsList.stream().forEach(new WifiConfiguration$$ExternalSyntheticLambda11(parcel, i));
        parcel.writeParcelable(this.enterpriseConfig, i);
        parcel.writeParcelable(this.mIpConfiguration, i);
        parcel.writeString(this.dhcpServer);
        parcel.writeString(this.defaultGwMacAddress);
        parcel.writeInt(this.validatedInternetAccess ? 1 : 0);
        parcel.writeInt(this.isLegacyPasspointConfig ? 1 : 0);
        parcel.writeInt(this.ephemeral ? 1 : 0);
        parcel.writeInt(this.trusted ? 1 : 0);
        parcel.writeInt(this.oemPaid ? 1 : 0);
        parcel.writeInt(this.oemPrivate ? 1 : 0);
        parcel.writeInt(this.carrierMerged ? 1 : 0);
        parcel.writeInt(this.fromWifiNetworkSuggestion ? 1 : 0);
        parcel.writeInt(this.fromWifiNetworkSpecifier ? 1 : 0);
        parcel.writeInt(this.dbsSecondaryInternet ? 1 : 0);
        parcel.writeInt(this.meteredHint ? 1 : 0);
        parcel.writeBoolean(this.mIsRepeaterEnabled);
        parcel.writeInt(this.meteredOverride);
        parcel.writeInt(this.useExternalScores ? 1 : 0);
        parcel.writeInt(this.creatorUid);
        parcel.writeInt(this.lastConnectUid);
        parcel.writeInt(this.lastUpdateUid);
        parcel.writeString(this.creatorName);
        parcel.writeString(this.lastUpdateName);
        parcel.writeInt(this.numScorerOverride);
        parcel.writeInt(this.numScorerOverrideAndSwitchedNetwork);
        parcel.writeInt(this.numAssociation);
        parcel.writeBoolean(this.allowAutojoin);
        parcel.writeInt(this.numNoInternetAccessReports);
        parcel.writeInt(this.noInternetAccessExpected ? 1 : 0);
        parcel.writeInt(this.shared ? 1 : 0);
        parcel.writeString(this.mPasspointManagementObjectTree);
        parcel.writeInt(this.recentFailure.getAssociationStatus());
        parcel.writeLong(this.recentFailure.getLastUpdateTimeSinceBootMillis());
        parcel.writeParcelable(this.mRandomizedMacAddress, i);
        parcel.writeInt(this.macRandomizationSetting);
        parcel.writeInt(this.osu ? 1 : 0);
        parcel.writeLong(this.randomizedMacExpirationTimeMs);
        parcel.writeLong(this.randomizedMacLastModifiedTimeMs);
        parcel.writeInt(this.carrierId);
        parcel.writeString(this.mPasspointUniqueId);
        parcel.writeInt(this.subscriptionId);
        parcel.writeBoolean(this.restricted);
        parcel.writeParcelable(this.mSubscriptionGroup, i);
        parcel.writeList(this.mBssidAllowlist);
        parcel.writeBoolean(this.mIsDppConfigurator);
        parcel.writeByteArray(this.mDppPrivateEcKey);
        parcel.writeByteArray(this.mDppConnector);
        parcel.writeByteArray(this.mDppCSignKey);
        parcel.writeByteArray(this.mDppNetAccessKey);
    }

    public void setPasspointUniqueId(String str) {
        this.mPasspointUniqueId = str;
    }

    public String getPasspointUniqueId() {
        return this.mPasspointUniqueId;
    }

    public boolean needsPreSharedKey() {
        return this.mSecurityParamsList.stream().anyMatch(new WifiConfiguration$$ExternalSyntheticLambda6());
    }

    static /* synthetic */ boolean lambda$needsPreSharedKey$25(SecurityParams securityParams) {
        return securityParams.isSecurityType(2) || securityParams.isSecurityType(4) || securityParams.isSecurityType(7);
    }

    @SystemApi
    public String getProfileKey() {
        if (!SdkLevel.isAtLeastS()) {
            return getKey();
        }
        String str = this.mPasspointUniqueId;
        if (str != null) {
            return str;
        }
        String str2 = this.SSID + getDefaultSecurityType();
        if (!this.shared) {
            str2 = str2 + LanguageTag.SEP + UserHandle.getUserHandleForUid(this.creatorUid).getIdentifier();
        }
        if (!this.fromWifiNetworkSuggestion) {
            return str2;
        }
        return str2 + BaseLocale.SEP + this.creatorName + LanguageTag.SEP + this.carrierId + LanguageTag.SEP + this.subscriptionId;
    }

    public String getDefaultSecurityType() {
        if (this.allowedKeyManagement.get(1)) {
            return KeyMgmt.strings[1];
        }
        if (!this.allowedKeyManagement.get(2) && !this.allowedKeyManagement.get(3)) {
            int i = this.wepTxKeyIndex;
            if (i >= 0) {
                String[] strArr = this.wepKeys;
                if (i < strArr.length && strArr[i] != null) {
                    return "WEP";
                }
            }
            if (this.allowedKeyManagement.get(9)) {
                return KeyMgmt.strings[9];
            }
            if (this.allowedKeyManagement.get(8)) {
                return KeyMgmt.strings[8];
            }
            if (this.allowedKeyManagement.get(10)) {
                return KeyMgmt.strings[10];
            }
            if (this.allowedKeyManagement.get(13)) {
                return KeyMgmt.strings[13];
            }
            if (this.allowedKeyManagement.get(14)) {
                return KeyMgmt.strings[14];
            }
            if (this.allowedKeyManagement.get(5)) {
                return KeyMgmt.strings[5];
            }
            if (this.allowedKeyManagement.get(17)) {
                return KeyMgmt.strings[17];
            }
            return KeyMgmt.strings[0];
        } else if (isWpa3EnterpriseConfiguration()) {
            return "WPA3_EAP";
        } else {
            return KeyMgmt.strings[2];
        }
    }

    public static String getSecurityTypeName(int i) {
        return (i < 0 || 13 < i) ? EnvironmentCompat.MEDIA_UNKNOWN : SECURITY_TYPE_NAMES[i];
    }

    public String getNetworkKeyFromSecurityType(int i) {
        if (this.mPasspointUniqueId != null) {
            return this.subscriptionId + LanguageTag.SEP + this.mPasspointUniqueId;
        }
        String str = this.SSID + getSecurityTypeName(i);
        if (!this.shared) {
            str = str + LanguageTag.SEP + UserHandle.getUserHandleForUid(this.creatorUid).getIdentifier();
        }
        if (!this.fromWifiNetworkSuggestion) {
            return str;
        }
        return str + BaseLocale.SEP + this.creatorName + LanguageTag.SEP + this.carrierId + LanguageTag.SEP + this.subscriptionId;
    }

    @SystemApi
    public Set<String> getAllNetworkKeys() {
        HashSet hashSet = new HashSet();
        for (SecurityParams securityType : this.mSecurityParamsList) {
            hashSet.add(getNetworkKeyFromSecurityType(securityType.getSecurityType()));
        }
        return hashSet;
    }

    public void setSubscriptionGroup(ParcelUuid parcelUuid) {
        this.mSubscriptionGroup = parcelUuid;
    }

    public ParcelUuid getSubscriptionGroup() {
        return this.mSubscriptionGroup;
    }
}
