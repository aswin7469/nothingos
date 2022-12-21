package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class UpperLayerIndInfo implements Parcelable {
    public static final Parcelable.Creator<UpperLayerIndInfo> CREATOR = new Parcelable.Creator() {
        public UpperLayerIndInfo createFromParcel(Parcel parcel) {
            return new UpperLayerIndInfo(parcel);
        }

        public UpperLayerIndInfo[] newArray(int i) {
            return new UpperLayerIndInfo[i];
        }
    };
    public static final int INVALID = -1;
    public static final int PLMN_INFO_LIST_AVAILABLE = 1;
    public static final int PLMN_INFO_LIST_UNAVAILABLE = 0;
    private static final String TAG = "UpperLayerIndInfo";
    public static final int UPPER_LAYER_IND_INFO_AVAILABLE = 1;
    public static final int UPPER_LAYER_IND_INFO_UNAVAILABLE = 0;
    private int mPlmnInfoListAvailable;
    private int mUpperLayerIndInfoAvailable;

    public int describeContents() {
        return 0;
    }

    public UpperLayerIndInfo(int i, int i2) {
        this.mPlmnInfoListAvailable = i;
        this.mUpperLayerIndInfoAvailable = i2;
    }

    public UpperLayerIndInfo(Parcel parcel) {
        this.mPlmnInfoListAvailable = 0;
        this.mUpperLayerIndInfoAvailable = 0;
        this.mPlmnInfoListAvailable = parcel.readInt();
        this.mUpperLayerIndInfoAvailable = parcel.readInt();
    }

    public int getPlmnInfoListAvailable() {
        return this.mPlmnInfoListAvailable;
    }

    public int getUpperLayerIndInfoAvailable() {
        return this.mUpperLayerIndInfoAvailable;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlmnInfoListAvailable);
        parcel.writeInt(this.mUpperLayerIndInfoAvailable);
    }

    public void readFromParcel(Parcel parcel) {
        this.mPlmnInfoListAvailable = parcel.readInt();
        this.mUpperLayerIndInfoAvailable = parcel.readInt();
    }

    public String toString() {
        return "UpperLayerIndInfo: PLMN: " + getPlmnInfoListAvailable() + " UpperLayerIndInfo: " + getUpperLayerIndInfoAvailable();
    }
}
