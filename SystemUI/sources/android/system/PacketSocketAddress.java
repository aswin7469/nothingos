package android.system;

import android.annotation.SystemApi;
import java.net.SocketAddress;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class PacketSocketAddress extends SocketAddress {
    public final byte[] sll_addr;
    public final int sll_hatype;
    public final int sll_ifindex;
    public final int sll_pkttype;
    public final int sll_protocol;

    public PacketSocketAddress(int i, int i2, int i3, int i4, byte[] bArr) {
        this.sll_protocol = i;
        this.sll_ifindex = i2;
        this.sll_hatype = i3;
        this.sll_pkttype = i4;
        this.sll_addr = bArr;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public PacketSocketAddress(int i, int i2, byte[] bArr) {
        this.sll_protocol = i;
        this.sll_ifindex = i2;
        this.sll_hatype = 0;
        this.sll_pkttype = 0;
        this.sll_addr = bArr;
    }

    public PacketSocketAddress(short s, int i) {
        this.sll_protocol = s;
        this.sll_ifindex = i;
        this.sll_hatype = 0;
        this.sll_pkttype = 0;
        this.sll_addr = null;
    }

    public PacketSocketAddress(int i, byte[] bArr) {
        this.sll_protocol = 0;
        this.sll_ifindex = i;
        this.sll_hatype = 0;
        this.sll_pkttype = 0;
        this.sll_addr = bArr;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
