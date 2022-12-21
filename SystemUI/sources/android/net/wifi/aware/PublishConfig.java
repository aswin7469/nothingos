package android.net.wifi.aware;

import android.net.wifi.aware.TlvBufferUtils;
import android.net.wifi.util.HexEncoding;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PublishConfig implements Parcelable {
    public static final Parcelable.Creator<PublishConfig> CREATOR = new Parcelable.Creator<PublishConfig>() {
        public PublishConfig[] newArray(int i) {
            return new PublishConfig[i];
        }

        public PublishConfig createFromParcel(Parcel parcel) {
            return new PublishConfig(parcel.createByteArray(), parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0, parcel.readBoolean(), parcel.readInt(), (WifiAwareDataPathSecurityConfig) parcel.readParcelable(WifiAwareDataPathSecurityConfig.class.getClassLoader()));
        }
    };
    public static final int PUBLISH_TYPE_SOLICITED = 1;
    public static final int PUBLISH_TYPE_UNSOLICITED = 0;
    private final int mBand;
    private final boolean mEnableInstantMode;
    public final boolean mEnableRanging;
    public final boolean mEnableTerminateNotification;
    public final byte[] mMatchFilter;
    public final int mPublishType;
    private final WifiAwareDataPathSecurityConfig mSecurityConfig;
    public final byte[] mServiceName;
    public final byte[] mServiceSpecificInfo;
    public final int mTtlSec;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PublishTypes {
    }

    public int describeContents() {
        return 0;
    }

    public PublishConfig(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2, boolean z, boolean z2, boolean z3, int i3, WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig) {
        this.mServiceName = bArr;
        this.mServiceSpecificInfo = bArr2;
        this.mMatchFilter = bArr3;
        this.mPublishType = i;
        this.mTtlSec = i2;
        this.mEnableTerminateNotification = z;
        this.mEnableRanging = z2;
        this.mEnableInstantMode = z3;
        this.mBand = i3;
        this.mSecurityConfig = wifiAwareDataPathSecurityConfig;
    }

    public String toString() {
        int i;
        StringBuilder sb = new StringBuilder("PublishConfig [mServiceName='");
        byte[] bArr = this.mServiceName;
        String str = "<null>";
        sb.append(bArr == null ? str : String.valueOf(HexEncoding.encode(bArr)));
        sb.append(", mServiceName.length=");
        byte[] bArr2 = this.mServiceName;
        int i2 = 0;
        sb.append(bArr2 == null ? 0 : bArr2.length);
        sb.append(", mServiceSpecificInfo='");
        byte[] bArr3 = this.mServiceSpecificInfo;
        if (bArr3 != null) {
            str = String.valueOf(HexEncoding.encode(bArr3));
        }
        sb.append(str);
        sb.append(", mServiceSpecificInfo.length=");
        byte[] bArr4 = this.mServiceSpecificInfo;
        if (bArr4 == null) {
            i = 0;
        } else {
            i = bArr4.length;
        }
        sb.append(i);
        sb.append(", mMatchFilter=");
        sb.append(new TlvBufferUtils.TlvIterable(0, 1, this.mMatchFilter).toString());
        sb.append(", mMatchFilter.length=");
        byte[] bArr5 = this.mMatchFilter;
        if (bArr5 != null) {
            i2 = bArr5.length;
        }
        sb.append(i2);
        sb.append(", mPublishType=");
        sb.append(this.mPublishType);
        sb.append(", mTtlSec=");
        sb.append(this.mTtlSec);
        sb.append(", mEnableTerminateNotification=");
        sb.append(this.mEnableTerminateNotification);
        sb.append(", mEnableRanging=");
        sb.append(this.mEnableRanging);
        sb.append("], mEnableInstantMode=");
        sb.append(this.mEnableInstantMode);
        sb.append(", mBand=");
        sb.append(this.mBand);
        sb.append(", mSecurityConfig");
        sb.append((Object) this.mSecurityConfig);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.mServiceName);
        parcel.writeByteArray(this.mServiceSpecificInfo);
        parcel.writeByteArray(this.mMatchFilter);
        parcel.writeInt(this.mPublishType);
        parcel.writeInt(this.mTtlSec);
        parcel.writeInt(this.mEnableTerminateNotification ? 1 : 0);
        parcel.writeInt(this.mEnableRanging ? 1 : 0);
        parcel.writeBoolean(this.mEnableInstantMode);
        parcel.writeInt(this.mBand);
        parcel.writeParcelable(this.mSecurityConfig, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PublishConfig)) {
            return false;
        }
        PublishConfig publishConfig = (PublishConfig) obj;
        if (Arrays.equals(this.mServiceName, publishConfig.mServiceName) && Arrays.equals(this.mServiceSpecificInfo, publishConfig.mServiceSpecificInfo) && Arrays.equals(this.mMatchFilter, publishConfig.mMatchFilter) && this.mPublishType == publishConfig.mPublishType && this.mTtlSec == publishConfig.mTtlSec && this.mEnableTerminateNotification == publishConfig.mEnableTerminateNotification && this.mEnableRanging == publishConfig.mEnableRanging && this.mEnableInstantMode == publishConfig.mEnableInstantMode && this.mBand == publishConfig.mBand && Objects.equals(this.mSecurityConfig, publishConfig.mSecurityConfig)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(Arrays.hashCode(this.mServiceName)), Integer.valueOf(Arrays.hashCode(this.mServiceSpecificInfo)), Integer.valueOf(Arrays.hashCode(this.mMatchFilter)), Integer.valueOf(this.mPublishType), Integer.valueOf(this.mTtlSec), Boolean.valueOf(this.mEnableTerminateNotification), Boolean.valueOf(this.mEnableRanging), Boolean.valueOf(this.mEnableInstantMode), Integer.valueOf(this.mBand), this.mSecurityConfig);
    }

    public void assertValid(Characteristics characteristics, boolean z) throws IllegalArgumentException {
        byte[] bArr;
        byte[] bArr2;
        WifiAwareUtils.validateServiceName(this.mServiceName);
        if (TlvBufferUtils.isValid(this.mMatchFilter, 0, 1)) {
            int i = this.mPublishType;
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("Invalid publishType - " + this.mPublishType);
            } else if (this.mTtlSec >= 0) {
                WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig = this.mSecurityConfig;
                if (wifiAwareDataPathSecurityConfig == null || wifiAwareDataPathSecurityConfig.isValid()) {
                    if (characteristics != null) {
                        int maxServiceNameLength = characteristics.getMaxServiceNameLength();
                        if (maxServiceNameLength == 0 || this.mServiceName.length <= maxServiceNameLength) {
                            int maxServiceSpecificInfoLength = characteristics.getMaxServiceSpecificInfoLength();
                            if (maxServiceSpecificInfoLength == 0 || (bArr2 = this.mServiceSpecificInfo) == null || bArr2.length <= maxServiceSpecificInfoLength) {
                                int maxMatchFilterLength = characteristics.getMaxMatchFilterLength();
                                if (maxMatchFilterLength != 0 && (bArr = this.mMatchFilter) != null && bArr.length > maxMatchFilterLength) {
                                    throw new IllegalArgumentException("Match filter longer than supported by device characteristics");
                                } else if (this.mEnableInstantMode && (!SdkLevel.isAtLeastT() || !characteristics.isInstantCommunicationModeSupported())) {
                                    throw new IllegalArgumentException("instant mode is not supported");
                                } else if (this.mSecurityConfig != null && (characteristics.getSupportedCipherSuites() & this.mSecurityConfig.getCipherSuite()) == 0) {
                                    throw new IllegalArgumentException("Unsupported cipher suite");
                                }
                            } else {
                                throw new IllegalArgumentException("Service specific info longer than supported by device characteristics");
                            }
                        } else {
                            throw new IllegalArgumentException("Service name longer than supported by device characteristics");
                        }
                    }
                    if (!z && this.mEnableRanging) {
                        throw new IllegalArgumentException("Ranging is not supported");
                    }
                    return;
                }
                throw new IllegalArgumentException("WifiAwareDataPathSecurityConfig is invalid");
            } else {
                throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
            }
        } else {
            throw new IllegalArgumentException("Invalid txFilter configuration - LV fields do not match up to length");
        }
    }

    public boolean isInstantCommunicationModeEnabled() {
        return this.mEnableInstantMode;
    }

    public int getInstantCommunicationBand() {
        return this.mBand;
    }

    public WifiAwareDataPathSecurityConfig getSecurityConfig() {
        return this.mSecurityConfig;
    }

    public static final class Builder {
        private int mBand = 1;
        private boolean mEnableInstantMode = false;
        private boolean mEnableRanging = false;
        private boolean mEnableTerminateNotification = true;
        private byte[] mMatchFilter;
        private int mPublishType = 0;
        private WifiAwareDataPathSecurityConfig mSecurityConfig = null;
        private byte[] mServiceName;
        private byte[] mServiceSpecificInfo;
        private int mTtlSec = 0;

        public Builder setServiceName(String str) {
            if (str != null) {
                this.mServiceName = str.getBytes(StandardCharsets.UTF_8);
                return this;
            }
            throw new IllegalArgumentException("Invalid service name - must be non-null");
        }

        public Builder setServiceSpecificInfo(byte[] bArr) {
            this.mServiceSpecificInfo = bArr;
            return this;
        }

        public Builder setMatchFilter(List<byte[]> list) {
            this.mMatchFilter = new TlvBufferUtils.TlvConstructor(0, 1).allocateAndPut(list).getArray();
            return this;
        }

        public Builder setPublishType(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("Invalid publishType - " + i);
            }
            this.mPublishType = i;
            return this;
        }

        public Builder setTtlSec(int i) {
            if (i >= 0) {
                this.mTtlSec = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
        }

        public Builder setTerminateNotificationEnabled(boolean z) {
            this.mEnableTerminateNotification = z;
            return this;
        }

        public Builder setRangingEnabled(boolean z) {
            this.mEnableRanging = z;
            return this;
        }

        public Builder setInstantCommunicationModeEnabled(boolean z, int i) {
            if (!SdkLevel.isAtLeastT()) {
                throw new UnsupportedOperationException();
            } else if (i == 1 || i == 2) {
                this.mBand = i;
                this.mEnableInstantMode = z;
                return this;
            } else {
                throw new IllegalArgumentException();
            }
        }

        public Builder setDataPathSecurityConfig(WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig) {
            if (wifiAwareDataPathSecurityConfig == null) {
                throw new IllegalArgumentException("The WifiAwareDataPathSecurityConfig should be non-null");
            } else if (wifiAwareDataPathSecurityConfig.isValid()) {
                this.mSecurityConfig = wifiAwareDataPathSecurityConfig;
                return this;
            } else {
                throw new IllegalArgumentException("The WifiAwareDataPathSecurityConfig is invalid");
            }
        }

        public PublishConfig build() {
            return new PublishConfig(this.mServiceName, this.mServiceSpecificInfo, this.mMatchFilter, this.mPublishType, this.mTtlSec, this.mEnableTerminateNotification, this.mEnableRanging, this.mEnableInstantMode, this.mBand, this.mSecurityConfig);
        }
    }
}
