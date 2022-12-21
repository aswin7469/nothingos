package sun.nio.p033ch;

import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.AbstractPollSelectorImpl */
abstract class AbstractPollSelectorImpl extends SelectorImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    protected final int INIT_CAP = 10;
    protected SelectionKeyImpl[] channelArray;
    protected int channelOffset = 0;
    private Object closeLock = new Object();
    private boolean closed = false;
    PollArrayWrapper pollWrapper;
    protected int totalChannels;

    /* access modifiers changed from: protected */
    public abstract int doSelect(long j) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void implCloseInterrupt() throws IOException;

    AbstractPollSelectorImpl(SelectorProvider selectorProvider, int i, int i2) {
        super(selectorProvider);
        this.totalChannels = i;
        this.channelOffset = i2;
    }

    public void putEventOps(SelectionKeyImpl selectionKeyImpl, int i) {
        synchronized (this.closeLock) {
            if (!this.closed) {
                this.pollWrapper.putEventOps(selectionKeyImpl.getIndex(), i);
            } else {
                throw new ClosedSelectorException();
            }
        }
    }

    public Selector wakeup() {
        this.pollWrapper.interrupt();
        return this;
    }

    /* access modifiers changed from: protected */
    public void implClose() throws IOException {
        synchronized (this.closeLock) {
            if (!this.closed) {
                this.closed = true;
                for (int i = this.channelOffset; i < this.totalChannels; i++) {
                    SelectionKeyImpl selectionKeyImpl = this.channelArray[i];
                    selectionKeyImpl.setIndex(-1);
                    deregister(selectionKeyImpl);
                    SelectableChannel channel = this.channelArray[i].channel();
                    if (!channel.isOpen() && !channel.isRegistered()) {
                        ((SelChImpl) channel).kill();
                    }
                }
                implCloseInterrupt();
                this.pollWrapper.free();
                this.pollWrapper = null;
                this.selectedKeys = null;
                this.channelArray = null;
                this.totalChannels = 0;
            }
        }
    }

    /* access modifiers changed from: protected */
    public int updateSelectedKeys() {
        int i = 0;
        for (int i2 = this.channelOffset; i2 < this.totalChannels; i2++) {
            int reventOps = this.pollWrapper.getReventOps(i2);
            if (reventOps != 0) {
                SelectionKeyImpl selectionKeyImpl = this.channelArray[i2];
                this.pollWrapper.putReventOps(i2, 0);
                if (!this.selectedKeys.contains(selectionKeyImpl)) {
                    selectionKeyImpl.channel.translateAndSetReadyOps(reventOps, selectionKeyImpl);
                    if ((selectionKeyImpl.nioReadyOps() & selectionKeyImpl.nioInterestOps()) != 0) {
                        this.selectedKeys.add(selectionKeyImpl);
                    }
                } else if (!selectionKeyImpl.channel.translateAndSetReadyOps(reventOps, selectionKeyImpl)) {
                }
                i++;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void implRegister(SelectionKeyImpl selectionKeyImpl) {
        synchronized (this.closeLock) {
            if (!this.closed) {
                if (this.channelArray.length == this.totalChannels) {
                    int i = this.pollWrapper.totalChannels * 2;
                    SelectionKeyImpl[] selectionKeyImplArr = new SelectionKeyImpl[i];
                    for (int i2 = this.channelOffset; i2 < this.totalChannels; i2++) {
                        selectionKeyImplArr[i2] = this.channelArray[i2];
                    }
                    this.channelArray = selectionKeyImplArr;
                    this.pollWrapper.grow(i);
                }
                SelectionKeyImpl[] selectionKeyImplArr2 = this.channelArray;
                int i3 = this.totalChannels;
                selectionKeyImplArr2[i3] = selectionKeyImpl;
                selectionKeyImpl.setIndex(i3);
                this.pollWrapper.addEntry(selectionKeyImpl.channel);
                this.totalChannels++;
                this.keys.add(selectionKeyImpl);
            } else {
                throw new ClosedSelectorException();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void implDereg(SelectionKeyImpl selectionKeyImpl) throws IOException {
        int index = selectionKeyImpl.getIndex();
        int i = this.totalChannels;
        if (index != i - 1) {
            SelectionKeyImpl[] selectionKeyImplArr = this.channelArray;
            SelectionKeyImpl selectionKeyImpl2 = selectionKeyImplArr[i - 1];
            selectionKeyImplArr[index] = selectionKeyImpl2;
            selectionKeyImpl2.setIndex(index);
            this.pollWrapper.release(index);
            PollArrayWrapper pollArrayWrapper = this.pollWrapper;
            PollArrayWrapper.replaceEntry(pollArrayWrapper, this.totalChannels - 1, pollArrayWrapper, index);
        } else {
            this.pollWrapper.release(index);
        }
        SelectionKeyImpl[] selectionKeyImplArr2 = this.channelArray;
        int i2 = this.totalChannels;
        selectionKeyImplArr2[i2 - 1] = null;
        this.totalChannels = i2 - 1;
        PollArrayWrapper pollArrayWrapper2 = this.pollWrapper;
        pollArrayWrapper2.totalChannels--;
        selectionKeyImpl.setIndex(-1);
        this.keys.remove(selectionKeyImpl);
        this.selectedKeys.remove(selectionKeyImpl);
        deregister(selectionKeyImpl);
        SelectableChannel channel = selectionKeyImpl.channel();
        if (!channel.isOpen() && !channel.isRegistered()) {
            ((SelChImpl) channel).kill();
        }
    }
}
