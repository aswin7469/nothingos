package sun.nio.p033ch;

import dalvik.system.CloseGuard;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.ShutdownChannelGroupException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.util.concurrent.Future;
import sun.net.NetHooks;
import sun.nio.p033ch.Port;
import sun.security.action.GetPropertyAction;

/* renamed from: sun.nio.ch.UnixAsynchronousSocketChannelImpl */
class UnixAsynchronousSocketChannelImpl extends AsynchronousSocketChannelImpl implements Port.PollableChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean disableSynchronousRead;

    /* renamed from: nd */
    private static final NativeDispatcher f899nd = new SocketDispatcher();
    private Object connectAttachment;
    private PendingFuture<Void, Object> connectFuture;
    private CompletionHandler<Void, Object> connectHandler;
    private boolean connectPending;
    private final int fdVal;
    private final CloseGuard guard;
    private boolean isGatheringWrite;
    private boolean isScatteringRead;
    private SocketAddress pendingRemote;
    private final Port port;
    /* access modifiers changed from: private */
    public Object readAttachment;
    private ByteBuffer readBuffer;
    private ByteBuffer[] readBuffers;
    /* access modifiers changed from: private */
    public PendingFuture<Number, Object> readFuture;
    /* access modifiers changed from: private */
    public CompletionHandler<Number, Object> readHandler;
    /* access modifiers changed from: private */
    public boolean readPending;
    private Runnable readTimeoutTask;
    private Future<?> readTimer;
    /* access modifiers changed from: private */
    public final Object updateLock = new Object();
    /* access modifiers changed from: private */
    public Object writeAttachment;
    private ByteBuffer writeBuffer;
    private ByteBuffer[] writeBuffers;
    /* access modifiers changed from: private */
    public PendingFuture<Number, Object> writeFuture;
    /* access modifiers changed from: private */
    public CompletionHandler<Number, Object> writeHandler;
    /* access modifiers changed from: private */
    public boolean writePending;
    private Runnable writeTimeoutTask;
    private Future<?> writeTimer;

    /* renamed from: sun.nio.ch.UnixAsynchronousSocketChannelImpl$OpType */
    private enum OpType {
        CONNECT,
        READ,
        WRITE
    }

    private static native void checkConnect(int i) throws IOException;

    static {
        boolean z;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.disableSynchronousRead", "false"));
        if (str.length() == 0) {
            z = true;
        } else {
            z = Boolean.valueOf(str).booleanValue();
        }
        disableSynchronousRead = z;
    }

    UnixAsynchronousSocketChannelImpl(Port port2) throws IOException {
        super(port2);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.readTimeoutTask = new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:10:0x0035, code lost:
                if (r1 != null) goto L_0x003b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
                r3.setFailure(r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x003b, code lost:
                sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5.this$0, r1, r2, null, (java.lang.Throwable) r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
                r5.this$0.enableReading(true);
                r0 = new java.nio.channels.InterruptedByTimeoutException();
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    java.lang.Object r0 = r0.updateLock
                    monitor-enter(r0)
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    boolean r1 = r1.readPending     // Catch:{ all -> 0x0042 }
                    if (r1 != 0) goto L_0x0011
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    return
                L_0x0011:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    r2 = 0
                    r1.readPending = r2     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.nio.channels.CompletionHandler r1 = r1.readHandler     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r2 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.lang.Object r2 = r2.readAttachment     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.PendingFuture r3 = r3.readFuture     // Catch:{ all -> 0x0042 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r4 = 1
                    r0.enableReading(r4)
                    java.nio.channels.InterruptedByTimeoutException r0 = new java.nio.channels.InterruptedByTimeoutException
                    r0.<init>()
                    if (r1 != 0) goto L_0x003b
                    r3.setFailure(r0)
                    goto L_0x0041
                L_0x003b:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r5 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r3 = 0
                    sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r1, r2, r3, (java.lang.Throwable) r0)
                L_0x0041:
                    return
                L_0x0042:
                    r5 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.C47741.run():void");
            }
        };
        this.writeTimeoutTask = new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:10:0x0035, code lost:
                if (r1 == null) goto L_0x003e;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
                sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5.this$0, r1, r2, null, (java.lang.Throwable) r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x003e, code lost:
                r3.setFailure(r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
                r5.this$0.enableWriting(true);
                r0 = new java.nio.channels.InterruptedByTimeoutException();
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    java.lang.Object r0 = r0.updateLock
                    monitor-enter(r0)
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    boolean r1 = r1.writePending     // Catch:{ all -> 0x0042 }
                    if (r1 != 0) goto L_0x0011
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    return
                L_0x0011:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    r2 = 0
                    r1.writePending = r2     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.nio.channels.CompletionHandler r1 = r1.writeHandler     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r2 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.lang.Object r2 = r2.writeAttachment     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.PendingFuture r3 = r3.writeFuture     // Catch:{ all -> 0x0042 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r4 = 1
                    r0.enableWriting(r4)
                    java.nio.channels.InterruptedByTimeoutException r0 = new java.nio.channels.InterruptedByTimeoutException
                    r0.<init>()
                    if (r1 == 0) goto L_0x003e
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r5 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r3 = 0
                    sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r1, r2, r3, (java.lang.Throwable) r0)
                    goto L_0x0041
                L_0x003e:
                    r3.setFailure(r0)
                L_0x0041:
                    return
                L_0x0042:
                    r5 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.C47752.run():void");
            }
        };
        try {
            IOUtil.configureBlocking(this.f874fd, false);
            this.port = port2;
            int fdVal2 = IOUtil.fdVal(this.f874fd);
            this.fdVal = fdVal2;
            port2.register(fdVal2, this);
            closeGuard.open("close");
        } catch (IOException e) {
            f899nd.close(this.f874fd);
            throw e;
        }
    }

    UnixAsynchronousSocketChannelImpl(Port port2, FileDescriptor fd, InetSocketAddress remote) throws IOException {
        super(port2, fd, remote);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.readTimeoutTask = new Runnable() {
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    java.lang.Object r0 = r0.updateLock
                    monitor-enter(r0)
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    boolean r1 = r1.readPending     // Catch:{ all -> 0x0042 }
                    if (r1 != 0) goto L_0x0011
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    return
                L_0x0011:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    r2 = 0
                    r1.readPending = r2     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.nio.channels.CompletionHandler r1 = r1.readHandler     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r2 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.lang.Object r2 = r2.readAttachment     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.PendingFuture r3 = r3.readFuture     // Catch:{ all -> 0x0042 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r4 = 1
                    r0.enableReading(r4)
                    java.nio.channels.InterruptedByTimeoutException r0 = new java.nio.channels.InterruptedByTimeoutException
                    r0.<init>()
                    if (r1 != 0) goto L_0x003b
                    r3.setFailure(r0)
                    goto L_0x0041
                L_0x003b:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r5 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r3 = 0
                    sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r1, r2, r3, (java.lang.Throwable) r0)
                L_0x0041:
                    return
                L_0x0042:
                    r5 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.C47741.run():void");
            }
        };
        this.writeTimeoutTask = new Runnable() {
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    java.lang.Object r0 = r0.updateLock
                    monitor-enter(r0)
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    boolean r1 = r1.writePending     // Catch:{ all -> 0x0042 }
                    if (r1 != 0) goto L_0x0011
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    return
                L_0x0011:
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    r2 = 0
                    r1.writePending = r2     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r1 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.nio.channels.CompletionHandler r1 = r1.writeHandler     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r2 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    java.lang.Object r2 = r2.writeAttachment     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.PendingFuture r3 = r3.writeFuture     // Catch:{ all -> 0x0042 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r0 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r4 = 1
                    r0.enableWriting(r4)
                    java.nio.channels.InterruptedByTimeoutException r0 = new java.nio.channels.InterruptedByTimeoutException
                    r0.<init>()
                    if (r1 == 0) goto L_0x003e
                    sun.nio.ch.UnixAsynchronousSocketChannelImpl r5 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.this
                    r3 = 0
                    sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r1, r2, r3, (java.lang.Throwable) r0)
                    goto L_0x0041
                L_0x003e:
                    r3.setFailure(r0)
                L_0x0041:
                    return
                L_0x0042:
                    r5 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.C47752.run():void");
            }
        };
        int fdVal2 = IOUtil.fdVal(fd);
        this.fdVal = fdVal2;
        IOUtil.configureBlocking(fd, false);
        try {
            port2.register(fdVal2, this);
            this.port = port2;
            closeGuard.open("close");
        } catch (ShutdownChannelGroupException e) {
            throw new IOException((Throwable) e);
        }
    }

    public AsynchronousChannelGroupImpl group() {
        return this.port;
    }

    private void updateEvents() {
        short s = 0;
        if (this.readPending) {
            s = 0 | Net.POLLIN;
        }
        if (this.connectPending || this.writePending) {
            s |= Net.POLLOUT;
        }
        if (s != 0) {
            this.port.startPoll(this.fdVal, s);
        }
    }

    private void lockAndUpdateEvents() {
        synchronized (this.updateLock) {
            updateEvents();
        }
    }

    private void finish(boolean mayInvokeDirect, boolean readable, boolean writable) {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        synchronized (this.updateLock) {
            if (readable) {
                try {
                    if (this.readPending) {
                        this.readPending = false;
                        z = true;
                    }
                } catch (Throwable th) {
                    while (true) {
                        throw th;
                    }
                }
            }
            if (writable) {
                if (this.writePending) {
                    this.writePending = false;
                    z2 = true;
                } else if (this.connectPending) {
                    this.connectPending = false;
                    z3 = true;
                }
            }
        }
        if (z) {
            if (z2) {
                finishWrite(false);
            }
            finishRead(mayInvokeDirect);
            return;
        }
        if (z2) {
            finishWrite(mayInvokeDirect);
        }
        if (z3) {
            finishConnect(mayInvokeDirect);
        }
    }

    public void onEvent(int events, boolean mayInvokeDirect) {
        boolean z = true;
        boolean z2 = (Net.POLLIN & events) > 0;
        if ((Net.POLLOUT & events) <= 0) {
            z = false;
        }
        if (((Net.POLLERR | Net.POLLHUP) & events) > 0) {
            z2 = true;
            z = true;
        }
        finish(mayInvokeDirect, z2, z);
    }

    /* access modifiers changed from: package-private */
    public void implClose() throws IOException {
        this.guard.close();
        this.port.unregister(this.fdVal);
        f899nd.close(this.f874fd);
        finish(false, true, true);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.guard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public void onCancel(PendingFuture<?, ?> pendingFuture) {
        if (pendingFuture.getContext() == OpType.CONNECT) {
            killConnect();
        }
        if (pendingFuture.getContext() == OpType.READ) {
            killReading();
        }
        if (pendingFuture.getContext() == OpType.WRITE) {
            killWriting();
        }
    }

    private void setConnected() throws IOException {
        synchronized (this.stateLock) {
            this.state = 2;
            this.localAddress = Net.localAddress(this.f874fd);
            this.remoteAddress = (InetSocketAddress) this.pendingRemote;
        }
    }

    private void finishConnect(boolean mayInvokeDirect) {
        AsynchronousCloseException asynchronousCloseException = null;
        try {
            begin();
            checkConnect(this.fdVal);
            setConnected();
        } catch (Throwable th) {
            end();
            throw th;
        }
        end();
        if (asynchronousCloseException != null) {
            try {
                close();
            } catch (Throwable th2) {
                asynchronousCloseException.addSuppressed(th2);
            }
        }
        CompletionHandler<Void, Object> completionHandler = this.connectHandler;
        Object obj = this.connectAttachment;
        PendingFuture<Void, Object> pendingFuture = this.connectFuture;
        if (completionHandler == null) {
            pendingFuture.setResult(null, asynchronousCloseException);
        } else if (mayInvokeDirect) {
            Invoker.invokeUnchecked(completionHandler, obj, null, asynchronousCloseException);
        } else {
            Invoker.invokeIndirectly((AsynchronousChannel) this, completionHandler, obj, null, (Throwable) asynchronousCloseException);
        }
    }

    /* access modifiers changed from: package-private */
    public <A> Future<Void> implConnect(SocketAddress remote, A a, CompletionHandler<Void, ? super A> completionHandler) {
        boolean z;
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a, null, closedChannelException);
            return null;
        }
        InetSocketAddress checkAddress = Net.checkAddress(remote);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(checkAddress.getAddress().getHostAddress(), checkAddress.getPort());
        }
        synchronized (this.stateLock) {
            if (this.state == 2) {
                throw new AlreadyConnectedException();
            } else if (this.state != 1) {
                this.state = 1;
                this.pendingRemote = remote;
                z = this.localAddress == null;
            } else {
                throw new ConnectionPendingException();
            }
        }
        AsynchronousCloseException asynchronousCloseException = null;
        try {
            begin();
            if (z) {
                NetHooks.beforeTcpConnect(this.f874fd, checkAddress.getAddress(), checkAddress.getPort());
            }
            if (Net.connect(this.f874fd, checkAddress.getAddress(), checkAddress.getPort()) == -2) {
                PendingFuture<Void, Object> pendingFuture = null;
                synchronized (this.updateLock) {
                    if (completionHandler == null) {
                        pendingFuture = new PendingFuture<>(this, OpType.CONNECT);
                        this.connectFuture = pendingFuture;
                    } else {
                        this.connectHandler = completionHandler;
                        this.connectAttachment = a;
                    }
                    this.connectPending = true;
                    updateEvents();
                }
                end();
                return pendingFuture;
            }
            setConnected();
            end();
            if (asynchronousCloseException != null) {
                try {
                    close();
                } catch (Throwable th) {
                    asynchronousCloseException.addSuppressed(th);
                }
            }
            if (completionHandler == null) {
                return CompletedFuture.withResult(null, asynchronousCloseException);
            }
            Invoker.invoke(this, completionHandler, a, null, asynchronousCloseException);
            return null;
        } catch (Throwable th2) {
            th = th2;
            try {
                if (th instanceof ClosedChannelException) {
                    th = new AsynchronousCloseException();
                }
                asynchronousCloseException = th;
            } catch (Throwable th3) {
                end();
                throw th3;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if ((r1 instanceof java.nio.channels.AsynchronousCloseException) == false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006b, code lost:
        if (r6 == null) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006d, code lost:
        r6.cancel(false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0071, code lost:
        if (r1 == null) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        if (r2 == false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        r7 = java.lang.Long.valueOf((long) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007c, code lost:
        r7 = java.lang.Integer.valueOf(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0081, code lost:
        if (r3 != null) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0083, code lost:
        r5.setResult(r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0087, code lost:
        if (r14 == false) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0089, code lost:
        sun.nio.p033ch.Invoker.invokeUnchecked(r3, r4, r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x008d, code lost:
        sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r13, r3, r4, r7, (java.lang.Throwable) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void finishRead(boolean r14) {
        /*
            r13 = this;
            r0 = -1
            r1 = 0
            boolean r2 = r13.isScatteringRead
            java.nio.channels.CompletionHandler<java.lang.Number, java.lang.Object> r3 = r13.readHandler
            java.lang.Object r4 = r13.readAttachment
            sun.nio.ch.PendingFuture<java.lang.Number, java.lang.Object> r5 = r13.readFuture
            java.util.concurrent.Future<?> r6 = r13.readTimer
            r7 = 0
            r13.begin()     // Catch:{ all -> 0x0051 }
            if (r2 == 0) goto L_0x001e
            java.io.FileDescriptor r8 = r13.f874fd     // Catch:{ all -> 0x0051 }
            java.nio.ByteBuffer[] r9 = r13.readBuffers     // Catch:{ all -> 0x0051 }
            sun.nio.ch.NativeDispatcher r10 = f899nd     // Catch:{ all -> 0x0051 }
            long r8 = sun.nio.p033ch.IOUtil.read(r8, r9, r10)     // Catch:{ all -> 0x0051 }
            int r0 = (int) r8     // Catch:{ all -> 0x0051 }
            goto L_0x002b
        L_0x001e:
            java.io.FileDescriptor r8 = r13.f874fd     // Catch:{ all -> 0x0051 }
            java.nio.ByteBuffer r9 = r13.readBuffer     // Catch:{ all -> 0x0051 }
            sun.nio.ch.NativeDispatcher r10 = f899nd     // Catch:{ all -> 0x0051 }
            r11 = -1
            int r8 = sun.nio.p033ch.IOUtil.read(r8, r9, r11, r10)     // Catch:{ all -> 0x0051 }
            r0 = r8
        L_0x002b:
            r8 = -2
            if (r0 != r8) goto L_0x0043
            java.lang.Object r8 = r13.updateLock     // Catch:{ all -> 0x0051 }
            monitor-enter(r8)     // Catch:{ all -> 0x0051 }
            r9 = 1
            r13.readPending = r9     // Catch:{ all -> 0x0040 }
            monitor-exit(r8)     // Catch:{ all -> 0x0040 }
            boolean r7 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r7 != 0) goto L_0x003c
            r13.lockAndUpdateEvents()
        L_0x003c:
            r13.end()
            return
        L_0x0040:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0040 }
            throw r9     // Catch:{ all -> 0x0051 }
        L_0x0043:
            r13.readBuffer = r7     // Catch:{ all -> 0x0051 }
            r13.readBuffers = r7     // Catch:{ all -> 0x0051 }
            r13.readAttachment = r7     // Catch:{ all -> 0x0051 }
            r13.enableReading()     // Catch:{ all -> 0x0051 }
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0067
            goto L_0x0064
        L_0x0051:
            r8 = move-exception
            r13.enableReading()     // Catch:{ all -> 0x0091 }
            boolean r9 = r8 instanceof java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0091 }
            if (r9 == 0) goto L_0x005f
            java.nio.channels.AsynchronousCloseException r9 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0091 }
            r9.<init>()     // Catch:{ all -> 0x0091 }
            r8 = r9
        L_0x005f:
            r1 = r8
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0067
        L_0x0064:
            r13.lockAndUpdateEvents()
        L_0x0067:
            r13.end()
            if (r6 == 0) goto L_0x0071
            r8 = 0
            r6.cancel(r8)
        L_0x0071:
            if (r1 == 0) goto L_0x0074
            goto L_0x0080
        L_0x0074:
            if (r2 == 0) goto L_0x007c
            long r7 = (long) r0
            java.lang.Long r7 = java.lang.Long.valueOf((long) r7)
            goto L_0x0080
        L_0x007c:
            java.lang.Integer r7 = java.lang.Integer.valueOf((int) r0)
        L_0x0080:
            if (r3 != 0) goto L_0x0087
            r5.setResult(r7, r1)
            goto L_0x0090
        L_0x0087:
            if (r14 == 0) goto L_0x008d
            sun.nio.p033ch.Invoker.invokeUnchecked(r3, r4, r7, r1)
            goto L_0x0090
        L_0x008d:
            sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r13, r3, r4, r7, (java.lang.Throwable) r1)
        L_0x0090:
            return
        L_0x0091:
            r7 = move-exception
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0099
            r13.lockAndUpdateEvents()
        L_0x0099:
            r13.end()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.finishRead(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b7, code lost:
        if (1 != 0) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b9, code lost:
        enableReading();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00bc, code lost:
        end();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00bf, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00cf, code lost:
        if (0 == 0) goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d1, code lost:
        enableReading();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00d4, code lost:
        end();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x00ea, code lost:
        if (0 != 0) goto L_0x00d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x00ed, code lost:
        if (r14 == null) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x00ef, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x00f1, code lost:
        if (r2 == false) goto L_0x00f9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x00f3, code lost:
        r3 = java.lang.Long.valueOf((long) r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00f9, code lost:
        r3 = java.lang.Integer.valueOf(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x00fd, code lost:
        r0 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x00fe, code lost:
        if (r8 == null) goto L_0x010b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0100, code lost:
        if (r10 == false) goto L_0x0106;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0102, code lost:
        sun.nio.p033ch.Invoker.invokeDirect(r9, r8, r7, r0, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0106, code lost:
        sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r1, r8, r7, r0, (java.lang.Throwable) r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x010f, code lost:
        return sun.nio.p033ch.CompletedFuture.withResult(r0, r14);
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x00e3 A[Catch:{ all -> 0x0110 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <V extends java.lang.Number, A> java.util.concurrent.Future<V> implRead(boolean r20, java.nio.ByteBuffer r21, java.nio.ByteBuffer[] r22, long r23, java.util.concurrent.TimeUnit r25, A r26, java.nio.channels.CompletionHandler<V, ? super A> r27) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r23
            r7 = r26
            r8 = r27
            r0 = 0
            r9 = 0
            r10 = 0
            boolean r11 = disableSynchronousRead
            if (r11 != 0) goto L_0x0033
            if (r8 != 0) goto L_0x0019
            r10 = 1
            goto L_0x0033
        L_0x0019:
            sun.nio.ch.Invoker$GroupAndInvokeCount r0 = sun.nio.p033ch.Invoker.getGroupAndInvokeCount()
            sun.nio.ch.Port r11 = r1.port
            boolean r9 = sun.nio.p033ch.Invoker.mayInvokeDirect(r0, r11)
            if (r9 != 0) goto L_0x0030
            sun.nio.ch.Port r11 = r1.port
            boolean r11 = r11.isFixedThreadPool()
            if (r11 != 0) goto L_0x002e
            goto L_0x0030
        L_0x002e:
            r11 = 0
            goto L_0x0031
        L_0x0030:
            r11 = 1
        L_0x0031:
            r10 = r11
            goto L_0x0034
        L_0x0033:
            r11 = r10
        L_0x0034:
            r10 = r9
            r9 = r0
            r13 = -2
            r14 = 0
            r15 = 0
            r19.begin()     // Catch:{ all -> 0x00d8 }
            if (r11 == 0) goto L_0x006c
            if (r2 == 0) goto L_0x0051
            java.io.FileDescriptor r0 = r1.f874fd     // Catch:{ all -> 0x004c }
            sun.nio.ch.NativeDispatcher r12 = f899nd     // Catch:{ all -> 0x004c }
            r16 = r11
            long r11 = sun.nio.p033ch.IOUtil.read(r0, r4, r12)     // Catch:{ all -> 0x00c9 }
            int r13 = (int) r11
            goto L_0x0070
        L_0x004c:
            r0 = move-exception
            r16 = r11
            goto L_0x00ca
        L_0x0051:
            r16 = r11
            java.io.FileDescriptor r0 = r1.f874fd     // Catch:{ all -> 0x0068 }
            sun.nio.ch.NativeDispatcher r11 = f899nd     // Catch:{ all -> 0x0068 }
            r17 = r13
            r12 = -1
            int r0 = sun.nio.p033ch.IOUtil.read(r0, r3, r12, r11)     // Catch:{ all -> 0x0061 }
            r13 = r0
            goto L_0x0070
        L_0x0061:
            r0 = move-exception
            r4 = r25
            r13 = r17
            goto L_0x00df
        L_0x0068:
            r0 = move-exception
            r17 = r13
            goto L_0x00ca
        L_0x006c:
            r16 = r11
            r17 = r13
        L_0x0070:
            r0 = -2
            if (r13 != r0) goto L_0x00cd
            r11 = 0
            java.lang.Object r12 = r1.updateLock     // Catch:{ all -> 0x00c9 }
            monitor-enter(r12)     // Catch:{ all -> 0x00c9 }
            r1.isScatteringRead = r2     // Catch:{ all -> 0x00c0 }
            r1.readBuffer = r3     // Catch:{ all -> 0x00c0 }
            r1.readBuffers = r4     // Catch:{ all -> 0x00c0 }
            if (r8 != 0) goto L_0x0090
            r3 = 0
            r1.readHandler = r3     // Catch:{ all -> 0x00c0 }
            sun.nio.ch.PendingFuture r0 = new sun.nio.ch.PendingFuture     // Catch:{ all -> 0x00c0 }
            sun.nio.ch.UnixAsynchronousSocketChannelImpl$OpType r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.OpType.READ     // Catch:{ all -> 0x00c0 }
            r0.<init>(r1, r3)     // Catch:{ all -> 0x00c0 }
            r11 = r0
            r1.readFuture = r11     // Catch:{ all -> 0x00c0 }
            r3 = 0
            r1.readAttachment = r3     // Catch:{ all -> 0x00c0 }
            goto L_0x0097
        L_0x0090:
            r1.readHandler = r8     // Catch:{ all -> 0x00c0 }
            r1.readAttachment = r7     // Catch:{ all -> 0x00c0 }
            r3 = 0
            r1.readFuture = r3     // Catch:{ all -> 0x00c0 }
        L_0x0097:
            r17 = 0
            int r0 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r0 <= 0) goto L_0x00ab
            sun.nio.ch.Port r0 = r1.port     // Catch:{ all -> 0x00c0 }
            java.lang.Runnable r3 = r1.readTimeoutTask     // Catch:{ all -> 0x00c0 }
            r4 = r25
            java.util.concurrent.Future r0 = r0.schedule(r3, r5, r4)     // Catch:{ all -> 0x00c7 }
            r1.readTimer = r0     // Catch:{ all -> 0x00c7 }
            goto L_0x00ad
        L_0x00ab:
            r4 = r25
        L_0x00ad:
            r0 = 1
            r1.readPending = r0     // Catch:{ all -> 0x00c7 }
            r19.updateEvents()     // Catch:{ all -> 0x00c7 }
            monitor-exit(r12)     // Catch:{ all -> 0x00c7 }
            r0 = 1
            if (r0 != 0) goto L_0x00bc
            r19.enableReading()
        L_0x00bc:
            r19.end()
            return r11
        L_0x00c0:
            r0 = move-exception
            r4 = r25
        L_0x00c3:
            monitor-exit(r12)     // Catch:{ all -> 0x00c7 }
            throw r0     // Catch:{ all -> 0x00c5 }
        L_0x00c5:
            r0 = move-exception
            goto L_0x00df
        L_0x00c7:
            r0 = move-exception
            goto L_0x00c3
        L_0x00c9:
            r0 = move-exception
        L_0x00ca:
            r4 = r25
            goto L_0x00df
        L_0x00cd:
            r4 = r25
            if (r15 != 0) goto L_0x00d4
        L_0x00d1:
            r19.enableReading()
        L_0x00d4:
            r19.end()
            goto L_0x00ed
        L_0x00d8:
            r0 = move-exception
            r4 = r25
            r16 = r11
            r17 = r13
        L_0x00df:
            boolean r3 = r0 instanceof java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0110 }
            if (r3 == 0) goto L_0x00e9
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0110 }
            r3.<init>()     // Catch:{ all -> 0x0110 }
            r0 = r3
        L_0x00e9:
            r14 = r0
            if (r15 != 0) goto L_0x00d4
            goto L_0x00d1
        L_0x00ed:
            if (r14 == 0) goto L_0x00f1
            r3 = 0
            goto L_0x00fd
        L_0x00f1:
            if (r2 == 0) goto L_0x00f9
            long r11 = (long) r13
            java.lang.Long r3 = java.lang.Long.valueOf((long) r11)
            goto L_0x00fd
        L_0x00f9:
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r13)
        L_0x00fd:
            r0 = r3
            if (r8 == 0) goto L_0x010b
            if (r10 == 0) goto L_0x0106
            sun.nio.p033ch.Invoker.invokeDirect(r9, r8, r7, r0, r14)
            goto L_0x0109
        L_0x0106:
            sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r1, r8, r7, r0, (java.lang.Throwable) r14)
        L_0x0109:
            r3 = 0
            return r3
        L_0x010b:
            sun.nio.ch.CompletedFuture r3 = sun.nio.p033ch.CompletedFuture.withResult(r0, r14)
            return r3
        L_0x0110:
            r0 = move-exception
            if (r15 != 0) goto L_0x0116
            r19.enableReading()
        L_0x0116:
            r19.end()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.implRead(boolean, java.nio.ByteBuffer, java.nio.ByteBuffer[], long, java.util.concurrent.TimeUnit, java.lang.Object, java.nio.channels.CompletionHandler):java.util.concurrent.Future");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if ((r1 instanceof java.nio.channels.AsynchronousCloseException) == false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006b, code lost:
        if (r6 == null) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006d, code lost:
        r6.cancel(false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0071, code lost:
        if (r1 == null) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        if (r2 == false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        r7 = java.lang.Long.valueOf((long) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007c, code lost:
        r7 = java.lang.Integer.valueOf(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0081, code lost:
        if (r3 != null) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0083, code lost:
        r5.setResult(r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0087, code lost:
        if (r14 == false) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0089, code lost:
        sun.nio.p033ch.Invoker.invokeUnchecked(r3, r4, r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x008d, code lost:
        sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r13, r3, r4, r7, (java.lang.Throwable) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void finishWrite(boolean r14) {
        /*
            r13 = this;
            r0 = -1
            r1 = 0
            boolean r2 = r13.isGatheringWrite
            java.nio.channels.CompletionHandler<java.lang.Number, java.lang.Object> r3 = r13.writeHandler
            java.lang.Object r4 = r13.writeAttachment
            sun.nio.ch.PendingFuture<java.lang.Number, java.lang.Object> r5 = r13.writeFuture
            java.util.concurrent.Future<?> r6 = r13.writeTimer
            r7 = 0
            r13.begin()     // Catch:{ all -> 0x0051 }
            if (r2 == 0) goto L_0x001e
            java.io.FileDescriptor r8 = r13.f874fd     // Catch:{ all -> 0x0051 }
            java.nio.ByteBuffer[] r9 = r13.writeBuffers     // Catch:{ all -> 0x0051 }
            sun.nio.ch.NativeDispatcher r10 = f899nd     // Catch:{ all -> 0x0051 }
            long r8 = sun.nio.p033ch.IOUtil.write(r8, r9, r10)     // Catch:{ all -> 0x0051 }
            int r0 = (int) r8     // Catch:{ all -> 0x0051 }
            goto L_0x002b
        L_0x001e:
            java.io.FileDescriptor r8 = r13.f874fd     // Catch:{ all -> 0x0051 }
            java.nio.ByteBuffer r9 = r13.writeBuffer     // Catch:{ all -> 0x0051 }
            sun.nio.ch.NativeDispatcher r10 = f899nd     // Catch:{ all -> 0x0051 }
            r11 = -1
            int r8 = sun.nio.p033ch.IOUtil.write(r8, r9, r11, r10)     // Catch:{ all -> 0x0051 }
            r0 = r8
        L_0x002b:
            r8 = -2
            if (r0 != r8) goto L_0x0043
            java.lang.Object r8 = r13.updateLock     // Catch:{ all -> 0x0051 }
            monitor-enter(r8)     // Catch:{ all -> 0x0051 }
            r9 = 1
            r13.writePending = r9     // Catch:{ all -> 0x0040 }
            monitor-exit(r8)     // Catch:{ all -> 0x0040 }
            boolean r7 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r7 != 0) goto L_0x003c
            r13.lockAndUpdateEvents()
        L_0x003c:
            r13.end()
            return
        L_0x0040:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0040 }
            throw r9     // Catch:{ all -> 0x0051 }
        L_0x0043:
            r13.writeBuffer = r7     // Catch:{ all -> 0x0051 }
            r13.writeBuffers = r7     // Catch:{ all -> 0x0051 }
            r13.writeAttachment = r7     // Catch:{ all -> 0x0051 }
            r13.enableWriting()     // Catch:{ all -> 0x0051 }
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0067
            goto L_0x0064
        L_0x0051:
            r8 = move-exception
            r13.enableWriting()     // Catch:{ all -> 0x0091 }
            boolean r9 = r8 instanceof java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0091 }
            if (r9 == 0) goto L_0x005f
            java.nio.channels.AsynchronousCloseException r9 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0091 }
            r9.<init>()     // Catch:{ all -> 0x0091 }
            r8 = r9
        L_0x005f:
            r1 = r8
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0067
        L_0x0064:
            r13.lockAndUpdateEvents()
        L_0x0067:
            r13.end()
            if (r6 == 0) goto L_0x0071
            r8 = 0
            r6.cancel(r8)
        L_0x0071:
            if (r1 == 0) goto L_0x0074
            goto L_0x0080
        L_0x0074:
            if (r2 == 0) goto L_0x007c
            long r7 = (long) r0
            java.lang.Long r7 = java.lang.Long.valueOf((long) r7)
            goto L_0x0080
        L_0x007c:
            java.lang.Integer r7 = java.lang.Integer.valueOf((int) r0)
        L_0x0080:
            if (r3 != 0) goto L_0x0087
            r5.setResult(r7, r1)
            goto L_0x0090
        L_0x0087:
            if (r14 == 0) goto L_0x008d
            sun.nio.p033ch.Invoker.invokeUnchecked(r3, r4, r7, r1)
            goto L_0x0090
        L_0x008d:
            sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r13, r3, r4, r7, (java.lang.Throwable) r1)
        L_0x0090:
            return
        L_0x0091:
            r7 = move-exception
            boolean r8 = r1 instanceof java.nio.channels.AsynchronousCloseException
            if (r8 != 0) goto L_0x0099
            r13.lockAndUpdateEvents()
        L_0x0099:
            r13.end()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.finishWrite(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00b6, code lost:
        if (1 != 0) goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b8, code lost:
        enableWriting();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00bb, code lost:
        end();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00be, code lost:
        return r11;
     */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00e4 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x00ed A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x00f6  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0112  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <V extends java.lang.Number, A> java.util.concurrent.Future<V> implWrite(boolean r20, java.nio.ByteBuffer r21, java.nio.ByteBuffer[] r22, long r23, java.util.concurrent.TimeUnit r25, A r26, java.nio.channels.CompletionHandler<V, ? super A> r27) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r23
            r7 = r26
            r8 = r27
            sun.nio.ch.Invoker$GroupAndInvokeCount r9 = sun.nio.p033ch.Invoker.getGroupAndInvokeCount()
            sun.nio.ch.Port r0 = r1.port
            boolean r10 = sun.nio.p033ch.Invoker.mayInvokeDirect(r9, r0)
            if (r8 == 0) goto L_0x0027
            if (r10 != 0) goto L_0x0027
            sun.nio.ch.Port r11 = r1.port
            boolean r11 = r11.isFixedThreadPool()
            if (r11 != 0) goto L_0x0025
            goto L_0x0027
        L_0x0025:
            r11 = 0
            goto L_0x0028
        L_0x0027:
            r11 = 1
        L_0x0028:
            r12 = -2
            r13 = 0
            r14 = 0
            r19.begin()     // Catch:{ all -> 0x00d8 }
            if (r11 == 0) goto L_0x006a
            if (r2 == 0) goto L_0x004f
            java.io.FileDescriptor r0 = r1.f874fd     // Catch:{ all -> 0x0046 }
            sun.nio.ch.NativeDispatcher r15 = f899nd     // Catch:{ all -> 0x0046 }
            r16 = r11
            r17 = r12
            long r11 = sun.nio.p033ch.IOUtil.write(r0, r4, r15)     // Catch:{ all -> 0x0041 }
            int r12 = (int) r11
            r15 = r13
            goto L_0x006f
        L_0x0041:
            r0 = move-exception
            r4 = r25
            r15 = r13
            goto L_0x0066
        L_0x0046:
            r0 = move-exception
            r16 = r11
            r17 = r12
            r4 = r25
            goto L_0x00df
        L_0x004f:
            r16 = r11
            r17 = r12
            java.io.FileDescriptor r0 = r1.f874fd     // Catch:{ all -> 0x0062 }
            sun.nio.ch.NativeDispatcher r11 = f899nd     // Catch:{ all -> 0x0062 }
            r15 = r13
            r12 = -1
            int r0 = sun.nio.p033ch.IOUtil.write(r0, r3, r12, r11)     // Catch:{ all -> 0x0060 }
            r12 = r0
            goto L_0x006f
        L_0x0060:
            r0 = move-exception
            goto L_0x0064
        L_0x0062:
            r0 = move-exception
            r15 = r13
        L_0x0064:
            r4 = r25
        L_0x0066:
            r12 = r17
            goto L_0x00e0
        L_0x006a:
            r16 = r11
            r17 = r12
            r15 = r13
        L_0x006f:
            r0 = -2
            if (r12 != r0) goto L_0x00cc
            r11 = 0
            java.lang.Object r13 = r1.updateLock     // Catch:{ all -> 0x00c8 }
            monitor-enter(r13)     // Catch:{ all -> 0x00c8 }
            r1.isGatheringWrite = r2     // Catch:{ all -> 0x00bf }
            r1.writeBuffer = r3     // Catch:{ all -> 0x00bf }
            r1.writeBuffers = r4     // Catch:{ all -> 0x00bf }
            if (r8 != 0) goto L_0x008f
            r3 = 0
            r1.writeHandler = r3     // Catch:{ all -> 0x00bf }
            sun.nio.ch.PendingFuture r0 = new sun.nio.ch.PendingFuture     // Catch:{ all -> 0x00bf }
            sun.nio.ch.UnixAsynchronousSocketChannelImpl$OpType r3 = sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.OpType.WRITE     // Catch:{ all -> 0x00bf }
            r0.<init>(r1, r3)     // Catch:{ all -> 0x00bf }
            r11 = r0
            r1.writeFuture = r11     // Catch:{ all -> 0x00bf }
            r3 = 0
            r1.writeAttachment = r3     // Catch:{ all -> 0x00bf }
            goto L_0x0096
        L_0x008f:
            r1.writeHandler = r8     // Catch:{ all -> 0x00bf }
            r1.writeAttachment = r7     // Catch:{ all -> 0x00bf }
            r3 = 0
            r1.writeFuture = r3     // Catch:{ all -> 0x00bf }
        L_0x0096:
            r17 = 0
            int r0 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r0 <= 0) goto L_0x00aa
            sun.nio.ch.Port r0 = r1.port     // Catch:{ all -> 0x00bf }
            java.lang.Runnable r3 = r1.writeTimeoutTask     // Catch:{ all -> 0x00bf }
            r4 = r25
            java.util.concurrent.Future r0 = r0.schedule(r3, r5, r4)     // Catch:{ all -> 0x00c6 }
            r1.writeTimer = r0     // Catch:{ all -> 0x00c6 }
            goto L_0x00ac
        L_0x00aa:
            r4 = r25
        L_0x00ac:
            r0 = 1
            r1.writePending = r0     // Catch:{ all -> 0x00c6 }
            r19.updateEvents()     // Catch:{ all -> 0x00c6 }
            monitor-exit(r13)     // Catch:{ all -> 0x00c6 }
            r0 = 1
            if (r0 != 0) goto L_0x00bb
            r19.enableWriting()
        L_0x00bb:
            r19.end()
            return r11
        L_0x00bf:
            r0 = move-exception
            r4 = r25
        L_0x00c2:
            monitor-exit(r13)     // Catch:{ all -> 0x00c6 }
            throw r0     // Catch:{ all -> 0x00c4 }
        L_0x00c4:
            r0 = move-exception
            goto L_0x00e0
        L_0x00c6:
            r0 = move-exception
            goto L_0x00c2
        L_0x00c8:
            r0 = move-exception
            r4 = r25
            goto L_0x00e0
        L_0x00cc:
            r4 = r25
            if (r14 != 0) goto L_0x00d3
            r19.enableWriting()
        L_0x00d3:
            r19.end()
            r13 = r15
            goto L_0x00f4
        L_0x00d8:
            r0 = move-exception
            r4 = r25
            r16 = r11
            r17 = r12
        L_0x00df:
            r15 = r13
        L_0x00e0:
            boolean r3 = r0 instanceof java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0117 }
            if (r3 == 0) goto L_0x00ea
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0117 }
            r3.<init>()     // Catch:{ all -> 0x0117 }
            r0 = r3
        L_0x00ea:
            r13 = r0
            if (r14 != 0) goto L_0x00f0
            r19.enableWriting()
        L_0x00f0:
            r19.end()
        L_0x00f4:
            if (r13 == 0) goto L_0x00f8
            r3 = 0
            goto L_0x0104
        L_0x00f8:
            if (r2 == 0) goto L_0x0100
            long r2 = (long) r12
            java.lang.Long r3 = java.lang.Long.valueOf((long) r2)
            goto L_0x0104
        L_0x0100:
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r12)
        L_0x0104:
            r0 = r3
            if (r8 == 0) goto L_0x0112
            if (r10 == 0) goto L_0x010d
            sun.nio.p033ch.Invoker.invokeDirect(r9, r8, r7, r0, r13)
            goto L_0x0110
        L_0x010d:
            sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r1, r8, r7, r0, (java.lang.Throwable) r13)
        L_0x0110:
            r2 = 0
            return r2
        L_0x0112:
            sun.nio.ch.CompletedFuture r2 = sun.nio.p033ch.CompletedFuture.withResult(r0, r13)
            return r2
        L_0x0117:
            r0 = move-exception
            if (r14 != 0) goto L_0x011d
            r19.enableWriting()
        L_0x011d:
            r19.end()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousSocketChannelImpl.implWrite(boolean, java.nio.ByteBuffer, java.nio.ByteBuffer[], long, java.util.concurrent.TimeUnit, java.lang.Object, java.nio.channels.CompletionHandler):java.util.concurrent.Future");
    }
}
