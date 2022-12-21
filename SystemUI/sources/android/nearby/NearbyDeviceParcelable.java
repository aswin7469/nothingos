package android.nearby;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Arrays;
import java.util.Objects;

public final class NearbyDeviceParcelable implements Parcelable {
    public static final Parcelable.Creator<NearbyDeviceParcelable> CREATOR = new Parcelable.Creator<NearbyDeviceParcelable>() {
        public NearbyDeviceParcelable createFromParcel(Parcel parcel) {
            Builder builder = new Builder();
            builder.setScanType(parcel.readInt());
            if (parcel.readInt() == 1) {
                builder.setName(parcel.readString());
            }
            builder.setMedium(parcel.readInt());
            builder.setTxPower(parcel.readInt());
            builder.setRssi(parcel.readInt());
            builder.setAction(parcel.readInt());
            builder.setPublicCredential((PublicCredential) parcel.readParcelable(PublicCredential.class.getClassLoader(), PublicCredential.class));
            if (parcel.readInt() == 1) {
                builder.setFastPairModelId(parcel.readString());
            }
            if (parcel.readInt() == 1) {
                builder.setBluetoothAddress(parcel.readString());
            }
            if (parcel.readInt() == 1) {
                byte[] bArr = new byte[parcel.readInt()];
                parcel.readByteArray(bArr);
                builder.setData(bArr);
            }
            if (parcel.readInt() == 1) {
                byte[] bArr2 = new byte[parcel.readInt()];
                parcel.readByteArray(bArr2);
                builder.setData(bArr2);
            }
            return builder.build();
        }

        public NearbyDeviceParcelable[] newArray(int i) {
            return new NearbyDeviceParcelable[i];
        }
    };
    private final int mAction;
    private final String mBluetoothAddress;
    private final byte[] mData;
    private final String mFastPairModelId;
    private final int mMedium;
    private final String mName;
    private final PublicCredential mPublicCredential;
    private final int mRssi;
    private final byte[] mSalt;
    int mScanType;
    private final int mTxPower;

    public int describeContents() {
        return 0;
    }

