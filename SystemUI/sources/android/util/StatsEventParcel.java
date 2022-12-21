package android.util;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class StatsEventParcel implements Parcelable {
    public static final Parcelable.Creator<StatsEventParcel> CREATOR = new Parcelable.Creator<StatsEventParcel>() {
        public StatsEventParcel createFromParcel(Parcel parcel) {
            StatsEventParcel statsEventParcel = new StatsEventParcel();
            statsEventParcel.readFromParcel(parcel);
            return statsEventParcel;
        }

        public StatsEventParcel[] newArray(int i) {
            return new StatsEventParcel[i];
        }
    };
    public byte[] buffer;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByteArray(this.buffer);
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
                    this.buffer = parcel.createByteArray();
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
