package android.system;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public final class StructMsghdr {
    public StructCmsghdr[] msg_control;
    public int msg_flags;
    public final ByteBuffer[] msg_iov;
    public SocketAddress msg_name;

    public StructMsghdr(SocketAddress socketAddress, ByteBuffer[] byteBufferArr, StructCmsghdr[] structCmsghdrArr, int i) {
        this.msg_name = socketAddress;
        this.msg_iov = byteBufferArr;
        this.msg_control = structCmsghdrArr;
        this.msg_flags = i;
    }
}