    private NearbyDeviceParcelable(int i, String str, int i2, int i3, int i4, int i5, PublicCredential publicCredential, String str2, String str3, byte[] bArr, byte[] bArr2) {
        this.mScanType = i;
        this.mName = str;
        this.mMedium = i2;
        this.mTxPower = i3;
        this.mRssi = i4;
        this.mAction = i5;
        this.mPublicCredential = publicCredential;
        this.mFastPairModelId = str2;
        this.mBluetoothAddress = str3;
        this.mData = bArr;
        this.mSalt = bArr2;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mScanType);
        int i2 = 0;
        parcel.writeInt(this.mName == null ? 0 : 1);
        String str = this.mName;
        if (str != null) {
            parcel.writeString(str);
        }
        parcel.writeInt(this.mMedium);
        parcel.writeInt(this.mTxPower);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mAction);
        parcel.writeParcelable(this.mPublicCredential, i);
        parcel.writeInt(this.mFastPairModelId == null ? 0 : 1);
        String str2 = this.mFastPairModelId;
        if (str2 != null) {
            parcel.writeString(str2);
        }
        parcel.writeInt(this.mBluetoothAddress == null ? 0 : 1);
        String str3 = this.mBluetoothAddress;
        if (str3 != null) {
            parcel.writeString(str3);
        }
        parcel.writeInt(this.mData == null ? 0 : 1);
        byte[] bArr = this.mData;
        if (bArr != null) {
            parcel.writeInt(bArr.length);
            parcel.writeByteArray(this.mData);
        }
        if (this.mSalt != null) {
            i2 = 1;
        }
        parcel.writeInt(i2);
        byte[] bArr2 = this.mSalt;
        if (bArr2 != null) {
            parcel.writeInt(bArr2.length);
            parcel.writeByteArray(this.mSalt);
        }
    }

    public String toString() {
        return "NearbyDeviceParcelable[scanType=" + this.mScanType + ", name=" + this.mName + ", medium=" + NearbyDevice.mediumToString(this.mMedium) + ", txPower=" + this.mTxPower + ", rssi=" + this.mRssi + ", action=" + this.mAction + ", bluetoothAddress=" + this.mBluetoothAddress + ", fastPairModelId=" + this.mFastPairModelId + ", data=" + Arrays.toString(this.mData) + ", salt=" + Arrays.toString(this.mSalt) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NearbyDeviceParcelable)) {
            return false;
        }
        NearbyDeviceParcelable nearbyDeviceParcelable = (NearbyDeviceParcelable) obj;
        if (this.mScanType != nearbyDeviceParcelable.mScanType || !Objects.equals(this.mName, nearbyDeviceParcelable.mName) || this.mMedium != nearbyDeviceParcelable.mMedium || this.mTxPower != nearbyDeviceParcelable.mTxPower || this.mRssi != nearbyDeviceParcelable.mRssi || this.mAction != nearbyDeviceParcelable.mAction || !Objects.equals(this.mPublicCredential, nearbyDeviceParcelable.mPublicCredential) || !Objects.equals(this.mBluetoothAddress, nearbyDeviceParcelable.mBluetoothAddress) || !Objects.equals(this.mFastPairModelId, nearbyDeviceParcelable.mFastPairModelId) || !Arrays.equals(this.mData, nearbyDeviceParcelable.mData) || !Arrays.equals(this.mSalt, nearbyDeviceParcelable.mSalt)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mScanType), this.mName, Integer.valueOf(this.mMedium), Integer.valueOf(this.mRssi), Integer.valueOf(this.mAction), Integer.valueOf(this.mPublicCredential.hashCode()), this.mBluetoothAddress, this.mFastPairModelId, Integer.valueOf(Arrays.hashCode(this.mData)), Integer.valueOf(Arrays.hashCode(this.mSalt)));
    }

    public int getScanType() {
        return this.mScanType;
    }

    public String getName() {
        return this.mName;
    }

    public int getMedium() {
        return this.mMedium;
    }

    public int getTxPower() {
        return this.mTxPower;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getAction() {
        return this.mAction;
    }

    public PublicCredential getPublicCredential() {
        return this.mPublicCredential;
    }

    public String getFastPairModelId() {
        return this.mFastPairModelId;
    }

    public String getBluetoothAddress() {
        return this.mBluetoothAddress;
    }

    public byte[] getData() {
        return this.mData;
    }

    public byte[] getSalt() {
        return this.mSalt;
    }

    public static final class Builder {
        private int mAction;
        private String mBluetoothAddress;
        private byte[] mData;
        private String mFastPairModelId;
        private int mMedium;
        private String mName;
        private PublicCredential mPublicCredential;
        private int mRssi;
        private byte[] mSalt;
        int mScanType;
        private int mTxPower;

        public Builder setScanType(int i) {
            this.mScanType = i;
            return this;
        }

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder setMedium(int i) {
            this.mMedium = i;
            return this;
        }

        public Builder setTxPower(int i) {
            this.mTxPower = i;
            return this;
        }

        public Builder setRssi(int i) {
            this.mRssi = i;
            return this;
        }

        public Builder setAction(int i) {
            this.mAction = i;
            return this;
        }

        public Builder setPublicCredential(PublicCredential publicCredential) {
            this.mPublicCredential = publicCredential;
            return this;
        }

        public Builder setFastPairModelId(String str) {
            this.mFastPairModelId = str;
            return this;
        }

        public Builder setBluetoothAddress(String str) {
            this.mBluetoothAddress = str;
            return this;
        }

        public Builder setData(byte[] bArr) {
            this.mData = bArr;
            return this;
        }

        public Builder setSalt(byte[] bArr) {
            this.mSalt = bArr;
            return this;
        }

        public NearbyDeviceParcelable build() {
            return new NearbyDeviceParcelable(this.mScanType, this.mName, this.mMedium, this.mTxPower, this.mRssi, this.mAction, this.mPublicCredential, this.mFastPairModelId, this.mBluetoothAddress, this.mData, this.mSalt);
        }
    }
}
