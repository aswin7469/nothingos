package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.ims.internal.uce.common.CapInfo;
/* loaded from: classes3.dex */
public class OptionsCapInfo implements Parcelable {
    public static final Parcelable.Creator<OptionsCapInfo> CREATOR = new Parcelable.Creator<OptionsCapInfo>() { // from class: com.android.ims.internal.uce.options.OptionsCapInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public OptionsCapInfo mo3559createFromParcel(Parcel source) {
            return new OptionsCapInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public OptionsCapInfo[] mo3560newArray(int size) {
            return new OptionsCapInfo[size];
        }
    };
    private CapInfo mCapInfo;
    private String mSdp;

    public static OptionsCapInfo getOptionsCapInfoInstance() {
        return new OptionsCapInfo();
    }

    public String getSdp() {
        return this.mSdp;
    }

    public void setSdp(String sdp) {
        this.mSdp = sdp;
    }

    public OptionsCapInfo() {
        this.mSdp = "";
        this.mCapInfo = new CapInfo();
    }

    public CapInfo getCapInfo() {
        return this.mCapInfo;
    }

    public void setCapInfo(CapInfo capInfo) {
        this.mCapInfo = capInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSdp);
        dest.writeParcelable(this.mCapInfo, flags);
    }

    private OptionsCapInfo(Parcel source) {
        this.mSdp = "";
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.mSdp = source.readString();
        this.mCapInfo = (CapInfo) source.readParcelable(CapInfo.class.getClassLoader());
    }
}
