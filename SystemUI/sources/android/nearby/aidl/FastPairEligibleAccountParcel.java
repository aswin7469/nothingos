package android.nearby.aidl;

import android.accounts.Account;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairEligibleAccountParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairEligibleAccountParcel> CREATOR = new Parcelable.Creator<FastPairEligibleAccountParcel>() {
        public FastPairEligibleAccountParcel createFromParcel(Parcel parcel) {
            FastPairEligibleAccountParcel fastPairEligibleAccountParcel = new FastPairEligibleAccountParcel();
            fastPairEligibleAccountParcel.readFromParcel(parcel);
            return fastPairEligibleAccountParcel;
        }

        public FastPairEligibleAccountParcel[] newArray(int i) {
            return new FastPairEligibleAccountParcel[i];
        }
    };
    public Account account;
    public boolean optIn = false;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.account, i);
        parcel.writeBoolean(this.optIn);
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
                    this.account = (Account) parcel.readTypedObject(Account.CREATOR);
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.optIn = parcel.readBoolean();
                        if (dataPosition <= Integer.MAX_VALUE - readInt) {
                            parcel.setDataPosition(dataPosition + readInt);
                            return;
                        }
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
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

    public int describeContents() {
        return describeContents(this.account) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
