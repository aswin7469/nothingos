package com.p007nt.facerecognition.manager;

import android.os.Parcel;
import android.os.Parcelable;

/* renamed from: com.nt.facerecognition.manager.CameraPreviewProperty */
public class CameraPreviewProperty implements Parcelable {
    public static final Parcelable.Creator<CameraPreviewProperty> CREATOR = new Parcelable.Creator<CameraPreviewProperty>() {
        public CameraPreviewProperty createFromParcel(Parcel parcel) {
            return new CameraPreviewProperty(parcel);
        }

        public CameraPreviewProperty[] newArray(int i) {
            return new CameraPreviewProperty[i];
        }
    };
    private int mCameraHeight;
    private boolean mCameraIsOpen;
    private int mCameraWidth;

    public int describeContents() {
        return 0;
    }

    private CameraPreviewProperty(Parcel parcel) {
        this.mCameraWidth = parcel.readInt();
        this.mCameraHeight = parcel.readInt();
        this.mCameraIsOpen = parcel.readBoolean();
    }

    public int getCameraWidth() {
        return this.mCameraWidth;
    }

    public int getCameraHeight() {
        return this.mCameraHeight;
    }

    public boolean getCameraIsOpen() {
        return this.mCameraIsOpen;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mCameraWidth);
        parcel.writeInt(this.mCameraHeight);
        parcel.writeBoolean(this.mCameraIsOpen);
    }
}
