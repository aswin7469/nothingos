package sun.nio.p033ch;

import java.net.SocketException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* renamed from: sun.nio.ch.SelectorImpl */
public abstract class SelectorImpl extends AbstractSelector {
    protected HashSet<SelectionKey> keys = new HashSet<>();
    private Set<SelectionKey> publicKeys;
    private Set<SelectionKey> publicSelectedKeys;
    protected Set<SelectionKey> selectedKeys = new HashSet();

    /* access modifiers changed from: protected */
    public abstract int doSelect(long j) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void implClose() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void implDereg(SelectionKeyImpl selectionKeyImpl) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void implRegister(SelectionKeyImpl selectionKeyImpl);

    public void putEventOps(SelectionKeyImpl selectionKeyImpl, int i) {
    }

    public abstract Selector wakeup();

    protected SelectorImpl(SelectorProvider selectorProvider) {
        super(selectorProvider);
        if (Util.atBugLevel("1.4")) {
            this.publicKeys = this.keys;
            this.publicSelectedKeys = this.selectedKeys;
            return;
        }
        this.publicKeys = Collections.unmodifiableSet(this.keys);
        this.publicSelectedKeys = Util.ungrowableSet(this.selectedKeys);
    }

    public Set<SelectionKey> keys() {
        if (isOpen() || Util.atBugLevel("1.4")) {
            return this.publicKeys;
        }
        throw new ClosedSelectorException();
    }

    public Set<SelectionKey> selectedKeys() {
        if (isOpen() || Util.atBugLevel("1.4")) {
            return this.publicSelectedKeys;
        }
        throw new ClosedSelectorException();
    }

    private int lockAndDoSelect(long j) throws IOException {
        int doSelect;
        synchronized (this) {
            if (isOpen()) {
                synchronized (this.publicKeys) {
                    synchronized (this.publicSelectedKeys) {
                        doSelect = doSelect(j);
                    }
                }
            } else {
                throw new ClosedSelectorException();
            }
        }
        return doSelect;
    }

    public int select(long j) throws IOException {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            if (i == 0) {
                j = -1;
            }
            return lockAndDoSelect(j);
        }
        throw new IllegalArgumentException("Negative timeout");
    }

    public int select() throws IOException {
        return select(0);
    }

    public int selectNow() throws IOException {
        return lockAndDoSelect(0);
    }

    public void implCloseSelector() throws IOException {
        wakeup();
        synchronized (this) {
            synchronized (this.publicKeys) {
                synchronized (this.publicSelectedKeys) {
                    implClose();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final SelectionKey register(AbstractSelectableChannel abstractSelectableChannel, int i, Object obj) {
        if (abstractSelectableChannel instanceof SelChImpl) {
            SelectionKeyImpl selectionKeyImpl = new SelectionKeyImpl((SelChImpl) abstractSelectableChannel, this);
            selectionKeyImpl.attach(obj);
            synchronized (this.publicKeys) {
                implRegister(selectionKeyImpl);
            }
            selectionKeyImpl.interestOps(i);
            return selectionKeyImpl;
        }
        throw new IllegalSelectorException();
    }

    /* access modifiers changed from: package-private */
    public void processDeregisterQueue() throws IOException {
        Set<SelectionKey> cancelledKeys = cancelledKeys();
        synchronized (cancelledKeys) {
            if (!cancelledKeys.isEmpty()) {
                Iterator<SelectionKey> it = cancelledKeys.iterator();
                while (it.hasNext()) {
                    try {
                        implDereg((SelectionKeyImpl) it.next());
                        it.remove();
                    } catch (SocketException e) {
                        throw new IOException("Error deregistering key", e);
                    } catch (Throwable th) {
                        it.remove();
                        throw th;
                    }
                }
            }
        }
    }
}
