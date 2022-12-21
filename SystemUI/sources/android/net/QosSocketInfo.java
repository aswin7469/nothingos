package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.system.OsConstants;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.p026io.IOException;
import java.util.Objects;

@SystemApi
public final class QosSocketInfo implements Parcelable {
    public static final Parcelable.Creator<QosSocketInfo> CREATOR = new Parcelable.Creator<QosSocketInfo>() {
        public QosSocketInfo createFromParcel(Parcel parcel) {
            return new QosSocketInfo(parcel);
        }

        public QosSocketInfo[] newArray(int i) {
            return new QosSocketInfo[i];
        }
    };
    private final InetSocketAddress mLocalSocketAddress;
    private final Network mNetwork;
    private final ParcelFileDescriptor mParcelFileDescriptor;
    private final InetSocketAddress mRemoteSocketAddress;
    private final int mSocketType;

    public int describeContents() {
        return 0;
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    /* access modifiers changed from: package-private */
    public ParcelFileDescriptor getParcelFileDescriptor() {
        return this.mParcelFileDescriptor;
    }

    public InetSocketAddress getLocalSocketAddress() {
        return this.mLocalSocketAddress;
    }

    public InetSocketAddress getRemoteSocketAddress() {
        return this.mRemoteSocketAddress;
    }

    public int getSocketType() {
        return this.mSocketType;
    }

    public QosSocketInfo(Network network, Socket socket) throws IOException {
        Objects.requireNonNull(socket, "socket cannot be null");
        this.mNetwork = (Network) Objects.requireNonNull(network, "network cannot be null");
        this.mParcelFileDescriptor = ParcelFileDescriptor.fromSocket(socket);
        this.mLocalSocketAddress = new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());
        this.mSocketType = OsConstants.SOCK_STREAM;
        if (socket.isConnected()) {
            this.mRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        } else {
            this.mRemoteSocketAddress = null;
        }
    }

    public QosSocketInfo(Network network, DatagramSocket datagramSocket) throws IOException {
        Objects.requireNonNull(datagramSocket, "socket cannot be null");
        this.mNetwork = (Network) Objects.requireNonNull(network, "network cannot be null");
        this.mParcelFileDescriptor = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
        this.mLocalSocketAddress = new InetSocketAddress(datagramSocket.getLocalAddress(), datagramSocket.getLocalPort());
        this.mSocketType = OsConstants.SOCK_DGRAM;
        if (datagramSocket.isConnected()) {
            this.mRemoteSocketAddress = (InetSocketAddress) datagramSocket.getRemoteSocketAddress();
        } else {
            this.mRemoteSocketAddress = null;
        }
    }

    private QosSocketInfo(Parcel parcel) {
        InetSocketAddress inetSocketAddress;
        this.mNetwork = (Network) Objects.requireNonNull(Network.CREATOR.createFromParcel(parcel));
        this.mParcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
        this.mLocalSocketAddress = readSocketAddress(parcel, parcel.readInt());
        int readInt = parcel.readInt();
        if (readInt == 0) {
            inetSocketAddress = null;
        } else {
            inetSocketAddress = readSocketAddress(parcel, readInt);
        }
        this.mRemoteSocketAddress = inetSocketAddress;
        this.mSocketType = parcel.readInt();
    }

    private InetSocketAddress readSocketAddress(Parcel parcel, int i) {
        byte[] bArr = new byte[i];
        parcel.readByteArray(bArr);
        try {
            return new InetSocketAddress(InetAddress.getByAddress(bArr), parcel.readInt());
        } catch (UnknownHostException e) {
            throw new RuntimeException("UnknownHostException on numeric address", e);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.mNetwork.writeToParcel(parcel, 0);
        this.mParcelFileDescriptor.writeToParcel(parcel, 0);
        byte[] address = this.mLocalSocketAddress.getAddress().getAddress();
        parcel.writeInt(address.length);
        parcel.writeByteArray(address);
        parcel.writeInt(this.mLocalSocketAddress.getPort());
        InetSocketAddress inetSocketAddress = this.mRemoteSocketAddress;
        if (inetSocketAddress == null) {
            parcel.writeInt(0);
        } else {
            byte[] address2 = inetSocketAddress.getAddress().getAddress();
            parcel.writeInt(address2.length);
            parcel.writeByteArray(address2);
            parcel.writeInt(this.mRemoteSocketAddress.getPort());
        }
        parcel.writeInt(this.mSocketType);
    }
}
