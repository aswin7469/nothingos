package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.modules.utils.build.SdkLevel;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class NetworkAgentConfig implements Parcelable {
    public static final Parcelable.Creator<NetworkAgentConfig> CREATOR = new Parcelable.Creator<NetworkAgentConfig>() {
        public NetworkAgentConfig createFromParcel(Parcel parcel) {
            NetworkAgentConfig networkAgentConfig = new NetworkAgentConfig();
            boolean z = true;
            networkAgentConfig.allowBypass = parcel.readInt() != 0;
            networkAgentConfig.explicitlySelected = parcel.readInt() != 0;
            networkAgentConfig.acceptUnvalidated = parcel.readInt() != 0;
            networkAgentConfig.acceptPartialConnectivity = parcel.readInt() != 0;
            networkAgentConfig.subscriberId = parcel.readString();
            networkAgentConfig.provisioningNotificationDisabled = parcel.readInt() != 0;
            networkAgentConfig.skip464xlat = parcel.readInt() != 0;
            networkAgentConfig.legacyType = parcel.readInt();
            networkAgentConfig.legacyTypeName = parcel.readString();
            networkAgentConfig.legacySubType = parcel.readInt();
            networkAgentConfig.legacySubTypeName = parcel.readString();
            networkAgentConfig.mLegacyExtraInfo = parcel.readString();
            networkAgentConfig.excludeLocalRouteVpn = parcel.readInt() != 0;
            if (parcel.readInt() == 0) {
                z = false;
            }
            networkAgentConfig.mVpnRequiresValidation = z;
            return networkAgentConfig;
        }

        public NetworkAgentConfig[] newArray(int i) {
            return new NetworkAgentConfig[i];
        }
    };
    public boolean acceptPartialConnectivity;
    public boolean acceptUnvalidated;
    public boolean allowBypass;
    public boolean excludeLocalRouteVpn = false;
    public boolean explicitlySelected;
    public transient boolean hasShownBroken;
    public int legacySubType = -1;
    public String legacySubTypeName = "";
    public int legacyType = -1;
    public String legacyTypeName = "";
    /* access modifiers changed from: private */
    public String mLegacyExtraInfo = "";
    /* access modifiers changed from: private */
    public boolean mVpnRequiresValidation = false;
    public boolean provisioningNotificationDisabled;
    public boolean skip464xlat;
    public String subscriberId;

    public int describeContents() {
        return 0;
    }

    public boolean isExplicitlySelected() {
        return this.explicitlySelected;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean isBypassableVpn() {
        return this.allowBypass;
    }

    public boolean isUnvalidatedConnectivityAcceptable() {
        return this.acceptUnvalidated;
    }

    public boolean isPartialConnectivityAcceptable() {
        return this.acceptPartialConnectivity;
    }

    public boolean isProvisioningNotificationEnabled() {
        return !this.provisioningNotificationDisabled;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String getSubscriberId() {
        return this.subscriberId;
    }

    public boolean isNat64DetectionEnabled() {
        return !this.skip464xlat;
    }

    public int getLegacyType() {
        return this.legacyType;
    }

    public String getLegacyTypeName() {
        return this.legacyTypeName;
    }

    public String getLegacyExtraInfo() {
        return this.mLegacyExtraInfo;
    }

    public boolean areLocalRoutesExcludedForVpn() {
        return this.excludeLocalRouteVpn;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean isVpnValidationRequired() {
        return this.mVpnRequiresValidation;
    }

    public NetworkAgentConfig() {
    }

    public NetworkAgentConfig(NetworkAgentConfig networkAgentConfig) {
        if (networkAgentConfig != null) {
            this.allowBypass = networkAgentConfig.allowBypass;
            this.explicitlySelected = networkAgentConfig.explicitlySelected;
            this.acceptUnvalidated = networkAgentConfig.acceptUnvalidated;
            this.acceptPartialConnectivity = networkAgentConfig.acceptPartialConnectivity;
            this.subscriberId = networkAgentConfig.subscriberId;
            this.provisioningNotificationDisabled = networkAgentConfig.provisioningNotificationDisabled;
            this.skip464xlat = networkAgentConfig.skip464xlat;
            this.legacyType = networkAgentConfig.legacyType;
            this.legacyTypeName = networkAgentConfig.legacyTypeName;
            this.legacySubType = networkAgentConfig.legacySubType;
            this.legacySubTypeName = networkAgentConfig.legacySubTypeName;
            this.mLegacyExtraInfo = networkAgentConfig.mLegacyExtraInfo;
            this.excludeLocalRouteVpn = networkAgentConfig.excludeLocalRouteVpn;
            this.mVpnRequiresValidation = networkAgentConfig.mVpnRequiresValidation;
        }
    }

    public static final class Builder {
        private final NetworkAgentConfig mConfig = new NetworkAgentConfig();

        public Builder setExplicitlySelected(boolean z) {
            this.mConfig.explicitlySelected = z;
            return this;
        }

        public Builder setUnvalidatedConnectivityAcceptable(boolean z) {
            this.mConfig.acceptUnvalidated = z;
            return this;
        }

        public Builder setPartialConnectivityAcceptable(boolean z) {
            this.mConfig.acceptPartialConnectivity = z;
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setSubscriberId(String str) {
            this.mConfig.subscriberId = str;
            return this;
        }

        public Builder setNat64DetectionEnabled(boolean z) {
            this.mConfig.skip464xlat = !z;
            return this;
        }

        public Builder setProvisioningNotificationEnabled(boolean z) {
            this.mConfig.provisioningNotificationDisabled = !z;
            return this;
        }

        public Builder setLegacyType(int i) {
            this.mConfig.legacyType = i;
            return this;
        }

        public Builder setLegacySubType(int i) {
            this.mConfig.legacySubType = i;
            return this;
        }

        public Builder setLegacyTypeName(String str) {
            this.mConfig.legacyTypeName = str;
            return this;
        }

        public Builder setLegacySubTypeName(String str) {
            this.mConfig.legacySubTypeName = str;
            return this;
        }

        public Builder setLegacyExtraInfo(String str) {
            this.mConfig.mLegacyExtraInfo = str;
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setVpnRequiresValidation(boolean z) {
            this.mConfig.mVpnRequiresValidation = z;
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setBypassableVpn(boolean z) {
            this.mConfig.allowBypass = z;
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setLocalRoutesExcludedForVpn(boolean z) {
            if (SdkLevel.isAtLeastT()) {
                this.mConfig.excludeLocalRouteVpn = z;
                return this;
            }
            throw new UnsupportedOperationException("Method is not supported");
        }

        public NetworkAgentConfig build() {
            return this.mConfig;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NetworkAgentConfig networkAgentConfig = (NetworkAgentConfig) obj;
        if (this.allowBypass == networkAgentConfig.allowBypass && this.explicitlySelected == networkAgentConfig.explicitlySelected && this.acceptUnvalidated == networkAgentConfig.acceptUnvalidated && this.acceptPartialConnectivity == networkAgentConfig.acceptPartialConnectivity && this.provisioningNotificationDisabled == networkAgentConfig.provisioningNotificationDisabled && this.skip464xlat == networkAgentConfig.skip464xlat && this.legacyType == networkAgentConfig.legacyType && Objects.equals(this.subscriberId, networkAgentConfig.subscriberId) && Objects.equals(this.legacyTypeName, networkAgentConfig.legacyTypeName) && Objects.equals(this.mLegacyExtraInfo, networkAgentConfig.mLegacyExtraInfo) && this.excludeLocalRouteVpn == networkAgentConfig.excludeLocalRouteVpn && this.mVpnRequiresValidation == networkAgentConfig.mVpnRequiresValidation) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.allowBypass), Boolean.valueOf(this.explicitlySelected), Boolean.valueOf(this.acceptUnvalidated), Boolean.valueOf(this.acceptPartialConnectivity), Boolean.valueOf(this.provisioningNotificationDisabled), this.subscriberId, Boolean.valueOf(this.skip464xlat), Integer.valueOf(this.legacyType), this.legacyTypeName, this.mLegacyExtraInfo, Boolean.valueOf(this.excludeLocalRouteVpn), Boolean.valueOf(this.mVpnRequiresValidation));
    }

    public String toString() {
        return "NetworkAgentConfig { allowBypass = " + this.allowBypass + ", explicitlySelected = " + this.explicitlySelected + ", acceptUnvalidated = " + this.acceptUnvalidated + ", acceptPartialConnectivity = " + this.acceptPartialConnectivity + ", provisioningNotificationDisabled = " + this.provisioningNotificationDisabled + ", subscriberId = '" + this.subscriberId + "', skip464xlat = " + this.skip464xlat + ", legacyType = " + this.legacyType + ", hasShownBroken = " + this.hasShownBroken + ", legacyTypeName = '" + this.legacyTypeName + "', legacyExtraInfo = '" + this.mLegacyExtraInfo + "', excludeLocalRouteVpn = '" + this.excludeLocalRouteVpn + "', vpnRequiresValidation = '" + this.mVpnRequiresValidation + "'}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.allowBypass ? 1 : 0);
        parcel.writeInt(this.explicitlySelected ? 1 : 0);
        parcel.writeInt(this.acceptUnvalidated ? 1 : 0);
        parcel.writeInt(this.acceptPartialConnectivity ? 1 : 0);
        parcel.writeString(this.subscriberId);
        parcel.writeInt(this.provisioningNotificationDisabled ? 1 : 0);
        parcel.writeInt(this.skip464xlat ? 1 : 0);
        parcel.writeInt(this.legacyType);
        parcel.writeString(this.legacyTypeName);
        parcel.writeInt(this.legacySubType);
        parcel.writeString(this.legacySubTypeName);
        parcel.writeString(this.mLegacyExtraInfo);
        parcel.writeInt(this.excludeLocalRouteVpn ? 1 : 0);
        parcel.writeInt(this.mVpnRequiresValidation ? 1 : 0);
    }
}
