package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairEligibleAccountsRequestParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairEligibleAccountsRequestParcel> CREATOR = new Parcelable.Creator<FastPairEligibleAccountsRequestParcel>() {
        public FastPairEligibleAccountsRequestParcel createFromParcel(Parcel parcel) {
            FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel = new FastPairEligibleAccountsRequestParcel();
            fastPairEligibleAccountsRequestParcel.readFromParcel(parcel);
            return fastPairEligibleAccountsRequestParcel;
        }

        public FastPairEligibleAccountsRequestParcel[] newArray(int i) {
            return new FastPairEligibleAccountsRequestParcel[i];
        }
    };

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt < 4) {
            try {
                throw new BadParcelableException("Parcelable too small");
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        } else if (dataPosition <= Integer.MAX_VALUE - readInt) {
            parcel.setDataPosition(dataPosition + readInt);
        } else {
            throw new BadParcelableException("Overflow in the size of parcelable");
        }
    }
}
