package sun.nio.p033ch;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.net.NetHooks;

/* renamed from: sun.nio.ch.ServerSocketChannelImpl */
class ServerSocketChannelImpl extends ServerSocketChannel implements SelChImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_INUSE = 0;
    private static final int ST_KILLED = 1;
    private static final int ST_UNINITIALIZED = -1;

    /* renamed from: nd */
    private static NativeDispatcher f886nd = new SocketDispatcher();

    /* renamed from: fd */
    private final FileDescriptor f887fd;
    private int fdVal;
    private boolean isReuseAddress;
    private InetSocketAddress localAddress;
    private final Object lock = new Object();
    ServerSocket socket;
    private int state = -1;
    private final Object stateLock = new Object();
    private volatile long thread = 0;

    private native int accept0(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, InetSocketAddress[] inetSocketAddressArr) throws IOException;

    private static native void initIDs();

    ServerSocketChannelImpl(SelectorProvider selectorProvider) throws IOException {
        super(selectorProvider);
        FileDescriptor serverSocket = Net.serverSocket(true);
        this.f887fd = serverSocket;
        this.fdVal = IOUtil.fdVal(serverSocket);
        this.state = 0;
    }

    ServerSocketChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor, boolean z) throws IOException {
        super(selectorProvider);
        this.f887fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 0;
        if (z) {
            this.localAddress = Net.localAddress(fileDescriptor);
        }
    }

    public ServerSocket socket() {
        ServerSocket serverSocket;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = ServerSocketAdaptor.create(this);
            }
            serverSocket = this.socket;
        }
        return serverSocket;
    }

    public SocketAddress getLocalAddress() throws IOException {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            if (isOpen()) {
                inetSocketAddress = this.localAddress;
                if (inetSocketAddress != null) {
                    inetSocketAddress = Net.getRevealedLocalAddress(Net.asInetSocketAddress(inetSocketAddress));
                }
            } else {
                throw new ClosedChannelException();
            }
        }
        return inetSocketAddress;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0047, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> java.nio.channels.ServerSocketChannel setOption(java.net.SocketOption<T> r4, T r5) throws java.p026io.IOException {
        /*
            r3 = this;
            r4.getClass()
            java.util.Set r0 = r3.supportedOptions()
            boolean r0 = r0.contains(r4)
            if (r0 == 0) goto L_0x0051
            java.lang.Object r0 = r3.stateLock
            monitor-enter(r0)
            boolean r1 = r3.isOpen()     // Catch:{ all -> 0x004e }
            if (r1 == 0) goto L_0x0048
            java.net.SocketOption<java.lang.Integer> r1 = java.net.StandardSocketOptions.IP_TOS     // Catch:{ all -> 0x004e }
            if (r4 != r1) goto L_0x002c
            boolean r1 = sun.nio.p033ch.Net.isIPv6Available()     // Catch:{ all -> 0x004e }
            if (r1 == 0) goto L_0x0023
            java.net.StandardProtocolFamily r1 = java.net.StandardProtocolFamily.INET6     // Catch:{ all -> 0x004e }
            goto L_0x0025
        L_0x0023:
            java.net.StandardProtocolFamily r1 = java.net.StandardProtocolFamily.INET     // Catch:{ all -> 0x004e }
        L_0x0025:
            java.io.FileDescriptor r2 = r3.f887fd     // Catch:{ all -> 0x004e }
            sun.nio.p033ch.Net.setSocketOption(r2, r1, r4, r5)     // Catch:{ all -> 0x004e }
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            return r3
        L_0x002c:
            java.net.SocketOption<java.lang.Boolean> r1 = java.net.StandardSocketOptions.SO_REUSEADDR     // Catch:{ all -> 0x004e }
            if (r4 != r1) goto L_0x003f
            boolean r1 = sun.nio.p033ch.Net.useExclusiveBind()     // Catch:{ all -> 0x004e }
            if (r1 == 0) goto L_0x003f
            java.lang.Boolean r5 = (java.lang.Boolean) r5     // Catch:{ all -> 0x004e }
            boolean r4 = r5.booleanValue()     // Catch:{ all -> 0x004e }
            r3.isReuseAddress = r4     // Catch:{ all -> 0x004e }
            goto L_0x0046
        L_0x003f:
            java.io.FileDescriptor r1 = r3.f887fd     // Catch:{ all -> 0x004e }
            java.net.ProtocolFamily r2 = sun.nio.p033ch.Net.UNSPEC     // Catch:{ all -> 0x004e }
            sun.nio.p033ch.Net.setSocketOption(r1, r2, r4, r5)     // Catch:{ all -> 0x004e }
        L_0x0046:
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            return r3
        L_0x0048:
            java.nio.channels.ClosedChannelException r3 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x004e }
            r3.<init>()     // Catch:{ all -> 0x004e }
            throw r3     // Catch:{ all -> 0x004e }
        L_0x004e:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            throw r3
        L_0x0051:
            java.lang.UnsupportedOperationException r3 = new java.lang.UnsupportedOperationException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r0 = "'"
            r5.<init>((java.lang.String) r0)
            r5.append((java.lang.Object) r4)
            java.lang.String r4 = "' not supported"
            r5.append((java.lang.String) r4)
            java.lang.String r4 = r5.toString()
            r3.<init>((java.lang.String) r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.ServerSocketChannelImpl.setOption(java.net.SocketOption, java.lang.Object):java.nio.channels.ServerSocketChannel");
    }

    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        socketOption.getClass();
        if (supportedOptions().contains(socketOption)) {
            synchronized (this.stateLock) {
                if (!isOpen()) {
                    throw new ClosedChannelException();
                } else if (socketOption != StandardSocketOptions.SO_REUSEADDR || !Net.useExclusiveBind()) {
                    T socketOption2 = Net.getSocketOption(this.f887fd, Net.UNSPEC, socketOption);
                    return socketOption2;
                } else {
                    T valueOf = Boolean.valueOf(this.isReuseAddress);
                    return valueOf;
                }
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    /* renamed from: sun.nio.ch.ServerSocketChannelImpl$DefaultOptionsHolder */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(2);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.IP_TOS);
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    public boolean isBound() {
        boolean z;
        synchronized (this.stateLock) {
            z = this.localAddress != null;
        }
        return z;
    }

    public InetSocketAddress localAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.localAddress;
        }
        return inetSocketAddress;
    }

    public ServerSocketChannel bind(SocketAddress socketAddress, int i) throws IOException {
        InetSocketAddress inetSocketAddress;
        synchronized (this.lock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (!isBound()) {
                if (socketAddress == null) {
                    inetSocketAddress = new InetSocketAddress(0);
                } else {
                    inetSocketAddress = Net.checkAddress(socketAddress);
                }
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkListen(inetSocketAddress.getPort());
                }
                NetHooks.beforeTcpBind(this.f887fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                Net.bind(this.f887fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                FileDescriptor fileDescriptor = this.f887fd;
                if (i < 1) {
                    i = 50;
                }
                Net.listen(fileDescriptor, i);
                synchronized (this.stateLock) {
                    this.localAddress = Net.localAddress(this.f887fd);
                }
            } else {
                throw new AlreadyBoundException();
            }
        }
        return this;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x007a, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x007b, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0084, code lost:
        r2 = false;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0083 A[Catch:{ SecurityException -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0084 A[Catch:{ SecurityException -> 0x0074 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.channels.SocketChannel accept() throws java.p026io.IOException {
        /*
            r11 = this;
            java.lang.Object r0 = r11.lock
            monitor-enter(r0)
            boolean r1 = r11.isOpen()     // Catch:{ all -> 0x0095 }
            if (r1 == 0) goto L_0x008f
            boolean r1 = r11.isBound()     // Catch:{ all -> 0x0095 }
            if (r1 == 0) goto L_0x0089
            java.io.FileDescriptor r1 = new java.io.FileDescriptor     // Catch:{ all -> 0x0095 }
            r1.<init>()     // Catch:{ all -> 0x0095 }
            r2 = 1
            java.net.InetSocketAddress[] r3 = new java.net.InetSocketAddress[r2]     // Catch:{ all -> 0x0095 }
            r4 = 0
            r6 = 0
            r11.begin()     // Catch:{ all -> 0x007d }
            boolean r7 = r11.isOpen()     // Catch:{ all -> 0x007d }
            r8 = 0
            if (r7 != 0) goto L_0x002b
            r11.thread = r4     // Catch:{ all -> 0x0095 }
            r11.end(r6)     // Catch:{ all -> 0x0095 }
            monitor-exit(r0)     // Catch:{ all -> 0x0095 }
            return r8
        L_0x002b:
            long r9 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x007d }
            r11.thread = r9     // Catch:{ all -> 0x007d }
            r7 = r6
        L_0x0032:
            java.io.FileDescriptor r9 = r11.f887fd     // Catch:{ all -> 0x007b }
            int r7 = r11.accept(r9, r1, r3)     // Catch:{ all -> 0x007b }
            r9 = -3
            if (r7 != r9) goto L_0x0042
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x007b }
            if (r9 == 0) goto L_0x0042
            goto L_0x0032
        L_0x0042:
            r11.thread = r4     // Catch:{ all -> 0x0095 }
            if (r7 <= 0) goto L_0x0048
            r4 = r2
            goto L_0x0049
        L_0x0048:
            r4 = r6
        L_0x0049:
            r11.end(r4)     // Catch:{ all -> 0x0095 }
            if (r7 >= r2) goto L_0x0050
            monitor-exit(r0)     // Catch:{ all -> 0x0095 }
            return r8
        L_0x0050:
            sun.nio.p033ch.IOUtil.configureBlocking(r1, r2)     // Catch:{ all -> 0x0095 }
            r2 = r3[r6]     // Catch:{ all -> 0x0095 }
            sun.nio.ch.SocketChannelImpl r3 = new sun.nio.ch.SocketChannelImpl     // Catch:{ all -> 0x0095 }
            java.nio.channels.spi.SelectorProvider r11 = r11.provider()     // Catch:{ all -> 0x0095 }
            r3.<init>((java.nio.channels.spi.SelectorProvider) r11, (java.p026io.FileDescriptor) r1, (java.net.InetSocketAddress) r2)     // Catch:{ all -> 0x0095 }
            java.lang.SecurityManager r11 = java.lang.System.getSecurityManager()     // Catch:{ all -> 0x0095 }
            if (r11 == 0) goto L_0x0079
            java.net.InetAddress r1 = r2.getAddress()     // Catch:{ SecurityException -> 0x0074 }
            java.lang.String r1 = r1.getHostAddress()     // Catch:{ SecurityException -> 0x0074 }
            int r2 = r2.getPort()     // Catch:{ SecurityException -> 0x0074 }
            r11.checkAccept(r1, r2)     // Catch:{ SecurityException -> 0x0074 }
            goto L_0x0079
        L_0x0074:
            r11 = move-exception
            r3.close()     // Catch:{ all -> 0x0095 }
            throw r11     // Catch:{ all -> 0x0095 }
        L_0x0079:
            monitor-exit(r0)     // Catch:{ all -> 0x0095 }
            return r3
        L_0x007b:
            r1 = move-exception
            goto L_0x007f
        L_0x007d:
            r1 = move-exception
            r7 = r6
        L_0x007f:
            r11.thread = r4     // Catch:{ all -> 0x0095 }
            if (r7 <= 0) goto L_0x0084
            goto L_0x0085
        L_0x0084:
            r2 = r6
        L_0x0085:
            r11.end(r2)     // Catch:{ all -> 0x0095 }
            throw r1     // Catch:{ all -> 0x0095 }
        L_0x0089:
            java.nio.channels.NotYetBoundException r11 = new java.nio.channels.NotYetBoundException     // Catch:{ all -> 0x0095 }
            r11.<init>()     // Catch:{ all -> 0x0095 }
            throw r11     // Catch:{ all -> 0x0095 }
        L_0x008f:
            java.nio.channels.ClosedChannelException r11 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0095 }
            r11.<init>()     // Catch:{ all -> 0x0095 }
            throw r11     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r11 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0095 }
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.ServerSocketChannelImpl.accept():java.nio.channels.SocketChannel");
    }

    /* access modifiers changed from: protected */
    public void implConfigureBlocking(boolean z) throws IOException {
        IOUtil.configureBlocking(this.f887fd, z);
    }

    /* access modifiers changed from: protected */
    public void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            if (this.state != 1) {
                f886nd.preClose(this.f887fd);
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
            int i = this.state;
            if (i != 1) {
                if (i == -1) {
                    this.state = 1;
                    return;
                }
                f886nd.close(this.f887fd);
                this.state = 1;
            }
        }
    }

    public boolean translateReadyOps(int i, int i2, SelectionKeyImpl selectionKeyImpl) {
        int nioInterestOps = selectionKeyImpl.nioInterestOps();
        int nioReadyOps = selectionKeyImpl.nioReadyOps();
        if ((Net.POLLNVAL & i) != 0) {
            return false;
        }
        if (((Net.POLLERR | Net.POLLHUP) & i) != 0) {
            selectionKeyImpl.nioReadyOps(nioInterestOps);
            if ((nioInterestOps & (~nioReadyOps)) != 0) {
                return true;
            }
            return false;
        }
        if (!((i & Net.POLLIN) == 0 || (nioInterestOps & 16) == 0)) {
            i2 |= 16;
        }
        selectionKeyImpl.nioReadyOps(i2);
        if (((~nioReadyOps) & i2) != 0) {
            return true;
        }
        return false;
    }

    public boolean translateAndUpdateReadyOps(int i, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i, selectionKeyImpl.nioReadyOps(), selectionKeyImpl);
    }

    public boolean translateAndSetReadyOps(int i, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i, 0, selectionKeyImpl);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r8 = sun.nio.p033ch.Net.poll(r7.f887fd, r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r7.thread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0029, code lost:
        if (r8 <= 0) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002b, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002c, code lost:
        end(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0030, code lost:
        return r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int poll(int r8, long r9) throws java.p026io.IOException {
        /*
            r7 = this;
            java.lang.Object r0 = r7.lock
            monitor-enter(r0)
            r1 = 0
            r3 = 0
            r7.begin()     // Catch:{ all -> 0x0034 }
            java.lang.Object r4 = r7.stateLock     // Catch:{ all -> 0x0034 }
            monitor-enter(r4)     // Catch:{ all -> 0x0034 }
            boolean r5 = r7.isOpen()     // Catch:{ all -> 0x0031 }
            if (r5 != 0) goto L_0x001a
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
            r7.thread = r1     // Catch:{ all -> 0x003b }
            r7.end(r3)     // Catch:{ all -> 0x003b }
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            return r3
        L_0x001a:
            long r5 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x0031 }
            r7.thread = r5     // Catch:{ all -> 0x0031 }
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
            java.io.FileDescriptor r4 = r7.f887fd     // Catch:{ all -> 0x0034 }
            int r8 = sun.nio.p033ch.Net.poll(r4, r8, r9)     // Catch:{ all -> 0x0034 }
            r7.thread = r1     // Catch:{ all -> 0x003b }
            if (r8 <= 0) goto L_0x002c
            r3 = 1
        L_0x002c:
            r7.end(r3)     // Catch:{ all -> 0x003b }
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            return r8
        L_0x0031:
            r8 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0031 }
            throw r8     // Catch:{ all -> 0x0034 }
        L_0x0034:
            r8 = move-exception
            r7.thread = r1     // Catch:{ all -> 0x003b }
            r7.end(r3)     // Catch:{ all -> 0x003b }
            throw r8     // Catch:{ all -> 0x003b }
        L_0x003b:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003b }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.ServerSocketChannelImpl.poll(int, long):int");
    }

    public void translateAndSetInterestOps(int i, SelectionKeyImpl selectionKeyImpl) {
        int i2 = i & 16;
        short s = 0;
        if (i2 != 0) {
            s = 0 | Net.POLLIN;
        }
        selectionKeyImpl.selector.putEventOps(selectionKeyImpl, s);
    }

    public FileDescriptor getFD() {
        return this.f887fd;
    }

    public int getFDVal() {
        return this.fdVal;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getName());
        stringBuffer.append('[');
        if (!isOpen()) {
            stringBuffer.append("closed");
        } else {
            synchronized (this.stateLock) {
                InetSocketAddress localAddress2 = localAddress();
                if (localAddress2 == null) {
                    stringBuffer.append("unbound");
                } else {
                    stringBuffer.append(Net.getRevealedLocalAddressAsString(localAddress2));
                }
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    private int accept(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, InetSocketAddress[] inetSocketAddressArr) throws IOException {
        return accept0(fileDescriptor, fileDescriptor2, inetSocketAddressArr);
    }

    static {
        initIDs();
    }
}
