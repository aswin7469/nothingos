package android.system;

import java.net.SocketAddress;

public final class VmSocketAddress extends SocketAddress {
    private int svmCid;
    private int svmPort;

    public VmSocketAddress(int i, int i2) {
        this.svmPort = i;
        this.svmCid = i2;
    }

    public int getSvmPort() {
        return this.svmPort;
    }

    public int getSvmCid() {
        return this.svmCid;
    }
}
