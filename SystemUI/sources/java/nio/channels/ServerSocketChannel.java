package java.nio.channels;

import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

public abstract class ServerSocketChannel extends AbstractSelectableChannel implements NetworkChannel {
    public abstract SocketChannel accept() throws IOException;

    public abstract ServerSocketChannel bind(SocketAddress socketAddress, int i) throws IOException;

    public abstract SocketAddress getLocalAddress() throws IOException;

    public abstract <T> ServerSocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException;

    public abstract ServerSocket socket();

    public final int validOps() {
        return 16;
    }

    protected ServerSocketChannel(SelectorProvider selectorProvider) {
        super(selectorProvider);
    }

    public static ServerSocketChannel open() throws IOException {
        return SelectorProvider.provider().openServerSocketChannel();
    }

    public final ServerSocketChannel bind(SocketAddress socketAddress) throws IOException {
        return bind(socketAddress, 0);
    }
}
