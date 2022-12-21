package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.IpUtils;
import android.util.Log;
import java.net.InetAddress;

@SystemApi
public class KeepalivePacketData {
    private static final String TAG = "KeepalivePacketData";
    private final InetAddress mDstAddress;
    private final int mDstPort;
    private final byte[] mPacket;
    private final InetAddress mSrcAddress;
    private final int mSrcPort;

    protected KeepalivePacketData(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2, byte[] bArr) throws InvalidPacketException {
        this.mSrcAddress = inetAddress;
        this.mDstAddress = inetAddress2;
        this.mSrcPort = i;
        this.mDstPort = i2;
        this.mPacket = bArr;
        if (inetAddress == null || inetAddress2 == null || !inetAddress.getClass().getName().equals(inetAddress2.getClass().getName())) {
            Log.e(TAG, "Invalid or mismatched InetAddresses in KeepalivePacketData");
            throw new InvalidPacketException(-21);
        } else if (!IpUtils.isValidUdpOrTcpPort(i) || !IpUtils.isValidUdpOrTcpPort(i2)) {
            Log.e(TAG, "Invalid ports in KeepalivePacketData");
            throw new InvalidPacketException(-22);
        }
    }

    public InetAddress getSrcAddress() {
        return this.mSrcAddress;
    }

    public InetAddress getDstAddress() {
        return this.mDstAddress;
    }

    public int getSrcPort() {
        return this.mSrcPort;
    }

    public int getDstPort() {
        return this.mDstPort;
    }

    public byte[] getPacket() {
        return (byte[]) this.mPacket.clone();
    }

    public String toString() {
        return "KeepalivePacketData[srcAddress=" + this.mSrcAddress + ", dstAddress=" + this.mDstAddress + ", srcPort=" + this.mSrcPort + ", dstPort=" + this.mDstPort + ", packet.length=" + this.mPacket.length + ']';
    }
}
