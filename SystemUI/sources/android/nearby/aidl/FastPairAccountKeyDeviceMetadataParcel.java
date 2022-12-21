package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairAccountKeyDeviceMetadataParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairAccountKeyDeviceMetadataParcel> CREATOR = new Parcelable.Creator<FastPairAccountKeyDeviceMetadataParcel>() {
        public FastPairAccountKeyDeviceMetadataParcel createFromParcel(Parcel parcel) {
            FastPairAccountKeyDeviceMetadataParcel fastPairAccountKeyDeviceMetadataParcel = new FastPairAccountKeyDeviceMetadataParcel();
            fastPairAccountKeyDeviceMetadataParcel.readFromParcel(parcel);
            return fastPairAccountKeyDeviceMetadataParcel;
        }

        public FastPairAccountKeyDeviceMetadataParcel[] newArray(int i) {
            return new FastPairAccountKeyDeviceMetadataParcel[i];
        }
    };
    public byte[] deviceAccountKey;
    public FastPairDiscoveryItemParcel discoveryItem;
    public FastPairDeviceMetadataParcel metadata;
    public byte[] sha256DeviceAccountKeyPublicAddress;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByteArray(this.deviceAccountKey);
        parcel.writeByteArray(this.sha256DeviceAccountKeyPublicAddress);
        parcel.writeTypedObject(this.metadata, i);
        parcel.writeTypedObject(this.discoveryItem, i);
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
                    this.deviceAccountKey = parcel.createByteArray();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.sha256DeviceAccountKeyPublicAddress = parcel.createByteArray();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.metadata = (FastPairDeviceMetadataParcel) parcel.readTypedObject(FastPairDeviceMetadataParcel.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.discoveryItem = (FastPairDiscoveryItemParcel) parcel.readTypedObject(FastPairDiscoveryItemParcel.CREATOR);
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
        return describeContents(this.discoveryItem) | describeContents(this.metadata) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
