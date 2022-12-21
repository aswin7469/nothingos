package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.SourceChannelImpl */
class SourceChannelImpl extends Pipe.SourceChannel implements SelChImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_INUSE = 0;
    private static final int ST_KILLED = 1;
    private static final int ST_UNINITIALIZED = -1;

    /* renamed from: nd */
    private static final NativeDispatcher f896nd = new FileDispatcherImpl();

    /* renamed from: fd */
    FileDescriptor f897fd;
    int fdVal;
    private final Object lock = new Object();
    private volatile int state = -1;
    private final Object stateLock = new Object();
    private volatile long thread = 0;

    public FileDescriptor getFD() {
        return this.f897fd;
    }

    public int getFDVal() {
        return this.fdVal;
    }

    SourceChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor) {
        super(selectorProvider);
        this.f897fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 0;
    }

    /* access modifiers changed from: protected */
    public void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            if (this.state != 1) {
                f896nd.preClose(this.f897fd);
            }
            long j = this.thread;
            if (j != 0) {
                NativeThread.signal(j);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    public void kill() throws IOException {
        synchronized (this.stateLock) {
            if (this.state != 1) {
                if (this.state == -1) {
                    this.state = 1;
                    return;
                }
                f896nd.close(this.f897fd);
                this.state = 1;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void implConfigureBlocking(boolean z) throws IOException {
        IOUtil.configureBlocking(this.f897fd, z);
    }

    public boolean translateReadyOps(int i, int i2, SelectionKeyImpl selectionKeyImpl) {
        int nioInterestOps = selectionKeyImpl.nioInterestOps();
        int nioReadyOps = selectionKeyImpl.nioReadyOps();
        if ((Net.POLLNVAL & i) != 0) {
            throw new Error("POLLNVAL detected");
        } else if (((Net.POLLERR | Net.POLLHUP) & i) != 0) {
            selectionKeyImpl.nioReadyOps(nioInterestOps);
            if ((nioInterestOps & (~nioReadyOps)) != 0) {
                return true;
            }
            return false;
        } else {
            if (!((i & Net.POLLIN) == 0 || (nioInterestOps & 1) == 0)) {
                i2 |= 1;
            }
            selectionKeyImpl.nioReadyOps(i2);
            if (((~nioReadyOps) & i2) != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean translateAndUpdateReadyOps(int i, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i, selectionKeyImpl.nioReadyOps(), selectionKeyImpl);
    }

    public boolean translateAndSetReadyOps(int i, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i, 0, selectionKeyImpl);
    }

    public void translateAndSetInterestOps(int i, SelectionKeyImpl selectionKeyImpl) {
        if (i == 1) {
            i = Net.POLLIN;
        }
        selectionKeyImpl.selector.putEventOps(selectionKeyImpl, i);
    }

    private void ensureOpen() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0051 A[Catch:{ all -> 0x0049 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(java.nio.ByteBuffer r12) throws java.p026io.IOException {
        /*
            r11 = this;
            r12.getClass()
            r11.ensureOpen()
            java.lang.Object r0 = r11.lock
            monitor-enter(r0)
            r1 = -2
            r2 = 1
            r3 = 0
            r5 = 0
            r11.begin()     // Catch:{ all -> 0x004b }
            boolean r6 = r11.isOpen()     // Catch:{ all -> 0x004b }
            if (r6 != 0) goto L_0x001e
            r11.thread = r3     // Catch:{ all -> 0x0059 }
            r11.end(r5)     // Catch:{ all -> 0x0059 }
            monitor-exit(r0)     // Catch:{ all -> 0x0059 }
            return r5
        L_0x001e:
            long r6 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x004b }
            r11.thread = r6     // Catch:{ all -> 0x004b }
            r6 = r5
        L_0x0025:
            java.io.FileDescriptor r7 = r11.f897fd     // Catch:{ all -> 0x0049 }
            sun.nio.ch.NativeDispatcher r8 = f896nd     // Catch:{ all -> 0x0049 }
            r9 = -1
            int r6 = sun.nio.p033ch.IOUtil.read(r7, r12, r9, r8)     // Catch:{ all -> 0x0049 }
            r7 = -3
            if (r6 != r7) goto L_0x0038
            boolean r7 = r11.isOpen()     // Catch:{ all -> 0x0049 }
            if (r7 != 0) goto L_0x0025
        L_0x0038:
            int r12 = sun.nio.p033ch.IOStatus.normalize((int) r6)     // Catch:{ all -> 0x0049 }
            r11.thread = r3     // Catch:{ all -> 0x0059 }
            if (r6 > 0) goto L_0x0044
            if (r6 != r1) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r2 = r5
        L_0x0044:
            r11.end(r2)     // Catch:{ all -> 0x0059 }
            monitor-exit(r0)     // Catch:{ all -> 0x0059 }
            return r12
        L_0x0049:
            r12 = move-exception
            goto L_0x004d
        L_0x004b:
            r12 = move-exception
            r6 = r5
        L_0x004d:
            r11.thread = r3     // Catch:{ all -> 0x0059 }
            if (r6 > 0) goto L_0x0055
            if (r6 != r1) goto L_0x0054
            goto L_0x0055
        L_0x0054:
            r2 = r5
        L_0x0055:
            r11.end(r2)     // Catch:{ all -> 0x0059 }
            throw r12     // Catch:{ all -> 0x0059 }
        L_0x0059:
            r11 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0059 }
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SourceChannelImpl.read(java.nio.ByteBuffer):int");
    }

    public long read(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException {
        if (i >= 0 && i2 >= 0 && i <= byteBufferArr.length - i2) {
            return read(Util.subsequence(byteBufferArr, i, i2));
        }
        throw new IndexOutOfBoundsException();
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x0059 A[Catch:{ all -> 0x004f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long read(java.nio.ByteBuffer[] r12) throws java.p026io.IOException {
        /*
            r11 = this;
            r12.getClass()
            r11.ensureOpen()
            java.lang.Object r0 = r11.lock
            monitor-enter(r0)
            r1 = -2
            r3 = 1
            r4 = 0
            r5 = 0
            r11.begin()     // Catch:{ all -> 0x0051 }
            boolean r7 = r11.isOpen()     // Catch:{ all -> 0x0051 }
            if (r7 != 0) goto L_0x001f
            r11.thread = r5     // Catch:{ all -> 0x0063 }
            r11.end(r4)     // Catch:{ all -> 0x0063 }
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            return r5
        L_0x001f:
            long r7 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x0051 }
            r11.thread = r7     // Catch:{ all -> 0x0051 }
            r7 = r5
        L_0x0026:
            java.io.FileDescriptor r9 = r11.f897fd     // Catch:{ all -> 0x004f }
            sun.nio.ch.NativeDispatcher r10 = f896nd     // Catch:{ all -> 0x004f }
            long r7 = sun.nio.p033ch.IOUtil.read(r9, r12, r10)     // Catch:{ all -> 0x004f }
            r9 = -3
            int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r9 != 0) goto L_0x003a
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x004f }
            if (r9 != 0) goto L_0x0026
        L_0x003a:
            long r9 = sun.nio.p033ch.IOStatus.normalize((long) r7)     // Catch:{ all -> 0x004f }
            r11.thread = r5     // Catch:{ all -> 0x0063 }
            int r12 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r12 > 0) goto L_0x004a
            int r12 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r12 != 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r3 = r4
        L_0x004a:
            r11.end(r3)     // Catch:{ all -> 0x0063 }
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            return r9
        L_0x004f:
            r12 = move-exception
            goto L_0x0053
        L_0x0051:
            r12 = move-exception
            r7 = r5
        L_0x0053:
            r11.thread = r5     // Catch:{ all -> 0x0063 }
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 > 0) goto L_0x005f
            int r1 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x005e
            goto L_0x005f
        L_0x005e:
            r3 = r4
        L_0x005f:
            r11.end(r3)     // Catch:{ all -> 0x0063 }
            throw r12     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r11 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SourceChannelImpl.read(java.nio.ByteBuffer[]):long");
    }
}
