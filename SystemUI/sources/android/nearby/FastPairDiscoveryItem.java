package android.nearby;

import android.nearby.aidl.FastPairDiscoveryItemParcel;

public class FastPairDiscoveryItem {
    FastPairDiscoveryItemParcel mMetadataParcel;

    FastPairDiscoveryItem(FastPairDiscoveryItemParcel fastPairDiscoveryItemParcel) {
        this.mMetadataParcel = fastPairDiscoveryItemParcel;
    }

    public String getId() {
        return this.mMetadataParcel.f39id;
    }

    public String getMacAddress() {
        return this.mMetadataParcel.macAddress;
    }

    public String getActionUrl() {
        return this.mMetadataParcel.actionUrl;
    }

    public String getDeviceName() {
        return this.mMetadataParcel.deviceName;
    }

    public String getTitle() {
        return this.mMetadataParcel.title;
    }

    public String getDescription() {
        return this.mMetadataParcel.description;
    }

    public String getDisplayUrl() {
        return this.mMetadataParcel.displayUrl;
    }

    public long getLastObservationTimestampMillis() {
        return this.mMetadataParcel.lastObservationTimestampMillis;
    }

    public long getFirstObservationTimestampMillis() {
        return this.mMetadataParcel.firstObservationTimestampMillis;
    }

    public int getState() {
        return this.mMetadataParcel.state;
    }

    public int getActionUrlType() {
        return this.mMetadataParcel.actionUrlType;
    }

    public int getRssi() {
        return this.mMetadataParcel.rssi;
    }

    public long getPendingAppInstallTimestampMillis() {
        return this.mMetadataParcel.pendingAppInstallTimestampMillis;
    }

    public int getTxPower() {
        return this.mMetadataParcel.txPower;
    }

    public String getAppName() {
        return this.mMetadataParcel.appName;
    }

    public String getPackageName() {
        return this.mMetadataParcel.packageName;
    }

    public String getTriggerId() {
        return this.mMetadataParcel.triggerId;
    }

    public byte[] getIconPng() {
        return this.mMetadataParcel.iconPng;
    }

    public String getIconFfeUrl() {
        return this.mMetadataParcel.iconFifeUrl;
    }

    public byte[] getAuthenticationPublicKeySecp256r1() {
        return this.mMetadataParcel.authenticationPublicKeySecp256r1;
    }

    public static final class Builder {
        private final FastPairDiscoveryItemParcel mBuilderParcel = new FastPairDiscoveryItemParcel();

        public Builder setId(String str) {
            this.mBuilderParcel.f39id = str;
            return this;
        }

        public Builder setMacAddress(String str) {
            this.mBuilderParcel.macAddress = str;
            return this;
        }

        public Builder setActionUrl(String str) {
            this.mBuilderParcel.actionUrl = str;
            return this;
        }

        public Builder setDeviceName(String str) {
            this.mBuilderParcel.deviceName = str;
            return this;
        }

        public Builder setTitle(String str) {
            this.mBuilderParcel.title = str;
            return this;
        }

        public Builder setDescription(String str) {
            this.mBuilderParcel.description = str;
            return this;
        }

        public Builder setDisplayUrl(String str) {
            this.mBuilderParcel.displayUrl = str;
            return this;
        }

        public Builder setLastObservationTimestampMillis(long j) {
            this.mBuilderParcel.lastObservationTimestampMillis = j;
            return this;
        }

        public Builder setFirstObservationTimestampMillis(long j) {
            this.mBuilderParcel.firstObservationTimestampMillis = j;
            return this;
        }

        public Builder setState(int i) {
            this.mBuilderParcel.state = i;
            return this;
        }

        public Builder setActionUrlType(int i) {
            this.mBuilderParcel.actionUrlType = i;
            return this;
        }

        public Builder setRssi(int i) {
            this.mBuilderParcel.rssi = i;
            return this;
        }

        public Builder setPendingAppInstallTimestampMillis(long j) {
            this.mBuilderParcel.pendingAppInstallTimestampMillis = j;
            return this;
        }

        public Builder setTxPower(int i) {
            this.mBuilderParcel.txPower = i;
            return this;
        }

        public Builder setAppName(String str) {
            this.mBuilderParcel.appName = str;
            return this;
        }

        public Builder setPackageName(String str) {
            this.mBuilderParcel.packageName = str;
            return this;
        }

        public Builder setTriggerId(String str) {
            this.mBuilderParcel.triggerId = str;
            return this;
        }

        public Builder setIconPng(byte[] bArr) {
            this.mBuilderParcel.iconPng = bArr;
            return this;
        }

        public Builder setIconFfeUrl(String str) {
            this.mBuilderParcel.iconFifeUrl = str;
            return this;
        }

        public Builder setAuthenticationPublicKeySecp256r1(byte[] bArr) {
            this.mBuilderParcel.authenticationPublicKeySecp256r1 = bArr;
            return this;
        }

        public FastPairDiscoveryItem build() {
            return new FastPairDiscoveryItem(this.mBuilderParcel);
        }
    }
}
