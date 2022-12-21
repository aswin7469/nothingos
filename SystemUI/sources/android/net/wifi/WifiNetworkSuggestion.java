package android.net.wifi;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.wifi.p018x.com.android.internal.util.Preconditions;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class WifiNetworkSuggestion implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkSuggestion> CREATOR = new Parcelable.Creator<WifiNetworkSuggestion>() {
        public WifiNetworkSuggestion createFromParcel(Parcel parcel) {
            return new WifiNetworkSuggestion((WifiConfiguration) parcel.readParcelable((ClassLoader) null), (PasspointConfiguration) parcel.readParcelable((ClassLoader) null), parcel.readBoolean(), parcel.readBoolean(), parcel.readBoolean(), parcel.readBoolean(), parcel.readInt());
        }

        public WifiNetworkSuggestion[] newArray(int i) {
            return new WifiNetworkSuggestion[i];
        }
    };
    public static final int RANDOMIZATION_NON_PERSISTENT = 1;
    public static final int RANDOMIZATION_PERSISTENT = 0;
    public final boolean isAppInteractionRequired;
    public final boolean isInitialAutoJoinEnabled;
    public final boolean isUserAllowedToManuallyConnect;
    public final boolean isUserInteractionRequired;
    public final PasspointConfiguration passpointConfiguration;
    public final int priorityGroup;
    public final WifiConfiguration wifiConfiguration;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MacRandomizationSetting {
    }

    public int describeContents() {
        return 0;
    }

    public static final class Builder {
        private static final int UNASSIGNED_PRIORITY = -1;
        private static final int WPA3_ENTERPRISE_192_BIT = 2;
        private static final int WPA3_ENTERPRISE_AUTO = 0;
        private static final int WPA3_ENTERPRISE_STANDARD = 1;
        private MacAddress mBssid = null;
        private int mCarrierId = -1;
        private boolean mIsAppInteractionRequired = false;
        private boolean mIsCarrierMerged = false;
        private boolean mIsEnhancedOpen = false;
        private boolean mIsHiddenSSID = false;
        private boolean mIsInitialAutojoinEnabled = true;
        private boolean mIsNetworkOemPaid = false;
        private boolean mIsNetworkOemPrivate = false;
        private boolean mIsNetworkRestricted = false;
        private boolean mIsNetworkUntrusted = false;
        private boolean mIsSharedWithUser = true;
        private boolean mIsSharedWithUserSet = false;
        private boolean mIsUserInteractionRequired = false;
        private int mMacRandomizationSetting = 0;
        private int mMeteredOverride = 0;
        private PasspointConfiguration mPasspointConfiguration = null;
        private int mPriority = -1;
        private int mPriorityGroup = 0;
        private boolean mSaeH2eOnlyMode = false;
        private ParcelUuid mSubscriptionGroup = null;
        private int mSubscriptionId = -1;
        private WifiEnterpriseConfig mWapiEnterpriseConfig = null;
        private String mWapiPskPassphrase = null;
        private WifiSsid mWifiSsid = null;
        private WifiEnterpriseConfig mWpa2EnterpriseConfig = null;
        private String mWpa2PskPassphrase = null;
        private WifiEnterpriseConfig mWpa3EnterpriseConfig = null;
        private int mWpa3EnterpriseType = 0;
        private String mWpa3SaePassphrase = null;

        public Builder setSsid(String str) {
            Preconditions.checkNotNull(str);
            if (StandardCharsets.UTF_8.newEncoder().canEncode((CharSequence) str)) {
                this.mWifiSsid = WifiSsid.fromUtf8Text(str);
                return this;
            }
            throw new IllegalArgumentException("SSID is not a valid unicode string");
        }

        public Builder setWifiSsid(WifiSsid wifiSsid) {
            Preconditions.checkNotNull(wifiSsid);
            if (wifiSsid.getBytes().length != 0) {
                this.mWifiSsid = WifiSsid.fromBytes(wifiSsid.getBytes());
                return this;
            }
            throw new IllegalArgumentException("Empty WifiSsid is invalid");
        }

        public Builder setBssid(MacAddress macAddress) {
            Preconditions.checkNotNull(macAddress);
            this.mBssid = MacAddress.fromBytes(macAddress.toByteArray());
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
            if (!wifiEnterpriseConfig.isEapMethodServerCertUsed() || wifiEnterpriseConfig.isMandatoryParameterSetForServerCertValidation()) {
                this.mWpa2EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
                return this;
            }
            throw new IllegalArgumentException("Enterprise configuration mandates server certificate but validation is not enabled.");
        }

        @Deprecated
        public Builder setWpa3EnterpriseConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            if (!wifiEnterpriseConfig.isEapMethodServerCertUsed() || wifiEnterpriseConfig.isMandatoryParameterSetForServerCertValidation()) {
                this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
                return this;
            }
            throw new IllegalArgumentException("Enterprise configuration mandates server certificate but validation is not enabled.");
        }

        public Builder setWpa3EnterpriseStandardModeConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            if (!wifiEnterpriseConfig.isEapMethodServerCertUsed() || wifiEnterpriseConfig.isMandatoryParameterSetForServerCertValidation()) {
                this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
                this.mWpa3EnterpriseType = 1;
                return this;
            }
            throw new IllegalArgumentException("Enterprise configuration mandates server certificate but validation is not enabled.");
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

        public Builder setPasspointConfig(PasspointConfiguration passpointConfiguration) {
            Preconditions.checkNotNull(passpointConfiguration);
            if (passpointConfiguration.validate()) {
                this.mPasspointConfiguration = new PasspointConfiguration(passpointConfiguration);
                return this;
            }
            throw new IllegalArgumentException("Passpoint configuration is invalid");
        }

        @SystemApi
        public Builder setCarrierId(int i) {
            this.mCarrierId = i;
            return this;
        }

        public Builder setSubscriptionId(int i) {
            if (!SdkLevel.isAtLeastS()) {
                throw new UnsupportedOperationException();
            } else if (i != -1) {
                this.mSubscriptionId = i;
                return this;
            } else {
                throw new IllegalArgumentException("Subscription Id is invalid");
            }
        }

        public Builder setSubscriptionGroup(ParcelUuid parcelUuid) {
            if (!SdkLevel.isAtLeastT()) {
                throw new UnsupportedOperationException();
            } else if (parcelUuid != null) {
                this.mSubscriptionGroup = parcelUuid;
                return this;
            } else {
                throw new IllegalArgumentException("SubscriptionGroup is invalid");
            }
        }

        public Builder setPriorityGroup(int i) {
            this.mPriorityGroup = i;
            return this;
        }

        public Builder setWapiPassphrase(String str) {
            Preconditions.checkNotNull(str);
            if (StandardCharsets.US_ASCII.newEncoder().canEncode((CharSequence) str)) {
                this.mWapiPskPassphrase = str;
                return this;
            }
            throw new IllegalArgumentException("passphrase not ASCII encodable");
        }

        public Builder setWapiEnterpriseConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
            Preconditions.checkNotNull(wifiEnterpriseConfig);
            this.mWapiEnterpriseConfig = new WifiEnterpriseConfig(wifiEnterpriseConfig);
            return this;
        }

        public Builder setIsHiddenSsid(boolean z) {
            this.mIsHiddenSSID = z;
            return this;
        }

        public Builder setMacRandomizationSetting(int i) {
            if (i == 0 || i == 1) {
                this.mMacRandomizationSetting = i;
                return this;
            }
            throw new IllegalArgumentException();
        }

        public Builder setIsAppInteractionRequired(boolean z) {
            this.mIsAppInteractionRequired = z;
            return this;
        }

        public Builder setIsUserInteractionRequired(boolean z) {
            this.mIsUserInteractionRequired = z;
            return this;
        }

        public Builder setPriority(int i) {
            if (i >= 0) {
                this.mPriority = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid priority value " + i);
        }

        public Builder setIsMetered(boolean z) {
            if (z) {
                this.mMeteredOverride = 1;
            } else {
                this.mMeteredOverride = 2;
            }
            return this;
        }

        public Builder setCredentialSharedWithUser(boolean z) {
            this.mIsSharedWithUser = z;
            this.mIsSharedWithUserSet = true;
            return this;
        }

        public Builder setIsInitialAutojoinEnabled(boolean z) {
            this.mIsInitialAutojoinEnabled = z;
            return this;
        }

        public Builder setUntrusted(boolean z) {
            this.mIsNetworkUntrusted = z;
            return this;
        }

        public Builder setRestricted(boolean z) {
            this.mIsNetworkRestricted = z;
            return this;
        }

        @SystemApi
        public Builder setOemPaid(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mIsNetworkOemPaid = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        @SystemApi
        public Builder setOemPrivate(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mIsNetworkOemPrivate = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setCarrierMerged(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mIsCarrierMerged = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        public Builder setIsWpa3SaeH2eOnlyModeEnabled(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mSaeH2eOnlyMode = z;
                return this;
            }
            throw new UnsupportedOperationException();
        }

        private void setSecurityParamsInWifiConfiguration(WifiConfiguration wifiConfiguration) {
            if (!TextUtils.isEmpty(this.mWpa2PskPassphrase)) {
                wifiConfiguration.setSecurityParams(2);
                wifiConfiguration.preSharedKey = "\"" + this.mWpa2PskPassphrase + "\"";
            } else if (!TextUtils.isEmpty(this.mWpa3SaePassphrase)) {
                wifiConfiguration.setSecurityParams(4);
                wifiConfiguration.preSharedKey = "\"" + this.mWpa3SaePassphrase + "\"";
                boolean z = this.mSaeH2eOnlyMode;
                if (z) {
                    wifiConfiguration.enableSaeH2eOnlyMode(z);
                }
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
                } else if (!TextUtils.isEmpty(this.mWapiPskPassphrase)) {
                    wifiConfiguration.setSecurityParams(7);
                    wifiConfiguration.preSharedKey = "\"" + this.mWapiPskPassphrase + "\"";
                } else if (this.mWapiEnterpriseConfig != null) {
                    wifiConfiguration.setSecurityParams(8);
                    wifiConfiguration.enterpriseConfig = this.mWapiEnterpriseConfig;
                } else {
                    wifiConfiguration.setSecurityParams(0);
                }
            }
        }

        private WifiConfiguration buildWifiConfiguration() {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = this.mWifiSsid.toString();
            MacAddress macAddress = this.mBssid;
            if (macAddress != null) {
                wifiConfiguration.BSSID = macAddress.toString();
            }
            setSecurityParamsInWifiConfiguration(wifiConfiguration);
            wifiConfiguration.hiddenSSID = this.mIsHiddenSSID;
            wifiConfiguration.priority = this.mPriority;
            wifiConfiguration.meteredOverride = this.mMeteredOverride;
            wifiConfiguration.carrierId = this.mCarrierId;
            int i = 1;
            wifiConfiguration.trusted = !this.mIsNetworkUntrusted;
            wifiConfiguration.oemPaid = this.mIsNetworkOemPaid;
            wifiConfiguration.oemPrivate = this.mIsNetworkOemPrivate;
            wifiConfiguration.carrierMerged = this.mIsCarrierMerged;
            if (this.mMacRandomizationSetting == 1) {
                i = 2;
            }
            wifiConfiguration.macRandomizationSetting = i;
            wifiConfiguration.subscriptionId = this.mSubscriptionId;
            wifiConfiguration.restricted = this.mIsNetworkRestricted;
            wifiConfiguration.setSubscriptionGroup(this.mSubscriptionGroup);
            return wifiConfiguration;
        }

        private void validateSecurityParams() {
            int i = 0;
            int i2 = (this.mIsEnhancedOpen ? 1 : 0) + false + (TextUtils.isEmpty(this.mWpa2PskPassphrase) ^ true ? 1 : 0) + (TextUtils.isEmpty(this.mWpa3SaePassphrase) ^ true ? 1 : 0) + (TextUtils.isEmpty(this.mWapiPskPassphrase) ^ true ? 1 : 0) + (this.mWpa2EnterpriseConfig != null ? 1 : 0) + (this.mWpa3EnterpriseConfig != null ? 1 : 0) + (this.mWapiEnterpriseConfig != null ? 1 : 0);
            if (this.mPasspointConfiguration != null) {
                i = 1;
            }
            if (i2 + i > 1) {
                throw new IllegalStateException("only one of setIsEnhancedOpen, setWpa2Passphrase, setWpa3Passphrase, setWpa2EnterpriseConfig, setWpa3EnterpriseConfig setWapiPassphrase, setWapiCertSuite, setIsWapiCertSuiteAuto or setPasspointConfig can be invoked for network suggestion");
            }
        }

        private WifiConfiguration buildWifiConfigurationForPasspoint() {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.FQDN = this.mPasspointConfiguration.getHomeSp().getFqdn();
            wifiConfiguration.setPasspointUniqueId(this.mPasspointConfiguration.getUniqueId());
            wifiConfiguration.priority = this.mPriority;
            wifiConfiguration.meteredOverride = this.mMeteredOverride;
            boolean z = true;
            wifiConfiguration.trusted = !this.mIsNetworkUntrusted;
            wifiConfiguration.oemPaid = this.mIsNetworkOemPaid;
            wifiConfiguration.oemPrivate = this.mIsNetworkOemPrivate;
            wifiConfiguration.carrierMerged = this.mIsCarrierMerged;
            wifiConfiguration.carrierId = this.mCarrierId;
            wifiConfiguration.subscriptionId = this.mSubscriptionId;
            wifiConfiguration.macRandomizationSetting = this.mMacRandomizationSetting == 1 ? 2 : 1;
            wifiConfiguration.restricted = this.mIsNetworkRestricted;
            wifiConfiguration.setSubscriptionGroup(this.mSubscriptionGroup);
            this.mPasspointConfiguration.setCarrierId(this.mCarrierId);
            this.mPasspointConfiguration.setSubscriptionId(this.mSubscriptionId);
            this.mPasspointConfiguration.setSubscriptionGroup(this.mSubscriptionGroup);
            this.mPasspointConfiguration.setMeteredOverride(wifiConfiguration.meteredOverride);
            this.mPasspointConfiguration.setOemPrivate(this.mIsNetworkOemPrivate);
            this.mPasspointConfiguration.setOemPaid(this.mIsNetworkOemPaid);
            this.mPasspointConfiguration.setCarrierMerged(this.mIsCarrierMerged);
            this.mPasspointConfiguration.setMacRandomizationEnabled(true);
            PasspointConfiguration passpointConfiguration = this.mPasspointConfiguration;
            if (this.mMacRandomizationSetting != 1) {
                z = false;
            }
            passpointConfiguration.setNonPersistentMacRandomizationEnabled(z);
            return wifiConfiguration;
        }

        public WifiNetworkSuggestion build() {
            WifiConfiguration buildWifiConfiguration;
            validateSecurityParams();
            if (this.mPasspointConfiguration == null) {
                WifiSsid wifiSsid = this.mWifiSsid;
                if (wifiSsid == null) {
                    throw new IllegalStateException("setSsid should be invoked for suggestion");
                } else if (wifiSsid.getBytes().length != 0) {
                    MacAddress macAddress = this.mBssid;
                    if (macAddress != null && (macAddress.equals(MacAddress.BROADCAST_ADDRESS) || this.mBssid.equals(WifiManager.ALL_ZEROS_MAC_ADDRESS))) {
                        throw new IllegalStateException("invalid bssid for suggestion");
                    } else if (!TextUtils.isEmpty(this.mWpa3SaePassphrase) || !this.mSaeH2eOnlyMode) {
                        buildWifiConfiguration = buildWifiConfiguration();
                        if (buildWifiConfiguration.isOpenNetwork()) {
                            if (!this.mIsSharedWithUserSet || !this.mIsSharedWithUser) {
                                this.mIsSharedWithUser = false;
                            } else {
                                throw new IllegalStateException("Open network should not be setCredentialSharedWithUser to true");
                            }
                        }
                    } else {
                        throw new IllegalStateException("Hash-to-Element only mode is only allowed for the SAE network");
                    }
                } else {
                    throw new IllegalStateException("invalid ssid for suggestion");
                }
            } else if (this.mWifiSsid != null) {
                throw new IllegalStateException("setSsid should not be invoked for suggestion with Passpoint configuration");
            } else if (!this.mIsHiddenSSID) {
                buildWifiConfiguration = buildWifiConfigurationForPasspoint();
            } else {
                throw new IllegalStateException("setIsHiddenSsid should not be invoked for suggestion with Passpoint configuration");
            }
            WifiConfiguration wifiConfiguration = buildWifiConfiguration;
            boolean z = this.mIsSharedWithUser;
            if (z || this.mIsInitialAutojoinEnabled) {
                if (this.mIsNetworkUntrusted || this.mIsNetworkRestricted) {
                    if (!this.mIsSharedWithUserSet || !z) {
                        this.mIsSharedWithUser = false;
                    } else {
                        throw new IllegalStateException("Should not be bothsetCredentialSharedWithUser and +setUntrusted or setRestricted to true");
                    }
                }
                if (this.mIsNetworkOemPaid) {
                    if (!this.mIsSharedWithUserSet || !this.mIsSharedWithUser) {
                        this.mIsSharedWithUser = false;
                    } else {
                        throw new IllegalStateException("Should not be bothsetCredentialSharedWithUser and +setOemPaid to true");
                    }
                }
                if (this.mIsNetworkOemPrivate) {
                    if (!this.mIsSharedWithUserSet || !this.mIsSharedWithUser) {
                        this.mIsSharedWithUser = false;
                    } else {
                        throw new IllegalStateException("Should not be bothsetCredentialSharedWithUser and +setOemPrivate to true");
                    }
                }
                if (this.mIsCarrierMerged && ((this.mSubscriptionId == -1 && this.mSubscriptionGroup == null) || this.mMeteredOverride != 1 || !isEnterpriseSuggestion())) {
                    throw new IllegalStateException("A carrier merged network must be a metered, enterprise network with valid subscription Id");
                } else if (this.mSubscriptionGroup == null || this.mSubscriptionId == -1) {
                    return new WifiNetworkSuggestion(wifiConfiguration, this.mPasspointConfiguration, this.mIsAppInteractionRequired, this.mIsUserInteractionRequired, this.mIsSharedWithUser, this.mIsInitialAutojoinEnabled, this.mPriorityGroup);
                } else {
                    throw new IllegalStateException("Should not be set both SubscriptionGroup and SubscriptionId");
                }
            } else {
                throw new IllegalStateException("Should have not a network with both setCredentialSharedWithUser and setIsAutojoinEnabled set to false");
            }
        }

        private boolean isEnterpriseSuggestion() {
            return (this.mWpa2EnterpriseConfig == null && this.mWpa3EnterpriseConfig == null && this.mWapiEnterpriseConfig == null && this.mPasspointConfiguration == null) ? false : true;
        }
    }

    public WifiNetworkSuggestion() {
        this.wifiConfiguration = new WifiConfiguration();
        this.passpointConfiguration = null;
        this.isAppInteractionRequired = false;
        this.isUserInteractionRequired = false;
        this.isUserAllowedToManuallyConnect = true;
        this.isInitialAutoJoinEnabled = true;
        this.priorityGroup = 0;
    }

    public WifiNetworkSuggestion(WifiConfiguration wifiConfiguration2, PasspointConfiguration passpointConfiguration2, boolean z, boolean z2, boolean z3, boolean z4, int i) {
        Preconditions.checkNotNull(wifiConfiguration2);
        this.wifiConfiguration = wifiConfiguration2;
        this.passpointConfiguration = passpointConfiguration2;
        this.isAppInteractionRequired = z;
        this.isUserInteractionRequired = z2;
        this.isUserAllowedToManuallyConnect = z3;
        this.isInitialAutoJoinEnabled = z4;
        this.priorityGroup = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.wifiConfiguration, i);
        parcel.writeParcelable(this.passpointConfiguration, i);
        parcel.writeBoolean(this.isAppInteractionRequired);
        parcel.writeBoolean(this.isUserInteractionRequired);
        parcel.writeBoolean(this.isUserAllowedToManuallyConnect);
        parcel.writeBoolean(this.isInitialAutoJoinEnabled);
        parcel.writeInt(this.priorityGroup);
    }

    public int hashCode() {
        return Objects.hash(this.wifiConfiguration.SSID, this.wifiConfiguration.BSSID, this.wifiConfiguration.getDefaultSecurityType(), this.wifiConfiguration.getPasspointUniqueId(), Integer.valueOf(this.wifiConfiguration.subscriptionId), Integer.valueOf(this.wifiConfiguration.carrierId), this.wifiConfiguration.getSubscriptionGroup());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiNetworkSuggestion)) {
            return false;
        }
        WifiNetworkSuggestion wifiNetworkSuggestion = (WifiNetworkSuggestion) obj;
        if ((this.passpointConfiguration == null) ^ (wifiNetworkSuggestion.passpointConfiguration == null)) {
            return false;
        }
        if (!TextUtils.equals(this.wifiConfiguration.SSID, wifiNetworkSuggestion.wifiConfiguration.SSID) || !TextUtils.equals(this.wifiConfiguration.BSSID, wifiNetworkSuggestion.wifiConfiguration.BSSID) || !TextUtils.equals(this.wifiConfiguration.getDefaultSecurityType(), wifiNetworkSuggestion.wifiConfiguration.getDefaultSecurityType()) || !TextUtils.equals(this.wifiConfiguration.getPasspointUniqueId(), wifiNetworkSuggestion.wifiConfiguration.getPasspointUniqueId()) || this.wifiConfiguration.carrierId != wifiNetworkSuggestion.wifiConfiguration.carrierId || this.wifiConfiguration.subscriptionId != wifiNetworkSuggestion.wifiConfiguration.subscriptionId || !Objects.equals(this.wifiConfiguration.getSubscriptionGroup(), wifiNetworkSuggestion.wifiConfiguration.getSubscriptionGroup())) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("WifiNetworkSuggestion[ ");
        sb.append("SSID=");
        sb.append(this.wifiConfiguration.SSID);
        sb.append(", BSSID=");
        sb.append(this.wifiConfiguration.BSSID);
        sb.append(", FQDN=");
        sb.append(this.wifiConfiguration.FQDN);
        sb.append(", SecurityParams=");
        this.wifiConfiguration.getSecurityParamsList().stream().forEach(new WifiNetworkSuggestion$$ExternalSyntheticLambda0(sb));
        sb.append(", isAppInteractionRequired=");
        sb.append(this.isAppInteractionRequired);
        sb.append(", isUserInteractionRequired=");
        sb.append(this.isUserInteractionRequired);
        sb.append(", isCredentialSharedWithUser=");
        sb.append(this.isUserAllowedToManuallyConnect);
        sb.append(", isInitialAutoJoinEnabled=");
        sb.append(this.isInitialAutoJoinEnabled);
        sb.append(", isUnTrusted=");
        sb.append(!this.wifiConfiguration.trusted);
        sb.append(", isOemPaid=");
        sb.append(this.wifiConfiguration.oemPaid);
        sb.append(", isOemPrivate=");
        sb.append(this.wifiConfiguration.oemPrivate);
        sb.append(", isCarrierMerged=");
        sb.append(this.wifiConfiguration.carrierMerged);
        sb.append(", isHiddenSsid=");
        sb.append(this.wifiConfiguration.hiddenSSID);
        sb.append(", priorityGroup=");
        sb.append(this.priorityGroup);
        sb.append(", subscriptionId=");
        sb.append(this.wifiConfiguration.subscriptionId);
        sb.append(", subscriptionGroup=");
        sb.append((Object) this.wifiConfiguration.getSubscriptionGroup());
        sb.append(", carrierId=");
        sb.append(this.wifiConfiguration.carrierId);
        sb.append(", priority=");
        sb.append(this.wifiConfiguration.priority);
        sb.append(", meteredness=");
        sb.append(this.wifiConfiguration.meteredOverride);
        sb.append(", restricted=");
        sb.append(this.wifiConfiguration.restricted);
        sb.append(" ]");
        return sb.toString();
    }

    static /* synthetic */ void lambda$toString$0(StringBuilder sb, SecurityParams securityParams) {
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(WifiConfiguration.getSecurityTypeName(securityParams.getSecurityType()));
        if (securityParams.isAddedByAutoUpgrade()) {
            sb.append("^");
        }
    }

    @SystemApi
    public WifiConfiguration getWifiConfiguration() {
        return this.wifiConfiguration;
    }

    public MacAddress getBssid() {
        if (this.wifiConfiguration.BSSID == null) {
            return null;
        }
        return MacAddress.fromString(this.wifiConfiguration.BSSID);
    }

    public boolean isCredentialSharedWithUser() {
        return this.isUserAllowedToManuallyConnect;
    }

    public boolean isAppInteractionRequired() {
        return this.isAppInteractionRequired;
    }

    public boolean isEnhancedOpen() {
        return this.wifiConfiguration.allowedKeyManagement.get(9);
    }

    public boolean isHiddenSsid() {
        return this.wifiConfiguration.hiddenSSID;
    }

    public boolean isInitialAutojoinEnabled() {
        return this.isInitialAutoJoinEnabled;
    }

    public boolean isMetered() {
        return this.wifiConfiguration.meteredOverride == 1;
    }

    public boolean isUserInteractionRequired() {
        return this.isUserInteractionRequired;
    }

    public PasspointConfiguration getPasspointConfig() {
        return this.passpointConfiguration;
    }

    public int getPriority() {
        return this.wifiConfiguration.priority;
    }

    public String getSsid() {
        if (this.wifiConfiguration.SSID == null) {
            return null;
        }
        try {
            WifiSsid fromString = WifiSsid.fromString(this.wifiConfiguration.SSID);
            if (fromString.getUtf8Text() == null) {
                return null;
            }
            return fromString.getUtf8Text().toString();
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public WifiSsid getWifiSsid() {
        if (this.wifiConfiguration.SSID == null) {
            return null;
        }
        try {
            return WifiSsid.fromString(this.wifiConfiguration.SSID);
        } catch (IllegalArgumentException unused) {
            throw new IllegalStateException("Invalid SSID in the network suggestion");
        }
    }

    public boolean isUntrusted() {
        return !this.wifiConfiguration.trusted;
    }

    public boolean isRestricted() {
        return this.wifiConfiguration.restricted;
    }

    @SystemApi
    public boolean isOemPaid() {
        if (SdkLevel.isAtLeastS()) {
            return this.wifiConfiguration.oemPaid;
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public boolean isOemPrivate() {
        if (SdkLevel.isAtLeastS()) {
            return this.wifiConfiguration.oemPrivate;
        }
        throw new UnsupportedOperationException();
    }

    public boolean isCarrierMerged() {
        if (SdkLevel.isAtLeastS()) {
            return this.wifiConfiguration.carrierMerged;
        }
        throw new UnsupportedOperationException();
    }

    public WifiEnterpriseConfig getEnterpriseConfig() {
        if (!this.wifiConfiguration.isEnterprise()) {
            return null;
        }
        return this.wifiConfiguration.enterpriseConfig;
    }

    public String getPassphrase() {
        if (this.wifiConfiguration.preSharedKey == null) {
            return null;
        }
        return WifiInfo.removeDoubleQuotes(this.wifiConfiguration.preSharedKey);
    }

    public int getPriorityGroup() {
        return this.priorityGroup;
    }

    public int getSubscriptionId() {
        if (SdkLevel.isAtLeastS()) {
            return this.wifiConfiguration.subscriptionId;
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public int getCarrierId() {
        return this.wifiConfiguration.carrierId;
    }

    public int getMacRandomizationSetting() {
        return this.wifiConfiguration.macRandomizationSetting == 2 ? 1 : 0;
    }

    public ParcelUuid getSubscriptionGroup() {
        if (SdkLevel.isAtLeastT()) {
            return this.wifiConfiguration.getSubscriptionGroup();
        }
        throw new UnsupportedOperationException();
    }
}
