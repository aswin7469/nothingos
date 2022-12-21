package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {
    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator() {
        public Status createFromParcel(Parcel parcel) {
            return new Status(parcel);
        }

        public Status[] newArray(int i) {
            return new Status[i];
        }
    };
    public static final int EXCEPTION = -1;
    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    private static final String TAG = "Status";
    private int mStatus;

    public int describeContents() {
        return 0;
    }

    public Status(int i) {
        this.mStatus = i;
    }

    public Status(Parcel parcel) {
        this.mStatus = parcel.readInt();
    }

    public int get() {
        return this.mStatus;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mStatus);
    }

    public void readFromParcel(Parcel parcel) {
        this.mStatus = parcel.readInt();
    }

    public String toString() {
        return "Status: " + get();
    }
}
