package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class QtiImeiInfo implements Parcelable {
    public static final Parcelable.Creator<QtiImeiInfo> CREATOR = new Parcelable.Creator() {
        public QtiImeiInfo createFromParcel(Parcel parcel) {
            return new QtiImeiInfo(parcel);
        }

        public QtiImeiInfo[] newArray(int i) {
            return new QtiImeiInfo[i];
        }
    };
    public static final int IMEI_TYPE_INVALID = 0;
    public static final int IMEI_TYPE_PRIMARY = 1;
    public static final int IMEI_TYPE_SECONDARY = 2;
    private static final String TAG = "QtiImeiInfo";
    private String mImei;
    private int mImeiType;
    private int mSlotId;

    public int describeContents() {
        return 0;
    }

    public QtiImeiInfo(int i, String str, int i2) {
        this.mSlotId = i;
        this.mImei = str;
        this.mImeiType = i2;
    }

    public QtiImeiInfo(Parcel parcel) {
        this.mSlotId = parcel.readInt();
        this.mImei = parcel.readString();
        this.mImeiType = parcel.readInt();
    }

    public int getSlotId() {
        return this.mSlotId;
    }

    public String getImei() {
        return this.mImei;
    }

    public int getImeiType() {
        return this.mImeiType;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSlotId);
        parcel.writeString(this.mImei);
        parcel.writeInt(this.mImeiType);
    }

    public void readFromParcel(Parcel parcel) {
        this.mSlotId = parcel.readInt();
        this.mImei = parcel.readString();
        this.mImeiType = parcel.readInt();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QtiImeiInfo qtiImeiInfo = (QtiImeiInfo) obj;
        if (this.mSlotId == qtiImeiInfo.mSlotId && this.mImeiType == qtiImeiInfo.mImeiType && Objects.equals(this.mImei, qtiImeiInfo.mImei)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "QtiImeiInfo{ slotId=" + this.mSlotId + " mImeiType=" + getImeiType() + "}";
    }
}
