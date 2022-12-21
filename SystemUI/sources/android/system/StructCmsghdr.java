package android.system;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class StructCmsghdr {
    public final byte[] cmsg_data;
    public final int cmsg_level;
    public final int cmsg_type;

    public StructCmsghdr(int i, int i2, short s) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.order(ByteOrder.nativeOrder());
        allocate.putShort(s);
        this.cmsg_level = i;
        this.cmsg_type = i2;
        this.cmsg_data = allocate.array();
    }

    public StructCmsghdr(int i, int i2, byte[] bArr) {
        this.cmsg_level = i;
        this.cmsg_type = i2;
        this.cmsg_data = bArr;
    }
}
