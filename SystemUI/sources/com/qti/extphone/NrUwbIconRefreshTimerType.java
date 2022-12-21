package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrUwbIconRefreshTimerType implements Parcelable {
    public static final Parcelable.Creator<NrUwbIconRefreshTimerType> CREATOR = new Parcelable.Creator() {
        public NrUwbIconRefreshTimerType createFromParcel(Parcel parcel) {
            return new NrUwbIconRefreshTimerType(parcel);
        }

        public NrUwbIconRefreshTimerType[] newArray(int i) {
            return new NrUwbIconRefreshTimerType[i];
        }
    };
    public static final int IDLE = 2;
    public static final int IDLE_TO_CONNECT = 1;
    public static final int SCG_TO_MCG = 0;
    private static final String TAG = "NrUwbIconRefreshTimerType";
    private int mNrUwbIconRefreshTimerType;

    public static boolean isValid(int i) {
        return i == 0 || i == 1 || i == 2;
    }

    public int describeContents() {
        return 0;
    }

    public NrUwbIconRefreshTimerType(int i) {
        this.mNrUwbIconRefreshTimerType = i;
    }

    public NrUwbIconRefreshTimerType(Parcel parcel) {
        this.mNrUwbIconRefreshTimerType = parcel.readInt();
    }

    public int get() {
        return this.mNrUwbIconRefreshTimerType;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mNrUwbIconRefreshTimerType);
    }

    public void readFromParcel(Parcel parcel) {
        this.mNrUwbIconRefreshTimerType = parcel.readInt();
    }

    public String toString() {
        return "NrUwbIconRefreshTimerType: " + get();
    }
}
