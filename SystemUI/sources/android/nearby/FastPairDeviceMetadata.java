package android.nearby;

import android.nearby.aidl.FastPairDeviceMetadataParcel;

public class FastPairDeviceMetadata {
    FastPairDeviceMetadataParcel mMetadataParcel;

    FastPairDeviceMetadata(FastPairDeviceMetadataParcel fastPairDeviceMetadataParcel) {
        this.mMetadataParcel = fastPairDeviceMetadataParcel;
    }

    public String getImageUrl() {
        return this.mMetadataParcel.imageUrl;
    }

    public String getIntentUri() {
        return this.mMetadataParcel.intentUri;
    }

    public int getBleTxPower() {
        return this.mMetadataParcel.bleTxPower;
    }

    public float getTriggerDistance() {
        return this.mMetadataParcel.triggerDistance;
    }

    public byte[] getImage() {
        return this.mMetadataParcel.image;
    }

    public int getDeviceType() {
        return this.mMetadataParcel.deviceType;
    }

    public String getName() {
        return this.mMetadataParcel.name;
    }

    public String getTrueWirelessImageUrlLeftBud() {
        return this.mMetadataParcel.trueWirelessImageUrlLeftBud;
    }

    public String getTrueWirelessImageUrlRightBud() {
        return this.mMetadataParcel.trueWirelessImageUrlRightBud;
    }

    public String getTrueWirelessImageUrlCase() {
        return this.mMetadataParcel.trueWirelessImageUrlCase;
    }

    public String getInitialNotificationDescription() {
        return this.mMetadataParcel.initialNotificationDescription;
    }

    public String getInitialNotificationDescriptionNoAccount() {
        return this.mMetadataParcel.initialNotificationDescriptionNoAccount;
    }

    public String getOpenCompanionAppDescription() {
        return this.mMetadataParcel.openCompanionAppDescription;
    }

    public String getUpdateCompanionAppDescription() {
        return this.mMetadataParcel.updateCompanionAppDescription;
    }

    public String getDownloadCompanionAppDescription() {
        return this.mMetadataParcel.downloadCompanionAppDescription;
    }

    public String getUnableToConnectTitle() {
        return this.mMetadataParcel.unableToConnectTitle;
    }

    public String getUnableToConnectDescription() {
        return this.mMetadataParcel.unableToConnectDescription;
    }

    public String getInitialPairingDescription() {
        return this.mMetadataParcel.initialPairingDescription;
    }

    public String getConnectSuccessCompanionAppInstalled() {
        return this.mMetadataParcel.connectSuccessCompanionAppInstalled;
    }

    public String getConnectSuccessCompanionAppNotInstalled() {
        return this.mMetadataParcel.connectSuccessCompanionAppNotInstalled;
    }

    public String getSubsequentPairingDescription() {
        return this.mMetadataParcel.subsequentPairingDescription;
    }

    public String getRetroactivePairingDescription() {
        return this.mMetadataParcel.retroactivePairingDescription;
    }

    public String getWaitLaunchCompanionAppDescription() {
        return this.mMetadataParcel.waitLaunchCompanionAppDescription;
    }

    public String getFailConnectGoToSettingsDescription() {
        return this.mMetadataParcel.failConnectGoToSettingsDescription;
    }

    public static final class Builder {
        private final FastPairDeviceMetadataParcel mBuilderParcel;

        public Builder() {
            FastPairDeviceMetadataParcel fastPairDeviceMetadataParcel = new FastPairDeviceMetadataParcel();
            this.mBuilderParcel = fastPairDeviceMetadataParcel;
            fastPairDeviceMetadataParcel.imageUrl = null;
            fastPairDeviceMetadataParcel.intentUri = null;
            fastPairDeviceMetadataParcel.name = null;
            fastPairDeviceMetadataParcel.bleTxPower = 0;
            fastPairDeviceMetadataParcel.triggerDistance = 0.0f;
            fastPairDeviceMetadataParcel.image = null;
            fastPairDeviceMetadataParcel.deviceType = 0;
            fastPairDeviceMetadataParcel.trueWirelessImageUrlLeftBud = null;
            fastPairDeviceMetadataParcel.trueWirelessImageUrlRightBud = null;
            fastPairDeviceMetadataParcel.trueWirelessImageUrlCase = null;
            fastPairDeviceMetadataParcel.initialNotificationDescription = null;
            fastPairDeviceMetadataParcel.initialNotificationDescriptionNoAccount = null;
            fastPairDeviceMetadataParcel.openCompanionAppDescription = null;
            fastPairDeviceMetadataParcel.updateCompanionAppDescription = null;
            fastPairDeviceMetadataParcel.downloadCompanionAppDescription = null;
            fastPairDeviceMetadataParcel.unableToConnectTitle = null;
            fastPairDeviceMetadataParcel.unableToConnectDescription = null;
            fastPairDeviceMetadataParcel.initialPairingDescription = null;
            fastPairDeviceMetadataParcel.connectSuccessCompanionAppInstalled = null;
            fastPairDeviceMetadataParcel.connectSuccessCompanionAppNotInstalled = null;
            fastPairDeviceMetadataParcel.subsequentPairingDescription = null;
            fastPairDeviceMetadataParcel.retroactivePairingDescription = null;
            fastPairDeviceMetadataParcel.waitLaunchCompanionAppDescription = null;
            fastPairDeviceMetadataParcel.failConnectGoToSettingsDescription = null;
        }

