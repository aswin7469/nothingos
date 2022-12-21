package android.nearby;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FastPairDevice extends NearbyDevice implements Parcelable {
    public static final Parcelable.Creator<FastPairDevice> CREATOR = new Parcelable.Creator<FastPairDevice>() {
        public FastPairDevice createFromParcel(Parcel parcel) {
            Builder builder = new Builder();
            if (parcel.readInt() == 1) {
                builder.setName(parcel.readString());
            }
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                builder.addMedium(parcel.readInt());
            }
            builder.setRssi(parcel.readInt());
            builder.setTxPower(parcel.readInt());
            if (parcel.readInt() == 1) {
                builder.setModelId(parcel.readString());
            }
            builder.setBluetoothAddress(parcel.readString());
            if (parcel.readInt() == 1) {
                byte[] bArr = new byte[parcel.readInt()];
                parcel.readByteArray(bArr);
                builder.setData(bArr);
            }
            return builder.build();
        }

        public FastPairDevice[] newArray(int i) {
            return new FastPairDevice[i];
        }
    };
    private final String mBluetoothAddress;
    private final byte[] mData;
    private final String mModelId;
    private int mTxPower;

    public int describeContents() {
        return 0;
    }

    public FastPairDevice(String str, List<Integer> list, int i, int i2, String str2, String str3, byte[] bArr) {
        super(str, list, i);
        this.mTxPower = i2;
        this.mModelId = str2;
        this.mBluetoothAddress = str3;
        this.mData = bArr;
    }

    public int getTxPower() {
        return this.mTxPower;
    }

    public String getModelId() {
        return this.mModelId;
    }

    public String getBluetoothAddress() {
        return this.mBluetoothAddress;
    }

    public byte[] getData() {
        return this.mData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("FastPairDevice [");
        String name = getName();
        if (getName() != null && !name.isEmpty()) {
            sb.append("name=");
            sb.append(name);
            sb.append(", ");
        }
        sb.append("medium={");
        for (Integer intValue : getMediums()) {
            sb.append(mediumToString(intValue.intValue()));
        }
        sb.append("} rssi=");
        sb.append(getRssi());
        sb.append(" txPower=");
        sb.append(this.mTxPower);
        sb.append(" modelId=");
        sb.append(this.mModelId);
        sb.append(" bluetoothAddress=");
        sb.append(this.mBluetoothAddress);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FastPairDevice)) {
            return false;
        }
        FastPairDevice fastPairDevice = (FastPairDevice) obj;
        if (super.equals(obj) && this.mTxPower == fastPairDevice.mTxPower && Objects.equals(this.mModelId, fastPairDevice.mModelId) && Objects.equals(this.mBluetoothAddress, fastPairDevice.mBluetoothAddress) && Arrays.equals(this.mData, fastPairDevice.mData)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getName(), getMediums(), Integer.valueOf(getRssi()), Integer.valueOf(this.mTxPower), this.mModelId, this.mBluetoothAddress, Integer.valueOf(Arrays.hashCode(this.mData)));
    }

    public void writeToParcel(Parcel parcel, int i) {
        String name = getName();
        int i2 = 0;
        parcel.writeInt(name == null ? 0 : 1);
        if (name != null) {
            parcel.writeString(name);
        }
        List<Integer> mediums = getMediums();
        parcel.writeInt(mediums.size());
        for (Integer intValue : mediums) {
            parcel.writeInt(intValue.intValue());
        }
        parcel.writeInt(getRssi());
        parcel.writeInt(this.mTxPower);
        parcel.writeInt(this.mModelId == null ? 0 : 1);
        String str = this.mModelId;
        if (str != null) {
            parcel.writeString(str);
        }
        parcel.writeString(this.mBluetoothAddress);
        if (this.mData != null) {
            i2 = 1;
        }
        parcel.writeInt(i2);
        byte[] bArr = this.mData;
        if (bArr != null) {
            parcel.writeInt(bArr.length);
            parcel.writeByteArray(this.mData);
        }
    }

    public static final class Builder {
        private String mBluetoothAddress;
        private byte[] mData;
        private final List<Integer> mMediums = new ArrayList();
        private String mModelId;
        private String mName;
        private int mRssi;
        private int mTxPower;

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder addMedium(int i) {
            this.mMediums.add(Integer.valueOf(i));
            return this;
        }

        public Builder setRssi(int i) {
            this.mRssi = i;
            return this;
        }

        public Builder setTxPower(int i) {
            this.mTxPower = i;
            return this;
        }

        public Builder setModelId(String str) {
            this.mModelId = str;
            return this;
        }

        public Builder setBluetoothAddress(String str) {
            Objects.requireNonNull(str);
            this.mBluetoothAddress = str;
            return this;
        }

        public Builder setData(byte[] bArr) {
            this.mData = bArr;
            return this;
        }

        public FastPairDevice build() {
            return new FastPairDevice(this.mName, this.mMediums, this.mRssi, this.mTxPower, this.mModelId, this.mBluetoothAddress, this.mData);
        }
    }
}
