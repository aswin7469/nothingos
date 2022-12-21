package android.net.wifi;

import android.net.MacAddress;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.internal.util.Preconditions;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class WifiNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkSpecifier> CREATOR = new Parcelable.Creator<WifiNetworkSpecifier>() {
        public WifiNetworkSpecifier createFromParcel(Parcel parcel) {
            return new WifiNetworkSpecifier((PatternMatcher) parcel.readParcelable((ClassLoader) null), Pair.create((MacAddress) parcel.readParcelable((ClassLoader) null), (MacAddress) parcel.readParcelable((ClassLoader) null)), parcel.readInt(), (WifiConfiguration) parcel.readParcelable((ClassLoader) null));
        }

        public WifiNetworkSpecifier[] newArray(int i) {
            return new WifiNetworkSpecifier[i];
        }
    };
    private static final String TAG = "WifiNetworkSpecifier";
    public final Pair<MacAddress, MacAddress> bssidPatternMatcher;
    private final int mBand;
    public final PatternMatcher ssidPatternMatcher;
    public final WifiConfiguration wifiConfiguration;

    public static boolean validateBand(int i) {
        return i == -1 || i == 8 || i == 16 || i == 1 || i == 2;
    }

    public int describeContents() {
        return 0;
    }

    public static int getBand(int i) {
        if (ScanResult.is24GHz(i)) {
            return 1;
        }
        if (ScanResult.is5GHz(i)) {
            return 2;
        }
        if (ScanResult.is6GHz(i)) {
            return 8;
        }
        return ScanResult.is60GHz(i) ? 16 : -1;
    }

    public static final class Builder {
        private static final Pair<MacAddress, MacAddress> MATCH_ALL_BSSID_PATTERN = new Pair<>(WifiManager.ALL_ZEROS_MAC_ADDRESS, WifiManager.ALL_ZEROS_MAC_ADDRESS);
        private static final String MATCH_ALL_SSID_PATTERN_PATH = ".*";
        private static final String MATCH_EMPTY_SSID_PATTERN_PATH = "";
        private static final MacAddress MATCH_EXACT_BSSID_PATTERN_MASK = MacAddress.BROADCAST_ADDRESS;
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN1 = new Pair<>(MacAddress.BROADCAST_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN2 = new Pair<>(WifiManager.ALL_ZEROS_MAC_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private static final int WPA3_ENTERPRISE_192_BIT = 2;
        private static final int WPA3_ENTERPRISE_AUTO = 0;
        private static final int WPA3_ENTERPRISE_STANDARD = 1;
        private int mBand = -1;
        private Pair<MacAddress, MacAddress> mBssidPatternMatcher = null;
        private boolean mIsEnhancedOpen = false;
        private boolean mIsHiddenSSID = false;
        private PatternMatcher mSsidPatternMatcher = null;
        private WifiEnterpriseConfig mWpa2EnterpriseConfig = null;
        private String mWpa2PskPassphrase = null;
        private WifiEnterpriseConfig mWpa3EnterpriseConfig = null;
        private int mWpa3EnterpriseType = 0;
        private String mWpa3SaePassphrase = null;

        public Builder setSsidPattern(PatternMatcher patternMatcher) {
            Preconditions.checkNotNull(patternMatcher);
            this.mSsidPatternMatcher = patternMatcher;
            return this;
        }

        public Builder setSsid(String str) {
            Preconditions.checkNotNull(str);
            if (StandardCharsets.UTF_8.newEncoder().canEncode((CharSequence) str)) {
                this.mSsidPatternMatcher = new PatternMatcher(str, 0);
                return this;
            }
            throw new IllegalArgumentException("SSID is not a valid unicode string");
        }

        public Builder setBssidPattern(MacAddress macAddress, MacAddress macAddress2) {
            Preconditions.checkNotNull(macAddress);
            Preconditions.checkNotNull(macAddress2);
            this.mBssidPatternMatcher = Pair.create(macAddress, macAddress2);
            return this;
        }

        public Builder setBssid(MacAddress macAddress) {
            Preconditions.checkNotNull(macAddress);
            this.mBssidPatternMatcher = Pair.create(macAddress, MATCH_EXACT_BSSID_PATTERN_MASK);
            return this;
        }

        public Builder setIsEnhancedOpen(boolean z) {
            this.mIsEnhancedOpen = z;
            return this;
        }

        public Builder setWpa2Passphrase(String str) {
            Preconditions.checkNotNull(str);
            if (StandardCharsets.US_ASCII.newEncoder().canEncode((CharSequence) str)) {
                this.mWpa2PskPassphrase = str;
                return this;
            }
            throw new IllegalArgumentException("passphrase not ASCII encodable");
        }

        public Builder setWpa3Passphrase(String str) {
            Preconditions.checkNotNull(str);
            if (StandardCharsets.US_ASCII.newEncoder().canEncode((CharSequence) str)) {
                this.mWpa3SaePassphrase = str;
                return this;
            }
            throw new IllegalArgumentException("passphrase not ASCII encodable");
        }

        public Builder setWpa2EnterpriseConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            this.mWpa2EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
            return this;
        }

        @Deprecated
        public Builder setWpa3EnterpriseConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
            return this;
        }

        public Builder setWpa3EnterpriseStandardModeConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
            this.mWpa3EnterpriseType = 1;
            return this;
        }

        public Builder setWpa3Enterprise192BitModeConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            if (wifiEnterpriseConfig.getEapMethod() != 1) {
                throw new IllegalArgumentException("The 192-bit mode network type must be TLS");
            } else if (!WifiEnterpriseConfig.isSuiteBCipherCert(wifiEnterpriseConfig.getClientCertificate())) {
                throw new IllegalArgumentException("The client certificate does not meet 192-bit mode requirements.");
            } else if (WifiEnterpriseConfig.isSuiteBCipherCert(wifiEnterpriseConfig.getCaCertificate())) {
                this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
                this.mWpa3EnterpriseType = 2;
                return this;
            } else {
                throw new IllegalArgumentException("The CA certificate does not meet 192-bit mode requirements.");
            }
        }

        public Builder setIsHiddenSsid(boolean z) {
            this.mIsHiddenSSID = z;
            return this;
        }

        public Builder setBand(int i) {
            if (WifiNetworkSpecifier.validateBand(i)) {
                this.mBand = i;
                return this;
            }
            throw new IllegalArgumentException("Unexpected band in setBand : " + i);
        }

        private void setSecurityParamsInWifiConfiguration(WifiConfiguration wifiConfiguration) {
            if (!TextUtils.isEmpty(this.mWpa2PskPassphrase)) {
                wifiConfiguration.setSecurityParams(2);
                wifiConfiguration.preSharedKey = "\"" + this.mWpa2PskPassphrase + "\"";
            } else if (!TextUtils.isEmpty(this.mWpa3SaePassphrase)) {
                wifiConfiguration.setSecurityParams(4);
                wifiConfiguration.preSharedKey = "\"" + this.mWpa3SaePassphrase + "\"";
            } else if (this.mWpa2EnterpriseConfig != null) {
                wifiConfiguration.setSecurityParams(3);
                wifiConfiguration.enterpriseConfig = this.mWpa2EnterpriseConfig;
            } else {
                WifiEnterpriseConfig wifiEnterpriseConfig = this.mWpa3EnterpriseConfig;
                if (wifiEnterpriseConfig != null) {
                    if (this.mWpa3EnterpriseType == 0 && wifiEnterpriseConfig.getEapMethod() == 1 && WifiEnterpriseConfig.isSuiteBCipherCert(this.mWpa3EnterpriseConfig.getClientCertificate()) && WifiEnterpriseConfig.isSuiteBCipherCert(this.mWpa3EnterpriseConfig.getCaCertificate())) {
                        wifiConfiguration.setSecurityParams(5);
                    } else if (this.mWpa3EnterpriseType == 2) {
                        wifiConfiguration.setSecurityParams(5);
                    } else {
                        wifiConfiguration.setSecurityParams(9);
                    }
                    wifiConfiguration.enterpriseConfig = this.mWpa3EnterpriseConfig;
                } else if (this.mIsEnhancedOpen) {
                    wifiConfiguration.setSecurityParams(6);
                } else {
                    wifiConfiguration.setSecurityParams(0);
                }
            }
        }

        private WifiConfiguration buildWifiConfiguration() {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            if (this.mSsidPatternMatcher.getType() == 0) {
                wifiConfiguration.SSID = "\"" + this.mSsidPatternMatcher.getPath() + "\"";
            }
            if (this.mBssidPatternMatcher.second == MATCH_EXACT_BSSID_PATTERN_MASK) {
                wifiConfiguration.BSSID = ((MacAddress) this.mBssidPatternMatcher.first).toString();
            }
            setSecurityParamsInWifiConfiguration(wifiConfiguration);
            wifiConfiguration.hiddenSSID = this.mIsHiddenSSID;
            return wifiConfiguration;
        }

        private boolean hasSetAnyPattern() {
            return (this.mSsidPatternMatcher == null && this.mBssidPatternMatcher == null) ? false : true;
        }

        private void setMatchAnyPatternIfUnset() {
            if (this.mSsidPatternMatcher == null) {
                this.mSsidPatternMatcher = new PatternMatcher(MATCH_ALL_SSID_PATTERN_PATH, 2);
            }
            if (this.mBssidPatternMatcher == null) {
                this.mBssidPatternMatcher = MATCH_ALL_BSSID_PATTERN;
            }
        }

        private boolean hasSetMatchNonePattern() {
            if ((this.mSsidPatternMatcher.getType() == 1 || !this.mSsidPatternMatcher.getPath().equals("")) && !this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN1) && !this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN2)) {
                return false;
            }
            return true;
        }

        private boolean hasSetMatchAllPattern() {
            return this.mSsidPatternMatcher.match("") && this.mBssidPatternMatcher.equals(MATCH_ALL_BSSID_PATTERN);
        }

        private void validateSecurityParams() {
            int i = 0;
            int i2 = (this.mIsEnhancedOpen ? 1 : 0) + false + (TextUtils.isEmpty(this.mWpa2PskPassphrase) ^ true ? 1 : 0) + (TextUtils.isEmpty(this.mWpa3SaePassphrase) ^ true ? 1 : 0) + (this.mWpa2EnterpriseConfig != null ? 1 : 0);
            if (this.mWpa3EnterpriseConfig != null) {
                i = 1;
            }
            if (i2 + i > 1) {
                throw new IllegalStateException("only one of setIsEnhancedOpen, setWpa2Passphrase,setWpa3Passphrase, setWpa2EnterpriseConfig or setWpa3EnterpriseConfig can be invoked for network specifier");
            }
        }

        public WifiNetworkSpecifier build() {
            if (hasSetAnyPattern() || this.mBand != -1) {
                setMatchAnyPatternIfUnset();
                if (hasSetMatchNonePattern()) {
                    throw new IllegalStateException("cannot set match-none pattern for specifier");
                } else if (hasSetMatchAllPattern() && this.mBand == -1) {
                    throw new IllegalStateException("cannot set match-all pattern for specifier");
                } else if (!this.mIsHiddenSSID || this.mSsidPatternMatcher.getType() == 0) {
                    validateSecurityParams();
                    return new WifiNetworkSpecifier(this.mSsidPatternMatcher, this.mBssidPatternMatcher, this.mBand, buildWifiConfiguration());
                } else {
                    throw new IllegalStateException("setSsid should also be invoked when setIsHiddenSsid is invoked for network specifier");
                }
            } else {
                throw new IllegalStateException("one of setSsidPattern/setSsid/setBssidPattern/setBssid/setBand should be invoked for specifier");
            }
        }
    }

    public WifiNetworkSpecifier() throws IllegalAccessException {
        throw new IllegalAccessException("Use the builder to create an instance");
    }

    public WifiNetworkSpecifier(PatternMatcher patternMatcher, Pair<MacAddress, MacAddress> pair, int i, WifiConfiguration wifiConfiguration2) {
        Preconditions.checkNotNull(patternMatcher);
        Preconditions.checkNotNull(pair);
        Preconditions.checkNotNull(wifiConfiguration2);
        this.ssidPatternMatcher = patternMatcher;
        this.bssidPatternMatcher = pair;
        this.mBand = i;
        this.wifiConfiguration = wifiConfiguration2;
    }

    public int getBand() {
        return this.mBand;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.ssidPatternMatcher, i);
        parcel.writeParcelable((Parcelable) this.bssidPatternMatcher.first, i);
        parcel.writeParcelable((Parcelable) this.bssidPatternMatcher.second, i);
        parcel.writeInt(this.mBand);
        parcel.writeParcelable(this.wifiConfiguration, i);
    }

    public int hashCode() {
        return Objects.hash(this.ssidPatternMatcher.getPath(), Integer.valueOf(this.ssidPatternMatcher.getType()), this.bssidPatternMatcher, Integer.valueOf(this.mBand), this.wifiConfiguration.allowedKeyManagement);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiNetworkSpecifier)) {
            return false;
        }
        WifiNetworkSpecifier wifiNetworkSpecifier = (WifiNetworkSpecifier) obj;
        if (!Objects.equals(this.ssidPatternMatcher.getPath(), wifiNetworkSpecifier.ssidPatternMatcher.getPath()) || !Objects.equals(Integer.valueOf(this.ssidPatternMatcher.getType()), Integer.valueOf(wifiNetworkSpecifier.ssidPatternMatcher.getType())) || !Objects.equals(this.bssidPatternMatcher, wifiNetworkSpecifier.bssidPatternMatcher) || this.mBand != wifiNetworkSpecifier.mBand || !Objects.equals(this.wifiConfiguration.allowedKeyManagement, wifiNetworkSpecifier.wifiConfiguration.allowedKeyManagement)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "WifiNetworkSpecifier [, SSID Match pattern=" + this.ssidPatternMatcher + ", BSSID Match pattern=" + this.bssidPatternMatcher + ", SSID=" + this.wifiConfiguration.SSID + ", BSSID=" + this.wifiConfiguration.BSSID + ", band=" + this.mBand + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier instanceof WifiNetworkAgentSpecifier) {
            return ((WifiNetworkAgentSpecifier) networkSpecifier).satisfiesNetworkSpecifier(this);
        }
        return equals(networkSpecifier);
    }

    public NetworkSpecifier redact() {
        if (!SdkLevel.isAtLeastS()) {
            return this;
        }
        return new Builder().setBand(this.mBand).build();
    }
}
