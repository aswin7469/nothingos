package android.net.wifi.aware;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class MacAddrMapping implements Parcelable {
    public static final Parcelable.Creator<MacAddrMapping> CREATOR = new Parcelable.Creator<MacAddrMapping>() {
        public MacAddrMapping createFromParcel(Parcel parcel) {
            MacAddrMapping macAddrMapping = new MacAddrMapping();
            macAddrMapping.readFromParcel(parcel);
            return macAddrMapping;
        }

        public MacAddrMapping[] newArray(int i) {
            return new MacAddrMapping[i];
        }
    };
    public byte[] macAddress;
    public int peerId = 0;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.peerId);
        parcel.writeByteArray(this.macAddress);
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
                    this.peerId = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.macAddress = parcel.createByteArray();
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
}
