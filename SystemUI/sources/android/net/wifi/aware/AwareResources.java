package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;

public final class AwareResources implements Parcelable {
    public static final Parcelable.Creator<AwareResources> CREATOR = new Parcelable.Creator<AwareResources>() {
        public AwareResources createFromParcel(Parcel parcel) {
            return new AwareResources(parcel.readInt(), parcel.readInt(), parcel.readInt());
        }

        public AwareResources[] newArray(int i) {
            return new AwareResources[i];
        }
    };
    private int mNumOfAvailableNdps;
    private int mNumOfAvailablePublishSessions;
    private int mNumOfAvailableSubscribeSessions;

    public int describeContents() {
        return 0;
    }

    public AwareResources(int i, int i2, int i3) {
        this.mNumOfAvailableNdps = i;
        this.mNumOfAvailablePublishSessions = i2;
        this.mNumOfAvailableSubscribeSessions = i3;
    }

    public int getAvailableDataPathsCount() {
        return this.mNumOfAvailableNdps;
    }

    public int getAvailablePublishSessionsCount() {
        return this.mNumOfAvailablePublishSessions;
    }

    public int getAvailableSubscribeSessionsCount() {
        return this.mNumOfAvailableSubscribeSessions;
    }

    public void setNumOfAvailableDataPaths(int i) {
        this.mNumOfAvailableNdps = i;
    }

    public void setNumOfAvailablePublishSessions(int i) {
        this.mNumOfAvailablePublishSessions = i;
    }

    public void setNumOfAvailableSubscribeSessions(int i) {
        this.mNumOfAvailableSubscribeSessions = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mNumOfAvailableNdps);
        parcel.writeInt(this.mNumOfAvailablePublishSessions);
        parcel.writeInt(this.mNumOfAvailableSubscribeSessions);
    }
}
