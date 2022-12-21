package sun.nio.p033ch;

import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectionKey;

/* renamed from: sun.nio.ch.SelectionKeyImpl */
public class SelectionKeyImpl extends AbstractSelectionKey {
    final SelChImpl channel;
    private int index;
    private volatile int interestOps;
    private int readyOps;
    public final SelectorImpl selector;

    SelectionKeyImpl(SelChImpl selChImpl, SelectorImpl selectorImpl) {
        this.channel = selChImpl;
        this.selector = selectorImpl;
    }

    public SelectableChannel channel() {
        return (SelectableChannel) this.channel;
    }

    public Selector selector() {
        return this.selector;
    }

    /* access modifiers changed from: package-private */
    public int getIndex() {
        return this.index;
    }

    /* access modifiers changed from: package-private */
    public void setIndex(int i) {
        this.index = i;
    }

    private void ensureValid() {
        if (!isValid()) {
            throw new CancelledKeyException();
        }
    }

    public int interestOps() {
        ensureValid();
        return this.interestOps;
    }

    public SelectionKey interestOps(int i) {
        ensureValid();
        return nioInterestOps(i);
    }

    public int readyOps() {
        ensureValid();
        return this.readyOps;
    }

    public void nioReadyOps(int i) {
        this.readyOps = i;
    }

    public int nioReadyOps() {
        return this.readyOps;
    }

    public SelectionKey nioInterestOps(int i) {
        if (((~channel().validOps()) & i) == 0) {
            this.channel.translateAndSetInterestOps(i, this);
            this.interestOps = i;
            return this;
        }
        throw new IllegalArgumentException();
    }

    public int nioInterestOps() {
        return this.interestOps;
    }
}
