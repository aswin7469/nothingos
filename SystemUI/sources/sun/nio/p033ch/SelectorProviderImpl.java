package sun.nio.p033ch;

import java.net.ProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.SelectorProviderImpl */
public abstract class SelectorProviderImpl extends SelectorProvider {
    public abstract AbstractSelector openSelector() throws IOException;

    public DatagramChannel openDatagramChannel() throws IOException {
        return new DatagramChannelImpl(this);
    }

    public DatagramChannel openDatagramChannel(ProtocolFamily protocolFamily) throws IOException {
        return new DatagramChannelImpl((SelectorProvider) this, protocolFamily);
    }

    public Pipe openPipe() throws IOException {
        return new PipeImpl(this);
    }

    public ServerSocketChannel openServerSocketChannel() throws IOException {
        return new ServerSocketChannelImpl(this);
    }

    public SocketChannel openSocketChannel() throws IOException {
        return new SocketChannelImpl(this);
    }
}
