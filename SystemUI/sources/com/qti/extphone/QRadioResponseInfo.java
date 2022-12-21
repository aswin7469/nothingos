package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class QRadioResponseInfo implements Parcelable {
    public static final Parcelable.Creator<QRadioResponseInfo> CREATOR = new Parcelable.Creator() {
        public QRadioResponseInfo createFromParcel(Parcel parcel) {
            return new QRadioResponseInfo(parcel);
        }

        public QRadioResponseInfo[] newArray(int i) {
            return new QRadioResponseInfo[i];
        }
    };
    public static final int GENERIC_FAILURE = 2;
    public static final int NONE = 0;
    public static final int RADIO_NOT_AVAILABLE = 1;
    public static final int SOLICITED = 0;
    public static final int SOLICITED_ACK = 1;
    public static final int SOLICITED_ACK_EXP = 2;
    private static final String TAG = "QRadioResponseInfo";
    private int mError;
    private int mResponseType;
    private int mSerial;

    public int describeContents() {
        return 0;
    }

    public QRadioResponseInfo(int i, int i2, int i3) {
        this.mResponseType = i;
        this.mSerial = i2;
        this.mError = i3;
    }

    public QRadioResponseInfo(Parcel parcel) {
        this.mResponseType = parcel.readInt();
        this.mSerial = parcel.readInt();
        this.mError = parcel.readInt();
    }

    public int getResponseType() {
        return this.mResponseType;
    }

    public int getSerial() {
        return this.mSerial;
    }

    public int getError() {
        return this.mError;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mResponseType);
        parcel.writeInt(this.mSerial);
        parcel.writeInt(this.mError);
    }

    public void readFromParcel(Parcel parcel) {
        this.mResponseType = parcel.readInt();
        this.mSerial = parcel.readInt();
        this.mError = parcel.readInt();
    }

    public String toString() {
        return "QRadioResponseInfo: ResponseType: " + getResponseType() + " Serial: " + getSerial() + " Error: " + getError();
    }
}
