package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
public final class WifiConnectedSessionInfo implements Parcelable {
    public static final Parcelable.Creator<WifiConnectedSessionInfo> CREATOR = new Parcelable.Creator<WifiConnectedSessionInfo>() {
        public WifiConnectedSessionInfo createFromParcel(Parcel parcel) {
            return new WifiConnectedSessionInfo(parcel.readInt(), parcel.readBoolean());
        }

        public WifiConnectedSessionInfo[] newArray(int i) {
            return new WifiConnectedSessionInfo[i];
        }
    };
    private final boolean mIsUserSelected;
    private final int mSessionId;

    public int describeContents() {
        return 0;
    }

    private WifiConnectedSessionInfo(int i, boolean z) {
        this.mSessionId = i;
        this.mIsUserSelected = z;
    }

    public static final class Builder {
        private boolean mIsUserSelected = false;
        private final int mSessionId;

        public Builder(int i) {
            this.mSessionId = i;
        }

        public Builder setUserSelected(boolean z) {
            this.mIsUserSelected = z;
            return this;
        }

        public WifiConnectedSessionInfo build() {
            return new WifiConnectedSessionInfo(this.mSessionId, this.mIsUserSelected);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSessionId);
        parcel.writeBoolean(this.mIsUserSelected);
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public boolean isUserSelected() {
        return this.mIsUserSelected;
    }
}
