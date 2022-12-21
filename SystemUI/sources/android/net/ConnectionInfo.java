package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public final class ConnectionInfo implements Parcelable {
    public static final Parcelable.Creator<ConnectionInfo> CREATOR = new Parcelable.Creator<ConnectionInfo>() {
        public ConnectionInfo createFromParcel(Parcel parcel) {
            try {
                try {
                    return new ConnectionInfo(parcel.readInt(), new InetSocketAddress(InetAddress.getByAddress(parcel.createByteArray()), parcel.readInt()), new InetSocketAddress(InetAddress.getByAddress(parcel.createByteArray()), parcel.readInt()));
                } catch (UnknownHostException unused) {
                    throw new IllegalArgumentException("Invalid InetAddress");
                }
            } catch (UnknownHostException unused2) {
                throw new IllegalArgumentException("Invalid InetAddress");
            }
        }

        public ConnectionInfo[] newArray(int i) {
            return new ConnectionInfo[i];
        }
    };
    public final InetSocketAddress local;
    public final int protocol;
    public final InetSocketAddress remote;

    public int describeContents() {
        return 0;
    }

    public ConnectionInfo(int i, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) {
        this.protocol = i;
        this.local = inetSocketAddress;
        this.remote = inetSocketAddress2;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.protocol);
        parcel.writeByteArray(this.local.getAddress().getAddress());
        parcel.writeInt(this.local.getPort());
        parcel.writeByteArray(this.remote.getAddress().getAddress());
        parcel.writeInt(this.remote.getPort());
    }
}
