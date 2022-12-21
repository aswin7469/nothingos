package sun.nio.p033ch;

import android.net.wifi.WifiManager;
import dalvik.system.CloseGuard;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.net.ExtendedSocketOptions;
import sun.net.ExtendedOptionsImpl;
import sun.net.NetHooks;

/* renamed from: sun.nio.ch.SocketChannelImpl */
class SocketChannelImpl extends SocketChannel implements SelChImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_CONNECTED = 2;
    private static final int ST_KILLED = 4;
    private static final int ST_KILLPENDING = 3;
    private static final int ST_PENDING = 1;
    private static final int ST_UNCONNECTED = 0;
    private static final int ST_UNINITIALIZED = -1;

    /* renamed from: nd */
    private static NativeDispatcher f894nd = new SocketDispatcher();

    /* renamed from: fd */
    private final FileDescriptor f895fd;
    private final int fdVal;
    private final CloseGuard guard;
    private boolean isInputOpen = true;
    private boolean isOutputOpen = true;
    private boolean isReuseAddress;
    private InetSocketAddress localAddress;
    private final Object readLock = new Object();
    private volatile long readerThread = 0;
    private boolean readyToConnect = false;
    private InetSocketAddress remoteAddress;
    private Socket socket;
    private int state = -1;
    private final Object stateLock = new Object();
    private final Object writeLock = new Object();
    private volatile long writerThread = 0;

    private static native int checkConnect(FileDescriptor fileDescriptor, boolean z, boolean z2) throws IOException;

    private static native int sendOutOfBandData(FileDescriptor fileDescriptor, byte b) throws IOException;

    SocketChannelImpl(SelectorProvider sp) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        FileDescriptor socket2 = Net.socket(true);
        this.f895fd = socket2;
        this.fdVal = IOUtil.fdVal(socket2);
        this.state = 0;
        if (socket2 != null && socket2.valid()) {
            closeGuard.open("close");
        }
    }

    SocketChannelImpl(SelectorProvider sp, FileDescriptor fd, boolean bound) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.f895fd = fd;
        this.fdVal = IOUtil.fdVal(fd);
        this.state = 0;
        if (fd != null && fd.valid()) {
            closeGuard.open("close");
        }
        if (bound) {
            this.localAddress = Net.localAddress(fd);
        }
    }

    SocketChannelImpl(SelectorProvider sp, FileDescriptor fd, InetSocketAddress remote) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.f895fd = fd;
        this.fdVal = IOUtil.fdVal(fd);
        this.state = 2;
        this.localAddress = Net.localAddress(fd);
        this.remoteAddress = remote;
        if (fd != null && fd.valid()) {
            closeGuard.open("close");
        }
    }

    public Socket socket() {
        Socket socket2;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = SocketAdaptor.create(this);
            }
            socket2 = this.socket;
        }
        return socket2;
    }

    public SocketAddress getLocalAddress() throws IOException {
        InetSocketAddress revealedLocalAddress;
        synchronized (this.stateLock) {
            if (isOpen()) {
                revealedLocalAddress = Net.getRevealedLocalAddress(this.localAddress);
            } else {
                throw new ClosedChannelException();
            }
        }
        return revealedLocalAddress;
    }

    public SocketAddress getRemoteAddress() throws IOException {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            if (isOpen()) {
                inetSocketAddress = this.remoteAddress;
            } else {
                throw new ClosedChannelException();
            }
        }
        return inetSocketAddress;
    }

    public <T> SocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        } else if (supportedOptions().contains(socketOption)) {
            synchronized (this.stateLock) {
                if (!isOpen()) {
                    throw new ClosedChannelException();
                } else if (socketOption == StandardSocketOptions.IP_TOS) {
                    Net.setSocketOption(this.f895fd, Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET, socketOption, t);
                    return this;
                } else if (socketOption != StandardSocketOptions.SO_REUSEADDR || !Net.useExclusiveBind()) {
                    Net.setSocketOption(this.f895fd, Net.UNSPEC, socketOption, t);
                    return this;
                } else {
                    this.isReuseAddress = ((Boolean) t).booleanValue();
                    return this;
                }
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        } else if (supportedOptions().contains(socketOption)) {
            synchronized (this.stateLock) {
                if (!isOpen()) {
                    throw new ClosedChannelException();
                } else if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                    T valueOf = Boolean.valueOf(this.isReuseAddress);
                    return valueOf;
                } else if (socketOption == StandardSocketOptions.IP_TOS) {
                    T socketOption2 = Net.getSocketOption(this.f895fd, Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET, socketOption);
                    return socketOption2;
                } else {
                    T socketOption3 = Net.getSocketOption(this.f895fd, Net.UNSPEC, socketOption);
                    return socketOption3;
                }
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    /* renamed from: sun.nio.ch.SocketChannelImpl$DefaultOptionsHolder */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(8);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_KEEPALIVE);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.SO_LINGER);
            hashSet.add(StandardSocketOptions.TCP_NODELAY);
            hashSet.add(StandardSocketOptions.IP_TOS);
            hashSet.add(ExtendedSocketOption.SO_OOBINLINE);
            if (ExtendedOptionsImpl.flowSupported()) {
                hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
            }
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    private boolean ensureReadOpen() throws ClosedChannelException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (!isConnected()) {
                throw new NotYetConnectedException();
            } else if (!this.isInputOpen) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void ensureWriteOpen() throws ClosedChannelException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (!this.isOutputOpen) {
                throw new ClosedChannelException();
            } else if (!isConnected()) {
                throw new NotYetConnectedException();
            }
        }
    }

    private void readerCleanup() throws IOException {
        synchronized (this.stateLock) {
            this.readerThread = 0;
            if (this.state == 3) {
                kill();
            }
        }
    }

    private void writerCleanup() throws IOException {
        synchronized (this.stateLock) {
            this.writerThread = 0;
            if (this.state == 3) {
                kill();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0022, code lost:
        if (0 > 0) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0024, code lost:
        if (0 != -2) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0027, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0028, code lost:
        end(r4);
        r3 = r10.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002d, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002e, code lost:
        if (0 > 0) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0032, code lost:
        if (r10.isInputOpen != false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0034, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0036, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0039, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x003a, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        r1 = sun.nio.p033ch.IOUtil.read(r10.f895fd, r11, -1, f894nd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0050, code lost:
        if (r1 != -3) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0056, code lost:
        if (isOpen() == false) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0059, code lost:
        r6 = sun.nio.p033ch.IOStatus.normalize(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0060, code lost:
        if (r1 > 0) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0062, code lost:
        if (r1 != -2) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0065, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0066, code lost:
        end(r4);
        r3 = r10.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x006b, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x006c, code lost:
        if (r1 > 0) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0070, code lost:
        if (r10.isInputOpen != false) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0072, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0074, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0077, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0078, code lost:
        r2 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(java.nio.ByteBuffer r11) throws java.p026io.IOException {
        /*
            r10 = this;
            if (r11 == 0) goto L_0x00a0
            java.lang.Object r0 = r10.readLock
            monitor-enter(r0)
            boolean r1 = r10.ensureReadOpen()     // Catch:{ all -> 0x009d }
            r2 = -1
            if (r1 != 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r2
        L_0x000e:
            r1 = 0
            r3 = -2
            r4 = 1
            r5 = 0
            r10.begin()     // Catch:{ all -> 0x007e }
            java.lang.Object r6 = r10.stateLock     // Catch:{ all -> 0x007e }
            monitor-enter(r6)     // Catch:{ all -> 0x007e }
            boolean r7 = r10.isOpen()     // Catch:{ all -> 0x007b }
            if (r7 != 0) goto L_0x003d
            monitor-exit(r6)     // Catch:{ all -> 0x007b }
            r10.readerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0028
            if (r1 != r3) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r4 = r5
        L_0x0028:
            r10.end(r4)     // Catch:{ all -> 0x009d }
            java.lang.Object r3 = r10.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r3)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0037
            boolean r4 = r10.isInputOpen     // Catch:{ all -> 0x003a }
            if (r4 != 0) goto L_0x0037
            monitor-exit(r3)     // Catch:{ all -> 0x003a }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r2
        L_0x0037:
            monitor-exit(r3)     // Catch:{ all -> 0x003a }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r5
        L_0x003a:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x003a }
        L_0x003c:
            throw r2     // Catch:{ all -> 0x009d }
        L_0x003d:
            long r7 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x007b }
            r10.readerThread = r7     // Catch:{ all -> 0x007b }
            monitor-exit(r6)     // Catch:{ all -> 0x007b }
        L_0x0044:
            java.io.FileDescriptor r6 = r10.f895fd     // Catch:{ all -> 0x007e }
            sun.nio.ch.NativeDispatcher r7 = f894nd     // Catch:{ all -> 0x007e }
            r8 = -1
            int r6 = sun.nio.p033ch.IOUtil.read(r6, r11, r8, r7)     // Catch:{ all -> 0x007e }
            r1 = r6
            r6 = -3
            if (r1 != r6) goto L_0x0059
            boolean r6 = r10.isOpen()     // Catch:{ all -> 0x007e }
            if (r6 == 0) goto L_0x0059
            goto L_0x0044
        L_0x0059:
            int r6 = sun.nio.p033ch.IOStatus.normalize((int) r1)     // Catch:{ all -> 0x007e }
            r10.readerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0066
            if (r1 != r3) goto L_0x0065
            goto L_0x0066
        L_0x0065:
            r4 = r5
        L_0x0066:
            r10.end(r4)     // Catch:{ all -> 0x009d }
            java.lang.Object r3 = r10.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r3)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0075
            boolean r4 = r10.isInputOpen     // Catch:{ all -> 0x0078 }
            if (r4 != 0) goto L_0x0075
            monitor-exit(r3)     // Catch:{ all -> 0x0078 }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r2
        L_0x0075:
            monitor-exit(r3)     // Catch:{ all -> 0x0078 }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r6
        L_0x0078:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0078 }
            goto L_0x003c
        L_0x007b:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x007b }
            throw r7     // Catch:{ all -> 0x007e }
        L_0x007e:
            r6 = move-exception
            r10.readerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0088
            if (r1 != r3) goto L_0x0087
            goto L_0x0088
        L_0x0087:
            r4 = r5
        L_0x0088:
            r10.end(r4)     // Catch:{ all -> 0x009d }
            java.lang.Object r3 = r10.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r3)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0097
            boolean r4 = r10.isInputOpen     // Catch:{ all -> 0x009a }
            if (r4 != 0) goto L_0x0097
            monitor-exit(r3)     // Catch:{ all -> 0x009a }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r2
        L_0x0097:
            monitor-exit(r3)     // Catch:{ all -> 0x009a }
            throw r6     // Catch:{ all -> 0x009d }
        L_0x009a:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x009a }
            goto L_0x003c
        L_0x009d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            throw r1
        L_0x00a0:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.read(java.nio.ByteBuffer):int");
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public long read(java.nio.ByteBuffer[] r17, int r18, int r19) throws java.p026io.IOException {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            if (r3 < 0) goto L_0x00cc
            if (r4 < 0) goto L_0x00cc
            int r0 = r2.length
            int r0 = r0 - r4
            if (r3 > r0) goto L_0x00cc
            java.lang.Object r5 = r1.readLock
            monitor-enter(r5)
            boolean r0 = r16.ensureReadOpen()     // Catch:{ all -> 0x00c9 }
            r6 = -1
            if (r0 != 0) goto L_0x001d
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            return r6
        L_0x001d:
            r8 = 0
            r11 = -2
            r13 = 1
            r14 = 0
            r16.begin()     // Catch:{ all -> 0x00a3 }
            java.lang.Object r10 = r1.stateLock     // Catch:{ all -> 0x00a3 }
            monitor-enter(r10)     // Catch:{ all -> 0x00a3 }
            boolean r0 = r16.isOpen()     // Catch:{ all -> 0x00a0 }
            if (r0 != 0) goto L_0x0057
            monitor-exit(r10)     // Catch:{ all -> 0x00a0 }
            r16.readerCleanup()     // Catch:{ all -> 0x00c9 }
            int r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x003f
            int r0 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x003d
            goto L_0x003f
        L_0x003d:
            r10 = 0
            goto L_0x0040
        L_0x003f:
            r10 = r13
        L_0x0040:
            r1.end(r10)     // Catch:{ all -> 0x00c9 }
            java.lang.Object r10 = r1.stateLock     // Catch:{ all -> 0x00c9 }
            monitor-enter(r10)     // Catch:{ all -> 0x00c9 }
            int r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x0051
            boolean r0 = r1.isInputOpen     // Catch:{ all -> 0x0054 }
            if (r0 != 0) goto L_0x0051
            monitor-exit(r10)     // Catch:{ all -> 0x0054 }
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            return r6
        L_0x0051:
            monitor-exit(r10)     // Catch:{ all -> 0x0054 }
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            return r14
        L_0x0054:
            r0 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0054 }
        L_0x0056:
            throw r0     // Catch:{ all -> 0x00c9 }
        L_0x0057:
            long r6 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x00a0 }
            r1.readerThread = r6     // Catch:{ all -> 0x00a0 }
            monitor-exit(r10)     // Catch:{ all -> 0x00a0 }
        L_0x005e:
            java.io.FileDescriptor r0 = r1.f895fd     // Catch:{ all -> 0x00a3 }
            sun.nio.ch.NativeDispatcher r6 = f894nd     // Catch:{ all -> 0x00a3 }
            long r6 = sun.nio.p033ch.IOUtil.read(r0, r2, r3, r4, r6)     // Catch:{ all -> 0x00a3 }
            r8 = r6
            r6 = -3
            int r0 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0074
            boolean r0 = r16.isOpen()     // Catch:{ all -> 0x00a3 }
            if (r0 == 0) goto L_0x0074
            goto L_0x005e
        L_0x0074:
            long r6 = sun.nio.p033ch.IOStatus.normalize((long) r8)     // Catch:{ all -> 0x00a3 }
            r16.readerCleanup()     // Catch:{ all -> 0x00c9 }
            int r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x0086
            int r0 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x0084
            goto L_0x0086
        L_0x0084:
            r10 = 0
            goto L_0x0087
        L_0x0086:
            r10 = r13
        L_0x0087:
            r1.end(r10)     // Catch:{ all -> 0x00c9 }
            java.lang.Object r10 = r1.stateLock     // Catch:{ all -> 0x00c9 }
            monitor-enter(r10)     // Catch:{ all -> 0x00c9 }
            int r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x009a
            boolean r0 = r1.isInputOpen     // Catch:{ all -> 0x009d }
            if (r0 != 0) goto L_0x009a
            monitor-exit(r10)     // Catch:{ all -> 0x009d }
        L_0x0096:
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            r5 = -1
            return r5
        L_0x009a:
            monitor-exit(r10)     // Catch:{ all -> 0x009d }
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            return r6
        L_0x009d:
            r0 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x009d }
            goto L_0x0056
        L_0x00a0:
            r0 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x00a0 }
            throw r0     // Catch:{ all -> 0x00a3 }
        L_0x00a3:
            r0 = move-exception
            r16.readerCleanup()     // Catch:{ all -> 0x00c9 }
            int r6 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r6 > 0) goto L_0x00b2
            int r6 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r6 != 0) goto L_0x00b0
            goto L_0x00b2
        L_0x00b0:
            r10 = 0
            goto L_0x00b3
        L_0x00b2:
            r10 = r13
        L_0x00b3:
            r1.end(r10)     // Catch:{ all -> 0x00c9 }
            java.lang.Object r6 = r1.stateLock     // Catch:{ all -> 0x00c9 }
            monitor-enter(r6)     // Catch:{ all -> 0x00c9 }
            int r7 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r7 > 0) goto L_0x00c3
            boolean r7 = r1.isInputOpen     // Catch:{ all -> 0x00c6 }
            if (r7 != 0) goto L_0x00c3
            monitor-exit(r6)     // Catch:{ all -> 0x00c6 }
            goto L_0x0096
        L_0x00c3:
            monitor-exit(r6)     // Catch:{ all -> 0x00c6 }
            throw r0     // Catch:{ all -> 0x00c9 }
        L_0x00c6:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00c6 }
            goto L_0x0056
        L_0x00c9:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00c9 }
            throw r0
        L_0x00cc:
            java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.read(java.nio.ByteBuffer[], int, int):long");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001c, code lost:
        if (0 > 0) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001e, code lost:
        if (0 != -2) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0021, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0022, code lost:
        end(r3);
        r2 = r9.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0027, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0028, code lost:
        if (0 > 0) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002c, code lost:
        if (r9.isOutputOpen == false) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0034, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0035, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0037, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0038, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r1 = sun.nio.p033ch.IOUtil.write(r9.f895fd, r10, -1, f894nd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x004e, code lost:
        if (r1 != -3) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0054, code lost:
        if (isOpen() == false) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0057, code lost:
        r5 = sun.nio.p033ch.IOStatus.normalize(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x005e, code lost:
        if (r1 > 0) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0060, code lost:
        if (r1 != -2) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0063, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0064, code lost:
        end(r3);
        r2 = r9.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0069, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x006a, code lost:
        if (r1 > 0) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x006e, code lost:
        if (r9.isOutputOpen == false) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0076, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0077, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0079, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x007a, code lost:
        r3 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int write(java.nio.ByteBuffer r10) throws java.p026io.IOException {
        /*
            r9 = this;
            if (r10 == 0) goto L_0x00a6
            java.lang.Object r0 = r9.writeLock
            monitor-enter(r0)
            r9.ensureWriteOpen()     // Catch:{ all -> 0x00a3 }
            r1 = 0
            r2 = -2
            r3 = 1
            r4 = 0
            r9.begin()     // Catch:{ all -> 0x0080 }
            java.lang.Object r5 = r9.stateLock     // Catch:{ all -> 0x0080 }
            monitor-enter(r5)     // Catch:{ all -> 0x0080 }
            boolean r6 = r9.isOpen()     // Catch:{ all -> 0x007d }
            if (r6 != 0) goto L_0x003b
            monitor-exit(r5)     // Catch:{ all -> 0x007d }
            r9.writerCleanup()     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x0022
            if (r1 != r2) goto L_0x0021
            goto L_0x0022
        L_0x0021:
            r3 = r4
        L_0x0022:
            r9.end(r3)     // Catch:{ all -> 0x00a3 }
            java.lang.Object r2 = r9.stateLock     // Catch:{ all -> 0x00a3 }
            monitor-enter(r2)     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x0035
            boolean r3 = r9.isOutputOpen     // Catch:{ all -> 0x0038 }
            if (r3 == 0) goto L_0x002f
            goto L_0x0035
        L_0x002f:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0038 }
            r3.<init>()     // Catch:{ all -> 0x0038 }
            throw r3     // Catch:{ all -> 0x0038 }
        L_0x0035:
            monitor-exit(r2)     // Catch:{ all -> 0x0038 }
            monitor-exit(r0)     // Catch:{ all -> 0x00a3 }
            return r4
        L_0x0038:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0038 }
        L_0x003a:
            throw r3     // Catch:{ all -> 0x00a3 }
        L_0x003b:
            long r6 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x007d }
            r9.writerThread = r6     // Catch:{ all -> 0x007d }
            monitor-exit(r5)     // Catch:{ all -> 0x007d }
        L_0x0042:
            java.io.FileDescriptor r5 = r9.f895fd     // Catch:{ all -> 0x0080 }
            sun.nio.ch.NativeDispatcher r6 = f894nd     // Catch:{ all -> 0x0080 }
            r7 = -1
            int r5 = sun.nio.p033ch.IOUtil.write(r5, r10, r7, r6)     // Catch:{ all -> 0x0080 }
            r1 = r5
            r5 = -3
            if (r1 != r5) goto L_0x0057
            boolean r5 = r9.isOpen()     // Catch:{ all -> 0x0080 }
            if (r5 == 0) goto L_0x0057
            goto L_0x0042
        L_0x0057:
            int r5 = sun.nio.p033ch.IOStatus.normalize((int) r1)     // Catch:{ all -> 0x0080 }
            r9.writerCleanup()     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x0064
            if (r1 != r2) goto L_0x0063
            goto L_0x0064
        L_0x0063:
            r3 = r4
        L_0x0064:
            r9.end(r3)     // Catch:{ all -> 0x00a3 }
            java.lang.Object r2 = r9.stateLock     // Catch:{ all -> 0x00a3 }
            monitor-enter(r2)     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x0077
            boolean r3 = r9.isOutputOpen     // Catch:{ all -> 0x007a }
            if (r3 == 0) goto L_0x0071
            goto L_0x0077
        L_0x0071:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x007a }
            r3.<init>()     // Catch:{ all -> 0x007a }
            throw r3     // Catch:{ all -> 0x007a }
        L_0x0077:
            monitor-exit(r2)     // Catch:{ all -> 0x007a }
            monitor-exit(r0)     // Catch:{ all -> 0x00a3 }
            return r5
        L_0x007a:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x007a }
            goto L_0x003a
        L_0x007d:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x007d }
            throw r6     // Catch:{ all -> 0x0080 }
        L_0x0080:
            r5 = move-exception
            r9.writerCleanup()     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x008a
            if (r1 != r2) goto L_0x0089
            goto L_0x008a
        L_0x0089:
            r3 = r4
        L_0x008a:
            r9.end(r3)     // Catch:{ all -> 0x00a3 }
            java.lang.Object r2 = r9.stateLock     // Catch:{ all -> 0x00a3 }
            monitor-enter(r2)     // Catch:{ all -> 0x00a3 }
            if (r1 > 0) goto L_0x009d
            boolean r3 = r9.isOutputOpen     // Catch:{ all -> 0x00a0 }
            if (r3 == 0) goto L_0x0097
            goto L_0x009d
        L_0x0097:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x00a0 }
            r3.<init>()     // Catch:{ all -> 0x00a0 }
            throw r3     // Catch:{ all -> 0x00a0 }
        L_0x009d:
            monitor-exit(r2)     // Catch:{ all -> 0x00a0 }
            throw r5     // Catch:{ all -> 0x00a3 }
        L_0x00a0:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00a0 }
            goto L_0x003a
        L_0x00a3:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00a3 }
            throw r1
        L_0x00a6:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.write(java.nio.ByteBuffer):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0028, code lost:
        if (0 > 0) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002c, code lost:
        if (0 != -2) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002e, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002f, code lost:
        end(r3);
        r3 = r12.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0034, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0037, code lost:
        if (0 > 0) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003b, code lost:
        if (r12.isOutputOpen == false) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0043, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0044, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0046, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0047, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r1 = sun.nio.p033ch.IOUtil.write(r12.f895fd, r13, r14, r15, f894nd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x005e, code lost:
        if (r1 != -3) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0064, code lost:
        if (isOpen() == false) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0067, code lost:
        r9 = sun.nio.p033ch.IOStatus.normalize(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0070, code lost:
        if (r1 > 0) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0074, code lost:
        if (r1 != -2) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0076, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0077, code lost:
        end(r3);
        r3 = r12.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x007c, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x007f, code lost:
        if (r1 > 0) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0083, code lost:
        if (r12.isOutputOpen == false) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x008b, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x008c, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x008e, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x008f, code lost:
        r4 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long write(java.nio.ByteBuffer[] r13, int r14, int r15) throws java.p026io.IOException {
        /*
            r12 = this;
            if (r14 < 0) goto L_0x00c0
            if (r15 < 0) goto L_0x00c0
            int r0 = r13.length
            int r0 = r0 - r15
            if (r14 > r0) goto L_0x00c0
            java.lang.Object r0 = r12.writeLock
            monitor-enter(r0)
            r12.ensureWriteOpen()     // Catch:{ all -> 0x00bd }
            r1 = 0
            r3 = 0
            r4 = -2
            r6 = 1
            r7 = 0
            r12.begin()     // Catch:{ all -> 0x0095 }
            java.lang.Object r9 = r12.stateLock     // Catch:{ all -> 0x0095 }
            monitor-enter(r9)     // Catch:{ all -> 0x0095 }
            boolean r10 = r12.isOpen()     // Catch:{ all -> 0x0092 }
            if (r10 != 0) goto L_0x004a
            monitor-exit(r9)     // Catch:{ all -> 0x0092 }
            r12.writerCleanup()     // Catch:{ all -> 0x00bd }
            int r9 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r9 > 0) goto L_0x002e
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 != 0) goto L_0x002f
        L_0x002e:
            r3 = r6
        L_0x002f:
            r12.end(r3)     // Catch:{ all -> 0x00bd }
            java.lang.Object r3 = r12.stateLock     // Catch:{ all -> 0x00bd }
            monitor-enter(r3)     // Catch:{ all -> 0x00bd }
            int r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r4 > 0) goto L_0x0044
            boolean r4 = r12.isOutputOpen     // Catch:{ all -> 0x0047 }
            if (r4 == 0) goto L_0x003e
            goto L_0x0044
        L_0x003e:
            java.nio.channels.AsynchronousCloseException r4 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0047 }
            r4.<init>()     // Catch:{ all -> 0x0047 }
            throw r4     // Catch:{ all -> 0x0047 }
        L_0x0044:
            monitor-exit(r3)     // Catch:{ all -> 0x0047 }
            monitor-exit(r0)     // Catch:{ all -> 0x00bd }
            return r7
        L_0x0047:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0047 }
        L_0x0049:
            throw r4     // Catch:{ all -> 0x00bd }
        L_0x004a:
            long r10 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x0092 }
            r12.writerThread = r10     // Catch:{ all -> 0x0092 }
            monitor-exit(r9)     // Catch:{ all -> 0x0092 }
        L_0x0051:
            java.io.FileDescriptor r9 = r12.f895fd     // Catch:{ all -> 0x0095 }
            sun.nio.ch.NativeDispatcher r10 = f894nd     // Catch:{ all -> 0x0095 }
            long r9 = sun.nio.p033ch.IOUtil.write(r9, r13, r14, r15, r10)     // Catch:{ all -> 0x0095 }
            r1 = r9
            r9 = -3
            int r9 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r9 != 0) goto L_0x0067
            boolean r9 = r12.isOpen()     // Catch:{ all -> 0x0095 }
            if (r9 == 0) goto L_0x0067
            goto L_0x0051
        L_0x0067:
            long r9 = sun.nio.p033ch.IOStatus.normalize((long) r1)     // Catch:{ all -> 0x0095 }
            r12.writerCleanup()     // Catch:{ all -> 0x00bd }
            int r11 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r11 > 0) goto L_0x0076
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 != 0) goto L_0x0077
        L_0x0076:
            r3 = r6
        L_0x0077:
            r12.end(r3)     // Catch:{ all -> 0x00bd }
            java.lang.Object r3 = r12.stateLock     // Catch:{ all -> 0x00bd }
            monitor-enter(r3)     // Catch:{ all -> 0x00bd }
            int r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r4 > 0) goto L_0x008c
            boolean r4 = r12.isOutputOpen     // Catch:{ all -> 0x008f }
            if (r4 == 0) goto L_0x0086
            goto L_0x008c
        L_0x0086:
            java.nio.channels.AsynchronousCloseException r4 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x008f }
            r4.<init>()     // Catch:{ all -> 0x008f }
            throw r4     // Catch:{ all -> 0x008f }
        L_0x008c:
            monitor-exit(r3)     // Catch:{ all -> 0x008f }
            monitor-exit(r0)     // Catch:{ all -> 0x00bd }
            return r9
        L_0x008f:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x008f }
            goto L_0x0049
        L_0x0092:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0092 }
            throw r10     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r9 = move-exception
            r12.writerCleanup()     // Catch:{ all -> 0x00bd }
            int r10 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r10 > 0) goto L_0x00a1
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 != 0) goto L_0x00a2
        L_0x00a1:
            r3 = r6
        L_0x00a2:
            r12.end(r3)     // Catch:{ all -> 0x00bd }
            java.lang.Object r3 = r12.stateLock     // Catch:{ all -> 0x00bd }
            monitor-enter(r3)     // Catch:{ all -> 0x00bd }
            int r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r4 > 0) goto L_0x00b7
            boolean r4 = r12.isOutputOpen     // Catch:{ all -> 0x00ba }
            if (r4 == 0) goto L_0x00b1
            goto L_0x00b7
        L_0x00b1:
            java.nio.channels.AsynchronousCloseException r4 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x00ba }
            r4.<init>()     // Catch:{ all -> 0x00ba }
            throw r4     // Catch:{ all -> 0x00ba }
        L_0x00b7:
            monitor-exit(r3)     // Catch:{ all -> 0x00ba }
            throw r9     // Catch:{ all -> 0x00bd }
        L_0x00ba:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00ba }
            goto L_0x0049
        L_0x00bd:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00bd }
            throw r1
        L_0x00c0:
            java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.write(java.nio.ByteBuffer[], int, int):long");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
        if (0 > 0) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001c, code lost:
        if (0 != -2) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001f, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0020, code lost:
        end(r3);
        r2 = r8.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0025, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0026, code lost:
        if (0 > 0) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002a, code lost:
        if (r8.isOutputOpen == false) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0032, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0033, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0035, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0036, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r1 = sendOutOfBandData(r8.f895fd, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0048, code lost:
        if (r1 != -3) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x004e, code lost:
        if (isOpen() == false) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0051, code lost:
        r5 = sun.nio.p033ch.IOStatus.normalize(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        writerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0058, code lost:
        if (r1 > 0) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x005a, code lost:
        if (r1 != -2) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x005d, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x005e, code lost:
        end(r3);
        r2 = r8.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0063, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0064, code lost:
        if (r1 > 0) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0068, code lost:
        if (r8.isOutputOpen == false) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0070, code lost:
        throw new java.nio.channels.AsynchronousCloseException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0071, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0073, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0074, code lost:
        r3 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int sendOutOfBandData(byte r9) throws java.p026io.IOException {
        /*
            r8 = this;
            java.lang.Object r0 = r8.writeLock
            monitor-enter(r0)
            r8.ensureWriteOpen()     // Catch:{ all -> 0x009d }
            r1 = 0
            r2 = -2
            r3 = 1
            r4 = 0
            r8.begin()     // Catch:{ all -> 0x007a }
            java.lang.Object r5 = r8.stateLock     // Catch:{ all -> 0x007a }
            monitor-enter(r5)     // Catch:{ all -> 0x007a }
            boolean r6 = r8.isOpen()     // Catch:{ all -> 0x0077 }
            if (r6 != 0) goto L_0x0039
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            r8.writerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0020
            if (r1 != r2) goto L_0x001f
            goto L_0x0020
        L_0x001f:
            r3 = r4
        L_0x0020:
            r8.end(r3)     // Catch:{ all -> 0x009d }
            java.lang.Object r2 = r8.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r2)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0033
            boolean r3 = r8.isOutputOpen     // Catch:{ all -> 0x0036 }
            if (r3 == 0) goto L_0x002d
            goto L_0x0033
        L_0x002d:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0036 }
            r3.<init>()     // Catch:{ all -> 0x0036 }
            throw r3     // Catch:{ all -> 0x0036 }
        L_0x0033:
            monitor-exit(r2)     // Catch:{ all -> 0x0036 }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r4
        L_0x0036:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0036 }
        L_0x0038:
            throw r3     // Catch:{ all -> 0x009d }
        L_0x0039:
            long r6 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x0077 }
            r8.writerThread = r6     // Catch:{ all -> 0x0077 }
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
        L_0x0040:
            java.io.FileDescriptor r5 = r8.f895fd     // Catch:{ all -> 0x007a }
            int r5 = sendOutOfBandData(r5, r9)     // Catch:{ all -> 0x007a }
            r1 = r5
            r5 = -3
            if (r1 != r5) goto L_0x0051
            boolean r5 = r8.isOpen()     // Catch:{ all -> 0x007a }
            if (r5 == 0) goto L_0x0051
            goto L_0x0040
        L_0x0051:
            int r5 = sun.nio.p033ch.IOStatus.normalize((int) r1)     // Catch:{ all -> 0x007a }
            r8.writerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x005e
            if (r1 != r2) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r3 = r4
        L_0x005e:
            r8.end(r3)     // Catch:{ all -> 0x009d }
            java.lang.Object r2 = r8.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r2)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0071
            boolean r3 = r8.isOutputOpen     // Catch:{ all -> 0x0074 }
            if (r3 == 0) goto L_0x006b
            goto L_0x0071
        L_0x006b:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x0074 }
            r3.<init>()     // Catch:{ all -> 0x0074 }
            throw r3     // Catch:{ all -> 0x0074 }
        L_0x0071:
            monitor-exit(r2)     // Catch:{ all -> 0x0074 }
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            return r5
        L_0x0074:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0074 }
            goto L_0x0038
        L_0x0077:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0077 }
            throw r6     // Catch:{ all -> 0x007a }
        L_0x007a:
            r5 = move-exception
            r8.writerCleanup()     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0084
            if (r1 != r2) goto L_0x0083
            goto L_0x0084
        L_0x0083:
            r3 = r4
        L_0x0084:
            r8.end(r3)     // Catch:{ all -> 0x009d }
            java.lang.Object r2 = r8.stateLock     // Catch:{ all -> 0x009d }
            monitor-enter(r2)     // Catch:{ all -> 0x009d }
            if (r1 > 0) goto L_0x0097
            boolean r3 = r8.isOutputOpen     // Catch:{ all -> 0x009a }
            if (r3 == 0) goto L_0x0091
            goto L_0x0097
        L_0x0091:
            java.nio.channels.AsynchronousCloseException r3 = new java.nio.channels.AsynchronousCloseException     // Catch:{ all -> 0x009a }
            r3.<init>()     // Catch:{ all -> 0x009a }
            throw r3     // Catch:{ all -> 0x009a }
        L_0x0097:
            monitor-exit(r2)     // Catch:{ all -> 0x009a }
            throw r5     // Catch:{ all -> 0x009d }
        L_0x009a:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x009a }
            goto L_0x0038
        L_0x009d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.sendOutOfBandData(byte):int");
    }

    /* access modifiers changed from: protected */
    public void implConfigureBlocking(boolean block) throws IOException {
        IOUtil.configureBlocking(this.f895fd, block);
    }

    public InetSocketAddress localAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.localAddress;
        }
        return inetSocketAddress;
    }

    public SocketAddress remoteAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.remoteAddress;
        }
        return inetSocketAddress;
    }

    public SocketChannel bind(SocketAddress local) throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    if (!isOpen()) {
                        throw new ClosedChannelException();
                    } else if (this.state == 1) {
                        throw new ConnectionPendingException();
                    } else if (this.localAddress == null) {
                        InetSocketAddress inetSocketAddress = local == null ? new InetSocketAddress(0) : Net.checkAddress(local);
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityManager.checkListen(inetSocketAddress.getPort());
                        }
                        NetHooks.beforeTcpBind(this.f895fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                        Net.bind(this.f895fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                        this.localAddress = Net.localAddress(this.f895fd);
                    } else {
                        throw new AlreadyBoundException();
                    }
                }
            }
        }
        return this;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.stateLock) {
            z = this.state == 2;
        }
        return z;
    }

    public boolean isConnectionPending() {
        boolean z;
        synchronized (this.stateLock) {
            z = true;
            if (this.state != 1) {
                z = false;
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void ensureOpenAndUnconnected() throws IOException {
        synchronized (this.stateLock) {
            if (isOpen()) {
                int i = this.state;
                if (i == 2) {
                    throw new AlreadyConnectedException();
                } else if (i == 1) {
                    throw new ConnectionPendingException();
                }
            } else {
                throw new ClosedChannelException();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x00e8, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:?, code lost:
        close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x00ec, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003d, code lost:
        if (0 > 0) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003f, code lost:
        if (0 != -2) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0042, code lost:
        r9 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0043, code lost:
        end(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0049, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r10 = r3.getAddress();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x006b, code lost:
        if (r10.isAnyLocalAddress() == false) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x006d, code lost:
        r10 = java.net.InetAddress.getLocalHost();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0072, code lost:
        r6 = sun.nio.p033ch.Net.connect(r14.f895fd, r10, r3.getPort());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x007e, code lost:
        if (r6 != -3) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0084, code lost:
        if (isOpen() == false) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x008b, code lost:
        if (r6 > 0) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x008d, code lost:
        if (r6 != -2) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0090, code lost:
        r7 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0092, code lost:
        r7 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0093, code lost:
        end(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r7 = r14.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x009a, code lost:
        monitor-enter(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r14.remoteAddress = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x009d, code lost:
        if (r6 <= 0) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x009f, code lost:
        r14.state = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00a6, code lost:
        if (isOpen() == false) goto L_0x00b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00a8, code lost:
        r14.localAddress = sun.nio.p033ch.Net.localAddress(r14.f895fd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00b1, code lost:
        monitor-exit(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00b5, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00ba, code lost:
        if (isBlocking() != false) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00bc, code lost:
        r14.state = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00c2, code lost:
        if (isOpen() == false) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00c4, code lost:
        r14.localAddress = sun.nio.p033ch.Net.localAddress(r14.f895fd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00cd, code lost:
        monitor-exit(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x00d1, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(java.net.SocketAddress r15) throws java.p026io.IOException {
        /*
            r14 = this;
            r0 = 0
            java.lang.Object r1 = r14.readLock
            monitor-enter(r1)
            java.lang.Object r2 = r14.writeLock     // Catch:{ all -> 0x00f2 }
            monitor-enter(r2)     // Catch:{ all -> 0x00f2 }
            r14.ensureOpenAndUnconnected()     // Catch:{ all -> 0x00ef }
            java.net.InetSocketAddress r3 = sun.nio.p033ch.Net.checkAddress(r15)     // Catch:{ all -> 0x00ef }
            java.lang.SecurityManager r4 = java.lang.System.getSecurityManager()     // Catch:{ all -> 0x00ef }
            if (r4 == 0) goto L_0x0023
            java.net.InetAddress r5 = r3.getAddress()     // Catch:{ all -> 0x00ef }
            java.lang.String r5 = r5.getHostAddress()     // Catch:{ all -> 0x00ef }
            int r6 = r3.getPort()     // Catch:{ all -> 0x00ef }
            r4.checkConnect(r5, r6)     // Catch:{ all -> 0x00ef }
        L_0x0023:
            java.lang.Object r5 = r14.blockingLock()     // Catch:{ all -> 0x00ef }
            monitor-enter(r5)     // Catch:{ all -> 0x00ef }
            r6 = 0
            r7 = -2
            r8 = 0
            r9 = 1
            r14.begin()     // Catch:{ all -> 0x00d8 }
            java.lang.Object r10 = r14.stateLock     // Catch:{ all -> 0x00d8 }
            monitor-enter(r10)     // Catch:{ all -> 0x00d8 }
            boolean r11 = r14.isOpen()     // Catch:{ all -> 0x00d5 }
            if (r11 != 0) goto L_0x004a
            monitor-exit(r10)     // Catch:{ all -> 0x00d5 }
            r14.readerCleanup()     // Catch:{ IOException -> 0x00e8 }
            if (r6 > 0) goto L_0x0043
            if (r6 != r7) goto L_0x0042
            goto L_0x0043
        L_0x0042:
            r9 = r8
        L_0x0043:
            r14.end(r9)     // Catch:{ IOException -> 0x00e8 }
            monitor-exit(r5)     // Catch:{ all -> 0x00e6 }
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            monitor-exit(r1)     // Catch:{ all -> 0x00f2 }
            return r8
        L_0x004a:
            java.net.InetSocketAddress r11 = r14.localAddress     // Catch:{ all -> 0x00d5 }
            if (r11 != 0) goto L_0x005b
            java.io.FileDescriptor r11 = r14.f895fd     // Catch:{ all -> 0x00d5 }
            java.net.InetAddress r12 = r3.getAddress()     // Catch:{ all -> 0x00d5 }
            int r13 = r3.getPort()     // Catch:{ all -> 0x00d5 }
            sun.net.NetHooks.beforeTcpConnect(r11, r12, r13)     // Catch:{ all -> 0x00d5 }
        L_0x005b:
            long r11 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x00d5 }
            r14.readerThread = r11     // Catch:{ all -> 0x00d5 }
            monitor-exit(r10)     // Catch:{ all -> 0x00d5 }
        L_0x0063:
            java.net.InetAddress r10 = r3.getAddress()     // Catch:{ all -> 0x00d8 }
            boolean r11 = r10.isAnyLocalAddress()     // Catch:{ all -> 0x00d8 }
            if (r11 == 0) goto L_0x0072
            java.net.InetAddress r11 = java.net.InetAddress.getLocalHost()     // Catch:{ all -> 0x00d8 }
            r10 = r11
        L_0x0072:
            java.io.FileDescriptor r11 = r14.f895fd     // Catch:{ all -> 0x00d8 }
            int r12 = r3.getPort()     // Catch:{ all -> 0x00d8 }
            int r11 = sun.nio.p033ch.Net.connect(r11, r10, r12)     // Catch:{ all -> 0x00d8 }
            r6 = r11
            r11 = -3
            if (r6 != r11) goto L_0x0087
            boolean r11 = r14.isOpen()     // Catch:{ all -> 0x00d8 }
            if (r11 == 0) goto L_0x0087
            goto L_0x0063
        L_0x0087:
            r14.readerCleanup()     // Catch:{ IOException -> 0x00e8 }
            if (r6 > 0) goto L_0x0092
            if (r6 != r7) goto L_0x0090
            goto L_0x0092
        L_0x0090:
            r7 = r8
            goto L_0x0093
        L_0x0092:
            r7 = r9
        L_0x0093:
            r14.end(r7)     // Catch:{ IOException -> 0x00e8 }
            java.lang.Object r7 = r14.stateLock     // Catch:{ all -> 0x00e6 }
            monitor-enter(r7)     // Catch:{ all -> 0x00e6 }
            r14.remoteAddress = r3     // Catch:{ all -> 0x00d2 }
            if (r6 <= 0) goto L_0x00b6
            r8 = 2
            r14.state = r8     // Catch:{ all -> 0x00d2 }
            boolean r8 = r14.isOpen()     // Catch:{ all -> 0x00d2 }
            if (r8 == 0) goto L_0x00b0
            java.io.FileDescriptor r8 = r14.f895fd     // Catch:{ all -> 0x00d2 }
            java.net.InetSocketAddress r8 = sun.nio.p033ch.Net.localAddress(r8)     // Catch:{ all -> 0x00d2 }
            r14.localAddress = r8     // Catch:{ all -> 0x00d2 }
        L_0x00b0:
            monitor-exit(r7)     // Catch:{ all -> 0x00d2 }
            monitor-exit(r5)     // Catch:{ all -> 0x00e6 }
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            monitor-exit(r1)     // Catch:{ all -> 0x00f2 }
            return r9
        L_0x00b6:
            boolean r10 = r14.isBlocking()     // Catch:{ all -> 0x00d2 }
            if (r10 != 0) goto L_0x00cc
            r14.state = r9     // Catch:{ all -> 0x00d2 }
            boolean r9 = r14.isOpen()     // Catch:{ all -> 0x00d2 }
            if (r9 == 0) goto L_0x00cc
            java.io.FileDescriptor r9 = r14.f895fd     // Catch:{ all -> 0x00d2 }
            java.net.InetSocketAddress r9 = sun.nio.p033ch.Net.localAddress(r9)     // Catch:{ all -> 0x00d2 }
            r14.localAddress = r9     // Catch:{ all -> 0x00d2 }
        L_0x00cc:
            monitor-exit(r7)     // Catch:{ all -> 0x00d2 }
            monitor-exit(r5)     // Catch:{ all -> 0x00e6 }
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            monitor-exit(r1)     // Catch:{ all -> 0x00f2 }
            return r8
        L_0x00d2:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00d2 }
            throw r8     // Catch:{ all -> 0x00e6 }
        L_0x00d5:
            r11 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x00d5 }
            throw r11     // Catch:{ all -> 0x00d8 }
        L_0x00d8:
            r10 = move-exception
            r14.readerCleanup()     // Catch:{ IOException -> 0x00e8 }
            if (r6 > 0) goto L_0x00e0
            if (r6 != r7) goto L_0x00e1
        L_0x00e0:
            r8 = r9
        L_0x00e1:
            r14.end(r8)     // Catch:{ IOException -> 0x00e8 }
            throw r10     // Catch:{ IOException -> 0x00e8 }
        L_0x00e6:
            r6 = move-exception
            goto L_0x00ed
        L_0x00e8:
            r7 = move-exception
            r14.close()     // Catch:{ all -> 0x00e6 }
            throw r7     // Catch:{ all -> 0x00e6 }
        L_0x00ed:
            monitor-exit(r5)     // Catch:{ all -> 0x00e6 }
            throw r6     // Catch:{ all -> 0x00ef }
        L_0x00ef:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            throw r3     // Catch:{ all -> 0x00f2 }
        L_0x00f2:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x00f2 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.connect(java.net.SocketAddress):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x00c1, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x00c4, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x00ca, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x00cb, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x00d4, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x00d7, code lost:
        monitor-enter(r14.stateLock);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:?, code lost:
        r14.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x00dc, code lost:
        if (r14.state == 3) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x00de, code lost:
        kill();
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x00e8, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:?, code lost:
        end(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x00ed, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x00ee, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x00f2, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:?, code lost:
        close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x00f6, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x001c, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        begin();
        r10 = blockingLock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0029, code lost:
        monitor-enter(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r11 = r14.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x002c, code lost:
        monitor-enter(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0031, code lost:
        if (isOpen() != false) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0033, code lost:
        monitor-exit(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r4 = r14.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0037, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r14.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x003c, code lost:
        if (r14.state != 3) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x003e, code lost:
        kill();
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0042, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0043, code lost:
        if (r2 > 0) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0045, code lost:
        if (r2 != 65534) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0048, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        end(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x004e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x004f, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r14.readerThread = sun.nio.p033ch.NativeThread.current();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0058, code lost:
        monitor-exit(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
        dalvik.system.BlockGuard.getThreadPolicy().onNetwork();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0065, code lost:
        if (isBlocking() != false) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0067, code lost:
        r2 = checkConnect(r14.f895fd, false, r14.readyToConnect);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0070, code lost:
        if (r2 != -3) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0076, code lost:
        if (isOpen() == false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x007a, code lost:
        r2 = checkConnect(r14.f895fd, true, r14.readyToConnect);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0083, code lost:
        if (r2 != 0) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0086, code lost:
        if (r2 != -3) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x008c, code lost:
        if (isOpen() == false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x008f, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
        r10 = r14.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0092, code lost:
        monitor-enter(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:?, code lost:
        r14.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0097, code lost:
        if (r14.state != 3) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0099, code lost:
        kill();
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x009d, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x009e, code lost:
        if (r2 > 0) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x00a0, code lost:
        if (r2 != -2) goto L_0x00a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x00a3, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00a5, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        end(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x00ab, code lost:
        if (r2 <= 0) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:?, code lost:
        r3 = r14.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x00af, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        r14.state = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x00b6, code lost:
        if (isOpen() == false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x00b8, code lost:
        r14.localAddress = sun.nio.p033ch.Net.localAddress(r14.f895fd);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:35:0x0035, B:131:0x00d8] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:56:0x0051=Splitter:B:56:0x0051, B:90:0x00a6=Splitter:B:90:0x00a6, B:46:0x0049=Splitter:B:46:0x0049, B:139:0x00e9=Splitter:B:139:0x00e9} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean finishConnect() throws java.p026io.IOException {
        /*
            r14 = this;
            java.lang.Object r0 = r14.readLock
            monitor-enter(r0)
            java.lang.Object r1 = r14.writeLock     // Catch:{ all -> 0x0109 }
            monitor-enter(r1)     // Catch:{ all -> 0x0109 }
            java.lang.Object r2 = r14.stateLock     // Catch:{ all -> 0x0106 }
            monitor-enter(r2)     // Catch:{ all -> 0x0106 }
            boolean r3 = r14.isOpen()     // Catch:{ all -> 0x0103 }
            if (r3 == 0) goto L_0x00fd
            int r3 = r14.state     // Catch:{ all -> 0x0103 }
            r4 = 2
            r5 = 1
            if (r3 != r4) goto L_0x0019
            monitor-exit(r2)     // Catch:{ all -> 0x0103 }
            monitor-exit(r1)     // Catch:{ all -> 0x0106 }
            monitor-exit(r0)     // Catch:{ all -> 0x0109 }
            return r5
        L_0x0019:
            if (r3 != r5) goto L_0x00f7
            monitor-exit(r2)     // Catch:{ all -> 0x0103 }
            r2 = 0
            r3 = -2
            r6 = 3
            r7 = 0
            r9 = 0
            r14.begin()     // Catch:{ all -> 0x00d4 }
            java.lang.Object r10 = r14.blockingLock()     // Catch:{ all -> 0x00d4 }
            monitor-enter(r10)     // Catch:{ all -> 0x00d4 }
            java.lang.Object r11 = r14.stateLock     // Catch:{ all -> 0x00d1 }
            monitor-enter(r11)     // Catch:{ all -> 0x00d1 }
            boolean r12 = r14.isOpen()     // Catch:{ all -> 0x00ce }
            if (r12 != 0) goto L_0x0052
            monitor-exit(r11)     // Catch:{ all -> 0x00ce }
            monitor-exit(r10)     // Catch:{ all -> 0x00d1 }
            java.lang.Object r4 = r14.stateLock     // Catch:{ IOException -> 0x00f2 }
            monitor-enter(r4)     // Catch:{ IOException -> 0x00f2 }
            r14.readerThread = r7     // Catch:{ all -> 0x004f }
            int r7 = r14.state     // Catch:{ all -> 0x004f }
            if (r7 != r6) goto L_0x0042
            r14.kill()     // Catch:{ all -> 0x004f }
            r2 = 0
        L_0x0042:
            monitor-exit(r4)     // Catch:{ all -> 0x004f }
            if (r2 > 0) goto L_0x0049
            if (r2 != r3) goto L_0x0048
            goto L_0x0049
        L_0x0048:
            r5 = r9
        L_0x0049:
            r14.end(r5)     // Catch:{ IOException -> 0x00f2 }
            monitor-exit(r1)     // Catch:{ all -> 0x0106 }
            monitor-exit(r0)     // Catch:{ all -> 0x0109 }
            return r9
        L_0x004f:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x004f }
        L_0x0051:
            throw r3     // Catch:{ IOException -> 0x00f2 }
        L_0x0052:
            long r12 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x00ce }
            r14.readerThread = r12     // Catch:{ all -> 0x00ce }
            monitor-exit(r11)     // Catch:{ all -> 0x00ce }
            dalvik.system.BlockGuard$Policy r11 = dalvik.system.BlockGuard.getThreadPolicy()     // Catch:{ all -> 0x00d1 }
            r11.onNetwork()     // Catch:{ all -> 0x00d1 }
            boolean r11 = r14.isBlocking()     // Catch:{ all -> 0x00d1 }
            r12 = -3
            if (r11 != 0) goto L_0x007a
        L_0x0067:
            java.io.FileDescriptor r11 = r14.f895fd     // Catch:{ all -> 0x00d1 }
            boolean r13 = r14.readyToConnect     // Catch:{ all -> 0x00d1 }
            int r11 = checkConnect(r11, r9, r13)     // Catch:{ all -> 0x00d1 }
            r2 = r11
            if (r2 != r12) goto L_0x0079
            boolean r11 = r14.isOpen()     // Catch:{ all -> 0x00d1 }
            if (r11 == 0) goto L_0x0079
            goto L_0x0067
        L_0x0079:
            goto L_0x008f
        L_0x007a:
            java.io.FileDescriptor r11 = r14.f895fd     // Catch:{ all -> 0x00d1 }
            boolean r13 = r14.readyToConnect     // Catch:{ all -> 0x00d1 }
            int r11 = checkConnect(r11, r5, r13)     // Catch:{ all -> 0x00d1 }
            r2 = r11
            if (r2 != 0) goto L_0x0086
            goto L_0x007a
        L_0x0086:
            if (r2 != r12) goto L_0x0079
            boolean r11 = r14.isOpen()     // Catch:{ all -> 0x00d1 }
            if (r11 == 0) goto L_0x0079
            goto L_0x007a
        L_0x008f:
            monitor-exit(r10)     // Catch:{ all -> 0x00d1 }
            java.lang.Object r10 = r14.stateLock     // Catch:{ IOException -> 0x00f2 }
            monitor-enter(r10)     // Catch:{ IOException -> 0x00f2 }
            r14.readerThread = r7     // Catch:{ all -> 0x00cb }
            int r7 = r14.state     // Catch:{ all -> 0x00cb }
            if (r7 != r6) goto L_0x009d
            r14.kill()     // Catch:{ all -> 0x00cb }
            r2 = 0
        L_0x009d:
            monitor-exit(r10)     // Catch:{ all -> 0x00cb }
            if (r2 > 0) goto L_0x00a5
            if (r2 != r3) goto L_0x00a3
            goto L_0x00a5
        L_0x00a3:
            r3 = r9
            goto L_0x00a6
        L_0x00a5:
            r3 = r5
        L_0x00a6:
            r14.end(r3)     // Catch:{ IOException -> 0x00f2 }
            if (r2 <= 0) goto L_0x00c8
            java.lang.Object r3 = r14.stateLock     // Catch:{ all -> 0x0106 }
            monitor-enter(r3)     // Catch:{ all -> 0x0106 }
            r14.state = r4     // Catch:{ all -> 0x00c5 }
            boolean r4 = r14.isOpen()     // Catch:{ all -> 0x00c5 }
            if (r4 == 0) goto L_0x00c0
            java.io.FileDescriptor r4 = r14.f895fd     // Catch:{ all -> 0x00c5 }
            java.net.InetSocketAddress r4 = sun.nio.p033ch.Net.localAddress(r4)     // Catch:{ all -> 0x00c5 }
            r14.localAddress = r4     // Catch:{ all -> 0x00c5 }
        L_0x00c0:
            monitor-exit(r3)     // Catch:{ all -> 0x00c5 }
            monitor-exit(r1)     // Catch:{ all -> 0x0106 }
            monitor-exit(r0)     // Catch:{ all -> 0x0109 }
            return r5
        L_0x00c5:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00c5 }
            throw r4     // Catch:{ all -> 0x0106 }
        L_0x00c8:
            monitor-exit(r1)     // Catch:{ all -> 0x0106 }
            monitor-exit(r0)     // Catch:{ all -> 0x0109 }
            return r9
        L_0x00cb:
            r3 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x00cb }
            goto L_0x0051
        L_0x00ce:
            r4 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x00ce }
            throw r4     // Catch:{ all -> 0x00d1 }
        L_0x00d1:
            r4 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x00d1 }
            throw r4     // Catch:{ all -> 0x00d4 }
        L_0x00d4:
            r4 = move-exception
            java.lang.Object r10 = r14.stateLock     // Catch:{ IOException -> 0x00f2 }
            monitor-enter(r10)     // Catch:{ IOException -> 0x00f2 }
            r14.readerThread = r7     // Catch:{ all -> 0x00ee }
            int r7 = r14.state     // Catch:{ all -> 0x00ee }
            if (r7 != r6) goto L_0x00e2
            r14.kill()     // Catch:{ all -> 0x00ee }
            r2 = 0
        L_0x00e2:
            monitor-exit(r10)     // Catch:{ all -> 0x00ee }
            if (r2 > 0) goto L_0x00e9
            if (r2 != r3) goto L_0x00e8
            goto L_0x00e9
        L_0x00e8:
            r5 = r9
        L_0x00e9:
            r14.end(r5)     // Catch:{ IOException -> 0x00f2 }
            throw r4     // Catch:{ IOException -> 0x00f2 }
        L_0x00ee:
            r3 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x00ee }
            goto L_0x0051
        L_0x00f2:
            r3 = move-exception
            r14.close()     // Catch:{ all -> 0x0106 }
            throw r3     // Catch:{ all -> 0x0106 }
        L_0x00f7:
            java.nio.channels.NoConnectionPendingException r3 = new java.nio.channels.NoConnectionPendingException     // Catch:{ all -> 0x0103 }
            r3.<init>()     // Catch:{ all -> 0x0103 }
            throw r3     // Catch:{ all -> 0x0103 }
        L_0x00fd:
            java.nio.channels.ClosedChannelException r3 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x0103 }
            r3.<init>()     // Catch:{ all -> 0x0103 }
            throw r3     // Catch:{ all -> 0x0103 }
        L_0x0103:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0103 }
            throw r3     // Catch:{ all -> 0x0106 }
        L_0x0106:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0106 }
            throw r2     // Catch:{ all -> 0x0109 }
        L_0x0109:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0109 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.finishConnect():boolean");
    }

    public SocketChannel shutdownInput() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (!isConnected()) {
                throw new NotYetConnectedException();
            } else if (this.isInputOpen) {
                Net.shutdown(this.f895fd, 0);
                if (this.readerThread != 0) {
                    NativeThread.signal(this.readerThread);
                }
                this.isInputOpen = false;
            }
        }
        return this;
    }

    public SocketChannel shutdownOutput() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (!isConnected()) {
                throw new NotYetConnectedException();
            } else if (this.isOutputOpen) {
                Net.shutdown(this.f895fd, 1);
                if (this.writerThread != 0) {
                    NativeThread.signal(this.writerThread);
                }
                this.isOutputOpen = false;
            }
        }
        return this;
    }

    public boolean isInputOpen() {
        boolean z;
        synchronized (this.stateLock) {
            z = this.isInputOpen;
        }
        return z;
    }

    public boolean isOutputOpen() {
        boolean z;
        synchronized (this.stateLock) {
            z = this.isOutputOpen;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            this.isInputOpen = false;
            this.isOutputOpen = false;
            if (this.state != 4) {
                this.guard.close();
                f894nd.preClose(this.f895fd);
            }
            if (this.readerThread != 0) {
                NativeThread.signal(this.readerThread);
            }
            if (this.writerThread != 0) {
                NativeThread.signal(this.writerThread);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void kill() throws java.p026io.IOException {
        /*
            r7 = this;
            java.lang.Object r0 = r7.stateLock
            monitor-enter(r0)
            int r1 = r7.state     // Catch:{ all -> 0x0030 }
            r2 = 4
            if (r1 != r2) goto L_0x000a
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            return
        L_0x000a:
            r3 = -1
            if (r1 != r3) goto L_0x0011
            r7.state = r2     // Catch:{ all -> 0x0030 }
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            return
        L_0x0011:
            long r3 = r7.readerThread     // Catch:{ all -> 0x0030 }
            r5 = 0
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 != 0) goto L_0x002a
            long r3 = r7.writerThread     // Catch:{ all -> 0x0030 }
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 != 0) goto L_0x002a
            sun.nio.ch.NativeDispatcher r1 = f894nd     // Catch:{ all -> 0x0030 }
            java.io.FileDescriptor r3 = r7.f895fd     // Catch:{ all -> 0x0030 }
            r1.close(r3)     // Catch:{ all -> 0x0030 }
            r7.state = r2     // Catch:{ all -> 0x0030 }
            goto L_0x002d
        L_0x002a:
            r1 = 3
            r7.state = r1     // Catch:{ all -> 0x0030 }
        L_0x002d:
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            return
        L_0x0030:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.kill():void");
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        if (this.f895fd != null) {
            close();
        }
    }

    public boolean translateReadyOps(int ops, int initialOps, SelectionKeyImpl sk) {
        int i;
        int nioInterestOps = sk.nioInterestOps();
        int nioReadyOps = sk.nioReadyOps();
        int i2 = initialOps;
        if ((Net.POLLNVAL & ops) != 0) {
            return false;
        }
        if (((Net.POLLERR | Net.POLLHUP) & ops) != 0) {
            int i3 = nioInterestOps;
            sk.nioReadyOps(i3);
            this.readyToConnect = true;
            if (((~nioReadyOps) & i3) != 0) {
                return true;
            }
            return false;
        }
        if (!((Net.POLLIN & ops) == 0 || (nioInterestOps & 1) == 0 || this.state != 2)) {
            i2 |= 1;
        }
        if (!((Net.POLLCONN & ops) == 0 || (nioInterestOps & 8) == 0 || ((i = this.state) != 0 && i != 1))) {
            i2 |= 8;
            this.readyToConnect = true;
        }
        if (!((Net.POLLOUT & ops) == 0 || (nioInterestOps & 4) == 0 || this.state != 2)) {
            i2 |= 4;
        }
        sk.nioReadyOps(i2);
        if (((~nioReadyOps) & i2) != 0) {
            return true;
        }
        return false;
    }

    public boolean translateAndUpdateReadyOps(int ops, SelectionKeyImpl sk) {
        return translateReadyOps(ops, sk.nioReadyOps(), sk);
    }

    public boolean translateAndSetReadyOps(int ops, SelectionKeyImpl sk) {
        return translateReadyOps(ops, 0, sk);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        if (0 <= 0) goto L_0x001a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
        end(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001f, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002d, code lost:
        r1 = sun.nio.p033ch.Net.poll(r7.f895fd, r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        readerCleanup();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0031, code lost:
        if (r1 <= 0) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0034, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0035, code lost:
        end(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003a, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int poll(int r8, long r9) throws java.p026io.IOException {
        /*
            r7 = this;
            java.lang.Object r0 = r7.readLock
            monitor-enter(r0)
            r1 = 0
            r2 = 1
            r3 = 0
            r7.begin()     // Catch:{ all -> 0x003e }
            java.lang.Object r4 = r7.stateLock     // Catch:{ all -> 0x003e }
            monitor-enter(r4)     // Catch:{ all -> 0x003e }
            boolean r5 = r7.isOpen()     // Catch:{ all -> 0x003b }
            if (r5 != 0) goto L_0x0020
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            r7.readerCleanup()     // Catch:{ all -> 0x004a }
            if (r1 <= 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r2 = r3
        L_0x001b:
            r7.end(r2)     // Catch:{ all -> 0x004a }
            monitor-exit(r0)     // Catch:{ all -> 0x004a }
            return r3
        L_0x0020:
            long r5 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x003b }
            r7.readerThread = r5     // Catch:{ all -> 0x003b }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            java.io.FileDescriptor r4 = r7.f895fd     // Catch:{ all -> 0x003e }
            int r4 = sun.nio.p033ch.Net.poll(r4, r8, r9)     // Catch:{ all -> 0x003e }
            r1 = r4
            r7.readerCleanup()     // Catch:{ all -> 0x004a }
            if (r1 <= 0) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r2 = r3
        L_0x0035:
            r7.end(r2)     // Catch:{ all -> 0x004a }
            monitor-exit(r0)     // Catch:{ all -> 0x004a }
            return r1
        L_0x003b:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r5     // Catch:{ all -> 0x003e }
        L_0x003e:
            r4 = move-exception
            r7.readerCleanup()     // Catch:{ all -> 0x004a }
            if (r1 <= 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r2 = r3
        L_0x0046:
            r7.end(r2)     // Catch:{ all -> 0x004a }
            throw r4     // Catch:{ all -> 0x004a }
        L_0x004a:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketChannelImpl.poll(int, long):int");
    }

    public void translateAndSetInterestOps(int ops, SelectionKeyImpl sk) {
        short s = 0;
        if ((ops & 1) != 0) {
            s = 0 | Net.POLLIN;
        }
        if ((ops & 4) != 0) {
            s |= Net.POLLOUT;
        }
        if ((ops & 8) != 0) {
            s |= Net.POLLCONN;
        }
        sk.selector.putEventOps(sk, s);
    }

    public FileDescriptor getFD() {
        return this.f895fd;
    }

    public int getFDVal() {
        return this.fdVal;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getSuperclass().getName());
        stringBuffer.append('[');
        if (!isOpen()) {
            stringBuffer.append("closed");
        } else {
            synchronized (this.stateLock) {
                int i = this.state;
                if (i == 0) {
                    stringBuffer.append("unconnected");
                } else if (i == 1) {
                    stringBuffer.append("connection-pending");
                } else if (i == 2) {
                    stringBuffer.append(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
                    if (!this.isInputOpen) {
                        stringBuffer.append(" ishut");
                    }
                    if (!this.isOutputOpen) {
                        stringBuffer.append(" oshut");
                    }
                }
                InetSocketAddress localAddress2 = localAddress();
                if (localAddress2 != null) {
                    stringBuffer.append(" local=");
                    stringBuffer.append(Net.getRevealedLocalAddressAsString(localAddress2));
                }
                if (remoteAddress() != null) {
                    stringBuffer.append(" remote=");
                    stringBuffer.append(remoteAddress().toString());
                }
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }
}
