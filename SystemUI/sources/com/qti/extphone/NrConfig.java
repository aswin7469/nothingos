package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrConfig implements Parcelable {
    public static final Parcelable.Creator<NrConfig> CREATOR = new Parcelable.Creator() {
        public NrConfig createFromParcel(Parcel parcel) {
            return new NrConfig(parcel);
        }

        public NrConfig[] newArray(int i) {
            return new NrConfig[i];
        }
    };
    public static final int NR_CONFIG_COMBINED_SA_NSA = 0;
    public static final int NR_CONFIG_INVALID = -1;
    public static final int NR_CONFIG_NSA = 1;
    public static final int NR_CONFIG_SA = 2;
    private static final String TAG = "NrConfig";
    private int mValue;

    public int describeContents() {
        return 0;
    }

    public NrConfig(int i) {
        this.mValue = i;
    }

    public NrConfig(Parcel parcel) {
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
        return "NrConfig: " + get();
    }
}
