package com.p025nt.facerecognition.manager;

import android.os.Parcel;
import android.os.Parcelable;

/* renamed from: com.nt.facerecognition.manager.FaceMetadata */
public class FaceMetadata implements Parcelable {
    public static final Parcelable.Creator<FaceMetadata> CREATOR = new Parcelable.Creator<FaceMetadata>() {
        public FaceMetadata createFromParcel(Parcel parcel) {
            return new FaceMetadata(parcel);
        }

        public FaceMetadata[] newArray(int i) {
            return new FaceMetadata[i];
        }
    };
    private long mDeviceId;
    private int mFaceId;
    private int mGroupId;
    private CharSequence mName;

    public int describeContents() {
        return 0;
    }

    public FaceMetadata(CharSequence charSequence, int i, int i2, long j) {
        this.mName = charSequence;
        this.mGroupId = i;
        this.mFaceId = i2;
        this.mDeviceId = j;
    }

    private FaceMetadata(Parcel parcel) {
        this.mName = parcel.readString();
        this.mGroupId = parcel.readInt();
        this.mFaceId = parcel.readInt();
        this.mDeviceId = parcel.readLong();
    }

    public CharSequence getName() {
        return this.mName;
    }

    public int getFaceId() {
        return this.mFaceId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public long getDeviceId() {
        return this.mDeviceId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName.toString());
        parcel.writeInt(this.mGroupId);
        parcel.writeInt(this.mFaceId);
        parcel.writeLong(this.mDeviceId);
    }
}
