package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class ByteArrayParcel implements Parcelable {
    public static final Parcelable.Creator<ByteArrayParcel> CREATOR = new Parcelable.Creator<ByteArrayParcel>() {
        public ByteArrayParcel createFromParcel(Parcel parcel) {
            ByteArrayParcel byteArrayParcel = new ByteArrayParcel();
            byteArrayParcel.readFromParcel(parcel);
            return byteArrayParcel;
        }

        public ByteArrayParcel[] newArray(int i) {
            return new ByteArrayParcel[i];
        }
    };
    public byte[] byteArray;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByteArray(this.byteArray);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt >= 4) {
            try {
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.byteArray = parcel.createByteArray();
                    if (dataPosition <= Integer.MAX_VALUE - readInt) {
                        parcel.setDataPosition(dataPosition + readInt);
                        return;
                    }
                    throw new BadParcelableException("Overflow in the size of parcelable");
                } else if (dataPosition <= Integer.MAX_VALUE - readInt) {
                    parcel.setDataPosition(dataPosition + readInt);
                } else {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        } else {
            throw new BadParcelableException("Parcelable too small");
        }
    }
}
