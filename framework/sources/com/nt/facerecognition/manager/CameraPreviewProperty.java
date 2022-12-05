package com.nt.facerecognition.manager;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public class CameraPreviewProperty implements Parcelable {
    public static final Parcelable.Creator<CameraPreviewProperty> CREATOR = new Parcelable.Creator<CameraPreviewProperty>() { // from class: com.nt.facerecognition.manager.CameraPreviewProperty.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CameraPreviewProperty mo3559createFromParcel(Parcel in) {
            return new CameraPreviewProperty(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CameraPreviewProperty[] mo3560newArray(int size) {
            return new CameraPreviewProperty[size];
        }
    };
    private int mCameraHeight;
    private boolean mCameraIsOpen;
    private int mCameraWidth;

    public CameraPreviewProperty(int cameraWith, int cameraHeight, boolean cameraIsOpen) {
        this.mCameraWidth = cameraWith;
        this.mCameraHeight = cameraHeight;
        this.mCameraIsOpen = cameraIsOpen;
    }

    private CameraPreviewProperty(Parcel in) {
        this.mCameraWidth = in.readInt();
        this.mCameraHeight = in.readInt();
        this.mCameraIsOpen = in.readBoolean();
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCameraWidth);
        out.writeInt(this.mCameraHeight);
        out.writeBoolean(this.mCameraIsOpen);
    }
}
