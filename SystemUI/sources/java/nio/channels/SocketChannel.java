package java.nio.channels;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

public abstract class SocketChannel extends AbstractSelectableChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public abstract SocketChannel bind(SocketAddress socketAddress) throws IOException;

    public abstract boolean connect(SocketAddress socketAddress) throws IOException;

    public abstract boolean finishConnect() throws IOException;

    public abstract SocketAddress getLocalAddress() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    public abstract boolean isConnected();

    public abstract boolean isConnectionPending();

    public abstract int read(ByteBuffer byteBuffer) throws IOException;

    public abstract long read(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;

    public abstract <T> SocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException;

    public abstract SocketChannel shutdownInput() throws IOException;

    public abstract SocketChannel shutdownOutput() throws IOException;

    public abstract Socket socket();

    public final int validOps() {
        return 13;
    }

    public abstract int write(ByteBuffer byteBuffer) throws IOException;

    public abstract long write(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;

    protected SocketChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static SocketChannel open() throws IOException {
        return SelectorProvider.provider().openSocketChannel();
    }

    public static SocketChannel open(SocketAddress socketAddress) throws IOException {
        SocketChannel open = open();
        try {
            open.connect(socketAddress);
            return open;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public final long read(ByteBuffer[] byteBufferArr) throws IOException {
        return read(byteBufferArr, 0, byteBufferArr.length);
    }

    public final long write(ByteBuffer[] byteBufferArr) throws IOException {
        return write(byteBufferArr, 0, byteBufferArr.length);
    }
}
