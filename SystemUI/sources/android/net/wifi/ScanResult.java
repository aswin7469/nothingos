package android.net.wifi;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.util.ScanResultUtil;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.p004os.EnvironmentCompat;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ScanResult implements Parcelable {
    public static final int BAND_24_GHZ_END_FREQ_MHZ = 2484;
    public static final int BAND_24_GHZ_FIRST_CH_NUM = 1;
    public static final int BAND_24_GHZ_LAST_CH_NUM = 14;
    public static final int BAND_24_GHZ_START_FREQ_MHZ = 2412;
    public static final int BAND_5_GHZ_END_FREQ_MHZ = 5885;
    public static final int BAND_5_GHZ_FIRST_CH_NUM = 32;
    public static final int BAND_5_GHZ_LAST_CH_NUM = 177;
    public static final int BAND_5_GHZ_START_FREQ_MHZ = 5160;
    public static final int BAND_60_GHZ_END_FREQ_MHZ = 70200;
    public static final int BAND_60_GHZ_FIRST_CH_NUM = 1;
    public static final int BAND_60_GHZ_LAST_CH_NUM = 6;
    public static final int BAND_60_GHZ_START_FREQ_MHZ = 58320;
    public static final int BAND_6_GHZ_END_FREQ_MHZ = 7115;
    public static final int BAND_6_GHZ_FIRST_CH_NUM = 1;
    public static final int BAND_6_GHZ_LAST_CH_NUM = 233;
    public static final int BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ = 5935;
    public static final int BAND_6_GHZ_PSC_START_MHZ = 5975;
    public static final int BAND_6_GHZ_PSC_STEP_SIZE_MHZ = 80;
    public static final int BAND_6_GHZ_START_FREQ_MHZ = 5955;
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    public static final int CHANNEL_WIDTH_320MHZ = 5;
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    @SystemApi
    public static final int CIPHER_BIP_CMAC_256 = 9;
    @SystemApi
    public static final int CIPHER_BIP_GMAC_128 = 7;
    @SystemApi
    public static final int CIPHER_BIP_GMAC_256 = 8;
    @SystemApi
    public static final int CIPHER_CCMP = 3;
    @SystemApi
    public static final int CIPHER_GCMP_128 = 6;
    @SystemApi
    public static final int CIPHER_GCMP_256 = 4;
    @SystemApi
    public static final int CIPHER_NONE = 0;
    @SystemApi
    public static final int CIPHER_NO_GROUP_ADDRESSED = 1;
    @SystemApi
    public static final int CIPHER_SMS4 = 5;
    @SystemApi
    public static final int CIPHER_TKIP = 2;
    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel parcel) {
            Parcel parcel2 = parcel;
            boolean z = true;
            ScanResult scanResult = new ScanResult(parcel.readInt() == 1 ? WifiSsid.CREATOR.createFromParcel(parcel2) : null, parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), false);
            scanResult.mWifiStandard = parcel.readInt();
            scanResult.seen = parcel.readLong();
            if (parcel.readInt() == 0) {
                z = false;
            }
            scanResult.untrusted = z;
            scanResult.numUsage = parcel.readInt();
            scanResult.venueName = parcel.readString();
            scanResult.operatorFriendlyName = parcel.readString();
            scanResult.flags = parcel.readLong();
            scanResult.informationElements = (InformationElement[]) parcel2.createTypedArray(InformationElement.CREATOR);
            int readInt = parcel.readInt();
            if (readInt != 0) {
                scanResult.anqpLines = new ArrayList();
                for (int i = 0; i < readInt; i++) {
                    scanResult.anqpLines.add(parcel.readString());
                }
            }
            int readInt2 = parcel.readInt();
            if (readInt2 != 0) {
                scanResult.anqpElements = new AnqpInformationElement[readInt2];
                for (int i2 = 0; i2 < readInt2; i2++) {
                    int readInt3 = parcel.readInt();
                    int readInt4 = parcel.readInt();
                    byte[] bArr = new byte[parcel.readInt()];
                    parcel2.readByteArray(bArr);
                    scanResult.anqpElements[i2] = new AnqpInformationElement(readInt3, readInt4, bArr);
                }
            }
            int readInt5 = parcel.readInt();
            if (readInt5 != 0) {
                scanResult.radioChainInfos = new RadioChainInfo[readInt5];
                for (int i3 = 0; i3 < readInt5; i3++) {
                    scanResult.radioChainInfos[i3] = new RadioChainInfo();
                    scanResult.radioChainInfos[i3].f55id = parcel.readInt();
                    scanResult.radioChainInfos[i3].level = parcel.readInt();
                }
            }
            scanResult.ifaceName = parcel.readString();
            scanResult.mApMldMacAddress = (MacAddress) parcel2.readParcelable(MacAddress.class.getClassLoader());
            scanResult.mApMloLinkId = parcel.readInt();
            scanResult.mAffiliatedMloLinks = parcel2.createTypedArrayList(MloLink.CREATOR);
            return scanResult;
        }

        public ScanResult[] newArray(int i) {
            return new ScanResult[i];
        }
    };
    public static final long FLAG_80211mc_RESPONDER = 2;
    public static final long FLAG_PASSPOINT_NETWORK = 1;
    public static final int KEY_MGMT_DPP = 17;
    @SystemApi
    public static final int KEY_MGMT_EAP = 2;
    @SystemApi
    public static final int KEY_MGMT_EAP_SHA256 = 6;
    @SystemApi
    public static final int KEY_MGMT_EAP_SUITE_B_192 = 10;
    public static final int KEY_MGMT_FILS_SHA256 = 15;
    public static final int KEY_MGMT_FILS_SHA384 = 16;
    @SystemApi
    public static final int KEY_MGMT_FT_EAP = 4;
    public static final int KEY_MGMT_FT_EAP_SHA384 = 18;
    @SystemApi
    public static final int KEY_MGMT_FT_PSK = 3;
    @SystemApi
    public static final int KEY_MGMT_FT_SAE = 11;
    @SystemApi
    public static final int KEY_MGMT_NONE = 0;
    @SystemApi
    public static final int KEY_MGMT_OSEN = 7;
    @SystemApi
    public static final int KEY_MGMT_OWE = 9;
    @SystemApi
    public static final int KEY_MGMT_OWE_TRANSITION = 12;
    @SystemApi
    public static final int KEY_MGMT_PSK = 1;
    @SystemApi
    public static final int KEY_MGMT_PSK_SHA256 = 5;
    @SystemApi
    public static final int KEY_MGMT_SAE = 8;
    public static final int KEY_MGMT_UNKNOWN = 18;
    @SystemApi
    public static final int KEY_MGMT_WAPI_CERT = 14;
    @SystemApi
    public static final int KEY_MGMT_WAPI_PSK = 13;
    public static final int PREAMBLE_EHT = 4;
    public static final int PREAMBLE_HE = 3;
    public static final int PREAMBLE_HT = 1;
    public static final int PREAMBLE_LEGACY = 0;
    public static final int PREAMBLE_VHT = 2;
    @SystemApi
    public static final int PROTOCOL_NONE = 0;
    @SystemApi
    public static final int PROTOCOL_OSEN = 3;
    @SystemApi
    public static final int PROTOCOL_RSN = 2;
    @SystemApi
    public static final int PROTOCOL_WAPI = 4;
    @SystemApi
    public static final int PROTOCOL_WPA = 1;
    public static final int UNSPECIFIED = -1;
    public static final int WIFI_BAND_24_GHZ = 1;
    public static final int WIFI_BAND_5_GHZ = 2;
    public static final int WIFI_BAND_60_GHZ = 16;
    public static final int WIFI_BAND_6_GHZ = 8;
    public static final int WIFI_STANDARD_11AC = 5;
    public static final int WIFI_STANDARD_11AD = 7;
    public static final int WIFI_STANDARD_11AX = 6;
    public static final int WIFI_STANDARD_11BE = 8;
    public static final int WIFI_STANDARD_11N = 4;
    public static final int WIFI_STANDARD_LEGACY = 1;
    public static final int WIFI_STANDARD_UNKNOWN = 0;
    public String BSSID;
    @Deprecated
    public String SSID;
    public int anqpDomainId;
    public AnqpInformationElement[] anqpElements;
    public List<String> anqpLines;
    public String capabilities;
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    public int distanceCm;
    public int distanceSdCm;
    public long flags;
    public int frequency;
    public long hessid;
    public String ifaceName;
    public InformationElement[] informationElements;
    public boolean is80211McRTTResponder;
    public int level;
    /* access modifiers changed from: private */
    public List<MloLink> mAffiliatedMloLinks;
    /* access modifiers changed from: private */
    public MacAddress mApMldMacAddress;
    /* access modifiers changed from: private */
    public int mApMloLinkId;
    /* access modifiers changed from: private */
    public int mWifiStandard;
    public int numUsage;
    @Deprecated
    public CharSequence operatorFriendlyName;
    public RadioChainInfo[] radioChainInfos;
    public long seen;
    public long timestamp;
    @SystemApi
    public boolean untrusted;
    @Deprecated
    public CharSequence venueName;
    @Deprecated
    public WifiSsid wifiSsid;

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiBand {
    }

    public static int convertChannelToFrequencyMhzIfSupported(int i, int i2) {
        if (i2 == 1) {
            if (i == 14) {
                return BAND_24_GHZ_END_FREQ_MHZ;
            }
            if (i < 1 || i > 14) {
                return -1;
            }
            return ((i - 1) * 5) + BAND_24_GHZ_START_FREQ_MHZ;
        } else if (i2 == 2) {
            if (i < 32 || i > 177) {
                return -1;
            }
            return ((i - 32) * 5) + BAND_5_GHZ_START_FREQ_MHZ;
        } else if (i2 == 8) {
            if (i < 1 || i > 233) {
                return -1;
            }
            if (i == 2) {
                return 5935;
            }
            return ((i - 1) * 5) + BAND_6_GHZ_START_FREQ_MHZ;
        } else if (i2 != 16 || i < 1 || i > 6) {
            return -1;
        } else {
            return ((i - 1) * 2160) + 58320;
        }
    }

    public static int getBandFromOpClass(int i, int i2) {
        return (i < 81 || i > 84) ? (i < 115 || i > 130) ? (i < 131 || i > 137 || i2 < 1 || i2 > 233) ? 0 : 8 : (i2 < 32 || i2 > 177) ? 0 : 2 : (i2 < 1 || i2 > 14) ? 0 : 1;
    }

    public static boolean is24GHz(int i) {
        return i >= 2412 && i <= 2484;
    }

    public static boolean is5GHz(int i) {
        return i >= 5160 && i <= 5885;
    }

    public static boolean is60GHz(int i) {
        return i >= 58320 && i <= 70200;
    }

    public static boolean is6GHz(int i) {
        if (i == 5935) {
            return true;
        }
        return i >= 5955 && i <= 7115;
    }

    private static String wifiStandardToString(int i) {
        if (i == 0) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (i == 1) {
            return "legacy";
        }
        switch (i) {
            case 4:
                return "11n";
            case 5:
                return "11ac";
            case 6:
                return "11ax";
            case 7:
                return "11ad";
            case 8:
                return "11be";
            default:
                return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    @SystemApi
    public void setWifiSsid(WifiSsid wifiSsid2) {
        this.wifiSsid = wifiSsid2;
        CharSequence utf8Text = wifiSsid2.getUtf8Text();
        this.SSID = utf8Text != null ? utf8Text.toString() : "<unknown ssid>";
    }

    public WifiSsid getWifiSsid() {
        return this.wifiSsid;
    }

    public MacAddress getApMldMacAddress() {
        return this.mApMldMacAddress;
    }

    public void setApMldMacAddress(MacAddress macAddress) {
        this.mApMldMacAddress = macAddress;
    }

    public int getApMloLinkId() {
        return this.mApMloLinkId;
    }

    public void setApMloLinkId(int i) {
        this.mApMloLinkId = i;
    }

    public List<MloLink> getAffiliatedMloLinks() {
        return new ArrayList(this.mAffiliatedMloLinks);
    }

    public void setAffiliatedMloLinks(List<MloLink> list) {
        this.mAffiliatedMloLinks = new ArrayList(list);
    }

    public int getWifiStandard() {
        return this.mWifiStandard;
    }

    public void setWifiStandard(int i) {
        this.mWifiStandard = i;
    }

    public static class RadioChainInfo {

        /* renamed from: id */
        public int f55id;
        public int level;

        public String toString() {
            return "RadioChainInfo: id=" + this.f55id + ", level=" + this.level;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RadioChainInfo)) {
                return false;
            }
            RadioChainInfo radioChainInfo = (RadioChainInfo) obj;
            if (this.f55id == radioChainInfo.f55id && this.level == radioChainInfo.level) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.f55id), Integer.valueOf(this.level));
        }
    }

    public void setFlag(long j) {
        this.flags = j | this.flags;
    }

    public void clearFlag(long j) {
        this.flags = (~j) & this.flags;
    }

    public boolean is80211mcResponder() {
        return (this.flags & 2) != 0;
    }

    public boolean isPasspointNetwork() {
        return (this.flags & 1) != 0;
    }

    public static boolean is6GHzPsc(int i) {
        if (is6GHz(i) && (i - 5975) % 80 == 0) {
            return true;
        }
        return false;
    }

    public static int convertFrequencyMhzToChannelIfSupported(int i) {
        if (i == 2484) {
            return 14;
        }
        if (is24GHz(i)) {
            return ((i - 2412) / 5) + 1;
        }
        if (is5GHz(i)) {
            return ((i - 5160) / 5) + 32;
        }
        if (is6GHz(i)) {
            if (i == 5935) {
                return 2;
            }
            return ((i - 5955) / 5) + 1;
        } else if (is60GHz(i)) {
            return ((i - 58320) / 2160) + 1;
        } else {
            return -1;
        }
    }

    public static int toBand(int i) {
        if (is24GHz(i)) {
            return 1;
        }
        if (is5GHz(i)) {
            return 2;
        }
        if (is6GHz(i)) {
            return 8;
        }
        return is60GHz(i) ? 16 : -1;
    }

    @SystemApi
    public int getBand() {
        return toBand(this.frequency);
    }

    public boolean is24GHz() {
        return is24GHz(this.frequency);
    }

    public boolean is5GHz() {
        return is5GHz(this.frequency);
    }

    public boolean is6GHz() {
        return is6GHz(this.frequency);
    }

    public boolean is6GhzPsc() {
        return is6GHzPsc(this.frequency);
    }

    public boolean is60GHz() {
        return is60GHz(this.frequency);
    }

    public static class InformationElement implements Parcelable {
        public static final Parcelable.Creator<InformationElement> CREATOR = new Parcelable.Creator<InformationElement>() {
            public InformationElement createFromParcel(Parcel parcel) {
                InformationElement informationElement = new InformationElement();
                informationElement.f54id = parcel.readInt();
                informationElement.idExt = parcel.readInt();
                informationElement.bytes = parcel.createByteArray();
                return informationElement;
            }

            public InformationElement[] newArray(int i) {
                return new InformationElement[i];
            }
        };
        public static final int EID_BSS_LOAD = 11;
        public static final int EID_COUNTRY = 7;
        public static final int EID_ERP = 42;
        public static final int EID_EXTENDED_CAPS = 127;
        public static final int EID_EXTENDED_SUPPORTED_RATES = 50;
        public static final int EID_EXTENSION_PRESENT = 255;
        public static final int EID_EXT_EHT_CAPABILITIES = 108;
        public static final int EID_EXT_EHT_OPERATION = 106;
        public static final int EID_EXT_HE_CAPABILITIES = 35;
        public static final int EID_EXT_HE_OPERATION = 36;
        public static final int EID_EXT_MULTI_LINK = 107;
        public static final int EID_HT_CAPABILITIES = 45;
        public static final int EID_HT_OPERATION = 61;
        public static final int EID_INTERWORKING = 107;
        public static final int EID_RNR = 201;
        public static final int EID_ROAMING_CONSORTIUM = 111;
        public static final int EID_RSN = 48;
        public static final int EID_SSID = 0;
        public static final int EID_SUPPORTED_RATES = 1;
        public static final int EID_TIM = 5;
        public static final int EID_VHT_CAPABILITIES = 191;
        public static final int EID_VHT_OPERATION = 192;
        public static final int EID_VSA = 221;
        public byte[] bytes;

        /* renamed from: id */
        public int f54id;
        public int idExt;

        public int describeContents() {
            return 0;
        }

        public InformationElement() {
        }

        public InformationElement(int i, int i2, byte[] bArr) {
            this.f54id = i;
            this.idExt = i2;
            this.bytes = (byte[]) bArr.clone();
        }

        public InformationElement(InformationElement informationElement) {
            this.f54id = informationElement.f54id;
            this.idExt = informationElement.idExt;
            this.bytes = (byte[]) informationElement.bytes.clone();
        }

        public int getId() {
            return this.f54id;
        }

        public int getIdExt() {
            return this.idExt;
        }

        public ByteBuffer getBytes() {
            return ByteBuffer.wrap(this.bytes).asReadOnlyBuffer();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.f54id);
            parcel.writeInt(this.idExt);
            parcel.writeByteArray(this.bytes);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!SdkLevel.isAtLeastS() || !(obj instanceof InformationElement)) {
                return false;
            }
            InformationElement informationElement = (InformationElement) obj;
            if (this.f54id == informationElement.f54id && this.idExt == informationElement.idExt && Arrays.equals(this.bytes, informationElement.bytes)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (!SdkLevel.isAtLeastS()) {
                return System.identityHashCode(this);
            }
            return Objects.hash(Integer.valueOf(this.f54id), Integer.valueOf(this.idExt), Integer.valueOf(Arrays.hashCode(this.bytes)));
        }
    }

    public List<InformationElement> getInformationElements() {
        return Collections.unmodifiableList(Arrays.asList(this.informationElements));
    }

    public int[] getSecurityTypes() {
        List<SecurityParams> generateSecurityParamsListFromScanResult = ScanResultUtil.generateSecurityParamsListFromScanResult(this);
        int size = generateSecurityParamsListFromScanResult.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = WifiInfo.convertWifiConfigurationSecurityType(generateSecurityParamsListFromScanResult.get(i).getSecurityType());
        }
        return iArr;
    }

    private boolean isHiddenSsid(WifiSsid wifiSsid2) {
        for (byte b : wifiSsid2.getBytes()) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public ScanResult(WifiSsid wifiSsid2, String str, long j, int i, byte[] bArr, String str2, int i2, int i3, long j2) {
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mWifiStandard = 0;
        this.wifiSsid = wifiSsid2;
        if (wifiSsid2 == null || !isHiddenSsid(wifiSsid2)) {
            CharSequence utf8Text = wifiSsid2 != null ? wifiSsid2.getUtf8Text() : null;
            this.SSID = utf8Text != null ? utf8Text.toString() : "<unknown ssid>";
        } else {
            this.SSID = "";
        }
        this.BSSID = str;
        this.hessid = j;
        this.anqpDomainId = i;
        if (bArr != null) {
            AnqpInformationElement[] anqpInformationElementArr = new AnqpInformationElement[1];
            this.anqpElements = anqpInformationElementArr;
            anqpInformationElementArr[0] = new AnqpInformationElement(AnqpInformationElement.HOTSPOT20_VENDOR_ID, 8, bArr);
        }
        this.capabilities = str2;
        this.level = i2;
        this.frequency = i3;
        this.timestamp = j2;
        this.distanceCm = -1;
        this.distanceSdCm = -1;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
        this.radioChainInfos = null;
        this.mApMldMacAddress = null;
    }

    public ScanResult(WifiSsid wifiSsid2, String str, String str2, int i, int i2, long j, int i3, int i4) {
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mWifiStandard = 0;
        this.wifiSsid = wifiSsid2;
        if (wifiSsid2 == null || !isHiddenSsid(wifiSsid2)) {
            CharSequence utf8Text = wifiSsid2 != null ? wifiSsid2.getUtf8Text() : null;
            this.SSID = utf8Text != null ? utf8Text.toString() : "<unknown ssid>";
        } else {
            this.SSID = "";
        }
        this.BSSID = str;
        this.capabilities = str2;
        this.level = i;
        this.frequency = i2;
        this.timestamp = j;
        this.distanceCm = i3;
        this.distanceSdCm = i4;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
        this.radioChainInfos = null;
        this.mApMldMacAddress = null;
    }

    public ScanResult(String str, String str2, long j, int i, String str3, int i2, int i3, long j2, int i4, int i5, int i6, int i7, int i8, boolean z) {
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mWifiStandard = 0;
        this.SSID = str;
        this.BSSID = str2;
        this.hessid = j;
        this.anqpDomainId = i;
        this.capabilities = str3;
        this.level = i2;
        this.frequency = i3;
        this.timestamp = j2;
        this.distanceCm = i4;
        this.distanceSdCm = i5;
        this.channelWidth = i6;
        this.centerFreq0 = i7;
        this.centerFreq1 = i8;
        if (z) {
            this.flags = 2;
        } else {
            this.flags = 0;
        }
        this.radioChainInfos = null;
        this.mApMldMacAddress = null;
    }

    public ScanResult(WifiSsid wifiSsid2, String str, String str2, long j, int i, String str3, int i2, int i3, long j2, int i4, int i5, int i6, int i7, int i8, boolean z) {
        this(str, str2, j, i, str3, i2, i3, j2, i4, i5, i6, i7, i8, z);
        this.wifiSsid = wifiSsid2;
    }

    public ScanResult(ScanResult scanResult) {
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mWifiStandard = 0;
        if (scanResult != null) {
            this.wifiSsid = scanResult.wifiSsid;
            this.SSID = scanResult.SSID;
            this.BSSID = scanResult.BSSID;
            this.hessid = scanResult.hessid;
            this.anqpDomainId = scanResult.anqpDomainId;
            this.informationElements = scanResult.informationElements;
            this.anqpElements = scanResult.anqpElements;
            this.capabilities = scanResult.capabilities;
            this.level = scanResult.level;
            this.frequency = scanResult.frequency;
            this.channelWidth = scanResult.channelWidth;
            this.centerFreq0 = scanResult.centerFreq0;
            this.centerFreq1 = scanResult.centerFreq1;
            this.timestamp = scanResult.timestamp;
            this.distanceCm = scanResult.distanceCm;
            this.distanceSdCm = scanResult.distanceSdCm;
            this.seen = scanResult.seen;
            this.untrusted = scanResult.untrusted;
            this.numUsage = scanResult.numUsage;
            this.venueName = scanResult.venueName;
            this.operatorFriendlyName = scanResult.operatorFriendlyName;
            this.flags = scanResult.flags;
            this.radioChainInfos = scanResult.radioChainInfos;
            this.mWifiStandard = scanResult.mWifiStandard;
            this.ifaceName = scanResult.ifaceName;
            this.mApMldMacAddress = scanResult.mApMldMacAddress;
            this.mApMloLinkId = scanResult.mApMloLinkId;
            this.mAffiliatedMloLinks = scanResult.mAffiliatedMloLinks != null ? new ArrayList<>(scanResult.mAffiliatedMloLinks) : Collections.emptyList();
        }
    }

    public ScanResult() {
        this.mApMloLinkId = -1;
        this.mAffiliatedMloLinks = Collections.emptyList();
        this.mWifiStandard = 0;
    }

    public String toString() {
        Object obj;
        StringBuffer stringBuffer = new StringBuffer("SSID: ");
        Object obj2 = this.wifiSsid;
        if (obj2 == null) {
            obj2 = "<unknown ssid>";
        }
        StringBuffer append = stringBuffer.append(obj2).append(", BSSID: ");
        String str = this.BSSID;
        String str2 = "<none>";
        if (str == null) {
            str = str2;
        }
        StringBuffer append2 = append.append(str).append(", capabilities: ");
        String str3 = this.capabilities;
        if (str3 != null) {
            str2 = str3;
        }
        append2.append(str2).append(", level: ").append(this.level).append(", frequency: ").append(this.frequency).append(", timestamp: ").append(this.timestamp);
        StringBuffer append3 = stringBuffer.append(", distance: ");
        int i = this.distanceCm;
        Object obj3 = "?";
        append3.append(i != -1 ? Integer.valueOf(i) : obj3).append("(cm), distanceSd: ");
        int i2 = this.distanceSdCm;
        if (i2 != -1) {
            obj3 = Integer.valueOf(i2);
        }
        stringBuffer.append(obj3).append("(cm), passpoint: ");
        stringBuffer.append((this.flags & 1) != 0 ? "yes" : "no");
        stringBuffer.append(", ChannelBandwidth: ").append(this.channelWidth);
        stringBuffer.append(", centerFreq0: ").append(this.centerFreq0);
        stringBuffer.append(", centerFreq1: ").append(this.centerFreq1);
        stringBuffer.append(", standard: ").append(wifiStandardToString(this.mWifiStandard));
        stringBuffer.append(", 80211mcResponder: ");
        stringBuffer.append((this.flags & 2) != 0 ? "is supported" : "is not supported");
        stringBuffer.append(", Radio Chain Infos: ").append(Arrays.toString((Object[]) this.radioChainInfos));
        stringBuffer.append(", interface name: ").append(this.ifaceName);
        if (this.mApMldMacAddress != null) {
            StringBuffer append4 = stringBuffer.append(", MLO Info:  AP MLD MAC Address: ").append(this.mApMldMacAddress.toString()).append(", AP MLO Link-Id: ");
            int i3 = this.mApMloLinkId;
            if (i3 == -1) {
                obj = "Unspecified";
            } else {
                obj = Integer.valueOf(i3);
            }
            append4.append(obj).append(", AP MLO Affiliated Links: ").append((Object) this.mAffiliatedMloLinks);
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 0;
        if (this.wifiSsid != null) {
            parcel.writeInt(1);
            this.wifiSsid.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(this.SSID);
        parcel.writeString(this.BSSID);
        parcel.writeLong(this.hessid);
        parcel.writeInt(this.anqpDomainId);
        parcel.writeString(this.capabilities);
        parcel.writeInt(this.level);
        parcel.writeInt(this.frequency);
        parcel.writeLong(this.timestamp);
        parcel.writeInt(this.distanceCm);
        parcel.writeInt(this.distanceSdCm);
        parcel.writeInt(this.channelWidth);
        parcel.writeInt(this.centerFreq0);
        parcel.writeInt(this.centerFreq1);
        parcel.writeInt(this.mWifiStandard);
        parcel.writeLong(this.seen);
        parcel.writeInt(this.untrusted ? 1 : 0);
        parcel.writeInt(this.numUsage);
        CharSequence charSequence = this.venueName;
        String str = "";
        parcel.writeString(charSequence != null ? charSequence.toString() : str);
        CharSequence charSequence2 = this.operatorFriendlyName;
        parcel.writeString(charSequence2 != null ? charSequence2.toString() : str);
        parcel.writeLong(this.flags);
        parcel.writeTypedArray(this.informationElements, i);
        List<String> list = this.anqpLines;
        if (list != null) {
            parcel.writeInt(list.size());
            for (int i3 = 0; i3 < this.anqpLines.size(); i3++) {
                parcel.writeString(this.anqpLines.get(i3));
            }
        } else {
            parcel.writeInt(0);
        }
        AnqpInformationElement[] anqpInformationElementArr = this.anqpElements;
        if (anqpInformationElementArr != null) {
            parcel.writeInt(anqpInformationElementArr.length);
            for (AnqpInformationElement anqpInformationElement : this.anqpElements) {
                parcel.writeInt(anqpInformationElement.getVendorId());
                parcel.writeInt(anqpInformationElement.getElementId());
                parcel.writeInt(anqpInformationElement.getPayload().length);
                parcel.writeByteArray(anqpInformationElement.getPayload());
            }
        } else {
            parcel.writeInt(0);
        }
        RadioChainInfo[] radioChainInfoArr = this.radioChainInfos;
        if (radioChainInfoArr != null) {
            parcel.writeInt(radioChainInfoArr.length);
            while (true) {
                RadioChainInfo[] radioChainInfoArr2 = this.radioChainInfos;
                if (i2 >= radioChainInfoArr2.length) {
                    break;
                }
                parcel.writeInt(radioChainInfoArr2[i2].f55id);
                parcel.writeInt(this.radioChainInfos[i2].level);
                i2++;
            }
        } else {
            parcel.writeInt(0);
        }
        String str2 = this.ifaceName;
        if (str2 != null) {
            str = str2.toString();
        }
        parcel.writeString(str);
        parcel.writeParcelable(this.mApMldMacAddress, i);
        parcel.writeInt(this.mApMloLinkId);
        parcel.writeTypedList(this.mAffiliatedMloLinks);
    }
}
