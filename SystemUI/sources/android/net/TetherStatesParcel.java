package android.net;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class TetherStatesParcel implements Parcelable {
    public static final Parcelable.Creator<TetherStatesParcel> CREATOR = new Parcelable.Creator<TetherStatesParcel>() {
        public TetherStatesParcel createFromParcel(Parcel parcel) {
            TetherStatesParcel tetherStatesParcel = new TetherStatesParcel();
            tetherStatesParcel.readFromParcel(parcel);
            return tetherStatesParcel;
        }

        public TetherStatesParcel[] newArray(int i) {
            return new TetherStatesParcel[i];
        }
    };
    public TetheringInterface[] availableList;
    public TetheringInterface[] erroredIfaceList;
    public int[] lastErrorList;
    public TetheringInterface[] localOnlyList;
    public TetheringInterface[] tetheredList;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedArray(this.availableList, i);
        parcel.writeTypedArray(this.tetheredList, i);
        parcel.writeTypedArray(this.localOnlyList, i);
        parcel.writeTypedArray(this.erroredIfaceList, i);
        parcel.writeIntArray(this.lastErrorList);
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
                    this.availableList = (TetheringInterface[]) parcel.createTypedArray(TetheringInterface.CREATOR);
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.tetheredList = (TetheringInterface[]) parcel.createTypedArray(TetheringInterface.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.localOnlyList = (TetheringInterface[]) parcel.createTypedArray(TetheringInterface.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.erroredIfaceList = (TetheringInterface[]) parcel.createTypedArray(TetheringInterface.CREATOR);
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.lastErrorList = parcel.createIntArray();
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
        return describeContents(this.erroredIfaceList) | describeContents(this.availableList) | 0 | describeContents(this.tetheredList) | describeContents(this.localOnlyList);
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
