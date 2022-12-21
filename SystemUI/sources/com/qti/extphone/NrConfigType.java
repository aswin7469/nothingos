package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrConfigType implements Parcelable {
    public static final Parcelable.Creator<NrConfigType> CREATOR = new Parcelable.Creator() {
        public NrConfigType createFromParcel(Parcel parcel) {
            return new NrConfigType(parcel);
        }

        public NrConfigType[] newArray(int i) {
            return new NrConfigType[i];
        }
    };
    public static final int INVALID = -1;
    public static final int NSA_CONFIGURATION = 0;
    public static final int SA_CONFIGURATION = 1;
    private static final String TAG = "NrConfigType";
    private int mValue;

    public int describeContents() {
        return 0;
    }

    public NrConfigType(int i) {
        this.mValue = i;
    }

    public NrConfigType(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public int getNrConfigType() {
        return this.mValue;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mValue);
    }

    public void readFromParcel(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public String toString() {
        return "NrConfigType: " + getNrConfigType();
    }
}
