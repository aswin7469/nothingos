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

public final class SubscribeConfig implements Parcelable {
    public static final Parcelable.Creator<SubscribeConfig> CREATOR = new Parcelable.Creator<SubscribeConfig>() {
        public SubscribeConfig[] newArray(int i) {
            return new SubscribeConfig[i];
        }

        public SubscribeConfig createFromParcel(Parcel parcel) {
            byte[] createByteArray = parcel.createByteArray();
            byte[] createByteArray2 = parcel.createByteArray();
            byte[] createByteArray3 = parcel.createByteArray();
            int readInt = parcel.readInt();
            int readInt2 = parcel.readInt();
            boolean z = parcel.readInt() != 0;
            int readInt3 = parcel.readInt();
            return new SubscribeConfig(createByteArray, createByteArray2, createByteArray3, readInt, readInt2, z, parcel.readInt() != 0, readInt3, parcel.readInt() != 0, parcel.readInt(), parcel.readBoolean(), parcel.readInt());
        }
    };
    public static final int SUBSCRIBE_TYPE_ACTIVE = 1;
    public static final int SUBSCRIBE_TYPE_PASSIVE = 0;
    private final int mBand;
    private final boolean mEnableInstantMode;
    public final boolean mEnableTerminateNotification;
    public final byte[] mMatchFilter;
    public final int mMaxDistanceMm;
    public final boolean mMaxDistanceMmSet;
    public final int mMinDistanceMm;
    public final boolean mMinDistanceMmSet;
    public final byte[] mServiceName;
    public final byte[] mServiceSpecificInfo;
    public final int mSubscribeType;
    public final int mTtlSec;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SubscribeTypes {
    }

    public int describeContents() {
        return 0;
    }

    public SubscribeConfig(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2, boolean z, boolean z2, int i3, boolean z3, int i4, boolean z4, int i5) {
        this.mServiceName = bArr;
        this.mServiceSpecificInfo = bArr2;
        this.mMatchFilter = bArr3;
        this.mSubscribeType = i;
        this.mTtlSec = i2;
        this.mEnableTerminateNotification = z;
        this.mMinDistanceMm = i3;
        this.mMinDistanceMmSet = z2;
        this.mMaxDistanceMm = i4;
        this.mMaxDistanceMmSet = z3;
        this.mEnableInstantMode = z4;
        this.mBand = i5;
    }

