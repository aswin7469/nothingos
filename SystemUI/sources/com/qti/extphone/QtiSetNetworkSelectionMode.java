package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class QtiSetNetworkSelectionMode implements Parcelable {
    public static final int ACCESS_MODE_INVALID = 0;
    public static final int ACCESS_NETWORK_UNKNOWN = 0;
    public static final long CAG_ID_INVALID = -1;
    public static final Parcelable.Creator<QtiSetNetworkSelectionMode> CREATOR = new Parcelable.Creator() {
        public QtiSetNetworkSelectionMode createFromParcel(Parcel parcel) {
            return new QtiSetNetworkSelectionMode(parcel);
        }

        public QtiSetNetworkSelectionMode[] newArray(int i) {
            return new QtiSetNetworkSelectionMode[i];
        }
    };
    private static final String TAG = "QtiSetNetworkSelectionMode";
    private int mAccessMode;
    private long mCagId;
    private byte[] mNid;
    private String mOperatorNumeric;
    private int mRan;

    public int describeContents() {
        return 0;
    }

    public QtiSetNetworkSelectionMode(String str, int i, int i2, long j, byte[] bArr) {
        this.mOperatorNumeric = str;
        this.mRan = i;
        this.mAccessMode = i2;
        this.mCagId = j;
        this.mNid = bArr;
    }

    public QtiSetNetworkSelectionMode(Parcel parcel) {
        this.mRan = 0;
        this.mAccessMode = 0;
        this.mCagId = -1;
        this.mOperatorNumeric = parcel.readString();
        this.mRan = parcel.readInt();
        this.mAccessMode = parcel.readInt();
        this.mCagId = parcel.readLong();
        int readInt = parcel.readInt();
        if (readInt > 0) {
            byte[] bArr = new byte[readInt];
            this.mNid = bArr;
            parcel.readByteArray(bArr);
            return;
        }
        this.mNid = null;
    }

    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    public int getRan() {
        return this.mRan;
    }

    public int getAccessMode() {
        return this.mAccessMode;
    }

    public long getCagId() {
        return this.mCagId;
    }

    public byte[] getNid() {
        return this.mNid;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mOperatorNumeric);
        parcel.writeInt(this.mRan);
        parcel.writeInt(this.mAccessMode);
        parcel.writeLong(this.mCagId);
        byte[] bArr = this.mNid;
        if (bArr == null || bArr.length <= 0) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(bArr.length);
        parcel.writeByteArray(this.mNid);
    }

    public void readFromParcel(Parcel parcel) {
        this.mOperatorNumeric = parcel.readString();
        this.mRan = parcel.readInt();
        this.mAccessMode = parcel.readInt();
        this.mCagId = parcel.readLong();
        int readInt = parcel.readInt();
        if (readInt > 0) {
            byte[] bArr = new byte[readInt];
            this.mNid = bArr;
            parcel.readByteArray(bArr);
            return;
        }
        this.mNid = null;
    }

    public String toString() {
        return TAG + this.mOperatorNumeric + "/" + this.mRan + "/" + this.mAccessMode + "/" + this.mCagId + "/" + this.mNid;
    }
}
