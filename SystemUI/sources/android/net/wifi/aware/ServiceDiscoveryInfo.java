package android.net.wifi.aware;

import java.util.List;

public final class ServiceDiscoveryInfo {
    private final List<byte[]> mMatchFilters;
    private final int mPeerCipherSuite;
    private final PeerHandle mPeerHandle;
    private final byte[] mScid;
    private final byte[] mServiceSpecificInfo;

    public ServiceDiscoveryInfo(PeerHandle peerHandle, int i, byte[] bArr, List<byte[]> list, byte[] bArr2) {
        this.mServiceSpecificInfo = bArr;
        this.mMatchFilters = list;
        this.mPeerCipherSuite = i;
        this.mScid = bArr2;
        this.mPeerHandle = peerHandle;
    }

    public PeerHandle getPeerHandle() {
        return this.mPeerHandle;
    }

    public List<byte[]> getMatchFilters() {
        return this.mMatchFilters;
    }

    public byte[] getServiceSpecificInfo() {
        return this.mServiceSpecificInfo;
    }

    public byte[] getScid() {
        return this.mScid;
    }

    public int getPeerCipherSuite() {
        return this.mPeerCipherSuite;
    }
}
