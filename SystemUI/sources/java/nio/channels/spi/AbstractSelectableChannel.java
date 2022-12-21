package java.nio.channels.spi;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.p026io.IOException;

public abstract class AbstractSelectableChannel extends SelectableChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private int keyCount = 0;
    private final Object keyLock = new Object();
    private SelectionKey[] keys = null;
    private volatile boolean nonBlocking;
    private final SelectorProvider provider;
    private final Object regLock = new Object();

    /* access modifiers changed from: protected */
    public abstract void implCloseSelectableChannel() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void implConfigureBlocking(boolean z) throws IOException;

    protected AbstractSelectableChannel(SelectorProvider selectorProvider) {
        this.provider = selectorProvider;
    }

    public final SelectorProvider provider() {
        return this.provider;
    }

    private void addKey(SelectionKey selectionKey) {
        SelectionKey[] selectionKeyArr = this.keys;
        int i = 0;
        if (selectionKeyArr != null && this.keyCount < selectionKeyArr.length) {
            while (true) {
                SelectionKey[] selectionKeyArr2 = this.keys;
                if (i >= selectionKeyArr2.length || selectionKeyArr2[i] == null) {
                    break;
                }
                i++;
            }
        } else if (selectionKeyArr == null) {
            this.keys = new SelectionKey[2];
        } else {
            SelectionKey[] selectionKeyArr3 = new SelectionKey[(selectionKeyArr.length * 2)];
            while (true) {
                SelectionKey[] selectionKeyArr4 = this.keys;
                if (i >= selectionKeyArr4.length) {
                    break;
                }
                selectionKeyArr3[i] = selectionKeyArr4[i];
                i++;
            }
            this.keys = selectionKeyArr3;
            i = this.keyCount;
        }
        this.keys[i] = selectionKey;
        this.keyCount++;
    }

    private SelectionKey findKey(Selector selector) {
        if (this.keys == null) {
            return null;
        }
        int i = 0;
        while (true) {
            SelectionKey[] selectionKeyArr = this.keys;
            if (i >= selectionKeyArr.length) {
                return null;
            }
            SelectionKey selectionKey = selectionKeyArr[i];
            if (selectionKey != null && selectionKey.selector() == selector) {
                return this.keys[i];
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public void removeKey(SelectionKey selectionKey) {
        synchronized (this.keyLock) {
            int i = 0;
            while (true) {
                SelectionKey[] selectionKeyArr = this.keys;
                if (i < selectionKeyArr.length) {
                    if (selectionKeyArr[i] == selectionKey) {
                        selectionKeyArr[i] = null;
                        this.keyCount--;
                    }
                    i++;
                } else {
                    ((AbstractSelectionKey) selectionKey).invalidate();
                }
            }
        }
    }

    private boolean haveValidKeys() {
        synchronized (this.keyLock) {
            if (this.keyCount == 0) {
                return false;
            }
            int i = 0;
            while (true) {
                SelectionKey[] selectionKeyArr = this.keys;
                if (i >= selectionKeyArr.length) {
                    return false;
                }
                SelectionKey selectionKey = selectionKeyArr[i];
                if (selectionKey != null && selectionKey.isValid()) {
                    return true;
                }
                i++;
            }
        }
    }

    public final boolean isRegistered() {
        boolean z;
        synchronized (this.keyLock) {
            z = this.keyCount != 0;
        }
        return z;
    }

    public final SelectionKey keyFor(Selector selector) {
        SelectionKey findKey;
        synchronized (this.keyLock) {
            findKey = findKey(selector);
        }
        return findKey;
    }

    public final SelectionKey register(Selector selector, int i, Object obj) throws ClosedChannelException {
        SelectionKey findKey;
        if (((~validOps()) & i) != 0) {
            throw new IllegalArgumentException();
        } else if (isOpen()) {
            synchronized (this.regLock) {
                if (!isBlocking()) {
                    synchronized (this.keyLock) {
                        if (isOpen()) {
                            findKey = findKey(selector);
                            if (findKey != null) {
                                findKey.attach(obj);
                                findKey.interestOps(i);
                            } else {
                                findKey = ((AbstractSelector) selector).register(this, i, obj);
                                addKey(findKey);
                            }
                        } else {
                            throw new ClosedChannelException();
                        }
                    }
                } else {
                    throw new IllegalBlockingModeException();
                }
            }
            return findKey;
        } else {
            throw new ClosedChannelException();
        }
    }

    /* access modifiers changed from: protected */
    public final void implCloseChannel() throws IOException {
        SelectionKey[] selectionKeyArr;
        implCloseSelectableChannel();
        synchronized (this.keyLock) {
            SelectionKey[] selectionKeyArr2 = this.keys;
            selectionKeyArr = selectionKeyArr2 != null ? (SelectionKey[]) selectionKeyArr2.clone() : null;
        }
        if (selectionKeyArr != null) {
            for (SelectionKey selectionKey : selectionKeyArr) {
                if (selectionKey != null) {
                    selectionKey.cancel();
                }
            }
        }
    }

    public final boolean isBlocking() {
        return !this.nonBlocking;
    }

    public final Object blockingLock() {
        return this.regLock;
    }

    public final SelectableChannel configureBlocking(boolean z) throws IOException {
        synchronized (this.regLock) {
            if (isOpen()) {
                boolean z2 = true;
                if (z != (!this.nonBlocking)) {
                    if (z) {
                        if (haveValidKeys()) {
                            throw new IllegalBlockingModeException();
                        }
                    }
                    implConfigureBlocking(z);
                    if (z) {
                        z2 = false;
                    }
                    this.nonBlocking = z2;
                }
            } else {
                throw new ClosedChannelException();
            }
        }
        return this;
    }
}
