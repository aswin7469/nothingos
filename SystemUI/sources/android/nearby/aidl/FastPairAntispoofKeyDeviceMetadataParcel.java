package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairAntispoofKeyDeviceMetadataParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairAntispoofKeyDeviceMetadataParcel> CREATOR = new Parcelable.Creator<FastPairAntispoofKeyDeviceMetadataParcel>() {
        public FastPairAntispoofKeyDeviceMetadataParcel createFromParcel(Parcel parcel) {
            FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel = new FastPairAntispoofKeyDeviceMetadataParcel();
            fastPairAntispoofKeyDeviceMetadataParcel.readFromParcel(parcel);
            return fastPairAntispoofKeyDeviceMetadataParcel;
        }

        public FastPairAntispoofKeyDeviceMetadataParcel[] newArray(int i) {
            return new FastPairAntispoofKeyDeviceMetadataParcel[i];
        }
    };
    public byte[] antispoofPublicKey;
    public FastPairDeviceMetadataParcel deviceMetadata;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByteArray(this.antispoofPublicKey);
        parcel.writeTypedObject(this.deviceMetadata, i);
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
                    this.antispoofPublicKey = parcel.createByteArray();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.deviceMetadata = (FastPairDeviceMetadataParcel) parcel.readTypedObject(FastPairDeviceMetadataParcel.CREATOR);
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
        return describeContents(this.deviceMetadata) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
