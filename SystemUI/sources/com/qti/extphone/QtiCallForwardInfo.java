package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class QtiCallForwardInfo implements Parcelable {
    public static final Parcelable.Creator<QtiCallForwardInfo> CREATOR = new Parcelable.Creator() {
        public QtiCallForwardInfo createFromParcel(Parcel parcel) {
            return new QtiCallForwardInfo(parcel);
        }

        public QtiCallForwardInfo[] newArray(int i) {
            return new QtiCallForwardInfo[i];
        }
    };
    private static final String TAG = "QtiCallForwardInfo";
    public String number;
    public int reason;
    public int serviceClass;
    public int status;
    public int timeSeconds;
    public int toa;

    public int describeContents() {
        return 0;
    }

    public QtiCallForwardInfo() {
    }

    public QtiCallForwardInfo(Parcel parcel) {
        this.status = parcel.readInt();
        this.reason = parcel.readInt();
        this.serviceClass = parcel.readInt();
        this.toa = parcel.readInt();
        this.number = parcel.readString();
        this.timeSeconds = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.reason);
        parcel.writeInt(this.serviceClass);
        parcel.writeInt(this.toa);
        parcel.writeString(this.number);
        parcel.writeInt(this.timeSeconds);
    }

    public void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.reason = parcel.readInt();
        this.serviceClass = parcel.readInt();
        this.toa = parcel.readInt();
        this.number = parcel.readString();
        this.timeSeconds = parcel.readInt();
    }

    public String toString() {
        return "[QtiCallForwardInfo: status=" + (this.status == 0 ? " not active " : " active ") + ", reason= " + this.reason + ", serviceClass= " + this.serviceClass + ", timeSec= " + this.timeSeconds + " seconds]";
    }
}
