package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;
import com.qti.extphone.IExtPhoneCallback;

public class Client implements Parcelable {
    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator() {
        public Client createFromParcel(Parcel parcel) {
            return new Client(parcel);
        }

        public Client[] newArray(int i) {
            return new Client[i];
        }
    };
    private static final String TAG = "Client";
    private IExtPhoneCallback mCallback;
    private int mId;
    private String mPackageName;
    private int mUid;

    public int describeContents() {
        return 0;
    }

    public Client(int i, int i2, String str, IExtPhoneCallback iExtPhoneCallback) {
        this.mId = i;
        this.mUid = i2;
        this.mPackageName = str;
        this.mCallback = iExtPhoneCallback;
    }

    public Client(Parcel parcel) {
        this.mId = parcel.readInt();
        this.mUid = parcel.readInt();
        this.mPackageName = parcel.readString();
        this.mCallback = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
    }

    public int getId() {
        return this.mId;
    }

    public int getUid() {
        return this.mUid;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public IExtPhoneCallback getCallback() {
        return this.mCallback;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mId);
        parcel.writeInt(this.mUid);
        parcel.writeString(this.mPackageName);
        parcel.writeStrongInterface(this.mCallback);
    }

    public void readFromParcel(Parcel parcel) {
        this.mId = parcel.readInt();
        this.mUid = parcel.readInt();
        this.mPackageName = parcel.readString();
        this.mCallback = IExtPhoneCallback.Stub.asInterface(parcel.readStrongBinder());
    }

    public String toString() {
        return "Client{mId=" + getId() + ", mUid=" + getUid() + ", mPackageName='" + getPackageName() + "', mCallback=" + getCallback() + '}';
    }
}
