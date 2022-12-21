package android.nearby;

import android.os.Parcel;
import android.os.Parcelable;

public class BroadcastRequestParcelable implements Parcelable {
    public static final Parcelable.Creator<BroadcastRequestParcelable> CREATOR = new Parcelable.Creator<BroadcastRequestParcelable>() {
        public BroadcastRequestParcelable createFromParcel(Parcel parcel) {
            return new BroadcastRequestParcelable(BroadcastRequest.createFromParcel(parcel));
        }

        public BroadcastRequestParcelable[] newArray(int i) {
            return new BroadcastRequestParcelable[i];
        }
    };
    private final BroadcastRequest mBroadcastRequest;

    public int describeContents() {
        return 0;
    }

    BroadcastRequestParcelable(BroadcastRequest broadcastRequest) {
        this.mBroadcastRequest = broadcastRequest;
    }

    public BroadcastRequest getBroadcastRequest() {
        return this.mBroadcastRequest;
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.mBroadcastRequest.writeToParcel(parcel, i);
    }
}
