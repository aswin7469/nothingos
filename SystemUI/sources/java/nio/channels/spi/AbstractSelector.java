package java.nio.channels.spi;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.p026io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import sun.nio.p033ch.Interruptible;

public abstract class AbstractSelector extends Selector {
    private final Set<SelectionKey> cancelledKeys = new HashSet();
    private Interruptible interruptor = null;
    private final SelectorProvider provider;
    private final AtomicBoolean selectorOpen = new AtomicBoolean(true);

    /* access modifiers changed from: protected */
    public abstract void implCloseSelector() throws IOException;

    /* access modifiers changed from: protected */
    public abstract SelectionKey register(AbstractSelectableChannel abstractSelectableChannel, int i, Object obj);

    protected AbstractSelector(SelectorProvider selectorProvider) {
        this.provider = selectorProvider;
    }

    /* access modifiers changed from: package-private */
    public void cancel(SelectionKey selectionKey) {
        synchronized (this.cancelledKeys) {
            this.cancelledKeys.add(selectionKey);
        }
    }

    public final void close() throws IOException {
        if (this.selectorOpen.getAndSet(false)) {
            implCloseSelector();
        }
    }

    public final boolean isOpen() {
        return this.selectorOpen.get();
    }

    public final SelectorProvider provider() {
        return this.provider;
    }

    /* access modifiers changed from: protected */
    public final Set<SelectionKey> cancelledKeys() {
        return this.cancelledKeys;
    }

    /* access modifiers changed from: protected */
    public final void deregister(AbstractSelectionKey abstractSelectionKey) {
        ((AbstractSelectableChannel) abstractSelectionKey.channel()).removeKey(abstractSelectionKey);
    }

    /* access modifiers changed from: protected */
    public final void begin() {
        if (this.interruptor == null) {
            this.interruptor = new Interruptible() {
                public void interrupt(Thread thread) {
                    AbstractSelector.this.wakeup();
                }
            };
        }
        AbstractInterruptibleChannel.blockedOn(this.interruptor);
        Thread currentThread = Thread.currentThread();
        if (currentThread.isInterrupted()) {
            this.interruptor.interrupt(currentThread);
        }
    }

    /* access modifiers changed from: protected */
    public final void end() {
        AbstractInterruptibleChannel.blockedOn((Interruptible) null);
    }
}
