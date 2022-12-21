package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;

public final class ParcelablePeerHandle extends PeerHandle implements Parcelable {
    public static final Parcelable.Creator<ParcelablePeerHandle> CREATOR = new Parcelable.Creator<ParcelablePeerHandle>() {
        public ParcelablePeerHandle[] newArray(int i) {
            return new ParcelablePeerHandle[i];
        }

        public ParcelablePeerHandle createFromParcel(Parcel parcel) {
            return new ParcelablePeerHandle(new PeerHandle(parcel.readInt()));
        }
    };

    public int describeContents() {
        return 0;
    }

    public ParcelablePeerHandle(PeerHandle peerHandle) {
        super(peerHandle.peerId);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.peerId);
    }
}
