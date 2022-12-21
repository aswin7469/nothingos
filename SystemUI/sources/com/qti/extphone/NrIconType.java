package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrIconType implements Parcelable {
    public static final Parcelable.Creator<NrIconType> CREATOR = new Parcelable.Creator() {
        public NrIconType createFromParcel(Parcel parcel) {
            return new NrIconType(parcel);
        }

        public NrIconType[] newArray(int i) {
            return new NrIconType[i];
        }
    };
    public static final int INVALID = -1;
    private static final String TAG = "NrIconType";
    public static final int TYPE_5G_BASIC = 1;
    public static final int TYPE_5G_UWB = 2;
    public static final int TYPE_NONE = 0;
    private int mValue;

    public int describeContents() {
        return 0;
    }

    public NrIconType(int i) {
        this.mValue = i;
    }

    public NrIconType(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public int get() {
        return this.mValue;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mValue);
    }

    public void readFromParcel(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public String toString() {
        return "NrIconType: " + get();
    }
}
