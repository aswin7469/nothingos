package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class BearerAllocationStatus implements Parcelable {
    public static final int ALLOCATED = 1;
    public static final Parcelable.Creator<BearerAllocationStatus> CREATOR = new Parcelable.Creator() {
        public BearerAllocationStatus createFromParcel(Parcel parcel) {
            return new BearerAllocationStatus(parcel);
        }

        public BearerAllocationStatus[] newArray(int i) {
            return new BearerAllocationStatus[i];
        }
    };
    public static final int INVALID = -1;
    public static final int MMW_ALLOCATED = 2;
    public static final int NOT_ALLOCATED = 0;
    private static final String TAG = "BearerAllocationStatus";
    private int mValue;

    public int describeContents() {
        return 0;
    }

    public BearerAllocationStatus(int i) {
        this.mValue = i;
    }

    public BearerAllocationStatus(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public int getBearerAllocationStatus() {
        return this.mValue;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mValue);
    }

    public void readFromParcel(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public String toString() {
        return "BearerAllocationStatus: " + getBearerAllocationStatus();
    }
}
