package sun.nio.p033ch;

import dalvik.system.CloseGuard;
import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NotYetBoundException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import sun.nio.p033ch.Port;

/* renamed from: sun.nio.ch.UnixAsynchronousServerSocketChannelImpl */
class UnixAsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannelImpl implements Port.PollableChannel {

    /* renamed from: nd */
    private static final NativeDispatcher f898nd = new SocketDispatcher();
    private AccessControlContext acceptAcc;
    private Object acceptAttachment;
    private PendingFuture<AsynchronousSocketChannel, Object> acceptFuture;
    private CompletionHandler<AsynchronousSocketChannel, Object> acceptHandler;
    private boolean acceptPending;
    private final AtomicBoolean accepting = new AtomicBoolean();
    private final int fdVal;
    private final CloseGuard guard;
    private final Port port;
    private final Object updateLock = new Object();

    private native int accept0(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, InetSocketAddress[] inetSocketAddressArr) throws IOException;

    private static native void initIDs();

    static {
        initIDs();
    }

    private void enableAccept() {
        this.accepting.set(false);
    }

    UnixAsynchronousServerSocketChannelImpl(Port port2) throws IOException {
        super(port2);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        try {
            IOUtil.configureBlocking(this.f873fd, false);
            this.port = port2;
            int fdVal2 = IOUtil.fdVal(this.f873fd);
            this.fdVal = fdVal2;
            port2.register(fdVal2, this);
            closeGuard.open("close");
        } catch (IOException e) {
            f898nd.close(this.f873fd);
            throw e;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0030, code lost:
        if (r2 != null) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        r4.setFailure(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0036, code lost:
        sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r2, r3, null, (java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        r0 = new java.nio.channels.AsynchronousCloseException();
        r0.setStackTrace(new java.lang.StackTraceElement[0]);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void implClose() throws java.p026io.IOException {
        /*
            r5 = this;
            dalvik.system.CloseGuard r0 = r5.guard
            r0.close()
            sun.nio.ch.Port r0 = r5.port
            int r1 = r5.fdVal
            r0.unregister(r1)
            sun.nio.ch.NativeDispatcher r0 = f898nd
            java.io.FileDescriptor r1 = r5.f873fd
            r0.close(r1)
            java.lang.Object r0 = r5.updateLock
            monitor-enter(r0)
            boolean r1 = r5.acceptPending     // Catch:{ all -> 0x003b }
            if (r1 != 0) goto L_0x001c
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            return
        L_0x001c:
            r1 = 0
            r5.acceptPending = r1     // Catch:{ all -> 0x003b }
            java.nio.channels.CompletionHandler<java.nio.channels.AsynchronousSocketChannel, java.lang.Object> r2 = r5.acceptHandler     // Catch:{ all -> 0x003b }
            java.lang.Object r3 = r5.acceptAttachment     // Catch:{ all -> 0x003b }
            sun.nio.ch.PendingFuture<java.nio.channels.AsynchronousSocketChannel, java.lang.Object> r4 = r5.acceptFuture     // Catch:{ all -> 0x003b }
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            java.nio.channels.AsynchronousCloseException r0 = new java.nio.channels.AsynchronousCloseException
            r0.<init>()
            java.lang.StackTraceElement[] r1 = new java.lang.StackTraceElement[r1]
            r0.setStackTrace(r1)
            if (r2 != 0) goto L_0x0036
            r4.setFailure(r0)
            goto L_0x003a
        L_0x0036:
            r1 = 0
            sun.nio.p033ch.Invoker.invokeIndirectly((java.nio.channels.AsynchronousChannel) r5, r2, r3, r1, (java.lang.Throwable) r0)
        L_0x003a:
            return
        L_0x003b:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousServerSocketChannelImpl.implClose():void");
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

    public AsynchronousChannelGroupImpl group() {
        return this.port;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        begin();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        if (accept(r8.f873fd, r0, r3) != -2) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
        r6 = r8.updateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r8.acceptPending = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r8.port.startPoll(r8.fdVal, sun.nio.p033ch.Net.POLLIN);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0031, code lost:
        end();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0038, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003b, code lost:
        if ((r2 instanceof java.nio.channels.ClosedChannelException) != false) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0042, code lost:
        r2 = new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0043, code lost:
        r4 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0087, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0088, code lost:
        end();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x008b, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000d, code lost:
        r0 = new java.p026io.FileDescriptor();
        r3 = new java.net.InetSocketAddress[1];
        r4 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0083  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onEvent(int r9, boolean r10) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.updateLock
            monitor-enter(r0)
            boolean r1 = r8.acceptPending     // Catch:{ all -> 0x008c }
            if (r1 != 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x008c }
            return
        L_0x0009:
            r1 = 0
            r8.acceptPending = r1     // Catch:{ all -> 0x008c }
            monitor-exit(r0)     // Catch:{ all -> 0x008c }
            java.io.FileDescriptor r0 = new java.io.FileDescriptor
            r0.<init>()
            r2 = 1
            java.net.InetSocketAddress[] r3 = new java.net.InetSocketAddress[r2]
            r4 = 0
            r8.begin()     // Catch:{ all -> 0x0038 }
            java.io.FileDescriptor r5 = r8.f873fd     // Catch:{ all -> 0x0038 }
            int r5 = r8.accept(r5, r0, r3)     // Catch:{ all -> 0x0038 }
            r6 = -2
            if (r5 != r6) goto L_0x0044
            java.lang.Object r6 = r8.updateLock     // Catch:{ all -> 0x0038 }
            monitor-enter(r6)     // Catch:{ all -> 0x0038 }
            r8.acceptPending = r2     // Catch:{ all -> 0x0035 }
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            sun.nio.ch.Port r2 = r8.port     // Catch:{ all -> 0x0038 }
            int r6 = r8.fdVal     // Catch:{ all -> 0x0038 }
            short r7 = sun.nio.p033ch.Net.POLLIN     // Catch:{ all -> 0x0038 }
            r2.startPoll(r6, r7)     // Catch:{ all -> 0x0038 }
            r8.end()
            return
        L_0x0035:
            r2 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            throw r2     // Catch:{ all -> 0x0038 }
        L_0x0038:
            r2 = move-exception
            boolean r5 = r2 instanceof java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0087 }
            if (r5 == 0) goto L_0x0043
            java.nio.channels.AsynchronousCloseException r5 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0087 }
            r5.<init>()     // Catch:{ all -> 0x0087 }
            r2 = r5
        L_0x0043:
            r4 = r2
        L_0x0044:
            r8.end()
            r2 = 0
            if (r4 != 0) goto L_0x0066
            r1 = r3[r1]     // Catch:{ all -> 0x0055 }
            java.security.AccessControlContext r5 = r8.acceptAcc     // Catch:{ all -> 0x0055 }
            java.nio.channels.AsynchronousSocketChannel r1 = r8.finishAccept(r0, r1, r5)     // Catch:{ all -> 0x0055 }
            r2 = r1
            goto L_0x0066
        L_0x0055:
            r1 = move-exception
            boolean r5 = r1 instanceof java.p026io.IOException
            if (r5 != 0) goto L_0x0064
            boolean r5 = r1 instanceof java.lang.SecurityException
            if (r5 != 0) goto L_0x0064
            java.io.IOException r5 = new java.io.IOException
            r5.<init>((java.lang.Throwable) r1)
            r1 = r5
        L_0x0064:
            r4 = r1
            goto L_0x0067
        L_0x0066:
        L_0x0067:
            java.nio.channels.CompletionHandler<java.nio.channels.AsynchronousSocketChannel, java.lang.Object> r1 = r8.acceptHandler
            java.lang.Object r5 = r8.acceptAttachment
            sun.nio.ch.PendingFuture<java.nio.channels.AsynchronousSocketChannel, java.lang.Object> r6 = r8.acceptFuture
            r8.enableAccept()
            if (r1 != 0) goto L_0x0083
            r6.setResult(r2, r4)
            if (r2 == 0) goto L_0x0082
            boolean r7 = r6.isCancelled()
            if (r7 == 0) goto L_0x0082
            r2.close()     // Catch:{ IOException -> 0x0081 }
            goto L_0x0082
        L_0x0081:
            r7 = move-exception
        L_0x0082:
            goto L_0x0086
        L_0x0083:
            sun.nio.p033ch.Invoker.invoke(r8, r1, r5, r2, r4)
        L_0x0086:
            return
        L_0x0087:
            r1 = move-exception
            r8.end()
            throw r1
        L_0x008c:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x008c }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.UnixAsynchronousServerSocketChannelImpl.onEvent(int, boolean):void");
    }

    private AsynchronousSocketChannel finishAccept(FileDescriptor newfd, final InetSocketAddress remote, AccessControlContext acc) throws IOException, SecurityException {
        try {
            UnixAsynchronousSocketChannelImpl unixAsynchronousSocketChannelImpl = new UnixAsynchronousSocketChannelImpl(this.port, newfd, remote);
            if (acc != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        public Void run() {
                            SecurityManager securityManager = System.getSecurityManager();
                            if (securityManager == null) {
                                return null;
                            }
                            securityManager.checkAccept(remote.getAddress().getHostAddress(), remote.getPort());
                            return null;
                        }
                    }, acc);
                } catch (SecurityException e) {
                    unixAsynchronousSocketChannelImpl.close();
                } catch (Throwable th) {
                    e.addSuppressed(th);
                }
            } else {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkAccept(remote.getAddress().getHostAddress(), remote.getPort());
                }
            }
            return unixAsynchronousSocketChannelImpl;
            throw e;
        } catch (IOException e2) {
            f898nd.close(newfd);
            throw e2;
        }
    }

    /* access modifiers changed from: package-private */
    public Future<AsynchronousSocketChannel> implAccept(Object att, CompletionHandler<AsynchronousSocketChannel, Object> completionHandler) {
        AccessControlContext accessControlContext;
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, att, null, closedChannelException);
            return null;
        } else if (this.localAddress == null) {
            throw new NotYetBoundException();
        } else if (isAcceptKilled()) {
            throw new RuntimeException("Accept not allowed due cancellation");
        } else if (this.accepting.compareAndSet(false, true)) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            InetSocketAddress[] inetSocketAddressArr = new InetSocketAddress[1];
            AsynchronousCloseException asynchronousCloseException = null;
            try {
                begin();
                if (accept(this.f873fd, fileDescriptor, inetSocketAddressArr) == -2) {
                    PendingFuture<AsynchronousSocketChannel, Object> pendingFuture = null;
                    synchronized (this.updateLock) {
                        if (completionHandler == null) {
                            this.acceptHandler = null;
                            pendingFuture = new PendingFuture<>(this);
                            this.acceptFuture = pendingFuture;
                        } else {
                            this.acceptHandler = completionHandler;
                            this.acceptAttachment = att;
                        }
                        if (System.getSecurityManager() == null) {
                            accessControlContext = null;
                        } else {
                            accessControlContext = AccessController.getContext();
                        }
                        this.acceptAcc = accessControlContext;
                        this.acceptPending = true;
                    }
                    this.port.startPoll(this.fdVal, Net.POLLIN);
                    end();
                    return pendingFuture;
                }
            } catch (Throwable th) {
                th = th;
                try {
                    if (th instanceof ClosedChannelException) {
                        th = new AsynchronousCloseException();
                    }
                    asynchronousCloseException = th;
                } catch (Throwable th2) {
                    end();
                    throw th2;
                }
            }
            end();
            AsynchronousSocketChannel asynchronousSocketChannel = null;
            if (asynchronousCloseException == null) {
                try {
                    asynchronousSocketChannel = finishAccept(fileDescriptor, inetSocketAddressArr[0], (AccessControlContext) null);
                } catch (Throwable th3) {
                    asynchronousCloseException = th3;
                }
            }
            enableAccept();
            if (completionHandler == null) {
                return CompletedFuture.withResult(asynchronousSocketChannel, asynchronousCloseException);
            }
            Invoker.invokeIndirectly((AsynchronousChannel) this, completionHandler, att, asynchronousSocketChannel, (Throwable) asynchronousCloseException);
            return null;
        } else {
            throw new AcceptPendingException();
        }
    }

    private int accept(FileDescriptor ssfd, FileDescriptor newfd, InetSocketAddress[] isaa) throws IOException {
        return accept0(ssfd, newfd, isaa);
    }
}