    public String toString() {
        String str;
        int i;
        StringBuilder sb = new StringBuilder("SubscribeConfig [mServiceName='");
        byte[] bArr = this.mServiceName;
        String str2 = "<null>";
        if (bArr == null) {
            str = str2;
        } else {
            str = String.valueOf(HexEncoding.encode(bArr));
        }
        sb.append(str);
        sb.append(", mServiceName.length=");
        byte[] bArr2 = this.mServiceName;
        int i2 = 0;
        sb.append(bArr2 == null ? 0 : bArr2.length);
        sb.append(", mServiceSpecificInfo='");
        byte[] bArr3 = this.mServiceSpecificInfo;
        if (bArr3 != null) {
            str2 = String.valueOf(HexEncoding.encode(bArr3));
        }
        sb.append(str2);
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
        sb.append(", mSubscribeType=");
        sb.append(this.mSubscribeType);
        sb.append(", mTtlSec=");
        sb.append(this.mTtlSec);
        sb.append(", mEnableTerminateNotification=");
        sb.append(this.mEnableTerminateNotification);
        sb.append(", mMinDistanceMm=");
        sb.append(this.mMinDistanceMm);
        sb.append(", mMinDistanceMmSet=");
        sb.append(this.mMinDistanceMmSet);
        sb.append(", mMaxDistanceMm=");
        sb.append(this.mMaxDistanceMm);
        sb.append(", mMaxDistanceMmSet=");
        sb.append(this.mMaxDistanceMmSet);
        sb.append("], mEnableInstantMode=");
        sb.append(this.mEnableInstantMode);
        sb.append(", mBand=");
        sb.append(this.mBand);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.mServiceName);
        parcel.writeByteArray(this.mServiceSpecificInfo);
        parcel.writeByteArray(this.mMatchFilter);
        parcel.writeInt(this.mSubscribeType);
        parcel.writeInt(this.mTtlSec);
        parcel.writeInt(this.mEnableTerminateNotification ? 1 : 0);
        parcel.writeInt(this.mMinDistanceMm);
        parcel.writeInt(this.mMinDistanceMmSet ? 1 : 0);
        parcel.writeInt(this.mMaxDistanceMm);
        parcel.writeInt(this.mMaxDistanceMmSet ? 1 : 0);
        parcel.writeBoolean(this.mEnableInstantMode);
        parcel.writeInt(this.mBand);
    }

    public boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SubscribeConfig)) {
            return false;
        }
        SubscribeConfig subscribeConfig = (SubscribeConfig) obj;
        if (!Arrays.equals(this.mServiceName, subscribeConfig.mServiceName) || !Arrays.equals(this.mServiceSpecificInfo, subscribeConfig.mServiceSpecificInfo) || !Arrays.equals(this.mMatchFilter, subscribeConfig.mMatchFilter) || this.mSubscribeType != subscribeConfig.mSubscribeType || this.mTtlSec != subscribeConfig.mTtlSec || this.mEnableTerminateNotification != subscribeConfig.mEnableTerminateNotification || (z = this.mMinDistanceMmSet) != subscribeConfig.mMinDistanceMmSet || (z2 = this.mMaxDistanceMmSet) != subscribeConfig.mMaxDistanceMmSet || this.mEnableInstantMode != subscribeConfig.mEnableInstantMode || this.mBand != subscribeConfig.mBand) {
            return false;
        }
        if (!z || this.mMinDistanceMm == subscribeConfig.mMinDistanceMm) {
            return !z2 || this.mMaxDistanceMm == subscribeConfig.mMaxDistanceMm;
        }
        return false;
    }

    public int hashCode() {
        int hash = Objects.hash(Integer.valueOf(Arrays.hashCode(this.mServiceName)), Integer.valueOf(Arrays.hashCode(this.mServiceSpecificInfo)), Integer.valueOf(Arrays.hashCode(this.mMatchFilter)), Integer.valueOf(this.mSubscribeType), Integer.valueOf(this.mTtlSec), Boolean.valueOf(this.mEnableTerminateNotification), Boolean.valueOf(this.mMinDistanceMmSet), Boolean.valueOf(this.mMaxDistanceMmSet), Boolean.valueOf(this.mEnableInstantMode), Integer.valueOf(this.mBand));
        if (this.mMinDistanceMmSet) {
            hash = Objects.hash(Integer.valueOf(hash), Integer.valueOf(this.mMinDistanceMm));
        }
        if (!this.mMaxDistanceMmSet) {
            return hash;
        }
        return Objects.hash(Integer.valueOf(hash), Integer.valueOf(this.mMaxDistanceMm));
    }

    public void assertValid(Characteristics characteristics, boolean z) throws IllegalArgumentException {
        byte[] bArr;
        byte[] bArr2;
        WifiAwareUtils.validateServiceName(this.mServiceName);
        if (TlvBufferUtils.isValid(this.mMatchFilter, 0, 1)) {
            int i = this.mSubscribeType;
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("Invalid subscribeType - " + this.mSubscribeType);
            } else if (this.mTtlSec >= 0) {
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
                            }
                        } else {
                            throw new IllegalArgumentException("Service specific info longer than supported by device characteristics");
                        }
                    } else {
                        throw new IllegalArgumentException("Service name longer than supported by device characteristics");
                    }
                }
                boolean z2 = this.mMinDistanceMmSet;
                if (!z2 || this.mMinDistanceMm >= 0) {
                    boolean z3 = this.mMaxDistanceMmSet;
                    if (z3 && this.mMaxDistanceMm < 0) {
                        throw new IllegalArgumentException("Maximum distance must be non-negative");
                    } else if (z2 && z3 && this.mMaxDistanceMm <= this.mMinDistanceMm) {
                        throw new IllegalArgumentException("Maximum distance must be greater than minimum distance");
                    } else if (z) {
                    } else {
                        if (z2 || z3) {
                            throw new IllegalArgumentException("Ranging is not supported");
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Minimum distance must be non-negative");
                }
            } else {
                throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
            }
        } else {
            throw new IllegalArgumentException("Invalid matchFilter configuration - LV fields do not match up to length");
        }
    }

    public boolean isInstantCommunicationModeEnabled() {
        return this.mEnableInstantMode;
    }

    public int getInstantCommunicationBand() {
        return this.mBand;
    }

    public static final class Builder {
        private int mBand = 1;
        private boolean mEnableInstantMode;
        private boolean mEnableTerminateNotification = true;
        private byte[] mMatchFilter;
        private int mMaxDistanceMm;
        private boolean mMaxDistanceMmSet = false;
        private int mMinDistanceMm;
        private boolean mMinDistanceMmSet = false;
        private byte[] mServiceName;
        private byte[] mServiceSpecificInfo;
        private int mSubscribeType = 0;
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

        public Builder setSubscribeType(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("Invalid subscribeType - " + i);
            }
            this.mSubscribeType = i;
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

        public Builder setMinDistanceMm(int i) {
            this.mMinDistanceMm = i;
            this.mMinDistanceMmSet = true;
            return this;
        }

        public Builder setMaxDistanceMm(int i) {
            this.mMaxDistanceMm = i;
            this.mMaxDistanceMmSet = true;
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

        public SubscribeConfig build() {
            return new SubscribeConfig(this.mServiceName, this.mServiceSpecificInfo, this.mMatchFilter, this.mSubscribeType, this.mTtlSec, this.mEnableTerminateNotification, this.mMinDistanceMmSet, this.mMinDistanceMm, this.mMaxDistanceMmSet, this.mMaxDistanceMm, this.mEnableInstantMode, this.mBand);
        }
    }
}
