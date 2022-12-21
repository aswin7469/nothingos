package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrUwbIconMode implements Parcelable {
    public static final int CONNECTED = 1;
    public static final int CONNECTED_AND_IDLE = 3;
    public static final Parcelable.Creator<NrUwbIconMode> CREATOR = new Parcelable.Creator() {
        public NrUwbIconMode createFromParcel(Parcel parcel) {
            return new NrUwbIconMode(parcel);
        }

        public NrUwbIconMode[] newArray(int i) {
            return new NrUwbIconMode[i];
        }
    };
    public static final int IDLE = 2;
    public static final int NONE = 0;
    private static final String TAG = "NrUwbIconMode";
    private int mNrUwbIconMode;

    public static boolean isValid(int i) {
        return i == 0 || i == 1 || i == 2 || i == 3;
    }

    public int describeContents() {
        return 0;
    }

    public NrUwbIconMode(int i) {
        this.mNrUwbIconMode = i;
    }

    public NrUwbIconMode(Parcel parcel) {
        this.mNrUwbIconMode = parcel.readInt();
    }

    public int get() {
        return this.mNrUwbIconMode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mNrUwbIconMode);
    }

    public void readFromParcel(Parcel parcel) {
        this.mNrUwbIconMode = parcel.readInt();
    }

    public String toString() {
        return "NrUwbIconMode: " + get();
    }
}
