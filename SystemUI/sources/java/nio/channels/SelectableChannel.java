package java.nio.channels;

import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

public abstract class SelectableChannel extends AbstractInterruptibleChannel implements Channel {
    public abstract Object blockingLock();

    public abstract SelectableChannel configureBlocking(boolean z) throws IOException;

    public abstract boolean isBlocking();

    public abstract boolean isRegistered();

    public abstract SelectionKey keyFor(Selector selector);

    public abstract SelectorProvider provider();

    public abstract SelectionKey register(Selector selector, int i, Object obj) throws ClosedChannelException;

    public abstract int validOps();

    protected SelectableChannel() {
    }

    public final SelectionKey register(Selector selector, int i) throws ClosedChannelException {
        return register(selector, i, (Object) null);
    }
}