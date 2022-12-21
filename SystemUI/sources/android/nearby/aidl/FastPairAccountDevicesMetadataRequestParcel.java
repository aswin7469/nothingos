package android.nearby.aidl;

import android.accounts.Account;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairAccountDevicesMetadataRequestParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairAccountDevicesMetadataRequestParcel> CREATOR = new Parcelable.Creator<FastPairAccountDevicesMetadataRequestParcel>() {
        public FastPairAccountDevicesMetadataRequestParcel createFromParcel(Parcel parcel) {
            FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel = new FastPairAccountDevicesMetadataRequestParcel();
            fastPairAccountDevicesMetadataRequestParcel.readFromParcel(parcel);
            return fastPairAccountDevicesMetadataRequestParcel;
        }

        public FastPairAccountDevicesMetadataRequestParcel[] newArray(int i) {
            return new FastPairAccountDevicesMetadataRequestParcel[i];
        }
    };
    public Account account;
    public ByteArrayParcel[] deviceAccountKeys;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.account, i);
        parcel.writeTypedArray(this.deviceAccountKeys, i);
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
                        this.deviceAccountKeys = (ByteArrayParcel[]) parcel.createTypedArray(ByteArrayParcel.CREATOR);
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
        return describeContents(this.deviceAccountKeys) | describeContents(this.account) | 0;
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int i = 0;
            for (Object describeContents : (Object[]) obj) {
                i |= describeContents(describeContents);
            }
            return i;
        } else if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        } else {
            return 0;
        }
    }
}
