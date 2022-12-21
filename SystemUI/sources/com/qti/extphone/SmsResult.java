package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class SmsResult implements Parcelable {
    public static final Parcelable.Creator<SmsResult> CREATOR = new Parcelable.Creator() {
        public SmsResult createFromParcel(Parcel parcel) {
            return new SmsResult(parcel);
        }

        public SmsResult[] newArray(int i) {
            return new SmsResult[i];
        }
    };
    private static final String TAG = "SmsResult";
    private String mAckPDU;
    private int mErrorCode;
    private int mMessageRef;

    public int describeContents() {
        return 0;
    }

    public SmsResult(int i, String str, int i2) {
        this.mMessageRef = i;
        this.mAckPDU = str;
        this.mErrorCode = i2;
    }

    public SmsResult(Parcel parcel) {
        this.mMessageRef = parcel.readInt();
        this.mAckPDU = parcel.readString();
        this.mErrorCode = parcel.readInt();
    }

    public int getMessageRef() {
        return this.mMessageRef;
    }

    public String getAckPDU() {
        return this.mAckPDU;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mMessageRef);
        parcel.writeString(this.mAckPDU);
        parcel.writeInt(this.mErrorCode);
    }

    public void readFromParcel(Parcel parcel) {
        this.mMessageRef = parcel.readInt();
        this.mAckPDU = parcel.readString();
        this.mErrorCode = parcel.readInt();
    }

    public String toString() {
        return "SmsResult{mMessageRef=" + getMessageRef() + ", mErrorCode=" + getErrorCode() + ", mAckPDU='" + getAckPDU() + "'}";
    }
}
