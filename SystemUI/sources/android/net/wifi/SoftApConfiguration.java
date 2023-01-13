package android.net.wifi;

import android.annotation.SystemApi;
import android.compat.Compatibility;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.util.HexEncoding;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.wifi.p018x.com.android.internal.util.Preconditions;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SoftApConfiguration implements Parcelable {
    @SystemApi
    public static final int BAND_2GHZ = 1;
    @SystemApi
    public static final int BAND_5GHZ = 2;
    @SystemApi
    public static final int BAND_60GHZ = 8;
    @SystemApi
    public static final int BAND_6GHZ = 4;
    @SystemApi
    public static final int BAND_ANY = 7;
    public static int[] BAND_TYPES = {1, 2, 4, 8};
    public static final Parcelable.Creator<SoftApConfiguration> CREATOR = new Parcelable.Creator<SoftApConfiguration>() {
        public SoftApConfiguration createFromParcel(Parcel parcel) {
            Parcel parcel2 = parcel;
            return new SoftApConfiguration((WifiSsid) parcel2.readParcelable(WifiSsid.class.getClassLoader()), (MacAddress) parcel2.readParcelable(MacAddress.class.getClassLoader()), parcel.readString(), parcel.readBoolean(), SoftApConfiguration.readSparseIntArray(parcel), parcel.readInt(), parcel.readInt(), parcel.readBoolean(), parcel.readLong(), parcel.readBoolean(), parcel2.createTypedArrayList(MacAddress.CREATOR), parcel2.createTypedArrayList(MacAddress.CREATOR), parcel.readInt(), parcel.readBoolean(), parcel.readBoolean(), parcel.readBoolean(), parcel.readBoolean(), parcel.readLong(), parcel2.createTypedArrayList(ScanResult.InformationElement.CREATOR), (MacAddress) parcel2.readParcelable(MacAddress.class.getClassLoader()), SoftApConfiguration.readHashSetInt(parcel), SoftApConfiguration.readHashSetInt(parcel), SoftApConfiguration.readHashSetInt(parcel), parcel.readInt(), parcel.readString());
        }

        public SoftApConfiguration[] newArray(int i) {
            return new SoftApConfiguration[i];
        }
    };
    @SystemApi
    public static final long DEFAULT_TIMEOUT = -1;
    private static final long FORCE_MUTUAL_EXCLUSIVE_BSSID_MAC_RAMDONIZATION_SETTING = 215656264;
    private static final int MAX_CH_2G_BAND = 14;
    private static final int MAX_CH_5G_BAND = 196;
    private static final int MAX_CH_60G_BAND = 6;
    private static final int MAX_CH_6G_BAND = 253;
    private static final int MIN_CH_2G_BAND = 1;
    private static final int MIN_CH_5G_BAND = 34;
    private static final int MIN_CH_60G_BAND = 1;
    private static final int MIN_CH_6G_BAND = 1;
    @SystemApi
    public static final int RANDOMIZATION_NONE = 0;
    @SystemApi
    public static final int RANDOMIZATION_NON_PERSISTENT = 2;
    @SystemApi
    public static final int RANDOMIZATION_PERSISTENT = 1;
    public static final long REMOVE_ZERO_FOR_TIMEOUT_SETTING = 213289672;
    public static final int SECURITY_TYPE_OPEN = 0;
    public static final int SECURITY_TYPE_WPA2_PSK = 1;
    public static final int SECURITY_TYPE_WPA3_OWE = 5;
    public static final int SECURITY_TYPE_WPA3_OWE_TRANSITION = 4;
    public static final int SECURITY_TYPE_WPA3_SAE = 3;
    public static final int SECURITY_TYPE_WPA3_SAE_TRANSITION = 2;
    private static final String TAG = "SoftApConfiguration";
    /* access modifiers changed from: private */
    public final Set<Integer> mAllowedAcsChannels2g;
    /* access modifiers changed from: private */
    public final Set<Integer> mAllowedAcsChannels5g;
    /* access modifiers changed from: private */
    public final Set<Integer> mAllowedAcsChannels6g;
    /* access modifiers changed from: private */
    public final List<MacAddress> mAllowedClientList;
    /* access modifiers changed from: private */
    public final boolean mAutoShutdownEnabled;
    /* access modifiers changed from: private */
    public final List<MacAddress> mBlockedClientList;
    /* access modifiers changed from: private */
    public boolean mBridgedModeOpportunisticShutdownEnabled;
    /* access modifiers changed from: private */
    public final long mBridgedModeOpportunisticShutdownTimeoutMillis;
    /* access modifiers changed from: private */
    public final MacAddress mBssid;
    /* access modifiers changed from: private */
    public final SparseIntArray mChannels;
    /* access modifiers changed from: private */
    public final boolean mClientControlByUser;
    /* access modifiers changed from: private */
    public final boolean mHiddenSsid;
    /* access modifiers changed from: private */
    public boolean mIeee80211axEnabled;
    /* access modifiers changed from: private */
    public boolean mIeee80211beEnabled;
    /* access modifiers changed from: private */
    public boolean mIsUserConfiguration;
    /* access modifiers changed from: private */
    public int mMacRandomizationSetting;
    /* access modifiers changed from: private */
    public final int mMaxChannelBandwidth;
    /* access modifiers changed from: private */
    public final int mMaxNumberOfClients;
    /* access modifiers changed from: private */
    public final String mOweTransIfaceName;
    /* access modifiers changed from: private */
    public final String mPassphrase;
    /* access modifiers changed from: private */
    public final MacAddress mPersistentRandomizedMacAddress;
    /* access modifiers changed from: private */
    public final int mSecurityType;
    /* access modifiers changed from: private */
    public final long mShutdownTimeoutMillis;
    /* access modifiers changed from: private */
    public final List<ScanResult.InformationElement> mVendorElements;
    /* access modifiers changed from: private */
    public final WifiSsid mWifiSsid;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BandType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MacRandomizationSetting {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType {
    }

    /* access modifiers changed from: private */
    public static boolean isBandValid(int i) {
        return i != 0 && (i & -16) == 0;
    }

    /* access modifiers changed from: private */
    public static boolean isChannelBandPairValid(int i, int i2) {
        if (i2 != 1) {
            if (i2 != 2) {
                if (i2 != 4) {
                    return i2 == 8 && i >= 1 && i <= 6;
                }
                if (i < 1 || i > 253) {
                    return false;
                }
            } else if (i < 34 || i > 196) {
                return false;
            }
        } else if (i < 1 || i > 14) {
            return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    private SoftApConfiguration(WifiSsid wifiSsid, MacAddress macAddress, String str, boolean z, SparseIntArray sparseIntArray, int i, int i2, boolean z2, long j, boolean z3, List<MacAddress> list, List<MacAddress> list2, int i3, boolean z4, boolean z5, boolean z6, boolean z7, long j2, List<ScanResult.InformationElement> list3, MacAddress macAddress2, Set<Integer> set, Set<Integer> set2, Set<Integer> set3, int i4, String str2) {
        this.mWifiSsid = wifiSsid;
        this.mBssid = macAddress;
        this.mPassphrase = str;
        this.mHiddenSsid = z;
        if (sparseIntArray.size() != 0) {
            this.mChannels = sparseIntArray.clone();
        } else {
            SparseIntArray sparseIntArray2 = new SparseIntArray(1);
            this.mChannels = sparseIntArray2;
            sparseIntArray2.put(1, 0);
        }
        this.mSecurityType = i;
        this.mMaxNumberOfClients = i2;
        this.mAutoShutdownEnabled = z2;
        this.mShutdownTimeoutMillis = j;
        this.mClientControlByUser = z3;
        this.mBlockedClientList = new ArrayList(list);
        this.mAllowedClientList = new ArrayList(list2);
        this.mMacRandomizationSetting = i3;
        this.mBridgedModeOpportunisticShutdownEnabled = z4;
        this.mIeee80211axEnabled = z5;
        this.mIeee80211beEnabled = z6;
        this.mIsUserConfiguration = z7;
        this.mBridgedModeOpportunisticShutdownTimeoutMillis = j2;
        this.mVendorElements = new ArrayList(list3);
        this.mPersistentRandomizedMacAddress = macAddress2;
        this.mAllowedAcsChannels2g = new HashSet(set);
        this.mAllowedAcsChannels5g = new HashSet(set2);
        this.mAllowedAcsChannels6g = new HashSet(set3);
        this.mMaxChannelBandwidth = i4;
        this.mOweTransIfaceName = str2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SoftApConfiguration)) {
            return false;
        }
        SoftApConfiguration softApConfiguration = (SoftApConfiguration) obj;
        if (Objects.equals(this.mWifiSsid, softApConfiguration.mWifiSsid) && Objects.equals(this.mBssid, softApConfiguration.mBssid) && Objects.equals(this.mPassphrase, softApConfiguration.mPassphrase) && this.mHiddenSsid == softApConfiguration.mHiddenSsid && this.mChannels.toString().equals(softApConfiguration.mChannels.toString()) && this.mSecurityType == softApConfiguration.mSecurityType && this.mMaxNumberOfClients == softApConfiguration.mMaxNumberOfClients && this.mAutoShutdownEnabled == softApConfiguration.mAutoShutdownEnabled && this.mShutdownTimeoutMillis == softApConfiguration.mShutdownTimeoutMillis && this.mClientControlByUser == softApConfiguration.mClientControlByUser && Objects.equals(this.mBlockedClientList, softApConfiguration.mBlockedClientList) && Objects.equals(this.mAllowedClientList, softApConfiguration.mAllowedClientList) && this.mMacRandomizationSetting == softApConfiguration.mMacRandomizationSetting && this.mBridgedModeOpportunisticShutdownEnabled == softApConfiguration.mBridgedModeOpportunisticShutdownEnabled && this.mIeee80211axEnabled == softApConfiguration.mIeee80211axEnabled && this.mIeee80211beEnabled == softApConfiguration.mIeee80211beEnabled && this.mIsUserConfiguration == softApConfiguration.mIsUserConfiguration && this.mBridgedModeOpportunisticShutdownTimeoutMillis == softApConfiguration.mBridgedModeOpportunisticShutdownTimeoutMillis && Objects.equals(this.mVendorElements, softApConfiguration.mVendorElements) && Objects.equals(this.mPersistentRandomizedMacAddress, softApConfiguration.mPersistentRandomizedMacAddress) && Objects.equals(this.mAllowedAcsChannels2g, softApConfiguration.mAllowedAcsChannels2g) && Objects.equals(this.mAllowedAcsChannels5g, softApConfiguration.mAllowedAcsChannels5g) && Objects.equals(this.mAllowedAcsChannels6g, softApConfiguration.mAllowedAcsChannels6g) && this.mOweTransIfaceName == softApConfiguration.mOweTransIfaceName && this.mMaxChannelBandwidth == softApConfiguration.mMaxChannelBandwidth) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mWifiSsid, this.mBssid, this.mPassphrase, Boolean.valueOf(this.mHiddenSsid), this.mChannels.toString(), Integer.valueOf(this.mSecurityType), Integer.valueOf(this.mMaxNumberOfClients), Boolean.valueOf(this.mAutoShutdownEnabled), Long.valueOf(this.mShutdownTimeoutMillis), Boolean.valueOf(this.mClientControlByUser), this.mBlockedClientList, this.mAllowedClientList, Integer.valueOf(this.mMacRandomizationSetting), Boolean.valueOf(this.mBridgedModeOpportunisticShutdownEnabled), Boolean.valueOf(this.mIeee80211axEnabled), Boolean.valueOf(this.mIeee80211beEnabled), Boolean.valueOf(this.mIsUserConfiguration), Long.valueOf(this.mBridgedModeOpportunisticShutdownTimeoutMillis), this.mVendorElements, this.mPersistentRandomizedMacAddress, this.mAllowedAcsChannels2g, this.mAllowedAcsChannels5g, this.mAllowedAcsChannels6g, Integer.valueOf(this.mMaxChannelBandwidth), this.mOweTransIfaceName);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ssid = ");
        WifiSsid wifiSsid = this.mWifiSsid;
        sb.append(wifiSsid == null ? null : wifiSsid.toString());
        if (this.mBssid != null) {
            sb.append(" \n bssid = ");
            sb.append(this.mBssid.toString());
        }
        sb.append(" \n Passphrase = ");
        sb.append(TextUtils.isEmpty(this.mPassphrase) ? "<empty>" : "<non-empty>");
        sb.append(" \n HiddenSsid = ");
        sb.append(this.mHiddenSsid);
        sb.append(" \n Channels = ");
        sb.append((Object) this.mChannels);
        sb.append(" \n SecurityType = ");
        sb.append(getSecurityType());
        sb.append(" \n MaxClient = ");
        sb.append(this.mMaxNumberOfClients);
        sb.append(" \n AutoShutdownEnabled = ");
        sb.append(this.mAutoShutdownEnabled);
        sb.append(" \n ShutdownTimeoutMillis = ");
        sb.append(this.mShutdownTimeoutMillis);
        sb.append(" \n ClientControlByUser = ");
        sb.append(this.mClientControlByUser);
        sb.append(" \n BlockedClientList = ");
        sb.append((Object) this.mBlockedClientList);
        sb.append(" \n AllowedClientList= ");
        sb.append((Object) this.mAllowedClientList);
        sb.append(" \n MacRandomizationSetting = ");
        sb.append(this.mMacRandomizationSetting);
        sb.append(" \n BridgedModeInstanceOpportunisticEnabled = ");
        sb.append(this.mBridgedModeOpportunisticShutdownEnabled);
        sb.append(" \n BridgedModeOpportunisticShutdownTimeoutMillis = ");
        sb.append(this.mBridgedModeOpportunisticShutdownTimeoutMillis);
        sb.append(" \n Ieee80211axEnabled = ");
        sb.append(this.mIeee80211axEnabled);
        sb.append(" \n Ieee80211beEnabled = ");
        sb.append(this.mIeee80211beEnabled);
        sb.append(" \n isUserConfiguration = ");
        sb.append(this.mIsUserConfiguration);
        sb.append(" \n vendorElements = ");
        sb.append((Object) this.mVendorElements);
        sb.append(" \n mPersistentRandomizedMacAddress = ");
        sb.append((Object) this.mPersistentRandomizedMacAddress);
        sb.append(" \n mAllowedAcsChannels2g = ");
        sb.append((Object) this.mAllowedAcsChannels2g);
        sb.append(" \n mAllowedAcsChannels5g = ");
        sb.append((Object) this.mAllowedAcsChannels5g);
        sb.append(" \n mAllowedAcsChannels6g = ");
        sb.append((Object) this.mAllowedAcsChannels6g);
        sb.append(" \n mMaxChannelBandwidth = ");
        sb.append(this.mMaxChannelBandwidth);
        sb.append(" \n OWE Transition mode Iface =");
        sb.append(this.mOweTransIfaceName);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mWifiSsid, 0);
        parcel.writeParcelable(this.mBssid, i);
        parcel.writeString(this.mPassphrase);
        parcel.writeBoolean(this.mHiddenSsid);
        writeSparseIntArray(parcel, this.mChannels);
        parcel.writeInt(this.mSecurityType);
        parcel.writeInt(this.mMaxNumberOfClients);
        parcel.writeBoolean(this.mAutoShutdownEnabled);
        parcel.writeLong(this.mShutdownTimeoutMillis);
        parcel.writeBoolean(this.mClientControlByUser);
        parcel.writeTypedList(this.mBlockedClientList);
        parcel.writeTypedList(this.mAllowedClientList);
        parcel.writeInt(this.mMacRandomizationSetting);
        parcel.writeBoolean(this.mBridgedModeOpportunisticShutdownEnabled);
        parcel.writeBoolean(this.mIeee80211axEnabled);
        parcel.writeBoolean(this.mIeee80211beEnabled);
        parcel.writeBoolean(this.mIsUserConfiguration);
        parcel.writeLong(this.mBridgedModeOpportunisticShutdownTimeoutMillis);
        parcel.writeTypedList(this.mVendorElements);
        parcel.writeParcelable(this.mPersistentRandomizedMacAddress, i);
        writeHashSetInt(parcel, this.mAllowedAcsChannels2g);
        writeHashSetInt(parcel, this.mAllowedAcsChannels5g);
        writeHashSetInt(parcel, this.mAllowedAcsChannels6g);
        parcel.writeInt(this.mMaxChannelBandwidth);
        parcel.writeString(this.mOweTransIfaceName);
    }

    private static void writeSparseIntArray(Parcel parcel, SparseIntArray sparseIntArray) {
        if (sparseIntArray == null) {
            parcel.writeInt(-1);
            return;
        }
        int size = sparseIntArray.size();
        parcel.writeInt(size);
        for (int i = 0; i < size; i++) {
            parcel.writeInt(sparseIntArray.keyAt(i));
            parcel.writeInt(sparseIntArray.valueAt(i));
        }
    }

    /* access modifiers changed from: private */
    public static SparseIntArray readSparseIntArray(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt < 0) {
            return new SparseIntArray();
        }
        SparseIntArray sparseIntArray = new SparseIntArray(readInt);
        while (readInt > 0) {
            sparseIntArray.append(parcel.readInt(), parcel.readInt());
            readInt--;
        }
        return sparseIntArray;
    }

    private static void writeHashSetInt(Parcel parcel, Set<Integer> set) {
        if (set.isEmpty()) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(set.size());
        for (Integer intValue : set) {
            parcel.writeInt(intValue.intValue());
        }
    }

    /* access modifiers changed from: private */
    public static Set<Integer> readHashSetInt(Parcel parcel) {
        HashSet hashSet = new HashSet();
        int readInt = parcel.readInt();
        if (readInt < 0) {
            return hashSet;
        }
        for (int i = 0; i < readInt; i++) {
            hashSet.add(Integer.valueOf(parcel.readInt()));
        }
        return hashSet;
    }

    @Deprecated
    public String getSsid() {
        WifiSsid wifiSsid = this.mWifiSsid;
        if (wifiSsid == null) {
            return null;
        }
        CharSequence utf8Text = wifiSsid.getUtf8Text();
        return utf8Text != null ? utf8Text.toString() : "<unknown ssid>";
    }

    public WifiSsid getWifiSsid() {
        return this.mWifiSsid;
    }

    @SystemApi
    public List<ScanResult.InformationElement> getVendorElements() {
        if (SdkLevel.isAtLeastT()) {
            return getVendorElementsInternal();
        }
        throw new UnsupportedOperationException();
    }

    public List<ScanResult.InformationElement> getVendorElementsInternal() {
        return new ArrayList(this.mVendorElements);
    }

    public MacAddress getBssid() {
        return this.mBssid;
    }

    public String getPassphrase() {
        return this.mPassphrase;
    }

    public boolean isHiddenSsid() {
        return this.mHiddenSsid;
    }

    @SystemApi
    @Deprecated
    public int getBand() {
        return this.mChannels.keyAt(0);
    }

    public int[] getBands() {
        int size = this.mChannels.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.mChannels.keyAt(i);
        }
        return iArr;
    }

    @SystemApi
    @Deprecated
    public int getChannel() {
        return this.mChannels.valueAt(0);
    }

    @SystemApi
    public SparseIntArray getChannels() {
        if (SdkLevel.isAtLeastS()) {
            return this.mChannels.clone();
        }
        throw new UnsupportedOperationException();
    }

    public int getSecurityType() {
        return this.mSecurityType;
    }

    @SystemApi
    public int getMaxNumberOfClients() {
        return this.mMaxNumberOfClients;
    }

    @SystemApi
    public boolean isAutoShutdownEnabled() {
        return this.mAutoShutdownEnabled;
    }

    @SystemApi
    public long getShutdownTimeoutMillis() {
        if (Compatibility.isChangeEnabled(REMOVE_ZERO_FOR_TIMEOUT_SETTING) || this.mShutdownTimeoutMillis != -1) {
            return this.mShutdownTimeoutMillis;
        }
        return 0;
    }

    @SystemApi
    public boolean isClientControlByUserEnabled() {
        return this.mClientControlByUser;
    }

    @SystemApi
    public List<MacAddress> getBlockedClientList() {
        return this.mBlockedClientList;
    }

    @SystemApi
    public List<MacAddress> getAllowedClientList() {
        return this.mAllowedClientList;
    }

    @SystemApi
    public int getMacRandomizationSetting() {
        if (SdkLevel.isAtLeastS()) {
            return getMacRandomizationSettingInternal();
        }
        throw new UnsupportedOperationException();
    }

    public int getMacRandomizationSettingInternal() {
        return this.mMacRandomizationSetting;
    }

    @SystemApi
    public boolean isBridgedModeOpportunisticShutdownEnabled() {
        if (SdkLevel.isAtLeastS()) {
            return isBridgedModeOpportunisticShutdownEnabledInternal();
        }
        throw new UnsupportedOperationException();
    }

    public boolean isBridgedModeOpportunisticShutdownEnabledInternal() {
        return this.mBridgedModeOpportunisticShutdownEnabled;
    }

    public boolean isIeee80211axEnabledInternal() {
        return this.mIeee80211axEnabled;
    }

    @SystemApi
    public boolean isIeee80211axEnabled() {
        if (SdkLevel.isAtLeastS()) {
            return isIeee80211axEnabledInternal();
        }
        throw new UnsupportedOperationException();
    }

    public boolean isIeee80211beEnabledInternal() {
        return this.mIeee80211beEnabled;
    }

    @SystemApi
    public boolean isIeee80211beEnabled() {
        if (SdkLevel.isAtLeastT()) {
            return isIeee80211beEnabledInternal();
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public int[] getAllowedAcsChannels(int i) {
        if (!SdkLevel.isAtLeastT()) {
            throw new UnsupportedOperationException();
        } else if (i == 1) {
            return this.mAllowedAcsChannels2g.stream().mapToInt(new SoftApConfiguration$$ExternalSyntheticLambda0()).toArray();
        } else {
            if (i == 2) {
                return this.mAllowedAcsChannels5g.stream().mapToInt(new SoftApConfiguration$$ExternalSyntheticLambda0()).toArray();
            }
            if (i == 4) {
                return this.mAllowedAcsChannels6g.stream().mapToInt(new SoftApConfiguration$$ExternalSyntheticLambda0()).toArray();
            }
            throw new IllegalArgumentException("getAllowedAcsChannels: Invalid band: " + i);
        }
    }

    @SystemApi
    public int getMaxChannelBandwidth() {
        if (SdkLevel.isAtLeastT()) {
            return this.mMaxChannelBandwidth;
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public boolean isUserConfiguration() {
        if (SdkLevel.isAtLeastS()) {
            return isUserConfigurationInternal();
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public MacAddress getPersistentRandomizedMacAddress() {
        return this.mPersistentRandomizedMacAddress;
    }

    public boolean isUserConfigurationInternal() {
        return this.mIsUserConfiguration;
    }

    @SystemApi
    public long getBridgedModeOpportunisticShutdownTimeoutMillis() {
        if (SdkLevel.isAtLeastT()) {
            return this.mBridgedModeOpportunisticShutdownTimeoutMillis;
        }
        throw new UnsupportedOperationException();
    }

    public long getBridgedModeOpportunisticShutdownTimeoutMillisInternal() {
        return this.mBridgedModeOpportunisticShutdownTimeoutMillis;
    }

    @SystemApi
    public WifiConfiguration toWifiConfiguration() {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        WifiSsid wifiSsid = this.mWifiSsid;
        CharSequence utf8Text = wifiSsid != null ? wifiSsid.getUtf8Text() : null;
        wifiConfiguration.SSID = utf8Text != null ? utf8Text.toString() : "<unknown ssid>";
        wifiConfiguration.preSharedKey = this.mPassphrase;
        wifiConfiguration.hiddenSSID = this.mHiddenSsid;
        wifiConfiguration.apChannel = getChannel();
        int i = this.mSecurityType;
        if (i == 0) {
            wifiConfiguration.allowedKeyManagement.set(0);
        } else if (i == 1 || i == 2) {
            wifiConfiguration.allowedKeyManagement.set(4);
        } else {
            Log.e(TAG, "Convert fail, unsupported security type :" + this.mSecurityType);
            return null;
        }
        int band = getBand();
        if (band == 1) {
            wifiConfiguration.apBand = 0;
        } else if (band == 2) {
            wifiConfiguration.apBand = 1;
        } else if (band == 3) {
            wifiConfiguration.apBand = -1;
        } else if (band != 7) {
            Log.e(TAG, "Convert fail, unsupported band setting :" + getBand());
            return null;
        } else {
            wifiConfiguration.apBand = -1;
        }
        return wifiConfiguration;
    }

    public String getOweTransIfaceName() {
        return this.mOweTransIfaceName;
    }

    @SystemApi
    public static final class Builder {
        private Set<Integer> mAllowedAcsChannels2g;
        private Set<Integer> mAllowedAcsChannels5g;
        private Set<Integer> mAllowedAcsChannels6g;
        private List<MacAddress> mAllowedClientList;
        private boolean mAutoShutdownEnabled;
        private List<MacAddress> mBlockedClientList;
        private boolean mBridgedModeOpportunisticShutdownEnabled;
        private long mBridgedModeOpportunisticShutdownTimeoutMillis;
        private MacAddress mBssid;
        private SparseIntArray mChannels;
        private boolean mClientControlByUser;
        private boolean mHiddenSsid;
        private boolean mIeee80211axEnabled;
        private boolean mIeee80211beEnabled;
        private boolean mIsUserConfiguration;
        private int mMacRandomizationSetting;
        private int mMaxChannelBandwidth;
        private int mMaxNumberOfClients;
        private String mOweTransIfaceName;
        private String mPassphrase;
        private MacAddress mPersistentRandomizedMacAddress;
        private int mSecurityType;
        private long mShutdownTimeoutMillis;
        private List<ScanResult.InformationElement> mVendorElements;
        private WifiSsid mWifiSsid;

        public Builder() {
            this.mWifiSsid = null;
            this.mBssid = null;
            this.mPassphrase = null;
            this.mHiddenSsid = false;
            SparseIntArray sparseIntArray = new SparseIntArray(1);
            this.mChannels = sparseIntArray;
            sparseIntArray.put(1, 0);
            this.mMaxNumberOfClients = 0;
            this.mSecurityType = 0;
            this.mAutoShutdownEnabled = true;
            this.mShutdownTimeoutMillis = -1;
            this.mClientControlByUser = false;
            this.mBlockedClientList = new ArrayList();
            this.mAllowedClientList = new ArrayList();
            if (SdkLevel.isAtLeastT()) {
                this.mMacRandomizationSetting = 2;
            } else {
                this.mMacRandomizationSetting = 1;
            }
            this.mBridgedModeOpportunisticShutdownEnabled = true;
            this.mIeee80211axEnabled = true;
            this.mIeee80211beEnabled = true;
            this.mIsUserConfiguration = true;
            this.mBridgedModeOpportunisticShutdownTimeoutMillis = -1;
            this.mVendorElements = new ArrayList();
            this.mPersistentRandomizedMacAddress = null;
            this.mAllowedAcsChannels2g = new HashSet();
            this.mAllowedAcsChannels5g = new HashSet();
            this.mAllowedAcsChannels6g = new HashSet();
            this.mMaxChannelBandwidth = -1;
            this.mOweTransIfaceName = null;
        }

        public Builder(SoftApConfiguration softApConfiguration) {
            Objects.requireNonNull(softApConfiguration);
            this.mWifiSsid = softApConfiguration.mWifiSsid;
            this.mBssid = softApConfiguration.mBssid;
            this.mPassphrase = softApConfiguration.mPassphrase;
            this.mHiddenSsid = softApConfiguration.mHiddenSsid;
            this.mChannels = softApConfiguration.mChannels.clone();
            this.mMaxNumberOfClients = softApConfiguration.mMaxNumberOfClients;
            this.mSecurityType = softApConfiguration.mSecurityType;
            this.mAutoShutdownEnabled = softApConfiguration.mAutoShutdownEnabled;
            this.mShutdownTimeoutMillis = softApConfiguration.mShutdownTimeoutMillis;
            this.mClientControlByUser = softApConfiguration.mClientControlByUser;
            this.mBlockedClientList = new ArrayList(softApConfiguration.mBlockedClientList);
            this.mAllowedClientList = new ArrayList(softApConfiguration.mAllowedClientList);
            this.mMacRandomizationSetting = softApConfiguration.mMacRandomizationSetting;
            this.mBridgedModeOpportunisticShutdownEnabled = softApConfiguration.mBridgedModeOpportunisticShutdownEnabled;
            this.mIeee80211axEnabled = softApConfiguration.mIeee80211axEnabled;
            this.mIeee80211beEnabled = softApConfiguration.mIeee80211beEnabled;
            this.mIsUserConfiguration = softApConfiguration.mIsUserConfiguration;
            this.mBridgedModeOpportunisticShutdownTimeoutMillis = softApConfiguration.mBridgedModeOpportunisticShutdownTimeoutMillis;
            this.mVendorElements = new ArrayList(softApConfiguration.mVendorElements);
            this.mPersistentRandomizedMacAddress = softApConfiguration.mPersistentRandomizedMacAddress;
            this.mAllowedAcsChannels2g = new HashSet(softApConfiguration.mAllowedAcsChannels2g);
            this.mAllowedAcsChannels5g = new HashSet(softApConfiguration.mAllowedAcsChannels5g);
            this.mAllowedAcsChannels6g = new HashSet(softApConfiguration.mAllowedAcsChannels6g);
            this.mMaxChannelBandwidth = softApConfiguration.mMaxChannelBandwidth;
            if (SdkLevel.isAtLeastS() && this.mBssid != null) {
                this.mMacRandomizationSetting = 0;
            }
            this.mOweTransIfaceName = softApConfiguration.mOweTransIfaceName;
        }

        public SoftApConfiguration build() {
            for (MacAddress contains : this.mAllowedClientList) {
                if (this.mBlockedClientList.contains(contains)) {
                    throw new IllegalArgumentException("A MacAddress exist in both client list");
                }
            }
            if (!SdkLevel.isAtLeastS() || !Compatibility.isChangeEnabled(SoftApConfiguration.FORCE_MUTUAL_EXCLUSIVE_BSSID_MAC_RAMDONIZATION_SETTING) || this.mBssid == null || this.mMacRandomizationSetting == 0) {
                if (!Compatibility.isChangeEnabled(SoftApConfiguration.REMOVE_ZERO_FOR_TIMEOUT_SETTING) && this.mShutdownTimeoutMillis == -1) {
                    this.mShutdownTimeoutMillis = 0;
                }
                SoftApConfiguration softApConfiguration = r3;
                SoftApConfiguration softApConfiguration2 = new SoftApConfiguration(this.mWifiSsid, this.mBssid, this.mPassphrase, this.mHiddenSsid, this.mChannels, this.mSecurityType, this.mMaxNumberOfClients, this.mAutoShutdownEnabled, this.mShutdownTimeoutMillis, this.mClientControlByUser, this.mBlockedClientList, this.mAllowedClientList, this.mMacRandomizationSetting, this.mBridgedModeOpportunisticShutdownEnabled, this.mIeee80211axEnabled, this.mIeee80211beEnabled, this.mIsUserConfiguration, this.mBridgedModeOpportunisticShutdownTimeoutMillis, this.mVendorElements, this.mPersistentRandomizedMacAddress, this.mAllowedAcsChannels2g, this.mAllowedAcsChannels5g, this.mAllowedAcsChannels6g, this.mMaxChannelBandwidth, this.mOweTransIfaceName);
                return softApConfiguration;
            }
            throw new IllegalArgumentException("A BSSID had configured but MAC randomization setting is not NONE");
        }

        @Deprecated
        public Builder setSsid(String str) {
            if (str == null) {
                this.mWifiSsid = null;
                return this;
            }
            Preconditions.checkStringNotEmpty(str);
            Preconditions.checkArgument(StandardCharsets.UTF_8.newEncoder().canEncode((CharSequence) str));
            this.mWifiSsid = WifiSsid.fromUtf8Text(str);
            return this;
        }

        public Builder setWifiSsid(WifiSsid wifiSsid) {
            if (SdkLevel.isAtLeastT()) {
                this.mWifiSsid = wifiSsid;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setVendorElements(List<ScanResult.InformationElement> list) {
            if (SdkLevel.isAtLeastT()) {
                for (ScanResult.InformationElement informationElement : list) {
                    if (informationElement.f54id != 221) {
                        throw new IllegalArgumentException("received InformationElement which is not related to VendorElements. VendorElement block should start with " + HexEncoding.encodeToString(new byte[]{-35}));
                    }
                }
                if (new HashSet(list).size() >= list.size()) {
                    this.mVendorElements = new ArrayList(list);
                    return this;
                }
                throw new IllegalArgumentException("vendor elements array contain duplicates. Please avoid passing duplicated and keep structure clean.");
            }
            throw new UnsupportedOperationException();
        }

        public Builder setBssid(MacAddress macAddress) {
            if (macAddress != null) {
                Preconditions.checkArgument(!macAddress.equals(WifiManager.ALL_ZEROS_MAC_ADDRESS));
                if (macAddress.getAddressType() != 1) {
                    throw new IllegalArgumentException("bssid doesn't support multicast or broadcast mac address");
                }
            }
            this.mBssid = macAddress;
            return this;
        }

        public Builder setPassphrase(String str, int i) {
            if (SdkLevel.isAtLeastT() || !(i == 4 || i == 5)) {
                if (i != 0 && i != 4 && i != 5) {
                    Preconditions.checkStringNotEmpty(str);
                } else if (str != null) {
                    throw new IllegalArgumentException("passphrase should be null when security type is open");
                }
                this.mSecurityType = i;
                this.mPassphrase = str;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setHiddenSsid(boolean z) {
            this.mHiddenSsid = z;
            return this;
        }

        public Builder setBand(int i) {
            if (SoftApConfiguration.isBandValid(i)) {
                SparseIntArray sparseIntArray = new SparseIntArray(1);
                this.mChannels = sparseIntArray;
                sparseIntArray.put(i, 0);
                return this;
            }
            throw new IllegalArgumentException("Invalid band type: " + i);
        }

        public Builder setBands(int[] iArr) {
            if (!SdkLevel.isAtLeastS()) {
                throw new UnsupportedOperationException();
            } else if (iArr.length == 0 || iArr.length > 2) {
                throw new IllegalArgumentException("Unsupported number of bands(" + iArr.length + ") configured");
            } else {
                SparseIntArray sparseIntArray = new SparseIntArray(iArr.length);
                int length = iArr.length;
                int i = 0;
                while (i < length) {
                    int i2 = iArr[i];
                    if (SoftApConfiguration.isBandValid(i2)) {
                        sparseIntArray.put(i2, 0);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Invalid band type: " + i2);
                    }
                }
                this.mChannels = sparseIntArray;
                return this;
            }
        }

        public Builder setChannel(int i, int i2) {
            if (SoftApConfiguration.isChannelBandPairValid(i, i2)) {
                SparseIntArray sparseIntArray = new SparseIntArray(1);
                this.mChannels = sparseIntArray;
                sparseIntArray.put(i2, i);
                return this;
            }
            throw new IllegalArgumentException("Invalid channel(" + i + ") & band (" + i2 + ") configured");
        }

        public Builder setChannels(SparseIntArray sparseIntArray) {
            if (!SdkLevel.isAtLeastS()) {
                throw new UnsupportedOperationException();
            } else if (sparseIntArray.size() == 0 || sparseIntArray.size() > 2) {
                throw new IllegalArgumentException("Unsupported number of channels(" + sparseIntArray.size() + ") configured");
            } else {
                for (int i = 0; i < sparseIntArray.size(); i++) {
                    int valueAt = sparseIntArray.valueAt(i);
                    int keyAt = sparseIntArray.keyAt(i);
                    if (valueAt == 0) {
                        if (!SoftApConfiguration.isBandValid(keyAt)) {
                            throw new IllegalArgumentException("Invalid band type: " + keyAt);
                        }
                    } else if (!SoftApConfiguration.isChannelBandPairValid(valueAt, keyAt)) {
                        throw new IllegalArgumentException("Invalid channel(" + valueAt + ") & band (" + keyAt + ") configured");
                    }
                }
                this.mChannels = sparseIntArray.clone();
                return this;
            }
        }

        public Builder setMaxNumberOfClients(int i) {
            if (i >= 0) {
                this.mMaxNumberOfClients = i;
                return this;
            }
            throw new IllegalArgumentException("maxNumberOfClients should be not negative");
        }

        public Builder setAutoShutdownEnabled(boolean z) {
            this.mAutoShutdownEnabled = z;
            return this;
        }

        public Builder setShutdownTimeoutMillis(long j) {
            if (!Compatibility.isChangeEnabled(SoftApConfiguration.REMOVE_ZERO_FOR_TIMEOUT_SETTING) || j >= 1) {
                if (j < 0) {
                    throw new IllegalArgumentException("Invalid timeout value from legacy app: " + j);
                }
            } else if (j != -1) {
                throw new IllegalArgumentException("Invalid timeout value: " + j);
            }
            this.mShutdownTimeoutMillis = j;
            return this;
        }

        public Builder setClientControlByUserEnabled(boolean z) {
            this.mClientControlByUser = z;
            return this;
        }

        public Builder setAllowedAcsChannels(int i, int[] iArr) {
            if (!SdkLevel.isAtLeastT()) {
                throw new UnsupportedOperationException();
            } else if (iArr == null) {
                throw new IllegalArgumentException("Passing a null object to setAllowedAcsChannels");
            } else if (i == 1 || i == 2 || i == 4) {
                int length = iArr.length;
                int i2 = 0;
                while (i2 < length) {
                    int i3 = iArr[i2];
                    if (SoftApConfiguration.isChannelBandPairValid(i3, i)) {
                        i2++;
                    } else {
                        throw new IllegalArgumentException("Invalid channel to setAllowedAcsChannels: band: " + i + "channel: " + i3);
                    }
                }
                HashSet hashSet = (HashSet) IntStream.m1783of(iArr).boxed().collect(Collectors.toCollection(new SoftApConfiguration$Builder$$ExternalSyntheticLambda0()));
                if (i == 1) {
                    this.mAllowedAcsChannels2g = hashSet;
                } else if (i == 2) {
                    this.mAllowedAcsChannels5g = hashSet;
                } else if (i == 4) {
                    this.mAllowedAcsChannels6g = hashSet;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Passing an invalid band to setAllowedAcsChannels");
            }
        }

        public Builder setMaxChannelBandwidth(int i) {
            if (!SdkLevel.isAtLeastT()) {
                throw new UnsupportedOperationException();
            } else if (i == -1 || i == 6 || i == 11 || i == 2 || i == 3 || i == 4) {
                this.mMaxChannelBandwidth = i;
                return this;
            } else {
                throw new IllegalArgumentException("Invalid channel bandwidth value(" + i + ")  configured");
            }
        }

        public Builder setAllowedClientList(List<MacAddress> list) {
            this.mAllowedClientList = new ArrayList(list);
            return this;
        }

        public Builder setBlockedClientList(List<MacAddress> list) {
            this.mBlockedClientList = new ArrayList(list);
            return this;
        }

        public Builder setMacRandomizationSetting(int i) {
            if (SdkLevel.isAtLeastS()) {
                this.mMacRandomizationSetting = i;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setBridgedModeOpportunisticShutdownEnabled(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mBridgedModeOpportunisticShutdownEnabled = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setIeee80211axEnabled(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mIeee80211axEnabled = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setIeee80211beEnabled(boolean z) {
            if (SdkLevel.isAtLeastT()) {
                this.mIeee80211beEnabled = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setUserConfiguration(boolean z) {
            this.mIsUserConfiguration = z;
            return this;
        }

        public Builder setBridgedModeOpportunisticShutdownTimeoutMillis(long j) {
            if (!SdkLevel.isAtLeastT()) {
                throw new UnsupportedOperationException();
            } else if (j >= 1 || j == -1) {
                this.mBridgedModeOpportunisticShutdownTimeoutMillis = j;
                return this;
            } else {
                throw new IllegalArgumentException("Invalid timeout value: " + j);
            }
        }

        public Builder setRandomizedMacAddress(MacAddress macAddress) {
            if (macAddress != null) {
                this.mPersistentRandomizedMacAddress = macAddress;
                return this;
            }
            throw new IllegalArgumentException("setRandomizedMacAddress received null MacAddress.");
        }

        public Builder setOweTransIfaceName(String str) {
            this.mOweTransIfaceName = str;
            return this;
        }
    }
}
