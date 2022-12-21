package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public final class IpSecTunnelInterfaceResponse implements Parcelable {
    public static final Parcelable.Creator<IpSecTunnelInterfaceResponse> CREATOR = new Parcelable.Creator<IpSecTunnelInterfaceResponse>() {
        public IpSecTunnelInterfaceResponse createFromParcel(Parcel parcel) {
            return new IpSecTunnelInterfaceResponse(parcel);
        }

        public IpSecTunnelInterfaceResponse[] newArray(int i) {
            return new IpSecTunnelInterfaceResponse[i];
        }
    };
    private static final String TAG = "IpSecTunnelInterfaceResponse";
    public final String interfaceName;
    public final int resourceId;
    public final int status;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.resourceId);
        parcel.writeString(this.interfaceName);
    }

    public IpSecTunnelInterfaceResponse(int i) {
        if (i != 0) {
            this.status = i;
            this.resourceId = -1;
            this.interfaceName = "";
            return;
        }
        throw new IllegalArgumentException("Valid status implies other args must be provided");
    }

    public IpSecTunnelInterfaceResponse(int i, int i2, String str) {
        this.status = i;
        this.resourceId = i2;
        this.interfaceName = str;
    }

    private IpSecTunnelInterfaceResponse(Parcel parcel) {
        this.status = parcel.readInt();
        this.resourceId = parcel.readInt();
        this.interfaceName = parcel.readString();
    }
}
