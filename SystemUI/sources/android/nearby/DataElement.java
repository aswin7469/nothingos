package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
public final class DataElement implements Parcelable {
    public static final Parcelable.Creator<DataElement> CREATOR = new Parcelable.Creator<DataElement>() {
        public DataElement createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            byte[] bArr = new byte[parcel.readInt()];
            parcel.readByteArray(bArr);
            return new DataElement(readInt, bArr);
        }

        public DataElement[] newArray(int i) {
            return new DataElement[i];
        }
    };
    private final int mKey;
    private final byte[] mValue;

    public int describeContents() {
        return 0;
    }

    public DataElement(int i, byte[] bArr) {
        Preconditions.checkState(bArr != null, "value cannot be null");
        this.mKey = i;
        this.mValue = bArr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mKey);
        parcel.writeInt(this.mValue.length);
        parcel.writeByteArray(this.mValue);
    }

    public int getKey() {
        return this.mKey;
    }

    public byte[] getValue() {
        return this.mValue;
    }
}
