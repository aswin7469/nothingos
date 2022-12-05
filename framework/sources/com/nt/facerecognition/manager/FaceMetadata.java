package com.nt.facerecognition.manager;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public class FaceMetadata implements Parcelable {
    public static final Parcelable.Creator<FaceMetadata> CREATOR = new Parcelable.Creator<FaceMetadata>() { // from class: com.nt.facerecognition.manager.FaceMetadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FaceMetadata mo3559createFromParcel(Parcel in) {
            return new FaceMetadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FaceMetadata[] mo3560newArray(int size) {
            return new FaceMetadata[size];
        }
    };
    private long mDeviceId;
    private int mFaceId;
    private int mGroupId;
    private CharSequence mName;

    public FaceMetadata(CharSequence name, int groupId, int faceId, long deviceId) {
        this.mName = name;
        this.mGroupId = groupId;
        this.mFaceId = faceId;
        this.mDeviceId = deviceId;
    }

    private FaceMetadata(Parcel in) {
        this.mName = in.readString();
        this.mGroupId = in.readInt();
        this.mFaceId = in.readInt();
        this.mDeviceId = in.readLong();
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mName.toString());
        out.writeInt(this.mGroupId);
        out.writeInt(this.mFaceId);
        out.writeLong(this.mDeviceId);
    }
}
