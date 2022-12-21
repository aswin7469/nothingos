package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.net.InetAddress;
import java.util.Objects;

@SystemApi
public final class TcpKeepalivePacketData extends KeepalivePacketData implements Parcelable {
    public static final Parcelable.Creator<TcpKeepalivePacketData> CREATOR = new Parcelable.Creator<TcpKeepalivePacketData>() {
        public TcpKeepalivePacketData createFromParcel(Parcel parcel) {
            try {
                return TcpKeepalivePacketData.readFromParcel(parcel);
            } catch (InvalidPacketException e) {
                throw new IllegalArgumentException("Invalid TCP keepalive data: " + e.getError());
            }
        }

        public TcpKeepalivePacketData[] newArray(int i) {
            return new TcpKeepalivePacketData[i];
        }
    };
    private static final String TAG = "TcpKeepalivePacketData";
    public final int ipTos;
    public final int ipTtl;
    public final int tcpAck;
    public final int tcpSeq;
    public final int tcpWindow;
    public final int tcpWindowScale;

    public int describeContents() {
        return 0;
    }

    public TcpKeepalivePacketData(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2, byte[] bArr, int i3, int i4, int i5, int i6, int i7, int i8) throws InvalidPacketException {
        super(inetAddress, i, inetAddress2, i2, bArr);
        this.tcpSeq = i3;
        this.tcpAck = i4;
        this.tcpWindow = i5;
        this.tcpWindowScale = i6;
        this.ipTos = i7;
        this.ipTtl = i8;
    }

    public int getTcpSeq() {
        return this.tcpSeq;
    }

    public int getTcpAck() {
        return this.tcpAck;
    }

    public int getTcpWindow() {
        return this.tcpWindow;
    }

    public int getTcpWindowScale() {
        return this.tcpWindowScale;
    }

    public int getIpTos() {
        return this.ipTos;
    }

    public int getIpTtl() {
        return this.ipTtl;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TcpKeepalivePacketData)) {
            return false;
        }
        TcpKeepalivePacketData tcpKeepalivePacketData = (TcpKeepalivePacketData) obj;
        InetAddress srcAddress = getSrcAddress();
        InetAddress dstAddress = getDstAddress();
        if (srcAddress.equals(tcpKeepalivePacketData.getSrcAddress()) && dstAddress.equals(tcpKeepalivePacketData.getDstAddress()) && getSrcPort() == tcpKeepalivePacketData.getSrcPort() && getDstPort() == tcpKeepalivePacketData.getDstPort() && this.tcpAck == tcpKeepalivePacketData.tcpAck && this.tcpSeq == tcpKeepalivePacketData.tcpSeq && this.tcpWindow == tcpKeepalivePacketData.tcpWindow && this.tcpWindowScale == tcpKeepalivePacketData.tcpWindowScale && this.ipTos == tcpKeepalivePacketData.ipTos && this.ipTtl == tcpKeepalivePacketData.ipTtl) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getSrcAddress(), getDstAddress(), Integer.valueOf(getSrcPort()), Integer.valueOf(getDstPort()), Integer.valueOf(this.tcpAck), Integer.valueOf(this.tcpSeq), Integer.valueOf(this.tcpWindow), Integer.valueOf(this.tcpWindowScale), Integer.valueOf(this.ipTos), Integer.valueOf(this.ipTtl));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getSrcAddress().getHostAddress());
        parcel.writeString(getDstAddress().getHostAddress());
        parcel.writeInt(getSrcPort());
        parcel.writeInt(getDstPort());
        parcel.writeByteArray(getPacket());
        parcel.writeInt(this.tcpSeq);
        parcel.writeInt(this.tcpAck);
        parcel.writeInt(this.tcpWindow);
        parcel.writeInt(this.tcpWindowScale);
        parcel.writeInt(this.ipTos);
        parcel.writeInt(this.ipTtl);
    }

    /* access modifiers changed from: private */
    public static TcpKeepalivePacketData readFromParcel(Parcel parcel) throws InvalidPacketException {
        return new TcpKeepalivePacketData(InetAddresses.parseNumericAddress(parcel.readString()), parcel.readInt(), InetAddresses.parseNumericAddress(parcel.readString()), parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
    }

    public String toString() {
        return "saddr: " + getSrcAddress() + " daddr: " + getDstAddress() + " sport: " + getSrcPort() + " dport: " + getDstPort() + " seq: " + this.tcpSeq + " ack: " + this.tcpAck + " window: " + this.tcpWindow + " windowScale: " + this.tcpWindowScale + " tos: " + this.ipTos + " ttl: " + this.ipTtl;
    }
}
