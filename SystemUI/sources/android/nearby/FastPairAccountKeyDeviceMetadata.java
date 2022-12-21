package android.nearby;

import android.nearby.aidl.FastPairAccountKeyDeviceMetadataParcel;

public class FastPairAccountKeyDeviceMetadata {
    FastPairAccountKeyDeviceMetadataParcel mMetadataParcel;

    FastPairAccountKeyDeviceMetadata(FastPairAccountKeyDeviceMetadataParcel fastPairAccountKeyDeviceMetadataParcel) {
        this.mMetadataParcel = fastPairAccountKeyDeviceMetadataParcel;
    }

    public byte[] getDeviceAccountKey() {
        return this.mMetadataParcel.deviceAccountKey;
    }

    public byte[] getSha256DeviceAccountKeyPublicAddress() {
        return this.mMetadataParcel.sha256DeviceAccountKeyPublicAddress;
    }

    public FastPairDeviceMetadata getFastPairDeviceMetadata() {
        if (this.mMetadataParcel.metadata == null) {
            return null;
        }
        return new FastPairDeviceMetadata(this.mMetadataParcel.metadata);
    }

    public FastPairDiscoveryItem getFastPairDiscoveryItem() {
        if (this.mMetadataParcel.discoveryItem == null) {
            return null;
        }
        return new FastPairDiscoveryItem(this.mMetadataParcel.discoveryItem);
    }

    public static final class Builder {
        private final FastPairAccountKeyDeviceMetadataParcel mBuilderParcel;

        public Builder() {
            FastPairAccountKeyDeviceMetadataParcel fastPairAccountKeyDeviceMetadataParcel = new FastPairAccountKeyDeviceMetadataParcel();
            this.mBuilderParcel = fastPairAccountKeyDeviceMetadataParcel;
            fastPairAccountKeyDeviceMetadataParcel.deviceAccountKey = null;
            fastPairAccountKeyDeviceMetadataParcel.sha256DeviceAccountKeyPublicAddress = null;
            fastPairAccountKeyDeviceMetadataParcel.metadata = null;
            fastPairAccountKeyDeviceMetadataParcel.discoveryItem = null;
        }

        public Builder setDeviceAccountKey(byte[] bArr) {
            this.mBuilderParcel.deviceAccountKey = bArr;
            return this;
        }

        public Builder setSha256DeviceAccountKeyPublicAddress(byte[] bArr) {
            this.mBuilderParcel.sha256DeviceAccountKeyPublicAddress = bArr;
            return this;
        }

        public Builder setFastPairDeviceMetadata(FastPairDeviceMetadata fastPairDeviceMetadata) {
            if (fastPairDeviceMetadata == null) {
                this.mBuilderParcel.metadata = null;
            } else {
                this.mBuilderParcel.metadata = fastPairDeviceMetadata.mMetadataParcel;
            }
            return this;
        }

        public Builder setFastPairDiscoveryItem(FastPairDiscoveryItem fastPairDiscoveryItem) {
            if (fastPairDiscoveryItem == null) {
                this.mBuilderParcel.discoveryItem = null;
            } else {
                this.mBuilderParcel.discoveryItem = fastPairDiscoveryItem.mMetadataParcel;
            }
            return this;
        }

        public FastPairAccountKeyDeviceMetadata build() {
            return new FastPairAccountKeyDeviceMetadata(this.mBuilderParcel);
        }
    }
}
