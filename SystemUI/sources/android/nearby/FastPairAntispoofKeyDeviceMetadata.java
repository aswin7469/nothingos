package android.nearby;

import android.nearby.aidl.FastPairAntispoofKeyDeviceMetadataParcel;

public class FastPairAntispoofKeyDeviceMetadata {
    FastPairAntispoofKeyDeviceMetadataParcel mMetadataParcel;

    FastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel) {
        this.mMetadataParcel = fastPairAntispoofKeyDeviceMetadataParcel;
    }

    public byte[] getAntispoofPublicKey() {
        return this.mMetadataParcel.antispoofPublicKey;
    }

    public FastPairDeviceMetadata getFastPairDeviceMetadata() {
        if (this.mMetadataParcel.deviceMetadata == null) {
            return null;
        }
        return new FastPairDeviceMetadata(this.mMetadataParcel.deviceMetadata);
    }

    public static final class Builder {
        private final FastPairAntispoofKeyDeviceMetadataParcel mBuilderParcel;

        public Builder() {
            FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel = new FastPairAntispoofKeyDeviceMetadataParcel();
            this.mBuilderParcel = fastPairAntispoofKeyDeviceMetadataParcel;
            fastPairAntispoofKeyDeviceMetadataParcel.antispoofPublicKey = null;
            fastPairAntispoofKeyDeviceMetadataParcel.deviceMetadata = null;
        }

        public Builder setAntispoofPublicKey(byte[] bArr) {
            this.mBuilderParcel.antispoofPublicKey = bArr;
            return this;
        }

        public Builder setFastPairDeviceMetadata(FastPairDeviceMetadata fastPairDeviceMetadata) {
            if (fastPairDeviceMetadata != null) {
                this.mBuilderParcel.deviceMetadata = fastPairDeviceMetadata.mMetadataParcel;
            } else {
                this.mBuilderParcel.deviceMetadata = null;
            }
            return this;
        }

        public FastPairAntispoofKeyDeviceMetadata build() {
            return new FastPairAntispoofKeyDeviceMetadata(this.mBuilderParcel);
        }
    }
}
