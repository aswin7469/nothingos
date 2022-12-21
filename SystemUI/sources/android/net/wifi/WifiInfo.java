package android.net.wifi;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.NetworkInfo;
import android.net.TransportInfo;
import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import com.android.wifi.p018x.com.android.net.module.util.Inet4AddressUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WifiInfo implements TransportInfo, Parcelable {
    public static final Parcelable.Creator<WifiInfo> CREATOR = new Parcelable.Creator<WifiInfo>() {
        public WifiInfo createFromParcel(Parcel parcel) {
            WifiInfo wifiInfo = new WifiInfo();
            wifiInfo.setNetworkId(parcel.readInt());
            wifiInfo.setRssi(parcel.readInt());
            wifiInfo.setLinkSpeed(parcel.readInt());
            wifiInfo.setTxLinkSpeedMbps(parcel.readInt());
            wifiInfo.setRxLinkSpeedMbps(parcel.readInt());
            wifiInfo.setFrequency(parcel.readInt());
            boolean z = true;
            if (parcel.readByte() == 1) {
                try {
                    wifiInfo.setInetAddress(InetAddress.getByAddress(parcel.createByteArray()));
                } catch (UnknownHostException unused) {
                }
            }
            if (parcel.readInt() == 1) {
                wifiInfo.mWifiSsid = WifiSsid.CREATOR.createFromParcel(parcel);
            }
            wifiInfo.mBSSID = parcel.readString();
            wifiInfo.mMacAddress = parcel.readString();
            wifiInfo.mMeteredHint = parcel.readInt() != 0;
            wifiInfo.mEphemeral = parcel.readInt() != 0;
            wifiInfo.mTrusted = parcel.readInt() != 0;
            wifiInfo.mOemPaid = parcel.readInt() != 0;
            wifiInfo.mOemPrivate = parcel.readInt() != 0;
            wifiInfo.mCarrierMerged = parcel.readInt() != 0;
            wifiInfo.score = parcel.readInt();
            wifiInfo.mIsUsable = parcel.readBoolean();
            wifiInfo.txSuccess = parcel.readLong();
            wifiInfo.mSuccessfulTxPacketsPerSecond = parcel.readDouble();
            wifiInfo.txRetries = parcel.readLong();
            wifiInfo.mTxRetriedTxPacketsPerSecond = parcel.readDouble();
            wifiInfo.txBad = parcel.readLong();
            wifiInfo.mLostTxPacketsPerSecond = parcel.readDouble();
            wifiInfo.rxSuccess = parcel.readLong();
            wifiInfo.mSuccessfulRxPacketsPerSecond = parcel.readDouble();
            wifiInfo.mSupplicantState = SupplicantState.CREATOR.createFromParcel(parcel);
            wifiInfo.mOsuAp = parcel.readInt() != 0;
            wifiInfo.mRequestingPackageName = parcel.readString();
            wifiInfo.mFqdn = parcel.readString();
            wifiInfo.mProviderFriendlyName = parcel.readString();
            wifiInfo.mWifiStandard = parcel.readInt();
            wifiInfo.mMaxSupportedTxLinkSpeed = parcel.readInt();
            wifiInfo.mMaxSupportedRxLinkSpeed = parcel.readInt();
            wifiInfo.mPasspointUniqueId = parcel.readString();
            wifiInfo.mSubscriptionId = parcel.readInt();
            wifiInfo.mInformationElements = parcel.createTypedArrayList(ScanResult.InformationElement.CREATOR);
            if (SdkLevel.isAtLeastS()) {
                wifiInfo.mIsPrimary = parcel.readInt();
            }
            wifiInfo.mSecurityType = parcel.readInt();
            if (parcel.readInt() == 0) {
                z = false;
            }
            wifiInfo.mRestricted = z;
            wifiInfo.mNetworkKey = parcel.readString();
            wifiInfo.mApMldMacAddress = (MacAddress) parcel.readParcelable(MacAddress.class.getClassLoader());
            wifiInfo.mApMloLinkId = parcel.readInt();
            wifiInfo.mAffiliatedMloLinks = parcel.createTypedArrayList(MloLink.CREATOR);
            return wifiInfo;
        }

        public WifiInfo[] newArray(int i) {
            return new WifiInfo[i];
        }
    };
    @SystemApi
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final int DPM_SECURITY_TYPE_UNKNOWN = -1;
    public static final String FREQUENCY_UNITS = "MHz";
    @SystemApi
    public static final int INVALID_RSSI = -127;
    private static final int IS_PRIMARY_FALSE = 0;
    private static final int IS_PRIMARY_NO_PERMISSION = -1;
    private static final int IS_PRIMARY_TRUE = 1;
    public static final String LINK_SPEED_UNITS = "Mbps";
    public static final int LINK_SPEED_UNKNOWN = -1;
    public static final int MAX_RSSI = 200;
    public static final int MIN_RSSI = -126;
    public static final int SECURITY_TYPE_DPP = 13;
    public static final int SECURITY_TYPE_EAP = 3;
    public static final int SECURITY_TYPE_EAP_WPA3_ENTERPRISE = 9;
    public static final int SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT = 5;
    public static final int SECURITY_TYPE_OPEN = 0;
    public static final int SECURITY_TYPE_OSEN = 10;
    public static final int SECURITY_TYPE_OWE = 6;
    public static final int SECURITY_TYPE_PASSPOINT_R1_R2 = 11;
    public static final int SECURITY_TYPE_PASSPOINT_R3 = 12;
    public static final int SECURITY_TYPE_PSK = 2;
    public static final int SECURITY_TYPE_SAE = 4;
    public static final int SECURITY_TYPE_UNKNOWN = -1;
    public static final int SECURITY_TYPE_WAPI_CERT = 8;
    public static final int SECURITY_TYPE_WAPI_PSK = 7;
    public static final int SECURITY_TYPE_WEP = 1;
    private static final String TAG = "WifiInfo";
    private static final EnumMap<SupplicantState, NetworkInfo.DetailedState> stateMap;
    /* access modifiers changed from: private */
    public List<MloLink> mAffiliatedMloLinks;
    /* access modifiers changed from: private */
    public MacAddress mApMldMacAddress;
    /* access modifiers changed from: private */
    public int mApMloLinkId;
    /* access modifiers changed from: private */
    public String mBSSID;
    /* access modifiers changed from: private */
    public boolean mCarrierMerged;
    /* access modifiers changed from: private */
    public boolean mEphemeral;
    /* access modifiers changed from: private */
    public String mFqdn;
    private int mFrequency;
    /* access modifiers changed from: private */
    public List<ScanResult.InformationElement> mInformationElements;
    private InetAddress mIpAddress;
    private boolean mIsHiddenSsid;
    /* access modifiers changed from: private */
    public int mIsPrimary;
    /* access modifiers changed from: private */
    public boolean mIsUsable;
    private int mLinkSpeed;
    /* access modifiers changed from: private */
    public double mLostTxPacketsPerSecond;
    /* access modifiers changed from: private */
    public String mMacAddress;
    /* access modifiers changed from: private */
    public int mMaxSupportedRxLinkSpeed;
    /* access modifiers changed from: private */
    public int mMaxSupportedTxLinkSpeed;
    /* access modifiers changed from: private */
    public boolean mMeteredHint;
    private int mNetworkId;
    /* access modifiers changed from: private */
    public String mNetworkKey;
    /* access modifiers changed from: private */
    public boolean mOemPaid;
    /* access modifiers changed from: private */
    public boolean mOemPrivate;
    /* access modifiers changed from: private */
    public boolean mOsuAp;
    /* access modifiers changed from: private */
    public String mPasspointUniqueId;
    /* access modifiers changed from: private */
    public String mProviderFriendlyName;
    /* access modifiers changed from: private */
    public String mRequestingPackageName;
    /* access modifiers changed from: private */
    public boolean mRestricted;
    private int mRssi;
    private int mRxLinkSpeed;
    /* access modifiers changed from: private */
    public int mSecurityType;
    /* access modifiers changed from: private */
    public int mSubscriptionId;
    /* access modifiers changed from: private */
    public double mSuccessfulRxPacketsPerSecond;
    /* access modifiers changed from: private */
    public double mSuccessfulTxPacketsPerSecond;
    /* access modifiers changed from: private */
    public SupplicantState mSupplicantState;
    /* access modifiers changed from: private */
    public boolean mTrusted;
    private int mTxLinkSpeed;
    /* access modifiers changed from: private */
    public double mTxRetriedTxPacketsPerSecond;
    /* access modifiers changed from: private */
    public WifiSsid mWifiSsid;
    /* access modifiers changed from: private */
    public int mWifiStandard;
    public long rxSuccess;
    public int score;
    public long txBad;
    public long txRetries;
    public long txSuccess;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IsPrimaryValues {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType {
    }

    public static int convertSecurityTypeToDpmWifiSecurity(int i) {
        switch (i) {
            case 0:
            case 6:
                return 0;
            case 1:
            case 2:
            case 4:
            case 7:
                return 1;
            case 3:
            case 8:
            case 9:
            case 11:
            case 12:
                return 2;
            case 5:
                return 3;
            default:
                return -1;
        }
    }

    public static int convertWifiConfigurationSecurityType(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            default:
                return -1;
        }
    }

    private boolean shouldRedactLocalMacAddressFields(long j) {
        return (j & 2) != 0;
    }

    private boolean shouldRedactLocationSensitiveFields(long j) {
        return (j & 1) != 0;
    }

    private boolean shouldRedactNetworkSettingsFields(long j) {
        return (j & 4) != 0;
    }

    public int describeContents() {
        return 0;
    }

    public long getApplicableRedactions() {
        return 7;
    }

    static {
        EnumMap<SupplicantState, NetworkInfo.DetailedState> enumMap = new EnumMap<>(SupplicantState.class);
        stateMap = enumMap;
        enumMap.put(SupplicantState.DISCONNECTED, NetworkInfo.DetailedState.DISCONNECTED);
        enumMap.put(SupplicantState.INTERFACE_DISABLED, NetworkInfo.DetailedState.DISCONNECTED);
        enumMap.put(SupplicantState.INACTIVE, NetworkInfo.DetailedState.IDLE);
        enumMap.put(SupplicantState.SCANNING, NetworkInfo.DetailedState.SCANNING);
        enumMap.put(SupplicantState.AUTHENTICATING, NetworkInfo.DetailedState.CONNECTING);
        enumMap.put(SupplicantState.ASSOCIATING, NetworkInfo.DetailedState.CONNECTING);
        enumMap.put(SupplicantState.ASSOCIATED, NetworkInfo.DetailedState.CONNECTING);
        enumMap.put(SupplicantState.FOUR_WAY_HANDSHAKE, NetworkInfo.DetailedState.AUTHENTICATING);
        enumMap.put(SupplicantState.GROUP_HANDSHAKE, NetworkInfo.DetailedState.AUTHENTICATING);
        enumMap.put(SupplicantState.COMPLETED, NetworkInfo.DetailedState.OBTAINING_IPADDR);
        enumMap.put(SupplicantState.DORMANT, NetworkInfo.DetailedState.DISCONNECTED);
        enumMap.put(SupplicantState.UNINITIALIZED, NetworkInfo.DetailedState.IDLE);
        enumMap.put(SupplicantState.INVALID, NetworkInfo.DetailedState.FAILED);
    }

    @SystemApi
    public double getLostTxPacketsPerSecond() {
        return this.mLostTxPacketsPerSecond;
    }

    public void setLostTxPacketsPerSecond(double d) {
        this.mLostTxPacketsPerSecond = d;
    }

    @SystemApi
    public double getRetriedTxPacketsPerSecond() {
        return this.mTxRetriedTxPacketsPerSecond;
    }

    public void setRetriedTxPacketsRate(double d) {
        this.mTxRetriedTxPacketsPerSecond = d;
    }

    @SystemApi
    public double getSuccessfulTxPacketsPerSecond() {
        return this.mSuccessfulTxPacketsPerSecond;
    }

    public void setSuccessfulTxPacketsPerSecond(double d) {
        this.mSuccessfulTxPacketsPerSecond = d;
    }

    @SystemApi
    public double getSuccessfulRxPacketsPerSecond() {
        return this.mSuccessfulRxPacketsPerSecond;
    }

    public void setSuccessfulRxPacketsPerSecond(double d) {
        this.mSuccessfulRxPacketsPerSecond = d;
    }

    @SystemApi
    public int getScore() {
        return this.score;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public boolean isUsable() {
        return this.mIsUsable;
    }

    public void setUsable(boolean z) {
        this.mIsUsable = z;
    }

    public WifiInfo() {
        this.mIsHiddenSsid = false;
        this.mMacAddress = DEFAULT_MAC_ADDRESS;
        this.mIsUsable = true;
        this.mWifiSsid = null;
        this.mBSSID = null;
        this.mApMldMacAddress = null;
        this.mApMloLinkId = 0;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mNetworkId = -1;
        this.mSupplicantState = SupplicantState.UNINITIALIZED;
        this.mRssi = -127;
        this.mLinkSpeed = -1;
        this.mFrequency = -1;
        this.mSubscriptionId = -1;
        this.mSecurityType = -1;
        this.mIsPrimary = 0;
        this.mNetworkKey = null;
        this.mApMloLinkId = -1;
    }

    public void reset() {
        setInetAddress((InetAddress) null);
        setBSSID((String) null);
        setSSID((WifiSsid) null);
        setHiddenSSID(false);
        setNetworkId(-1);
        setRssi(-127);
        setLinkSpeed(-1);
        setTxLinkSpeedMbps(-1);
        setRxLinkSpeedMbps(-1);
        setMaxSupportedTxLinkSpeedMbps(-1);
        setMaxSupportedRxLinkSpeedMbps(-1);
        setFrequency(-1);
        setMeteredHint(false);
        setEphemeral(false);
        setTrusted(false);
        setOemPaid(false);
        setOemPrivate(false);
        setCarrierMerged(false);
        setOsuAp(false);
        setRequestingPackageName((String) null);
        setFQDN((String) null);
        setProviderFriendlyName((String) null);
        setPasspointUniqueId((String) null);
        setSubscriptionId(-1);
        setInformationElements((List<ScanResult.InformationElement>) null);
        setIsPrimary(false);
        setRestricted(false);
        this.txBad = 0;
        this.txSuccess = 0;
        this.rxSuccess = 0;
        this.txRetries = 0;
        this.mLostTxPacketsPerSecond = 0.0d;
        this.mSuccessfulTxPacketsPerSecond = 0.0d;
        this.mSuccessfulRxPacketsPerSecond = 0.0d;
        this.mTxRetriedTxPacketsPerSecond = 0.0d;
        this.score = 0;
        this.mIsUsable = true;
        this.mSecurityType = -1;
        this.mNetworkKey = null;
        resetMultiLinkInfo();
    }

    public void resetMultiLinkInfo() {
        setApMldMacAddress((MacAddress) null);
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
    }

    public WifiInfo(WifiInfo wifiInfo) {
        this(wifiInfo, 0);
    }

    private WifiInfo(WifiInfo wifiInfo, long j) {
        String str;
        MacAddress macAddress;
        WifiSsid wifiSsid;
        int i;
        String str2;
        String str3;
        String str4;
        this.mIsHiddenSsid = false;
        String str5 = DEFAULT_MAC_ADDRESS;
        this.mMacAddress = str5;
        this.mIsUsable = true;
        if (wifiInfo != null) {
            this.mSupplicantState = wifiInfo.mSupplicantState;
            if (shouldRedactLocationSensitiveFields(j)) {
                str = str5;
            } else {
                str = wifiInfo.mBSSID;
            }
            this.mBSSID = str;
            String str6 = null;
            if (shouldRedactLocationSensitiveFields(j)) {
                macAddress = null;
            } else {
                macAddress = wifiInfo.mApMldMacAddress;
            }
            this.mApMldMacAddress = macAddress;
            this.mApMloLinkId = wifiInfo.mApMloLinkId;
            if (wifiInfo.mApMldMacAddress != null) {
                this.mAffiliatedMloLinks = new ArrayList();
                for (MloLink mloLink : wifiInfo.mAffiliatedMloLinks) {
                    this.mAffiliatedMloLinks.add(new MloLink(mloLink, j));
                }
            } else {
                this.mAffiliatedMloLinks = Collections.emptyList();
            }
            if (shouldRedactLocationSensitiveFields(j)) {
                wifiSsid = null;
            } else {
                wifiSsid = wifiInfo.mWifiSsid;
            }
            this.mWifiSsid = wifiSsid;
            int i2 = -1;
            if (shouldRedactLocationSensitiveFields(j)) {
                i = -1;
            } else {
                i = wifiInfo.mNetworkId;
            }
            this.mNetworkId = i;
            this.mRssi = wifiInfo.mRssi;
            this.mLinkSpeed = wifiInfo.mLinkSpeed;
            this.mTxLinkSpeed = wifiInfo.mTxLinkSpeed;
            this.mRxLinkSpeed = wifiInfo.mRxLinkSpeed;
            this.mFrequency = wifiInfo.mFrequency;
            this.mIpAddress = wifiInfo.mIpAddress;
            if (!shouldRedactLocalMacAddressFields(j) && !shouldRedactLocationSensitiveFields(j)) {
                str5 = wifiInfo.mMacAddress;
            }
            this.mMacAddress = str5;
            this.mMeteredHint = wifiInfo.mMeteredHint;
            this.mEphemeral = wifiInfo.mEphemeral;
            this.mTrusted = wifiInfo.mTrusted;
            this.mRestricted = wifiInfo.mRestricted;
            this.mOemPaid = wifiInfo.mOemPaid;
            this.mOemPrivate = wifiInfo.mOemPrivate;
            this.mCarrierMerged = wifiInfo.mCarrierMerged;
            this.mRequestingPackageName = wifiInfo.mRequestingPackageName;
            this.mOsuAp = wifiInfo.mOsuAp;
            if (shouldRedactLocationSensitiveFields(j)) {
                str2 = null;
            } else {
                str2 = wifiInfo.mFqdn;
            }
            this.mFqdn = str2;
            if (shouldRedactLocationSensitiveFields(j)) {
                str3 = null;
            } else {
                str3 = wifiInfo.mProviderFriendlyName;
            }
            this.mProviderFriendlyName = str3;
            this.mSubscriptionId = wifiInfo.mSubscriptionId;
            this.txBad = wifiInfo.txBad;
            this.txRetries = wifiInfo.txRetries;
            this.txSuccess = wifiInfo.txSuccess;
            this.rxSuccess = wifiInfo.rxSuccess;
            this.mLostTxPacketsPerSecond = wifiInfo.mLostTxPacketsPerSecond;
            this.mTxRetriedTxPacketsPerSecond = wifiInfo.mTxRetriedTxPacketsPerSecond;
            this.mSuccessfulTxPacketsPerSecond = wifiInfo.mSuccessfulTxPacketsPerSecond;
            this.mSuccessfulRxPacketsPerSecond = wifiInfo.mSuccessfulRxPacketsPerSecond;
            this.score = wifiInfo.score;
            this.mIsUsable = wifiInfo.mIsUsable;
            this.mWifiStandard = wifiInfo.mWifiStandard;
            this.mMaxSupportedTxLinkSpeed = wifiInfo.mMaxSupportedTxLinkSpeed;
            this.mMaxSupportedRxLinkSpeed = wifiInfo.mMaxSupportedRxLinkSpeed;
            if (shouldRedactLocationSensitiveFields(j)) {
                str4 = null;
            } else {
                str4 = wifiInfo.mPasspointUniqueId;
            }
            this.mPasspointUniqueId = str4;
            if (wifiInfo.mInformationElements != null && !shouldRedactLocationSensitiveFields(j)) {
                this.mInformationElements = new ArrayList(wifiInfo.mInformationElements);
            }
            this.mIsPrimary = !shouldRedactNetworkSettingsFields(j) ? wifiInfo.mIsPrimary : i2;
            this.mSecurityType = wifiInfo.mSecurityType;
            this.mNetworkKey = !shouldRedactLocationSensitiveFields(j) ? wifiInfo.mNetworkKey : str6;
        }
    }

    public static final class Builder {
        private final WifiInfo mWifiInfo = new WifiInfo();

        public Builder setSsid(byte[] bArr) {
            this.mWifiInfo.setSSID(WifiSsid.fromBytes(bArr));
            return this;
        }

        public Builder setBssid(String str) {
            this.mWifiInfo.setBSSID(str);
            return this;
        }

        public Builder setApMldMacAddress(MacAddress macAddress) {
            this.mWifiInfo.setApMldMacAddress(macAddress);
            return this;
        }

        public Builder setApMloLinkId(int i) {
            this.mWifiInfo.setApMloLinkId(i);
            return this;
        }

        public Builder setAffiliatedMloLinks(List<MloLink> list) {
            this.mWifiInfo.setAffiliatedMloLinks(list);
            return this;
        }

        public Builder setRssi(int i) {
            this.mWifiInfo.setRssi(i);
            return this;
        }

        public Builder setNetworkId(int i) {
            this.mWifiInfo.setNetworkId(i);
            return this;
        }

        public Builder setCurrentSecurityType(int i) {
            this.mWifiInfo.setCurrentSecurityType(i);
            return this;
        }

        public WifiInfo build() {
            return new WifiInfo(this.mWifiInfo);
        }
    }

    public void setSSID(WifiSsid wifiSsid) {
        this.mWifiSsid = wifiSsid;
    }

    public String getSSID() {
        WifiSsid wifiSsid = this.mWifiSsid;
        if (wifiSsid == null) {
            return "<unknown ssid>";
        }
        String wifiSsid2 = wifiSsid.toString();
        return !TextUtils.isEmpty(wifiSsid2) ? wifiSsid2 : "<unknown ssid>";
    }

    public WifiSsid getWifiSsid() {
        return this.mWifiSsid;
    }

    public void setBSSID(String str) {
        this.mBSSID = str;
    }

    public void setApMldMacAddress(MacAddress macAddress) {
        this.mApMldMacAddress = macAddress;
    }

    public void setApMloLinkId(int i) {
        this.mApMloLinkId = i;
    }

    public void setAffiliatedMloLinks(List<MloLink> list) {
        this.mAffiliatedMloLinks = new ArrayList(list);
    }

    public boolean updateMloLinkStaAddress(int i, MacAddress macAddress) {
        for (MloLink next : this.mAffiliatedMloLinks) {
            if (next.getLinkId() == i) {
                next.setStaMacAddress(macAddress);
                return true;
            }
        }
        return false;
    }

    public boolean updateMloLinkState(int i, int i2) {
        if (!MloLink.isValidState(i2)) {
            return false;
        }
        for (MloLink next : this.mAffiliatedMloLinks) {
            if (next.getLinkId() == i) {
                next.setState(i2);
                return true;
            }
        }
        return false;
    }

    public String getBSSID() {
        return this.mBSSID;
    }

    public MacAddress getApMldMacAddress() {
        return this.mApMldMacAddress;
    }

    public int getApMloLinkId() {
        return this.mApMloLinkId;
    }

    public List<MloLink> getAffiliatedMloLinks() {
        return new ArrayList(this.mAffiliatedMloLinks);
    }

    public int getRssi() {
        return this.mRssi;
    }

    public void setRssi(int i) {
        if (i < -127) {
            i = -127;
        }
        if (i > 200) {
            i = 200;
        }
        this.mRssi = i;
    }

    public void setWifiStandard(int i) {
        this.mWifiStandard = i;
    }

    public int getWifiStandard() {
        return this.mWifiStandard;
    }

    public int getLinkSpeed() {
        return this.mLinkSpeed;
    }

    public void setLinkSpeed(int i) {
        this.mLinkSpeed = i;
    }

    public int getTxLinkSpeedMbps() {
        return this.mTxLinkSpeed;
    }

    public int getMaxSupportedTxLinkSpeedMbps() {
        return this.mMaxSupportedTxLinkSpeed;
    }

    public void setTxLinkSpeedMbps(int i) {
        this.mTxLinkSpeed = i;
    }

    public void setMaxSupportedTxLinkSpeedMbps(int i) {
        this.mMaxSupportedTxLinkSpeed = i;
    }

    public int getRxLinkSpeedMbps() {
        return this.mRxLinkSpeed;
    }

    public int getMaxSupportedRxLinkSpeedMbps() {
        return this.mMaxSupportedRxLinkSpeed;
    }

    public void setRxLinkSpeedMbps(int i) {
        this.mRxLinkSpeed = i;
    }

    public void setMaxSupportedRxLinkSpeedMbps(int i) {
        this.mMaxSupportedRxLinkSpeed = i;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int i) {
        this.mFrequency = i;
    }

    public boolean is24GHz() {
        return ScanResult.is24GHz(this.mFrequency);
    }

    public boolean is5GHz() {
        return ScanResult.is5GHz(this.mFrequency);
    }

    public boolean is6GHz() {
        return ScanResult.is6GHz(this.mFrequency);
    }

    public void setMacAddress(String str) {
        this.mMacAddress = str;
    }

    public String getMacAddress() {
        return this.mMacAddress;
    }

    public boolean hasRealMacAddress() {
        String str = this.mMacAddress;
        return str != null && !DEFAULT_MAC_ADDRESS.equals(str);
    }

    public void setMeteredHint(boolean z) {
        this.mMeteredHint = z;
    }

    public boolean getMeteredHint() {
        return this.mMeteredHint;
    }

    public void setEphemeral(boolean z) {
        this.mEphemeral = z;
    }

    @SystemApi
    public boolean isEphemeral() {
        return this.mEphemeral;
    }

    public void setTrusted(boolean z) {
        this.mTrusted = z;
    }

    @SystemApi
    public boolean isTrusted() {
        return this.mTrusted;
    }

    public void setRestricted(boolean z) {
        this.mRestricted = z;
    }

    public boolean isRestricted() {
        return this.mRestricted;
    }

    public void setOemPaid(boolean z) {
        this.mOemPaid = z;
    }

    @SystemApi
    public boolean isOemPaid() {
        if (SdkLevel.isAtLeastS()) {
            return this.mOemPaid;
        }
        throw new UnsupportedOperationException();
    }

    public void setOemPrivate(boolean z) {
        this.mOemPrivate = z;
    }

    @SystemApi
    public boolean isOemPrivate() {
        if (SdkLevel.isAtLeastS()) {
            return this.mOemPrivate;
        }
        throw new UnsupportedOperationException();
    }

    public void setCarrierMerged(boolean z) {
        this.mCarrierMerged = z;
    }

    @SystemApi
    public boolean isCarrierMerged() {
        if (SdkLevel.isAtLeastS()) {
            return this.mCarrierMerged;
        }
        throw new UnsupportedOperationException();
    }

    public void setOsuAp(boolean z) {
        this.mOsuAp = z;
    }

    @SystemApi
    public boolean isOsuAp() {
        return this.mOsuAp;
    }

    @SystemApi
    public boolean isPasspointAp() {
        return (this.mFqdn == null || this.mProviderFriendlyName == null) ? false : true;
    }

    public void setFQDN(String str) {
        this.mFqdn = str;
    }

    public String getPasspointFqdn() {
        return this.mFqdn;
    }

    public void setProviderFriendlyName(String str) {
        this.mProviderFriendlyName = str;
    }

    public String getPasspointProviderFriendlyName() {
        return this.mProviderFriendlyName;
    }

    public void setRequestingPackageName(String str) {
        this.mRequestingPackageName = str;
    }

    @SystemApi
    public String getRequestingPackageName() {
        return this.mRequestingPackageName;
    }

    public void setSubscriptionId(int i) {
        this.mSubscriptionId = i;
    }

    public int getSubscriptionId() {
        if (SdkLevel.isAtLeastS()) {
            return this.mSubscriptionId;
        }
        throw new UnsupportedOperationException();
    }

    public void setNetworkId(int i) {
        this.mNetworkId = i;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public SupplicantState getSupplicantState() {
        return this.mSupplicantState;
    }

    public void setSupplicantState(SupplicantState supplicantState) {
        this.mSupplicantState = supplicantState;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.mIpAddress = inetAddress;
    }

    @Deprecated
    public int getIpAddress() {
        InetAddress inetAddress = this.mIpAddress;
        if (inetAddress instanceof Inet4Address) {
            return Inet4AddressUtils.inet4AddressToIntHTL((Inet4Address) inetAddress);
        }
        return 0;
    }

    public boolean getHiddenSSID() {
        return this.mIsHiddenSsid;
    }

    public void setHiddenSSID(boolean z) {
        this.mIsHiddenSsid = z;
    }

    public static NetworkInfo.DetailedState getDetailedStateOf(SupplicantState supplicantState) {
        return stateMap.get(supplicantState);
    }

    /* access modifiers changed from: package-private */
    public void setSupplicantState(String str) {
        this.mSupplicantState = valueOf(str);
    }

    static SupplicantState valueOf(String str) {
        if ("4WAY_HANDSHAKE".equalsIgnoreCase(str)) {
            return SupplicantState.FOUR_WAY_HANDSHAKE;
        }
        try {
            return SupplicantState.valueOf(str.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException unused) {
            return SupplicantState.INVALID;
        }
    }

    @SystemApi
    public static String sanitizeSsid(String str) {
        return removeDoubleQuotes(str);
    }

    public static String removeDoubleQuotes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("SSID: ");
        StringBuffer append = stringBuffer.append(getSSID()).append(", BSSID: ");
        String str = this.mBSSID;
        Object obj = "<none>";
        if (str == null) {
            str = obj;
        }
        StringBuffer append2 = append.append(str).append(", MAC: ");
        String str2 = this.mMacAddress;
        if (str2 == null) {
            str2 = obj;
        }
        StringBuffer append3 = append2.append(str2).append(", IP: ").append((Object) this.mIpAddress).append(", Security type: ").append(this.mSecurityType).append(", Supplicant state: ");
        SupplicantState supplicantState = this.mSupplicantState;
        if (supplicantState == null) {
            supplicantState = obj;
        }
        StringBuffer append4 = append3.append((Object) supplicantState).append(", Wi-Fi standard: ").append(this.mWifiStandard).append(", RSSI: ").append(this.mRssi).append(", Link speed: ").append(this.mLinkSpeed).append("Mbps, Tx Link speed: ").append(this.mTxLinkSpeed).append("Mbps, Max Supported Tx Link speed: ").append(this.mMaxSupportedTxLinkSpeed).append("Mbps, Rx Link speed: ").append(this.mRxLinkSpeed).append("Mbps, Max Supported Rx Link speed: ").append(this.mMaxSupportedRxLinkSpeed).append("Mbps, Frequency: ").append(this.mFrequency).append("MHz, Net ID: ").append(this.mNetworkId).append(", Metered hint: ").append(this.mMeteredHint).append(", score: ").append(Integer.toString(this.score)).append(", isUsable: ").append(this.mIsUsable).append(", CarrierMerged: ").append(this.mCarrierMerged).append(", SubscriptionId: ").append(this.mSubscriptionId).append(", IsPrimary: ").append(this.mIsPrimary).append(", Trusted: ").append(this.mTrusted).append(", Restricted: ").append(this.mRestricted).append(", Ephemeral: ").append(this.mEphemeral).append(", OEM paid: ").append(this.mOemPaid).append(", OEM private: ").append(this.mOemPrivate).append(", OSU AP: ").append(this.mOsuAp).append(", FQDN: ");
        String str3 = this.mFqdn;
        if (str3 == null) {
            str3 = obj;
        }
        StringBuffer append5 = append4.append(str3).append(", Provider friendly name: ");
        String str4 = this.mProviderFriendlyName;
        if (str4 == null) {
            str4 = obj;
        }
        StringBuffer append6 = append5.append(str4).append(", Requesting package name: ");
        String str5 = this.mRequestingPackageName;
        if (str5 == null) {
            str5 = obj;
        }
        StringBuffer append7 = append6.append(str5);
        String str6 = this.mNetworkKey;
        if (str6 == null) {
            str6 = obj;
        }
        StringBuffer append8 = append7.append(str6).append("MLO Information: , AP MLD Address: ");
        MacAddress macAddress = this.mApMldMacAddress;
        StringBuffer append9 = append8.append(macAddress == null ? obj : macAddress.toString()).append(", AP MLO Link Id: ").append(this.mApMldMacAddress == null ? obj : Integer.valueOf(this.mApMloLinkId)).append(", AP MLO Affiliated links: ");
        if (this.mApMldMacAddress != null) {
            obj = this.mAffiliatedMloLinks;
        }
        append9.append(obj);
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mNetworkId);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mLinkSpeed);
        parcel.writeInt(this.mTxLinkSpeed);
        parcel.writeInt(this.mRxLinkSpeed);
        parcel.writeInt(this.mFrequency);
        if (this.mIpAddress != null) {
            parcel.writeByte((byte) 1);
            parcel.writeByteArray(this.mIpAddress.getAddress());
        } else {
            parcel.writeByte((byte) 0);
        }
        if (this.mWifiSsid != null) {
            parcel.writeInt(1);
            this.mWifiSsid.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(this.mBSSID);
        parcel.writeString(this.mMacAddress);
        parcel.writeInt(this.mMeteredHint ? 1 : 0);
        parcel.writeInt(this.mEphemeral ? 1 : 0);
        parcel.writeInt(this.mTrusted ? 1 : 0);
        parcel.writeInt(this.mOemPaid ? 1 : 0);
        parcel.writeInt(this.mOemPrivate ? 1 : 0);
        parcel.writeInt(this.mCarrierMerged ? 1 : 0);
        parcel.writeInt(this.score);
        parcel.writeBoolean(this.mIsUsable);
        parcel.writeLong(this.txSuccess);
        parcel.writeDouble(this.mSuccessfulTxPacketsPerSecond);
        parcel.writeLong(this.txRetries);
        parcel.writeDouble(this.mTxRetriedTxPacketsPerSecond);
        parcel.writeLong(this.txBad);
        parcel.writeDouble(this.mLostTxPacketsPerSecond);
        parcel.writeLong(this.rxSuccess);
        parcel.writeDouble(this.mSuccessfulRxPacketsPerSecond);
        this.mSupplicantState.writeToParcel(parcel, i);
        parcel.writeInt(this.mOsuAp ? 1 : 0);
        parcel.writeString(this.mRequestingPackageName);
        parcel.writeString(this.mFqdn);
        parcel.writeString(this.mProviderFriendlyName);
        parcel.writeInt(this.mWifiStandard);
        parcel.writeInt(this.mMaxSupportedTxLinkSpeed);
        parcel.writeInt(this.mMaxSupportedRxLinkSpeed);
        parcel.writeString(this.mPasspointUniqueId);
        parcel.writeInt(this.mSubscriptionId);
        parcel.writeTypedList(this.mInformationElements);
        if (SdkLevel.isAtLeastS()) {
            parcel.writeInt(this.mIsPrimary);
        }
        parcel.writeInt(this.mSecurityType);
        parcel.writeInt(this.mRestricted ? 1 : 0);
        parcel.writeString(this.mNetworkKey);
        parcel.writeParcelable(this.mApMldMacAddress, i);
        parcel.writeInt(this.mApMloLinkId);
        parcel.writeTypedList(this.mAffiliatedMloLinks);
    }

    public void setPasspointUniqueId(String str) {
        this.mPasspointUniqueId = str;
    }

    public String getPasspointUniqueId() {
        return this.mPasspointUniqueId;
    }

    public void setInformationElements(List<ScanResult.InformationElement> list) {
        if (list == null) {
            this.mInformationElements = null;
        } else {
            this.mInformationElements = new ArrayList(list);
        }
    }

    public List<ScanResult.InformationElement> getInformationElements() {
        if (this.mInformationElements == null) {
            return null;
        }
        return new ArrayList(this.mInformationElements);
    }

    public void setIsPrimary(boolean z) {
        this.mIsPrimary = z ? 1 : 0;
    }

    @SystemApi
    public boolean isPrimary() {
        if (SdkLevel.isAtLeastS()) {
            int i = this.mIsPrimary;
            if (i != -1) {
                return i == 1;
            }
            throw new SecurityException("Not allowed to access this field");
        }
        throw new UnsupportedOperationException();
    }

    private List<MloLink> getSortedMloLinkList(List<MloLink> list) {
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new Comparator<MloLink>() {
            public int compare(MloLink mloLink, MloLink mloLink2) {
                return mloLink.getLinkId() - mloLink2.getLinkId();
            }
        });
        return arrayList;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!SdkLevel.isAtLeastS() || !(obj instanceof WifiInfo)) {
            return false;
        }
        WifiInfo wifiInfo = (WifiInfo) obj;
        if (!Objects.equals(getSortedMloLinkList(this.mAffiliatedMloLinks), getSortedMloLinkList(wifiInfo.mAffiliatedMloLinks))) {
            return false;
        }
        if (!Objects.equals(this.mWifiSsid, wifiInfo.mWifiSsid) || !Objects.equals(this.mBSSID, wifiInfo.mBSSID) || !Objects.equals(this.mApMldMacAddress, wifiInfo.mApMldMacAddress) || this.mApMloLinkId != wifiInfo.mApMloLinkId || !Objects.equals(Integer.valueOf(this.mNetworkId), Integer.valueOf(wifiInfo.mNetworkId)) || !Objects.equals(Integer.valueOf(this.mRssi), Integer.valueOf(wifiInfo.mRssi)) || !Objects.equals(this.mSupplicantState, wifiInfo.mSupplicantState) || !Objects.equals(Integer.valueOf(this.mLinkSpeed), Integer.valueOf(wifiInfo.mLinkSpeed)) || !Objects.equals(Integer.valueOf(this.mTxLinkSpeed), Integer.valueOf(wifiInfo.mTxLinkSpeed)) || !Objects.equals(Integer.valueOf(this.mRxLinkSpeed), Integer.valueOf(wifiInfo.mRxLinkSpeed)) || !Objects.equals(Integer.valueOf(this.mFrequency), Integer.valueOf(wifiInfo.mFrequency)) || !Objects.equals(this.mIpAddress, wifiInfo.mIpAddress) || !Objects.equals(this.mMacAddress, wifiInfo.mMacAddress) || !Objects.equals(Boolean.valueOf(this.mMeteredHint), Boolean.valueOf(wifiInfo.mMeteredHint)) || !Objects.equals(Boolean.valueOf(this.mEphemeral), Boolean.valueOf(wifiInfo.mEphemeral)) || !Objects.equals(Boolean.valueOf(this.mTrusted), Boolean.valueOf(wifiInfo.mTrusted)) || !Objects.equals(Boolean.valueOf(this.mOemPaid), Boolean.valueOf(wifiInfo.mOemPaid)) || !Objects.equals(Boolean.valueOf(this.mOemPrivate), Boolean.valueOf(wifiInfo.mOemPrivate)) || !Objects.equals(Boolean.valueOf(this.mCarrierMerged), Boolean.valueOf(wifiInfo.mCarrierMerged)) || !Objects.equals(this.mRequestingPackageName, wifiInfo.mRequestingPackageName) || !Objects.equals(Boolean.valueOf(this.mOsuAp), Boolean.valueOf(wifiInfo.mOsuAp)) || !Objects.equals(this.mFqdn, wifiInfo.mFqdn) || !Objects.equals(this.mProviderFriendlyName, wifiInfo.mProviderFriendlyName) || !Objects.equals(Integer.valueOf(this.mSubscriptionId), Integer.valueOf(wifiInfo.mSubscriptionId)) || !Objects.equals(Long.valueOf(this.txBad), Long.valueOf(wifiInfo.txBad)) || !Objects.equals(Long.valueOf(this.txRetries), Long.valueOf(wifiInfo.txRetries)) || !Objects.equals(Long.valueOf(this.txSuccess), Long.valueOf(wifiInfo.txSuccess)) || !Objects.equals(Long.valueOf(this.rxSuccess), Long.valueOf(wifiInfo.rxSuccess)) || !Objects.equals(Double.valueOf(this.mLostTxPacketsPerSecond), Double.valueOf(wifiInfo.mLostTxPacketsPerSecond)) || !Objects.equals(Double.valueOf(this.mTxRetriedTxPacketsPerSecond), Double.valueOf(wifiInfo.mTxRetriedTxPacketsPerSecond)) || !Objects.equals(Double.valueOf(this.mSuccessfulTxPacketsPerSecond), Double.valueOf(wifiInfo.mSuccessfulTxPacketsPerSecond)) || !Objects.equals(Double.valueOf(this.mSuccessfulRxPacketsPerSecond), Double.valueOf(wifiInfo.mSuccessfulRxPacketsPerSecond)) || !Objects.equals(Integer.valueOf(this.score), Integer.valueOf(wifiInfo.score)) || !Objects.equals(Boolean.valueOf(this.mIsUsable), Boolean.valueOf(wifiInfo.mIsUsable)) || !Objects.equals(Integer.valueOf(this.mWifiStandard), Integer.valueOf(wifiInfo.mWifiStandard)) || !Objects.equals(Integer.valueOf(this.mMaxSupportedTxLinkSpeed), Integer.valueOf(wifiInfo.mMaxSupportedTxLinkSpeed)) || !Objects.equals(Integer.valueOf(this.mMaxSupportedRxLinkSpeed), Integer.valueOf(wifiInfo.mMaxSupportedRxLinkSpeed)) || !Objects.equals(this.mPasspointUniqueId, wifiInfo.mPasspointUniqueId) || !Objects.equals(this.mInformationElements, wifiInfo.mInformationElements) || this.mIsPrimary != wifiInfo.mIsPrimary || this.mSecurityType != wifiInfo.mSecurityType || this.mRestricted != wifiInfo.mRestricted || !Objects.equals(this.mNetworkKey, wifiInfo.mNetworkKey)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (!SdkLevel.isAtLeastS()) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.mWifiSsid, this.mBSSID, this.mApMldMacAddress, Integer.valueOf(this.mApMloLinkId), this.mAffiliatedMloLinks, Integer.valueOf(this.mNetworkId), Integer.valueOf(this.mRssi), this.mSupplicantState, Integer.valueOf(this.mLinkSpeed), Integer.valueOf(this.mTxLinkSpeed), Integer.valueOf(this.mRxLinkSpeed), Integer.valueOf(this.mFrequency), this.mIpAddress, this.mMacAddress, Boolean.valueOf(this.mMeteredHint), Boolean.valueOf(this.mEphemeral), Boolean.valueOf(this.mTrusted), Boolean.valueOf(this.mOemPaid), Boolean.valueOf(this.mOemPrivate), Boolean.valueOf(this.mCarrierMerged), this.mRequestingPackageName, Boolean.valueOf(this.mOsuAp), this.mFqdn, this.mProviderFriendlyName, Integer.valueOf(this.mSubscriptionId), Long.valueOf(this.txBad), Long.valueOf(this.txRetries), Long.valueOf(this.txSuccess), Long.valueOf(this.rxSuccess), Double.valueOf(this.mLostTxPacketsPerSecond), Double.valueOf(this.mTxRetriedTxPacketsPerSecond), Double.valueOf(this.mSuccessfulTxPacketsPerSecond), Double.valueOf(this.mSuccessfulRxPacketsPerSecond), Integer.valueOf(this.score), Boolean.valueOf(this.mIsUsable), Integer.valueOf(this.mWifiStandard), Integer.valueOf(this.mMaxSupportedTxLinkSpeed), Integer.valueOf(this.mMaxSupportedRxLinkSpeed), this.mPasspointUniqueId, this.mInformationElements, Integer.valueOf(this.mIsPrimary), Integer.valueOf(this.mSecurityType), Boolean.valueOf(this.mRestricted), this.mNetworkKey);
    }

    public WifiInfo makeCopy(long j) {
        return new WifiInfo(this, j);
    }

    public void setCurrentSecurityType(int i) {
        this.mSecurityType = convertWifiConfigurationSecurityType(i);
    }

    public void clearCurrentSecurityType() {
        this.mSecurityType = -1;
    }

    public int getCurrentSecurityType() {
        return this.mSecurityType;
    }

    public void setNetworkKey(String str) {
        this.mNetworkKey = str;
    }

    @SystemApi
    public String getNetworkKey() {
        return this.mNetworkKey;
    }
}
