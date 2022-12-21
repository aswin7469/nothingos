package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class MsimPreference implements Parcelable {
    public static final Parcelable.Creator<MsimPreference> CREATOR = new Parcelable.Creator() {
        public MsimPreference createFromParcel(Parcel parcel) {
            return new MsimPreference(parcel);
        }

        public MsimPreference[] newArray(int i) {
            return new MsimPreference[i];
        }
    };
    public static final int DSDA = 0;
    public static final int DSDS = 1;
    private static final String TAG = "MsimPreference";
    private int mMsimPreference;

    public static boolean isValid(int i) {
        return i == 0 || i == 1;
    }

    public int describeContents() {
        return 0;
    }

    public MsimPreference(int i) {
        this.mMsimPreference = i;
    }

    public MsimPreference(Parcel parcel) {
        this.mMsimPreference = parcel.readInt();
    }

    public int get() {
        return this.mMsimPreference;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mMsimPreference);
    }

    public String toString() {
        return "MsimPreference: " + get();
    }
}