        public Builder setImageUrl(String str) {
            this.mBuilderParcel.imageUrl = str;
            return this;
        }

        public Builder setIntentUri(String str) {
            this.mBuilderParcel.intentUri = str;
            return this;
        }

        public Builder setName(String str) {
            this.mBuilderParcel.name = str;
            return this;
        }

        public Builder setBleTxPower(int i) {
            this.mBuilderParcel.bleTxPower = i;
            return this;
        }

        public Builder setTriggerDistance(float f) {
            this.mBuilderParcel.triggerDistance = f;
            return this;
        }

        public Builder setImage(byte[] bArr) {
            this.mBuilderParcel.image = bArr;
            return this;
        }

        public Builder setDeviceType(int i) {
            this.mBuilderParcel.deviceType = i;
            return this;
        }

        public Builder setTrueWirelessImageUrlLeftBud(String str) {
            this.mBuilderParcel.trueWirelessImageUrlLeftBud = str;
            return this;
        }

        public Builder setTrueWirelessImageUrlRightBud(String str) {
            this.mBuilderParcel.trueWirelessImageUrlRightBud = str;
            return this;
        }

        public Builder setTrueWirelessImageUrlCase(String str) {
            this.mBuilderParcel.trueWirelessImageUrlCase = str;
            return this;
        }

        public Builder setInitialNotificationDescription(String str) {
            this.mBuilderParcel.initialNotificationDescription = str;
            return this;
        }

        public Builder setInitialNotificationDescriptionNoAccount(String str) {
            this.mBuilderParcel.initialNotificationDescriptionNoAccount = str;
            return this;
        }

        public Builder setOpenCompanionAppDescription(String str) {
            this.mBuilderParcel.openCompanionAppDescription = str;
            return this;
        }

        public Builder setUpdateCompanionAppDescription(String str) {
            this.mBuilderParcel.updateCompanionAppDescription = str;
            return this;
        }

        public Builder setDownloadCompanionAppDescription(String str) {
            this.mBuilderParcel.downloadCompanionAppDescription = str;
            return this;
        }

        public Builder setUnableToConnectTitle(String str) {
            this.mBuilderParcel.unableToConnectTitle = str;
            return this;
        }

        public Builder setUnableToConnectDescription(String str) {
            this.mBuilderParcel.unableToConnectDescription = str;
            return this;
        }

        public Builder setInitialPairingDescription(String str) {
            this.mBuilderParcel.initialPairingDescription = str;
            return this;
        }

        public Builder setConnectSuccessCompanionAppInstalled(String str) {
            this.mBuilderParcel.connectSuccessCompanionAppInstalled = str;
            return this;
        }

        public Builder setConnectSuccessCompanionAppNotInstalled(String str) {
            this.mBuilderParcel.connectSuccessCompanionAppNotInstalled = str;
            return this;
        }

        public Builder setSubsequentPairingDescription(String str) {
            this.mBuilderParcel.subsequentPairingDescription = str;
            return this;
        }

        public Builder setRetroactivePairingDescription(String str) {
            this.mBuilderParcel.retroactivePairingDescription = str;
            return this;
        }

        public Builder setWaitLaunchCompanionAppDescription(String str) {
            this.mBuilderParcel.waitLaunchCompanionAppDescription = str;
            return this;
        }

        public Builder setFailConnectGoToSettingsDescription(String str) {
            this.mBuilderParcel.failConnectGoToSettingsDescription = str;
            return this;
        }

        public FastPairDeviceMetadata build() {
            return new FastPairDeviceMetadata(this.mBuilderParcel);
        }
    }
}
