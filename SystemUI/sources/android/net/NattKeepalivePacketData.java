package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.IpUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.system.OsConstants;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

@SystemApi
public final class NattKeepalivePacketData extends KeepalivePacketData implements Parcelable {
    public static final Parcelable.Creator<NattKeepalivePacketData> CREATOR = new Parcelable.Creator<NattKeepalivePacketData>() {
        public NattKeepalivePacketData createFromParcel(Parcel parcel) {
            try {
                return NattKeepalivePacketData.nattKeepalivePacket(InetAddresses.parseNumericAddress(parcel.readString()), parcel.readInt(), InetAddresses.parseNumericAddress(parcel.readString()), parcel.readInt());
            } catch (InvalidPacketException e) {
                throw new IllegalArgumentException("Invalid NAT-T keepalive data: " + e.getError());
            }
        }

        public NattKeepalivePacketData[] newArray(int i) {
            return new NattKeepalivePacketData[i];
        }
    };
    private static final int IPV4_HEADER_LENGTH = 20;
    private static final int UDP_HEADER_LENGTH = 8;

    public int describeContents() {
        return 0;
    }

    public NattKeepalivePacketData(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2, byte[] bArr) throws InvalidPacketException {
        super(inetAddress, i, inetAddress2, i2, bArr);
    }

    public static NattKeepalivePacketData nattKeepalivePacket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws InvalidPacketException {
        if (!(inetAddress instanceof Inet4Address) || !(inetAddress2 instanceof Inet4Address)) {
            throw new InvalidPacketException(-21);
        } else if (i2 == 4500) {
            ByteBuffer allocate = ByteBuffer.allocate(29);
            allocate.order(ByteOrder.BIG_ENDIAN);
            allocate.putShort(17664);
            allocate.putShort((short) 29);
            allocate.putInt(0);
            allocate.put((byte) 64);
            allocate.put((byte) OsConstants.IPPROTO_UDP);
            int position = allocate.position();
            allocate.putShort(0);
            allocate.put(inetAddress.getAddress());
            allocate.put(inetAddress2.getAddress());
            allocate.putShort((short) i);
            allocate.putShort((short) i2);
            allocate.putShort((short) 9);
            int position2 = allocate.position();
            allocate.putShort(0);
            allocate.put((byte) -1);
            allocate.putShort(position, IpUtils.ipChecksum(allocate, 0));
            allocate.putShort(position2, IpUtils.udpChecksum(allocate, 0, 20));
            return new NattKeepalivePacketData(inetAddress, i, inetAddress2, i2, allocate.array());
        } else {
            throw new InvalidPacketException(-22);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getSrcAddress().getHostAddress());
        parcel.writeString(getDstAddress().getHostAddress());
        parcel.writeInt(getSrcPort());
        parcel.writeInt(getDstPort());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NattKeepalivePacketData)) {
            return false;
        }
        NattKeepalivePacketData nattKeepalivePacketData = (NattKeepalivePacketData) obj;
        InetAddress srcAddress = getSrcAddress();
        InetAddress dstAddress = getDstAddress();
        if (!srcAddress.equals(nattKeepalivePacketData.getSrcAddress()) || !dstAddress.equals(nattKeepalivePacketData.getDstAddress()) || getSrcPort() != nattKeepalivePacketData.getSrcPort() || getDstPort() != nattKeepalivePacketData.getDstPort()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(getSrcAddress(), getDstAddress(), Integer.valueOf(getSrcPort()), Integer.valueOf(getDstPort()));
    }
}
