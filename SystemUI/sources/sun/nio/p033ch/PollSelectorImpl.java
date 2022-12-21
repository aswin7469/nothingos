package sun.nio.p033ch;

import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.PollSelectorImpl */
class PollSelectorImpl extends AbstractPollSelectorImpl {
    private int fd0;
    private int fd1;
    private Object interruptLock = new Object();
    private boolean interruptTriggered = false;

    PollSelectorImpl(SelectorProvider selectorProvider) {
        super(selectorProvider, 1, 1);
        long makePipe = IOUtil.makePipe(false);
        this.fd0 = (int) (makePipe >>> 32);
        this.fd1 = (int) makePipe;
        try {
            this.pollWrapper = new PollArrayWrapper(10);
            this.pollWrapper.initInterrupt(this.fd0, this.fd1);
            this.channelArray = new SelectionKeyImpl[10];
        } catch (Throwable th) {
            try {
                FileDispatcherImpl.closeIntFD(this.fd0);
            } catch (IOException e) {
                th.addSuppressed(e);
            }
            try {
                FileDispatcherImpl.closeIntFD(this.fd1);
            } catch (IOException e2) {
                th.addSuppressed(e2);
            }
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public int doSelect(long j) throws IOException {
        if (this.channelArray != null) {
            processDeregisterQueue();
            try {
                begin();
                this.pollWrapper.poll(this.totalChannels, 0, j);
                end();
                processDeregisterQueue();
                int updateSelectedKeys = updateSelectedKeys();
                if (this.pollWrapper.getReventOps(0) != 0) {
                    this.pollWrapper.putReventOps(0, 0);
                    synchronized (this.interruptLock) {
                        IOUtil.drain(this.fd0);
                        this.interruptTriggered = false;
                    }
                }
                return updateSelectedKeys;
            } catch (Throwable th) {
                end();
                throw th;
            }
        } else {
            throw new ClosedSelectorException();
        }
    }

    /* access modifiers changed from: protected */
    public void implCloseInterrupt() throws IOException {
        synchronized (this.interruptLock) {
            this.interruptTriggered = true;
        }
        FileDispatcherImpl.closeIntFD(this.fd0);
        FileDispatcherImpl.closeIntFD(this.fd1);
        this.fd0 = -1;
        this.fd1 = -1;
        this.pollWrapper.release(0);
    }

    public Selector wakeup() {
        synchronized (this.interruptLock) {
            if (!this.interruptTriggered) {
                this.pollWrapper.interrupt();
                this.interruptTriggered = true;
            }
        }
        return this;
    }
}
