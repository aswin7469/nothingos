package java.nio.channels;

import java.net.DatagramSocket;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

public abstract class DatagramChannel extends AbstractSelectableChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, MulticastChannel {
    public abstract DatagramChannel bind(SocketAddress socketAddress) throws IOException;

    public abstract DatagramChannel connect(SocketAddress socketAddress) throws IOException;

    public abstract DatagramChannel disconnect() throws IOException;

    public abstract SocketAddress getLocalAddress() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    public abstract boolean isConnected();

    public abstract int read(ByteBuffer byteBuffer) throws IOException;

    public abstract long read(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;

    public abstract SocketAddress receive(ByteBuffer byteBuffer) throws IOException;

    public abstract int send(ByteBuffer byteBuffer, SocketAddress socketAddress) throws IOException;

    public abstract <T> DatagramChannel setOption(SocketOption<T> socketOption, T t) throws IOException;

    public abstract DatagramSocket socket();

    public final int validOps() {
        return 5;
    }

    public abstract int write(ByteBuffer byteBuffer) throws IOException;

    public abstract long write(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;

    protected DatagramChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static DatagramChannel open() throws IOException {
        return SelectorProvider.provider().openDatagramChannel();
    }

    public static DatagramChannel open(ProtocolFamily protocolFamily) throws IOException {
        return SelectorProvider.provider().openDatagramChannel(protocolFamily);
    }

    public final long read(ByteBuffer[] byteBufferArr) throws IOException {
        return read(byteBufferArr, 0, byteBufferArr.length);
    }

    public final long write(ByteBuffer[] byteBufferArr) throws IOException {
        return write(byteBufferArr, 0, byteBufferArr.length);
    }
}
