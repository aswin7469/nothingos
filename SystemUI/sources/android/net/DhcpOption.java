package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class DhcpOption implements Parcelable {
    public static final Parcelable.Creator<DhcpOption> CREATOR = new Parcelable.Creator<DhcpOption>() {
        public DhcpOption createFromParcel(Parcel parcel) {
            return new DhcpOption(parcel.readByte(), parcel.createByteArray());
        }

        public DhcpOption[] newArray(int i) {
            return new DhcpOption[i];
        }
    };
    private final byte mType;
    private final byte[] mValue;

    public int describeContents() {
        return 0;
    }

    public DhcpOption(byte b, byte[] bArr) {
        this.mType = b;
        this.mValue = bArr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mType);
        parcel.writeByteArray(this.mValue);
    }

    public byte getType() {
        return this.mType;
    }

    public byte[] getValue() {
        byte[] bArr = this.mValue;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }
}
