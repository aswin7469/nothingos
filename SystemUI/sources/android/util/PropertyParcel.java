package android.util;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class PropertyParcel implements Parcelable {
    public static final Parcelable.Creator<PropertyParcel> CREATOR = new Parcelable.Creator<PropertyParcel>() {
        public PropertyParcel createFromParcel(Parcel parcel) {
            PropertyParcel propertyParcel = new PropertyParcel();
            propertyParcel.readFromParcel(parcel);
            return propertyParcel;
        }

        public PropertyParcel[] newArray(int i) {
            return new PropertyParcel[i];
        }
    };
    public String property;
    public String value;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeString(this.property);
        parcel.writeString(this.value);
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
                    this.property = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.value = parcel.readString();
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
