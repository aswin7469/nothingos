package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkSelectionMode implements Parcelable {
    public static final int ACCESS_MODE_INVALID = 0;
    public static final Parcelable.Creator<NetworkSelectionMode> CREATOR = new Parcelable.Creator() {
        public NetworkSelectionMode createFromParcel(Parcel parcel) {
            return new NetworkSelectionMode(parcel);
        }

        public NetworkSelectionMode[] newArray(int i) {
            return new NetworkSelectionMode[i];
        }
    };
    private static final String TAG = "NetworkSelectionMode";
    private int mAccessMode;
    private boolean mIsManual;

    public int describeContents() {
        return 0;
    }

    public NetworkSelectionMode(int i, boolean z) {
        this.mAccessMode = i;
        this.mIsManual = z;
    }

    public NetworkSelectionMode(Parcel parcel) {
        this.mAccessMode = 0;
        this.mIsManual = false;
        this.mAccessMode = parcel.readInt();
        this.mIsManual = parcel.readBoolean();
    }

    public int getAccessMode() {
        return this.mAccessMode;
    }

    public boolean getIsManual() {
        return this.mIsManual;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mAccessMode);
        parcel.writeBoolean(this.mIsManual);
    }

    public void readFromParcel(Parcel parcel) {
        this.mAccessMode = parcel.readInt();
        this.mIsManual = parcel.readBoolean();
    }

    public String toString() {
        return TAG + this.mAccessMode + "/" + this.mIsManual;
    }
}
