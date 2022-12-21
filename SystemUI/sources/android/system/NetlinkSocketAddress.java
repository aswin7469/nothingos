package android.system;

import android.annotation.SystemApi;
import java.net.SocketAddress;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NetlinkSocketAddress extends SocketAddress {
    private final int nlGroupsMask;
    private final int nlPortId;

    public NetlinkSocketAddress() {
        this(0, 0);
    }

    public NetlinkSocketAddress(int i) {
        this(i, 0);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetlinkSocketAddress(int i, int i2) {
        this.nlPortId = i;
        this.nlGroupsMask = i2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getPortId() {
        return this.nlPortId;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getGroupsMask() {
        return this.nlGroupsMask;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
