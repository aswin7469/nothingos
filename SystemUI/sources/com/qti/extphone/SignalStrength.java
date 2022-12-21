package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class SignalStrength implements Parcelable {
    public static final Parcelable.Creator<SignalStrength> CREATOR = new Parcelable.Creator() {
        public SignalStrength createFromParcel(Parcel parcel) {
            return new SignalStrength(parcel);
        }

        public SignalStrength[] newArray(int i) {
            return new SignalStrength[i];
        }
    };
    public static final int INVALID = -32768;
    private static final int MAX_NR_RSRP = -44;
    private static final int MAX_NR_SNR = -160;
    private static final int MIN_NR_RSRP = -140;
    private static final int MIN_NR_SNR = -240;
    private static final String TAG = "SignalStrength";
    private int mNrRsrp;
    private int mNrSnr;

    public int describeContents() {
        return 0;
    }

    public SignalStrength() {
        this.mNrRsrp = INVALID;
        this.mNrSnr = INVALID;
    }

    public SignalStrength(int i, int i2) {
        this.mNrRsrp = i;
        this.mNrSnr = i2;
    }

    public SignalStrength(Parcel parcel) {
        this.mNrRsrp = parcel.readInt();
        this.mNrSnr = parcel.readInt();
    }

    public int getRsrp() {
        return this.mNrRsrp;
    }

    public int getSnr() {
        return this.mNrSnr;
    }

    public String toString() {
        return "SignalStrength: Rsrp: " + getRsrp() + " Snr: " + getSnr();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mNrRsrp);
        parcel.writeInt(this.mNrSnr);
    }

    public void readFromParcel(Parcel parcel) {
        this.mNrRsrp = parcel.readInt();
        this.mNrSnr = parcel.readInt();
    }
}
