package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class DcParam implements Parcelable {
    public static final Parcelable.Creator<DcParam> CREATOR = new Parcelable.Creator() {
        public DcParam createFromParcel(Parcel parcel) {
            return new DcParam(parcel);
        }

        public DcParam[] newArray(int i) {
            return new DcParam[i];
        }
    };
    public static final int DCNR_RESTRICTED = 0;
    public static final int DCNR_UNRESTRICTED = 1;
    public static final int ENDC_AVAILABLE = 1;
    public static final int ENDC_UNAVAILABLE = 0;
    public static final int INVALID = -1;
    private static final String TAG = "DcParam";
    private int mDcnr;
    private int mEndc;

    public int describeContents() {
        return 0;
    }

    public DcParam(int i, int i2) {
        this.mEndc = i;
        this.mDcnr = i2;
    }

    public DcParam(Parcel parcel) {
        this.mEndc = 0;
        this.mDcnr = 0;
        this.mEndc = parcel.readInt();
        this.mDcnr = parcel.readInt();
    }

    public int getEndc() {
        return this.mEndc;
    }

    public int getDcnr() {
        return this.mDcnr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mEndc);
        parcel.writeInt(this.mDcnr);
    }

    public void readFromParcel(Parcel parcel) {
        this.mEndc = parcel.readInt();
        this.mDcnr = parcel.readInt();
    }

    public String toString() {
        return "DcParam: Endc: " + getEndc() + " Dcnr: " + getDcnr();
    }
}
